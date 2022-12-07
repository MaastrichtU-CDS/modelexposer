package com.carrier.modelexposer.classifier.openmarkov;

import com.carrier.modelexposer.exception.UnknownAttributeException;
import com.carrier.modelexposer.exception.UnknownStateException;
import com.carrier.modelexposer.webservice.domain.Comparison;
import com.carrier.modelexposer.webservice.domain.ReducedRiskResponse;
import com.carrier.modelexposer.webservice.domain.RiskResponse;
import org.junit.jupiter.api.Test;
import org.openmarkov.core.exception.*;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OpenMarkovClassifierTest {
    @Test
    public void testClassifyTestSanaNetExample()
            throws NodeNotFoundException, NotEvaluableNetworkException, IncompatibleEvidenceException,
                   InvalidStateException, UnexpectedInferenceException, UnknownStateException,
                   UnknownAttributeException {
        {
            OpenMarkovClassifier classifier = new OpenMarkovClassifier(
                    "resources/", "model.pgmx", "CVD", "yes");
            Map<String, String> evidence = new HashMap<>();
            evidence.put("age", "60_70");

            RiskResponse response = classifier.classify(evidence);
            assertEquals(response.getProbabilities().size(), 1);
            assertEquals(response.getProbabilities().get("CVD"), 0.034, 0.001);

        }
    }

    @Test
    public void testComparisons() throws Exception {
        {
            OpenMarkovClassifier classifier = new OpenMarkovClassifier(
                    "resources/", "model.pgmx", "CVD", "yes");
            Map<String, String> evidence = new HashMap<>();
            evidence.put("smoking_status", "current_smoker");
            ReducedRiskResponse result = classifier.compareClassifications(evidence);
            assertEquals(result.getComparisons().size(), 5);// 1 original, 5 comparisons

            Map<String, Double> comparisons = new HashMap<>();
            for (Comparison c : result.getComparisons()) {
                String name = "";
                for (String s : c.getChanged().keySet()) {
                    name += s + " " + c.getChanged().get(s);
                }
                comparisons.put(name, c.getProbabilities().get("CVD"));
            }

            assertEquals(comparisons.get("smoking_status ex_smoker"), 0.041, 0.01);
            assertEquals(comparisons.get("physical_activity_score medium"), 0.074, 0.01);
            assertEquals(comparisons.get("physical_activity_score high"), 0.074, 0.01);
            assertEquals(comparisons.get("nutrition_score high"), 0.053, 0.01);
            assertEquals(comparisons.get("nutrition_score medium"), 0.073, 0.01);
        }
    }

    @Test
    public void testDiscretizedModel() throws Exception {
        {
            OpenMarkovClassifier classifier = new OpenMarkovClassifier(
                    "resources/", "discretized.pgmx", "B", "present");
            Map<String, String> evidence = new HashMap<>();
            evidence.put("A", "99");
            RiskResponse result = classifier.classify(evidence);

            assertEquals(result.getProbabilities().get("B"), 0.9, 0.01);

            evidence.put("A", "101");
            RiskResponse result2 = classifier.classify(evidence);
            assertEquals(result2.getProbabilities().get("B"), 0.1, 0.01);
        }
    }
}