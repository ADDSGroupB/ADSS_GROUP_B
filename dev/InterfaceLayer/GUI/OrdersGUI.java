package InterfaceLayer.GUI;

import BusinessLayer.InventoryBusinessLayer.Branch;
import BusinessLayer.InventoryBusinessLayer.MainController;
import BusinessLayer.SupplierBusinessLayer.Order;
import BusinessLayer.SupplierBusinessLayer.PeriodicOrder;
import BusinessLayer.SupplierBusinessLayer.SupplierProduct;
import InterfaceLayer.Main;
import ServiceLayer.SupplierServiceLayer.OrderService;
import Utillity.Response;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class OrdersGUI extends JFrame {
    private OrderService orderService;
    private Branch branch;
    private JFrame branchMenu;
    public OrdersGUI(Branch _branch, MainController _mainController, JFrame _branchMenu) {
        // Main orders GUI
        JButton periodicOrderButton = new JButton("Periodic Order");
        JButton existingOrderButton = new JButton("Existing Order");
        JButton executePeriodicOrdersButton = new JButton("Execute Periodic Orders For Today");
        JButton executeShortageOrdersButton = new JButton("Execute Shortage Orders For Today");
        JButton printOrdersHistoryButton = new JButton("Print branch's orders history");
        JButton backButton = new JButton("Back To Branch Menu");
        orderService = new OrderService();
        branch = _branch;
        branchMenu = _branchMenu;
//        branch.setBranchID(616);


        setTitle("Orders Menu");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(7, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Orders Menu");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));

        panel.add(titleLabel);
        panel.add(periodicOrderButton);
        panel.add(existingOrderButton);
        panel.add(executePeriodicOrdersButton);
        panel.add(executeShortageOrdersButton);
        panel.add(printOrdersHistoryButton);
        panel.add(backButton);
        getContentPane().add(panel);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                setVisible(false);
                branchMenu.setVisible(true);
            }
        });

        periodicOrderButton.addActionListener(e -> {
            JFrame periodicOrderUI = new JFrame("Periodic Orders Menu");
            openPeriodicOrderUI(periodicOrderUI, branch.getBranchID());
            setVisible(false);
            periodicOrderUI.setVisible(true);
            periodicOrderUI.pack();
        });

        existingOrderButton.addActionListener(e -> {
            JFrame existingOrderUI = new JFrame("Update Existing Orders");
            openExistingOrderUI(existingOrderUI, branch.getBranchID());
            setVisible(false);
            existingOrderUI.setVisible(true);
            existingOrderUI.pack();

        });

        executePeriodicOrdersButton.addActionListener(e -> {
            if(LocalTime.now().isAfter(LocalTime.of(10, 0))) orderService.run();
            else JOptionPane.showMessageDialog(null, "Periodic Orders Will Execute Automatically at 10AM", "Error", JOptionPane.ERROR_MESSAGE);
        });

        executeShortageOrdersButton.addActionListener(e -> {
            if(LocalTime.now().isAfter(LocalTime.of(20, 0))) Main.autoShortage();
            else JOptionPane.showMessageDialog(null, "Shortage Orders Will Execute Automatically at 8PM", "Error", JOptionPane.ERROR_MESSAGE);
        });

        printOrdersHistoryButton.addActionListener(e -> {

            JFrame orderHistoryUI = new JFrame("Branch Order History");
            setVisible(false);
            printOrdersHistory(orderHistoryUI, this);
//            orderHistoryUI.setVisible(true);
            orderHistoryUI.pack();

        });

        backButton.addActionListener(e -> {
            dispose();
            branchMenu.setVisible(true);
        });
    }
    // Option 1 Periodic order menu
    private void openPeriodicOrderUI(JFrame periodicOrderUI, int branchID) {

        periodicOrderUI.setSize(400, 300);
        periodicOrderUI.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        periodicOrderUI.setLocationRelativeTo(null);



//        JFrame removeProductsPeriodicOrderFrame = new JFrame("Remove Products From Periodic Order");
//        setRemoveProductsPeriodicOrderFrame(removeProductsPeriodicOrderFrame, periodicOrderUI, branchID);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Periodic Orders Menu");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));

        JButton createOrderButton = new JButton("Create New Periodic Order");
        JButton updateProductsButton = new JButton("Update Periodic Order");
        JButton backButton = new JButton("Back To Orders Menu");


        panel.add(createOrderButton);
        panel.add(updateProductsButton);
        panel.add(backButton);
        periodicOrderUI.getContentPane().add(panel);

        periodicOrderUI.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                periodicOrderUI.dispose();
                setVisible(true);
                pack();
            }
        });


        createOrderButton.addActionListener(e -> {
            JFrame createPeriodicOrderFrame = new JFrame("Create New Periodic Order");
            setCreatePeriodicOrderFrame(createPeriodicOrderFrame, periodicOrderUI, branchID);
            periodicOrderUI.setVisible(false);
            createPeriodicOrderFrame.setVisible(true);
//            createPeriodicOrderFrame.pack();
        });

        updateProductsButton.addActionListener(e -> {
            JFrame updatePeriodicOrderFrame = new JFrame("Update Periodic Order");
            setUpdatePeriodicOrderFrame(updatePeriodicOrderFrame, periodicOrderUI, branchID);
            periodicOrderUI.setVisible(false);
            updatePeriodicOrderFrame.setVisible(true);
//            updatePeriodicOrderFrame.pack();
        });

        backButton.addActionListener(e -> {
            periodicOrderUI.setVisible(false);
            setVisible(true);
            pack();
        });
    }

    private void setCreatePeriodicOrderFrame(JFrame createPeriodicOrderFrame, JFrame periodicOrderUI, int branchID) {

        //Frame Design
        createPeriodicOrderFrame.setSize(800, 600);
        createPeriodicOrderFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        createPeriodicOrderFrame.setLocationRelativeTo(null);


        AtomicInteger supplierID = new AtomicInteger();

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(0, 0)); // Add spacing between sections
        panel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        JPanel selectSupplierPanel = new JPanel();
        selectSupplierPanel.setLayout(new GridLayout(2, 1, 0, 0));
        JLabel selectSupplierLabel = new JLabel("Please select the the supplier you want to order from:");
        selectSupplierPanel.add(selectSupplierLabel);
        DefaultTableModel supplierChooseTableModel = new DefaultTableModel();
        supplierChooseTableModel.addColumn("Supplier ID");
        supplierChooseTableModel.addColumn("Name");
        JTable supplierChooseTable = new JTable(supplierChooseTableModel);
        JScrollPane supplierChooseScrollPane = new JScrollPane(supplierChooseTable);
        selectSupplierPanel.add(supplierChooseScrollPane);
        selectSupplierPanel.setPreferredSize(new Dimension(800, 200));
        panel.add(selectSupplierPanel, BorderLayout.NORTH);


        JPanel selectProductPanel = new JPanel();
        selectProductPanel.setLayout(new GridLayout(5, 1, 0, 0));
        JLabel selectProductLabel = new JLabel("Please select the Products that you want to order:");
        selectProductPanel.add(selectProductLabel);

        // Make the table uneditable
        DefaultTableModel ProductChooseTableModel = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Set all cells to be not editable
            }
        };
        ProductChooseTableModel.addColumn("Name");
        ProductChooseTableModel.addColumn("Product ID");
        ProductChooseTableModel.addColumn("Catalog Number");
        ProductChooseTableModel.addColumn("Price");
        ProductChooseTableModel.addColumn("Amount");
        ProductChooseTableModel.addColumn("Manufacturer");
        ProductChooseTableModel.addColumn("Expiration Days");
        ProductChooseTableModel.addColumn("Weight");
        JTable ProductChooseTable = new JTable(ProductChooseTableModel);
        JScrollPane ProductChooseScrollPane = new JScrollPane(ProductChooseTable);
        ProductChooseScrollPane.setPreferredSize(new Dimension(ProductChooseScrollPane.getPreferredSize().width, 200));
        selectProductPanel.add(ProductChooseScrollPane);


        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(1, 2, 10, 10));

        JButton chooseButton = new JButton("↓");
        chooseButton.setFont(new Font("Arial", Font.BOLD, 25));
        chooseButton.setForeground(Color.WHITE);
        chooseButton.setBackground(Color.BLACK);
        chooseButton.setPreferredSize(new Dimension(25, 25));
        buttonsPanel.add(chooseButton);

        JButton chooseButton2 = new JButton("↑");
        chooseButton2.setFont(new Font("Arial", Font.BOLD, 25));
        chooseButton2.setForeground(Color.WHITE);
        chooseButton2.setBackground(Color.BLACK);
        chooseButton2.setPreferredSize(new Dimension(25, 25));
        buttonsPanel.add(chooseButton2);

        selectProductPanel.add(buttonsPanel);

        JLabel chosenProductLabel = new JLabel("Chosen products for periodic Order:");
        selectProductPanel.add(chosenProductLabel);

        // Make the table uneditable
        DefaultTableModel chosenProductsTableModel = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 8; // Set all cells to be not editable except column 8
            }
        };
        chosenProductsTableModel.addColumn("Name");
        chosenProductsTableModel.addColumn("Product ID");
        chosenProductsTableModel.addColumn("Catalog Number");
        chosenProductsTableModel.addColumn("Price");
        chosenProductsTableModel.addColumn("Amount");
        chosenProductsTableModel.addColumn("Manufacturer");
        chosenProductsTableModel.addColumn("Expiration Days");
        chosenProductsTableModel.addColumn("Weight");
        chosenProductsTableModel.addColumn("Amount To Order");
        JTable chosenProductsTable = new JTable(chosenProductsTableModel);
        JScrollPane chosenProductsScrollPane = new JScrollPane(chosenProductsTable);
        chosenProductsScrollPane.setPreferredSize(new Dimension(chosenProductsScrollPane.getPreferredSize().width, 200));
        selectProductPanel.add(chosenProductsScrollPane);

        selectProductPanel.setPreferredSize(new Dimension(800, 600));
        panel.add(selectProductPanel, BorderLayout.CENTER);

        HashMap<Integer, Integer> productsAndAmount = new HashMap<>();

        // Event that will execute when row has been chosen on supplier choose table
        supplierChooseTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                chosenProductsTableModel.setRowCount(0);
                ProductChooseTableModel.setRowCount(0);
                productsAndAmount.clear();
                int selectedRow = supplierChooseTable.getSelectedRow();
                supplierID.set(Integer.parseInt(supplierChooseTable.getValueAt(selectedRow, 0).toString()));
                HashMap<Integer, SupplierProduct> supplierProducts = orderService.getAllSupplierProductsByID(supplierID.get());
                for (SupplierProduct supplierProduct : supplierProducts.values())
                    ProductChooseTableModel.addRow(new Object[]{supplierProduct.getName(), supplierProduct.getProductID(), supplierProduct.getCatalogID(), supplierProduct.getPrice(), supplierProduct.getAmount(), supplierProduct.getManufacturer(), supplierProduct.getExpirationDays(), supplierProduct.getWeight()});
            }
        });

        chooseButton.addActionListener(e -> {
            int selectedRow = ProductChooseTable.getSelectedRow();
            if (selectedRow != -1) {
                // Get the data from the selected row in the ProductChooseTableModel
                Object[] rowData = new Object[ProductChooseTableModel.getColumnCount() + 1];
                for (int i = 0; i < rowData.length - 1; i++)
                    rowData[i] = ProductChooseTableModel.getValueAt(selectedRow, i);
                rowData[rowData.length - 1] = 0;
                // Add the data to the chosenProductsTableModel
                chosenProductsTableModel.addRow(rowData);
                // Remove the row from the ProductChooseTableModel
                ProductChooseTableModel.removeRow(selectedRow);
            }
        });

        chooseButton2.addActionListener(e -> {
            int selectedRow = chosenProductsTable.getSelectedRow();
            if (selectedRow != -1) {
                // Get the selected row data from the chosenProductsTableModel
                Object[] rowData = new Object[chosenProductsTableModel.getColumnCount() - 1];
                for (int i = 0; i < rowData.length; i++) {
                    rowData[i] = chosenProductsTableModel.getValueAt(selectedRow, i);
                }
                // Add the row data to the ProductChooseTableModel
                ProductChooseTableModel.addRow(rowData);
                // Remove the selected row from the chosenProductsTableModel
                chosenProductsTableModel.removeRow(selectedRow);
            }
        });

        JPanel selectSupplyingDay = new JPanel();
        selectSupplyingDay.setLayout(new GridLayout(2, 2, 10, 10));
        JLabel type = new JLabel("Order Day: ");
        JComboBox<String> combo = new JComboBox<>(new String[]{"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"});
        combo.setEditable(false);
        combo.setSelectedIndex(-1);
        selectSupplyingDay.add(type);
        selectSupplyingDay.add(combo);
        JButton backButton = new JButton("Back");
        selectSupplyingDay.add(backButton);
        JButton createOrderButton = new JButton("Create Periodic Order");
        selectSupplyingDay.add(createOrderButton);
        panel.add(selectSupplyingDay, BorderLayout.SOUTH);
        createPeriodicOrderFrame.getContentPane().add(panel);

        createOrderButton.addActionListener(e -> {
            if(supplierChooseTable.getSelectedRow() == -1)
            {
                JOptionPane.showMessageDialog(null, "You must choose supplier", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if(combo.getSelectedIndex() == -1)
            {
                JOptionPane.showMessageDialog(null, "You must choose order day", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            for (int row = 0; row < chosenProductsTableModel.getRowCount(); row++) {
                String amountToOrderStr = String.valueOf(chosenProductsTableModel.getValueAt(row, chosenProductsTableModel.getColumnCount() - 1));
                int amountToOrder;
                try
                {
                    amountToOrder = Integer.parseInt(amountToOrderStr);
                }
                catch (NumberFormatException exception)
                {
                    JOptionPane.showMessageDialog(null, "The amount to order should be integer, please try again", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int productID = Integer.parseInt(String.valueOf(chosenProductsTableModel.getValueAt(row, 1)));
                if (amountToOrder <= 0) {
                    JOptionPane.showMessageDialog(null, "The amount to order should be more than zero, the amount of product ID " + productID + " is zero", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                productsAndAmount.put(productID, amountToOrder);
            }
            Response response = orderService.createPeriodicOrder(supplierID.get(), branchID, DayOfWeek.valueOf(((String) combo.getSelectedItem()).toUpperCase()), productsAndAmount);
            if(response.errorOccurred())
            {
                JOptionPane.showMessageDialog(null, response.getErrorMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            else
                JOptionPane.showMessageDialog(null, "Periodic Order With The ID " + response.getSupplierId() + " Has Successfully Been Created", "Error", JOptionPane.ERROR_MESSAGE);

            resetCreateOrderFrame(supplierChooseTableModel, ProductChooseTableModel, chosenProductsTableModel, combo);
            createPeriodicOrderFrame.setVisible(false);
            periodicOrderUI.setVisible(true);
            periodicOrderUI.pack();
        });

        backButton.addActionListener(e -> {
            createPeriodicOrderFrame.setVisible(false);
            periodicOrderUI.setVisible(true);
            periodicOrderUI.pack();
        });

        // Load all the suppliers to the table
        int i = orderService.getLastSupplierID();
        if (i != -1)
        {
            for (int j = 1; j <= i; j++)
            {
                Response res = orderService.getSupplierNameById(j);
                if (res != null && res.getAnswer())
                {
                    Object[] rowData = {j, res.getStringValue()};
                    supplierChooseTableModel.addRow(rowData);
                }
            }
        }

        createPeriodicOrderFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                resetCreateOrderFrame(supplierChooseTableModel, ProductChooseTableModel, chosenProductsTableModel, combo);
                createPeriodicOrderFrame.setVisible(false);
                periodicOrderUI.setVisible(true);
                periodicOrderUI.pack();
            }
        });

    }

    private void setUpdatePeriodicOrderFrame(JFrame updatePeriodicOrderFrame, JFrame periodicOrderUI, int branchID) {
        updatePeriodicOrderFrame.setSize(800, 600);
        updatePeriodicOrderFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        updatePeriodicOrderFrame.setLocationRelativeTo(null);

        AtomicInteger supplierID = new AtomicInteger();
        AtomicInteger orderID = new AtomicInteger();
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(0, 0)); // Add spacing between sections
        panel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

        JPanel selectOrderPanel = new JPanel();
        selectOrderPanel.setLayout(new GridLayout(2, 1, 0, 0));
        JLabel selectSupplierLabel = new JLabel("Please select the the periodic order: ");
        selectOrderPanel.add(selectSupplierLabel);

        DefaultTableModel orderChooseTableModel = new DefaultTableModel();
        orderChooseTableModel.addColumn("Periodic Order ID");
        orderChooseTableModel.addColumn("Supplier ID");
        orderChooseTableModel.addColumn("Order Day");
        JTable orderChooseTable = new JTable(orderChooseTableModel);
        JScrollPane orderChooseScrollPane = new JScrollPane(orderChooseTable);
        selectOrderPanel.add(orderChooseScrollPane);
        selectOrderPanel.setPreferredSize(new Dimension(800, 100));
        panel.add(selectOrderPanel, BorderLayout.NORTH);


        JPanel selectProductPanel = new JPanel();
        selectProductPanel.setLayout(new GridLayout(5, 1, 0, 0));
        JLabel selectProductLabel = new JLabel("Please select the Products that you want to add to order:");
        selectProductPanel.add(selectProductLabel);
        DefaultTableModel ProductChooseTableModel = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Set all cells to be not editable
            }
        };
        ProductChooseTableModel.addColumn("Name");
        ProductChooseTableModel.addColumn("Product ID");
        ProductChooseTableModel.addColumn("Catalog Number");
        ProductChooseTableModel.addColumn("Price");
        ProductChooseTableModel.addColumn("Amount");
        ProductChooseTableModel.addColumn("Manufacturer");
        ProductChooseTableModel.addColumn("Expiration Days");
        ProductChooseTableModel.addColumn("Weight");
        JTable ProductChooseTable = new JTable(ProductChooseTableModel);
        JScrollPane ProductChooseScrollPane = new JScrollPane(ProductChooseTable);
        ProductChooseScrollPane.setPreferredSize(new Dimension(ProductChooseScrollPane.getPreferredSize().width, 200));
        selectProductPanel.add(ProductChooseScrollPane);


        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(1, 2, 10, 10));

        JButton chooseButton = new JButton("↓");
        chooseButton.setFont(new Font("Arial", Font.BOLD, 25));
        chooseButton.setForeground(Color.WHITE);
        chooseButton.setBackground(Color.BLACK);
        chooseButton.setPreferredSize(new Dimension(25, 25));
        buttonsPanel.add(chooseButton);

        JButton chooseButton2 = new JButton("↑");
        chooseButton2.setFont(new Font("Arial", Font.BOLD, 25));
        chooseButton2.setForeground(Color.WHITE);
        chooseButton2.setBackground(Color.BLACK);
        chooseButton2.setPreferredSize(new Dimension(25, 25));
        buttonsPanel.add(chooseButton2);

        selectProductPanel.add(buttonsPanel);

        JLabel chosenProductLabel = new JLabel("Chosen products for periodic Order:");
        selectProductPanel.add(chosenProductLabel);
        DefaultTableModel chosenProductsTableModel = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 8; // Set all cells to be not editable except column 8
            }
        };
        chosenProductsTableModel.addColumn("Name");
        chosenProductsTableModel.addColumn("Product ID");
        chosenProductsTableModel.addColumn("Catalog Number");
        chosenProductsTableModel.addColumn("Price");
        chosenProductsTableModel.addColumn("Amount");
        chosenProductsTableModel.addColumn("Manufacturer");
        chosenProductsTableModel.addColumn("Expiration Days");
        chosenProductsTableModel.addColumn("Weight");
        chosenProductsTableModel.addColumn("Amount To Order");
        JTable chosenProductsTable = new JTable(chosenProductsTableModel);
        JScrollPane chosenProductsScrollPane = new JScrollPane(chosenProductsTable);
        chosenProductsScrollPane.setPreferredSize(new Dimension(chosenProductsScrollPane.getPreferredSize().width, 200));
        selectProductPanel.add(chosenProductsScrollPane);

        selectProductPanel.setPreferredSize(new Dimension(800, 600));
        panel.add(selectProductPanel, BorderLayout.CENTER);

        HashMap<Integer, Integer> productsAndAmount = new HashMap<>();


        chooseButton.addActionListener(e -> {
            int selectedRow = ProductChooseTable.getSelectedRow();
            if (selectedRow != -1) {
                // Get the data from the selected row in the ProductChooseTableModel
                Object[] rowData = new Object[ProductChooseTableModel.getColumnCount() + 1];
                for (int i = 0; i < rowData.length - 1; i++)
                    rowData[i] = ProductChooseTableModel.getValueAt(selectedRow, i);
                rowData[rowData.length - 1] = 0;
                // Add the data to the chosenProductsTableModel
                chosenProductsTableModel.addRow(rowData);
                // Remove the row from the ProductChooseTableModel
                ProductChooseTableModel.removeRow(selectedRow);
            }
        });

        chooseButton2.addActionListener(e -> {
            int selectedRow = chosenProductsTable.getSelectedRow();
            if (selectedRow != -1) {
                // Get the selected row data from the chosenProductsTableModel
                Object[] rowData = new Object[chosenProductsTableModel.getColumnCount() - 1];
                for (int i = 0; i < rowData.length; i++) {
                    rowData[i] = chosenProductsTableModel.getValueAt(selectedRow, i);
                }
                // Add the row data to the ProductChooseTableModel
                ProductChooseTableModel.addRow(rowData);
                // Remove the selected row from the chosenProductsTableModel
                chosenProductsTableModel.removeRow(selectedRow);
            }
        });

        JPanel selectSupplyingDay = new JPanel();
        selectSupplyingDay.setLayout(new GridLayout(2, 2, 10, 10));
        JLabel type = new JLabel("Order Day: ");
        JComboBox<String> combo = new JComboBox<>(new String[]{"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"});
        combo.setEditable(false);
        combo.setSelectedIndex(-1);
        selectSupplyingDay.add(type);
        selectSupplyingDay.add(combo);
        JButton backButton = new JButton("Back");
        selectSupplyingDay.add(backButton);
        JButton updateOrderButton = new JButton("Update Periodic Order");
        selectSupplyingDay.add(updateOrderButton);
        panel.add(selectSupplyingDay, BorderLayout.SOUTH);
        updatePeriodicOrderFrame.getContentPane().add(panel);

        updateOrderButton.addActionListener(e -> {
            if(orderChooseTable.getSelectedRow() == -1)
            {
                JOptionPane.showMessageDialog(null, "You must choose order", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if(combo.getSelectedIndex() == -1)
            {
                JOptionPane.showMessageDialog(null, "You must choose order day", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            for (int row = 0; row < chosenProductsTableModel.getRowCount(); row++) {
                String amountToOrderStr = String.valueOf(chosenProductsTableModel.getValueAt(row, chosenProductsTableModel.getColumnCount() - 1));
                int amountToOrder;
                try
                {
                    amountToOrder = Integer.parseInt(amountToOrderStr);
                }
                catch (NumberFormatException exception)
                {
                    JOptionPane.showMessageDialog(null, "The amount to order should be integer, please try again", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int productID = Integer.parseInt(String.valueOf(chosenProductsTableModel.getValueAt(row, 1)));
                if (amountToOrder <= 0) {
                    JOptionPane.showMessageDialog(null, "The amount to order should be more than zero, the amount of product ID " + productID + " is zero", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                productsAndAmount.put(productID, amountToOrder);
            }
            Response response = orderService.updatePeriodicOrder(orderID.get(), DayOfWeek.valueOf(((String) combo.getSelectedItem()).toUpperCase()), productsAndAmount);
            if(response.errorOccurred())
            {
                JOptionPane.showMessageDialog(null, response.getErrorMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            else
                JOptionPane.showMessageDialog(null, "Periodic Order With The ID " + response.getSupplierId() + " Has Successfully Been Updated", "Error", JOptionPane.ERROR_MESSAGE);

            resetCreateOrderFrame(orderChooseTableModel, ProductChooseTableModel, chosenProductsTableModel, combo);
            updatePeriodicOrderFrame.setVisible(false);
            periodicOrderUI.setVisible(true);
            periodicOrderUI.pack();
        });

        backButton.addActionListener(e -> {
            updatePeriodicOrderFrame.setVisible(false);
            periodicOrderUI.setVisible(true);
            periodicOrderUI.pack();
        });

        // Load all the orders to the table
        HashMap<Integer, PeriodicOrder> periodicOrders = orderService.getPeriodicOrdersToBranch(branchID);
        for(PeriodicOrder periodicOrder : periodicOrders.values())
        {
            Object[] rowData = {periodicOrder.getPeriodicOrderID(), periodicOrder.getSupplierID(), periodicOrder.getFixedDay()};
            orderChooseTableModel.addRow(rowData);
        }

        // Event that will execute when row has been chosen on supplier choose table
        orderChooseTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                chosenProductsTableModel.setRowCount(0);
                ProductChooseTableModel.setRowCount(0);
                productsAndAmount.clear();

                int selectedRow = orderChooseTable.getSelectedRow();
                supplierID.set(Integer.parseInt(orderChooseTable.getValueAt(selectedRow, 1).toString()));
                HashMap<Integer, SupplierProduct> supplierProducts = orderService.getAllSupplierProductsByID(supplierID.get());
                orderID.set(Integer.parseInt(orderChooseTable.getValueAt(selectedRow, 0).toString()));
                PeriodicOrder periodicOrder = orderService.getPeriodicOrderByID(orderID.get());
                int index = -1;
                switch (periodicOrder.getFixedDay())
                {
                    case SUNDAY -> index = 0;
                    case MONDAY -> index = 1;
                    case TUESDAY -> index = 2;
                    case WEDNESDAY -> index = 3;
                    case THURSDAY -> index = 4;
                    case FRIDAY -> index = 5;
                    case SATURDAY -> index = 6;
                }
                combo.setSelectedIndex(index);
                ArrayList<SupplierProduct> orderProducts = periodicOrder.getItemsInOrder();
                for (SupplierProduct supplierProduct : supplierProducts.values())
                {

                    if(!orderProducts.contains(supplierProduct))
                        ProductChooseTableModel.addRow(new Object[]{supplierProduct.getName(), supplierProduct.getProductID(), supplierProduct.getCatalogID(), supplierProduct.getPrice(), supplierProduct.getAmount(), supplierProduct.getManufacturer(), supplierProduct.getExpirationDays(), supplierProduct.getWeight()});
                    else
                        chosenProductsTableModel.addRow(new Object[]{supplierProduct.getName(), supplierProduct.getProductID(), supplierProduct.getCatalogID(), supplierProduct.getPrice(), supplierProduct.getAmount(), supplierProduct.getManufacturer(), supplierProduct.getExpirationDays(), supplierProduct.getWeight(), orderProducts.get(orderProducts.indexOf(supplierProduct)).getAmount()});

                }
            }
        });

        updatePeriodicOrderFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                resetCreateOrderFrame(orderChooseTableModel, ProductChooseTableModel, chosenProductsTableModel, combo);
                updatePeriodicOrderFrame.setVisible(false);
                periodicOrderUI.setVisible(true);
            }
        });

    }

    // Option 2 Existing order menu
    private void openExistingOrderUI(JFrame existingOrderUI, int branchID) {


        existingOrderUI.setPreferredSize(new Dimension(800, 600));
        existingOrderUI.pack(); // Pack the components inside the JFrame
        existingOrderUI.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        existingOrderUI.setLocationRelativeTo(null);

        AtomicInteger supplierID = new AtomicInteger();
        AtomicInteger orderID = new AtomicInteger();
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(0, 0)); // Add spacing between sections
        panel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

        JPanel selectOrderPanel = new JPanel();
        selectOrderPanel.setLayout(new GridLayout(2, 1, 0, 0));
        JLabel selectSupplierLabel = new JLabel("Please select order: ");
        selectOrderPanel.add(selectSupplierLabel);

        DefaultTableModel orderChooseTableModel = new DefaultTableModel();
        orderChooseTableModel.addColumn("Order ID");
        orderChooseTableModel.addColumn("Supplier ID");
        JTable orderChooseTable = new JTable(orderChooseTableModel);
        JScrollPane orderChooseScrollPane = new JScrollPane(orderChooseTable);
        selectOrderPanel.add(orderChooseScrollPane);
        selectOrderPanel.setPreferredSize(new Dimension(800, 200));
        panel.add(selectOrderPanel, BorderLayout.NORTH);


        JPanel selectProductPanel = new JPanel();
        selectProductPanel.setLayout(new GridLayout(5, 1, 0, 0));
        JLabel selectProductLabel = new JLabel("Please select the Products that you want to add to order:");
        selectProductPanel.add(selectProductLabel);
        DefaultTableModel ProductChooseTableModel = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Set all cells to be not editable
            }
        };
        ProductChooseTableModel.addColumn("Name");
        ProductChooseTableModel.addColumn("Product ID");
        ProductChooseTableModel.addColumn("Catalog Number");
        ProductChooseTableModel.addColumn("Price");
        ProductChooseTableModel.addColumn("Amount");
        ProductChooseTableModel.addColumn("Manufacturer");
        ProductChooseTableModel.addColumn("Expiration Days");
        ProductChooseTableModel.addColumn("Weight");
        JTable ProductChooseTable = new JTable(ProductChooseTableModel);
        JScrollPane ProductChooseScrollPane = new JScrollPane(ProductChooseTable);
        ProductChooseScrollPane.setPreferredSize(new Dimension(ProductChooseScrollPane.getPreferredSize().width, 200));
        selectProductPanel.add(ProductChooseScrollPane);


        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(1, 2, 10, 10));

        JButton chooseButton = new JButton("↓");
        chooseButton.setFont(new Font("Arial", Font.BOLD, 25));
        chooseButton.setForeground(Color.WHITE);
        chooseButton.setBackground(Color.BLACK);
        chooseButton.setPreferredSize(new Dimension(25, 25));
        buttonsPanel.add(chooseButton);

        JButton chooseButton2 = new JButton("↑");
        chooseButton2.setFont(new Font("Arial", Font.BOLD, 25));
        chooseButton2.setForeground(Color.WHITE);
        chooseButton2.setBackground(Color.BLACK);
        chooseButton2.setPreferredSize(new Dimension(25, 25));
        buttonsPanel.add(chooseButton2);

        selectProductPanel.add(buttonsPanel);

        JLabel chosenProductLabel = new JLabel("Chosen products for the order:");
        selectProductPanel.add(chosenProductLabel);
        DefaultTableModel chosenProductsTableModel = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 8; // Set all cells to be not editable except column 8
            }
        };
        chosenProductsTableModel.addColumn("Name");
        chosenProductsTableModel.addColumn("Product ID");
        chosenProductsTableModel.addColumn("Catalog Number");
        chosenProductsTableModel.addColumn("Price");
        chosenProductsTableModel.addColumn("Amount");
        chosenProductsTableModel.addColumn("Manufacturer");
        chosenProductsTableModel.addColumn("Expiration Days");
        chosenProductsTableModel.addColumn("Weight");
        chosenProductsTableModel.addColumn("Amount To Order");
        JTable chosenProductsTable = new JTable(chosenProductsTableModel);
        JScrollPane chosenProductsScrollPane = new JScrollPane(chosenProductsTable);
        chosenProductsScrollPane.setPreferredSize(new Dimension(chosenProductsScrollPane.getPreferredSize().width, 200));
        selectProductPanel.add(chosenProductsScrollPane);

        selectProductPanel.setPreferredSize(new Dimension(800, 600));
        panel.add(selectProductPanel, BorderLayout.CENTER);

        HashMap<Integer, Integer> productsAndAmount = new HashMap<>();


        chooseButton.addActionListener(e -> {
            int selectedRow = ProductChooseTable.getSelectedRow();
            if (selectedRow != -1) {
                // Get the data from the selected row in the ProductChooseTableModel
                Object[] rowData = new Object[ProductChooseTableModel.getColumnCount() + 1];
                for (int i = 0; i < rowData.length - 1; i++)
                    rowData[i] = ProductChooseTableModel.getValueAt(selectedRow, i);
                rowData[rowData.length - 1] = 0;
                // Add the data to the chosenProductsTableModel
                chosenProductsTableModel.addRow(rowData);
                // Remove the row from the ProductChooseTableModel
                ProductChooseTableModel.removeRow(selectedRow);
            }
        });

        chooseButton2.addActionListener(e -> {
            int selectedRow = chosenProductsTable.getSelectedRow();
            if (selectedRow != -1) {
                // Get the selected row data from the chosenProductsTableModel
                Object[] rowData = new Object[chosenProductsTableModel.getColumnCount() - 1];
                for (int i = 0; i < rowData.length; i++) {
                    rowData[i] = chosenProductsTableModel.getValueAt(selectedRow, i);
                }
                // Add the row data to the ProductChooseTableModel
                ProductChooseTableModel.addRow(rowData);
                // Remove the selected row from the chosenProductsTableModel
                chosenProductsTableModel.removeRow(selectedRow);
            }
        });

        JPanel selectSupplyingDay = new JPanel();
        selectSupplyingDay.setLayout(new GridLayout(1, 2, 10, 10));
        JButton backButton = new JButton("Back");
        selectSupplyingDay.add(backButton);
        JButton updateOrderButton = new JButton("Update Order");
        selectSupplyingDay.add(updateOrderButton);
        panel.add(selectSupplyingDay, BorderLayout.SOUTH);
        existingOrderUI.getContentPane().add(panel);

        updateOrderButton.addActionListener(e -> {
            if(orderChooseTable.getSelectedRow() == -1)
            {
                JOptionPane.showMessageDialog(null, "You must choose order", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            for (int row = 0; row < chosenProductsTableModel.getRowCount(); row++) {
                String amountToOrderStr = String.valueOf(chosenProductsTableModel.getValueAt(row, chosenProductsTableModel.getColumnCount() - 1));
                int amountToOrder;
                try
                {
                    amountToOrder = Integer.parseInt(amountToOrderStr);
                }
                catch (NumberFormatException exception)
                {
                    JOptionPane.showMessageDialog(null, "The amount to order should be integer, please try again", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int productID = Integer.parseInt(String.valueOf(chosenProductsTableModel.getValueAt(row, 1)));
                if (amountToOrder <= 0) {
                    JOptionPane.showMessageDialog(null, "The amount to order should be more than zero, the amount of product ID " + productID + " is zero", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                productsAndAmount.put(productID, amountToOrder);
            }
            Response response = orderService.updateOrder(orderID.get(), productsAndAmount);
            if(response.errorOccurred())
            {
                JOptionPane.showMessageDialog(null, response.getErrorMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            else
                JOptionPane.showMessageDialog(null, "Order With The ID " + response.getSupplierId() + " Has Successfully Been Updated", "Error", JOptionPane.ERROR_MESSAGE);
            resetCreateOrderFrame(orderChooseTableModel, ProductChooseTableModel, chosenProductsTableModel, null);
            existingOrderUI.setVisible(false);
            setVisible(true);
        });

        backButton.addActionListener(e -> {
            existingOrderUI.setVisible(false);
            setVisible(true);
        });

        // Load all the orders to the table
        HashMap<Integer, Order> periodicOrders = orderService.getOrdersToBranch(branchID);
        for(Order order : periodicOrders.values())
        {
            Object[] rowData = {order.getOrderID(), order.getSupplierId()};
            orderChooseTableModel.addRow(rowData);
        }

        // Event that will execute when row has been chosen on supplier choose table
        orderChooseTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                chosenProductsTableModel.setRowCount(0);
                ProductChooseTableModel.setRowCount(0);
                productsAndAmount.clear();

                int selectedRow = orderChooseTable.getSelectedRow();
                supplierID.set(Integer.parseInt(orderChooseTable.getValueAt(selectedRow, 1).toString()));
                HashMap<Integer, SupplierProduct> supplierProducts = orderService.getAllSupplierProductsByID(supplierID.get());
                orderID.set(Integer.parseInt(orderChooseTable.getValueAt(selectedRow, 0).toString()));
                Order order = orderService.getOrderByID(orderID.get());
                int index = -1;
                ArrayList<SupplierProduct> orderProducts = order.getItemsInOrder();
                for (SupplierProduct supplierProduct : supplierProducts.values())
                {
                    if(!orderProducts.contains(supplierProduct))
                        ProductChooseTableModel.addRow(new Object[]{supplierProduct.getName(), supplierProduct.getProductID(), supplierProduct.getCatalogID(), supplierProduct.getPrice(), supplierProduct.getAmount(), supplierProduct.getManufacturer(), supplierProduct.getExpirationDays(), supplierProduct.getWeight()});
                    else
                        chosenProductsTableModel.addRow(new Object[]{supplierProduct.getName(), supplierProduct.getProductID(), supplierProduct.getCatalogID(), supplierProduct.getPrice(), supplierProduct.getAmount(), supplierProduct.getManufacturer(), supplierProduct.getExpirationDays(), supplierProduct.getWeight(), orderProducts.get(orderProducts.indexOf(supplierProduct)).getAmount()});

                }
            }
        });

        existingOrderUI.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                resetCreateOrderFrame(orderChooseTableModel, ProductChooseTableModel, chosenProductsTableModel, null);
                existingOrderUI.setVisible(false);
                setVisible(true);
            }
        });

    }
    // Option 3 Execute Periodic Orders For Today menu


    // Option 5 Print branch's orders history
    public void printOrdersHistory(JFrame orderHistoryUI, JFrame backFrame)
    {

        HashMap<Integer, Order> orders = orderService.getOrdersToBranch(branch.getBranchID());
        if(orders == null || orders.size() == 0)
        {
            JOptionPane.showMessageDialog(null, "There is not orders in this branch", "Error", JOptionPane.ERROR_MESSAGE);
            backFrame.setVisible(true);
            return;
        }

        orderHistoryUI.setSize(800, 600);
        orderHistoryUI.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);


        JPanel orderHistoryPanel = new JPanel(new BorderLayout(10, 10));
        orderHistoryPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel labelsPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        JLabel branchNameLabel = new JLabel("Branch Name: " + branch.getBranchName());
        labelsPanel.add(branchNameLabel);
        JLabel branchIDLabel = new JLabel("Branch ID: " + branch.getBranchID());
        labelsPanel.add(branchIDLabel);

        orderHistoryPanel.add(labelsPanel, BorderLayout.NORTH);

        DefaultTableModel ordersTableModel = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Set all cells to be not editable
            }
        };
        ordersTableModel.setRowCount(0);
        ordersTableModel.addColumn("Order ID");
        ordersTableModel.addColumn("Supplier ID");
        ordersTableModel.addColumn("Supplier Name");
        ordersTableModel.addColumn("Contact Phone Number");
        ordersTableModel.addColumn("Creation Date");
        ordersTableModel.addColumn("Delivery Date");
        ordersTableModel.addColumn("Collected");
        ordersTableModel.addColumn("Price Before Discount");
        ordersTableModel.addColumn("Price After Discount");
        JTable ordersTable = new JTable(ordersTableModel);
        JScrollPane ordersScrollPane = new JScrollPane(ordersTable);
        orderHistoryPanel.add(ordersScrollPane, BorderLayout.CENTER);

        orderHistoryUI.getContentPane().add(orderHistoryPanel);

        for(Order order : orders.values())
            ordersTableModel.addRow(new Object[] { order.getOrderID(), order.getSupplierId(), order.getSupplierName(), order.getContactPhoneNumber(), order.getCreationDate(), order.getDeliveryDate(), order.isCollected(), order.getTotalPriceBeforeDiscount(), order.getTotalPriceAfterDiscount() });
        ordersTableModel.fireTableDataChanged();


        ordersTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    JPopupMenu popupMenu = new JPopupMenu();
                    JMenuItem showProductsMenuItem = new JMenuItem("Show Products");
                    showProductsMenuItem.addActionListener(ee -> {
                        // Handle the action when "Add Discount" is clicked
                        // discount frame
                        int selectedRow = ordersTable.rowAtPoint(e.getPoint());
                        if (selectedRow >= 0)
                        {
                            orderHistoryUI.setVisible(false);
                            JFrame showProductsFrame = new JFrame("Products On Order "+ ordersTable.getValueAt(selectedRow, 0));
                            showProductsFrame.setSize(new Dimension(400, 300));
                            DefaultTableModel chosenProductsTableModel = new DefaultTableModel(){
                                @Override
                                public boolean isCellEditable(int row, int column) {
                                    return false;
                                }
                            };
                            chosenProductsTableModel.addColumn("Name");
                            chosenProductsTableModel.addColumn("Product ID");
                            chosenProductsTableModel.addColumn("Catalog Number");
                            chosenProductsTableModel.addColumn("Price");
                            chosenProductsTableModel.addColumn("Amount");
                            chosenProductsTableModel.addColumn("Manufacturer");
                            chosenProductsTableModel.addColumn("Expiration Days");
                            chosenProductsTableModel.addColumn("Weight");
                            chosenProductsTableModel.addColumn("Amount To Order");
                            JTable chosenProductsTable = new JTable(chosenProductsTableModel);
                            JScrollPane chosenProductsScrollPane = new JScrollPane(chosenProductsTable);
                            chosenProductsScrollPane.setPreferredSize(new Dimension(chosenProductsScrollPane.getPreferredSize().width, 200));
                            showProductsFrame.add(chosenProductsScrollPane);

                            int supplierID = Integer.parseInt(ordersTable.getValueAt(selectedRow, 1).toString());
                            int orderID = Integer.parseInt(ordersTable.getValueAt(selectedRow, 0).toString());
                            HashMap<Integer, SupplierProduct> supplierProducts = orderService.getAllSupplierProductsByID(supplierID);
                            Order order = orderService.getOrderByID(orderID);
                            ArrayList<SupplierProduct> orderProducts = order.getItemsInOrder();
                            for (SupplierProduct supplierProduct : supplierProducts.values())
                                chosenProductsTableModel.addRow(new Object[]{supplierProduct.getName(), supplierProduct.getProductID(), supplierProduct.getCatalogID(), supplierProduct.getPrice(), supplierProduct.getAmount(), supplierProduct.getManufacturer(), supplierProduct.getExpirationDays(), supplierProduct.getWeight(), orderProducts.get(orderProducts.indexOf(supplierProduct)).getAmount()});
                            showProductsFrame.setVisible(true);
                            showProductsFrame.setLocationRelativeTo(null);
                            showProductsFrame.addWindowListener(new WindowAdapter() {
                                @Override
                                public void windowClosing(WindowEvent e) {
                                    showProductsFrame.dispose();
                                    orderHistoryUI.setVisible(true);
                                }
                            });
                        }
                    });
                    popupMenu.add(showProductsMenuItem);
                    popupMenu.show(ordersTable, e.getX(), e.getY());
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    JPopupMenu popupMenu = new JPopupMenu();
                    JMenuItem showProductsMenuItem = new JMenuItem("Show Products");
                    showProductsMenuItem.addActionListener(ee -> {
                        // Handle the action when "Add Discount" is clicked
                        // discount frame
                        int selectedRow = ordersTable.rowAtPoint(e.getPoint());
                        if (selectedRow >= 0)
                        {

                            orderHistoryUI.setVisible(false);
                            JFrame showProductsFrame = new JFrame("Products On Order "+ ordersTable.getValueAt(selectedRow, 0));
                            showProductsFrame.setLocationRelativeTo(null);
                            showProductsFrame.setPreferredSize(new Dimension(400, 300));

                            DefaultTableModel chosenProductsTableModel = new DefaultTableModel(){
                                @Override
                                public boolean isCellEditable(int row, int column) {
                                    return false;
                                }
                            };
                            chosenProductsTableModel.addColumn("Name");
                            chosenProductsTableModel.addColumn("Product ID");
                            chosenProductsTableModel.addColumn("Catalog Number");
                            chosenProductsTableModel.addColumn("Price");
                            chosenProductsTableModel.addColumn("Amount On Order");
                            chosenProductsTableModel.addColumn("Manufacturer");
                            chosenProductsTableModel.addColumn("Expiration Days");
                            chosenProductsTableModel.addColumn("Weight");
                            JTable chosenProductsTable = new JTable(chosenProductsTableModel);
                            JScrollPane chosenProductsScrollPane = new JScrollPane(chosenProductsTable);
                            chosenProductsScrollPane.setPreferredSize(new Dimension(chosenProductsScrollPane.getPreferredSize().width, 200));
                            showProductsFrame.add(chosenProductsScrollPane);

                            int orderID = Integer.parseInt(ordersTable.getValueAt(selectedRow, 0).toString());
                            Order order = orderService.getOrderByID(orderID);
                            ArrayList<SupplierProduct> orderProducts = order.getItemsInOrder();
                            for (SupplierProduct supplierProduct : orderProducts)
                                chosenProductsTableModel.addRow(new Object[]{supplierProduct.getName(), supplierProduct.getProductID(), supplierProduct.getCatalogID(), supplierProduct.getPrice(), supplierProduct.getAmount(), supplierProduct.getManufacturer(), supplierProduct.getExpirationDays(), supplierProduct.getWeight() });
                            showProductsFrame.pack();
                            showProductsFrame.setVisible(true);
                            showProductsFrame.addWindowListener(new WindowAdapter() {
                                @Override
                                public void windowClosing(WindowEvent e) {
                                    showProductsFrame.dispose();
                                    orderHistoryUI.setVisible(true);
                                }
                            });
                        }
                    });
                    popupMenu.add(showProductsMenuItem);
                    popupMenu.show(ordersTable, e.getX(), e.getY());
                }
            }
        });



        orderHistoryUI.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                orderHistoryUI.dispose();
                backFrame.setVisible(true);
            }
        });

        orderHistoryUI.setLocationRelativeTo(null);
        orderHistoryUI.setVisible(true);

    }

    private void resetCreateOrderFrame(DefaultTableModel supplierChooseTableModel, DefaultTableModel ProductChooseTableModel, DefaultTableModel chosenProductsTableModel, JComboBox<String> combo) {
        // Clear table models
//        supplierChooseTableModel.setRowCount(0);
        ProductChooseTableModel.setRowCount(0);
        chosenProductsTableModel.setRowCount(0);

        // Reset combo box
        if(combo != null) combo.setSelectedIndex(-1);
    }
}

