package com.carrier.modelexposer.util;

import com.carrier.modelexposer.exception.InvalidDoubleException;
import com.carrier.modelexposer.exception.InvalidIntegerException;
import com.carrier.modelexposer.exception.MissingAttributeException;
import com.carrier.modelexposer.exception.UnknownStateException;

import java.util.Map;

public final class Util {

    private Util() {
    }

    public static Boolean checkForNull(Map<String, String> evidence, String key) throws MissingAttributeException {
        return evidence.get(key) != null && evidence.get(key).toLowerCase().equals("null");
    }

    public static String getOptionalStringValue(Map<String, String> evidence, String key) {
        try {
            if (!checkForNull(evidence, key)) {
                return getStringValue(evidence, key);
            } else {
                return null;
            }
        } catch (MissingAttributeException e) {
            return null;
        }
    }

    public static String getStringValue(Map<String, String> evidence, String key) throws MissingAttributeException {
        if (evidence.get(key) == null || checkForNull(evidence, key)) {
            throw new MissingAttributeException(key);
        } else {
            return evidence.get(key);
        }
    }

    public static Boolean getOptionalBooleanValue(Map<String, String> evidence, String key)
            throws UnknownStateException {
        try {
            if (!checkForNull(evidence, key)) {
                return getBooleanFromYesNoValue(evidence, key);
            } else {
                return null;
            }
        } catch (MissingAttributeException e) {
            return null;
        }
    }

    public static Boolean getBooleanFromYesNoValue(Map<String, String> evidence, String key)
            throws MissingAttributeException, UnknownStateException {
        if (evidence.get(key) == null) {
            throw new MissingAttributeException(key);
        } else {
            if (!checkForNull(evidence, key)) {
                if (!(evidence.get(key).equals("yes") || evidence.get(key).equals("no"))) {
                    throw new UnknownStateException(evidence.get(key), key, "'yes', 'no'");
                }
            }
            return evidence.get(key).equals("yes");
        }
    }

    public static Double getOptionalDoubleValue(Map<String, String> evidence, String key)
            throws InvalidDoubleException {
        try {
            if (!checkForNull(evidence, key)) {
                return getDoubleValue(evidence, key);
            } else {
                return null;
            }
        } catch (MissingAttributeException e) {
            return null;
        }
    }

    public static Double getDoubleValue(Map<String, String> evidence, String key)
            throws MissingAttributeException, InvalidDoubleException {
        if (evidence.get(key) == null || checkForNull(evidence, key)) {
            throw new MissingAttributeException(key);
        } else {
            try {
                return Double.parseDouble(evidence.get(key));
            } catch (NumberFormatException e) {
                throw new InvalidDoubleException(key);
            }
        }
    }

    public static Integer getOptionalIntValue(Map<String, String> evidence, String key)
            throws InvalidIntegerException {
        try {
            if (!checkForNull(evidence, key)) {
                return getIntValue(evidence, key);
            } else {
                return null;
            }
        } catch (MissingAttributeException e) {
            return null;
        }
    }

    public static Integer getIntValue(Map<String, String> evidence, String key)
            throws MissingAttributeException, InvalidIntegerException {
        if (evidence.get(key) == null || checkForNull(evidence, key)) {
            throw new MissingAttributeException(key);
        } else {
            try {
                return Integer.parseInt(evidence.get(key));
            } catch (NumberFormatException e) {
                throw new InvalidIntegerException(key);
            }
        }
    }

    public static Map<String, String> removeValue(Map<String, String> input, String key) {
        if (input.containsKey(key)) {
            input.remove(key);
        }
        return input;
    }
}
