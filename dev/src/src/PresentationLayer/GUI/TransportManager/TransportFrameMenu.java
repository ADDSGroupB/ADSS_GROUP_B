package PresentationLayer.GUI.TransportManager;

import BuisnessLayer.TransportManager.TransportController;
import BuisnessLayer.TransportManager.TransportOrder;
import BuisnessLayer.TransportManager.TransportShipment;
import ServiceLayer.TransportManager.TransportService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

public class TransportFrameMenu extends JFrame {

    private JPanel mainPanel;
    private JPanel buttonPanel;
    private TransportService transportService;

    public TransportFrameMenu() {
        TransportFrameMenuOption();
    }

    public void TransportFrameMenuOption() {
        transportService = new TransportService();

        setTitle("TransportManager Form");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 1000);

        // Create the main panel with a layered pane
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // Create the layered pane
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(getWidth(), getHeight()));

        // Create the background image label
        ImageIcon backgroundImage = new ImageIcon("background.jpg"); // Replace "transportMenu.jpg" with your actual image file path
        JLabel backgroundLabel = new JLabel(backgroundImage);
        backgroundLabel.setBounds(0, 0, getWidth(), getHeight());

        // Create the "Hello HR" label
        JLabel helloLabel = new JLabel("<html><h1>Hello Manager</h1></html>");
        helloLabel.setBounds(0, 70, getWidth(), getHeight());
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

        // Resize and add the image under the last button in the button panel
        ImageIcon managerImageIcon = new ImageIcon("manager1.jpg"); // Replace with the actual image file path
        Image managerImage = managerImageIcon.getImage();

        int desiredWidth = 300; // Set the desired width of the image
        int desiredHeight = 220; // Set the desired height of the image

        // Resize the image
        Image resizedManagerImage = managerImage.getScaledInstance(desiredWidth, desiredHeight, Image.SCALE_SMOOTH);

        // Create the resized image icon
        ImageIcon resizedManagerImageIcon = new ImageIcon(resizedManagerImage);

        // Create the label with the resized image icon
        JLabel managerLabel = new JLabel(resizedManagerImageIcon);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0; // Column 0
        constraints.gridy = 10; // Row 5
        constraints.gridwidth = 2; // Span across two columns
        constraints.anchor = GridBagConstraints.CENTER; // Center the image horizontally
        buttonPanel.add(managerLabel, constraints);

        // Add the layered pane to the main panel
        mainPanel.add(layeredPane, BorderLayout.CENTER);

        // Add the main panel to the frame
        add(mainPanel);

        setVisible(true);

    }

    private void addButtons() {

        String[] buttonLabels = {"<html>Add or Remove a truck or Print all<br/>all trucks or change truck status</html>",
                "Show details about the transportations",
                "Enter a new order from supplier",
                "Create a new Transport Document",
                "Execute Transport",
                "Manage active  transport shipments", "Exit"};

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
                    new Case1TrucksFrame(TransportFrameMenu.this); // Open the Case1TrucksFrame
                    dispose(); // Close the current frame
                    break;
                case 2:
                    // Handle button 2 action
                    new Case2PrintDetailsFrame(TransportFrameMenu.this);
                    dispose();
                    break;
                case 3:
                    new Case3NewOrderFrame(TransportFrameMenu.this);
                    dispose();
                    // Handle button 3 action
                    break;
                case 4:
                    ArrayList<TransportOrder> allOrders = transportService.getAll_unsigned_transport_orders();
                    if (allOrders.isEmpty()) {
                        showMessage("There are no transport orders awaiting, so you can't create a new transport document");
                        return;
                    }
                    else {
                        new Case4NewTransportDocumentFrame(TransportFrameMenu.this);
                        dispose();
                    }
                    // Handle button 4 action
                    break;
                case 5:
                    int sum_of_transport_controller = transportService.get_num_of_waiting_transport_documents();
                    if (sum_of_transport_controller == 0 || sum_of_transport_controller == -1) {
                        // Alert the user that there aren't any transport documents waiting to be executed
                        showMessage("There aren't any transport documents waiting to be executed");
                    }
                    else {
                        new Case5ExecuteTransportFrame(TransportFrameMenu.this);
                        dispose();
                    }
                    // Handle button 5 action
                    break;
                case 6:
                    HashMap<Integer, TransportShipment> total_active_transports = transportService.getAll_active_transports();
                    if (total_active_transports.isEmpty()) {
                        // Alert the user that there aren't any transport documents waiting to be executed
                        showMessage("There aren't any active transport to manage ");
                    }
                    else {
                        new Case6ManageActiveTransportFrame(TransportFrameMenu.this);
                        dispose();
                    }
                    // Handle button 6 action
                    break;
                case 7:
                    // Handle button 7 action
                    System.exit(0);
                    break;
            }
        }
    }
        private void showMessage(String message) {
            int result = JOptionPane.showOptionDialog(this, message, "Message", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new Object[]{"OK"}, null);
            if (result == JOptionPane.OK_OPTION) {
                setVisible(true); // Show the TransportFrameMenu
            }
        }

}
