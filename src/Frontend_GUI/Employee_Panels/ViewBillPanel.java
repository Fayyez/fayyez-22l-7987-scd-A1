package Frontend_GUI.Employee_Panels;

import models.Bill;
import utils.BillManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ViewBillPanel extends JPanel {
    private JTextField searchField;
    private JLabel lblSearchIcon;
    private Bill currentBill;
    private JPanel pnlContent;

    public ViewBillPanel() {
        this.init();
    }

    private void init() {
        // Set layout and background
        this.setLayout(new BorderLayout(20, 20)); // 20px padding for modern look
        this.setBackground(Color.WHITE);

        // Create header with search bar and search icon
        JPanel pnlHeader = new JPanel(new GridBagLayout()); // Use GridBagLayout for modern alignment
        pnlHeader.setBackground(new Color(33, 150, 243));
        pnlHeader.setPreferredSize(new Dimension(0, 80)); // Reduce height for a cleaner look

        // Constraints for search components
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;

        // Search field (smaller and more modern)
        searchField = new JTextField(15); // Reduced the width of the text field
        searchField.setFont(new Font("Arial", Font.PLAIN, 16)); // Smaller font for compact appearance
        searchField.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Padding inside the text field
        searchField.setText("Enter Customer ID...");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        pnlHeader.add(searchField, gbc);

        // Search icon (modern style)
        lblSearchIcon = new JLabel(new ImageIcon(new ImageIcon("src/Assets/search_icon.png").getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
        lblSearchIcon.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.0;
        pnlHeader.add(lblSearchIcon, gbc);

        // Add search panel to the top
        this.add(pnlHeader, BorderLayout.NORTH);

        // Main content area for showing bill details
        pnlContent = new JPanel(new GridBagLayout());
        pnlContent.setBackground(Color.WHITE);
        this.add(pnlContent, BorderLayout.CENTER);

        // Action listener for the search
        lblSearchIcon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                try {
                    searchBill();
                } catch (Exception e) {
                    System.out.println("Error searching for bill: " + e.getMessage());
                }
            }
        });
    }

    // Method to handle searching for a bill
    private void searchBill() throws Exception {
        String customerIdStr = searchField.getText().trim();
        if (customerIdStr.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please enter a customer ID.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int customerId;
        try {
            customerId = Integer.parseInt(customerIdStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid customer ID format.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Get the latest bill for the entered customer ID
        Bill temp = BillManager.getLatestBill(customerId);

        if (temp == null) {
            JOptionPane.showMessageDialog(null, "No bills found for this customer.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            currentBill = temp;
            showBillDetails();
        }
    }

    // Method to show the bill details in the panel
    private void showBillDetails() {
        pnlContent.removeAll(); // Clear the panel before displaying new content

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        Font headingFont = new Font("Arial", Font.BOLD, 16);
        Font labelFont = new Font("Arial", Font.PLAIN, 14);

        // Bill Information Panel
        JPanel pnlBillInfo = createInfoPanel("Bill Information", headingFont, labelFont);
        addInfoRow(pnlBillInfo, "Customer ID:", String.valueOf(currentBill.getCustomerID()), labelFont);
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

        gbc.gridx = 0;
        gbc.gridy = 0;
        pnlContent.add(pnlBillInfo, gbc);

        // Button to mark the bill as paid (if unpaid)
        if (!currentBill.getIsPaid()) {
            JButton btnMarkAsPaid = new JButton("Mark as Paid");
            btnMarkAsPaid.setFont(new Font("Arial", Font.BOLD, 14));
            gbc.gridx = 0;
            gbc.gridy = 1;
            pnlContent.add(btnMarkAsPaid, gbc);

            // Action listener for marking the bill as paid
            btnMarkAsPaid.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    markBillAsPaid();
                }
            });
        }

        // Refresh the content panel
        pnlContent.revalidate();
        pnlContent.repaint();
    }

    private JPanel createInfoPanel(String title, Font headingFont, Font labelFont) {
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10)); // 10px horizontal and vertical gaps
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), title,
                0, 0, headingFont, Color.BLACK));
        panel.setBackground(new Color(240, 240, 240));
        return panel;
    }

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

    // Method to mark a bill as paid
    private void markBillAsPaid() {
        try {
            if (BillManager.setBillToPaid(currentBill.getCustomerID(), currentBill.getBillingmonth())) {
                JOptionPane.showMessageDialog(null, "Bill marked as paid successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                searchBill(); // Refresh the bill details
            } else {
                JOptionPane.showMessageDialog(null, "Failed to mark the bill as paid.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "An error occurred while marking the bill as paid.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
