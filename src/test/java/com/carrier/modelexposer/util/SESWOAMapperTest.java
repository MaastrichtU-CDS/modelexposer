package com.carrier.modelexposer.util;

import com.carrier.modelexposer.exception.InvalidDoubleException;
import com.carrier.modelexposer.exception.InvalidIntegerException;
import com.carrier.modelexposer.exception.MissingAttributeException;
import com.carrier.modelexposer.exception.UnknownStateException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SESWOAMapperTest {
    @Test
    public void testUtil()
            throws InvalidIntegerException, InvalidDoubleException,
                   MissingAttributeException, UnknownStateException {
        {
            SESWOAMapper m = new SESWOAMapper();

            assertEquals(m.findSESWOA("9712EA", "9"), -0.616);
            assertEquals(m.findSESWOA("9712EA", "wrong"), 0.096); // wrong so default value
        }
    }
}