package com.carrier.modelexposer.baseline;

import org.junit.jupiter.api.Test;
import org.openmarkov.core.exception.*;

import java.util.Map;

import static com.carrier.modelexposer.baseline.BaseLine.collectExampleBaseLinesEvidences;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BaseLineTest {
    @Test
    public void testCountCombinations()
            throws NodeNotFoundException, NotEvaluableNetworkException, IncompatibleEvidenceException,
                   InvalidStateException, UnexpectedInferenceException {
        {
            Map<String, Map<String, String>> baseLineEvidences = collectExampleBaseLinesEvidences();
            assertEquals(baseLineEvidences.size(), 17);

            // Should contain the following combos:
            // 1 value combo:
            //"ex-Smoker"
            //"Physical medium"
            //"Physical high"
            //"Nutrition medium"
            //"Nutrition high"

            //combo's containing 2 values:
            //"ex-Smoker Physical medium"
            //"ex-Smoker Physical high"
            //"ex-Smoker Nutrition medium"
            //"ex-Smoker Nutrition high"

            //"Nutrition medium Physical medium"
            //"Nutrition high Physical high"
            //"Nutrition medium Physical high"
            //"Nutrition high Physical medium"

            //combo's containing 3 values:
            //"ex-Smoker Nutrition medium Physical medium"
            //"ex-Smoker Nutrition high Physical high"
            //"ex-Smoker Nutrition medium Physical high"
            //"ex-Smoker Nutrition high Physical medium"

        }
    }
}
