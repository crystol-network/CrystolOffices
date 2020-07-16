package com.walkgs.crystolnetwork.offices.services.classlife;

import com.walkgs.crystolnetwork.offices.services.classlife.annotation.ClassLife;
import com.walkgs.crystolnetwork.offices.services.classlife.exception.ClassLifeException;
import com.walkgs.crystolnetwork.offices.services.classlife.factory.ClassLifeFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Singleton {

    private final static Map<Class, Object> INSTANCES = new ConcurrentHashMap<>();
    private final static ClassLifeFactory CLASS_LIFE_FACTORY = new ClassLifeFactory();

    public static <T> T getOrFill (Class<T> clazz){
        final Object seeked = INSTANCES.get(clazz);
        if (seeked != null) return (T) seeked;
        try {
            if(clazz.isAnnotationPresent(ClassLife.class)){
                final ClassLife annotation = clazz.getDeclaredAnnotation(ClassLife.class);
                final T instance = CLASS_LIFE_FACTORY.apply(clazz, annotation);
                INSTANCES.put(clazz, instance);
                return instance;
            } else {
                throw new ClassLifeException(
                        "The '" + clazz.getName() + "' class does not contain a required annotation."
                        , clazz);
            }
        } catch (Exception exception){
            exception.printStackTrace();
        }
        return null;
    }

    public static Map<Class, Object> getInstances(){
        return INSTANCES;
    }

}
