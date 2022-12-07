package com.carrier.modelexposer.exception;

public class UnknownStateException extends Exception {
    public UnknownStateException(String state, String attribute) {
        super("Unknown state '" + state + "' for attribute '" + attribute + "'");
    }
}
