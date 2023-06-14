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

//    public void loadDataSupplier() {
////        Product p1 = productsDao.addProduct("Milk 3%", "Tara", 500, 1, 2, 7);
////        productMinAmountDao.addNewProductToAllBranches(1);
////        Product p2 = productsDao.addProduct("Cottage 5%", "Tnova", 250, 1, 3, 8);
////        productMinAmountDao.addNewProductToAllBranches(2);
////        Product p3 = productsDao.addProduct("White Onion", "VegAndFruits", 20, 10, 11, 14);
////        productMinAmountDao.addNewProductToAllBranches(3);
////        Product p4 = productsDao.addProduct("Green Onion", "VegAndFruits", 10, 10, 11, 13);
////        productMinAmountDao.addNewProductToAllBranches(4);
////        Product p5 = productsDao.addProduct("Red Potato", "VegAndFruits", 10, 10, 12, 15);
////        productMinAmountDao.addNewProductToAllBranches(5);
////        Product p6 = productsDao.addProduct("Red Apple", "VegAndFruits", 10, 16, 17, 18);
////        productMinAmountDao.addNewProductToAllBranches(6);
////        Product p7 = productsDao.addProduct("Green Apple", "VegAndFruits", 10, 16, 17, 18);
////        productMinAmountDao.addNewProductToAllBranches(7);
////        Product p8 = productsDao.addProduct("Cottage 9%", "Tara", 250, 1, 3, 9);
////        productMinAmountDao.addNewProductToAllBranches(8);
////        Product p9 =  productsDao.addProduct("Milk 9%", "Tnova", 500, 1, 2, 9);
////        productMinAmountDao.addNewProductToAllBranches(9);
////        Product p10 = productsDao.addProduct("Milk 1%", "Tnova", 500, 1, 2, 6);
////        productMinAmountDao.addNewProductToAllBranches(10);
////        Product p11 = productsDao.addProduct("Milk 5%", "Tnova", 500, 1, 2, 8);
////        productMinAmountDao.addNewProductToAllBranches(11);
////        Product p12 = productsDao.addProduct("Cottage 3%", "Tara", 250, 1, 3, 7);
////        productMinAmountDao.addNewProductToAllBranches(12);
////        Product p13 = productsDao.addProduct("Cottage 1%", "Tara", 250, 1, 3, 6);
////        productMinAmountDao.addNewProductToAllBranches(13);
////        Product p14 = productsDao.addProduct("Cream Cheese 3%", "Tnova", 350, 1, 4, 7);
////        productMinAmountDao.addNewProductToAllBranches(14);
////        Product p15 = productsDao.addProduct("Cream Cheese 1%", "Tnova", 350, 1, 4, 6);
////        productMinAmountDao.addNewProductToAllBranches(15);
////        Product p16 = productsDao.addProduct("Cream Cheese 5%", "Tnova", 350, 1, 4, 8);
////        productMinAmountDao.addNewProductToAllBranches(16);
////        Product p17 = productsDao.addProduct("Milk 3%", "Tnova", 500, 1, 2, 7);
////        productMinAmountDao.addNewProductToAllBranches(17);
////        Product p18 = productsDao.addProduct("Coca Cola Zero 0.5 Liter", "CocaCola", 500, 22, 24, 23);
////        productMinAmountDao.addNewProductToAllBranches(18);
////        Product p19 = productsDao.addProduct("Coca Cola Zero 1 Liter", "CocaCola", 1000, 22, 24, 25);
////        productMinAmountDao.addNewProductToAllBranches(19);
////        Product p20 = productsDao.addProduct("Coca Cola Zero 1.5 Liter", "CocaCola", 1500, 22, 24, 26);
////        productMinAmountDao.addNewProductToAllBranches(20);
////        Product p21 = productsDao.addProduct("Banana And Strawberry 1 Liter", "Spring", 1000, 22, 27, 25);
////        productMinAmountDao.addNewProductToAllBranches(21);
////        Product p22 = productsDao.addProduct("Orange juice 1 Liter", "Spring", 1000, 22, 27, 25);
////        productMinAmountDao.addNewProductToAllBranches(22);
//
//        //  supplier1   //
//        ArrayList<DayOfWeek> deliveryDays1 = new ArrayList<>();
//        deliveryDays1.add(DayOfWeek.MONDAY);
//        deliveryDays1.add(DayOfWeek.FRIDAY);
//        HashMap<Integer, SupplierProductService> supllyingProducts1 = new HashMap<>();
//        HashMap<Integer, Double> discountPerAmount1 = new HashMap<>();
//        discountPerAmount1.put(10, 15.0);
//        supllyingProducts1.put(1, new SupplierProductService("Milk 3%", 1, 7, 6.9, 100, discountPerAmount1, "Tara", 14, 500.0));
//        supllyingProducts1.put(2, new SupplierProductService("Cottage 5%", 2, 8, 7.9, 150, discountPerAmount1, "Tnova", 14, 250.0));
//        supllyingProducts1.put(3, new SupplierProductService("White Onion", 3, 14, 12.9, 50, discountPerAmount1, "VegAndFruits", 14, 20.0));
//        supllyingProducts1.put(4, new SupplierProductService("Green Onion", 4, 13, 11.9, 50, discountPerAmount1, "VegAndFruits", 14, 10.0));
//        supllyingProducts1.put(5, new SupplierProductService("Red Potato", 5, 15, 14.9, 50, discountPerAmount1, "VegAndFruits", 14, 10.0));
//        supllyingProducts1.put(6, new SupplierProductService("Red Apple", 6, 18, 16.9, 50, discountPerAmount1, "VegAndFruits", 14, 10.0));
//        supllyingProducts1.put(7, new SupplierProductService("Green Apple", 7, 18, 16.9, 50, discountPerAmount1, "VegAndFruits", 14, 10.0));
//        supllyingProducts1.put(8, new SupplierProductService("Cottage 9%", 8, 9, 8.9, 150, discountPerAmount1, "Tara", 14, 250.0));
//        supllyingProducts1.put(9, new SupplierProductService("Milk 9%", 9, 9, 8.9, 100, discountPerAmount1, "Tnova", 14, 500.0));
//        supllyingProducts1.put(10, new SupplierProductService("Milk 1%", 10, 6, 5.9, 100, discountPerAmount1, "Tnova", 14, 500.0));
//        ServiceAgreement agreement1 = new ServiceAgreement("Cash", true, deliveryDays1, supllyingProducts1, "By Days", 0);
//        ArrayList<ServiceContact> contacts1 = new ArrayList<>();
//        contacts1.add(new ServiceContact("Itamar Barami", "itamar@gmail.com", "052-3801919"));
//        supplierService.addSupplier("Gal Halifa", "Beit Ezra", "123456", agreement1, contacts1);
//
//        //  supplier2   //
//        ArrayList<DayOfWeek> deliveryDays2 = new ArrayList<>();
//        HashMap<Integer, SupplierProductService> supllyingProducts2 = new HashMap<>();
//        HashMap<Integer, Double> discountPerAmount2 = new HashMap<>();
////        discountPerAmount2.put(10, 15.0);
////        discountPerAmount2.put(30, 20.0);
//        supllyingProducts2.put(7, new SupplierProductService("Green Apple", 7, 18, 16.9, 50, discountPerAmount2, "VegAndFruits", 14, 10.0));
//        supllyingProducts2.put(8, new SupplierProductService("Cottage 9%", 8, 9, 8.9, 150, discountPerAmount2, "Tara", 14, 250.0));
//        supllyingProducts2.put(9, new SupplierProductService("Milk 9%", 9, 9, 8.9, 100, discountPerAmount2, "Tnova", 14, 500.0));
//        supllyingProducts2.put(10, new SupplierProductService("Milk 1%", 10, 6, 5.9, 100, discountPerAmount2, "Tnova", 14, 500.0));
//        supllyingProducts2.put(11, new SupplierProductService("Milk 5%", 11, 8, 7.9, 100, discountPerAmount2, "Tnova", 14, 500.0));
//        supllyingProducts2.put(12, new SupplierProductService("Cottage 3%", 12, 7, 6.9, 150, discountPerAmount2, "Tara", 14, 250.0));
//        supllyingProducts2.put(13, new SupplierProductService("Cottage 1%", 13, 6, 5.9, 150, discountPerAmount2, "Tara", 14, 250.0));
//        supllyingProducts2.put(14, new SupplierProductService("Cream Cheese 3%", 14, 7, 6.9, 100, discountPerAmount2, "Tnova", 14, 350.0));
//        supllyingProducts2.put(15, new SupplierProductService("Cream Cheese 1%", 1, 4, 5.9, 100, discountPerAmount2, "Tnova", 14, 350.0));
//        ServiceAgreement agreement2 = new ServiceAgreement("net 30 EOM", true, deliveryDays2, supllyingProducts2, "ByOrder", 3);
//        ArrayList<ServiceContact> contacts2 = new ArrayList<>();
//        contacts2.add(new ServiceContact("Miki daniarov", "miki@gmail.com", "054-2453536"));
//        supplierService.addSupplier("Mor Shuker", "Rehovot", "205155", agreement2, contacts2);
//
//        //  supplier3   //
//        ArrayList<DayOfWeek> deliveryDays3 = new ArrayList<>();
//        deliveryDays3.add(DayOfWeek.SUNDAY);
//        deliveryDays3.add(DayOfWeek.MONDAY);
//        HashMap<Integer, SupplierProductService> supllyingProducts3 = new HashMap<>();
//        HashMap<Integer, Double> discountPerAmount3 = new HashMap<>();
//        Pair<Integer, Double> totalDiscountPerAmount3 =new Pair<>(30,5.0);
//        Pair<Double, Double> totalDiscountPerPrice3 =new Pair<>(200.0,20.0);
//        discountPerAmount3.put(30, 20.0);
//        supllyingProducts3.put(11, new SupplierProductService("Milk 5%", 11, 8, 7.9, 100, discountPerAmount3, "Tnova", 14, 500.0));
//        supllyingProducts3.put(12, new SupplierProductService("Cottage 3%", 12, 7, 6.9, 150, discountPerAmount3, "Tara", 14, 250.0));
//        supllyingProducts3.put(13, new SupplierProductService("Cottage 1%", 13, 6, 5.9, 150, discountPerAmount3, "Tara", 14, 250.0));
//        supllyingProducts3.put(14, new SupplierProductService("Cream Cheese 3%", 14, 7, 6.9, 100, discountPerAmount3, "Tnova", 14, 350.0));
//        supllyingProducts3.put(15, new SupplierProductService("Cream Cheese 1%", 1, 4, 5.9, 100, discountPerAmount3, "Tnova", 14, 350.0));
//        supllyingProducts3.put(16, new SupplierProductService("Cream Cheese 5%", 1, 4, 7.9, 100, discountPerAmount3, "Tnova", 14, 350.0));
//        supllyingProducts3.put(17, new SupplierProductService("Milk 3%", 1, 2, 7, 100, discountPerAmount3, "Tnova", 14, 500.0));
//        supllyingProducts3.put(18, new SupplierProductService("Coca Cola Zero 0.5 Liter", 22, 24, 23, 100, discountPerAmount3, "CocaCola", 14, 500.0));
//        ServiceAgreement agreement3 = new ServiceAgreement("TransitToAccount", true, deliveryDays3, supllyingProducts3, totalDiscountPerAmount3, totalDiscountPerPrice3, "ByDays", 0);
//        ArrayList<ServiceContact> contacts3 = new ArrayList<>();
//        contacts3.add(new ServiceContact("Noa Aviv", "noa@gmail.com", "050-5838687"));
//        supplierService.addSupplier("Itay Gershon", "Beit ezra", "121212", agreement3, contacts3);
//
//
//        //  supplier4   //
//        ArrayList<DayOfWeek> deliveryDays4 = new ArrayList<>();
//        deliveryDays4.add(DayOfWeek.SUNDAY);
//        deliveryDays4.add(DayOfWeek.WEDNESDAY);
//        deliveryDays4.add(DayOfWeek.THURSDAY);
//        HashMap<Integer, SupplierProductService> supllyingProducts4 = new HashMap<>();
//        HashMap<Integer, Double> discountPerAmount4 = new HashMap<>();
//        Pair<Integer, Double> totalDiscountPerAmount4 =new Pair<>(30,5.0);
//        Pair<Double, Double> totalDiscountPerPrice4 =new Pair<>(200.0,20.0);
//        discountPerAmount4.put(10, 20.0);
//        supllyingProducts4.put(15, new SupplierProductService("Cream Cheese 1%", 1, 4, 5.9, 100, discountPerAmount4, "Tnova", 14, 350.0));
//        supllyingProducts4.put(16, new SupplierProductService("Cream Cheese 5%", 1, 4, 7.9, 100, discountPerAmount4, "Tnova", 14, 350.0));
//        supllyingProducts4.put(17, new SupplierProductService("Milk 3%", 1, 2, 7, 100, discountPerAmount4, "Tnova", 14, 500.0));
//        supllyingProducts4.put(18, new SupplierProductService("Coca Cola Zero 0.5 Liter", 22, 24, 23, 100, discountPerAmount4, "CocaCola", 14, 500.0));
//        supllyingProducts4.put(19, new SupplierProductService("Coca Cola Zero 1 Liter", 22, 24, 25, 100, discountPerAmount4, "CocaCola", 14, 1000.0));
//        supllyingProducts4.put(20, new SupplierProductService("Coca Cola Zero 1.5 Liter", 22, 24, 26, 100, discountPerAmount4, "CocaCola", 14, 1500.0));
//        supllyingProducts4.put(21, new SupplierProductService("Banana And Strawberry 1 Liter", 22, 27, 25, 100, discountPerAmount4, "Spring", 14, 1000.0));
//        supllyingProducts4.put(22, new SupplierProductService("Orange juice 1 Liter", 22, 27, 25, 100, discountPerAmount4, "Spring", 14, 1000.0));
//        ServiceAgreement agreement4 = new ServiceAgreement("TransitToAccount", true, deliveryDays4, supllyingProducts4, totalDiscountPerAmount4, totalDiscountPerPrice4, "ByDays", 0);
//        ArrayList<ServiceContact> contacts4 = new ArrayList<>();
//        contacts4.add(new ServiceContact("Dan Weizmann", "dan@gmail.com", "050-5839494"));
//        supplierService.addSupplier("Guy Biton", "Beer Sheva", "7891234", agreement4, contacts4);
//    }


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
                    "6. Back To Main Menu");
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
                case 6 -> { return; }
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
