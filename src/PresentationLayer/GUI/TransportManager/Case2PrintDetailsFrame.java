package PresentationLayer.GUI.TransportManager;

import BuisnessLayer.TransportManager.*;
import ServiceLayer.TransportManager.TransportService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;


public class Case2PrintDetailsFrame extends JFrame implements ActionListener {

    private JPanel cards;
    private JPanel buttonPanel;
    private CardLayout cardLayout;
    private TransportService transportService;
    private TransportFrameMenu transportFramemenu;


    public Case2PrintDetailsFrame(TransportFrameMenu transportFrameMenu) {
        // Set up the JFrame
        try {
            transportFramemenu = transportFrameMenu;
            transportService = new TransportService();
        }
        catch (Exception e){
            System.out.println("Error in Case1TrucksFrame " + e.getMessage());
        }
        setTitle("Print Details");
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
        helloLabel.setBounds(0, 130, getWidth(), getHeight());
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
        JPanel printActiveTransportsCard = createPrintActiveTransportsCard(transportFrameMenu);
        JPanel printFormerTransportDocumentsCard = createPrintFormerTransportDocumentsCard(transportFrameMenu);
        JPanel printTransportDocumentsCard = createPrintTransportDocumentsCard(transportFrameMenu);
        JPanel printTransportOrdersCard = createPrintTransportOrdersCard(transportFrameMenu);

        // Add the components to the layered pane
        layeredPane.add(backgroundLabel, new Integer(0));
        layeredPane.add(helloLabel, new Integer(1));
        layeredPane.add(buttonPanel, new Integer(2));

        // Add the cards to the card layout
        //cards.add(mainMenuCard, "Main Menu");
        cards.add(layeredPane, BorderLayout.CENTER);
        cards.add(printActiveTransportsCard, "Print Active Transports");
        cards.add(printFormerTransportDocumentsCard, "Print Former Transport Documents");
        cards.add(printTransportDocumentsCard, "Print Transport Documents");
        cards.add(printTransportOrdersCard, "Print Transport Order");

        // Add the main panel to the frame
        add(cards);

        setVisible(true);
    }

    private void createMainMenuCard(TransportFrameMenu transportFrameMenu) {
        String[] buttonLabels = {"Print Active Transports",
                "Print Former Transport Documents",
                "Print Transport Documents",
                "Print Transport Order",
                "Return to Main Menu"};

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10); // Add spacing between buttons

        for (int i = 0; i < buttonLabels.length; i++) {
            JButton button = new JButton(buttonLabels[i]);
            button.addActionListener(new Case2PrintDetailsFrame.ButtonClickListener(i + 1));
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
                    cardLayout.show(cards, "Print Active Transports");
                    break;
                case 2:
                    // Handle button 2 action
                    cardLayout.show(cards, "Print Former Transport Documents");
                    break;
                case 3:
                    cardLayout.show(cards, "Print Transport Documents");
                    // Handle button 3 action
                    break;
                case 4:
                    cardLayout.show(cards, "Print Transport Order");
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

    private JPanel createPrintActiveTransportsCard(TransportFrameMenu transportFrameMenu) {
        JPanel printActiveTransportsPanel = new JPanel();
        // Add components specific to the "Print Active Transports" card
        // ...
        printActiveTransportsPanel.setLayout(new BorderLayout());

        // Fetch all trucks using transportService.get_all_trucks()
        HashMap<Integer, TransportShipment> allActiveTransports = transportService.getAll_active_transports();
        // Create a text area to display the truck information
        JTextArea allActiveTransportsTextArea = new JTextArea();
        allActiveTransportsTextArea.setEditable(false);

        // Append the truck information to the text area
        for (HashMap.Entry<Integer, TransportShipment> entry : allActiveTransports.entrySet()) {
            TransportShipment transportShipment = entry.getValue();
            String transportShipmentInfo = transportShipment.toString() + "\n\n";
            allActiveTransportsTextArea.append(transportShipmentInfo);
        }
        // Create a scroll pane to contain the text area
        JScrollPane scrollPane = new JScrollPane(allActiveTransportsTextArea);

        // Add the scroll pane to the panel
        printActiveTransportsPanel.add(scrollPane, BorderLayout.CENTER);
        // Button to return to the main menu
        JButton returnButton = new JButton("Return To Main Menu");
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                transportFrameMenu.setVisible(true);
                dispose();
            }
        });
        printActiveTransportsPanel.add(returnButton, BorderLayout.SOUTH);

        return printActiveTransportsPanel;
    }

    private JPanel createPrintFormerTransportDocumentsCard(TransportFrameMenu transportFrameMenu) {
        JPanel printFormerTransportDocumentsPanel = new JPanel();
        // Add components specific to the "Print Active Transports" card
        // ...
        printFormerTransportDocumentsPanel.setLayout(new BorderLayout());

        // Fetch all trucks using transportService.get_all_trucks()
        ArrayList<TransportDocument> allFormerDocuments = transportService.getAll_former_transport_document();
        // Create a text area to display the truck information
        JTextArea allFormerDocumentsTextArea = new JTextArea();
        allFormerDocumentsTextArea.setEditable(false);

        // Append the truck information to the text area
        for (TransportDocument entry : allFormerDocuments) {
            String transportDocumentInfo = entry.toString() + "\n\n";
            allFormerDocumentsTextArea.append(transportDocumentInfo);
        }
        // Create a scroll pane to contain the text area
        JScrollPane scrollPane = new JScrollPane(allFormerDocumentsTextArea);

        // Add the scroll pane to the panel
        printFormerTransportDocumentsPanel.add(scrollPane, BorderLayout.CENTER);
        // Button to return to the main menu
        JButton returnButton = new JButton("Return To Main Menu");
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                transportFrameMenu.setVisible(true);
                dispose();
            }
        });
        printFormerTransportDocumentsPanel.add(returnButton, BorderLayout.SOUTH);

        return printFormerTransportDocumentsPanel;

    }

    private JPanel createPrintTransportDocumentsCard(TransportFrameMenu transportFrameMenu) {
        JPanel printTransportDocumentsPanel = new JPanel();
        // Add components specific to the "Print Active Transports" card
        // ...
        printTransportDocumentsPanel.setLayout(new BorderLayout());

        // Fetch all trucks using transportService.get_all_trucks()
        HashMap<Integer, TransportDocument> allTransportDocuments = transportService.getAll_transport_documents();
        // Create a text area to display the truck information
        JTextArea allTransportDocumentsTextArea = new JTextArea();
        allTransportDocumentsTextArea.setEditable(false);

        // Append the truck information to the text area
        for (TransportDocument entry : allTransportDocuments.values()) {
            String transportDocumentInfo = entry.toString() + "\n\n";
            allTransportDocumentsTextArea.append(transportDocumentInfo);
        }
        // Create a scroll pane to contain the text area
        JScrollPane scrollPane = new JScrollPane(allTransportDocumentsTextArea);

        // Add the scroll pane to the panel
        printTransportDocumentsPanel.add(scrollPane, BorderLayout.CENTER);
        // Button to return to the main menu
        JButton returnButton = new JButton("Return To Main Menu");
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                transportFrameMenu.setVisible(true);
                dispose();
            }
        });
        printTransportDocumentsPanel.add(returnButton, BorderLayout.SOUTH);

        return printTransportDocumentsPanel;

    }

    private JPanel createPrintTransportOrdersCard(TransportFrameMenu transportFrameMenu) {
        JPanel printTransportOrdersPanel = new JPanel();
        // Add components specific to the "Print Active Transports" card
        // ...
        printTransportOrdersPanel.setLayout(new BorderLayout());

        // Fetch all trucks using transportService.get_all_trucks()
        HashMap<Integer, TransportOrder> allTransportOrders = transportService.getAll_transport_orders();
        // Create a text area to display the truck information
        JTextArea allTransportOrdersTextArea = new JTextArea();
        allTransportOrdersTextArea.setEditable(false);

        // Append the truck information to the text area
        for (TransportOrder entry : allTransportOrders.values()) {
            String transportOrderInfo = entry.toString() + "\n\n";
            allTransportOrdersTextArea.append(transportOrderInfo);
        }
        // Create a scroll pane to contain the text area
        JScrollPane scrollPane = new JScrollPane(allTransportOrdersTextArea);

        // Add the scroll pane to the panel
        printTransportOrdersPanel.add(scrollPane, BorderLayout.CENTER);
        // Button to return to the main menu
        JButton returnButton = new JButton("Return To Main Menu");
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                transportFrameMenu.setVisible(true);
                dispose();
            }
        });
        printTransportOrdersPanel.add(returnButton, BorderLayout.SOUTH);

        return printTransportOrdersPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Handle action events
    }

}




