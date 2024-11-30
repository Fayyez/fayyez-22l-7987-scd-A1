package Frontend_GUI.Employee_Panels;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import models.Customer;
import models.OnePhaseCust;
import models.ThreePhaseCust;
import utils.CustomerManager;

public class ViewCustomersPanel extends JPanel {
    private JTable customerTable;
    private DefaultTableModel tableModel;
    private ArrayList<Customer> allCustomers = new ArrayList<>(); // List of all the customers in DB
    private JScrollPane scrollPane_table; // Changed to JScrollPane
    private JPanel pnl_top;
    private JPanel pnl_bottom;
    private JTextField txt_searchbar;

    public ViewCustomersPanel() {
        init();
    }
    public void init() {
        setLayout(new BorderLayout());

        // Setting the top panel
        pnl_top = new JPanel();
        pnl_top.setLayout(new FlowLayout());
        JLabel lbl_heading = new JLabel("Customers database");
        lbl_heading.setFont(new Font("Arial", Font.BOLD, 16));
        pnl_top.add(lbl_heading, FlowLayout.LEFT);
        txt_searchbar = new JTextField(30);
        pnl_top.add(txt_searchbar, FlowLayout.CENTER);
        ImageIcon searchIcon = new ImageIcon(new ImageIcon("src/Assets/search_icon.png").getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
        JLabel lbl_icon = new JLabel(searchIcon); // Use JLabel to hold the icon
        pnl_top.add(lbl_icon);
        add(pnl_top, BorderLayout.NORTH);

        // Table setup
        String[] columnNames = {"ID", "CNIC", "Name", "Address", "Phone", "Type", "Domestic/Commercial", "Update", "Remove"};
        tableModel = new DefaultTableModel(columnNames, 0);
        customerTable = new JTable(tableModel);

        // Make the table scrollable
        scrollPane_table = new JScrollPane(customerTable);
        add(scrollPane_table, BorderLayout.CENTER);

        // Load all customers into the table
        loadAllCustomers();

        // Button renderers for update and remove
        customerTable.getColumn("Update").setCellRenderer(new ButtonRenderer());
        customerTable.getColumn("Remove").setCellRenderer(new ButtonRenderer());

        customerTable.getColumn("Update").setCellEditor(new ButtonEditor(new JCheckBox(), row -> updateCustomer(row)));
        customerTable.getColumn("Remove").setCellEditor(new ButtonEditor(new JCheckBox(), row -> removeCustomer(row)));

        // Bottom panel with save button
        pnl_bottom = new JPanel();
        JButton btn_save = new JButton("Save Changes");
        btn_save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveCustomers(); // Save current state of the customers
            }
        });
        pnl_bottom.add(btn_save);
        add(pnl_bottom, BorderLayout.SOUTH);

        // Add search functionality
        txt_searchbar.addActionListener(e -> searchCustomers()); // Trigger search on pressing Enter
    }
    // Method to load all customers and display them in the table
    private void loadAllCustomers() {
        try {
            allCustomers.clear(); // Clear any previous data
            CustomerManager.getListOfCustomers(allCustomers); // Load customers from file
            updateTable(allCustomers); // Populate table
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // Method to update the table with the list of customers
    private void updateTable(ArrayList<Customer> customers) {
        tableModel.setRowCount(0); // Clear the table
        for (Customer c : customers) {
            String meterType = c instanceof OnePhaseCust ? "Single" : "Three";
            String isDomestic = c.getIsDomesticStr(); // Get domestic/commercial value
            tableModel.addRow(new Object[]{c.getId(), c.getCnic(), c.getName(), c.getAddress(), c.getPhone(), meterType, isDomestic, "Update", "Remove"});
        }
    }
    // Method to search customers based on the search bar input
    private void searchCustomers() {
        String searchText = txt_searchbar.getText().toLowerCase();
        ArrayList<Customer> filteredCustomers = new ArrayList<>();
        for (Customer c : allCustomers) {
            if (String.valueOf(c.getId()).contains(searchText) ||
                    String.valueOf(c.getCnic()).contains(searchText) ||
                    c.getName().toLowerCase().contains(searchText) ||
                    c.getAddress().toLowerCase().contains(searchText) ||
                    c.getPhone().contains(searchText)) {
                filteredCustomers.add(c);
            }
        }
        updateTable(filteredCustomers);
    }
    private void saveCustomers() {
        CustomerManager.writeCustomerInfo(allCustomers); // Save customers back to file
        JOptionPane.showMessageDialog(this, "Changes saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    // Method to update a customer (to be implemented)
    private void updateCustomer(int row) {
        int customerId = (int) tableModel.getValueAt(row, 0);
        // Logic to open a dialog and edit the customer information based on ID
        System.out.println("Update button clicked for ID: " + customerId);
        ArrayList<Customer> temp_customers = new ArrayList<>();
        try {
            CustomerManager.getListOfCustomers(temp_customers);
            for (Customer c : temp_customers) {
                if (c.getId() == customerId) {// replace that customer with the new data from that row of the table
                    Customer updated_cust;
                    if(c instanceof OnePhaseCust) {
                        // copy the data from table into the customer object and save
                        // String[] columnNames = {"ID", "CNIC", "Name", "Address", "Phone", "Type", "Domestic/Commercial", "Update", "Remove"};
                        updated_cust = new OnePhaseCust(c.getId(),
                                                        (Double) tableModel.getValueAt(row, 1),
                                                        (String) tableModel.getValueAt(row, 2),
                                                        (String) tableModel.getValueAt(row, 3),
                                                        (String) tableModel.getValueAt(row, 4),
                                                        ((String) tableModel.getValueAt(row, 6)).equalsIgnoreCase("domestic"),
                                                        c.getConnectionDateObj(),
                                                        c.getUnitsConsumed()
                                        );
                    } else {
                        updated_cust = new ThreePhaseCust(c.getId(),
                                                        (Double) tableModel.getValueAt(row, 1),
                                                        (String) tableModel.getValueAt(row, 2),
                                                        (String) tableModel.getValueAt(row, 3),
                                                        (String) tableModel.getValueAt(row, 4),
                                                        ((String) tableModel.getValueAt(row, 6)).equalsIgnoreCase("domestic"),
                                                        c.getConnectionDateObj(),
                                                        c.getUnitsConsumed(),
                                                        ((ThreePhaseCust) c).getPeakUnitsConsumed()
                                        );
                    }
                    int index = temp_customers.indexOf(c);
                    temp_customers.set(index, updated_cust);
                    break;// update and break the loop
                }
            }
            // now write these changes in all customers
            allCustomers = temp_customers;
            updateTable(allCustomers);
            saveCustomers();
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Error loading customer data", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to remove a customer
    private void removeCustomer(int row) {
        int customerId = (int) tableModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to remove customer ID: " + customerId + "?", "Confirm Remove", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            allCustomers.removeIf(c -> c.getId() == customerId); // Remove customer from the list
            updateTable(allCustomers); // Update the table
            saveCustomers(); // Save changes to file
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
                buttonClickListener.onButtonClick(customerTable.getSelectedRow());
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