package InterfaceLayer;

import BusinessLayer.InventoryBusinessLayer.*;
import DataAccessLayer.InventoryDataAccessLayer.*;
import InterfaceLayer.CLI.StorekeeperCLI;
import InterfaceLayer.CLI.StoreManagerCLI;
import InterfaceLayer.CLI.SupplierManagerCLI;
import InterfaceLayer.GUI.StoreKeeperGUI;
import InterfaceLayer.GUI.SupplierManagerGUI;
import ServiceLayer.SupplierServiceLayer.*;
import Utillity.Pair;

import javax.swing.*;
import java.io.File;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main {

    public static void main(String[] args) throws SQLException {
        switch (args[0].toLowerCase())
        {
            case "cli" -> workersCLI(args[1]);
            case "gui" -> workersGUI(args[1]);
            default -> System.out.printf("Argument 1 is invalid, given argument: %s should be: cli / gui", args[1]);
        }
//        System.exit(0);
    }

    private static void workersGUI(String worker) throws SQLException {
        int choice = JOptionPane.showConfirmDialog(null, "Do you want to load data?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION)
        {
            File file = new File("res/superlee.db");
            if(!file.exists()) {
                MainController mainController = new MainController();
                OrderService orderService = new OrderService();
                LoadDataInventory(mainController);
                loadDataSupplier();
                mainController.getItemsDao().checkExpiredItemsInAllBranches();
                List<Branch> allBranches = mainController.getBranchesDao().getAllBranches();
                if (allBranches.size() > 0) {
                    mainController.getItemsDao().checkAllOrdersForToday(orderService, allBranches);
                }
                for (Branch branch : allBranches) {
                    mainController.getItemsDao().fromStorageToStore(branch);
                }
            }
            else
                JOptionPane.showMessageDialog(null,"You already have loaded data in your system", "No branches error", JOptionPane.ERROR_MESSAGE);

        }
        switch (worker.toLowerCase()) {
            case "storekeeper" -> {
                StoreKeeperGUI storekeeperGUI = new StoreKeeperGUI();
                storekeeperGUI.setVisible(true);
            }
            case "suppliermanager" -> {
                SupplierManagerGUI supplierManagerGUI = new SupplierManagerGUI();
                supplierManagerGUI.setVisible(true);
//                supplierManagerGUI.setVisible(true);
            }
//            case "storemanager" -> {
//                StoreManagerGUI storeManagerGUI = new StoreManagerGUI();
//                storeManagerGUI.Start();
//            }
            default -> System.out.printf("Argument 2 is invalid, given argument: %s should be: storekeeper / suppliermanager / storemanager", worker);
        }
    }

    private static void workersCLI(String worker) throws SQLException {
        int choice = JOptionPane.showConfirmDialog(null, "Do you want to load data?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION)
        {
            File file = new File("res/superlee.db");
            if(!file.exists()) {
                System.out.println("Initializing the information in the system... ");
                MainController mainController = new MainController();
                OrderService orderService = new OrderService();
                LoadDataInventory(mainController);
                loadDataSupplier();
                mainController.getItemsDao().checkExpiredItemsInAllBranches();
                List<Branch> allBranches = mainController.getBranchesDao().getAllBranches();
                if (allBranches.size() > 0) {
                    mainController.getItemsDao().checkAllOrdersForToday(orderService, allBranches);
                }
                for (Branch branch : allBranches) {
                    mainController.getItemsDao().fromStorageToStore(branch);
                }
            }
            else System.out.println("You already have loaded data in your system");
        }
        else
            System.out.println("Initializes the system without information... ");
        switch (worker.toLowerCase())
        {
            case "storekeeper" ->
            {
                StorekeeperCLI storekeeperCLI = new StorekeeperCLI();
                storekeeperCLI.Start();
            }
            case "suppliermanager" ->
            {
                SupplierManagerCLI supplierManagerCLI = new SupplierManagerCLI();
                supplierManagerCLI.Start();
            }
            case "storemanager" ->
            {
                StoreManagerCLI storeManagerCLI = new StoreManagerCLI();
                storeManagerCLI.Start();
            }
            default -> System.out.printf("Argument 2 is invalid, given argument: %s should be: storekeeper / suppliermanager / storemanager", worker);
        }
    }
    public static void loadDataSupplier() {
//        Product p1 = productsDao.addProduct("Milk 3%", "Tara", 500, 1, 2, 7);
//        productMinAmountDao.addNewProductToAllBranches(1);
//        Product p2 = productsDao.addProduct("Cottage 5%", "Tnova", 250, 1, 3, 8);
//        productMinAmountDao.addNewProductToAllBranches(2);
//        Product p3 = productsDao.addProduct("White Onion", "VegAndFruits", 20, 10, 11, 14);
//        productMinAmountDao.addNewProductToAllBranches(3);
//        Product p4 = productsDao.addProduct("Green Onion", "VegAndFruits", 10, 10, 11, 13);
//        productMinAmountDao.addNewProductToAllBranches(4);
//        Product p5 = productsDao.addProduct("Red Potato", "VegAndFruits", 10, 10, 12, 15);
//        productMinAmountDao.addNewProductToAllBranches(5);
//        Product p6 = productsDao.addProduct("Red Apple", "VegAndFruits", 10, 16, 17, 18);
//        productMinAmountDao.addNewProductToAllBranches(6);
//        Product p7 = productsDao.addProduct("Green Apple", "VegAndFruits", 10, 16, 17, 18);
//        productMinAmountDao.addNewProductToAllBranches(7);
//        Product p8 = productsDao.addProduct("Cottage 9%", "Tara", 250, 1, 3, 9);
//        productMinAmountDao.addNewProductToAllBranches(8);
//        Product p9 =  productsDao.addProduct("Milk 9%", "Tnova", 500, 1, 2, 9);
//        productMinAmountDao.addNewProductToAllBranches(9);
//        Product p10 = productsDao.addProduct("Milk 1%", "Tnova", 500, 1, 2, 6);
//        productMinAmountDao.addNewProductToAllBranches(10);
//        Product p11 = productsDao.addProduct("Milk 5%", "Tnova", 500, 1, 2, 8);
//        productMinAmountDao.addNewProductToAllBranches(11);
//        Product p12 = productsDao.addProduct("Cottage 3%", "Tara", 250, 1, 3, 7);
//        productMinAmountDao.addNewProductToAllBranches(12);
//        Product p13 = productsDao.addProduct("Cottage 1%", "Tara", 250, 1, 3, 6);
//        productMinAmountDao.addNewProductToAllBranches(13);
//        Product p14 = productsDao.addProduct("Cream Cheese 3%", "Tnova", 350, 1, 4, 7);
//        productMinAmountDao.addNewProductToAllBranches(14);
//        Product p15 = productsDao.addProduct("Cream Cheese 1%", "Tnova", 350, 1, 4, 6);
//        productMinAmountDao.addNewProductToAllBranches(15);
//        Product p16 = productsDao.addProduct("Cream Cheese 5%", "Tnova", 350, 1, 4, 8);
//        productMinAmountDao.addNewProductToAllBranches(16);
//        Product p17 = productsDao.addProduct("Milk 3%", "Tnova", 500, 1, 2, 7);
//        productMinAmountDao.addNewProductToAllBranches(17);
//        Product p18 = productsDao.addProduct("Coca Cola Zero 0.5 Liter", "CocaCola", 500, 22, 24, 23);
//        productMinAmountDao.addNewProductToAllBranches(18);
//        Product p19 = productsDao.addProduct("Coca Cola Zero 1 Liter", "CocaCola", 1000, 22, 24, 25);
//        productMinAmountDao.addNewProductToAllBranches(19);
//        Product p20 = productsDao.addProduct("Coca Cola Zero 1.5 Liter", "CocaCola", 1500, 22, 24, 26);
//        productMinAmountDao.addNewProductToAllBranches(20);
//        Product p21 = productsDao.addProduct("Banana And Strawberry 1 Liter", "Spring", 1000, 22, 27, 25);
//        productMinAmountDao.addNewProductToAllBranches(21);
//        Product p22 = productsDao.addProduct("Orange juice 1 Liter", "Spring", 1000, 22, 27, 25);
//        productMinAmountDao.addNewProductToAllBranches(22);

        SupplierService supplierService = new SupplierService();
        //  supplier1   //
        ArrayList<DayOfWeek> deliveryDays1 = new ArrayList<>();
        deliveryDays1.add(DayOfWeek.MONDAY);
        deliveryDays1.add(DayOfWeek.FRIDAY);
        HashMap<Integer, SupplierProductService> supllyingProducts1 = new HashMap<>();
        HashMap<Integer, Double> discountPerAmount1 = new HashMap<>();
        discountPerAmount1.put(10, 15.0);
        supllyingProducts1.put(1, new SupplierProductService("Milk 3%", 1, 7, 6.9, 100, discountPerAmount1, "Tara", 14, 500.0));
        supllyingProducts1.put(2, new SupplierProductService("Cottage 5%", 2, 8, 7.9, 150, discountPerAmount1, "Tnova", 14, 250.0));
        supllyingProducts1.put(3, new SupplierProductService("White Onion", 3, 14, 12.9, 50, discountPerAmount1, "VegAndFruits", 14, 20.0));
        supllyingProducts1.put(4, new SupplierProductService("Green Onion", 4, 13, 11.9, 50, discountPerAmount1, "VegAndFruits", 14, 10.0));
        supllyingProducts1.put(5, new SupplierProductService("Red Potato", 5, 15, 14.9, 50, discountPerAmount1, "VegAndFruits", 14, 10.0));
        supllyingProducts1.put(6, new SupplierProductService("Red Apple", 6, 18, 16.9, 50, discountPerAmount1, "VegAndFruits", 14, 10.0));
        supllyingProducts1.put(7, new SupplierProductService("Green Apple", 7, 18, 16.9, 50, discountPerAmount1, "VegAndFruits", 14, 10.0));
        supllyingProducts1.put(8, new SupplierProductService("Cottage 9%", 8, 9, 8.9, 150, discountPerAmount1, "Tara", 14, 250.0));
        supllyingProducts1.put(9, new SupplierProductService("Milk 9%", 9, 9, 8.9, 100, discountPerAmount1, "Tnova", 14, 500.0));
        supllyingProducts1.put(10, new SupplierProductService("Milk 1%", 10, 6, 5.9, 100, discountPerAmount1, "Tnova", 14, 500.0));
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
        supllyingProducts2.put(7, new SupplierProductService("Green Apple", 7, 18, 16.9, 50, discountPerAmount2, "VegAndFruits", 14, 10.0));
        supllyingProducts2.put(8, new SupplierProductService("Cottage 9%", 8, 9, 8.9, 150, discountPerAmount2, "Tara", 14, 250.0));
        supllyingProducts2.put(9, new SupplierProductService("Milk 9%", 9, 9, 8.9, 100, discountPerAmount2, "Tnova", 14, 500.0));
        supllyingProducts2.put(10, new SupplierProductService("Milk 1%", 10, 6, 5.9, 100, discountPerAmount2, "Tnova", 14, 500.0));
        supllyingProducts2.put(11, new SupplierProductService("Milk 5%", 11, 8, 7.9, 100, discountPerAmount2, "Tnova", 14, 500.0));
        supllyingProducts2.put(12, new SupplierProductService("Cottage 3%", 12, 7, 6.9, 150, discountPerAmount2, "Tara", 14, 250.0));
        supllyingProducts2.put(13, new SupplierProductService("Cottage 1%", 13, 6, 5.9, 150, discountPerAmount2, "Tara", 14, 250.0));
        supllyingProducts2.put(14, new SupplierProductService("Cream Cheese 3%", 14, 7, 6.9, 100, discountPerAmount2, "Tnova", 14, 350.0));
        supllyingProducts2.put(15, new SupplierProductService("Cream Cheese 1%", 1, 4, 5.9, 100, discountPerAmount2, "Tnova", 14, 350.0));
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
        supllyingProducts3.put(11, new SupplierProductService("Milk 5%", 11, 8, 7.9, 100, discountPerAmount3, "Tnova", 14, 500.0));
        supllyingProducts3.put(12, new SupplierProductService("Cottage 3%", 12, 7, 6.9, 150, discountPerAmount3, "Tara", 14, 250.0));
        supllyingProducts3.put(13, new SupplierProductService("Cottage 1%", 13, 6, 5.9, 150, discountPerAmount3, "Tara", 14, 250.0));
        supllyingProducts3.put(14, new SupplierProductService("Cream Cheese 3%", 14, 7, 6.9, 100, discountPerAmount3, "Tnova", 14, 350.0));
        supllyingProducts3.put(15, new SupplierProductService("Cream Cheese 1%", 1, 4, 5.9, 100, discountPerAmount3, "Tnova", 14, 350.0));
        supllyingProducts3.put(16, new SupplierProductService("Cream Cheese 5%", 1, 4, 7.9, 100, discountPerAmount3, "Tnova", 14, 350.0));
        supllyingProducts3.put(17, new SupplierProductService("Milk 3%", 1, 2, 7, 100, discountPerAmount3, "Tnova", 14, 500.0));
        supllyingProducts3.put(18, new SupplierProductService("Coca Cola Zero 0.5 Liter", 22, 24, 23, 100, discountPerAmount3, "CocaCola", 14, 500.0));
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
        supllyingProducts4.put(15, new SupplierProductService("Cream Cheese 1%", 1, 4, 5.9, 100, discountPerAmount4, "Tnova", 14, 350.0));
        supllyingProducts4.put(16, new SupplierProductService("Cream Cheese 5%", 1, 4, 7.9, 100, discountPerAmount4, "Tnova", 14, 350.0));
        supllyingProducts4.put(17, new SupplierProductService("Milk 3%", 1, 2, 7, 100, discountPerAmount4, "Tnova", 14, 500.0));
        supllyingProducts4.put(18, new SupplierProductService("Coca Cola Zero 0.5 Liter", 22, 24, 23, 100, discountPerAmount4, "CocaCola", 14, 500.0));
        supllyingProducts4.put(19, new SupplierProductService("Coca Cola Zero 1 Liter", 22, 24, 25, 100, discountPerAmount4, "CocaCola", 14, 1000.0));
        supllyingProducts4.put(20, new SupplierProductService("Coca Cola Zero 1.5 Liter", 22, 24, 26, 100, discountPerAmount4, "CocaCola", 14, 1500.0));
        supllyingProducts4.put(21, new SupplierProductService("Banana And Strawberry 1 Liter", 22, 27, 25, 100, discountPerAmount4, "Spring", 14, 1000.0));
        supllyingProducts4.put(22, new SupplierProductService("Orange juice 1 Liter", 22, 27, 25, 100, discountPerAmount4, "Spring", 14, 1000.0));
        ServiceAgreement agreement4 = new ServiceAgreement("TransitToAccount", true, deliveryDays4, supllyingProducts4, totalDiscountPerAmount4, totalDiscountPerPrice4, "ByDays", 0);
        ArrayList<ServiceContact> contacts4 = new ArrayList<>();
        contacts4.add(new ServiceContact("Dan Weizmann", "dan@gmail.com", "050-5839494"));
        supplierService.addSupplier("Guy Biton", "Beer Sheva", "7891234", agreement4, contacts4);
    }

    public static void LoadDataInventory(MainController mainController) throws SQLException
    {

        ProductsDao productsDao = mainController.getProductsDao();
        ItemsDao itemsDao = mainController.getItemsDao();
        BranchesDao branchesDao = mainController.getBranchesDao();
        CategoryDao categoryDao = mainController.getCategoryDao();
        DiscountsDao discountsDao = mainController.getDiscountsDao();
        ProductMinAmountDao productMinAmountDao = mainController.getProductMinAmountDao();
// Data Base From Nothing
//==================================================
//Branches
        Branch b1 = branchesDao.addBranch("SuperLi Beer Sheva");
        Branch b2 = branchesDao.addBranch("SuperLi Tel Aviv");
        Branch b3 = branchesDao.addBranch("SuperLi Jerusalem");
        Branch b4 = branchesDao.addBranch("SuperLi Herzliya");
        Branch b5 = branchesDao.addBranch("SuperLi Eilat");
// Categories
        Category c1 =categoryDao.addCategory("Dairy products");
        Category c2 =categoryDao.addCategory("Milk");
        Category c3 =categoryDao.addCategory("Cottage");
        Category c4 =categoryDao.addCategory("Cream Cheese");
        Category c5 =categoryDao.addCategory("Yellow Cheese");
        Category c6 =categoryDao.addCategory("1% fat");
        Category c7 =categoryDao.addCategory("3% fat");
        Category c8 =categoryDao.addCategory("5% fat");
        Category c9 =categoryDao.addCategory("9% fat");
        Category c10 =categoryDao.addCategory("Vegetables");
        Category c11 =categoryDao.addCategory("Onions");
        Category c12 =categoryDao.addCategory("Potatoes");
        Category c13 =categoryDao.addCategory("Green Onions");
        Category c14 =categoryDao.addCategory("White Onions");
        Category c15 =categoryDao.addCategory("Red Potatoes");
        Category c16 =categoryDao.addCategory("Fruits");
        Category c17 =categoryDao.addCategory("Apples");
        Category c18 =categoryDao.addCategory("Red Apples");
        Category c19 =categoryDao.addCategory("Green Apples");
        Category c20 =categoryDao.addCategory("Citrus Fruits");
        Category c21 =categoryDao.addCategory("Oranges");
        Category c22 =categoryDao.addCategory("Sweet Drinks");
        Category c23 =categoryDao.addCategory("0.5 Liters");
        Category c24 =categoryDao.addCategory("Sodas");
        Category c25 =categoryDao.addCategory("1 Liters");
        Category c26 =categoryDao.addCategory("1.5 Liters");
        Category c27 =categoryDao.addCategory("Soft Drinks");

// Products
        Product p1 = productsDao.addProduct("Milk 3%", "Tara", 500, 1, 2, 7);
        productMinAmountDao.addNewProductToAllBranches(1);
        Product p2 = productsDao.addProduct("Cottage 5%", "Tnova", 250, 1, 3, 8);
        productMinAmountDao.addNewProductToAllBranches(2);
        Product p3 = productsDao.addProduct("White Onion", "VegAndFruits", 20, 10, 11, 14);
        productMinAmountDao.addNewProductToAllBranches(3);
        Product p4 = productsDao.addProduct("Green Onion", "VegAndFruits", 10, 10, 11, 13);
        productMinAmountDao.addNewProductToAllBranches(4);
        Product p5 = productsDao.addProduct("Red Potato", "VegAndFruits", 10, 10, 12, 15);
        productMinAmountDao.addNewProductToAllBranches(5);
        Product p6 = productsDao.addProduct("Red Apple", "VegAndFruits", 10, 16, 17, 18);
        productMinAmountDao.addNewProductToAllBranches(6);
        Product p7 = productsDao.addProduct("Green Apple", "VegAndFruits", 10, 16, 17, 18);
        productMinAmountDao.addNewProductToAllBranches(7);
        Product p8 = productsDao.addProduct("Cottage 9%", "Tara", 250, 1, 3, 9);
        productMinAmountDao.addNewProductToAllBranches(8);
        Product p9 =  productsDao.addProduct("Milk 9%", "Tnova", 500, 1, 2, 9);
        productMinAmountDao.addNewProductToAllBranches(9);
        Product p10 = productsDao.addProduct("Milk 1%", "Tnova", 500, 1, 2, 6);
        productMinAmountDao.addNewProductToAllBranches(10);
        Product p11 = productsDao.addProduct("Milk 5%", "Tnova", 500, 1, 2, 8);
        productMinAmountDao.addNewProductToAllBranches(11);
        Product p12 = productsDao.addProduct("Cottage 3%", "Tara", 250, 1, 3, 7);
        productMinAmountDao.addNewProductToAllBranches(12);
        Product p13 = productsDao.addProduct("Cottage 1%", "Tara", 250, 1, 3, 6);
        productMinAmountDao.addNewProductToAllBranches(13);
        Product p14 = productsDao.addProduct("Cream Cheese 3%", "Tnova", 350, 1, 4, 7);
        productMinAmountDao.addNewProductToAllBranches(14);
        Product p15 = productsDao.addProduct("Cream Cheese 1%", "Tnova", 350, 1, 4, 6);
        productMinAmountDao.addNewProductToAllBranches(15);
        Product p16 = productsDao.addProduct("Cream Cheese 5%", "Tnova", 350, 1, 4, 8);
        productMinAmountDao.addNewProductToAllBranches(16);
        Product p17 = productsDao.addProduct("Milk 3%", "Tnova", 500, 1, 2, 7);
        productMinAmountDao.addNewProductToAllBranches(17);
        Product p18 = productsDao.addProduct("Coca Cola Zero 0.5 Liter", "CocaCola", 500, 22, 24, 23);
        productMinAmountDao.addNewProductToAllBranches(18);
        Product p19 = productsDao.addProduct("Coca Cola Zero 1 Liter", "CocaCola", 1000, 22, 24, 25);
        productMinAmountDao.addNewProductToAllBranches(19);
        Product p20 = productsDao.addProduct("Coca Cola Zero 1.5 Liter", "CocaCola", 1500, 22, 24, 26);
        productMinAmountDao.addNewProductToAllBranches(20);
        Product p21 = productsDao.addProduct("Banana And Strawberry 1 Liter", "Spring", 1000, 22, 27, 25);
        productMinAmountDao.addNewProductToAllBranches(21);
        Product p22 = productsDao.addProduct("Orange juice 1 Liter", "Spring", 1000, 22, 27, 25);
        productMinAmountDao.addNewProductToAllBranches(22);

// Product Min Amount Table
        for (int j=1;j<6;j++)
        {
            for (int i = 1; i < 23; i++)
            {
                productMinAmountDao.UpdateMinAmountToProductInBranch(i,j,30);
            }
        }
//Dates
        LocalDate date1 = LocalDate.of(2023, 7, 26);
        LocalDate date2 = LocalDate.of(2023, 7, 25);
        LocalDate date3 = LocalDate.of(2023, 7, 30);
        LocalDate date4 = LocalDate.of(2023, 8, 12);
        LocalDate date5 = LocalDate.of(2023, 9, 1);
        LocalDate date6 = LocalDate.of(2023, 10, 22);
        LocalDate date7 = LocalDate.of(2023, 11, 17);
        LocalDate date8 = LocalDate.of(2023, 12, 4);
        LocalDate date9 = LocalDate.of(2023, 1, 31);   //"Expired"
        LocalDate date10 = LocalDate.of(2023, 2, 28);  //"Expired"
        LocalDate date11 = LocalDate.of(2023, 3, 15);  //"Expired"
        LocalDate date12 = LocalDate.of(2023, 4, 8);   //"Expired"
        LocalDate date13 = LocalDate.of(2023, 5, 5);   //"Expired"
        LocalDate date14 = LocalDate.of(2023, 6, 19);
        LocalDate date15 = LocalDate.of(2023, 7, 23);
        LocalDate date16 = LocalDate.of(2023, 8, 10);
        LocalDate date17 = LocalDate.of(2023, 9, 2);
        LocalDate date18 = LocalDate.of(2023, 10, 16);
        LocalDate date19 = LocalDate.of(2023, 11, 21);
        LocalDate date20 = LocalDate.of(2023, 5, 13);
//Discounts

// discounts on p1 for all branches
        Discount d1 = discountsDao.addNewDiscount(1,date9, date12, 15, null,p1);
        Discount d2 = discountsDao.addNewDiscount(2,date9, date12, 15, null,p1);
        Discount d3 = discountsDao.addNewDiscount(3,date9, date12, 15, null,p1);
        Discount d4 = discountsDao.addNewDiscount(4,date9, date12, 15, null,p1);
        Discount d5 = discountsDao.addNewDiscount(5,date9, date12, 15, null,p1);
// discounts on p1 for all branches
        Discount d6 = discountsDao.addNewDiscount(1,date10, date3, 15, null,p1);
        Discount d7 = discountsDao.addNewDiscount(2,date10, date3, 15, null,p1);
        Discount d8 = discountsDao.addNewDiscount(3,date10, date3, 15, null,p1);
        Discount d9 = discountsDao.addNewDiscount(4,date10, date3, 15, null,p1);
        Discount d10 = discountsDao.addNewDiscount(5,date10, date3, 15, null,p1);
// discounts on p2 for all branches
        Discount d11 = discountsDao.addNewDiscount(1,date10, date3, 10, null,p2);
        Discount d12 = discountsDao.addNewDiscount(2,date10, date3, 10, null,p2);
        Discount d13 = discountsDao.addNewDiscount(3,date10, date3, 10, null,p2);
        Discount d14 = discountsDao.addNewDiscount(4,date10, date3, 10, null,p2);
        Discount d15 = discountsDao.addNewDiscount(5,date10, date3, 10, null,p2);
// discounts on p3 for all branches
        Discount d16 = discountsDao.addNewDiscount(1,date10, date3, 15, null,p3);
        Discount d17 = discountsDao.addNewDiscount(2,date10, date3, 15, null,p3);
        Discount d18 = discountsDao.addNewDiscount(3,date10, date3, 15, null,p3);
        Discount d19 = discountsDao.addNewDiscount(4,date10, date3, 15, null,p3);
        Discount d20 = discountsDao.addNewDiscount(5,date10, date3, 15, null,p3);
// discounts on p4 for all branches
        Discount d21 = discountsDao.addNewDiscount(1,date14, date4, 20, null,p4);
        Discount d22 = discountsDao.addNewDiscount(2,date14, date4, 20, null,p4);
        Discount d23 = discountsDao.addNewDiscount(3,date14, date4, 20, null,p4);
        Discount d24 = discountsDao.addNewDiscount(4,date14, date4, 20, null,p4);
        Discount d25 = discountsDao.addNewDiscount(5,date14, date4, 20, null,p4);
// discounts on p5 for all branches
        Discount d26 = discountsDao.addNewDiscount(1,date1, date6, 5, null,p5);
        Discount d27 = discountsDao.addNewDiscount(2,date1, date6, 5, null,p5);
        Discount d28 = discountsDao.addNewDiscount(3,date1, date6, 5, null,p5);
        Discount d29 = discountsDao.addNewDiscount(4,date1, date6, 5, null,p5);
        Discount d30 = discountsDao.addNewDiscount(5,date1, date6, 5, null,p5);
// discounts on c1 for all branches
        Discount d31 = discountsDao.addNewDiscount(1,date14, date4, 12, c1,null);
        Discount d32 = discountsDao.addNewDiscount(2,date14, date4, 12, c1,null);
        Discount d33 = discountsDao.addNewDiscount(3,date14, date4, 12, c1,null);
        Discount d34 = discountsDao.addNewDiscount(4,date14, date4, 12, c1,null);
        Discount d35 = discountsDao.addNewDiscount(5,date14, date4, 12, c1,null);
// discounts on c10 for all branches
        Discount d36 = discountsDao.addNewDiscount(1,date14, date19, 12, c10,null);
        Discount d37 = discountsDao.addNewDiscount(2,date14, date19, 12, c10,null);
        Discount d38 = discountsDao.addNewDiscount(3,date14, date19, 12, c10,null);
        Discount d39 = discountsDao.addNewDiscount(4,date14, date19, 12, c10,null);
        Discount d40 = discountsDao.addNewDiscount(5,date14, date19, 12, c10,null);
// discounts on c8 for all branches
        Discount d41 = discountsDao.addNewDiscount(1,date14, date4, 7, c8,null);
        Discount d42 = discountsDao.addNewDiscount(2,date14, date4, 7, c8,null);
        Discount d43 = discountsDao.addNewDiscount(3,date14, date4, 7, c8,null);
        Discount d44 = discountsDao.addNewDiscount(4,date14, date4, 7, c8,null);
        Discount d45 = discountsDao.addNewDiscount(5,date14, date4, 7, c8,null);
// discounts on c21 for all branches
        Discount d46 = discountsDao.addNewDiscount(1,date16, date17, 25, c10,null);
        Discount d47 = discountsDao.addNewDiscount(2,date16, date17, 25, c10,null);
        Discount d48 = discountsDao.addNewDiscount(3,date16, date17, 25, c10,null);
        Discount d49 = discountsDao.addNewDiscount(4,date16, date17, 25, c10,null);
        Discount d50 = discountsDao.addNewDiscount(5,date16, date17, 25, c10,null);

// Items for all Branches
        for (int j=1;j<6;j++)
        {
            for (int i = 1; i < 50; i++)
            {
                Item item1 = itemsDao.addItem(j,date1,date13 , 2, 9 ,1,p1);
                Item item2 = itemsDao.addItem(j,date1,date13 , 4, 12 ,2,p2);
                Item item3 = itemsDao.addItem(j,date4,date13 , 1, 5 ,3,p3);
                Item item4 = itemsDao.addItem(j,date4,date13 , 1, 4 ,4,p4);
                Item item5 = itemsDao.addItem(j,date4,date13 , 0.5, 3 ,5,p5);
                Item item6 = itemsDao.addItem(j,date4,date13 , 1, 3 ,6,p6);
                Item item7 = itemsDao.addItem(j,date4,date13 , 1, 4 ,7,p7);
                Item item8 = itemsDao.addItem(j,date1,date13 , 4, 9 ,3,p8);
                Item item9 = itemsDao.addItem(j,date1,date13 , 3, 10 ,4,p9);
                Item item10 = itemsDao.addItem(j,date1,date13 , 5, 11 ,5,p10);
                Item item11 = itemsDao.addItem(j,date1,date13 , 5, 12 ,6,p11);
                Item item12 = itemsDao.addItem(j,date1,date13 , 5, 14 ,7,p12);
                Item item13 = itemsDao.addItem(j,date1,date13 , 6, 15 ,3,p13);
                Item item14 = itemsDao.addItem(j,date1,date13 , 5, 9 ,4,p14);
                Item item15 = itemsDao.addItem(j,date1,date13 , 4, 12 ,5,p15);
                Item item16 = itemsDao.addItem(j,date1,date13 , 5, 9 ,6,p16);
                Item item17 = itemsDao.addItem(j,date1,date13 , 5, 9 ,7,p17);
                Item item18 = itemsDao.addItem(j,null,date13 , 2, 6 ,3,p18);
                Item item19 = itemsDao.addItem(j,null,date13 , 4, 9 ,4,p19);
                Item item20 = itemsDao.addItem(j,null,date13 , 6, 12 ,5,p20);
                Item item21 = itemsDao.addItem(j,null,date13 , 4,  9,9,p21);
                Item item22 = itemsDao.addItem(j,null,date13 ,4 , 9 ,7,p22);

            }
        }
        // add expired Items
        for (int j=1;j<6;j++)
        {
            for (int i = 1; i < 6; i++)
            {
                Item item1 = itemsDao.addItem(j,date20,date13 , 2, 9 ,1,p1);
                Item item2 = itemsDao.addItem(j,date20,date13 , 4, 12 ,2,p2);
            }
        }
    }
}

