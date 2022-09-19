package com.carrier.modelexposer.webservice.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClassifyIndividualComparisonResponse {
    private List<Comparison> comparisons;
    private ClassifyIndividualResponse baseline;

    public ClassifyIndividualResponse getBaseline() {
        return baseline;
    }

    public void setBaseline(ClassifyIndividualResponse baseline) {
        this.baseline = baseline;
    }

    public List<Comparison> getComparisons() {
        return comparisons;
    }

    public void setComparisons(List<Comparison> comparisons) {
        this.comparisons = comparisons;
    }

    public void addResult(Map<String, String> evidence, List<Attribute> result) {
        if (comparisons == null) {
            comparisons = new ArrayList<>();
        }
        Comparison c = new Comparison();
        c.setAttributes(result);
        c.setChanged(evidence);
        comparisons.add(c);
    }
}
