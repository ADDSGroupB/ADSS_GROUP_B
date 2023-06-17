package PresentationLayer.GUI.HR;

import BuisnessLayer.HR.JobType;
import ServiceLayer.HR.CompanyService;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class AddDriver extends JFrame{
    private JPanel cards;
    private CardLayout cardLayout;
    private CompanyService service;

    public AddDriver(HrFrame HRFrame){
        service = new CompanyService();
        // Set up the JFrame
        setTitle("Register driver");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 1000);

        // Create the card layout and panel
        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);
        getContentPane().add(cards, BorderLayout.CENTER);

        JPanel addDriverPanel = addDriver(HRFrame);


        cards.add(addDriverPanel, "Add Driver");

        cardLayout.show(cards, "Main Menu");

        setVisible(true);
    }


    private JPanel addDriver(HrFrame HRFrame) {
        JPanel addDriverPanel =new JPanel(new GridLayout(0,1));

        JPanel detailsPanel = new JPanel(new FlowLayout());
        JPanel buttonPanel = new JPanel(new FlowLayout());

        // Create labels and text fields
        JLabel nameLabel = new JLabel("Full name");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JTextField nameField = new JTextField(30);

        addDriverPanel.add(nameLabel);
        addDriverPanel.add(nameField);

        JLabel idLabel = new JLabel("ID");
        idLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JTextField idField = new JTextField(30);

        addDriverPanel.add(idLabel);
        addDriverPanel.add(idField);

        JLabel bankInformationLabel = new JLabel("Bank information");
        bankInformationLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JTextField bankInformationField = new JTextField(50);

        addDriverPanel.add(bankInformationLabel);
        addDriverPanel.add(bankInformationField);

        // Set layout manager
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Add padding



        JLabel startEmploymentDateLabel = new JLabel("Start of employment date");
        startEmploymentDateLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JTextField startEmploymentDateField = new JTextField(20);

        addDriverPanel.add(startEmploymentDateLabel);
        addDriverPanel.add(startEmploymentDateField);

        JLabel salaryLabel = new JLabel("Salary per hour");
        salaryLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JTextField salaryField = new JTextField(20);

        addDriverPanel.add(salaryLabel);
        addDriverPanel.add(salaryField);

        JLabel termsOfEmploymentLabel = new JLabel("Terms of employment");
        termsOfEmploymentLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JTextField termsOfEmploymentField = new JTextField(50);

        addDriverPanel.add(termsOfEmploymentLabel);
        addDriverPanel.add(termsOfEmploymentField);

        JLabel personalDetailsLabel = new JLabel("Personal Details");
        personalDetailsLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JTextField personalDetailsField = new JTextField(20);

        addDriverPanel.add(personalDetailsLabel);
        addDriverPanel.add(personalDetailsField);

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JTextField passwordField = new JTextField(20);

        addDriverPanel.add(passwordLabel);
        addDriverPanel.add(passwordField);

        // Max Weight Allowed
        JLabel lblWeight = new JLabel("Max weight allowed");
        lblWeight.setFont(new Font("Arial", Font.BOLD, 14));

        JComboBox<String> cmbWeight = new JComboBox<>(new String[]{"1500", "2250", "3000"});
        addDriverPanel.add(lblWeight);
        addDriverPanel.add(cmbWeight);

        // Driver license
        JLabel lblLicense = new JLabel("License");
        lblLicense.setFont(new Font("Arial", Font.BOLD, 14));

        JComboBox<String> cmbLicense = new JComboBox<>(new String[]{"1", "2", "3"});
        addDriverPanel.add(lblLicense);
        addDriverPanel.add(cmbLicense);

        // Add register Button
        JButton btnRegister = new JButton("Register");
        buttonPanel.add(btnRegister);


        //Add action listener to the button
        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText().trim();
                String id = idField.getText().trim();
                String bankAccount = bankInformationField.getText().trim();
                String startDate = startEmploymentDateField.getText().trim();
                String salary = salaryField.getText().trim();
                String termsOfEmployment = termsOfEmploymentField.getText().trim();
                String personalDetails = personalDetailsField.getText().trim();
                String password = passwordField.getText().trim();

                int counter = 0;

                // Validate the input
                if (name.isEmpty()) {
                    counter++;
                }
                else{
                    resetFieldBorder(nameField);
                    nameField.setText("");
                }

                if (id.isEmpty()) {
                    counter++;
                }
                else{
                    resetFieldBorder(idField);
                    idField.setText("");
                }

                if (bankAccount.isEmpty()) {
                    counter++;
                }
                else{
                    resetFieldBorder(bankInformationField);
                    bankInformationField.setText("");
                }

                if (startDate.isEmpty()) {
                    counter++;
                }
                else{
                    resetFieldBorder(startEmploymentDateField);
                    startEmploymentDateField.setText("");
                }

                if (salary.isEmpty()) {
                    counter++;
                }
                else{
                    resetFieldBorder(salaryField);
                    salaryField.setText("");
                }

                if (termsOfEmployment.isEmpty()) {
                    counter++;
                }
                else{
                    resetFieldBorder(termsOfEmploymentField);
                    termsOfEmploymentField.setText("");
                }

                if (personalDetails.isEmpty()) {
                    counter++;
                }
                else{
                    resetFieldBorder(personalDetailsField);
                    personalDetailsField.setText("");
                }

                if (password.isEmpty()) {
                    counter++;
                }
                else{
                    resetFieldBorder(passwordField);
                    passwordField.setText("");
                }

                int driverMaxWeight = Integer.parseInt(cmbWeight.getSelectedItem().toString());
                int driverLicense = Integer.parseInt(cmbLicense.getSelectedItem().toString());
                int salaryInt = checkInt(salary);

                if (salaryInt == -1) {
                    counter++;
                    JOptionPane.showMessageDialog(addDriverPanel, "Salary must be a positive number", "Input Error", JOptionPane.ERROR_MESSAGE);
                }

                if (!id.matches("^[\\d]+$")) {
                    counter++;
                    JOptionPane.showMessageDialog(addDriverPanel, "ID must all numbers", "Input Error", JOptionPane.ERROR_MESSAGE);
                }

                try{
                    if(!id.equals("")) {
                        service.checkIfEmployeesInCompany(id);
                    }
                }
                catch (RuntimeException ex){
                    counter++;
                    JOptionPane.showMessageDialog(addDriverPanel, "This employee already exists", "Input Error", JOptionPane.ERROR_MESSAGE);
                }

                //register driver
                if(counter == 0){
                    service.registerNewDriver(driverMaxWeight, driverLicense, name, id, bankAccount, startDate,
                            salaryInt, personalDetails, termsOfEmployment, password);
                    JOptionPane.showMessageDialog(addDriverPanel, "The driver has been registered", "Success", JOptionPane.PLAIN_MESSAGE);

                }
            }
        });

        // Button to return to the main menu
        JButton returnButton = new JButton("Return to Main Menu");
        buttonPanel.add(returnButton);
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                HRFrame.setVisible(true);
                dispose();
            }
        });

        // Add components to the main panel
        addDriverPanel.add(detailsPanel, BorderLayout.WEST);
        addDriverPanel.add(buttonPanel, BorderLayout.SOUTH);

        return addDriverPanel;
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
