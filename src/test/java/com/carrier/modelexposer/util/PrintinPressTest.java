package com.carrier.modelexposer.util;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.carrier.modelexposer.util.PrintingPress.printCSV;
import static com.carrier.modelexposer.util.PrintingPress.readCsv;

public class PrintinPressTest {

    @Test
    public void splitSESWOA() {
        //not a unit test, used to split the seswoa into more manageable files
        List<List<String>> records = readCsv("resources/20230420_data_PC_SESWOA_2014.csv");

        List<List<String>> z0_1000 = new ArrayList<>();
        List<List<String>> z1000_2000 = new ArrayList<>();
        List<List<String>> z2000_3000 = new ArrayList<>();
        List<List<String>> z3000_4000 = new ArrayList<>();
        List<List<String>> z4000_5000 = new ArrayList<>();
        List<List<String>> z5000_6000 = new ArrayList<>();
        List<List<String>> z6000_7000 = new ArrayList<>();
        List<List<String>> z7000_8000 = new ArrayList<>();
        List<List<String>> z8000_9000 = new ArrayList<>();
        List<List<String>> z9000_10000 = new ArrayList<>();

        for (List<String> record : records) {
            if (record.get(0).contains("addre")) {
                z0_1000.add(record);
                z2000_3000.add(record);
                z3000_4000.add(record);
                z4000_5000.add(record);
                z5000_6000.add(record);
                z6000_7000.add(record);
                z7000_8000.add(record);
                z8000_9000.add(record);
                z9000_10000.add(record);
                continue;
            }
            if (Integer.valueOf(record.get(0).substring(0, 4)) < 1000) {
                z0_1000.add(record);
            } else if (Integer.valueOf(record.get(0).substring(0, 4)) < 2000) {
                z1000_2000.add(record);
            } else if (Integer.valueOf(record.get(0).substring(0, 4)) < 3000) {
                z2000_3000.add(record);
            } else if (Integer.valueOf(record.get(0).substring(0, 4)) < 4000) {
                z3000_4000.add(record);
            } else if (Integer.valueOf(record.get(0).substring(0, 4)) < 5000) {
                z4000_5000.add(record);
            } else if (Integer.valueOf(record.get(0).substring(0, 4)) < 6000) {
                z5000_6000.add(record);
            } else if (Integer.valueOf(record.get(0).substring(0, 4)) < 7000) {
                z6000_7000.add(record);
            } else if (Integer.valueOf(record.get(0).substring(0, 4)) < 8000) {
                z7000_8000.add(record);
            } else if (Integer.valueOf(record.get(0).substring(0, 4)) < 9000) {
                z8000_9000.add(record);
            } else if (Integer.valueOf(record.get(0).substring(0, 4)) < 10000) {
                z9000_10000.add(record);
            }
        }

        print(z0_1000, "resources/seswoa_0_1000.csv");
        print(z1000_2000, "resources/seswoa_1000_2000.csv");
        print(z2000_3000, "resources/seswoa_2000_3000.csv");
        print(z3000_4000, "resources/seswoa_3000_4000.csv");
        print(z4000_5000, "resources/seswoa_4000_5000.csv");
        print(z5000_6000, "resources/seswoa_5000_6000.csv");
        print(z6000_7000, "resources/seswoa_6000_7000.csv");
        print(z7000_8000, "resources/seswoa_7000_8000.csv");
        print(z8000_9000, "resources/seswoa_8000_9000.csv");
        print(z9000_10000, "resources/seswoa_9000_10000.csv");

    }

    public void print(List<List<String>> records, String path) {
        List<String> data = new ArrayList<>();
        for (List<String> record : records) {
            String d = record.get(0) + "," + record.get(1) + "," + record.get(2);
            data.add(d);
        }
        printCSV(data, path);
    }
}
