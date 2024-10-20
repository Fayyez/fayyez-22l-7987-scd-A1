package Frontend_GUI.Employee_Panels;

import javax.swing.*;

public class ViewCustomersPanel extends JPanel {
    private JLabel temp = new JLabel("View Customers Panel");

    public ViewCustomersPanel() {
        init();
        setVisible(true);
    }
    public void init() {
        add(temp);
    }
}
