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

public class ProductGUI extends JFrame {
    private JButton addProductButton;
    private JButton getCategoryButton;
    private JButton getProductDetailsButton;
    private JButton printAllProductsButton;
    private JButton exitButton;
    private MainController mainController;
    private JFrame storeKeeperMenu;


    public ProductGUI(MainController _mainController, JFrame _storeKeeperMenu) {
        setTitle("Product Menu");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 300);
        setLayout(new GridLayout(6, 1));
        setLocationRelativeTo(null);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                storeKeeperMenu.setVisible(true);
            }
        });

        addProductButton = new JButton("Add new product");
        addProductButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addProduct();
            }
        });

        getCategoryButton = new JButton("Get product categories by ID");
        getCategoryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getCategory();
            }
        });

        getProductDetailsButton = new JButton("Get product details by ID");
        getProductDetailsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                printProductByID();
            }
        });

        printAllProductsButton = new JButton("Print all products");
        printAllProductsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                printAllProducts();
            }
        });

        exitButton = new JButton("Exit to Storekeeper menu");
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                storeKeeperMenu.setVisible(true);
            }
        });
        JLabel titleLabel = new JLabel("Please choose one of the following options :");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 15));
        add(titleLabel);

        mainController = _mainController;
        storeKeeperMenu = _storeKeeperMenu;

        add(addProductButton);
        add(getCategoryButton);
        add(getProductDetailsButton);
        add(printAllProductsButton);
        add(exitButton);

        setVisible(true);
    }

    private void addProduct() {
        setVisible(false);
        JFrame addProductFrame = new JFrame("Add New Product");
        addProductFrame.setLayout(new GridLayout(7, 2));

        addProductFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                setVisible(true);
            }
        });

        JLabel nameLabel = new JLabel("Product Name:");
        JTextField nameField = new JTextField();

        JLabel manufacturerLabel = new JLabel("Manufacturer:");
        JTextField manufacturerField = new JTextField();

        JLabel weightLabel = new JLabel("Weight (in gr):");
        JTextField weightField = new JTextField();

        JLabel parentLabel = new JLabel("Parent Category ID:");
        JTextField parentField = new JTextField();

        JLabel subLabel = new JLabel("Sub Category ID:");
        JTextField subField = new JTextField();

        JLabel subSubLabel = new JLabel("SubSub Category ID:");
        JTextField subSubField = new JTextField();

        JButton addButton = new JButton("Add Product");
        addButton.addActionListener(e -> {
            String newProductName = nameField.getText();
            String manufacturer = manufacturerField.getText();
            if (newProductName.isEmpty() || manufacturer.isEmpty() || weightField.getText().isEmpty() || parentField.getText().isEmpty() || subField.getText().isEmpty() || subSubField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Fields cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if(newProductName.matches(".*\\d.*") || manufacturer.matches(".*\\d.*")){
                JOptionPane.showMessageDialog(null, "Invalid product name or manufacturer, these fields should not contain numbers.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double weight;
            int parentInt, subInt, subSubInt;
            try {
                weight = Double.parseDouble(weightField.getText());
                parentInt = Integer.parseInt(parentField.getText());
                subInt = Integer.parseInt(subField.getText());
                subSubInt = Integer.parseInt(subSubField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid numeric input. Please enter positive numbers only.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!(subSubInt != subInt && subSubInt != parentInt && subInt != parentInt)) {
                JOptionPane.showMessageDialog(null, "The three categories must be different", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (weight <= 0 || parentInt <= 0 || subInt <= 0 || subSubInt <= 0) {
                JOptionPane.showMessageDialog(null, "Numeric inputs must be positive.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Category parent = null;
            Category sub = null;
            Category subSub = null;
            try {
                parent = mainController.getCategoryDao().getCategoryByID(parentInt);
                sub = mainController.getCategoryDao().getCategoryByID(subInt);
                subSub = mainController.getCategoryDao().getCategoryByID(subSubInt);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            if (parent == null || sub == null || subSub == null) {
                JOptionPane.showMessageDialog(null, "There is some problem importing the categories", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Product product = null;
            try {
                product = mainController.getProductController().createProduct(newProductName, weight, manufacturer, parent, sub, subSub);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            if (product != null) {
                try {
                    if (mainController.getProductController().newProductToAllBranches(product)) {
                        JOptionPane.showMessageDialog(null, "Product added successfully!\nProduct Details:\n" + product);
                    } else {
                        JOptionPane.showMessageDialog(null, "The product name you provided already exists under the manufacturer you provided in the system");
                    }
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
            addProductFrame.dispose();
            setVisible(true);
        });

        addProductFrame.add(nameLabel);
        addProductFrame.add(nameField);
        addProductFrame.add(manufacturerLabel);
        addProductFrame.add(manufacturerField);
        addProductFrame.add(weightLabel);
        addProductFrame.add(weightField);
        addProductFrame.add(parentLabel);
        addProductFrame.add(parentField);
        addProductFrame.add(subLabel);
        addProductFrame.add(subField);
        addProductFrame.add(subSubLabel);
        addProductFrame.add(subSubField);
        addProductFrame.add(new JLabel()); // Placeholder for spacing
        addProductFrame.add(addButton);

        addProductFrame.pack();
        addProductFrame.setLocationRelativeTo(null);
        addProductFrame.setVisible(true);
    }

    private void getCategory() {
        setVisible(false);
        JFrame getCategoryFrame = new JFrame("Get product categories by ID");
        getCategoryFrame.setLayout(new GridLayout(2, 2));

        getCategoryFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                setVisible(true);
            }
        });

        JLabel idLabel = new JLabel("Product ID:");
        JTextField idField = new JTextField();

        JButton getCategories = new JButton("Get Categories");
        getCategories.addActionListener(e -> {
            int productID;
            try {
                productID = Integer.parseInt(idField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid numeric input. Please enter positive numbers only.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (productID <= 0) {
                JOptionPane.showMessageDialog(null, "Numeric inputs must be positive.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Product product = null;
            try {
                product = mainController.getProductController().getProduct(productID);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            if (product == null) {
                JOptionPane.showMessageDialog(null, "We do not have a product in the system with the ID number you provided", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Category parent = product.getParentCategory();
            Category sub = product.getSubCategory();
            Category subSub = product.getSubSubCategory();
            String output = "** Category ID: " + parent.getCategoryID() + " ** " + "Category Name: " + parent.getCategoryName() + " **\n" + "** Category ID: " + sub.getCategoryID() + " ** " + "Category Name: " + sub.getCategoryName() + " **\n" +
                    "** Category ID: " + subSub.getCategoryID() + " ** " + "Category Name: " + subSub.getCategoryName() + " **\n";
            JOptionPane.showMessageDialog(null, "The product: " + product.getProductName() + " with the ID: " + productID + " is under the following categories:\n" + output);
            getCategoryFrame.dispose();
            setVisible(true);
        });
        getCategoryFrame.add(idLabel);
        getCategoryFrame.add(idField);
        getCategoryFrame.add(new JLabel());
        getCategoryFrame.add(getCategories);
        getCategoryFrame.pack();
        getCategoryFrame.setLocationRelativeTo(null);
        getCategoryFrame.setVisible(true);
    }

    private void printProductByID() {
        setVisible(false);
        JFrame printProductFrame = new JFrame("Get product details by ID");
        printProductFrame.setLayout(new GridLayout(2, 2));

        printProductFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                setVisible(true);
            }
        });

        JLabel idLabel = new JLabel("Product ID:");
        JTextField idField = new JTextField();

        JButton getProductDetails = new JButton("Get details");
        getProductDetails.addActionListener(e -> {
            int productID;
            try {
                productID = Integer.parseInt(idField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid numeric input. Please enter positive numbers only.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (productID <= 0) {
                JOptionPane.showMessageDialog(null, "Numeric inputs must be positive.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Product product = null;
            try {
                product = mainController.getProductController().getProduct(productID);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            if (product == null) {
                JOptionPane.showMessageDialog(null, "We do not have a product in the system with the ID number you provided", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            JOptionPane.showMessageDialog(null, product);
            printProductFrame.dispose();
            setVisible(true);
        });
        printProductFrame.add(idLabel);
        printProductFrame.add(idField);
        printProductFrame.add(new JLabel());
        printProductFrame.add(getProductDetails);
        printProductFrame.pack();
        printProductFrame.setLocationRelativeTo(null);
        printProductFrame.setVisible(true);
    }
    private void printAllProducts() {
        setVisible(false);
        List<Product> products = null;
        try {
            products = mainController.getProductController().getAllProducts();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (products == null) {
            JOptionPane.showMessageDialog(null, "We currently have no products in the system", "Error", JOptionPane.ERROR_MESSAGE);
            setVisible(true);
            return;
        }
        JFrame allProducts = new JFrame("All Products");
        allProducts.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        allProducts.setSize(400, 500);
        JLabel titleLabel = new JLabel("The system includes the following products:");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 15));

        DefaultTableModel productsTableModel = new DefaultTableModel();
        productsTableModel.addColumn("Name");
        productsTableModel.addColumn("Product ID");
        productsTableModel.addColumn("Manufacturer");
        productsTableModel.addColumn("Weight");
        productsTableModel.addColumn("Parent Category");
        productsTableModel.addColumn("Sub Category");
        productsTableModel.addColumn("Sub Sub Category");
        JTable productsTable = new JTable(productsTableModel);
        JScrollPane scrollPane = new JScrollPane(productsTable);
        scrollPane.setPreferredSize(new Dimension(500, 200));

        for (Product product : products) {
            productsTableModel.addRow(new Object[]{product.getProductName(), product.getProductID(), product.getManufacturer(),
                    product.getProductWeight(), product.getParentCategory().getCategoryName(),
                    product.getSubCategory().getCategoryName(), product.getSubSubCategory().getCategoryName()});
        }

        allProducts.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                setVisible(true);
            }
        });

        allProducts.setLayout(new BorderLayout());
        allProducts.add(titleLabel, BorderLayout.NORTH);
        allProducts.add(scrollPane, BorderLayout.CENTER);
        allProducts.setLocationRelativeTo(null);
        allProducts.setVisible(true);
    }
}
//