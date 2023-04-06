package com.carrier.modelexposer.classifier.openmarkov;

import com.carrier.modelexposer.exception.MissingAttributeException;
import com.carrier.modelexposer.exception.UnknownAttributeException;
import com.carrier.modelexposer.exception.UnknownStateException;
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
                   UnknownAttributeException, MissingAttributeException {
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

            Map<String, String> change1 = new HashMap<>();
            change1.put("smoking_status", "ex_smoker");

            ReducedRiskResponse result = classifier.compareClassifications(evidence, change1);

            Map<String, Double> comparisons = new HashMap<>();

            String name = "";
            for (String s : result.getChanges().getChanged().keySet()) {
                name += s + " " + result.getChanges().getChanged().get(s);
            }
            comparisons.put(name, result.getChanges().getProbabilities().get("CVD"));


            assertEquals(comparisons.get("smoking_status ex_smoker"), 0.041, 0.01);
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