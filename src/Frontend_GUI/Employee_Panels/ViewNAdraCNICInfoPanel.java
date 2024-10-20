package Frontend_GUI.Employee_Panels;

import models.Cnic;
import utils.CNICManager;
import utils.DateBuilder;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ViewNAdraCNICInfoPanel extends JPanel {
    private JTable cnicTable;
    private DefaultTableModel tableModel;
    private ArrayList<Cnic> cnics;
    private JTextField searchField;
    private JCheckBox showExpiringCheckBox;

    public ViewNAdraCNICInfoPanel() {
        this.init();
    }

    private void init() {
        // Set layout
        this.setLayout(new BorderLayout());

        // Create search panel
        JPanel searchPanel = new JPanel();
        searchField = new JTextField(15);
        JButton searchButton = new JButton("Search");
        showExpiringCheckBox = new JCheckBox("Show Expiring (within 30 days)");
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(showExpiringCheckBox);

        // Table setup
        String[] columns = {"CNIC Number", "Expiry Date", "Edit"};
        tableModel = new DefaultTableModel(columns, 0);
        cnicTable = new JTable(tableModel);
        cnicTable.setRowHeight(30);
        JScrollPane scrollPane = new JScrollPane(cnicTable);

        // Save button
        JButton saveButton = new JButton("Save Changes");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveChanges();
            }
        });

        // Add components to the panel
        this.add(searchPanel, BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);
        this.add(saveButton, BorderLayout.SOUTH);

        // Load CNICs into the table
        loadCnics();

        // Add action listeners
        searchButton.addActionListener(e -> searchCnic());
        showExpiringCheckBox.addActionListener(e -> updateTable());
    }

    // Load CNICs from file into the table
    private void loadCnics() {
        try {
            cnics = new ArrayList<>();
            CNICManager.getAllCnics(cnics);

            // Clear the current rows
            tableModel.setRowCount(0);

            for (Cnic cnic : cnics) {
                Object[] rowData = {
                        cnic.getNumber(),
                        cnic.getDateStr(),
                        createEditButton(cnic)
                };
                tableModel.addRow(rowData);
                highlightExpiringRows(cnic);
            }
        } catch (Exception e) {
            showErrorDialog("Error loading CNICs: " + e.getMessage());
        }
    }

    // Highlight rows that are expiring
    private void highlightExpiringRows(Cnic cnic) {
        try {
            if (DateBuilder.lessThan30DaysDifference(cnic.getDate())) {
                int rowIndex = tableModel.getRowCount() - 1; // Last added row
                cnicTable.setValueAt("<html><font color='red'>" + cnic.getDateStr() + "</font></html>", rowIndex, 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Create an edit button for each row
    private JButton createEditButton(Cnic cnic) {
        JButton editButton = new JButton("Edit");
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editCnic(cnic);
            }
        });
        return editButton;
    }

    // Edit CNIC expiry date only
    private void editCnic(Cnic cnic) {
        String newExpiryDate = JOptionPane.showInputDialog(this, "Enter new Expiry Date (dd/MM/yyyy):", cnic.getDateStr());
        if (newExpiryDate != null) {
            try {
                // Validate and update the expiry date
                cnic.setDate(newExpiryDate);
                loadCnics(); // Reload CNICs to reflect changes
            } catch (Exception e) {
                showErrorDialog("Invalid expiry date format. Please use dd/MM/yyyy.");
            }
        }
    }

    // Search CNIC by number
    private void searchCnic() {
        String searchQuery = searchField.getText().trim();
        tableModel.setRowCount(0); // Clear the current table

        for (Cnic cnic : cnics) {
            if (cnic.getNumber().contains(searchQuery)) {
                Object[] rowData = {
                        cnic.getNumber(),
                        cnic.getDateStr(),
                        createEditButton(cnic)
                };
                tableModel.addRow(rowData);
                highlightExpiringRows(cnic);
            }
        }

        if (tableModel.getRowCount() == 0) {
            showErrorDialog("No CNIC found for the search query: " + searchQuery);
        }
    }

    // Update the table based on the show expiring checkbox
    private void updateTable() {
        tableModel.setRowCount(0); // Clear the current table
        try {
            if (showExpiringCheckBox.isSelected()) {
                ArrayList<Cnic> expiringCnics = CNICManager.getCnicsAboutToExpireIn30Days();
                for (Cnic cnic : expiringCnics) {
                    Object[] rowData = {
                            cnic.getNumber(),
                            cnic.getDateStr(),
                            createEditButton(cnic)
                    };
                    tableModel.addRow(rowData);
                    highlightExpiringRows(cnic);
                }
            } else {
                loadCnics(); // Load all CNICs
            }
        } catch (Exception e) {
            showErrorDialog("Error retrieving expiring CNICs: " + e.getMessage());
        }
    }

    // Save changes to the file
    private void saveChanges() {
        try {
            CNICManager.writeCnicInfo(cnics); // Save all CNICs back to file
            JOptionPane.showMessageDialog(this, "Changes saved successfully!");
        } catch (Exception e) {
            showErrorDialog("Error saving changes: " + e.getMessage());
        }
    }

    // Show error dialog
    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
