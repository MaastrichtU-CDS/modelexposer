package com.carrier.modelexposer.exception;

public class UnknownAttributeException extends Exception {
    public UnknownAttributeException(String attribute) {
        super("Unknown attribute '" + attribute + "'");
    }
}

