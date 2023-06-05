package com.carrier.modelexposer.exception;

public class ModelExposerException extends Exception {
    private static final String VERSION = "2.7";

    public ModelExposerException(String errormessage) {
        super("Encountered an error. \nCurrently running version: " + VERSION + "\nError: \n" + errormessage);
    }
}
