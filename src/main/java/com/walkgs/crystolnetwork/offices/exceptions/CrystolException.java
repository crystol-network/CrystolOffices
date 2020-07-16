package com.walkgs.crystolnetwork.offices.services.classlife.exception;

public class ClassLifeException extends Exception {

    private final String exception;
    private final Class<?> classTarget;

    public ClassLifeException(final String exception, final Class<?> classTarget) {
        super(exception);
        this.exception = exception;
        this.classTarget = classTarget;
    }

    public String getException() {
        return exception;
    }

    public Class<?> getClassTarget() {
        return classTarget;
    }

}
