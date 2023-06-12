package InterfaceLayer.GUI;

import ServiceLayer.SupplierServiceLayer.OrderService;
import ServiceLayer.SupplierServiceLayer.ServiceAgreement;
import ServiceLayer.SupplierServiceLayer.ServiceContact;
import ServiceLayer.SupplierServiceLayer.SupplierService;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class SupplierManagerGUI extends JFrame {

    private SupplierService supplierService;
    private OrderService orderService;
    private ServiceContact serviceContact;

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
        addSupplierButton.addActionListener(e -> addNewSupplier());

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
        ArrayList<ServiceContact> serviceContacts = new ArrayList<>();
        ArrayList<ServiceAgreement> serviceAgreements = new ArrayList<>();
        final JFrame[] addSupplierFrame = {new JFrame("Add New Supplier")};
        addSupplierFrame[0].setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addSupplierFrame[0].setSize(400, 300);
        addSupplierFrame[0].setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));





        JLabel nameLabel = new JLabel("Supplier's Name:");
        JTextField nameTextField = new JTextField();

        JLabel addressLabel = new JLabel("Supplier's Address:");
        JTextField addressTextField = new JTextField();

        JLabel bankAccountLabel = new JLabel("Bank Account Number:");
        JTextField bankAccountTextField = new JTextField();

        JButton nextButton = new JButton("Next");

//        JButton addSupplierButton = new JButton("Add Supplier's Contacts");
        nextButton.addActionListener(e -> {
            String name = nameTextField.getText();
            String address = addressTextField.getText();
            String bankAccount = bankAccountTextField.getText();
            if (!checkBankAccountValidation(bankAccount)){
                JOptionPane.showMessageDialog(null, "Not a valid bankAccount number, please try again", "Error", JOptionPane.ERROR_MESSAGE);
                bankAccountTextField.setText("");
                return;
            }

            try {
                addSupplierFrame[0].setVisible(false);
                addSupplierContacts(serviceContacts, addSupplierFrame[0]);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
//              System.out.println( "size now: "+serviceContacts.size());
            // Perform validation and other necessary operations
            // to add the supplier using the entered details

            // supplierService.addSupplier()

            // Close the dialog/frame after adding the supplier

            addSupplierFrame[0].dispose();
        });

        panel.add(nameLabel);
        panel.add(nameTextField);
        panel.add(addressLabel);
        panel.add(addressTextField);
        panel.add(bankAccountLabel);
        panel.add(bankAccountTextField);
        panel.add(new JLabel()); // Empty label for spacing
        panel.add(nextButton);
//        panel.add(addSupplierButton);
//        panel.add(new JLabel());
//        panel.add(new JLabel());
//        panel.add(new JLabel(), BorderLayout.WEST);  // Bottom-left corner
//        panel.add(nextButton, BorderLayout.EAST);  // Bottom-right corner

        addSupplierFrame[0].getContentPane().add(panel);
        addSupplierFrame[0].setVisible(true);
    }

    private void addSupplierAgreement() {
        JComboBox<String> combo;
        JLabel myLabel;
        JFrame f = new JFrame("payment Type");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container c = f.getContentPane();
        c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));

        JLabel type = new JLabel("choose the Payment Type:");
        c.add(type);

        String[] options = {"By Days", "By Order", "By Super-lee"};
        combo = new JComboBox<>(options);
        combo.setEditable(true);
        combo.setSelectedIndex(-1);
        c.add(combo);

        JButton done = new JButton("Done");
        c.add(done);

        myLabel = new JLabel("Please select");
        c.add(myLabel);
        f.pack();
        f.setVisible(true);
        JLabel finalMyLabel = myLabel;
        done.addActionListener(e -> {
            String t = (String) combo.getSelectedItem();
            if (t == null) t = "Nothing selected";
            finalMyLabel.setText(t);
        });
    }

    private void addSupplierContacts(ArrayList<ServiceContact> serviceContacts, JFrame addSupplierFrame) throws InterruptedException {
        JFrame addContactFrame = new JFrame("Add Supplier's Contacts");
        addContactFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addContactFrame.setSize(400, 300);
        addContactFrame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel nameLabel = new JLabel("Contact Name:");
        JTextField nameTextField = new JTextField();

        JLabel emailLabel = new JLabel("Contact Email:");
        JTextField emailTextField = new JTextField();

        JLabel phoneNumberLabel = new JLabel("Contact Phone Number:");
        JTextField phoneNumberTextField = new JTextField();

        JButton addAnotherContact = new JButton("Add Another Contact");
        JButton finish = new JButton("stop adding contacts");
        addAnotherContact.addActionListener(e -> {
            serviceContacts.add(new ServiceContact(nameTextField.getText(), emailTextField.getText(), phoneNumberTextField.getText()));
            System.out.println(serviceContacts.size());
            try {
                addSupplierContacts(serviceContacts, addSupplierFrame);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }

            // Perform validation and other necessary operations
            // to add the supplier using the entered details

            // supplierService.addSupplier()

            // Close the dialog/frame after adding the supplier
            addContactFrame.dispose();
        });
        finish.addActionListener(e -> {
            addContactFrame.dispose();
            addSupplierAgreement();
            // Perform validation and other necessary operations
            // to add the supplier using the entered details

            // supplierService.addSupplier()
            // Close the dialog/frame after adding the supplier
        });

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> { addSupplierFrame.setVisible(true); addContactFrame.setVisible(false); });

        panel.add(nameLabel);
        panel.add(nameTextField);
        panel.add(emailLabel);
        panel.add(emailTextField);
        panel.add(phoneNumberLabel);
        panel.add(phoneNumberTextField);
        panel.add(addAnotherContact);
        panel.add(finish);
        panel.add(new JLabel());
        panel.add(new JLabel());
        panel.add(backButton);

        addContactFrame.getContentPane().add(panel);
        addContactFrame.setVisible(true);
    }

    private void deleteSupplier() {
        // Implementation of the deleteSupplier method for the GUI
    }

    private void editSupplier() {
        // Implementation of the editSupplier method for the GUI
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

//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            SupplierManagerGUI gui = new SupplierManagerGUI();
//            gui.setVisible(true);
//        });
//    }
}