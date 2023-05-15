package Tests;

import BusinessLayer.InventoryBusinessLayer.Branch;
import BusinessLayer.InventoryBusinessLayer.MainController;
import BusinessLayer.SupplierBusinessLayer.*;
import ServiceLayer.SupplierServiceLayer.ServiceAgreement;
import ServiceLayer.SupplierServiceLayer.ServiceContact;
import ServiceLayer.SupplierServiceLayer.SupplierProductService;
import ServiceLayer.SupplierServiceLayer.SupplierService;
import DataAccessLayer.DBConnector;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashMap;

import static junit.framework.TestCase.*;

public class InventorySupplierTests {
    private SupplierController sc;
    private SupplierService ssl;
    private MainController mainController;

    @Before
    public void setUp() throws SQLException {
        DBConnector.connect();
        DBConnector.deleteRecordsOfTables();
        sc = new SupplierController();
        ssl = new SupplierService();
        mainController = new MainController();
        Branch b1 = mainController.getBranchesDao().addBranch("supperlee");
        ArrayList<ServiceContact> sc1 = new ArrayList<>();
        ArrayList<ServiceContact> sc2 = new ArrayList<>();
        ServiceContact c1 = new ServiceContact("Itamar Barami", "itamar@gmail.com", "054-2453119");
        ServiceContact c2 = new ServiceContact("Dan Waizmann", "dan@gmail.com", "054-2453539");
        sc1.add(c1);
        sc2.add(c2);
        ArrayList<DayOfWeek> days1 = new ArrayList<>();
        days1.add(DayOfWeek.TUESDAY);
        days1.add(DayOfWeek.MONDAY);
        HashMap<Integer, Double> discount1 = new HashMap<>();
        SupplierProductService product11 = new SupplierProductService("Coffee", 24, 10, 7.90, 60, discount1, "Elite", 120, 0.4);
        SupplierProductService product12 = new SupplierProductService("Shampoo", 4, 14, 12.90, 74, discount1, "Head and Shoulders", 300, 1.2);;
        HashMap<Integer, SupplierProductService> supplyingProduct1 = new HashMap<>();
        supplyingProduct1.put(5, product11);
        supplyingProduct1.put(6, product12);
        ServiceAgreement a1 = new ServiceAgreement("Cash", true, days1,  supplyingProduct1, "By Days", 0);
//        sc.setAgreement(a1, 1);

        ArrayList<DayOfWeek> days2 = new ArrayList<>();
        days2.add(DayOfWeek.TUESDAY);
        days2.add(DayOfWeek.WEDNESDAY);
        HashMap<Integer, Double> discount2 = new HashMap<>();
        SupplierProductService product21 = new SupplierProductService("Bamba", 6, 24, 7.90, 70, discount2, "Osem", 80, 0.3);
        SupplierProductService product22 = new SupplierProductService("Milk", 16, 19, 12.90, 65, discount2, "Tnuva", 14, 1.0);
        HashMap<Integer, SupplierProductService> supplyingProduct2 = new HashMap<>();
        supplyingProduct2.put(24, product21);
        supplyingProduct2.put(4, product22);
        ServiceAgreement a2 = new ServiceAgreement("Cash", true, days2,  supplyingProduct2, "By Days", 0);
//        sc.setAgreement(a2, 2);
        ssl.addSupplier("Gal Halifa", "Beit ezra", "123456", a1, sc1);
        ssl.addSupplier("Guy Biton", "Beer Sheva", "456123", a2, sc2);
    }
    @Test
    public void createPeriodicOrder(){
        HashMap<Integer, Integer> productsToOrder = new HashMap<>();
        productsToOrder.put(24, 50);
        productsToOrder.put(4, 35);
        // System.out.println(ssl.createPeriodicOrder(1, 1, DayOfWeek.MONDAY, productsToOrder).getErrorMessage())
        int num = ssl.createPeriodicOrder(1, 1, DayOfWeek.MONDAY, productsToOrder).getSupplierId();
        int num2 = ssl.createPeriodicOrder(2, 1, DayOfWeek.MONDAY, productsToOrder).getSupplierId();
        assertEquals(1, num);
        assertEquals(0, num2);
    }
}

