package InterfaceLayer.GUI;

import BusinessLayer.InventoryBusinessLayer.Branch;
import BusinessLayer.InventoryBusinessLayer.MainController;
import BusinessLayer.SupplierBusinessLayer.SupplierProduct;
import DataAccessLayer.InventoryDataAccessLayer.BranchesDao;
import ServiceLayer.SupplierServiceLayer.OrderService;
import Utillity.Response;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.HashMap;

public class OrdersGUI extends JFrame {
    private JButton periodicOrderButton;
    private JButton existingOrderButton;
    private JButton executePeriodicOrdersButton;
    private JButton executeShortageOrdersButton;
    private JButton printOrdersHistoryButton;
    private JButton backButton;
    private OrderService orderService;

    private MainController mainController;
    private Branch branch;
    private JFrame branchMenu;
    public OrdersGUI(Branch _branch, MainController _mainController, JFrame _branchMenu) {
        // Main orders GUI
        periodicOrderButton = new JButton("Periodic Order");
        existingOrderButton = new JButton("Existing Order");
        executePeriodicOrdersButton = new JButton("Execute Periodic Orders For Today");
        executeShortageOrdersButton = new JButton("Execute Shortage Orders For Today");
        printOrdersHistoryButton = new JButton("Print branch's orders history");
        backButton = new JButton("Back To Branch Menu");
        orderService = new OrderService();
        branch = _branch;
        mainController = _mainController;
        branchMenu = _branchMenu;

        setTitle("Orders Menu");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(7, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Orders Menu");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));

        JFrame periodicOrderUI = new JFrame("Periodic Orders Menu");
        openPeriodicOrderUI(periodicOrderUI, branch.getBranchID());
        openExistingOrderUI();
        executePeriodicOrders();
        executeShortageOrders();
        printOrdersHistory();


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
            setVisible(false);
            periodicOrderUI.setVisible(true);
//            periodicOrderUI.pack();
        });

        existingOrderButton.addActionListener(e -> {
            setVisible(false);

        });

        executePeriodicOrdersButton.addActionListener(e -> {
            setVisible(false);

        });

        executeShortageOrdersButton.addActionListener(e -> {
            setVisible(false);
        });

        printOrdersHistoryButton.addActionListener(e -> {
            setVisible(false);

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

        JFrame createPeriodicOrderFrame = new JFrame("Create New Periodic Order");
        setCreatePeriodicOrderFrame(createPeriodicOrderFrame, periodicOrderUI, branchID);
        JFrame updatePeriodicOrderFrame = new JFrame("Add / Update Products On Periodic Order");
        setUpdatePeriodicOrderFrame(updatePeriodicOrderFrame, periodicOrderUI, branchID);
        JFrame removeProductsPeriodicOrderFrame = new JFrame("Remove Products From Periodic Order");
        setRemoveProductsPeriodicOrderFrame(removeProductsPeriodicOrderFrame, periodicOrderUI, branchID);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Periodic Orders Menu");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));

        JButton createOrderButton = new JButton("Create New Periodic Order");;
        JButton updateProductsButton = new JButton("Add / Update Products On Periodic Order");;
        JButton removeProductsButton = new JButton("Remove Products From Periodic Order");;
        JButton backButton = new JButton("Back To Orders Menu");


        panel.add(createOrderButton);
        panel.add(updateProductsButton);
        panel.add(removeProductsButton);
        panel.add(backButton);
        periodicOrderUI.getContentPane().add(panel);

        periodicOrderUI.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                periodicOrderUI.dispose();
                setVisible(true);
//                pack();
            }
        });

        createOrderButton.addActionListener(e -> {
            periodicOrderUI.setVisible(false);
            createPeriodicOrderFrame.setVisible(true);
//            createPeriodicOrderFrame.pack();
        });

        updateProductsButton.addActionListener(e -> {
            periodicOrderUI.setVisible(false);
            updatePeriodicOrderFrame.setVisible(true);
            updatePeriodicOrderFrame.pack();
        });

        removeProductsButton.addActionListener(e -> {
            periodicOrderUI.setVisible(false);
            removeProductsPeriodicOrderFrame.setVisible(true);
            removeProductsPeriodicOrderFrame.pack();
        });

        backButton.addActionListener(e -> {
            periodicOrderUI.setVisible(false);
            setVisible(true);
        });
    }

    private void setCreatePeriodicOrderFrame(JFrame createPeriodicOrderFrame, JFrame periodicOrderUI, int branchID) {

        createPeriodicOrderFrame.setSize(800, 600);
        createPeriodicOrderFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        createPeriodicOrderFrame.setLocationRelativeTo(null);
        createPeriodicOrderFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                createPeriodicOrderFrame.setVisible(false);
                periodicOrderUI.setVisible(true);
            }
        });

//        JPanel panel = new JPanel();
//        panel.setLayout(new BorderLayout(10, 10));
//        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
//
//        DefaultTableModel productsTableModel = new DefaultTableModel();
//        productsTableModel.addColumn("Name");
//        productsTableModel.addColumn("ID");
//        productsTableModel.addColumn("Catalog Number");
//        productsTableModel.addColumn("Price");
//        productsTableModel.addColumn("Amount");
//        productsTableModel.addColumn("Manufacturer");
//        productsTableModel.addColumn("Expiration Days");
//        productsTableModel.addColumn("Weight");
//        JTable productsTable = new JTable(productsTableModel);
//        JScrollPane scrollPane = new JScrollPane(productsTable);
//        scrollPane.setPreferredSize(new Dimension(500, 200));
//
//        JPanel inputPanel = new JPanel();
//        inputPanel.setLayout(new GridLayout(9, 2, 10, 10));
//
//        JLabel nameLabel = new JLabel("Product Name:");
//        JTextField nameTextField = new JTextField();
//        inputPanel.add(nameLabel);
//        inputPanel.add(nameTextField);
//
//        JLabel idLabel = new JLabel("Product ID:");
//        JTextField idTextField = new JTextField();
//        inputPanel.add(idLabel);
//        inputPanel.add(idTextField);
//
//        JLabel catalogNumberLabel = new JLabel("Product Catalog Number:");
//        JTextField catalogNumberTextField = new JTextField();
//        inputPanel.add(catalogNumberLabel);
//        inputPanel.add(catalogNumberTextField);
//
//        JLabel PriceLabel = new JLabel("Product Price:");
//        JTextField PriceTextField = new JTextField();
//        inputPanel.add(PriceLabel);
//        inputPanel.add(PriceTextField);
//
//        JLabel amountLabel = new JLabel("Product Amount:");
//        JTextField amountTextField = new JTextField();
//        inputPanel.add(amountLabel);
//        inputPanel.add(amountTextField);
//
//        JLabel manufacturerLabel = new JLabel("Product Manufacturer:");
//        JTextField manufacturerTextField = new JTextField();
//        inputPanel.add(manufacturerLabel);
//        inputPanel.add(manufacturerTextField);
//
//        JLabel expirationDaysLabel = new JLabel("Product Expiration Days:");
//        JTextField expirationDaysTextField = new JTextField();
//        inputPanel.add(expirationDaysLabel);
//        inputPanel.add(expirationDaysTextField);
//
//        JLabel weightLabel = new JLabel("Product Weight:");
//        JTextField weightTextField = new JTextField();
//        inputPanel.add(weightLabel);
//        inputPanel.add(weightTextField);
//
//        JButton addProduct = new JButton("Add Product");
////        JButton addProductDiscount = new JButton("Add Discount");
//
//        JLabel rightClickLabel = new JLabel("Right click on product that you want add discount to");
//        rightClickLabel.setFont(rightClickLabel.getFont().deriveFont(Font.ITALIC));
//        rightClickLabel.setForeground(Color.GRAY);
//        inputPanel.add(rightClickLabel);
//        inputPanel.add(addProduct);
//
//        JButton nextButton = new JButton("Next");
//        JButton backButton = new JButton("Back");
//
//        JPanel backNextPanel = new JPanel();
//        backNextPanel.setLayout(new GridLayout(1, 2, 10, 10));
//        backNextPanel.add(backButton);
//        backNextPanel.add(nextButton);
//
//        panel.add(inputPanel, BorderLayout.NORTH);
//        panel.add(scrollPane, BorderLayout.CENTER);
//        panel.add(backNextPanel, BorderLayout.SOUTH);
//
//
//        createPeriodicOrderFrame.getContentPane().add(panel);




        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(0, 0)); // Add spacing between sections
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

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
        DefaultTableModel ProductChooseTableModel = new DefaultTableModel();
        ProductChooseTableModel.addColumn("Name");
        ProductChooseTableModel.addColumn("ID");
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

        JButton chooseButton = new JButton("↕");
        chooseButton.setFont(new Font("Arial", Font.BOLD, 30));
        chooseButton.setForeground(Color.WHITE);
        chooseButton.setBackground(Color.BLACK);
        chooseButton.setPreferredSize(new Dimension(50, 50));
        selectProductPanel.add(chooseButton);

        JLabel chosenProductLabel = new JLabel("Chosen products for periodic Order:");
        selectProductPanel.add(chosenProductLabel);
        DefaultTableModel chosenProductsTableModel = new DefaultTableModel();
        chosenProductsTableModel.addColumn("Name");
        chosenProductsTableModel.addColumn("ID");
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

        createPeriodicOrderFrame.getContentPane().add(panel);


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

        supplierChooseTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = supplierChooseTable.getSelectedRow();
                int supplierID = Integer.parseInt(supplierChooseTable.getValueAt(selectedRow, 0).toString());
                HashMap<Integer, SupplierProduct> supplierProducts = orderService.getAllSupplierProductsByID(supplierID);
                for (SupplierProduct supplierProduct : supplierProducts.values())
                    ProductChooseTableModel.addRow(new Object[]{supplierProduct.getName(), supplierProduct.getProductID(), supplierProduct.getCatalogID(), supplierProduct.getPrice(), supplierProduct.getAmount(), supplierProduct.getManufacturer(), supplierProduct.getExpirationDays(), supplierProduct.getWeight()});
            }
        });

    }

    private void setUpdatePeriodicOrderFrame(JFrame updatePeriodicOrderFrame, JFrame periodicOrderUI, int branchID) {
    }

    private void setRemoveProductsPeriodicOrderFrame(JFrame removeProductsPeriodicOrderFrame, JFrame periodicOrderUI, int branchID) {
    }

    private void createPeriodicOrder(int branchID, JFrame periodicOrderUI){

    }
    private void updateProducts(int branchID, JFrame periodicOrderUI){

    }
    private void removeProducts(int branchID, JFrame periodicOrderUI){

    }

    // Option 2 Existing order menu
    private void openExistingOrderUI() {

    }
    // Option 3 Execute Periodic Orders For Today menu

    private void executePeriodicOrders() {

    }
    // Option 4 Execute Shortage Orders For Today menu

    private void executeShortageOrders() {

    }
    // Option 5 Print branch's orders history

    private void printOrdersHistory() { }

    public static void main(String[] args) throws SQLException {
        MainController mainController = new MainController();
        BranchesDao branchesDao = mainController.getBranchesDao();
        Branch b1 = branchesDao.addBranch("SuperLi Beer Sheva");
        Branch b2 = branchesDao.addBranch("SuperLi Tel Aviv");
        Branch b3 = branchesDao.addBranch("SuperLi Jerusalem");
        Branch b4 = branchesDao.addBranch("SuperLi Herzliya");
        Branch b5 = branchesDao.addBranch("SuperLi Eilat");
        OrdersGUI ordersGUI = new OrdersGUI(b1, mainController, new JFrame());
        ordersGUI.setVisible(true);
    }
}

