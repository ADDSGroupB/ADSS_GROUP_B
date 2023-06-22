package InterfaceLayer.CLI;

import ServiceLayer.SupplierServiceLayer.*;
import Utillity.Pair;
import Utillity.Response;

import java.time.DayOfWeek;
import java.util.*;

public class SupplierManagerCLI {
    private final Scanner reader;
    private final SupplierService supplierService;
    private final ServiceContact serviceContact;
    private final OrderService orderService;
    public SupplierManagerCLI() {
        reader = new Scanner(System.in);
        supplierService = new SupplierService();
        serviceContact = new ServiceContact();
        orderService = new OrderService();
    }

//    public static void main(String[] args) {
//        SupplierCLI supplierCLI = new SupplierCLI();
////        supplierCLI.createPeriodicOrder();
//        supplierCLI.updateProductsInOrder();
//        supplierCLI.removeProductsFromOrder();
//    }

    public void print(String message){
        System.out.println(message);
    }


    public void Start() {
        supplierManagerCLI();
    }

    private void supplierManagerCLI() {
        int action =0;
        while (action != 6)
        {
            print("Please choose one of the following option:\n" +
                    "1. Add new Supplier\n" +
                    "2. Delete Supplier \n" +
                    "3. Edit Supplier's information \n" +
                    "4. Print suppliers \n" +
                    "5. Show Supplier Order History \n" +
                    "6. Exit");
            try
            {
                action = reader.nextInt();
                reader.nextLine();
            }
            catch (Exception e)
            {
                System.out.println("Please enter an integer between 1-6 ");
                reader.nextLine();
                continue;
            }
            switch (action) {
                case 1 -> addNewSupplier();
                case 2 -> deleteSupplier();
                case 3 -> editSupplier();
                case 4 -> printSuppliers();
                case 5 -> supplierOrderHistory();
                case 6 -> { System.out.println("Exiting from the system"); System.exit(0); }
                default -> System.out.println("Please enter an integer between 1-6 ");
            }
        }

    }

    private void supplierOrderHistory() {
        System.out.println("Please Enter Supplier ID: ");
        int supplierID = reader.nextInt();
        reader.nextLine();
        orderService.printOrder(supplierID);
    }

    private void printSuppliers() {
        supplierService.printSuppliers();
    }

    private void editSupplier() {
        print("Please Enter The ID of the Supplier you wish to edit");
        int id = reader.nextInt();
        reader.nextLine();
        int keepEditing = 1;
        while (keepEditing == 1) {
            System.out.println("What would you like to edit?  \n1. Supplier's personal details \n2. Supplier's agreement details ");
            int action = reader.nextInt();
            reader.nextLine();
            switch (action) {
                case 1 -> editSupplierPersonalDetails(id);
                case 2 -> editSupplierAgreement(id);
            }
            System.out.println("Would you like to edit anything else?  \n1. Yes\n2. No");
            keepEditing = reader.nextInt();
            reader.nextLine();
        }
    }

    private void editSupplierAgreement(int id) {
        System.out.println("What would you like to edit? \n1. Supplier's payment method + delivery method + delivery days \n2.Supplier's items \n");
        int action = reader.nextInt();
        reader.nextLine();
        while (action < 1 || action > 4) {
            System.out.println("Please Enter a valid number (1 to 3)");
            action = reader.nextInt();
            reader.nextLine();
        }
        switch (action) {
            case 1 -> editPaymentMethodAndDeliveryMethodAndDeliveryDays(id);
            case 2 -> editItems(id);
        }
    }
    private void editPaymentMethodAndDeliveryMethodAndDeliveryDays(int id) {
        String paymentMethod = choosePaymentMethod();
        boolean selfSupply;
        String supplyMethod;
        int supplyTime;
        ArrayList<DayOfWeek> days = new ArrayList<>();
        print("Choose one of the following Supply methods according to the supplier's agreement \n1. By Days \n2. By Order \n3. By Super-Lee");
        int userInput = reader.nextInt();
        reader.nextLine();
        while (userInput < 1 || userInput > 3) {
            print("Please Enter a valid number (1,2,3)");
            userInput = reader.nextInt();
            reader.nextLine();
        }
        //supplying in specific days
        if (userInput == 1) {
            selfSupply = true;
            supplyMethod = "FixedDay";
            supplyTime = -1;
            int day = 0;
            print("please choose supplying days");
            print("\n1. Monday \n2. Tuesday \n3. Wednesday \n4. Thursday \n5. Friday \n6. Saturday \n7. Sunday \n8.That's all...");
            while (day != 8) {
                day = reader.nextInt();
                reader.nextLine();
                if (day >= 1 && day <= 7)
                    days.add(DayOfWeek.of(day));
                else if (day != 8)
                    print("Please enter a valid number : 1 to 7, or 8 if you done adding days");
            }
        }
        //supplying by order
        else if (userInput == 2){
            selfSupply = true;
            supplyMethod = "DaysAmount";
            print("please choose number of days to supply:");
            supplyTime = reader.nextInt();
        }
        ////userinput=3 , superLee supply
        else {
            selfSupply = false;
            supplyMethod = "SuperLee";
            print("please choose number of days to supply:");
            supplyTime = reader.nextInt();
        }

        Response res = supplierService.editPaymentMethodAndDeliveryMethodAndDeliveryDays(id, selfSupply, paymentMethod ,days, supplyMethod, supplyTime);
        if(!res.errorOccurred()){
            print("The supplier's delivery term changed successfully for Supplier with id: " + id);
        }
        else {
            print(res.getErrorMessage());
        }
    }

    private void editItems(int supplierID) {
        print("Please choose an action?  \n1. Add a new item\n2. Delete an item \n3. edit an item");
        int action = reader.nextInt();
        reader.nextLine();
        if (action == 1) {
            SupplierProductService newItem = createProduct();
            // printAllProducts();
            Response res = supplierService.addItemToAgreement(supplierID, newItem.getName(), newItem.getProductId(), newItem.getCatalogNumber(), newItem.getPrice(), newItem.getAmount(), newItem.getDiscountPerAmount(), newItem.getWeight(), newItem.getManufacturer(), newItem.getExpirationDays());
            if (res.errorOccurred())
                print(res.getErrorMessage());
        }
        else if (action == 2) {
            print("please enter the product ID you wish to delete from suppliers agreement");
            int itemIdToDelete = reader.nextInt();
            Response res = supplierService.removeItemFromAgreement(supplierID, itemIdToDelete);
            if (res.errorOccurred())
                print(res.getErrorMessage());
        }
        else if (action == 3) {
            editItem(supplierID);
        }
    }
    public void editItem(int supplierID) {
        print("please enter the product ID you want to edit");
        int productId = reader.nextInt();
        print("Please choose an action?  \n1. edit item's catalog number\n2. Delete a discount for this item \n3. add a discount for this item");
        int action = reader.nextInt();
        reader.nextLine();
        switch (action) {
            case 1 -> editItemCatalodNumber(supplierID, productId);
            case 2 -> editRemoveDiscount(supplierID, productId);
            case 3 -> editAddDiscount(supplierID, productId);
        }
    }

    private void editAddDiscount(int supplierID, int productId) {
        print("please add Discount according to the format:amount:Discount in percentages");
        String s=reader.nextLine();
        String[] val = s.split(":");
        int ammount = Integer.parseInt(val[0]);
        double discount = Double.parseDouble(val[1]);
        Response res = supplierService.addDiscounts(supplierID, productId, ammount, discount);
        if (res.errorOccurred())
            print(res.getErrorMessage());
        else{
            print("discount added successfully");
        }
    }


    private void editRemoveDiscount(int supplierID, int productId) {
        print("please enter the discount you want to delete");
        String s=reader.nextLine();
        String[] val = s.split(":");
        int amount = Integer.parseInt(val[0]);
        double discount = Double.parseDouble(val[1]);
        Response res = supplierService.removeDiscounts(supplierID, productId, amount, discount);
        if (res.errorOccurred())
            print(res.getErrorMessage());
        else{
            print("discount deleted successfully");
        }
    }

    private void editItemCatalodNumber(int supplierId, int productId) {
        print("please enter the new catalog number");
        int newCatalogNumber = reader.nextInt();
        Response res = supplierService.editItemCatalogdNumber(supplierId, productId, newCatalogNumber);
        if (res.errorOccurred())
            print(res.getErrorMessage());
    }


    private void editSupplierPersonalDetails(int id) {
        System.out.println("What would you like to edit?  \n1. Supplier's contacts \n2. Supplier's address \n3. Supplier's bank account \n4. Supplier's name ");
        int action = reader.nextInt();
        reader.nextLine();
        while (action < 1 || action > 6) {
            System.out.println("Please Enter a valid number (1 to 4)");
            action = reader.nextInt();
            reader.nextLine();
        }
        switch (action) {
            case 1 -> editContacts(id);
            case 2 -> editAddress(id);
            case 3 -> editBankAccount(id);
            case 4 -> editSupplierName(id);
        }
    }

    private void editSupplierName(int id) {
        print("Please enter the new name");
        String name = reader.nextLine();
        Response res = supplierService.changeSupplierName(id, name);
        if (res.errorOccurred())
            print(res.getErrorMessage());

    }

    private void editBankAccount(int id) {
        print("Please enter the new bank account number");
        String bankAccount = reader.nextLine();
        Response res = supplierService.changeSupplierBankAccount(id, bankAccount);
        if (res.errorOccurred())
            print(res.getErrorMessage());

    }

    private void editAddress(int id) {
        print("Please enter the new address");
        String address = reader.nextLine();
        Response res = supplierService.changeAddress(id, address);
        if (res.errorOccurred())
            print(res.getErrorMessage());
        else {
            print("email changed successfully");
        }

    }

    private void editContacts(int id) {
        print("Choose an action \n1. Add a contact \n2. Delete a contact \n3. Edit contact's email \n4. Edit contact's phone number");
        int action;
        try { action = Integer.parseInt(reader.nextLine()); }
        catch (NumberFormatException e) { action = 5; }
        while (action < 1 || action > 4) {
            System.out.println("Please Enter a valid number (1 to 4)");
            try { action = Integer.parseInt(reader.nextLine()); }
            catch (NumberFormatException e) { action = 5; }
        }
        switch (action) {
            case 1 -> addContactToSupplier(id);
            case 2 -> deleteContact(id);
            case 3 -> editContactEmail(id);
            case 4 -> editContactPhone(id);
        }
    }

    private void editContactPhone(int id) {
//        String email=getValidEmail();
        String oldPhone = getValidPhoneNumber();
        //String name = reader.nextLine();
        print("Please enter the new phone number");
        String newPhone = reader.nextLine();
        while (!serviceContact.validatePhoneNumber(newPhone)) {
            print("Not a valid phone number, please try again");
            newPhone = reader.nextLine();
        }
        Response res = supplierService.editSupplierContacts(id, "", "", newPhone, oldPhone);
        if (res.errorOccurred())
            print(res.getErrorMessage());

    }


    private void editContactEmail(int id) {
        String email=getValidEmail();
        String phoneNumber = getValidPhoneNumber();
        print("Please enter the new email ");
        String newEmail = reader.nextLine();
        while (!serviceContact.isValidEmail(email)) {
            print("Not a valid email, please try again");
            newEmail = reader.nextLine();
        }
        Response res = supplierService.editSupplierContacts(id, email, newEmail, "", phoneNumber);
        if (res.errorOccurred())
            print(res.getErrorMessage());

    }


    private void deleteContact(int id) {
//        String email=getValidEmail();
        String phoneNumber = getValidPhoneNumber();
        Response res = supplierService.removeSupplierContact(id, phoneNumber);
        if (res.errorOccurred())
            print(res.getErrorMessage());
        else{
            print("contact withe phone number: "+ phoneNumber+" deleted successfully from supplier with id: "+id);
        }
    }
    public String getValidEmail(){
        print("Please enter contact's email: ");
        String email = reader.nextLine();
        // reader.nextLine();
        while (!serviceContact.isValidEmail(email)) {
            print("not a valid email. please enter a legal email address");
            email = reader.nextLine();
            //  reader.nextLine();
        }
        return email;
    }

    public String getValidPhoneNumber(){
        print("Please enter contact's phoneNumber: ");
        String phoneNumber = reader.nextLine();
        // reader.nextLine();
        while (!serviceContact.validatePhoneNumber(phoneNumber)) {
            print("not a valid phone number. please enter a legal phone number");
            phoneNumber = reader.nextLine();
            //  reader.nextLine();
        }
        return phoneNumber;
    }

    private void addContactToSupplier(int id) {
        ServiceContact c = addContact();
        Response res = supplierService.addContactsTOSupplier(id, c.getName(), c.getEmail(), c.getPhoneNumber());
        if (res.errorOccurred())
            print(res.getErrorMessage());
    }

    private void deleteSupplier() {
        int keepDeleting = 1;
        while (keepDeleting == 1) {
            print("Please Enter The ID of the Supplier you wish to delete");
            int id = reader.nextInt();
            reader.nextLine();
            Response res = supplierService.removeSupplier(id);
            if (res.errorOccurred())
                print(res.getErrorMessage());
            else {
                print("The supplier with id: " + res.getSupplierId() + " deleted successfully");
            }
            print("Would you like to delete another supplier?  \n1. Yes\n2. No");
            keepDeleting = reader.nextInt();
            reader.nextLine();
        }
    }



    private void addNewSupplier() {
        int keepAdding = 1;
        while (keepAdding==1) {
            print("Please enter the supplier's name");
            String name = reader.nextLine();
            print("Please enter the supplier's address");
            String address = reader.nextLine();
            print("Please enter the supplier's bank account number");
            String bankAccount = reader.nextLine();
            while (!checkBankAccountValidation(bankAccount)){
                print("not a valid bankAccount number, please try again");
                bankAccount = reader.nextLine();
            }
            ArrayList<ServiceContact> contactList = createContacts();

            ServiceAgreement serviceAgreement = createAgreement();
            Response res = supplierService.addSupplier(name, address, bankAccount, serviceAgreement, contactList);
            if (!res.errorOccurred()) {
                print("Supplier added successfully, supplier's id is: " + res.getSupplierId());
            }
            else{
                print(res.getErrorMessage());
            }
            print("Would you like to add another supplier?  \n1. Yes\n2. No");
            keepAdding = reader.nextInt();
            reader.nextLine();
        }
    }

    private ServiceAgreement createAgreement() {
        String paymentMethod = choosePaymentMethod();
        String supplyMethod;
        int supplyTime;
        boolean selfSupply;
        ArrayList<DayOfWeek> days = new ArrayList<>();
        print("Choose one of the following Supply methods according to the supplier's agreement \n1. By Days \n2. By Order \n3. By Super-Lee");
        int userInput = reader.nextInt();
        reader.nextLine();
        while (userInput < 1 || userInput > 3) {
            print("Please Enter a valid number (1,2,3)");
            userInput = reader.nextInt();
            reader.nextLine();
        }
        //supplying in specific days
        if (userInput == 1) {
            selfSupply = true;
            supplyMethod = "FixedDay";
            supplyTime = -1;
            int day = 0;
            print("please choose suppling days");
            print("\n1. Monday \n2. Tuesday \n3. Wednesday \n4. Thursday \n5. Friday \n6. Saturday \n7. Sunday \n8.That's all...");
            while (day != 8) {
                day = reader.nextInt();
                reader.nextLine();
                if (day >= 1 && day <= 7)
                    days.add(DayOfWeek.of(day));
                else if (day != 8)
                    print("Please enter a valid number : 1 to 7, or 8 if you done adding days");
            }
        }
        //supplying by order
        else if (userInput == 2){
            selfSupply = true;
            supplyMethod = "DaysAmount";
            print("please choose number of days to supply:");
            supplyTime = reader.nextInt();
        }
        ////userinput=3 , superLee supply
        else {
            selfSupply = false;
            supplyMethod = "SuperLee";
            print("please choose number of days to supply:");
            supplyTime = reader.nextInt();
        }

        print("Would you like to add specific items  to the agreement? \n1. Yes\n2. No");
        int keepAdding = reader.nextInt();

        reader.nextLine();
        HashMap<Integer, SupplierProductService> items = new HashMap<>();
        while (keepAdding == 1) {
            SupplierProductService newProduct = createProduct();
            items.put(newProduct.getProductId(), newProduct);
            print("Would you like to add another Item? \n1. Yes\n2. No");
            keepAdding = reader.nextInt();
            reader.nextLine();
        }
        print("Would you like to add discounts per order price and per order amount? \n1. Yes\n2. No");
        int addDiscount = reader.nextInt();
        reader.nextLine();
        if (addDiscount ==1){
            print("please enter the minimum amount you want to give a discount for, and the discount in precentage in the format : amount:discount");
            String amountDiscount = reader.nextLine();
            //reader.nextLine();
            String [] arr1 = amountDiscount.split(":");
            print("please enter the minimum order price you want to give a discount for, and the discount price in the format : price:discount price");
            String priceDiscount = reader.nextLine();
            String [] arr2 = priceDiscount.split(":");
            Pair <Integer, Double> amountPair = new Pair<>(Integer.parseInt(arr1[0]), Double.parseDouble(arr1[1]));
            Pair <Double, Double> pricePair = new Pair<>(Double.parseDouble(arr2[0]), Double.parseDouble(arr2[1]));
            return new ServiceAgreement(paymentMethod, selfSupply, days, items,amountPair,pricePair, supplyMethod, supplyTime);

        }
        return new ServiceAgreement(paymentMethod, selfSupply, days, items, supplyMethod, supplyTime);
    }
    public SupplierProductService createProduct(){
        print("Please enter product's name");
        String name = reader.nextLine();
        print("Please enter product's id");
        int productId = reader.nextInt();
        print("Please enter product's price");
        double price = reader.nextDouble();
        print("Please enter product's catalog number");
        int catalogNumber = reader.nextInt();
        print("Please enter product's amount");
        int amount = reader.nextInt();
        print("Please enter product's manufacturer");
        String manufacturer = reader.next();
        print("Please enter product's expiration Days");
        int expirationDays = reader.nextInt();
        print("Please enter product's weight");
        double weight = reader.nextDouble();
        reader.nextLine();
        print("Ok, now please add Discounts according to the format:amount:Discount in percentages,amount:Discount in percentages,..");
        String[] arr = reader.nextLine().split("\\s*,\\s*");
        HashMap<Integer, Double> discounts = new HashMap<>();
        for(String s1 : arr) {
            String[] val = s1.split(":");
            int key = Integer.parseInt(val[0]);
            double value = Double.parseDouble(val[1]);
            discounts.put(key, value);}
        return new SupplierProductService(name, productId, catalogNumber, price, amount, discounts, manufacturer, expirationDays, weight);
    }


    private String choosePaymentMethod() {
        print("please choose  Payment method according to the supplier's agreement \n1. Cash \n2. TransitToAccount \n3. Credit\n4. net 30 EOM \n5. net 60 EOM");
        int paymentMethod = reader.nextInt();
        reader.nextLine();
        while (paymentMethod < 1 || paymentMethod > 5) {
            print("Please Enter a valid number (1 to 5)");
            paymentMethod = reader.nextInt();
            reader.nextLine();
        }
        switch (paymentMethod) {
            case 1:
                return "Cash";
            case 2:
                return "TransitToAccount";
            case 3:
                return "Credit";
            case 4:
                return "net 30 EOM";
            case 5:
                return "net 60 EOM";
            default:
                choosePaymentMethod();
        }
        return "";
    }

    private ArrayList<ServiceContact> createContacts() {
        ArrayList<ServiceContact> contactList = new ArrayList<>();
        int keepAdding = 1;
        while (keepAdding == 1) {
            ServiceContact c=addContact();
            contactList.add(c);
            print("Would you like to add another contact? \n1. Yes\n2. No");
            keepAdding = reader.nextInt();
            reader.nextLine();
        }
        return contactList;
    }



    public ServiceContact addContact(){
        print("Please enter a contact name");
        String nameC = reader.nextLine();
        String email=getValidEmail();
        print("Please enter " + nameC + "'s phone number");
        String phone = reader.nextLine();
        while (!serviceContact.validatePhoneNumber(phone)){
            print("Not a valid phone number, please try again");
            phone = reader.nextLine();
        }


        //reader.nextLine();
        return new ServiceContact(nameC, email, phone);

    }


    public boolean checkBankAccountValidation(String input) {
        String pattern = "^\\d{6,9}$";
        return input.matches(pattern);
    }


}
