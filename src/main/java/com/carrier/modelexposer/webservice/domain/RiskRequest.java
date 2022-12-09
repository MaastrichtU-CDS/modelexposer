package com.carrier.modelexposer.webservice.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RiskRequest {
    public enum ModelType { bayesian }

    private Map<String, String> input;
    private List<Map<String, String>> comparisons = new ArrayList<>();
    private ModelType modelType;

    public List<Map<String, String>> getComparisons() {
        return comparisons;
    }

    public void setComparisons(List<Map<String, String>> comparisons) {
        this.comparisons = comparisons;
    }

    public ModelType getModelType() {
        return modelType;
    }

    public void setModelType(ModelType modelType) {
        this.modelType = modelType;
    }

    public Map<String, String> getInput() {
        return input;
    }

    public void setInput(Map<String, String> input) {
        this.input = input;
    }
}
