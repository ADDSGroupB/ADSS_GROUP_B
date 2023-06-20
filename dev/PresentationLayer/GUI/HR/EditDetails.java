package PresentationLayer.GUI.HR;

import ServiceLayer.HR.CompanyService;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Enumeration;
import java.util.List;


public class EditDetails extends JFrame {
    private JPanel cards;
    private CardLayout cardLayout;
    private CompanyService service;
    private String employeeID;

    public EditDetails(HrFrame HRFrame) {
        employeeID = "";
        service = new CompanyService();
        // Set up the JFrame
        setTitle("Edit employee details");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 1000);

        // Create the card layout and panel
        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);
        getContentPane().add(cards, BorderLayout.CENTER);

        JPanel editDetailsPanel = editDetails(HRFrame);


        cards.add(editDetailsPanel, "Edit employee details");

        cardLayout.show(cards, "Main Menu");

        setVisible(true);
    }

    private JPanel editDetails(HrFrame HRFrame) {
        JPanel editDetailsPanel = new JPanel(new BorderLayout());
        JPanel buttonPanel1 = new JPanel(new FlowLayout());
        // Create label and textbox for Employee ID
        JLabel idLabel = new JLabel("Employee ID");
        JTextField idField = new JTextField(10);
        JPanel idPanel = new JPanel();
        idPanel.add(idLabel);
        idPanel.add(idField);

        // Create row of buttons
        JPanel buttonPanel = new JPanel(new GridLayout(1, 7, 10, 10));

        JButton addAuthorizationButton = new JButton("Add job");
        JButton removeJobButton = new JButton("Remove job");
        JButton changeSalaryButton = new JButton("Change salary");
        JButton changeBankButton = new JButton("Change bank");
        JButton addDetailsButton = new JButton("<html>Add personal<br/>details</html>");
        JButton addBonusButton = new JButton("Add bonus");
        JButton editDriverLicenseButton = new JButton("Driver license");
        JButton editDriverWeightButton = new JButton("<html>Driver max<br/>weight allowed</html>");

        // Add buttons to the panel
        buttonPanel.add(addAuthorizationButton);
        buttonPanel.add(removeJobButton);
        buttonPanel.add(changeSalaryButton);
        buttonPanel.add(changeBankButton);
        buttonPanel.add(addDetailsButton);
        buttonPanel.add(addBonusButton);
        buttonPanel.add(editDriverLicenseButton);
        buttonPanel.add(editDriverWeightButton);

        // Disable all buttons initially
        addAuthorizationButton.setEnabled(false);
        removeJobButton.setEnabled(false);
        changeSalaryButton.setEnabled(false);
        changeBankButton.setEnabled(false);
        addDetailsButton.setEnabled(false);
        addBonusButton.setEnabled(false);
        editDriverLicenseButton.setEnabled(false);
        editDriverWeightButton.setEnabled(false);

        // Create a panel to hold the button panel and additional options
        JPanel optionsPanel = new JPanel(new BorderLayout());
        optionsPanel.add(buttonPanel, BorderLayout.NORTH);

        // Add the panels to the main panel
        editDetailsPanel.add(idPanel, BorderLayout.NORTH);
        editDetailsPanel.add(optionsPanel, BorderLayout.CENTER);

        // Action listener for the Employee ID textbox
        idField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                employeeID = idField.getText().trim();
                // Check if employee or driver
                boolean isEmployee = service.isEmployee(employeeID);
                boolean isDriver = service.isDriver(employeeID);

                // Enable/disable buttons based on status
                addAuthorizationButton.setEnabled(isEmployee);
                removeJobButton.setEnabled(isEmployee);
                changeSalaryButton.setEnabled(isEmployee || isDriver);
                changeBankButton.setEnabled(isEmployee || isDriver);
                addDetailsButton.setEnabled(isEmployee || isDriver);
                addBonusButton.setEnabled(isEmployee || isDriver);
                editDriverLicenseButton.setEnabled(isDriver);
                editDriverWeightButton.setEnabled(isDriver);

                // Check if options panel already has a component at index 1
                if (optionsPanel.getComponentCount() > 1) {
                    optionsPanel.removeAll(); // Remove all components from optionsPanel
                    optionsPanel.add(buttonPanel, BorderLayout.NORTH); // Add buttonPanel back to optionsPanel
                    optionsPanel.revalidate(); // Revalidate the panel to update the layout
                    optionsPanel.repaint(); // Repaint the panel
                }
            }
        });

        changeSalaryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeSalaryOptions(optionsPanel, buttonPanel, employeeID);
            }
        });

        changeBankButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeBankOptions(optionsPanel, buttonPanel, employeeID);
            }
        });
        addDetailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addDetailsOptions(optionsPanel, buttonPanel, employeeID);
            }
        });

        removeJobButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeAuthorizationOptions(optionsPanel, buttonPanel, employeeID);
            }
        });

        addAuthorizationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addAuthorizationOptions(optionsPanel, buttonPanel, employeeID);
            }

        });

        addBonusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addBonusOptions(optionsPanel, buttonPanel, employeeID);
            }

        });

        editDriverLicenseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editDriverLicenseOptions(optionsPanel, buttonPanel, employeeID);
            }
        });

        editDriverWeightButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editDriverWeightOptions(optionsPanel, buttonPanel, employeeID);
            }

        });

        // Button to return to the main menu
        JButton returnButton = new JButton("Return to Main Menu");
        buttonPanel1.add(returnButton);
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                HRFrame.setVisible(true);
                dispose();
            }
        });
        editDetailsPanel.add(buttonPanel1, BorderLayout.SOUTH);

        return editDetailsPanel;
    }

    private void changeSalaryOptions(JPanel optionsPanel, JPanel buttonPanel, String employeeId){
        // Check if options panel already has a component at index 1
        if (optionsPanel.getComponentCount() > 1) {
            optionsPanel.removeAll(); // Remove all components from optionsPanel
            optionsPanel.add(buttonPanel, BorderLayout.NORTH); // Add buttonPanel back to optionsPanel
            optionsPanel.revalidate(); // Revalidate the panel to update the layout
            optionsPanel.repaint(); // Repaint the panel
        }

        // Create a panel for the radio buttons
        JPanel changeSalaryPanel = new JPanel(new GridBagLayout());



        // Create a constraints object to control component positioning
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(30, 30, 30, 30); // Add some padding around components
        constraints.anchor = GridBagConstraints.WEST; // Center components horizontally and vertically


        // Create label and text field for new salary
        JLabel salaryLabel = new JLabel("New Salary");
        salaryLabel.setFont(salaryLabel.getFont().deriveFont(24.0f));
        //salaryLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JTextField salaryField = new JTextField(20);
        constraints.gridx = 0; // Column 0
        constraints.gridy = 0; // Row 0
        changeSalaryPanel.add(salaryLabel, constraints);
        salaryField.setPreferredSize(new Dimension(200, salaryField.getPreferredSize().height+ 30)); // Adjust the width and height
        constraints.gridx = 1; // Column 1
        changeSalaryPanel.add(salaryField, constraints);


        // Create a button for adding the selected job authorization
        JButton changeSalaryButton = new JButton("Change salary");

        changeSalaryButton.setEnabled(true); // Disable the button initially

        Dimension buttonSize = new Dimension(200, 40);
        changeSalaryButton.setPreferredSize(buttonSize);

        changeSalaryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int salaryInt = checkInt(salaryField.getText().trim());
                int counter = 0;
                if (salaryInt == -1) {
                    counter++;
                    JOptionPane.showMessageDialog(changeSalaryButton, "Salary must be a positive number", "Input Error", JOptionPane.ERROR_MESSAGE);
                }
                if(counter == 0){
                    service.setSalaryPerHourForEmployee(employeeId, salaryInt);
                    salaryField.setText("");
                    JOptionPane.showMessageDialog(changeSalaryButton, "This salary has been changed", "Success", JOptionPane.PLAIN_MESSAGE);
                }
            }
        });

        // Add radio buttons panel and add button to the options panel
        optionsPanel.add(changeSalaryPanel, BorderLayout.CENTER);
        JPanel newButtonPanel = new JPanel(new FlowLayout());
        newButtonPanel.add(changeSalaryButton);
        optionsPanel.add(newButtonPanel, BorderLayout.SOUTH);

        // Repaint the options panel
        optionsPanel.revalidate();
        optionsPanel.repaint();

        // Clear the text field
        salaryField.setText("");
    }

    private void editDriverLicenseOptions(JPanel optionsPanel, JPanel buttonPanel, String employeeId){
        // Check if options panel already has a component at index 1
        if (optionsPanel.getComponentCount() > 1) {
            optionsPanel.removeAll(); // Remove all components from optionsPanel
            optionsPanel.add(buttonPanel, BorderLayout.NORTH); // Add buttonPanel back to optionsPanel
            optionsPanel.revalidate(); // Revalidate the panel to update the layout
            optionsPanel.repaint(); // Repaint the panel
        }


        // Create a constraints object to control component positioning
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(30, 30, 30, 30); // Add some padding around components
        constraints.anchor = GridBagConstraints.WEST; // Center components horizontally and vertically

        // Create a panel for the radio buttons
        JPanel editLicensePanel = new JPanel(new GridBagLayout());


        // Driver license
        JLabel lblLicense = new JLabel("License");
        lblLicense.setFont(lblLicense.getFont().deriveFont(24.0f));
        JComboBox<String> cmbLicense = new JComboBox<>(new String[]{"1", "2", "3"});
        cmbLicense.setFont(cmbLicense.getFont().deriveFont(16.0f)); // Adjust the font size (e.g., 16.0f)
        cmbLicense.setPreferredSize(new Dimension(200, cmbLicense.getPreferredSize().height)); // Adjust the preferred width
        editLicensePanel.add(lblLicense, constraints);
        editLicensePanel.add(cmbLicense, constraints);



        // Create a button for adding the selected job authorization
        JButton changeButton = new JButton("Change");
        Dimension buttonSize = new Dimension(200, 40);
        changeButton.setPreferredSize(buttonSize);
        changeButton.setEnabled(true); // Disable the button initially

        changeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int driverLicense = Integer.parseInt(cmbLicense.getSelectedItem().toString());
                service.updateDriverLicense(employeeId, driverLicense);
                JOptionPane.showMessageDialog(changeButton, "License has been changed", "Success", JOptionPane.PLAIN_MESSAGE);

            }
        });

        // Add radio buttons panel and add button to the options panel
        optionsPanel.add(editLicensePanel, BorderLayout.CENTER);
        JPanel newButtonPanel = new JPanel(new FlowLayout());
        newButtonPanel.add(changeButton);
        optionsPanel.add(newButtonPanel, BorderLayout.SOUTH);

        // Repaint the options panel
        optionsPanel.revalidate();
        optionsPanel.repaint();

    }

    private void editDriverWeightOptions(JPanel optionsPanel, JPanel buttonPanel, String employeeId){
        // Check if options panel already has a component at index 1
        if (optionsPanel.getComponentCount() > 1) {
            optionsPanel.removeAll(); // Remove all components from optionsPanel
            optionsPanel.add(buttonPanel, BorderLayout.NORTH); // Add buttonPanel back to optionsPanel
            optionsPanel.revalidate(); // Revalidate the panel to update the layout
            optionsPanel.repaint(); // Repaint the panel
        }


        // Create a constraints object to control component positioning
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(30, 30, 30, 30); // Add some padding around components
        constraints.anchor = GridBagConstraints.WEST; // Center components horizontally and vertically

        // Create a panel for the radio buttons
        JPanel editWeightPanel = new JPanel(new GridBagLayout());

        // Driver license
        JLabel lblWeight = new JLabel("Max weight");
        lblWeight.setFont(lblWeight.getFont().deriveFont(24.0f));

        JComboBox<String> cmbWeight = new JComboBox<>(new String[]{"1500", "2250", "3000"});
        cmbWeight.setFont(cmbWeight.getFont().deriveFont(16.0f)); // Adjust the font size (e.g., 16.0f)
        cmbWeight.setPreferredSize(new Dimension(200, cmbWeight.getPreferredSize().height)); // Adjust the preferred width
        editWeightPanel.add(lblWeight, constraints);
        editWeightPanel.add(cmbWeight, constraints);



        // Create a button for adding the selected job authorization
        JButton changeButton = new JButton("Change");
        changeButton.setEnabled(true); // Disable the button initially

        Dimension buttonSize = new Dimension(200, 40);
        changeButton.setPreferredSize(buttonSize);

        changeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int driverWeight = Integer.parseInt(cmbWeight.getSelectedItem().toString());
                service.updateDriverMaxWeightAllowed(employeeId, driverWeight);
                JOptionPane.showMessageDialog(changeButton, "Max weight has been changed", "Success", JOptionPane.PLAIN_MESSAGE);

            }
        });

        // Add radio buttons panel and add button to the options panel
        optionsPanel.add(editWeightPanel, BorderLayout.CENTER);
        JPanel newButtonPanel = new JPanel(new FlowLayout());
        newButtonPanel.add(changeButton);
        optionsPanel.add(newButtonPanel, BorderLayout.SOUTH);

        // Repaint the options panel
        optionsPanel.revalidate();
        optionsPanel.repaint();

    }

    private void addBonusOptions(JPanel optionsPanel, JPanel buttonPanel, String employeeId){
        // Check if options panel already has a component at index 1
        if (optionsPanel.getComponentCount() > 1) {
            optionsPanel.removeAll(); // Remove all components from optionsPanel
            optionsPanel.add(buttonPanel, BorderLayout.NORTH); // Add buttonPanel back to optionsPanel
            optionsPanel.revalidate(); // Revalidate the panel to update the layout
            optionsPanel.repaint(); // Repaint the panel
        }



        // Create a constraints object to control component positioning
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(30, 30, 30, 30); // Add some padding around components
        constraints.anchor = GridBagConstraints.WEST; // Center components horizontally and vertically


        // Create a panel for the radio buttons
        JPanel addBonusPanel = new JPanel(new GridBagLayout());

        // Create label and text field for new salary
        JLabel addBonusLabel = new JLabel("Bonus to add");
        addBonusLabel.setFont(addBonusLabel.getFont().deriveFont(24.0f));

        JTextField addBonusField = new JTextField(20);
        constraints.gridx = 0; // Column 0
        constraints.gridy = 0; // Row 0
        addBonusPanel.add(addBonusLabel, constraints);
        addBonusField.setPreferredSize(new Dimension(200, addBonusField.getPreferredSize().height +30)); // Adjust the width and height
        constraints.gridx = 1; // Column 1
        addBonusPanel.add(addBonusField, constraints);


        // Create a button for adding the selected job authorization
        JButton addBonusButton = new JButton("Add Bonus");
        Dimension buttonSize = new Dimension(200, 40);
        addBonusButton.setPreferredSize(buttonSize);
        addBonusButton.setEnabled(true); // Disable the button initially

        addBonusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int salaryInt = checkInt(addBonusField.getText().trim());
                int counter = 0;
                if (salaryInt == -1) {
                    counter++;
                    JOptionPane.showMessageDialog(addBonusButton, "Bonus must be a positive number", "Input Error", JOptionPane.ERROR_MESSAGE);
                }
                if(counter == 0){
                    addBonusField.setText("");
                    service.addBonusToEmployee(employeeId, salaryInt);
                    JOptionPane.showMessageDialog(addBonusButton, "This bonus has been added", "Success", JOptionPane.PLAIN_MESSAGE);
                }
            }
        });

        // Add radio buttons panel and add button to the options panel
        optionsPanel.add(addBonusPanel, BorderLayout.CENTER);
        JPanel newButtonPanel = new JPanel(new FlowLayout());
        newButtonPanel.add(addBonusButton);
        optionsPanel.add(newButtonPanel, BorderLayout.SOUTH);

        // Repaint the options panel
        optionsPanel.revalidate();
        optionsPanel.repaint();

        // Clear the text field
        addBonusField.setText("");
    }

    private void changeBankOptions(JPanel optionsPanel, JPanel buttonPanel, String employeeId){
        // Check if options panel already has a component at index 1
        if (optionsPanel.getComponentCount() > 1) {
            optionsPanel.removeAll(); // Remove all components from optionsPanel
            optionsPanel.add(buttonPanel, BorderLayout.NORTH); // Add buttonPanel back to optionsPanel
            optionsPanel.revalidate(); // Revalidate the panel to update the layout
            optionsPanel.repaint(); // Repaint the panel
        }

        JPanel changeSalaryPanel = new JPanel(new GridBagLayout());



        // Create a constraints object to control component positioning
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(30, 30, 30, 30); // Add some padding around components
        constraints.anchor = GridBagConstraints.WEST; // Center components horizontally and vertically


        // Create a panel for the radio buttons
        JPanel changeBankPanel = new JPanel(new GridBagLayout());

        // Create label and text field for new salary
        JLabel bankLabel = new JLabel("Bank account details");
        bankLabel.setFont(bankLabel.getFont().deriveFont(24.0f));
        //salaryLabel.setFont(new Font("Arial", Font.BOLD, 14));
        JTextField bankField = new JTextField(20);
        constraints.gridx = 0; // Column 0
        constraints.gridy = 0; // Row 0
        changeBankPanel.add(bankLabel);
        bankField.setPreferredSize(new Dimension(200, bankField.getPreferredSize().height+ 30)); // Adjust the width and height
        constraints.gridx = 1; // Column 1
        changeSalaryPanel.add(bankField, constraints);
        changeBankPanel.add(bankField);


        // Create a button for adding the selected job authorization
        JButton changeBankButton = new JButton("Change");
        changeBankButton.setEnabled(true); // Disable the button initially

        Dimension buttonSize = new Dimension(200, 40);
        changeBankButton.setPreferredSize(buttonSize);

        changeBankButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String bankAccount = bankField.getText().trim();
                int counter = 0;
                if (bankAccount.equals("")) {
                    counter++;
                    JOptionPane.showMessageDialog(changeBankButton, "Bank account details cannot be empty", "Input Error", JOptionPane.ERROR_MESSAGE);
                }
                if(counter == 0){
                    bankField.setText("");
                    service.setBankAccountForEmployee(employeeId, bankAccount);
                    JOptionPane.showMessageDialog(changeBankButton, "Bank account details have been changed", "Success", JOptionPane.PLAIN_MESSAGE);
                }
            }
        });

        // Add radio buttons panel and add button to the options panel
        optionsPanel.add(changeBankPanel, BorderLayout.CENTER);

        JPanel newButtonPanel = new JPanel(new FlowLayout());
        newButtonPanel.add(changeBankButton);

        optionsPanel.add(newButtonPanel, BorderLayout.SOUTH);

        // Repaint the options panel
        optionsPanel.revalidate();
        optionsPanel.repaint();

        // Clear the text field
        bankField.setText("");
    }

    private void addDetailsOptions(JPanel optionsPanel, JPanel buttonPanel, String employeeId){
        // Check if options panel already has a component at index 1
        if (optionsPanel.getComponentCount() > 1) {
            optionsPanel.removeAll(); // Remove all components from optionsPanel
            optionsPanel.add(buttonPanel, BorderLayout.NORTH); // Add buttonPanel back to optionsPanel
            optionsPanel.revalidate(); // Revalidate the panel to update the layout
            optionsPanel.repaint(); // Repaint the panel
        }
        // Create a constraints object to control component positioning
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(30, 30, 30, 30); // Add some padding around components
        constraints.anchor = GridBagConstraints.WEST; // Center components horizontally and vertically

        // Create a button for adding the selected job authorization
        JButton addDetailsButton = new JButton("Add");
        addDetailsButton.setEnabled(true); // Disable the button initially

        Dimension buttonSize = new Dimension(200, 40);
        addDetailsButton.setPreferredSize(buttonSize);

        // Create a panel for the radio buttons
        JPanel addDetailsPanel = new JPanel(new GridBagLayout());

        // Create label and text field for new salary
        JLabel addDetailsLabel = new JLabel("Add personal details");
        addDetailsLabel.setFont(addDetailsLabel.getFont().deriveFont(24.0f));

        JTextField addDetailsField = new JTextField(20);
        constraints.gridx = 0; // Column 0
        constraints.gridy = 0; // Row 0
        addDetailsPanel.add(addDetailsLabel, constraints);
        addDetailsField.setPreferredSize(new Dimension(200, addDetailsField.getPreferredSize().height+ 30)); // Adjust the width and height
        constraints.gridx = 1; // Column 1
        addDetailsPanel.add(addDetailsField, constraints);




        addDetailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String addDetails = addDetailsField.getText().trim();
                int counter = 0;
                if (addDetails.equals("")) {
                    counter++;
                    JOptionPane.showMessageDialog(addDetailsButton, "Personal details cannot be empty", "Input Error", JOptionPane.ERROR_MESSAGE);
                }
                if(counter == 0){
                    service.setPersonalDetailsForEmployee(employeeId, addDetails);
                    addDetailsField.setText("");
                    JOptionPane.showMessageDialog(addDetailsButton, "Personal details have been changed", "Success", JOptionPane.PLAIN_MESSAGE);
                }
            }
        });

        // Add radio buttons panel and add button to the options panel
        optionsPanel.add(addDetailsPanel, BorderLayout.CENTER);

        JPanel newButtonPanel = new JPanel(new FlowLayout());
        newButtonPanel.add(addDetailsButton);

        optionsPanel.add(newButtonPanel, BorderLayout.SOUTH);

        // Repaint the options panel
        optionsPanel.revalidate();
        optionsPanel.repaint();

        // Clear the text field
        addDetailsField.setText("");
    }

    private void addAuthorizationOptions(JPanel optionsPanel, JPanel buttonPanel, String employeeId) {
        // Check if options panel already has a component at index 1
        if (optionsPanel.getComponentCount() > 1) {
            optionsPanel.removeAll(); // Remove all components from optionsPanel
            optionsPanel.add(buttonPanel, BorderLayout.NORTH); // Add buttonPanel back to optionsPanel
            optionsPanel.revalidate(); // Revalidate the panel to update the layout
            optionsPanel.repaint(); // Repaint the panel
        }

        // Create a panel for the radio buttons
        JPanel radioButtonsPanel = new JPanel(new GridLayout(0, 1));

        // Create radio buttons and button group for the job options
        JRadioButton shiftManagerRadioButton = new JRadioButton("Shift Manager");
        JRadioButton storekeeperRadioButton = new JRadioButton("Storekeeper");
        JRadioButton cashierRadioButton = new JRadioButton("Cashier");
        JRadioButton usherRadioButton = new JRadioButton("Usher");
        JRadioButton cleanerRadioButton = new JRadioButton("Cleaner");
        JRadioButton securityRadioButton = new JRadioButton("Security");

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(shiftManagerRadioButton);
        buttonGroup.add(storekeeperRadioButton);
        buttonGroup.add(cashierRadioButton);
        buttonGroup.add(usherRadioButton);
        buttonGroup.add(cleanerRadioButton);
        buttonGroup.add(securityRadioButton);

        // Create an "Add" button
        JButton addButton = new JButton("Add");
        Dimension buttonSize = new Dimension(200, 40);
        addButton.setPreferredSize(buttonSize);
        addButton.setEnabled(false); // Disable the button initially

        addButton.addActionListener(new ActionListener() {
            String jobType = "";

            @Override
            public void actionPerformed(ActionEvent e) {
                // Check which radio button is selected
                if (shiftManagerRadioButton.isSelected()) {
                    jobType = "3";
                    // Add Shift Manager job authorization
                    // Add your code here
                } else if (storekeeperRadioButton.isSelected()) {
                    jobType = "1";
                    // Add Storekeeper job authorization
                    // Add your code here
                } else if (cashierRadioButton.isSelected()) {
                    jobType = "2";
                    // Add Cashier job authorization
                    // Add your code here
                } else if (usherRadioButton.isSelected()) {
                    jobType = "6";
                    // Add Usher job authorization
                    // Add your code here
                } else if (cleanerRadioButton.isSelected()) {
                    jobType = "7";
                    // Add Cleaner job authorization
                    // Add your code here
                } else if (securityRadioButton.isSelected()) {
                    jobType = "5";
                    // Add Security job authorization
                    // Add your code here
                }

                // Reset radio button selection
                buttonGroup.clearSelection();

                service.AddJobTypeToEmployee(employeeId, jobType);
                JOptionPane.showMessageDialog(radioButtonsPanel, "This job has been added", "Success", JOptionPane.PLAIN_MESSAGE);
            }
        });

        // Create an ItemListener for the radio buttons
        ItemListener itemListener = new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                addButton.setEnabled(e.getStateChange() == ItemEvent.SELECTED);
            }
        };

        // Add the same ItemListener to each radio button in the group
        shiftManagerRadioButton.addItemListener(itemListener);
        storekeeperRadioButton.addItemListener(itemListener);
        cashierRadioButton.addItemListener(itemListener);
        usherRadioButton.addItemListener(itemListener);
        cleanerRadioButton.addItemListener(itemListener);
        securityRadioButton.addItemListener(itemListener);

        // Add radio buttons to the panel
        radioButtonsPanel.add(shiftManagerRadioButton);
        radioButtonsPanel.add(storekeeperRadioButton);
        radioButtonsPanel.add(cashierRadioButton);
        radioButtonsPanel.add(usherRadioButton);
        radioButtonsPanel.add(cleanerRadioButton);
        radioButtonsPanel.add(securityRadioButton);

        // Add radio buttons panel and add button to the options panel
        optionsPanel.add(radioButtonsPanel, BorderLayout.CENTER);

        // Create a new button panel with FlowLayout for the "Add" button
        JPanel newButtonPanel = new JPanel(new FlowLayout());
        newButtonPanel.add(addButton);
        optionsPanel.add(newButtonPanel, BorderLayout.SOUTH);

        // Repaint the options panel
        optionsPanel.revalidate();
        optionsPanel.repaint();
    }



    private void removeAuthorizationOptions(JPanel optionsPanel, JPanel buttonPanel, String employeeId) {
        // Check if options panel already has a component at index 1
        if (optionsPanel.getComponentCount() > 1) {
            optionsPanel.removeAll(); // Remove all components from optionsPanel
            optionsPanel.add(buttonPanel, BorderLayout.NORTH); // Add buttonPanel back to optionsPanel
            optionsPanel.revalidate(); // Revalidate the panel to update the layout
            optionsPanel.repaint(); // Repaint the panel
        }

        // Retrieve the authorized jobs for the employee
        List<String> authorizedJobs = service.getEmployeesJobs(employeeId);

        // Create a panel for the radio buttons
        JPanel radioButtonsPanel = new JPanel(new GridLayout(0, 1));

        // Create a button group for the radio buttons
        ButtonGroup buttonGroup = new ButtonGroup();


        // Create a button for updating the authorized jobs
        JButton removeButton = new JButton("Remove");
        Dimension buttonSize = new Dimension(200, 40);
        removeButton.setPreferredSize(buttonSize);
        removeButton.setEnabled(false); // Disable the button initially
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Update the authorized jobs based on the selected checkbox
                String jobType = "";
                Enumeration<AbstractButton> radioButtons = buttonGroup.getElements();
                while (radioButtons.hasMoreElements()) {
                    JRadioButton radioButton = (JRadioButton) radioButtons.nextElement();
                    if (radioButton.isSelected()) {
                        jobType = getJobTypeFromJobName(radioButton.getText());
                        break;
                    }
                }

                // Update the employee's authorized jobs
                service.removeJobTypeFromEmployee(employeeId, jobType);
                // Reset radio button selection
                buttonGroup.clearSelection();
                JOptionPane.showMessageDialog(radioButtonsPanel, "Authorized job has been removed", "Success", JOptionPane.PLAIN_MESSAGE);


                // Clear the radio buttons panel
                radioButtonsPanel.removeAll();
                buttonGroup.clearSelection();

                List<String> updatedAuthorizedJobs  = service.getEmployeesJobs(employeeId);
                // Create an ItemListener for the radio buttons
                ItemListener itemListener = new ItemListener() {
                    @Override
                    public void itemStateChanged(ItemEvent e) {
                        if (e.getStateChange() == ItemEvent.SELECTED) {
                            removeButton.setEnabled(true); // Enable the button when a radio button is selected
                        } else {
                            removeButton.setEnabled(false); // Disable the button when no radio button is selected
                        }
                    }


                };

                // Create radio buttons for the job options based on authorized jobs
                for (String job : updatedAuthorizedJobs) {
                    JRadioButton radioButton = new JRadioButton(job.toLowerCase());
                    radioButton.addItemListener(itemListener); // Add the ItemListener to enable/disable the button
                    buttonGroup.add(radioButton);
                    radioButtonsPanel.add(radioButton);
                }

            }
        });


        // Create an ItemListener for the radio buttons
        ItemListener itemListener = new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    removeButton.setEnabled(true); // Enable the button when a radio button is selected
                } else {
                    removeButton.setEnabled(false); // Disable the button when no radio button is selected
                }
            }


        };

        // Create radio buttons for the job options based on authorized jobs
        for (String job : authorizedJobs) {
            JRadioButton radioButton = new JRadioButton(job.toLowerCase());
            radioButton.addItemListener(itemListener); // Add the ItemListener to enable/disable the button
            buttonGroup.add(radioButton);
            radioButtonsPanel.add(radioButton);
        }

        // Add radio buttons panel and add button to the options panel
        optionsPanel.add(radioButtonsPanel, BorderLayout.CENTER);

        // Create a new button panel with FlowLayout for the "Add" button
        JPanel newButtonPanel = new JPanel(new FlowLayout());
        newButtonPanel.add(removeButton);
        optionsPanel.add(newButtonPanel, BorderLayout.SOUTH);


//        // Set the layout manager of optionsPanel to BorderLayout
//        optionsPanel.setLayout(new BorderLayout());
//
//        // Create a Box container to center the radioButtonsPanel horizontally and vertically
//        Box containerBox = Box.createVerticalBox();
//        containerBox.add(Box.createVerticalGlue());
//        containerBox.add(Box.createHorizontalStrut(200)); // Adjust the horizontal strut as needed for centering
//        containerBox.add(radioButtonsPanel);
//        containerBox.add(Box.createVerticalGlue());
//
//        // Add the containerBox and removeButton to the options panel
//        optionsPanel.add(containerBox, BorderLayout.CENTER);
//        optionsPanel.add(removeButton, BorderLayout.SOUTH);
//
//        // Apply empty borders for spacing
//        int horizontalSpacing = 20;
//        int verticalSpacing = 20;
//        optionsPanel.setBorder(BorderFactory.createEmptyBorder(verticalSpacing, horizontalSpacing, verticalSpacing, horizontalSpacing));

        // Repaint the options panel
        optionsPanel.revalidate();
        optionsPanel.repaint();
    }

    // Helper method to retrieve job type based on job name
    private String getJobTypeFromJobName(String jobName) {
        if (jobName.equals("shift_manager")) {
            return "3";
        } else if (jobName.equals("storekeeper")) {
            return "1";
        } else if (jobName.equals("cashier")) {
            return "2";
        } else if (jobName.equals("usher")) {
            return "6";
        } else if (jobName.equals("cleaner")) {
            // Add Cleaner job authorization
            return "7";
        } else if (jobName.equals("security")) {
            return "5";
        }
        return "";
    }


    private void resetFieldBorder(JTextField field) {
        Border border = UIManager.getBorder("TextField.border");
        field.setBorder(border);
    }


    private int checkInt(String num) {
        int returnNum;
        try {
            //check if it is an int
            returnNum = Integer.parseInt(num);
            // check if the int is positive
            if (returnNum < 0) {
                return -1;
            } else {
                return returnNum;
            }
        }
        // if not an int
        catch (NumberFormatException e) {
            return -1;
        }
    }
}

