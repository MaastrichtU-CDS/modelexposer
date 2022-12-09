package com.carrier.modelexposer.webservice;

import com.carrier.modelexposer.webservice.domain.*;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.carrier.modelexposer.baseline.BaseLine.collectExampleBaseLinesEvidences;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
            req.setInput(evidence);

            RiskResponse response = (RiskResponse) server.estimateBaseLineRisk(req);
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
            req.setInput(evidence);

            ExceptionResponse r = (ExceptionResponse) server.estimateBaseLineRisk(req);
            assertEquals(r.getMessage(), "Unknown attribute 'nonsense'");

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
            req.setInput(evidence);

            ExceptionResponse r = (ExceptionResponse) server.estimateBaseLineRisk(req);
            assertEquals(r.getMessage(),
                         "Unknown state 'nonsense' for attribute 'smoking_status', expected valid states: " +
                                 "'never_smoker', 'ex_smoker', 'current_smoker'");
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
            req.setInput(evidence);

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

            req.setChanges(changes);
            ReducedRiskResponse result = (ReducedRiskResponse) server.estimateReducedRisk(req);
            assertEquals(result.getChanges().size(), 5); // 1 original, 5 comparisons

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
    public void testPredefinedComparison()
            throws Exception {
        {
            String path = "resources/";
            String model = "model.pgmx";

            Server server = new Server("CVD", "yes", RiskRequest.ModelType.bayesian, path, model);

            Map<String, String> evidence = new HashMap<>();
            evidence.put("smoking_status", "current_smoker");


            ReducedRiskRequest req = new ReducedRiskRequest();
            req.setInput(evidence);
            req.setChanges(collectExampleBaseLinesEvidences());
            req.getChanges().get(0).putAll(req.getChanges().get(1));

            ReducedRiskResponse result = (ReducedRiskResponse) server.estimateReducedRisk(req);
            assertEquals(result.getChanges().size(), 5); // 1 original, 5 comparisons

            Map<String, Double> comparisons = new HashMap<>();
            for (Intervention c : result.getChanges()) {
                String name = "";
                for (String s : c.getChanged().keySet()) {
                    if (name.length() > 0) {
                        name += ", ";
                    }
                    name += s + " " + c.getChanged().get(s);
                }
                comparisons.put(name, c.getProbabilities().get("CVD"));
            }

            assertEquals(comparisons.get("smoking_status ex_smoker, nutrition_score medium"), 0.032, 0.01);
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