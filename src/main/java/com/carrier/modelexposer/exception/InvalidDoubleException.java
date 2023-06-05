package com.carrier.modelexposer.exception;

public class InvalidDoubleException extends ModelExposerException {
    public InvalidDoubleException(String attribute) {
        super("Attribute '" + attribute + "' is expected to be an double value");
    }
}
