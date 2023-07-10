package com.carrier.modelexposer.classifier.score2;

import com.carrier.modelexposer.exception.InvalidDoubleException;
import com.carrier.modelexposer.exception.InvalidIntegerException;
import com.carrier.modelexposer.exception.MissingAttributeException;
import com.carrier.modelexposer.exception.UnknownStateException;
import com.carrier.modelexposer.webservice.domain.ReducedRiskResponse;
import com.carrier.modelexposer.webservice.domain.RiskResponse;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class Score2ClassifierTest {
    @Test
    public void testClassifyTestSanaNetExample()
            throws Exception {
        {
            Map<String, String> evidence = new HashMap<>();
            evidence.put("gender", "male");
            evidence.put("age", "50");
            evidence.put("current_smoker", "yes");
            evidence.put("SBP", "140");
            evidence.put("TC", "6.3");
            evidence.put("HDL", "1.4");

            Score2Classifier classifier = new Score2Classifier();
            RiskResponse response = classifier.classify(evidence);
            assertEquals(response.getProbabilities().get("CVD"), 0.064, 0.01);

            Map<String, String> evidence2 = new HashMap<>();
            evidence2.put("gender", "female");
            evidence2.put("age", "50");
            evidence2.put("current_smoker", "yes");
            evidence2.put("SBP", "140");
            evidence2.put("TC", "6.3");
            evidence2.put("HDL", "1.4");

            response = classifier.classify(evidence2);
            assertEquals(response.getProbabilities().get("CVD"), 0.044, 0.001);

            ReducedRiskResponse reduced = classifier.compareClassifications(evidence, evidence2);
            assertEquals(reduced.getBaseline().getProbabilities().get("CVD"), 0.064, 0.001);
            assertEquals(reduced.getChanges().getProbabilities().get("CVD"), 0.044, 0.001);
        }
    }

    @Test
    public void testScore2Example()
            throws InvalidIntegerException, InvalidDoubleException,
                   MissingAttributeException, UnknownStateException {
        {
            Map<String, String> evidence = new HashMap<>();
            evidence.put("gender", "male");
            evidence.put("age", "50");
            evidence.put("current_smoker", "yes");
            evidence.put("SBP", "140");
            evidence.put("TC", "6.3");
            evidence.put("HDL", "1.4");

            Score2Classifier classifier = new Score2Classifier();
            double score = classifier.score2(evidence);
            assertEquals(score, 0.063, 0.001);

            Map<String, String> evidence2 = new HashMap<>();
            evidence2.put("gender", "female");
            evidence2.put("age", "50");
            evidence2.put("current_smoker", "yes");
            evidence2.put("SBP", "140");
            evidence2.put("TC", "6.3");
            evidence2.put("HDL", "1.4");

            score = classifier.score2(evidence2);
            assertEquals(score, 0.043, 0.001);
        }
    }

    @Test
    public void testClassifyTestSanaNetExampleNull()
            throws InvalidIntegerException, InvalidDoubleException,
                   MissingAttributeException, UnknownStateException {
        {
            Map<String, String> evidence = new HashMap<>();
            evidence.put("gender", "null");
            evidence.put("age", "50");
            evidence.put("current_smoker", "yes");
            evidence.put("SBP", "140");
            evidence.put("TC", "6.3");
            evidence.put("HDL", "1.4");

            Score2Classifier classifier = new Score2Classifier();
            assertThrows(MissingAttributeException.class, () -> classifier.classify(evidence),
                         "Missing attribute 'gender' is expected to be present");
        }
    }

}