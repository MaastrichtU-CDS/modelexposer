package com.carrier.modelexposer.exception;

public class UnknownAddressException extends Exception {
    public UnknownAddressException(String postcode, String number) {
        super("Unknown adress '" + postcode + " " + number + "'");
    }
}
