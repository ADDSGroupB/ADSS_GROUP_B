package InterfaceLayer.GUI;

import BusinessLayer.InventoryBusinessLayer.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.List;

public class CategoryGUI extends JFrame {
    private JButton addCategoryButton;
    private JButton getCategoryButton;
    private JButton printAllCategoriesButton;
    private JButton exitButton;
    private MainController mainController;
    private JFrame storeKeeperMenu;


    public CategoryGUI(MainController _mainController, JFrame _storeKeeperMenu) {
        setTitle("Category Menu");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 300);
        setLayout(new GridLayout(5, 1));
        setLocationRelativeTo(null);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                storeKeeperMenu.setVisible(true);
            }
        });

        addCategoryButton = new JButton("Add new category");
        addCategoryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addCategory();
            }
        });

        getCategoryButton = new JButton("Get category details by ID");
        getCategoryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getCategoryByID();
            }
        });

        printAllCategoriesButton = new JButton("Print all categories");
        printAllCategoriesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                printAllCategories();
            }
        });

        exitButton = new JButton("Exit to Storekeeper menu");
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                storeKeeperMenu.setVisible(true);
            }
        });//
        JLabel titleLabel = new JLabel("Please choose one of the following options :");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 15));
        add(titleLabel);

        mainController = _mainController;
        storeKeeperMenu = _storeKeeperMenu;

        add(addCategoryButton);
        add(getCategoryButton);
        add(printAllCategoriesButton);
        add(exitButton);

        setVisible(true);
    }

    private void addCategory() {
        String newCategoryName = JOptionPane.showInputDialog("Enter the name of the new category:");
        if (newCategoryName == null) {
            return;
        }
        if (newCategoryName.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Category name cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if(newCategoryName.matches(".*\\d.*")){
            JOptionPane.showMessageDialog(null, "Invalid category name. Category name should not contain numbers.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            if (mainController.getCategoryDao().checkNewCategoryName(newCategoryName))
            {
                Category category = mainController.getCategoryController().createCategory(newCategoryName);
                if (category != null)
                {
                    String result = "The category has been successfully added.\n"
                            + "Below are the details of the newly created category:\n"
                            + category;
                    JOptionPane.showMessageDialog(null, result);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void getCategoryByID() {
        int categoryID;
        String inputDialog = JOptionPane.showInputDialog("Enter the ID of the category:");
        if (inputDialog == null)
            return;
        if (inputDialog.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Category ID cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            categoryID = Integer.parseInt(inputDialog);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Invalid numeric input. Please enter positive numbers only.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (categoryID <= 0) {
            JOptionPane.showMessageDialog(null, "Numeric inputs must be positive.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Category category = null;
        try {
            category = mainController.getCategoryController().getCategory(categoryID);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (category == null) {
            JOptionPane.showMessageDialog(null, "We do not have a category in the system with the ID number you provided.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        List<Product> productsInCategory = null;
        try {
            productsInCategory = mainController.getCategoryController().getProductInCategory(categoryID);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (productsInCategory != null)
        {
            category.setProductsToCategory(productsInCategory);
        }
        JTextArea textArea = new JTextArea(category.toString());
        JScrollPane scrollPane = new JScrollPane(textArea);
        JOptionPane.showMessageDialog(null, scrollPane, "Category Details", JOptionPane.INFORMATION_MESSAGE);
    }
    private void printAllCategories() {
        setVisible(false);
        List<Category> categories = null;
        try {
            categories = mainController.getCategoryController().getAllCategories();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (categories == null) {
            JOptionPane.showMessageDialog(null, "We currently have no categories in the system", "Error", JOptionPane.ERROR_MESSAGE);
            setVisible(true);
            return;
        }
        JFrame allCategories = new JFrame("All Categories");
        allCategories.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        allCategories.setSize(400, 500);
        JLabel titleLabel = new JLabel("The system includes the following categories:");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 15));

        DefaultTableModel productsTableModel = new DefaultTableModel();
        productsTableModel.addColumn("Category Name");
        productsTableModel.addColumn("Category ID");
        JTable productsTable = new JTable(productsTableModel);
        JScrollPane scrollPane = new JScrollPane(productsTable);
        scrollPane.setPreferredSize(new Dimension(500, 200));

        for (Category category : categories) {
            productsTableModel.addRow(new Object[]{category.getCategoryName(), category.getCategoryID()});
        }

        allCategories.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                setVisible(true);
            }
        });

        allCategories.setLayout(new BorderLayout());
        allCategories.add(titleLabel, BorderLayout.NORTH);
        allCategories.add(scrollPane, BorderLayout.CENTER);
        allCategories.setLocationRelativeTo(null);
        allCategories.setVisible(true);
    }
}
