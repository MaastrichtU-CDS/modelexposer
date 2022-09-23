package com.carrier.modelexposer.openmarkov;

import com.carrier.modelexposer.webservice.domain.ClassifyIndividualComparisonResponse;
import com.carrier.modelexposer.webservice.domain.ClassifyIndividualResponse;
import org.junit.jupiter.api.Test;
import org.openmarkov.core.exception.*;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OpenMarkovClassifierTest {

    @Test
    public void testClassifyTestFiniteStateVariables()
            throws NodeNotFoundException, NotEvaluableNetworkException, IncompatibleEvidenceException,
                   InvalidStateException, UnexpectedInferenceException {
        {
            OpenMarkovClassifier classifier = new OpenMarkovClassifier(
                    "resources/", "BN-two-diseases.pgmx");
            Map<String, String> targets = new HashMap<>();
            targets.put("Disease 1", "present");
            targets.put("Disease 2", "present");
            Map<String, String> evidence = new HashMap<>();
            evidence.put("Symptom", "absent");
            ClassifyIndividualResponse response = classifier.classify(evidence, targets);
            assertEquals(response.getProbabilities().size(), 2);

            assertEquals(response.getProbabilities().get("Disease 1").get("present"), 0.00, 0.01);
            assertEquals(response.getProbabilities().get("Disease 2").get("present"), 0.00, 0.01);
        }
    }

    @Test
    public void testClassifyTestSanaNetExample()
            throws NodeNotFoundException, NotEvaluableNetworkException, IncompatibleEvidenceException,
                   InvalidStateException, UnexpectedInferenceException {
        {
            OpenMarkovClassifier classifier = new OpenMarkovClassifier(
                    "resources/", "20211222-Model.pgmx");
            Map<String, String> targets = new HashMap<>();
            targets.put("CVD", "yes");
            Map<String, String> evidence = new HashMap<>();
            evidence.put("age", "60_70");

            ClassifyIndividualResponse response = classifier.classify(evidence, targets);
            assertEquals(response.getProbabilities().size(), 1);
            assertEquals(response.getProbabilities().get("CVD").get("yes"), 0.034, 0.001);

        }
    }


    @Test
    public void testClassifyTestDiscreteVariables()
            throws NodeNotFoundException, NotEvaluableNetworkException, IncompatibleEvidenceException,
                   InvalidStateException, UnexpectedInferenceException {
        {
            OpenMarkovClassifier classifier = new OpenMarkovClassifier(
                    "resources/", "numericExample.pgmx");
            Map<String, String> targets = new HashMap<>();
            targets.put("Discrete Child", "Negative");
            Map<String, String> evidence = new HashMap<>();
            evidence.put("Discrete Parent", "1");
            ClassifyIndividualResponse response = classifier.classify(evidence, targets);
            assertEquals(response.getProbabilities().size(), 1);

            assertEquals(response.getProbabilities().get("Discrete Child").get("Negative"), 0.5, 0.01);

        }
    }

    @Test
    public void testComparisons()
            throws NodeNotFoundException, NotEvaluableNetworkException, IncompatibleEvidenceException,
                   InvalidStateException, UnexpectedInferenceException {
        {
            OpenMarkovClassifier classifier = new OpenMarkovClassifier(
                    "resources/", "model.pgmx");
            Map<String, String> targets = new HashMap<>();
            targets.put("CVD", "yes");
            Map<String, String> evidence = new HashMap<>();
            evidence.put("smoking_status", "current_smoker");
            ClassifyIndividualComparisonResponse result = classifier.compareClassifications(evidence, targets);
            assertEquals(result.getComparisons().size(), 5); // 1 original, 5 comparisons
        }
    }
}