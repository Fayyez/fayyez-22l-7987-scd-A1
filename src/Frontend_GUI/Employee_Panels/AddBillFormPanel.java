package Frontend_GUI.Employee_Panels;

import models.Bill;
import utils.BillManager;
import utils.CustomerManager;
import utils.DateBuilder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AddBillFormPanel extends JPanel {
    private JTextField customerIdField, regReadingField, peakReadingField, costField, taxField, fixedChargesField, totalBillField;
    private JSpinner readingDateSpinner;
    private JButton saveButton;
    private JCheckBox paidStatusCheckbox;

    public AddBillFormPanel() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Customer ID
        JLabel customerIdLabel = new JLabel("Customer ID:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(customerIdLabel, gbc);

        customerIdField = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 0;
        add(customerIdField, gbc);

        // Regular Meter Reading
        JLabel regReadingLabel = new JLabel("Current Regular Reading:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(regReadingLabel, gbc);

        regReadingField = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(regReadingField, gbc);

        // Peak Meter Reading
        JLabel peakReadingLabel = new JLabel("Current Peak Reading:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(peakReadingLabel, gbc);

        peakReadingField = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 2;
        add(peakReadingField, gbc);

        // Reading Date
        JLabel readingDateLabel = new JLabel("Reading Entry Date:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(readingDateLabel, gbc);

        readingDateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(readingDateSpinner, "dd/MM/yyyy");
        readingDateSpinner.setEditor(dateEditor);
        gbc.gridx = 1;
        gbc.gridy = 3;
        add(readingDateSpinner, gbc);

        // Cost of Electricity
        JLabel costLabel = new JLabel("Cost of Electricity:");
        gbc.gridx = 0;
        gbc.gridy = 4;
        add(costLabel, gbc);

        costField = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 4;
        add(costField, gbc);

        // Sales Tax Amount
        JLabel taxLabel = new JLabel("Sales Tax Amount:");
        gbc.gridx = 0;
        gbc.gridy = 5;
        add(taxLabel, gbc);

        taxField = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 5;
        add(taxField, gbc);

        // Fixed Charges
        JLabel fixedChargesLabel = new JLabel("Fixed Charges:");
        gbc.gridx = 0;
        gbc.gridy = 6;
        add(fixedChargesLabel, gbc);

        fixedChargesField = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 6;
        add(fixedChargesField, gbc);

        // Total Billing Amount
        JLabel totalBillLabel = new JLabel("Total Billing Amount:");
        gbc.gridx = 0;
        gbc.gridy = 7;
        add(totalBillLabel, gbc);

        totalBillField = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 7;
        add(totalBillField, gbc);

        // Paid Status Checkbox
        JLabel paidStatusLabel = new JLabel("Paid Status:");
        gbc.gridx = 0;
        gbc.gridy = 8;
        add(paidStatusLabel, gbc);

        paidStatusCheckbox = new JCheckBox("Paid");
        gbc.gridx = 1;
        gbc.gridy = 8;
        add(paidStatusCheckbox, gbc);

        // Save Button
        saveButton = new JButton("Save");
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(saveButton, gbc);

        // Save button action
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addBill();
            }
        });
    }

    // Method to handle adding a new bill
    private void addBill() {
        try {
            // Get form values
            int customerId = Integer.parseInt(customerIdField.getText());
            int regReading = Integer.parseInt(regReadingField.getText());
            int peakReading = Integer.parseInt(peakReadingField.getText());
            Date readingDate = (Date) readingDateSpinner.getValue();
            int cost = Integer.parseInt(costField.getText());
            float taxAmount = Float.parseFloat(taxField.getText());
            int fixedCharges = Integer.parseInt(fixedChargesField.getText());
            int totalBill = Integer.parseInt(totalBillField.getText());
            boolean isPaid = paidStatusCheckbox.isSelected();

            // Validate reading date (cannot be in future)
            Date currentDate = new Date();
            if (readingDate.after(currentDate)) {
                JOptionPane.showMessageDialog(this, "Reading date cannot be in the future!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Set issue and due date (7 days after issue date)
            Date issueDate = readingDate;
            Calendar cal = Calendar.getInstance();
            cal.setTime(issueDate);
            cal.add(Calendar.DATE, 7);
            Date dueDate = cal.getTime();

            // Check if latest bill can be generated
            Bill latestBill = BillManager.getLatestBill(customerId);
            if (latestBill != null && latestBill.getIssueDate().after(issueDate)) {
                JOptionPane.showMessageDialog(this, "You can only generate a new bill for the latest month!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Add new bill to the system
            BillManager.addBill(customerId, DateBuilder.getMonth(issueDate), regReading, peakReading, issueDate, cost, taxAmount, fixedCharges, totalBill, dueDate, isPaid, null);
            JOptionPane.showMessageDialog(this, "Bill added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

            // Clear form fields
            customerIdField.setText("");
            regReadingField.setText("");
            peakReadingField.setText("");
            costField.setText("");
            taxField.setText("");
            fixedChargesField.setText("");
            totalBillField.setText("");
            readingDateSpinner.setValue(new Date());
            paidStatusCheckbox.setSelected(false);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error adding bill: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
