package com.notronix.hibermate;

import java.time.Instant;

public interface Syncable
{
    Instant getLastSynchronizedDate();
    void setLastSynchronizedDate(Instant lastSynchronizedDate);

    boolean isOutdated(int timeoutInHours);

    static boolean isOutdated(Syncable syncable, int timeoutInHours) {
        if (syncable == null) {
            return false;
        }

        Instant date = syncable.getLastSynchronizedDate();

        if (date == null) {
            return true;
        }

        long time = date.toEpochMilli();
        long now = Instant.now().toEpochMilli();
        long limit = timeoutInHours * 60 * 60 * 1000;

        return (now - time) > limit;
    }
}
