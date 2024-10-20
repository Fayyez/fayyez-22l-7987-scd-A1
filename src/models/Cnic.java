package models;

import utils.DateBuilder;

import java.util.Date;

public class Cnic {
    private String number;
    private Date expiry_date;

    // constructor
    public Cnic(String number, String ex_date) {
        this.setNumber(number);
        this.setDate(ex_date);
    }
    // setters and getters
    public void setDate(String exdate) {
        this.expiry_date = DateBuilder.getDateobj(exdate);
    }
    public void setNumber(String number) {
        // must be 13 digits in cnic
        if(number.length()!=13) {
            throw new IllegalArgumentException("CNIC must be a 13 digit number says cnic class");
        }
        this.number = number;
    }
    public String getNumber() {
        return this.number;
    }
    public Date getDate() {
        return this.expiry_date;
    }
    public String getDateStr() {
        return DateBuilder.getDateStr(this.expiry_date);
    }
    // printing
    public String toString() {
        return "CNIC: " + this.number + " Expiry Date: " + this.getDateStr();
    }
}
