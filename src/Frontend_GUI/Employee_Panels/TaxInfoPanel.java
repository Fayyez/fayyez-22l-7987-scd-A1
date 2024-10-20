package Frontend_GUI.Employee_Panels;

import utils.TaxManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class TaxInfoPanel extends JPanel {
    private JTable taxTable;
    private DefaultTableModel tableModel;
    private JButton saveButton;
    private TaxManager taxManager;

    public TaxInfoPanel() {
        taxManager = TaxManager.getInstance();
        setLayout(new BorderLayout());

        // Define column names
        String[] columnNames = {"Type", "Regular Unit Price", "Peak Unit Price", "Tax Percentage", "Fixed Charges", "Edit"};

        // Initialize table model
        tableModel = new DefaultTableModel(columnNames, 0);

        // Populate table with tax information
        populateTable();

        // Create table and set properties
        taxTable = new JTable(tableModel) {
            public boolean isCellEditable(int row, int column) {
                // Make only the edit button column editable
                return column == 5;
            }
        };

        // Add a button to each row for editing
        taxTable.getColumn("Edit").setCellRenderer(new ButtonRenderer());
        taxTable.getColumn("Edit").setCellEditor(new ButtonEditor(new JCheckBox()));

        JScrollPane scrollPane = new JScrollPane(taxTable);
        add(scrollPane, BorderLayout.CENTER);

        // Save button at the bottom
        saveButton = new JButton("Save Changes");
        add(saveButton, BorderLayout.SOUTH);

        // Add action listener to save button
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveTaxInfo();
            }
        });
    }

    // Method to populate the table with tax information
    private void populateTable() {
        String[] types = {"1Phase Domestic", "1Phase Commercial", "3Phase Domestic", "3Phase Commercial"};
        for (int i = 0; i < 4; i++) {
            Object[] rowData = new Object[]{
                    types[i],
                    taxManager.getRegUnitPrice(i),
                    taxManager.getPeakUnitPrice(i),
                    taxManager.getTaxPercentage(i),
                    taxManager.getFixedCharge(i),
                    "Edit"
            };
            tableModel.addRow(rowData);
        }
    }

    // Method to save the updated tax information back to the file
    private void saveTaxInfo() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/database/taxInfo.txt"))) {
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String type = (String) tableModel.getValueAt(i, 0);
                int regUnitPrice = Integer.parseInt(tableModel.getValueAt(i, 1).toString());
                int peakUnitPrice = Integer.parseInt(tableModel.getValueAt(i, 2).toString());
                float taxPercentage = Float.parseFloat(tableModel.getValueAt(i, 3).toString());
                int fixedCharges = Integer.parseInt(tableModel.getValueAt(i, 4).toString());

                // Ensure no negative values
                if (regUnitPrice < 0 || peakUnitPrice < 0 || taxPercentage < 0 || fixedCharges < 0) {
                    JOptionPane.showMessageDialog(this, "Values cannot be negative!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Update the tax manager with new values
                taxManager.updateTaxInfo(i, regUnitPrice, peakUnitPrice, taxPercentage, fixedCharges);

                // Write to the file
                String line = String.format("%s,%d,%d,%.2f,%d", type, regUnitPrice, peakUnitPrice, taxPercentage, fixedCharges);
                writer.write(line);
                writer.newLine();
            }
            JOptionPane.showMessageDialog(this, "Tax information saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error saving file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Button renderer class for the edit button
    class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "Edit" : value.toString());
            return this;
        }
    }

    // Button editor class for the edit button
    class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private String label;
        private boolean isPushed;
        private int row;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                }
            });
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            label = (value == null) ? "Edit" : value.toString();
            button.setText(label);
            isPushed = true;
            this.row = row;
            return button;
        }

        public Object getCellEditorValue() {
            if (isPushed) {
                // Edit logic here
                int regUnitPrice = Integer.parseInt(JOptionPane.showInputDialog("Enter Regular Unit Price:"));
                int peakUnitPrice = Integer.parseInt(JOptionPane.showInputDialog("Enter Peak Unit Price:"));
                float taxPercentage = Float.parseFloat(JOptionPane.showInputDialog("Enter Tax Percentage:"));
                int fixedCharges = Integer.parseInt(JOptionPane.showInputDialog("Enter Fixed Charges:"));

                // Ensure no negative values
                if (regUnitPrice < 0 || peakUnitPrice < 0 || taxPercentage < 0 || fixedCharges < 0) {
                    JOptionPane.showMessageDialog(null, "Values cannot be negative!", "Error", JOptionPane.ERROR_MESSAGE);
                    return label;
                }

                // Update the table model
                tableModel.setValueAt(regUnitPrice, row, 1);
                tableModel.setValueAt(peakUnitPrice, row, 2);
                tableModel.setValueAt(taxPercentage, row, 3);
                tableModel.setValueAt(fixedCharges, row, 4);
            }
            isPushed = false;
            return label;
        }

        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }

        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }
    }
}
