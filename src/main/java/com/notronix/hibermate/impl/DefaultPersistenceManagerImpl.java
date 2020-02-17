package com.notronix.hibermate.impl;

import com.notronix.hibermate.api.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.model.relational.Database;
import org.hibernate.boot.registry.BootstrapServiceRegistryBuilder;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.jpa.TypedParameterValue;
import org.hibernate.query.NativeQuery;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import org.hibernate.type.BasicTypeRegistry;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.Map;

import static com.notronix.albacore.ContainerUtils.*;
import static com.notronix.albacore.NumberUtils.doubleValueOf;
import static com.notronix.albacore.NumberUtils.longValueOf;
import static java.util.Objects.requireNonNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class DefaultPersistenceManagerImpl implements PersistenceManager
{
    private Database database;
    private final BasicTypeRegistry typeRegistry;
    private final BootstrapServiceRegistryBuilder bootstrapRegistryBuilder;
    private SessionFactory sessionFactory;
    private final Object lock = new Object();

    public static PersistenceManager createDefaultManager() {
        DefaultPersistenceManagerImpl manager =
                new DefaultPersistenceManagerImpl(new BasicTypeRegistry(), new BootstrapServiceRegistryBuilder());
        manager.getBootstrapRegistryBuilder().applyIntegrator(new DatabaseIntegrator(manager));

        return manager;
    }

    public DefaultPersistenceManagerImpl(BasicTypeRegistry typeRegistry, BootstrapServiceRegistryBuilder bootstrapRegistryBuilder) {
        this.typeRegistry = requireNonNull(typeRegistry);
        this.bootstrapRegistryBuilder = requireNonNull(bootstrapRegistryBuilder);
    }

    protected BootstrapServiceRegistryBuilder getBootstrapRegistryBuilder() {
        return bootstrapRegistryBuilder;
    }

    @Override
    public Database getDatabase() {
        return database;
    }

    protected void setDatabase(Database database) {
        this.database = database;
    }

    @Override
    public <K extends Serializable> K makePersistent(PersistenceCapable<K> object) throws PersistenceException {
        Session session = openSession();
        Transaction transaction = null;
        K systemId;

        try {
            transaction = session.beginTransaction();
            //noinspection unchecked
            systemId = (K) session.save(object);
            transaction.commit();
        }
        catch (Exception e) {
            if (transaction != null && transaction.getStatus() == TransactionStatus.ACTIVE) {
                transaction.rollback();
            }

            throw new PersistenceException("An error occurred trying to persist object.", e);
        }
        finally {
            session.close();
        }

        return systemId;
    }

    @Override
    public void deletePersistent(PersistenceCapable<?> object) throws PersistenceException {
        Transaction transaction = null;

        try (Session session = openSession()) {
            @SuppressWarnings("unchecked")
            Class<PersistenceCapable<?>> clazz = (Class<PersistenceCapable<?>>) object.getClass();
            transaction = session.beginTransaction();
            PersistenceCapable<?> alias = session.get(clazz, object.getSystemId());
            session.delete(alias);
            transaction.commit();
        }
        catch (Exception e) {
            if (transaction != null && transaction.getStatus() == TransactionStatus.ACTIVE) {
                transaction.rollback();
            }

            throw new PersistenceException("An error occurred trying to delete object.", e);
        }
    }

    @Override
    public void update(String recordId, String objectType, PersistenceCapable<?> object)
            throws PersistenceException {
        try {
            if (object instanceof Syncable) {
                ((Syncable) object).setLastSynchronizedDate(Instant.now());
            }

            if (PersistenceManager.itIsAValid(object)) {
                update(object);
            }
            else {
                makePersistent(object);
            }
        }
        catch (Exception ex) {
            throw new PersistenceException(recordId + ": Failed storing " + objectType + ".", ex);
        }
    }

    @Override
    public <T extends PersistenceCapable<?>> T update(T object) throws PersistenceException {
        Session session = openSession();
        Transaction transaction = null;
        T alias;

        try {
            transaction = session.beginTransaction();

            //noinspection unchecked
            alias = (T) session.merge(object);
            transaction.commit();
        }
        catch (Exception e) {
            if (transaction != null && transaction.getStatus() == TransactionStatus.ACTIVE) {
                transaction.rollback();
            }

            throw new PersistenceException("An error occurred trying to update object.", e);
        }
        finally {
            session.close();
        }

        return alias;
    }

    @Override
    public <K extends Serializable, T extends PersistenceCapable<K>> T get(Class<T> objectClass, K systemId)
            throws PersistenceException {
        if (systemId == null) {
            throw new PersistenceException("systemId is null", null);
        }

        Session session = openSession();
        Transaction transaction = null;
        T alias;

        try {
            transaction = session.beginTransaction();
            alias = session.get(objectClass, systemId);
            transaction.commit();
        }
        catch (Exception e) {
            if (transaction != null && transaction.getStatus() == TransactionStatus.ACTIVE) {
                transaction.rollback();
            }

            throw new PersistenceException("An error occurred trying to get object with system id.", e);
        }
        finally {
            session.close();
        }

        return alias;
    }

    @Override
    public <T extends PersistenceCapable<?>> T getFirst(Class<T> objectClass) throws PersistenceException {
        return getFirst(objectClass, null, null);
    }

    @Override
    public <T extends PersistenceCapable<?>> T getFirst(Class<T> objectClass, String predicate)
            throws PersistenceException {
        return getFirst(objectClass, predicate, null);
    }

    @Override
    public <T extends PersistenceCapable<?>> T getFirst(Class<T> objectClass, String
            predicate, Map<String, Object> params)
            throws PersistenceException {
        return theFirst(getList(objectClass, predicate, params));
    }

    @Override
    public <T extends PersistenceCapable<?>> List<T> getList(Class<T> objectClass, String join, String
            predicate, Map<String, Object> params)
            throws PersistenceException {
        String query = "from " + objectClass.getSimpleName();

        if (isNotBlank(join)) {
            query += " " + join;
        }

        if (isNotBlank(predicate)) {
            query += " where " + predicate;
        }

        return getList(query, params, objectClass);
    }

    @Override
    public <T extends PersistenceCapable<?>> List<T> getList(Class<T> objectClass, String join, String predicate)
            throws PersistenceException {
        return getList(objectClass, join, predicate, null);
    }

    @Override
    public <T extends PersistenceCapable<?>> List<T> getList(Class<T> objectClass, String
            predicate, Map<String, Object> params)
            throws PersistenceException {
        return getList(objectClass, null, predicate, params);
    }

    @Override
    public <T extends PersistenceCapable<?>> List<T> getList(Class<T> objectClass, String predicate)
            throws PersistenceException {
        return getList(objectClass, null, predicate, null);
    }

    @Override
    public Long getLong(DBQuery query) throws PersistenceException {
        List<Object> results = getResults(query);

        if (thereAreNo(results)) {
            return null;
        }

        Object result = theFirst(results);

        if (result == null) {
            return null;
        }

        try {
            return longValueOf(result);
        }
        catch (Exception ex) {
            throw new PersistenceException("Query did not return a number value.", ex);
        }
    }

    public long getCount(Class<? extends PersistenceCapable<?>> objectClass, String predicate)
            throws PersistenceException {
        String query = "select count(*) from " + objectClass.getSimpleName();

        if (isNotBlank(predicate)) {
            query += " " + predicate;
        }

        try (Session session = openSession()) {
            return (Long) session.createQuery(query).iterate().next();
        }
        catch (Exception ex) {
            throw new PersistenceException("Query did not return an Integer value.", ex);
        }
    }

    @Override
    public long getCount(DBQuery query) throws PersistenceException {
        List<Object> results = getResults(query);

        if (thereAreNo(results)) {
            return 0;
        }

        Object result = theFirst(results);

        if (result == null) {
            return 0;
        }

        try {
            return longValueOf(result);
        }
        catch (Exception e) {
            throw new PersistenceException("Query did not return a number value.", e);
        }
    }

    @Override
    public double getSum(DBQuery query) throws PersistenceException {
        List<Object> results = getResults(query);

        if (thereAreNo(results)) {
            return 0.0;
        }

        Object result = theFirst(results);

        if (result == null) {
            return 0.0;
        }

        try {
            return doubleValueOf(result);
        }
        catch (Exception e) {
            throw new PersistenceException("Query did not return a number value.", e);
        }
    }

    @Override
    public <T extends PersistenceCapable<?>> List<T> getList(DBQuery dbQuery, Class<T> resultType)
            throws PersistenceException {
        Session session = openSession();
        Transaction transaction = null;
        List<T> results;

        try {
            transaction = session.beginTransaction();
            NativeQuery<T> query = dbQuery.getQuery(session, resultType);
            results = query.getResultList();
            transaction.commit();
        }
        catch (Exception e) {
            if (transaction != null && transaction.getStatus() == TransactionStatus.ACTIVE) {
                transaction.rollback();
            }

            throw new PersistenceException("An error occurred trying to execute a query.", e);
        }
        finally {
            session.close();
        }

        return results;
    }

    @Override
    public int executeManipulationQuery(DBQuery dbQuery) throws PersistenceException {
        Session session = openSession();
        Transaction transaction = null;
        int affectedItems;

        try {
            transaction = session.beginTransaction();
            NativeQuery<?> query = dbQuery.getQuery(session, null);
            affectedItems = query.executeUpdate();
            transaction.commit();
        }
        catch (Exception e) {
            if (transaction != null && transaction.getStatus() == TransactionStatus.ACTIVE) {
                transaction.rollback();
            }

            throw new PersistenceException("An error occurred trying to execute a manipulation query.", e);
        }
        finally {
            session.close();
        }

        return affectedItems;
    }

    @Override
    public List<Object[]> getList(DBQuery dbQuery) throws PersistenceException {
        Session session = openSession();
        Transaction transaction = null;
        List<?> results;

        try {
            transaction = session.beginTransaction();
            results = dbQuery.getQuery(session, null).list();
            transaction.commit();
        }
        catch (Exception e) {
            if (transaction != null && transaction.getStatus() == TransactionStatus.ACTIVE) {
                transaction.rollback();
            }

            throw new PersistenceException("An error occurred trying to execute a query.", e);
        }
        finally {
            session.close();
        }

        //noinspection unchecked
        return (List<Object[]>) results;
    }

    private <T> List<T> getList(String query, Map<String, Object> params, Class<T> resultType)
            throws PersistenceException {
        Session session = openSession();
        Transaction transaction = null;
        List<T> results;

        try {
            transaction = session.beginTransaction();
            org.hibernate.query.Query<T> hibernateQuery = session.createQuery(query, resultType);

            if (thereAreOneOrMore(params)) {
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    Object value = entry.getValue();
                    String registryKey = value.getClass().getName();
                    Type type = typeRegistry.getRegisteredType(registryKey);

                    if (type != null) {
                        value = new TypedParameterValue(type, value);
                    }

                    hibernateQuery.setParameter(entry.getKey(), value);
                }
            }

            results = hibernateQuery.list();
            transaction.commit();
        }
        catch (Exception e) {
            if (transaction != null && transaction.getStatus() == TransactionStatus.ACTIVE) {
                transaction.rollback();
            }

            throw new PersistenceException("An error occurred executing a query.", e);
        }
        finally {
            session.close();
        }

        return results;
    }

    private List<Object> getResults(DBQuery query) throws PersistenceException {
        Session session = openSession();
        Transaction transaction = null;
        List<Object> results;

        try {
            transaction = session.beginTransaction();
            NativeQuery<Object> sqlQuery = query.getQuery(session, null);
            sqlQuery.setMaxResults(1);
            results = sqlQuery.list();
            transaction.commit();
        }
        catch (Exception e) {
            if (transaction != null && transaction.getStatus() == TransactionStatus.ACTIVE) {
                transaction.rollback();
            }

            throw new PersistenceException("An error occurred trying to get the count.", e);
        }
        finally {
            session.close();
        }

        return results;
    }

    public Session openSession() throws PersistenceException {
        try {
            return getOrCreateSessionFactory().openSession();
        }
        catch (Exception ex) {
            throw new PersistenceException("An error occurred trying to open a session.", ex);
        }
    }

    private SessionFactory getOrCreateSessionFactory() {
        if (sessionFactory != null) {
            return sessionFactory;
        }

        synchronized (lock) {
            if (sessionFactory != null) {
                return sessionFactory;
            }

            StandardServiceRegistry registry =
                    new StandardServiceRegistryBuilder(bootstrapRegistryBuilder.build()).configure().build();
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        }

        return sessionFactory;
    }
}
