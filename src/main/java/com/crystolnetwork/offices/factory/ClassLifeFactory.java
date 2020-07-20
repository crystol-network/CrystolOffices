package com.crystolnetwork.offices.factory;

import com.crystolnetwork.offices.annotations.Singleton;
import com.crystolnetwork.offices.annotations.supplier.LifeType;
import com.crystolnetwork.offices.utils.exceptions.CrystolException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public final class ClassLifeFactory {

    public final <T> T apply(Class<T> clazz, Singleton annotation) throws CrystolException {
        final LifeType supplier = annotation.supplier();
        try {
            if (supplier == LifeType.SYNC)
                return sync(clazz);
            return make(clazz);
        } catch (Exception exception) {
            throw new CrystolException("It was not possible to instantiate the '" + clazz.getName() + "' class in " + supplier.name() + " mode.", clazz);
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
