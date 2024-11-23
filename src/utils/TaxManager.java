package utils;

import java.io.BufferedReader;
import java.io.FileReader;

public class TaxManager { // is a singleton class
    private static TaxManager instance; // static for singleton pattern
    private int[] regUnitPrices;
    private int[] peakUnitPrices;
    private float[] taxPercentages;
    private int[] fixedCharges;
    private final static String filepath = "src/database/taxInfo.txt";
    // constant static vars
    static final int ONE_PHASE_DOMESTIC = 0;
    static final int ONE_PHASE_COMMERCIAL = 1;
    static final int THREE_PHASE_DOMESTIC = 2;
    static final int THREE_PHASE_COMMERCIAL = 3;

    // Private constructor for singleton pattern
    private TaxManager() {
        regUnitPrices = new int[4];
        peakUnitPrices = new int[4];
        taxPercentages = new float[4];
        fixedCharges = new int[4];
        loadData();
    }

    public void loadData() {
        try {
            FileReader fr = new FileReader(filepath);
            BufferedReader br = new BufferedReader(fr);
            String entry;
            int i = 0;
            while ((entry = br.readLine()) != null) {
                if (i >= 4) {
                    throw new IllegalArgumentException("Too many entries in tax info file");
                }

                String[] data = entry.split(",");
                // For rows with 1-phase meters, handle missing peak unit price (blank)
                regUnitPrices[i] = Integer.parseInt(data[1]);

                // Only add peak unit price if it's a 3-phase meter row
                if (data[0].startsWith("3Phase")) {
                    peakUnitPrices[i] = Integer.parseInt(data[2]);
                }
                else {
                    peakUnitPrices[i] = 0; // No peak unit price for 1-phase meters
                }
                taxPercentages[i] = Float.parseFloat(data[3]);
                fixedCharges[i] = Integer.parseInt(data[4]);
                i++;
            }
            br.close();
            fr.close();
        } catch (Exception e) {
            System.out.println("Error reading tax info file: " + e.getMessage());
        }
    }

    // Public method to get the singleton instance
    public static TaxManager getInstance() {
        if (instance == null) {
            instance = new TaxManager();
        }
        return instance;
    }

    // Methods for getting desired indices
    public int get1phaseDomesticIndex() {
        return 0;
    }
    public int get3phaseDomesticIndex() {
        return 2;
    }
    public int get1phaseCommercialIndex() {
        return 1;
    }
    public int get3phaseCommercialIndex() {
        return 3;
    }
    // Getters
    public int getRegUnitPrice(int index) {
        return regUnitPrices[index];
    }
    public int getPeakUnitPrice(int index) {
        return peakUnitPrices[index];
    }
    public float getTaxPercentage(int index) {
        return taxPercentages[index];
    }
    public int getFixedCharge(int index) {
        return fixedCharges[index];
    }
    // Setters
    public void setRegUnitPrice(int index, int price) {
        if (index < 0 || index > 3) {
            throw new IllegalArgumentException("Invalid index while setting price in tax manager");
        }
        regUnitPrices[index] = price;
    }
    public void setPeakUnitPrice(int index, int price) {
        if (index < 0 || index > 3) {
            throw new IllegalArgumentException("Invalid index while setting price in tax manager");
        }
        peakUnitPrices[index] = price;
    }
    public void setFixedCharges(int index, int charges) {
        if (index < 0 || index > 3) {
            throw new IllegalArgumentException("Invalid index while setting price in tax manager");
        }
        fixedCharges[index] = charges;
    }
    public void setTaxPercentage(int index, float percentage) {
        if (index < 0 || index > 3) {
            throw new IllegalArgumentException("Invalid index while setting price in tax manager");
        }
        taxPercentages[index] = percentage;
    }
    // printing
    public void printSingleTaxSummary(int index) {
        System.out.println("Tax Summary:");
        System.out.println("1-Phase Domestic: Regular Unit Price: " + regUnitPrices[index] + " Peak Unit Price: "
                + peakUnitPrices[index] + " Tax Percentage: " + taxPercentages[index] + " Fixed Charges: " + fixedCharges[index]);
    }
    public void printTaxSummary() {
        System.out.println("Tax Summary:");
        System.out.println("1-Phase Domestic: Reg Unit Price: " + regUnitPrices[0] + " Tax Percentage: " + taxPercentages[0] + " Fixed Charges: " + fixedCharges[0]);
        System.out.println("1-Phase Commercial: Reg Unit Price: " + regUnitPrices[1] + " Tax Percentage: " + taxPercentages[1] + " Fixed Charges: " + fixedCharges[1]);
        System.out.println("3-Phase Domestic: Reg Unit Price: " + regUnitPrices[2] + " Peak Unit Price: "
                + peakUnitPrices[2] + " Tax Percentage: " + taxPercentages[2] + " Fixed Charges: " + fixedCharges[2]);
        System.out.println("3-Phase Commercial: Reg Unit Price: " + regUnitPrices[3] + " Peak Unit Price: "
                + peakUnitPrices[3] + " Tax Percentage: " + taxPercentages[3] + " Fixed Charges: " + fixedCharges[3]);
    }

    public void updateTaxInfo(int choice, int regUnitPrice, int peakUnitPrice, float taxPercentage, int fixedCharges) {
        // set values using the setter
        setFixedCharges(choice,fixedCharges);
        setTaxPercentage(choice, taxPercentage);
        setPeakUnitPrice(choice, peakUnitPrice);
        setRegUnitPrice(choice, regUnitPrice);
    }
}
