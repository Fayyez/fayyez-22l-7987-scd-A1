package Frontend_GUI;

import models.Employee;
import javax.swing.*;
import java.awt.*;
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
    private CardLayout card_layout_right;

    // left panel buttons
    private JLabel lbl_heading;
    private JButton btn_view_customers;

    // right panel stuff here

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
        pane_tabs = new JTabbedPane();
        pnl_left = new JPanel();
        // set right panel
        // set all actioin buttons just like customer dashboard

        setContentPane(pane_main_split);

    }

    private void setUPleftPanel() {


    }

    public static void main(String[] args) {
        new AdminDashboard(new Employee("shazia", "420"));
    }

}
