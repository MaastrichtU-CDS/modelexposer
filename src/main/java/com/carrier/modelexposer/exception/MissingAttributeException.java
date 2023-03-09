package com.carrier.modelexposer.exception;

public class MissingAttributeException extends Exception {
    public MissingAttributeException(String attribute) {
        super("Missing attribute '" + attribute + "' is expected to be present");
    }
}
