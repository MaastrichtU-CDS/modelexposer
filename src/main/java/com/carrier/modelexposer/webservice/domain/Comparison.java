package com.carrier.modelexposer.webservice.domain;

import java.util.Map;

public class Comparison extends ClassifyIndividualResponse {
    private Map<String, String> changed;

    public Map<String, String> getChanged() {
        return changed;
    }

    public void setChanged(Map<String, String> changed) {
        this.changed = changed;
    }
}
