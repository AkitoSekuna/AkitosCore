package com.akito_sekuna.core.managers;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Counts command misuse (no-permission, unknown subcommand) between bStats reports.
 * getAndReset() snapshots and clears the counts, so each report reflects issues
 * since the last report rather than an ever-growing lifetime total.
 */
public class CommandIssueTracker {

    private final Map<String, AtomicInteger> counts = new ConcurrentHashMap<>();

    public void record(String issueType) {
        counts.computeIfAbsent(issueType, k -> new AtomicInteger()).incrementAndGet();
    }

    public Map<String, Integer> getAndReset() {
        Map<String, Integer> snapshot = new HashMap<>();
        counts.forEach((type, counter) -> {
            int value = counter.getAndSet(0);
            if (value > 0) snapshot.put(type, value);
        });
        return snapshot;
    }
}
