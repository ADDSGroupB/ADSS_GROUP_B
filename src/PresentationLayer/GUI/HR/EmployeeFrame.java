package PresentationLayer.GUI.HR;
import ServiceLayer.HR.CompanyService;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class EmployeeFrame extends JFrame {
    private JPanel main;
    private JPanel buttonPanel;
    private CompanyService service;
    private String employeeID;

    public EmployeeFrame() {
        service = new CompanyService();
        // Set up the JFrame
        setTitle("Employee Login");
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
        JLabel helloLabel = new JLabel("<html><h1>Hello Employee</h1></html>");
        helloLabel.setBounds(0, 100, getWidth(), getHeight());
        helloLabel.setHorizontalAlignment(JLabel.CENTER);
        helloLabel.setVerticalAlignment(JLabel.TOP);

        // Create the button panel
        buttonPanel = new JPanel(new GridBagLayout());
        // Create the login panel
        JPanel loginPanel = new JPanel(new GridBagLayout());


        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10); // Add spacing between buttons


// Add ID label and field
        JLabel idLabel = new JLabel("ID:");
        idLabel.setFont(idLabel.getFont().deriveFont(20.0f));
        JTextField idField = new JTextField(20);
        idField.setColumns(15);

        loginPanel.add(idLabel, gbc);
        gbc.gridx++;
        loginPanel.add(idField, gbc);
        gbc.gridy++;
        gbc.gridx--;


// Add password label and field
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(passwordLabel.getFont().deriveFont(20.0f));
        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setColumns(15);
        loginPanel.add(passwordLabel, gbc);
        gbc.gridx++;
        loginPanel.add(passwordField, gbc);
        gbc.gridy++;


        JButton loginButton = new JButton("Login");
        Dimension buttonSize = new Dimension(200, 40);
        loginButton.setPreferredSize(buttonSize);
//        loginPanel.add(loginButton);
        loginPanel.add(loginButton, gbc);
//        loginPanel.add(passwordField, gbc);
        gbc.gridy++;
        gbc.gridx--;

        // Set preferred size for ID and Password buttons
        idField.setPreferredSize(new Dimension(150, 20));
        passwordField.setPreferredSize(new Dimension(150, 20));
        loginPanel.setOpaque(false); // Make the panel transparent
        loginPanel.setBounds(0, 0, getWidth(), getHeight());
        // Add the components to the layered pane
        layeredPane.add(backgroundLabel, new Integer(0));
        layeredPane.add(helloLabel, new Integer(1));
        layeredPane.add(loginPanel, new Integer(2));

        // Add the layered pane to the main panel
        main.add(layeredPane, BorderLayout.CENTER);

        // Add the main panel to the frame
        add(main);
        // ActionListener for the login button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                employeeID = idField.getText();
                String password = new String(passwordField.getPassword());

                // Validate the login credentials
                if (service.CheckEmployeePassword(employeeID, password)) {
                    // Remove the login panel
//                    main.remove(loginPanel);
                    //JButton logoutButton = new JButton("Exit");
                    main.removeAll(); // Remove all components from optionsPanel
                    main.add(buttonPanel, BorderLayout.NORTH); // Add buttonPanel back to optionsPanel
//                    main.add(menuPanel, BorderLayout.CENTER);
                    main.revalidate(); // Revalidate the panel to update the layout
                    main.repaint(); // Repaint the panel

                    JPanel menuPanel = new JPanel(new GridLayout());
                    menuPanel.setOpaque(false); // Make the panel transparent
                    menuPanel.setBounds(0, 0, getWidth(), getHeight());
                    employeeMenu(menuPanel, loginPanel, idField, passwordField);
                    main.add(menuPanel, BorderLayout.CENTER);

                    // Refresh the frame
                    revalidate();
                    repaint();

                } else {
                    JOptionPane.showMessageDialog(EmployeeFrame.this, "Invalid login credentials. Please try again.");
                }
            }
        });
        this.setVisible(true);
        setVisible(true);
    }

    private void employeeMenu(JPanel menuPanel, JPanel loginPanel, JTextField idField, JTextField passwordField){
        menuPanel.setLayout(new GridBagLayout()); // Set the layout manager to GridBagLayout

        // Create a constraints object to control component positioning
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10); // Add some padding around components
        constraints.anchor = GridBagConstraints.CENTER; // Center components horizontally and vertically

        JButton submitShiftsButton = new JButton("Submit Shifts for next week");
        JButton checkTransportsButton = new JButton("Check Expected Transports");
        JButton logoutButton = new JButton("Logout");

        // Set preferred size for the buttons
        Dimension buttonSize = new Dimension(300, 100);
        submitShiftsButton.setPreferredSize(buttonSize);
        checkTransportsButton.setPreferredSize(buttonSize);
        logoutButton.setPreferredSize(buttonSize);

        // Add components to the menu panel
        constraints.gridx = 0; // Column 0
        constraints.gridy = 0; // Row 0
        menuPanel.add(submitShiftsButton, constraints);

        constraints.gridy = 1; // Row 1
        menuPanel.add(checkTransportsButton, constraints);

        constraints.gridy = 2; // Row 2
        menuPanel.add(logoutButton, constraints);

        submitShiftsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitShiftsOptions(menuPanel);
            }
        });

        checkTransportsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkTransportsOptions(menuPanel);
            }
        });

        // ActionListener for the logout button
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    private void submitShiftsOptions(JPanel menuPanel){

//        // Create a constraints object to control component positioning
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 5, 5, 5); // Add some padding around components
        constraints.anchor = GridBagConstraints.CENTER; // Center components horizontally and vertically*/

        // Create the submit shifts panel
        JPanel submitShiftsPanel = new JPanel(new GridBagLayout());
        JLabel dateLabel = new JLabel("Enter the date of the shift you want (yyyy-mm-dd):");
        dateLabel.setFont(dateLabel.getFont().deriveFont(20.0f));

        // Create the date format
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        // Create the masked text field for date input
        MaskFormatter dateFormatter;
        try {
            dateFormatter = new MaskFormatter("####-##-##");
        } catch (ParseException e) {
            e.printStackTrace();
            return;
        }
        JFormattedTextField dateField = new JFormattedTextField(dateFormatter);
        constraints.gridx = 0; // Column 0
        constraints.gridy = 0; // Row 0
        submitShiftsPanel.add(dateLabel, constraints);
        dateField.setPreferredSize(new Dimension(200, dateField.getPreferredSize().height+30)); // Adjust the width and height
        constraints.gridx = 1; // Column 1
//        dateField.setColumns(10);
        submitShiftsPanel.add(dateField,constraints);

        JLabel shiftLabel = new JLabel("Morning or Evening?");
        shiftLabel.setFont(shiftLabel.getFont().deriveFont(20.0f));

        String[] shiftOptions = {"Morning", "Evening"};
        JComboBox<String> shiftComboBox = new JComboBox<>(shiftOptions);
        shiftComboBox.setFont(shiftComboBox.getFont().deriveFont(16.0f)); // Adjust the font size (e.g., 16.0f)
        shiftComboBox.setPreferredSize(new Dimension(200, shiftComboBox.getPreferredSize().height + 30)); // Adjust the preferred width
        constraints.gridx = 0; // Column 1
        constraints.gridy = 1; // Row 0
        submitShiftsPanel.add(shiftLabel, constraints);
        constraints.gridx = 1; // Row 1
        submitShiftsPanel.add(shiftComboBox, constraints);



        JButton submitButton = new JButton("Submit");

        JButton backButton = new JButton("Return");

        // Set up the layout for the submit shifts panel
        constraints.gridx = 0;
        constraints.gridy = 0;
        submitShiftsPanel.add(dateLabel, constraints);

        constraints.gridy = 1;
        submitShiftsPanel.add(shiftLabel, constraints);

        constraints.gridx = 1;
        constraints.gridy = 0;
        submitShiftsPanel.add(dateField, constraints);

        constraints.gridy = 1;
        submitShiftsPanel.add(shiftComboBox, constraints);

        constraints.gridy = 2;
        submitShiftsPanel.add(submitButton, constraints);

        constraints.gridy = 3;
        submitShiftsPanel.add(backButton, constraints);

        // Add components to the submit shifts panel
        JPanel buttonPanel = new JPanel(); // Create a new panel for the buttons
//        buttonPanel.add(submitButton);
        constraints.gridy = 2;
        constraints.anchor = GridBagConstraints.LINE_START;
        submitShiftsPanel.add(submitButton, constraints);

        constraints.gridy = 3;
        buttonPanel.add(backButton, constraints);

        // ActionListener for the submit button
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String date = dateField.getText();
                String shift = (String) shiftComboBox.getSelectedItem();
                try {
                    String shiftType;
                    if(shift.equals("Morning")){
                        shiftType = "m";
                    }
                    else{
                        shiftType = "e";
                    }
                    service.submitAShiftForEmployee(employeeID, date, shiftType);
                    JOptionPane.showMessageDialog(EmployeeFrame.this, "Submitted");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(EmployeeFrame.this, "You can only submit shifts for the next week");
                }
            }
        });

        // ActionListener for the back button
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                main.removeAll(); // Remove all components from optionsPanel
                main.add(menuPanel, BorderLayout.CENTER);
                main.revalidate(); // Revalidate the panel to update the layout
                main.repaint(); // Repaint the panel
            }
        });

        main.removeAll(); // Remove all components from optionsPanel
        main.add(submitShiftsPanel, BorderLayout.CENTER); // Add buttonPanel back to optionsPanel
        main.add(buttonPanel, BorderLayout.SOUTH);
        main.revalidate(); // Revalidate the panel to update the layout
        main.repaint(); // Repaint the panel

    }

    private void checkTransportsOptions(JPanel menuPanel){

        //        // Create a constraints object to control component positioning
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 5, 5, 5); // Add some padding around components
        constraints.anchor = GridBagConstraints.CENTER; // Center components horizontally and vertically*/

        // Create the submit shifts panel
        JPanel submitShiftsPanel = new JPanel(new GridBagLayout());
        JLabel dateLabel = new JLabel("Enter the date of the shift you want (yyyy-mm-dd):");
        dateLabel.setFont(dateLabel.getFont().deriveFont(20.0f));
        // Create the date format
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        // Create the masked text field for date input
        MaskFormatter dateFormatter;
        try {
            dateFormatter = new MaskFormatter("####-##-##");
        } catch (ParseException e) {
            e.printStackTrace();
            return;
        }
        JFormattedTextField dateField = new JFormattedTextField(dateFormatter);
        constraints.gridx = 0; // Column 0
        constraints.gridy = 0; // Row 0
        submitShiftsPanel.add(dateLabel, constraints);
//        dateField.setColumns(10);
        dateField.setPreferredSize(new Dimension(200, dateField.getPreferredSize().height+30)); // Adjust the width and height
        constraints.gridx = 1; // Column 1
//        dateField.setColumns(10);

        submitShiftsPanel.add(dateField,constraints);
        JLabel shiftLabel = new JLabel("Morning or Evening?");
        shiftLabel.setFont(shiftLabel.getFont().deriveFont(20.0f));
        String[] shiftOptions = {"Morning", "Evening"};
        JComboBox<String> shiftComboBox = new JComboBox<>(shiftOptions);
        shiftComboBox.setFont(shiftComboBox.getFont().deriveFont(16.0f)); // Adjust the font size (e.g., 16.0f)
        shiftComboBox.setPreferredSize(new Dimension(200, shiftComboBox.getPreferredSize().height + 30)); // Adjust the preferred width
        constraints.gridx = 0; // Column 1
        constraints.gridy = 1; // Row 0
        submitShiftsPanel.add(shiftLabel, constraints);
        constraints.gridx = 1; // Row 1
        submitShiftsPanel.add(shiftComboBox, constraints);
        JButton submitButton = new JButton("Check");


        JButton backButton = new JButton("Return");

        JLabel lblbranch = new JLabel("Branch number");
        lblbranch.setFont(lblbranch.getFont().deriveFont(20.0f));

        JComboBox<String> cmbbranch = new JComboBox<>(new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"});
        cmbbranch.setFont(cmbbranch.getFont().deriveFont(16.0f)); // Adjust the font size (e.g., 16.0f)
        cmbbranch.setPreferredSize(new Dimension(200, cmbbranch.getPreferredSize().height + 30)); // Adjust the preferred width
        constraints.gridx = 0; // Column 1
        constraints.gridy = 1; // Row 0
        submitShiftsPanel.add(lblbranch, constraints);
        constraints.gridx = 1; // Row 1
        submitShiftsPanel.add(cmbbranch, constraints);

        // Set up the layout for the submit shifts panel
        constraints.gridx = 0;
        constraints.gridy = 0;
        submitShiftsPanel.add(dateLabel, constraints);

        constraints.gridy = 1;
        submitShiftsPanel.add(shiftLabel, constraints);

        constraints.gridy = 2;
        submitShiftsPanel.add(lblbranch, constraints);

        constraints.gridx = 1;
        constraints.gridy = 0;
        submitShiftsPanel.add(dateField, constraints);

        constraints.gridy = 1;
        submitShiftsPanel.add(shiftComboBox, constraints);

        constraints.gridy = 2;
        submitShiftsPanel.add(cmbbranch, constraints);

        constraints.gridy = 3;
//        constraints.gridx = 1;
        submitShiftsPanel.add(submitButton, constraints);

        constraints.gridy = 3;
        submitShiftsPanel.add(backButton, constraints);

        constraints.gridy = 3;
        buttonPanel.add(backButton, constraints);


        // ActionListener for the submit button
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                String date = dateField.getText();
                String shift = (String) shiftComboBox.getSelectedItem();
                int branchInt = Integer.parseInt(cmbbranch.getSelectedItem().toString());

                JPanel submitPanel = new JPanel(new GridLayout(0, 2));
                String transports = service.getTransportsForShift(date, shift, branchInt, employeeID);
                JLabel transportsLabel = new JLabel();
                submitShiftsPanel.add(transportsLabel);
                transportsLabel.setText("Transports: " + transports);
                submitPanel.add(transportsLabel);

                // Show the submit panel in a dialog
                JOptionPane.showOptionDialog(submitShiftsPanel, submitPanel, "Enter Shift Details",JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);

                }
                catch(Exception exc){
                    JOptionPane.showMessageDialog(EmployeeFrame.this, exc.getMessage());
                }

            }
        });

        // ActionListener for the back button
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Remove the submit shifts panel
//                main.remove(submitShiftsPanel);
                main.removeAll();
                // Add the main menu panel
                main.add(menuPanel, BorderLayout.CENTER);

                // Refresh the frame
                revalidate();
                repaint();
            }
        });

        // Remove the main menu panel
        main.remove(menuPanel);

        // Add the submit shifts panel
        main.add(submitShiftsPanel, BorderLayout.CENTER);
        main.add(buttonPanel, BorderLayout.SOUTH);

        // Refresh the frame
        revalidate();
        repaint();
    }

}
