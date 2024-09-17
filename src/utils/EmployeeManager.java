package utils;

import models.Employee;

import java.io.*;
import java.util.ArrayList;

public class EmployeeManager {
    private final static String filepath = "src/database/employeeInfo.txt";
    //methods
    public static void getListOfEmployees(ArrayList<Employee> employees) throws Exception {
        try(FileReader fr = new FileReader(filepath);
            BufferedReader br = new BufferedReader(fr);) {
            String entry;
            while((entry = br.readLine()) != null) {
                String[]data = entry.split(",");
                String username = data[0];
                String password = data[1];
                Employee e = new Employee(username, password);
                employees.add(e);
            }
        } catch (Exception e) {
            throw new Exception("Error reading employees data in employee manager");
        }
    }
    public static void writeEmployeesInfo(ArrayList<Employee> employees) throws Exception {
        try {
            FileWriter fw = new FileWriter(filepath);
            BufferedWriter bw = new BufferedWriter(fw);
            for(Employee e: employees) {
                String entry = e.getUsername() + "," + e.getPassword();
                bw.write(entry);
                bw.newLine();
            }
            bw.close();
            fw.close();
        } catch (Exception e) {
            throw new Exception("Error writing to employees file");
        }
    }
    public static Employee getEmployee(String username, String password) throws Exception {
        ArrayList<Employee> emps = new ArrayList<>();
        getListOfEmployees(emps);
        for(Employee e: emps) {
            if(e.getPassword().equalsIgnoreCase(password) &&
            e.getUsername().equalsIgnoreCase(username)) {
                return e;
            }
        }
        throw new IllegalArgumentException("Employee not found");
    }
    public static void updatePassword(Employee e, ArrayList<Employee> employees, String newPassword) throws Exception {
        for(Employee emp: employees) {
            if(emp.getUsername().equalsIgnoreCase(e.getUsername())) {
                emp.setPassword(newPassword);
                e.setPassword(newPassword);
                writeEmployeesInfo(employees);
                return;
            }
        }
        System.out.println("Employee not found in the records when changing password.");

    }
    public static boolean addNewEmployee(String username, String password) throws Exception {
        ArrayList<Employee> employees = new ArrayList<>();
        getListOfEmployees(employees);
        for(Employee e: employees) {
            if(e.getUsername().equalsIgnoreCase(username)) {
                System.out.println("Employee already exists in the records");
                return false;
            }
        }
        Employee e = new Employee(username, password);
        employees.add(e);
        writeEmployeesInfo(employees);
        return true;
    }
}
