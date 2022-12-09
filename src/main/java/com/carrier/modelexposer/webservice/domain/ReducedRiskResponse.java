package com.carrier.modelexposer.webservice.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReducedRiskResponse extends Response {
    private List<Intervention> changes;
    private RiskResponse baseline;

    public RiskResponse getBaseline() {
        return baseline;
    }

    public void setBaseline(RiskResponse baseline) {
        this.baseline = baseline;
    }

    public List<Intervention> getChanges() {
        return changes;
    }

    public void setChanges(List<Intervention> changes) {
        this.changes = changes;
    }

    public void addResult(Map<String, String> evidence, Map<String, Double> probabilities) {
        if (changes == null) {
            changes = new ArrayList<>();
        }
        Intervention c = new Intervention();
        c.setProbabilities(probabilities);
        c.setChanged(evidence);
        changes.add(c);
    }
}
