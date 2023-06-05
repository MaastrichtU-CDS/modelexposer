package com.carrier.modelexposer.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.carrier.modelexposer.util.PrintingPress.readCsv;

public class SESWOAMapper {
    private Map<String, Map<String, String>> seswoa;
    private static final double DEFAULT_SESWOA = 0.096;

    private List<String> paths;

    public SESWOAMapper(List<String> paths) {
        this.paths = paths;
        if (paths != null) {
            initSESWOA();
        }
    }

    public void initSESWOA() {
        List<List<String>> records = new ArrayList<>();
        for (String path : paths) {
            records.addAll(readCsv(path));
        }
        seswoa = new HashMap<>();
        for (int i = 1; i < records.size(); i++) {
            // row 1 = header
            // column 1 = postal code, column 2 = number, column 3 = SESWOA
            List<String> record = records.get(i);
            if (!seswoa.containsKey(record.get(0))) {
                Map<String, String> postalcode = new HashMap<>();
                postalcode.put(record.get(1), record.get(2));
                seswoa.put(record.get(0), postalcode);
            } else {
                Map<String, String> postalcode = seswoa.get(record.get(0));
                postalcode.put(record.get(1), record.get(2));
            }
        }
    }

    public Double findSESWOA(String postal, String number) {
        try {
            String value = seswoa.computeIfPresent(postal, (k, v) -> {
                return v;
            }).computeIfPresent(number, (k, v) -> {
                return v;
            });
            return value == null ? DEFAULT_SESWOA : Double.parseDouble(value);
        } catch (Exception e) {
            //unknown adress return default value
            return DEFAULT_SESWOA;
        }
    }
}
