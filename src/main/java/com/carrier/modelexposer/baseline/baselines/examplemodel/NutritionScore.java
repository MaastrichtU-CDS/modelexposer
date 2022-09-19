package com.carrier.modelexposer.baseline.baselines.examplemodel;

import com.carrier.modelexposer.baseline.BaseLine;

import java.util.ArrayList;
import java.util.List;

public class NutritionScore extends BaseLine {
    private static final String NAME = "Nutrition";

    protected NutritionScore(String score) {
        super(NAME + " " + score);
        evidence.put("nutrition_score", score);
    }

    public static class MediumNutrition extends NutritionScore {
        public MediumNutrition() {
            super("medium");
        }
    }

    public static class HighNutrition extends NutritionScore {
        public HighNutrition() {
            super("high");
        }
    }

    public static List<BaseLine> generateBaseLines() {
        List<BaseLine> list = new ArrayList<>();
        list.add(new NutritionScore.MediumNutrition());
        list.add(new NutritionScore.HighNutrition());

        return list;
    }
}
