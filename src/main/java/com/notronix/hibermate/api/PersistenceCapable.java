package com.notronix.hibermate.api;

import java.io.Serializable;

public interface PersistenceCapable<T extends Serializable>
{
    T getSystemId();
    void setSystemId(T systemId);
}
