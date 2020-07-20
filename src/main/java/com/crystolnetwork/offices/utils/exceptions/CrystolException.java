package com.crystolnetwork.offices.utils.exceptions;

public class CrystolException extends Exception {

    private final String exception;
    private final Class<?> classTarget;

    public CrystolException(final String exception, final Class<?> classTarget) {
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
