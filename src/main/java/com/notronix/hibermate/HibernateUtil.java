package com.notronix.hibermate;

import org.hibernate.SessionFactory;
import org.hibernate.boot.model.relational.Database;

public interface HibernateUtil
{
    String CONNECTION_URL = "hibernate.connection.url";
    String CONNECTION_USERNAME = "hibernate.connection.username";
    String CONNECTION_PASSWORD = "hibernate.connection.password";

    PersistenceConfiguration getConfiguration() throws PersistenceException;
    void setConfiguration(PersistenceConfiguration persistenceConfiguration);

    Database getDatabase();
    SessionFactory getSessionFactory() throws PersistenceException;
}
