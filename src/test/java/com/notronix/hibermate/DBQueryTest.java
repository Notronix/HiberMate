package com.notronix.hibermate;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(JUnit4.class)
public class DBQueryTest
{
    @Test
    public void testQueryString()
    {
        DBQuery query = new DBQuery("test");
        assertEquals("Constructor did not save query string.", "test", query.getQuery());

        query.setQuery(null);
        assertNull("Query string should be null.", query.getQuery());
    }

    @Test
    public void testSetString()
    {
        DBQuery query = new DBQuery("test");
        assertNull("param1 should be null.", query.getString("param1"));

        query.setString("param1", "param value");
        assertEquals("String param not stored correctly.", "param value", query.getString("param1"));

        query.setString("param1", null);
        assertNull("Failed setting string param to null.", query.getString("param1"));
    }

    @Test
    public void testSetDouble()
    {
        DBQuery query = new DBQuery("test");
        assertNull("param1 should be null.", query.getDouble("param1"));

        query.setDouble("param1", (double) 10);
        assertEquals("Double param not stored correctly.", new Double(10), query.getDouble("param1"));

        query.setDouble("param1", null);
        assertNull("Failed setting double param to null.", query.getDouble("param1"));
    }

    @Test
    public void testSetLong()
    {
        DBQuery query = new DBQuery("test");
        assertNull("param1 should be null.", query.getLong("param1"));

        query.setLong("param1", (long) 10);
        assertEquals("Long param not stored correctly.", new Long(10), query.getLong("param1"));

        query.setLong("param1", null);
        assertNull("Failed setting long param to null.", query.getLong("param1"));
    }

    @Test
    public void testSetInteger()
    {
        DBQuery query = new DBQuery("test");
        assertNull("param1 should be null.", query.getInteger("param1"));

        query.setInteger("param1", 10);
        assertEquals("Integer param not stored correctly.", new Integer(10), query.getInteger("param1"));

        query.setInteger("param1", null);
        assertNull("Failed setting integer param to null.", query.getInteger("param1"));
    }
}
