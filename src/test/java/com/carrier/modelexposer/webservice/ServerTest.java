package com.carrier.modelexposer.webservice;

import com.carrier.modelexposer.openmarkov.OpenMarkovClassifier;
import com.carrier.modelexposer.webservice.domain.Attribute;
import com.carrier.modelexposer.webservice.domain.ClassifyIndividualComparisonResponse;
import com.carrier.modelexposer.webservice.domain.ClassifyIndividualRequest;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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

            List<String> targets = Arrays.asList("Disease 1", "Disease 2");
            Map<String, String> evidence = new HashMap<>();
            evidence.put("Symptom", "absent");

            ClassifyIndividualRequest req = new ClassifyIndividualRequest();
            req.setEvidence(evidence);
            req.setTargets(targets);


            ClassifyIndividualResponse response = server.classifyIndividualBayesian(req);
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
    public void testClassifyTestWrongNodeName()
            throws NodeNotFoundException, NotEvaluableNetworkException, IncompatibleEvidenceException,
                   InvalidStateException, UnexpectedInferenceException {
        {

            OpenMarkovClassifier classifier = new OpenMarkovClassifier(
                    "resources/", "BN-two-diseases.pgmx");
            Server server = new Server(classifier);

            List<String> targets = Arrays.asList("this is wrong");
            Map<String, String> evidence = new HashMap<>();
            evidence.put("Symptom", "absent");

            ClassifyIndividualRequest req = new ClassifyIndividualRequest();
            req.setEvidence(evidence);
            req.setTargets(targets);

            assertThrows(NodeNotFoundException.class, () -> {
                server.classifyIndividualBayesian(req);
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

            List<String> targets = Arrays.asList("Disease 1");
            Map<String, String> evidence = new HashMap<>();
            evidence.put("Symptom", "nonsense");

            ClassifyIndividualRequest req = new ClassifyIndividualRequest();
            req.setEvidence(evidence);
            req.setTargets(targets);

            assertThrows(InvalidStateException.class, () -> {
                server.classifyIndividualBayesian(req);
            });

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

            List<String> targets = Arrays.asList("CVD_risk");
            Map<String, String> evidence = new HashMap<>();
            evidence.put("smoking_status", "current_smoker");

            ClassifyIndividualRequest req = new ClassifyIndividualRequest();
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