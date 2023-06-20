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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportsGUI extends JFrame {
    private MainController mainController;
    private Branch branch;
    private JFrame branchMenu;
    private JButton missingProductsButton;
    private JButton defectiveItemsButton;
    private JButton weeklyStorageButton;
    private JButton exitToBranchMenuButton;
    public ReportsGUI(Branch _branch, MainController _mainController, JFrame _branchMenu) {
        branch = _branch;
        mainController = _mainController;
        branchMenu = _branchMenu;
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Reports Menu");

        JPanel panel = new JPanel(new GridLayout(5, 1));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Reports Menu");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 15));
        panel.add(titleLabel);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                branchMenu.setVisible(true);
            }
        });

        missingProductsButton = new JButton("Missing Products Report");
        missingProductsButton.addActionListener(e -> missingReportUI());
        panel.add(missingProductsButton);

        defectiveItemsButton = new JButton("Defective Items Report");
        defectiveItemsButton.addActionListener(e -> defectiveReportUI());
        panel.add(defectiveItemsButton);

        weeklyStorageButton = new JButton("Weekly Storage Report");
        weeklyStorageButton.addActionListener(e -> weeklyReportUI());
        panel.add(weeklyStorageButton);

        exitToBranchMenuButton = new JButton("Exit back to branch menu");
        exitToBranchMenuButton.addActionListener(e -> {
            dispose();
            branchMenu.setVisible(true);
        });
        panel.add(exitToBranchMenuButton);

        getContentPane().add(panel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void missingReportUI() {
        setVisible(false);
        JFrame missingReportGUI = new JFrame("Missing Reports Menu");
        missingReportGUI.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        missingReportGUI.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                setVisible(true);
            }
        });

        JPanel panel = new JPanel(new GridLayout(4, 1));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Missing Reports Menu");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 15));
        panel.add(titleLabel);

        JButton createReportButton = new JButton("Create a new Missing Products Report");
        JButton printReportButton = new JButton("Print the current Missing Products Report");
        JButton exitToReportsMenuButton = new JButton("Exit back to reports menu");

        createReportButton.addActionListener(e -> createMissingReport(missingReportGUI));
        panel.add(createReportButton);

        printReportButton.addActionListener(e -> {
            MissingProductsReport report = branch.getBranchReportManager().getCurrentMissingReport();
            if (report != null) {
                JOptionPane.showMessageDialog(this, new JScrollPane(new JTextArea(report.toString())));
            } else {
                JOptionPane.showMessageDialog(this, "Missing Products Report has not been created yet", "Missing Report Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        panel.add(printReportButton);

        exitToReportsMenuButton.addActionListener(e -> {
            missingReportGUI.dispose();
            setVisible(true);
        });
        panel.add(exitToReportsMenuButton);

        missingReportGUI.getContentPane().add(panel);
        missingReportGUI.pack();
        missingReportGUI.setLocationRelativeTo(null);
        missingReportGUI.setVisible(true);
    }

    private void createMissingReport(JFrame missingReportGUI) {
        int reportID = 0;
        try {
            reportID = mainController.getReportDao().getNewReportID();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        MissingProductsReport report = mainController.getBranchController().getReportController().createNewMissingReport(reportID, branch.getBranchID());

        missingReportGUI.setVisible(false);
        JFrame createMissingReport = new JFrame("Create New Missing Report");
        createMissingReport.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        createMissingReport.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                missingReportGUI.setVisible(true);
            }
        });

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10 ,10));

        JPanel productPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        JPanel southPanel = new JPanel(new GridLayout(1, 2, 10, 10));

        JLabel productIdLabel = new JLabel("Product ID:");
        JLabel amountLabel = new JLabel("Amount: ");
        JTextField productIdField = new JTextField(10);
        JSpinner productAmountSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 999999999, 1));
        JButton addButton = new JButton("Add");
        JTextArea reportTextArea = new JTextArea(10, 30);
        JButton finishButton = new JButton("Finish");

        productPanel.add(productIdLabel);
        productPanel.add(amountLabel);
        productPanel.add(new JLabel());
        productPanel.add(productIdField);
        productPanel.add(productAmountSpinner);
        productPanel.add(addButton);
        southPanel.add(new JLabel());
        southPanel.add(finishButton);
        panel.add(productPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(reportTextArea), BorderLayout.CENTER);
        panel.add(southPanel, BorderLayout.SOUTH);

        createMissingReport.getContentPane().add(panel);
        createMissingReport.pack();
        createMissingReport.setLocationRelativeTo(null);
        createMissingReport.setVisible(true);

        StringBuilder reportDetails = new StringBuilder();
        addButton.addActionListener(e -> {
            int productID;
            try {
                productID = Integer.parseInt(productIdField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter an product ID (integer)", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Product productToAdd = null;
            try {
                productToAdd = mainController.getProductController().getProduct(productID);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            if (productToAdd == null) {
                JOptionPane.showMessageDialog(this, "Unknown product ID. Please try again", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (report.getMissingProductsMap().containsKey(productToAdd)){
                JOptionPane.showMessageDialog(this, "The product is already in the report", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            report.addMissingProduct(productToAdd, (int)productAmountSpinner.getValue());

            reportDetails.append("Product Name: ").append(productToAdd.getProductName())
                    .append(", Product ID: ").append(productToAdd.getProductID())
                    .append(", Amount: ").append((int)productAmountSpinner.getValue())
                    .append("\n");
            reportTextArea.setText(reportDetails.toString());

            productIdField.setText("");
            productAmountSpinner.setValue(1);
        });

        finishButton.addActionListener(e -> {
            branch.getBranchReportManager().addNewReport(report);
            try {
                mainController.getReportDao().addReport(report);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            JOptionPane.showMessageDialog(this, "The products have been successfully added to the report.", "Information", JOptionPane.INFORMATION_MESSAGE);
            createMissingReport.dispose();
            missingReportGUI.setVisible(true);
        });
    }

    private void defectiveReportUI() {
        setVisible(false);
        JFrame defectiveReportGUI = new JFrame("Defective Reports Menu");
        defectiveReportGUI.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        defectiveReportGUI.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                setVisible(true);
            }
        });

        JPanel panel = new JPanel(new GridLayout(4, 1));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Defective Reports Menu");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 15));
        panel.add(titleLabel);

        JButton createReportButton = new JButton("Create a new Defective Products Report");
        JButton printReportButton = new JButton("Print the current Defective Products Report");
        JButton exitToReportsMenuButton = new JButton("Exit back to reports menu");

        createReportButton.addActionListener(e -> {
            try {
                createDefectiveReport();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        panel.add(createReportButton);

        printReportButton.addActionListener(e -> {
            DefectiveProductsReport report = branch.getBranchReportManager().getCurrentDefectiveReport();
            if (report != null) {
                JOptionPane.showMessageDialog(this, new JScrollPane(new JTextArea(report.toString())));
            } else {
                JOptionPane.showMessageDialog(this, "Defective Report has not been created yet", "Defective Report Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        panel.add(printReportButton);

        exitToReportsMenuButton.addActionListener(e -> {
            defectiveReportGUI.dispose();
            setVisible(true);
        });
        panel.add(exitToReportsMenuButton);

        defectiveReportGUI.getContentPane().add(panel);
        defectiveReportGUI.pack();
        defectiveReportGUI.setLocationRelativeTo(null);
        defectiveReportGUI.setVisible(true);
    }

    private void createDefectiveReport() throws SQLException {
        int reportID = mainController.getReportDao().getNewReportID();;
        DefectiveProductsReport report = mainController.getBranchController().getReportController().createNewDefectiveReport(reportID, branch.getBranchID());
        List<Item> defectiveItems = mainController.getItemsDao().getAllDamagedItemsByBranchID(branch.getBranchID());
        List<Item> expiredItems = mainController.getItemsDao().getAllExpiredItemsByBranchID(branch.getBranchID());
        if (defectiveItems.size() == 0 && expiredItems.size() == 0)
        {
            JOptionPane.showMessageDialog(this, "We currently have no damaged or expired items to report", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Map<Integer, DefectiveProductsReport> allDefectiveReports = mainController.getReportDao().getAllDefectiveReports();
        if (allDefectiveReports != null) {
            List<Item> allItemsInReports = new ArrayList<>();
            for (DefectiveProductsReport defectiveProductsReport : allDefectiveReports.values()) {
                if (defectiveProductsReport.getBranchID() == branch.getBranchID()) {
                    List<Item> currItemsInReport = defectiveProductsReport.getDefectiveOrExpiredProducts(defectiveProductsReport.getReportID());
                    allItemsInReports.addAll(currItemsInReport);
                }
            }
            for (Item item : defectiveItems)
            {
                boolean check = false;
                if (allItemsInReports.size() > 0) {
                    for (Item item1 : allItemsInReports)
                    {
                        if (item1.getItemID() == item.getItemID())
                        {
                            check = true;
                            break;
                        }
                    }
                    if (!check)
                    {
                        report.addDefectiveItem(item);
                    }
                }
                else {report.addDefectiveItem(item);}
            }
            for (Item item : expiredItems) {
                boolean check = false;
                if (allItemsInReports.size()>0) {
                    for (Item item1 : allItemsInReports) {
                        if (item1.getItemID() == item.getItemID()) {
                            check = true;
                            break;
                        }
                    }
                    if (!check) {
                        report.addDefectiveItem(item);
                    }
                }
                else {report.addDefectiveItem(item);}
            }
        }
        else
        {
            for (Item item : defectiveItems)
            {
                report.addDefectiveItem(item);
            }
            for (Item item : expiredItems)
            {
                report.addDefectiveItem(item);
            }
        }
        branch.getBranchReportManager().addNewReport(report);
        mainController.getReportDao().addReport(report);
        JOptionPane.showMessageDialog(this, "Adding items to the report has been successfully completed.", "Information", JOptionPane.INFORMATION_MESSAGE);
    }

    private void weeklyReportUI() {
        setVisible(false);
        JFrame weeklyReportGUI = new JFrame("Weekly Reports Menu");
        weeklyReportGUI.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        weeklyReportGUI.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                setVisible(true);
            }
        });

        JPanel panel = new JPanel(new GridLayout(4, 1));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Weekly Reports Menu");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 15));
        panel.add(titleLabel);

        JButton createReportButton = new JButton("Create a new Weekly Report");
        JButton printReportButton = new JButton("Print the current Weekly Report");
        JButton exitToReportsMenuButton = new JButton("Exit back to reports menu");

        createReportButton.addActionListener(e -> createWeeklyReport(weeklyReportGUI));
        panel.add(createReportButton);

        printReportButton.addActionListener(e -> {
            WeeklyStorageReport report = branch.getBranchReportManager().getCurrentWeeklyReport();
            if (report != null) {
                JOptionPane.showMessageDialog(this, new JScrollPane(new JTextArea(report.toString(branch))));
            } else {
                JOptionPane.showMessageDialog(this, "Weekly Report has not been created yet", "Weekly Report Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        panel.add(printReportButton);

        exitToReportsMenuButton.addActionListener(e -> {
            weeklyReportGUI.dispose();
            setVisible(true);
        });
        panel.add(exitToReportsMenuButton);

        weeklyReportGUI.getContentPane().add(panel);
        weeklyReportGUI.pack();
        weeklyReportGUI.setLocationRelativeTo(null);
        weeklyReportGUI.setVisible(true);
    }

    private void createWeeklyReport(JFrame weeklyReportGUI) {
        int reportID = 0;
        try {
            reportID = mainController.getReportDao().getNewReportID();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        WeeklyStorageReport report = mainController.getBranchController().getReportController().createNewWeeklyReport(reportID, branch.getBranchID());

        weeklyReportGUI.setVisible(false);
        JFrame createWeeklyReport = new JFrame("Create New Weekly Report");
        createWeeklyReport.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        createWeeklyReport.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                weeklyReportGUI.setVisible(true);
            }
        });

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10 ,10));

        JPanel productPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        JPanel southPanel = new JPanel(new GridLayout(1, 2, 10, 10));

        JLabel productIdLabel = new JLabel("Category ID:");
        JTextField productIdField = new JTextField(10);
        JButton addButton = new JButton("Add");
        JTextArea reportTextArea = new JTextArea(10, 30);
        JButton finishButton = new JButton("Finish");

        productPanel.add(productIdLabel);
        productPanel.add(productIdField);
        productPanel.add(addButton);
        southPanel.add(new JLabel());
        southPanel.add(finishButton);
        panel.add(productPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(reportTextArea), BorderLayout.CENTER);
        panel.add(southPanel, BorderLayout.SOUTH);

        createWeeklyReport.getContentPane().add(panel);
        createWeeklyReport.pack();
        createWeeklyReport.setLocationRelativeTo(null);
        createWeeklyReport.setVisible(true);

        StringBuilder reportDetails = new StringBuilder();
        addButton.addActionListener(e -> {
            int categoryID;
            try {
                categoryID = Integer.parseInt(productIdField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter an product ID (integer)", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Category categoryToAdd = null;
            try {
                categoryToAdd = mainController.getCategoryController().getCategory(categoryID);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            if (categoryToAdd == null) {
                JOptionPane.showMessageDialog(this, "Unknown category ID. Please try again", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (report.getWeeklyReportMap().containsKey(categoryToAdd)){
                JOptionPane.showMessageDialog(this, "The category is already in the report", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            List<Product> products = null;
            Map<Product, Integer> productCurrAmount = new HashMap<>();
            try {
                products = mainController.getProductsDao().getAllProductsInCategory(categoryID);
                for (Product product : products) {
                    int productAmount = mainController.getItemsDao().getAllStorageItemsByBranchIDAndProductID(branch.getBranchID(), product.getProductID()).size();
                    productAmount += mainController.getItemsDao().getAllStoreItemsByBranchIDAndProductID(branch.getBranchID(), product.getProductID()).size();
                    productCurrAmount.put(product, productAmount);
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            report.addCategoryToReport(categoryToAdd, productCurrAmount);

            reportDetails.append("Category Name: ").append(categoryToAdd.getCategoryName())
                    .append(", Category ID: ").append(categoryToAdd.getCategoryID())
                    .append("\n");
            reportTextArea.setText(reportDetails.toString());

            productIdField.setText("");
        });

        finishButton.addActionListener(e -> {
            branch.getBranchReportManager().addNewReport(report);
            try {
                mainController.getReportDao().addReport(report);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            JOptionPane.showMessageDialog(this, "The categories have been successfully added to the report.", "Information", JOptionPane.INFORMATION_MESSAGE);
            createWeeklyReport.dispose();
            weeklyReportGUI.setVisible(true);
        });
    }
}
