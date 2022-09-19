package com.carrier.modelexposer.baseline.baselines.examplemodel;

import com.carrier.modelexposer.baseline.BaseLine;

import java.util.ArrayList;
import java.util.List;

public class PhysicalActivity extends BaseLine {
    private static final String NAME = "Physical";

    protected PhysicalActivity(String active) {
        super(NAME + " " + active);
        evidence.put("physical_activity_score", active);
    }

    public static class MediumActive extends PhysicalActivity {
        public MediumActive() {
            super("medium");
        }
    }

    public static class HighActive extends PhysicalActivity {
        public HighActive() {
            super("high");
        }
    }

    public static List<BaseLine> generateBaseLines() {
        List<BaseLine> list = new ArrayList<>();
        list.add(new PhysicalActivity.MediumActive());
        list.add(new PhysicalActivity.HighActive());

        return list;
    }
}
