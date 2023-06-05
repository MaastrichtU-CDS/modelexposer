package com.carrier.modelexposer.exception;

public class InvalidIntegerException extends ModelExposerException {
    public InvalidIntegerException(String attribute) {
        super("Attribute '" + attribute + "' is expected to be an integer value");
    }
}
