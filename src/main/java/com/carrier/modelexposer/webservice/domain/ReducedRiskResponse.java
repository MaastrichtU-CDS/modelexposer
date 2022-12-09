package com.carrier.modelexposer.webservice.domain;

import java.util.Map;

public class ReducedRiskResponse extends Response {
    private Intervention changes;
    private RiskResponse baseline;

    public RiskResponse getBaseline() {
        return baseline;
    }

    public void setBaseline(RiskResponse baseline) {
        this.baseline = baseline;
    }

    public Intervention getChanges() {
        return changes;
    }

    public void setChanges(Intervention changes) {
        this.changes = changes;
    }

    public void setResult(Map<String, String> evidence, Map<String, Double> probabilities) {
        Intervention c = new Intervention();
        c.setProbabilities(probabilities);
        c.setChanged(evidence);
        changes = c;
    }
}
