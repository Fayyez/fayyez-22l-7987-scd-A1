package Test;

import org.junit.jupiter.api.*;
import utils.CustomerManager;
import models.Customer;
import models.OnePhaseCust;
import models.ThreePhaseCust;
import utils.DBClient;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CustomerTest {

    @Test
    @Order(1)
    void testAddNewCustomer() {
        boolean result = false;
        try {
            result = CustomerManager.addNewCustomerToFile(
                    1234567890123L, "John Doe", "123 Street", "12345678901",
                    true, "single", new Date(), 100, 0);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        assertTrue(result, "Customer should be added successfully");
    }

    @Test
    @Order(2)
    void testAddThreeMetersForSameCNIC() {
        boolean result1 = false;
        try {
            result1 = CustomerManager.addNewCustomerToFile(
                    1234567890123L, "John Doe", "123 Street", "12345678901",
                    true, "single", new Date(), 100, 0);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        assertTrue(result1, "First meter should be added successfully");

        boolean result2 = false;
        try {
            result2 = CustomerManager.addNewCustomerToFile(
                    1234567890123L, "John Doe", "123 Street", "1234567890",
                    true, "three", new Date(), 200, 50);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        assertTrue(result2, "Second meter should be added successfully");

        boolean result3 = false;
        try {
            result3 = CustomerManager.addNewCustomerToFile(
                    1234567890123L, "John Doe", "123 Street", "1234567890",
                    true, "single", new Date(), 150, 0);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        assertTrue(result3, "Third meter should be added successfully");

        boolean result4 = false;
        try {
            result4 = CustomerManager.addNewCustomerToFile(
                    1234567890123L, "John Doe", "123 Street", "1234567890",
                    true, "single", new Date(), 150, 0);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        assertFalse(result4, "Fourth meter should not be allowed for the same CNIC");
    }

    @Test
    @Order(3)
    void testGetCustomerById() {
        Customer customer = CustomerManager.getCustomer(1000); // Assuming ID 1 exists
        assertNotNull(customer, "Customer with ID 1 should exist");
        assertEquals(1000, customer.getId(), "Customer ID should match");
    }

    @Test
    @Order(4)
    void testGetCustomerByIdAndCNIC() {
        Customer customer = CustomerManager.getCustomer(1000, 1234567890123L); // Assuming ID 1 and CNIC match
        assertNotNull(customer, "Customer with ID 1 and matching CNIC should exist");
    }

    @Test
    @Order(5)
    void testGetCustomerByNameAndId() {
        Customer customer = CustomerManager.getCustomer(1000, "Fayyez Farrukh"); // Assuming name matches ID 1
        assertNotNull(customer, "Customer with ID 1000 and name Fayyez Farrukh should exist");
    }

    @Test
    @Order(6)
    void testWriteCustomerInfo() throws IOException, ClassNotFoundException {
        ArrayList<Customer> customers = new ArrayList<>();
        CustomerManager.getListOfCustomers(customers);
        customers.add(new OnePhaseCust(2, 9876543210123L, "Jani shouq", "456 Avenue", "0987654321", true, new Date(), 120));
        customers.add(new ThreePhaseCust(3, 1122334455667L, "Alice Brown", "789 Boulevard", "1122334455", false, new Date(), 220, 60));
        assertDoesNotThrow(() -> CustomerManager.writeCustomerInfo(customers), "Writing customer info should not throw an exception");
    }

    @Test
    @Order(7)
    void testDeleteCustomer() {
        ArrayList<Customer> customers = new ArrayList<>();
        try {
            CustomerManager.getListOfCustomers(customers);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        int initialSize = customers.size();

        // Assuming customer with ID 1 exists
        customers.removeIf(customer -> customer.getId() == 1);
        CustomerManager.writeCustomerInfo(customers);

        try {
            CustomerManager.getListOfCustomers(customers);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        int newSize = customers.size();

        assertEquals(initialSize - 1, newSize, "Customer with ID 1 should be deleted");
    }

    @Test
    @Order(8)
    void testAddInvalidMeterType() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                CustomerManager.addNewCustomerToFile(
                        1234567890123L, "Invalid Meter", "999 Street", "0000000000",
                        false, "invalid", new Date(), 0, 0)
        );
        assertEquals("Invalid meter type", exception.getMessage(), "Should throw an exception for invalid meter type");
    }
}
