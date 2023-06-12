package Tests;

import BusinessLayer.SupplierBusinessLayer.Agreement;
import BusinessLayer.SupplierBusinessLayer.Supplier;
import BusinessLayer.SupplierBusinessLayer.SupplierController;
import BusinessLayer.SupplierBusinessLayer.SupplierProduct;
import DataAccessLayer.DBConnector;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashMap;

import static junit.framework.TestCase.*;

public class SupplierControllerTests {
    private SupplierController sc;

    @Before
    public void setUp() throws SQLException {
        DBConnector.connect();
        DBConnector.deleteRecordsOfTables();
        sc = new SupplierController();
        sc.addSupplier("Gal Halifa", "Beit ezra", "123456");
        sc.addSupplier("Guy Biton", "Beer Sheva", "456123");
    }

    @Test
    public void addSupplier(){
        assertTrue(sc.addSupplier("Gal Halifa", "Beit ezra", "123456").errorOccurred());
        assertFalse(sc.addSupplier("Mor Shuker", "Rehovot", "963741").errorOccurred());
    }

    @Test
    public void changeAddress(){
        assertFalse(sc.changeAddress(1, "Ashdod").errorOccurred());
    }

    @Test
    public void changeBankAccount(){
        assertTrue(sc.changeSupplierBankAccount(1, "456123").errorOccurred());
        assertFalse(sc.changeSupplierBankAccount(1, "141414").errorOccurred());
    }

    @Test
    public void removeSupplier(){
        assertFalse(sc.removeSupplier(1).errorOccurred());
        assertTrue(sc.removeSupplier(1).errorOccurred());
    }

    @Test
    public void addContactToSupplier(){
        assertFalse(sc.addContactsTOSupplier(1, "Mor Shuker", "mor@gmail.com", "054-2453539").errorOccurred());
        assertTrue(sc.addContactsTOSupplier(1, "Mor Shuker", "mor@gmail.com", "054-2453539").errorOccurred());
    }

    @Test
    public void addAgreementToSupplier(){
        boolean bool;
        ArrayList<DayOfWeek> days = new ArrayList<>();
        days.add(DayOfWeek.TUESDAY);
        HashMap<Integer, Double> discount = new HashMap<>();
        SupplierProduct product = new SupplierProduct("Bamba", 5, 88, 6.90, 20, discount);
        HashMap<Integer, SupplierProduct> supplyingProduct = new HashMap<>();
        supplyingProduct.put(5, product);
        Agreement a = sc.createAgreement("Cash", true, days,  supplyingProduct, "By Days", 0);
        sc.setAgreement(a, 1);
        Supplier s = sc.getSupllierByID(1);
        bool = s.getAgreement() != null;
        assertTrue(bool);
    }

    @Test
    public void changeSupplierName(){
        assertFalse(sc.changeSupplierName(1, "Dan weizmann").errorOccurred());
    }

    @Test
    public void addDiscountToSupplier(){
        ArrayList<DayOfWeek> days = new ArrayList<>();
        days.add(DayOfWeek.TUESDAY);
        HashMap<Integer, Double> discount = new HashMap<>();
        SupplierProduct product = new SupplierProduct("Bamba", 5, 88, 6.90, 20, discount);
        HashMap<Integer, SupplierProduct> supplyingProduct = new HashMap<>();
        supplyingProduct.put(5, product);
        Agreement a = sc.createAgreement("Cash", true, days,  supplyingProduct, "By Days", 0);
        sc.setAgreement(a, 1);
        assertTrue(sc.addDiscount(1, 5, 50, 20.0).errorOccurred());
    }

    @Test
    public void getUnexcitingSupplier(){
        Supplier supplier = sc.getSupllierByID(5);
        assertNull(supplier);
    }

    @Test
    public void getExistingSupplier(){
        Supplier supplier = sc.getSupllierByID(2);
        assertNotNull(supplier);
    }

    @Test
    public void changeCatalogNumber(){
        ArrayList<DayOfWeek> days = new ArrayList<>();
        days.add(DayOfWeek.TUESDAY);
        HashMap<Integer, Double> discount = new HashMap<>();
        SupplierProduct product = new SupplierProduct("Bamba", 5, 88, 6.90, 20, discount);
        HashMap<Integer, SupplierProduct> supplyingProduct = new HashMap<>();
        supplyingProduct.put(5, product);
        Agreement a = sc.createAgreement("Cash", true, days,  supplyingProduct, "By Days", 0);
        sc.setAgreement(a, 1);
        assertFalse(sc.editItemCatalodNumber(1, 5, 99).errorOccurred());
    }

    @Test
    public void removeItemFromAgreement(){
        ArrayList<DayOfWeek> days = new ArrayList<>();
        days.add(DayOfWeek.TUESDAY);
        HashMap<Integer, Double> discount = new HashMap<>();
        SupplierProduct product = new SupplierProduct("Bamba", 5, 88, 6.90, 20, discount);
        HashMap<Integer, SupplierProduct> supplyingProduct = new HashMap<>();
        supplyingProduct.put(5, product);
        Agreement a = sc.createAgreement("Cash", true, days,  supplyingProduct, "By Days", 0);
        sc.setAgreement(a, 1);
        assertTrue(sc.removeItemFromAgreement(1, 14).errorOccurred());
        assertFalse(sc.removeItemFromAgreement(1, 5).errorOccurred());
    }

    @Test
    public void createOrderByShortage(){
        assertTrue(sc.removeSupplierContact(1, "054-2453539").errorOccurred());
    }

    @Test
    public void removeDiscount(){
        ArrayList<DayOfWeek> days = new ArrayList<>();
        days.add(DayOfWeek.TUESDAY);
        HashMap<Integer, Double> discount = new HashMap<>();
        discount.put(5, 30.0);
        SupplierProduct product = new SupplierProduct("Bamba", 5, 88, 6.90, 20, discount);
        HashMap<Integer, SupplierProduct> supplyingProduct = new HashMap<>();
        supplyingProduct.put(5, product);
        Agreement a = sc.createAgreement("Cash", true, days,  supplyingProduct, "By Days", 0);
        sc.setAgreement(a, 1);
        assertFalse(sc.removeDiscount(1, 5, 20, 30.0).errorOccurred());
    }
    @Test
    public void changeSupplierContactEmail(){
        assertFalse(sc.addContactsTOSupplier(1, "Mor Shuker", "mor@gmail.com", "054-2453539").errorOccurred());
        assertFalse(sc.editSupplierContactEmail(1, "mor@gmail.com", "morShuker@gmail.com").errorOccurred());
        assertTrue(sc.editSupplierContactEmail(1, "mor@gmail.com", "morShuker@gmail.com").errorOccurred());
    }


    public void runTests() {
        addSupplier();
        changeAddress();
        changeBankAccount();
        removeSupplier();
        addContactToSupplier();
        addAgreementToSupplier();
        changeSupplierName();
        addDiscountToSupplier();
        getUnexcitingSupplier();
        changeCatalogNumber();
        removeItemFromAgreement();
        createOrderByShortage();
        removeDiscount();
        changeSupplierContactEmail();
    }
}
