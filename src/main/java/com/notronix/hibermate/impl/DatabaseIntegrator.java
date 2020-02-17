package com.notronix.hibermate.impl;

import org.hibernate.boot.Metadata;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.integrator.spi.Integrator;
import org.hibernate.service.spi.SessionFactoryServiceRegistry;

public class DatabaseIntegrator implements Integrator
{
    private final DefaultPersistenceManagerImpl persistenceManager;

    public DatabaseIntegrator(DefaultPersistenceManagerImpl persistenceManager) {
        this.persistenceManager = persistenceManager;
    }

    @Override
    public void integrate(Metadata metadata, SessionFactoryImplementor sessionFactory, SessionFactoryServiceRegistry serviceRegistry) {
        persistenceManager.setDatabase(metadata.getDatabase());
    }

    @Override
    public void disintegrate(SessionFactoryImplementor sessionFactory, SessionFactoryServiceRegistry serviceRegistry) {
        persistenceManager.setDatabase(null);
    }
}
