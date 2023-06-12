package Tests;

import BusinessLayer.InventoryBusinessLayer.*;
import BusinessLayer.SupplierBusinessLayer.*;
import ServiceLayer.SupplierServiceLayer.*;
import DataAccessLayer.DBConnector;
import Utillity.Response;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

import static junit.framework.TestCase.*;

public class InventorySupplierTests {
    private SupplierController sc;
    private SupplierService ssl;
    private OrderService orderService;
    private MainController mainController;
    @Before
    public void setUp() throws SQLException {
        DBConnector.connect();
        DBConnector.deleteRecordsOfInventoryTables();
        DBConnector.deleteRecordsOfTables();
        ssl = new SupplierService();
        sc = new SupplierController();
        mainController = new MainController();
        orderService = new OrderService();

        Branch b1 = mainController.getBranchesDao().addBranch("supperlee");
        HashMap<Integer, Double> discount1 = new HashMap<>();
        discount1.put(50,10.0);
        discount1.put(10,5.0);
        SupplierProductService product11 = new SupplierProductService("Coffee", 1, 105, 7.90, 60, discount1, "Elite", 120, 0.4);
        SupplierProductService product12 = new SupplierProductService("Shampoo", 2, 104, 12.90, 74, discount1, "Head and Shoulders", 300, 1.2);
        SupplierProductService product10 = new SupplierProductService("water", 3, 103, 7.90, 60, discount1, "Elite", 120, 0.4);


        ServiceContact c1 = new ServiceContact("Itamar Barami", "itamar@gmail.com", "054-2453119");
        ServiceContact c2 = new ServiceContact("Dan Waizmann", "dan@gmail.com", "054-2453539");

        ArrayList<DayOfWeek> days1 = new ArrayList<>();

        days1.add(DayOfWeek.SUNDAY);
        days1.add(DayOfWeek.WEDNESDAY);

        HashMap<Integer, SupplierProductService> supplyingProduct1 = new HashMap<>();
        supplyingProduct1.put(1,product11);
        supplyingProduct1.put(2,product12);
        supplyingProduct1.put(3,product10);

        ServiceAgreement a1 = new ServiceAgreement("Cash", true, days1,  supplyingProduct1, "By Days", 0);

        ArrayList<ServiceContact> sc1 = new ArrayList<>();
        ArrayList<ServiceContact> sc2 = new ArrayList<>();

        sc1.add(c1);
        sc2.add(c2);
//        sc.setAgreement(a1, 1);

        ArrayList<DayOfWeek> days2 = new ArrayList<>();
        days2.add(DayOfWeek.TUESDAY);
        days2.add(DayOfWeek.WEDNESDAY);

        HashMap<Integer, Double> discount2 = new HashMap<>();

        SupplierProductService product21 = new SupplierProductService("Bamba", 52, 24, 7.90, 70, discount2, "Osem", 80, 0.3);
        SupplierProductService product22 = new SupplierProductService("Milk", 53, 19, 12.90, 65, discount2, "Tnuva", 14, 1.0);

        HashMap<Integer, SupplierProductService> supplyingProduct2 = new HashMap<>();

        supplyingProduct2.put(52, product21);
        supplyingProduct2.put(53, product22);

        ServiceAgreement a2 = new ServiceAgreement("Cash", true, days2,  supplyingProduct2, "By Days", 0);
//        sc.setAgreement(a2, 2);
        ssl.addSupplier("Gal Halifa", "Beit ezra", "123456", a1, sc1);
        ssl.addSupplier("Guy Biton", "Beer Sheva", "456123", a2, sc2);

    }
    @Test
    public void createPeriodicOrder()
    {
        HashMap<Integer, Integer> productsToOrder = new HashMap<>();
        productsToOrder.put(1, 10);
        productsToOrder.put(2, 2);
        int num = ssl.createPeriodicOrder(1, 1, DayOfWeek.MONDAY, productsToOrder).getSupplierId();
        assertEquals(1, num);
    }
    @Test
    public void executePeriodicOrder() {
        HashMap<Integer, Integer> productsToOrder = new HashMap<>();
        productsToOrder.put(1, 50);
        productsToOrder.put(2, 35);
        // System.out.println(ssl.createPeriodicOrder(1, 1, DayOfWeek.MONDAY, productsToOrder).getErrorMessage())
        int num1 = ssl.createPeriodicOrder(1, 1, DayOfWeek.MONDAY, productsToOrder).getSupplierId();
        int num2 = ssl.createPeriodicOrder(1, 1, DayOfWeek.TUESDAY, productsToOrder).getSupplierId();
        int num3 = ssl.createPeriodicOrder(1, 1, DayOfWeek.WEDNESDAY, productsToOrder).getSupplierId();
        int num4 = ssl.createPeriodicOrder(1, 1, DayOfWeek.THURSDAY, productsToOrder).getSupplierId();
        int num5 = ssl.createPeriodicOrder(1, 1, DayOfWeek.FRIDAY, productsToOrder).getSupplierId();
        int num6 = ssl.createPeriodicOrder(1, 1, DayOfWeek.SATURDAY, productsToOrder).getSupplierId();
        int num7 = ssl.createPeriodicOrder(1, 1, DayOfWeek.SUNDAY, productsToOrder).getSupplierId();
        assertEquals(1, num1);
        assertEquals(2, num2);
        assertEquals(3, num3);
        assertEquals(4, num4);
        assertEquals(5, num5);
        assertEquals(6, num6);
        assertEquals(7, num7);
        Response response = ssl.executePeriodicOrder(2);
        if (!response.errorOccurred()) assertEquals(1, response.getSupplierId());
    }

    public void runTests() {
        createPeriodicOrder();
        executePeriodicOrder();
    }

//    @Test
//    public void notCreateShortageOrder() throws SQLException {
//        mainController.getProductMinAmountDao().UpdateMinAmountToProductInBranch(1,1,5);
//        Product p = mainController.getProductsDao().getProductByID(1);
//
//        LocalDate date1 = LocalDate.of(2023, 5, 26);
//        LocalDate date18 = LocalDate.of(2023, 10, 16);
//
//        for (int i = 0; i < 8 ;i++)
//        {
//            Item item1 = mainController.getItemsDao().addItem(1,date18,date1, 2, 9 ,1,p);
//        }
//
//        HashMap<Integer, Integer> shortage;
//        shortage = mainController.getItemsDao().fromStorageToStore(mainController.getBranchesDao().getBranchByID(1));
//
//        Response response = orderService.createOrderByShortage(1, shortage);
//        HashMap<Integer, Order > check = orderService.getOrdersToBranch(1);
//        Order order = check.get(1);
//        assertNull(order);
//
//    }

}

