package com.notronix.hibermate;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static junit.framework.TestCase.assertEquals;

@RunWith(JUnit4.class)
public class PersistenceExceptionTest
{
    @Test
    public void testConstructor() {
        RuntimeException re = new RuntimeException();
        PersistenceException pe = new PersistenceException("message", re);

        assertEquals("Failed to set message.", "message", pe.getMessage());
        assertEquals("Failed to set cause.", re, pe.getCause());
    }
}
