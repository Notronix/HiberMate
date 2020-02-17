package com.notronix.hibermate.impl;

import com.notronix.hibermate.api.PersistenceCapable;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class PersistedLong implements PersistenceCapable<Long>
{
    @Id
    private Long systemId;

    @Override
    public Long getSystemId() {
        return systemId;
    }

    @Override
    public void setSystemId(Long systemId) {
        this.systemId = systemId;
    }
}
