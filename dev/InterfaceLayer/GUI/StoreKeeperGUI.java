package InterfaceLayer.GUI;

import BusinessLayer.InventoryBusinessLayer.Branch;
import BusinessLayer.InventoryBusinessLayer.Item;
import BusinessLayer.InventoryBusinessLayer.MainController;
import BusinessLayer.InventoryBusinessLayer.Product;
import DataAccessLayer.DBConnector;
import ServiceLayer.SupplierServiceLayer.OrderService;
import Utillity.Response;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.*;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

public class StoreKeeperGUI extends JFrame {
    private MainController mainController;
    private OrderService orderService;
    public StoreKeeperGUI(){
        this.mainController = new MainController();
        orderService = new OrderService();
        startDailyTask();
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
            printItemsStore(branch);
        });
        branchMenu.add(printItemsStoreButton);

        JButton printItemsStorageButton  = new JButton("Print all items in storage");
        printItemsStorageButton.addActionListener(e -> {
            printItemsStorage(branch);
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
    private void printItemsStore(Branch branch){
        List<Item> storeItems = null;
        try {
            storeItems = mainController.getItemsDao().getAllStoreItemsByBranchID(branch.getBranchID());
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        if (storeItems.size()==0)
        {
            JOptionPane.showMessageDialog(null,"We currently have no items in the store.", "No items error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        JFrame itemsInStore = new JFrame("Items In Store");
        itemsInStore.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        itemsInStore.setSize(400, 500);
        JLabel titleLabel = new JLabel("Branch Name :" + branch.getBranchName() + ", Branch ID : " + branch.getBranchID());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 15));

        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (Item item : storeItems) {
            listModel.addElement(item.toString());
        }
        JList<String> jListItems = new JList<>(listModel);
        jListItems.setFont(new Font("Arial", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(jListItems);

        itemsInStore.setLayout(new BorderLayout());
        itemsInStore.add(titleLabel, BorderLayout.NORTH);
        itemsInStore.add(scrollPane, BorderLayout.CENTER);
        itemsInStore.setLocationRelativeTo(null);
        itemsInStore.setVisible(true);
    }
    private void printItemsStorage(Branch branch) {
        List<Item> storageItems = null;
        try {
            storageItems = mainController.getItemsDao().getAllStorageItemsByBranchID(branch.getBranchID());
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        if (storageItems.size()==0)
        {
            JOptionPane.showMessageDialog(null,"We currently have no items in the store.", "No items error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        JFrame itemsInStorage = new JFrame("Items In Store");
        itemsInStorage.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        itemsInStorage.setSize(400, 500);
        JLabel titleLabel = new JLabel("Branch Name :" + branch.getBranchName() + ", Branch ID : " + branch.getBranchID());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 15));

        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (Item item : storageItems) {
            listModel.addElement(item.toString());
        }
        JList<String> jListItems = new JList<>(listModel);
        jListItems.setFont(new Font("Arial", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(jListItems);

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

    private void startDailyTask()
    {
        java.util.Timer timerPeriodicOrder = new java.util.Timer();
        // Schedule the task to execute every day at 10:00am
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 10);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        if (calendar.getTimeInMillis() < System.currentTimeMillis())
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        timerPeriodicOrder.scheduleAtFixedRate(orderService, calendar.getTime(), TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS));
        java.util.Timer timerForShortageOrder = new Timer();
        TimerTask otherTask = new TimerTask() {
            @Override
            public void run()  {
                autoShortage();
            }
        };
        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(Calendar.HOUR_OF_DAY, 20);
        calendar2.set(Calendar.MINUTE, 0);
        calendar2.set(Calendar.SECOND, 0);
        if (calendar2.getTimeInMillis() < System.currentTimeMillis())
            calendar2.add(Calendar.DAY_OF_MONTH, 1);
        timerForShortageOrder.scheduleAtFixedRate(otherTask, calendar2.getTime(), TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS));
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            timerPeriodicOrder.cancel();
            timerForShortageOrder.cancel();
            DBConnector.disconnect();
        }));
    }

    public void autoShortage()
    {
        try {
            List<Branch> branches = mainController.getBranchesDao().getAllBranches();
            HashMap<Integer, Integer> shortage;
            for(Branch branch: branches) {
                shortage = mainController.getItemsDao().fromStorageToStore(branch);
                Response response = orderService.createOrderByShortage(branch.getBranchID(), shortage);
                if (!response.errorOccurred())
                {
                    for (Integer productID : shortage.keySet())
                    {
                        mainController.getProductMinAmountDao().UpdateOrderStatusToProductInBranch(productID, branch.getBranchID(),"Invited");
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error while run function autoShortage in GUI: " + e.getMessage(), "Order Errors", JOptionPane.ERROR_MESSAGE);
        }
    }
}
