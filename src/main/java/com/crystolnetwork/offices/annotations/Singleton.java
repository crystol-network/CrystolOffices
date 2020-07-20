package com.crystolnetwork.offices.annotations;

import com.crystolnetwork.offices.annotations.supplier.LifeType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Singleton {

    LifeType supplier() default LifeType.NORMAL;

}
