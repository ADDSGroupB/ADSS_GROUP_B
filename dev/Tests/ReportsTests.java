package Tests;
import BusinessLayer.InventoryBusinessLayer.*;
import DataAccessLayer.DBConnector;
import DataAccessLayer.InventoryDataAccessLayer.*;
import InterfaceLayer.Main;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import java.sql.SQLException;
import java.time.LocalDate;




import static org.junit.jupiter.api.Assertions.*;

public class ReportsTests {
    private MainController mainController;
    private ProductsDao productsDao;
    private ItemsDao itemsDao;
    private BranchesDao branchesDao;
    private CategoryDao categoryDao;
    private DiscountsDao discountsDao;
    private ProductMinAmountDao productMinAmountDao;

    @BeforeEach
    void setUp() throws SQLException {
        DBConnector.connect();
        DBConnector.deleteRecordsOfTables();
        mainController = new MainController();
        productsDao = mainController.getProductsDao();
        itemsDao = mainController.getItemsDao();
        branchesDao = mainController.getBranchesDao();
        categoryDao = mainController.getCategoryDao();
        discountsDao = mainController.getDiscountsDao();
        productMinAmountDao = mainController.getProductMinAmountDao();
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
        LocalDate date1 = LocalDate.of(2023, 5, 26);
        LocalDate date2 = LocalDate.of(2023, 6, 25);
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

        // add damaged Items
        for (int j=1;j<6;j++)
        {
            for (int i = 1; i < 6; i++)
            {
                itemsDao.updateItemStatus(300+j*3, "Damaged");
                itemsDao.updateItemDefectiveDescription(300+j*3, "Defect" + (300+j*3));
            }
        }
    }
    @Test
    void createReports() throws SQLException {
        MissingProductsReport missingProductsReport1 = new MissingProductsReport(mainController.getReportDao().getNewReportID(), 2, LocalDate.now());
        missingProductsReport1.addMissingProduct(mainController.getProductsDao().getProductByID(1), 20);
        missingProductsReport1.addMissingProduct(mainController.getProductsDao().getProductByID(2), 30);
        missingProductsReport1.addMissingProduct(mainController.getProductsDao().getProductByID(3), 34);
        mainController.getReportDao().addReport(missingProductsReport1);
        MissingProductsReport missingProductsReport2 = new MissingProductsReport(mainController.getReportDao().getNewReportID(), 3, LocalDate.now());
        missingProductsReport2.addMissingProduct(mainController.getProductsDao().getProductByID(5), 20);
        missingProductsReport2.addMissingProduct(mainController.getProductsDao().getProductByID(2), 50);
        missingProductsReport2.addMissingProduct(mainController.getProductsDao().getProductByID(3), 39);
        mainController.getReportDao().addReport(missingProductsReport2);
        assertEquals(20, ((MissingProductsReport)mainController.getReportDao().getReportByID(1)).getMissingProductsMap().get(mainController.getProductsDao().getProductByID(1)));
        assertEquals(30, ((MissingProductsReport)mainController.getReportDao().getReportByID(1)).getMissingProductsMap().get(mainController.getProductsDao().getProductByID(2)));
        assertEquals(34, ((MissingProductsReport)mainController.getReportDao().getReportByID(1)).getMissingProductsMap().get(mainController.getProductsDao().getProductByID(3)));
        assertEquals(20, ((MissingProductsReport)mainController.getReportDao().getReportByID(2)).getMissingProductsMap().get(mainController.getProductsDao().getProductByID(5)));
        assertEquals(50, ((MissingProductsReport)mainController.getReportDao().getReportByID(2)).getMissingProductsMap().get(mainController.getProductsDao().getProductByID(2)));
        assertEquals(39, ((MissingProductsReport)mainController.getReportDao().getReportByID(2)).getMissingProductsMap().get(mainController.getProductsDao().getProductByID(3)));

        Map<Category, Map<Product, Integer>> weeklyCategoryMap = new HashMap<>();
        Map<Product, Integer> productInCategory1 = new HashMap<>();
        Map<Product, Integer> productInCategory2 = new HashMap<>();
        Category category = mainController.getCategoryDao().getCategoryByID(1);
        List<Product> productList = mainController.getProductsDao().getAllProductsInCategory(1);
        for (Product product : productList){
            List<Item> store = mainController.getItemsDao().getAllStoreItemsByBranchIDAndProductID(1,product.getProductID());
            List<Item> storage = mainController.getItemsDao().getAllStoreItemsByBranchIDAndProductID(1,product.getProductID());

            productInCategory1.put(product, storage.size()+store.size());
        }
        weeklyCategoryMap.put(category, productInCategory1);
        category = mainController.getCategoryDao().getCategoryByID(11);
        productList = mainController.getProductsDao().getAllProductsInCategory(11);
        for (Product product : productList){
            List<Item> store = mainController.getItemsDao().getAllStoreItemsByBranchIDAndProductID(1,product.getProductID());
            List<Item> storage = mainController.getItemsDao().getAllStoreItemsByBranchIDAndProductID(1,product.getProductID());
            productInCategory2.put(product, storage.size()+store.size());
        }
        weeklyCategoryMap.put(category, productInCategory2);
        WeeklyStorageReport weeklyStorageReport = new WeeklyStorageReport(mainController.getReportDao().getNewReportID(), 1, LocalDate.now(), weeklyCategoryMap);
        mainController.getReportDao().addReport(weeklyStorageReport);
        assertEquals(49, ((WeeklyStorageReport)mainController.getReportDao().getReportByID(3)).getWeeklyReportMap().get(mainController.getCategoryDao().getCategoryByID(1)).get(mainController.getProductsDao().getProductByID(13)));
        assertEquals(53, ((WeeklyStorageReport)mainController.getReportDao().getReportByID(3)).getWeeklyReportMap().get(mainController.getCategoryDao().getCategoryByID(1)).get(mainController.getProductsDao().getProductByID(1)));
        assertEquals(48, ((WeeklyStorageReport)mainController.getReportDao().getReportByID(3)).getWeeklyReportMap().get(mainController.getCategoryDao().getCategoryByID(11)).get(mainController.getProductsDao().getProductByID(4)));

        ArrayList<Item> itemsList = (ArrayList<Item>) mainController.getItemsDao().getAllDamagedItemsByBranchID(1);
        DefectiveProductsReport defectiveProductsReport = new DefectiveProductsReport(mainController.getReportDao().getNewReportID(), 1, LocalDate.now(), itemsList);
        mainController.getReportDao().addReport(defectiveProductsReport);
        System.out.println(defectiveProductsReport);
        assertEquals("** Defective Products Report **\n" +
                "-------------------------------\n" +
                "Report ID: 4\n" +
                "Production date: 2023-05-15\n" +
                "---------------------------\n" +
                "Item ID: 303, Product ID: 17, Product name: Milk 3%\n" +
                "DefectiveType: Damaged\n" +
                "Defective Discription: Defect303\n" +
                "Item ID: 306, Product ID: 20, Product name: Coca Cola Zero 1.5 Liter\n" +
                "DefectiveType: Damaged\n" +
                "Defective Discription: Defect306\n" +
                "Item ID: 309, Product ID: 1, Product name: Milk 3%\n" +
                "DefectiveType: Damaged\n" +
                "Defective Discription: Defect309\n" +
                "Item ID: 312, Product ID: 4, Product name: Green Onion\n" +
                "DefectiveType: Damaged\n" +
                "Defective Discription: Defect312\n" +
                "Item ID: 315, Product ID: 7, Product name: Green Apple\n" +
                "DefectiveType: Damaged\n" +
                "Defective Discription: Defect315\n", mainController.getReportDao().getReportByID(4).toString());
    }
}
