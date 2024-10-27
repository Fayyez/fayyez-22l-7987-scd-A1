package Frontend_GUI.Employee_Panels;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import models.Bill;
import utils.BillManager;
import utils.DateBuilder;

public class ViewBillsPanel extends JPanel {
    private JTable billTable;
    private DefaultTableModel tableModel;
    private ArrayList<Bill> allBills = new ArrayList<>(); // List of all bills in DB
    private JScrollPane scrollPaneTable;
    private JPanel pnlTop;
    private JPanel pnlBottom;
    private JTextField txtSearchBar;

    public ViewBillsPanel() {
        init();
    }

    public void init() {
        setLayout(new BorderLayout());

        // Setting up the top panel
        pnlTop = new JPanel();
        pnlTop.setLayout(new FlowLayout());
        JLabel lblHeading = new JLabel("Bills Database");
        lblHeading.setFont(new Font("Arial", Font.BOLD, 16));
        pnlTop.add(lblHeading, FlowLayout.LEFT);

        txtSearchBar = new JTextField(30);
        pnlTop.add(txtSearchBar, FlowLayout.CENTER);

        ImageIcon searchIcon = new ImageIcon(new ImageIcon("src/Assets/search_icon.png").getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
        JLabel lblIcon = new JLabel(searchIcon);
        pnlTop.add(lblIcon);

        add(pnlTop, BorderLayout.NORTH);

        // Table setup
        String[] columnNames = {"Billing Month", "Customer ID", "Amount", "Due Date", "Paid", "Update", "Remove"};
        tableModel = new DefaultTableModel(columnNames, 0);
        billTable = new JTable(tableModel);

        scrollPaneTable = new JScrollPane(billTable);
        add(scrollPaneTable, BorderLayout.CENTER);

        loadAllBills();

        billTable.getColumn("Update").setCellRenderer(new ButtonRenderer());
        billTable.getColumn("Remove").setCellRenderer(new ButtonRenderer());

        billTable.getColumn("Update").setCellEditor(new ButtonEditor(new JCheckBox(), row -> updateBill(row)));
        billTable.getColumn("Remove").setCellEditor(new ButtonEditor(new JCheckBox(), row -> removeBill(row)));

        // Bottom panel with save button
        pnlBottom = new JPanel();
        JButton btnSave = new JButton("Save Changes");
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveBills();
            }
        });
        pnlBottom.add(btnSave);
        add(pnlBottom, BorderLayout.SOUTH);

        // Add search functionality
        txtSearchBar.addActionListener(e -> searchBills());
    }

    // Load all bills into the table
    private void loadAllBills() {
        try {
            allBills.clear();
            BillManager.getListOfBills(allBills);
            updateTable(allBills);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Update the table with the list of bills
    private void updateTable(ArrayList<Bill> bills) {
        tableModel.setRowCount(0);
        for (Bill b : bills) {
            tableModel.addRow(new Object[]{b.getBillingmonth(), b.getCustomerID(), b.getTotalbill(), DateBuilder.getDateStr(b.getDueDate()), b.getIsPaid() ? "Yes" : "No", "Update", "Remove"});
        }
    }

    // Search bills based on the search bar input
    private void searchBills() {
        String searchText = txtSearchBar.getText().toLowerCase();
        ArrayList<Bill> filteredBills = new ArrayList<>();
        for (Bill b : allBills) {
            if (String.valueOf(b.getBillingmonth()).contains(searchText) ||
                    String.valueOf(b.getCustomerID()).contains(searchText) ||
                    String.valueOf(b.getTotalbill()).contains(searchText) ||
                    b.getDueDate().toString().contains(searchText)) {
                filteredBills.add(b);
            }
        }
        updateTable(filteredBills);
    }

    private void saveBills() {
        BillManager.writeBillInfo(allBills);
        JOptionPane.showMessageDialog(this, "Changes saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    // Update a bill
    private void updateBill(int row) {
        int billId = (int) tableModel.getValueAt(row, 0);
        Bill updatedBill = null;

        for (Bill b : allBills) {
            if (b.getBillingmonth() == billId) {
                b.setTotalbill(Integer.parseInt( tableModel.getValueAt(row, 2).toString() ));
                b.setDueDate( DateBuilder.getDateobj(tableModel.getValueAt(row, 3).toString()) ); // Assuming dueDate is a String in this case
                b.setIsPaid("Yes".equals(tableModel.getValueAt(row, 4)));
                updatedBill = b;
                break;
            }
        }

        if (updatedBill != null) {
            JOptionPane.showMessageDialog(this, "Bill updated for ID: " + billId, "Update Successful", JOptionPane.INFORMATION_MESSAGE);
            saveBills();
        } else {
            JOptionPane.showMessageDialog(this, "Error updating bill", "Update Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Remove a bill
    private void removeBill(int row) {
        int billId = (int) tableModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to remove Bill ID: " + billId + "?", "Confirm Remove", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            allBills.removeIf(b -> b.getBillingmonth() == billId);
            updateTable(allBills);
            saveBills();
        }
    }

    // Custom button renderer for JTable
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    // Custom button editor for JTable
    class ButtonEditor extends DefaultCellEditor {
        private String label;
        private JButton button;
        private boolean isPushed;
        private final ButtonClickListener buttonClickListener;

        public ButtonEditor(JCheckBox checkBox, ButtonClickListener buttonClickListener) {
            super(checkBox);
            this.button = new JButton();
            this.button.setOpaque(true);
            this.buttonClickListener = buttonClickListener;
            this.button.addActionListener(e -> fireEditingStopped());
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            isPushed = true;
            return button;
        }

        public Object getCellEditorValue() {
            if (isPushed) {
                buttonClickListener.onButtonClick(billTable.getSelectedRow());
            }
            isPushed = false;
            return label;
        }
    }

    // Functional interface for handling button clicks in the table
    @FunctionalInterface
    interface ButtonClickListener {
        void onButtonClick(int row);
    }
}
