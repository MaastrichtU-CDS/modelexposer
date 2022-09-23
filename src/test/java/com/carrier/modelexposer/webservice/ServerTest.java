package com.carrier.modelexposer.webservice;

import com.carrier.modelexposer.openmarkov.OpenMarkovClassifier;
import com.carrier.modelexposer.webservice.domain.ClassifyIndividualBayesianRequest;
import com.carrier.modelexposer.webservice.domain.ClassifyIndividualComparisonResponse;
import com.carrier.modelexposer.webservice.domain.ClassifyIndividualResponse;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.openmarkov.core.exception.*;
import org.openmarkov.core.model.network.ProbNet;
import org.openmarkov.io.probmodel.reader.PGMXReader_0_2;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ServerTest {

    @Test
    public void testClassifyTestCorrect()
            throws NodeNotFoundException, NotEvaluableNetworkException, IncompatibleEvidenceException,
                   InvalidStateException, UnexpectedInferenceException {
        {

            OpenMarkovClassifier classifier = new OpenMarkovClassifier(
                    "resources/", "BN-two-diseases.pgmx");
            //for the sake of test coverage include this one line.
            Server server = new Server();
            //now make the real test server
            server = new Server(classifier);

            Map<String, String> targets = new HashMap<>();
            targets.put("Disease 1", "present");
            targets.put("Disease 2", "present");
            Map<String, String> evidence = new HashMap<>();
            evidence.put("Symptom", "absent");

            ClassifyIndividualBayesianRequest req = new ClassifyIndividualBayesianRequest();
            req.setEvidence(evidence);
            req.setTargets(targets);


            ClassifyIndividualResponse response = server.estimateBaseLineRisk(req);
            assertEquals(response.getProbabilities().size(), 2);

            assertEquals(response.getProbabilities().get("Disease 1").get("present"), 0.00, 0.01);
            assertEquals(response.getProbabilities().get("Disease 2").get("present"), 0.00, 0.01);
        }
    }

    @Test
    public void testClassifyTestWrongNodeName()
            throws NodeNotFoundException, NotEvaluableNetworkException, IncompatibleEvidenceException,
                   InvalidStateException, UnexpectedInferenceException {
        {

            OpenMarkovClassifier classifier = new OpenMarkovClassifier(
                    "resources/", "BN-two-diseases.pgmx");
            Server server = new Server(classifier);

            Map<String, String> targets = new HashMap<>();
            targets.put("this is wrong", "and so is this");
            Map<String, String> evidence = new HashMap<>();
            evidence.put("Symptom", "absent");

            ClassifyIndividualBayesianRequest req = new ClassifyIndividualBayesianRequest();
            req.setEvidence(evidence);
            req.setTargets(targets);

            assertThrows(NodeNotFoundException.class, () -> {
                server.estimateBaseLineRisk(req);
            });

        }
    }

    @Test
    public void testClassifyTestWrongState()
            throws NodeNotFoundException, NotEvaluableNetworkException, IncompatibleEvidenceException,
                   InvalidStateException, UnexpectedInferenceException {
        {

            OpenMarkovClassifier classifier = new OpenMarkovClassifier(
                    "resources/", "BN-two-diseases.pgmx");
            Server server = new Server(classifier);

            Map<String, String> targets = new HashMap<>();
            targets.put("Disease 1", "present");
            Map<String, String> evidence = new HashMap<>();
            evidence.put("Symptom", "nonsense");

            ClassifyIndividualBayesianRequest req = new ClassifyIndividualBayesianRequest();
            req.setEvidence(evidence);
            req.setTargets(targets);

            assertThrows(InvalidStateException.class, () -> {
                server.estimateBaseLineRisk(req);
            });

        }
    }

    @Test
    public void testClassifyTestIncorrectTargetState()
            throws NodeNotFoundException, NotEvaluableNetworkException, IncompatibleEvidenceException,
                   InvalidStateException, UnexpectedInferenceException {
        {

            OpenMarkovClassifier classifier = new OpenMarkovClassifier(
                    "resources/", "BN-two-diseases.pgmx");
            Server server = new Server(classifier);

            Map<String, String> targets = new HashMap<>();
            targets.put("Disease 1", "nonsense");
            Map<String, String> evidence = new HashMap<>();
            evidence.put("Symptom", "present");

            ClassifyIndividualBayesianRequest req = new ClassifyIndividualBayesianRequest();
            req.setEvidence(evidence);
            req.setTargets(targets);


            ClassifyIndividualResponse response = server.estimateBaseLineRisk(req);
            assertEquals(response.getProbabilities().size(), 1);
            assertEquals(response.getProbabilities().get("Disease 1").size(), 0);

        }
    }


    @Test
    public void testGetBayesianModel()
            throws NodeNotFoundException, NotEvaluableNetworkException, IncompatibleEvidenceException,
                   InvalidStateException, UnexpectedInferenceException, IOException, ParserException {
        {
            String path = "resources/";
            String model = "BN-two-diseases.pgmx";
            OpenMarkovClassifier classifier = new OpenMarkovClassifier(
                    path, model);
            Server server = new Server(classifier);
            String output = server.getBayesianModel();
            String expected = expectedModel(path + model);

            assertEquals(output, expected);

            PGMXReader_0_2 pgmxReader = new PGMXReader_0_2();
            InputStream targetStream = new ByteArrayInputStream(output.getBytes());
            ProbNet network = pgmxReader.loadProbNet(model, targetStream);

            assertEquals(classifier.getNetwork().toString(), network.toString());
        }
    }

    @Test
    public void testComparison()
            throws NodeNotFoundException, NotEvaluableNetworkException, IncompatibleEvidenceException,
                   InvalidStateException, UnexpectedInferenceException, IOException, ParserException {
        {
            String path = "resources/";
            String model = "model.pgmx";
            OpenMarkovClassifier classifier = new OpenMarkovClassifier(
                    path, model);
            Server server = new Server(classifier);

            Map<String, String> targets = new HashMap<>();
            targets.put("CVD", "yes");
            Map<String, String> evidence = new HashMap<>();
            evidence.put("smoking_status", "current_smoker");


            ClassifyIndividualBayesianRequest req = new ClassifyIndividualBayesianRequest();
            req.setEvidence(evidence);
            req.setTargets(targets);
        
            ClassifyIndividualComparisonResponse result = server.classifyIndividualWithComparisons(req);
            assertEquals(result.getComparisons().size(), 5); // 1 original, 5 comparisons
        }
    }

    private String expectedModel(String path) throws IOException {
        FileInputStream fis = new FileInputStream(path);
        return IOUtils.toString(fis, "UTF-8");
    }
}