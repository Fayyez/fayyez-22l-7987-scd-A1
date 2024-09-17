import utils.Login;
import models.*;
import java.util.Scanner;

public class TerminalApp {
    public void EmployeeMenu(Employee current_employee) {
        System.out.println("Welcome " + current_employee.getUsername() + " to the Employee Menu");
        System.out.println("1. Add new customer");
        System.out.println("2. Generate bill");
        System.out.println("3. View customer info");
        System.out.println("4. View bill info");
        System.out.println("5. Exit");
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                // add new customer
                break;
            case 2:
                // generate bill
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
