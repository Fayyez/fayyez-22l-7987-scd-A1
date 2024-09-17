package utils;
import models.*;

import java.sql.SQLOutput;
import java.util.Date;
import java.util.Scanner;

public class Login {
    // this static class is responsible for logging in user
    // and retrieving its data from the customer and employee infor files when needed
    // performs only read operations and matches the user credentials and returns the user object
    public static void showLoginMessage() {
        // prints the login screen
        System.out.println("Welcome to the login screen✌️");
        System.out.println("<ENTER 1> for Customer Login");
        System.out.println("<ENTER 2> for Employee Login");
        System.out.println("<ENTER 3> to Exit");
        System.out.println("here: ");
    }
    public static Customer LoginCustomer() {
        showLoginMessage();//display login menu
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        // get id and cnic number. id must be 4 digit integer and cnic must be 13 digit double
        System.out.println("Enter your ID: ");
        int id = scanner.nextInt();
        System.out.println("Enter your CNIC: ");
        double cnic = scanner.nextDouble();
        // checks for input
        if((id > 9999) || (id < 1000)) {
            System.out.println("ID must be a 4 digit number");
            return null;
        }
        else if (cnic>9999999999999L||cnic<1000000000000L) {
            System.out.println("CNIC must be a 13 digit number");
            return null;
        }
        return CustomerManager.getCustomer(id, cnic);
    }
}
