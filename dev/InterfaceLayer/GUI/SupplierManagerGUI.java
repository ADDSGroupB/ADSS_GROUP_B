package InterfaceLayer.GUI;

import BusinessLayer.SupplierBusinessLayer.Order;
import BusinessLayer.SupplierBusinessLayer.PeriodicOrder;
import BusinessLayer.SupplierBusinessLayer.SupplierProduct;
import ServiceLayer.SupplierServiceLayer.*;
import Utillity.Pair;
import Utillity.Response;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SupplierManagerGUI extends JFrame {

    private SupplierService supplierService;
    private ServiceContact serviceContact;

    private String name;
    private String address;
    private String bankAccount;
    private ArrayList<ServiceContact> serviceContacts;
    private String paymentMethod;
    private String supplyMethod;
    private int supplyTime;
    private boolean selfSupply;
    private ArrayList<DayOfWeek> days = new ArrayList<>();
    private HashMap<Integer, SupplierProductService> items = new HashMap<>();
    private Pair<Integer, Double> amountPair;
    private Pair<Double, Double> pricePair;
    //    private HashMap<Integer, Double> discounts = new HashMap<>();
    private SupplierProductService currProduct;

    // ------------------------------------------------------------------------

    private SupplierProductService product = new SupplierProductService();

    public SupplierManagerGUI() {
        supplierService = new SupplierService();
        serviceContact = new ServiceContact();

        JFrame currentFrame = this;
        setTitle("Supplier Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(7, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Supplier Manager");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));

        JButton addSupplierButton = new JButton("Add new Supplier");
        addSupplierButton.addActionListener(e -> {
            addNewSupplier();
            this.setVisible(false);
        });

        JButton deleteSupplierButton = new JButton("Delete Supplier");
        deleteSupplierButton.addActionListener(e -> {
            deleteSupplier(currentFrame);
            this.setVisible(false);

        });

        JButton editSupplierButton = new JButton("Edit Supplier's information");
        editSupplierButton.addActionListener(e -> {
            editSupplier(currentFrame);
            this.setVisible(false);
        });

        JButton printSuppliersButton = new JButton("Print suppliers");
        printSuppliersButton.addActionListener(e -> printSuppliersGui());

        JButton supplierOrderHistoryButton = new JButton("Show Supplier Order History");
        supplierOrderHistoryButton.addActionListener(e ->{
            supplierOrderHistory(currentFrame);
            this.setVisible(false);
        });

        JButton backButton = new JButton("EXIT");
        backButton.addActionListener(e -> dispose());

        panel.add(titleLabel);
        panel.add(addSupplierButton);
        panel.add(deleteSupplierButton);
        panel.add(editSupplierButton);
        panel.add(printSuppliersButton);
        panel.add(supplierOrderHistoryButton);
        panel.add(backButton);

        getContentPane().add(panel);
//        setVisible(true);
    }

    private void addNewSupplier() {
        cleanAddNewSupplierValues();
        // --------------------------- Add New Supplier ---------------------------
        ArrayList<JFrame> addSupplierFrame = new ArrayList<>();
        addSupplierFrame.add(new JFrame("Add New Supplier")); // 0
        addSupplierFrame.add(new JFrame("Add Contacts")); // 1
        addSupplierFrame.add(new JFrame("Choose Supplying Method")); // 2
        addSupplierFrame.add(new JFrame("Choose Days To Supply - Fixed Days")); // 3
        addSupplierFrame.add(new JFrame("Choose Days To Supply - Days Amount")); // 4
        addSupplierFrame.add(new JFrame("Add Items To The Agreement")); // 5
        addSupplierFrame.add(new JFrame("Add Amount Discount To The Agreement")); // 6
        addSupplierFrame.add(new JFrame("Add Price Discount To The Agreement")); // 7
        addSupplierFrame.add(new JFrame("Add Product Discount")); // 8
        addSupplierFrame.add(new JFrame("Add Payment Type")); // 9

        for (int i = 0; i < 10; i++) {
            addSupplierFrame.get(i).setSize(400, 300);
            addSupplierFrame.get(i).setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            addSupplierFrame.get(i).setLocationRelativeTo(null);
        }

        setAddContactFrame(addSupplierFrame);
        setPaymentMethodFrame(addSupplierFrame);
        setSupplyingMethodFrame(addSupplierFrame);
        setFixedDaysFrame(addSupplierFrame);
        setDaysAmountFrame(addSupplierFrame);
        setSAddItemsFrame(addSupplierFrame);
        setDiscountFrame(addSupplierFrame);
        setAddAmountDiscountFrame(addSupplierFrame);
        setAddPriceDiscountFrame(addSupplierFrame);


        JFrame backFrame = this;
        JFrame currentFrame = addSupplierFrame.get(0);
        JFrame nextFrame = addSupplierFrame.get(1);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel nameLabel = new JLabel("Supplier's Name:");
        JTextField nameTextField = new JTextField();

        JLabel addressLabel = new JLabel("Supplier's Address:");
        JTextField addressTextField = new JTextField();

        JLabel bankAccountLabel = new JLabel("Bank Account Number:");
        JTextField bankAccountTextField = new JTextField();
        setFormatExample(bankAccountTextField, "6-9 digits");

        JButton nextButton = new JButton("Next");
        nextButton.addActionListener(e -> {
            name = nameTextField.getText();
            address = addressTextField.getText();
            bankAccount = bankAccountTextField.getText();
            if (name.length() == 0) {
                JOptionPane.showMessageDialog(null, "You must enter name, please try again", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (address.length() == 0) {
                JOptionPane.showMessageDialog(null, "You must enter address, please try again", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!checkBankAccountValidation(bankAccount)) {
                JOptionPane.showMessageDialog(null, "Not a valid bankAccount number, please try again", "Error", JOptionPane.ERROR_MESSAGE);
                bankAccountTextField.setText("");
                return;
            }

            Response res = supplierService.checkBankAccount(bankAccount);
            if (res.errorOccurred())
            {
                JOptionPane.showMessageDialog(null, "This bank account is already exist", "Bank Account Already Exists", JOptionPane.ERROR_MESSAGE);
                return;
            }

            currentFrame.setVisible(false);
            nextFrame.setVisible(true);
            //Adjust Size
            nextFrame.pack();
            // Close the dialog/frame after adding the supplier
//            addSupplierFrame.get(0).dispose();
        });

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            currentFrame.setVisible(false);
            backFrame.setVisible(true);
         //   backFrame.pack();
        });

        JFrame supplierManagerFrame = this;
        currentFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                currentFrame.setVisible(false);
                supplierManagerFrame.setVisible(true);
            }
        });


        panel.add(nameLabel);
        panel.add(nameTextField);
        panel.add(addressLabel);
        panel.add(addressTextField);
        panel.add(bankAccountLabel);
        panel.add(bankAccountTextField);
        panel.add(backButton);
        panel.add(nextButton);

        currentFrame.getContentPane().add(panel);
        currentFrame.setVisible(true);
        currentFrame.pack();

//        addSupplierFrame.get(5).setVisible(true);
//        addSupplierFrame.get(5).pack();
    }


    private void setAddContactFrame(ArrayList<JFrame> addSupplierFrame) {
        // Set the frames
        JFrame backFrame = addSupplierFrame.get(0);
        JFrame currentFrame = addSupplierFrame.get(1);
        JFrame nextFrame = addSupplierFrame.get(9);
        JFrame supplierManagerFrame = this;
        currentFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                currentFrame.setVisible(false);
                supplierManagerFrame.setVisible(true);
            }
        });


        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        DefaultTableModel contactTableModel = new DefaultTableModel();
        contactTableModel.addColumn("Name");
        contactTableModel.addColumn("Email");
        contactTableModel.addColumn("Phone Number");
        JTable contactTable = new JTable(contactTableModel);
        JScrollPane scrollPane = new JScrollPane(contactTable);
        scrollPane.setPreferredSize(new Dimension(400, 200));

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(4, 2, 10, 10));

        JLabel nameLabel = new JLabel("Contact Name:");
        JTextField nameTextField = new JTextField();

        JLabel emailLabel = new JLabel("Contact Email:");
        JTextField emailTextField = new JTextField();
        setFormatExample(emailTextField, "____@___.___");

        JLabel phoneNumberLabel = new JLabel("Contact Phone Number:");
        JTextField phoneNumberTextField = new JTextField();
        setFormatExample(phoneNumberTextField, "05_-_______");

        JButton addAnotherContact = new JButton("Add Contact");

        inputPanel.add(nameLabel);
        inputPanel.add(nameTextField);
        inputPanel.add(emailLabel);
        inputPanel.add(emailTextField);
        inputPanel.add(phoneNumberLabel);
        inputPanel.add(phoneNumberTextField);
        inputPanel.add(new JLabel());
        inputPanel.add(addAnotherContact);

        addAnotherContact.addActionListener(e -> {
            if (nameTextField.getText().length() == 0) {
                JOptionPane.showMessageDialog(null, "You must enter name, please try again", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!serviceContact.isValidEmail(emailTextField.getText())) {
                JOptionPane.showMessageDialog(null, "Not a valid email, please try again", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!serviceContact.validatePhoneNumber(phoneNumberTextField.getText())) {
                JOptionPane.showMessageDialog(null, "Not a valid phone number, please try again", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            for (int row = 0; row < contactTableModel.getRowCount(); row++) {
                Object value = contactTableModel.getValueAt(row, 2);
                if (value != null && value.toString().equals(phoneNumberTextField.getText())) {
                    JOptionPane.showMessageDialog(null, "You already add this phone number, please try again", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            ServiceContact newContact = new ServiceContact(nameTextField.getText(), emailTextField.getText(), phoneNumberTextField.getText());
            serviceContacts.add(newContact);
            contactTableModel.addRow(new Object[]{newContact.getName(), newContact.getEmail(), newContact.getPhoneNumber()});
            nameTextField.setText("");
            emailTextField.setText("");
            phoneNumberTextField.setText("");
        });

        JButton nextButton = new JButton("Next");
        nextButton.addActionListener(e -> {
            if (serviceContacts.size() < 1) {
                JOptionPane.showMessageDialog(null, "You must add at least one contact", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            currentFrame.setVisible(false);
            nextFrame.setVisible(true);
            //Adjust Size
            nextFrame.pack();
        });

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            currentFrame.setVisible(false);
            backFrame.setVisible(true);
            backFrame.pack();
        });

        JPanel backNextPanel = new JPanel();
        backNextPanel.setLayout(new GridLayout(1, 2, 10, 10));
        backNextPanel.add(backButton);
        backNextPanel.add(nextButton);

        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(backNextPanel, BorderLayout.SOUTH);


        currentFrame.getContentPane().add(panel);
    }

    private void setPaymentMethodFrame(ArrayList<JFrame> addSupplierFrame) {
        JFrame backFrame = addSupplierFrame.get(1);
        JFrame currentFrame = addSupplierFrame.get(9);
        JFrame nextFrame = addSupplierFrame.get(2);

        JFrame supplierManagerFrame = this;
        currentFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                currentFrame.setVisible(false);
                supplierManagerFrame.setVisible(true);
            }
        });

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel type = new JLabel("Payment Type: ");
        JComboBox<String> combo = new JComboBox<>(new String[]{"Cash", "TransitToAccount", "Credit", "net 30 EOM", "net 60 EOM"});
        combo.setEditable(false);
        combo.setSelectedIndex(-1);

        panel.add(type);
        panel.add(combo);

        JButton nextButton = new JButton("Next");
        nextButton.addActionListener(e -> {
            paymentMethod = combo.getSelectedItem().toString();
            currentFrame.setVisible(false);
            nextFrame.setVisible(true);
            nextFrame.pack();
        });

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            currentFrame.setVisible(false);
            backFrame.setVisible(true);
            //Adjust Size
            backFrame.pack();
        });

        panel.add(backButton);
        panel.add(nextButton);
        currentFrame.getContentPane().add(panel);
    }

    private void setSupplyingMethodFrame(ArrayList<JFrame> addSupplierFrame) {
        JFrame backFrame = addSupplierFrame.get(9);
        JFrame currentFrame = addSupplierFrame.get(2);
        JFrame fixedDayFrame = addSupplierFrame.get(3);
        JFrame daysAmountFrame = addSupplierFrame.get(4);

        JFrame supplierManagerFrame = this;
        currentFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                currentFrame.setVisible(false);
                supplierManagerFrame.setVisible(true);
            }
        });


        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel type = new JLabel("Supplying Method: ");
        JComboBox<String> combo = new JComboBox<>(new String[]{"By Days", "By Order", "By Super-lee"});
        combo.setEditable(false);
        combo.setSelectedIndex(-1);

        panel.add(type);
        panel.add(combo);

        JButton nextButton = new JButton("Next");
        nextButton.addActionListener(e -> {
            currentFrame.setVisible(false);
            switch (combo.getSelectedIndex()) {
                case 0 -> {
                    selfSupply = true;
                    supplyMethod = "FixedDay";
                    supplyTime = -1;
                    days.clear();
                    fixedDayFrame.setVisible(true);
                    //Adjust Size
                    fixedDayFrame.pack();
                }
                case 1 -> {
                    selfSupply = true;
                    supplyMethod = "DaysAmount";
                    days.clear();
                    daysAmountFrame.setVisible(true);
                    daysAmountFrame.pack();
                }
                case 2 -> {
                    selfSupply = false;
                    supplyMethod = "SuperLee";
                    days.clear();
                    daysAmountFrame.setVisible(true);
                    daysAmountFrame.pack();
                }
            }
        });

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            currentFrame.setVisible(false);
            backFrame.setVisible(true);
            //Adjust Size
            backFrame.pack();
        });

        panel.add(backButton);
        panel.add(nextButton);
        currentFrame.getContentPane().add(panel);

    }

    private void setFixedDaysFrame(ArrayList<JFrame> addSupplierFrame) {
        JFrame backFrame = addSupplierFrame.get(2);
        JFrame currentFrame = addSupplierFrame.get(3);
        JFrame nextFrame = addSupplierFrame.get(5);

        JFrame supplierManagerFrame = this;
        currentFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                currentFrame.setVisible(false);
                supplierManagerFrame.setVisible(true);
            }
        });

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(9, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel fixedDays = new JLabel("Supplying Days: ");
        panel.add(fixedDays);

        String[] weekdays = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        JCheckBox[] dayCheckboxes = new JCheckBox[weekdays.length];
        for (int i = 0; i < weekdays.length; i++) {
            dayCheckboxes[i] = new JCheckBox(weekdays[i]);
            panel.add(new JLabel());
            panel.add(dayCheckboxes[i]);
        }

        JButton nextButton = new JButton("Next");
        nextButton.addActionListener(e -> {
            currentFrame.setVisible(false);
            for (int i = 0; i < dayCheckboxes.length; i++)
                if (dayCheckboxes[i].isSelected())
                    days.add(DayOfWeek.valueOf(weekdays[i].toUpperCase()));
            nextFrame.setVisible(true);
            nextFrame.pack();
            nextFrame.setLocationRelativeTo(null);
            nextFrame.setLocation(nextFrame.getX(), nextFrame.getY() - 30);
        });

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            currentFrame.setVisible(false);
            backFrame.setVisible(true);
            //Adjust Size
            backFrame.pack();
        });

        panel.add(new JLabel());
        panel.add(backButton);
        panel.add(nextButton);
        currentFrame.getContentPane().add(panel);
    }

    private void setDaysAmountFrame(ArrayList<JFrame> addSupplierFrame) {
        JFrame backFrame = addSupplierFrame.get(2);
        JFrame currentFrame = addSupplierFrame.get(4);
        JFrame nextFrame = addSupplierFrame.get(5);

        JFrame supplierManagerFrame = this;
        currentFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                currentFrame.setVisible(false);
                supplierManagerFrame.setVisible(true);
            }
        });

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel daysAmountLabel = new JLabel("Number Of Days To Supply: ");
        panel.add(daysAmountLabel);

        JTextField daysAmountTextField = new JTextField();
        panel.add(daysAmountTextField);

        JButton nextButton = new JButton("Next");
        nextButton.addActionListener(e -> {

            try {
                supplyTime = Integer.parseInt(daysAmountTextField.getText());
            } catch (NumberFormatException exxception) {
                JOptionPane.showMessageDialog(null, "You must enter valid number, please try again", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            currentFrame.setVisible(false);
            nextFrame.setVisible(true);
            nextFrame.pack();
            nextFrame.setLocationRelativeTo(null);
            nextFrame.setLocation(nextFrame.getX(), nextFrame.getY() - 30);
        });

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            currentFrame.setVisible(false);
            backFrame.setVisible(true);
            //Adjust Size
            backFrame.pack();
        });

        panel.add(backButton);
        panel.add(nextButton);
        currentFrame.getContentPane().add(panel);
    }

    private void setSAddItemsFrame(ArrayList<JFrame> addSupplierFrame) {
        JFrame backFrame = supplyMethod.equals("FixedDay") ? addSupplierFrame.get(3) : addSupplierFrame.get(4);
        JFrame currentFrame = addSupplierFrame.get(5);
        JFrame nextFrame = addSupplierFrame.get(6);

        JFrame supplierManagerFrame = this;
        currentFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                currentFrame.setVisible(false);
                supplierManagerFrame.setVisible(true);
            }
        });

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        DefaultTableModel productsTableModel = new DefaultTableModel();
        productsTableModel.addColumn("Name");
        productsTableModel.addColumn("ID");
        productsTableModel.addColumn("Catalog Number");
        productsTableModel.addColumn("Price");
        productsTableModel.addColumn("Amount");
        productsTableModel.addColumn("Manufacturer");
        productsTableModel.addColumn("Expiration Days");
        productsTableModel.addColumn("Weight");
        JTable productsTable = new JTable(productsTableModel);
        JScrollPane scrollPane = new JScrollPane(productsTable);
        scrollPane.setPreferredSize(new Dimension(500, 200));

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(9, 2, 10, 10));

        JLabel nameLabel = new JLabel("Product Name:");
        JTextField nameTextField = new JTextField();
        inputPanel.add(nameLabel);
        inputPanel.add(nameTextField);

        JLabel idLabel = new JLabel("Product ID:");
        JTextField idTextField = new JTextField();
        inputPanel.add(idLabel);
        inputPanel.add(idTextField);

        JLabel catalogNumberLabel = new JLabel("Product Catalog Number:");
        JTextField catalogNumberTextField = new JTextField();
        inputPanel.add(catalogNumberLabel);
        inputPanel.add(catalogNumberTextField);

        JLabel PriceLabel = new JLabel("Product Price:");
        JTextField PriceTextField = new JTextField();
        inputPanel.add(PriceLabel);
        inputPanel.add(PriceTextField);

        JLabel amountLabel = new JLabel("Product Amount:");
        JTextField amountTextField = new JTextField();
        inputPanel.add(amountLabel);
        inputPanel.add(amountTextField);

        JLabel manufacturerLabel = new JLabel("Product Manufacturer:");
        JTextField manufacturerTextField = new JTextField();
        inputPanel.add(manufacturerLabel);
        inputPanel.add(manufacturerTextField);

        JLabel expirationDaysLabel = new JLabel("Product Expiration Days:");
        JTextField expirationDaysTextField = new JTextField();
        inputPanel.add(expirationDaysLabel);
        inputPanel.add(expirationDaysTextField);

        JLabel weightLabel = new JLabel("Product Weight:");
        JTextField weightTextField = new JTextField();
        inputPanel.add(weightLabel);
        inputPanel.add(weightTextField);

        JButton addProduct = new JButton("Add Product");
//        JButton addProductDiscount = new JButton("Add Discount");

        JLabel rightClickLabel = new JLabel("Right click on product that you want add discount to");
        rightClickLabel.setFont(rightClickLabel.getFont().deriveFont(Font.ITALIC));
        rightClickLabel.setForeground(Color.GRAY);
        inputPanel.add(rightClickLabel);
        inputPanel.add(addProduct);

        addProduct.addActionListener(e -> {
            int productID;
            try {
                productID = Integer.parseInt(idTextField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid product ID, please enter a valid integer", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int catalogNumber;
            try {
                catalogNumber = Integer.parseInt(catalogNumberTextField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid catalog number, please enter a valid integer", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double price;
            try {
                price = Double.parseDouble(PriceTextField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid product price, please enter a valid number", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int amount;
            try {
                amount = Integer.parseInt(amountTextField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid product amount, please enter a valid integer", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int expirationDays;
            try {
                expirationDays = Integer.parseInt(expirationDaysTextField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid expiration days, please enter a valid integer", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double weight;
            try {
                weight = Double.parseDouble(weightTextField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid product weight, please enter a valid number", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String productName = nameTextField.getText();
            String manufacturer = manufacturerTextField.getText();

            if (productName.length() == 0) {
                JOptionPane.showMessageDialog(null, "You must enter product name, please try again", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (manufacturer.length() == 0) {
                JOptionPane.showMessageDialog(null, "You must enter product name, please try again", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (items.containsKey(productID)) {
                JOptionPane.showMessageDialog(null, "This product ID already exists on this supplier, please try again", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // Add the product after adding the discount
//            if (discounts.size() < 1) {
//                int choice = JOptionPane.showConfirmDialog(null, "Do you want to add discount?", "Confirmation", JOptionPane.YES_NO_OPTION);
//                if (choice == JOptionPane.YES_OPTION) {
//                    //discount frame
//                    addSupplierFrame.get(8).setVisible(true);
//                    currentFrame.setVisible(false);
//                    //Adjust Size
//                    nextFrame.pack();
//                }
//            }
            SupplierProductService newSupplierProduct = new SupplierProductService(productName, productID, catalogNumber, price, amount, new HashMap<>(), manufacturer, expirationDays, weight);
            items.put(productID, newSupplierProduct);
            productsTableModel.addRow(new Object[]{productName, productID, catalogNumber, price, amount, manufacturer, expirationDays, weight});
            nameTextField.setText("");
            idTextField.setText("");
            catalogNumberTextField.setText("");
            PriceTextField.setText("");
            amountTextField.setText("");
            manufacturerTextField.setText("");
            expirationDaysTextField.setText("");
            weightTextField.setText("");
//            discounts.clear();
        });

        productsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    JPopupMenu popupMenu = new JPopupMenu();
                    JMenuItem addDiscountMenuItem = new JMenuItem("Add Discount");
                    addDiscountMenuItem.addActionListener(ee -> {
                        // Handle the action when "Add Discount" is clicked
                        // discount frame
                        int selectedRow = productsTable.rowAtPoint(e.getPoint());
                        if (selectedRow >= 0)
                            currProduct = items.get(Integer.parseInt(productsTable.getValueAt(selectedRow, 1).toString()));
                        currentFrame.setVisible(false);
                        addSupplierFrame.get(8).setVisible(true);
                        // Adjust Size
                        addSupplierFrame.get(8).pack();
                    });
                    popupMenu.add(addDiscountMenuItem);

                    popupMenu.show(productsTable, e.getX(), e.getY());
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    JPopupMenu popupMenu = new JPopupMenu();
                    JMenuItem addDiscountMenuItem = new JMenuItem("Add Discount");
                    addDiscountMenuItem.addActionListener(ee -> {
                        // Handle the action when "Add Discount" is clicked
                        int selectedRow = productsTable.rowAtPoint(e.getPoint());
                        if (selectedRow >= 0)
                            currProduct = items.get(Integer.parseInt(productsTable.getValueAt(selectedRow, 1).toString()));
                        // discount frame
                        addSupplierFrame.get(8).setVisible(true);
                        addSupplierFrame.get(5).setVisible(false);
                        // Adjust Size
                        addSupplierFrame.get(6).pack();
                    });
                    popupMenu.add(addDiscountMenuItem);

                    popupMenu.show(productsTable, e.getX(), e.getY());
                }
            }
        });

//        addProductDiscount.addActionListener(e -> {
//            //discount frame
//            addSupplierFrame.get(8).setVisible(true);
//            currentFrame.setVisible(false);
//            //Adjust Size
//            nextFrame.pack();
//
//        });

        JButton nextButton = new JButton("Next");
        nextButton.addActionListener(e -> {
            currentFrame.setVisible(false);
            nextFrame.setVisible(true);
//            nextFrame.setLocationRelativeTo(null);
            //Adjust Size
            nextFrame.pack();
        });

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            currentFrame.setVisible(false);
            backFrame.setVisible(true);
//            backFrame.setLocationRelativeTo(null);
            backFrame.pack();
        });

        JPanel backNextPanel = new JPanel();
        backNextPanel.setLayout(new GridLayout(1, 2, 10, 10));
        backNextPanel.add(backButton);
        backNextPanel.add(nextButton);

        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(backNextPanel, BorderLayout.SOUTH);


        currentFrame.getContentPane().add(panel);
    }

    private void setDiscountFrame(ArrayList<JFrame> addSupplierFrame) {
        JFrame currentFrame = addSupplierFrame.get(8);
        JFrame nextFrame = addSupplierFrame.get(5);

        JFrame supplierManagerFrame = this;
        currentFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                currentFrame.setVisible(false);
                nextFrame.setVisible(true);
            }
        });

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        DefaultTableModel discountTableModel = new DefaultTableModel();
        discountTableModel.addColumn("Product Amount");
        discountTableModel.addColumn("Discount (%)");
        JTable discountTable = new JTable(discountTableModel);
        JScrollPane scrollPane = new JScrollPane(discountTable);
        scrollPane.setPreferredSize(new Dimension(400, 200));

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(4, 2, 10, 10));

        JLabel amountLabel = new JLabel("Product Amount:");
        JTextField amountTextField = new JTextField();
        inputPanel.add(amountLabel);
        inputPanel.add(amountTextField);

        JLabel discountLabel = new JLabel("Discount (%):");
        JTextField discountTextField = new JTextField();
        inputPanel.add(discountLabel);
        inputPanel.add(discountTextField);


        JButton removeDiscount = new JButton("Remove Discount");
        inputPanel.add(removeDiscount);

        JButton addDiscount = new JButton("Add Discount");
        inputPanel.add(addDiscount);

        JButton nextButton = new JButton("Stop Adding Discounts");
        inputPanel.add(new JLabel());
        inputPanel.add(nextButton);

        removeDiscount.addActionListener(e -> {
            int selectedRow = discountTable.getSelectedRow();
            int productAmount = Integer.parseInt(discountTable.getValueAt(selectedRow, 0).toString());

            currProduct.removeDiscountPerAmount(productAmount);
//            discounts.remove(productAmount);
            discountTableModel.removeRow(selectedRow);
        });

        addDiscount.addActionListener(e -> {
            int productAmount;
            try {
                productAmount = Integer.parseInt(amountTextField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid product amount, please enter a valid integer", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double productDiscount;
            try {
                productDiscount = Double.parseDouble(discountTextField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid product discount, please enter a valid integer", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            currProduct.addDiscountPerAmount(productAmount, productDiscount);
//            discounts.put(productAmount, productDiscount);
            discountTableModel.addRow(new Object[]{productAmount, productDiscount});
            amountTextField.setText("");
            discountTextField.setText("");
        });


        nextButton.addActionListener(e -> {
//            currProduct.setDiscountPerAmount(discounts);
//            discounts = new HashMap<>();
            discountTableModel.setRowCount(0);
            currProduct = null;
            currentFrame.setVisible(false);
            nextFrame.setVisible(true);
//            nextFrame.setLocationRelativeTo(null);
            //Adjust Size
            nextFrame.pack();
        });


//        JPanel backNextPanel = new JPanel();
//        backNextPanel.setLayout(new GridLayout(1, 2, 10, 10));
//        backNextPanel.add(new JLabel());
//        backNextPanel.add(nextButton);

        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
//        panel.add(backNextPanel, BorderLayout.SOUTH);


        currentFrame.getContentPane().add(panel);
        currentFrame.pack();
    }

    private void setAddAmountDiscountFrame(ArrayList<JFrame> addSupplierFrame) {
        JFrame backFrame = addSupplierFrame.get(5);
        JFrame currentFrame = addSupplierFrame.get(6);
        JFrame nextFrame = addSupplierFrame.get(7);

        JFrame supplierManagerFrame = this;
        currentFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                currentFrame.setVisible(false);
                supplierManagerFrame.setVisible(true);
            }
        });


        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(3, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel amountLabel = new JLabel("Product Amount:");
        JTextField amountTextField = new JTextField();
        inputPanel.add(amountLabel);
        inputPanel.add(amountTextField);

        JLabel discountLabel = new JLabel("Discount (%):");
        JTextField discountTextField = new JTextField();
        inputPanel.add(discountLabel);
        inputPanel.add(discountTextField);

        JButton nextButton = new JButton("Next");
        nextButton.addActionListener(e -> {

            int productAmount;
            try {
                productAmount = Integer.parseInt(amountTextField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid product amount, please enter a valid integer", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double productDiscount;
            try {
                productDiscount = Double.parseDouble(discountTextField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid product discount, please enter a valid integer", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!amountTextField.getText().isEmpty() && !discountTextField.getText().isEmpty())
                amountPair = new Pair<>(productAmount, productDiscount);
            currentFrame.setVisible(false);
            nextFrame.setVisible(true);
            nextFrame.setLocationRelativeTo(null);
            //Adjust Size
            nextFrame.pack();
        });

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            currentFrame.setVisible(false);
            backFrame.setVisible(true);
//            backFrame.setLocationRelativeTo(null);
            backFrame.pack();
            nextFrame.setLocationRelativeTo(null);
            nextFrame.setLocation(nextFrame.getX(), nextFrame.getY() - 30);
        });

        JPanel backNextPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(1, 2, 10, 10));
        inputPanel.add(backButton);
        inputPanel.add(nextButton);
        currentFrame.getContentPane().add(inputPanel);

    }

    private void setAddPriceDiscountFrame(ArrayList<JFrame> addSupplierFrame) {
        JFrame backFrame = addSupplierFrame.get(6);
        JFrame currentFrame = addSupplierFrame.get(7);
        JFrame nextFrame = this;

        currentFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                currentFrame.setVisible(false);
                nextFrame.setVisible(true);
            }
        });

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(3, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel amountLabel = new JLabel("Order Price:");
        JTextField amountTextField = new JTextField();
        inputPanel.add(amountLabel);
        inputPanel.add(amountTextField);

        JLabel discountLabel = new JLabel("Discount (₪):");
        JTextField discountTextField = new JTextField();
        inputPanel.add(discountLabel);
        inputPanel.add(discountTextField);

        JButton nextButton = new JButton("Next");
        nextButton.addActionListener(e -> {

            double productAmount = 0;
            if (!amountTextField.getText().isEmpty()) {
                try {
                    productAmount = Double.parseDouble(amountTextField.getText());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid product amount, please enter a valid integer", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            double productDiscount = 0;
            if (!discountTextField.getText().isEmpty()) {
                try {
                    productDiscount = Double.parseDouble(discountTextField.getText());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid product discount, please enter a valid integer", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            if (!amountTextField.getText().isEmpty() && !discountTextField.getText().isEmpty())
                pricePair = new Pair<>(productAmount, productDiscount);
            ServiceAgreement serviceAgreement;
            serviceAgreement = new ServiceAgreement(paymentMethod, selfSupply, days, items, amountPair, pricePair, supplyMethod, supplyTime);
            Response res = supplierService.addSupplier(name, address, bankAccount, serviceAgreement, serviceContacts);
            if (!res.errorOccurred())
                JOptionPane.showMessageDialog(null, "Supplier added successfully, supplier's id is: " + res.getSupplierId(), "Supplier Added Successfully", JOptionPane.INFORMATION_MESSAGE);
            else
                JOptionPane.showMessageDialog(null, res.getErrorMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            currentFrame.setVisible(false);
            nextFrame.setVisible(true);
//            nextFrame.setLocationRelativeTo(null);
            //Adjust Size
//            nextFrame.pack();
        });

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            currentFrame.setVisible(false);
            backFrame.setVisible(true);
//            backFrame.setLocationRelativeTo(null);
            backFrame.pack();
        });

        JPanel backNextPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(1, 2, 10, 10));
        inputPanel.add(backButton);
        inputPanel.add(nextButton);
        currentFrame.getContentPane().add(inputPanel);
    }

    private void deleteSupplier(JFrame frame) {
        JFrame f = new JFrame("Delete Supplier");
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Container c = f.getContentPane();
        c.setLayout(new BorderLayout());

        JLabel type = new JLabel("Please select the ID of the supplier you want to remove:");
        c.add(type, BorderLayout.NORTH);

        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Name");
        // Add more columns as needed for additional supplier data

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        c.add(scrollPane, BorderLayout.CENTER);

        JButton deleteButton = new JButton("Delete");
        JButton backButton = new JButton("Back"); // Add the back button

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout()); // Set the layout to FlowLayout
        buttonPanel.add(deleteButton);
        buttonPanel.add(backButton); // Add both buttons to the panel

        c.add(buttonPanel, BorderLayout.SOUTH); // Add the button panel to the south side of the layout

        int i = supplierService.getLastSupplierID();
        if (i != -1) {
            for (int j = 1; j <= i; j++) {
                Response res = supplierService.getSupplierNameById(j);
                if (res != null) {
                    if (res.getAnswer()) {
                        Object[] rowData = { j, res.getStringValue() };
                        model.addRow(rowData);
                    }
                }
            }
        }

        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(f, "Please select a supplier from the table.", "No Supplier Selected", JOptionPane.WARNING_MESSAGE);
            } else {
                int selectedID = (int) table.getValueAt(selectedRow, 0);
                String selectedName = (String) table.getValueAt(selectedRow, 1);
                supplierService.removeSupplier(selectedID);
                model.removeRow(selectedRow);
                JOptionPane.showMessageDialog(f, "Supplier with ID " + selectedID + " (" + selectedName + ") has been deleted.", "Supplier Deleted", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        backButton.addActionListener(e -> {
            f.dispose();
            frame.setVisible(true); // Close the current frame
        });

        f.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                f.dispose();
                frame.setVisible(true); // Close the current frame
            }
        });

        f.setSize(400, 300);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }




    private void editSupplier(JFrame prev) {
        JFrame f = new JFrame("Edit Supplier");
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Container c = f.getContentPane();
        c.setLayout(new BorderLayout());

        JLabel type = new JLabel("Please select the supplier you want to edit:");
        c.add(type, BorderLayout.NORTH);

        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Name");
        // Add more columns as needed for additional supplier data

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        c.add(scrollPane, BorderLayout.CENTER);

        JButton back = new JButton("Back");
        JButton editPersonalDetailsButton = new JButton("Edit Supplier Personal Details");
        JButton editAgreementButton = new JButton("Edit Supplier Agreement");

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0;
        gbc.gridy = 0;
        buttonPanel.add(editPersonalDetailsButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(10, 0, 0, 0);
        buttonPanel.add(editAgreementButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.insets = new Insets(10, 0, 0, 0);
        buttonPanel.add(back, gbc);

        c.add(buttonPanel, BorderLayout.SOUTH);

        int i = supplierService.getLastSupplierID();
        if (i != -1) {
            for (int j = 1; j <= i; j++) {
                Response res = supplierService.getSupplierNameById(j);
                if (res != null && res.getAnswer()) {
                    Object[] rowData = {j, res.getStringValue()};
                    model.addRow(rowData);
                }
            }
        }

        editPersonalDetailsButton.addActionListener(e -> {
//            this.setVisible(false);
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(f, "Please select a supplier from the table.", "No Supplier Selected", JOptionPane.WARNING_MESSAGE);
            } else {
                int selectedID = (int) table.getValueAt(selectedRow, 0);
                String selectedName = (String) table.getValueAt(selectedRow, 1);

                // Call the editSupplierPersonalDetails() method with the selected ID
                f.dispose();

                int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to edit personal details of the supplier's with ID " + selectedID + " (" + selectedName + ")", "Confirmation", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION)
                    editSupplierPersonalDetails(selectedID, prev, f, table, selectedRow);
                else
                    f.setVisible(true);


                //JOptionPane.showMessageDialog(f, "Editing supplier's personal details with ID " + selectedID + " (" + selectedName + ")", "Supplier Edit", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        editAgreementButton.addActionListener(e -> {
            this.setVisible(false);
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(f, "Please select a supplier from the table.", "No Supplier Selected", JOptionPane.WARNING_MESSAGE);
            } else {
                int selectedID = (int) table.getValueAt(selectedRow, 0);
                String selectedName = (String) table.getValueAt(selectedRow, 1);

                // Call the editSupplierAgreement() method with the selected ID
                f.dispose();

                int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to edit supplier's agreement with ID " + selectedID + " (" + selectedName + ")", "Confirmation", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION)
                {
                    editSupplierAgreement(selectedID, prev, f);
                }
                else
                    f.setVisible(true);
            }
        });

        back.addActionListener(e -> {
            prev.setVisible(true);
            f.dispose();
        });

        f.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                prev.setVisible(true);
                f.dispose();
            }
        });

        f.setSize(400, 300);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }




    private void editSupplierPersonalDetails(int supplierID, JFrame prevprev, JFrame prev, JTable table, int selectedRow) {
        JFrame f = new JFrame("Edit Supplier Personal Details");
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        f.revalidate();
        f.repaint();
        Container c = f.getContentPane();
        c.setLayout(new GridBagLayout());

        JLabel titleLabel = new JLabel("Edit Supplier Personal Details");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));

        JButton editContactButton = new JButton("Edit Supplier Contact");
        editContactButton.addActionListener(e -> editSupplierContact(supplierID, f));

        JButton editNameButton = new JButton("Edit Supplier Name");
        editNameButton.addActionListener(e -> editSupplierName(supplierID, f, table, selectedRow));

        JButton editAddressButton = new JButton("Edit Supplier Address");
        editAddressButton.addActionListener(e -> editSupplierAddress(supplierID, f));

        JButton editBankAccountButton = new JButton("Edit Supplier Bank Account");
        editBankAccountButton.addActionListener(e -> editSupplierBankAccount(supplierID, f));

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            f.dispose();
            prev.setVisible(true);
//            editSupplier(this); // Go back to the editSupplier function
        });

        f.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                prev.setVisible(true);
                f.dispose();
            }
        });

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(10, 10, 0, 10); // Top padding for titleLabel
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        c.add(titleLabel, constraints);

        constraints.insets = new Insets(10, 10, 10, 10); // Padding for buttons
        constraints.gridwidth = 1;
        constraints.gridy = 1;
        c.add(editContactButton, constraints);

        constraints.gridx = 1;
        c.add(editNameButton, constraints);

        constraints.gridy = 2;
        constraints.gridx = 0;
        c.add(editAddressButton, constraints);

        constraints.gridx = 1;
        c.add(editBankAccountButton, constraints);

        constraints.gridy = 3;
        constraints.gridx = 0;
        constraints.gridwidth = 2;
        c.add(backButton, constraints);

        f.setSize(400, 350); // Increased height to accommodate the "Back" button
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }


    private void editSupplierBankAccount(int supplierID, JFrame prev) {
        JFrame frame = new JFrame("Change Supplier Bank Account");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel label = new JLabel("Enter new bank account for the supplier:");
        JTextField bankAccountFiled = new JTextField();
        setFormatExample(bankAccountFiled, "6-9 digits");

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            String newBankAccount = bankAccountFiled.getText();

            // Validate the new name using regular expression
            if (!checkBankAccountValidation(newBankAccount)) {
                JOptionPane.showMessageDialog(frame, "Invalid bank account. Please enter a bank account containing only digits.", "Invalid bank account", JOptionPane.ERROR_MESSAGE);
                return; // Stop further execution
            }

            Response res = supplierService.changeSupplierBankAccount(supplierID, newBankAccount);
            if (res.errorOccurred()) {
                JOptionPane.showMessageDialog(frame, "Something went wrong. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                String successMessage = "The bank account of the supplier with ID " + supplierID + " has been changed to '" + newBankAccount + "' successfully.";
                JOptionPane.showMessageDialog(frame, successMessage, "Success", JOptionPane.INFORMATION_MESSAGE);
                frame.dispose(); // Close the frame
                editSupplier(this); // Return to the editSupplier function
            }
        });

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            frame.dispose();
            prev.setVisible(true);


//            editSupplierPersonalDetails(supplierID, this); // Go back to the editSupplierPersonalDetails function
        });

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame.dispose();
                prev.setVisible(true);
            }
        });

        bankAccountFiled.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    saveButton.doClick(); // Trigger the save button action
                }
            }
        });

        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(label);
        panel.add(bankAccountFiled);
        panel.add(saveButton);
        panel.add(backButton);

        frame.getContentPane().add(panel);
        frame.setSize(400, 200);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void editSupplierAddress(int supplierID, JFrame prev) {
        JFrame frame = new JFrame("Change Supplier address");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel label = new JLabel("Enter new address for the supplier:");
        JTextField addressFiled = new JTextField();
        setFormatExample(addressFiled, "Beer sheva");

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            String newAddress = addressFiled.getText();

            // Validate the new name using regular expression
            if (!newAddress.matches("^[a-zA-Z]+( [a-zA-Z]+)?$")) {
                JOptionPane.showMessageDialog(frame, "Invalid address. Please enter a adsress containing only letters.", "Invalid address", JOptionPane.ERROR_MESSAGE);
                return; // Stop further execution
            }

            Response res = supplierService.changeAddress(supplierID, newAddress);
            if (res.errorOccurred()) {
                JOptionPane.showMessageDialog(frame, "Something went wrong. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                String successMessage = "The address of the supplier with ID " + supplierID + " has been changed to '" + newAddress + "' successfully.";
                JOptionPane.showMessageDialog(frame, successMessage, "Success", JOptionPane.INFORMATION_MESSAGE);
                frame.dispose(); // Close the frame
            }
        });

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            frame.dispose();
            prev.setVisible(true);

//            editSupplierPersonalDetails(supplierID, this); // Go back to the editSupplierPersonalDetails function
        });

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame.dispose();
                prev.setVisible(true);
            }
        });

        addressFiled.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    saveButton.doClick(); // Trigger the save button action
                }
            }
        });

        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(label);
        panel.add(addressFiled);
        panel.add(saveButton);
        panel.add(backButton);

        frame.getContentPane().add(panel);
        frame.setSize(400, 200);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void editSupplierName(int supplierID, JFrame prev, JTable table, int selectedRow) {
        JFrame frame = new JFrame("Change Supplier Name");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel label = new JLabel("Enter new name for the supplier:");
        JTextField nameFiled = new JTextField();
        setFormatExample(nameFiled, "Israel Israeli");

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            String newName = nameFiled.getText();

            // Validate the new name using regular expression
            if (!newName.matches("^[a-zA-Z]+( [a-zA-Z]+)?$")) {
                JOptionPane.showMessageDialog(frame, "Invalid name. Please enter a name containing only letters.", "Invalid Name", JOptionPane.ERROR_MESSAGE);
                return; // Stop further execution
            }

            Response res = supplierService.changeSupplierName(supplierID, newName);
            if (res.errorOccurred()) {
                JOptionPane.showMessageDialog(frame, "Something went wrong. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                table.setValueAt(newName,selectedRow, 1);
                String successMessage = "The name of the supplier with ID " + supplierID + " has been changed to '" + newName + "' successfully.";
                JOptionPane.showMessageDialog(frame, successMessage, "Success", JOptionPane.INFORMATION_MESSAGE);
                frame.dispose(); // Close the frame
            }
        });

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            frame.dispose();
            prev.setVisible(true);

//            editSupplierPersonalDetails(supplierID, this); // Go back to the editSupplierPersonalDetails function
        });

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame.dispose();
                prev.setVisible(true);
            }
        });

        nameFiled.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    saveButton.doClick(); // Trigger the save button action
                }
            }
        });

        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(label);
        panel.add(nameFiled);
        panel.add(saveButton);
        panel.add(backButton);

        frame.getContentPane().add(panel);
        frame.setSize(400, 200);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }



    private void editSupplierContact(int supplierID, JFrame prev) {
        serviceContacts = new ArrayList<>();
        JFrame frame = new JFrame("Change Supplier Contacts");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        DefaultTableModel contactTableModel = new DefaultTableModel();
        contactTableModel.addColumn("Name");
        contactTableModel.addColumn("Email");
        contactTableModel.addColumn("Phone Number");
        JTable contactTable = new JTable(contactTableModel);
        HashMap<String, String> contactInfo = supplierService.getContactsFromSupplier(supplierID);
        for (Map.Entry<String, String> entry : contactInfo.entrySet()) {
            String key = entry.getKey();
            String name = key.split("\\$")[0];
            String email = key.split("\\$")[1];
            String phoneNumber = entry.getValue();

            contactTableModel.addRow(new Object[]{name, email, phoneNumber});
            ServiceContact contact = new ServiceContact(name, email, phoneNumber);
            serviceContacts.add(contact);
        }
        JScrollPane scrollPane = new JScrollPane(contactTable);
        scrollPane.setPreferredSize(new Dimension(400, 200));

        // Right-click menu for contacts
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem removeMenuItem = new JMenuItem("Remove Contact");
        JMenuItem editPhoneMenuItem = new JMenuItem("Edit Phone Number");
        popupMenu.add(removeMenuItem);
        popupMenu.add(editPhoneMenuItem);

        removeMenuItem.addActionListener(e -> {
            int selectedRow = contactTable.getSelectedRow();
            if (selectedRow != -1) {
                String currentPhone = (String) contactTableModel.getValueAt(selectedRow, 2);
                int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to remove this contact?", "Remove Contact", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    contactTableModel.removeRow(selectedRow);
                    serviceContacts.remove(selectedRow);
                    supplierService.removeSupplierContact(supplierID, currentPhone);
                }
            }
        });

        editPhoneMenuItem.addActionListener(e -> {
            int selectedRow = contactTable.getSelectedRow();
            if (selectedRow != -1) {
//              String cNmae = (String) contactTableModel.getValueAt(selectedRow, 0);
                String currentPhone = (String) contactTableModel.getValueAt(selectedRow, 2);
                String currentEmail = (String) contactTableModel.getValueAt(selectedRow, 1);
                String newPhone = JOptionPane.showInputDialog(frame, "Enter new phone number:", currentPhone);
                if (newPhone != null) {
                    contactTableModel.setValueAt(newPhone, selectedRow, 2);
                    supplierService.editSupplierContacts(supplierID, currentEmail, "", newPhone, currentPhone);
                    System.out.println(currentPhone);
                    System.out.println(newPhone);
                }
            }
        });

        contactTable.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    int row = contactTable.rowAtPoint(e.getPoint());
                    contactTable.getSelectionModel().setSelectionInterval(row, row);
                    popupMenu.show(contactTable, e.getX(), e.getY());
                }
            }
        });

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(4, 2, 10, 10));

        JLabel nameLabel = new JLabel("Contact Name:");
        JTextField nameTextField = new JTextField();

        JLabel emailLabel = new JLabel("Contact Email:");
        JTextField emailTextField = new JTextField();
        setFormatExample(emailTextField, "____@___.___");

        JLabel phoneNumberLabel = new JLabel("Contact Phone Number:");
        JTextField phoneNumberTextField = new JTextField();
        setFormatExample(phoneNumberTextField, "05_-_______");

        JButton addAnotherContact = new JButton("Add Contact");

        inputPanel.add(nameLabel);
        inputPanel.add(nameTextField);
        inputPanel.add(emailLabel);
        inputPanel.add(emailTextField);
        inputPanel.add(phoneNumberLabel);
        inputPanel.add(phoneNumberTextField);
        inputPanel.add(new JLabel());
        inputPanel.add(addAnotherContact);


        addAnotherContact.addActionListener(e -> {
            if (nameTextField.getText().length() == 0) {
                JOptionPane.showMessageDialog(null, "You must enter name, please try again", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!serviceContact.isValidEmail(emailTextField.getText())) {
                JOptionPane.showMessageDialog(null, "Not a valid email, please try again", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!serviceContact.validatePhoneNumber(phoneNumberTextField.getText())) {
                JOptionPane.showMessageDialog(null, "Not a valid phone number, please try again", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            for (int row = 0; row < contactTableModel.getRowCount(); row++) {
                Object value = contactTableModel.getValueAt(row, 2);
                if (value != null && value.toString().equals(phoneNumberTextField.getText())) {
                    JOptionPane.showMessageDialog(null, "You already added this phone number, please try again", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            ServiceContact newContact = new ServiceContact(nameTextField.getText(), emailTextField.getText(), phoneNumberTextField.getText());
            System.out.println(nameTextField.getText());
            serviceContacts.add(newContact);
            System.out.println(newContact.getName());
            contactTableModel.addRow(new Object[]{newContact.getName(), newContact.getEmail(), newContact.getPhoneNumber()});
            supplierService.addContactsTOSupplier(supplierID, newContact.getName(), newContact.getEmail(), newContact.getPhoneNumber());
        });


        JButton nextButton = new JButton("Next");
        nextButton.addActionListener(e -> {
            if (serviceContacts.size() < 1) {
                JOptionPane.showMessageDialog(null, "You must add at least one contact", "Error", JOptionPane.ERROR_MESSAGE);
            }
            // Add your code for handling the next steps here
            frame.dispose(); // Close the current frame
            prev.setVisible(true);
        });

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            frame.dispose(); // Close the current frame
            prev.setVisible(true);

        });

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame.dispose();
                prev.setVisible(true);
            }
        });

        JPanel backNextPanel = new JPanel();
        backNextPanel.setLayout(new GridLayout(1, 2, 10, 10));
        backNextPanel.add(backButton);
        backNextPanel.add(nextButton);

        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(backNextPanel, BorderLayout.SOUTH);

        // Create a new JFrame and set the panel as its content
        frame.getContentPane().add(panel);
        frame.setSize(500, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }


    private void editSupplierAgreement(int supplierID, JFrame prevprev, JFrame prev) {
        JFrame frame = new JFrame("Edit Supplier Agreement");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);


        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        String[] options = {"Supplier's payment method + delivery method + delivery days", "Supplier's items"};
        JComboBox<String> comboBox = new JComboBox<>(options);
        panel.add(comboBox, BorderLayout.LINE_END);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1,2,10,10));
        panel.add(buttonPanel, BorderLayout.SOUTH);

        JButton backButton = new JButton("Back");
//        backButton.setFont(backButton.getFont().deriveFont(15f)); // Increase button font size
        buttonPanel.add(backButton);

        JButton editButton = new JButton("Edit");
//        editButton.setFont(editButton.getFont().deriveFont(15f)); // Increase button font size
        buttonPanel.add(editButton);



        editButton.addActionListener(e -> {
            int action = comboBox.getSelectedIndex();
            switch (action) {
                case 0 -> editPaymentMethodAndDeliveryMethodAndDeliveryDays(supplierID, prev, frame);
                case 1 -> editItems(supplierID, prev, frame);
            }
            frame.dispose();
        });

        backButton.addActionListener(e -> {
            frame.dispose();
            prev.setVisible(true);
        });

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame.dispose();
                prev.setVisible(true);
            }
        });

        frame.getContentPane().add(panel);
        frame.setVisible(true);
        frame.pack();
    }

//    private void editOrderDiscount(int supplierID, JFrame frame) {
//
//    }

    private void editItems(int supplierID, JFrame prevprev, JFrame prev) {
        JFrame frame = new JFrame("Edit Items");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        HashMap<Integer, SupplierProductService> items = new HashMap<>();

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        DefaultTableModel productsTableModel = new DefaultTableModel();
        productsTableModel.addColumn("Name");
        productsTableModel.addColumn("ID");
        productsTableModel.addColumn("Catalog Number");
        productsTableModel.addColumn("Price");
        productsTableModel.addColumn("Amount");
        productsTableModel.addColumn("Manufacturer");
        productsTableModel.addColumn("Expiration Days");
        productsTableModel.addColumn("Weight");
        JTable productsTable = new JTable(productsTableModel);
        JScrollPane scrollPane = new JScrollPane(productsTable);
        scrollPane.setPreferredSize(new Dimension(500, 200));

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(9, 2, 10, 10));

        JLabel nameLabel = new JLabel("Product Name:");
        JTextField nameTextField = new JTextField();
        inputPanel.add(nameLabel);
        inputPanel.add(nameTextField);

        JLabel idLabel = new JLabel("Product ID:");
        JTextField idTextField = new JTextField();
        inputPanel.add(idLabel);
        inputPanel.add(idTextField);

        JLabel catalogNumberLabel = new JLabel("Product Catalog Number:");
        JTextField catalogNumberTextField = new JTextField();
        inputPanel.add(catalogNumberLabel);
        inputPanel.add(catalogNumberTextField);

        JLabel PriceLabel = new JLabel("Product Price:");
        JTextField PriceTextField = new JTextField();
        inputPanel.add(PriceLabel);
        inputPanel.add(PriceTextField);

        JLabel amountLabel = new JLabel("Product Amount:");
        JTextField amountTextField = new JTextField();
        inputPanel.add(amountLabel);
        inputPanel.add(amountTextField);

        JLabel manufacturerLabel = new JLabel("Product Manufacturer:");
        JTextField manufacturerTextField = new JTextField();
        inputPanel.add(manufacturerLabel);
        inputPanel.add(manufacturerTextField);

        JLabel expirationDaysLabel = new JLabel("Product Expiration Days:");
        JTextField expirationDaysTextField = new JTextField();
        inputPanel.add(expirationDaysLabel);
        inputPanel.add(expirationDaysTextField);

        JLabel weightLabel = new JLabel("Product Weight:");
        JTextField weightTextField = new JTextField();
        inputPanel.add(weightLabel);
        inputPanel.add(weightTextField);

        JButton addProduct = new JButton("Add Product");
        JButton removeProduct = new JButton("Remove Product");

        inputPanel.add(removeProduct);
        inputPanel.add(addProduct);

        productsTableModel.setRowCount(0);
        HashMap<Integer, SupplierProduct> supplierProducts = supplierService.getAllSupplierProductsByID(supplierID);
        for (SupplierProduct supplierProduct : supplierProducts.values())
        {
            items.put(supplierProduct.getProductID(), new SupplierProductService(supplierProduct.getName(), supplierProduct.getProductID(), supplierProduct.getCatalogID(), supplierProduct.getPrice(), supplierProduct.getAmount(), supplierProduct.getDiscountPerAmount(), supplierProduct.getManufacturer(), supplierProduct.getExpirationDays(), supplierProduct.getWeight()));
            productsTableModel.addRow(new Object[]{supplierProduct.getName(), supplierProduct.getProductID(), supplierProduct.getCatalogID(), supplierProduct.getPrice(), supplierProduct.getAmount(), supplierProduct.getManufacturer(), supplierProduct.getExpirationDays(), supplierProduct.getWeight()});
        }

        removeProduct.addActionListener(e -> {
            int selectedRow = productsTable.getSelectedRow();
            if (selectedRow != -1)
            {
                items.remove(Integer.parseInt(String.valueOf(productsTableModel.getValueAt(selectedRow, 1))));
                productsTableModel.removeRow(selectedRow);
            }

        });

        addProduct.addActionListener(e -> {
            int productID;
            try {
                productID = Integer.parseInt(idTextField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid product ID, please enter a valid integer", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int catalogNumber;
            try {
                catalogNumber = Integer.parseInt(catalogNumberTextField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid catalog number, please enter a valid integer", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double price;
            try {
                price = Double.parseDouble(PriceTextField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid product price, please enter a valid number", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int amount;
            try {
                amount = Integer.parseInt(amountTextField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid product amount, please enter a valid integer", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int expirationDays;
            try {
                expirationDays = Integer.parseInt(expirationDaysTextField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid expiration days, please enter a valid integer", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double weight;
            try {
                weight = Double.parseDouble(weightTextField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid product weight, please enter a valid number", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String productName = nameTextField.getText();
            String manufacturer = manufacturerTextField.getText();

            if (productName.length() == 0) {
                JOptionPane.showMessageDialog(null, "You must enter product name, please try again", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (manufacturer.length() == 0) {
                JOptionPane.showMessageDialog(null, "You must enter product name, please try again", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (items.containsKey(productID)) {
                JOptionPane.showMessageDialog(null, "This product ID already exists on this supplier, please try again", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            SupplierProductService newSupplierProduct = new SupplierProductService(productName, productID, catalogNumber, price, amount, new HashMap<>(), manufacturer, expirationDays, weight);
            items.put(productID, newSupplierProduct);
            productsTableModel.addRow(new Object[]{productName, productID, catalogNumber, price, amount, manufacturer, expirationDays, weight});
            nameTextField.setText("");
            idTextField.setText("");
            catalogNumberTextField.setText("");
            PriceTextField.setText("");
            amountTextField.setText("");
            manufacturerTextField.setText("");
            expirationDaysTextField.setText("");
            weightTextField.setText("");
//            discounts.clear();
        });

        productsTable.addMouseListener(new MouseAdapter() {
            JFrame nextFrame = frame;
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    JPopupMenu popupMenu = new JPopupMenu();
                    JMenuItem addDiscountMenuItem = new JMenuItem("Add Discount");
                    addDiscountMenuItem.addActionListener(ee -> {
                        // Handle the action when "Add Discount" is clicked
                        // discount frame
                        int selectedRow = productsTable.rowAtPoint(e.getPoint());
                        if (selectedRow >= 0)
                        {
                            SupplierProductService supplierProductService = items.get(Integer.parseInt(productsTable.getValueAt(selectedRow, 1).toString()));
                            setDiscountFrame(nextFrame, supplierProductService, supplierID);
                            nextFrame.setVisible(false);
                            // Adjust Size
//                            editItemsFrame.get(8).pack();
                        }
                    });
                    popupMenu.add(addDiscountMenuItem);
                    popupMenu.show(productsTable, e.getX(), e.getY());
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    JPopupMenu popupMenu = new JPopupMenu();
                    JMenuItem addDiscountMenuItem = new JMenuItem("Add Discount");
                    addDiscountMenuItem.addActionListener(ee -> {
                        // Handle the action when "Add Discount" is clicked
                        int selectedRow = productsTable.rowAtPoint(e.getPoint());
                        if (selectedRow >= 0)
                        {
                            SupplierProductService supplierProductService = items.get(Integer.parseInt(productsTable.getValueAt(selectedRow, 1).toString()));
                            setDiscountFrame(nextFrame, supplierProductService, supplierID);
                            nextFrame.setVisible(false);
                            // Adjust Size
//                            editItemsFrame.get(5).pack();
                        }

                        // discount frame

                    });
                    popupMenu.add(addDiscountMenuItem);

                    popupMenu.show(productsTable, e.getX(), e.getY());
                }
            }
        });

        JButton nextButton = new JButton("Update");
        nextButton.addActionListener(e -> {
            Response res = supplierService.updateSupplierProducts(supplierID, items);
            if (!res.errorOccurred())
                JOptionPane.showMessageDialog(null, "Supplier updated successfully, supplier's id is: " + res.getSupplierId(), "Supplier Added Successfully", JOptionPane.INFORMATION_MESSAGE);
            else {
                JOptionPane.showMessageDialog(null, res.getErrorMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            frame.setVisible(false);
            prev.setVisible(true);
//            nextFrame.setLocationRelativeTo(null);
            //Adjust Size
            prev.pack();
        });

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            frame.setVisible(false);
            prev.setVisible(true);
            prev.pack();
        });

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame.setVisible(false);
                prev.setVisible(true);
                prev.pack();
            }
        });

        JPanel backNextPanel = new JPanel();
        backNextPanel.setLayout(new GridLayout(1, 2, 10, 10));
        backNextPanel.add(backButton);
        backNextPanel.add(nextButton);

        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(backNextPanel, BorderLayout.SOUTH);


        frame.getContentPane().add(panel);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setLocation(frame.getX(), frame.getY() - 35);
    }

//    public void createProduct() {
//        JFrame frame = new JFrame("Create Product");
//        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        frame.setSize(400, 400);
//        frame.setLocationRelativeTo(null);
//
//        JPanel panel = new JPanel();
//        panel.setLayout(new GridLayout(10, 2, 10, 10));
//
//        JLabel nameLabel = new JLabel("Product's Name:");
//        panel.add(nameLabel);
//
//        JTextField nameTextField = new JTextField();
//        panel.add(nameTextField);
//
//        JLabel productIdLabel = new JLabel("Product's ID:");
//        panel.add(productIdLabel);
//
//        JTextField productIdTextField = new JTextField();
//        panel.add(productIdTextField);
//
//        JLabel priceLabel = new JLabel("Product's Price:");
//        panel.add(priceLabel);
//
//        JTextField priceTextField = new JTextField();
//        panel.add(priceTextField);
//
//        JLabel catalogNumberLabel = new JLabel("Product's Catalog Number:");
//        panel.add(catalogNumberLabel);
//
//        JTextField catalogNumberTextField = new JTextField();
//        panel.add(catalogNumberTextField);
//
//        JLabel amountLabel = new JLabel("Product's Amount:");
//        panel.add(amountLabel);
//
//        JTextField amountTextField = new JTextField();
//        panel.add(amountTextField);
//
//        JLabel manufacturerLabel = new JLabel("Product's Manufacturer:");
//        panel.add(manufacturerLabel);
//
//        JTextField manufacturerTextField = new JTextField();
//        panel.add(manufacturerTextField);
//
//        JLabel expirationDaysLabel = new JLabel("Product's Expiration Days:");
//        panel.add(expirationDaysLabel);
//
//        JTextField expirationDaysTextField = new JTextField();
//        panel.add(expirationDaysTextField);
//
//        JLabel weightLabel = new JLabel("Product's Weight:");
//        panel.add(weightLabel);
//
//        JTextField weightTextField = new JTextField();
//        panel.add(weightTextField);
//
//        JLabel discountsLabel = new JLabel("Discounts (amount:discount %,amount:discount %,...):");
//        panel.add(discountsLabel);
//
//        JTextField discountsTextField = new JTextField();
//        panel.add(discountsTextField);
//
//        JButton submitButton = new JButton("Submit");
//        submitButton.addActionListener(e -> {
//            String name = nameTextField.getText();
//            int productId = Integer.parseInt(productIdTextField.getText());
//            double price = Double.parseDouble(priceTextField.getText());
//            int catalogNumber = Integer.parseInt(catalogNumberTextField.getText());
//            int amount = Integer.parseInt(amountTextField.getText());
//            String manufacturer = manufacturerTextField.getText();
//            int expirationDays = Integer.parseInt(expirationDaysTextField.getText());
//            double weight = Double.parseDouble(weightTextField.getText());
//            String discountsText = discountsTextField.getText();
//            HashMap<Integer, Double> discounts = new HashMap<>();
//
//            String[] arr = discountsText.split("\\s*,\\s*");
//            for (String s1 : arr) {
//                String[] val = s1.split(":");
//                int key = Integer.parseInt(val[0]);
//                double value = Double.parseDouble(val[1]);
//                discounts.put(key, value);
//            }
//
//             product = new SupplierProductService(name, productId, catalogNumber, price, amount, discounts, manufacturer, expirationDays, weight);
//
//
//            // Call the supplierService.createProduct method with the collected data
//            // Response res = supplierService.createProduct(product);
//            // Handle the response accordingly
//            frame.dispose();
//
//        });
//
//        panel.add(submitButton);
//
//        frame.add(panel);
//        frame.setVisible(true);
//    }
//
//
//    public void editItem(int supplierID) {
//        JFrame frame = new JFrame("Edit Item");
//        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        frame.setSize(400, 200);
//        frame.setLocationRelativeTo(null);
//
//        JPanel panel = new JPanel();
//        panel.setLayout(new GridLayout(3, 1, 10, 10));
//
//        JLabel productIdLabel = new JLabel("Please enter the product ID you want to edit:");
//        panel.add(productIdLabel);
//
//        JTextField productIdTextField = new JTextField();
//        panel.add(productIdTextField);
//
//        JButton editButton = new JButton("Edit");
//        editButton.addActionListener(e -> {
//            String productIdInput = productIdTextField.getText();
//            if (productIdInput != null && !productIdInput.isEmpty()) {
//                int productId = Integer.parseInt(productIdInput);
//                String[] options = {"Edit item's catalog number", "Delete a discount for this item", "Add a discount for this item"};
//                int action = JOptionPane.showOptionDialog(frame, "Please choose an action:", "Edit Item", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
//                switch (action) {
//                    case 0 -> editItemCatalogNumber(supplierID, productId);
//                    case 1 -> editRemoveDiscount(supplierID, productId);
//                    case 2 -> editAddDiscount(supplierID, productId);
//                }
//            }
//        });
//        panel.add(editButton);
//
//        frame.add(panel);
//        frame.setVisible(true);
//    }

    private void editAddDiscount(int supplierID, int productId) {
        JFrame frame = new JFrame("Edit Add Discount");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(300, 150);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 2, 10, 10));

        JLabel discountLabel = new JLabel("Discount (amount:discount %):");
        panel.add(discountLabel);

        JTextField discountTextField = new JTextField();
        panel.add(discountTextField);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            String s = discountTextField.getText();
            String[] val = s.split(":");
            int amount = Integer.parseInt(val[0]);
            double discount = Double.parseDouble(val[1]);
            Response res = supplierService.addDiscounts(supplierID, productId, amount, discount);
            if (res.errorOccurred())
                JOptionPane.showMessageDialog(frame, res.getErrorMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            else
                JOptionPane.showMessageDialog(frame, "Discount added successfully");

            frame.dispose();
        });

        panel.add(submitButton);

        frame.add(panel);
        frame.setVisible(true);
    }


    private void editRemoveDiscount(int supplierID, int productId) {
        JFrame frame = new JFrame("Edit Remove Discount");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(300, 150);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 2, 10, 10));

        JLabel discountLabel = new JLabel("Discount (amount:discount %):");
        panel.add(discountLabel);

        JTextField discountTextField = new JTextField();
        panel.add(discountTextField);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            String s = discountTextField.getText();
            String[] val = s.split(":");
            int amount = Integer.parseInt(val[0]);
            double discount = Double.parseDouble(val[1]);
            Response res = supplierService.removeDiscounts(supplierID, productId, amount, discount);
            if (res.errorOccurred())
                JOptionPane.showMessageDialog(frame, res.getErrorMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            else
                JOptionPane.showMessageDialog(frame, "Discount deleted successfully");

            frame.dispose();
        });

        panel.add(submitButton);

        frame.add(panel);
        frame.setVisible(true);
    }


    private void editItemCatalogNumber(int supplierId, int productId) {
        JFrame frame = new JFrame("Edit Catalog Number");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(300, 150);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 2, 10, 10));

        JLabel catalogNumberLabel = new JLabel("New Catalog Number:");
        panel.add(catalogNumberLabel);

        JTextField catalogNumberTextField = new JTextField();
        panel.add(catalogNumberTextField);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            int newCatalogNumber = Integer.parseInt(catalogNumberTextField.getText());
            Response res = supplierService.editItemCatalogdNumber(supplierId, productId, newCatalogNumber);
            if (res.errorOccurred())
                JOptionPane.showMessageDialog(frame, res.getErrorMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            else
                JOptionPane.showMessageDialog(frame, "Catalog number updated successfully");

            frame.dispose();
        });

        panel.add(submitButton);

        frame.add(panel);
        frame.setVisible(true);
    }



    private void editPaymentMethodAndDeliveryMethodAndDeliveryDays(int id, JFrame prevprev, JFrame prev) {
        JFrame frame = new JFrame("Edit Supplier Agreement");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.pack();

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 2, 10, 10));

        JLabel paymentMethodLabel = new JLabel("Payment Method:");
        panel.add(paymentMethodLabel);

        String[] paymentMethods = {"Cash", "TransitToAccount", "Credit", "net 30 EOM", "net 60 EOM"};
        JComboBox<String> paymentMethodComboBox = new JComboBox<>(paymentMethods);
        panel.add(paymentMethodComboBox);

        JLabel supplyMethodLabel = new JLabel("Supply Method:");
        panel.add(supplyMethodLabel);

        JRadioButton byDaysRadioButton = new JRadioButton("By Days");
        JRadioButton byOrderRadioButton = new JRadioButton("By Order");
        JRadioButton superLeeRadioButton = new JRadioButton("By Super-Lee");

        ButtonGroup supplyMethodGroup = new ButtonGroup();
        supplyMethodGroup.add(byDaysRadioButton);
        supplyMethodGroup.add(byOrderRadioButton);
        supplyMethodGroup.add(superLeeRadioButton);

        JPanel supplyMethodPanel = new JPanel();
        supplyMethodPanel.setLayout(new GridLayout(1, 3, 10, 10));
        supplyMethodPanel.add(byDaysRadioButton);
        supplyMethodPanel.add(byOrderRadioButton);
        supplyMethodPanel.add(superLeeRadioButton);
        panel.add(supplyMethodPanel);

        JLabel daysLabel = new JLabel("Supplying Days:");
        panel.add(daysLabel);

        JPanel daysPanel = new JPanel();
        daysPanel.setLayout(new GridLayout(2, 4, 10, 10));

        JCheckBox mondayCheckBox = new JCheckBox("Monday");
        daysPanel.add(mondayCheckBox);
        JCheckBox tuesdayCheckBox = new JCheckBox("Tuesday");
        daysPanel.add(tuesdayCheckBox);
        JCheckBox wednesdayCheckBox = new JCheckBox("Wednesday");
        daysPanel.add(wednesdayCheckBox);
        JCheckBox thursdayCheckBox = new JCheckBox("Thursday");
        daysPanel.add(thursdayCheckBox);
        JCheckBox fridayCheckBox = new JCheckBox("Friday");
        daysPanel.add(fridayCheckBox);
        JCheckBox saturdayCheckBox = new JCheckBox("Saturday");
        daysPanel.add(saturdayCheckBox);
        JCheckBox sundayCheckBox = new JCheckBox("Sunday");
        daysPanel.add(sundayCheckBox);

        panel.add(daysPanel);

        JLabel supplyTimeLabel = new JLabel("Supply Time:");
        panel.add(supplyTimeLabel);

        JTextField supplyTimeTextField = new JTextField();
        panel.add(supplyTimeTextField);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            String paymentMethod = (String) paymentMethodComboBox.getSelectedItem();
            boolean selfSupply;
            String supplyMethod;
            int supplyTime;
            ArrayList<DayOfWeek> days = new ArrayList<>();

            if (byDaysRadioButton.isSelected()) {
                selfSupply = true;
                supplyMethod = "FixedDay";
                supplyTime = -1;

                if (mondayCheckBox.isSelected()) {
                    days.add(DayOfWeek.MONDAY);
                }
                if (tuesdayCheckBox.isSelected()) {
                    days.add(DayOfWeek.TUESDAY);
                }
                if (wednesdayCheckBox.isSelected()) {
                    days.add(DayOfWeek.WEDNESDAY);
                }
                if (thursdayCheckBox.isSelected()) {
                    days.add(DayOfWeek.THURSDAY);
                }
                if (fridayCheckBox.isSelected()) {
                    days.add(DayOfWeek.FRIDAY);
                }
                if (saturdayCheckBox.isSelected()) {
                    days.add(DayOfWeek.SATURDAY);
                }
                if (sundayCheckBox.isSelected()) {
                    days.add(DayOfWeek.SUNDAY);
                }
            } else if (byOrderRadioButton.isSelected()) {
                selfSupply = true;
                supplyMethod = "DaysAmount";
                supplyTime = Integer.parseInt(supplyTimeTextField.getText());
            } else {
                selfSupply = false;
                supplyMethod = "SuperLee";
                supplyTime = Integer.parseInt(supplyTimeTextField.getText());
            }

            // Call the supplierService.editPaymentMethodAndDeliveryMethodAndDeliveryDays method with the collected data
            Response res = supplierService.editPaymentMethodAndDeliveryMethodAndDeliveryDays(id, selfSupply, paymentMethod, days, supplyMethod, supplyTime);
            if (!res.errorOccurred()) {
                JOptionPane.showMessageDialog(frame, "The supplier's delivery term changed successfully for Supplier with id: " + id);
            } else {
                JOptionPane.showMessageDialog(frame, res.getErrorMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }

            frame.dispose();
            prevprev.setVisible(true);
            prev.dispose();
        });

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            frame.dispose();
            prev.setVisible(true);
        });

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame.dispose();
                prev.setVisible(true);
            }
        });

        panel.add(backButton);
        panel.add(submitButton);


        frame.add(panel);
        frame.setVisible(true);
        frame.pack();
    }





    private void printSuppliersGui() {
        supplierService.printSuppliersGui();
    }

    private void supplierOrderHistory(JFrame frame) {
        JFrame f = new JFrame("Order History");
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Container c = f.getContentPane();
        c.setLayout(new BorderLayout());

        JLabel type = new JLabel("Please select the ID of the supplier you want to show his order history:");
        c.add(type, BorderLayout.NORTH);

        DefaultTableModel model = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Set all cells to be not editable
            }
        };
        model.addColumn("ID");
        model.addColumn("Name");
        // Add more columns as needed for additional supplier data

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        c.add(scrollPane, BorderLayout.CENTER);

        JButton showOrders = new JButton("Show Orders");
        JButton backButton = new JButton("Back"); // Add the back button

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout()); // Set the layout to FlowLayout
        buttonPanel.add(showOrders);
        buttonPanel.add(backButton); // Add both buttons to the panel

        c.add(buttonPanel, BorderLayout.SOUTH); // Add the button panel to the south side of the layout

        int i = supplierService.getLastSupplierID();
        if (i != -1) {
            for (int j = 1; j <= i; j++) {
                Response res = supplierService.getSupplierNameById(j);
                if (res != null) {
                    if (res.getAnswer()) {
                        Object[] rowData = { j, res.getStringValue() };
                        model.addRow(rowData);
                    }
                }
            }
        }

        showOrders.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(f, "Please select a supplier from the table.", "No Supplier Selected", JOptionPane.WARNING_MESSAGE);
            } else {

                int selectedID = Integer.parseInt(String.valueOf(table.getValueAt(selectedRow, 0)));
                String supplierName = String.valueOf(table.getValueAt(selectedRow, 1));
                JFrame orderHistoryUI = new JFrame("Supplier " + selectedID + " Order History");
                f.setVisible(false);
                printOrdersHistory(orderHistoryUI, selectedID, supplierName, f);
                orderHistoryUI.pack();

            }
        });

        backButton.addActionListener(e -> {
            f.dispose();
            frame.setVisible(true); // Close the current frame
        });

        f.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                f.dispose();
                frame.setVisible(true);
            }
        });

        f.setSize(400, 300);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }

    private void printOrdersHistory(JFrame orderHistoryUI, int supplierID, String supplierName, JFrame beforeFrame)
    {

        JPanel orderHistoryPanel = new JPanel(new GridLayout(1, 1, 10,10));
        orderHistoryPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

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



        HashMap<Integer, Order> orders = supplierService.getOrdersFromSupplier(supplierID);
        if(orders == null || orders.size() == 0)
        {
            JOptionPane.showMessageDialog(null, "There is not orders for this supplier", "Error", JOptionPane.ERROR_MESSAGE);
            orderHistoryUI.dispose();
            beforeFrame.setVisible(true);
        }
        else
        {
            orderHistoryUI.setVisible(true);
            beforeFrame.setVisible(false);
            for(Order order : orders.values())
                ordersTableModel.addRow(new Object[] { order.getOrderID(), order.getSupplierId(), order.getSupplierName(), order.getContactPhoneNumber(), order.getCreationDate(), order.getDeliveryDate(), order.isCollected(), order.getTotalPriceBeforeDiscount(), order.getTotalPriceAfterDiscount() });
            ordersTableModel.fireTableDataChanged();
        }


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
                            HashMap<Integer, SupplierProduct> supplierProducts = supplierService.getAllSupplierProductsByID(supplierID);
                            Order order = supplierService.getOrderByID(orderID);
                            ArrayList<SupplierProduct> orderProducts = order.getItemsInOrder();
                            for (SupplierProduct supplierProduct : supplierProducts.values())
                                chosenProductsTableModel.addRow(new Object[]{supplierProduct.getName(), supplierProduct.getProductID(), supplierProduct.getCatalogID(), supplierProduct.getPrice(), supplierProduct.getAmount(), supplierProduct.getManufacturer(), supplierProduct.getExpirationDays(), supplierProduct.getWeight(), orderProducts.get(orderProducts.indexOf(supplierProduct)).getAmount()});
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
                            Order order = supplierService.getOrderByID(orderID);
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
                beforeFrame.setVisible(true);
            }
        });

        orderHistoryUI.setSize(800, 600);
        orderHistoryUI.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        orderHistoryUI.setLocationRelativeTo(null);
//        orderHistoryUI.setVisible(true);
        orderHistoryUI.pack();

    }
    private void setDiscountFrame(JFrame nextFrame, SupplierProductService supplierProduct, int supplierID) {

        JFrame currentFrame = new JFrame("Add Product Discount");
        currentFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                currentFrame.setVisible(false);
                nextFrame.setVisible(true);
            }
        });

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        DefaultTableModel discountTableModel = new DefaultTableModel();
        discountTableModel.setRowCount(0);
        discountTableModel.addColumn("Product Amount");
        discountTableModel.addColumn("Discount (%)");
        JTable discountTable = new JTable(discountTableModel);
        JScrollPane scrollPane = new JScrollPane(discountTable);
        scrollPane.setPreferredSize(new Dimension(400, 200));

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(4, 2, 10, 10));

        JLabel amountLabel = new JLabel("Product Amount:");
        JTextField amountTextField = new JTextField();
        inputPanel.add(amountLabel);
        inputPanel.add(amountTextField);

        JLabel discountLabel = new JLabel("Discount (%):");
        JTextField discountTextField = new JTextField();
        inputPanel.add(discountLabel);
        inputPanel.add(discountTextField);


        JButton removeDiscount = new JButton("Remove Discount");
        inputPanel.add(removeDiscount);

        JButton addDiscount = new JButton("Add Discount");
        inputPanel.add(addDiscount);

        JButton nextButton = new JButton("Stop Adding Discounts");
        inputPanel.add(new JLabel());
        inputPanel.add(nextButton);


        HashMap<Integer, Double> discounts = supplierService.getProductDiscountByID(supplierID, supplierProduct.getProductId());
        for(Map.Entry<Integer, Double> entry : discounts.entrySet())
            discountTableModel.addRow(new Object[] { entry.getKey(), entry.getValue() });
        removeDiscount.addActionListener(e -> {
            int selectedRow = discountTable.getSelectedRow();
            int productAmount = Integer.parseInt(discountTable.getValueAt(selectedRow, 0).toString());

            supplierProduct.removeDiscountPerAmount(productAmount);
//            discounts.remove(productAmount);
            discountTableModel.removeRow(selectedRow);
        });

        addDiscount.addActionListener(e -> {
            int productAmount;
            try {
                productAmount = Integer.parseInt(amountTextField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid product amount, please enter a valid integer", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double productDiscount;
            try {
                productDiscount = Double.parseDouble(discountTextField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid product discount, please enter a valid integer", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            supplierProduct.addDiscountPerAmount(productAmount, productDiscount);
//            discounts.put(productAmount, productDiscount);
            discountTableModel.addRow(new Object[]{productAmount, productDiscount});
            amountTextField.setText("");
            discountTextField.setText("");
        });


        nextButton.addActionListener(e -> {
            discountTableModel.setRowCount(0);
            currentFrame.setVisible(false);
            nextFrame.setVisible(true);
//            nextFrame.setLocationRelativeTo(null);
            //Adjust Size
            nextFrame.pack();
        });


//        JPanel backNextPanel = new JPanel();
//        backNextPanel.setLayout(new GridLayout(1, 2, 10, 10));
//        backNextPanel.add(new JLabel());
//        backNextPanel.add(nextButton);

        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
//        panel.add(backNextPanel, BorderLayout.SOUTH);


        currentFrame.getContentPane().add(panel);
        currentFrame.setSize(400, 300);
        currentFrame.setLocationRelativeTo(null);
        currentFrame.setVisible(true);
        currentFrame.pack();
    }




    public boolean checkBankAccountValidation(String input) {
        String pattern = "^\\d{6,9}$";
        return input.matches(pattern);
    }

    public Response checkBankAccount(String bankAccount) { return supplierService.checkBankAccount(bankAccount); }

    private void cleanAddNewSupplierValues() {
        name = "";
        address = "";
        bankAccount = "";
        serviceContacts = new ArrayList<>();
        paymentMethod = "";
        supplyMethod = "";
        supplyTime = 0;
        selfSupply = false;
        days = new ArrayList<>();
        items = new HashMap<>();
        amountPair = null;
        pricePair = null;
//        discounts = new HashMap<>();
    }


    private static void setFormatExample(JTextField textField, String formatExample) {
        Font defaultFont = textField.getFont();
        Color hintTextColor = Color.GRAY;

        textField.setText(formatExample);
        textField.setForeground(hintTextColor);
        textField.setFont(defaultFont.deriveFont(Font.ITALIC));

        textField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (textField.getText().equals(formatExample)) {
                    textField.setText("");
                    textField.setForeground(Color.BLACK);
                    textField.setFont(defaultFont);
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (textField.getText().isEmpty()) {
                    textField.setText(formatExample);
                    textField.setForeground(hintTextColor);
                    textField.setFont(defaultFont.deriveFont(Font.ITALIC));
                }
            }
        });

        textField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateFormatExample();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateFormatExample();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateFormatExample();
            }

            private void updateFormatExample() {
                if (textField.getText().isEmpty()) {
                    textField.setForeground(hintTextColor);
                    textField.setFont(defaultFont.deriveFont(Font.ITALIC));
                } else {
                    textField.setForeground(Color.BLACK);
                    textField.setFont(defaultFont);
                }
            }
        });
    }
}