package com.carrier.modelexposer.util;

import java.util.HashMap;
import java.util.List;

import static com.carrier.modelexposer.util.PrintingPress.readCsv;

public class SESWOAMapper {
    private static final double DEFAULT_SESWOA = 0.096;
    private String path = "resources/seswoa_";

    public SESWOAMapper() {

    }

    public SESWOAMapper(String path) {
        this.path = path;
    }

    public HashMap<String, HashMap<Integer, Double>> initSESWOA(String id) {
        List<List<String>> records = readCsv(path + id + ".csv");
        HashMap<String, HashMap<Integer, Double>> seswoa = new HashMap<>();
        for (int i = 1; i < records.size(); i++) {

            // row 1 = header
            // column 1 = postal code, column 2 = number, column 3 = SESWOA
            List<String> record = records.get(i);

            if (record.get(2).equals("NA") || record.get(1).equals("address_house_number")) {
                // apparently there's null values in there.
                // also occasionally there'll be a second header in there due to the split over multiple files
                continue;
            }

            if (!seswoa.containsKey(record.get(0))) {
                HashMap<Integer, Double> postalcode = new HashMap<>();

                try {
                    postalcode.put(Integer.valueOf(record.get(1)), Double.valueOf(record.get(2)));
                } catch (Exception e) {
                    System.out.println(e);
                }
                seswoa.put(record.get(0), postalcode);
            } else {
                if (record.get(2).equals("NA") || record.get(1).equals("address_house_number")) {
                    // apparently there's null values in there.
                    // also occasionally there'll be a second header in there due to the split over multiple files
                    continue;
                }
                HashMap<Integer, Double> postalcode = seswoa.get(record.get(0));
                postalcode.put(Integer.valueOf(record.get(1)), Double.valueOf(record.get(2)));
            }
        }
        return seswoa;
    }

    public Double findSESWOA(String postal, String number) {

        HashMap<String, HashMap<Integer, Double>> seswoa = initSESWOA(determineFile(postal));

        try {
            Double value = seswoa.computeIfPresent(postal, (k, v) -> {
                return v;
            }).computeIfPresent(Integer.valueOf(number), (k, v) -> {
                return v;
            });
            return value == null ? DEFAULT_SESWOA : value;
        } catch (Exception e) {
            //unknown adress return default value
            return DEFAULT_SESWOA;
        }
    }

    @SuppressWarnings ("checkstyle:magicNumber")
    private String determineFile(String postal) {
        if (Integer.valueOf(postal.substring(0, 4)) < 1000) {
            return "0_1000";
        } else if (Integer.valueOf(postal.substring(0, 4)) < 2000) {
            return "1000_2000";
        } else if (Integer.valueOf(postal.substring(0, 4)) < 3000) {
            return "2000_3000";
        } else if (Integer.valueOf(postal.substring(0, 4)) < 4000) {
            return "3000_4000";
        } else if (Integer.valueOf(postal.substring(0, 4)) < 5000) {
            return "4000_5000";
        } else if (Integer.valueOf(postal.substring(0, 4)) < 6000) {
            return "5000_6000";
        } else if (Integer.valueOf(postal.substring(0, 4)) < 7000) {
            return "6000_7000";
        } else if (Integer.valueOf(postal.substring(0, 4)) < 8000) {
            return "7000_8000";
        } else if (Integer.valueOf(postal.substring(0, 4)) < 9000) {
            return "8000_9000";
        } else {
            return "9000_10000";
        }
    }
}
