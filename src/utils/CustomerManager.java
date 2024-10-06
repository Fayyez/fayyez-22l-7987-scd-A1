package utils;

import models.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;

public class CustomerManager {
    private final static String filepath = "src/database/custermerInfo.txt";
    public static void getListOfCustomers(ArrayList<Customer> customers) throws FileNotFoundException {
        // read all the customers in the customerInfo text file and return a list of customers
        try (FileReader fr = new FileReader(filepath);
             BufferedReader br = new BufferedReader(fr);) {
            String entry;
            while ((entry = br.readLine()) != null) {
                // read the data
                String[] data = entry.split(",");
                int cust_id = Integer.parseInt(data[0]);
                double cnic = Double.parseDouble(data[1]);
                String name = data[2];
                String address = data[3];
                String phone = data[4];
                boolean is_domestic = Boolean.parseBoolean(data[5]);
                String meter_type = data[6];
                String connection_date_string = data[7];
                Date connection_date = DateBuilder.getDateobj(connection_date_string);
                int units_consumed = Integer.parseInt(data[8]);
                int peak_units_consumed = Integer.parseInt(data[9]);
                // create appropriate customer object based on the meter type
                Customer cust = null;
                if(meter_type.equalsIgnoreCase("single")) {
                    cust = new OnePhaseCust(cust_id, cnic, name, address, phone, is_domestic, DateBuilder.getDateobj(connection_date_string), units_consumed);
                }
                else if(meter_type.equalsIgnoreCase("three")) {
                    cust = new ThreePhaseCust(cust_id, cnic, name, address, phone, is_domestic, DateBuilder.getDateobj(connection_date_string), units_consumed, peak_units_consumed);
                }
                else throw new IllegalArgumentException("Invalid meter type in customerInfo.txt");
                customers.add(cust);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void writeCustomerInfo(ArrayList<Customer> customers) {
        // write the customer info to the customerInfo text file
        try (FileWriter fw = new FileWriter(filepath);
             BufferedWriter bw = new BufferedWriter(fw);) {
            for (Customer cust : customers) {
                String meter_type = cust instanceof OnePhaseCust ? "single" : "three";
                int peak_units_consumed = cust instanceof ThreePhaseCust ? ((ThreePhaseCust) cust).getPeakUnitsConsumed() : 0;
                String entry = cust.getId() + "," + cust.getCnic() + "," + cust.getName() + "," + cust.getAddress() + "," + cust.getPhone() + "," + cust.getIsDomesticStr() + "," + meter_type + "," + DateBuilder.getDateStr(cust.getConnectionDateObj()) + "," + cust.getUnitsConsumed() + "," + peak_units_consumed;
                bw.write(entry);
                bw.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error writing to customers file");
        }
    }
    public static boolean addNewCustomerToFile(double cnic, String name, String address, String pno, boolean isDomestic, String meter_type, Date connection_date, int units_consumed, int peak_units_consumed) throws FileNotFoundException {
        // get all customers in the file in an array list of customers
        // check if entered id occurs 3 time, if yes then return false and display msg only 3 meters are allowed per cnic
        // if not then generate a 4 digit id based on the latest id in the file+1 and save to file using write operation
        ArrayList<Customer> customers = new ArrayList<>();
        CustomerManager.getListOfCustomers(customers);
        if(customers.isEmpty()) {
            System.out.println("Customer manager says there are no customers found in the records");
            return false;
        }
        int metercount = 0;
        for(Customer c: customers) {
            if(c.getCnic() == cnic) {
                metercount++;
            }
        }
        if(metercount>=3) {
            System.out.println("Customer manager says only 3 meters are allowed per cnic");
            return false;
        }
        int id = customers.getLast().getId()+1;
        if(meter_type.equalsIgnoreCase("single")) {
            customers.add(new OnePhaseCust(id, cnic, name, address, pno, isDomestic, connection_date, units_consumed));
        }
        else if(meter_type.equalsIgnoreCase("three")) {
            customers.add(new ThreePhaseCust(id, cnic, name, address, pno, isDomestic, connection_date, units_consumed, peak_units_consumed));
        }
        else throw new IllegalArgumentException("Invalid meter type");
        CustomerManager.writeCustomerInfo(customers);//update the text file.
        return true;
    }
    public static Customer getCustomer(int id, double cnic) {
        ArrayList<Customer> customers = new ArrayList<>();
        try {// read all the cusomters in a list
            CustomerManager.getListOfCustomers(customers);
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
        for(Customer c: customers) {
            if(c.getId() == id && c.getCnic() == cnic) {
                return c;
            }
        }
        throw new IllegalArgumentException("Customer not found");
    }
    public static Customer getCustomer(int id) {
        ArrayList<Customer> customers = new ArrayList<>();
        try {// read all the cusomters in a list
            CustomerManager.getListOfCustomers(customers);
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
        for(Customer c: customers) {
            if(c.getId() == id) {
                return c;
            }
        }
        throw new IllegalArgumentException("Customer not found");
    }
    public static Customer getCustomer(ArrayList<Customer> customers, int id) {
        for(Customer c: customers) {
            if(c.getId() == id) {
                return c;
            }
        }
        throw new IllegalArgumentException("Customer not found");
    }
}
