package Tests;

import BusinessLayer.InventoryBusinessLayer.*;
import DataAccessLayer.DBConnector;
import InterfaceLayer.Main;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ItemTest {
    private MainController mainController;

    @BeforeEach
    void setUp() throws SQLException {
        DBConnector.connect();
        DBConnector.deleteRecordsOfTables();
        mainController = new MainController();
        Branch b1 = mainController.getBranchesDao().addBranch("SuperLi Beer Sheva");
        Category c1 = mainController.getCategoryDao().addCategory("Dairy products");
        Category c2 = mainController.getCategoryDao().addCategory("Milk");
        Category c7 = mainController.getCategoryDao().addCategory("3% fat");
        Product p1 = mainController.getProductsDao().addProduct("Milk 3%", "Tara", 500, 1, 2, 3);
        LocalDate date1 = LocalDate.of(2023, 5, 26);
        LocalDate date13 = LocalDate.of(2023, 5, 5);
        Item item1 = mainController.getItemsDao().addItem(1, date1, date13 , 2, 9 ,1, p1);
    }

    @Test
    void getProduct() throws SQLException {
        assertEquals(mainController.getProductsDao().getProductByID(1), mainController.getItemsDao().getItemByID(1).getProduct());
    }

    @Test
    void getBranchID() throws SQLException {
        assertEquals(1, mainController.getItemsDao().getItemByID(1).getBranchID());
    }

    @Test
    void getProductID() throws SQLException {
        assertEquals(1, mainController.getItemsDao().getItemByID(1).getProductID());
    }

    @Test
    void getExpiredDate() throws SQLException {
        assertEquals(LocalDate.of(2023, 5, 26), mainController.getItemsDao().getItemByID(1).getExpiredDate());
    }

    @Test
    void getPriceFromSupplier() throws SQLException {
        assertEquals(2, mainController.getItemsDao().getItemByID(1).getPriceFromSupplier());
    }

    @Test
    void getPriceInBranch() throws SQLException {
        assertEquals(9, mainController.getItemsDao().getItemByID(1).getPriceInBranch());
    }

    @Test
    void getItemID() throws SQLException {
        assertEquals(1, mainController.getItemsDao().getItemByID(1).getItemID());
    }

    @Test
    void getSupplierID() throws SQLException {
        assertEquals(1, mainController.getItemsDao().getItemByID(1).getSupplierID());
    }

    @Test
    void getDefectiveDiscription() throws SQLException {
        mainController.getItemsDao().getItemByID(1).setDefectiveDiscription("defect1");
        assertEquals("defect1", mainController.getItemsDao().getItemByID(1).getDefectiveDiscription());
    }

    @Test
    void testToString() throws SQLException {
        String itemToString = "Item ID : 1 \n" +
                "Branch ID : 1 \n" +
                "Product ID : 1 \n" +
                "Supplier ID : 1 \n" +
                "Expired Date : 2023-05-26 \n" +
                "PriceFromSupplier : 2.0 \n" +
                "PriceInBranch : 9.0 \n" +
                "PriceAfterDiscount : 9.0 \n" +
                "Status : null \n" +
                "Arrival Date : 2023-05-05 ";
        assertEquals(itemToString, mainController.getItemsDao().getItemByID(1).toString());
    }
}