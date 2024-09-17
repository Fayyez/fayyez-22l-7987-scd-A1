package utils;

import models.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;

//4 digit customer id (borrowed from the CustomersInfo file),
//Billing month, Current Meter Reading Regular, Current Meter
//Reading Peak, Reading Entry Date in format DD/MM/YYYY (cannot be in future, issue date of the bill),
//Cost of electricity, Sales Tax Amount, Fixed Charges, Total Billing
//amount, Due Date in format DD/MM/YYYY (7 calendar days after the entry of Current Meter
//Reading), Bill Paid Status (Paid or Unpaid), Bill payment Date in format DD/MM/YYYY (cannot be before Reading Entry Date)

public class BillManager {
    private final static String filepath = "src/database/bills.txt";

    public static void getListOfBills(ArrayList<Bill> bills) throws FileNotFoundException {
        // read all the bills in the bills text file and return a list of bills
        try (FileReader fr = new FileReader(filepath);
             BufferedReader br = new BufferedReader(fr);) {
            String entry;
            while ((entry = br.readLine()) != null) {
                // read the data
                String[] data = entry.split(",");
                int cust_id = Integer.parseInt(data[0]);
                int billingmonth = Integer.parseInt(data[1]);
                int current_reg_reading = Integer.parseInt(data[2]);
                int current_peak_reading = Integer.parseInt(data[3]);
                Date issueDate = DateBuilder.getDateobj(data[4]);
                int cost = Integer.parseInt(data[5]);
                float taxAmount = Float.parseFloat(data[6]);
                int fixedcharges = Integer.parseInt(data[7]);
                int totalbill = Integer.parseInt(data[8]);
                Date dueDate = DateBuilder.getDateobj(data[9]);
                boolean isPaid = Boolean.parseBoolean(data[10]);
                Date paidDate = DateBuilder.getDateobj(data[11]);
                if(paidDate.equals("not paid yet")) paidDate = null;
                // create appropriate bill object
                Bill bill = new Bill(cust_id, billingmonth, current_reg_reading, current_peak_reading, issueDate, cost, taxAmount, fixedcharges, totalbill, dueDate, isPaid, paidDate);
                bills.add(bill);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void writeBillInfo(ArrayList<Bill> bills) {
        // write the bill info to the bills text file
        try (FileWriter fw = new FileWriter(filepath);
             BufferedWriter bw = new BufferedWriter(fw);) {
            for (Bill bill : bills) {
                String paiddate;
                if (!bill.getIsPaid())  paiddate = "not paid yet";
                else paiddate = DateBuilder.getDateStr(bill.getPaidDate());
                String entry = bill.getCustomerID() + "," + bill.getBillingmonth() + "," + bill.getCurrent_reg_reading() + "," + bill.getCurrent_peak_reading() + "," + DateBuilder.getDateStr(bill.getIssueDate()) + "," + bill.getCost() + "," + bill.getTaxAmount() + "," + bill.getFixedcharges() + "," + bill.getTotalbill() + "," + DateBuilder.getDateStr(bill.getDueDate()) + "," + bill.getIsPaid() + "," + paiddate;
                bw.write(entry);
                bw.newLine();
            }
        } catch (Exception e) {
            throw new RuntimeException("Error writing to bills file");
        }
    }
    public static boolean addBill(int cust_id, int billingmonth, int current_reg_reading, int current_peak_reading, Date issueDate, int cost, float taxAmount, int fixedcharges, int totalbill, Date dueDate, boolean isPaid, Date paidDate) throws FileNotFoundException {
        // get all bills in a list
        // create new bill and add to the list and write to file
        ArrayList<Bill> bills = new ArrayList<>();
        BillManager.getListOfBills(bills);
        Bill bill = new Bill(cust_id, billingmonth, current_reg_reading, current_peak_reading, issueDate, cost, taxAmount, fixedcharges, totalbill, dueDate, isPaid, paidDate);
        bills.add(bill);
        BillManager.writeBillInfo(bills);
        return true;
    }
    public static ArrayList<Bill> getAllBillsOfCustomer(int cust_id) throws FileNotFoundException {
        // get all bills of a customer
        ArrayList<Bill> bills = new ArrayList<>();
        BillManager.getListOfBills(bills);//read all bills in file
        ArrayList<Bill> customerBills = new ArrayList<>();
        for (Bill bill : bills) {// filter the bills of the customer passed
            if (bill.getCustomerID() == cust_id) {
                customerBills.add(bill);
            }
        }
        return customerBills;
    }
    public static ArrayList<Bill> getAllPaidBills(ArrayList<Bill> allbills) throws FileNotFoundException {
        // get all paid bills from the bills list passed as arg
        ArrayList<Bill> paidBills = new ArrayList<>();
        for (Bill bill : allbills) {
            if (bill.getIsPaid()) {
                paidBills.add(bill);
            }
        }
        return paidBills;
    }
    public static ArrayList<Bill> getAllUnpaidBills(ArrayList<Bill> bills) throws FileNotFoundException {
        // get all unpaid bills
        ArrayList<Bill> unpaidBills = new ArrayList<>();
        for (Bill bill : bills) {
            if (!bill.getIsPaid()) {
                unpaidBills.add(bill);
            }
        }
        return unpaidBills;
    }
}
