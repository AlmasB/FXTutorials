package com.almasb.event;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Almas Baimagambetov (AlmasB) (almaslvl@gmail.com)
 */
public enum ServiceLocator {
    INSTANCE;

    private Map<Class<?>, Class<?>> services = new HashMap<>();
    private Map<Class<?>, Object> cache = new HashMap<>();

    public <T> void registerService(Class<T> service, Class<? extends T> provider) {
        services.put(service, provider);
    }

    public <T> T getService(Class<T> type) {
        Class<?> provider = services.get(type);

        try {
            Object instance;
            if (cache.containsKey(type)) {
                instance = cache.get(type);
            } else {
                instance = provider.getConstructor().newInstance();
                cache.put(type, instance);
            }

            return type.cast(instance);
        } catch (Exception e) {
            throw new IllegalArgumentException("Service is not available");
        }
    }
}
