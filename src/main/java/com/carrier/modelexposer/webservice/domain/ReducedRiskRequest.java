package com.carrier.modelexposer.webservice.domain;

public class ReducedRiskRequest extends RiskRequest {
    public ReducedRiskRequest() {
        super();
        this.setModelType(ModelType.bayesian);
    }
}
