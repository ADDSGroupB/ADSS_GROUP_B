package InterfaceLayer.GUI;

import ServiceLayer.SupplierServiceLayer.*;
import Utillity.Pair;
import Utillity.Response;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class SupplierManagerGUI extends JFrame {

    private SupplierService supplierService;
    private OrderService orderService;
    private ServiceContact serviceContact;

    // --------------------------- Add New Supplier ---------------------------

    private String name;
    private String address;
    private String bankAccount;
    private ArrayList<ServiceContact> serviceContacts;
    private ServiceAgreement serviceAgreement;
    private String paymentMethod;
    private String supplyMethod;
    private int supplyTime;
    private boolean selfSupply;
    private ArrayList<DayOfWeek> days = new ArrayList<>();
    private HashMap<Integer, SupplierProductService> items = new HashMap<>();
    private Pair<Integer, Double> amountPair;
    private Pair <Double, Double> pricePair;
    HashMap<Integer, Double> discounts = new HashMap<>();


    // ------------------------------------------------------------------------

    public SupplierManagerGUI() {
        supplierService = new SupplierService();
        orderService = new OrderService();
        serviceContact = new ServiceContact();

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
        addSupplierButton.addActionListener(e -> { addNewSupplier(); this.setVisible(false); });

        JButton deleteSupplierButton = new JButton("Delete Supplier");
        deleteSupplierButton.addActionListener(e -> deleteSupplier());

        JButton editSupplierButton = new JButton("Edit Supplier's information");
        editSupplierButton.addActionListener(e -> editSupplier());

        JButton printSuppliersButton = new JButton("Print suppliers");
        printSuppliersButton.addActionListener(e -> printSuppliersGui());

        JButton supplierOrderHistoryButton = new JButton("Show Supplier Order History");
        supplierOrderHistoryButton.addActionListener(e -> supplierOrderHistory());

        JButton backButton = new JButton("Back To Main Menu");
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
        // TODO: Test add 2 suppliers and after that try to get info from the first one.
        cleanAddNewSupplierValues();
        ArrayList <JFrame> addSupplierFrame = new ArrayList<>();
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

        for (int i = 0; i < 9; i++)
        {
            addSupplierFrame.get(i).setSize(400, 300);
            addSupplierFrame.get(i).setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            addSupplierFrame.get(i).setLocationRelativeTo(null);
        }

        setAddContactFrame(addSupplierFrame);
        setSupplyingMethodFrame(addSupplierFrame);
        setFixedDaysFrame(addSupplierFrame);
        setDaysAmountFrame(addSupplierFrame);
        setSAddItemsFrame(addSupplierFrame);
        setDiscountFrame(addSupplierFrame);
        setAddAmountDiscountFrame(addSupplierFrame);
        setAddPriceDiscountFrame(addSupplierFrame);
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

        JButton nextButton = new JButton("Next");
        nextButton.addActionListener(e -> {
            name = nameTextField.getText();
            address = addressTextField.getText();
            bankAccount = bankAccountTextField.getText();
            if (name.length() == 0 ) {
                JOptionPane.showMessageDialog(null, "You must enter name, please try again", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (address.length() == 0) {
                JOptionPane.showMessageDialog(null, "You must enter address, please try again", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!checkBankAccountValidation(bankAccount)){
                JOptionPane.showMessageDialog(null, "Not a valid bankAccount number, please try again", "Error", JOptionPane.ERROR_MESSAGE);
                bankAccountTextField.setText("");
                return;
            }
           currentFrame.setVisible(false);
            nextFrame.setVisible(true);
            //Adjust Size
            nextFrame.pack();
            // Close the dialog/frame after adding the supplier
//            addSupplierFrame.get(0).dispose();
        });

        panel.add(nameLabel);
        panel.add(nameTextField);
        panel.add(addressLabel);
        panel.add(addressTextField);
        panel.add(bankAccountLabel);
        panel.add(bankAccountTextField);
        panel.add(new JLabel()); // Empty label for spacing
        panel.add(nextButton);

        currentFrame.getContentPane().add(panel);
        currentFrame.setVisible(true);
        currentFrame.pack();

//        addSupplierFrame.get(4).setVisible(true);
//        addSupplierFrame.get(4).pack();
    }






    private void setAddContactFrame(ArrayList<JFrame> addSupplierFrame) {
        // Set the frames
        JFrame backFrame = addSupplierFrame.get(0);
        JFrame currentFrame = addSupplierFrame.get(1);
        JFrame nextFrame = addSupplierFrame.get(2);

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

        JLabel phoneNumberLabel = new JLabel("Contact Phone Number:");
        JTextField phoneNumberTextField = new JTextField();

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
            ServiceContact newContact = new ServiceContact(nameTextField.getText(), emailTextField.getText(), phoneNumberTextField.getText());
            serviceContacts.add(newContact);
            contactTableModel.addRow(new Object[]{newContact.getName(), newContact.getEmail(), newContact.getPhoneNumber()});
            nameTextField.setText("");
            emailTextField.setText("");
            phoneNumberTextField.setText("");
        });

        JButton nextButton = new JButton("Next");
        nextButton.addActionListener(e -> {
            if(serviceContacts.size() < 1)
            {
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

    private void setSupplyingMethodFrame(ArrayList<JFrame> addSupplierFrame) {
        JFrame backFrame = addSupplierFrame.get(1);
        JFrame currentFrame = addSupplierFrame.get(2);
        JFrame fixedDayFrame = addSupplierFrame.get(3);
        JFrame daysAmountFrame = addSupplierFrame.get(4);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel type = new JLabel("Payment Type: ");
        JComboBox<String> combo = new JComboBox<>( new String[] { "By Days", "By Order", "By Super-lee" });
        combo.setEditable(false);
        combo.setSelectedIndex(-1);

        panel.add(type);
        panel.add(combo);

        JButton nextButton = new JButton("Next");
        nextButton.addActionListener(e -> {
            currentFrame.setVisible(false);
            switch (combo.getSelectedIndex())
            {
                case 0 ->
                {
                    selfSupply = true;
                    supplyMethod = "FixedDay";
                    supplyTime = -1;
                    days.clear();
                    fixedDayFrame.setVisible(true);
                    //Adjust Size
                    fixedDayFrame.pack();
                }
                case 1 ->
                {
                    selfSupply = true;
                    supplyMethod = "DaysAmount";
                    days.clear();
                    daysAmountFrame.setVisible(true);
                    daysAmountFrame.pack();
                }
                case 2 ->
                {
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

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(9, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel fixedDays = new JLabel("Supplying Days: ");
        panel.add(fixedDays);

        String[] weekdays = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        JCheckBox[] dayCheckboxes = new JCheckBox[weekdays.length];
        for (int i = 0; i < weekdays.length; i++)
        {
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
            nextFrame.setLocationRelativeTo(null);
            nextFrame.setLocation(nextFrame.getX(), nextFrame.getY() - 50);
            nextFrame.pack();
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

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel daysAmountLabel = new JLabel("Number Of Days To Supply: ");
        panel.add(daysAmountLabel);

        JTextField daysAmountTextField = new JTextField();
        panel.add(daysAmountTextField);

        JButton nextButton = new JButton("Next");
        nextButton.addActionListener(e -> {

            try { supplyTime = Integer.parseInt(daysAmountTextField.getText()); }
            catch (NumberFormatException exxception)
            {
                JOptionPane.showMessageDialog(null, "You must enter valid number, please try again", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            currentFrame.setVisible(false);
            nextFrame.setVisible(true);
            nextFrame.setLocationRelativeTo(null);
            nextFrame.setLocation(nextFrame.getX(), nextFrame.getY() - 50);
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

    private void setSAddItemsFrame(ArrayList<JFrame> addSupplierFrame) {
        JFrame backFrame = supplyMethod.equals("FixedDay") ? addSupplierFrame.get(3) : addSupplierFrame.get(4);
        JFrame currentFrame = addSupplierFrame.get(5);
        JFrame nextFrame = addSupplierFrame.get(6);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        DefaultTableModel contactTableModel = new DefaultTableModel();
        contactTableModel.addColumn("Name");
        contactTableModel.addColumn("ID");
        contactTableModel.addColumn("Catalog Number");
        contactTableModel.addColumn("Price");
        contactTableModel.addColumn("Amount");
        contactTableModel.addColumn("Manufacturer");
        contactTableModel.addColumn("Expiration Days");
        contactTableModel.addColumn("Weight");
        JTable contactTable = new JTable(contactTableModel);
        JScrollPane scrollPane = new JScrollPane(contactTable);
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

        inputPanel.add(new JLabel());
        inputPanel.add(addProduct);

        addProduct.addActionListener(e -> {
            int productID;
            try { productID = Integer.parseInt(idTextField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid product ID, please enter a valid integer", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int catalogNumber;
            try { catalogNumber = Integer.parseInt(catalogNumberTextField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid catalog number, please enter a valid integer", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double price;
            try { price = Double.parseDouble(PriceTextField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid product price, please enter a valid number", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int amount;
            try { amount = Integer.parseInt(amountTextField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid product amount, please enter a valid integer", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int expirationDays;
            try { expirationDays = Integer.parseInt(expirationDaysTextField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid expiration days, please enter a valid integer", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double weight;
            try { weight = Double.parseDouble(weightTextField.getText());
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


            // Add the product after adding the discount
            if(discounts.size() < 1)
            {
                int choice = JOptionPane.showConfirmDialog(null, "Do you want to add discount?", "Confirmation", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION)
                {
                    //discount frame
                    addSupplierFrame.get(8).setVisible(true);
                    currentFrame.setVisible(false);
                    //Adjust Size
                    nextFrame.pack();
                }
            }
            SupplierProductService newSupplierProduct = new SupplierProductService(productName, productID, catalogNumber, price, amount, discounts, manufacturer, expirationDays, weight);
            items.put(productID, newSupplierProduct);
            contactTableModel.addRow(new Object[]{productName, productID, catalogNumber, price, amount, manufacturer, expirationDays, weight});
            nameTextField.setText("");
            idTextField.setText("");
            catalogNumberTextField.setText("");
            PriceTextField.setText("");
            amountTextField.setText("");
            manufacturerTextField.setText("");
            expirationDaysTextField.setText("");
            weightTextField.setText("");
            discounts.clear();
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
            nextFrame.setLocationRelativeTo(null);
            //Adjust Size
            nextFrame.pack();
        });

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            currentFrame.setVisible(false);
            backFrame.setVisible(true);
            backFrame.setLocationRelativeTo(null);
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

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        DefaultTableModel contactTableModel = new DefaultTableModel();
        contactTableModel.addColumn("Product Amount");
        contactTableModel.addColumn("Discount (%)");
        JTable contactTable = new JTable(contactTableModel);
        JScrollPane scrollPane = new JScrollPane(contactTable);
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


        JButton addDiscount = new JButton("Add Discount");
        inputPanel.add(new JLabel());
        inputPanel.add(addDiscount);

        JButton nextButton = new JButton("Stop Adding Discounts");
        inputPanel.add(new JLabel());
        inputPanel.add(nextButton);

        addDiscount.addActionListener(e -> {
            int productAmount;
            try { productAmount = Integer.parseInt(amountTextField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid product amount, please enter a valid integer", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double productDiscount;
            try { productDiscount = Double.parseDouble(discountTextField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid product discount, please enter a valid integer", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            discounts.put(productAmount, productDiscount);
            contactTableModel.addRow(new Object[]{productAmount, productDiscount});
            amountTextField.setText("");
            discountTextField.setText("");
        });


        nextButton.addActionListener(e -> {
            currentFrame.setVisible(false);
            nextFrame.setVisible(true);
            nextFrame.setLocationRelativeTo(null);
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
    }

    private void setAddAmountDiscountFrame(ArrayList<JFrame> addSupplierFrame) {
        JFrame backFrame = addSupplierFrame.get(5);
        JFrame currentFrame = addSupplierFrame.get(6);
        JFrame nextFrame = addSupplierFrame.get(7);


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
            try { productAmount = Integer.parseInt(amountTextField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid product amount, please enter a valid integer", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double productDiscount;
            try { productDiscount = Double.parseDouble(discountTextField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid product discount, please enter a valid integer", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if(!amountTextField.getText().isEmpty() && !discountTextField.getText().isEmpty())
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
            backFrame.setLocationRelativeTo(null);
            backFrame.pack();
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
            if(!amountTextField.getText().isEmpty()) {
                try {
                    productAmount = Double.parseDouble(amountTextField.getText());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid product amount, please enter a valid integer", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            double productDiscount = 0;
            if(!discountTextField.getText().isEmpty()) {
                try {
                    productDiscount = Double.parseDouble(discountTextField.getText());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid product discount, please enter a valid integer", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            if(!amountTextField.getText().isEmpty() && !discountTextField.getText().isEmpty())
                pricePair = new Pair<>(productAmount, productDiscount);
            ServiceAgreement serviceAgreement;
            serviceAgreement = new ServiceAgreement(paymentMethod, selfSupply, days, items,amountPair,pricePair, supplyMethod, supplyTime);
            Response res = supplierService.addSupplier(name, address, bankAccount, serviceAgreement, serviceContacts);
            if (!res.errorOccurred())
                JOptionPane.showMessageDialog(null, "Supplier added successfully, supplier's id is: " + res.getSupplierId(), "Supplier Added Successfully", JOptionPane.INFORMATION_MESSAGE);
            else
                JOptionPane.showMessageDialog(null, res.getErrorMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
            backFrame.setLocationRelativeTo(null);
            backFrame.pack();
        });

        JPanel backNextPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(1, 2, 10, 10));
        inputPanel.add(backButton);
        inputPanel.add(nextButton);
        currentFrame.getContentPane().add(inputPanel);
    }

    private void deleteSupplier() {
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
        c.add(deleteButton, BorderLayout.SOUTH);

        int i = supplierService.getLastSupplierID();
        if (i != -1) {
            for (int j = 1; j <= i; j++) {
                Response res = supplierService.getSupplierNameById(j);
                if (res != null) {
                    if(res.getAnswer()) {
                        Object[] rowData = {j, res.getStringValue()};
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

        f.setSize(400, 300);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }



    private void editSupplier() {
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

        JButton editPersonalDetailsButton = new JButton("Edit Supplier Personal Details");
        JButton editAgreementButton = new JButton("Edit Supplier Agreement");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(editPersonalDetailsButton);
        buttonPanel.add(editAgreementButton);
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
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(f, "Please select a supplier from the table.", "No Supplier Selected", JOptionPane.WARNING_MESSAGE);
            } else {
                int selectedID = (int) table.getValueAt(selectedRow, 0);
                String selectedName = (String) table.getValueAt(selectedRow, 1);

                // Call the editSupplierPersonalDetails() method with the selected ID
                editSupplierPersonalDetails(selectedID);

                JOptionPane.showMessageDialog(f, "Editing supplier's personal details with ID " + selectedID + " (" + selectedName + ")", "Supplier Edit", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        editAgreementButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(f, "Please select a supplier from the table.", "No Supplier Selected", JOptionPane.WARNING_MESSAGE);
            } else {
                int selectedID = (int) table.getValueAt(selectedRow, 0);
                String selectedName = (String) table.getValueAt(selectedRow, 1);

                // Call the editSupplierAgreement() method with the selected ID
                editSupplierAgreement(selectedID);

                JOptionPane.showMessageDialog(f, "Editing supplier's agreement with ID " + selectedID + " (" + selectedName + ")", "Supplier Edit", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        f.setSize(400, 300);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }

    private void editSupplierPersonalDetails(int supplierID) {
        String[] options = {"Edit Supplier Contact", "Edit Supplier Name", "Edit Supplier Address", "Edit Supplier Bank Account"};
        int choice = JOptionPane.showOptionDialog(null, "Select an option:", "Edit Supplier Personal Details",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

        switch (choice) {
            case 0:
                // Edit Supplier Contact
                editSupplierContact(supplierID);
                break;
            case 1:
                // Edit Supplier Name
                editSupplierName(supplierID);
                break;
            case 2:
                // Edit Supplier Address
                editSupplierAddress(supplierID);
                break;
            case 3:
                // Edit Supplier Bank Account
                editSupplierBankAccount(supplierID);
                break;
            default:
                // Invalid choice or dialog closed
                break;
        }
    }

    private void editSupplierContact(int supplierID) {
        // Logic to edit supplier contact
        // Example:
        String newContact = JOptionPane.showInputDialog(null, "Enter new contact for the supplier:");
        // Perform the necessary operations to update the supplier's contact information
    }

    private void editSupplierName(int supplierID) {
        // Logic to edit supplier name
        // Example:
        String newName = JOptionPane.showInputDialog(null, "Enter new name for the supplier:");
        // Perform the necessary operations to update the supplier's name
    }

    private void editSupplierAddress(int supplierID) {
        // Logic to edit supplier address
        // Example:
        String newAddress = JOptionPane.showInputDialog(null, "Enter new address for the supplier:");
        // Perform the necessary operations to update the supplier's address
    }

    private void editSupplierBankAccount(int supplierID) {
        // Logic to edit supplier bank account
        // Example:
        String newBankAccount = JOptionPane.showInputDialog(null, "Enter new bank account for the supplier:");
        // Perform the necessary operations to update the supplier's bank account information
    }


    private void editSupplierAgreement(int supplierID) {
        // Add your logic for editing supplier's agreement details based on the supplierID
        // You can use JOptionPane or other GUI components to gather user input
        // Example:
        String newAgreement = JOptionPane.showInputDialog(null, "Enter new agreement details for the supplier:");
        // Perform the necessary operations to update the supplier's agreement details
    }


    private void printSuppliersGui() {
        supplierService.printSuppliersGui();
    }

    private void supplierOrderHistory() {
        // Implementation of the supplierOrderHistory method for the GUI
    }

    public boolean checkBankAccountValidation(String input) {
        String pattern = "^\\d{6,9}$";
        return input.matches(pattern);
    }

    private void cleanAddNewSupplierValues()
    {
        name = "";
        address = "";
        bankAccount = "";
        serviceContacts = new ArrayList<>();
        serviceAgreement = null;
        paymentMethod = "";
        supplyMethod = "";
        supplyTime = 0;
        selfSupply = false;
        days = new ArrayList<>();
        items = new HashMap<>();
        amountPair = null;
        pricePair = null;
        discounts = new HashMap<>();
    }

//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            SupplierManagerGUI gui = new SupplierManagerGUI();
//            gui.setVisible(true);
//        });
//    }
}