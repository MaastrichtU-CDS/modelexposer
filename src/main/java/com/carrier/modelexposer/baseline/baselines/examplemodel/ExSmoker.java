package com.carrier.modelexposer.baseline.baselines.examplemodel;

import com.carrier.modelexposer.baseline.BaseLine;

import java.util.ArrayList;
import java.util.List;

public class ExSmoker extends BaseLine {
    private static final String NAME = "ex-Smoker";

    public ExSmoker() {
        super(NAME);
        evidence.put("smoking_status", "ex_smoker");
    }

    public static List<BaseLine> generateBaseLines() {
        List<BaseLine> list = new ArrayList<>();
        list.add(new ExSmoker());
        return list;
    }
}
