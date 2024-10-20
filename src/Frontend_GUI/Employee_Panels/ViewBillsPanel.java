package Frontend_GUI.Employee_Panels;

import models.Bill;
import utils.BillManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class ViewBillsPanel extends JPanel {
    private JTable billsTable;
    private DefaultTableModel tableModel;
    private JScrollPane scrollPane;
    private ArrayList<Bill> bills;

    public ViewBillsPanel() {
        this.init();
    }

    private void init() {
        // Set layout
        this.setLayout(new BorderLayout());

        // Initialize table with headers
        String[] columns = {
                "Customer ID", "Billing Month", "Regular Reading", "Peak Reading", "Entry Date", "Cost",
                "Tax Amount", "Fixed Charges", "Total Bill", "Due Date", "Paid Status", "Paid Date", "Update", "Remove"
        };
        tableModel = new DefaultTableModel(columns, 0);
        billsTable = new JTable(tableModel);
        billsTable.setRowHeight(30);

        // ScrollPane for scrolling
        scrollPane = new JScrollPane(billsTable);
        this.add(scrollPane, BorderLayout.CENTER);

        // Load the bills data
        loadBills();
    }

    // Method to load bills into the table
    private void loadBills() {
        try {
            bills = new ArrayList<>();
            BillManager.getListOfBills(bills); // Fetch all bills from the file

            // Clear the current rows
            tableModel.setRowCount(0);

            for (Bill bill : bills) {
                Object[] rowData = {
                        bill.getCustomerID(), bill.getBillingmonth(), bill.getCurrent_reg_reading(),
                        bill.getCurrent_peak_reading(), bill.getIssueDate(), bill.getCost(),
                        bill.getTaxAmount(), bill.getFixedcharges(), bill.getTotalbill(),
                        bill.getDueDate(), bill.getIsPaid() ? "Paid" : "Unpaid",
                        bill.getPaidDateStr(),
                        createUpdateButton(bill), createRemoveButton(bill)
                };
                tableModel.addRow(rowData);
            }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Error loading bills: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Helper method to create the "Update" button
    private JButton createUpdateButton(Bill bill) {
        JButton btnUpdate = new JButton("Update");
        btnUpdate.setEnabled(isEditable(bill)); // Only enable if the bill is editable
        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Update logic here
                updateBill(bill);
            }
        });
        return btnUpdate;
    }

    // Helper method to create the "Remove" button
    private JButton createRemoveButton(Bill bill) {
        JButton btnRemove = new JButton("Remove");
        btnRemove.setEnabled(isEditable(bill)); // Only enable if the bill is editable
        btnRemove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Remove logic here
                removeBill(bill);
            }
        });
        return btnRemove;
    }

    // Method to determine if a bill is editable (only the latest month's bill is editable)
    private boolean isEditable(Bill bill) {
        Bill latestBill = BillManager.getLatestBill(bill.getCustomerID());
        return latestBill != null && bill.equals(latestBill);
    }

    // Method to update a bill
    private void updateBill(Bill bill) {
        // Logic to handle bill update, e.g., open a new panel/form to edit bill details
        JOptionPane.showMessageDialog(this, "Updating bill for Customer ID: " + bill.getCustomerID() + ", Billing Month: " + bill.getBillingmonth());
    }

    // Method to remove a bill
    private void removeBill(Bill bill) {
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to remove this bill?", "Confirm Removal", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                ArrayList<Bill> allBills = new ArrayList<>();
                BillManager.getListOfBills(allBills);
                allBills.remove(bill); // Remove the bill from the list
                BillManager.writeBillInfo(allBills); // Write the updated list back to the file
                loadBills(); // Reload the table
                JOptionPane.showMessageDialog(this, "Bill removed successfully!");
            } catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(this, "Error removing bill: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
