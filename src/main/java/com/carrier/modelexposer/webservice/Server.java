package com.carrier.modelexposer.webservice;

import com.carrier.modelexposer.classifier.Classifier;
import com.carrier.modelexposer.classifier.openmarkov.OpenMarkovClassifier;
import com.carrier.modelexposer.exception.UnknownAttributeException;
import com.carrier.modelexposer.exception.UnknownStateException;
import com.carrier.modelexposer.webservice.domain.ExceptionResponse;
import com.carrier.modelexposer.webservice.domain.Response;
import com.carrier.modelexposer.webservice.domain.RiskRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
            return classifier.classify(req.getInput());
        } catch (UnknownStateException | UnknownAttributeException e) {
            return new ExceptionResponse(e);
        }
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
            @RequestBody RiskRequest req) throws Exception {
        setClassifier(req);
        try {
            if (req.getComparisons().size() > 0) {
                return classifier.compareClassifications(req.getInput(), req.getComparisons());

            } else {
                return classifier.compareClassifications(req.getInput());
            }
        } catch (UnknownStateException | UnknownAttributeException e) {
            return new ExceptionResponse(e);
        }
    }
}
