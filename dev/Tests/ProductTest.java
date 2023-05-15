package Tests;
import BusinessLayer.InventoryBusinessLayer.*;
import BusinessLayer.InventoryBusinessLayer.MainController;
import BusinessLayer.InventoryBusinessLayer.Product;
import DataAccessLayer.DBConnector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
class ProductTest {
    private MainController mainController;
    @BeforeEach
    void setUp() throws SQLException {
        DBConnector.connect();
        DBConnector.deleteRecordsOfInventoryTables();
        DBConnector.deleteRecordsOfTables();

        mainController = new MainController();
        Category c1 = mainController.getCategoryDao().addCategory("Dairy products");
        Category c2 = mainController.getCategoryDao().addCategory("Milk");
        Category c7 = mainController.getCategoryDao().addCategory("3% fat");
        Product p1 = mainController.getProductsDao().addProduct("Milk 3%", "Tara", 500, 1, 2, 3);
    }

    @Test
    void getParentCategory() throws SQLException {
        assertEquals(1, mainController.getProductsDao().getProductByID(1).getParentCategory().getCategoryID());
    }

    @Test
    void getSubCategory() throws SQLException {
        assertEquals(2, mainController.getProductsDao().getProductByID(1).getSubCategory().getCategoryID());
    }

    @Test
    void getSubSubCategory() throws SQLException {
        assertEquals(3, mainController.getProductsDao().getProductByID(1).getSubSubCategory().getCategoryID());
    }

    @Test
    void getProductWeight() throws SQLException {
        assertEquals(500, mainController.getProductsDao().getProductByID(1).getProductWeight());
    }

    @Test
    void getProductID() throws SQLException {
        assertEquals(1, mainController.getProductsDao().getProductByID(1).getProductID());
    }

    @Test
    void getManufacturer() throws SQLException {
        assertEquals("Tara", mainController.getProductsDao().getProductByID(1).getManufacturer());
    }

    @Test
    void getProductName() throws SQLException {
        assertEquals("Milk 3%", mainController.getProductsDao().getProductByID(1).getProductName());
    }


    @Test
    void testToString() throws SQLException {
        String productToString = "Product ID: 1" + "\n" +
                "Product Name: Milk 3%" + "\n" +
                "Manufacturer: Tara" + "\n" +
                "Product Weight: 500.0" + "\n" +
                "Product Parent Category: Dairy products" + "\n" +
                "Product Sub Category: Milk" + "\n" +
                "Product SubSub Category: 3% fat" + "\n";
        assertEquals(productToString, mainController.getProductsDao().getProductByID(1).toString());
    }
}