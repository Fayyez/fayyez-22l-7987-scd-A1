package Frontend_GUI;

import models.Employee;

import javax.swing.*;
import java.awt.event.ActionListener;

public class AdminDashboard extends JFrame {
    // Static global
    public static int WIDTH = 1200;
    public static int HEIGHT = 800;
    // Attributes
    private Employee user;
    // gui elements //
    private JSplitPane pnl_main; // (left half has all available operations list, right half has all the details)

    public AdminDashboard(Employee user) {
        this.user = user;
        this.init();
        setVisible(true);
    }

    private void init() {
        pnl_main.removeAll();
        pnl_main.add(new JLabel("testing"));
    }

}
