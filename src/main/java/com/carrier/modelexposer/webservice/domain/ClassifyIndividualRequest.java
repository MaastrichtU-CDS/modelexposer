package com.carrier.modelexposer.webservice.domain;

import java.util.Map;

public class ClassifyIndividualRequest {
    public enum ModelType { baysian }

    private Map<String, String> evidence;
    private ModelType modelType;
    private Map<String, String> targets;

    public ModelType getModelType() {
        return modelType;
    }

    public void setModelType(ModelType modelType) {
        this.modelType = modelType;
    }

    public Map<String, String> getTargets() {
        return targets;
    }

    public void setTargets(Map<String, String> targets) {
        this.targets = targets;
    }

    public Map<String, String> getEvidence() {
        return evidence;
    }

    public void setEvidence(Map<String, String> evidence) {
        this.evidence = evidence;
    }
}
