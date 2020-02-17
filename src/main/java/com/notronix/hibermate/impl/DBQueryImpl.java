package com.notronix.hibermate.impl;

import com.notronix.hibermate.api.DBQuery;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;

import java.util.HashMap;
import java.util.Map;

import static com.notronix.albacore.ContainerUtils.thereAreOneOrMore;

public class DBQueryImpl implements DBQuery
{
    private String query;
    private Map<String, Object> params;

    public DBQueryImpl() {
    }

    public DBQueryImpl(String query) {
        this.query = query;
    }

    @Override
    public String getQuery() {
        return query;
    }

    @Override
    public void setQuery(String query) {
        this.query = query;
    }

    @Override
    public void setParameter(String name, Object value) {
        if (params == null) {
            params = new HashMap<>();
        }

        params.put(name, value);
    }

    @Override
    public Object getParameter(String name) {
        return params == null ? null : params.get(name);
    }

    @Override
    public <T> NativeQuery<T> getQuery(Session session, Class<T> resultType) {
        @SuppressWarnings("unchecked")
        NativeQuery<T> sqlQuery = resultType == null ?
                session.createNativeQuery(query) : session.createNativeQuery(query, resultType);

        if (thereAreOneOrMore(params)) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                sqlQuery.setParameter(entry.getKey(), entry.getValue());
            }
        }

        return sqlQuery;
    }
}
