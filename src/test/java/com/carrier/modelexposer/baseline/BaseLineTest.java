package com.carrier.modelexposer.baseline;

import org.junit.jupiter.api.Test;
import org.openmarkov.core.exception.*;

import java.util.List;
import java.util.Map;

import static com.carrier.modelexposer.baseline.BaseLine.collectExampleBaseLinesEvidences;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BaseLineTest {
    @Test
    public void testCountCombinations()
            throws NodeNotFoundException, NotEvaluableNetworkException, IncompatibleEvidenceException,
                   InvalidStateException, UnexpectedInferenceException {
        {
            List<Map<String, String>> baseLineEvidences = collectExampleBaseLinesEvidences();
            assertEquals(baseLineEvidences.size(), 5);

            // Should contain the following values:
            //"ex-Smoker"
            //"Physical medium"
            //"Physical high"
            //"Nutrition medium"
            //"Nutrition high"

        }
    }
}
