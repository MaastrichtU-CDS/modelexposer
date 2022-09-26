package com.carrier.modelexposer.webservice;

import com.carrier.modelexposer.webservice.domain.*;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.openmarkov.core.exception.InvalidStateException;
import org.openmarkov.core.exception.NodeNotFoundException;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ServerTest {

    @Test
    public void testClassifyTestCorrect()
            throws Exception {
        {
            String path = "resources/";
            String model = "model.pgmx";

            Server server = new Server("CVD", "yes", RiskRequest.ModelType.bayesian, path, model);


            Map<String, String> evidence = new HashMap<>();
            evidence.put("smoking_status", "current_smoker");

            ReducedRiskRequest req = new ReducedRiskRequest();
            req.setEvidence(evidence);

            RiskResponse response = server.estimateBaseLineRisk(req);
            assertEquals(response.getProbabilities().size(), 1);

            assertEquals(response.getProbabilities().get("CVD"), 0.078, 0.001);
        }
    }

    @Test
    public void testClassifyTestWrongNodeName()
            throws Exception {
        {

            String path = "resources/";
            String model = "model.pgmx";

            Server server = new Server("CVD", "yes", RiskRequest.ModelType.bayesian, path, model);


            Map<String, String> evidence = new HashMap<>();
            evidence.put("nonsense", "current_smoker");

            ReducedRiskRequest req = new ReducedRiskRequest();
            req.setEvidence(evidence);

            assertThrows(NodeNotFoundException.class, () -> {
                server.estimateBaseLineRisk(req);
            });

        }
    }

    @Test
    public void testClassifyTestWrongState()
            throws Exception {
        {
            String path = "resources/";
            String model = "model.pgmx";

            Server server = new Server("CVD", "yes", RiskRequest.ModelType.bayesian, path, model);


            Map<String, String> evidence = new HashMap<>();
            evidence.put("smoking_status", "nonsense");

            ReducedRiskRequest req = new ReducedRiskRequest();
            req.setEvidence(evidence);

            assertThrows(InvalidStateException.class, () -> {
                server.estimateBaseLineRisk(req);
            });

        }
    }

    @Test
    public void testComparison()
            throws Exception {
        {
            String path = "resources/";
            String model = "model.pgmx";

            Server server = new Server("CVD", "yes", RiskRequest.ModelType.bayesian, path, model);

            Map<String, String> evidence = new HashMap<>();
            evidence.put("smoking_status", "current_smoker");


            ReducedRiskRequest req = new ReducedRiskRequest();
            req.setEvidence(evidence);

            ReducedRiskResponse result = server.estimateReducedRisk(req);
            assertEquals(result.getComparisons().size(), 5); // 1 original, 5 comparisons

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

    private String expectedModel(String path) throws IOException {
        FileInputStream fis = new FileInputStream(path);
        return IOUtils.toString(fis, "UTF-8");
    }
}