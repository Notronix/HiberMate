package com.notronix.hibermate.api;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static junit.framework.TestCase.assertEquals;

@RunWith(JUnit4.class)
public class PersistenceExceptionTest
{
    @Test
    public void testConstructor() {
        PersistenceException pe = new PersistenceException("message");
        assertEquals("Failed to set message", "message", pe.getMessage());

        RuntimeException re = new RuntimeException();
        pe = new PersistenceException("message", re);

        assertEquals("Failed to set message.", "message", pe.getMessage());
        assertEquals("Failed to set cause.", re, pe.getCause());
    }
}
