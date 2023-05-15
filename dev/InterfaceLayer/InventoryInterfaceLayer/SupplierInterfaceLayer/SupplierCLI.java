package InterfaceLayer.InventoryInterfaceLayer.SupplierInterfaceLayer;

import DataAccessLayer.InventoryDataAccessLayer.SupplierDataAccessLayer.Database;
import ServiceLayer.SupplierServiceLayer.*;
import Utillity.Pair;
import Utillity.Response;

import java.time.DayOfWeek;
import java.util.*;
import java.util.concurrent.TimeUnit;


public class SupplierCLI {
    private final Scanner reader;
    private final SupplierService supplierService;

    private final ServiceContact serviceContact;

    private final OrderService orderService;



    public SupplierCLI() {
        Database.connect();
        Database.createTables();
        reader = new Scanner(System.in);
        supplierService = new SupplierService();
        serviceContact = new ServiceContact();
        orderService = new OrderService();
        startDailyTask();
    }

    public static void main(String[] args) {
        SupplierCLI supplierCLI = new SupplierCLI();
//        supplierCLI.createPeriodicOrder();
        supplierCLI.updateProductsInOrder();
        supplierCLI.removeProductsFromOrder();
    }

    public void print(String message){
        System.out.println(message);
    }


    public void start() {



        int userInput;

        print("Hello! and Welcome to Super Lee- supplier module, I am available to assist you with any requests you may have\nDo you want to upload existing data or start over?  \n1. Load Data\n2. New Data \n3. EXIT\n");
        int n = reader.nextInt();
        //reader.nextLine();
        if (n == 1) {
            loadData();
            start();
            ArrayList<HashMap<Integer, Integer>> shortage = enterShortage();
            boolean valid = false;
            while (!valid) {
                print("For scenario one press 1- create an order from one supplier (only one supplier can supply all the products)\n");
                print("For scenario two press 2- The order will be split into several orders according to different suppliers\n");
                print("For scenario three press 3- the order can't be created\n");
                print("Press 0- back to the menu");
//                printOrders();
                int choose = reader.nextInt();
                switch (choose) {
                    case 1 -> {
                        valid = true;
                        supplierService.createOrderByShortage(1 ,shortage.get(0));
                        printOrders();
                    }
                    case 2 -> {
                        valid = true;
                        supplierService.createOrderByShortage(1 ,shortage.get(1));
                        printOrders();
                    }
                    case 3 -> {
                        valid = true;
                        supplierService.createOrderByShortage(1 ,shortage.get(2));
                        printOrders();
                    }
                    case 0 -> { createPeriodicOrder(); start(); }
                    default -> print("Please choose a valid number (1,2,3)");
                }
            }
        }
        if (n == 3) {
            print("Hope you enjoyed, see you next time :)");
            Database.disconnect();
            System.exit(0);
        }
        //if the user pressed 2 we play the next section
        if (n == 2) {
            while (true) {
                    print("Which module are you interested in?  \n1. Supplier\n2. EXIT\n3. Back");
                    userInput = reader.nextInt();
                    reader.nextLine();
                    if (userInput < 1 || userInput > 3) {
                        print("Please choose one of the following options:\n1. Supplier\n2. EXIT\n3. Back");
                        userInput = reader.nextInt();
                        reader.nextLine();
                }
                if (userInput == 1) {
                    supplierManagerCLI();
                }
                if (userInput == 2) {
                    print("Hope you enjoyed, see you next time :)");
                    Database.disconnect();
                    System.exit(0);
                }
                else {
                    start();
                }
            }
        }
        else{
            print("Input out of range, please choose again");
            start();
        }
    }

        private ArrayList<HashMap<Integer, Integer>> enterShortage() {
        HashMap <Integer,Integer> productsToOrder1 = new HashMap<>();//order 1
        productsToOrder1.put(5, 10);//bamba
        productsToOrder1.put(11, 30);//cafe

        HashMap <Integer,Integer> productsToOrder2 = new HashMap<>();//order 2(split)

        productsToOrder2.put(5, 40);//bamba
        // productsToOrder2.put(11, 20);//cafe
        //productsToOrder2.put(29, 5);//shoko

        HashMap <Integer,Integer> productsToOrder3=new HashMap<>();//fail
        productsToOrder3.put(11, 100); //cafe
       // productsToOrder3.put(29,30); //shoko

        ArrayList<HashMap<Integer, Integer>> shortage = new ArrayList<>();
        shortage.add(productsToOrder1);
        shortage.add(productsToOrder2);
        shortage.add(productsToOrder3);
        return shortage;
    }

    private void loadData() {
        //  supplier1   //
        ArrayList<DayOfWeek> deliveryDays1 = new ArrayList<>();
        deliveryDays1.add(DayOfWeek.MONDAY);
        deliveryDays1.add(DayOfWeek.FRIDAY);
        HashMap<Integer, SupplierProductService> supllyingProducts1 = new HashMap<>();
        HashMap<Integer, Double> discountPerAmount1 = new HashMap<>();
        discountPerAmount1.put(10, 15.0);
        supllyingProducts1.put(5, new SupplierProductService("Bamba", 5, 7, 6.9, 30, discountPerAmount1, "Osem", 120, 0.4));
        supllyingProducts1.put(29, new SupplierProductService("Shoko", 29, 11, 4.9, 20, discountPerAmount1, "Tnuva", 25, 0.3));
        supllyingProducts1.put(17, new SupplierProductService("Coffee", 17, 86, 12.5, 50, discountPerAmount1, "Elite", 90, 0.7));
        supllyingProducts1.put(68, new SupplierProductService("Nutella", 68, 5, 29.9, 45, discountPerAmount1, "Ferraro", 100, 1.0));
        ServiceAgreement agreement1 = new ServiceAgreement("Cash", true, deliveryDays1, supllyingProducts1, "By Days", 0);
        ArrayList<ServiceContact> contacts1 = new ArrayList<>();
        contacts1.add(new ServiceContact("Itamar Barami", "itamar@gmail.com", "052-3801919"));
        supplierService.addSupplier("Gal Halifa", "Beit Ezra", "123456", agreement1, contacts1);

        //  supplier2   //
        ArrayList<DayOfWeek> deliveryDays2 = new ArrayList<>();
        HashMap<Integer, SupplierProductService> supllyingProducts2 = new HashMap<>();
        HashMap<Integer, Double> discountPerAmount2 = new HashMap<>();
//        discountPerAmount2.put(10, 15.0);
//        discountPerAmount2.put(30, 20.0);
        supllyingProducts2.put(5, new SupplierProductService("Bamba", 5, 15, 7, 20, discountPerAmount2, "Osem", 120, 0.4));
        supllyingProducts2.put(17, new SupplierProductService("Coffee", 17, 100, 10, 40, discountPerAmount2, "Elite", 90, 0.3));
        supllyingProducts2.put(45, new SupplierProductService("Shampoo", 45, 9, 17.9, 100, discountPerAmount2, "Head and Shoulders", 180, 1.2));
        ServiceAgreement agreement2 = new ServiceAgreement("net 30 EOM", true, deliveryDays2, supllyingProducts2, "ByOrder", 3);
        ArrayList<ServiceContact> contacts2 = new ArrayList<>();
        contacts2.add(new ServiceContact("Miki daniarov", "miki@gmail.com", "054-2453536"));
        supplierService.addSupplier("Mor Shuker", "Rehovot", "205155", agreement2, contacts2);

        //  supplier3   //
        ArrayList<DayOfWeek> deliveryDays3 = new ArrayList<>();
        deliveryDays3.add(DayOfWeek.SUNDAY);
        deliveryDays3.add(DayOfWeek.MONDAY);
        HashMap<Integer, SupplierProductService> supllyingProducts3 = new HashMap<>();
        HashMap<Integer, Double> discountPerAmount3 = new HashMap<>();
        Pair<Integer, Double> totalDiscountPerAmount3 =new Pair<>(30,5.0);
        Pair<Double, Double> totalDiscountPerPrice3 =new Pair<>(200.0,20.0);
        discountPerAmount3.put(30, 20.0);
        supllyingProducts3.put(5, new SupplierProductService("Bamba", 5, 31, 5.9, 30, discountPerAmount3, "Osem", 120, 0.4));
        supllyingProducts3.put(17, new SupplierProductService("Coffee", 17, 99, 5, 80, discountPerAmount3, "Elite", 90, 0.7));
        supllyingProducts3.put(68, new SupplierProductService("Nutella", 68, 4, 39.9, 20, discountPerAmount3, "Ferraro", 100, 1.0));
        ServiceAgreement agreement3 = new ServiceAgreement("TransitToAccount", true, deliveryDays3, supllyingProducts3, totalDiscountPerAmount3, totalDiscountPerPrice3, "ByDays", 0);
        ArrayList<ServiceContact> contacts3 = new ArrayList<>();
        contacts3.add(new ServiceContact("Noa Aviv", "noa@gmail.com", "050-5838687"));
        supplierService.addSupplier("Itay Gershon", "Beit ezra", "121212", agreement3, contacts3);


        //  supplier4   //
        ArrayList<DayOfWeek> deliveryDays4 = new ArrayList<>();
        deliveryDays4.add(DayOfWeek.SUNDAY);
        deliveryDays4.add(DayOfWeek.WEDNESDAY);
        deliveryDays4.add(DayOfWeek.THURSDAY);
        HashMap<Integer, SupplierProductService> supllyingProducts4 = new HashMap<>();
        HashMap<Integer, Double> discountPerAmount4 = new HashMap<>();
        Pair<Integer, Double> totalDiscountPerAmount4 =new Pair<>(30,5.0);
        Pair<Double, Double> totalDiscountPerPrice4 =new Pair<>(200.0,20.0);
        discountPerAmount4.put(10, 20.0);
        supllyingProducts4.put(5, new SupplierProductService("Bamba", 5, 71, 4.9, 40, discountPerAmount4, "Osem", 120, 0.4));
        supllyingProducts4.put(17, new SupplierProductService("Coffee", 17, 24, 9.9, 35, discountPerAmount4, "Elite", 90, 0.7));
        supllyingProducts4.put(68, new SupplierProductService("Nutella", 68, 104, 23.9, 10, discountPerAmount4, "Ferraro", 100, 1.0));
        ServiceAgreement agreement4 = new ServiceAgreement("TransitToAccount", true, deliveryDays4, supllyingProducts4, totalDiscountPerAmount4, totalDiscountPerPrice4, "ByDays", 0);
        ArrayList<ServiceContact> contacts4 = new ArrayList<>();
        contacts3.add(new ServiceContact("Dan Weizmann", "dan@gmail.com", "050-5839494"));
        supplierService.addSupplier("Guy Biton", "Beer Sheva", "7891234", agreement4, contacts4);

    }


    private void supplierManagerCLI() {
        print("Please choose one of the following option:\n1. Add new Supplier\n2. Delete Supplier \n3. Edit Supplier's information \n4. Print suppliers \n5. Back");
        int action =0;
        try {
            action = Integer.parseInt(reader.nextLine());
        }
        catch (Exception e){
            print("Illegal Input");
            supplierManagerCLI();
        }
        switch (action) {
            case 1:
                addNewSupplier();
                break;
            case 2:
                deleteSupplier();
                break;
            case 3:
                editSupplier();
                break;
            case 4:
                printSuppliers();
            case 5:
                return;
            default:
                supplierManagerCLI();
        }
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
        Response res = supplierService.editItemCatalodNumber(supplierId, productId, newCatalogNumber);
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

    private void printOrders() {
        supplierService.printOrders();
    }

    private void createPeriodicOrder()
    {
        print("Please Enter Branch ID: ");
        int branchID = reader.nextInt();
        reader.nextLine();
        print("Please Enter Supplier ID: ");
        int supplierID = reader.nextInt();
        reader.nextLine();
        print("Please Choose The Periodic Order Day");
        print("1. Monday \n2. Tuesday \n3. Wednesday \n4. Thursday \n5. Friday \n6. Saturday \n7. Sunday");
        int day = reader.nextInt();
        reader.nextLine();
        while (day < 1 || day > 7)
        {
            print("Please enter a valid number : 1 to 7");
            day = reader.nextInt();
            reader.nextLine();
        }
        DayOfWeek fixedDay = DayOfWeek.of(day);
        boolean correct = false;
        HashMap<Integer, Integer> productsAndAmount = new HashMap<>();
        while(!correct) {
            print("Choose Products And Amounts According To The Format: ProductID:Amount");
            try {
                String[] arr = reader.nextLine().split("\\s*,\\s*");
                for (String s1 : arr) {
                    String[] val = s1.split(":");
                    int productID = Integer.parseInt(val[0]);
                    int amount = Integer.parseInt(val[1]);
                    productsAndAmount.put(productID, amount);
                }
                correct = true;
            } catch (Exception e) {
                System.out.println("Please Enter Only According To The Format!");
                productsAndAmount.clear();
            }
        }
        Response response = orderService.createPeriodicOrder(supplierID, branchID, fixedDay, productsAndAmount);
        if(response.errorOccurred()) System.out.println(response.getErrorMessage());
        else System.out.println("Periodic Order With The ID " + response.getSupplierId() + " Has Successfully Been Created");
    }

    private void updateProductsInOrder()
    {
        print("Please Enter Order ID: ");
        int orderID = reader.nextInt();
        reader.nextLine();
        boolean correct = false;
        HashMap<Integer, Integer> productsAndAmount = new HashMap<>();
        while (!correct)
        {
            print("Choose Products And Amounts According To The Format: ProductID:Amount");
            try {
                String[] arr = reader.nextLine().split("\\s*,\\s*");
                for (String s1 : arr) {
                    String[] val = s1.split(":");
                    int productID = Integer.parseInt(val[0]);
                    int amount = Integer.parseInt(val[1]);
                    productsAndAmount.put(productID, amount);
                }
                correct = true;
            }
            catch (Exception e)
            {
                System.out.println("Please Enter Only According To The Format!");
                productsAndAmount.clear();
            }
        }

        Response response = orderService.updateProductsInOrder(orderID, productsAndAmount);
        if(response.errorOccurred()) System.out.println(response.getErrorMessage());
        else System.out.println("Order With The ID " + response.getSupplierId() + " Has Successfully Been Updated");
    }

    private void removeProductsFromOrder()
    {
        print("Please Enter Order ID: ");
        int orderID = reader.nextInt();
        reader.nextLine();
        boolean correct = false;
        ArrayList<Integer> products = new ArrayList<>();
        while (!correct)
        {
            print("Choose Products To The Format: ProductID_1, ProductID_2, ProductID_3,...");
            try {
                String[] arr = reader.nextLine().split("\\s*,\\s*");
                for (String s1 : arr)
                    products.add(Integer.parseInt(s1));
                correct = true;
            }
            catch (Exception e) { System.out.println("Please Enter Only Product IDs!"); products.clear(); }
        }
        Response response = orderService.removeProductsFromOrder(orderID, products);
        if(response.errorOccurred()) System.out.println(response.getErrorMessage());
        else System.out.println("Order With The ID " + response.getSupplierId() + " Has Successfully Been Updated");
    }

    private void startDailyTask()
    {
        Timer timer = new Timer();
        // Schedule the task to execute every day at 10:00am
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 14);
        calendar.set(Calendar.MINUTE, 14);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        if (calendar.getTimeInMillis() < System.currentTimeMillis())
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        timer.scheduleAtFixedRate(orderService, calendar.getTime(), TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS));
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            timer.cancel();
            Database.disconnect();
        }));
    }
}
