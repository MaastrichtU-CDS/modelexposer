package com.carrier.modelexposer.webservice.domain;

import java.util.Map;

public class ReducedRiskRequest extends RiskRequest {
    private Map<String, String> targets;

    public ReducedRiskRequest() {
        super();
        this.setModelType(ModelType.bayesian);
    }

    public Map<String, String> getTargets() {
        return targets;
    }

    public void setTargets(Map<String, String> targets) {
        this.targets = targets;
    }
}
