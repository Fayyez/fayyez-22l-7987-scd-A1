package models;

import utils.CustomerManager;
import utils.DateBuilder;

import java.util.Date;

public class Bill {
    private int customerID;
    private int billingmonth;
    private int current_reg_reading;
    private int current_peak_reading;
    private Date issueDate;
    private int cost;
    private float taxAmount;//total bill*tax percentage
    private int fixedcharges;
    private int totalbill;
    private Date dueDate;
    private boolean isPaid;
    private Date paidDate;

    // setters
    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }
    public void setBillingmonth(int billingmonth) {
        if(billingmonth<1 || billingmonth>12) {
            throw new IllegalArgumentException("Invalid billing month");
        }
        this.billingmonth = billingmonth;
    }
    public void setCurrent_reg_reading(int current_reg_reading) {
        if(current_reg_reading<0) {
            throw new IllegalArgumentException("Invalid current regular reading");
        }
        this.current_reg_reading = current_reg_reading;
    }
    public void setCurrent_peak_reading(int current_peak_reading) {
        if(current_peak_reading<0) {
            throw new IllegalArgumentException("Invalid current peak reading");
        }
        this.current_peak_reading = current_peak_reading;
    }
    public void setIssueDate(Date issueDate) {
        if(DateBuilder.isInFuture(issueDate)) {
            throw new IllegalArgumentException("Issue date cannot be in future");
        }
        this.issueDate = issueDate;
    }
    public void setCost(int cost) {
        if(cost<0) {
            throw new IllegalArgumentException("Invalid cost");
        }
        this.cost = cost;
    }
    public void setTaxAmount(float taxAmount) {
        if(taxAmount<0) {
            throw new IllegalArgumentException("Invalid tax amount");
        }
        this.taxAmount = taxAmount;
    }
    public void setFixedcharges(int fixedcharges) {
        if(fixedcharges<0) {
            throw new IllegalArgumentException("Invalid fixed charges");
        }
        this.fixedcharges = fixedcharges;
    }
    public void setTotalbill(int totalbill) {
        if(totalbill<0) {
            throw new IllegalArgumentException("Invalid total bill");
        }
        this.totalbill = totalbill;
    }
    public void setDueDate(Date dueDate) {
        if(DateBuilder.isInPast(dueDate)) {
            throw new IllegalArgumentException("Due date cannot be in past");
        }
        this.dueDate = dueDate;
    }
    public void setIsPaid(boolean isPaid) {
        this.isPaid = isPaid;
    }
    public void setPaidDate(Date paidDate) {
        if(paidDate.before(this.issueDate)) {
            throw new IllegalArgumentException("Paid date cannot be in future");
        }
        this.paidDate = paidDate;
    }
    // getters
    public int getCustomerID() {
        return this.customerID;
    }
    public int getBillingmonth() {
        return this.billingmonth;
    }
    public int getCurrent_reg_reading() {
        return this.current_reg_reading;
    }
    public int getCurrent_peak_reading() {
        return this.current_peak_reading;
    }
    public Date getIssueDate() {
        return this.issueDate;
    }
    public int getCost() {
        return this.cost;
    }
    public float getTaxAmount() {
        return this.taxAmount;
    }
    public int getFixedcharges() {
        return this.fixedcharges;
    }
    public int getTotalbill() {
        return this.totalbill;
    }
    public Date getDueDate() {
        return this.dueDate;
    }
    public boolean getIsPaid() {
        return this.isPaid;
    }
    public Date getPaidDate() {
        return this.paidDate;
    }
    // constructors
    public Bill(int customerID, int billingmonth, int current_reg_reading, int current_peak_reading, Date issueDate, int cost, float taxAmount, int fixedcharges, int totalbill, Date dueDate, boolean isPaid, Date paidDate) {
        setCustomerID(customerID);
        setBillingmonth(billingmonth);
        setCurrent_reg_reading(current_reg_reading);
        setCurrent_peak_reading(current_peak_reading);
        setIssueDate(issueDate);
        setCost(cost);
        setTaxAmount(taxAmount);
        setFixedcharges(fixedcharges);
        setTotalbill(totalbill);
        setDueDate(DateBuilder.add7Days(issueDate));
        setIsPaid(isPaid);
        setPaidDate(paidDate);
    }
}
