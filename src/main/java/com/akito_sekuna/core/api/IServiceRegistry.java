package com.akito_sekuna.core.api;

import java.util.Optional;

public interface IServiceRegistry {
    <T> void register(Class<T> type, T implementation);
    <T> Optional<T> get(Class<T> type);
    <T> void unregister(Class<T> type);
}
