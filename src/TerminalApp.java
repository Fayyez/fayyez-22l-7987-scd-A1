import utils.CustomerManager;
import utils.DateBuilder;
import utils.Login;
import models.*;
import utils.TaxManager;

import java.io.FileNotFoundException;
import java.util.Date;
import java.util.Scanner;
//The LESCO billing system shall maintain records of usernames and passwords of LESCO
//employees. This information can be kept in a file with name EmployeesData. The
//Employeesdata file shall have one row for each employee, in each row there are two comma
//        separated values, the first value is username and the second value is password. Usernames
//shall be unique (i.e. no two rows will have the same username appearing in them).
//Employees can change their passwords by providing their username and current password.
//        2. The LESCO billing system shall maintain record of customer information in a file named
//        CustomerInfo. The CustomersInfo file shall save the information such as a unique 4 digit
//customer id (generated by the system), customer's CNIC number (13 digit number provided
//by the customer without dashes), Customer name, Address, Phone number, Customer Type
//        (commercial or domestic), meter type (Single Phase or Three Phase), Connection Date,
//Regular Units Consumed, Peak Hour Units Consumed. All these fields will be comma
//separated. Customer id and CNIC number may serve as username and password for the
//LESCO customers respectively. The Regular Units Consumed field corresponds to the meter
//reading for which the customer has paid the bill. In other words this field represents the
//units consumed (for both types of meters) and paid for at a given time. It is set to zero when
//a new meter is installed and a row is added in the CustomersInfo file. Then this field is
//updated whenever a bill is paid (its status is set to paid by the LESCO employees). The Peak
//Hours Units Consumed is also similar to the Regular Units Consumed but it is set for 3-phase
//meters only. For the 1-phase meters, it is kept blank. These two fields will be used to
//calculate the number of units consumed in a particular month.
//        3. The LESCO billing system shall maintain record of billing information in a file named
//        BillingInfo. The BillingInfo file shall store information such as 4 digit customer id (borrowed
//                                                                                                       from the CustomersInfo file), Billing month, Current Meter Reading Regular, Current Meter
//        Reading Peak, Reading Entry Date in format DD/MM/YYYY (cannot be in future, consider it
//to be issue date of the bill), Cost of electricity, Sales Tax Amount, Fixed Charges, Total Billing
//amount, Due Date in format DD/MM/YYYY (7 calendar days after the entry of Current Meter
//        Reading), Bill Paid Status (Paid or Unpaid), Bill payment Date in format DD/MM/YYYY
//        (cannot be before Reading Entry Date). There will be a new row for each meter customer
//every month in this file. For single phase meters, the unit price of electricity is 5 rupees per
//unit for domestic customers and 15 rupees per unit for the commercial customers. For three
//phase meters two meter readings are stored and for the regular units the price is 8 rupees
//per unit and for peak hours the price is 12 rupees per unit for domestic customers. For
//commercial customers the unit price is 18 rupees and 25 rupees per unit in regular and peak
//hours respectively. For the 1-phase meters and the off-peak units in 3-phase meters the
//number of units consumed can be calculated by subtracting the Regular Units Consumed
//        (mentioned in file CustomerInfo) from the Current Meter Reading Regular. For calculation
//about the peak hours units subtract the Peak Hours Units from Current Meter Reading Peak.
//4. The LESCO billing system shall maintain the information about the sales tax, fixed charges
//and the per unit tariff TariffTaxInfo file. This file shall have 4 rows only. The first row keeps
//tariff and tax information about domestic customers with 1-phase meter. The second row
//keeps tariff and tax information about the commercial customers with 1-phase meter. The
//third row keeps tariff and tax information about domestic customers with 3-phase meter.
//The fourth row keeps tariff and tax information about the commercial customers with 3-
//phase meter. Sample entries are as follows:
//        1Phase,5,,17,150
//        1Phase,15,,20,250
//        3Phase,8,12,17,150
//        3Phase,18,25,20,250
//The entries in a row correspond to the meter type, regular unit price, peak hour unit price
//        (left blank for 1-phase meters in the first 2 rows), the percentage of tax (for example 17
//percent of the cost of electricity), fixed charges.
//        5. The LESCO billing system shall allow the LESCO employees with valid username and
//password (after login) to add or update an entry (or row) in the files CustomerInfo and
//BillingInfo. The employees will add a row in CustomerInfo file whenever a new meter is
//installed for a customer. The system shall not allow more than 3 meters against a CNIC. If
//an employee tries to enter more than 3 meters for a CNIC, the shall disallow the action and
//display an error message stating "Not Allowed! Maximum 3 meters allowed per CNIC.". The
//system has access to NADRADB file which stores three comma separated values for each
//CNIC: CNIC Number (13 digit without dashes), Issuance Date (format DD/MM/YYYY), Expiry
//Date (format DD/MM/YYYY). The NADRADB file shall have multiple records (one for each
//        person with a CNIC)
public class TerminalApp {
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
        int index = 0;
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
        return 0;// default retirn 1phase single meter
    }
    public void generateBill(Scanner scanner) {
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
        int index = this.getIndex(cust);


    }
    public void EmployeeMenu(Employee current_employee) {
        System.out.println("Welcome " + current_employee.getUsername() + " to the Employee Menu");
        int choice = 0;
        Scanner scanner = new Scanner(System.in);
        do {
            System.out.println("1. Add new customer");
            System.out.println("2. Generate bill");
            System.out.println("3. View customer info");
            System.out.println("4. View bill info");
            System.out.println("0. Exit");
            choice = Integer.parseInt(scanner.nextLine());
            try {
                switch (choice) {
                    case 1:
                        this.addCustomer(scanner);
                        break;
                    case 2:
                        // generate bill
                        this.generateBill(scanner);
                        break;
                    case 3:
                        // view customer info
                        break;
                    case 4:
                        // view bill info
                        break;
                    case 5:
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
        System.out.println("Welcome to SESCO Billing System\n");
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
