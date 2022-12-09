package com.carrier.modelexposer.classifier.openmarkov;

import com.carrier.modelexposer.exception.UnknownAttributeException;
import com.carrier.modelexposer.exception.UnknownStateException;
import com.carrier.modelexposer.webservice.domain.Intervention;
import com.carrier.modelexposer.webservice.domain.ReducedRiskResponse;
import com.carrier.modelexposer.webservice.domain.RiskResponse;
import org.junit.jupiter.api.Test;
import org.openmarkov.core.exception.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

            List<Map<String, String>> changes = new ArrayList<>();
            Map<String, String> change1 = new HashMap<>();
            change1.put("smoking_status", "ex_smoker");
            Map<String, String> change2 = new HashMap<>();
            change2.put("physical_activity_score", "medium");
            Map<String, String> change3 = new HashMap<>();
            change3.put("physical_activity_score", "high");
            Map<String, String> change4 = new HashMap<>();
            change4.put("nutrition_score", "medium");
            Map<String, String> change5 = new HashMap<>();
            change5.put("nutrition_score", "high");
            changes.add(change1);
            changes.add(change2);
            changes.add(change3);
            changes.add(change4);
            changes.add(change5);
            ReducedRiskResponse result = classifier.compareClassifications(evidence, changes);
            assertEquals(result.getChanges().size(), 5);// 1 original, 5 comparisons

            Map<String, Double> comparisons = new HashMap<>();
            for (Intervention c : result.getChanges()) {
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