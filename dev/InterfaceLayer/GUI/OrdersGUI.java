package InterfaceLayer.GUI;

import BusinessLayer.InventoryBusinessLayer.Branch;
import BusinessLayer.InventoryBusinessLayer.MainController;
import InterfaceLayer.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalTime;

public class OrdersGUI extends JFrame {
    private JButton periodicOrderButton;
    private JButton existingOrderButton;
    private JButton executePeriodicOrdersButton;
    private JButton executeShortageOrdersButton;
    private JButton printOrdersHistoryButton;
    private JButton backButton;

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
        branch = _branch;
        mainController = _mainController;
        branchMenu = _branchMenu;

        setTitle("Orders Menu");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(7, 1));
        JLabel titleLabel = new JLabel("Please choose one of the following options :");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 15));

        add(titleLabel);
        add(periodicOrderButton);
        add(existingOrderButton);
        add(executePeriodicOrdersButton);
        add(executeShortageOrdersButton);
        add(printOrdersHistoryButton);
        add(backButton);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                branchMenu.setVisible(true);
            }
        });

        periodicOrderButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                openPeriodicOrderUI(branch.getBranchID());
            }
        });

        existingOrderButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                openExistingOrderUI();
            }
        });

        executePeriodicOrdersButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                executePeriodicOrders();
            }
        });

        executeShortageOrdersButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                executeShortageOrders();
            }
        });

        printOrdersHistoryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                printOrdersHistory();
            }
        });

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                branchMenu.setVisible(true);
            }
        });
    }
    // Option 1 Periodic order menu
    private void openPeriodicOrderUI(int branchID) {
        JFrame periodicOrderUI = new JFrame("Periodic Orders Menu");

        JButton createOrderButton = new JButton("Create New Periodic Order");;
        JButton updateProductsButton = new JButton("Add / Update Products On Periodic Order");;
        JButton removeProductsButton = new JButton("Remove Products From Periodic Order");;
        JButton backButton = new JButton("Back To Orders Menu");
        periodicOrderUI.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        periodicOrderUI.setLayout(new GridLayout(5, 1));
        JLabel titleLabel = new JLabel("Please choose one of the following options :");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 15));

        periodicOrderUI.add(titleLabel);
        periodicOrderUI.add(createOrderButton);
        periodicOrderUI.add(updateProductsButton);
        periodicOrderUI.add(removeProductsButton);
        periodicOrderUI.add(backButton);

        periodicOrderUI.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                setVisible(true);
            }
        });

        createOrderButton.addActionListener(e -> {
            periodicOrderUI.setVisible(false);
            createPeriodicOrder(branchID, periodicOrderUI);
        });

        updateProductsButton.addActionListener(e -> {
            periodicOrderUI.setVisible(false);
            updateProducts(branchID, periodicOrderUI);
        });

        removeProductsButton.addActionListener(e -> {
            periodicOrderUI.setVisible(false);
            removeProducts(branchID, periodicOrderUI);
        });

        backButton.addActionListener(e -> {
            periodicOrderUI.dispose();
            setVisible(true);
        });

        periodicOrderUI.setVisible(true);
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
}
