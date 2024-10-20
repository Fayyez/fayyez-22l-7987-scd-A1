package Frontend_GUI.Customer_Panels;

import models.Cnic;
import models.Customer;
import utils.CNICManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class UpdateCNICPanel extends JPanel {
    private Cnic customerCnic = null;
    private JSpinner daySpinner, monthSpinner, yearSpinner;
    private JButton changeExpiryButton, saveButton;

    public UpdateCNICPanel(Customer user) {
        ArrayList<Cnic> cnics = new ArrayList<>();
        CNICManager.getAllCnics(cnics);

        // Find the user's CNIC
        for (Cnic cnic : cnics) {
            if (user.getCnicStr().contains(cnic.getNumber())) {
                this.customerCnic = cnic;
                break;
            }
        }

        if (this.customerCnic == null) {
            JOptionPane.showMessageDialog(null, "CNIC not found!", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            this.init();
        }
    }

    private void init() {
        this.setLayout(new BorderLayout(10, 10));

        // Add padding to the panel
        this.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));  // Padding for top, left, bottom, right

        // Header Label
        JLabel headerLabel = new JLabel("CNIC INFO", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        headerLabel.setForeground(Color.BLUE);  // Set the color to blue
        this.add(headerLabel, BorderLayout.NORTH);

        // Create a panel for displaying current CNIC info
        JPanel infoPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        infoPanel.setBorder(BorderFactory.createTitledBorder("CNIC Information"));

        infoPanel.add(new JLabel("CNIC Number:"));
        infoPanel.add(new JLabel(customerCnic.getNumber()));

        infoPanel.add(new JLabel("Current Expiry Date:"));
        infoPanel.add(new JLabel(customerCnic.getDateStr()));

        this.add(infoPanel, BorderLayout.NORTH);

        // Create spinners for date selection
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);

        daySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 31, 1));
        monthSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 12, 1));
        yearSpinner = new JSpinner(new SpinnerNumberModel(currentYear, currentYear, currentYear + 50, 1));

        // Set spinner size
        Dimension spinnerSize = new Dimension(10, 10);
        daySpinner.setPreferredSize(spinnerSize);
        monthSpinner.setPreferredSize(spinnerSize);
        yearSpinner.setPreferredSize(spinnerSize);

        // Disable spinners initially
        daySpinner.setEnabled(false);
        monthSpinner.setEnabled(false);
        yearSpinner.setEnabled(false);

        // Panel to hold the spinners
        JPanel datePanel = new JPanel(new GridLayout(2, 3, 10, 10));
        datePanel.setBorder(BorderFactory.createTitledBorder("Update Expiry Date"));

        datePanel.add(new JLabel("Day:"));
        datePanel.add(new JLabel("Month:"));
        datePanel.add(new JLabel("Year:"));

        datePanel.add(daySpinner);
        datePanel.add(monthSpinner);
        datePanel.add(yearSpinner);

        this.add(datePanel, BorderLayout.CENTER);

        // Buttons for changing expiry and saving
        changeExpiryButton = new JButton("Change Expiry");
        saveButton = new JButton("Save Changes");

        // Initially disable the save button
        saveButton.setEnabled(false);

        // Panel to hold the buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(changeExpiryButton);
        buttonPanel.add(saveButton);

        this.add(buttonPanel, BorderLayout.CENTER);

        // Action listener for enabling spinners and save button
        changeExpiryButton.addActionListener(e -> {
            daySpinner.setEnabled(true);
            monthSpinner.setEnabled(true);
            yearSpinner.setEnabled(true);
            saveButton.setEnabled(true);
        });

        // Action listener for saving the changes and disabling again
        saveButton.addActionListener(e -> {
            // Get selected values from spinners
            int day = (int) daySpinner.getValue();
            int month = (int) monthSpinner.getValue();
            int year = (int) yearSpinner.getValue();

            // Format the new expiry date and compare with current expiry date
            String newExpiryDate = String.format("%02d/%02d/%d", day, month, year);
            Date selectedDate = utils.DateBuilder.getDateobj(newExpiryDate);

            if (selectedDate.before(customerCnic.getDate())) {
                JOptionPane.showMessageDialog(null, "New expiry date cannot be earlier than the current expiry date.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                customerCnic.setDate(newExpiryDate);

                // Disable the spinners and save button after saving
                daySpinner.setEnabled(false);
                monthSpinner.setEnabled(false);
                yearSpinner.setEnabled(false);
                saveButton.setEnabled(false);

                // Write updated CNIC back to file
                ArrayList<Cnic> cnics = new ArrayList<>();
                CNICManager.getAllCnics(cnics);

                // Find and update the customer's CNIC
                for (Cnic cnic : cnics) {
                    if (cnic.getNumber().equals(customerCnic.getNumber())) {
                        cnic.setDate(newExpiryDate);
                        break;
                    }
                }

                CNICManager.writeCnicInfo(cnics);

                // Display confirmation
                JOptionPane.showMessageDialog(null, "Expiry date updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }
}
