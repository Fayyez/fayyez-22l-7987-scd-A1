package utils;

import models.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;

public class BillManager {
    private final static String filename = "bills.txt";
//    public static void getListOfBills(ArrayList<Bill> bills) throws FileNotFoundException {
//        // read all the bills in the bills text file and return a list of bills
//        try (FileReader fr = new FileReader(filepath);
//             BufferedReader br = new BufferedReader(fr);) {
//            String entry;
//            while ((entry = br.readLine()) != null) {
//                // read the data
//                String[] data = entry.split(",");
//                int cust_id = Integer.parseInt(data[0]);
//                int billingmonth = Integer.parseInt(data[1]);
//                int current_reg_reading = Integer.parseInt(data[2]);
//                int current_peak_reading = Integer.parseInt(data[3]);
//                Date issueDate = DateBuilder.getDateobj(data[4]);
//                int cost = Integer.parseInt(data[5]);
//                float taxAmount = Float.parseFloat(data[6]);
//                int fixedcharges = Integer.parseInt(data[7]);
//                int totalbill = Integer.parseInt(data[8]);
//                Date dueDate = DateBuilder.getDateobj(data[9]);
//                boolean isPaid = Boolean.parseBoolean(data[10]);
//                String paidDatestr = (data[11]);
//                Date paidDate;
//                if(paidDatestr.equals("not paid yet")) paidDate = null;
//                else paidDate = DateBuilder.getDateobj(paidDatestr);
//                // create appropriate bill object
//                Bill bill = new Bill(cust_id, billingmonth, current_reg_reading, current_peak_reading, issueDate, cost, taxAmount, fixedcharges, totalbill, dueDate, isPaid, paidDate);
//                bills.add(bill);
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
    public static void getListOfBills(ArrayList<Bill> bills) throws Exception {
        // read all the bills in the bills text file and return a list of bills
        try  {
            ArrayList<String> alldata = DBClient.readFromFile(filename);
            for (String entry: alldata) {
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
                String paidDatestr = (data[11]);
                Date paidDate;
                if(paidDatestr.equals("not paid yet")) paidDate = null;
                else paidDate = DateBuilder.getDateobj(paidDatestr);
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
        ArrayList<String> dataToWrite = new ArrayList<>();
        try {
            for (Bill bill : bills) {
                String paiddate;
                if (!bill.getIsPaid())  paiddate = "not paid yet";
                else paiddate = DateBuilder.getDateStr(bill.getPaidDate());
                dataToWrite.add(bill.getCustomerID() + "," + bill.getBillingmonth() + "," + bill.getCurrent_reg_reading() +
                        "," + bill.getCurrent_peak_reading() + "," + DateBuilder.getDateStr(bill.getIssueDate()) + "," + bill.getCost() +
                        "," + bill.getTaxAmount() + "," + bill.getFixedcharges() + "," + bill.getTotalbill() + "," +
                        DateBuilder.getDateStr(bill.getDueDate()) + "," + bill.getIsPaid() + "," + paiddate);
            }
            DBClient.wrtieToFile(filename, dataToWrite);
        } catch (Exception e) {
            throw new RuntimeException("Error writing to bills file");
        }
    }
    public static boolean addBill(int cust_id, int billingmonth, int current_reg_reading, int current_peak_reading, Date issueDate, int cost, float taxAmount, int fixedcharges, int totalbill, Date dueDate, boolean isPaid, Date paidDate) throws Exception {
        // get all bills in a list
        ArrayList<Bill> bills = new ArrayList<>();
        BillManager.getListOfBills(bills);
        // create new bill and add to the list and write to file
        Bill bill = new Bill(cust_id, billingmonth, current_reg_reading, current_peak_reading, issueDate, cost, taxAmount, fixedcharges, totalbill, dueDate, isPaid, paidDate);
        bills.add(bill);
        BillManager.writeBillInfo(bills);
        return true;
    }
    public static ArrayList<Bill> getAllBillsOfCustomer(int cust_id) throws Exception {
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
    public static Bill getBill(int cust_id, int billingmonth) throws Exception {
        // get a bill of a customer with a billing month
        ArrayList<Bill> bills = new ArrayList<>();
        BillManager.getListOfBills(bills);
        for (Bill bill : bills) {
            if (bill.getCustomerID() == cust_id && bill.getBillingmonth() == billingmonth) {
                return bill;
            }
        }
        return null;
    }
    public static Bill getBill(ArrayList<Bill> bills, int cust_id, int billingmonth) {
        // get a bill of a customer with a billing month
        for (Bill bill : bills) {
            if (bill.getCustomerID() == cust_id && bill.getBillingmonth() == billingmonth) {
                return bill;
            }
        }
        return null;
    }
    public static Bill getLatestBill(int cust_id) throws Exception {
        // get the latest bill of a customer
        ArrayList<Bill> bills = new ArrayList<>();
        try {
            BillManager.getListOfBills(bills);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Error reading bills file");
        }
        Bill latestBill = null;
        for (Bill bill : bills) {
            if (bill.getCustomerID() == cust_id) {
                if (latestBill == null) {
                    latestBill = bill;
                } else {
                    if(bill.getIssueDate().after(latestBill.getIssueDate())) {
                        latestBill = bill;
                    }
                }
            }
        }
        return latestBill;
    }
    public static boolean setBillToPaid(int custId, int billingmonth) throws Exception {
        // read all the bills and match the bill with the customer id and billing month
        ArrayList<Bill> bills = new ArrayList<>();
        Bill bill = null;
        try {
            BillManager.getListOfBills(bills);
            bill = BillManager.getBill(bills, custId, billingmonth);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Error reading bills file");
        }
        if(bill==null) {
            System.out.println("Bill not found while setting to paid status");
            return false;
        }
        ArrayList<Customer> allCustomers = new ArrayList<>();
        try {
            CustomerManager.getListOfCustomers(allCustomers);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Error reading customers file");
        }
        Customer customer = CustomerManager.getCustomer(allCustomers, custId);
        customer.setUnitsConsumed(bill.getCurrent_reg_reading());
        if (customer instanceof ThreePhaseCust) {
            ThreePhaseCust threePhaseCust = (ThreePhaseCust) customer;
            threePhaseCust.setPeakUnitsConsumed(bill.getCurrent_peak_reading());
        }
        // set bill status to paid and write/update customers and bills records
        bill.setIsPaid(true);
        bill.setPaidDate(new Date());
        BillManager.writeBillInfo(bills);
        return true;
    }
}
