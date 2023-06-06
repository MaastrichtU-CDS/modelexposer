package com.carrier.modelexposer.webservice;

import com.carrier.modelexposer.webservice.domain.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ServerTest {

    @Test
    public void testScore2Classifier()
            throws Exception {
        {
            String path = "resources/";
            String model = "model.pgmx";

            Server server = new Server("CVD", "yes", RiskRequest.ModelType.fineGray, path, model);


            Map<String, String> evidence = new HashMap<>();
            evidence.put("gender", "male");
            evidence.put("age", "50");
            evidence.put("current_smoker", "yes");
            evidence.put("SBP", "140");
            evidence.put("TC", "6.3");
            evidence.put("HDL", "1.4");
            evidence.put("current_smoker_cigarette", "no");
            evidence.put("current_smoker_cigar", "no");
            evidence.put("current_smoker_pipe", "no");
            evidence.put("current_smoker_e_cigarette", "no");
            evidence.put("current_smoker_other", "no");
            evidence.put("address_house_number", "50");
            evidence.put("address_postcode", "50");


            RiskRequest req = new RiskRequest();
            req.setInput(evidence);
            req.setModelType(RiskRequest.ModelType.score2);

            RiskResponse r = (RiskResponse) server.estimateBaseLineRisk(req);
            assertEquals(r.getProbabilities().get("CVD"), 0.06, 0.01);
        }
    }

    @Test
    public void testScore2ClassifierWithIntervention()
            throws Exception {
        {
            String path = "resources/";
            String model = "model.pgmx";

            Server server = new Server("CVD", "yes", RiskRequest.ModelType.fineGray, path, model);


            Map<String, String> evidence = new HashMap<>();
            evidence.put("gender", "male");
            evidence.put("age", "50");
            evidence.put("current_smoker", "yes");
            evidence.put("SBP", "140");
            evidence.put("TC", "6.3");
            evidence.put("HDL", "1.4");
            evidence.put("current_smoker_cigarette", "no");
            evidence.put("current_smoker_cigar", "no");
            evidence.put("current_smoker_pipe", "no");
            evidence.put("current_smoker_e_cigarette", "no");
            evidence.put("current_smoker_other", "no");
            evidence.put("intervention_smoking", "no");
            evidence.put("address_house_number", "50");
            evidence.put("address_postcode", "50");


            RiskRequest req = new RiskRequest();
            req.setInput(evidence);
            req.setModelType(RiskRequest.ModelType.score2);

            ReducedRiskResponse r = (ReducedRiskResponse) server.estimateBaseLineRisk(req);
            Map<String, Double> comparisons = new HashMap<>();

            String name = "";
            for (String s : r.getChanges().getChanged().keySet()) {
                name += s + " " + r.getChanges().getChanged().get(s);
            }
            comparisons.put(name, r.getChanges().getProbabilities().get("CVD"));


            assertEquals(comparisons.get("ex_smoker yescurrent_smoker no"), 0.04, 0.01);
        }
    }

    @Test
    public void testScore2ClassifierInvalidValues()
            throws Exception {
        {
            String path = "resources/";
            String model = "model.pgmx";

            Server server = new Server("CVD", "yes", RiskRequest.ModelType.fineGray, path, model);


            Map<String, String> evidence = new HashMap<>();
            evidence.put("gender", "nonsense");
            evidence.put("age", "50");
            evidence.put("address_house_number", "50");
            evidence.put("address_postcode", "50");
            evidence.put("current_smoker", "no");
            evidence.put("SBP", "140");
            evidence.put("TC", "6.3");
            evidence.put("HDL", "1.4");

            RiskRequest req = new RiskRequest();
            req.setInput(evidence);
            req.setModelType(RiskRequest.ModelType.score2);

            ExceptionResponse r = (ExceptionResponse) server.estimateBaseLineRisk(req);
            assertEquals(r.getMessage(),
                         "Encountered an error. \n" +
                                 "Currently running version: 2.9\n" +
                                 "Error: \n" +
                                 "Unknown state 'nonsense' for attribute 'gender', expected valid states: 'male', " +
                                 "'female'");

            evidence.put("gender", "male");
            evidence.put("current_smoker", "nonsense");
            evidence.put("address_house_number", "50");
            evidence.put("address_postcode", "50");

            r = (ExceptionResponse) server.estimateBaseLineRisk(req);
            assertEquals(r.getMessage(),
                         "Encountered an error. \n" +
                                 "Currently running version: 2.9\n" +
                                 "Error: \n" +
                                 "Unknown state 'nonsense' for attribute 'current_smoker', expected valid states: " +
                                 "'yes', 'no'");

            evidence.put("current_smoker", "yes");
            evidence.put("SBP", "nonsense");
            evidence.put("current_smoker_cigarette", "no");
            evidence.put("current_smoker_cigar", "no");
            evidence.put("current_smoker_pipe", "no");
            evidence.put("current_smoker_e_cigarette", "no");
            evidence.put("current_smoker_other", "no");
            evidence.put("address_house_number", "50");
            evidence.put("address_postcode", "50");

            r = (ExceptionResponse) server.estimateBaseLineRisk(req);
            assertEquals(r.getMessage(), "Encountered an error. \n" +
                    "Currently running version: 2.9\n" +
                    "Error: \n" +
                    "Attribute 'SBP' is expected to be an double value");

            evidence.remove("SBP");
            //readd smoking variables due to unit-test passing along values, unlike the real world where i would be a
            // new list
            evidence.put("current_smoker_cigarette", "no");
            evidence.put("current_smoker_cigar", "no");
            evidence.put("current_smoker_pipe", "no");
            evidence.put("current_smoker_e_cigarette", "no");
            evidence.put("current_smoker_other", "no");

            evidence.put("address_house_number", "50");
            evidence.put("address_postcode", "50");

            r = (ExceptionResponse) server.estimateBaseLineRisk(req);
            assertEquals(r.getMessage(), "Encountered an error. \n" +
                    "Currently running version: 2.9\n" +
                    "Error: \n" +
                    "Missing attribute 'SBP' is expected to be present");
        }
    }

    @Test
    public void testExample1Score2()
            throws Exception {
        {
            String path = "resources/";
            String model = "dummy_model_sananet.pgmx";

            Server server = new Server("CVD", "yes", RiskRequest.ModelType.fineGray, path, model);

            ReducedRiskRequest req = readJSONReducedRisk(path + "examples/example1.txt");
            req.setModelType(RiskRequest.ModelType.score2);

            ReducedRiskResponse result = (ReducedRiskResponse) server.estimateBaseLineRisk(req);

            Map<String, Double> comparisons = new HashMap<>();

            String name = "";
            for (String s : result.getChanges().getChanged().keySet()) {
                name += s + " " + result.getChanges().getChanged().get(s);
            }
            comparisons.put(name, result.getChanges().getProbabilities().get("CVD"));


            assertEquals(comparisons.get("ex_smoker yescurrent_smoker no"), 0.08, 0.01);
        }
    }

    @Test
    public void testExample2Score2()
            throws Exception {
        {
            String path = "resources/";
            String model = "dummy_model_sananet.pgmx";

            Server server = new Server("CVD", "yes", RiskRequest.ModelType.fineGray, path, model);

            ReducedRiskRequest req = readJSONReducedRisk(path + "examples/example2.txt");
            req.setModelType(RiskRequest.ModelType.score2);

            ReducedRiskResponse result = (ReducedRiskResponse) server.estimateBaseLineRisk(req);

            Map<String, Double> comparisons = new HashMap<>();

            String name = "";
            for (String s : result.getChanges().getChanged().keySet()) {
                name += s + " " + result.getChanges().getChanged().get(s);
            }
            comparisons.put(name, result.getChanges().getProbabilities().get("CVD"));


            assertEquals(comparisons.get("LDL 3.5"), 0.12, 0.01);
        }
    }

    @Test
    public void testExample3Score2()
            throws Exception {
        {
            String path = "resources/";
            String model = "dummy_model_sananet.pgmx";

            Server server = new Server("CVD", "yes", RiskRequest.ModelType.fineGray, path, model);

            ReducedRiskRequest req = readJSONReducedRisk(path + "examples/example3.txt");
            req.setModelType(RiskRequest.ModelType.score2);

            ReducedRiskResponse result = (ReducedRiskResponse) server.estimateBaseLineRisk(req);

            Map<String, Double> comparisons = new HashMap<>();

            String name = "";
            for (String s : result.getChanges().getChanged().keySet()) {
                name += s + " " + result.getChanges().getChanged().get(s);
            }
            comparisons.put(name, result.getChanges().getProbabilities().get("CVD"));


            assertEquals(comparisons.get("eetscore 110"), 0.10, 0.01);
        }
    }

    @Test
    public void testExample4Score2()
            throws Exception {
        {
            String path = "resources/";
            String model = "dummy_model_sananet.pgmx";

            Server server = new Server("CVD", "yes", RiskRequest.ModelType.fineGray, path, model);

            ReducedRiskRequest req = readJSONReducedRisk(path + "examples/example4.txt");
            req.setModelType(RiskRequest.ModelType.score2);

            ReducedRiskResponse result = (ReducedRiskResponse) server.estimateBaseLineRisk(req);

            Map<String, Double> comparisons = new HashMap<>();

            String name = "";
            for (String s : result.getChanges().getChanged().keySet()) {
                name += s + " " + result.getChanges().getChanged().get(s);
            }
            comparisons.put(name, result.getChanges().getProbabilities().get("CVD"));


            assertEquals(comparisons.get("CHAMPS_MVPA_score 5.833333333333333"), 0.09, 0.01);
        }
    }

    @Test
    public void testExample5Score2()
            throws Exception {
        {
            String path = "resources/";
            String model = "dummy_model_sananet.pgmx";

            Server server = new Server("CVD", "yes", RiskRequest.ModelType.fineGray, path, model);

            ReducedRiskRequest req = readJSONReducedRisk(path + "examples/example5.txt");
            req.setModelType(RiskRequest.ModelType.score2);

            ReducedRiskResponse result = (ReducedRiskResponse) server.estimateBaseLineRisk(req);

            Map<String, Double> comparisons = new HashMap<>();

            String name = "";
            for (String s : result.getChanges().getChanged().keySet()) {
                name += s + " " + result.getChanges().getChanged().get(s);
            }
            comparisons.put(name, result.getChanges().getProbabilities().get("CVD"));


            assertEquals(comparisons.get("SBP 135"), 0.10, 0.01);
        }
    }

    @Test
    public void testExample6Score2()
            throws Exception {
        {
            String path = "resources/";
            String model = "dummy_model_sananet.pgmx";

            Server server = new Server("CVD", "yes", RiskRequest.ModelType.fineGray, path, model);

            ReducedRiskRequest req = readJSONReducedRisk(path + "examples/example6.txt");
            req.setModelType(RiskRequest.ModelType.score2);

            ReducedRiskResponse result = (ReducedRiskResponse) server.estimateBaseLineRisk(req);

            Map<String, Double> comparisons = new HashMap<>();

            String name = "";
            for (String s : result.getChanges().getChanged().keySet()) {
                name += s + " " + result.getChanges().getChanged().get(s);
            }
            comparisons.put(name, result.getChanges().getProbabilities().get("CVD"));


            assertEquals(comparisons.get("ex_smoker yescurrent_smoker no"), 0.06, 0.01);
        }
    }


    @Test
    public void testFineGrayExample_1()
            throws Exception {
        {
            String path = "resources/";
            String model = "dummy_model_sananet.pgmx";
            String seswoa = "resources/seswoa_";


            Server server = new Server("CVD", "yes", RiskRequest.ModelType.fineGray, path, model, seswoa);

            ReducedRiskRequest req = readJSONReducedRisk(path + "examples/fineGray1.txt");
            req.setModelType(RiskRequest.ModelType.fineGray);

            ReducedRiskResponse result = (ReducedRiskResponse) server.estimateBaseLineRisk(req);

            Map<String, Double> comparisons = new HashMap<>();

            String name = "";
            for (String s : result.getChanges().getChanged().keySet()) {
                name += s + " " + result.getChanges().getChanged().get(s);
            }
            comparisons.put(name, result.getChanges().getProbabilities().get("CVD"));

            assertEquals(result.getBaseline().getProbabilities().get("CVD"), 0.0748, 0.001);
            assertEquals(comparisons.get("ex_smoker yescurrent_smoker no"), 0.0423, 0.001);
        }
    }

    @Test
    public void testFineGrayExample_2()
            throws Exception {
        {
            String path = "resources/";
            String model = "dummy_model_sananet.pgmx";
            String seswoa = "resources/seswoa_";


            Server server = new Server("CVD", "yes", RiskRequest.ModelType.fineGray, path, model, seswoa);

            ReducedRiskRequest req = readJSONReducedRisk(path + "examples/fineGray2.txt");
            req.setModelType(RiskRequest.ModelType.fineGray);

            ReducedRiskResponse result = (ReducedRiskResponse) server.estimateBaseLineRisk(req);

            Map<String, Double> comparisons = new HashMap<>();

            String name = "";
            for (String s : result.getChanges().getChanged().keySet()) {
                name += s + " " + result.getChanges().getChanged().get(s);
            }
            comparisons.put(name, result.getChanges().getProbabilities().get("CVD"));

            assertEquals(result.getBaseline().getProbabilities().get("CVD"), 0.0748, 0.001);
            assertEquals(comparisons.get("eetscore 110"), 0.0686, 0.001);
        }
    }

    @Test
    public void testFineGrayExample_3()
            throws Exception {
        {
            String path = "resources/";
            String model = "dummy_model_sananet.pgmx";
            String seswoa = "resources/seswoa_";


            Server server = new Server("CVD", "yes", RiskRequest.ModelType.fineGray, path, model, seswoa);

            ReducedRiskRequest req = readJSONReducedRisk(path + "examples/fineGray3.txt");
            req.setModelType(RiskRequest.ModelType.fineGray);

            ReducedRiskResponse result = (ReducedRiskResponse) server.estimateBaseLineRisk(req);

            Map<String, Double> comparisons = new HashMap<>();

            String name = "";
            for (String s : result.getChanges().getChanged().keySet()) {
                name += s + " " + result.getChanges().getChanged().get(s);
            }
            comparisons.put(name, result.getChanges().getProbabilities().get("CVD"));

            assertEquals(result.getBaseline().getProbabilities().get("CVD"), 0.0748, 0.001);
            assertEquals(comparisons.get("ex_smoker yescurrent_smoker noeetscore 110"), 0.0388, 0.001);
        }
    }

    @Test
    public void testFineGrayExample_4()
            throws Exception {
        {
            String path = "resources/";
            String model = "dummy_model_sananet.pgmx";
            String seswoa = "resources/seswoa_";


            Server server = new Server("CVD", "yes", RiskRequest.ModelType.fineGray, path, model, seswoa);

            ReducedRiskRequest req = readJSONReducedRisk(path + "examples/fineGray4.txt");
            req.setModelType(RiskRequest.ModelType.fineGray);

            ReducedRiskResponse result = (ReducedRiskResponse) server.estimateBaseLineRisk(req);

            Map<String, Double> comparisons = new HashMap<>();

            String name = "";
            for (String s : result.getChanges().getChanged().keySet()) {
                name += s + " " + result.getChanges().getChanged().get(s);
            }
            comparisons.put(name, result.getChanges().getProbabilities().get("CVD"));

            assertEquals(result.getBaseline().getProbabilities().get("CVD"), 0.0224, 0.001);
            assertEquals(comparisons.get("CHAMPS_MVPA_score 3.75"), 0.0222, 0.001);
        }
    }

    @Test
    public void testFineGrayExample_5()
            throws Exception {
        {
            String path = "resources/";
            String model = "dummy_model_sananet.pgmx";
            String seswoa = "resources/seswoa_";


            Server server = new Server("CVD", "yes", RiskRequest.ModelType.fineGray, path, model, seswoa);

            ReducedRiskRequest req = readJSONReducedRisk(path + "examples/fineGray5.txt");
            req.setModelType(RiskRequest.ModelType.fineGray);

            ReducedRiskResponse result = (ReducedRiskResponse) server.estimateBaseLineRisk(req);

            Map<String, Double> comparisons = new HashMap<>();

            String name = "";
            for (String s : result.getChanges().getChanged().keySet()) {
                name += s + " " + result.getChanges().getChanged().get(s);
            }
            comparisons.put(name, result.getChanges().getProbabilities().get("CVD"));

            assertEquals(result.getBaseline().getProbabilities().get("CVD"), 0.0224, 0.001);
            assertEquals(comparisons.get("eetscore 110"), 0.0200, 0.001);
        }
    }

    @Test
    public void testFineGrayExample_6()
            throws Exception {
        {
            String path = "resources/";
            String model = "dummy_model_sananet.pgmx";
            String seswoa = "resources/seswoa_";


            Server server = new Server("CVD", "yes", RiskRequest.ModelType.fineGray, path, model, seswoa);

            ReducedRiskRequest req = readJSONReducedRisk(path + "examples/fineGray6.txt");
            req.setModelType(RiskRequest.ModelType.fineGray);

            ReducedRiskResponse result = (ReducedRiskResponse) server.estimateBaseLineRisk(req);

            Map<String, Double> comparisons = new HashMap<>();

            String name = "";
            for (String s : result.getChanges().getChanged().keySet()) {
                name += s + " " + result.getChanges().getChanged().get(s);
            }
            comparisons.put(name, result.getChanges().getProbabilities().get("CVD"));

            assertEquals(result.getBaseline().getProbabilities().get("CVD"), 0.0224, 0.001);
            assertEquals(comparisons.get("eetscore 110CHAMPS_MVPA_score 3.75"), 0.0197, 0.001);
        }
    }

    @Test
    public void testFineGrayExample_7()
            throws Exception {
        {
            String path = "resources/";
            String model = "dummy_model_sananet.pgmx";
            String seswoa = "resources/seswoa_";


            Server server = new Server("CVD", "yes", RiskRequest.ModelType.fineGray, path, model, seswoa);

            ReducedRiskRequest req = readJSONReducedRisk(path + "examples/fineGray7.txt");
            req.setModelType(RiskRequest.ModelType.fineGray);

            ReducedRiskResponse result = (ReducedRiskResponse) server.estimateBaseLineRisk(req);

            Map<String, Double> comparisons = new HashMap<>();

            String name = "";
            for (String s : result.getChanges().getChanged().keySet()) {
                name += s + " " + result.getChanges().getChanged().get(s);
            }
            comparisons.put(name, result.getChanges().getProbabilities().get("CVD"));

            assertEquals(result.getBaseline().getProbabilities().get("CVD"), 0.0224, 0.001);
            assertEquals(comparisons.get("SBP 125"), 0.016, 0.001);
        }
    }

    @Test
    public void testFineGrayExample_8()
            throws Exception {
        {
            String path = "resources/";
            String model = "dummy_model_sananet.pgmx";
            String seswoa = "resources/seswoa_";


            Server server = new Server("CVD", "yes", RiskRequest.ModelType.fineGray, path, model, seswoa);

            ReducedRiskRequest req = readJSONReducedRisk(path + "examples/fineGray8.txt");
            req.setModelType(RiskRequest.ModelType.fineGray);

            ReducedRiskResponse result = (ReducedRiskResponse) server.estimateBaseLineRisk(req);

            Map<String, Double> comparisons = new HashMap<>();

            String name = "";
            for (String s : result.getChanges().getChanged().keySet()) {
                name += s + " " + result.getChanges().getChanged().get(s);
            }
            comparisons.put(name, result.getChanges().getProbabilities().get("CVD"));

            assertEquals(result.getBaseline().getProbabilities().get("CVD"), 0.0748, 0.001);
            assertEquals(comparisons.get("ex_smoker yesLDL 2.2current_smoker noeetscore 110"), 0.0171, 0.001);
        }
    }

    private ReducedRiskRequest readJSONReducedRisk(String path) throws IOException {
        FileInputStream fis = new FileInputStream(path);
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(IOUtils.toString(fis, "UTF-8"), ReducedRiskRequest.class);
    }

    private RiskRequest readJSONRiskRequest(String path) throws IOException {
        FileInputStream fis = new FileInputStream(path);
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(IOUtils.toString(fis, "UTF-8"), ReducedRiskRequest.class);
    }

    private String expectedModel(String path) throws IOException {
        FileInputStream fis = new FileInputStream(path);
        return IOUtils.toString(fis, "UTF-8");
    }
}