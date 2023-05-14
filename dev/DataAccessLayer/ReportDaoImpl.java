package DataAccessLayer;

import BusinessLayer.*;

import java.sql.*;
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
        productsDao = new ProductsDaoImpl();
        itemsDao = new ItemsDaoImpl();
    }

    @Override
    public Map<Integer, MissingProductsReport> getAllMissingReports() throws SQLException {
        Map<Product, Integer> missingProductsMap = new HashMap<>();
        Statement stmt1= null;
        Statement stmt2= null;
        ResultSet allReportTable = null;
        ResultSet missingAndWeeklyReportTable = null;
        try {
            stmt1 = connection.createStatement();
            allReportTable = stmt1.executeQuery("SELECT * FROM AllReports");
            while (allReportTable.next()) {
                int reportID = allReportTable.getInt("ReportID");
                if (!Objects.equals(allReportTable.getString("ReportType"), "Missing") || identityMissingReportMap.containsKey(reportID)) continue;
                int branchID = allReportTable.getInt("BranchID");
                LocalDate reportDate = LocalDate.parse(allReportTable.getString("ReportDate"));
                stmt2 = connection.createStatement();
                missingAndWeeklyReportTable = stmt2.executeQuery("SELECT * FROM MissingAndWeeklyReports WHERE ReportID = " + reportID);
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
            if (stmt1 != null) {stmt1.close();}
            if (stmt2 != null) {stmt2.close();}
        }
    }

    @Override
    public Map<Integer, DefectiveProductsReport> getAllDefectiveReports() throws SQLException {
        ArrayList<Item> defectiveItemsMap = new ArrayList<>();
        Statement stmt1 = null;
        Statement stmt2 = null;
        ResultSet allReportTable = null;
        ResultSet defectiveReportTable = null;
        try {
            stmt1 = connection.createStatement();
            stmt2 = connection.createStatement();
            allReportTable = stmt1.executeQuery("SELECT * FROM AllReports");
            while (allReportTable.next()) {
                int reportID = allReportTable.getInt("ReportID");
                if (!Objects.equals(allReportTable.getString("ReportType"), "Defective") || identityDefectiveReportMap.containsKey(reportID)) continue;
                int branchID = allReportTable.getInt("BranchID");
                LocalDate reportDate = LocalDate.parse(allReportTable.getString("ReportDate"));
                defectiveReportTable = stmt2.executeQuery("SELECT * FROM DefectiveReports WHERE ReportID = " + reportID);
                while (defectiveReportTable.next()) {
                    int itemID = defectiveReportTable.getInt("ItemID");
                    Item item = itemsDao.getItemByID(itemID);
                    defectiveItemsMap.add(item);
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
            if (stmt1 != null) {stmt1.close();}
            if (stmt2 != null) {stmt2.close();}        }
    }

    @Override
    public Map<Integer, WeeklyStorageReport> getAllWeeklyReports() throws SQLException {
        Map<Product, Integer> weeklyProductsMap = new HashMap<>();
        Statement stmt1= null;
        Statement stmt2= null;
        ResultSet allReportTable = null;
        ResultSet missingAndWeeklyReportTable = null;
        try {
            stmt1 = connection.createStatement();
            stmt2 = connection.createStatement();
            allReportTable = stmt1.executeQuery("SELECT * FROM AllReports");
            while (allReportTable.next()) {
                int reportID = allReportTable.getInt("ReportID");
                if (!Objects.equals(allReportTable.getString("ReportType"), "Weekly") || identityWeeklyReportMap.containsKey(reportID)) continue;
                int branchID = allReportTable.getInt("BranchID");
                LocalDate reportDate = LocalDate.parse(allReportTable.getString("ReportDate"));
                missingAndWeeklyReportTable = stmt2.executeQuery("SELECT * FROM MissingAndWeeklyReports WHERE ReportID = " + reportID);
                while (missingAndWeeklyReportTable.next()) {
                    int productID = missingAndWeeklyReportTable.getInt("ProductID");
                    int amount = missingAndWeeklyReportTable.getInt("AmountToOrder");
                    weeklyProductsMap.put(productsDao.getProductByID(productID), amount);
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
            if (stmt1 != null) {stmt1.close();}
            if (stmt2 != null) {stmt2.close();}
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
                LocalDate reportDate = LocalDate.parse(rs.getString("ReportDate"));
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
                        ArrayList<Item> defectiveItems = new ArrayList<>();
                        while (rs.next()) {
                            int itemID = rs.getInt("ItemID");
                            Item item = itemsDao.getItemByID(itemID);
                            defectiveItems.add(item);
                        }
                        report = new DefectiveProductsReport(reportID, branchID, reportDate, defectiveItems);
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
            preparedStatement.setString(4, report.getReportDate().toString());
            preparedStatement.executeUpdate();
            switch (reportType) {
                case "Missing":
                    MissingProductsReport missingReport = (MissingProductsReport)report;
                    identityMissingReportMap.put(missingReport.getReportID(), missingReport);
                    Map<Product, Integer> missingProductsMap = missingReport.getMissingProductsMap();
                    preparedStatement = connection.prepareStatement("INSERT INTO MissingAndWeeklyReports (ReportID, ProductID, AmountToOrder) VALUES(?, ?, ?)");
                    for (Map.Entry<Product, Integer> entry : missingProductsMap.entrySet()) {
                        preparedStatement.setInt(1, missingReport.getReportID());
                        preparedStatement.setInt(2, entry.getKey().getProductID());
                        preparedStatement.setInt(3, entry.getValue());
                        preparedStatement.executeUpdate();
                    }
                    break;
                case "Defective":
                    DefectiveProductsReport defectiveReport = (DefectiveProductsReport) report;
                    identityDefectiveReportMap.put(defectiveReport.getReportID(), defectiveReport);
                    ArrayList<Item> defectiveItemsList = defectiveReport.getDefectiveItemsList();
                    preparedStatement = connection.prepareStatement("INSERT INTO DefectiveReport (ReportID, ItemID) VALUES(?, ?)");
                    for (Item item : defectiveItemsList) {
                        preparedStatement.setInt(1, defectiveReport.getReportID());
                        preparedStatement.setInt(2, item.getItemID());
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
    @Override
    public int getNewReportID() throws SQLException{
        ResultSet rs = connection.createStatement().executeQuery("SELECT MAX(ReportID) FROM AllReports");
        return (rs.getInt("ReportID") + 1);
    }
}