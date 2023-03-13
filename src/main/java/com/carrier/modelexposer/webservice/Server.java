package com.carrier.modelexposer.webservice;

import com.carrier.modelexposer.classifier.Classifier;
import com.carrier.modelexposer.classifier.openmarkov.OpenMarkovClassifier;
import com.carrier.modelexposer.classifier.score2.Score2Classifier;
import com.carrier.modelexposer.exception.*;
import com.carrier.modelexposer.webservice.domain.ExceptionResponse;
import com.carrier.modelexposer.webservice.domain.ReducedRiskRequest;
import com.carrier.modelexposer.webservice.domain.Response;
import com.carrier.modelexposer.webservice.domain.RiskRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.carrier.modelexposer.util.Util.getIntValue;

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
        try {
            Map<String, String> updatedInput = cleanUpEvidence(req.getInput());
            return classifier.classify(updatedInput);
        } catch (UnknownStateException | UnknownAttributeException | InvalidIntegerException
                | MissingAttributeException | InvalidDoubleException e) {
            return new ExceptionResponse(e);
        }
    }

    @PostMapping ("estimateReducedRisk")
    public Response estimateReducedRisk(
            @RequestBody ReducedRiskRequest req) throws Exception {
        setClassifier(req);
        try {
            Map<String, String> updatedInput = cleanUpEvidence(req.getInput());
            Map<String, String> updatedChanges = cleanUpEvidence(req.getInput());
            return classifier.compareClassifications(updatedInput, updatedChanges);
        } catch (UnknownStateException | UnknownAttributeException | InvalidIntegerException
                | MissingAttributeException e) {
            return new ExceptionResponse(e);
        }
    }

    private Map<String, String> cleanUpEvidence(Map<String, String> input)
            throws InvalidIntegerException, MissingAttributeException {
        //This is a general cleanup of variables which are too specific but can be generalized.
        //E.g. adress is too unique, but can be used to derive if you live in a "bad" location
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

    private Map<String, String> updatePackYears(Map<String, String> input)
            throws InvalidIntegerException, MissingAttributeException {
        //replaces smoking values with a more general "pack_years"
        int packyears = 0;

        if ((input.computeIfPresent("current_smoker", (k, v) -> {
            return v;
        }) + "").equals("yes")) {
            String substance = "" + input.computeIfPresent("current_smoker_substance", (k, v) -> {
                return v;
            });
            if (substance.equals("cigarette")) {
                packyears = createPackYears("current_smoker_cigarette_years",
                                            "current_smoker_cigarette_number_per_day", input);
            } else if (substance.equals("cigar")) {
                packyears += createPackYears("current_smoker_cigar_years",
                                             "current_smoker_cigar_number_per_week",
                                             input);
            } else if (substance.equals("pipe")) {
                packyears += createPackYears("current_smoker_pipe_years",
                                             "current_smoker_pipe_number_per_week", input);
            } else if (substance.equals("e-cigarette")) {
                packyears += createPackYears("current_smoker_e-cigarette_years",
                                             "current_smoker_e-cigarette_number_per_day", input);
            }
        } else if (("" + input.computeIfPresent("ex_smoker", (k, v) -> {
            return v;
        })).equals("yes")) {
            String substance = "" + input.computeIfPresent("ex_smoker_substance", (k, v) -> {
                return v;
            });
            if (substance.equals("cigarette")) {
                packyears += createPackYears("ex_smoker_cigarette_years",
                                             "ex_smoker_cigarette_number_per_day", input);
            } else if (substance.equals("cigar")) {
                packyears += createPackYears("ex_smoker_cigar_years",
                                             "ex_smoker_cigar_number_per_week", input);
            } else if (substance.equals("pipe")) {
                packyears += createPackYears("ex_smoker_pipe_years",
                                             "ex_smoker_pipe_number_per_week", input);
            } else if (substance.equals("e-cigarette")) {
                packyears += createPackYears("ex_smoker_e-cigarette_years",
                                             "ex_smoker_e-cigarette_number_per_day", input);
            }
        }

        input = cleanUpSmoking(input);
        input.put("pack_years", String.valueOf(packyears));
        return input;
    }

    private int createPackYears(String years, String count, Map<String, String> input)
            throws MissingAttributeException, InvalidIntegerException {

        int yInt = getIntValue(input, years);
        int cInt = getIntValue(input, count);
        return yInt * cInt;

    }

    private Map<String, String> cleanUpSmoking(Map<String, String> input) {
        List<String> toBeRemoved = new ArrayList<>();
        toBeRemoved.add("current_smoker_cigarette_years");
        toBeRemoved.add("current_smoker_cigarette_number_per_day");
        toBeRemoved.add("current_smoker_cigar_years");
        toBeRemoved.add("current_smoker_cigar_number_per_week");
        toBeRemoved.add("current_smoker_pipe_years");
        toBeRemoved.add("current_smoker_pipe_number_per_week");
        toBeRemoved.add("current_smoker_e-cigarette_years");
        toBeRemoved.add("current_smoker_e-cigarette_number_per_day");
        toBeRemoved.add("ex_smoker_cigarette_years");
        toBeRemoved.add("ex_smoker_cigarette_number_per_day");
        toBeRemoved.add("ex_smoker_cigar_years");
        toBeRemoved.add("ex_smoker_cigar_number_per_week");
        toBeRemoved.add("ex_smoker_pipe_years");
        toBeRemoved.add("ex_smoker_pipe_number_per_week");
        toBeRemoved.add("ex_smoker_e-cigarette_years");
        toBeRemoved.add("ex_smoker_e-cigarette_number_per_day");

        for (String s : toBeRemoved) {
            if (input.get(s) != null) {
                input.remove(s);
            }
        }
        return input;
    }

    private void setClassifier(RiskRequest req) {
        checkDefault(req);
        if (req.getModelType() == RiskRequest.ModelType.bayesian) {
            classifier = new OpenMarkovClassifier(path, model, target, targetValue);
        } else if (req.getModelType() == RiskRequest.ModelType.score2) {
            classifier = new Score2Classifier();
        }
    }


    private void checkDefault(RiskRequest req) {
        if (req.getModelType() == null) {
            req.setModelType(defaultClassifier);
        }
    }
}
