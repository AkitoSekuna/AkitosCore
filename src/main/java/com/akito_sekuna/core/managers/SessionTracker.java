package com.akito_sekuna.core.managers;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SessionTracker {

    private final ConcurrentHashMap<UUID, Long> joinTimes = new ConcurrentHashMap<>();

    public void startSession(UUID uuid) {
        joinTimes.put(uuid, System.currentTimeMillis());
    }

    public long getSessionSeconds(UUID uuid) {
        Long start = joinTimes.get(uuid);
        if (start == null) return 0;
        return (System.currentTimeMillis() - start) / 1000;
    }

    public void endSession(UUID uuid) {
        joinTimes.remove(uuid);
    }
}
