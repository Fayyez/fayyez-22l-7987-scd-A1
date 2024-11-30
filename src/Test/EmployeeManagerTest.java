package Test;

import models.Employee;
import org.junit.jupiter.api.*;
import java.util.ArrayList;
import java.io.*;

import utils.EmployeeManager;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EmployeeManagerTest {

    private static final String testFilePath = "testEmployeeInfo.txt";

    // Helper method to clear the file for each test
    private void clearTestFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFilePath))) {
            writer.write("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @BeforeEach
    public void setUp() {
        clearTestFile();
    }

    @AfterAll
    public static void tearDown() {
        File file = new File(testFilePath);
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    public void testAddNewEmployee() throws Exception {
        String username = "testUser";
        String password = "testPassword";

        boolean result = EmployeeManager.addNewEmployee(username, password);
        Assertions.assertTrue(result);

        ArrayList<Employee> employees = new ArrayList<>();
        EmployeeManager.getListOfEmployees(employees);
        Assertions.assertEquals(1, employees.size());
        Assertions.assertEquals(username, employees.get(0).getUsername());
    }

    @Test
    public void testAddExistingEmployee() throws Exception {
        String username = "existingUser";
        String password = "password123";

        EmployeeManager.addNewEmployee(username, password);

        // Try adding the same employee again
        boolean result = EmployeeManager.addNewEmployee(username, password);

        Assertions.assertFalse(result); // Employee already exists
    }

    @Test
    public void testGetEmployeeValid() throws Exception {
        String username = "validUser";
        String password = "validPassword";
        EmployeeManager.addNewEmployee(username, password);

        Employee employee = EmployeeManager.getEmployee(username, password);

        Assertions.assertNotNull(employee);
        Assertions.assertEquals(username, employee.getUsername());
        Assertions.assertEquals(password, employee.getPassword());
    }

    @Test
    public void testGetEmployeeInvalid() {
        String username = "invalidUser";
        String password = "invalidPassword";

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            EmployeeManager.getEmployee(username, password);
        });
    }

    @Test
    public void testUpdatePassword() throws Exception {
        String username = "userToUpdate";
        String password = "oldPassword";
        String newPassword = "newPassword";

        EmployeeManager.addNewEmployee(username, password);

        ArrayList<Employee> employees = new ArrayList<>();
        Employee employee = EmployeeManager.getEmployee(username, password);

        EmployeeManager.updatePassword(employee, employees, newPassword);

        // Ensure the password has been updated
        Assertions.assertEquals(newPassword, employee.getPassword());
    }

    @Test
    public void testUpdatePasswordEmployeeNotFound() throws Exception {
        String username = "nonExistentUser";
        String password = "password";
        ArrayList<Employee> employees = new ArrayList<>();
        Employee nonExistentEmployee = new Employee(username, password);

        // Employee does not exist, so nothing should happen
        EmployeeManager.updatePassword(nonExistentEmployee, employees, "newPassword");

        // Check that no password change occurred by verifying the employee wasn't added
        Assertions.assertEquals(0, employees.size());
    }

    @Test
    public void testWriteEmployeesInfo() throws Exception {
        ArrayList<Employee> employees = new ArrayList<>();
        Employee employee1 = new Employee("user1", "password1");
        Employee employee2 = new Employee("user2", "password2");

        employees.add(employee1);
        employees.add(employee2);

        EmployeeManager.writeEmployeesInfo(employees);

        ArrayList<Employee> readEmployees = new ArrayList<>();
        EmployeeManager.getListOfEmployees(readEmployees);

        Assertions.assertEquals(2, readEmployees.size());
        Assertions.assertEquals("user1", readEmployees.get(0).getUsername());
        Assertions.assertEquals("user2", readEmployees.get(1).getUsername());
    }

    @Test
    public void testGetListOfEmployees() throws Exception {
        ArrayList<Employee> employees = new ArrayList<>();
        Employee employee = new Employee("user1", "password1");
        employees.add(employee);

        EmployeeManager.writeEmployeesInfo(employees);

        ArrayList<Employee> retrievedEmployees = new ArrayList<>();
        EmployeeManager.getListOfEmployees(retrievedEmployees);

        Assertions.assertEquals(1, retrievedEmployees.size());
        Assertions.assertEquals("user1", retrievedEmployees.get(0).getUsername());
    }

    @Test
    public void testGetListOfEmployeesFileError() {
        // Simulate file read error by deleting the file
        File file = new File(testFilePath);
        file.delete();

        Assertions.assertThrows(Exception.class, () -> {
            ArrayList<Employee> employees = new ArrayList<>();
            EmployeeManager.getListOfEmployees(employees);
        });
    }

}
