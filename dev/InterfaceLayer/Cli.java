package InterfaceLayer;

import DataAccessLayer.Database;
import ServiceLayer.ServiceAgreement;
import ServiceLayer.ServiceContact;
import ServiceLayer.SupplierProductService;
import ServiceLayer.SupplierService;
import Utillity.Pair;
import Utillity.Response;

import java.sql.*;
import java.time.DayOfWeek;
import java.util.*;
//import ServiceLayer.MainService;
//import BusinessLayer.SuppliersBusiness.Supplier;



public class Cli {
    private Scanner reader;
    private SupplierService supplierService;

    private ServiceContact serviceContact;


    public Cli() {
        reader = new Scanner(System.in);
        supplierService = new SupplierService();
        serviceContact = new ServiceContact();
    }

//    private void loadData() {     // this function will load the data
//        service.loadData();
//    }

    public void print(String message){
        System.out.println(message);
    }


    public void start() throws SQLException {
        Connection connection = Database.connect();
        Database.createTables();
        int userInput = 0;

        print("Hello! and Welcome to Super Lee- supplier module, I am available to assist you with any requests you may have\nDo you want to upload existing data or start over?  \n1. Load Data\n2. New Data \n3. EXIT\n");
        int n = reader.nextInt();
        //reader.nextLine();
        if (n == 1) {
            loadData();
            ArrayList<HashMap<Integer, Integer>> shortage = enterShortage();
            boolean valid = false;
            while (!valid) {
                print("For scenario one press 1- create an order from one supplier (only one supplier can supply all the products)\n");
                print("For scenario two press 2- The order will be split into several orders according to different suppliers\n");
                print("For scenario three press 3- the order can't be created\n");
                print("Press 0- back to the menu");
                int choose = reader.nextInt();
                if (choose == 1) {
                    valid = true;
                    supplierService.createAnOrder(shortage.get(0));
                    printOrders();
                    choose = 0;
                } else if (choose == 2) {
                    valid = true;
                    supplierService.createAnOrder(shortage.get(1));
                    printOrders();
                    choose = 0;
                } else if (choose == 3) {
                    valid = true;
                    supplierService.createAnOrder(shortage.get(2));
                    printOrders();
                    choose = 0;
                }
                    else if(choose ==0){
                        break;
                    }
                 else {
                    print("Please choose a valid number (1,2,3)");
                }
            }
        }
        if (n == 3) {
            print("Hope you enjoyed, see you next time :)");
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            return;
        }
        //if the user pressed 2 we play the next section
        if (n == 2) {
            while (true) {
                    print("Which module are you interested in?  \n1. Supplier\n2. EXIT\n3. Back");
                    userInput = reader.nextInt();
                    reader.nextLine();
                    if (userInput < 1 || userInput > 2) {
                        print("Please choose one of the following options:\n1. Supplier\n2. EXIT\n3. Back");
                        userInput = reader.nextInt();
                        reader.nextLine();
                }
                if (userInput == 1) {
                    supplierManagerCLI();
                }
                if (userInput == 2) {
                    print("Hope you enjoyed, see you next time :)");
                    return;
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
        ArrayList<DayOfWeek> deliveryDays1 =new ArrayList<>();
        deliveryDays1.add(DayOfWeek.MONDAY);
        deliveryDays1.add(DayOfWeek.FRIDAY);
        HashMap <Integer, SupplierProductService> supllyingProducts1 = new HashMap<>();
        HashMap<Integer, Double> discountPerAmount1 =new HashMap<>();
        discountPerAmount1.put(20, 5.0);
        supllyingProducts1.put(5, new SupplierProductService("bamba" ,5, 7, 4.5, 10, discountPerAmount1));
        supllyingProducts1.put(29, new SupplierProductService("shoko" ,29, 11, 12, 20, discountPerAmount1));
        ServiceAgreement agreement1 = new ServiceAgreement("Cash", true, deliveryDays1, supllyingProducts1);
        ArrayList<ServiceContact> contacts1 =new ArrayList<>();
        contacts1.add(new ServiceContact("mor", "mor@gmail.com", "052-3801919"));
        supplierService.addSupplier("gal halifa", "beit ezra", "123456", agreement1, contacts1);

        //  supplier2   //
        ArrayList<DayOfWeek> deliveryDays2 =new ArrayList<>();
        HashMap <Integer, SupplierProductService> supllyingProducts2 = new HashMap<>();
        HashMap<Integer, Double> discountPerAmount2 =new HashMap<>();
        discountPerAmount2.put(10, 15.0);
        discountPerAmount2.put(30, 20.0);
        supllyingProducts2.put(5, new SupplierProductService("bamba" ,5, 15, 5, 20, discountPerAmount2));
        supllyingProducts2.put(11, new SupplierProductService("coffe" ,11, 9, 10, 50, discountPerAmount2));
        ServiceAgreement agreement2 = new ServiceAgreement("Cash", true, deliveryDays2, supllyingProducts2);
        ArrayList<ServiceContact> contacts2 =new ArrayList<>();
        contacts2.add(new ServiceContact("miki", "miki@gmail.com", "054-2453536" ));
        supplierService.addSupplier("mor", "rehovot", "205155", agreement2, contacts2);

        //  supplier3   //
        ArrayList<DayOfWeek> deliveryDays3 =new ArrayList<>();
        deliveryDays3.add(DayOfWeek.MONDAY);
        deliveryDays3.add(DayOfWeek.TUESDAY);
        HashMap <Integer, SupplierProductService> supllyingProducts3 = new HashMap<>();
        HashMap<Integer, Double> discountPerAmount3 =new HashMap<>();
        Pair<Integer, Double> totalDiscountPerAmount =new Pair<>(30,5.0);
        Pair<Double, Double> totalDiscountPerPrice =new Pair<>(200.0,20.0);
        discountPerAmount3.put(20, 5.0);
        supllyingProducts3.put(5, new SupplierProductService("bamba" ,5, 16, 8, 30, discountPerAmount3));
        supllyingProducts3.put(11, new SupplierProductService("coffee" ,11, 6, 5, 40, discountPerAmount3));
        ServiceAgreement agreement3 = new ServiceAgreement("Cash", true, deliveryDays3, supllyingProducts3 , totalDiscountPerAmount, totalDiscountPerPrice);
        ArrayList<ServiceContact> contacts3 =new ArrayList<>();
        contacts3.add(new ServiceContact("noa aviv", "noa@gmail.com", "050-5838687"));
        supplierService.addSupplier("itay", "beit ezra", "121212", agreement3, contacts3);




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
                case 1:
                    editSupplierPersonalDetails(id);
                    break;
                case 2:
                    editSupplierAgreement(id);
                    break;
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
        }
        ////userinput=3 , superLee supply
        else {
            selfSupply = false;
        }

        Response res = supplierService.editPaymentMethodAndDeliveryMethodAndDeliveryDays(id, selfSupply, paymentMethod  ,days);
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
            Response res = supplierService.addItemToAgreement(supplierID, newItem.getName(), newItem.getProductId(), newItem.getCatalogNumber(), newItem.getPrice(), newItem.getAmount(), newItem.getDiscountPerAmount());
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
            case 1:
                editItemCatalodNumber(supplierID, productId);
                break;
            case 2:
                editRemoveDiscount(supplierID, productId);
                break;
            case 3:
                editAddDiscount(supplierID, productId);
                break;

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
        int ammount = Integer.parseInt(val[0]);
        double discount = Double.parseDouble(val[1]);
        Response res = supplierService.removeDiscounts(supplierID, productId, ammount, discount);
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
            case 1:
                editContacts(id);
                break;
            case 2:
                editAddress(id);
                break;
            case 3:
                editBankAccount(id);
                break;
            case 4:
                editSupplierName(id);
                break;
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
        int action = Integer.parseInt(reader.nextLine());
        while (action < 1 || action > 4) {
            System.out.println("Please Enter a valid number (1 to 4)");
            action = Integer.parseInt(reader.nextLine());
        }
        switch (action) {
            case 1:
                addContactToSupplier(id);
                break;
            case 2:
                deleteContact(id);
                break;
            case 3:
                editContactEmail(id);
                break;
            case 4:
                editContactPhone(id);
                break;
        }
    }

    private void editContactPhone(int id) {
        String email=getValidEmail();
        //String name = reader.nextLine();
        print("Please enter the new phone number");
        String newPhone = reader.nextLine();
        while (!serviceContact.validatePhoneNumber(newPhone)) {
            print("Not a valid phone number, please try again");
            newPhone = reader.nextLine();
        }
        Response res = supplierService.editSupplierContacts(id, email, "", newPhone);
        if (res.errorOccurred())
            print(res.getErrorMessage());

        }


    private void editContactEmail(int id) {
        String email=getValidEmail();
        print("Please enter the new email ");
        String newEmail = reader.nextLine();
        while (!serviceContact.isValidEmail(email)) {
            print("Not a valid email, please try again");
            newEmail = reader.nextLine();
        }
        Response res = supplierService.editSupplierContacts(id, email, newEmail, "");
        if (res.errorOccurred())
           print(res.getErrorMessage());

    }


    private void deleteContact(int id) {
        String email=getValidEmail();
        Response res = supplierService.removeSupplierContact(id, email);
        if (res.errorOccurred())
            print(res.getErrorMessage());
        else{
            print("contact withe email: "+email+" deleted successfully from supplier with id: "+id);
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
        }
        ////userinput=3 , superLee supply
        else
            selfSupply = false;

        print("Would you like to add specific items  to the agreement? \n1. Yes\n2. No");
        int keepAdding = reader.nextInt();

        reader.nextLine();
        HashMap<Integer, SupplierProductService> items = new HashMap<>();
//        if (keepAdding == 1)
//            printAllProducts();
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
                return new ServiceAgreement(paymentMethod, selfSupply, days, items,amountPair,pricePair);

        }
        return new ServiceAgreement(paymentMethod, selfSupply, days, items);
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
        reader.nextLine();
        print("Ok, now please add Discounts according to the format:amount:Discount in percentages,amount:Discount in percentages,..");
        String[] arr = reader.nextLine().split("\\s*,\\s*");
        HashMap<Integer, Double> discounts = new HashMap<>();
        for(String s1 : arr) {
            String[] val = s1.split(":");
            int key = Integer.parseInt(val[0]);
            double value = Double.parseDouble(val[1]);
            discounts.put(key, value);}
        SupplierProductService newProduct = new SupplierProductService(name, productId, catalogNumber, price, amount, discounts);
        return newProduct;
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
//    private ArrayList<ServiceContact> createContacts() {
//        ArrayList<ServiceContact> contactList = new ArrayList<>();
//        int keepAdding = 1;
//        while (keepAdding == 1) {
//            print("Please enter a contact name");
//            String nameC = reader.nextLine();
//            print("Please enter " + nameC + "'s email");
//            String email = reader.nextLine();
//            while (!serviceContact.isValidEmail(email)) {
//                print("not a valid email. please enter a legal email address");
//                email = reader.nextLine();
//            }
//            print("Please enter " + nameC + "'s phone number");
//            String phone = reader.nextLine();
//            contactList.add(new ServiceContact(nameC, email, phone));
//            print("Would you like to add another contact? \n1. Yes\n2. No");
//            keepAdding = reader.nextInt();
//            reader.nextLine();
//        }
//        return contactList;
//    }

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


}
