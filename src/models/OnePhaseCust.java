package models;
//imports
import java.util.Date;

public class OnePhaseCust extends Customer {
    //constructor
    public OnePhaseCust(int id, double cnic, String name, String address, String phone, boolean isDomestic, Date connectionDate, int unitsConsumed) {
        super(id, cnic, name, address, phone, isDomestic, connectionDate, unitsConsumed);
    }
    //other methods
}
