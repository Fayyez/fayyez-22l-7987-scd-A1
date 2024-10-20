package Frontend_GUI;

import models.Customer;
import models.Employee;
import utils.CustomerManager;
import utils.EmployeeManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPage extends JFrame implements ActionListener {
    // main panels
    private JPanel pnl_main;
    private JPanel pnl_left;
    private JPanel pnl_right;// a gridbaglayout
    // right panel components
    private JCheckBox cb_admin_login;
    private JLabel lbl_logo;
    private JLabel lbl_title;
    private JLabel lbl_username;
    private JLabel lbl_password;
    private JTextField txt_username;
    private JPasswordField txt_password;
    private JButton btn_login, btn_cancel;

    public LoginPage() {
        init();
        setVisible(true);
    }

    public void init() {
        // setting tile and defaults
        setTitle("Login Page");
        // set icon of application
        setIconImage(new ImageIcon("src/Assets/logo.png").getImage());
        setBounds(100,50,900,600);
        setPreferredSize(new Dimension(900, 600));
        setMinimumSize(new Dimension(900, 600));
        this.setMaximumSize(new Dimension(900, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        pnl_main = new JPanel(new FlowLayout(FlowLayout.LEFT));
        setContentPane(pnl_main);
        this.setLeftPanel();
        this.setRightPanel();
    }

    private void setRightPanel() {
        // create a panel with grid layout for the form
        this.pnl_right = new JPanel();
        this.pnl_right.setBounds(450, 0, 450, 600);
        LayoutManager layout_right = new GridLayout(4,2,5,10);
        pnl_right.setAlignmentX(Panel.CENTER_ALIGNMENT);
        pnl_right.setAlignmentY(Panel.CENTER_ALIGNMENT);
        pnl_right.setLayout(layout_right);
        // setting elements in the right panel
        Image icon = new ImageIcon("src/Assets/logo.png").getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        JLabel lbl_usericon = new JLabel();
        lbl_usericon.setText("Login Page");
        lbl_usericon.setFont(new Font("Arial", Font.BOLD, 18));
        lbl_usericon.setIcon(new ImageIcon(icon));
        lbl_usericon.setHorizontalTextPosition(SwingConstants.LEFT);
        lbl_usericon.setIconTextGap(5);
        lbl_usericon.setHorizontalAlignment(SwingConstants.CENTER);
        JLabel lbl_heading = new JLabel("Welcome to e-LESCO");
        lbl_heading.setHorizontalAlignment(SwingConstants.LEFT);
        lbl_heading.setBounds(0,0,225, 20);
        lbl_heading.setFont(new Font("Arial", Font.BOLD, 18));
        lbl_username = new JLabel("Enter Username: ");
        lbl_password = new JLabel("Enter ID: ");
        lbl_username.setHorizontalAlignment(SwingConstants.CENTER);
        lbl_password.setHorizontalAlignment(SwingConstants.CENTER);
        txt_password = new JPasswordField(20);
        txt_username = new JTextField("Username here...");
        txt_username.setMinimumSize(new Dimension(200, 30));
        btn_login = new JButton("Login");
        btn_login.setBackground(Color.green);
        btn_cancel = new JButton("Close");
        btn_cancel.setMaximumSize(new Dimension(50, 30));
        this.cb_admin_login = new JCheckBox("Admin Login");
        this.cb_admin_login.setHorizontalAlignment(SwingConstants.RIGHT);
        this.setBtnActions();// set actions on the button
        // adding the elements on right panel
        pnl_right.add(lbl_usericon);
        //pnl_right.add(lbl_heading);
        pnl_right.add(cb_admin_login);
        pnl_right.add(lbl_username);
        pnl_right.add(txt_username);
        pnl_right.add(lbl_password);
        pnl_right.add(txt_password);
        pnl_right.add(btn_cancel);
        pnl_right.add(btn_login);

        pnl_right.setVisible(true);
        pnl_main.add(pnl_right);
    }

    private void setBtnActions() {
        // handle login button clicked
        btn_login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Login button clicked");
                String username = txt_username.getText();
                String password = txt_password.getText();
                txt_username.setText("");
                txt_password.setText("");
                if(username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Username or password cannot be empty");
                    return;
                }
                // else manage login
                if(cb_admin_login.isSelected()) {
                    // manage employee login
                    try {
                        System.out.println("username = " + username);
                        System.out.println("password = " + password);
                        Employee user = EmployeeManager.getEmployee(username,password);
                        System.out.println("Employee login successful");
                        // open the dashboard
                        pnl_main.removeAll();
                        dispose();
                        new AdminDashboard(user);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Invalid username or password for admin");
                        return;
                    }
                }
                else {
                    // manage customer login
                    try {
                        int id = Integer.parseInt(password);
                        Customer user = CustomerManager.getCustomer(id, username);
                        // open the cutomer dashboard
                        dispose();
                        System.out.println("Customer login successful");
                        new CustomerDashboard(user);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "ID must be an integer value");
                        return;
                    } catch (IllegalArgumentException ex) {
                        JOptionPane.showMessageDialog(null, "Wrong username or password. Try again");
                        return;
                    }
                }
            }
        });
        // add hover effect on the login button

        // handle cancel button clicked
        btn_cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // display googbye button and close the window
                System.out.println("Cancel button clicked");
                JOptionPane.showMessageDialog(null, "Goodbye! Have a nice day");
                dispose();
            }
        });

        // handle toggle button switched
        cb_admin_login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(cb_admin_login.isSelected()) {// for employee
                    lbl_username.setText("Enter Username: ");
                    lbl_password.setText("Enter Password: ");
                    txt_username.setText("Username here...");
                }
                else {// for customer
                    lbl_username.setText("Enter Username: ");
                    lbl_password.setText("Enter ID: ");
                }
            }
        });
    }

    public void setLeftPanel() {
        this.pnl_left = new JPanel(new BorderLayout());
        pnl_left.setBorder(BorderFactory.createEmptyBorder(175, 50, 175, 50));
        pnl_left.setPreferredSize(new Dimension(450, 600));
        pnl_left.setBackground(Color.BLUE);
        // create logo and image and add to the centre make north east west south borders broad
        ImageIcon icon_logo = new ImageIcon("src/Assets/logo.png");
        this.lbl_logo = new JLabel(icon_logo);
        lbl_title = new JLabel("<html><b><i>E-LESCO BILLING SYSTEM</i></b></html>");
        lbl_title.setHorizontalAlignment(SwingConstants.CENTER);
        lbl_title.setFont(new Font("Arial", Font.BOLD, 20));
        // adding to panel
        pnl_left.add(lbl_logo, BorderLayout.CENTER);
        pnl_left.add(lbl_title, BorderLayout.SOUTH);

        pnl_main.add(pnl_left);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Action performed");
    }

    public static void main(String[] args) {
        new LoginPage();// for testing
    }
}
