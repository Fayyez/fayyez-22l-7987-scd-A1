import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class SplitPaneExample extends JFrame {

    private JPanel rightPanel; // Dynamic panel to be updated

    public SplitPaneExample() {
        // Create the left panel with tabs
        JTabbedPane tabbedPane = new JTabbedPane();

        // Add tabs to the left panel (tabs can be buttons, menus, or any components)
        JButton viewLogButton = new JButton("View Log");
        JButton viewProfileButton = new JButton("View Profile");
        JButton viewBillsButton = new JButton("View Bills");

        // Adding buttons to a JPanel (as tabs)
        JPanel tabPanel = new JPanel();
        tabPanel.setLayout(new GridLayout(3, 1)); // Layout with 3 rows
        tabPanel.add(viewLogButton);
        tabPanel.add(viewProfileButton);
        tabPanel.add(viewBillsButton);

        // Add tabPanel to JTabbedPane
        tabbedPane.addTab("Operations", tabPanel);

        // Create the right panel, which will change based on the tab selection
        rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(new JLabel("Select an option from the left tabs"), BorderLayout.CENTER);

        // Split pane to divide left and right
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tabbedPane, rightPanel);
        splitPane.setDividerLocation(200); // Set divider position

        // Add ActionListener to buttons to update the right panel content
        viewLogButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateRightPanel("Log");
            }
        });

        viewProfileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateRightPanel("Profile");
            }
        });

        viewBillsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateRightPanel("Bills");
            }
        });

        // Set up the frame
        setLayout(new BorderLayout());
        add(splitPane, BorderLayout.CENTER);

        setTitle("JSplitPane Example");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame
        setVisible(true);
    }

    // Method to update the right panel content dynamically
    private void updateRightPanel(String tabName) {
        rightPanel.removeAll(); // Remove current content

        // Based on the tab selected, update the right panel with new content
        switch (tabName) {
            case "Log":
                rightPanel.add(new JLabel("Viewing Log Data"), BorderLayout.CENTER);
                break;
            case "Profile":
                rightPanel.add(new JLabel("Viewing Profile Data"), BorderLayout.CENTER);
                break;
            case "Bills":
                rightPanel.add(new JLabel("Viewing Bills Data"), BorderLayout.CENTER);
                break;
            default:
                rightPanel.add(new JLabel("Select an option from the left tabs"), BorderLayout.CENTER);
        }

        // Refresh the right panel to show the new content
        rightPanel.revalidate();
        rightPanel.repaint();
    }

    public static void main(String[] args) {
        new SplitPaneExample();
    }
}

