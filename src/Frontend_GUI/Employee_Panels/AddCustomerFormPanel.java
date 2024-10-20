package Frontend_GUI.Employee_Panels;

import utils.CustomerManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

public class AddCustomerFormPanel extends JPanel {
    // Form fields
    private JTextField txtName;
    private JTextField txtCNIC;
    private JTextField txtAddress;
    private JTextField txtPhoneNumber;
    private JComboBox<String> cmbMeterType;
    private JCheckBox chkIsDomestic;
    private JSpinner spnConnectionDate;
    private JTextField txtUnitsConsumed;
    private JTextField txtPeakUnitsConsumed;
    private JButton btnSubmit;
    private JLabel lblStatus;

    public AddCustomerFormPanel() {
        init();
        setVisible(true);
    }

    public void init() {
        setLayout(new GridLayout(10, 2, 5, 5));

        // Initializing fields
        txtName = new JTextField();
        txtCNIC = new JTextField();
        txtAddress = new JTextField();
        txtPhoneNumber = new JTextField();
        cmbMeterType = new JComboBox<>(new String[] {"Single", "Three"});
        chkIsDomestic = new JCheckBox("Is Domestic");
        spnConnectionDate = new JSpinner(new SpinnerDateModel());
        txtUnitsConsumed = new JTextField();
        txtPeakUnitsConsumed = new JTextField();
        btnSubmit = new JButton("Add Customer");
        lblStatus = new JLabel("");

        // Add components to panel
        add(new JLabel("Name:"));
        add(txtName);
        add(new JLabel("CNIC:"));
        add(txtCNIC);
        add(new JLabel("Address:"));
        add(txtAddress);
        add(new JLabel("Phone Number:"));
        add(txtPhoneNumber);
        add(new JLabel("Meter Type:"));
        add(cmbMeterType);
        add(new JLabel("Is Domestic:"));
        add(chkIsDomestic);
        add(new JLabel("Connection Date:"));
        add(spnConnectionDate);
        add(new JLabel("Units Consumed:"));
        add(txtUnitsConsumed);
        add(new JLabel("Peak Units Consumed (if three-phase):"));
        add(txtPeakUnitsConsumed);
        add(btnSubmit);
        add(lblStatus);

        // Add button action listener
        btnSubmit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addCustomer();
            }
        });
    }

    private void addCustomer() {
        // Retrieve form values
        String name = txtName.getText();
        double cnic = Double.parseDouble(txtCNIC.getText());
        String address = txtAddress.getText();
        String phoneNumber = txtPhoneNumber.getText();
        String meterType = (String) cmbMeterType.getSelectedItem();
        boolean isDomestic = chkIsDomestic.isSelected();
        Date connectionDate = (Date) spnConnectionDate.getValue();
        int unitsConsumed = Integer.parseInt(txtUnitsConsumed.getText());
        int peakUnitsConsumed = meterType.equals("Three") ? Integer.parseInt(txtPeakUnitsConsumed.getText()) : 0;

        // Try adding customer using CustomerManager
        try {
            boolean success = CustomerManager.addNewCustomerToFile(cnic, name, address, phoneNumber, isDomestic, meterType, connectionDate, unitsConsumed, peakUnitsConsumed);
            if (success) {
                lblStatus.setText("Customer added successfully.");
                lblStatus.setForeground(Color.GREEN);
            } else {
                lblStatus.setText("Customer already has 3 meters.");
                lblStatus.setForeground(Color.RED);
            }
        } catch (Exception e) {
            lblStatus.setText("Error: " + e.getMessage());
            lblStatus.setForeground(Color.RED);
        }
    }
}
