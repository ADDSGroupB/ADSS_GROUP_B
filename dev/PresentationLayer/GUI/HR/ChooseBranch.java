package PresentationLayer.GUI.HR;

import BuisnessLayer.HR.JobType;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

import BuisnessLayer.HR.Pair;
import BuisnessLayer.HR.Shift;
import BuisnessLayer.HR.ShiftType;
import ServiceLayer.HR.CompanyService;

public class ChooseBranch extends JFrame {
    private JPanel cards;
    private CardLayout cardLayout;
    private CompanyService service;
    private int branch;
    private int shiftCounter;
    private DefaultTableCellRenderer cellRenderer;
    private LocalDate[] datesOfWeek;
    private String startTimeOfShiftMorning;
    private String endTimeOfShiftMorning;
    private String startTimeOfShiftEvening;
    private String endTimeOfShiftEvening;
    private String selectedJob;
    private int dateCounter;
    private String date;
    String shift;
    Pair<Shift, JobType> returnValues;
    JScrollPane scrollPane;
    private String startTimeOfShift;
    private String endTimeOfShift;

    public ChooseBranch(HrFrame HRFrame) {
        dateCounter = 0;
        shiftCounter = 1;
        service = new CompanyService();
        // Set up the JFrame
        setTitle("Branch Option");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 1000);

        // Create the card layout and panel
        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);
        getContentPane().add(cards, BorderLayout.CENTER);

        JPanel addEmployeePanelCard = chooseBranch(HRFrame);


        cards.add(addEmployeePanelCard, "Add Employee");

        cardLayout.show(cards, "Main Menu");

        setVisible(true);
    }

    private JPanel chooseBranch(HrFrame HRFrame) {
        JPanel editDetailsPanel = new JPanel(new BorderLayout());
        JPanel buttonPanel1 = new JPanel(new FlowLayout());

        // Driver license
        JLabel lblbranch = new JLabel("Branch number");
        JComboBox<String> cmbbranch = new JComboBox<>(new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"});
        JPanel idPanel = new JPanel();
        idPanel.add(lblbranch);
        idPanel.add(cmbbranch);

        // Create row of buttons
        JPanel buttonPanel = new JPanel(new GridLayout(1, 7, 10, 10));

        JButton createShiftsButton = new JButton("Create shifts");
        JButton assignRemoveFromShiftButton = new JButton("<html>Assign/remove<br/>employee</html>");
        JButton editShiftDetailsButton = new JButton("Edit shift");
        JButton displayShiftsButton = new JButton("Display shifts");
        JButton cancelledProductsButton = new JButton("Cancellations");
        JButton salaryReportButton = new JButton("Salary report");
        JButton displayEmployeesButton = new JButton("All employees");
        JButton checkValidationButton = new JButton("<html>Shift<br/>validation</html>");



        // Add buttons to the panel
        buttonPanel.add(createShiftsButton);
        buttonPanel.add(assignRemoveFromShiftButton);
        buttonPanel.add(editShiftDetailsButton);
        buttonPanel.add(displayShiftsButton);
        buttonPanel.add(salaryReportButton);
        buttonPanel.add(cancelledProductsButton);
        buttonPanel.add(displayEmployeesButton);
        buttonPanel.add(checkValidationButton);

        // Create a panel to hold the button panel and additional options
        JPanel optionsPanel = new JPanel(new BorderLayout());
        optionsPanel.add(buttonPanel, BorderLayout.NORTH);

        // Create a panel to hold the idPanel and optionsPanel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(idPanel, BorderLayout.NORTH);
        mainPanel.add(optionsPanel, BorderLayout.CENTER);

        editDetailsPanel.add(mainPanel, BorderLayout.CENTER);

        // Action listener for the Employee ID textbox
        cmbbranch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                branch = Integer.parseInt(cmbbranch.getSelectedItem().toString());
            }
        });
        // Action listener for the assignRemoveFromShiftButton
        displayShiftsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayShiftsOption(optionsPanel, buttonPanel, branch);
            }
        });


        createShiftsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int count = 0;
                try {
                    service.checkIfShiftsAlreadyMade(branch);
                } catch (Exception ex) {
                    count++;
                    JOptionPane.showMessageDialog(editDetailsPanel, "Shifts for next week are already made", "Input Error", JOptionPane.ERROR_MESSAGE);
                }
                if (count == 0) {
                    createShiftsOption(optionsPanel, buttonPanel, branch);
                }
            }
        });

        // Action listener for the assignRemoveFromShiftButton
        assignRemoveFromShiftButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editShiftsOption(optionsPanel, buttonPanel, branch);
            }
        });

        // Action listener for the assignRemoveFromShiftButton
        editShiftDetailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editShiftDetailsOption(optionsPanel, buttonPanel, branch);
            }
        });

        // Action listener for the salaryReportButton
        salaryReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                salaryReportOption(optionsPanel, buttonPanel, branch);
            }
        });

        // Action listener for the cancelledProductsButton
        cancelledProductsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelledProductOption(optionsPanel, buttonPanel, branch);
            }
        });

        // Action listener for the displayEmployeesButton
        displayEmployeesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayEmployeesOption(optionsPanel, buttonPanel, branch);
            }
        });

        // Action listener for the checkValidationButton
        checkValidationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkValidationOption(optionsPanel, buttonPanel, branch);
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

    private void checkValidationOption(JPanel optionsPanel, JPanel buttonPanel, int branch) {
// Create the submit shifts panel
        JPanel submitPanel = new JPanel(new GridLayout(0, 2));

        JLabel dateLabel = new JLabel("Enter the date of the shift you want (yyyy-mm-dd):");
        // Create the masked text field for date input
        MaskFormatter dateFormatter;
        try {
            dateFormatter = new MaskFormatter("####-##-##");
        } catch (ParseException ex) {
            ex.printStackTrace();
            return;
        }
        JFormattedTextField dateField = new JFormattedTextField(dateFormatter);
        dateField.setColumns(10);

        JLabel shiftLabel = new JLabel("Morning or Evening?");
        String[] shiftOptions = {"Morning", "Evening"};
        JComboBox<String> shiftComboBox = new JComboBox<>(shiftOptions);

        // Add components to the submit shifts panel
        submitPanel.add(dateLabel);
        submitPanel.add(dateField);
        submitPanel.add(shiftLabel);
        submitPanel.add(shiftComboBox);

        // Show the submit panel in a dialog
        int option = JOptionPane.showOptionDialog(optionsPanel, submitPanel, "Enter Shift Details", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);

        if (option == JOptionPane.OK_OPTION) {
            String date = dateField.getText();
            String shiftType = shiftComboBox.getSelectedItem().toString();
            String shift = "";
            if(shiftType.equals("Morning")){
                shift = "m";
            } else {
                shift = "e";
            }
            try {
                service.isShiftValid(date, shift, branch);
                JOptionPane.showMessageDialog(optionsPanel, "Shift is valid", "Not valid", JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(optionsPanel, ex.getMessage(), "Not valid", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }


    // Create the shift details panel
    private void displayShiftsOption(JPanel optionsPanel, JPanel buttonPanel, int branch) {
        try {
            String cancellationText = service.displayAllShiftsDetails(branch);
            JTextArea cancellationTextArea = new JTextArea(10, 30);
            cancellationTextArea.setText(cancellationText);
            cancellationTextArea.setEditable(false);
            Font newFont = cancellationTextArea.getFont().deriveFont(20f); // Create a new Font object with size 16
            cancellationTextArea.setFont(newFont); // Set the new font to the salaryReportArea
            JScrollPane cancellationScrollPane = new JScrollPane(cancellationTextArea);

            // Create a panel to hold the cancellation scroll pane and the cancel button
            JPanel cancellationPanel = new JPanel(new BorderLayout());
            cancellationPanel.add(cancellationScrollPane, BorderLayout.CENTER);


            // Create a panel to hold both the cancellation panel and the cancellation buttons
            JPanel combinedPanel = new JPanel(new BorderLayout());
            combinedPanel.add(cancellationPanel, BorderLayout.CENTER);

            // Remove all components from optionsPanel
            optionsPanel.removeAll();
            // Add the combined panel to optionsPanel
            optionsPanel.add(combinedPanel, BorderLayout.CENTER);
            // Add buttonPanel back to optionsPanel
            optionsPanel.add(buttonPanel, BorderLayout.NORTH);
            // Revalidate and repaint the panel
            optionsPanel.revalidate();
            optionsPanel.repaint();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(optionsPanel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }






    // Create the shift details panel
    private void createShiftsOption(JPanel optionsPanel, JPanel buttonPanel, int branch) {
        // Check if options panel already has a component at index 1
        if (optionsPanel.getComponentCount() > 1) {
            optionsPanel.removeAll(); // Remove all components from optionsPanel
            optionsPanel.add(buttonPanel, BorderLayout.NORTH); // Add buttonPanel back to optionsPanel
            optionsPanel.revalidate(); // Revalidate the panel to update the layout
            optionsPanel.repaint(); // Repaint the panel
        }

        datesOfWeek = service.currDateAndEndOfWeek(branch);
        LocalDate date = datesOfWeek[0];
        LocalDate[] realDates = new LocalDate[10];
        realDates[0] = datesOfWeek[0];
        int temp = 1;
        while (date.isBefore(datesOfWeek[1])) {
            date = date.plusDays(1);
            realDates[temp] = date;
            temp++;
        }
        datesOfWeek = realDates;

        // Create a panel for the radio buttons
        JPanel createShiftsPanel = new JPanel(new GridLayout(0, 1));



        // Morning Shift Start Time
        JLabel morningShiftStartLabel = new JLabel("Morning Shift Start Time:");
        morningShiftStartLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JSpinner morningShiftStartSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor morningShiftStartEditor = new JSpinner.DateEditor(morningShiftStartSpinner, "HH:mm");
        morningShiftStartSpinner.setEditor(morningShiftStartEditor);
        Dimension buttonSize = new Dimension(200, 40);
        morningShiftStartSpinner.setPreferredSize(buttonSize);

        JPanel newButtonPanel3 = new JPanel(new FlowLayout());
        newButtonPanel3.add(morningShiftStartLabel);
        newButtonPanel3.add(morningShiftStartSpinner);
        createShiftsPanel.add(newButtonPanel3);

        // Morning Shift End Time
        JLabel morningShiftEndLabel = new JLabel("Morning Shift End Time:");
        morningShiftEndLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JSpinner morningShiftEndSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor morningShiftEndEditor = new JSpinner.DateEditor(morningShiftEndSpinner, "HH:mm");
        morningShiftEndSpinner.setEditor(morningShiftEndEditor);
        buttonSize = new Dimension(200, 40);
        morningShiftEndSpinner.setPreferredSize(buttonSize);

        JPanel newButtonPanel2 = new JPanel(new FlowLayout());
        newButtonPanel2.add(morningShiftEndLabel);
        newButtonPanel2.add(morningShiftEndSpinner);
        createShiftsPanel.add(newButtonPanel2);

        // Evening Shift Start Time
        JLabel eveningShiftStartLabel = new JLabel("Evening Shift Start Time:");
        eveningShiftStartLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JSpinner eveningShiftStartSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor eveningShiftStartEditor = new JSpinner.DateEditor(eveningShiftStartSpinner, "HH:mm");
        eveningShiftStartSpinner.setEditor(eveningShiftStartEditor);
        buttonSize = new Dimension(200, 40);
        eveningShiftStartSpinner.setPreferredSize(buttonSize);

        JPanel newButtonPanel = new JPanel(new FlowLayout());
        newButtonPanel.add(eveningShiftStartLabel);
        newButtonPanel.add(eveningShiftStartSpinner);
        createShiftsPanel.add(newButtonPanel);
        createShiftsPanel.add(newButtonPanel);


        // Evening Shift End Time
        JLabel eveningShiftEndLabel = new JLabel("Evening Shift End Time:");
        eveningShiftEndLabel.setFont(new Font("Arial", Font.BOLD, 14));


        JSpinner eveningShiftEndSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor eveningShiftEndEditor = new JSpinner.DateEditor(eveningShiftEndSpinner, "HH:mm");
        eveningShiftEndSpinner.setEditor(eveningShiftEndEditor);
        buttonSize = new Dimension(200, 40);
        eveningShiftEndSpinner.setPreferredSize(buttonSize);

        JPanel newButtonPanel1 = new JPanel(new FlowLayout());
        newButtonPanel1.add(eveningShiftEndLabel);
        newButtonPanel1.add(eveningShiftEndSpinner);
        createShiftsPanel.add(newButtonPanel1);


        // Set layout manager
//        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Add padding

// Set the default time for the Morning Shift Start Time
        Calendar morningShiftStartDefaultTime = Calendar.getInstance();
        morningShiftStartDefaultTime.set(Calendar.HOUR_OF_DAY, 8); // Set the desired hour
        morningShiftStartDefaultTime.set(Calendar.MINUTE, 0); // Set the desired minute
        morningShiftStartSpinner.setValue(morningShiftStartDefaultTime.getTime());

// Set the default time for the Morning Shift End Time
        Calendar morningShiftEndDefaultTime = Calendar.getInstance();
        morningShiftEndDefaultTime.set(Calendar.HOUR_OF_DAY, 15); // Set the desired hour
        morningShiftEndDefaultTime.set(Calendar.MINUTE, 0); // Set the desired minute
        morningShiftEndSpinner.setValue(morningShiftEndDefaultTime.getTime());

// Set the default time for the Evening Shift Start Time
        Calendar eveningShiftStartDefaultTime = Calendar.getInstance();
        eveningShiftStartDefaultTime.set(Calendar.HOUR_OF_DAY, 15); // Set the desired hour
        eveningShiftStartDefaultTime.set(Calendar.MINUTE, 0); // Set the desired minute
        eveningShiftStartSpinner.setValue(eveningShiftStartDefaultTime.getTime());

// Set the default time for the Evening Shift End Time
        Calendar eveningShiftEndDefaultTime = Calendar.getInstance();
        eveningShiftEndDefaultTime.set(Calendar.HOUR_OF_DAY, 22); // Set the desired hour
        eveningShiftEndDefaultTime.set(Calendar.MINUTE, 0); // Set the desired minute
        eveningShiftEndSpinner.setValue(eveningShiftEndDefaultTime.getTime());

        JButton createShiftButton = new JButton("Create Shift");
        createShiftsPanel.add(createShiftButton);
        buttonSize = new Dimension(200, 40);
        eveningShiftEndLabel.setPreferredSize(buttonSize);

        JPanel newButtonPanel4 = new JPanel(new FlowLayout());
        newButtonPanel4.add(createShiftButton);
        createShiftsPanel.add(newButtonPanel4);

        optionsPanel.add(createShiftsPanel, BorderLayout.CENTER);

// ActionListener for the createShiftButton
        createShiftButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Date startTimeOfShiftMorningDate = (Date) morningShiftStartSpinner.getValue();
                LocalTime startTimeOfShiftMorningValue = LocalDateTime.ofInstant(startTimeOfShiftMorningDate.toInstant(), ZoneId.systemDefault()).toLocalTime();
                startTimeOfShiftMorning = startTimeOfShiftMorningValue.format(DateTimeFormatter.ofPattern("HH:mm"));

                Date endTimeOfShiftMorningDate = (Date) morningShiftEndSpinner.getValue();
                LocalTime endTimeOfShiftMorningValue = LocalDateTime.ofInstant(endTimeOfShiftMorningDate.toInstant(), ZoneId.systemDefault()).toLocalTime();
                endTimeOfShiftMorning = endTimeOfShiftMorningValue.format(DateTimeFormatter.ofPattern("HH:mm"));

                Date startTimeOfShiftEveningDate = (Date) eveningShiftStartSpinner.getValue();
                LocalTime startTimeOfShiftEveningValue = LocalDateTime.ofInstant(startTimeOfShiftEveningDate.toInstant(), ZoneId.systemDefault()).toLocalTime();
                startTimeOfShiftEvening = startTimeOfShiftEveningValue.format(DateTimeFormatter.ofPattern("HH:mm"));

                Date endTimeOfShiftEveningDate = (Date) eveningShiftEndSpinner.getValue();
                LocalTime endTimeOfShiftEveningValue = LocalDateTime.ofInstant(endTimeOfShiftEveningDate.toInstant(), ZoneId.systemDefault()).toLocalTime();
                endTimeOfShiftEvening = endTimeOfShiftEveningValue.format(DateTimeFormatter.ofPattern("HH:mm"));


                // Proceed to the next page with the selected hour values
                createShiftsOptionPageTwo(optionsPanel, buttonPanel, branch);
            }
        });
    }

    // Create the shift details page two
    private void createShiftsOptionPageTwo(JPanel optionsPanel, JPanel buttonPanel, int branch) {
        JPanel createShiftsPanel = new JPanel(new GridLayout(0, 2));
        optionsPanel.removeAll(); // Remove all components from optionsPanel
        optionsPanel.add(buttonPanel, BorderLayout.NORTH); // Add buttonPanel back to optionsPanel
        optionsPanel.revalidate(); // Revalidate the panel to update the layout
        optionsPanel.repaint(); // Repaint the panel

        JLabel cancelShiftLabel = new JLabel("Cancel the shift:");
        JCheckBox cancelShiftCheckbox = new JCheckBox();
        createShiftsPanel.add(cancelShiftLabel);
        createShiftsPanel.add(cancelShiftCheckbox);

        JLabel storekeepersLabel = new JLabel("Number of storekeepers:");
        JTextField storekeepersField = new JTextField(10);
        createShiftsPanel.add(storekeepersLabel);
        createShiftsPanel.add(storekeepersField);

        JLabel generalEmployeesLabel = new JLabel("Number of general employees:");
        JTextField generalEmployeesField = new JTextField(10);
        createShiftsPanel.add(generalEmployeesLabel);
        createShiftsPanel.add(generalEmployeesField);

        JLabel securitysLabel = new JLabel("Number of securitys:");
        JTextField securitysField = new JTextField(10);
        createShiftsPanel.add(securitysLabel);
        createShiftsPanel.add(securitysField);

        JLabel ushersLabel = new JLabel("Number of ushers:");
        JTextField ushersField = new JTextField(10);
        createShiftsPanel.add(ushersLabel);
        createShiftsPanel.add(ushersField);

        JLabel cleanersLabel = new JLabel("Number of cleaners:");
        JTextField cleanersField = new JTextField(10);
        createShiftsPanel.add(cleanersLabel);
        createShiftsPanel.add(cleanersField);

        JLabel shiftManagersLabel = new JLabel("Number of shift managers:");
        JTextField shiftManagersField = new JTextField(10);
        createShiftsPanel.add(shiftManagersLabel);
        createShiftsPanel.add(shiftManagersField);

        JLabel CashiersLabel = new JLabel("Number of cashiers:");
        JTextField CashiersField = new JTextField(10);
        createShiftsPanel.add(CashiersLabel);
        createShiftsPanel.add(CashiersField);

        // ActionListener for the cancelShiftCheckbox
        cancelShiftCheckbox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean checkboxSelected = cancelShiftCheckbox.isSelected();
                storekeepersLabel.setVisible(!checkboxSelected);
                storekeepersField.setVisible(!checkboxSelected);
                generalEmployeesLabel.setVisible(!checkboxSelected);
                generalEmployeesField.setVisible(!checkboxSelected);
                securitysLabel.setVisible(!checkboxSelected);
                securitysField.setVisible(!checkboxSelected);
                ushersLabel.setVisible(!checkboxSelected);
                ushersField.setVisible(!checkboxSelected);
                cleanersLabel.setVisible(!checkboxSelected);
                cleanersField.setVisible(!checkboxSelected);
                shiftManagersLabel.setVisible(!checkboxSelected);
                shiftManagersField.setVisible(!checkboxSelected);
                CashiersLabel.setVisible(!checkboxSelected);
                CashiersField.setVisible(!checkboxSelected);

            }
        });

        JButton nextButton = new JButton("Next");
        createShiftsPanel.add(nextButton);

        // Create the shift counter label
        JLabel shiftCounterLabel = new JLabel(shiftCounter + "/14");
        createShiftsPanel.add(shiftCounterLabel);

        // Add radio buttons panel and add button to the options panel
        optionsPanel.add(createShiftsPanel, BorderLayout.CENTER);


        // Create the shift details button panel
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean isSelectedCancel = cancelShiftCheckbox.isSelected();
                int counter = 0;
                if (!isSelectedCancel) {
                    LocalDate shiftDate = datesOfWeek[dateCounter];
                    String shiftType;
                    if (shiftCounter % 2 == 0) {
                        shiftType = ShiftType.EVENING.toString().toLowerCase();
                        dateCounter++;
                    } else {
                        shiftType = ShiftType.MORNING.toString().toLowerCase();
                    }
                    int usherCount = checkInt(ushersField.getText().trim());
                    int storekeeperCount = checkInt(storekeepersField.getText().trim());
                    int generalEmployeeCount = checkInt(generalEmployeesField.getText().trim());
                    int securityCount = checkInt(securitysField.getText().trim());
                    int cleanersCount = checkInt(cleanersField.getText().trim());
                    int shiftManagersCount = checkInt(shiftManagersField.getText().trim());
                    int cashiersCount = checkInt(CashiersField.getText().trim());

                    if (usherCount == -1 || storekeeperCount == -1 || generalEmployeeCount == -1 || securityCount == -1 || cleanersCount == -1 || shiftManagersCount == -1 || cashiersCount == -1) {
                        counter++;
                        JOptionPane.showMessageDialog(createShiftsPanel, "Employees count should be a positive number", "Input Error", JOptionPane.ERROR_MESSAGE);
                    }
                    if (shiftManagersCount < 1 && counter == 0) {
                        counter++;
                        JOptionPane.showMessageDialog(createShiftsPanel, "There has to be at least 1 shift manager", "Input Error", JOptionPane.ERROR_MESSAGE);
                    }
                    if (counter == 0) {
                        service.createNewShiftsForNextWeek(shiftType, shiftDate, startTimeOfShiftMorning, endTimeOfShiftMorning, startTimeOfShiftEvening, endTimeOfShiftEvening
                                , cashiersCount, storekeeperCount, generalEmployeeCount, securityCount, usherCount, cleanersCount, shiftManagersCount, branch);
                    }
                } else {
                    if (shiftCounter % 2 == 0) {
                        dateCounter++;
                    }
                }
                if (shiftCounter == 14 && counter == 0) {
                    optionsPanel.removeAll(); // Remove all components from optionsPanel
                    optionsPanel.add(buttonPanel, BorderLayout.NORTH); // Add buttonPanel back to optionsPanel
                    optionsPanel.revalidate(); // Revalidate the panel to update the layout
                    optionsPanel.repaint(); // Repaint the panel
                    JOptionPane.showMessageDialog(optionsPanel, "All shifts have been added", "Success", JOptionPane.PLAIN_MESSAGE);
                } else {
                    if (counter == 0) {
                        shiftCounter++;
                        createShiftsOptionPageTwo(optionsPanel, buttonPanel, branch);
                    }
                }
            }
        });
        // Repaint the options panel
        optionsPanel.revalidate();
        optionsPanel.repaint();
    }

    private void displayEmployeesOption(JPanel optionsPanel, JPanel buttonPanel, int branch) {
        optionsPanel.removeAll(); // Remove all components from optionsPanel
        optionsPanel.add(buttonPanel, BorderLayout.NORTH); // Add buttonPanel back to optionsPanel
        optionsPanel.revalidate(); // Revalidate the panel to update the layout
        optionsPanel.repaint(); // Repaint the panel
        try {
            // Generate the string for displaying the employees
            String employeeDisplayText = service.displayAllRegisterEmployee2(branch);

            // Create the table model with column names
            String[] columnNames = { "Full Name", "Employee ID", "Authorized Jobs"};
            DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

            // Split the salary report string by line breaks
            String[] reportLines = employeeDisplayText.split("\n");

            // Iterate over each line and extract the employee data
            for (String line : reportLines) {
                // Split the line by the separator "Employee: " to extract the employee data
                String[] parts = line.split("Employee: ");
                if (parts.length == 2) {
                    String[] employeeData = parts[1].split(" Id: ");
                    if (employeeData.length == 2) {
                        String employeeName = employeeData[0].trim();
                        String[] jobs = employeeData[1].split(" Jobs: ");
                        if (jobs.length == 2) {
                            String employeeId = jobs[0].trim();
                            String salary = jobs[1].trim();

                            // Add the employee data to the table model
                            Object[] rowData = {employeeName, employeeId, salary};
                            tableModel.addRow(rowData);
                        }
                    }
                }
            }

            // Create the JTable with the table model
            JTable table = new JTable(tableModel);

// Set the column widths using TableColumnModel
            TableColumnModel columnModel = table.getColumnModel();
            columnModel.getColumn(0).setPreferredWidth(150); // First column width
            columnModel.getColumn(1).setPreferredWidth(100); // Second column width
            columnModel.getColumn(2).setPreferredWidth(750); // Stretch the column to occupy the remaining space

// Set the column widths using the table's autoResizeMode
            table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            table.getColumnModel().getColumn(0).setPreferredWidth(150);
            table.getColumnModel().getColumn(1).setPreferredWidth(100);
            table.getColumnModel().getColumn(2).setPreferredWidth(750);
            // Create a JScrollPane to contain the JTable
            JScrollPane scrollPane = new JScrollPane(table);

            // Add the scroll pane to the options panel
            optionsPanel.add(scrollPane, BorderLayout.CENTER);

            // Refresh the options panel to display the salary report
            optionsPanel.revalidate();
            optionsPanel.repaint();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(optionsPanel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }




    private void salaryReportOption(JPanel optionsPanel, JPanel buttonPanel, int branch){
        optionsPanel.removeAll(); // Remove all components from optionsPanel
        optionsPanel.add(buttonPanel, BorderLayout.NORTH); // Add buttonPanel back to optionsPanel
        optionsPanel.revalidate(); // Revalidate the panel to update the layout
        optionsPanel.repaint(); // Repaint the panel
        // Create the table model with column names
        String[] columnNames = {"Employee", "ID", "Salary"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        String salaryReport = service.SalaryReportForAllEmployee(branch);

        // Split the salary report string by line breaks
        String[] reportLines = salaryReport.split("\n");

        // Iterate over each line and extract the employee data
        for (String line : reportLines) {
            // Split the line by the separator "Employee: " to extract the employee data
            String[] parts = line.split("Employee: ");
            if (parts.length == 2) {
                String[] employeeData = parts[1].split(" Id: ");
                if (employeeData.length == 2) {
                    String employeeName = employeeData[0].trim();
                    String[] idSalary = employeeData[1].split(" Salary: ");
                    if (idSalary.length == 2) {
                        String employeeId = idSalary[0].trim();
                        String salary = idSalary[1].trim();

                        // Add the employee data to the table modele
                        Object[] rowData = {employeeName, employeeId, salary};
                        tableModel.addRow(rowData);
                    }
                }
            }
        }

        // Create the JTable with the table model
        JTable table = new JTable(tableModel);

        // Create a JScrollPane to contain the JTable
        JScrollPane scrollPane = new JScrollPane(table);

        // Add the scroll pane to the options panel
        optionsPanel.add(scrollPane, BorderLayout.CENTER);

        // Refresh the options panel to display the salary report
        optionsPanel.revalidate();
        optionsPanel.repaint();
    }

    private void cancelledProductOption(JPanel optionsPanel, JPanel buttonPanel, int branch){
// Create a panel to hold the button panel and additional options
        JPanel cancelledProductPanel = new JPanel(new BorderLayout());
        cancelledProductPanel.add(buttonPanel, BorderLayout.NORTH);

        // Create a panel to hold the three cancellation buttons
        JPanel cancellationButtonPanel = new JPanel(new GridLayout(1, 3, 10, 10));

        // Create the three cancellation buttons
        JButton displayCancellationsButton = new JButton("Display cancellations");
        Dimension buttonSize = new Dimension(200, 40);
        displayCancellationsButton.setPreferredSize(buttonSize);
        JPanel newButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        newButtonPanel.add(displayCancellationsButton, BorderLayout.CENTER);
        JButton addCancellationButton = new JButton("Add cancellation");
        buttonSize = new Dimension(200, 40);
        addCancellationButton.setPreferredSize(buttonSize);
        newButtonPanel.add(addCancellationButton);
        JButton removeCancellationButton = new JButton("Remove cancellation");
        buttonSize = new Dimension(200, 40);
        removeCancellationButton.setPreferredSize(buttonSize);
        newButtonPanel.add(removeCancellationButton);

        // Add the cancellation buttons to the panel
//        cancellationButtonPanel.add(displayCancellationsButton);
//        cancellationButtonPanel.add(addCancellationButton);
//        cancellationButtonPanel.add(removeCancellationButton);

        // Add the cancellation button panel to the cancelledProductPanel
        cancelledProductPanel.add(newButtonPanel, BorderLayout.CENTER);

        // Action listener for the displayCancellationsButton
        displayCancellationsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create the submit shifts panel
                JPanel submitPanel = new JPanel(new GridLayout(0, 2));

                JLabel dateLabel = new JLabel("Enter the date of the shift you want (yyyy-mm-dd):");
                // Create the masked text field for date input
                MaskFormatter dateFormatter;
                try {
                    dateFormatter = new MaskFormatter("####-##-##");
                } catch (ParseException ex) {
                    ex.printStackTrace();
                    return;
                }
                JFormattedTextField dateField = new JFormattedTextField(dateFormatter);
                dateField.setColumns(10);

                JLabel shiftLabel = new JLabel("Morning or Evening?");
                String[] shiftOptions = {"Morning", "Evening"};
                JComboBox<String> shiftComboBox = new JComboBox<>(shiftOptions);

                // Add components to the submit shifts panel
                submitPanel.add(dateLabel);
                submitPanel.add(dateField);
                submitPanel.add(shiftLabel);
                submitPanel.add(shiftComboBox);

                // Show the submit panel in a dialog
                int option = JOptionPane.showOptionDialog(optionsPanel, submitPanel, "Enter Shift Details", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);

                if (option == JOptionPane.OK_OPTION) {
                    String date = dateField.getText();
                    String shiftType = shiftComboBox.getSelectedItem().toString();
                    String shift = "";
                    if(shiftType.equals("Morning")){
                        shift = "m";
                    } else {
                        shift = "e";
                    }
                    try {
                        String cancellationText = service.displayCashierCancellation(date, shift, branch);

                        JTextArea cancellationTextArea = new JTextArea(10, 30);
                        cancellationTextArea.setText(cancellationText);
                        cancellationTextArea.setEditable(false);
                        Font newFont = cancellationTextArea.getFont().deriveFont(20f); // Create a new Font object with size 16
                        cancellationTextArea.setFont(newFont); // Set the new font to the salaryReportArea
                        JScrollPane cancellationScrollPane = new JScrollPane(cancellationTextArea);

                        // Create a panel to hold the cancellation scroll pane and the cancel button
                        JPanel cancellationPanel = new JPanel(new BorderLayout());
                        cancellationPanel.add(cancellationScrollPane, BorderLayout.CENTER);
                        cancellationPanel.add(displayCancellationsButton, BorderLayout.SOUTH);

                        // Create a panel to hold the cancellation buttons
                        JPanel cancellationButtonPanel = new JPanel();
                        cancellationButtonPanel.add(displayCancellationsButton);
                        cancellationButtonPanel.add(addCancellationButton);
                        cancellationButtonPanel.add(removeCancellationButton);

                        // Create a panel to hold both the cancellation panel and the cancellation buttons
                        JPanel combinedPanel = new JPanel(new BorderLayout());
                        combinedPanel.add(cancellationPanel, BorderLayout.CENTER);
                        combinedPanel.add(cancellationButtonPanel, BorderLayout.NORTH);

                        // Remove all components from optionsPanel
                        optionsPanel.removeAll();
                        // Add the combined panel to optionsPanel
                        optionsPanel.add(combinedPanel, BorderLayout.CENTER);
                        // Add buttonPanel back to optionsPanel
                        optionsPanel.add(buttonPanel, BorderLayout.NORTH);
                        // Revalidate and repaint the panel
                        optionsPanel.revalidate();
                        optionsPanel.repaint();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(optionsPanel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });



        // Action listener for the addCancellationButton
        addCancellationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create the submit shifts panel
                JPanel submitPanel = new JPanel(new GridLayout(0, 2));

                JLabel dateLabel = new JLabel("Enter the date of the shift you want (yyyy-mm-dd):");
                // Create the masked text field for date input
                MaskFormatter dateFormatter;
                try {
                    dateFormatter = new MaskFormatter("####-##-##");
                } catch (ParseException ex) {
                    ex.printStackTrace();
                    return;
                }
                JFormattedTextField dateField = new JFormattedTextField(dateFormatter);
                dateField.setColumns(10);

                JLabel shiftLabel = new JLabel("Morning or Evening?");
                String[] shiftOptions = {"Morning", "Evening"};
                JComboBox<String> shiftComboBox = new JComboBox<>(shiftOptions);

                JLabel employeeIdLabel = new JLabel("Employee ID");
                JTextField employeeIdField = new JTextField();

                JLabel barCodeLabel = new JLabel("Product");
                JTextField barCodeField = new JTextField();

                // Add components to the submit shifts panel
                submitPanel.add(dateLabel);
                submitPanel.add(dateField);
                submitPanel.add(shiftLabel);
                submitPanel.add(shiftComboBox);
                submitPanel.add(employeeIdLabel);
                submitPanel.add(employeeIdField);
                submitPanel.add(barCodeLabel);
                submitPanel.add(barCodeField);

                // Show the submit panel in a dialog
                int option = JOptionPane.showOptionDialog(optionsPanel, submitPanel, "Enter Shift Details", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);

                if (option == JOptionPane.OK_OPTION) {
                    String date = dateField.getText();
                    String shiftType = shiftComboBox.getSelectedItem().toString();
                    String ID = employeeIdField.getText();
                    String product = barCodeField.getText();
                    String shift = "";
                    if (shiftType.equals("Morning")) {
                        shift = "m";
                    } else {
                        shift = "e";
                    }
                    try {
                        service.addOrDeleteCashierCancellation(date, shift, ID, product, branch, "A");
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(optionsPanel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });


        // Action listener for the removeCancellationButton
        removeCancellationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create the submit shifts panel
                JPanel submitPanel = new JPanel(new GridLayout(0, 2));

                JLabel dateLabel = new JLabel("Enter the date of the shift you want (yyyy-mm-dd):");
                // Create the masked text field for date input
                MaskFormatter dateFormatter;
                try {
                    dateFormatter = new MaskFormatter("####-##-##");
                } catch (ParseException ex) {
                    ex.printStackTrace();
                    return;
                }
                JFormattedTextField dateField = new JFormattedTextField(dateFormatter);
                dateField.setColumns(10);

                JLabel shiftLabel = new JLabel("Morning or Evening?");
                String[] shiftOptions = {"Morning", "Evening"};
                JComboBox<String> shiftComboBox = new JComboBox<>(shiftOptions);

                JLabel employeeIdLabel = new JLabel("Employee ID");
                JTextField employeeIdField = new JTextField();

                JLabel barCodeLabel = new JLabel("Product");
                JTextField barCodeField = new JTextField();

                // Add components to the submit shifts panel
                submitPanel.add(dateLabel);
                submitPanel.add(dateField);
                submitPanel.add(shiftLabel);
                submitPanel.add(shiftComboBox);
                submitPanel.add(employeeIdLabel);
                submitPanel.add(employeeIdField);
                submitPanel.add(barCodeLabel);
                submitPanel.add(barCodeField);

                // Show the submit panel in a dialog
                int option = JOptionPane.showOptionDialog(optionsPanel, submitPanel, "Enter Shift Details", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);

                if (option == JOptionPane.OK_OPTION) {
                    String date = dateField.getText();
                    String shiftType = shiftComboBox.getSelectedItem().toString();
                    String ID = employeeIdField.getText();
                    String product = barCodeField.getText();
                    String shift = "";
                    if (shiftType.equals("Morning")) {
                        shift = "m";
                    } else {
                        shift = "e";
                    }
                    try {
                        service.addOrDeleteCashierCancellation(date, shift, ID, product, branch, "D");
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(optionsPanel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // Replace the optionsPanel content with the cancelledProductPanel
        optionsPanel.removeAll();
        optionsPanel.add(cancelledProductPanel, BorderLayout.CENTER);
        optionsPanel.revalidate();
        optionsPanel.repaint();
    }


    // Create the shift details page two
    private void editShiftDetailsOption(JPanel optionsPanel, JPanel buttonPanel, int branch) {
        optionsPanel.removeAll(); // Remove all components from optionsPanel
        optionsPanel.add(buttonPanel, BorderLayout.NORTH); // Add buttonPanel back to optionsPanel
        optionsPanel.revalidate(); // Revalidate the panel to update the layout
        optionsPanel.repaint(); // Repaint the panel

        JPanel ShiftsPanel = new JPanel(new GridLayout());



        JButton changeHoursButton = new JButton("Change hours");
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10); // Add spacing between buttons
//        changeHoursButton.addActionListener(new HrFrame.ButtonClickListener(i + 1));
        changeHoursButton.setPreferredSize(new Dimension(300, 50)); // Set preferred size for the button
        Dimension buttonSize = new Dimension(200, 40);
        changeHoursButton.setPreferredSize(buttonSize);
        JPanel newButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        newButtonPanel.add(changeHoursButton, BorderLayout.CENTER);
//        buttonPanel.add(changeHoursButton, gbc);

        JButton editJobButton = new JButton("Edit Job's count");
        buttonSize = new Dimension(200, 40);
        editJobButton.setPreferredSize(buttonSize);
        newButtonPanel.add(editJobButton, BorderLayout.CENTER);

        changeHoursButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeHoursPage(optionsPanel, buttonPanel, branch);
            }
        });
//        ShiftsPanel.add(changeHoursButton);

        editJobButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editJobPage(optionsPanel, buttonPanel, branch);
            }
        });

        ShiftsPanel.add(newButtonPanel);
        optionsPanel.add(ShiftsPanel);
    }

    private void editJobPage(JPanel optionsPanel, JPanel buttonPanel, int branch) {
        // Create the submit shifts panel
        JPanel editJobCountPanel = new JPanel(new GridLayout(0, 2));
        optionsPanel.removeAll(); // Remove all components from optionsPanel
        optionsPanel.add(buttonPanel, BorderLayout.NORTH); // Add buttonPanel back to optionsPanel
        optionsPanel.revalidate(); // Revalidate the panel to update the layout
        optionsPanel.repaint(); // Repaint the panel


        JLabel dateLabel = new JLabel("Enter the date of the shift you want (yyyy-mm-dd):");
        // Create the masked text field for date input
        MaskFormatter dateFormatter;
        try {
            dateFormatter = new MaskFormatter("####-##-##");
        } catch (ParseException e) {
            e.printStackTrace();
            return;
        }
        JFormattedTextField dateField = new JFormattedTextField(dateFormatter);
        dateField.setColumns(10);

        JLabel shiftLabel = new JLabel("Morning or Evening?");
        String[] shiftOptions = {"Morning", "Evening"};
        JComboBox<String> shiftComboBox = new JComboBox<>(shiftOptions);
        JButton submitButton = new JButton("Edit");


        // Create the list of jobs
        String[] jobs = {
                "Storekeeper",
                "Cashier",
                "Shift manager",
                "General employee",
                "Security",
                "Usher",
                "Cleaner"
        };

        // Create a JList with the job array
        JList<String> jobList = new JList<>(jobs);

        // Create a JScrollPane and add the JList to it
        JScrollPane scrollPane1 = new JScrollPane(jobList);


        // Add components to the submit shifts panel
        editJobCountPanel.add(dateLabel);
        editJobCountPanel.add(dateField);
        editJobCountPanel.add(shiftLabel);
        editJobCountPanel.add(shiftComboBox);


        // Create a JTextField for entering the employee ID
        JLabel countLabel = new JLabel("Count");
        JTextField countField = new JTextField();
        countField.setColumns(10);
        editJobCountPanel.add(countLabel);
        editJobCountPanel.add(countField);
        editJobCountPanel.add(scrollPane1);
        editJobCountPanel.add(submitButton);


        // ActionListener for the "Add" button
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedJob = jobList.getSelectedValue();
                String date = dateField.getText();
                String jobCount = countField.getText();
                int count = Integer.parseInt(jobCount);
                String shift = (String) shiftComboBox.getSelectedItem();
                try {
                    String shiftType;
                    if (shift.equals("Morning")) {
                        shiftType = "m";
                    } else {
                        shiftType = "e";
                    }
                    service.changeJobTypeCount(date, shift, getJobTypeFromJobName(selectedJob), branch, count);
                    JOptionPane.showMessageDialog(editJobCountPanel, "This shift has been changed", "Success", JOptionPane.PLAIN_MESSAGE);
                    countField.setText("");

                } catch (Exception exception) {
                    JOptionPane.showMessageDialog(editJobCountPanel, exception.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
                    countField.setText("");
                }

            }
        });


        JButton backButton2 = new JButton("Back");
        backButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editShiftsOption(optionsPanel, buttonPanel, branch);
            }
        });
        optionsPanel.add(editJobCountPanel);

    }

    private void changeHoursPage(JPanel optionsPanel, JPanel buttonPanel, int branch) {
        // Create the submit shifts panel
        JPanel editHoursPanel = new JPanel(new GridLayout(0, 2));
        optionsPanel.removeAll(); // Remove all components from optionsPanel
        optionsPanel.add(buttonPanel, BorderLayout.NORTH); // Add buttonPanel back to optionsPanel
        optionsPanel.revalidate(); // Revalidate the panel to update the layout
        optionsPanel.repaint(); // Repaint the panel


        JLabel dateLabel = new JLabel("Enter the date of the shift you want (yyyy-mm-dd):");
        // Create the masked text field for date input
        MaskFormatter dateFormatter;
        try {
            dateFormatter = new MaskFormatter("####-##-##");
        } catch (ParseException e) {
            e.printStackTrace();
            return;
        }
        JFormattedTextField dateField = new JFormattedTextField(dateFormatter);
        dateField.setColumns(10);

        JLabel shiftLabel = new JLabel("Morning or Evening?");
        String[] shiftOptions = {"Morning", "Evening"};
        JComboBox<String> shiftComboBox = new JComboBox<>(shiftOptions);
        JButton submitButton = new JButton("Edit");

        JLabel morningShiftEndLabel = new JLabel("Start Time:");
        JSpinner StartSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor eveningShiftStartEditor = new JSpinner.DateEditor(StartSpinner, "HH:mm");
        StartSpinner.setEditor(eveningShiftStartEditor);


        JLabel eveningShiftStartLabel = new JLabel("End Time:");
        JSpinner EndSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor morningShiftEndEditor = new JSpinner.DateEditor(EndSpinner, "HH:mm");
        EndSpinner.setEditor(morningShiftEndEditor);


// Set the default time for the Morning Shift Start Time
        Calendar morningShiftStartDefaultTime = Calendar.getInstance();
        morningShiftStartDefaultTime.set(Calendar.HOUR_OF_DAY, 15); // Set the desired hour
        morningShiftStartDefaultTime.set(Calendar.MINUTE, 0); // Set the desired minute
        StartSpinner.setValue(morningShiftStartDefaultTime.getTime());

// Set the default time for the Morning Shift End Time
        Calendar morningShiftEndDefaultTime = Calendar.getInstance();
        morningShiftEndDefaultTime.set(Calendar.HOUR_OF_DAY, 8); // Set the desired hour
        morningShiftEndDefaultTime.set(Calendar.MINUTE, 0); // Set the desired minute
        EndSpinner.setValue(morningShiftEndDefaultTime.getTime());


        // Add components to the submit shifts panel
        editHoursPanel.add(dateLabel);
        editHoursPanel.add(dateField);
        editHoursPanel.add(shiftLabel);
        editHoursPanel.add(shiftComboBox);
        editHoursPanel.add(morningShiftEndLabel);
        editHoursPanel.add(EndSpinner);
        editHoursPanel.add(eveningShiftStartLabel);
        editHoursPanel.add(StartSpinner);
        editHoursPanel.add(submitButton);

        // ActionListener for the "Add" button
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Date startTime = (Date) StartSpinner.getValue();
                LocalTime startTimeOfShiftValue = LocalDateTime.ofInstant(startTime.toInstant(), ZoneId.systemDefault()).toLocalTime();
                startTimeOfShift = startTimeOfShiftValue.format(DateTimeFormatter.ofPattern("HH:mm"));

                Date EndTime = (Date) EndSpinner.getValue();
                LocalTime endTimeOfShiftValue = LocalDateTime.ofInstant(EndTime.toInstant(), ZoneId.systemDefault()).toLocalTime();
                endTimeOfShift = endTimeOfShiftValue.format(DateTimeFormatter.ofPattern("HH:mm"));
                String[] hours = new String[]{startTimeOfShift, endTimeOfShift};

                date = dateField.getText();
                shift = (String) shiftComboBox.getSelectedItem();
                try {
                    String shiftType;
                    if (shift.equals("Morning")) {
                        shiftType = "m";
                    } else {
                        shiftType = "e";
                    }
                    service.changeShiftTime(date, shift, hours, branch);
                    JOptionPane.showMessageDialog(editHoursPanel, "This hours has been changed", "Success", JOptionPane.PLAIN_MESSAGE);
                } catch (Exception t) {
                    JOptionPane.showMessageDialog(editHoursPanel, t.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        JButton backButton2 = new JButton("Back");
        backButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editShiftsOption(optionsPanel, buttonPanel, branch);
            }
        });
        optionsPanel.add(editHoursPanel);

    }



    // Create the shift details page two
    private void editShiftsOption(JPanel optionsPanel, JPanel buttonPanel, int branch) {
        optionsPanel.removeAll(); // Remove all components from optionsPanel
        optionsPanel.add(buttonPanel, BorderLayout.NORTH); // Add buttonPanel back to optionsPanel
        optionsPanel.revalidate(); // Revalidate the panel to update the layout
        optionsPanel.repaint(); // Repaint the panel

        JPanel ShiftsPanel = new JPanel(new GridLayout(0,2));
        JButton removeButton = new JButton("Remove Employee");
        Dimension buttonSize = new Dimension(200, 40);
        removeButton.setPreferredSize(buttonSize);

        JPanel newButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        newButtonPanel.add(removeButton, BorderLayout.CENTER);

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createRemoveEmployeePage(optionsPanel, buttonPanel, branch);
            }
        });


        JButton addButton = new JButton("Add Employee");
        buttonSize = new Dimension(200, 40);
        addButton.setPreferredSize(buttonSize);
        newButtonPanel.add(addButton, BorderLayout.CENTER);
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createAddEmployeePage(optionsPanel, buttonPanel, branch);
            }
        });


        optionsPanel.add(newButtonPanel, BorderLayout.CENTER);
    }

    private void createRemoveEmployeePage(JPanel optionsPanel, JPanel buttonPanel, int branch) {
        // Create the submit shifts panel
        JPanel addEmployeePanel = new JPanel(new GridLayout(0, 1));
        optionsPanel.removeAll(); // Remove all components from optionsPanel
        optionsPanel.add(buttonPanel, BorderLayout.NORTH); // Add buttonPanel back to optionsPanel
        optionsPanel.revalidate(); // Revalidate the panel to update the layout
        optionsPanel.repaint(); // Repaint the panel

        // Create a constraints object to control component positioning
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 5, 5, 5); // Add some padding around components
        constraints.anchor = GridBagConstraints.WEST; // Center components horizontally and vertically


        JLabel dateLabel = new JLabel("Enter the date of the shift you want (yyyy-mm-dd):");
        // Create the masked text field for date input
        MaskFormatter dateFormatter;
        try {
            dateFormatter = new MaskFormatter("####-##-##");
        } catch (ParseException e) {
            e.printStackTrace();
            return;
        }
        JFormattedTextField dateField = new JFormattedTextField(dateFormatter);
//        dateField.setColumns(10);
        Dimension buttonSize = new Dimension(200, 40);
        dateField.setPreferredSize(buttonSize);
        JPanel newButtonPanel = new JPanel(new FlowLayout());
        newButtonPanel.add(dateLabel);
        newButtonPanel.add(dateField);
        constraints.gridx = 0;
        constraints.gridy = 0;
        addEmployeePanel.add(newButtonPanel, BorderLayout.CENTER);

        JLabel shiftLabel = new JLabel("Morning or Evening?");
        String[] shiftOptions = {"Morning", "Evening"};
        JComboBox<String> shiftComboBox = new JComboBox<>(shiftOptions);
        buttonSize = new Dimension(200, 40);
        shiftComboBox.setPreferredSize(buttonSize);
        newButtonPanel = new JPanel(new FlowLayout());
        newButtonPanel.add(shiftLabel);
        newButtonPanel.add(shiftComboBox);
        addEmployeePanel.add(newButtonPanel, BorderLayout.CENTER);

        JButton shiftOkButton = new JButton("OK");
        buttonSize = new Dimension(200, 40);
        shiftOkButton.setPreferredSize(buttonSize);
        newButtonPanel = new JPanel(new FlowLayout());
        newButtonPanel.add(shiftOkButton);
        addEmployeePanel.add(newButtonPanel, BorderLayout.CENTER);


        // Create a JTextField for entering the employee ID
        JLabel idLabel = new JLabel("Employee ID");
        JTextField employeeIDField = new JTextField();
        buttonSize = new Dimension(200, 40);
        employeeIDField.setPreferredSize(buttonSize);
        newButtonPanel = new JPanel(new FlowLayout());
        newButtonPanel.add(idLabel);
        newButtonPanel.add(employeeIDField);
        addEmployeePanel.add(newButtonPanel, BorderLayout.CENTER);

        JButton submitButton = new JButton("Remove");
        buttonSize = new Dimension(200, 40);
        submitButton.setPreferredSize(buttonSize);
        newButtonPanel = new JPanel(new FlowLayout());
        newButtonPanel.add(submitButton);
        addEmployeePanel.add(newButtonPanel, BorderLayout.CENTER);

//        scrollPane = null; // Declare the scrollPane variable outside the listener

        shiftOkButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
//                addEmployeePanel.remove(scrollPane);
                date = dateField.getText();
                shift = (String) shiftComboBox.getSelectedItem();
                try {
                    String shiftType;
                    if (shift.equals("Morning")) {
                        shiftType = "m";
                    } else {
                        shiftType = "e";
                    }
                    try {
                        List<String> availableEmployees = service.getEmployeesInShift(date, shiftType, branch);

                        // Create a JTextArea to display the available employees
                        JTextArea employeesTextArea = new JTextArea();
                        employeesTextArea.setEditable(false);

                        // Append the employee names and IDs to the JTextArea
                        for (String employeeInfo : availableEmployees) {
                            employeesTextArea.append(employeeInfo + "\n");
                        }

                        scrollPane = new JScrollPane(employeesTextArea);
                        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

                        addEmployeePanel.add(scrollPane);

                        scrollPane.setVisible(true); // Make the scroll pane visible
                        addEmployeePanel.revalidate();
                        addEmployeePanel.repaint();

                    } catch (RuntimeException exx) {
                        scrollPane.setVisible(false); // Make the scroll pane visible
                        JOptionPane.showMessageDialog(addEmployeePanel, exx.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (
                        Exception exception) {
                    scrollPane.setVisible(false); // Make the scroll pane visible
                    JOptionPane.showMessageDialog(addEmployeePanel, exception.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // ActionListener for the "Add" button
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String employeeID = employeeIDField.getText();
                String shiftType;
                if (shift.equals("Morning")) {
                    shiftType = "m";
                } else {
                    shiftType = "e";
                }
                // Call the add function with the provided employee ID
                try {
                    service.removeEmployeeFromShift(date, shiftType, employeeID, branch);
                    JOptionPane.showMessageDialog(addEmployeePanel, "This employee has been removed", "Success", JOptionPane.PLAIN_MESSAGE);
                    employeeIDField.setText("");
                    scrollPane.setVisible(false);
                } catch (Exception t) {
                    JOptionPane.showMessageDialog(addEmployeePanel, t.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
                    employeeIDField.setText("");

                }
            }
        });


        JButton backButton2 = new JButton("Back");
        backButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editShiftsOption(optionsPanel, buttonPanel, branch);
            }
        });
        optionsPanel.add(addEmployeePanel);
    }

    private void createAddEmployeePage(JPanel optionsPanel, JPanel buttonPanel, int branch) {
        // Create the submit shifts panel
        JPanel addEmployeePanel = new JPanel(new GridLayout(0, 2));
        optionsPanel.removeAll(); // Remove all components from optionsPanel
        optionsPanel.add(buttonPanel, BorderLayout.NORTH); // Add buttonPanel back to optionsPanel
        optionsPanel.revalidate(); // Revalidate the panel to update the layout
        optionsPanel.repaint(); // Repaint the panel


        JLabel dateLabel = new JLabel("Enter the date of the shift you want (yyyy-mm-dd):");
        // Create the masked text field for date input
        MaskFormatter dateFormatter;
        try {
            dateFormatter = new MaskFormatter("####-##-##");
        } catch (ParseException e) {
            e.printStackTrace();
            return;
        }
        JFormattedTextField dateField = new JFormattedTextField(dateFormatter);
        Dimension buttonSize = new Dimension(200, 40);
        dateField.setPreferredSize(buttonSize);
        JPanel newButtonPanel = new JPanel(new FlowLayout());
        newButtonPanel.add(dateLabel);
        newButtonPanel.add(dateField);
        addEmployeePanel.add(newButtonPanel, BorderLayout.CENTER);

        JLabel shiftLabel = new JLabel("Morning or Evening?");
        String[] shiftOptions = {"Morning", "Evening"};
        JComboBox<String> shiftComboBox = new JComboBox<>(shiftOptions);
        buttonSize = new Dimension(200, 40);
        shiftComboBox.setPreferredSize(buttonSize);
        newButtonPanel = new JPanel(new FlowLayout());
        newButtonPanel.add(shiftLabel);
        newButtonPanel.add(shiftComboBox);
        addEmployeePanel.add(newButtonPanel, BorderLayout.CENTER);




        // Create the list of jobs
        String[] jobs = {
                "Storekeeper",
                "Cashier",
                "Shift manager",
                "General employee",
                "Security",
                "Usher",
                "Cleaner"
        };

        // Create a JList with the job array
        JList<String> jobList = new JList<>(jobs);

        // Create a JScrollPane and add the JList to it
        JScrollPane scrollPane1 = new JScrollPane(jobList);
        buttonSize = new Dimension(200, 300);
        scrollPane1.setPreferredSize(buttonSize);
        newButtonPanel = new JPanel(new FlowLayout());
        newButtonPanel.add(scrollPane1);
        addEmployeePanel.add(newButtonPanel, BorderLayout.CENTER);

//        // Add components to the submit shifts panel
//        addEmployeePanel.add(dateLabel);
//        addEmployeePanel.add(dateField);
//        addEmployeePanel.add(shiftLabel);
//        addEmployeePanel.add(shiftComboBox);

//
//        addEmployeePanel.add(scrollPane1);
//        addEmployeePanel.add(submitButton);
        // Create a JTextField for entering the employee ID
        JLabel idLabel = new JLabel("Employee ID");
        JTextField employeeIDField = new JTextField();
//        employeeIDField.setColumns(10);
        buttonSize = new Dimension(200, 40);
        employeeIDField.setPreferredSize(buttonSize);
        newButtonPanel = new JPanel(new FlowLayout());
        newButtonPanel.add(idLabel, BorderLayout.SOUTH);
        newButtonPanel.add(employeeIDField, BorderLayout.SOUTH);
        addEmployeePanel.add(newButtonPanel, BorderLayout.SOUTH);

//        addEmployeePanel.add(idLabel);
//        addEmployeePanel.add(employeeIDField);

        JButton submitButton = new JButton("Add");
        buttonSize = new Dimension(200, 40);
        submitButton.setPreferredSize(buttonSize);
        newButtonPanel = new JPanel(new FlowLayout());
        newButtonPanel.add(submitButton, BorderLayout.SOUTH);
        addEmployeePanel.add(newButtonPanel, BorderLayout.SOUTH);

        //scrollPane = null; // Declare the scrollPane variable outside the listener

        // ActionListener for the submit button
        jobList.addListSelectionListener(new ListSelectionListener() {
            boolean scrollPaneAdded = false; // Flag to track whether the scroll pane has been added or not
            JScrollPane scrollPane; // Declare the scrollPane variable here

            @Override
            public void valueChanged(ListSelectionEvent e) {
                selectedJob = jobList.getSelectedValue();
                String date = dateField.getText();
                String shift = (String) shiftComboBox.getSelectedItem();
                try {
                    String shiftType;
                    if (shift.equals("Morning")) {
                        shiftType = "m";
                    } else {
                        shiftType = "e";
                    }

                    if (selectedJob != null) {
                        try {
                            returnValues = service.checkJobToAdd(date, shiftType, getJobTypeFromJobName(selectedJob), branch);
                            List<String> availableEmployees = service.getAvailableEmployeesForJobType(date, shiftType, getJobTypeFromJobName(selectedJob), branch);

                            // Create a JTextArea to display the available employees
                            JTextArea employeesTextArea = new JTextArea();
                            employeesTextArea.setEditable(false);

                            // Append the employee names and IDs to the JTextArea
                            for (String employeeInfo : availableEmployees) {
                                employeesTextArea.append(employeeInfo + "\n");
                            }

                            // Create a new scroll pane only if it hasn't been added before
                            if (!scrollPaneAdded) {
                                scrollPane = new JScrollPane(employeesTextArea);
                                scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                                addEmployeePanel.add(scrollPane, BorderLayout.CENTER);
                                scrollPaneAdded = true; // Set the flag to true
                            } else {
                                // If the scroll pane has been added, update its viewport with the new JTextArea
                                scrollPane.setViewportView(employeesTextArea);
                            }
                            scrollPane.setVisible(true); // Make the scroll pane visible
                            addEmployeePanel.revalidate();
                            addEmployeePanel.repaint();

                        } catch (RuntimeException exx) {
                            if (scrollPane != null) {
                                scrollPane.setVisible(false); // Make the scroll pane invisible if it exists
                            }
                            JOptionPane.showMessageDialog(addEmployeePanel, exx.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } catch (Exception exception) {
                    if (scrollPane != null) {
                        scrollPane.setVisible(false); // Make the scroll pane invisible if it exists
                    }
                    JOptionPane.showMessageDialog(addEmployeePanel, exception.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // ActionListener for the "Add" button
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String employeeID = employeeIDField.getText();
                // Call the add function with the provided employee ID
                try {
                    service.addEmployeeToShift(employeeID, returnValues.getValue(), returnValues.getKey(), branch);
                    JOptionPane.showMessageDialog(addEmployeePanel, "This employee has been added", "Success", JOptionPane.PLAIN_MESSAGE);
                    employeeIDField.setText("");
                    if (scrollPane != null) {
                        scrollPane.setVisible(false); // Make the scroll pane invisible if it exists
                    }
                } catch (Exception t) {
                    JOptionPane.showMessageDialog(addEmployeePanel, t.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
                    employeeIDField.setText("");

                }
            }
        });


        JButton backButton2 = new JButton("Back");
        backButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editShiftsOption(optionsPanel, buttonPanel, branch);
            }
        });

        optionsPanel.add(addEmployeePanel);

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

    // Helper method to retrieve job type based on job name
    private String getJobTypeFromJobName(String jobName) {
        if (jobName.equals("Shift manager")) {
            return "3";
        } else if (jobName.equals("Storekeeper")) {
            return "1";
        } else if (jobName.equals("Cashier")) {
            return "2";
        } else if (jobName.equals("Usher")) {
            return "6";
        } else if (jobName.equals("Cleaner")) {
            // Add Cleaner job authorization
            return "7";
        } else if (jobName.equals("Security")) {
            return "5";
        } else if (jobName.equals("General employee")) {
            return "4";
        }
        return "";
    }


    //    public class MultiLineTableCellRenderer extends DefaultTableCellRenderer {
//
//        public MultiLineTableCellRenderer() {
//            setOpaque(true); // Make sure renderer is opaque
//
//        }
//
//        @Override
//        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
//            JTextArea textArea = new JTextArea();
//            textArea.setText((String) value);
//            textArea.setLineWrap(true);
//            textArea.setWrapStyleWord(true);
//            textArea.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8)); // Add padding to text area
//
//            if (isSelected) {
//                textArea.setBackground(table.getSelectionBackground());
//                textArea.setForeground(table.getSelectionForeground());
//            } else {
//                textArea.setBackground(table.getBackground());
//                textArea.setForeground(table.getForeground());
//            }
//
//            return textArea;
//        }
//    }
// Custom cell renderer to adjust the height of each cell
    class CustomTableCellRenderer extends DefaultTableCellRenderer {
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            // Set the height of each cell
            if (table.getRowHeight(row) < cellComponent.getPreferredSize().height) {
                table.setRowHeight(row, cellComponent.getPreferredSize().height);
            }

            return cellComponent;
        }
    }
}
