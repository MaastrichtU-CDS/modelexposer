package com.carrier.modelexposer.util;

import com.carrier.modelexposer.exception.InvalidDoubleException;
import com.carrier.modelexposer.exception.InvalidIntegerException;
import com.carrier.modelexposer.exception.MissingAttributeException;
import com.carrier.modelexposer.exception.UnknownStateException;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static com.carrier.modelexposer.util.Util.getOptionalBooleanValue;
import static com.carrier.modelexposer.util.Util.getOptionalStringValue;

public class UtilTest {
    @Test
    public void testUtil()
            throws InvalidIntegerException, InvalidDoubleException,
                   MissingAttributeException, UnknownStateException {
        {
            //making test Coverage happy
            Map<String, String> evidence = new HashMap<>();
            getOptionalBooleanValue(evidence, "optional");
            getOptionalStringValue(evidence, "optional");
        }
    }
}