package PresentationLayer.GUI.TransportManager;

import BuisnessLayer.TransportManager.TransportDocument;
import BuisnessLayer.TransportManager.TransportOrder;
import BuisnessLayer.TransportManager.ATruck;
import ServiceLayer.TransportManager.TransportService;
import BuisnessLayer.HR.Driver;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;


public class Case4NewTransportDocumentFrame extends JFrame {
    private TransportService transportService;
    private TransportOrder transportOrder;
    private TransportDocument transportDocument;
    private TransportFrameMenu parentFrame;
    private String orderType;
    private String shippingArea;
    private ATruck truck;
    private  String chosenDriverID;
    private int selectedRow = -1;

    public Case4NewTransportDocumentFrame(TransportFrameMenu parentFrame) {

        setTitle("Create a New Transport Document");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000,1000);
        setLayout(new BorderLayout());

        //JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel transportOrderPanel = new JPanel(new BorderLayout());

        this.parentFrame = parentFrame;
        this.transportService = new TransportService();
        ArrayList<TransportOrder> allOrders = transportService.getAll_unsigned_transport_orders();

        String[] columnNames = {"Select", "Transport Order ID", "Assigned to Transport Document ID", "Source ID", "Destination ID", "Products"};

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

        for (TransportOrder curr_order : allOrders){
            Object[] rowData = new Object[6];
            rowData[0] = false;
            rowData[1] = curr_order.getTransport_order_id();
            rowData[2] = curr_order.getAssigned_doc_id();
            rowData[3] = curr_order.getSource().getID();
            rowData[4] = curr_order.getDestination().getID();
            rowData[5] = curr_order.productListToString();
            tableModel.addRow(rowData);
        }

        // Create a JTable using the table model
        JTable transportOrdersTable = new JTable(tableModel);

        // Set the checkbox column width
        TableColumnModel columnModel = transportOrdersTable.getColumnModel();
        columnModel.getColumn(0).setMaxWidth(50);
        columnModel.getColumn(0).setMinWidth(50);
        columnModel.getColumn(0).setCellRenderer(transportOrdersTable.getDefaultRenderer(Boolean.class));
        columnModel.getColumn(0).setCellEditor(transportOrdersTable.getDefaultEditor(Boolean.class));
        columnModel.getColumn(0).setResizable(false);

        // Disable multiple row selection
        transportOrdersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Create a scroll pane to contain the JTable
        JScrollPane scrollPane = new JScrollPane(transportOrdersTable);

        JButton returnButton = new JButton("Return to Main Menu");
        JButton createNewDocumentButton = new JButton("Create New Transport Document");
        JButton addToExistingDocumentButton = new JButton("Add to Existing Transport Document");
        createNewDocumentButton.setEnabled(false); // Initially disabled until a row is selected
        addToExistingDocumentButton.setEnabled(false); // Initially disabled until a row is selected

        // Add action listeners to the buttons
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parentFrame.setVisible(true); // Show the TransportFrameMenu
                dispose(); // Close the current frame (Case3TrucksFrame)
            }
        });


        createNewDocumentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createNewDocumentButton.setVisible(false);
                addToExistingDocumentButton.setVisible(false);
                transportOrdersTable.setVisible(false);
                //titleLabel.setVisible(false);
                returnButton.setVisible(false);
                createNewTransportDocument();
            }
        });

        addToExistingDocumentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createNewDocumentButton.setVisible(false);
                addToExistingDocumentButton.setVisible(false);
                transportOrdersTable.setVisible(false);
                //titleLabel.setVisible(false);
                returnButton.setVisible(false);
                addToExistingTransportDocument();
            }
        });

        // Add a listener to enable/disable the select button based on checkbox selection
        ListSelectionListener selectionListener = new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int newlySelectedRow = transportOrdersTable.getSelectedRow();
                boolean isSelected = (newlySelectedRow >= 0) && (Boolean) tableModel.getValueAt(newlySelectedRow, 0);

                if (selectedRow >= 0 && selectedRow != newlySelectedRow) {
                    // Clear the "v" from the old selection
                    tableModel.setValueAt(false, selectedRow, 0);
                }

                selectedRow = newlySelectedRow;
                createNewDocumentButton.setEnabled(isSelected);
                addToExistingDocumentButton.setEnabled(isSelected);

                if (!isSelected) {
                    createNewDocumentButton.setEnabled(false);
                    addToExistingDocumentButton.setEnabled(false);
                }
                else {
                    // Assign the selected transport order to the transportOrder attribute
                    int transportOrderID = (int) tableModel.getValueAt(newlySelectedRow, 1); // Assuming the transport order ID is stored in column 1
                    transportOrder = transportService.get_transport_order_by_id(transportOrderID); // Implement this method to find the transport order based on its ID
                    shippingArea = transportOrder.getDestination().getAddress().getShipping_area();
                    orderType = transportOrder.getProducts_list().keySet().iterator().next().getType();

                }
            }
        };
        // Replace the buttonPanel declaration with the following
        JPanel buttonPanel2 = new JPanel(new GridBagLayout());

        // Create a GridBagConstraints object
        GridBagConstraints buttonConstraints = new GridBagConstraints();
        buttonConstraints.gridx = 0;
        buttonConstraints.gridy = 0;
        buttonConstraints.fill = GridBagConstraints.HORIZONTAL;
        buttonConstraints.weightx = 1.0;

        // Add the "Create New Transport Document" button
        buttonPanel2.add(createNewDocumentButton, buttonConstraints);

        // Increment the gridy value for the next button
        buttonConstraints.gridx++;

        // Add the "Add to Existing Transport Document" button
        buttonPanel2.add(addToExistingDocumentButton, buttonConstraints);

        // Increment the gridy value for the next button
        buttonConstraints.gridx++;

        // Add the "Return to Main Menu" button
        buttonPanel2.add(returnButton, buttonConstraints);

        // Add the selection listener to the table
        transportOrdersTable.getSelectionModel().addListSelectionListener(selectionListener);
        transportOrderPanel.add(scrollPane, BorderLayout.CENTER);
        transportOrderPanel.add(buttonPanel2,BorderLayout.SOUTH);

        setContentPane(transportOrderPanel);

        setVisible(true);
    }

    private void createNewTransportDocument() {
        JPanel panel = new JPanel();
        panel.setLayout(null);

        JButton checkStorekeeperButton = new JButton("Check if a storekeeper is available");
        checkStorekeeperButton.setBounds(50, 190, 250, 25);
        checkStorekeeperButton.setEnabled(false);
        panel.add(checkStorekeeperButton);

        JLabel dateLabel = new JLabel("Enter the date of the transport in format yyyy-mm-dd");
        dateLabel.setBounds(50, 50, 300, 20);
        dateLabel.setOpaque(true);
        panel.add(dateLabel);

        JTextField dateField = new JTextField();
        dateField.setBounds(50, 80, 100, 25);
        panel.add(dateField);

        JLabel startTimeLabel = new JLabel("Enter the start time of the shift in the format hh:mm:ss");
        startTimeLabel.setBounds(50, 120, 300, 20);
        startTimeLabel.setOpaque(true);
        panel.add(startTimeLabel);

        JTextField startTimeField = new JTextField();
        startTimeField.setBounds(50, 150, 100, 25);
        panel.add(startTimeField);

        JButton returnButton = new JButton("Return Main Menu");
        returnButton.setBounds(50, 230, 250, 25);
        panel.add(returnButton);

        getContentPane().removeAll();
        getContentPane().add(panel);
        revalidate();
        repaint();

        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parentFrame.setVisible(true);
                dispose();
            }
        });
        // Add KeyListener to the dateField
        dateField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String dateText = dateField.getText();
                boolean isValidDate = isValidDateFormat(dateText, "yyyy-MM-dd");
                checkStorekeeperButton.setEnabled(isValidDate && isValidTimeFormat(startTimeField.getText(), "HH:mm:ss"));
            }
        });

        // Add KeyListener to the startTimeField
        startTimeField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String timeText = startTimeField.getText();
                boolean isValidTime = isValidTimeFormat(timeText, "HH:mm:ss");
                checkStorekeeperButton.setEnabled(isValidTime && isValidDateFormat(dateField.getText(), "yyyy-MM-dd"));
            }
        });

        checkStorekeeperButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String dateText = dateField.getText();
                String timeText = startTimeField.getText();

                LocalDate date = LocalDate.parse(dateText);
                LocalTime startTime = LocalTime.parse(timeText);

                boolean existsStorekeeper = transportService.check_if_storeKeeperA_assigned(
                        transportOrder.getDestination().getID(), date, startTime);

                if (!existsStorekeeper) {
                    int option = JOptionPane.showOptionDialog(Case4NewTransportDocumentFrame.this,
                            "There aren't any storekeepers available at this date. Do you want to try another date?",
                            "No Storekeeper Available",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            new Object[]{"Yes", "No"},
                            JOptionPane.YES_OPTION);

                    if (option == JOptionPane.NO_OPTION) {
                        setVisible(false);
                        parentFrame.setVisible(true);
                    } else {
                        dateField.setText("");
                        startTimeField.setText("");
                    }
                } else {
                    startTimeLabel.setVisible(false);
                    startTimeField.setVisible(false);
                    dateLabel.setVisible(false);
                    dateField.setVisible(false);
                    checkStorekeeperButton.setVisible(false);
                    setVisible(false);
                    createDocument(date, startTime);
                }
            }
        });
    }

    // Helper method to check if a date string matches a specific format
    private boolean isValidDateFormat(String dateText, String format) {
        try {
            LocalDate.parse(dateText, DateTimeFormatter.ofPattern(format));
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    // Helper method to check if a time string matches a specific format
    private boolean isValidTimeFormat(String timeText, String format) {
        try {
            LocalTime.parse(timeText, DateTimeFormatter.ofPattern(format));
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }


    private void addToExistingTransportDocument() {
        setTitle("Add to Existing Transport Document");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 1000);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel addToExistingPanel = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        ArrayList<TransportDocument> allTransportDocuments = transportService.getAll_transport_documents_by_type(orderType, shippingArea);

        if (allTransportDocuments.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "There aren't any relevant Transport Documents. Please create a new Transport Document.",
                    "No Relevant Transport Documents",
                    JOptionPane.INFORMATION_MESSAGE);
            createNewTransportDocument();
        } else {

            String[] columnNames = {"Select", "Transport Document ID", "Date", "Departure Time", "Shipping Area", "Type", "Status", "Holding Transport Orders IDs'"};

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

            for (TransportDocument curr_doc : allTransportDocuments) {
                Object[] rowData = new Object[8];
                rowData[0] = false;
                rowData[1] = curr_doc.getTransport_document_ID();
                rowData[2] = curr_doc.getDate();
                rowData[3] = curr_doc.getDeparture_time();
                rowData[4] = curr_doc.getShipping_area();
                rowData[5] = curr_doc.getTransport_type();
                rowData[6] = curr_doc.getDoc_status();
                rowData[7] = curr_doc.get_all_transport_orders_id_in_string();
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

            JButton executeButton = new JButton("Execute Transport");
            executeButton.setEnabled(false);

            executeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    transportOrder.setAssigned_doc_id(transportDocument.getTransport_document_ID());
                    transportService.update_transport_order(transportOrder);
                    transportService.updating_documents_in_the_system(transportOrder, transportDocument.getTransport_document_ID());
                    JOptionPane.showMessageDialog(Case4NewTransportDocumentFrame.this,
                            "Finished adding your transport order to an existing transport document",
                            "Transport Order Added",
                            JOptionPane.INFORMATION_MESSAGE);
                    setVisible(false);
                    parentFrame.setVisible(true);

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
                    executeButton.setEnabled(isSelected);

                    if (!isSelected) {
                        executeButton.setEnabled(false);
                    } else {
                        // Assign the selected transport order to the transportOrder attribute
                        int transportDocumentID = (int) tableModel.getValueAt(newlySelectedRow, 1); // Assuming the transport order ID is stored in column 1
                        transportDocument = transportService.get_transport_document_by_id(transportDocumentID); // Implement this method to find the transport order based on its ID

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
                    executeButton.setEnabled(isSelected);
                }
            });

            JButton returnButton = new JButton("Return to Main Menu");
            returnButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    parentFrame.setVisible(true);
                    dispose();
                }
            });

            buttonPanel.add(executeButton);
            buttonPanel.add(returnButton);

            docsTable.getSelectionModel().addListSelectionListener(selectionListener);

            addToExistingPanel.add(scrollPane, BorderLayout.CENTER);
            addToExistingPanel.add(buttonPanel, BorderLayout.SOUTH);

            mainPanel.add(addToExistingPanel, BorderLayout.CENTER);

            setContentPane(mainPanel);
            setVisible(true);
        }
    }



    private void createDocument(LocalDate date, LocalTime startTime) {

        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel truckPanel = new JPanel(new BorderLayout());

        int levelSkill;
        if (orderType.equals("Dry")) {
            levelSkill = 1;
        } else if (orderType.equals("Cooler")) {
            levelSkill = 2;
        } else {
            levelSkill = 3;
        }

        ArrayList<ATruck> allTrucks = transportService.getAll_trucks_by_type_and_available(levelSkill, date);
        if (allTrucks.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No available trucks at this moment. Please try again later",
                    "No Available Trucks",
                    JOptionPane.INFORMATION_MESSAGE);
            setVisible(false);
            parentFrame.setVisible(true);
            return;
        }

        String[] columnNames = {"Select", "License Plate Number", "Type", "Net Weight", "Max Cargo Weight", "Status"};

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


        for (ATruck curr_truck : allTrucks){
            Object[] rowData = new Object[6];
            rowData[0] = false;
            rowData[1] = curr_truck.getLicense_plate_number();
            rowData[2] = curr_truck.getType();
            rowData[3] = curr_truck.getNet_weight();
            rowData[4] = curr_truck.getMax_cargo_weight();
            rowData[5] = curr_truck.getStatus();
            tableModel.addRow(rowData);
        }

        // Create a JTable using the table model
        JTable trucksTable = new JTable(tableModel);

        // Set the checkbox column width
        TableColumnModel columnModel = trucksTable.getColumnModel();
        columnModel.getColumn(0).setMaxWidth(50);
        columnModel.getColumn(0).setMinWidth(50);
        columnModel.getColumn(0).setCellRenderer(trucksTable.getDefaultRenderer(Boolean.class));
        columnModel.getColumn(0).setCellEditor(trucksTable.getDefaultEditor(Boolean.class));
        columnModel.getColumn(0).setResizable(false);

        // Disable multiple row selection
        trucksTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(trucksTable);
        JButton returnButton = new JButton("Return Main Menu");
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parentFrame.setVisible(true);
                dispose();
            }
        });

        JButton searchDriverButton = new JButton("Search a suitable driver");
        searchDriverButton.setEnabled(false);

        searchDriverButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double netWeight = truck.getNet_weight();
                int truckLevel = truck.getTruck_level();

                chooseDriver(netWeight, truckLevel, date, startTime);
            }
        });

        // Add a listener to enable/disable the select button based on checkbox selection
        ListSelectionListener selectionListener = new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int newlySelectedRow = trucksTable.getSelectedRow();
                boolean isSelected = (newlySelectedRow >= 0) && (Boolean) tableModel.getValueAt(newlySelectedRow, 0);

                if (selectedRow >= 0 && selectedRow != newlySelectedRow) {
                    // Clear the "v" from the old selection
                    tableModel.setValueAt(false, selectedRow, 0);
                }

                selectedRow = newlySelectedRow;
                searchDriverButton.setEnabled(isSelected);

                if (!isSelected) {
                    searchDriverButton.setEnabled(false);
                } else {
                    int truck_license_plate = (int) tableModel.getValueAt(newlySelectedRow,1);
                    truck = transportService.get_truck(truck_license_plate);
                }
            }
        };

        JPanel buttonsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints buttonConstraints = new GridBagConstraints();
        buttonConstraints.gridx = 0;
        buttonConstraints.gridy = 0;
        buttonConstraints.weightx = 1; // Allow the buttons to expand horizontally
        buttonConstraints.fill = GridBagConstraints.HORIZONTAL;

        buttonsPanel.add(searchDriverButton, buttonConstraints);
        buttonConstraints.gridx++;
        buttonsPanel.add(returnButton, buttonConstraints);

        buttonConstraints.weightx = 1; // Allow the empty component to expand horizontally
        buttonsPanel.add(Box.createHorizontalGlue(), buttonConstraints);

        trucksTable.getSelectionModel().addListSelectionListener((selectionListener));
        truckPanel.add(scrollPane,BorderLayout.CENTER);
        truckPanel.add(buttonsPanel,BorderLayout.SOUTH);
        mainPanel.add(truckPanel,BorderLayout.CENTER);
        setTitle("Choose Truck");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setContentPane(mainPanel);
        setVisible(true);
    }

    private void chooseDriver(double netWeight, int truckLevel, LocalDate date, LocalTime startTime) {


        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel driverPanel = new JPanel(new BorderLayout());

        ArrayList<Driver> allRelevantDrivers = transportService.get_all_available_drivers_controller(date, startTime, netWeight, truckLevel);
        if (allRelevantDrivers.size() == 0) {
            JOptionPane.showMessageDialog(this,
                    "There are no available Drivers for this shift. Please try again later",
                    "No Available Employees",
                    JOptionPane.INFORMATION_MESSAGE);
            setVisible(false);
            parentFrame.setVisible(true);
            return;
        }

        String[] columnNames = {"Select", "Name", "ID", "Max Weight Allowed", "License"};

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


        for (Driver curr_driver : allRelevantDrivers){
            Object[] rowData = new Object[5];
            rowData[0] = false;
            rowData[1] = curr_driver.employeeFullName;
            rowData[2] = curr_driver.employeeID;
            rowData[3] = curr_driver.getDriver_max_weight_allowed();
            rowData[4] = curr_driver.getDriver_license();

            tableModel.addRow(rowData);
        }

        // Create a JTable using the table model
        JTable driversTable = new JTable(tableModel);

        // Set the checkbox column width
        TableColumnModel columnModel = driversTable.getColumnModel();
        columnModel.getColumn(0).setMaxWidth(50);
        columnModel.getColumn(0).setMinWidth(50);
        columnModel.getColumn(0).setCellRenderer(driversTable.getDefaultRenderer(Boolean.class));
        columnModel.getColumn(0).setCellEditor(driversTable.getDefaultEditor(Boolean.class));
        columnModel.getColumn(0).setResizable(false);

        // Disable multiple row selection
        driversTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(driversTable);

        JButton createTransportDocumentButton = new JButton("Create Transport Document");
        createTransportDocumentButton.setEnabled(false);

        createTransportDocumentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                ArrayList<TransportOrder> all_transport_orders_into_document = new ArrayList<TransportOrder>();
                all_transport_orders_into_document.add(transportOrder);
                Driver chosen_driver = transportService.get_driver_in_company_controller(chosenDriverID);
                transportService.creating_new_transport_document(date, truck.getLicense_plate_number(), startTime, chosen_driver, all_transport_orders_into_document, shippingArea, orderType);
                transportService.update_truck_transports_controller(truck, date);
                transportService.add_driver_to_shift_controller(chosenDriverID, date, startTime);
                JOptionPane.showMessageDialog(Case4NewTransportDocumentFrame.this,
                        "Transport document created successfully.",
                        "Transport Document Created",
                        JOptionPane.INFORMATION_MESSAGE);

                setVisible(false);
                parentFrame.setVisible(true);


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
                createTransportDocumentButton.setEnabled(isSelected);


                if (!isSelected) {
                    createTransportDocumentButton.setEnabled(false);
                } else {
                    chosenDriverID = tableModel.getValueAt(newlySelectedRow,2).toString();
                }
            }
        };

        JButton returnButton = new JButton("Return Main Menu");

        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parentFrame.setVisible(true);
                dispose();
            }
        });

        JPanel buttonsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints buttonConstraints = new GridBagConstraints();
        buttonConstraints.gridx = 0;
        buttonConstraints.gridy = 0;
        buttonConstraints.weightx = 1; // Allow the buttons to expand horizontally
        buttonConstraints.fill = GridBagConstraints.HORIZONTAL;

        buttonsPanel.add(createTransportDocumentButton, buttonConstraints);
        buttonConstraints.gridx++;
        buttonsPanel.add(returnButton, buttonConstraints);

        buttonConstraints.weightx = 1; // Allow the empty component to expand horizontally
        buttonsPanel.add(Box.createHorizontalGlue(), buttonConstraints);

        driversTable.getSelectionModel().addListSelectionListener((selectionListener));
        driverPanel.add(scrollPane,BorderLayout.CENTER);
        driverPanel.add(buttonsPanel,BorderLayout.SOUTH);

        mainPanel.add(driverPanel,BorderLayout.CENTER);

        setTitle("Choose Driver");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setContentPane(mainPanel);
        setVisible(true);
    }
}
