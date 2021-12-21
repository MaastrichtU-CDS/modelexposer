package com.carrier.modelexposer.webservice.domain;

import java.util.Map;

public class Attribute {
    Map<String, Double> probabilities;
    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Double> getProbabilities() {
        return probabilities;
    }

    public void setProbabilities(Map<String, Double> probabilities) {
        this.probabilities = probabilities;
    }
}
