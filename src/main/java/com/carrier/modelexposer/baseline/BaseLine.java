package com.carrier.modelexposer.baseline;

import com.carrier.modelexposer.baseline.baselines.examplemodel.ExSmoker;
import com.carrier.modelexposer.baseline.baselines.examplemodel.NutritionScore;
import com.carrier.modelexposer.baseline.baselines.examplemodel.PhysicalActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BaseLine {
    protected Map<String, String> evidence;
    private String name;

    public BaseLine(String name) {
        this.name = name;
        evidence = new HashMap<>();
    }

    public Map<String, String> getEvidence() {
        return evidence;
    }

    public String getName() {
        return name;
    }

    public static List<Map<String, String>> collectExampleBaseLinesEvidences() {
        List<BaseLine> baseLines = new ArrayList<>();
        baseLines.addAll(ExSmoker.generateBaseLines());
        baseLines.addAll(NutritionScore.generateBaseLines());
        baseLines.addAll(PhysicalActivity.generateBaseLines());

        return baseLines.stream().map(x -> x.getEvidence()).collect(Collectors.toList());
    }
}
