package com.neu.edu.stocktrading.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileReaderUtil {
    public static String readServiceChargeValue() {
        String expected_value = "";
        String file = "src/main/resources/admin.txt";

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            reader = null;
        }

        if (reader != null) 
        {
            try {
                expected_value = reader.readLine();
            } catch (IOException e) 
            {
                expected_value = "10";
                e.printStackTrace();
            } finally {
                try {
                    reader.close();
                } catch (IOException e) 
                {
                    expected_value = "10";
                    e.printStackTrace();
                }
            }

        }
        return expected_value;
    }

    public static void writeServiceChargeValue (String val)
    {
        try (FileWriter writer = new FileWriter("src/main/resources/admin.txt");
             BufferedWriter bw = new BufferedWriter(writer)) {

            bw.write(val);

        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }
    }


}