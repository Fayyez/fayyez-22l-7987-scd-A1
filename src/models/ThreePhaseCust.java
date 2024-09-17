package models;
//imports
import java.util.Date;

public class ThreePhaseCust extends Customer {
    private int peakUnitsConsumed;

    public ThreePhaseCust(int id, double cnic, String name, String address, String phone, boolean isDomestic, Date connectionDate, int unitsConsumed, int peakUnitsConsumed) {
        super(id, cnic, name, address, phone, isDomestic, connectionDate, unitsConsumed);
        setPeakUnitsConsumed(peakUnitsConsumed);
    }

    public int getPeakUnitsConsumed() {
        return this.peakUnitsConsumed;
    }

    public void setPeakUnitsConsumed(int peakUnitsConsumed) {
        if(peakUnitsConsumed<0) {
            throw new IllegalArgumentException("Peak units consumed must be a positive number");
        }
        this.peakUnitsConsumed = peakUnitsConsumed;
    }

    public String toString() {
        return "ThreePhaseCust:\n id: " + this.getId() + "\n cnic: " + this.getCnicStr() + "\n name: " + this.getName()
                + "\n address: " + this.getAddress() + "\n phone: " + this.getPhone() + "\n isDomestic: " + this.getIsDomesticStr()
                + "\n connectionDate: " + this.getConnectionDate() + "\n unitsConsumed: " + this.getUnitsConsumed()
                + "\n peakUnitsConsumed: " + this.getPeakUnitsConsumed() + "\n";
    }

}
