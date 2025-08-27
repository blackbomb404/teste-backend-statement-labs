package com.statementlabs.BackEndTest.application.exceptions;

public class InvalidPayloadException extends RuntimeException {
    public InvalidPayloadException(String message) {
        super(message);
    }
}
