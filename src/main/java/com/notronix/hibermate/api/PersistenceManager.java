package com.notronix.hibermate.api;

import org.hibernate.Session;
import org.hibernate.boot.model.relational.Database;

import java.io.Serializable;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.notronix.albacore.Optionals.ofBlankable;
import static java.util.Arrays.asList;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public interface PersistenceManager
{
    Database getDatabase();
    Session openSession() throws PersistenceException;

    <K extends Serializable> K makePersistent(PersistenceCapable<K> object) throws PersistenceException;
    void deletePersistent(PersistenceCapable<?> object) throws PersistenceException;
    <T extends PersistenceCapable<?>> T update(T object) throws PersistenceException;
    void update(String recordId, String objectType, PersistenceCapable<?> object) throws PersistenceException;

    <K extends Serializable, T extends PersistenceCapable<K>> T get(Class<T> objectClass, K systemId) throws PersistenceException;

    <T extends PersistenceCapable<?>> T getFirst(Class<T> objectClass) throws PersistenceException;
    <T extends PersistenceCapable<?>> T getFirst(Class<T> objectClass, String predicate) throws PersistenceException;
    <T extends PersistenceCapable<?>> T getFirst(Class<T> objectClass, String predicate, Map<String, Object> params) throws PersistenceException;

    <T extends PersistenceCapable<?>> List<T> getList(Class<T> objectClass, String join, String predicate, Map<String, Object> params) throws PersistenceException;
    <T extends PersistenceCapable<?>> List<T> getList(Class<T> objectClass, String join, String predicate) throws PersistenceException;

    <T extends PersistenceCapable<?>> List<T> getList(Class<T> objectClass, String predicate, Map<String, Object> params) throws PersistenceException;
    <T extends PersistenceCapable<?>> List<T> getList(Class<T> objectClass, String predicate) throws PersistenceException;

    <T extends PersistenceCapable<?>> List<T> getList(DBQuery query, Class<T> resultType) throws PersistenceException;
    List<Object[]> getList(DBQuery query) throws PersistenceException;

    int executeManipulationQuery(DBQuery query) throws PersistenceException;
    Long getLong(DBQuery query) throws PersistenceException;

    long getCount(DBQuery query) throws PersistenceException;
    long getCount(Class<? extends PersistenceCapable<?>> objectClass, String predicate) throws PersistenceException;

    double getSum(DBQuery query) throws PersistenceException;

    static void validate(List<Object> inputs) throws PersistenceException {
        if (inputs == null) {
            return;
        }

        for (Object input : inputs) {
            ofNullable(input).orElseThrow(() -> new PersistenceException("null value"));

            if (input instanceof PersistenceCapable) {
                if (itIsNotAValid((PersistenceCapable<?>) input)) {
                    throw new PersistenceException("invalid " + input.getClass().getSimpleName());
                }
            }
            else if (input instanceof Long) {
                if (((Long) input) == 0L) {
                    throw new PersistenceException("invalid system id");
                }
            }
            else if (input instanceof CharSequence) {
                ofBlankable((CharSequence) input)
                        .orElseThrow(() -> new javax.persistence.PersistenceException("invalid value"));
            }
        }
    }

    static void validateLessThan(Double value, Double limit, boolean equalsAllowed) throws PersistenceException {
        validate(asList(value, limit));

        if (equalsAllowed) {
            if (value > limit) {
                throw new PersistenceException("min is greater than max");
            }
        }
        else {
            if (value >= limit) {
                throw new PersistenceException("min is greater than or equal to max");
            }
        }
    }

    static void addPredicateLogic(final StringBuilder predicate, PredicateLogicType logicType) {
        if (logicType == PredicateLogicType.AND) {
            addAnd(predicate);
        }
        else if (logicType == PredicateLogicType.OR) {
            addOr(predicate);
        }
    }

    static void addAnd(final StringBuilder predicate) {
        ofBlankable(predicate.toString()).ifPresent(p -> predicate.append(" and "));
    }

    static void addOr(final StringBuilder predicate) {
        ofBlankable(predicate.toString()).ifPresent(p -> predicate.append(" or "));
    }

    static void addBoolean(final StringBuilder predicate, boolean b, String variableName, PredicateLogicType logicType) {
        addPredicateLogic(predicate, logicType);

        if (b) {
            predicate.append(variableName).append(" = 1");
        }
        else {
            predicate.append("(").append(variableName).append(" is null or ").append(variableName).append(" = 0)");
        }
    }

    static void addParams(final StringBuilder predicate, String params, PredicateLogicType logicType) {
        addPredicateLogic(predicate, logicType);
        predicate.append("(").append(params).append(")");
    }

    static void addEquals(final StringBuilder predicate,
                                 String variable,
                                 String variableName,
                                 Object value,
                                 final Map<String, Object> params,
                                 PredicateLogicType logicType) {
        addPredicateLogic(predicate, logicType);
        predicate.append(variable).append(" = :").append(variableName);
        params.put(variableName, value);
    }

    static void addLike(final StringBuilder predicate,
                               String variable,
                               String variableName,
                               String value,
                               final Map<String, Object> params,
                               PredicateLogicType logicType) {
        addPredicateLogic(predicate, logicType);
        predicate.append("lower(").append(variable).append(") like :").append(variableName);
        params.put(variableName, "%" + value.trim().toLowerCase() + "%");
    }

    static void addStartDate(final StringBuilder predicate,
                                    String variable,
                                    String variableName,
                                    Instant date,
                                    final Map<String, Object> params,
                                    PredicateLogicType logicType) {
        addPredicateLogic(predicate, logicType);
        predicate.append(variable).append(" >= :").append(variableName);
        params.put(variableName, date.toEpochMilli());
    }

    static void addClause(final StringBuilder predicate,
                                 String variable,
                                 String variableName,
                                 Object value,
                                 final Map<String, Object> params,
                                 FilterType clauseType,
                                 PredicateLogicType logicType) {
        addPredicateLogic(predicate, logicType);
        predicate.append(variable).append(" ").append(clauseType.syntax()).append(" :").append(variableName);
        params.put(variableName, value);
    }

    static void addEndDate(final StringBuilder predicate,
                                  String variable,
                                  String variableName,
                                  Instant date,
                                  final Map<String, Object> params,
                                  PredicateLogicType logicType) {
        addPredicateLogic(predicate, logicType);
        predicate.append(variable).append(" <= :").append(variableName);
        params.put(variableName, date.toEpochMilli());
    }

    static void addIn(final StringBuilder predicate,
                             String variable,
                             String variableName,
                             Collection<?> objects,
                             final Map<String, Object> params,
                             PredicateLogicType logicType) {
        addPredicateLogic(predicate, logicType);
        predicate.append(variable).append(" IN (:").append(variableName).append(")");
        params.put(variableName, objects);
    }

    static boolean itIsAValid(PersistenceCapable<?> object) {
        return nonNull(object) && isValid(object.getSystemId());
    }

    static boolean isValid(Object object) {
        if (isNull(object)) {
            return false;
        }

        if (object instanceof Long) {
            return (Long) object != 0;
        }

        if (object instanceof CharSequence) {
            return isNotBlank((CharSequence) object);
        }

        return true;
    }

    static boolean isValid(String object) {
        return isNotBlank(object);
    }

    static boolean itIsNotAValid(PersistenceCapable<?> object) {
        return !itIsAValid(object);
    }
}
