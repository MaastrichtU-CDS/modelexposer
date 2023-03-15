package com.carrier.modelexposer.util;

import com.carrier.modelexposer.exception.InvalidDoubleException;
import com.carrier.modelexposer.exception.InvalidIntegerException;
import com.carrier.modelexposer.exception.MissingAttributeException;
import com.carrier.modelexposer.exception.UnknownStateException;

import java.util.Map;

public final class Util {

    private Util() {
    }

    public static String getStringValue(Map<String, String> evidence, String key) throws MissingAttributeException {
        if (evidence.get(key) == null) {
            throw new MissingAttributeException(key);
        } else {
            return evidence.get(key);
        }
    }

    public static Boolean getBooleanFromYesNoValue(Map<String, String> evidence, String key)
            throws MissingAttributeException, UnknownStateException {
        if (evidence.get(key) == null) {
            throw new MissingAttributeException(key);
        } else {
            if (!(evidence.get(key).equals("yes") || evidence.get(key).equals("no"))) {
                throw new UnknownStateException(evidence.get(key), key, "'yes', 'no'");
            }
            return evidence.get(key).equals("yes");
        }
    }

    public static Double getDoubleValue(Map<String, String> evidence, String key)
            throws MissingAttributeException, InvalidDoubleException {
        if (evidence.get(key) == null) {
            throw new MissingAttributeException(key);
        } else {
            try {
                return Double.parseDouble(evidence.get(key));
            } catch (NumberFormatException e) {
                throw new InvalidDoubleException(key);
            }
        }
    }

    public static Integer getIntValue(Map<String, String> evidence, String key)
            throws MissingAttributeException, InvalidIntegerException {
        if (evidence.get(key) == null) {
            throw new MissingAttributeException(key);
        } else {
            try {
                return Integer.parseInt(evidence.get(key));
            } catch (NumberFormatException e) {
                throw new InvalidIntegerException(key);
            }
        }
    }
}
