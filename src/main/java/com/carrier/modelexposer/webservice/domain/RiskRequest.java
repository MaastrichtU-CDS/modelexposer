package com.carrier.modelexposer.webservice.domain;

import java.util.Map;

public class RiskRequest {
    public enum ModelType { bayesian, score2 }

    private Map<String, String> input;
    private ModelType modelType;

    public ModelType getModelType() {
        return modelType;
    }

    public void setModelType(ModelType modelType) {
        this.modelType = modelType;
    }

    public Map<String, String> getInput() {
        return input;
    }

    public void setInput(Map<String, String> input) {
        this.input = input;
    }
}
