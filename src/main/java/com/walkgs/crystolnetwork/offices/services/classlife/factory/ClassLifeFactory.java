package com.walkgs.crystolnetwork.offices.services.classlife.factory;

import com.walkgs.crystolnetwork.offices.services.classlife.annotation.ClassLife;
import com.walkgs.crystolnetwork.offices.services.classlife.annotation.supplier.LifeType;
import com.walkgs.crystolnetwork.offices.services.classlife.exception.ClassLifeException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ClassLifeFactory {

    public final <T> T apply(Class<T> clazz, ClassLife annotation) throws ClassLifeException {
        final LifeType supplier = annotation.supplier();
        try {
            if (supplier == LifeType.SYNC)
                return sync(clazz);
            return make(clazz);
        } catch (Exception exception){
            throw new ClassLifeException("It was not possible to instantiate the '" + clazz.getName() + "' class in " + supplier.name() + " mode.", clazz);
        }
    }

    private synchronized <T> T sync(Class<T> clazz) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        return make(clazz);
    }

    private <T> T make(Class<T> clazz) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        final Constructor<T> constructor = (Constructor<T>) clazz.getConstructors()[0];
        constructor.setAccessible(true);
        return constructor.newInstance();
    }

}
