package com.statementlabs.BackEndTest.application.exceptions;

public class NifAlreadyInUseException extends RuntimeException {
    public NifAlreadyInUseException(String message) {
        super(message);
    }
}
