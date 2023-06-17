package PresentationLayer.GUI.TransportManager;

import BuisnessLayer.HR.Driver;
import BuisnessLayer.TransportManager.*;
import DataAccessLayer.Database;
import DataAccessLayer.TransportManager.ATruckDAO;
import ServiceLayer.TransportManager.TransportService;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Case6ManageActiveTransportFrame extends JFrame {
    private JPanel mainPanel;
    private JPanel buttonsPanel;
    private TransportFrameMenu transportFrameMenu;
    private ArrayList<String> allTrucks; // Assuming you have the list of all trucks
    private CardLayout cardLayout;
    private TransportService transportService;
    private TransportShipment chosen_shipment;
    private ATruck truck_of_shipment;
    private String currentWeightFromInput;
    private TransportOrder order_to_remove;
    private TransportOrder order_to_remove_product_from;
    private ATruck the_change_truck;
    private Driver the_change_driver;
    private ArrayList<TransportOrder> ordersMap;
    private int selectedRow=-1;
    private Driver newDriver;


    public Case6ManageActiveTransportFrame(TransportFrameMenu transportFrameMenu) {
        this.transportFrameMenu = transportFrameMenu;
        this.transportService = new TransportService();
        setTitle("Manage Active Transports");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 1000);
        setLayout(new BorderLayout());

        // Create the button panel
        buttonsPanel = new JPanel(new GridBagLayout());
        buttonsPanel.setOpaque(false); // Make the panel transparent
        buttonsPanel.setBounds(0, 0, getWidth(), getHeight());

        // Create the card layout and panel
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        getContentPane().add(mainPanel, BorderLayout.CENTER);

        // Create the cards for different views
        JPanel chooseShipmentCard = chooseShipmentCard(transportFrameMenu);
        JPanel weightTruckCard = createHandleTransportCard(transportFrameMenu);

        mainPanel.add(chooseShipmentCard, "Choose Shipment");
        mainPanel.add(weightTruckCard, "Weight Truck");

        // Display the main menu card by default
        cardLayout.show(mainPanel, "Choose Shipment");

        // Display the JFrame
        setVisible(true);
    }

    public JPanel createOrderCard(TransportFrameMenu transportFrameMenu) {
        setTitle("Choose Order to remove product from");
        JPanel orderPanel = new JPanel(new BorderLayout());
        //JLabel chooseOrderLabel = new JLabel("Please choose the order you want to remove products from: ");

        ArrayList<TransportOrder> OrdersMap = chosen_shipment.getTransport_document().getAll_transport_orders();
        //Please choose the shipment you want to manage:
        // Define the column names
        String[] columnNames = {"Select", "Order ID", "Source ID", "Destination ID", "Products"};

        // Create a DefaultTableModel with the column names
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) {
                    return Boolean.class; // Set the first column to Boolean (checkbox)
                }
                return super.getColumnClass(columnIndex);
            }
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0; // Only allow editing the first column (checkbox)
            }
        };
        String total_products = "";

        // Populate the table model with the source information
        for (TransportOrder transportOrder : OrdersMap) {
            Object[] rowData = new Object[5];
            rowData[0] = false; // Checkbox initial state
            rowData[1] = transportOrder.getTransport_order_id();
            rowData[2] = transportOrder.getSource().getID();
            rowData[3] = transportOrder.getDestination().getID();
            total_products = transportOrder.productListToString();
            rowData[4] = total_products;
            tableModel.addRow(rowData);
        }

        // Create a JTable using the table model
        JTable ordersTable = new JTable(tableModel);

        // Set the checkbox column width
        TableColumnModel columnModel = ordersTable.getColumnModel();
        columnModel.getColumn(0).setMaxWidth(50);
        columnModel.getColumn(0).setMinWidth(50);
        columnModel.getColumn(0).setCellRenderer(ordersTable.getDefaultRenderer(Boolean.class));
        columnModel.getColumn(0).setCellEditor(ordersTable.getDefaultEditor(Boolean.class));
        columnModel.getColumn(0).setResizable(false);

        // Disable multiple row selection
        ordersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Create a scroll pane to contain the JTable
        JScrollPane scrollPane = new JScrollPane(ordersTable);

        orderPanel.add(scrollPane, BorderLayout.CENTER);


        JButton returnButton = new JButton("Return Main Menu");
        JButton selectButton = new JButton("Select Order");
        selectButton.setEnabled(false); // Initially disabled until a row is selected

        // Add action listeners to the buttons
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                transportFrameMenu.setVisible(true); // Show the TransportFrameMenu
                dispose(); // Close the current frame (Case3TrucksFrame)
            }
        });

        selectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int rowCount = tableModel.getRowCount();
                for (int i = 0; i < rowCount; i++) {
                    boolean isSelected = (boolean) tableModel.getValueAt(i, 0);
                    if (isSelected) {
                        TransportOrder selectedOrder = OrdersMap.get(i);
                        order_to_remove_product_from = selectedOrder;
                        showRemoveProductFrame();
                        break; // Exit the loop after the first selected checkbox is found
                    }
                }
            }
        });

        // Add a listener to enable/disable the select button based on checkbox selection
        ActionListener checkboxListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean isAnySelected = false;
                int count = 0;

                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    boolean isSelected = (boolean) tableModel.getValueAt(i, 0);

                    if (isSelected) {
                        count++;
                        isAnySelected = true;
                    }
                    if (count > 1){
                        isAnySelected = false;
                    }
                }
                selectButton.setEnabled(isAnySelected);
            }
        };

        // Add the checkbox listener to the first column of the table
        ordersTable.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(new JCheckBox()));
        ordersTable.getColumnModel().getColumn(0).setCellRenderer(ordersTable.getDefaultRenderer(Boolean.class));
        ordersTable.getModel().addTableModelListener(e -> checkboxListener.actionPerformed(null));

        JPanel buttonsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints buttonConstraints = new GridBagConstraints();
        buttonConstraints.gridx = 0;
        buttonConstraints.gridy = 0;
        buttonConstraints.weightx = 1; // Allow the buttons to expand horizontally
        buttonConstraints.fill = GridBagConstraints.HORIZONTAL;

        buttonsPanel.add(selectButton, buttonConstraints);
        buttonConstraints.gridx++;
        buttonsPanel.add(returnButton, buttonConstraints);

        buttonConstraints.weightx = 1; // Allow the empty component to expand horizontally
        buttonsPanel.add(Box.createHorizontalGlue(), buttonConstraints);

        JPanel chooseOrderCard = new JPanel(new BorderLayout());
        chooseOrderCard.add(orderPanel, BorderLayout.CENTER);
        chooseOrderCard.add(buttonsPanel, BorderLayout.SOUTH);

        return chooseOrderCard;
    }

    private JPanel createHandleTransportCard(TransportFrameMenu transportFrameMenu) {
        String[] buttonLabels = {"Remove Supplier", "Change Truck", "Remove Products", "Return Main Menu"};

        // Create the main panel using a layered pane
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setLayout(null); // Set layout to null for absolute positioning

        // Create the background panel
        JPanel backgroundPanel = new JPanel();
        backgroundPanel.setLayout(new BorderLayout());
        ImageIcon backgroundImage = new ImageIcon("background.jpg"); // Replace "background.jpg" with your actual image file path
        JLabel backgroundLabel = new JLabel(backgroundImage);
        backgroundPanel.add(backgroundLabel);

        // Create the content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10); // Add spacing between buttons

        for (int i = 0; i < buttonLabels.length; i++) {
            JButton button = new JButton(buttonLabels[i]);
            button.addActionListener(new Case6ManageActiveTransportFrame.ButtonClickListener(i + 1));
            button.setPreferredSize(new Dimension(300, 80)); // Adjusted the preferred size for the button
            contentPanel.add(button, gbc);
            gbc.gridy++;
        }

        // Create the title label
        JLabel titleLabel = new JLabel("How to handle your shipment:");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Set the bounds for the components to position them correctly
        backgroundPanel.setBounds(0, 0, getWidth(), getHeight());
        contentPanel.setBounds((getWidth() - 300) / 2, (getHeight() - (buttonLabels.length * 100)) / 2, 300, buttonLabels.length * 100); // Adjusted the button height and spacing
        titleLabel.setBounds((getWidth() - 500) / 2, 50, 500, 100); // Adjusted the position and size of the label

        // Add components to the layered pane
        layeredPane.add(backgroundPanel, new Integer(0)); // Add background panel with a lower layer number
        layeredPane.add(contentPanel, new Integer(1)); // Add content panel with a higher layer number
        layeredPane.add(titleLabel, new Integer(2)); // Add title label with the highest layer number

        // Create the handle transport panel
        JPanel handleTransportPanel = new JPanel(new BorderLayout());
        handleTransportPanel.add(layeredPane, BorderLayout.CENTER);

        return handleTransportPanel;
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
                    showOrderSelectionDialog();
                    break;
                case 2:
                    // Handle button 2 action
                    showTruckFrame();
                    break;
                case 3:
                    cardLayout.show(mainPanel, "Choose order");
                    // Handle button 3 action
                    break;
                case 4:
                    transportFrameMenu.setVisible(true);
                    dispose();
                    // Handle button 4 action
                    break;
            }
        }
    }

    private void showRemoveProductFrame() {
        setTitle("Choose products to remove");
        JPanel productPanel = new JPanel(new GridBagLayout());
        GridBagConstraints productConstraints = new GridBagConstraints();
        productConstraints.gridx = 0;
        productConstraints.gridy = 0;
        productConstraints.anchor = GridBagConstraints.WEST;
        productConstraints.weighty = 0;
        productConstraints.weightx = 1; // Allow the table to expand horizontally
        productConstraints.fill = GridBagConstraints.HORIZONTAL;

        int max_order_id = transportService.get_highest_order_id();
        TransportOrder new_transport_order_after_remove_items = new TransportOrder(max_order_id + 1, order_to_remove_product_from.getSource(), order_to_remove_product_from.getDestination(), new HashMap<Product, Integer>());
        HashMap<Product, Integer> products_of_order_to_remove = order_to_remove_product_from.getProducts_list();

        JLabel chooseSourceLabel = new JLabel("Please choose the products you want to remove: ");
        productPanel.add(chooseSourceLabel, productConstraints);
        productConstraints.gridy++;

        DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"Select", "ID", "Product Name", "Amount", "Amount to remove"}, 0);
        JTable table = new JTable(tableModel);

        // Add products to the table
        for (Product product : products_of_order_to_remove.keySet()) {
            Object[] rowData = new Object[5];
            rowData[0] = false; // Initial checkbox value is unchecked
            rowData[1] = product.getProduct_code();
            rowData[2] = product.getName();
            rowData[3] = products_of_order_to_remove.get(product); // Leave the amount cell initially empty
            rowData[4] = ""; // Leave the amount cell initially empty
            tableModel.addRow(rowData);
        }
        JScrollPane scrollPane = new JScrollPane(table);
        productConstraints.weighty = 1; // Allow the table to expand vertically
        productConstraints.fill = GridBagConstraints.BOTH; // Fill both horizontal and vertical directions
        productPanel.add(scrollPane, productConstraints);
        productConstraints.gridy++;

        JButton returnButton = new JButton("Return to previous page");
        JButton RemoveButton = new JButton("Remove Products");
        RemoveButton.setEnabled(false); // Initially disabled until a row is selected

        // Add action listeners to the buttons
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "Choose order");
            }
        });

        RemoveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    boolean isSelected = (boolean) tableModel.getValueAt(i, 0);
                    String productId = (String) tableModel.getValueAt(i, 1);
                    String productName = (String) tableModel.getValueAt(i, 2);
                    String amountToRemove = (String) tableModel.getValueAt(i, 4);
                    int amountBeforeRemove = (int) tableModel.getValueAt(i, 3);

                    if (isSelected) {
                        Product product_to_remove_from_order = order_to_remove_product_from.FindProduct(productId);
                        transportService.update_product_amount_in_order_controller(order_to_remove_product_from, product_to_remove_from_order, (amountBeforeRemove - Integer.parseInt(amountToRemove)));
                        new_transport_order_after_remove_items.add_items_and_amount_to_transport_order(product_to_remove_from_order, Integer.parseInt(amountToRemove));
                    }
                }

                transportService.add_new_transport_order(new_transport_order_after_remove_items);
                transportService.setWeight(chosen_shipment, Integer.parseInt(currentWeightFromInput));
                // Show a message box indicating the order success
                JOptionPane.showMessageDialog(Case6ManageActiveTransportFrame.this,
                        "The removing product has been successful!",
                        "Removing Products Success",
                        JOptionPane.INFORMATION_MESSAGE);

                transportFrameMenu.setVisible(true);
                dispose();
            }
        });

        // Create a custom cell renderer for the "Amount" column
        TableCellRenderer amountRenderer = new TableCellRenderer() {
            private final JLabel label = new JLabel();

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                String amountString = (String) value;
                label.setText(amountString);

                if (!amountString.isEmpty() && !isInteger(amountString)) {
                    label.setBorder(BorderFactory.createLineBorder(Color.RED));
                } else if (!amountString.isEmpty() && isInteger(amountString)) {
                    int amountToRemove = 0;
                    try {
                        amountToRemove = Integer.parseInt(amountString);
                    } catch (NumberFormatException e) {
                        // Handle the exception if the input is not a valid integer
                    }
                    int amountInOrder = 0;
                    try {
                        amountInOrder = Integer.parseInt(table.getValueAt(row, 3).toString());
                    } catch (NumberFormatException e) {
                        // Handle the exception if the amount in the order is not a valid integer
                    }
                    if (amountToRemove > amountInOrder) {
                        label.setBorder(BorderFactory.createLineBorder(Color.RED));
                    } else {
                        label.setBorder(null);
                    }
                }
                return label;
            }
        };
        // Set the cell renderer for the "Amount" column
        table.getColumnModel().getColumn(4).setCellRenderer(amountRenderer);

        ActionListener checkboxListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean isAnySelected = false;
                boolean isAmountValid = true; // Assume all amounts are valid initially

                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    boolean isSelected = (boolean) tableModel.getValueAt(i, 0);
                    String amountString = (String) tableModel.getValueAt(i, 4);
                    int amountProduct = (int) tableModel.getValueAt(i, 3);

                    if (isSelected) {
                        isAnySelected = true;
                        if (amountString.isEmpty() || !isInteger(amountString) || amountProduct < Integer.parseInt(amountString)) {
                            isAmountValid = false;
                            break; // Exit the loop if any invalid amount is found
                        }
                    }
                }

                // Refresh the table to update the cell renderers
                table.repaint();

                RemoveButton.setEnabled(isAnySelected && isAmountValid);
            }
        };

        // Add the checkbox listener to the first column of the table
        table.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(new JCheckBox()));
        table.getColumnModel().getColumn(0).setCellRenderer(table.getDefaultRenderer(Boolean.class));
        table.getModel().addTableModelListener(e -> checkboxListener.actionPerformed(null));

        JPanel buttonsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints buttonConstraints = new GridBagConstraints();
        buttonConstraints.gridx = 0;
        buttonConstraints.gridy = 0;
        buttonConstraints.weightx = 1; // Allow the buttons to expand horizontally
        buttonConstraints.fill = GridBagConstraints.HORIZONTAL;

        buttonsPanel.add(RemoveButton, buttonConstraints);
        buttonConstraints.gridx++;
        buttonsPanel.add(returnButton, buttonConstraints);

        buttonConstraints.weightx = 1; // Allow the empty component to expand horizontally
        buttonsPanel.add(Box.createHorizontalGlue(), buttonConstraints);

        JPanel chooseProductCard = new JPanel(new BorderLayout());
        chooseProductCard.add(productPanel, BorderLayout.CENTER);
        chooseProductCard.add(buttonsPanel, BorderLayout.SOUTH);
        mainPanel.add(chooseProductCard, "ChooseProductCard");
        cardLayout.show(mainPanel, "ChooseProductCard");
    }

    private boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void showOrderSelectionDialog() {

        setTitle("Remove Supplier From Shipment");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 1000);
        setLayout(new BorderLayout());

        JPanel mainPanel2 = new JPanel(new BorderLayout());
        JPanel removeSupplierPanel = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        ArrayList<TransportOrder> orders_in_shipment = transportService.getAllTransportOrdersOfShipment(chosen_shipment);


        String[] columnNames = {"Select", "Transport Order ID", "Assigned to Transport Document ID", "Source ID", "Destination ID", "Cargo"};

        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) {
                    return Boolean.class;
                }
                return super.getColumnClass(columnIndex);
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0;
            }
        };

        for (TransportOrder curr_order : orders_in_shipment) {
            Object[] rowData = new Object[6];
            rowData[0] = false;
            rowData[1] = curr_order.getTransport_order_id();
            rowData[2] = curr_order.getAssigned_doc_id();
            rowData[3] = curr_order.getSource().getID();
            rowData[4] = curr_order.getDestination().getID();
            rowData[5] = curr_order.getProducts_list();

            tableModel.addRow(rowData);
        }

        JTable docsTable = new JTable(tableModel);

        TableColumnModel columnModel = docsTable.getColumnModel();
        columnModel.getColumn(0).setMaxWidth(50);
        columnModel.getColumn(0).setMinWidth(50);
        columnModel.getColumn(0).setCellRenderer(docsTable.getDefaultRenderer(Boolean.class));
        columnModel.getColumn(0).setCellEditor(docsTable.getDefaultEditor(Boolean.class));
        columnModel.getColumn(0).setResizable(false);

        docsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(docsTable);

        JButton removeButton = new JButton("Remove Supplier");
        removeButton.setEnabled(false);

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                transportService.HandleWeight_SkipSupplier_Controller(chosen_shipment, order_to_remove);
                JOptionPane.showMessageDialog(null, "The skipping on the supplier was successful!",
                        "Message", JOptionPane.INFORMATION_MESSAGE);
                transportFrameMenu.setVisible(true);
                dispose();

            }
        });

        // Add a listener to enable/disable the select button based on checkbox selection
        ListSelectionListener selectionListener = new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int newlySelectedRow = docsTable.getSelectedRow();
                boolean isSelected = (newlySelectedRow >= 0) && (Boolean) tableModel.getValueAt(newlySelectedRow, 0);

                if (selectedRow >= 0 && selectedRow != newlySelectedRow) {
                    // Clear the "v" from the old selection
                    tableModel.setValueAt(false, selectedRow, 0);
                }

                selectedRow = newlySelectedRow;
                removeButton.setEnabled(isSelected);

                if (!isSelected) {
                    removeButton.setEnabled(false);
                } else {
                    // Assign the selected transport order to the transportOrder attribute
                    int transportOrderToRemoveID = (int) tableModel.getValueAt(newlySelectedRow, 1); // Assuming the transport order ID is stored in column 1
                    order_to_remove = transportService.get_transport_order_by_id(transportOrderToRemoveID); // Implement this method to find the transport order based on its ID

                }
            }
        };

        // Add a change listener to the table model
        tableModel.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                boolean isSelected = false;
                for (int row = 0; row < tableModel.getRowCount(); row++) {
                    if ((Boolean) tableModel.getValueAt(row, 0)) {
                        isSelected = true;
                        break;
                    }
                }
                removeButton.setEnabled(isSelected);
            }
        });

        JButton returnButton = new JButton("Return Main Menu");
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                transportFrameMenu.setVisible(true);
                dispose();
            }
        });

        JPanel buttonsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints buttonConstraints = new GridBagConstraints();
        buttonConstraints.gridx = 0;
        buttonConstraints.gridy = 0;
        buttonConstraints.weightx = 1; // Allow the buttons to expand horizontally
        buttonConstraints.fill = GridBagConstraints.HORIZONTAL;

        buttonsPanel.add(removeButton, buttonConstraints);
        buttonConstraints.gridx++;
        buttonsPanel.add(returnButton, buttonConstraints);

        buttonConstraints.weightx = 1; // Allow the empty component to expand horizontally
        buttonsPanel.add(Box.createHorizontalGlue(), buttonConstraints);

        docsTable.getSelectionModel().addListSelectionListener(selectionListener);

        removeSupplierPanel.add(scrollPane, BorderLayout.CENTER);
        removeSupplierPanel.add(buttonsPanel, BorderLayout.SOUTH);

        mainPanel2.add(removeSupplierPanel, BorderLayout.CENTER);

        setContentPane(mainPanel2);
        setVisible(true);
    }



    public JPanel chooseShipmentCard(TransportFrameMenu transportFrameMenu){
        JPanel shipmentPanel = new JPanel(new BorderLayout());

        HashMap<Integer, TransportShipment> activeShipmentMap = transportService.getAll_active_transports();
        //Please choose the shipment you want to manage:
        // Define the column names
        String[] columnNames = {"Select", "Shipment ID", "Date", "Departure Time", "Cargo Weight", "Transport Document ID"};

        // Create a DefaultTableModel with the column names
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) {
                    return Boolean.class; // Set the first column to Boolean (checkbox)
                }
                return super.getColumnClass(columnIndex);
            }
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0; // Only allow editing the first column (checkbox)
            }
        };

        // Populate the table model with the source information
        for (int activeShipmentID : activeShipmentMap.keySet()) {
            TransportShipment shipment = activeShipmentMap.get(activeShipmentID);
            Object[] rowData = new Object[6];
            rowData[0] = false; // Checkbox initial state
            rowData[1] = activeShipmentID;
            rowData[2] = shipment.getDate();
            rowData[3] = shipment.getDeparture_time();
            rowData[4] = shipment.getCargo_weight();
            rowData[5] = shipment.getTransport_document().getTransport_document_ID();
            tableModel.addRow(rowData);
        }

        // Create a JTable using the table model
        JTable shipmentTable = new JTable(tableModel);

        // Set the checkbox column width
        TableColumnModel columnModel = shipmentTable.getColumnModel();
        columnModel.getColumn(0).setMaxWidth(50);
        columnModel.getColumn(0).setMinWidth(50);
        columnModel.getColumn(0).setCellRenderer(shipmentTable.getDefaultRenderer(Boolean.class));
        columnModel.getColumn(0).setCellEditor(shipmentTable.getDefaultEditor(Boolean.class));
        columnModel.getColumn(0).setResizable(false);

        // Disable multiple row selection
        shipmentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Create a scroll pane to contain the JTable
        JScrollPane scrollPane = new JScrollPane(shipmentTable);

        shipmentPanel.add(scrollPane, BorderLayout.CENTER);


        JButton returnButton = new JButton("Return Main Menu");
        JButton sourceButton = new JButton("Source");
        JButton destinationButton = new JButton("Destination");
        sourceButton.setEnabled(false); // Initially disabled until a row is selected
        destinationButton.setEnabled(false); // Initially disabled until a row is selected

        // Add action listeners to the buttons
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                transportFrameMenu.setVisible(true); // Show the TransportFrameMenu
                dispose(); // Close the current frame (Case3TrucksFrame)
            }
        });

        sourceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = shipmentTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int sourceId = (int) tableModel.getValueAt(selectedRow, 1); // Get the source ID from the selected row
                    TransportShipment selectedShipment = activeShipmentMap.get(sourceId); // Get the corresponding source
                    chosen_shipment = selectedShipment; // Store the selected source in the source_of_order field
                    ordersMap = chosen_shipment.getTransport_document().getAll_transport_orders();

                    int license_plate = chosen_shipment.getTruck_license_plate_number();
                    truck_of_shipment = transportService.get_truck(license_plate);

                    showMessage2("Please enter the total weight of the transport: ", transportFrameMenu);

                    JPanel chooseOrderCard = createOrderCard(transportFrameMenu);
                    mainPanel.add(chooseOrderCard, "Choose order");
                }
            }
        });

        destinationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showMessage("Did you finished the shipment? [Yes / No]", transportFrameMenu);
            }
        });
        // Add a listener to enable/disable the select button based on checkbox selection
        ActionListener checkboxListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean isAnySelected = false;
                int count = 0;

                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    boolean isSelected = (boolean) tableModel.getValueAt(i, 0);

                    if (isSelected) {
                        count++;
                        isAnySelected = true;
                    }
                    if (count > 1){
                        isAnySelected = false;
                    }
                }

                sourceButton.setEnabled(isAnySelected);
                destinationButton.setEnabled(isAnySelected);
            }
        };

        // Add the checkbox listener to the first column of the table
        shipmentTable.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(new JCheckBox()));
        shipmentTable.getColumnModel().getColumn(0).setCellRenderer(shipmentTable.getDefaultRenderer(Boolean.class));
        shipmentTable.getModel().addTableModelListener(e -> checkboxListener.actionPerformed(null));

        JPanel buttonsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints buttonConstraints = new GridBagConstraints();
        buttonConstraints.gridx = 0;
        buttonConstraints.gridy = 0;
        buttonConstraints.weightx = 1; // Allow the buttons to expand horizontally
        buttonConstraints.fill = GridBagConstraints.HORIZONTAL;

        buttonsPanel.add(returnButton, buttonConstraints);
        buttonConstraints.gridx++;
        buttonsPanel.add(sourceButton, buttonConstraints);
        buttonConstraints.gridx++;
        buttonsPanel.add(destinationButton, buttonConstraints);

        buttonConstraints.weightx = 1; // Allow the empty component to expand horizontally
        buttonsPanel.add(Box.createHorizontalGlue(), buttonConstraints);

        JPanel chooseShipmentCard = new JPanel(new BorderLayout());
        chooseShipmentCard.add(shipmentPanel, BorderLayout.CENTER);
        chooseShipmentCard.add(buttonsPanel, BorderLayout.SOUTH);

        return chooseShipmentCard;
    }

    private void showMessage2(String message, TransportFrameMenu transportFrameMenu) {
        JFrame frame = new JFrame("Weight Truck");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 200);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());

        JLabel messageLabel = new JLabel(message);
        panel.add(messageLabel, BorderLayout.NORTH);

        JTextField textField = new JTextField();
        textField.setHorizontalAlignment(JTextField.CENTER);
        panel.add(textField, BorderLayout.CENTER);

        JButton weightTruckButton = new JButton("Weight Truck Button");
        weightTruckButton.setEnabled(false);
        panel.add(weightTruckButton, BorderLayout.SOUTH);

        // Add a key listener to the text field
        textField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                // Unused
            }

            @Override
            public void keyPressed(KeyEvent e) {
                // Unused
            }

            @Override
            public void keyReleased(KeyEvent e) {
                String input = textField.getText();
                if (input.isEmpty()) {
                    textField.setBorder(new JTextField().getBorder()); // Reset border to default if empty
                    weightTruckButton.setEnabled(false);
                } else if (input.matches("\\d+")) {
                    textField.setBorder(new LineBorder(Color.BLACK)); // Change border color to black for valid input
                    weightTruckButton.setEnabled(true);
                } else {
                    textField.setBorder(new LineBorder(Color.RED)); // Change border color to red for invalid input
                    weightTruckButton.setEnabled(false);
                }
            }
        });

        weightTruckButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentWeightFromInput = textField.getText();
                boolean weightResult = transportService.WeightTruck(chosen_shipment, truck_of_shipment, Integer.parseInt(currentWeightFromInput));
                if (weightResult) {
                    // Show a message box indicating successful weight verification
                    JOptionPane.showMessageDialog(frame, "Your weight is okay! You can keep your shipment!");

                    // Go back to the transportFrameMenu
                    transportFrameMenu.setVisible(true);
                    frame.dispose();
                } else {
                    // Open the "Weight Truck" card
                    JOptionPane.showMessageDialog(frame, "You are overweight, please handle it  !");
                    frame.dispose();
                    CardLayout cardLayout = (CardLayout) mainPanel.getLayout();
                    cardLayout.show(mainPanel, "Weight Truck");
                }
            }
        });

        frame.add(panel);
        frame.setVisible(true);
    }
    private void showMessage(String message, TransportFrameMenu transportFrameMenu) {
        Object[] options = {"Yes", "No"};
        int result = JOptionPane.showOptionDialog(this, message, "Message", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, null);

        if (result == 0) { // "Yes" button clicked
            transportService.Finish_Transport_Controller(chosen_shipment);
            transportFrameMenu.setVisible(true);
            dispose(); // Close the current frame
        }
        if (result == 1) {
            transportFrameMenu.setVisible(true);
            dispose(); // Close the current frame
        }
    }

    private void showTruckFrame() {
        setTitle("Change Truck");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 1000);
        setLayout(new BorderLayout());

        JPanel mainPanel2 = new JPanel(new BorderLayout());
        JPanel changeTruckPanel = new JPanel(new BorderLayout());

        ArrayList<ATruck> trucks = transportService.getAll_trucks_by_type_totalWeight_availability(chosen_shipment, Integer.parseInt(currentWeightFromInput));

        String[] columnNames = {"Select", "Truck License Plate Number", "Type", "Net Weight", "Max Cargo Weight", "Level", "Status"};

        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) {
                    return Boolean.class;
                }
                return super.getColumnClass(columnIndex);
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0;
            }
        };

        for (ATruck curr_truck : trucks) {
            Object[] rowData = new Object[7];
            rowData[0] = false;
            rowData[1] = curr_truck.getLicense_plate_number();
            rowData[2] = curr_truck.getType();
            rowData[3] = curr_truck.getNet_weight();
            rowData[4] = curr_truck.getMax_cargo_weight();
            rowData[5] = curr_truck.getTruck_level();
            rowData[6] = curr_truck.getStatus();

            tableModel.addRow(rowData);
        }

        JTable docsTable = new JTable(tableModel);

        TableColumnModel columnModel = docsTable.getColumnModel();
        columnModel.getColumn(0).setMaxWidth(50);
        columnModel.getColumn(0).setMinWidth(50);
        columnModel.getColumn(0).setCellRenderer(docsTable.getDefaultRenderer(Boolean.class));
        columnModel.getColumn(0).setCellEditor(docsTable.getDefaultEditor(Boolean.class));
        columnModel.getColumn(0).setResizable(false);

        docsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(docsTable);

        JButton changeTruckButton = new JButton("Change Truck");
        changeTruckButton.setEnabled(false);

        changeTruckButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showDriverFrame();

            }
        });

        // Add a listener to enable/disable the select button based on checkbox selection
        ListSelectionListener selectionListener = new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int newlySelectedRow = docsTable.getSelectedRow();
                boolean isSelected = (newlySelectedRow >= 0) && (Boolean) tableModel.getValueAt(newlySelectedRow, 0);

                if (selectedRow >= 0 && selectedRow != newlySelectedRow) {
                    // Clear the "v" from the old selection
                    tableModel.setValueAt(false, selectedRow, 0);
                }

                selectedRow = newlySelectedRow;
                changeTruckButton.setEnabled(isSelected);

                if (!isSelected) {
                    changeTruckButton.setEnabled(false);
                } else {
                    // Assign the selected transport order to the transportOrder attribute
                    int truckToChangeLicensePlateNumber = (int) tableModel.getValueAt(newlySelectedRow, 1); // Assuming the transport order ID is stored in column 1
                    the_change_truck = transportService.get_truck(truckToChangeLicensePlateNumber); // Implement this method to find the transport order based on its ID

                }
            }
        };

        // Add a change listener to the table model
        tableModel.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                boolean isSelected = false;
                for (int row = 0; row < tableModel.getRowCount(); row++) {
                    if ((Boolean) tableModel.getValueAt(row, 0)) {
                        isSelected = true;
                        break;
                    }
                }
                changeTruckButton.setEnabled(isSelected);
            }
        });

        JButton returnButton = new JButton("Return To Main Menu");
        // Add action listeners to the buttons
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                transportFrameMenu.setVisible(true);
                dispose();
            }
        });

        JPanel buttonsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints buttonConstraints = new GridBagConstraints();
        buttonConstraints.gridx = 0;
        buttonConstraints.gridy = 0;
        buttonConstraints.weightx = 1; // Allow the buttons to expand horizontally
        buttonConstraints.fill = GridBagConstraints.HORIZONTAL;

        buttonsPanel.add(changeTruckButton, buttonConstraints);
        buttonConstraints.gridx++;
        buttonsPanel.add(returnButton, buttonConstraints);

        buttonConstraints.weightx = 1; // Allow the empty component to expand horizontally
        buttonsPanel.add(Box.createHorizontalGlue(), buttonConstraints);

        docsTable.getSelectionModel().addListSelectionListener(selectionListener);
        changeTruckPanel.add(scrollPane, BorderLayout.CENTER);
        changeTruckPanel.add(buttonsPanel, BorderLayout.SOUTH);

        mainPanel2.add(changeTruckPanel, BorderLayout.CENTER);

        setContentPane(mainPanel2);
        setVisible(true);
    }

    private void showDriverFrame() {
    setTitle("Choose New Driver");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(1000, 1000);
    setLayout(new BorderLayout());

    JPanel mainPanel3 = new JPanel(new BorderLayout());
    JPanel chooseDriverPanel = new JPanel(new BorderLayout());

    ArrayList<Driver> drivers = transportService.get_all_available_drivers_controller(chosen_shipment.getDate(), chosen_shipment.getDeparture_time(), (Double.parseDouble(currentWeightFromInput) - truck_of_shipment.getNet_weight()), truck_of_shipment.getTruck_level());


    if (drivers.isEmpty()) {
        JOptionPane.showMessageDialog(this,
                "There aren't available drivers match to your needs !",
                "No Available Drivers",
                JOptionPane.INFORMATION_MESSAGE);
        transportFrameMenu.setVisible(true);
        dispose();
    } else {

        String[] columnNames = {"Select", "Driver's Name", "ID", "Max weight allowed", "License"};

        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) {
                    return Boolean.class;
                }
                return super.getColumnClass(columnIndex);
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0;
            }
        };

        for (Driver curr_driver : drivers) {
            Object[] rowData = new Object[5];
            rowData[0] = false;
            rowData[1] = curr_driver.employeeFullName;
            rowData[2] = curr_driver.employeeID;
            rowData[3] = curr_driver.getDriver_max_weight_allowed();
            rowData[4] = curr_driver.getDriver_license();
            tableModel.addRow(rowData);
        }

        JTable driversTable = new JTable(tableModel);

        TableColumnModel columnModel = driversTable.getColumnModel();
        columnModel.getColumn(0).setMaxWidth(50);
        columnModel.getColumn(0).setMinWidth(50);
        columnModel.getColumn(0).setCellRenderer(driversTable.getDefaultRenderer(Boolean.class));
        columnModel.getColumn(0).setCellEditor(driversTable.getDefaultEditor(Boolean.class));
        columnModel.getColumn(0).setResizable(false);

        driversTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(driversTable);

        JButton chooseDriverButton = new JButton("Choose driver");
        chooseDriverButton.setEnabled(false);

        chooseDriverButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                transportService.update_details_after_change_truck(chosen_shipment, newDriver,the_change_truck,Integer.parseInt(currentWeightFromInput));


                  JOptionPane.showMessageDialog(Case6ManageActiveTransportFrame.this,
                        "You have successfully handled the over weight.",
                        "Handles Over Weight",
                        JOptionPane.INFORMATION_MESSAGE);
                transportFrameMenu.setVisible(true);
                dispose();

            }
        });

        // Add a listener to enable/disable the select button based on checkbox selection
        ListSelectionListener selectionListener = new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int newlySelectedRow = driversTable.getSelectedRow();
                boolean isSelected = (newlySelectedRow >= 0) && (Boolean) tableModel.getValueAt(newlySelectedRow, 0);

                if (selectedRow >= 0 && selectedRow != newlySelectedRow) {
                    // Clear the "v" from the old selection
                    tableModel.setValueAt(false, selectedRow, 0);
                }

                selectedRow = newlySelectedRow;
                chooseDriverButton.setEnabled(isSelected);

                if (!isSelected) {
                    chooseDriverButton.setEnabled(false);
                } else {
                    // Assign the selected transport order to the transportOrder attribute
                    String new_driver_id = (String) tableModel.getValueAt(newlySelectedRow, 2); // Assuming the transport order ID is stored in column 1
                    newDriver = transportService.get_driver_in_company_controller(new_driver_id); // Implement this method to find the transport order based on its ID

                }
            }
        };

        // Add a change listener to the table model
        tableModel.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                boolean isSelected = false;
                for (int row = 0; row < tableModel.getRowCount(); row++) {
                    if ((Boolean) tableModel.getValueAt(row, 0)) {
                        isSelected = true;
                        break;
                    }
                }
                chooseDriverButton.setEnabled(isSelected);
            }
        });

        JButton returnButton = new JButton("Return To Main Menu");
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                transportFrameMenu.setVisible(true);
                dispose();
            }
        });

        JPanel buttonsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints buttonConstraints = new GridBagConstraints();
        buttonConstraints.gridx = 0;
        buttonConstraints.gridy = 0;
        buttonConstraints.weightx = 1; // Allow the buttons to expand horizontally
        buttonConstraints.fill = GridBagConstraints.HORIZONTAL;

        buttonsPanel.add(chooseDriverButton, buttonConstraints);
        buttonConstraints.gridx++;
        buttonsPanel.add(returnButton, buttonConstraints);

        buttonConstraints.weightx = 1; // Allow the empty component to expand horizontally
        buttonsPanel.add(Box.createHorizontalGlue(), buttonConstraints);

        driversTable.getSelectionModel().addListSelectionListener(selectionListener);

        chooseDriverPanel.add(scrollPane, BorderLayout.CENTER);
        chooseDriverPanel.add(buttonsPanel, BorderLayout.SOUTH);

        mainPanel3.add(chooseDriverPanel, BorderLayout.CENTER);
        setContentPane(mainPanel3);
        setVisible(true);
    }
    }
    private TransportShipment getShipmentByIndex(HashMap<Integer, TransportShipment> shipmentMap, int index) {
        for (int shipmentId : shipmentMap.keySet()) {
            TransportShipment transportShipment = shipmentMap.get(shipmentId);
            if (index == 1) {
                return transportShipment; // Return the source at the specified index
            }
            index--;
        }
        return null; // Invalid index
    }
    private TransportOrder getOrderByIndex(ArrayList<TransportOrder> orderMap, int index) {
        for (TransportOrder order : orderMap) {
            if (index == 1) {
                return order; // Return the source at the specified index
            }
            index--;
        }
        return null; // Invalid index
    }
}
