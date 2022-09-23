package com.carrier.modelexposer.webservice.domain;

import java.util.Map;

public class ClassifyIndividualBayesianRequest extends ClassifyIndividualRequest {
    private Map<String, String> targets;

    public ClassifyIndividualBayesianRequest() {
        super();
        this.setModelType(ModelType.baysian);
    }

    public Map<String, String> getTargets() {
        return targets;
    }

    public void setTargets(Map<String, String> targets) {
        this.targets = targets;
    }
}
