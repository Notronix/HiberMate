package com.notronix.hibermate;

import java.time.Instant;
import java.time.temporal.TemporalUnit;

public interface Syncable
{
    Instant getLastSynchronizedDate();
    void setLastSynchronizedDate(Instant lastSynchronizedDate);

    default boolean hasNotBeenUpdatedIn(long value, TemporalUnit unit) {
        Instant date = getLastSynchronizedDate();
        if (date == null) {
            return true;
        }

        return date.isBefore(Instant.now().minus(value, unit));
    }
}
