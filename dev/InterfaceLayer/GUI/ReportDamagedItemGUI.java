package InterfaceLayer.GUI;

import BusinessLayer.InventoryBusinessLayer.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

public class ReportDamagedItemGUI extends JFrame {
    private JLabel productIdLabel;
    private JTextField productIdField;
    private JLabel itemIdLabel;
    private JTextField itemIdField;
    private JLabel descriptionLabel;

    private JTextField descriptionTextField;
    private JButton reportButton;

    private MainController mainController;
    private Branch branch;
    private JFrame branchMenu;

    public ReportDamagedItemGUI(Branch _branch, MainController _mainController, JFrame _branchMenu) {
        // Initialize the frame
        setTitle("Report Damaged Item Menu");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new FlowLayout());

        // Create components
        productIdLabel = new JLabel("Product ID:");
        productIdField = new JTextField(10);
        itemIdLabel = new JLabel("Item ID:");
        itemIdField = new JTextField(10);
        descriptionLabel = new JLabel("Defect Description:");
        descriptionTextField = new JTextField(20);
        reportButton = new JButton("Report");
        branch = _branch;
        mainController = _mainController;
        branchMenu = _branchMenu;

        // Add components to the frame
        add(productIdLabel);
        add(productIdField);
        add(itemIdLabel);
        add(itemIdField);
        add(descriptionLabel);
        add(new JScrollPane(descriptionTextField));
        add(reportButton);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                branchMenu.setVisible(true);
            }
        });

        // Add action listener to the Report button
        reportButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    reportDamagedItem();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    private void reportDamagedItem() throws SQLException {
        int productID = Integer.parseInt(productIdField.getText());
        int itemID = Integer.parseInt(itemIdField.getText());

        Product productDef = mainController.getProductController().getProduct(productID);
        if (productDef != null) {
            Item itemDef = mainController.getItemsDao().getItemByID(itemID);
            if (itemDef == null) {
                JOptionPane.showMessageDialog(this, "There is no item with the specified ID", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (itemDef.getBranchID() != branch.getBranchID()) {
                JOptionPane.showMessageDialog(this, "The item you specified does not belong to this branch", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (itemDef.getStatusType() == StatusEnum.Damaged) {
                JOptionPane.showMessageDialog(this, "This item has already been reported as damaged", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (itemDef.getStatusType() == StatusEnum.Sold) {
                JOptionPane.showMessageDialog(this, "This item has already been sold and you cannot report it as damaged", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (itemDef.getProduct().getProductID() != productDef.getProductID()) {
                JOptionPane.showMessageDialog(this, "There is a mismatch between the product ID and the item ID", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String description = descriptionTextField.getText();
            itemDef = mainController.getItemsDao().updateItemStatus(itemID, "Damaged");
            itemDef = mainController.getItemsDao().updateItemDefectiveDescription(itemDef.getItemID(), description);
            mainController.getItemsDao().fromStorageToStore(branch);

            JOptionPane.showMessageDialog(this, "The item has been successfully updated:\n" + itemDef, "Information", JOptionPane.INFORMATION_MESSAGE);

            // Clear the fields
            productIdField.setText("");
            itemIdField.setText("");
            descriptionTextField.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "The ID of the product you specified does not exist in the system", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
