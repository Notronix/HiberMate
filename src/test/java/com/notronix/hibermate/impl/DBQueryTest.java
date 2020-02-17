package com.notronix.hibermate.impl;

import com.notronix.hibermate.api.PersistenceException;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(JUnit4.class)
public class DBQueryTest
{
    @Test
    public void testDefaultConstructor() {
        DBQueryImpl query = new DBQueryImpl();
        assertNull("Query string should be null", query.getQuery());
    }

    @Test
    public void testQueryString() {
        DBQueryImpl query = new DBQueryImpl("test");
        assertEquals("Constructor did not save query string.", "test", query.getQuery());

        query.setQuery(null);
        assertNull("Query string should be null.", query.getQuery());
    }

    @Test
    public void testSetParameter() {
        DBQueryImpl query = new DBQueryImpl("test");
        assertNull("param1 should be null.", query.getParameter("param1"));

        query.setParameter("param1", "param value");
        assertEquals("String param not stored correctly.", "param value", query.getParameter("param1"));

        query.setParameter("param1", null);
        assertNull("Failed setting string param to null.", query.getParameter("param1"));
    }

    @Test
    public void testGetNativeQuery() throws PersistenceException {
        DBQueryImpl query = new DBQueryImpl("select * from test where z = :param");
        query.setParameter("param", "test");

        try (Session session = DefaultPersistenceManagerImpl.createDefaultManager().openSession()) {
            NativeQuery<?> nativeQuery = query.getQuery(session, null);

            assertEquals(query.getQuery(), nativeQuery.getQueryString());
        }
    }
}
