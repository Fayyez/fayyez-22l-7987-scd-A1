package Test;

import org.junit.jupiter.api.*;
import java.io.*;
import java.util.*;
import models.*;

import utils.BillManager;
import utils.DBClient;
import utils.DateBuilder;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BillManagerTest {

    private static final String TEST_FILENAME = "test_bills.txt";

    @BeforeAll
    public void setup() throws IOException {
        // Prepare a test file with sample data
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TEST_FILENAME))) {
            writer.write("1000,1,350,0,05/01/2024,5,17.0,150,1000,12/01/2024,true,06/10/2024\n");
            writer.write("1000,10,600,0,06/10/2024,8850,0.0,20,8870,13/10/2024,true,21/10/2024\n");
            writer.write("1001,8,0,0,03/09/2024,10,12.0,234,1122,10/09/2024,true,21/10/2024\n");
        }
    }

    @AfterAll
    public void teardown() {
        // Cleanup the test file
        new File(TEST_FILENAME).delete();
    }

    @Test
    public void testGetListOfBills() throws Exception {
        ArrayList<Bill> bills = new ArrayList<>();

        BillManager.getListOfBills(bills);

        Assertions.assertEquals(3, bills.size());
        Assertions.assertEquals(1000, bills.get(0).getCustomerID());
        Assertions.assertEquals(1, bills.get(0).getBillingmonth());
        Assertions.assertTrue(bills.get(0).getIsPaid());
    }

    @Test
    public void testWriteBillInfo() throws Exception {
        ArrayList<Bill> bills = new ArrayList<>();
        bills.add(new Bill(1002, 9, 500, 0, DateBuilder.getDateobj("10/09/2024"), 900, 10.0f, 100, 1000, DateBuilder.getDateobj("17/09/2024"), false, null));


        BillManager.writeBillInfo(bills);

        ArrayList<Bill> readBills = new ArrayList<>();
        BillManager.getListOfBills(readBills);
        Assertions.assertEquals(1, readBills.size());
        Assertions.assertEquals(1002, readBills.get(0).getCustomerID());
    }

    @Test
    public void testAddBill() throws Exception {
        //DBClient.overrideFilename(TEST_FILENAME);

        boolean result = BillManager.addBill(1003, 11, 400, 0, DateBuilder.getDateobj("01/11/2024"), 1200, 15.0f, 200, 1400, DateBuilder.getDateobj("08/11/2024"), false, null);

        Assertions.assertTrue(result);

        ArrayList<Bill> bills = new ArrayList<>();
        BillManager.getListOfBills(bills);

        Assertions.assertEquals(4, bills.size());
        Assertions.assertEquals(1003, bills.get(3).getCustomerID());
    }

    @Test
    public void testGetBill() throws Exception {
        //DBClient.overrideFilename(TEST_FILENAME);

        Bill bill = BillManager.getBill(1000, 1);

        Assertions.assertNotNull(bill);
        Assertions.assertEquals(1000, bill.getCustomerID());
        Assertions.assertEquals(1, bill.getBillingmonth());
        Assertions.assertTrue(bill.getIsPaid());
    }

    @Test
    public void testGetAllPaidBills() throws Exception {
        //DBClient.overrideFilename(TEST_FILENAME);

        ArrayList<Bill> allBills = new ArrayList<>();
        BillManager.getListOfBills(allBills);

        ArrayList<Bill> paidBills = BillManager.getAllPaidBills(allBills);

        Assertions.assertEquals(3, paidBills.size());
    }

    @Test
    public void testGetAllUnpaidBills() throws Exception {
        //DBClient.overrideFilename(TEST_FILENAME);

        ArrayList<Bill> allBills = new ArrayList<>();
        BillManager.getListOfBills(allBills);

        ArrayList<Bill> unpaidBills = BillManager.getAllUnpaidBills(allBills);

        Assertions.assertEquals(0, unpaidBills.size());
    }

    @Test
    public void testSetBillToPaid() throws Exception {
        //DBClient.overrideFilename(TEST_FILENAME);

        boolean result = BillManager.setBillToPaid(1000, 1);

        Assertions.assertTrue(result);

        Bill bill = BillManager.getBill(1000, 1);

        Assertions.assertTrue(bill.getIsPaid());
        Assertions.assertNotNull(bill.getPaidDate());
    }
}
