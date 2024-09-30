package utils;

import models.Cnic;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class CNICManager {
    private static final String filepath = "src/database/cnics.txt";
    public static void getAllCnics(ArrayList<Cnic> cnics) {
        try (FileReader fr = new FileReader(filepath);
             BufferedReader br = new BufferedReader(fr);) {
            String entry;
            while ((entry = br.readLine()) != null) {
                String[] data = entry.split(",");
                String number = data[0];
                String expiry_date = data[1];
                Cnic cnic = new Cnic(number, expiry_date);
                cnics.add(cnic);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error reading cnics file");
        }
    }
    public static void writeCnicInfo(ArrayList<Cnic> cnics) {
        try (FileWriter fw = new FileWriter(filepath);
             BufferedWriter bw = new BufferedWriter(fw);) {
            for (Cnic cnic : cnics) {
                String entry = cnic.getNumber() + "," + cnic.getDateStr();
                bw.write(entry);
                bw.newLine();
            }
        } catch (Exception e) {
            throw new RuntimeException("Error writing to cnics file");
        }
    }
    public static ArrayList<Cnic> getCnicsAboutToExpireIn30Days() throws Exception {
        ArrayList<Cnic> cnics = new ArrayList<>();
        CNICManager.getAllCnics(cnics);
        ArrayList<Cnic> expiringCnics = new ArrayList<>();
        for (Cnic cnic : cnics) {
            if (DateBuilder.lessThan30DaysDifference(cnic.getDate())) {
                expiringCnics.add(cnic);
            }
        }
        return expiringCnics;
    }
}
