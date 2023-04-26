package com.carrier.modelexposer.util;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.carrier.modelexposer.util.PrintingPress.printCSV;
import static com.carrier.modelexposer.util.PrintingPress.readCsv;

public class PrintinPressTest {

    @Test
    public void splitSESWOA() {
        //not a unit test, used to split the seswoa into two more manageable files
        List<List<String>> seswoa = readCsv("resources/20230420_data_PC_SESWOA_2014.csv");

        List<String> csv1 = new ArrayList<>();
        List<String> csv2 = new ArrayList<>();

        String header = "";
        for (int i = 0; i < seswoa.get(0).size(); i++) {
            header += seswoa.get(0).get(i);
            if (i < seswoa.get(0).size() - 1) {
                header += ",";
            }
        }

        csv1.add(header);
        csv2.add(header);
        int j = 0;
        for (List<String> row : seswoa) {
            j++;
            String rec = "";
            for (int i = 0; i < row.size(); i++) {
                rec += row.get(i);
                if (i < row.size() - 1) {
                    rec += ",";
                }
            }
            if (j % 2 == 0) {
                csv1.add(rec);
            } else {
                csv2.add(rec);
            }
        }

        printCSV(csv1, "resources/seswoa_1.csv");
        printCSV(csv2, "resources/seswoa_2.csv");
    }
}
