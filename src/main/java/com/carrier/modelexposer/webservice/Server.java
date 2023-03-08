package com.carrier.modelexposer.webservice;

import com.carrier.modelexposer.classifier.Classifier;
import com.carrier.modelexposer.classifier.openmarkov.OpenMarkovClassifier;
import com.carrier.modelexposer.exception.UnknownAttributeException;
import com.carrier.modelexposer.exception.UnknownStateException;
import com.carrier.modelexposer.webservice.domain.ExceptionResponse;
import com.carrier.modelexposer.webservice.domain.ReducedRiskRequest;
import com.carrier.modelexposer.webservice.domain.Response;
import com.carrier.modelexposer.webservice.domain.RiskRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class Server {

    @Value ("${modelpath}")
    private String path;
    @Value ("${model}")
    private String model;
    @Value ("${defaultModel}")
    private RiskRequest.ModelType defaultClassifier;

    @Value ("${targetAttribute}")
    private String target;
    @Value ("${targetValue}")
    private String targetValue;


    private Classifier classifier;

    public Server() {
    }

    public Server(String target, String targetValue, RiskRequest.ModelType def, String path, String model) {
        this.target = target;
        this.targetValue = targetValue;
        this.defaultClassifier = def;
        this.path = path;
        this.model = model;
    }

    @PostMapping ("estimateBaseLineRisk")
    public Response estimateBaseLineRisk(@RequestBody RiskRequest req) throws Exception {
        setClassifier(req);
        Map<String, String> updatedInput = cleanUpEvidence(req.getInput());
        try {
            return classifier.classify(updatedInput);
        } catch (UnknownStateException | UnknownAttributeException e) {
            return new ExceptionResponse(e);
        }
    }

    private Map<String, String> cleanUpEvidence(Map<String, String> input) {
        input = updateAdress(input);
        return updatePackYears(input);
    }

    private Map<String, String> updateAdress(Map<String, String> input) {
        //ToDo: do something with adress

        if (input.get("address_house_number") != null) {
            input.remove("address_house_number");
        }
        if (input.get("address_postcode") != null) {
            input.remove("address_postcode");
        }
        return input;
    }

    private Map<String, String> updatePackYears(Map<String, String> input) {
        //replaces smoking values with a more general "pack_years"
        int packyears = 0;

        if ((input.computeIfPresent("current_smoker", (k, v) -> {
            return v;
        }) + "").equals("yes")) {
            String substance = "" + input.computeIfPresent("current_smoker_substance", (k, v) -> {
                return v;
            });

            if (substance.equals("cigarette")) {
                packyears += Integer.valueOf(input.get("current_smoker_cigarette_years")) * Integer.valueOf(
                        input.get("current_smoker_cigarette_number_per_day"));
                input.remove("current_smoker_cigarette_years");
                input.remove("current_smoker_cigarette_number_per_day");
            } else if (substance.equals("cigar")) {
                packyears += Integer.valueOf(input.get("current_smoker_cigar_years")) * Integer.valueOf(
                        input.get("current_smoker_cigar_number_per_week"));
                input.remove("current_smoker_cigar_years");
                input.remove("current_smoker_cigar_number_per_week");
            } else if (substance.equals("pipe")) {
                packyears += Integer.valueOf(input.get("current_smoker_pipe_years")) * Integer.valueOf(
                        input.get("current_smoker_pipe_number_per_week"));
                input.remove("current_smoker_pipe_years");
                input.remove("current_smoker_pipe_number_per_week");
            } else if (substance.equals("e-cigarette")) {
                packyears += Integer.valueOf(input.get("current_smoker_e-cigarette_years")) * Integer.valueOf(
                        input.get("current_smoker_e-cigarette_number_per_day"));
                input.remove("current_smoker_e-cigarette_years");
                input.remove("current_smoker_e-cigarette_number_per_day");
            }
        } else if (("" + input.computeIfPresent("ex_smoker", (k, v) -> {
            return v;
        })).equals("yes")) {
            String substance = "" + input.computeIfPresent("ex_smoker_substance", (k, v) -> {
                return v;
            });
            if (substance.equals("cigarette")) {
                packyears += Integer.valueOf(input.get("ex_smoker_cigarette_years")) * Integer.valueOf(
                        input.get("ex_smoker_cigarette_number_per_day"));
                input.remove("ex_smoker_cigarette_years");
                input.remove("ex_smoker_cigarette_number_per_day");
            } else if (substance.equals("cigar")) {
                packyears += Integer.valueOf(input.get("ex_smoker_cigar_years")) * Integer.valueOf(
                        input.get("ex_smoker_cigar_number_per_week"));
                input.remove("ex_smoker_cigar_years");
                input.remove("ex_smoker_cigar_number_per_week");
            } else if (substance.equals("pipe")) {
                packyears += Integer.valueOf(input.get("ex_smoker_pipe_years")) * Integer.valueOf(
                        input.get("ex_smoker_pipe_number_per_week"));
                input.remove("ex_smoker_pipe_years");
                input.remove("ex_smoker_pipe_number_per_week");
            } else if (substance.equals("e-cigarette")) {
                packyears += Integer.valueOf(input.get("ex_smoker_e-cigarette_years")) * Integer.valueOf(
                        input.get("ex_smoker_e-cigarette_number_per_day"));
                input.remove("ex_smoker_e-cigarette_years");
                input.remove("ex_smoker_e-cigarette_number_per_day");
            }
        }
        input.put("pack_years", String.valueOf(packyears));
        return input;
    }

    private void setClassifier(RiskRequest req) {
        checkDefault(req);
        if (req.getModelType() == RiskRequest.ModelType.bayesian) {
            classifier = new OpenMarkovClassifier(path, model, target, targetValue);
        }
    }


    private void checkDefault(RiskRequest req) {
        if (req.getModelType() == null) {
            req.setModelType(defaultClassifier);
        }
    }

    @PostMapping ("estimateReducedRisk")
    public Response estimateReducedRisk(
            @RequestBody ReducedRiskRequest req) throws Exception {
        setClassifier(req);
        Map<String, String> updatedInput = cleanUpEvidence(req.getInput());
        Map<String, String> updatedChanges = cleanUpEvidence(req.getInput());
        try {
            return classifier.compareClassifications(updatedInput, updatedChanges);
        } catch (UnknownStateException | UnknownAttributeException e) {
            return new ExceptionResponse(e);
        }
    }
}
