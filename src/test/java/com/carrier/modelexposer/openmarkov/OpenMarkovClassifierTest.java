package com.carrier.modelexposer.openmarkov;

import com.carrier.modelexposer.webservice.domain.Attribute;
import com.carrier.modelexposer.webservice.domain.ClassifyIndividualComparisonResponse;
import com.carrier.modelexposer.webservice.domain.ClassifyIndividualResponse;
import org.junit.jupiter.api.Test;
import org.openmarkov.core.exception.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
            List<String> targets = Arrays.asList("Disease 1", "Disease 2");
            Map<String, String> evidence = new HashMap<>();
            evidence.put("Symptom", "absent");
            ClassifyIndividualResponse response = classifier.classify(evidence, targets);
            assertEquals(response.getAttributes().size(), 2);

            //there is some randomness to the order so find the results manually
            Attribute disease1 = null;
            for (Attribute a : response.getAttributes()) {
                if (a.getName().equals("Disease 1")) {
                    disease1 = a;
                }
            }
            Attribute disease2 = null;
            for (Attribute a : response.getAttributes()) {
                if (a.getName().equals("Disease 2")) {
                    disease2 = a;
                }
            }

            assertEquals(disease1.getName(), "Disease 1");
            assertEquals(disease1.getProbabilities().size(), 2);
            assertEquals(disease1.getProbabilities().get("absent"), 0.99, 0.01);
            assertEquals(disease1.getProbabilities().get("present"), 0.00, 0.01);

            assertEquals(disease2.getName(), "Disease 2");
            assertEquals(disease2.getProbabilities().size(), 2);
            assertEquals(disease2.getProbabilities().get("absent"), 0.99, 0.01);
            assertEquals(disease2.getProbabilities().get("present"), 0.00, 0.01);
        }
    }

    @Test
    public void testClassifyTestSanaNetExample()
            throws NodeNotFoundException, NotEvaluableNetworkException, IncompatibleEvidenceException,
                   InvalidStateException, UnexpectedInferenceException {
        {
            OpenMarkovClassifier classifier = new OpenMarkovClassifier(
                    "resources/", "20211222-Model.pgmx");
            List<String> targets = Arrays.asList("CVD_risk");
            Map<String, String> evidence = new HashMap<>();
            evidence.put("age", "60_70");
            ClassifyIndividualResponse response = classifier.classify(evidence, targets);
            assertEquals(response.getAttributes().size(), 1);


        }
    }


    @Test
    public void testClassifyTestDiscreteVariables()
            throws NodeNotFoundException, NotEvaluableNetworkException, IncompatibleEvidenceException,
                   InvalidStateException, UnexpectedInferenceException {
        {
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

    @Test
    public void testComparisons()
            throws NodeNotFoundException, NotEvaluableNetworkException, IncompatibleEvidenceException,
                   InvalidStateException, UnexpectedInferenceException {
        {
            OpenMarkovClassifier classifier = new OpenMarkovClassifier(
                    "resources/", "model.pgmx");
            List<String> targets = Arrays.asList("CVD_risk");
            Map<String, String> evidence = new HashMap<>();
            evidence.put("smoking_status", "current_smoker");
            ClassifyIndividualComparisonResponse result = classifier.compareClassifications(evidence, targets);
            assertEquals(result.getComparisons().size(), 5); // 1 original, 5 comparisons
        }
    }
}