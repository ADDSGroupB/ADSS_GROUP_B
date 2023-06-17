package PresentationLayer.GUI.TransportManager;

import BuisnessLayer.TransportManager.*;
import ServiceLayer.TransportManager.TransportService;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.HashMap;

public class Case3NewOrderFrame extends JFrame {
    private JPanel cards;
    private CardLayout cardLayout;
    private HelperFunctions helperFunctions;
    private TransportService transportService;
    private HashMap<Product, Integer> current_order;
    private ASite source_of_order;
    private ASite destination_of_order;
    private int selectedRow = -1;

    public Case3NewOrderFrame(TransportFrameMenu transportFrameMenu) {

        current_order = new HashMap<>();
        transportService = new TransportService();

        setTitle("Create New Transport Order");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 1000);

        // Create the card layout and panel
        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);
        getContentPane().add(cards, BorderLayout.CENTER);

        // Create the cards for different views
        JPanel chooseSourceCard = createSourceCard(transportFrameMenu);
        JPanel chooseDestinationCard = createDestinationCard(transportFrameMenu);
        JPanel chooseProductCard = createProductCard(transportFrameMenu);

        cards.add(chooseSourceCard, "Choose Source");
        cards.add(chooseDestinationCard, "Choose Destination");
        cards.add(chooseProductCard, "Choose Products");

        // Display the main menu card by default
        cardLayout.show(cards, "Main Menu");

        // Display the JFrame
        setVisible(true);
    }
    private JPanel createProductCard(TransportFrameMenu transportFrameMenu) {
        JPanel productPanel = new JPanel(new GridBagLayout());
        GridBagConstraints productConstraints = new GridBagConstraints();
        productConstraints.gridx = 0;
        productConstraints.gridy = 0;
        productConstraints.anchor = GridBagConstraints.WEST;
        productConstraints.weighty = 0;
        productConstraints.weightx = 1; // Allow the table to expand horizontally
        productConstraints.fill = GridBagConstraints.HORIZONTAL;

        HashMap<String, Product> productsMap = transportService.getAllProducts();

        JLabel chooseSourceLabel = new JLabel("Please choose your products: ");
        productPanel.add(chooseSourceLabel, productConstraints);
        productConstraints.gridy++;

        DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"Select", "ID", "Product Name", "Amount"}, 0);
        JTable table = new JTable(tableModel);

        // Add products to the table
        for (String productId : productsMap.keySet()) {
            Product product = productsMap.get(productId);
            Object[] rowData = new Object[4];
            rowData[0] = false; // Initial checkbox value is unchecked
            rowData[1] = productId;
            rowData[2] = product.getName();
            rowData[3] = ""; // Leave the amount cell initially empty
            tableModel.addRow(rowData);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        productConstraints.weighty = 1; // Allow the table to expand vertically
        productConstraints.fill = GridBagConstraints.BOTH; // Fill both horizontal and vertical directions
        productPanel.add(scrollPane, productConstraints);
        productConstraints.gridy++;

        JButton returnButton = new JButton("Return to Main Menu");
        JButton createOrderButton = new JButton("Create Order");
        createOrderButton.setEnabled(false); // Initially disabled until a row is selected

        // Add action listeners to the buttons
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                transportFrameMenu.setVisible(true); // Show the TransportFrameMenu
                dispose(); // Close the current frame (Case3TrucksFrame)
            }
        });

        createOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    boolean isSelected = (boolean) tableModel.getValueAt(i, 0);
                    String productId = (String) tableModel.getValueAt(i, 1);
                    String productName = (String) tableModel.getValueAt(i, 2);
                    String amountString = (String) tableModel.getValueAt(i, 3);

                    if (isSelected && !amountString.isEmpty()) {
                        int amount = Integer.parseInt(amountString);
                        Product product = productsMap.get(productId);
                        current_order.put(product, amount);
                    }
                }
                transportService.creating_new_transport_order_by_products_type_Controller((Source) source_of_order, (Destination) destination_of_order, current_order);
                // Show a message box indicating the order success
                JOptionPane.showMessageDialog(Case3NewOrderFrame.this,
                        "Your order has been successful!",
                        "Order Success",
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
                } else {
                    label.setBorder(null);
                }

                return label;
            }
        };
        // Set the cell renderer for the "Amount" column
        table.getColumnModel().getColumn(3).setCellRenderer(amountRenderer);

        ActionListener checkboxListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean isAnySelected = false;
                boolean isAmountValid = true; // Assume all amounts are valid initially

                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    boolean isSelected = (boolean) tableModel.getValueAt(i, 0);
                    String amountString = (String) tableModel.getValueAt(i, 3);

                    if (isSelected) {
                        isAnySelected = true;
                        if (amountString.isEmpty() || !isInteger(amountString)) {
                            isAmountValid = false;
                            break; // Exit the loop if any invalid amount is found
                        }
                    }
                }

                // Refresh the table to update the cell renderers
                table.repaint();

                createOrderButton.setEnabled(isAnySelected && isAmountValid);
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

        buttonsPanel.add(returnButton, buttonConstraints);
        buttonConstraints.gridx++;
        buttonsPanel.add(createOrderButton, buttonConstraints);
        buttonConstraints.gridx++;
        buttonsPanel.add(returnButton, buttonConstraints);
        buttonConstraints.weightx = 1; // Allow the empty component to expand horizontally
        buttonsPanel.add(Box.createHorizontalGlue(), buttonConstraints);

        JPanel chooseProductCard = new JPanel(new BorderLayout());
        chooseProductCard.add(productPanel, BorderLayout.CENTER);
        chooseProductCard.add(buttonsPanel, BorderLayout.SOUTH);

        return chooseProductCard;

    }

    private JPanel createSourceCard(TransportFrameMenu transportFrameMenu) {
        JPanel sourcePanel = new JPanel(new BorderLayout());

        HashMap<Integer, ASite> sourceMap = transportService.getAllSources();

        // Define the column names
        String[] columnNames = {"Select", "Source ID", "Address", "Contact Name", "Phone Number"};

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
        for (int sourceId : sourceMap.keySet()) {
            ASite source = sourceMap.get(sourceId);
            Object[] rowData = new Object[5];
            rowData[0] = false; // Checkbox initial state
            rowData[1] = sourceId;
            rowData[2] = source.getAddress();
            rowData[3] = source.getContact_name();
            rowData[4] = source.getPhone_number();
            tableModel.addRow(rowData);
        }

        // Create a JTable using the table model
        JTable sourceTable = new JTable(tableModel);

        // Set the checkbox column width
        TableColumnModel columnModel = sourceTable.getColumnModel();
        columnModel.getColumn(0).setMaxWidth(50);
        columnModel.getColumn(0).setMinWidth(50);
        columnModel.getColumn(0).setCellRenderer(sourceTable.getDefaultRenderer(Boolean.class));
        columnModel.getColumn(0).setCellEditor(sourceTable.getDefaultEditor(Boolean.class));
        columnModel.getColumn(0).setResizable(false);

        // Disable multiple row selection
        sourceTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Create a scroll pane to contain the JTable
        JScrollPane scrollPane = new JScrollPane(sourceTable);

        sourcePanel.add(scrollPane, BorderLayout.CENTER);


        JButton returnButton = new JButton("Return to Main Menu");
        JButton selectButton = new JButton("Select");
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
                int selectedRow = sourceTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int sourceId = (int) tableModel.getValueAt(selectedRow, 1); // Get the source ID from the selected row
                    ASite selectedSource = sourceMap.get(sourceId); // Get the corresponding source
                    source_of_order = selectedSource; // Store the selected source in the source_of_order field
                    cardLayout.show(cards, "Choose Destination"); // Move to the destinationCard
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
        sourceTable.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(new JCheckBox()));
        sourceTable.getColumnModel().getColumn(0).setCellRenderer(sourceTable.getDefaultRenderer(Boolean.class));
        sourceTable.getModel().addTableModelListener(e -> checkboxListener.actionPerformed(null));


        JPanel buttonsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints buttonConstraints = new GridBagConstraints();
        buttonConstraints.gridx = 0;
        buttonConstraints.gridy = 0;
        buttonConstraints.weightx = 1; // Allow the buttons to expand horizontally
        buttonConstraints.fill = GridBagConstraints.HORIZONTAL;

        buttonsPanel.add(returnButton, buttonConstraints);
        buttonConstraints.gridx++;
        buttonsPanel.add(selectButton, buttonConstraints);
        buttonConstraints.gridx++;
        buttonsPanel.add(returnButton, buttonConstraints);
        buttonConstraints.weightx = 1; // Allow the empty component to expand horizontally
        buttonsPanel.add(Box.createHorizontalGlue(), buttonConstraints);


        JPanel chooseSourceCard = new JPanel(new BorderLayout());
        chooseSourceCard.add(sourcePanel, BorderLayout.CENTER);
        chooseSourceCard.add(buttonsPanel, BorderLayout.SOUTH);

        return chooseSourceCard;
    }


    private JPanel createDestinationCard(TransportFrameMenu transportFrameMenu) {
        JPanel destinationPanel = new JPanel(new BorderLayout());

        HashMap<Integer, ASite> destinationMap = transportService.getAllDestinations();

        // Define the column names
        String[] columnNames = {"Select", "Destination ID", "Address", "Contact Name", "Phone Number"};

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
        for (int destinationId : destinationMap.keySet()) {
            ASite destination = destinationMap.get(destinationId);
            Object[] rowData = new Object[5];
            rowData[0] = false; // Checkbox initial state
            rowData[1] = destinationId;
            rowData[2] = destination.getAddress();
            rowData[3] = destination.getContact_name();
            rowData[4] = destination.getPhone_number();
            tableModel.addRow(rowData);
        }

        // Create a JTable using the table model
        JTable destinationTable = new JTable(tableModel);

        // Set the checkbox column width
        TableColumnModel columnModel = destinationTable.getColumnModel();
        columnModel.getColumn(0).setMaxWidth(50);
        columnModel.getColumn(0).setMinWidth(50);
        columnModel.getColumn(0).setCellRenderer(destinationTable.getDefaultRenderer(Boolean.class));
        columnModel.getColumn(0).setCellEditor(destinationTable.getDefaultEditor(Boolean.class));
        columnModel.getColumn(0).setResizable(false);

        // Disable multiple row selection
        destinationTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Create a scroll pane to contain the JTable
        JScrollPane scrollPane = new JScrollPane(destinationTable);

        destinationPanel.add(scrollPane, BorderLayout.CENTER);


        JButton returnButton = new JButton("Return to Main Menu");
        JButton selectButton = new JButton("Select");
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
                int selectedRow = destinationTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int sourceId = (int) tableModel.getValueAt(selectedRow, 1); // Get the source ID from the selected row
                    ASite selectedDestination = destinationMap.get(sourceId); // Get the corresponding source
                    destination_of_order = selectedDestination; // Store the selected source in the source_of_order field
                    cardLayout.show(cards, "Choose Products"); // Move to the destinationCard
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
        destinationTable.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(new JCheckBox()));
        destinationTable.getColumnModel().getColumn(0).setCellRenderer(destinationTable.getDefaultRenderer(Boolean.class));
        destinationTable.getModel().addTableModelListener(e -> checkboxListener.actionPerformed(null));


        JPanel buttonsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints buttonConstraints = new GridBagConstraints();
        buttonConstraints.gridx = 0;
        buttonConstraints.gridy = 0;
        buttonConstraints.weightx = 1; // Allow the buttons to expand horizontally
        buttonConstraints.fill = GridBagConstraints.HORIZONTAL;

        buttonsPanel.add(returnButton, buttonConstraints);
        buttonConstraints.gridx++;
        buttonsPanel.add(selectButton, buttonConstraints);
        buttonConstraints.gridx++;
        buttonsPanel.add(returnButton, buttonConstraints);
        buttonConstraints.weightx = 1; // Allow the empty component to expand horizontally
        buttonsPanel.add(Box.createHorizontalGlue(), buttonConstraints);

        JPanel chooseDestCard = new JPanel(new BorderLayout());
        chooseDestCard.add(destinationPanel, BorderLayout.CENTER);
        chooseDestCard.add(buttonsPanel, BorderLayout.SOUTH);

        return chooseDestCard;
    }

    private int getSelectedIndex(ButtonGroup buttonGroup) {
        Enumeration<AbstractButton> buttons = buttonGroup.getElements();
        int index = 0;
        while (buttons.hasMoreElements()) {
            AbstractButton button = buttons.nextElement();
            if (button.isSelected()) {
                return index;
            }
            index++;
        }
        return -1; // No checkbox selected
    }

    private ASite getSourceByIndex(HashMap<Integer, ASite> sourceMap, int index) {
        for (int sourceId : sourceMap.keySet()) {
            ASite source = sourceMap.get(sourceId);
            if (index == 0) {
                return source; // Return the source at the specified index
            }
            index--;
        }
        return null; // Invalid index
    }
    private ASite getDestinationByIndex(HashMap<Integer, ASite> destinationMap, int index) {
        for (int destinationId : destinationMap.keySet()) {
            ASite destination = destinationMap.get(destinationId);
            if (index == 0) {
                return destination; // Return the source at the specified index
            }
            index--;
        }
        return null; // Invalid index
    }


    // Method to check if a string is an integer
    private boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}