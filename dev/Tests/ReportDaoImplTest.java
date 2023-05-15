package Tests;

import BusinessLayer.*;
import BusinessLayer.InventoryBusinessLayer.*;
import DataAccessLayer.InventoryDataAccessLayer.ReportDaoImpl;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ReportDaoImplTest {
    private MainController mainController;

    @org.junit.jupiter.api.BeforeEach
    void setUp() throws SQLException {
    }

    @org.junit.jupiter.api.Test
    void getAllMissingReports() throws SQLException {
        Map<Integer, MissingProductsReport> missingProductsReportMap = mainController.getReportDao().getAllMissingReports();
        for (MissingProductsReport missingReport : missingProductsReportMap.values()) {
            System.out.println(missingReport);
        }
    }

    @org.junit.jupiter.api.Test
    void getAllDefectiveReports() throws SQLException {
        Map<Integer, DefectiveProductsReport> defectiveProductsReportMap = mainController.getReportDao().getAllDefectiveReports();
        for (DefectiveProductsReport defectiveReport : defectiveProductsReportMap.values()) {
            System.out.println(defectiveReport);
        }
    }

    @org.junit.jupiter.api.Test
    void getAllWeeklyReports() throws SQLException {
        Map<Integer, WeeklyStorageReport> weeklyStorageReportMap = mainController.getReportDao().getAllWeeklyReports();
        for (WeeklyStorageReport weeklyReport : weeklyStorageReportMap.values()) {
            System.out.println(weeklyReport);
        }
    }

    @org.junit.jupiter.api.Test
    void getReportByID() {
    }

    @org.junit.jupiter.api.Test
    void addReport() throws SQLException {
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

        Map<Category, Map<Product, Integer>> weeklyCategoryMap = new HashMap<>();
        Map<Product, Integer> productInCategory = new HashMap<>();
        Category category = mainController.getCategoryDao().getCategoryByID(1);
        List<Product> productList = mainController.getProductsDao().getAllProductsInCategory(1);
        for (Product product : productList){
            productInCategory.put(product, 25);
        }
        weeklyCategoryMap.put(category, productInCategory);
        category = mainController.getCategoryDao().getCategoryByID(1);
        productList = mainController.getProductsDao().getAllProductsInCategory(1);
        for (Product product : productList){
            productInCategory.put(product, 30);
        }
        weeklyCategoryMap.put(category, productInCategory);
        WeeklyStorageReport weeklyStorageReport = new WeeklyStorageReport(mainController.getReportDao().getNewReportID(), 1, LocalDate.now(), weeklyCategoryMap);
        mainController.getReportDao().addReport(weeklyStorageReport);

        ArrayList<Item> itemsList = new ArrayList<>();
        itemsList.add(mainController.getItemsDao().getItemByID(3));
        itemsList.add(mainController.getItemsDao().getItemByID(5));
        DefectiveProductsReport defectiveProductsReport = new DefectiveProductsReport(mainController.getReportDao().getNewReportID(), 5, LocalDate.now(), itemsList);
        mainController.getReportDao().addReport(defectiveProductsReport);
    }
}