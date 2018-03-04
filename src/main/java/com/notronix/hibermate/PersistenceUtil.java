package com.notronix.hibermate;

public abstract class PersistenceUtil
{
    public static boolean itIsAValid(PersistenceCapable object) {
        return (object != null && itIsAValid(object.getSystemId()));
    }
    public static boolean itIsAValid(Long systemId) {
        return systemId != null && systemId > 0;
    }

    public static boolean itIsNotAValid(PersistenceCapable object) {
        return !itIsAValid(object);
    }
    public static boolean itIsNotAValid(Long systemId) {
        return !itIsAValid(systemId);
    }
}
