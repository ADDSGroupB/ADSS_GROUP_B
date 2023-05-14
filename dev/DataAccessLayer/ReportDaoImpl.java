package DataAccessLayer;
import BusinessLayer.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;
public class ReportDaoImpl implements ReportDao {
    private Connection connection;
    private ProductsDao productsDao;
    private ItemsDao itemsDao;
    private MainController mainController;
    private Map<Integer, MissingProductsReport> identityMissingReportMap;
    private Map<Integer, DefectiveProductsReport> identityDefectiveReportMap;
    private Map<Integer, WeeklyStorageReport> identityWeeklyReportMap;

    public ReportDaoImpl(MainController m) throws SQLException {
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
        mainController = m;
    }
    @Override
    public Map<Integer, MissingProductsReport> getAllMissingReports() throws SQLException {
        Map<Product, Integer> missingProductsMap = new HashMap<>();
        Statement stmt1= null;
        Statement stmt2= null;
        ResultSet allReportTable = null;
        ResultSet missingReportTable = null;
        try {
            stmt1 = connection.createStatement();
            allReportTable = stmt1.executeQuery("SELECT * FROM AllReports WHERE ReportType = Missing");
            while (allReportTable.next()) {
                int reportID = allReportTable.getInt("ReportID");
                if (identityMissingReportMap.containsKey(reportID)) continue;
                int branchID = allReportTable.getInt("BranchID");
                LocalDate reportDate = LocalDate.parse(allReportTable.getString("ReportDate"));
                stmt2 = connection.createStatement();
                missingReportTable = stmt2.executeQuery("SELECT * FROM MissingReports WHERE ReportID = " + reportID);
                while (missingReportTable.next()) {
                    int productID = missingReportTable.getInt("ProductID");
                    int amountToOrder = missingReportTable.getInt("AmountToOrder");
                    missingProductsMap.put(mainController.getProductsDao().getProductByID(productID), amountToOrder);
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
            if (missingReportTable != null) {missingReportTable.close();}
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
            allReportTable = stmt1.executeQuery("SELECT * FROM AllReports WHERE ReportType = Defective");
            while (allReportTable.next()) {
                int reportID = allReportTable.getInt("ReportID");
                if (identityDefectiveReportMap.containsKey(reportID)) continue;
                int branchID = allReportTable.getInt("BranchID");
                LocalDate reportDate = LocalDate.parse(allReportTable.getString("ReportDate"));
                defectiveReportTable = stmt2.executeQuery("SELECT * FROM DefectiveReports WHERE ReportID = " + reportID);
                while (defectiveReportTable.next()) {
                    int itemID = defectiveReportTable.getInt("ItemID");
                    Item item = mainController.getItemsDao().getItemByID(itemID);
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
        Map<Category, Map<Product, Integer>> weeklyCategoriesMap = new HashMap<>();
        Statement stmt1= null;
        Statement stmt2= null;
        ResultSet allReportTable = null;
        ResultSet weeklyReportTable = null;
        try {
            stmt1 = connection.createStatement();
            stmt2 = connection.createStatement();
            allReportTable = stmt1.executeQuery("SELECT * FROM AllReports WHERE ReportType = Weekly");
            while (allReportTable.next()) {
                int reportID = allReportTable.getInt("ReportID");
                if (identityWeeklyReportMap.containsKey(reportID)) continue;
                int branchID = allReportTable.getInt("BranchID");
                LocalDate reportDate = LocalDate.parse(allReportTable.getString("ReportDate"));
                weeklyReportTable = stmt2.executeQuery("SELECT * FROM WeeklyReports WHERE ReportID = " + reportID);
                int categoryID = 0;
                while (weeklyReportTable.next()) {
                    if (categoryID != weeklyReportTable.getInt("CategoryID")){
                        if (!weeklyProductsMap.isEmpty()){
                            weeklyCategoriesMap.put(mainController.getCategoryDao().getCategoryByID(categoryID), weeklyProductsMap);
                        }
                        categoryID = weeklyReportTable.getInt("CategoryID");
                    }
                    int productID = weeklyReportTable.getInt("ProductID");
                    int amount = weeklyReportTable.getInt("AmountInBranch");
                    weeklyProductsMap.put(mainController.getProductsDao().getProductByID(productID), amount);
                }
                WeeklyStorageReport weeklyReport = new WeeklyStorageReport(reportID, branchID, reportDate, weeklyCategoriesMap);
                identityWeeklyReportMap.put(reportID, weeklyReport);
            }
            return identityWeeklyReportMap;
        } catch (Exception e) {
            System.out.println("Error while getting all missing reports: " + e.getMessage());
            return null;
        } finally
        {
            if (allReportTable != null) {allReportTable.close();}
            if (weeklyReportTable != null) {weeklyReportTable.close();}
            if (stmt1 != null) {stmt1.close();}
            if (stmt2 != null) {stmt2.close();}
        }
    }
    @Override
    public Report getReportByID(int reportID) throws SQLException {
        Statement stmt1 = null;
        Statement stmt2 = null;
        ResultSet allReportsTable = null;
        ResultSet specificReportsTable = null;
        try {
            stmt1 = connection.createStatement();
            stmt2 = connection.createStatement();
            allReportsTable = stmt1.executeQuery("SELECT * FROM AllReports WHERE ReportID = " + reportID);
            Report report = null;
            if (allReportsTable.next()) {
                String reportType = allReportsTable.getString("ReportType");
                int branchID = allReportsTable.getInt("BranchID");
                LocalDate reportDate = LocalDate.parse(allReportsTable.getString("ReportDate"));
                Map<Product, Integer> reportProductsMap = new HashMap<>();
                Map<Category, Map<Product, Integer>> weeklyCategoriesMap = new HashMap<>();
                int productID;
                int amountToOrder;
                switch (reportType) {
                    case "Missing":
                        if (identityMissingReportMap.containsKey(reportID)) {
                            return identityMissingReportMap.get(reportID);
                        }
                        specificReportsTable = stmt2.executeQuery("SELECT * FROM MissingReports WHERE ReportID = " + reportID);
                        while (specificReportsTable.next()) {
                            productID = specificReportsTable.getInt("ProductID");
                            amountToOrder = specificReportsTable.getInt("AmountToOrder");
                            reportProductsMap.put(mainController.getProductsDao().getProductByID(productID), amountToOrder);
                        }
                        report = new MissingProductsReport(reportID, branchID, reportDate, reportProductsMap);
                        identityMissingReportMap.put(reportID, (MissingProductsReport)report);
                        break;
                    case "Defective":
                        if (identityDefectiveReportMap.containsKey(reportID)) {
                            return identityDefectiveReportMap.get(reportID);
                        }
                        specificReportsTable = stmt2.executeQuery("SELECT * FROM DefectiveReport WHERE ReportID = " + reportID);
                        ArrayList<Item> defectiveItems = new ArrayList<>();
                        while (specificReportsTable.next()) {
                            int itemID = specificReportsTable.getInt("ItemID");
                            Item item = mainController.getItemsDao().getItemByID(itemID);
                            defectiveItems.add(item);
                        }
                        report = new DefectiveProductsReport(reportID, branchID, reportDate, defectiveItems);
                        identityDefectiveReportMap.put(reportID, (DefectiveProductsReport)report);
                        break;
                    case "Weekly":
                        if (identityWeeklyReportMap.containsKey(reportID)) {
                            return identityWeeklyReportMap.get(reportID);
                        }
                        specificReportsTable = stmt2.executeQuery("SELECT * FROM WeeklyReports WHERE ReportID = " + reportID);
                        int categoryID = 0;
                        while (specificReportsTable.next()) {
                            if (categoryID != specificReportsTable.getInt("CategoryID")){
                                if (!reportProductsMap.isEmpty()){
                                    weeklyCategoriesMap.put(mainController.getCategoryDao().getCategoryByID(categoryID), reportProductsMap);
                                }
                                categoryID = specificReportsTable.getInt("CategoryID");
                            }
                            productID = specificReportsTable.getInt("ProductID");
                            amountToOrder = specificReportsTable.getInt("AmountInBranch");
                            reportProductsMap.put(productsDao.getProductByID(productID), amountToOrder);
                        }
                        report = new WeeklyStorageReport(reportID, branchID, reportDate, weeklyCategoriesMap);
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
            if (allReportsTable != null) {allReportsTable.close();}
            if (specificReportsTable != null) {specificReportsTable.close();}
            if (stmt1 != null) {stmt1.close();}
            if (stmt2 != null) {stmt2.close();}
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
                    preparedStatement = connection.prepareStatement("INSERT INTO MissingReports (ReportID, ProductID, AmountToOrder) VALUES(?, ?, ?)");
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
                    Map<Category, Map<Product, Integer>> weeklyReportMap = weeklyReport.getWeeklyReportMap();
                    preparedStatement = connection.prepareStatement("INSERT INTO WeeklyReports (ReportID, CategoryID, ProductID, AmountToOrder) VALUES(?, ?, ?, ?)");
                    for (Category category : weeklyReportMap.keySet()){
                        for (Map.Entry<Product, Integer> entry : weeklyReportMap.get(category).entrySet()) {
                            preparedStatement.setInt(1, weeklyReport.getReportID());
                            preparedStatement.setInt(2, category.getCategoryID());
                            preparedStatement.setInt(3, entry.getKey().getProductID());
                            preparedStatement.setInt(4, entry.getValue());
                            preparedStatement.executeUpdate();
                        }
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
    public void addLineToWeeklyReport(int reportID, int categoryID, int productID, int amountInBranch) throws SQLException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("INSERT INTO WeeklyReports (ReportID, CategoryID, ProductID, AmountToOrder) VALUES(?, ?, ?, ?)");
            preparedStatement.setInt(1, reportID);
            preparedStatement.setInt(2, categoryID);
            preparedStatement.setInt(3, productID);
            preparedStatement.setInt(4, amountInBranch);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error while adding the line: " + e.getMessage());
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        }
    }
    @Override
    public void addLineToMissingReport(int reportID, int productID, int amountInBranch) throws SQLException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("INSERT INTO MissingReports (ReportID, ProductID, AmountToOrder) VALUES(?, ?, ?)");
            preparedStatement.setInt(1, reportID);
            preparedStatement.setInt(2, productID);
            preparedStatement.setInt(3, amountInBranch);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error while adding the line: " + e.getMessage());
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        }
    }
    @Override
    public void addLineToDefectiveReport(int reportID, int itemID) throws SQLException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("INSERT INTO DefectiveReports (ReportID, ItemID) VALUES(?, ?)");
            preparedStatement.setInt(1, reportID);
            preparedStatement.setInt(2, itemID);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error while adding the line: " + e.getMessage());
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