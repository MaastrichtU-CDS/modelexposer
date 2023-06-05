package com.carrier.modelexposer.exception;

public class MissingAttributeException extends ModelExposerException {
    public MissingAttributeException(String attribute) {
        super("Missing attribute '" + attribute + "' is expected to be present");
    }
}
