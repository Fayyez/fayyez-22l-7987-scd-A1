package Frontend_GUI.Customer_Panels;

import models.Bill;
import models.Customer;
import utils.BillManager;

import javax.swing.*;
import java.awt.*;

public class ViewBillPanel extends JPanel {
    private Customer customer;
    private Bill currentBill;

    public ViewBillPanel(Customer customer) {
        this.customer = customer;
        this.currentBill = BillManager.getLatestBill(customer.getId());

        if (currentBill == null) {
            JOptionPane.showMessageDialog(null, "No bills found for this customer", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        } else {
            this.init();
        }
    }

    private void init() {
        // Set the main panel's layout and background
        this.setLayout(new BorderLayout());
        this.setBackground(Color.WHITE);

        // Create header with logo and title
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(new Color(33, 150, 243));
        pnlHeader.setPreferredSize(new Dimension(0, 100));

        // Adding LESCO logo
        JLabel lblLogo = new JLabel(new ImageIcon("src/Assets/LESCO_logo.png"));
        lblLogo.setPreferredSize(new Dimension(100, 100));
        pnlHeader.add(lblLogo, BorderLayout.WEST);

        // Adding title
        JLabel lblTitle = new JLabel("Customer Bill", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 36));
        lblTitle.setForeground(Color.WHITE);
        pnlHeader.add(lblTitle, BorderLayout.CENTER);

        this.add(pnlHeader, BorderLayout.NORTH);

        // Create main content panel with grid layout
        JPanel pnlContent = new JPanel();
        pnlContent.setLayout(new GridBagLayout());
        pnlContent.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10); // Padding between elements

        // Define fonts and styles
        Font headingFont = new Font("Arial", Font.BOLD, 16);
        Font labelFont = new Font("Arial", Font.PLAIN, 14);

        // Customer Information Panel
        JPanel pnlCustomerInfo = createInfoPanel("Customer Information", headingFont, labelFont);
        addInfoRow(pnlCustomerInfo, "Customer ID:", String.valueOf(customer.getId()), labelFont);
        addInfoRow(pnlCustomerInfo, "Customer Name:", customer.getName(), labelFont);
        addInfoRow(pnlCustomerInfo, "Address:", customer.getAddress(), labelFont);
        addInfoRow(pnlCustomerInfo, "Phone:", customer.getPhone(), labelFont);
        addInfoRow(pnlCustomerInfo, "Customer Type:", customer.getIsDomesticStr(), labelFont);
        addInfoRow(pnlCustomerInfo, "Meter Type:", customer.getIsDomestic() ? "Single-phase" : "Three-phase", labelFont);

        // Adding Customer Info Panel to Content Panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        pnlContent.add(pnlCustomerInfo, gbc);

        // Bill Information Panel
        JPanel pnlBillInfo = createInfoPanel("Bill Information", headingFont, labelFont);
        addInfoRow(pnlBillInfo, "Billing Month:", String.valueOf(currentBill.getBillingmonth()), labelFont);
        addInfoRow(pnlBillInfo, "Regular Reading:", String.valueOf(currentBill.getCurrent_reg_reading()), labelFont);
        addInfoRow(pnlBillInfo, "Peak Reading:", String.valueOf(currentBill.getCurrent_peak_reading()), labelFont);
        addInfoRow(pnlBillInfo, "Cost of Electricity:", "Rs. " + currentBill.getCost(), labelFont);
        addInfoRow(pnlBillInfo, "Tax Amount:", "Rs. " + currentBill.getTaxAmount(), labelFont);
        addInfoRow(pnlBillInfo, "Fixed Charges:", "Rs. " + currentBill.getFixedcharges(), labelFont);
        addInfoRow(pnlBillInfo, "Total Bill:", "Rs. " + currentBill.getTotalbill(), labelFont);
        addInfoRow(pnlBillInfo, "Due Date:", currentBill.getDueDate().toString(), labelFont);
        addInfoRow(pnlBillInfo, "Payment Status:", currentBill.getIsPaid() ? "Paid" : "Unpaid", labelFont);
        addInfoRow(pnlBillInfo, "Paid Date:", currentBill.getPaidDateStr(), labelFont);

        // Adding Bill Info Panel to Content Panel
        gbc.gridx = 0;
        gbc.gridy = 1;
        pnlContent.add(pnlBillInfo, gbc);

        // Adding main content panel to the center
        this.add(pnlContent, BorderLayout.CENTER);
    }

    // Utility method to create a panel with a title and border
    private JPanel createInfoPanel(String title, Font headingFont, Font labelFont) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 2, 5, 5)); // Two-column layout
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), title,
                0, 0, headingFont, Color.BLACK));
        panel.setBackground(new Color(240, 240, 240)); // Light gray background
        return panel;
    }

    // Utility method to add information rows to the panel
    private void addInfoRow(JPanel panel, String label, String value, Font labelFont) {
        JLabel lblLabel = new JLabel(label);
        lblLabel.setFont(labelFont);
        lblLabel.setHorizontalAlignment(SwingConstants.LEFT);

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(labelFont);
        lblValue.setHorizontalAlignment(SwingConstants.RIGHT);

        panel.add(lblLabel);
        panel.add(lblValue);
    }
}
