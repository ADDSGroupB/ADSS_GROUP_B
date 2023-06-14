package InterfaceLayer.GUI;
import BusinessLayer.InventoryBusinessLayer.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SalesGUI extends JFrame {
    private JLabel productIdLabel;
    private JTextField productIdField;
    private JButton addButton;
    private JTextArea receiptTextArea;
    private JButton finishButton;
    private Branch branch;

    private List<Item> itemsInSale;
    private MainController mainController;
    private JFrame branchMenu;

    public SalesGUI(Branch _branch, MainController _mainController, JFrame _branchMenu) {
        // Initialize the frame
        setTitle("Sales GUI");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new FlowLayout());

        // Create components
        productIdLabel = new JLabel("Product ID:");
        productIdField = new JTextField(10);
        addButton = new JButton("Add");
        receiptTextArea = new JTextArea(10, 30);
        finishButton = new JButton("Finish");
        branch = _branch;
        mainController = _mainController;
        branchMenu = _branchMenu;

        // Add components to the frame
        add(productIdLabel);
        add(productIdField);
        add(addButton);
        add(new JScrollPane(receiptTextArea));
        add(finishButton);

        // Initialize the item list
        itemsInSale = new ArrayList<>();

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
    }

    private void addProductToSale() throws SQLException {
        int productID = 0;
        try {
            productID = Integer.parseInt(productIdField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter an integer", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Product productToSell = mainController.getProductController().getProduct(productID);
        if (productToSell == null) {
            JOptionPane.showMessageDialog(this, "Unknown product ID. Please try again", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<Item> itemInStore = mainController.getItemsDao().getAllStoreItemsByBranchIDAndProductID(branch.getBranchID(), productToSell.getProductID());
        List<Item> itemInStorage = mainController.getItemsDao().getAllStorageItemsByBranchIDAndProductID(branch.getBranchID(), productToSell.getProductID());

        if (itemInStore.size() == 0 && itemInStorage.size() == 0) {
            JOptionPane.showMessageDialog(this, "At the moment, we are unable to make a sale due to the lack of all the products in the store.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Item itemToSale = mainController.getItemsDao().getItemForSale(productID, branch.getBranchID());
        if (itemToSale == null) {
            JOptionPane.showMessageDialog(this, "We currently don't have items from the product you want. Please try again", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        itemToSale = mainController.getItemsDao().updateItemStatus(itemToSale.getItemID(), "Sold");
        itemsInSale.add(itemToSale);

        // Clear the product ID field
        productIdField.setText("");

        // Update the receipt text area
        updateReceipt();
    }

    private void updateReceipt() throws SQLException {
        StringBuilder receipt = new StringBuilder();
        for (Item itemToCheckPrice : itemsInSale) {
            itemToCheckPrice = mainController.PriceCalculationAfterDiscount(itemToCheckPrice, branch.getBranchID());
            receipt.append("Product Name: ").append(itemToCheckPrice.getProduct().getProductName())
                    .append(", Price before Discount: ").append(itemToCheckPrice.getPriceInBranch())
                    .append(", Price after Discount: ").append(itemToCheckPrice.getPriceAfterDiscount())
                    .append("\n");
        }
        receiptTextArea.setText(receipt.toString());
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