package com.crystolnetwork.offices.services;

import com.crystolnetwork.offices.annotations.Singleton;
import com.crystolnetwork.offices.factory.ClassLifeFactory;
import com.crystolnetwork.offices.utils.exceptions.CrystolException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SingletonService {

    private final static Map<Class, Object> INSTANCES = new ConcurrentHashMap<>();
    private final static ClassLifeFactory CLASS_LIFE_FACTORY = new ClassLifeFactory();

    public static <T> T getOrFill(Class<T> clazz) {
        final Object seeked = INSTANCES.get(clazz);
        if (seeked != null) return (T) seeked;
        try {
            if (clazz.isAnnotationPresent(Singleton.class)) {
                final Singleton annotation = clazz.getDeclaredAnnotation(Singleton.class);
                final T instance = CLASS_LIFE_FACTORY.apply(clazz, annotation);
                INSTANCES.put(clazz, instance);
                return instance;
            } else {
                throw new CrystolException(
                        "The '" + clazz.getName() + "' class does not contain a required annotations."
                        , clazz);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public static Map<Class, Object> getInstances() {
        return INSTANCES;
    }

}
