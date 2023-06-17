package PresentationLayer.GUI.TransportManager;

import BuisnessLayer.TransportManager.ATruck;
import BuisnessLayer.TransportManager.TransportOrder;
import ServiceLayer.TransportManager.TransportService;
import BuisnessLayer.TransportManager.TransportDocument;

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
import java.util.ArrayList;

public class Case5ExecuteTransportFrame extends JFrame implements ActionListener {

    private JPanel cards;
    private CardLayout cardLayout;
    private TransportService transportService;
    TransportDocument transportDocument;
    private int selectedRow = -1;

    public Case5ExecuteTransportFrame(TransportFrameMenu transportFrameMenu) {
        transportService = new TransportService();

        // Set up the JFrame
        setTitle("Execute Transport");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 1000);

        // Create the card layout and panel
        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);
        getContentPane().add(cards, BorderLayout.CENTER);

        // Create the cards for different views
        JPanel enterTransportDocumentIDCard = createChooseTransportDocumentCard(transportFrameMenu);

        // Add the cards to the card layout
        cards.add(enterTransportDocumentIDCard, "Enter Transport Document ID");

        // Display the enter transport document ID card by default
        cardLayout.show(cards, "Enter Transport Document ID");
        setVisible(true);

    }

    private JPanel createChooseTransportDocumentCard(TransportFrameMenu transportFrameMenu) {
        JPanel docsPanel = new JPanel(new BorderLayout());

        ArrayList<TransportDocument> all_waiting_transport_documents = transportService.getAll_waiting_transport_documents();

        if (all_waiting_transport_documents.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "There are no Transport Documents waiting to be executed.",
                    "No Transport Documents Waiting",
                    JOptionPane.INFORMATION_MESSAGE);
            transportFrameMenu.setVisible(true);
            return docsPanel;
        }

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

        for (TransportDocument curr_doc : all_waiting_transport_documents) {
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
                int selectedRow = docsTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int doc_id = (int) tableModel.getValueAt(selectedRow, 1);
                    TransportDocument transportDocument = transportService.get_transport_document_by_id(doc_id);
                    transportService.execute_transport_controller(transportDocument);
                    JOptionPane.showMessageDialog(null, "Transport executed successfully");
                    transportFrameMenu.setVisible(true);
                    dispose();
                }
            }
        });

        ListSelectionListener selectionListener = new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int newlySelectedRow = docsTable.getSelectedRow();
                boolean isSelected = (newlySelectedRow >= 0) && (Boolean) tableModel.getValueAt(newlySelectedRow, 0);

                executeButton.setEnabled(isSelected);
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

        docsTable.getSelectionModel().addListSelectionListener(selectionListener);

        JButton returnButton = new JButton("Return to Main Menu");
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                transportFrameMenu.setVisible(true);
                dispose();
            }
        });

        docsPanel.add(scrollPane, BorderLayout.CENTER);

        GridBagConstraints buttonConstraints = new GridBagConstraints();
        JPanel buttonsPanel = new JPanel(new GridBagLayout());
        buttonConstraints.gridx = 0;
        buttonConstraints.gridy = 0;
        buttonConstraints.weightx = 1; // Allow the buttons to expand horizontally
        buttonConstraints.fill = GridBagConstraints.HORIZONTAL;

        buttonsPanel.add(executeButton, buttonConstraints);
        buttonConstraints.gridx++;
        buttonsPanel.add(returnButton, buttonConstraints);

        buttonConstraints.weightx = 1; // Allow the empty component to expand horizontally
        buttonsPanel.add(Box.createHorizontalGlue(), buttonConstraints);

        JPanel chooseShipmentCard = new JPanel(new BorderLayout());
        chooseShipmentCard.add(docsPanel, BorderLayout.CENTER);
        chooseShipmentCard.add(buttonsPanel, BorderLayout.SOUTH);

        return chooseShipmentCard;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        // Handle action events
    }
}
