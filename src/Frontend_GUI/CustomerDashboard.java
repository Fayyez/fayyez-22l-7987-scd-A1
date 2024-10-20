package Frontend_GUI;

import Frontend_GUI.Customer_Panels.UpdateCNICPanel;
import Frontend_GUI.Customer_Panels.ViewBillPanel;
import models.Customer;
import utils.BillManager;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

/* customer functionality
    view current bill (latest bills)
    update cnic expiry
    log out
 */
public class CustomerDashboard extends JFrame {
    // attributes
    public static int WIDTH = 1000;
    public static int HEIGHT = 700;
    private Customer user;

    // panels
    private CardLayout card_layout_main;
    private JSplitPane pane_main;
    private JTabbedPane pane_tabs;
    private JPanel pnl_left;
    private JPanel pnl_right;

    // right panel views
    private JPanel pnl_view_bill;
    private JPanel pnl_update_cnic;

    // left panle which is the nav bar for our application
    private JButton btn_view_current_bill;
    private JButton btn_update_cnic_expiry;
    private JButton btn_logout;

    // constructor
    public CustomerDashboard(Customer user) {
        this.user = user;
        init();
        setVisible(true);
    }
    // other methods
    public void init() {
        // setting global standard settings for the frame
        setTitle("Customer Dashboard");
        setBounds(450,0,WIDTH,HEIGHT);
        setIconImage(new ImageIcon("src/Assets/logo.png").getImage());
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // setting up the main pane
        this.pane_main = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        this.pane_main .setDividerLocation(200);
        this.setUpLeftTabbedPanel();
        this.setUpRightTabbedPanel();
        this.setButtonActions();
        add(pane_main);
    }

    private void setButtonActions() {
        // setting up the button actions
        btn_view_current_bill.addActionListener(e -> {
            pnl_view_bill = new ViewBillPanel(user);
            card_layout_main.show(pnl_right, "view_bill");
        });
        btn_update_cnic_expiry.addActionListener(e -> {
            pnl_update_cnic = new UpdateCNICPanel(user);
            card_layout_main.show(pnl_right, "update_cnic");
        });
        btn_logout.addActionListener(e -> {
            System.out.println("customer logged out");
            this.dispose();
            new LoginPage();
        });
    }

    private void setUpLeftTabbedPanel() {

        this.pane_tabs = new JTabbedPane();
        // create the panel with alll action buttons
        this.pnl_left = new JPanel();
        pnl_left.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
        pnl_left.setLayout(new GridLayout(18,1,4,1));
        pnl_left.setBackground(Color.BLACK);
        // setting up all the buttons and adding to the panel
        btn_view_current_bill = new JButton("View Bill");
        btn_view_current_bill.setHorizontalAlignment(SwingConstants.CENTER);
        btn_update_cnic_expiry = new JButton("Update CNIC");
        btn_update_cnic_expiry.setHorizontalAlignment(SwingConstants.CENTER);
        btn_logout = new JButton("Logout");
        btn_logout.setHorizontalAlignment(SwingConstants.CENTER);
        btn_logout.setBackground(Color.RED);
        // TODO: add button actions on the nav bar
        // adding all the buttons to the panel
        JLabel lbl_welcome = new JLabel("Welcome, " + user.getName());
        lbl_welcome.setForeground(Color.white);
        lbl_welcome.setBackground(Color.gray);
        lbl_welcome.setOpaque(true);
        lbl_welcome.setFont(new Font("Arial", Font.BOLD, 18));
        Image user_icon = new ImageIcon("src/Assets/user_profile.png").getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        lbl_welcome.setIcon(new ImageIcon(user_icon));
        lbl_welcome.setHorizontalTextPosition(SwingConstants.RIGHT);
        lbl_welcome.setIconTextGap(5);

        pnl_left.add(lbl_welcome);
        pnl_left.add(btn_view_current_bill);
        pnl_left.add(btn_update_cnic_expiry);
        pnl_left.add(btn_logout);

        pane_tabs.addTab("Actions", pnl_left);
        pane_main.setLeftComponent(pane_tabs);
    }

    private void setUpRightTabbedPanel() {
        // create a card layout for pnl_right
        pnl_right = new JPanel();
        card_layout_main = new CardLayout();
        pnl_right.setLayout(card_layout_main);
        pnl_right.setBounds(0,0,WIDTH-200,HEIGHT);
        // create all three tabs as panels
        pnl_view_bill = new ViewBillPanel(user);
        pnl_update_cnic = new UpdateCNICPanel(user);

        // add all the panels to the main right panel with CardLayout
        pnl_right.add(pnl_view_bill, "view_bill");
        pnl_right.add(pnl_update_cnic, "update_cnic");

        card_layout_main.show(pnl_right, "view_bill");
        // add the main right panel
        pane_main.setRightComponent(pnl_right);
    }

    // for testing
    public static void main(String[] args) {
        new CustomerDashboard(new Customer(1000, 3310035693443L, "Fayyez Farrukh", "Lahore", "03001234567", false, new Date(), 10));
    }
}
