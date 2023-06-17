package InterfaceLayer.GUI;
import BusinessLayer.InventoryBusinessLayer.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SalesGUI extends JFrame {
    private JLabel productIdLabel;
    private JTextField productIdField;
    private JLabel amountLabel;
    private JSpinner productAmountSpinner;
    private JButton addButton;
    private JTextArea receiptTextArea;
    private JLabel totalPriceLabel;
    private JButton finishButton;
    private Branch branch;

    private List<Item> itemsInSale;
    private MainController mainController;
    private JFrame branchMenu;

    public SalesGUI(Branch _branch, MainController _mainController, JFrame _branchMenu) {
        // Initialize the frame
        setTitle("Sales GUI");

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10 ,10));

        JPanel productPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        JPanel northPanel = new JPanel(new GridLayout(1, 2, 10, 10));



        // Create components
        productIdLabel = new JLabel("Product ID:");
        productIdField = new JTextField(10);
        amountLabel = new JLabel("Amount: ");
        productAmountSpinner = new JSpinner(new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));
        addButton = new JButton("Add");
        receiptTextArea = new JTextArea(10, 30);
        totalPriceLabel = new JLabel("Total Price: 0.00 ₪");
        totalPriceLabel.setFont(new Font("Arial", Font.BOLD, 15));
        finishButton = new JButton("Finish");
        branch = _branch;
        mainController = _mainController;
        branchMenu = _branchMenu;

        // Add components to the frame


        productPanel.add(productIdLabel);
        productPanel.add(amountLabel);
        productPanel.add(new JLabel());
        productPanel.add(productIdField);
        productPanel.add(productAmountSpinner);
        productPanel.add(addButton);
        panel.add(productPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(receiptTextArea), BorderLayout.CENTER);
        northPanel.add(totalPriceLabel);
        northPanel.add(finishButton);
        panel.add(northPanel, BorderLayout.SOUTH);

        getContentPane().add(panel);

        // Initialize the item list
        itemsInSale = new ArrayList<>();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                branchMenu.setVisible(true);
            }
        });

        // Add action listener to the Add button
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    addProductToSale();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        // Add action listener to the Finish button
        finishButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    finishSale();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        setSize(400, 400);
        pack();
        setLocationRelativeTo(null);

    }

    private void addProductToSale() throws SQLException {
        int itemCounter = 0;
        int productID = 0;
        try {
            productID = Integer.parseInt(productIdField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter an product ID (integer)", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Product productToSell = mainController.getProductController().getProduct(productID);
        if (productToSell == null) {
            JOptionPane.showMessageDialog(this, "Unknown product ID. Please try again", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<Item> itemInStore = mainController.getItemsDao().getAllStoreItemsByBranchIDAndProductID(branch.getBranchID(), productToSell.getProductID());

        if (itemInStore.size() < (int)productAmountSpinner.getValue()) {
            JOptionPane.showMessageDialog(this, "At the moment, we are unable to make a sale due to the lack of all the products in the store, the maximum amount of this item is: " + itemInStore.size(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        for (int i = 0; i < (int)productAmountSpinner.getValue(); i++)
        {
            Item itemToSale = mainController.getItemsDao().getItemForSale(productID, branch.getBranchID());
            if (itemToSale == null) {
                JOptionPane.showMessageDialog(this, "We currently don't have items from the product you want. Please try again", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            itemToSale = mainController.getItemsDao().updateItemStatus(itemToSale.getItemID(), "Sold");
            itemsInSale.add(itemToSale);
        }

        // Clear the product ID field
        productIdField.setText("");
        productAmountSpinner.setValue(1);

        // Update the receipt text area
        updateReceipt();
    }

    private void updateReceipt() throws SQLException {
        StringBuilder receipt = new StringBuilder();
        double totalPrice = 0;
        for (Item itemToCheckPrice : itemsInSale) {
            itemToCheckPrice = mainController.PriceCalculationAfterDiscount(itemToCheckPrice, branch.getBranchID());
            totalPrice += itemToCheckPrice.getPriceAfterDiscount();
            receipt.append("Product Name: ").append(itemToCheckPrice.getProduct().getProductName())
                    .append(", Price before Discount: ").append(itemToCheckPrice.getPriceInBranch())
                    .append(", Price after Discount: ").append(String.format("%.2f", itemToCheckPrice.getPriceAfterDiscount()))
                    .append("\n");
        }
        receiptTextArea.setText(receipt.toString());
        totalPriceLabel.setText("Total Price: " + String.format("%.2f", totalPrice) + " ₪");
    }

    private void finishSale() throws SQLException {
        if (itemsInSale.size() == 0) {
            JOptionPane.showMessageDialog(this, "No products were added during the purchase.", "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Perform necessary actions to complete the sale
        mainController.getItemsDao().fromStorageToStore(branch);

        JOptionPane.showMessageDialog(this, "Sale completed.", "Information", JOptionPane.INFORMATION_MESSAGE);

        // Close the GUI
        dispose();
        branchMenu.setVisible(true);
    }
}