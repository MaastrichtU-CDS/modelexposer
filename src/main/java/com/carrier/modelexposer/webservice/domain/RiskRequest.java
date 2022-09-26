package com.carrier.modelexposer.webservice.domain;

import java.util.Map;

public class RiskRequest {
    public enum ModelType { bayesian }

    private Map<String, String> evidence;
    private ModelType modelType;

    public ModelType getModelType() {
        return modelType;
    }

    public void setModelType(ModelType modelType) {
        this.modelType = modelType;
    }

    public Map<String, String> getEvidence() {
        return evidence;
    }

    public void setEvidence(Map<String, String> evidence) {
        this.evidence = evidence;
    }
}
