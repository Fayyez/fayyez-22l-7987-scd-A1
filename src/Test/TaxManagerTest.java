package Test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.TaxManager;
import java.util.ArrayList;

public class TaxManagerTest {

    private TaxManager taxManager;

    @BeforeEach
    public void setUp() {
        // Initialize TaxManager before each test
        taxManager = TaxManager.getInstance();
    }

    @Test
    public void testSingletonInstance() {
        // Test to ensure that only one instance of TaxManager is created
        TaxManager anotherTaxManager = TaxManager.getInstance();
        assertSame(taxManager, anotherTaxManager, "TaxManager should be a singleton");
    }

    @Test
    public void testLoadData() {
        // Test that data is correctly loaded into the TaxManager instance
        assertNotNull(taxManager, "TaxManager instance should not be null");
        assertEquals(7, taxManager.getRegUnitPrice(TaxManager.ONE_PHASE_DOMESTIC), "Reg Unit Price for 1-Phase Domestic should be 7");
        assertEquals(0, taxManager.getPeakUnitPrice(TaxManager.ONE_PHASE_DOMESTIC), "Peak Unit Price for 1-Phase Domestic should be 69");
        assertEquals(2.50, taxManager.getTaxPercentage(TaxManager.ONE_PHASE_DOMESTIC), 0.01, "Tax Percentage for 1-Phase Domestic should be 2.50");
        assertEquals(30, taxManager.getFixedCharge(TaxManager.ONE_PHASE_DOMESTIC), "Fixed Charges for 1-Phase Domestic should be 30");
    }

    @Test
    public void testGetTaxDetails() {
        // Test retrieval of tax details
        int regUnitPrice = taxManager.getRegUnitPrice(TaxManager.THREE_PHASE_DOMESTIC);
        int peakUnitPrice = taxManager.getPeakUnitPrice(TaxManager.THREE_PHASE_DOMESTIC);
        float taxPercentage = taxManager.getTaxPercentage(TaxManager.THREE_PHASE_DOMESTIC);
        int fixedCharges = taxManager.getFixedCharge(TaxManager.THREE_PHASE_DOMESTIC);

        assertEquals(8, regUnitPrice, "Reg Unit Price for 3-Phase Domestic should be 8");
        assertEquals(12, peakUnitPrice, "Peak Unit Price for 3-Phase Domestic should be 12");
        assertEquals(17.00, taxPercentage, 0.01, "Tax Percentage for 3-Phase Domestic should be 17.00");
        assertEquals(150, fixedCharges, "Fixed Charges for 3-Phase Domestic should be 150");
    }

    @Test
    public void testSetRegUnitPrice() {
        // Test setting the Regular Unit Price for 1-Phase Commercial
        taxManager.setRegUnitPrice(TaxManager.ONE_PHASE_COMMERCIAL, 20);
        assertEquals(20, taxManager.getRegUnitPrice(TaxManager.ONE_PHASE_COMMERCIAL), "Reg Unit Price for 1-Phase Commercial should be updated to 20");
    }

    @Test
    public void testSetInvalidIndex() {
        // Test invalid index handling for setting unit price
        assertThrows(IllegalArgumentException.class, () -> {
            taxManager.setRegUnitPrice(5, 100); // Invalid index
        }, "Should throw IllegalArgumentException for invalid index");
    }

    @Test
    public void testUpdateTaxInfo() {
        // Test updating tax info
        taxManager.updateTaxInfo(TaxManager.ONE_PHASE_DOMESTIC, 10, 80, 3.00f, 40);

        assertEquals(10, taxManager.getRegUnitPrice(TaxManager.ONE_PHASE_DOMESTIC), "Reg Unit Price for 1-Phase Domestic should be updated to 10");
        assertEquals(80, taxManager.getPeakUnitPrice(TaxManager.ONE_PHASE_DOMESTIC), "Peak Unit Price for 1-Phase Domestic should be updated to 80");
        assertEquals(3.00, taxManager.getTaxPercentage(TaxManager.ONE_PHASE_DOMESTIC), 0.01, "Tax Percentage for 1-Phase Domestic should be updated to 3.00");
        assertEquals(40, taxManager.getFixedCharge(TaxManager.ONE_PHASE_DOMESTIC), "Fixed Charges for 1-Phase Domestic should be updated to 40");
    }

    @Test
    public void testPrintTaxSummary() {
        // Test printing the tax summary for all categories
        taxManager.printTaxSummary(); // Manual observation test
    }

    @Test
    public void testPrintSingleTaxSummary() {
        // Test printing tax summary for a single category
        taxManager.printSingleTaxSummary(TaxManager.THREE_PHASE_COMMERCIAL); // Manual observation test
   }

}
