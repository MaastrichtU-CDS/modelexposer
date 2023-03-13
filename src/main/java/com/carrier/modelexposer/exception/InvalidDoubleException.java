package com.carrier.modelexposer.exception;

public class InvalidDoubleException extends Exception {
    public InvalidDoubleException(String attribute) {
        super("Attribute '" + attribute + "' is expected to be an double value");
    }
}
