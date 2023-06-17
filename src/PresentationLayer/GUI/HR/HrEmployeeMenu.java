package PresentationLayer.GUI.HR;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HrEmployeeMenu extends JFrame{
    private JPanel main;
    private JPanel buttonPanel;

    public HrEmployeeMenu() {
        hremployeeOptions();
    }


    public void hremployeeOptions() {
        // Set up the JFrame
        setTitle("HR & Employee Form");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximize the frame
        setSize(1000,1000);
        // Create the main panel with a layered pane
        main = new JPanel();
        main.setLayout(new BorderLayout());

        // Create the layered pane
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(getWidth(), getHeight()));

        // Create the background image label
        ImageIcon backgroundImage = new ImageIcon("background.jpg"); // Replace "background.jpg" with your actual image file path
        JLabel backgroundLabel = new JLabel(backgroundImage);
        backgroundLabel.setBounds(0, 0, getWidth(), getHeight());

        // Create the "Hello HR" label
        JLabel helloLabel = new JLabel("<html><h1>Choose HR or Employee</h1></html>");
        helloLabel.setBounds(0, 100, getWidth(), getHeight());
        helloLabel.setHorizontalAlignment(JLabel.CENTER);
        helloLabel.setVerticalAlignment(JLabel.TOP);

        // Create the button panel
        buttonPanel = new JPanel(new GridBagLayout());
        addButtons();
        buttonPanel.setOpaque(false); // Make the panel transparent
        buttonPanel.setBounds(0, 0, getWidth(), getHeight());

        // Add the components to the layered pane
        layeredPane.add(backgroundLabel, new Integer(0));
        layeredPane.add(helloLabel, new Integer(1));
        layeredPane.add(buttonPanel, new Integer(2));

        // Add the layered pane to the main panel
        main.add(layeredPane, BorderLayout.CENTER);

        // Add the main panel to the frame
        add(main);

        setVisible(true);
    }

    private void addButtons() {
        String[] buttonLabels = {"HR Manager",
                "Employee",
                "Exit"
        };

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10); // Add spacing between buttons

        for (int i = 0; i < buttonLabels.length; i++) {
            JButton button = new JButton(buttonLabels[i]);
            button.addActionListener(new ButtonClickListener(i + 1));
            button.setPreferredSize(new Dimension(300, 50)); // Set preferred size for the button
            buttonPanel.add(button, gbc);
            gbc.gridy++;
        }
    }

    private class ButtonClickListener implements ActionListener {
        private int buttonNumber;

        public ButtonClickListener(int buttonNumber) {
            this.buttonNumber = buttonNumber;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            // Open a new frame or panel based on the button clicked
            switch (buttonNumber) {
                case 1:
                    new HrFrame();
                    dispose(); // Close the current frame
                    break;
                case 2:
                    new EmployeeFrame();
                    dispose();
                    break;
                case 3:
                    // Handle button 7 action
                    System.exit(0);
                    break;
            }
        }
    }
}



//    public EmployeeFrame() {
//        service = new CompanyService();
//        // Set up the JFrame
//        setTitle("Employee Login");
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setSize(1000, 1000);
//
//        // Create the card layout and panel
//        main = new JPanel(new BorderLayout());
//        buttonPanel = new JPanel(new FlowLayout());
//
//
//        // Create the login panel
//        JPanel loginPanel = new JPanel(new GridLayout(8,5));
//// Create a constraints object to control component positioning
//        GridBagConstraints constraints = new GridBagConstraints();
//        constraints.insets = new Insets(30, 30, 30, 30); // Add some padding around components
//        constraints.anchor = GridBagConstraints.WEST; // Align components to the left
//
//// Add ID label and field
//        JLabel idLabel = new JLabel("ID:");
//        idLabel.setFont(idLabel.getFont().deriveFont(20.0f));
//        JTextField idField = new JTextField(20);
//        constraints.gridx = 0; // Column 0
//        constraints.gridy = 0; // Row 0
//        loginPanel.add(idLabel, constraints);
//
//        constraints.gridy = 1; // Row 1
//        loginPanel.add(idField, constraints);
//
//// Add vertical space
//        constraints.gridy = 2; // Row 2
//        constraints.weighty = 1.0; // Allow vertical expansion
//        loginPanel.add(Box.createVerticalStrut(20), constraints);
//
//// Add password label and field
//        JLabel passwordLabel = new JLabel("Password:");
//        passwordLabel.setFont(passwordLabel.getFont().deriveFont(20.0f));
//        JPasswordField passwordField = new JPasswordField(20);
//        constraints.gridy = 3; // Row 3
//        constraints.weighty = 0.0; // Reset vertical weight
//        loginPanel.add(passwordLabel, constraints);
//
//        constraints.gridy = 4; // Row 4
//        loginPanel.add(passwordField, constraints);
//
//        passwordField.setPreferredSize(new Dimension(200, passwordField.getPreferredSize().height+ 30)); // Adjust the width and height
//        constraints.gridx = 3; // Column 1
//        loginPanel.add(passwordField, constraints);
//
//        JButton loginButton = new JButton("Login");
//        Dimension buttonSize = new Dimension(200, 40);
//        loginButton.setPreferredSize(buttonSize);
//
//        // Set preferred size for ID and Password buttons
//        idField.setPreferredSize(new Dimension(150, 20));
//        passwordField.setPreferredSize(new Dimension(150, 20));
//
//
//        // Create the main menu card
//        JPanel menuPanel = new JPanel(new GridLayout());
//        menuPanel.setOpaque(false); // Make the panel transparent
//        menuPanel.setBounds(0, 0, getWidth(), getHeight());
//
//
//        // Add the login panel to the main panel
//        // Create a panel for centering the login panel
//        JPanel centerPanel = new JPanel(new GridBagLayout());
//        centerPanel.add(loginPanel);
//        main.add(centerPanel, BorderLayout.CENTER);
//        main.add(loginButton, BorderLayout.SOUTH);
//
//        // Add the main panel to the frame
//        add(main);
//
//        // ActionListener for the login button
//        loginButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                employeeID = idField.getText();
//                String password = new String(passwordField.getPassword());
//
//                // Validate the login credentials
//                if (service.CheckEmployeePassword(employeeID, password)) {
//                    // Remove the login panel
////                    main.remove(loginPanel);
//                    //JButton logoutButton = new JButton("Exit");
//                    main.removeAll(); // Remove all components from optionsPanel
//                    main.add(buttonPanel, BorderLayout.NORTH); // Add buttonPanel back to optionsPanel
//                    main.add(menuPanel, BorderLayout.CENTER);
//                    main.revalidate(); // Revalidate the panel to update the layout
//                    main.repaint(); // Repaint the panel
//
//                    // Refresh the frame
//                    revalidate();
//                    repaint();
//
//                } else {
//                    JOptionPane.showMessageDialog(EmployeeFrame.this, "Invalid login credentials. Please try again.");
//                }
//            }
//        });
//        employeeMenu(menuPanel, loginPanel, idField, passwordField);
//
//        this.setVisible(true);
//    }
