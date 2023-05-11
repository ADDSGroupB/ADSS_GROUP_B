package DataAccess;

import BusinessLayer.*;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class ReportDaoImpl implements ReportDao {
    private Connection connection;
    private ProductsDao productsDao;
    private ItemsDao itemsDao;
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
        Statement stmt= null;
        ResultSet allReportTable = null;
        ResultSet missingAndWeeklyReportTable = null;
        try {
            stmt = connection.createStatement();
            allReportTable = stmt.executeQuery("SELECT * FROM AllReports");
            while (allReportTable.next()) {
                int reportID = allReportTable.getInt("ReportID");
                if (!Objects.equals(allReportTable.getString("ReportType"), "Missing") || identityMissingReportMap.containsKey(reportID)) continue;
                int branchID = allReportTable.getInt("BranchID");
                LocalDate reportDate = allReportTable.getObject("ReportDate", LocalDate.class);
                missingAndWeeklyReportTable = stmt.executeQuery("SELECT * FROM MissingAndWeeklyReports WHERE ReportID = " + reportID);
                while (missingAndWeeklyReportTable.next()) {
                    int productID = missingAndWeeklyReportTable.getInt("ProductID");
                    int amountToOrder = missingAndWeeklyReportTable.getInt("AmountToOrder");
                    missingProductsMap.put(productsDao.getProductByID(productID), amountToOrder);
                }
                MissingProductsReport missingReport = new MissingProductsReport(reportID, branchID, reportDate, missingProductsMap);
                identityMissingReportMap.put(reportID, missingReport);
            }
            return identityMissingReportMap;
        } catch (Exception e) {
            System.out.println("Error while getting all missing reports: " + e.getMessage());
            return null;
        } finally
        {
            if (allReportTable != null) {allReportTable.close();}
            if (missingAndWeeklyReportTable != null) {missingAndWeeklyReportTable.close();}
            if (stmt != null) {stmt.close();}
        }
    }

    @Override
    public Map<Integer, DefectiveProductsReport> getAllDefectiveReports() throws SQLException {
        Map<Item, String> defectiveItemsMap = null;
        Statement stmt= null;
        ResultSet allReportTable = null;
        ResultSet defectiveReportTable = null;
        try {
            stmt = connection.createStatement();
            allReportTable = stmt.executeQuery("SELECT * FROM AllReports");
            while (allReportTable.next()) {
                int reportID = allReportTable.getInt("ReportID");
                if (!Objects.equals(allReportTable.getString("ReportType"), "Defective") || identityDefectiveReportMap.containsKey(reportID)) continue;
                int branchID = allReportTable.getInt("BranchID");
                LocalDate reportDate = allReportTable.getObject("ReportDate", LocalDate.class);
                defectiveReportTable = stmt.executeQuery("SELECT * FROM DefectiveReports WHERE ReportID = " + reportID);
                while (defectiveReportTable.next()) {
                    int itemID = defectiveReportTable.getInt("ItemID");
                    Item item = itemsDao.getItemByID(itemID);
                    defectiveItemsMap.put(item, item.getDefectiveDiscription());
                }
                DefectiveProductsReport defectiveReport = new DefectiveProductsReport(reportID, branchID, reportDate, defectiveItemsMap);
                identityDefectiveReportMap.put(reportID, defectiveReport);
            }
            return identityDefectiveReportMap;
        } catch (Exception e) {
            System.out.println("Error while getting all missing reports: " + e.getMessage());
            return null;
        } finally
        {
            if (allReportTable != null) {allReportTable.close();}
            if (defectiveReportTable != null) {defectiveReportTable.close();}
            if (stmt != null) {stmt.close();}
        }
    }

    @Override
    public Map<Integer, WeeklyStorageReport> getAllWeeklyReports() throws SQLException {
        Map<Product, Integer> weeklyProductsMap = null;
        Statement stmt= null;
        ResultSet allReportTable = null;
        ResultSet missingAndWeeklyReportTable = null;
        try {
            stmt = connection.createStatement();
            allReportTable = stmt.executeQuery("SELECT * FROM AllReports");
            while (allReportTable.next()) {
                int reportID = allReportTable.getInt("ReportID");
                if (!Objects.equals(allReportTable.getString("ReportType"), "Weekly") || identityWeeklyReportMap.containsKey(reportID)) continue;
                int branchID = allReportTable.getInt("BranchID");
                LocalDate reportDate = allReportTable.getObject("ReportDate", LocalDate.class);
                missingAndWeeklyReportTable = stmt.executeQuery("SELECT * FROM MissingAndWeeklyReports WHERE ReportID = " + reportID);
                while (missingAndWeeklyReportTable.next()) {
                    int productID = missingAndWeeklyReportTable.getInt("ProductID");
                    int amountToOrder = missingAndWeeklyReportTable.getInt("AmountToOrder");
                    weeklyProductsMap.put(productsDao.getProductByID(productID), amountToOrder);
                }
                WeeklyStorageReport weeklyReport = new WeeklyStorageReport(reportID, branchID, reportDate, weeklyProductsMap);
                identityWeeklyReportMap.put(reportID, weeklyReport);
            }
            return identityWeeklyReportMap;
        } catch (Exception e) {
            System.out.println("Error while getting all missing reports: " + e.getMessage());
            return null;
        } finally
        {
            if (allReportTable != null) {allReportTable.close();}
            if (missingAndWeeklyReportTable != null) {missingAndWeeklyReportTable.close();}
            if (stmt != null) {stmt.close();}
        }
}

    @Override
    public Report getReportByID(int reportID) throws SQLException {
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery("SELECT * FROM AllReports WHERE ReportID = " + reportID);
            Report report = null;
            if (rs.next()) {
                String reportType = rs.getString("ReportType");
                int branchID = rs.getInt("BranchID");
                LocalDate reportDate = rs.getObject("ReportDate", LocalDate.class);
                Map<Product, Integer> reportProductsMap = new HashMap<>();
                int productID;
                int amountToOrder;
                switch (reportType) {
                    case "Missing":
                        if (identityMissingReportMap.containsKey(reportID)) {
                            return identityMissingReportMap.get(reportID);
                        }
                        rs = stmt.executeQuery("SELECT * FROM MissingAndWeeklyReports WHERE ReportID = " + reportID);
                        while (rs.next()) {
                            productID = rs.getInt("ProductID");
                            amountToOrder = rs.getInt("AmountToOrder");
                            reportProductsMap.put(productsDao.getProductByID(productID), amountToOrder);
                        }
                        report = new MissingProductsReport(reportID, branchID, reportDate, reportProductsMap);
                        identityMissingReportMap.put(reportID, (MissingProductsReport)report);
                        break;
                    case "Defective":
                        if (identityDefectiveReportMap.containsKey(reportID)) {
                            return identityDefectiveReportMap.get(reportID);
                        }
                        rs = stmt.executeQuery("SELECT * FROM DefectiveReport WHERE ReportID = " + reportID);
                        Map<Item, String> defectiveItemsMap = new HashMap<>();
                        while (rs.next()) {
                            int itemID = rs.getInt("ItemID");
                            Item item = itemsDao.getItemByID(itemID);
                            defectiveItemsMap.put(item, item.getDefectiveDiscription());
                        }
                        report = new DefectiveProductsReport(reportID, branchID, reportDate, defectiveItemsMap);
                        identityDefectiveReportMap.put(reportID, (DefectiveProductsReport)report);
                        break;
                    case "Weekly":
                        if (identityWeeklyReportMap.containsKey(reportID)) {
                            return identityWeeklyReportMap.get(reportID);
                        }
                        rs = stmt.executeQuery("SELECT * FROM MissingAndWeeklyReports WHERE ReportID = " + reportID);
                        while (rs.next()) {
                            productID = rs.getInt("ProductID");
                            amountToOrder = rs.getInt("AmountToOrder");
                            reportProductsMap.put(productsDao.getProductByID(productID), amountToOrder);
                        }
                        report = new WeeklyStorageReport(reportID, branchID, reportDate, reportProductsMap);
                        identityWeeklyReportMap.put(reportID, (WeeklyStorageReport)report);
                        break;
                    default:
                        throw new SQLException("Invalid report type");
                }
            }
            return report;
        } catch (Exception e) {
            System.out.println("Error while getting the report: " + e.getMessage());
            return null;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
        }
    }

    @Override
    public void addReport(Report report) throws SQLException {
        String reportType = report.getReportType();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("INSERT INTO AllReports (ReportID, ReportType, BranchID, ReportDate) VALUES(?, ?, ?, ?)");
            preparedStatement.setInt(1, report.getReportID());
            preparedStatement.setString(2, report.getReportType());
            preparedStatement.setInt(3, report.getBranchID());
            preparedStatement.setDate(4, Date.valueOf(report.getReportDate()));
        switch (reportType) {
            case "Missing":
                MissingProductsReport missingReport = (MissingProductsReport)report;
                identityMissingReportMap.put(missingReport.getReportID(), missingReport);
                Map<Product, Integer> missingProductsMap = missingReport.getMap();
                preparedStatement = connection.prepareStatement("INSERT INTO MissingAndWeeklyReports (ReportID, ProductID, AmountToOrder) VALUES(?, ?, ?)");
                for (Map.Entry<Product, Integer> entry : missingProductsMap.entrySet()) {
                    preparedStatement.setInt(1, missingReport.getReportID());
                    preparedStatement.setInt(2, entry.getKey().getProductID());
                    preparedStatement.setInt(3, entry.getValue());
                    preparedStatement.executeUpdate();
                }
            case "Defective":
                DefectiveProductsReport defectiveReport = (DefectiveProductsReport) report;
                identityDefectiveReportMap.put(defectiveReport.getReportID(), defectiveReport);
                Map<Item, String> defectiveProductsMap = defectiveReport.getMap();
                preparedStatement = connection.prepareStatement("INSERT INTO DefectiveReport (ReportID, ItemID) VALUES(?, ?)");
                for (Map.Entry<Item, String> entry : defectiveProductsMap.entrySet()) {
                    preparedStatement.setInt(1, defectiveReport.getReportID());
                    preparedStatement.setInt(2, entry.getKey().getItemID());
                    preparedStatement.executeUpdate();
                }
                break;
            case "Weekly":
                WeeklyStorageReport weeklyReport = (WeeklyStorageReport) report;
                identityWeeklyReportMap.put(weeklyReport.getReportID(), weeklyReport);
                Map<Product, Integer> weeklyProductsMap = weeklyReport.getMap();
                preparedStatement = connection.prepareStatement("INSERT INTO MissingAndWeeklyReports (ReportID, ProductID, AmountToOrder) VALUES(?, ?, ?)");
                for (Map.Entry<Product, Integer> entry : weeklyProductsMap.entrySet()) {
                    preparedStatement.setInt(1, weeklyReport.getReportID());
                    preparedStatement.setInt(2, entry.getKey().getProductID());
                    preparedStatement.setInt(3, entry.getValue());
                    preparedStatement.executeUpdate();
                }
                break;
            default:
                throw new SQLException("Invalid report type");
        }
        } catch (Exception e) {
            System.out.println("Error while adding the report: " + e.getMessage());
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        }
    }
}