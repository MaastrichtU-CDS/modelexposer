package com.carrier.modelexposer.util;

import com.carrier.modelexposer.exception.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SESWOAMapperTest {
    @Test
    public void testUtil()
            throws InvalidIntegerException, InvalidDoubleException,
                   MissingAttributeException, UnknownStateException, UnknownAddressException {
        {
            List<String> paths = new ArrayList<>();
            paths.add("resources/seswoa_1.csv");
            paths.add("resources/seswoa_2.csv");
            SESWOAMapper m = new SESWOAMapper(paths);


            m.initSESWOA();
            assertEquals(m.findSESWOA("9712EA", "9"), -0.616);
            assertEquals(m.findSESWOA("9712EA", "wrong"), null);
            assertThrows(UnknownAddressException.class, () -> m.findSESWOA("1234AA", "1"), "Unknown adress 12344AA 1");
        }
    }
}