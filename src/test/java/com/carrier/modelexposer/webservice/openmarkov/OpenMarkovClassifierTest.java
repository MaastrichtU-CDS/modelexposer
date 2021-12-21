package com.carrier.modelexposer.webservice.openmarkov;

import com.carrier.modelexposer.webservice.domain.ClassifyIndividualResponse;
import org.junit.jupiter.api.Test;
import org.openmarkov.core.exception.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OpenMarkovClassifierTest {

    @Test
    public void classifyTestDiscreteVariables()
            throws NodeNotFoundException, NotEvaluableNetworkException, IncompatibleEvidenceException,
                   InvalidStateException, UnexpectedInferenceException {
        OpenMarkovClassifier classifier = new OpenMarkovClassifier(
                "resources/", "BN-two-diseases.pgmx");
        List<String> targets = Arrays.asList("Disease 1", "Disease 2");
        Map<String, String> evidence = new HashMap<>();
        evidence.put("Symptom", "absent");
        ClassifyIndividualResponse response = classifier.classify(evidence, targets);
        assertEquals(response.getAttributes().size(), 2);

        assertEquals(response.getAttributes().get(0).getName(), "Disease 2");
        assertEquals(response.getAttributes().get(0).getProbabilities().size(), 2);
        assertEquals(response.getAttributes().get(0).getProbabilities().get("absent"), 0.99, 0.01);
        assertEquals(response.getAttributes().get(0).getProbabilities().get("present"), 0.00, 0.01);

        assertEquals(response.getAttributes().get(1).getName(), "Disease 1");
        assertEquals(response.getAttributes().get(1).getProbabilities().size(), 2);
        assertEquals(response.getAttributes().get(1).getProbabilities().get("absent"), 0.99, 0.01);
        assertEquals(response.getAttributes().get(1).getProbabilities().get("present"), 0.00, 0.01);
    }

    @Test
    public void classifyTestNumericVariables()
            throws NodeNotFoundException, NotEvaluableNetworkException, IncompatibleEvidenceException,
                   InvalidStateException, UnexpectedInferenceException {
        OpenMarkovClassifier classifier = new OpenMarkovClassifier(
                "resources/", "numericExample.pgmx");
        List<String> targets = Arrays.asList("Discrete Child");
        Map<String, String> evidence = new HashMap<>();
        evidence.put("Discrete Parent", "1");
        ClassifyIndividualResponse response = classifier.classify(evidence, targets);
        assertEquals(response.getAttributes().size(), 1);

        assertEquals(response.getAttributes().get(0).getName(), "Discrete Child");
        assertEquals(response.getAttributes().get(0).getProbabilities().size(), 2);
        assertEquals(response.getAttributes().get(0).getProbabilities().get("Negative"), 0.5, 0.01);
        assertEquals(response.getAttributes().get(0).getProbabilities().get("Positive"), 0.5, 0.01);

    }
}