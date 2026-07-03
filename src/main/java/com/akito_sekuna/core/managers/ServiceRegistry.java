package com.akito_sekuna.core.managers;

import com.akito_sekuna.core.api.IServiceRegistry;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ServiceRegistry implements IServiceRegistry {

    private final Map<Class<?>, Object> services = new HashMap<>();

    @Override
    public <T> void register(Class<T> type, T implementation) {
        services.put(type, implementation);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Optional<T> get(Class<T> type) {
        return Optional.ofNullable((T) services.get(type));
    }

    @Override
    public <T> void unregister(Class<T> type) {
        services.remove(type);
    }

    public void clear() {
        services.clear();
    }
}
