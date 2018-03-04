package com.notronix.hibermate;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class DefaultHibernateUtilImplTest
{
    private static final String TEST_DB = "stockupUnitTest";

    @Test
    public void testConfiguration() {
        try {
            DefaultHibernateUtilImpl util = new DefaultHibernateUtilImpl();
            Configuration configuration = util.getConfiguration();
            assertNotNull("Configuration should not be null.", configuration);
        }
        catch (PersistenceException pe) {
            fail("No exception should have been thrown.");
        }
    }

    @Test(expected = PersistenceException.class)
    public void testConfigurationException() throws PersistenceException {
        PersistenceConfiguration configuration = new PersistenceConfiguration()
        {
            @Override
            public Configuration configure() throws HibernateException {
                throw new HibernateException("Testing exception handling.");
            }
        };

        DefaultHibernateUtilImpl util = new DefaultHibernateUtilImpl();
        util.setConfiguration(configuration);
        util.getConfiguration();

        fail("Should have thrown a PersistenceException.");
    }

    @Test
    public void testSessionFactory() {
        try {
            System.setProperty(HibernateUtil.CONNECTION_URL, "jdbc:mysql://localhost/" + TEST_DB);
            System.setProperty(HibernateUtil.CONNECTION_USERNAME, "stock-up");
            System.setProperty(HibernateUtil.CONNECTION_PASSWORD, "ZQ.u!zBB$(Y");

            DefaultHibernateUtilImpl util = new DefaultHibernateUtilImpl();

            SessionFactory sessionFactory = util.getSessionFactory();
            assertNotNull("Session factory was null.", sessionFactory);

            util.setConfiguration(null);
            assertNotEquals("Should be a new session factory created.", sessionFactory, util.getSessionFactory());
        }
        catch (PersistenceException pe) {
            fail("No exception should have been thrown.");
        }
    }

    @Test(expected = PersistenceException.class)
    public void testSessionFactoryExceptionHandling() throws PersistenceException {
        PersistenceConfiguration configuration = new PersistenceConfiguration()
        {
            @Override
            public SessionFactory buildSessionFactory(ServiceRegistry serviceRegistry) throws HibernateException {
                throw new HibernateException("Testing exception handling.");
            }
        };

        DefaultHibernateUtilImpl util = new DefaultHibernateUtilImpl();
        util.setConfiguration(configuration);
        util.getSessionFactory();

        fail("Should have thrown a PersistenceException.");
    }
}
