package com.notronix.hibermate;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;

import java.util.HashMap;
import java.util.Map;

import static com.notronix.albacore.ContainerUtils.thereAreOneOrMore;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class DBQuery
{
    private String query;
    private Map<String, String> stringParams;
    private Map<String, Long> longParams;
    private Map<String, Double> doubleParams;
    private Map<String, Integer> integerParams;

    public DBQuery() {
    }

    public DBQuery(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public void setString(String name, String value) {
        if (stringParams == null) {
            stringParams = new HashMap<>();
        }

        stringParams.put(name, value);
    }

    public String getString(String name) {
        return stringParams == null ? null : stringParams.get(name);
    }

    public void setLong(String name, Long value) {
        if (longParams == null) {
            longParams = new HashMap<>();
        }

        longParams.put(name, value);
    }

    public Long getLong(String name) {
        return longParams == null ? null : longParams.get(name);
    }

    public void setDouble(String name, Double value) {
        if (doubleParams == null) {
            doubleParams = new HashMap<>();
        }

        doubleParams.put(name, value);
    }

    public Double getDouble(String name) {
        return doubleParams == null ? null : doubleParams.get(name);
    }

    public void setInteger(String name, Integer value) {
        if (integerParams == null) {
            integerParams = new HashMap<>();
        }

        integerParams.put(name, value);
    }

    public Integer getInteger(String name) {
        return integerParams == null ? null : integerParams.get(name);
    }

    public <T> NativeQuery<T> getQuery(Session session, Class<T> resultType) {
        NativeQuery<T> sqlQuery = resultType == null ?
                session.createNativeQuery(query) : session.createNativeQuery(query, resultType);

        if (thereAreOneOrMore(stringParams)) {
            for (Map.Entry<String, String> entry : stringParams.entrySet()) {
                sqlQuery.setString(entry.getKey(), entry.getValue());
            }
        }

        if (thereAreOneOrMore(longParams)) {
            for (Map.Entry<String, Long> entry : longParams.entrySet()) {
                sqlQuery.setLong(entry.getKey(), entry.getValue());
            }
        }

        if (thereAreOneOrMore(doubleParams)) {
            for (Map.Entry<String, Double> entry : doubleParams.entrySet()) {
                sqlQuery.setDouble(entry.getKey(), entry.getValue());
            }
        }

        if (thereAreOneOrMore(integerParams)) {
            for (Map.Entry<String, Integer> entry : integerParams.entrySet()) {
                sqlQuery.setInteger(entry.getKey(), entry.getValue());
            }
        }

        return sqlQuery;
    }

    public static String addFilter(String params, String filter, String field) {
        if (StringUtils.isEmpty(filter)) {
            return params;
        }

        if (isNotBlank(params)) {
            params += " AND ";
        }

        params += "lower(" + field + ") like '%" + filter.toLowerCase() + "%'";

        return params;
    }
}
