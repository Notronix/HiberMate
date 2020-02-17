package com.notronix.hibermate.impl;

import com.notronix.hibermate.api.PersistenceException;
import com.notronix.hibermate.api.PersistenceManager;
import org.hibernate.Session;
import org.hibernate.boot.model.relational.Database;
import org.hibernate.boot.registry.BootstrapServiceRegistryBuilder;
import org.hibernate.type.BasicTypeRegistry;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.*;

import static com.notronix.albacore.ContainerUtils.thereAreNo;
import static com.notronix.albacore.ContainerUtils.thereAreOneOrMore;
import static com.notronix.hibermate.api.PersistenceManager.itIsAValid;
import static com.notronix.hibermate.api.PersistenceManager.itIsNotAValid;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class DefaultPersistenceManagerImplTest
{
    private static PersistenceManager failingSessionManager;
    private static PersistenceManager realManager;

    @BeforeClass
    public static void preSuite() {
        failingSessionManager = new DefaultPersistenceManagerImpl(new BasicTypeRegistry(),
                new BootstrapServiceRegistryBuilder())
        {
            @Override
            public Session openSession() throws PersistenceException {
                throw new PersistenceException("failed opening session");
            }
        };

        realManager = DefaultPersistenceManagerImpl.createDefaultManager();
    }

    @Before
    public void preTest() {
    }

    @AfterClass
    public static void postSuite() {
        failingSessionManager = null;
        realManager = null;
    }

    @Test
    public void testItIsValid() {
        assertFalse("Null should not be valid.", itIsAValid(null));

        PersistedLong pcLong = new PersistedLong();
        assertFalse("New items should not be valid.", itIsAValid(pcLong));

        pcLong.setSystemId(0L);
        assertFalse("Should not be valid if systemId=0", itIsAValid(pcLong));

        pcLong.setSystemId(1L);
        assertTrue("Should be valid", itIsAValid(pcLong));
    }

    @Test
    public void testItIsNotValid() {
        assertTrue("Null should not be valid.", itIsNotAValid(null));

        PersistedLong pcLong = new PersistedLong();
        assertTrue("New items should not be valid.", itIsNotAValid(pcLong));

        pcLong.setSystemId(0L);
        assertTrue("Should not be valid if systemId=0", itIsNotAValid(pcLong));

        pcLong.setSystemId(1L);
        assertFalse("Should be valid", itIsNotAValid(pcLong));
    }

    @Test
    public void testInstance() {
        PersistenceManager manager = DefaultPersistenceManagerImpl.createDefaultManager();
        assertNotNull("Persistence manager should not be null.", manager);
    }

    @Test(expected = PersistenceException.class)
    public void testFailedOpenSession() throws PersistenceException {
        failingSessionManager.get(PersistedLong.class, 0L);
    }

    @Test(expected = PersistenceException.class)
    public void testGetWithIllegalParameters() throws PersistenceException {
        failingSessionManager.get(PersistedLong.class, null);
    }

    @Test
    public void testRealDatabaseStorage() throws PersistenceException {
        clearDatabase();

        PersistedLong o1 = new PersistedLong();
        o1.setSystemId(1L);
        long keyResult = realManager.makePersistent(o1);

        assertEquals(1, keyResult);
        assertEquals(o1.getSystemId(), realManager.get(PersistedLong.class, 1L).getSystemId());
    }

    private void clearDatabase() throws PersistenceException {
        try {
            DBQueryImpl query = new DBQueryImpl("SELECT 1");
            realManager.getList(query);
        }
        catch (PersistenceException pe) {
            // continue here... failures will still allow for reading of table names.
        }

        Map<String, List<String>> mappedDependencies = new HashMap<>();
        List<String> remainingTables = new ArrayList<>();
        Database database = realManager.getDatabase();

        for (org.hibernate.mapping.Table table : database.getDefaultNamespace().getTables()) {
            remainingTables.add(table.getName());

            Iterator<?> keys = table.getForeignKeyIterator();
            while (keys.hasNext()) {
                org.hibernate.mapping.ForeignKey key = (org.hibernate.mapping.ForeignKey) keys.next();
                String tableName = key.getReferencedTable().getName();
                List<String> dependencies = mappedDependencies.computeIfAbsent(tableName, k -> new ArrayList<>());
                dependencies.add(key.getTable().getName());
            }
        }

        while (thereAreOneOrMore(remainingTables)) {
            List<String> deletedTables = new ArrayList<>();

            for (String table : remainingTables) {
                Collection<String> blockingTables = mappedDependencies.get(table);

                if (thereAreNo(blockingTables)) {
                    DBQueryImpl query = new DBQueryImpl("DELETE FROM " + table);
                    realManager.executeManipulationQuery(query);
                    deletedTables.add(table);

                    for (String key : mappedDependencies.keySet()) {
                        mappedDependencies.get(key).remove(table);
                    }
                }
            }

            remainingTables.removeAll(deletedTables);
        }
    }
}
