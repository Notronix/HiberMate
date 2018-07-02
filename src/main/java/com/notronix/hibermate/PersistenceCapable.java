package com.notronix.hibermate;

public interface PersistenceCapable<T>
{
    T getSystemId();
    void setSystemId(T systemId);
}
