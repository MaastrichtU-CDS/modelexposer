package com.carrier.modelexposer.baseline;

import com.carrier.modelexposer.baseline.baselines.examplemodel.ExSmoker;
import com.carrier.modelexposer.baseline.baselines.examplemodel.NutritionScore;
import com.carrier.modelexposer.baseline.baselines.examplemodel.PhysicalActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public static Map<String, Map<String, String>> collectExampleBaseLinesEvidences() {
        List<List<BaseLine>> baseLines = new ArrayList<>();
        baseLines = addBaseLines(baseLines, ExSmoker.generateBaseLines());
        baseLines = addBaseLines(baseLines, NutritionScore.generateBaseLines());
        baseLines = addBaseLines(baseLines, PhysicalActivity.generateBaseLines());

        return generateEvidences(baseLines);
    }

    private static Map<String, Map<String, String>> generateEvidences(List<List<BaseLine>> lists) {
        Map<String, Map<String, String>> evidences = new HashMap<>();
        for (List<BaseLine> baseline : lists) {
            String name = "";
            Map<String, String> evidence = new HashMap<>();
            for (BaseLine base : baseline) {
                if (name.length() > 0) {
                    name += " ";
                }
                name += base.getName();
                evidence.putAll(base.getEvidence());
            }
            evidences.put(name, evidence);
        }
        return evidences;
    }

    private static List<List<BaseLine>> addBaseLines(List<List<BaseLine>> baselines, List<BaseLine> toBeAdded) {
        List<List<BaseLine>> combos = new ArrayList<>();
        combos.addAll(baselines);
        for (int i = 0; i < baselines.size(); i++) {
            for (int j = 0; j < toBeAdded.size(); j++) {
                List<BaseLine> listCopy = new ArrayList<>();
                listCopy.addAll(baselines.get(i));
                listCopy.add(toBeAdded.get(j));
                combos.add(listCopy);
            }
        }
        for (BaseLine base : toBeAdded) {
            List<BaseLine> list = new ArrayList<>();
            list.add(base);
            combos.add(list);
        }
        return combos;
    }
}
