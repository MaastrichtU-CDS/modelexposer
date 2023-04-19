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
                return false;
            }
        } catch (MissingAttributeException e) {
            return false;
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

    @SuppressWarnings ("checkstyle:magicNumber")
    public static double calcChampScore(Map<String, String> evidence)
            throws InvalidDoubleException, UnknownStateException {
        Double cHAMPSMVPAScore = getOptionalDoubleValue(evidence, "CHAMPS_MVPA_score");

        if (cHAMPSMVPAScore == null) {
            cHAMPSMVPAScore = 0.0;
            cHAMPSMVPAScore += getChampsValueFromRange(evidence, "CHAMPS_MVPA_Q1");
            cHAMPSMVPAScore += getChampsValueFromRange(evidence, "CHAMPS_MVPA_Q2");
            cHAMPSMVPAScore += getChampsValueFromRange(evidence, "CHAMPS_MVPA_Q3");
            cHAMPSMVPAScore += getChampsValueFromRange(evidence, "CHAMPS_MVPA_Q4");
            cHAMPSMVPAScore += getChampsValueFromRange(evidence, "CHAMPS_MVPA_Q5");
            cHAMPSMVPAScore += getChampsValueFromRange(evidence, "CHAMPS_MVPA_Q6");
            cHAMPSMVPAScore += getChampsValueFromRange(evidence, "CHAMPS_MVPA_Q7");
            cHAMPSMVPAScore += getChampsValueFromRange(evidence, "CHAMPS_MVPA_Q8");
            cHAMPSMVPAScore += getChampsValueFromRange(evidence, "CHAMPS_MVPA_Q9");
        }
        //transform to minutes
        cHAMPSMVPAScore *= 60;
        return cHAMPSMVPAScore;
    }

    @SuppressWarnings ("checkstyle:magicNumber")
    public static Double calcChampScoreIntervention(Map<String, String> evidence)
            throws InvalidDoubleException, UnknownStateException {
        String intervention = getOptionalStringValue(evidence, "intervention_exercise");
        if (intervention == null) {
            return null;
        }

        if (intervention.equals("<30")) {
            return 15.0;
        } else if (intervention.equals("30_90")) {
            return 60.0;
        } else if (intervention.equals("90_150")) {
            return 120.0;
        } else if (intervention.equals("150_300")) {
            return 225.0;
        } else if (intervention.equals(">300")) {
            return 350.0;
        } else {
            throw new UnknownStateException(intervention, "intervention_exercise",
                                            "'<30', '30_90', '90_150', '150_300', '>300'");
        }
    }

    @SuppressWarnings ("checkstyle:magicNumber")
    public static Integer calcDiet(Map<String, String> evidence)
            throws InvalidDoubleException, UnknownStateException {
        String diet = getOptionalStringValue(evidence, "intervention_diet");
        if (diet == null) {
            return null;
        }

        if (diet.equals("<60")) {
            return 30;
        } else if (diet.equals("60_70")) {
            return 65;
        } else if (diet.equals("70_80")) {
            return 75;
        } else if (diet.equals("80_90")) {
            return 85;
        } else if (diet.equals(">90")) {
            return 110;
        } else {
            throw new UnknownStateException(diet, "intervention_diet",
                                            "'<60', '60_70', '70_80', '80_90', '>90'");
        }
    }

    @SuppressWarnings ("checkstyle:magicNumber")
    private static double getChampsValueFromRange(Map<String, String> evidence, String key)
            throws UnknownStateException {
        String value = getOptionalStringValue(evidence, key);
        if (value == null) {
            return 0;
        }
        if (value.equals("0")) {
            return 0;
        } else if (value.equals("<0.5")) {
            return 0.25;
        }
        if (value.equals("0.5-1")) {
            return 0.75;
        }
        if (value.equals("1-2")) {
            return 1.5;
        }
        if (value.equals("2-4")) {
            return 3.0;
        }
        if (value.equals("4-6")) {
            return 5.0;
        }
        if (value.equals("6-8")) {
            return 7.0;
        }
        if (value.equals(">8")) {
            return 8.5;
        } else {
            throw new UnknownStateException(value, key, "'0', '<0.5', '0.5-1', '1-2', '2-4', '4-6', '6-8', '>8'");
        }
    }
}
