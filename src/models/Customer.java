package models;

import java.util.Date;

public class Customer extends User {
// (4 digit id,13digitcnic,name,address,phone,type{commercial/domestic}, metertype{single/three},connection_date,units_consumed,peak_units_consumed)
    private int id;//4 digit customer id
    private double cnic;
    private String name;
    private String address;
    private String phone;
    boolean isDomestic;
    private Date connectionDate;
    private int unitsConsumed;//units that have been paid for by the user
    //constructors
    public Customer(int id, double cnic, String name, String address, String phone, boolean isDomestic, Date connectionDate, int unitsConsumed) {
        setId(id);
        setCnic(cnic);
        setName(name);
        setAddress(address);
        setPhone(phone);
        setIsDomestic(isDomestic);
        setConnectionDate(connectionDate);
        setUnitsConsumed(unitsConsumed);
    }
    //getters & setters
    public int getId() {
        return this.id;
    }
    public void setId(int id) {
        if(id<1000 || id>9999) {
            throw new IllegalArgumentException("ID must be a 4 digit number");
        }
        this.id = id;
    }
    public double getCnic() {
        return this.cnic;
    }
    public String getCnicStr() {
        return String.valueOf(this.cnic).replace(".", "");
    }
    public void setCnic(double cnic) {
        if(cnic<1000000000000L || cnic>9999999999999L) {
            throw new IllegalArgumentException("CNIC must be a 13 digit number");
        }
        this.cnic = cnic;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        if(name.length()<=1) {
            throw new IllegalArgumentException("Name must be longer than 1 character");
        }
        this.name = name;
    }
    public String getAddress() {
        return this.address;
    }
    public void setAddress(String address) {
        if(address.length()<=1) {
            throw new IllegalArgumentException("Address must be longer than 1 character");
        }
        this.address = address;
    }
    public String getPhone() {
        return this.phone;
    }
    public void setPhone(String phone) {
        if(phone.length()!=11) {
            throw new IllegalArgumentException("Phone number must be 11 digits long");
        }
        this.phone = phone;
    }
    public String getIsDomesticStr() {
        if (this.isDomestic) {
            return "Domestic";
        }
        return "Commercial";
    }
    public boolean getIsDomestic() {
        return this.isDomestic;
    }
    public void setIsDomestic(boolean isDomestic) {
        this.isDomestic = isDomestic;
    }
    public String getConnectionDate() {
        return this.connectionDate.toString();
    }
    public Date getConnectionDateObj() {
        return this.connectionDate;
    }
    public void setConnectionDate(Date connectionDate) {
        this.connectionDate = connectionDate;
    }
    public int getUnitsConsumed() {
        return this.unitsConsumed;
    }
    public void setUnitsConsumed(int unitsConsumed) {
        this.unitsConsumed = unitsConsumed;
    }
    //other methods

    //printing
    @Override
    public String toString() {
        return "Customer:\n id: " + this.id + "\n cnic: " + this.getCnicStr() + "\n name: " + this.name
                + "\n address: " + this.address + "\n phone: " + this.phone + "\n customer type: "
                + this.getIsDomesticStr() + "\n connectionDate: " + this.connectionDate + "\n unitsConsumed: "
                + this.unitsConsumed + "\n";
    }
}
