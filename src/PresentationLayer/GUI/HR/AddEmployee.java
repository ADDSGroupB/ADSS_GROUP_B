package PresentationLayer.GUI.HR;

import BuisnessLayer.HR.JobType;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import ServiceLayer.HR.CompanyService;

public class AddEmployee extends JFrame{
    private JPanel cards;
    private CardLayout cardLayout;
    private CompanyService service;

   public AddEmployee(HrFrame HRFrame){
        service = new CompanyService();
        // Set up the JFrame
        setTitle("Register employee");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 1000);

        // Create the card layout and panel
        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);
        getContentPane().add(cards, BorderLayout.CENTER);

        JPanel addEmployeePanelCard = addEmployee(HRFrame);


        cards.add(addEmployeePanelCard, "Add Employee");
       add(cards);
       cardLayout.show(cards, "Main Menu");

       setVisible(true);
    }


    private JPanel addEmployee(HrFrame HRFrame) {
       JPanel addEmployeePanelCard =new JPanel(new GridLayout(0,1));

        JPanel detailsPanel = new JPanel(new FlowLayout());
        JPanel jobsPanel = new JPanel(new FlowLayout());
        JPanel branchesPanel = new JPanel(new FlowLayout());
        JPanel buttonPanel = new JPanel(new FlowLayout());

        // Create labels and text fields
        JLabel nameLabel = new JLabel("Full name");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JTextField nameField = new JTextField(10);

        addEmployeePanelCard.add(nameLabel);
        addEmployeePanelCard.add(nameField);


        JLabel idLabel = new JLabel("ID");
        idLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JTextField idField = new JTextField(10);
        addEmployeePanelCard.add(idLabel);
        addEmployeePanelCard.add(idField);

        JLabel bankInformationLabel = new JLabel("Bank information");
        bankInformationLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JTextField bankInformationField = new JTextField(10);

        addEmployeePanelCard.add(bankInformationLabel);
        addEmployeePanelCard.add(bankInformationField);

        // Set layout manager
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Add padding
        JLabel startEmploymentDateLabel = new JLabel("Start of employment date");
        startEmploymentDateLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JTextField startEmploymentDateField = new JTextField(10);

        addEmployeePanelCard.add(startEmploymentDateLabel);
        addEmployeePanelCard.add(startEmploymentDateField);

        JLabel salaryLabel = new JLabel("Salary per hour");
        salaryLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JTextField salaryField = new JTextField(10);

        addEmployeePanelCard.add(salaryLabel);
        addEmployeePanelCard.add(salaryField);

        JLabel termsOfEmploymentLabel = new JLabel("Terms of employment");
        termsOfEmploymentLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JTextField termsOfEmploymentField = new JTextField(10);

        addEmployeePanelCard.add(termsOfEmploymentLabel);
        addEmployeePanelCard.add(termsOfEmploymentField);

        JLabel personalDetailsLabel = new JLabel("Personal Details");
        personalDetailsLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JTextField personalDetailsField = new JTextField(10);

        addEmployeePanelCard.add(personalDetailsLabel);
        addEmployeePanelCard.add(personalDetailsField);

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JTextField passwordField = new JTextField(10);

        addEmployeePanelCard.add(passwordLabel);
        addEmployeePanelCard.add(passwordField);

        //addEmployeePanelCard.setLayout(new GridLayout(5, 2)); // Adjust the layout as per your preference

        // Add register Button
        JButton btnRegister = new JButton("Register");
        buttonPanel.add(btnRegister);

        // Create jobs title label
        JLabel jobsTitleLabel = new JLabel("Jobs");
        jobsTitleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        jobsPanel.add(jobsTitleLabel);

        // create checkboxes for jobs
        JCheckBox checkBox1 = new JCheckBox("Storekeeper");
        JCheckBox checkBox2 = new JCheckBox("Cashier");
        JCheckBox checkBox3 = new JCheckBox("Shift Manager");
        JCheckBox checkBox4 = new JCheckBox("Security");
        JCheckBox checkBox5 = new JCheckBox("Usher");
        JCheckBox checkBox6 = new JCheckBox("Cleaner");


        // Add jobs title and checkboxes to the panel

        jobsPanel.add(checkBox1);
        jobsPanel.add(checkBox2);
        jobsPanel.add(checkBox3);
        jobsPanel.add(checkBox4);
        jobsPanel.add(checkBox5);
        jobsPanel.add(checkBox6);

        // Create branches title label
        JLabel branchesTitleLabel = new JLabel("Branches");
        branchesTitleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        branchesPanel.add(branchesTitleLabel);

        // create checkboxes for branches
        JCheckBox checkBox11 = new JCheckBox("1");
        JCheckBox checkBox21 = new JCheckBox("2");
        JCheckBox checkBox31 = new JCheckBox("3");
        JCheckBox checkBox41 = new JCheckBox("4");
        JCheckBox checkBox51 = new JCheckBox("5");
        JCheckBox checkBox61 = new JCheckBox("6");
        JCheckBox checkBox71 = new JCheckBox("7");
        JCheckBox checkBox81 = new JCheckBox("8");
        JCheckBox checkBox91 = new JCheckBox("9");
        JCheckBox checkBox101 = new JCheckBox("10");

        // Add branches title and checkboxes to the panel

        branchesPanel.add(checkBox11);
        branchesPanel.add(checkBox21);
        branchesPanel.add(checkBox31);
        branchesPanel.add(checkBox41);
        branchesPanel.add(checkBox51);
        branchesPanel.add(checkBox61);
        branchesPanel.add(checkBox71);
        branchesPanel.add(checkBox81);
        branchesPanel.add(checkBox91);
        branchesPanel.add(checkBox101);
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

                int salaryInt = checkInt(salary);

                if (salaryInt == -1) {
                    counter++;
                    JOptionPane.showMessageDialog(addEmployeePanelCard, "Salary must be a positive number", "Input Error", JOptionPane.ERROR_MESSAGE);
                }

                if (!id.matches("^[\\d]+$")) {
                    counter++;
                    JOptionPane.showMessageDialog(addEmployeePanelCard, "ID must all numbers", "Input Error", JOptionPane.ERROR_MESSAGE);
                }

                try{
                    if(!id.equals("")) {
                        service.checkIfEmployeesInCompany(id);
                    }
                }
                catch (RuntimeException ex){
                    counter++;
                    JOptionPane.showMessageDialog(addEmployeePanelCard, "This employee already exists", "Input Error", JOptionPane.ERROR_MESSAGE);
                }

                //register employee


                // Create the branchList set
                Set<Integer> branchList = new HashSet<>();
                int branchCount = 0;
                if (checkBox11.isSelected()) {
                    branchCount++;
                    branchList.add(1);
                    checkBox11.setSelected(false);
                }
                if (checkBox21.isSelected()) {
                    branchCount++;
                    branchList.add(2);
                    checkBox21.setSelected(false);
                }
                if (checkBox31.isSelected()) {
                    branchCount++;
                    branchList.add(3);
                    checkBox31.setSelected(false);
                }
                if (checkBox41.isSelected()) {
                    branchCount++;
                    branchList.add(4);
                    checkBox41.setSelected(false);
                }
                if (checkBox51.isSelected()) {
                    branchCount++;
                    branchList.add(5);
                    checkBox51.setSelected(false);
                }
                if (checkBox61.isSelected()) {
                    branchCount++;
                    branchList.add(6);
                    checkBox61.setSelected(false);
                }
                if (checkBox71.isSelected()) {
                    branchCount++;
                    branchList.add(7);
                    checkBox71.setSelected(false);
                }
                if (checkBox81.isSelected()) {
                    branchCount++;
                    branchList.add(8);
                    checkBox81.setSelected(false);
                }
                if (checkBox91.isSelected()) {
                    branchCount++;
                    branchList.add(9);
                    checkBox91.setSelected(false);
                }
                if (checkBox101.isSelected()) {
                    branchCount++;
                    branchList.add(10);
                    checkBox101.setSelected(false);
                }

                if(branchCount == 0){
                    counter++;
                    JOptionPane.showMessageDialog(addEmployeePanelCard, "You have to choose at least one branch", "Input Error", JOptionPane.ERROR_MESSAGE);

                }

                // Create the employeesAuthorizedJobs list
                ArrayList<JobType> employeesAuthorizedJobs = new ArrayList<>();

                if (checkBox1.isSelected()) {
                    employeesAuthorizedJobs.add(JobType.STOREKEEPER);
                }
                if (checkBox2.isSelected()) {
                    employeesAuthorizedJobs.add(JobType.CASHIER);
                }
                if (checkBox3.isSelected()) {
                    employeesAuthorizedJobs.add(JobType.SHIFT_MANAGER);
                }
                if (checkBox4.isSelected()) {
                    employeesAuthorizedJobs.add(JobType.SECURITY);
                }
                if (checkBox5.isSelected()) {
                    employeesAuthorizedJobs.add(JobType.USHER);
                }
                if (checkBox6.isSelected()) {
                    employeesAuthorizedJobs.add(JobType.CLEANER);
                }

                if(counter == 0){
                    checkBox1.setSelected(false);
                    checkBox2.setSelected(false);
                    checkBox3.setSelected(false);
                    checkBox4.setSelected(false);
                    checkBox5.setSelected(false);
                    checkBox6.setSelected(false);
                    service.registerNewEmployee(name, id, bankAccount, startDate,
                            salaryInt, personalDetails, employeesAuthorizedJobs, termsOfEmployment, password, branchList);
                    JOptionPane.showMessageDialog(addEmployeePanelCard, "The employee has been registered", "Success", JOptionPane.PLAIN_MESSAGE);

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
        addEmployeePanelCard.add(detailsPanel, BorderLayout.WEST);
        addEmployeePanelCard.add(jobsPanel, BorderLayout.EAST);
        addEmployeePanelCard.add(branchesPanel, BorderLayout.EAST);
        addEmployeePanelCard.add(buttonPanel, BorderLayout.SOUTH);

        return addEmployeePanelCard;
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
