package com.notronix.hibermate;

import org.hibernate.Cache;
import org.hibernate.*;
import org.hibernate.boot.spi.SessionFactoryOptions;
import org.hibernate.engine.spi.FilterDefinition;
import org.hibernate.jdbc.ReturningWork;
import org.hibernate.jdbc.Work;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.metadata.CollectionMetadata;
import org.hibernate.procedure.ProcedureCall;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import org.hibernate.stat.SessionStatistics;
import org.hibernate.stat.Statistics;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.naming.NamingException;
import javax.naming.Reference;
import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.transaction.Synchronization;
import java.io.Serializable;
import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

@RunWith(JUnit4.class)
public class DefaultPersistenceManagerImplTest
{
    private static DefaultHibernateUtilImpl failedOpenSessionUtil;
    private static DefaultHibernateUtilImpl sessionFailureUtil;
    private static PersistenceCapable object;
    private static PersistenceManager manager;

    @BeforeClass
    public static void preSuite() {
        object = new PersistenceCapable()
        {
            @Override
            public Long getSystemId() {
                return null;
            }

            @Override
            public void setSystemId(Long systemId) {
            }
        };

        failedOpenSessionUtil = new DefaultHibernateUtilImpl()
        {
            @Override
            public SessionFactory getSessionFactory() {
                throw new HibernateException("simulate failure of opening session.");
            }
        };

        sessionFailureUtil = new DefaultHibernateUtilImpl()
        {
            @Override
            public SessionFactory getSessionFactory() {
                return new SessionFactory()
                {
                    @Override
                    public SessionFactoryOptions getSessionFactoryOptions() {
                        return null;
                    }

                    @Override
                    public <T> List<EntityGraph<? super T>> findEntityGraphsByType(Class<T> entityClass) {
                        return null;
                    }

                    @Override
                    public Metamodel getMetamodel() {
                        return null;
                    }

                    @Override
                    public EntityManager createEntityManager() {
                        return null;
                    }

                    @Override
                    public EntityManager createEntityManager(Map map) {
                        return null;
                    }

                    @Override
                    public EntityManager createEntityManager(SynchronizationType synchronizationType) {
                        return null;
                    }

                    @Override
                    public EntityManager createEntityManager(SynchronizationType synchronizationType, Map map) {
                        return null;
                    }

                    @Override
                    public CriteriaBuilder getCriteriaBuilder() {
                        return null;
                    }

                    @Override
                    public boolean isOpen() {
                        return false;
                    }

                    @Override
                    public Map<String, Object> getProperties() {
                        return null;
                    }

                    @Override
                    public PersistenceUnitUtil getPersistenceUnitUtil() {
                        return null;
                    }

                    @Override
                    public void addNamedQuery(String name, javax.persistence.Query query) {

                    }

                    @Override
                    public <T> T unwrap(Class<T> cls) {
                        return null;
                    }

                    @Override
                    public <T> void addNamedEntityGraph(String graphName, EntityGraph<T> entityGraph) {

                    }

                    @Override
                    public SessionBuilder withOptions() {
                        return null;
                    }

                    @Override
                    public Session openSession() throws HibernateException {
                        return new Session()
                        {
                            @Override
                            public NativeQuery createNativeQuery(String sqlString, Class resultClass) {
                                return null;
                            }

                            @Override
                            public FlushModeType getFlushMode() {
                                return null;
                            }

                            @Override
                            public void setHibernateFlushMode(FlushMode flushMode) {

                            }

                            @Override
                            public FlushMode getHibernateFlushMode() {
                                return null;
                            }

                            @Override
                            public boolean contains(String entityName, Object object) {
                                return false;
                            }

                            @Override
                            public Query createFilter(Object collection, String queryString) {
                                return null;
                            }

                            @Override
                            public <T> MultiIdentifierLoadAccess<T> byMultipleIds(Class<T> entityClass) {
                                return null;
                            }

                            @Override
                            public MultiIdentifierLoadAccess byMultipleIds(String entityName) {
                                return null;
                            }

                            @Override
                            public Query createQuery(String queryString) {
                                return null;
                            }

                            @Override
                            public <T> Query<T> createQuery(String queryString, Class<T> resultType) {
                                return null;
                            }

                            @Override
                            public <T> Query<T> createQuery(CriteriaQuery<T> criteriaQuery) {
                                return null;
                            }

                            @Override
                            public Query createQuery(CriteriaUpdate updateQuery) {
                                return null;
                            }

                            @Override
                            public Query createQuery(CriteriaDelete deleteQuery) {
                                return null;
                            }

                            @Override
                            public <T> Query<T> createNamedQuery(String name, Class<T> resultType) {
                                return null;
                            }

                            @Override
                            public Session getSession() {
                                return null;
                            }

                            @Override
                            public void remove(Object entity) {

                            }

                            @Override
                            public <T> T find(Class<T> entityClass, Object primaryKey) {
                                return null;
                            }

                            @Override
                            public <T> T find(Class<T> entityClass, Object primaryKey, Map<String, Object> properties) {
                                return null;
                            }

                            @Override
                            public <T> T find(Class<T> entityClass, Object primaryKey, LockModeType lockMode) {
                                return null;
                            }

                            @Override
                            public <T> T find(Class<T> entityClass, Object primaryKey, LockModeType lockMode, Map<String, Object> properties) {
                                return null;
                            }

                            @Override
                            public <T> T getReference(Class<T> entityClass, Object primaryKey) {
                                return null;
                            }

                            @Override
                            public void setFlushMode(FlushModeType flushMode) {

                            }

                            @Override
                            public void lock(Object entity, LockModeType lockMode) {

                            }

                            @Override
                            public void lock(Object entity, LockModeType lockMode, Map<String, Object> properties) {

                            }

                            @Override
                            public void refresh(Object entity, Map<String, Object> properties) {

                            }

                            @Override
                            public void refresh(Object entity, LockModeType lockMode) {

                            }

                            @Override
                            public void refresh(Object entity, LockModeType lockMode, Map<String, Object> properties) {

                            }

                            @Override
                            public void detach(Object entity) {

                            }

                            @Override
                            public LockModeType getLockMode(Object entity) {
                                return null;
                            }

                            @Override
                            public void setProperty(String propertyName, Object value) {

                            }

                            @Override
                            public Map<String, Object> getProperties() {
                                return null;
                            }

                            @Override
                            public StoredProcedureQuery createNamedStoredProcedureQuery(String name) {
                                return null;
                            }

                            @Override
                            public StoredProcedureQuery createStoredProcedureQuery(String procedureName) {
                                return null;
                            }

                            @Override
                            public StoredProcedureQuery createStoredProcedureQuery(String procedureName, Class... resultClasses) {
                                return null;
                            }

                            @Override
                            public StoredProcedureQuery createStoredProcedureQuery(String procedureName, String... resultSetMappings) {
                                return null;
                            }

                            @Override
                            public void joinTransaction() {

                            }

                            @Override
                            public boolean isJoinedToTransaction() {
                                return false;
                            }

                            @Override
                            public <T> T unwrap(Class<T> cls) {
                                return null;
                            }

                            @Override
                            public Object getDelegate() {
                                return null;
                            }

                            @Override
                            public EntityManagerFactory getEntityManagerFactory() {
                                return null;
                            }

                            @Override
                            public CriteriaBuilder getCriteriaBuilder() {
                                return null;
                            }

                            @Override
                            public javax.persistence.metamodel.Metamodel getMetamodel() {
                                return null;
                            }

                            @Override
                            public <T> EntityGraph<T> createEntityGraph(Class<T> rootType) {
                                return null;
                            }

                            @Override
                            public EntityGraph<?> createEntityGraph(String graphName) {
                                return null;
                            }

                            @Override
                            public EntityGraph<?> getEntityGraph(String graphName) {
                                return null;
                            }

                            @Override
                            public <T> List<EntityGraph<? super T>> getEntityGraphs(Class<T> entityClass) {
                                return null;
                            }

                            @Override
                            public void close() {

                            }

                            @Override
                            public ProcedureCall getNamedProcedureCall(String name) {
                                return null;
                            }

                            @Override
                            public ProcedureCall createStoredProcedureCall(String procedureName) {
                                return null;
                            }

                            @Override
                            public ProcedureCall createStoredProcedureCall(String procedureName, Class... resultClasses) {
                                return null;
                            }

                            @Override
                            public ProcedureCall createStoredProcedureCall(String procedureName, String... resultSetMappings) {
                                return null;
                            }

                            @Override
                            public Integer getJdbcBatchSize() {
                                return null;
                            }

                            @Override
                            public void setJdbcBatchSize(Integer jdbcBatchSize) {

                            }

                            @Override
                            public Query getNamedQuery(String queryName) {
                                return null;
                            }

                            @Override
                            public Query createNamedQuery(String name) {
                                return null;
                            }

                            @Override
                            public NativeQuery createNativeQuery(String sqlString) {
                                return null;
                            }

                            @Override
                            public NativeQuery createNativeQuery(String sqlString, String resultSetMapping) {
                                return null;
                            }

                            @Override
                            public NativeQuery getNamedNativeQuery(String name) {
                                return null;
                            }

                            @Override
                            public void addEventListeners(SessionEventListener... listeners) {
                            }

                            @Override
                            public boolean isReadOnly(Object o) {
                                return false;
                            }

                            @Override
                            public void setReadOnly(Object o, boolean b) {
                            }

                            @Override
                            public SharedSessionBuilder sessionWithOptions() {
                                return null;
                            }

                            @Override
                            public void flush() throws HibernateException {
                            }

                            @Override
                            @Deprecated
                            public void setFlushMode(FlushMode flushMode) {
                            }

                            @Override
                            public void setCacheMode(CacheMode cacheMode) {
                            }

                            @Override
                            public CacheMode getCacheMode() {
                                return null;
                            }

                            @Override
                            public SessionFactory getSessionFactory() {
                                return null;
                            }

                            @Override
                            public void cancelQuery() throws HibernateException {
                            }

                            @Override
                            public boolean isOpen() {
                                return false;
                            }

                            @Override
                            public boolean isConnected() {
                                return false;
                            }

                            @Override
                            public boolean isDirty() throws HibernateException {
                                return false;
                            }

                            @Override
                            public boolean isDefaultReadOnly() {
                                return false;
                            }

                            @Override
                            public void setDefaultReadOnly(boolean b) {
                            }

                            @Override
                            public Serializable getIdentifier(Object o) {
                                return null;
                            }

                            @Override
                            public boolean contains(Object o) {
                                return false;
                            }

                            @Override
                            public void evict(Object o) {
                            }

                            @Override
                            @Deprecated
                            public Object load(Class aClass, Serializable serializable, LockMode lockMode) {
                                return null;
                            }

                            @Override
                            public Object load(Class aClass, Serializable serializable, LockOptions lockOptions) {
                                return null;
                            }

                            @Override
                            @Deprecated
                            public Object load(String s, Serializable serializable, LockMode lockMode) {
                                return null;
                            }

                            @Override
                            public Object load(String s, Serializable serializable, LockOptions lockOptions) {
                                return null;
                            }

                            @Override
                            public Object load(Class aClass, Serializable serializable) {
                                return null;
                            }

                            @Override
                            public Object load(String s, Serializable serializable) {
                                return null;
                            }

                            @Override
                            public void load(Object o, Serializable serializable) {
                            }

                            @Override
                            public void replicate(Object o, ReplicationMode replicationMode) {
                            }

                            @Override
                            public void replicate(String s, Object o, ReplicationMode replicationMode) {
                            }

                            @Override
                            public Serializable save(Object o) {
                                return 1L;
                            }

                            @Override
                            public Serializable save(String s, Object o) {
                                return 1L;
                            }

                            @Override
                            public void saveOrUpdate(Object o) {
                            }

                            @Override
                            public void saveOrUpdate(String s, Object o) {
                            }

                            @Override
                            public void update(Object o) {
                            }

                            @Override
                            public void update(String s, Object o) {
                            }

                            @Override
                            public Object merge(Object o) {
                                return o;
                            }

                            @Override
                            public Object merge(String s, Object o) {
                                return o;
                            }

                            @Override
                            public void persist(Object o) {
                            }

                            @Override
                            public void persist(String s, Object o) {
                            }

                            @Override
                            public void delete(Object o) {
                            }

                            @Override
                            public void delete(String s, Object o) {
                            }

                            @Override
                            @Deprecated
                            public void lock(Object o, LockMode lockMode) {
                            }

                            @Override
                            @Deprecated
                            public void lock(String s, Object o, LockMode lockMode) {
                            }

                            @Override
                            public LockRequest buildLockRequest(LockOptions lockOptions) {
                                return null;
                            }

                            @Override
                            public void refresh(Object o) {
                            }

                            @Override
                            public void refresh(String s, Object o) {
                            }

                            @Override
                            @Deprecated
                            public void refresh(Object o, LockMode lockMode) {
                            }

                            @Override
                            public void refresh(Object o, LockOptions lockOptions) {
                            }

                            @Override
                            public void refresh(String s, Object o, LockOptions lockOptions) {
                            }

                            @Override
                            public LockMode getCurrentLockMode(Object o) {
                                return null;
                            }

                            @Override
                            public void clear() {
                            }

                            @Override
                            public Object get(Class aClass, Serializable serializable) {
                                return null;
                            }

                            @Override
                            @Deprecated
                            public Object get(Class aClass, Serializable serializable, LockMode lockMode) {
                                return null;
                            }

                            @Override
                            public Object get(Class aClass, Serializable serializable, LockOptions lockOptions) {
                                return null;
                            }

                            @Override
                            public Object get(String s, Serializable serializable) {
                                return null;
                            }

                            @Override
                            @Deprecated
                            public Object get(String s, Serializable serializable, LockMode lockMode) {
                                return null;
                            }

                            @Override
                            public Object get(String s, Serializable serializable, LockOptions lockOptions) {
                                return null;
                            }

                            @Override
                            public String getEntityName(Object o) {
                                return null;
                            }

                            @Override
                            public IdentifierLoadAccess byId(String s) {
                                return null;
                            }

                            @Override
                            public IdentifierLoadAccess byId(Class aClass) {
                                return null;
                            }

                            @Override
                            public NaturalIdLoadAccess byNaturalId(String s) {
                                return null;
                            }

                            @Override
                            public NaturalIdLoadAccess byNaturalId(Class aClass) {
                                return null;
                            }

                            @Override
                            public SimpleNaturalIdLoadAccess bySimpleNaturalId(String s) {
                                return null;
                            }

                            @Override
                            public SimpleNaturalIdLoadAccess bySimpleNaturalId(Class aClass) {
                                return null;
                            }

                            @Override
                            public Filter enableFilter(String s) {
                                return null;
                            }

                            @Override
                            public Filter getEnabledFilter(String s) {
                                return null;
                            }

                            @Override
                            public void disableFilter(String s) {
                            }

                            @Override
                            public SessionStatistics getStatistics() {
                                return null;
                            }

                            @Override
                            public void doWork(Work work) throws HibernateException {
                            }

                            @Override
                            public <T> T doReturningWork(ReturningWork<T> tReturningWork) throws HibernateException {
                                return null;
                            }

                            @Override
                            public Connection disconnect() {
                                return null;
                            }

                            @Override
                            public void reconnect(Connection connection) {
                            }

                            @Override
                            public boolean isFetchProfileEnabled(String s) throws UnknownProfileException {
                                return false;
                            }

                            @Override
                            public void disableFetchProfile(String s) throws UnknownProfileException {
                            }

                            @Override
                            public TypeHelper getTypeHelper() {
                                return null;
                            }

                            @Override
                            public LobHelper getLobHelper() {
                                return null;
                            }

                            @Override
                            public String getTenantIdentifier() {
                                return null;
                            }

                            @Override
                            public Transaction beginTransaction() {
                                return new Transaction()
                                {
                                    @Override
                                    public TransactionStatus getStatus() {
                                        return null;
                                    }

                                    @Override
                                    public void setRollbackOnly() {

                                    }

                                    @Override
                                    public boolean getRollbackOnly() {
                                        return false;
                                    }

                                    @Override
                                    public void begin() {
                                    }

                                    @Override
                                    public void commit() {
                                        throw new RuntimeException("Simulate failing session.");
                                    }

                                    @Override
                                    public void rollback() {
                                    }

                                    @Override
                                    public boolean isActive() {
                                        return false;
                                    }

                                    @Override
                                    public void registerSynchronization(Synchronization synchronization) throws HibernateException {
                                    }

                                    @Override
                                    public void setTimeout(int i) {
                                    }

                                    @Override
                                    public int getTimeout() {
                                        return 0;
                                    }
                                };
                            }

                            @Override
                            public Transaction getTransaction() {
                                return null;
                            }

                            @Override
                            @Deprecated
                            public Criteria createCriteria(Class aClass) {
                                return null;
                            }

                            @Override
                            @Deprecated
                            public Criteria createCriteria(Class aClass, String s) {
                                return null;
                            }

                            @Override
                            @Deprecated
                            public Criteria createCriteria(String s) {
                                return null;
                            }

                            @Override
                            @Deprecated
                            public Criteria createCriteria(String s, String s2) {
                                return null;
                            }

                            @Override
                            public void enableFetchProfile(String s) throws UnknownProfileException {
                            }
                        };
                    }

                    @Override
                    public Session getCurrentSession() throws HibernateException {
                        return null;
                    }

                    @Override
                    public StatelessSessionBuilder withStatelessOptions() {
                        return null;
                    }

                    @Override
                    public StatelessSession openStatelessSession() {
                        return null;
                    }

                    @Override
                    public StatelessSession openStatelessSession(Connection connection) {
                        return null;
                    }

                    @Override
                    @Deprecated
                    public ClassMetadata getClassMetadata(Class aClass) {
                        return null;
                    }

                    @Override
                    @Deprecated
                    public ClassMetadata getClassMetadata(String s) {
                        return null;
                    }

                    @Override
                    @Deprecated
                    public CollectionMetadata getCollectionMetadata(String s) {
                        return null;
                    }

                    @Override
                    @Deprecated
                    public Map<String, ClassMetadata> getAllClassMetadata() {
                        return null;
                    }

                    @Override
                    @Deprecated
                    public Map getAllCollectionMetadata() {
                        return null;
                    }

                    @Override
                    public Statistics getStatistics() {
                        return null;
                    }

                    @Override
                    public void close() throws HibernateException {
                    }

                    @Override
                    public boolean isClosed() {
                        return false;
                    }

                    @Override
                    public Cache getCache() {
                        return null;
                    }

                    @Override
                    public Set getDefinedFilterNames() {
                        return null;
                    }

                    @Override
                    public FilterDefinition getFilterDefinition(String s) throws HibernateException {
                        return null;
                    }

                    @Override
                    public boolean containsFetchProfileDefinition(String s) {
                        return false;
                    }

                    @Override
                    public TypeHelper getTypeHelper() {
                        return null;
                    }

                    @Override
                    public Reference getReference() throws NamingException {
                        return null;
                    }
                };
            }
        };

        manager = DefaultPersistenceManagerImpl.getInstance();
    }

    @Before
    public void preTest() {
        manager.setHibernateUtil(null);
    }

    @AfterClass
    public static void postSuite() {
        object = null;
        failedOpenSessionUtil = null;
        sessionFailureUtil = null;
    }

    @Test
    public void testInstance() {
        assertNotNull("Persistence manager should not be null.", manager);
        assertNotNull("Hibernate Utility should not be null.", manager.getHibernateUtil());
    }

    @Test
    public void testFailedOpenSession() {
        manager.setHibernateUtil(failedOpenSessionUtil);

        try {
            manager.get(PersistenceCapable.class, 1L);
            fail("This should have thrown a PersistenceException");
        }
        catch (PersistenceException pe) {
            // passed
        }
    }

    @Test
    public void testSessionFailure() {
        manager.setHibernateUtil(sessionFailureUtil);

        try {
            manager.makePersistent(object);
            fail("Should have thrown a PersistenceException");
        }
        catch (PersistenceException pe) {
            // passed
        }

        try {
            manager.deletePersistent(object);
            fail("Should have thrown a PersistenceException");
        }
        catch (PersistenceException pe) {
            // passed
        }

        try {
            manager.update(object);
            fail("Should have thrown a PersistenceException");
        }
        catch (PersistenceException pe) {
            // passed
        }

        try {
            manager.get(PersistenceCapable.class, 1L);
            fail("Should have thrown a PersistenceException");
        }
        catch (PersistenceException pe) {
            // passed
        }

        try {
            manager.getFirst(PersistenceCapable.class);
            fail("Should have thrown a PersistenceException");
        }
        catch (PersistenceException pe) {
            // passed
        }

        try {
            manager.getFirst(PersistenceCapable.class, "");
            fail("Should have thrown a PersistenceException");
        }
        catch (PersistenceException pe) {
            // passed
        }

        try {
            manager.getList(new DBQuery(""), PersistenceCapable.class);
            fail("Should have thrown a PersistenceException");
        }
        catch (PersistenceException pe) {
            // passed
        }

        try {
            manager.getList(new DBQuery(""));
            fail("Should have thrown a PersistenceException");
        }
        catch (PersistenceException pe) {
            // passed
        }

        try {
            manager.executeManipulationQuery(new DBQuery(""));
            fail("Should have thrown a PersistenceException");
        }
        catch (PersistenceException pe) {
            // passed
        }

        try {
            manager.getCount(new DBQuery(""));
            fail("Should have thrown a PersistenceException");
        }
        catch (PersistenceException pe) {
            // passed
        }
    }

    @Test(expected = PersistenceException.class)
    public void testGetWithIllegalParameters() throws PersistenceException {
        manager.get(PersistenceCapable.class, null);

        fail("Should have thrown a PersistenceException.");
    }
}
