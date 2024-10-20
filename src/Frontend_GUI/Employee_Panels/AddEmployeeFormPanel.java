package Frontend_GUI.Employee_Panels;

import utils.EmployeeManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddEmployeeFormPanel extends JPanel {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton addButton;
    private JButton cancelButton;

    public AddEmployeeFormPanel() {
        setLayout(new GridBagLayout());  // Use GridBagLayout for a flexible form layout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);  // Padding around components
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Create a titled border for the panel
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Add form fields with labels
        JLabel usernameLabel = new JLabel("Username:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(usernameLabel, gbc);

        usernameField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 0;
        add(usernameField, gbc);

        JLabel passwordLabel = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(passwordLabel, gbc);

        passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(passwordField, gbc);

        // Add buttons: Add and Cancel
        addButton = new JButton("Add Employee");
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(addButton, gbc);

        cancelButton = new JButton("Cancel");
        gbc.gridx = 1;
        gbc.gridy = 2;
        add(cancelButton, gbc);

        // Action listener for Add button
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addEmployee();
            }
        });

        // Action listener for Cancel button (reset fields)
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetForm();
            }
        });
    }

    // Method to add a new employee to the database
    private void addEmployee() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        // Basic validation
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Both username and password are required.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Add employee using EmployeeManager
            boolean success = EmployeeManager.addNewEmployee(username, password);
            if (success) {
                JOptionPane.showMessageDialog(this, "Employee added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                resetForm();  // Reset the form after successful addition
            } else {
                JOptionPane.showMessageDialog(this, "Employee already exists.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error adding employee: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to reset the form fields
    private void resetForm() {
        usernameField.setText("");
        passwordField.setText("");
    }
}
