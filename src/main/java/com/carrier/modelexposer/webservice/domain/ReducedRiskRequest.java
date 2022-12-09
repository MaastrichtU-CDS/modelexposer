package com.carrier.modelexposer.webservice.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReducedRiskRequest extends RiskRequest {
    private List<Map<String, String>> changes = new ArrayList<>();

    public List<Map<String, String>> getChanges() {
        return changes;
    }

    public void setChanges(List<Map<String, String>> changes) {
        this.changes = changes;
    }
}
