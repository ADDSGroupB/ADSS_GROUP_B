package InterfaceLayer.GUI;

import BusinessLayer.InventoryBusinessLayer.Branch;
import BusinessLayer.InventoryBusinessLayer.Item;
import BusinessLayer.InventoryBusinessLayer.MainController;
import BusinessLayer.InventoryBusinessLayer.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.List;

public class StoreKeeperGUI extends JFrame {
    private MainController mainController;
    public StoreKeeperGUI(){
        this.mainController = new MainController();

        setTitle("Store Keeper Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 500);
        setLayout(new GridLayout(4, 1, 10, 10));

        JLabel titleLabel = new JLabel("Store Keeper Menu");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));

        JButton branchMenu = new JButton("Branch Menu");
        JButton productMenu = new JButton("Product Menu");
        JButton categoryMenu = new JButton("Category Menu");
        branchMenu.addActionListener(e -> showPickBranchMenu());
        productMenu.addActionListener(e -> showProductMenu());
        categoryMenu.addActionListener(e -> showCategoryMenu());

        this.add(titleLabel);
        this.add(branchMenu);
        this.add(productMenu);
        this.add(categoryMenu);
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
        branchMenu.setLayout(new GridLayout(7, 1, 10, 10));
        branchMenu.setLocationRelativeTo(null);
        JLabel titleLabel = new JLabel("Please choose one of the following options :");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 15));
        branchMenu.add(titleLabel);

        JButton newSaleButton = new JButton("New sale");
        newSaleButton.addActionListener(e -> {
            branchMenu.setVisible(false);
            makeNewSale(branch, branchMenu);
        });
        branchMenu.add(newSaleButton);

        JButton updateDamagedItemButton = new JButton("Update damaged item");
        updateDamagedItemButton.addActionListener(e -> {
            branchMenu.setVisible(false);
            updateDamagedItem(branch, branchMenu);
        });
        branchMenu.add(updateDamagedItemButton);

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

        JButton ordersMenuButton = new JButton("Orders Menu");
        ordersMenuButton.addActionListener(e -> {
            branchMenu.setVisible(false);
            ordersMenu(branch, branchMenu);
        });
        branchMenu.add(ordersMenuButton);

        JButton exitToInventoryMenuButton = new JButton("Exit to Inventory Menu");
        exitToInventoryMenuButton.addActionListener(e -> {
            branchMenu.setVisible(false);
            this.setVisible(true);
        });
        branchMenu.add(exitToInventoryMenuButton);

        branchMenu.setVisible(true);
    }

    private void makeNewSale(Branch branch, JFrame branchMenu){
        SalesGUI salesGUI = new SalesGUI(branch, mainController, branchMenu);
        salesGUI.setVisible(true);
    }
    private void updateDamagedItem(Branch branch, JFrame branchMenu){
        ReportDamagedItemGUI reportDamagedItemGUI = new ReportDamagedItemGUI(branch, mainController, branchMenu);
        reportDamagedItemGUI.setVisible(true);
    }
    private void printItemsStore(Branch branch, JFrame branchMenu){
        List<Item> storeItems = null;
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
        List<Item> storageItems = null;
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
    private void ordersMenu(Branch branch, JFrame branchMenu) {
        OrdersGUI ordersGUI = new OrdersGUI(branch, mainController, branchMenu);
        ordersGUI.setVisible(true);
    }

    private void showProductMenu(){
        this.setVisible(false);
        ProductGUI productGUI = new ProductGUI(mainController, this);
        productGUI.setVisible(true);
    }
    private void showCategoryMenu(){
        this.setVisible(false);
        CategoryGUI categoryGUI = new CategoryGUI(mainController, this);
        categoryGUI.setVisible(true);
    }
}
