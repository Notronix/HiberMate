package com.notronix.hibermate;

import java.util.List;
import java.util.Map;

public interface PersistenceManager
{
    HibernateUtil getHibernateUtil();
    void setHibernateUtil(HibernateUtil hibernateUtil);

    <T extends PersistenceCapable> Long makePersistent(T object) throws PersistenceException;
    <T extends PersistenceCapable> void deletePersistent(T object) throws PersistenceException;
    <T extends PersistenceCapable> T update(T object) throws PersistenceException;

    <T extends PersistenceCapable> T get(Class<T> objectClass, Long systemId) throws PersistenceException;

    <T extends PersistenceCapable> T getFirst(Class<T> objectClass) throws PersistenceException;
    <T extends PersistenceCapable> T getFirst(Class<T> objectClass, String predicate) throws PersistenceException;
    <T extends PersistenceCapable> T getFirst(Class<T> objectClass, String predicate, Map<String, Object> params) throws PersistenceException;

    <T extends PersistenceCapable> List<T> getList(Class<T> objectClass, String join, String predicate, Map<String, Object> params) throws PersistenceException;
    <T extends PersistenceCapable> List<T> getList(Class<T> objectClass, String join, String predicate) throws PersistenceException;

    <T extends PersistenceCapable> List<T> getList(Class<T> objectClass, String predicate, Map<String, Object> params) throws PersistenceException;
    <T extends PersistenceCapable> List<T> getList(Class<T> objectClass, String predicate) throws PersistenceException;

    <T extends PersistenceCapable> List<T> getList(DBQuery query, Class<T> resultType) throws PersistenceException;
    List<Object[]> getList(DBQuery query) throws PersistenceException;

    int executeManipulationQuery(DBQuery query) throws PersistenceException;
    Long getLong(DBQuery query) throws PersistenceException;
    long getCount(DBQuery query) throws PersistenceException;
    double getSum(DBQuery query) throws PersistenceException;
}
