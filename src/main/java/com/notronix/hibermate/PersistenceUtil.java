package com.notronix.hibermate;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public abstract class PersistenceUtil
{
    public static boolean itIsAValid(PersistenceCapable object) {
        return nonNull(object) && isValid(object.getSystemId());
    }

    private static boolean isValid(Object object) {
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

    private static boolean isValid(String object) {
        return isNotBlank(object);
    }

    public static boolean itIsNotAValid(PersistenceCapable object) {
        return !itIsAValid(object);
    }
}
