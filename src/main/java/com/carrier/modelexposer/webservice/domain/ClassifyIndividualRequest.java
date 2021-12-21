package com.carrier.modelexposer.webservice.domain;

import java.util.List;
import java.util.Map;

public class ClassifyIndividualRequest {
    private Map<String, String> evidence;
    private List<String> targets;

    public List<String> getTargets() {
        return targets;
    }

    public void setTargets(List<String> targets) {
        this.targets = targets;
    }

    public Map<String, String> getEvidence() {
        return evidence;
    }

    public void setEvidence(Map<String, String> evidence) {
        this.evidence = evidence;
    }
}
