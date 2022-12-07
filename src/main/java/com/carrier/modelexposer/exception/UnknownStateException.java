package com.carrier.modelexposer.exception;

public class UnknownStateException extends Exception {
    public UnknownStateException(String state, String attribute, String validStates) {
        super("Unknown state '" + state + "' for attribute '" + attribute + "', expected valid states: " + validStates);
    }
}
