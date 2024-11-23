package Frontend_GUI.Employee_Panels;

import models.Bill;
import models.Customer;
import models.OnePhaseCust;
import models.ThreePhaseCust;
import utils.BillManager;
import utils.CustomerManager;
import utils.DateBuilder;
import utils.TaxManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AddBillFormPanel extends JPanel {
    private JTextField customerIdField, regReadingField, peakReadingField;
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
            TaxManager taxManager = TaxManager.getInstance();
            // Get form values
            int customerId = Integer.parseInt(customerIdField.getText());
            int regReading = Integer.parseInt(regReadingField.getText());
            int peakReading = Integer.parseInt(peakReadingField.getText());
            Date readingDate = (Date) readingDateSpinner.getValue();
            int index = 0;
            Customer c = CustomerManager.getCustomer(customerId);
            int unitsComsumed = regReading - c.getUnitsConsumed();
            int peakUnitsConsumed = 0;
            if(c instanceof OnePhaseCust) {
                index = taxManager.get1phaseDomesticIndex();
            }
            else {
                index = taxManager.get3phaseDomesticIndex();
                peakUnitsConsumed = peakReading - ((ThreePhaseCust) c).getPeakUnitsConsumed();
            }
            if(!c.getIsDomestic()) index++;
            int cost = taxManager.getRegUnitPrice(index) * unitsComsumed + taxManager.getPeakUnitPrice(index) * peakUnitsConsumed;
            float taxAmount = cost * taxManager.getTaxPercentage(index)/100;
            int fixedCharges = taxManager.getFixedCharge(index);
            int totalBill = cost + (int) taxAmount + fixedCharges;
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
            readingDateSpinner.setValue(new Date());
            paidStatusCheckbox.setSelected(false);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error adding bill: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
