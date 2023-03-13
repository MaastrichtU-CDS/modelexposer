package com.carrier.modelexposer.webservice;

import com.carrier.modelexposer.webservice.domain.*;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ServerTest {

    @Test
    public void testClassifyTestCorrect()
            throws Exception {
        {
            String path = "resources/";
            String model = "dummy_model_sananet.pgmx";

            Server server = new Server("CVD", "yes", RiskRequest.ModelType.bayesian, path, model);


            Map<String, String> evidence = new HashMap<>();
            evidence.put("current_smoker", "yes");

            ReducedRiskRequest req = new ReducedRiskRequest();
            req.setInput(evidence);

            RiskResponse response = (RiskResponse) server.estimateBaseLineRisk(req);
            assertEquals(response.getProbabilities().size(), 1);

            assertEquals(response.getProbabilities().get("CVD"), 0.085, 0.001);
        }
    }

    @Test
    public void testClassifyTestPackYearsConversion()
            throws Exception {
        {
            String path = "resources/";
            String model = "dummy_model_sananet.pgmx";

            Server server = new Server("CVD", "yes", RiskRequest.ModelType.bayesian, path, model);


            Map<String, String> evidence = new HashMap<>();

            evidence.put("current_smoker", "yes");
            evidence.put("current_smoker_substance", "cigarette");
            evidence.put("current_smoker_cigarette_years", "1");
            evidence.put("current_smoker_cigarette_number_per_day", "2");

            ReducedRiskRequest req = new ReducedRiskRequest();
            req.setInput(evidence);

            RiskResponse response = (RiskResponse) server.estimateBaseLineRisk(req);
            assertEquals(response.getProbabilities().size(), 1);

            Map<String, String> evidence2 = new HashMap<>();
            evidence2.put("current_smoker", "yes");
            evidence2.put("current_smoker_substance", "cigar");
            evidence2.put("current_smoker_cigar_years", "2");
            evidence2.put("current_smoker_cigar_number_per_week", "20");

            req.setInput(evidence2);

            RiskResponse response2 = (RiskResponse) server.estimateBaseLineRisk(req);
            assertEquals(response2.getProbabilities().size(), 1);

            Map<String, String> evidence3 = new HashMap<>();
            evidence3.put("current_smoker", "yes");
            evidence3.put("current_smoker_substance", "pipe");
            evidence3.put("current_smoker_pipe_years", "3");
            evidence3.put("current_smoker_pipe_number_per_week", "30");

            req.setInput(evidence3);

            RiskResponse response3 = (RiskResponse) server.estimateBaseLineRisk(req);
            assertEquals(response3.getProbabilities().size(), 1);

            Map<String, String> evidence4 = new HashMap<>();
            evidence4.put("current_smoker", "yes");
            evidence4.put("current_smoker_substance", "e-cigarette");
            evidence4.put("current_smoker_e-cigarette_years", "4");
            evidence4.put("current_smoker_e-cigarette_number_per_day", "40");

            req.setInput(evidence4);

            RiskResponse response4 = (RiskResponse) server.estimateBaseLineRisk(req);
            assertEquals(response4.getProbabilities().size(), 1);

            Map<String, String> evidence5 = new HashMap<>();

            evidence5.put("current_smoker", "no");
            evidence5.put("ex_smoker", "yes");
            evidence5.put("ex_smoker_substance", "cigarette");
            evidence5.put("ex_smoker_cigarette_years", "1");
            evidence5.put("ex_smoker_cigarette_number_per_day", "2");

            req.setInput(evidence5);

            RiskResponse response5 = (RiskResponse) server.estimateBaseLineRisk(req);
            assertEquals(response5.getProbabilities().size(), 1);

            Map<String, String> evidence6 = new HashMap<>();
            evidence6.put("current_smoker", "no");
            evidence6.put("ex_smoker", "yes");
            evidence6.put("ex_smoker_substance", "cigar");
            evidence6.put("ex_smoker_cigar_years", "6");
            evidence6.put("ex_smoker_cigar_number_per_week", "60");

            req.setInput(evidence6);

            RiskResponse response6 = (RiskResponse) server.estimateBaseLineRisk(req);
            assertEquals(response6.getProbabilities().size(), 1);

            Map<String, String> evidence7 = new HashMap<>();
            evidence7.put("current_smoker", "no");
            evidence7.put("ex_smoker", "yes");
            evidence7.put("ex_smoker_substance", "pipe");
            evidence7.put("ex_smoker_pipe_years", "7");
            evidence7.put("ex_smoker_pipe_number_per_week", "70");

            req.setInput(evidence7);

            RiskResponse response7 = (RiskResponse) server.estimateBaseLineRisk(req);
            assertEquals(response7.getProbabilities().size(), 1);

            Map<String, String> evidence8 = new HashMap<>();
            evidence8.put("current_smoker", "no");
            evidence8.put("ex_smoker", "yes");
            evidence8.put("ex_smoker_substance", "e-cigarette");
            evidence8.put("ex_smoker_e-cigarette_years", "8");
            evidence8.put("ex_smoker_e-cigarette_number_per_day", "80");

            req.setInput(evidence8);

            RiskResponse response8 = (RiskResponse) server.estimateBaseLineRisk(req);
            assertEquals(response8.getProbabilities().size(), 1);


            assertEquals(response.getProbabilities().get("CVD"), 0.085, 0.001);
            assertEquals(response2.getProbabilities().get("CVD"), 0.087, 0.001);
            assertEquals(response3.getProbabilities().get("CVD"), 0.087, 0.001);
            assertEquals(response4.getProbabilities().get("CVD"), 0.087, 0.001);
            assertEquals(response5.getProbabilities().get("CVD"), 0.085, 0.001);
            assertEquals(response6.getProbabilities().get("CVD"), 0.087, 0.001);
            assertEquals(response7.getProbabilities().get("CVD"), 0.087, 0.001);
            assertEquals(response8.getProbabilities().get("CVD"), 0.087, 0.001);
        }
    }

    @Test
    public void testClassifyTestPackYearsInvalidFormat()
            throws Exception {
        {
            String path = "resources/";
            String model = "dummy_model_sananet.pgmx";

            Server server = new Server("CVD", "yes", RiskRequest.ModelType.bayesian, path, model);


            Map<String, String> evidence = new HashMap<>();

            evidence.put("current_smoker", "yes");
            evidence.put("current_smoker_substance", "cigarette");


            ReducedRiskRequest req = new ReducedRiskRequest();
            req.setInput(evidence);

            ExceptionResponse r = (ExceptionResponse) server.estimateBaseLineRisk(req);
            assertEquals(r.getMessage(),
                         "Missing attribute 'current_smoker_cigarette_years' is expected to be present");

            evidence.put("current_smoker_cigarette_years", "fout");
            evidence.put("current_smoker_cigarette_number_per_day", "2");
            r = (ExceptionResponse) server.estimateBaseLineRisk(req);
            assertEquals(r.getMessage(),
                         "Attribute 'current_smoker_cigarette_years' is expected to be an integer value");

        }
    }

    @Test
    public void testClassifyTestWrongNodeName()
            throws Exception {
        {

            String path = "resources/";
            String model = "dummy_model_sananet.pgmx";

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
    public void testScore2Classifier()
            throws Exception {
        {
            String path = "resources/";
            String model = "model.pgmx";

            Server server = new Server("CVD", "yes", RiskRequest.ModelType.bayesian, path, model);


            Map<String, String> evidence = new HashMap<>();
            evidence.put("gender", "male");
            evidence.put("age", "50");
            evidence.put("current_smoker", "yes");
            evidence.put("SBP", "140");
            evidence.put("TC", "6.3");
            evidence.put("HDL", "1.4");

            RiskRequest req = new RiskRequest();
            req.setInput(evidence);
            req.setModelType(RiskRequest.ModelType.score2);

            RiskResponse r = (RiskResponse) server.estimateBaseLineRisk(req);
            assertEquals(r.getProbabilities().get("CVD"), 0.0631, 0.001);
        }
    }

    @Test
    public void testScore2ClassifierInvalidValues()
            throws Exception {
        {
            String path = "resources/";
            String model = "model.pgmx";

            Server server = new Server("CVD", "yes", RiskRequest.ModelType.bayesian, path, model);


            Map<String, String> evidence = new HashMap<>();
            evidence.put("gender", "nonsense");
            evidence.put("age", "50");
            evidence.put("current_smoker", "yes");
            evidence.put("SBP", "140");
            evidence.put("TC", "6.3");
            evidence.put("HDL", "1.4");

            RiskRequest req = new RiskRequest();
            req.setInput(evidence);
            req.setModelType(RiskRequest.ModelType.score2);

            ExceptionResponse r = (ExceptionResponse) server.estimateBaseLineRisk(req);
            assertEquals(r.getMessage(),
                         "Unknown state 'nonsense' for attribute 'gender', expected valid states: 'male', 'female'");

            evidence.put("gender", "male");
            evidence.put("current_smoker", "nonsense");

            r = (ExceptionResponse) server.estimateBaseLineRisk(req);
            assertEquals(r.getMessage(),
                         "Unknown state 'nonsense' for attribute 'current_smoker', expected valid states: 'yes', 'no'");

            evidence.put("current_smoker", "yes");
            evidence.put("SBP", "nonsense");

            r = (ExceptionResponse) server.estimateBaseLineRisk(req);
            assertEquals(r.getMessage(), "Attribute 'SBP' is expected to be an double value");

            evidence.remove("SBP");

            r = (ExceptionResponse) server.estimateBaseLineRisk(req);
            assertEquals(r.getMessage(), "Missing attribute 'SBP' is expected to be present");
        }
    }

    @Test
    public void testComparison()
            throws Exception {
        {
            String path = "resources/";
            String model = "dummy_model_sananet.pgmx";

            Server server = new Server("CVD", "yes", RiskRequest.ModelType.bayesian, path, model);

            Map<String, String> evidence = new HashMap<>();
            evidence.put("current_smoker", "yes");


            ReducedRiskRequest req = new ReducedRiskRequest();
            req.setInput(evidence);

            Map<String, String> change1 = new HashMap<>();
            change1.put("current_smoker", "no");
            req.setChanges(change1);

            ReducedRiskResponse result = (ReducedRiskResponse) server.estimateReducedRisk(req);

            Map<String, Double> comparisons = new HashMap<>();

            String name = "";
            for (String s : result.getChanges().getChanged().keySet()) {
                name += s + " " + result.getChanges().getChanged().get(s);
            }
            comparisons.put(name, result.getChanges().getProbabilities().get("CVD"));


            assertEquals(comparisons.get("pack_years 0current_smoker yes"), 0.085, 0.01);
        }
    }

    private String expectedModel(String path) throws IOException {
        FileInputStream fis = new FileInputStream(path);
        return IOUtils.toString(fis, "UTF-8");
    }
}