package com.carrier.modelexposer.webservice.domain;

import java.util.Map;

public class RiskResponse extends Response {
    private Map<String, Double> probabilities;

    public Map<String, Double> getProbabilities() {
        return probabilities;
    }

    public void setProbabilities(Map<String, Double> probabilities) {
        this.probabilities = probabilities;
    }
}
