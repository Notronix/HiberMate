package com.notronix.hibermate;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.model.relational.Database;
import org.hibernate.boot.registry.BootstrapServiceRegistry;
import org.hibernate.boot.registry.BootstrapServiceRegistryBuilder;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.integrator.spi.Integrator;
import org.hibernate.service.spi.SessionFactoryServiceRegistry;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class DefaultHibernateUtilImpl implements HibernateUtil
{
    private PersistenceConfiguration configuration;
    private SessionFactory sessionFactory;
    private Database database;

    @Override
    public PersistenceConfiguration getConfiguration() throws PersistenceException {
        try {
            if (configuration == null) {
                configuration = new PersistenceConfiguration();
            }

            if (!configuration.isConfigured()) {
                configuration.configure();
                sessionFactory = null;
            }

            return configuration;
        }
        catch (HibernateException he) {
            throw new PersistenceException("An error occurred getting hibernate configurations.", he);
        }
    }

    @Override
    public void setConfiguration(PersistenceConfiguration persistenceConfiguration) {
        this.configuration = persistenceConfiguration;

        if (sessionFactory != null) {
            this.sessionFactory.close();
            this.sessionFactory = null;
        }
    }

    @Override
    public Database getDatabase() {
        return database;
    }

    @Override
    public SessionFactory getSessionFactory() throws PersistenceException {
        if (sessionFactory == null) {
            BootstrapServiceRegistry bootstrapRegistry = new BootstrapServiceRegistryBuilder()
                    .applyIntegrator(new Integrator()
                    {
                        @Override
                        public void integrate(Metadata metadata,
                                              SessionFactoryImplementor sessionFactory,
                                              SessionFactoryServiceRegistry serviceRegistry) {
                            database = metadata.getDatabase();
                        }

                        @Override
                        public void disintegrate(SessionFactoryImplementor sessionFactory, SessionFactoryServiceRegistry serviceRegistry) {
                        }
                    }).build();

            StandardServiceRegistryBuilder serviceRegistry = new StandardServiceRegistryBuilder(bootstrapRegistry)
                    .configure()
                    .applySettings(getConfiguration().getProperties());

            String connectionUrl = System.getProperty(CONNECTION_URL);
            if (isNotBlank(connectionUrl)) {
                serviceRegistry.applySetting(CONNECTION_URL, connectionUrl);
            }

            String username = System.getProperty(CONNECTION_USERNAME);
            if (isNotBlank(username)) {
                serviceRegistry.applySetting(CONNECTION_USERNAME, username);
            }

            String password = System.getProperty(CONNECTION_PASSWORD);
            if (isNotBlank(password)) {
                serviceRegistry.applySetting(CONNECTION_PASSWORD, password);
            }

            try {
                StandardServiceRegistry registry = serviceRegistry.build();
                sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            }
            catch (HibernateException he) {
                throw new PersistenceException("An error occurred trying to get hibernate session factory.", he);
            }
        }

        return sessionFactory;
    }
}
