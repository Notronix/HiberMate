package com.notronix.hibermate;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static com.notronix.hibermate.PersistenceUtil.itIsAValid;
import static com.notronix.hibermate.PersistenceUtil.itIsNotAValid;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class PersistenceUtilTest
{
    private PersistenceCapable object;

    @Before
    public void preTest() {
        object = new PersistenceCapable()
        {
            private Object systemId;

            @Override
            public Object getSystemId() {
                return systemId;
            }

            @Override
            public void setSystemId(Object systemId) {
                this.systemId = systemId;
            }
        };
    }

    @After
    public void postTest() {
        object = null;
    }

    @Test
    public void testItIsValid() {
        assertFalse("Null should not be valid.", itIsAValid(null));
        assertFalse("New items should not be valid.", itIsAValid(object));

        object.setSystemId(0L);
        assertFalse("Should not be valid if systemId=0", itIsAValid(object));

        object.setSystemId(1L);
        assertTrue("Should be valid", itIsAValid(object));
    }

    @Test
    public void testItIsNotValid() {
        assertTrue("Null should not be valid.", itIsNotAValid(null));
        assertTrue("New items should not be valid.", itIsNotAValid(object));

        object.setSystemId(0L);
        assertTrue("Should not be valid if systemId=0", itIsNotAValid(object));

        object.setSystemId(1L);
        assertFalse("Should be valid", itIsNotAValid(object));
    }
}
