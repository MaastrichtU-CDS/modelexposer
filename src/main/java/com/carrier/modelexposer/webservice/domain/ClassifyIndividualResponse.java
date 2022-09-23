package com.carrier.modelexposer.webservice.domain;

import java.util.Map;

public class ClassifyIndividualResponse {
    private Map<String, Map<String, Double>> probabilities;

    public Map<String, Map<String, Double>> getProbabilities() {
        return probabilities;
    }

    public void setProbabilities(Map<String, Map<String, Double>> probabilities) {
        this.probabilities = probabilities;
    }
}


