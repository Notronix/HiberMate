package com.notronix.hibermate.api;

import org.hibernate.Session;
import org.hibernate.query.NativeQuery;

public interface DBQuery
{
    String getQuery();
    void setQuery(String query);

    Object getParameter(String name);
    void setParameter(String name, Object value);

    <T> NativeQuery<T> getQuery(Session session, Class<T> resultType);
}
