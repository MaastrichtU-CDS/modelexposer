package com.carrier.modelexposer.webservice.domain;

import java.util.HashMap;
import java.util.Map;

public class ReducedRiskRequest extends RiskRequest {
    private Map<String, String> changes = new HashMap<>();

    public Map<String, String> getChanges() {
        return changes;
    }

    public void setChanges(Map<String, String> changes) {
        this.changes = changes;
    }
}
