package com.carrier.modelexposer.webservice;

import com.carrier.modelexposer.classifier.Classifier;
import com.carrier.modelexposer.classifier.finegray.FineGrayClassifier;
import com.carrier.modelexposer.classifier.openmarkov.OpenMarkovClassifier;
import com.carrier.modelexposer.classifier.score2.Score2Classifier;
import com.carrier.modelexposer.exception.*;
import com.carrier.modelexposer.util.SESWOAMapper;
import com.carrier.modelexposer.webservice.domain.ExceptionResponse;
import com.carrier.modelexposer.webservice.domain.ReducedRiskRequest;
import com.carrier.modelexposer.webservice.domain.Response;
import com.carrier.modelexposer.webservice.domain.RiskRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.carrier.modelexposer.util.Util.*;

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
    @Value ("${SESWOAPath}")
    private List<String> seswoa;
    private SESWOAMapper seswaoMapper;


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

    public Server(String target, String targetValue, RiskRequest.ModelType def, String path, String model,
                  List<String> seswoa) {
        this.target = target;
        this.targetValue = targetValue;
        this.defaultClassifier = def;
        this.path = path;
        this.model = model;
        this.seswoa = seswoa;
    }

    @PostMapping ("estimateBaseLineRisk")
    public Response estimateBaseLineRisk(@RequestBody RiskRequest req) throws Exception {
        init();
        setClassifier(req);
        try {
            Map<String, String> changes = detectIntervention(req.getInput());
            if (changes.size() > 0) {
                ReducedRiskRequest r = new ReducedRiskRequest();
                r.setInput(req.getInput());
                r.setModelType(req.getModelType());
                r.setChanges(changes);
                return estimateReducedRisk(r);
            }
            Map<String, String> updatedInput = cleanUpEvidence(updateAdress(req.getInput()));
            return classifier.classify(updatedInput);
        } catch (UnknownStateException | UnknownAttributeException | InvalidIntegerException
                | MissingAttributeException | InvalidDoubleException e) {
            return new ExceptionResponse(e);
        }
    }

    private Map<String, String> cleanIntervention(Map<String, String> input) {
        input = removeValue(input, "intervention_bmi");
        input = removeValue(input, "intervention_diet");
        input = removeValue(input, "intervention_exercise");
        input = removeValue(input, "intervention_glucose");
        input = removeValue(input, "intervention_ldl");
        input = removeValue(input, "intervention_sbp");
        input = removeValue(input, "intervention_smoking");
        return input;
    }

    private Map<String, String> detectIntervention(Map<String, String> input)
            throws MissingAttributeException, InvalidIntegerException, UnknownStateException, InvalidDoubleException {
        init();
        Map<String, String> changes = new HashMap<>();
        Integer diet = calcDiet(input);
        Double interventionCHAMPS = calcChampScoreIntervention(input);
        Double interventionLdl = calcLDLIntervention(input);
        Integer sbp = calcSBPIntervention(input);
        Boolean smoking = getOptionalBooleanValue(input, "intervention_smoking");

        if (diet != null) {
            changes.put("eetscore", String.valueOf(diet));
        }
        if (interventionCHAMPS != null) {
            changes.put("CHAMPS_MVPA_score", String.valueOf(interventionCHAMPS));
        }

        if (interventionLdl != null) {
            changes.put("LDL", String.valueOf(interventionLdl));
        }
        if (sbp != null) {
            changes.put("SBP", String.valueOf(sbp));
        }
        if (smoking != null) {
            if (!smoking) {
                changes.put("current_smoker", "no");
                changes.put("ex_smoker", "yes");
            } else {
                changes.put("current_smoker", "yes");
            }
        }
        return changes;
    }

    @PostMapping ("estimateReducedRisk")
    public Response estimateReducedRisk(
            @RequestBody ReducedRiskRequest req) throws Exception {
        setClassifier(req);
        try {
            Map<String, String> updatedInput = cleanUpEvidence(updateAdress(req.getInput()));
            Map<String, String> updatedChanges = cleanUpEvidence(req.getChanges());
            return classifier.compareClassifications(updatedInput, updatedChanges);
        } catch (UnknownStateException | UnknownAttributeException | InvalidIntegerException
                | MissingAttributeException e) {
            return new ExceptionResponse(e);
        }
    }

    private Map<String, String> cleanUpEvidence(Map<String, String> input)
            throws InvalidIntegerException, MissingAttributeException, UnknownStateException {
        //Remove CVD,
        input = removeValue(input, "CVD");

        //This is a general cleanup of variables which are too specific but can be generalized.
        //E.g. adress is too unique, but can be used to derive if you live in a "bad" location
        input = removeValue(input, "date_baseline_consult");
        input = removeValue(input, "date_question_x_completed");
        input = removeValue(input, "birth_date");


        input = cleanIntervention(input);
        if (input.containsKey("current_smoker") || input.containsKey("ex_smoker")) {
            input = updatePackYears(input);
        }
        return input;
    }

    private Map<String, String> updateAdress(Map<String, String> input) throws MissingAttributeException {
        if (!input.containsKey("address_house_number")) {
            throw new MissingAttributeException("address_house_number");
        } else if (!input.containsKey("address_postcode")) {
            throw new MissingAttributeException("address_postcode");
        }

        if (seswaoMapper != null) {
            input.put("seswoa", String.valueOf(
                    seswaoMapper.findSESWOA(input.get("address_postcode"), input.get("address_house_number"))));
        }

        input = removeValue(input, "address_house_number");
        input = removeValue(input, "address_postcode");

        return input;
    }

    @SuppressWarnings ("checkstyle:magicNumber") //ignore magic numbers in packyears function
    private Map<String, String> updatePackYears(Map<String, String> input)
            throws InvalidIntegerException, MissingAttributeException, UnknownStateException {
        input = cleanUpSmoking(input);
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
        toBeRemoved.add("current_smoker_cigarette");
        toBeRemoved.add("current_smoker_cigar");
        toBeRemoved.add("current_smoker_pipe");
        toBeRemoved.add("current_smoker_e_cigarette");
        toBeRemoved.add("current_smoker_other");
        toBeRemoved.add("current_smoker_cigarette_years");
        toBeRemoved.add("current_smoker_cigarette_number_per_day");
        toBeRemoved.add("current_smoker_cigar_years");
        toBeRemoved.add("current_smoker_cigar_number_per_week");
        toBeRemoved.add("current_smoker_pipe_years");
        toBeRemoved.add("current_smoker_pipe_number_per_week");
        toBeRemoved.add("current_smoker_e_cigarette_years");
        toBeRemoved.add("current_smoker_e_cigarette_number_per_day");
        toBeRemoved.add("current_smoker_other_years");
        toBeRemoved.add("current_smoker_other_number_per_day");
        toBeRemoved.add("ex_smoker_cigarette");
        toBeRemoved.add("ex_smoker_cigar");
        toBeRemoved.add("ex_smoker_pipe");
        toBeRemoved.add("ex_smoker_e_cigarette");
        toBeRemoved.add("ex_smoker_other");
        toBeRemoved.add("ex_smoker_cigarette_years");
        toBeRemoved.add("ex_smoker_cigarette_number_per_day");
        toBeRemoved.add("ex_smoker_cigar_years");
        toBeRemoved.add("ex_smoker_cigar_number_per_week");
        toBeRemoved.add("ex_smoker_pipe_years");
        toBeRemoved.add("ex_smoker_pipe_number_per_week");
        toBeRemoved.add("ex_smoker_e_cigarette_years");
        toBeRemoved.add("ex_smoker_e_cigarette_number_per_day");
        toBeRemoved.add("ex_smoker_other_years");
        toBeRemoved.add("ex_smoker_other_number_per_day");

        for (String s : toBeRemoved) {
            input = removeValue(input, s);
        }
        return input;
    }

    private void setClassifier(RiskRequest req) {
        checkDefault(req);
        if (req.getModelType() == RiskRequest.ModelType.bayesian) {
            classifier = new OpenMarkovClassifier(path, model, target, targetValue);
        } else if (req.getModelType() == RiskRequest.ModelType.score2) {
            classifier = new Score2Classifier();
        } else if (req.getModelType() == RiskRequest.ModelType.fineGray) {
            classifier = new FineGrayClassifier();
        }
    }


    private void checkDefault(RiskRequest req) {
        if (req.getModelType() == null) {
            req.setModelType(defaultClassifier);
        }
    }

    private void init() {
        if (seswoa != null) {
            seswaoMapper = new SESWOAMapper(seswoa);
        }
    }
}
