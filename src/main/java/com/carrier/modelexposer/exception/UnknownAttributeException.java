package com.carrier.modelexposer.exception;

public class UnknownAttributeException extends ModelExposerException {
    public UnknownAttributeException(String attribute) {
        super("Unknown attribute '" + attribute + "'");
    }
}

