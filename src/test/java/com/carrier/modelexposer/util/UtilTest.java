package com.carrier.modelexposer.util;

import com.carrier.modelexposer.exception.InvalidDoubleException;
import com.carrier.modelexposer.exception.InvalidIntegerException;
import com.carrier.modelexposer.exception.MissingAttributeException;
import com.carrier.modelexposer.exception.UnknownStateException;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static com.carrier.modelexposer.util.Util.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UtilTest {
    @Test
    public void testUtil()
            throws InvalidIntegerException, InvalidDoubleException,
                   MissingAttributeException, UnknownStateException {
        {
            //making test Coverage happy
            Map<String, String> evidence = new HashMap<>();
            evidence.put("wrongInt", "wrong");
            getOptionalBooleanValue(evidence, "optional");
            getOptionalStringValue(evidence, "optional");
            assertThrows(InvalidIntegerException.class, () -> getIntValue(evidence, "wrongInt"), "");
            assertThrows(MissingAttributeException.class, () -> getIntValue(evidence, "this_doesnt_exist"), "");
        }
    }
}