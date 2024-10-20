package Frontend_GUI.Employee_Panels;

import models.Employee;
import utils.EmployeeManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class UpdatePasswordPanel extends JPanel {
    private JPasswordField newPasswordField;
    private JPasswordField confirmPasswordField;
    private JButton saveButton;
    private Employee currentEmployee;

    public UpdatePasswordPanel(Employee employee) {
        this.currentEmployee = employee;

        setLayout(new GridBagLayout());  // Use GridBagLayout for a flexible form layout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);  // Padding around components
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Create a titled border for the panel
        setBorder(BorderFactory.createTitledBorder("Update Profile"));

        // Create password and confirm password fields with labels
        JLabel newPasswordLabel = new JLabel("New Password:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(newPasswordLabel, gbc);

        newPasswordField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 0;
        add(newPasswordField, gbc);

        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(confirmPasswordLabel, gbc);

        confirmPasswordField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(confirmPasswordField, gbc);

        // Save button
        saveButton = new JButton("Save");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;  // Center align the save button
        add(saveButton, gbc);

        // Action listener for the save button
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updatePassword();
            }
        });
    }

    // Method to update the employee's password
    private void updatePassword() {
        String newPassword = new String(newPasswordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        // Check if the passwords match
        if (!newPassword.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Password fields cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            ArrayList<Employee> employees = new ArrayList<>();
            EmployeeManager.getListOfEmployees(employees);

            // Update the employee's password in the EmployeeManager
            EmployeeManager.updatePassword(currentEmployee, employees, newPassword);
            JOptionPane.showMessageDialog(this, "Password updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

            // Clear fields after saving
            newPasswordField.setText("");
            confirmPasswordField.setText("");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error updating password: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
