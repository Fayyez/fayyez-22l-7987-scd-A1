package Test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.CNICManager;
import models.Cnic;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class CNICManagerTest {

    private ArrayList<Cnic> testCnics;

    @BeforeEach
    public void setup() {
        // Initialize a list of test CNICs
        testCnics = new ArrayList<>();
        testCnics.add(new Cnic("1234567890123", "15/12/2024"));
        testCnics.add(new Cnic("9876543210987", "10/01/2025"));
        testCnics.add(new Cnic("4567891234567", "10/01/2025")); // This CNIC is about to expire
    }

    @Test
    public void testGetAllCnics() throws Exception {
        // Write test CNICs to file using the writeCnicInfo method
        CNICManager.writeCnicInfo(testCnics);

        // Retrieve all CNICs
        ArrayList<Cnic> retrievedCnics = new ArrayList<>();
        CNICManager.getAllCnics(retrievedCnics);

        // Verify the retrieved CNICs match the written CNICs
        System.out.println("size of test cnics: " + testCnics.size());
        System.out.println("size of retrieved cnics: " + retrievedCnics.size());
        assertEquals(testCnics.size(), retrievedCnics.size());
        for(int i = 0; i < testCnics.size(); i++) {
            assertEquals(testCnics.get(i).getNumber(), retrievedCnics.get(i).getNumber());
            assertEquals(testCnics.get(i).getDateStr(), retrievedCnics.get(i).getDateStr());
        }
    }

    @Test
    public void testWriteCnicInfo() throws Exception {
        // Write test CNICs to file
        CNICManager.writeCnicInfo(testCnics);

        // Retrieve CNICs from file
        ArrayList<Cnic> retrievedCnics = new ArrayList<>();
        CNICManager.getAllCnics(retrievedCnics);

        // Verify the data integrity
        assertEquals(testCnics.size(), retrievedCnics.size());
        for (int i = 0; i < testCnics.size(); i++) {
            assertEquals(testCnics.get(i).getNumber(), retrievedCnics.get(i).getNumber());
            assertEquals(testCnics.get(i).getDateStr(), retrievedCnics.get(i).getDateStr());
        }
    }

    @Test
    public void testGetCnicsAboutToExpireIn30Days() throws Exception {
        // Write test CNICs to file
        CNICManager.writeCnicInfo(testCnics);

        // Retrieve CNICs about to expire in 30 days
        ArrayList<Cnic> expiringCnics = CNICManager.getCnicsAboutToExpireIn30Days();

        // Verify the correct CNICs are identified
        assertEquals(1, expiringCnics.size());
        assertEquals("1234567890123", expiringCnics.getFirst().getNumber());
    }

    @Test
    public void testInvalidDateThrowsException() throws Exception {
        // Add an invalid date entry
        ArrayList<Cnic> invalidCnics = new ArrayList<>();
        assertThrows( Exception.class, () -> invalidCnics.add(new Cnic("1234567890123", "invalid-date")));
    }
}
