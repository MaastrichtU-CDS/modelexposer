package com.carrier.modelexposer.webservice.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReducedRiskResponse {
    private List<Comparison> comparisons;
    private RiskResponse baseline;

    public RiskResponse getBaseline() {
        return baseline;
    }

    public void setBaseline(RiskResponse baseline) {
        this.baseline = baseline;
    }

    public List<Comparison> getComparisons() {
        return comparisons;
    }

    public void setComparisons(List<Comparison> comparisons) {
        this.comparisons = comparisons;
    }

    public void addResult(Map<String, String> evidence, Map<String, Double> probabilities) {
        if (comparisons == null) {
            comparisons = new ArrayList<>();
        }
        Comparison c = new Comparison();
        c.setProbabilities(probabilities);
        c.setChanged(evidence);
        comparisons.add(c);
    }
}
