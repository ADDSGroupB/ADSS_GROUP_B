package DataAccess;

import BusinessLayer.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportDaoImpl implements ReportDao {
    private Connection connection;
    private ProductsDao productsDao;
    private Map<Integer, MissingProductsReport> identityMissingReportMap;
    private Map<Integer, DefectiveProductsReport> identityDefectiveReportMap;
    private Map<Integer, WeeklyStorageReport> identityWeeklyReportMap;

    public ReportDaoImpl() throws SQLException {
        connection = DBConnector.connect();
        try {
            Statement statement = connection.createStatement();
            statement.execute("PRAGMA foreign_keys=ON;");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        identityMissingReportMap = new HashMap<>();
        identityDefectiveReportMap = new HashMap<>();
        identityWeeklyReportMap = new HashMap<>();
    }

    @Override
    public Map<Integer, MissingProductsReport> getAllMissingReports() throws SQLException {
        Map<Product, Integer> missingProductsMap = null;
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM MissingProductsReport");
        int reportID = -1;
        int branchID = -1;
        LocalDate reportDate = null;
        while (rs.next()) {
            if (identityMissingReportMap.containsKey(reportID)) continue;
            if (reportID != rs.getInt("ReportID") || branchID != rs.getInt("BranchID") ||
                    reportDate != rs.getObject("ReportDate", LocalDate.class)) {
                if (missingProductsMap != null) {
                    MissingProductsReport missingReport = new MissingProductsReport(reportID, branchID, reportDate, missingProductsMap);
                    identityMissingReportMap.put(reportID, missingReport);
                }
                reportID = rs.getInt("ReportID");
                branchID = rs.getInt("BranchID");
                reportDate = rs.getObject("ReportDate", LocalDate.class);
                missingProductsMap = new HashMap<>();
            }
            int productID = rs.getInt("ProductID");
            int amountToOrder = rs.getInt("AmountToOrder");
            missingProductsMap.put(productsDao.getProductByID(productID), amountToOrder);
        }
        return identityMissingReportMap;
    }

    @Override
    public Map<Integer, DefectiveProductsReport> getAllDefectiveReports() throws SQLException {
        Map<Item, String> defectiveItemsMap = null;
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM DefectiveProductsReport");
        int reportID = -1;
        int branchID = -1;
        LocalDate reportDate = null;
        while (rs.next()) {
            if (identityDefectiveReportMap.containsKey(reportID)) continue;
            if (reportID != rs.getInt("ReportID") || branchID != rs.getInt("BranchID") ||
                    reportDate != rs.getObject("ReportDate", LocalDate.class)) {
                if (defectiveItemsMap != null) {
                    DefectiveProductsReport defectiveReport = new DefectiveProductsReport(reportID, branchID, reportDate, defectiveItemsMap);
                    identityDefectiveReportMap.put(reportID, defectiveReport);
                }
                reportID = rs.getInt("ReportID");
                branchID = rs.getInt("BranchID");
                reportDate = rs.getObject("ReportDate", LocalDate.class);
                defectiveItemsMap = new HashMap<>();
            }
            int productID = rs.getInt("ProductID");
            int itemID = rs.getInt("ItemID");
            // Item item = itemsDao.getItemByID(productID, itemID);
            // defectiveItemsMap.put(item, item.getDefectiveDiscription());
        }
        return identityDefectiveReportMap;
    }

    @Override
    public Map<Integer, WeeklyStorageReport> getAllWeeklyReports() throws SQLException {
        Map<Product, Integer> weeklyProductsMap = null;
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM WeeklyStorageReport");
        int reportID = -1;
        int branchID = -1;
        LocalDate reportDate = null;
        while (rs.next()) {
            if (identityWeeklyReportMap.containsKey(reportID)) continue;
            if (reportID != rs.getInt("ReportID") || branchID != rs.getInt("BranchID") ||
                    reportDate != rs.getObject("ReportDate", LocalDate.class)) {
                if (weeklyProductsMap != null) {
                    WeeklyStorageReport missingReport = new WeeklyStorageReport(reportID, branchID, reportDate, weeklyProductsMap);
                    identityWeeklyReportMap.put(reportID, missingReport);
                }
                reportID = rs.getInt("ReportID");
                branchID = rs.getInt("BranchID");
                reportDate = rs.getObject("ReportDate", LocalDate.class);
                weeklyProductsMap = new HashMap<>();
            }
            int productID = rs.getInt("ProductID");
            int amountToOrder = rs.getInt("AmountToOrder");
            weeklyProductsMap.put(productsDao.getProductByID(productID), amountToOrder);
            //TODO: Check about add categories.
        }
        return identityWeeklyReportMap;
    }

    @Override
    public Report getReportByID(int reportID) throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM Report WHERE reportID = " + reportID);
        Report report = null;
        if (rs.next()) {
            String reportType = rs.getString("ReportType");
            Map<Product, Integer> reportProductsMap = new HashMap<>();
            int branchID;
            int productID;
            int amountToOrder;
            LocalDate reportDate;
            switch (reportType) {
                case "MissingProducts":
                    if (identityMissingReportMap.containsKey(report.getReportID())){return identityMissingReportMap.get(report.getReportID());}
                    rs = stmt.executeQuery("SELECT * FROM MissingProductsReport WHERE reportID = " + reportID);
                    rs.next();
                    branchID = rs.getInt("BranchID");
                    reportDate = rs.getObject("ReportDate", LocalDate.class);
                    productID = rs.getInt("ProductID");
                    amountToOrder = rs.getInt("AmountToOrder");
                    reportProductsMap.put(productsDao.getProductByID(productID), amountToOrder);
                    while (rs.next()) {
                        productID = rs.getInt("ProductID");
                        amountToOrder = rs.getInt("AmountToOrder");
                        reportProductsMap.put(productsDao.getProductByID(productID), amountToOrder);
                    }
                    report = new MissingProductsReport(reportID, branchID, reportDate, reportProductsMap);
                    break;
                case "DefectiveProducts":
                    if (identityDefectiveReportMap.containsKey(report.getReportID())){return identityDefectiveReportMap.get(report.getReportID());}
                    rs = stmt.executeQuery("SELECT * FROM DefectiveProductsReport WHERE reportID = " + reportID);
                    rs.next();
                    branchID = rs.getInt("BranchID");
                    reportDate = rs.getObject("ReportDate", LocalDate.class);
                    productID = rs.getInt("ProductID");
                    int itemID = rs.getInt("ItemID");
                    Map<Item, String> defectiveItemsMap = new HashMap<>();
                    // Item item = itemsDao.getItemByID(productID, itemID);
                    // defectiveItemsMap.put(item, item.getDefectiveDiscription());
                    while (rs.next()) {
                        productID = rs.getInt("ProductID");
                        itemID = rs.getInt("ItemID");
                        // Item item = itemsDao.getItemByID(productID, itemID);
                        // defectiveItemsMap.put(item, item.getDefectiveDiscription());
                    }
                    report = new DefectiveProductsReport(reportID, branchID, reportDate, defectiveItemsMap);
                    break;
                case "WeeklyStorage":
                    if (identityWeeklyReportMap.containsKey(report.getReportID())){return identityWeeklyReportMap.get(report.getReportID());}
                    rs = stmt.executeQuery("SELECT * FROM WeeklyStorageReport WHERE reportID = " + reportID);
                    rs.next();
                    branchID = rs.getInt("BranchID");
                    reportDate = rs.getObject("ReportDate", LocalDate.class);
                    productID = rs.getInt("ProductID");
                    amountToOrder = rs.getInt("AmountToOrder");
                    reportProductsMap.put(productsDao.getProductByID(productID), amountToOrder);
                    while (rs.next()) {
                        productID = rs.getInt("ProductID");
                        amountToOrder = rs.getInt("AmountToOrder");
                        reportProductsMap.put(productsDao.getProductByID(productID), amountToOrder);
                    }
                    report = new WeeklyStorageReport(reportID, branchID, reportDate, reportProductsMap);
                    break;
                default:
                    throw new SQLException("Invalid report type");
            }
        }
        return report;
    }

    @Override
    public void addReport(Report report) throws SQLException {
        String reportType = report.getReportType();
        PreparedStatement preparedStatement;
        switch (reportType) {
            case "Missing":
                MissingProductsReport missingReport = (MissingProductsReport)report;
                identityMissingReportMap.put(missingReport.getReportID(), missingReport);
                preparedStatement = connection.prepareStatement("INSERT INTO MissingProductsReport (ReportID, BranchID, ProductID, AmountToOrder) VALUES(?, ?, ?, ?)");
                preparedStatement.setInt(1, missingReport.getReportID());
                preparedStatement.setInt(2, missingReport.getBranchID());
                Map<Product, Integer> missingProductsMap = missingReport.getMap();
                for (Map.Entry<Product, Integer> entry : missingProductsMap.entrySet()) {
                    preparedStatement.setInt(3, entry.getKey().getProductID());
                    preparedStatement.setInt(4, entry.getValue());
                    preparedStatement.executeUpdate();
                }
            case "Defective":
                DefectiveProductsReport defectiveReport = (DefectiveProductsReport) report;
                identityDefectiveReportMap.put(defectiveReport.getReportID(), defectiveReport);
                preparedStatement = connection.prepareStatement("INSERT INTO DefectiveProductsReport (ReportID, BranchID, ProductID, ItemID) VALUES(?, ?, ?, ?)");
                preparedStatement.setInt(1, defectiveReport.getReportID());
                preparedStatement.setInt(2, defectiveReport.getBranchID());
                Map<Item, String> defectiveProductsMap = defectiveReport.getMap();
                for (Map.Entry<Item, String> entry : defectiveProductsMap.entrySet()) {
                    preparedStatement.setInt(3, entry.getKey().getProductID());
                    preparedStatement.setInt(4, entry.getKey().getItemID());
                    preparedStatement.executeUpdate();
                }
                break;
            case "Weekly":
                WeeklyStorageReport weeklyReport = (WeeklyStorageReport) report;
                identityWeeklyReportMap.put(weeklyReport.getReportID(), weeklyReport);
                preparedStatement = connection.prepareStatement("INSERT INTO WeeklyStorageReport (ReportID, BranchID, ProductID, AmountToOrder) VALUES(?, ?, ?, ?)");
                preparedStatement.setInt(1, weeklyReport.getReportID());
                preparedStatement.setInt(2, weeklyReport.getBranchID());
                Map<Product, Integer> weeklyProductsMap = weeklyReport.getMap();
                for (Map.Entry<Product, Integer> entry : weeklyProductsMap.entrySet()) {
                    preparedStatement.setInt(3, entry.getKey().getProductID());
                    preparedStatement.setInt(4, entry.getValue());
                    preparedStatement.executeUpdate();
                }
                break;
            default:
                throw new SQLException("Invalid report type");
        }
    }
}