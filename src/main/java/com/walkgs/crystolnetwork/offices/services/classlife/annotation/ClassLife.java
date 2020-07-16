package com.walkgs.crystolnetwork.offices.services.classlife.annotation;

import com.walkgs.crystolnetwork.offices.services.classlife.annotation.supplier.LifeType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ClassLife {

    LifeType supplier() default LifeType.NORMAL;

}
