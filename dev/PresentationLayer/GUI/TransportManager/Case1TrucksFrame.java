package PresentationLayer.GUI.TransportManager;

import BuisnessLayer.TransportManager.ATruck;
import BuisnessLayer.TransportManager.HelperFunctions;
import BuisnessLayer.TransportManager.TransportOrder;
import BuisnessLayer.TransportManager.TruckStatus;
import ServiceLayer.TransportManager.TransportService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;


public class Case1TrucksFrame extends JFrame implements ActionListener {

    private JPanel cards;
    private CardLayout cardLayout;
    private JPanel buttonPanel;
    private HelperFunctions helperFunctions;
    private TransportService transportService;

    private TransportFrameMenu transportFramemenu;


    public Case1TrucksFrame(TransportFrameMenu transportFrameMenu) {

        try {
            transportFramemenu = transportFrameMenu;
            transportService = new TransportService();
            helperFunctions = new HelperFunctions();
        }
        catch (Exception e){
            System.out.println("Error in Case1TrucksFrame " + e.getMessage());
        }

        // Set up the JFrame
        setTitle("Truck Form");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 1000);

        cards = new JPanel();
        cards.setLayout(new BorderLayout());

        // Create the layered pane
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(getWidth(), getHeight()));

        // Create the background image label
        ImageIcon backgroundImage = new ImageIcon("background.jpg"); // Replace "transportMenu.jpg" with your actual image file path
        JLabel backgroundLabel = new JLabel(backgroundImage);
        backgroundLabel.setBounds(0, 0, getWidth(), getHeight());

        // Create the "Hello HR" label
        JLabel helloLabel = new JLabel("<html><h1>Please choose what you like to do : </h1></html>");
        helloLabel.setBounds(0, 100, getWidth(), getHeight());
        helloLabel.setHorizontalAlignment(JLabel.CENTER);
        helloLabel.setVerticalAlignment(JLabel.TOP);

        // Create the button panel
        buttonPanel = new JPanel(new GridBagLayout());
        createMainMenuCard(transportFrameMenu);
        buttonPanel.setOpaque(false); // Make the panel transparent
        buttonPanel.setBounds(0, 0, getWidth(), getHeight());

        // Create the card layout and panel
        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);
        getContentPane().add(cards, BorderLayout.CENTER);

        // Create the main menu card
        createMainMenuCard(transportFrameMenu);

        // Create the cards for different views
        JPanel addTruckCard = createAddTruckCard(transportFrameMenu);
        JPanel removeTruckCard = createRemoveTruckCard(transportFrameMenu);
        JPanel printTrucksCard = createPrintTrucksCard(transportFrameMenu);
        JPanel changeStatusCard = createChangeStatusCard(transportFrameMenu);

        // Add the components to the layered pane
        layeredPane.add(backgroundLabel, new Integer(0));
        layeredPane.add(helloLabel, new Integer(1));
        layeredPane.add(buttonPanel, new Integer(2));

        // Add the cards to the card layout
        //cards.add(mainMenuCard, "Main Menu");
        cards.add(layeredPane, BorderLayout.CENTER);
        cards.add(addTruckCard, "Add Truck");
        cards.add(removeTruckCard, "Remove Truck");
        cards.add(printTrucksCard, "Print Trucks");
        cards.add(changeStatusCard, "Change Truck Status");

        // Add the main panel to the frame
        add(cards);

        setVisible(true);
    }

    private void createMainMenuCard(TransportFrameMenu transportFrameMenu) {
        String[] buttonLabels = {"Add Truck",
                "Remove Truck",
                "Print Trucks",
                "Change Truck Status",
                "Return to Main Menu"};

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10); // Add spacing between buttons

        for (int i = 0; i < buttonLabels.length; i++) {
            JButton button = new JButton(buttonLabels[i]);
            button.addActionListener(new Case1TrucksFrame.ButtonClickListener(i + 1));
            button.setPreferredSize(new Dimension(300, 50)); // Set preferred size for the button
            buttonPanel.add(button, gbc);
            gbc.gridy++;
        }
    }
    public class ButtonClickListener implements ActionListener {
        private int buttonNumber;

        public ButtonClickListener(int buttonNumber) {
            this.buttonNumber = buttonNumber;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            // Open a new frame or panel based on the button clicked
            // For demonstration purposes, this example simply shows a message dialog
            switch (buttonNumber) {
                case 1:
                    // Switch to the panel of Case1TrucksFrame
                    cardLayout.show(cards, "Add Truck");
                    break;
                case 2:
                    // Handle button 2 action
                    cardLayout.show(cards, "Remove Truck");
                    break;
                case 3:
                    cardLayout.show(cards, "Print Trucks");
                    // Handle button 3 action
                    break;
                case 4:
                    cardLayout.show(cards, "Change Truck Status");
                    // Handle button 4 action
                    break;
                case 5:
                    transportFramemenu.setVisible(true); // Show the TransportFrameMenu
                    dispose(); // Close the current frame (Case1TrucksFrame)
                    // Handle button 5 action
                    break;
            }
        }
    }

    private JPanel createAddTruckCard(TransportFrameMenu transportFrameMenu) {
        JPanel addTruckPanel = new JPanel();
        addTruckPanel.setLayout(new GridBagLayout()); // Use GridBagLayout for more control

        // Create the background image label
        ImageIcon backgroundImage = new ImageIcon("background.jpg"); // Replace "background.jpg" with your actual image file path
        JLabel backgroundLabel = new JLabel(backgroundImage);
        backgroundLabel.setBounds(0, 0, getWidth(), getHeight());

        // Create a constraints object to control component positioning
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(30, 30, 30, 30); // Add some padding around components
        constraints.anchor = GridBagConstraints.WEST; // Center components horizontally and vertically

        // License Plate Number
        JLabel lblLicensePlate = new JLabel("License Plate Number:");
        lblLicensePlate.setFont(lblLicensePlate.getFont().deriveFont(24.0f));
        JTextField txtLicensePlate = new JTextField(18);
        constraints.gridx = 0; // Column 0
        constraints.gridy = 0; // Row 0
        addTruckPanel.add(lblLicensePlate, constraints);
        txtLicensePlate.setPreferredSize(new Dimension(200, txtLicensePlate.getPreferredSize().height)); // Adjust the width and height
        constraints.gridx = 1; // Column 1
        addTruckPanel.add(txtLicensePlate, constraints);


        // Truck's Net Weight
        JLabel lblNetWeight = new JLabel("Truck's Net Weight:");
        lblNetWeight.setFont(lblLicensePlate.getFont().deriveFont(24.0f));
        JComboBox<String> cmbNetWeight = new JComboBox<>(new String[]{"1500", "2250", "3000"});
        cmbNetWeight.setFont(cmbNetWeight.getFont().deriveFont(16.0f)); // Adjust the font size (e.g., 16.0f)
        cmbNetWeight.setPreferredSize(new Dimension(200, cmbNetWeight.getPreferredSize().height)); // Adjust the preferred width
        constraints.gridx = 0; // Column 1
        constraints.gridy = 1; // Row 0
        addTruckPanel.add(lblNetWeight, constraints);
        constraints.gridx = 1; // Row 1
        addTruckPanel.add(cmbNetWeight, constraints);

        // Max Cargo Weight
        JLabel lblCargoWeight = new JLabel("Max Cargo Weight:");
        lblCargoWeight.setFont(lblLicensePlate.getFont().deriveFont(24.0f));
        JComboBox<String> cmbCargoWeight = new JComboBox<>(new String[]{"1000", "1500", "2000"});
        cmbCargoWeight.setFont(cmbCargoWeight.getFont().deriveFont(16.0f)); // Adjust the font size (e.g., 16.0f)
        cmbCargoWeight.setPreferredSize(new Dimension(200, cmbCargoWeight.getPreferredSize().height));
        constraints.gridx = 0; // Row 2
        constraints.gridy = 2; // Row 2
        addTruckPanel.add(lblCargoWeight, constraints);
        constraints.gridx = 1; // Row 3
        addTruckPanel.add(cmbCargoWeight, constraints);

        // Truck Level by Type
        JLabel lblTruckLevel = new JLabel("Truck Level by Type:");
        lblTruckLevel.setFont(lblLicensePlate.getFont().deriveFont(24.0f));
        JComboBox<String> cmbTruckLevel = new JComboBox<>(new String[]{"Dry", "Cooler", "Freezer"});
        cmbTruckLevel.setFont(cmbTruckLevel.getFont().deriveFont(16.0f)); // Adjust the font size (e.g., 16.0f)
        cmbTruckLevel.setPreferredSize(new Dimension(200, cmbTruckLevel.getPreferredSize().height));
        constraints.gridx = 0; // Row 2
        constraints.gridy = 3; // Row 2
        addTruckPanel.add(lblTruckLevel, constraints);
        constraints.gridx = 1; // Row 5
        addTruckPanel.add(cmbTruckLevel, constraints);

        // Add Truck Button
        JButton btnAddTruck = new JButton("Add Truck");
        btnAddTruck.setFont(btnAddTruck.getFont().deriveFont(16.0f)); // Adjust the font size (e.g., 16.0f)
        btnAddTruck.setPreferredSize(new Dimension(520, 35)); // Adjust the preferred size (width: 200, height: 50)
        constraints.gridx = 0; // Row 2
        constraints.gridy = 4; // Row 2
        constraints.gridwidth = 3; // Span across two columns
        constraints.anchor = GridBagConstraints.CENTER; // Center the button horizontally
        addTruckPanel.add(btnAddTruck, constraints);
        btnAddTruck.setEnabled(false);

        // Add KeyListener to the txtLicensePlate
        txtLicensePlate.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String licenseText = txtLicensePlate.getText();
                boolean isValid = checkLicenseFormat(licenseText);
                btnAddTruck.setEnabled(isValid);
            }
        });

        // Add action listener to the button
        btnAddTruck.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                int licensePlateNumber=-1;
                int netWeightNumber=-1;
                int cargoWeightNumber=-1;
                int truckLevelNumber=-1 ;

                String licensePlate = txtLicensePlate.getText();
                licensePlateNumber = Integer.parseInt(licensePlate);

                String netWeight = (String) cmbNetWeight.getSelectedItem();
                netWeightNumber = Integer.parseInt(netWeight);

                String cargoWeight = (String) cmbCargoWeight.getSelectedItem();
                cargoWeightNumber = Integer.parseInt(cargoWeight);

                String truckLevel = (String) cmbTruckLevel.getSelectedItem();
                switch (truckLevel) {
                    case "Dry":
                        truckLevelNumber = 1;
                        break;
                    case "Cooler":
                        truckLevelNumber = 2;
                        break;
                    case "Freezer":
                        truckLevelNumber=3;
                }

                // Call the function in the controller to check if the license plate already exists
                boolean plateExists = transportService.check_if_truck_plate_number_exist(licensePlateNumber);

                if (plateExists) {
                    String errorMessage = "License Plate Number already exists.";
                    JOptionPane.showMessageDialog(addTruckPanel, errorMessage, "Plate Exists", JOptionPane.ERROR_MESSAGE);
                    txtLicensePlate.setText("");
                } else {
                    // Proceed with adding the truck or perform appropriate actions
                    transportService.add_Truck(licensePlateNumber, netWeightNumber, cargoWeightNumber, truckLevelNumber);

                    String successMessage = "Truck added successfully!";
                    JOptionPane.showMessageDialog(addTruckPanel, successMessage, "Success", JOptionPane.INFORMATION_MESSAGE);

                    transportFrameMenu.setVisible(true);
                    dispose();
                }

            }
        });

        // Button to return to the main menu
        JButton returnButton = new JButton("Return to Main Menu");
        returnButton.setFont(btnAddTruck.getFont().deriveFont(16.0f)); // Adjust the font size (e.g., 16.0f)
        returnButton.setPreferredSize(new Dimension(520, 35)); // Adjust the preferred size (width: 200, height: 50)
        constraints.gridx = 0; // Column 0
        constraints.gridy = 5; // Row 6
        constraints.gridwidth = 3; // Span across two columns
        constraints.anchor = GridBagConstraints.CENTER; // Center the button horizontally
        addTruckPanel.add(returnButton, constraints);
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                transportFramemenu.setVisible(true);
                dispose();
            }
        });

        Color colorNested = new Color(140, 80, 200);
        Color colorTotal = new Color(175, 80, 200);
        addTruckPanel.setBackground(colorTotal);

        // Create a nested panel to center the addTruckPanel horizontally
        JPanel nestedPanel = new JPanel(new GridBagLayout());
        nestedPanel.setBackground(colorNested);
        nestedPanel.add(addTruckPanel);

        // Create the final panel with GridBagLayout to center the nested panel vertically
        JPanel finalPanel = new JPanel(new GridBagLayout());
        finalPanel.setBackground(colorNested);
        finalPanel.add(nestedPanel);

        return finalPanel;

    }

    private JPanel createRemoveTruckCard(TransportFrameMenu transportFrameMenu) {
        Color colorNested = new Color(140, 80, 200);
        Color colorTotal = new Color(175, 80, 200);

        JPanel removeTruckPanel = new JPanel(new BorderLayout());
        removeTruckPanel.setBackground(colorTotal);

        JPanel checkboxPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(20, 20, 20, 20);
        constraints.anchor = GridBagConstraints.WEST;

        HashMap<Integer, ATruck> truckMap = transportService.get_all_trucks();
        // Create checkboxes for each truck in the truckMap
        ButtonGroup checkboxGroup = new ButtonGroup();
        int row = 0; // Row number for the checkboxes
        for (int truckId : truckMap.keySet()) {
            ATruck truck = truckMap.get(truckId);
            JRadioButton radioButton = new JRadioButton(truck.toString());
            radioButton.setFont(radioButton.getFont().deriveFont(13f)); // Increase the font size of the label to 16
            checkboxGroup.add(radioButton);

            constraints.gridy = row; // Set the row number for the checkbox
            checkboxPanel.add(radioButton, constraints);
            row++; // Increment the row number
        }

        JScrollPane scrollPane = new JScrollPane(checkboxPanel);
        removeTruckPanel.add(scrollPane, BorderLayout.CENTER);

        JButton removeButton = new JButton("Remove");
        removeButton.setEnabled(false);

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the selected truck and remove it
                for (Component component : checkboxPanel.getComponents()) {
                    if (component instanceof JRadioButton) {
                        JRadioButton radioButton = (JRadioButton) component;
                        if (radioButton.isSelected()) {
                            String truckLabelText = radioButton.getText();
                            String truckIdString = truckLabelText.split(":")[1].trim();

                            // Remove any non-digit characters from the truck ID string
                            truckIdString = truckIdString.replaceAll("[^0-9]", "");

                            int truckId = Integer.parseInt(truckIdString);
                            transportService.remove_Truck(truckId);

                            // Show a message or perform additional actions
                            JOptionPane.showMessageDialog(removeTruckPanel, "Truck removed successfully");

                            transportFrameMenu.setVisible(true);
                            dispose();
                            break;
                        }
                    }
                }
            }

        });

        // Add an ActionListener to the radio buttons
        for (Component component : checkboxPanel.getComponents()) {
            if (component instanceof JRadioButton) {
                JRadioButton radioButton = (JRadioButton) component;
                radioButton.addActionListener(e -> {
                    removeButton.setEnabled(true);
                });
            }
        }

        // Create a panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER)); // Center-align the buttons
        buttonPanel.add(removeButton);
        // Create the "Return to Main Menu" button
        JButton returnButton = new JButton("Return to Main Menu");
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                transportFrameMenu.setVisible(true);
                dispose();
            }
        });
        buttonPanel.add(returnButton);
        // Set the background color for the nestedPanel and finalPanel
        JPanel nestedPanel = new JPanel(new GridBagLayout());
        nestedPanel.setBackground(colorNested);
        nestedPanel.add(checkboxPanel);

        JPanel finalPanel = new JPanel(new GridBagLayout());
        finalPanel.setBackground(colorNested);
        finalPanel.add(nestedPanel);

        removeTruckPanel.setBackground(colorTotal);
        checkboxPanel.setBackground(colorTotal);

        removeTruckPanel.add(finalPanel, BorderLayout.CENTER);
        removeTruckPanel.add(buttonPanel, BorderLayout.SOUTH);

        return removeTruckPanel;
    }

    private JPanel createPrintTrucksCard(TransportFrameMenu transportFrameMenu) {
        JPanel printTrucksPanel = new JPanel();
        printTrucksPanel.setLayout(new BorderLayout());
        Color colorNested = new Color(140, 80, 200);
        Color colorTotal = new Color(175, 80, 200);

        // Fetch all trucks using transportService.get_all_trucks()
        HashMap<Integer, ATruck> allTrucks = transportService.get_all_trucks();

        // Create a DefaultListModel to store the truck information
        DefaultListModel<String> truckListModel = new DefaultListModel<>();

        // Add the truck information to the list model
        for (HashMap.Entry<Integer, ATruck> entry : allTrucks.entrySet()) {
            int licensePlateNumber = entry.getKey();
            ATruck truck = entry.getValue();
            truckListModel.addElement(truck.toString());
        }

        // Create a JList using the list model
        JList<String> truckList = new JList<>(truckListModel);

        // Set the font size and spacing between rows
        truckList.setFont(truckList.getFont().deriveFont(13f)); // Increase the font size to 16
        truckList.setFixedCellHeight(85); // Increase the height of each row
        truckList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Create a scroll pane to contain the JList
        JScrollPane scrollPane = new JScrollPane(truckList);

        // Center-align the truck list vertically
        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.weighty = 1.0; // Set the vertical weight to make it expand vertically
        centerPanel.add(scrollPane, constraints);

        // Add the center panel to the main panel
        printTrucksPanel.add(centerPanel, BorderLayout.CENTER);

        // Button to return to the main menu
        JButton returnButton = new JButton("Return to Main Menu");
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                transportFrameMenu.setVisible(true);
                dispose();
            }
        });
        // Create a panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER)); // Center-align the buttons
        buttonPanel.add(returnButton);

        printTrucksPanel.add(returnButton, BorderLayout.SOUTH);
        printTrucksPanel.setBackground(Color.WHITE);
        centerPanel.setBackground(Color.WHITE);
        return printTrucksPanel;
    }


    private JPanel createChangeStatusCard(TransportFrameMenu transportFrameMenu) {
        JPanel changeStatusPanel = new JPanel();
        changeStatusPanel.setLayout(new BorderLayout());

        // Create a panel for the checkboxes
        JPanel checkboxPanel = new JPanel();
        checkboxPanel.setLayout(new GridBagLayout()); // Use GridBagLayout for more control
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(20, 20, 20, 20); // Add some padding around checkboxes
        constraints.anchor = GridBagConstraints.CENTER; // Center components horizontally and vertically

        // Fetch all trucks using transportService.get_all_trucks()
        HashMap<Integer, ATruck> allTrucks = transportService.get_all_trucks();
        JButton changeStatusButton = new JButton("Change Status");
        int row = 0;
        ButtonGroup checkboxGroup = new ButtonGroup(); // Create ButtonGroup for exclusive selection

        // Create a checkbox for each truck in the truckMap
        for (int truckID : allTrucks.keySet()) {
            ATruck truck = allTrucks.get(truckID);
            int licensePlateNumber = truck.getLicense_plate_number();
            String status = truck.getStatus().toString();

            JRadioButton radioButton = new JRadioButton("Truck License Plate Number: " + licensePlateNumber + ", current status is: " + status);
            radioButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    changeStatusButton.setEnabled(true);
                }
            });
            constraints.gridy = row; // Set the row number for the checkbox
            checkboxPanel.add(radioButton, constraints);
            checkboxGroup.add(radioButton); // Add checkbox to the ButtonGroup
            row++; // Increment the row number
        }

        // Create a panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER)); // Center-align the buttons

        // Button to change the truck status
        changeStatusButton.setEnabled(false); // Disable initially
        changeStatusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean truckSelected = false;
                for (Component component : checkboxPanel.getComponents()) {
                    if (component instanceof JRadioButton) {
                        JRadioButton radioButton = (JRadioButton) component;

                        if (radioButton.isSelected()) {
                            // Extract the substring and convert it to an integer
                            int startIndex = radioButton.getText().indexOf(":") + 2;
                            int endIndex = radioButton.getText().indexOf(",");
                            String substring = radioButton.getText().substring(startIndex, endIndex).trim();
                            int licensePlateNumber = Integer.parseInt(substring);

                            boolean plateNumberExists = transportService.check_if_truck_plate_number_exist(licensePlateNumber);
                            if (!plateNumberExists) {
                                JOptionPane.showMessageDialog(changeStatusPanel, "License plate number is incorrect!", "Error", JOptionPane.ERROR_MESSAGE);
                                return;
                            }

                            boolean canChangeStatus = transportService.switch_truck_status_controller(licensePlateNumber);
                            if (!canChangeStatus) {
                                JOptionPane.showMessageDialog(changeStatusPanel, "You cannot change the status of a truck with future transports.", "Error", JOptionPane.ERROR_MESSAGE);
                                return;
                            }

                            ATruck truck = transportService.get_truck(licensePlateNumber);
                            String newStatus = truck.getStatus().toString();

                            JOptionPane.showMessageDialog(changeStatusPanel, "Truck status changed to " + newStatus);
                            truckSelected = true;
                            break; // Exit the loop since a truck is selected
                        }
                    }
                }

                if (!truckSelected) {
                    JOptionPane.showMessageDialog(changeStatusPanel, "Please choose a truck first");
                }

                transportFrameMenu.setVisible(true);
                dispose();
            }
        });
        changeStatusPanel.add(changeStatusButton, BorderLayout.SOUTH);


        // Button to return to the main menu
        JButton returnButton = new JButton("Return to Main Menu");
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                transportFrameMenu.setVisible(true);
                dispose();
            }
        });

        checkboxPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add some padding to the checkbox panel

        changeStatusPanel.add(new JScrollPane(checkboxPanel), BorderLayout.CENTER);
        buttonPanel.add(changeStatusButton);
        buttonPanel.add(returnButton);
        changeStatusPanel.add(buttonPanel, BorderLayout.SOUTH);

        return changeStatusPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();

        switch (actionCommand) {
            case "Add Truck":
                cardLayout.show(cards, "Add Truck");
                break;
            case "Remove Truck":
                cardLayout.show(cards, "Remove Truck");
                break;
            case "Print Trucks":
                cardLayout.show(cards, "Print Trucks");
                break;
            case "Change Truck Status":
                cardLayout.show(cards, "Change truck status");
                break;
        }
    }

    // Helper method to check if a date string matches a specific format
    private boolean checkLicenseFormat(String licenseText) {
        try {
            Integer.parseInt(licenseText);
            return licenseText.length()==7;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
