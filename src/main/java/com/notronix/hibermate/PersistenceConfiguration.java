package com.notronix.hibermate;

import org.hibernate.HibernateException;
import org.hibernate.cfg.Configuration;
import org.w3c.dom.Document;

import java.io.File;
import java.net.URL;

public class PersistenceConfiguration extends Configuration
{
    private boolean configured = false;

    public boolean isConfigured() {
        return configured;
    }

    @Override
    public Configuration configure() throws HibernateException {
        configured = true;

        return super.configure();
    }

    @Override
    public Configuration configure(String resource) throws HibernateException {
        configured = true;

        return super.configure(resource);
    }

    @Override
    public Configuration configure(URL url) throws HibernateException {
        configured = true;

        return super.configure(url);
    }

    @Override
    public Configuration configure(File configFile) throws HibernateException {
        configured = true;

        return super.configure(configFile);
    }

    @Override
    @Deprecated
    public Configuration configure(Document document) throws HibernateException {
        configured = true;

        return super.configure(document);
    }
}
