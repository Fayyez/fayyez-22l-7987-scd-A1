package Frontend_GUI;

import Frontend_GUI.Employee_Panels.*;
import models.Employee;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class AdminDashboard extends JFrame {
    // attributes
    public static int WIDTH = 1000;
    public static int HEIGHT = 700;
    private Employee user;

    // gui elements //
    private JSplitPane pane_main_split; // (left half has all available operations list, right half has all the details)
    private JTabbedPane pane_tabs; // (right half has tabs for each operation)
    private JPanel pnl_left;
    private JPanel pnl_right_container;
    private JPanel pnl_right;
    private CardLayout card_layout_right_pnl;

    // left panel buttons
    private JLabel lbl_heading;
    private JLabel lbl_heading_customer_management;
    private JButton btn_view_all_customers_table;
    private JButton btn_add_customer;
    private JButton btn_nadra_cnic_info_table;
    private JLabel lbl_heading_bill_management;
    private JButton btn_view_all_bills_table;
    private JButton btn_add_bill;
    private JButton btn_view_bill;// view one bill + set to paid
    private JButton btn_tax_info_table;
    private JLabel lbl_heading_employee_management;
    private JButton btn_add_employee;
    private JButton btn_update_password;
    private JButton btn_logout;

    // right panel stuff here
    private JLabel lbl_right_panel_title;
    private BorderLayout border_layout_right_pnl;
    private JPanel pnl_view_all_customers;
    private JPanel pnl_add_customer;
    private JPanel pnl_nadra_cnic_info;

    private JPanel pnl_view_all_bills;
    private JPanel pnl_add_bill;
    private JPanel pnl_view_bill;
    private JPanel pnl_tax_info;

    private JPanel pnl_add_employee;
    private JPanel pnl_update_password;

    public AdminDashboard(Employee user) {
        this.user = user;
        this.init();
        setVisible(true);
    }

    private void init() {
        // set default global settings
        setTitle("Admin Dashboard");
        setIconImage(new ImageIcon("src/Assets/logo.png").getImage());
        setBounds(450,0,WIDTH,HEIGHT);
        setPreferredSize(getSize());
        setMaximizedBounds(getBounds());
        setMinimumSize(getSize());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pane_main_split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        pane_main_split.setDividerLocation(200);
        // sset left panel with all the options
        setUpLeftPanel();
        // set right panel
        setUpRightPanel();
        
        // set all actioin buttons just like customer dashboard

        setContentPane(pane_main_split);

    }

    private void setUpRightPanel() {
        // create border layout in right panel container wala and then
        // add pnl_right to below
        pnl_right_container = new JPanel();
        border_layout_right_pnl = new BorderLayout();
        pnl_right_container.setLayout(border_layout_right_pnl);
        // setting up the right panel elements
        lbl_right_panel_title = new JLabel("Customers Data", JLabel.CENTER);// all customers is the default card to be shown on the screen when panel is opened
        lbl_right_panel_title.setFont(new Font("Arial", Font.BOLD, 20));
        lbl_right_panel_title.setPreferredSize(new Dimension(0, 50));
        lbl_right_panel_title.setBackground(Color.BLUE);
        lbl_right_panel_title.setForeground(Color.WHITE);
        lbl_right_panel_title.setOpaque(true);
        pnl_right_container.add(lbl_right_panel_title, BorderLayout.NORTH);

        card_layout_right_pnl = new CardLayout();
        pnl_right = new JPanel();
        pnl_right.setLayout(card_layout_right_pnl);
        pnl_right.setBackground(new Color(192, 192, 192));
        pnl_right.setOpaque(true);

        pnl_right_container.add(pnl_right, BorderLayout.CENTER);
        // setting up panels and default panel to be shown and add to the pnl_right
        this.SetAllPanelCards();
        pane_main_split.setRightComponent(pnl_right_container);
    }

    private void SetAllPanelCards() {
        // customer management panels
        pnl_view_all_customers = new ViewCustomersPanel();
        pnl_right.add(pnl_view_all_customers, "View All Customers");
        this.pnl_add_customer = new AddCustomerFormPanel();
        pnl_view_all_customers = new ViewCustomersPanel();
        // bill management panels
        // employee management panels

        // showing default panel
        card_layout_right_pnl.show(pnl_right, "View All Customers");
    }

    public void setRightPanelTitle(String title, int alignment) {
        // set the title of the right panel\
        lbl_right_panel_title.setText(title);
        lbl_right_panel_title.setHorizontalAlignment(alignment);
    }

    private void setUpLeftPanel() {
        // main panle layout
        pnl_left = new JPanel();
        pnl_left.setLayout(new GridLayout(18, 1, 4, 0));
        pnl_left.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pnl_left.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
        pnl_left.setBackground(Color.BLACK);

        /// panel elements ///
        // heading
        lbl_heading = new JLabel("Admin Panel", JLabel.CENTER);
        lbl_heading.setFont(new Font("Arial", Font.BOLD, 18));
        lbl_heading.setForeground(Color.WHITE);
        lbl_heading.setBackground(Color.LIGHT_GRAY);
        lbl_heading.setIcon(  new ImageIcon(new ImageIcon("src/Assets/logo.png").getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
        lbl_heading.setHorizontalTextPosition(JLabel.TRAILING);
        lbl_heading.setIconTextGap(4);
        lbl_heading.setOpaque(true);
        // adding action buttons
        this.setAllMenuButtons();
        // adding elements
        pane_tabs = new JTabbedPane();
        pane_tabs.addTab("Actions", pnl_left);
        this.addActionButtonsFunctionality();
        pane_main_split.setLeftComponent(pane_tabs);

    }

    private void addActionButtonsFunctionality() {
        // customer management panels
        addPanelAction(btn_view_all_customers_table, new ViewCustomersPanel(), "View All Customers", "View All Customers");
        addPanelAction(btn_add_customer, new AddCustomerFormPanel(), "Add Customer", "Add Customer");
        addPanelAction(btn_nadra_cnic_info_table, new ViewNAdraCNICInfoPanel(), "NADRA CNIC Info", "NADRA CNIC Info");
        // bill management panels
        addPanelAction(btn_view_all_bills_table, new ViewBillsPanel(), "View All Bills", "View All Bills");
        addPanelAction(btn_add_bill, new AddBillFormPanel(), "Add Bill", "Add Bill");
        addPanelAction(btn_view_bill, new ViewBillPanel(), "View Bill", "View Bill");
        addPanelAction(btn_tax_info_table, new TaxInfoPanel(), "Tax Info", "Tax Info");
        // employee management panels
        addPanelAction(btn_add_employee, new AddEmployeeFormPanel(), "Add Employee", "Add Employee");
        addPanelAction(btn_update_password, new UpdatePasswordPanel(), "Update Password", "Update Password");
        // logout functionality
        btn_logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Admin logged out");
                dispose();
                new LoginPage();
            }
        });
    }

    private void addPanelAction(JButton button, JPanel panel, String panelName, String title) {
        button.addActionListener(e -> {
            pnl_right.add(panel, panelName);
            card_layout_right_pnl.show(pnl_right, panelName);
            setRightPanelTitle(title, JLabel.CENTER);
        });
    }

    private void setAllMenuButtons() {
        // Initializing all Headings and subheadings
        lbl_heading_customer_management = new JLabel("Customer Management", JLabel.LEFT);
        lbl_heading_customer_management.setFont(new Font("Arial", Font.ITALIC, 14));
        lbl_heading_bill_management = new JLabel("Bill Management", SwingConstants.LEFT);
        lbl_heading_bill_management.setFont(new Font("Arial", Font.ITALIC,14));
        lbl_heading_employee_management = new JLabel("Employee Management", SwingConstants.LEFT);
        lbl_heading_employee_management.setFont(new Font("Arial", Font.ITALIC, 14));

        lbl_heading.setForeground(Color.WHITE);
        lbl_heading_customer_management.setForeground(Color.WHITE);
        lbl_heading_bill_management.setForeground(Color.WHITE);
        lbl_heading_employee_management.setForeground(Color.WHITE);

        // Initializing all buttons
        btn_view_all_customers_table = new JButton("View All Customers");
        btn_add_customer = new JButton("Add Customer");
        btn_nadra_cnic_info_table = new JButton("NADRA CNIC Info");

        btn_view_all_bills_table = new JButton("View All Bills");
        btn_add_bill = new JButton("Add Bill");
        btn_view_bill = new JButton("View Bill");
        btn_tax_info_table = new JButton("Tax Info");

        btn_add_employee = new JButton("Add Employee");
        btn_update_password = new JButton("Update Password");

        btn_logout = new JButton("Logout");
        btn_logout.setBackground(Color.RED);

        // Adding components to the left panel
        pnl_left.add(lbl_heading);
        pnl_left.add(lbl_heading_customer_management);
        pnl_left.add(btn_view_all_customers_table);
        pnl_left.add(btn_add_customer);
        pnl_left.add(btn_nadra_cnic_info_table);

        pnl_left.add(lbl_heading_bill_management);
        pnl_left.add(btn_view_all_bills_table);
        pnl_left.add(btn_add_bill);
        pnl_left.add(btn_view_bill);
        pnl_left.add(btn_tax_info_table);

        pnl_left.add(lbl_heading_employee_management);
        pnl_left.add(btn_add_employee);
        pnl_left.add(btn_update_password);
        // add 3 dummy elements
        pnl_left.add(new JLabel());
        pnl_left.add(new JLabel());
        pnl_left.add(new JLabel());
        pnl_left.add(new JLabel());
        pnl_left.add(btn_logout);
    }

    public static void main(String[] args) {
        new AdminDashboard(new Employee("shazia", "420"));
    }

}
