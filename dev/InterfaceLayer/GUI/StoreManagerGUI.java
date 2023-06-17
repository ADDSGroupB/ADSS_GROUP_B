package InterfaceLayer.GUI;

import BusinessLayer.InventoryBusinessLayer.*;
import ServiceLayer.SupplierServiceLayer.SupplierService;
import Utillity.DateChooserPanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class StoreManagerGUI extends JFrame {
    private MainController mainController;
    private SupplierService supplierService;
    public StoreManagerGUI(){
        this.mainController = new MainController();
        supplierService = new SupplierService();

        setTitle("Store Manager Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 500);

        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10 ,10));

        JLabel titleLabel = new JLabel("Store Manager Menu");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));

        JButton createNewBranch = new JButton("Create New Branch");
        JButton branchMenu = new JButton("Branch Menu");
        createNewBranch.addActionListener(e -> {
            try {
                showCreateNewBranch();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        branchMenu.addActionListener(e -> showPickBranchMenu());

        JButton printSuppliersButton = new JButton("Print Suppliers");
        printSuppliersButton.addActionListener(e -> supplierService.printSuppliersGui());

        panel.add(titleLabel);
        panel.add(createNewBranch);
        panel.add(branchMenu);
        panel.add(printSuppliersButton);
        getContentPane().add(panel);

        pack();
        setLocationRelativeTo(null);
    }

    private void showPickBranchMenu() {
        this.setVisible(false);
        List<Branch> allBranches = mainController.getBranchController().getAllBranchesController();
        int numOfBranches = allBranches.size();
        if (numOfBranches == 0) {
            JOptionPane.showMessageDialog(null,"There are currently no branches in the system.", "No branches error", JOptionPane.ERROR_MESSAGE);
            this.setVisible(true);
            return;
        }

        JFrame chooseBranch = new JFrame("Choose branch");
        chooseBranch.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        chooseBranch.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                setVisible(true);
            }
        });
        chooseBranch.setSize(400, 500);
        chooseBranch.setLayout(new GridLayout(numOfBranches + 1, 1, 10, 10));
        JLabel titleLabel = new JLabel("Choose branch:");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 15));
        chooseBranch.add(titleLabel);
        JButton[] branchesButtons = new JButton[numOfBranches];
        for (int i = 0; i < numOfBranches; i++) {
            branchesButtons[i] = new JButton("Branch " + (i + 1));
            branchesButtons[i].addActionListener(e -> {
                JButton source = (JButton) e.getSource();
                String buttonText = source.getText();
                int chosenBranch = Integer.parseInt(buttonText.substring(buttonText.lastIndexOf(" ") + 1));
                chooseBranch.setVisible(false);
                showBranchMenu(mainController.getBranchController().getBranchID(chosenBranch));
            });
            chooseBranch.add(branchesButtons[i]);
            chooseBranch.setLocationRelativeTo(null);
            chooseBranch.setVisible(true);
        }
    }
    private void showBranchMenu(Branch branch){
        JFrame branchMenu = new JFrame("Branch Menu");
        branchMenu.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        branchMenu.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                setVisible(true);
            }
        });
        branchMenu.setSize(400, 500);
        branchMenu.setLayout(new GridLayout(9, 1, 10, 10));
        branchMenu.setLocationRelativeTo(null);
        JLabel titleLabel = new JLabel("Please choose one of the following options :");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 15));
        branchMenu.add(titleLabel);

        JButton discountForProductButton = new JButton("Add new discount for product");
        discountForProductButton.addActionListener(e -> {
            branchMenu.setVisible(false);
            discountForProduct(branch, branchMenu);
        });
        branchMenu.add(discountForProductButton);

        JButton discountForCategoryButton = new JButton("Add new discount for category");
        discountForCategoryButton.addActionListener(e -> {
            branchMenu.setVisible(false);
            discountForCategory(branch, branchMenu);
        });
        branchMenu.add(discountForCategoryButton);

        JButton printItemsStoreButton = new JButton("Print all items in store");
        printItemsStoreButton.addActionListener(e -> {
            branchMenu.setVisible(false);
            printItemsStore(branch, branchMenu);
        });
        branchMenu.add(printItemsStoreButton);

        JButton printItemsStorageButton  = new JButton("Print all items in storage");
        printItemsStorageButton.addActionListener(e -> {
            branchMenu.setVisible(false);
            printItemsStorage(branch, branchMenu);
        });
        branchMenu.add(printItemsStorageButton);

        JButton productSalesHistoryButton  = new JButton("Print product sales history");
        productSalesHistoryButton.addActionListener(e -> {
            branchMenu.setVisible(false);
            productSalesHistory(branch, branchMenu);
        });
        branchMenu.add(productSalesHistoryButton);

        JButton orderHistoryButton  = new JButton("Print branch's order history");
        orderHistoryButton.addActionListener(e -> {
            branchMenu.setVisible(false);
            orderHistory(branch, branchMenu);
        });
        branchMenu.add(orderHistoryButton);

        JButton reportManagerButton  = new JButton("Report Manager");
        reportManagerButton.addActionListener(e -> {
            branchMenu.setVisible(false);
            reportManager(branch, branchMenu);
        });
        branchMenu.add(reportManagerButton);

        JButton exitToManagerMenuButton = new JButton("Exit to Manager Menu");
        exitToManagerMenuButton.addActionListener(e -> {
            branchMenu.setVisible(false);
            this.setVisible(true);
        });
        branchMenu.add(exitToManagerMenuButton);

        branchMenu.setVisible(true);
    }

    private void reportManager(Branch branch, JFrame branchMenu) {
        ReportsGUI reportsGUI = new ReportsGUI(branch, mainController, branchMenu);
        reportsGUI.setVisible(true);
    }

    private void orderHistory(Branch branch, JFrame branchMenu) {
        new OrdersGUI(branch, new MainController(), branchMenu).printOrdersHistory(new JFrame("Branch Order History"), branchMenu);
    }

    private void productSalesHistory(Branch branch, JFrame branchMenu) {
    }

    private void discountForCategory(Branch branch, JFrame branchMenu) {
        JFrame addProductFrame = new JFrame("Add new discount for category");
        addProductFrame.setLayout(new GridLayout(5, 2));

        addProductFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                branchMenu.setVisible(true);
            }
        });

        JLabel idLabel = new JLabel("Category ID:");
        JTextField idField = new JTextField();

        JLabel startDateLabel = new JLabel("Start Date:");
        DateChooserPanel startDateField = new DateChooserPanel();

        JLabel endDateLabel = new JLabel("End Date:");
        DateChooserPanel endDateField = new DateChooserPanel();

        JLabel percentageLabel = new JLabel("Percentage Of Discount:");
        JTextField percentageField = new JTextField();

        JButton addButton = new JButton("Add Discount");
        addButton.addActionListener(e -> {
            if (idField.getText().isEmpty() || percentageField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Fields cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            double percentage;
            int categoryID;
            try {
                percentage = Double.parseDouble(percentageField.getText());
                categoryID = Integer.parseInt(idField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid numeric input. Please enter positive numbers only.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (percentage <= 0 || categoryID <= 0) {
                JOptionPane.showMessageDialog(null, "Numeric inputs must be positive.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Category categoryDiscount;
            try {
                categoryDiscount = mainController.getCategoryController().getCategory(categoryID);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            if (categoryDiscount == null)
            {
                JOptionPane.showMessageDialog(null, "The ID of the category you specified does not exist in the system.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            LocalDate startDate = startDateField.getSelectedDate();
            LocalDate endDate = endDateField.getSelectedDate();
            if (startDate.isAfter(endDate))
                JOptionPane.showMessageDialog(null, "The start date of the discount must be before the end date of the discount please try again.", "Error", JOptionPane.ERROR_MESSAGE);

            Discount newDiscount;
            try {
                newDiscount = mainController.getDiscountsDao().addNewDiscount(branch.getBranchID(), startDate, endDate, percentage, categoryDiscount, null);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            if (newDiscount != null)
            {
                String result = "The discount has been successfully added.\n"
                        + "Below are the details of the newly created discount:\n"
                        + newDiscount;
                JOptionPane.showMessageDialog(null, result);
            }

            addProductFrame.dispose();
            branchMenu.setVisible(true);
        });

        addProductFrame.add(idLabel);
        addProductFrame.add(idField);
        addProductFrame.add(startDateLabel);
        addProductFrame.add(startDateField);
        addProductFrame.add(endDateLabel);
        addProductFrame.add(endDateField);
        addProductFrame.add(percentageLabel);
        addProductFrame.add(percentageField);
        addProductFrame.add(new JLabel()); // Placeholder for spacing
        addProductFrame.add(addButton);

        addProductFrame.pack();
        addProductFrame.setLocationRelativeTo(null);
        addProductFrame.setVisible(true);
    }

    private void discountForProduct(Branch branch, JFrame branchMenu) {
        JFrame addProductFrame = new JFrame("Add new discount for product");
        addProductFrame.setLayout(new GridLayout(5, 2));

        addProductFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                branchMenu.setVisible(true);
            }
        });

        JLabel idLabel = new JLabel("Product ID:");
        JTextField idField = new JTextField();

        JLabel startDateLabel = new JLabel("Start Date:");
        DateChooserPanel startDateField = new DateChooserPanel();

        JLabel endDateLabel = new JLabel("End Date:");
        DateChooserPanel endDateField = new DateChooserPanel();

        JLabel percentageLabel = new JLabel("Percentage Of Discount:");
        JTextField percentageField = new JTextField();

        JButton addButton = new JButton("Add Discount");
        addButton.addActionListener(e -> {
            if (idField.getText().isEmpty() || percentageField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Fields cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            double percentage;
            int productID;
            try {
                percentage = Double.parseDouble(percentageField.getText());
                productID = Integer.parseInt(idField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid numeric input. Please enter positive numbers only.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (percentage <= 0 || productID <= 0) {
                JOptionPane.showMessageDialog(null, "Numeric inputs must be positive.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Product productDiscount;
            try {
                productDiscount = mainController.getProductController().getProduct(productID);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            if (productDiscount == null)
            {
                JOptionPane.showMessageDialog(null, "The ID of the product you specified does not exist in the system.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            LocalDate startDate = startDateField.getSelectedDate();
            LocalDate endDate = endDateField.getSelectedDate();
            if (startDate.isAfter(endDate))
                JOptionPane.showMessageDialog(null, "The start date of the discount must be before the end date of the discount please try again.", "Error", JOptionPane.ERROR_MESSAGE);

            Discount newDiscount;
            try {
                newDiscount = mainController.getDiscountsDao().addNewDiscount(branch.getBranchID(), startDate, endDate, percentage, null, productDiscount);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            if (newDiscount != null)
            {
                String result = "The discount has been successfully added.\n"
                        + "Below are the details of the newly created discount:\n"
                        + newDiscount;
                JOptionPane.showMessageDialog(null, result);
            }

            addProductFrame.dispose();
            branchMenu.setVisible(true);
        });

        addProductFrame.add(idLabel);
        addProductFrame.add(idField);
        addProductFrame.add(startDateLabel);
        addProductFrame.add(startDateField);
        addProductFrame.add(endDateLabel);
        addProductFrame.add(endDateField);
        addProductFrame.add(percentageLabel);
        addProductFrame.add(percentageField);
        addProductFrame.add(new JLabel()); // Placeholder for spacing
        addProductFrame.add(addButton);

        addProductFrame.pack();
        addProductFrame.setLocationRelativeTo(null);
        addProductFrame.setVisible(true);
    }
    private void printItemsStore(Branch branch, JFrame branchMenu){
        List<Item> storeItems;
        try {
            storeItems = mainController.getItemsDao().getAllStoreItemsByBranchID(branch.getBranchID());
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        if (storeItems.size()==0)
        {
            JOptionPane.showMessageDialog(null,"We currently have no items in the store.", "No items error", JOptionPane.ERROR_MESSAGE);
            branchMenu.setVisible(true);
            return;
        }
        JFrame itemsInStore = new JFrame("Items In Store");
        itemsInStore.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        itemsInStore.setSize(400, 500);
        JLabel titleLabel = new JLabel("Branch Name: " + branch.getBranchName() + ", Branch ID: " + branch.getBranchID());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 15));

        DefaultTableModel itemsTableModel = new DefaultTableModel();
        itemsTableModel.addColumn("Name");
        itemsTableModel.addColumn("Product ID");
        itemsTableModel.addColumn("Item ID");
        itemsTableModel.addColumn("Supplier ID");
        itemsTableModel.addColumn("Expired Date");
        itemsTableModel.addColumn("Price From Supplier");
        itemsTableModel.addColumn("Price In Branch");
        itemsTableModel.addColumn("Price After Discount");
        itemsTableModel.addColumn("Status");
        itemsTableModel.addColumn("Defective Discription");
        itemsTableModel.addColumn("Arrival Date");
        JTable itemsTable = new JTable(itemsTableModel);
        JScrollPane scrollPane = new JScrollPane(itemsTable);
        scrollPane.setPreferredSize(new Dimension(500, 200));

        for (Item item : storeItems) {
            itemsTableModel.addRow(new Object[]{item.getProduct().getProductName(), item.getProductID(), item.getItemID(),
                    item.getSupplierID(), item.getExpiredDate(), item.getPriceFromSupplier(), item.getPriceInBranch(),
                    item.getPriceAfterDiscount(), item.getStatusType(), item.getDefectiveDiscription(), item.getArrivalDate()});
        }

        itemsInStore.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                branchMenu.setVisible(true);
            }
        });
        itemsInStore.setLayout(new BorderLayout());
        itemsInStore.add(titleLabel, BorderLayout.NORTH);
        itemsInStore.add(scrollPane, BorderLayout.CENTER);
        itemsInStore.setLocationRelativeTo(null);
        itemsInStore.setVisible(true);
    }
    private void printItemsStorage(Branch branch, JFrame branchMenu) {
        List<Item> storageItems;
        try {
            storageItems = mainController.getItemsDao().getAllStorageItemsByBranchID(branch.getBranchID());
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        if (storageItems.size()==0)
        {
            JOptionPane.showMessageDialog(null,"We currently have no items in the storage.", "No items error", JOptionPane.ERROR_MESSAGE);
            branchMenu.setVisible(true);
            return;
        }
        JFrame itemsInStorage = new JFrame("Items In Storage");
        itemsInStorage.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        itemsInStorage.setSize(400, 500);
        JLabel titleLabel = new JLabel("Branch Name: " + branch.getBranchName() + ", Branch ID: " + branch.getBranchID());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 15));

        DefaultTableModel itemsTableModel = new DefaultTableModel();
        itemsTableModel.addColumn("Name");
        itemsTableModel.addColumn("Product ID");
        itemsTableModel.addColumn("Item ID");
        itemsTableModel.addColumn("Supplier ID");
        itemsTableModel.addColumn("Expired Date");
        itemsTableModel.addColumn("Price From Supplier");
        itemsTableModel.addColumn("Price In Branch");
        itemsTableModel.addColumn("Price After Discount");
        itemsTableModel.addColumn("Status");
        itemsTableModel.addColumn("Defective Discription");
        itemsTableModel.addColumn("Arrival Date");
        JTable itemsTable = new JTable(itemsTableModel);
        JScrollPane scrollPane = new JScrollPane(itemsTable);
        scrollPane.setPreferredSize(new Dimension(500, 200));

        for (Item item : storageItems) {
            itemsTableModel.addRow(new Object[]{item.getProduct().getProductName(), item.getProductID(), item.getItemID(),
                    item.getSupplierID(), item.getExpiredDate(), item.getPriceFromSupplier(), item.getPriceInBranch(),
                    item.getPriceAfterDiscount(), item.getStatusType(), item.getDefectiveDiscription(), item.getArrivalDate()});
        }

        itemsInStorage.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                branchMenu.setVisible(true);
            }
        });

        itemsInStorage.setLayout(new BorderLayout());
        itemsInStorage.add(titleLabel, BorderLayout.NORTH);
        itemsInStorage.add(scrollPane, BorderLayout.CENTER);
        itemsInStorage.setLocationRelativeTo(null);
        itemsInStorage.setVisible(true);
    }

    private void showCreateNewBranch() throws SQLException {
        List<Branch> allBranches = mainController.getBranchController().getAllBranchesController();
        if (allBranches.size() >= 10)
        {
            JOptionPane.showMessageDialog(null,"We have reached the limit of branches in the network, you cannot open a new branch.", "Cannot open a new branch", JOptionPane.ERROR_MESSAGE);
            setVisible(true);
            return;
        }
        String newBranchName = JOptionPane.showInputDialog("Enter the name of the new branch:");
        if (newBranchName == null) {
            return;
        }
        if (newBranchName.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Branch name cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if(newBranchName.matches(".*\\d.*")){
            JOptionPane.showMessageDialog(null, "Invalid branch name. Branch name should not contain numbers.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Branch newBranch = mainController.getBranchController().createNewBranch(newBranchName);
        if (newBranch == null)
        {
            JOptionPane.showMessageDialog(null, "There is a problem creating a new branch, please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String result = "The branch has been successfully added.\n"
                + "Below are the details of the newly created branch:\n"
                + newBranch;
        JOptionPane.showMessageDialog(null, result);
    }
}
