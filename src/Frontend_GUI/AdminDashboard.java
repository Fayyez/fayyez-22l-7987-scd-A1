package Frontend_GUI;

import models.Employee;

import javax.swing.*;
import java.awt.event.ActionListener;

public class AdminDashboard extends JFrame {
    private JPanel pnl_main;
    private Employee user;

    public AdminDashboard(Employee user, JPanel pnlMain) {
        this.user = user;
        this.pnl_main = pnlMain;
        this.init();
        pnl_main.setVisible(true);
    }

    private void init() {
        pnl_main.removeAll();
        pnl_main.add(new JLabel("testing"));
    }

}
