import utils.*;
import models.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class TerminalApp {
    // employee menu options implementation
    public void addCustomer(Scanner scanner) throws FileNotFoundException {
        // add new customer
        System.out.println("Enter CNIC: ");
        double cnic = Double.parseDouble(scanner.nextLine());
        System.out.println("Enter Full Name: ");
        String name = scanner.nextLine();
        System.out.println("Enter Address: ");
        String address = scanner.nextLine();
        System.out.println("Enter Phone: ");
        String phone = scanner.nextLine();
        System.out.println("Enter 1 for Domestic, 0 for Commercial: ");
        boolean isDomestic = Boolean.parseBoolean(scanner.nextLine());
        System.out.println("Enter 1 for Single Phase, 0 for Three Phase: ");
        boolean metertype_bool = Boolean.parseBoolean(scanner.nextLine());
        String metertype = "single";
        if (!metertype_bool) metertype = "three";
        System.out.println("Enter Connection Date (DD/MM/YYYY): ");
        String connectionDateStr = scanner.nextLine();
        Date connectionDate = DateBuilder.getDateobj(connectionDateStr);
        CustomerManager.addNewCustomerToFile(cnic, name, address, phone, isDomestic, metertype, connectionDate, 0, 0);
    }
    public int getIndex(Customer c) {
        TaxManager taxManager = TaxManager.getInstance();
        if (c instanceof OnePhaseCust) {
            if(c.getIsDomestic()) {
                return taxManager.get1phaseDomesticIndex();
            }
            else  return taxManager.get1phaseCommercialIndex();
        }
        else if (c instanceof ThreePhaseCust) {
            if (c.getIsDomestic()) {
                return taxManager.get3phaseDomesticIndex();
            }
            else return taxManager.get3phaseCommercialIndex();
        }
        return 0;// default return 1phase single meter
    }
    public void generateBill(Scanner scanner) throws FileNotFoundException {
        // get the cutsomer whose bill is to be added
        System.out.println("Enter Customer ID: ");
        int cust_id = Integer.parseInt(scanner.nextLine());
        // check if customer exists and get the customer
        Customer cust = CustomerManager.getCustomer(cust_id);
        // billing month
        System.out.println("Enter Billing Month: ");
        int billingmonth = Integer.parseInt(scanner.nextLine());
        if(billingmonth<1 || billingmonth>12) {
            throw new IllegalArgumentException("Invalid billing month entered while genrating new bill");
        }
        // current regular reading
        System.out.println("Enter Current Regular Reading: ");
        int current_reg_reading = Integer.parseInt(scanner.nextLine());
        if(current_reg_reading<0 || current_reg_reading < cust.getUnitsConsumed()) {
            throw new IllegalArgumentException("Invalid current regular reading entered while genrating new bill");
        }
        Date issueDate = new Date();
        int tax_index = this.getIndex(cust);
        TaxManager taxManager = TaxManager.getInstance();
        int reg_units_consumed = current_reg_reading - cust.getUnitsConsumed();
        int peak_units_consumed = 0;
        if (cust instanceof ThreePhaseCust) {
            System.out.println("Enter Current Peak Reading: ");
            int current_peak_reading = Integer.parseInt(scanner.nextLine());
            peak_units_consumed = current_peak_reading - ((ThreePhaseCust) cust).getPeakUnitsConsumed();
        }
        int cost = reg_units_consumed * taxManager.getRegUnitPrice(tax_index) + peak_units_consumed * taxManager.getPeakUnitPrice(tax_index);
        float taxAmount = (cost * taxManager.getTaxPercentage(tax_index)) / 100;
        int fixedcharges = taxManager.getFixedCharge(tax_index);
        int totalbill = cost + (int)taxAmount + fixedcharges;
        Date dueDate = DateBuilder.add7Days(issueDate);
        boolean isPaid = false;
        BillManager.addBill(cust_id, billingmonth, current_reg_reading, peak_units_consumed, issueDate, cost, taxAmount, fixedcharges, totalbill, dueDate, isPaid, null);
    }
    public void displayExpiringCnics() {
        try {
            ArrayList<Cnic> expiringCnics = CNICManager.getCnicsAboutToExpireIn30Days();
            if(expiringCnics.size() == 0) {
                System.out.println("No CNICs are expiring in next 30 days");
                return;
            }
            for (Cnic cnic : expiringCnics) {
                System.out.println(cnic);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public void setBillToPaid() throws FileNotFoundException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter Customer ID: ");
        int cust_id = Integer.parseInt(scanner.nextLine());
        System.out.println("Enter Billing Month: ");
        int billingmonth = Integer.parseInt(scanner.nextLine());
        boolean response = BillManager.setBillToPaid(cust_id, billingmonth);
        if(response) {
            System.out.println("Bill set to paid successfully");
        }
        else {
            System.out.println("Bill not updated.");
        }
    }
    public void updateSalexTaxInfo() {
        Scanner scanner = new Scanner(System.in);
        TaxManager taxManager = TaxManager.getInstance();
        System.out.println("<Enter 1> for 1 Phase Domestic");
        System.out.println("<Enter 2> for 1 Phase Commercial");
        System.out.println("<Enter 3> for 3 Phase Domestic");
        System.out.println("<Enter 4> for 3 Phase Commercial");
        int choice = Integer.parseInt(scanner.nextLine()) - 1;
        System.out.println("Enter Regular Unit Price: ");
        int reg_unit_price = Integer.parseInt(scanner.nextLine());
        int peak_unit_price = 0;
        if(choice > 1) {
            System.out.println("Enter Peak Unit Price: ");
            peak_unit_price = Integer.parseInt(scanner.nextLine());
        }
        System.out.println("Enter Tax Percentage: ");
        float tax_percentage = Float.parseFloat(scanner.nextLine());
        System.out.println("Enter Fixed Charges: ");
        int fixed_charges = Integer.parseInt(scanner.nextLine());
        taxManager.updateTaxInfo(choice, reg_unit_price, peak_unit_price, tax_percentage, fixed_charges);
    }
    // Menus
    public void EmployeeMenu(Employee current_employee) {
        System.out.println("Welcome " + current_employee.getUsername() + " to the Employee Menu");
        int choice = 0;
        Scanner scanner = new Scanner(System.in);
        do {
            System.out.println("1. Add new customer");
            System.out.println("2. Generate bill");
            System.out.println("3. Display expiring CNICs");
            System.out.println("4. Update bill to paid status");
            System.out.println("5. Update rates & taxes");
            System.out.println("6. Get bill details");
            System.out.println("7. ");
            System.out.println("0. Exit");
            System.out.println("here: ");
            choice = Integer.parseInt(scanner.nextLine());
            try {
                switch (choice) {
                    case 1:
                        this.addCustomer(scanner);
                        System.out.println("Customer added successfully");
                        break;
                    case 2:
                        // generate bill
                        this.generateBill(scanner);
                        System.out.println("Bill generated successfully");
                        break;
                    case 3:
                        this.displayExpiringCnics();
                        break;
                    case 4:
                        setBillToPaid();
                        break;
                    case 5:
                        updateSalexTaxInfo();
                        break;
                    case 6:
                        System.out.println("Enter Customer ID: ");
                        int cust_id = Integer.parseInt(scanner.nextLine());
                        System.out.println("Enter Billing Month: ");
                        int billingmonth = Integer.parseInt(scanner.nextLine());
                        Bill bill = BillManager.getBill(cust_id, billingmonth);
                        if(bill==null) {
                            System.out.println("Bill not found");
                        }
                        else {
                            System.out.println("Bill found:");
                            System.out.println(bill);
                        }
                        break;
                    case 7:
                        // update their password
                        break;
                    case 8:
                        //add other employee
                        break;
                    case 9:
                        // show bills summary (paid and unpaid) + total amount of each category of bills
                        break;
                    case 0:
                        System.out.println("GOODBYE!!! Phir milein ge chalte chalte......");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid input, try again");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            System.out.println("Press Enter to continue...");
            scanner.nextLine();
            System.out.flush();
        } while (choice != 0);
    }
    public void CustomerMenu(Customer current_cust) {

    }
    public void start() {
        System.out.println("Welcome to LESCO Billing System\n");
        int option = 0;
        Scanner scanner = new Scanner(System.in);
        do {
            User current_user = null;
            try {
                current_user = Login.LetsLogin();
            if (current_user instanceof Employee) {
                this.EmployeeMenu((Employee) current_user);
            }
            else if(current_user instanceof Customer) {
                this.CustomerMenu((Customer) current_user);
            }
            else {
                System.out.println("Invalid user type (undefined action)");
            } } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } while(true);
    }
}
