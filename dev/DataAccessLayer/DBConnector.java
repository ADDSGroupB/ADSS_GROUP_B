package DataAccessLayer;
import java.sql.*;

public class DBConnector {
    private static final String DB_URL = "jdbc:sqlite:res/superlee.db";
    private static Connection connection;

    private DBConnector() {
    }

    public static Connection connect()
    {
        if(connection == null)
        {
            try { connection = DriverManager.getConnection(DB_URL); createTablesInventory(); createTables(); }
            catch (SQLException e) { System.out.println(e.getMessage()); }

        }
        return connection;
    }

    public static void disconnect()
    {
        try { if (connection != null) connection.close(); }
        catch (SQLException e) { System.out.println(e.getMessage()); }
    }

    public static void deleteRecordsOfTables() throws SQLException {
        String query1 = "DROP TABLE IF EXISTS supplier";
        String query2 = "DROP TABLE IF EXISTS contact";
        String query3 = "DROP TABLE IF EXISTS agreement";
        String query4 = "DROP TABLE IF EXISTS discount";
        String query5 = "DROP TABLE IF EXISTS supplierProduct";
        String query6 = "DROP TABLE IF EXISTS discountPerAmount";
        String query7 = "DROP TABLE IF EXISTS supplierOrder";
        String query8 = "DROP TABLE IF EXISTS itemsInOrder";
        String query9 = "DROP TABLE IF EXISTS periodicOrder";
        String query10 = "DROP TABLE IF EXISTS itemsInPeriodicOrder";
        String query11 = "DROP TABLE IF EXISTS deliveryDays";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(query1);
            stmt.execute(query2);
            stmt.execute(query3);
            stmt.execute(query4);
            stmt.execute(query6);
            stmt.execute(query7);
            stmt.execute(query8);
            stmt.execute(query9);
            stmt.execute(query10);
            stmt.execute(query11);
            stmt.execute(query5);
            createTables();
        }
    }
    public static void createTablesInventory()
    {
        String sql1 = "CREATE TABLE IF NOT EXISTS Branches (\n"
                + "BranchID INTEGER,\n"
                + "BranchName TEXT,\n"
                + "PRIMARY KEY(BranchID)\n"
                + ");";

        String sql2 = "CREATE TABLE IF NOT EXISTS Categories (\n"
                + "CategoryID INTEGER UNIQUE,\n"
                + "CategoryName TEXT NOT NULL UNIQUE,\n"
                + "PRIMARY KEY(CategoryID)\n"
                + ");";

        String sql3 = "CREATE TABLE IF NOT EXISTS Products (\n"
                + "ProductID INTEGER,\n"
                + "ProductName TEXT,\n"
                + "Manufacturer TEXT,\n"
                + "Weight INTEGER,\n"
                + "ParentCategory INTEGER,\n"
                + "SubCategory INTEGER,\n"
                + "SubSubCategory INTEGER,\n"
                + "FOREIGN KEY(SubSubCategory) REFERENCES Categories(CategoryID),\n"
                + "FOREIGN KEY(ParentCategory) REFERENCES Categories(CategoryID),\n"
                + "FOREIGN KEY(SubCategory) REFERENCES Categories(CategoryID),\n"
                + "PRIMARY KEY(ProductID)\n"
                + ");";

        String sql4 = "CREATE TABLE IF NOT EXISTS Discounts (\n"
                + "DiscountID INTEGER,\n"
                + "BranchID INTEGER,\n"
                + "ProductID INTEGER,\n"
                + "CategoryID INTEGER,\n"
                + "StartDate TEXT,\n"
                + "EndDate TEXT,\n"
                + "Amount INTEGER,\n"
                + "FOREIGN KEY(BranchID) REFERENCES Branches(BranchID),\n"
                + "FOREIGN KEY(ProductID) REFERENCES Products(ProductID),\n"
                + "FOREIGN KEY(CategoryID) REFERENCES Categories(CategoryID),\n"
                + "PRIMARY KEY(DiscountID)\n"
                + ");";

        String sql5 = "CREATE TABLE IF NOT EXISTS Items (\n"
                + "ItemID INTEGER,\n"
                + "BranchID INTEGER,\n"
                + "ProductID INTEGER,\n"
                + "SupplierID INTEGER,\n"
                + "ExpiredDate TEXT,\n"
                + "PriceFromSupplier INTEGER,\n"
                + "PriceInBranch INTEGER,\n"
                + "PriceAfterDiscount INTEGER,\n"
                + "Status TEXT,\n"
                + "DefectiveDiscription TEXT,\n"
                + "ArrivalDate TEXT,\n"
                + "FOREIGN KEY(BranchID) REFERENCES Branches(BranchID),\n"
                + "FOREIGN KEY(ProductID) REFERENCES Products(ProductID),\n"
                + "PRIMARY KEY(ItemID)\n"
                + ");";

        String sql6 = "CREATE TABLE IF NOT EXISTS ProductMinAmount (\n"
                + "ProductID INTEGER,\n"
                + "BranchID INTEGER,\n"
                + "MinAmount INTEGER,\n"
                + "OrderStatus TEXT,\n"
                + "FOREIGN KEY(BranchID) REFERENCES Branches(BranchID),\n"
                + "FOREIGN KEY(ProductID) REFERENCES Products(ProductID),\n"
                + "PRIMARY KEY(ProductID,BranchID)\n"
                + ");";
        String sql7 = "CREATE TABLE IF NOT EXISTS AllReports (\n"
                + "ReportID INTEGER,\n"
                + "ReportType TEXT,\n"
                + "BranchID INTEGER,\n"
                + "ReportDate TEXT,\n"
                + "FOREIGN KEY(BranchID) REFERENCES Branches(BranchID),\n"
                + "PRIMARY KEY(ReportID)\n"
                + ");";

        String sql8 = "CREATE TABLE IF NOT EXISTS DefectiveReport (\n"
                + "ReportID INTEGER,\n"
                + "ItemID INTEGER,\n"
                + "FOREIGN KEY(ItemID) REFERENCES Items(ItemID),\n"
                + "FOREIGN KEY(ReportID) REFERENCES AllReports(ReportID),\n"
                + "PRIMARY KEY(ReportID, ItemID)\n"
                + ");";

        String sql9 = "CREATE TABLE IF NOT EXISTS MissingReports (\n"
                + "ReportID INTEGER,\n"
                + "ProductID INTEGER,\n"
                + "AmountToOrder INTEGER,\n"
                + "FOREIGN KEY(ProductID) REFERENCES Products(ProductID),\n"
                + "FOREIGN KEY(ReportID) REFERENCES AllReports(ReportID),\n"
                + "PRIMARY KEY(ReportID, ProductID)\n"
                + ");";

        String sql10 = "CREATE TABLE IF NOT EXISTS WeeklyReports (\n"
                + "ReportID INTEGER,\n"
                + "CategoryID INTEGER,\n"
                + "ProductID INTEGER,\n"
                + "AmountInBranch INTEGER,\n"
                + "FOREIGN KEY(ReportID) REFERENCES AllReports(ReportID),\n"
                + "FOREIGN KEY(CategoryID) REFERENCES Categories(CategoryID),\n"
                + "PRIMARY KEY(ReportID, CategoryID, ProductID)\n"
                + ");";
        // create all tables
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql1);
            stmt.execute(sql2);
            stmt.execute(sql3);
            stmt.execute(sql4);
            stmt.execute(sql5);
            stmt.execute(sql6);
            stmt.execute(sql7);
            stmt.execute(sql8);
            stmt.execute(sql9);
            stmt.execute(sql10);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public static void createTables()
    {
        String sql1 = "CREATE TABLE IF NOT EXISTS supplier (\n"
                + "supplierID INTEGER,\n"
                + "name TEXT NOT NULL,\n"
                + "address TEXT NOT NULL,\n"
                + "bankAccount TEXT NOT NULL UNIQUE,\n"
                + "PRIMARY KEY(supplierID)\n"
                + ");";

        String sql2 = "CREATE TABLE IF NOT EXISTS contact (\n"
                + "supplierID INTEGER NOT NULL,\n"
                + "phoneNumber TEXT,\n"
                + "name TEXT NOT NULL,\n"
                + "email TEXT NOT NULL,\n"
                + "PRIMARY KEY(phoneNumber),\n"
                + "CONSTRAINT fk_contact FOREIGN KEY (supplierID) REFERENCES supplier(supplierID) ON DELETE CASCADE ON UPDATE CASCADE\n"
//                + "FOREIGN KEY(supplierID) REFERENCES supplier(supplierID) ON UPDATE CASCADE ON DELETE CASCADE\n"
                + ");";

        String sql3 = "CREATE TABLE IF NOT EXISTS agreement (\n"
                + "supplierID INTEGER,\n"
                + "paymentType TEXT NOT NULL,\n"
                + "selfSupply BOOLEAN NOT NULL,\n"
                + "supplyMethod TEXT NOT NULL,\n"
                + "supplyTime INTEGER NOT NULL,\n"
                + "PRIMARY KEY(supplierID),\n"
                + "FOREIGN KEY(supplierID) REFERENCES supplier(supplierID) ON UPDATE CASCADE ON DELETE CASCADE\n"
                + ");";

        String sql4 = "CREATE TABLE IF NOT EXISTS discount (\n"
                + "supplierID INTEGER,\n"
                + "type TEXT NOT NULL,\n"
                + "amount DOUBLE NOT NULL,\n"
                + "discount DOUBLE NOT NULL,\n"
                + "PRIMARY KEY(supplierID, type),\n"
                + "FOREIGN KEY(supplierID) REFERENCES supplier(supplierID) ON UPDATE CASCADE ON DELETE CASCADE\n"
                + ");";

        String sql5 = "CREATE TABLE IF NOT EXISTS supplierProduct (\n"
                + "supplierID INTEGER,\n"
                + "productID INTEGER,\n"
                + "catalogNumber INTEGER NOT NULL,\n"
                + "name TEXT NOT NULL,\n"
                + "amount INTEGER NOT NULL,\n"
                + "price DOUBLE NOT NULL,\n"
                + "weight DOUBLE NOT NULL,\n"
                + "manufacturer TEXT NOT NULL,\n"
                + "expirationDays TEXT NOT NULL,\n"
                + "PRIMARY KEY(supplierID,productID),\n"
                + "FOREIGN KEY(supplierID) REFERENCES supplier(supplierID) ON UPDATE CASCADE ON DELETE CASCADE\n"
                + ");";

        String sql6 = "CREATE TABLE IF NOT EXISTS discountPerAmount (\n"
                + "supplierID INTEGER,\n"
                + "productID INTEGER,\n"
                + "discountPerAmount INTEGER NOT NULL,\n"
                + "discount DOUBLE NOT NULL,\n"
                + "PRIMARY KEY(supplierID,productID, discountPerAmount),\n"
                + "FOREIGN KEY(supplierID, productID) REFERENCES supplierProduct(supplierID, productID) ON UPDATE CASCADE ON DELETE CASCADE\n"
                + ");";
        String sql7 = "CREATE TABLE IF NOT EXISTS supplierOrder (\n"
                + "orderID INTEGER,\n"
                + "supplierID INTEGER NOT NULL,\n"
                + "supplierName TEXT NOT NULL,\n"
                + "supplierAddress TEXT NOT NULL,\n"
                + "contactPhoneNumber TEXT NOT NULL,\n"
                + "branchID INTEGER NOT NULL,\n"
                + "creationDate TEXT NOT NULL,\n"
                + "deliveryDate TEXT NOT NULL,\n"
                + "collected BOOLEAN NOT NULL,\n"
                + "totalPriceBeforeDiscount DOUBLE NOT NULL,\n"
                + "totalPriceAfterDiscount DOUBLE NOT NULL,\n"
                + "PRIMARY KEY(orderID),\n"
                + "FOREIGN KEY(supplierID) REFERENCES supplier(supplierID) ON UPDATE CASCADE,\n"
                + "FOREIGN KEY(branchID) REFERENCES Branches(branchID) ON UPDATE CASCADE ON DELETE CASCADE\n"
                + ");";

        String sql8 = "CREATE TABLE IF NOT EXISTS itemsInOrder (\n"
                + "orderID INTEGER,\n"
                + "supplierID INTEGER NOT NULL,\n"
                + "productID INTEGER NOT NULL,\n"
                + "amountInOrder INTEGER NOT NULL,\n"
                + "PRIMARY KEY(orderID, productID),\n"
                + "FOREIGN KEY(orderID) REFERENCES supplierOrder(orderID) ON UPDATE CASCADE ON DELETE CASCADE,\n"
                + "FOREIGN KEY(productID, supplierID) REFERENCES supplierProduct(productID, supplierID) ON UPDATE CASCADE\n"
                + ");";

        String sql9 = "CREATE TABLE IF NOT EXISTS periodicOrder (\n"
                + "periodicOrderID INTEGER,\n"
                + "supplierID INTEGER NOT NULL,\n"
                + "branchID INTEGER NOT NULL,\n"
                + "fixedDay TEXT NOT NULL,\n"
                + "PRIMARY KEY(periodicOrderID),\n"
                + "FOREIGN KEY(supplierID) REFERENCES supplier(supplierID) ON UPDATE CASCADE,\n"
                + "FOREIGN KEY(branchID) REFERENCES Branches(branchID) ON UPDATE CASCADE ON DELETE CASCADE\n"
                + ");";

        String sql10 = "CREATE TABLE IF NOT EXISTS itemsInPeriodicOrder (\n"
                + "periodicOrderID INTEGER,\n"
                + "productID INTEGER NOT NULL,\n"
                + "supplierID INTEGER NOT NULL,\n"
                + "amountInOrder INTEGER NOT NULL,\n"
                + "PRIMARY KEY(periodicOrderID, productID),\n"
                + "FOREIGN KEY(periodicOrderID) REFERENCES periodicOrder(periodicOrderID) ON UPDATE CASCADE ON DELETE CASCADE,\n"
                + "FOREIGN KEY(productID, supplierID) REFERENCES supplierProduct(productID, supplierID) ON UPDATE CASCADE\n"
                + ");";
        String sql11 = "CREATE TABLE IF NOT EXISTS deliveryDays (\n"
                + "supplierID INTEGER,\n"
                + "day TEXT NOT NULL,\n"
                + "PRIMARY KEY(supplierID, day),\n"
                + "CONSTRAINT fk_deliveryDays FOREIGN KEY (supplierID) REFERENCES agreement(supplierID) ON DELETE CASCADE ON UPDATE CASCADE\n"
                + ");";

        // create all tables
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql1);
            stmt.execute(sql2);
            stmt.execute(sql3);
            stmt.execute(sql4);
            stmt.execute(sql5);
            stmt.execute(sql6);
            stmt.execute(sql7);
            stmt.execute(sql8);
            stmt.execute(sql9);
            stmt.execute(sql10);
            stmt.execute(sql11);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public static void deleteRecordsOfInventoryTables() throws SQLException
    {
        String dropQuery1 = "DROP TABLE IF EXISTS WeeklyReports";
        String dropQuery2 = "DROP TABLE IF EXISTS MissingReports";
        String dropQuery3 = "DROP TABLE IF EXISTS DefectiveReport";
        String dropQuery4 = "DROP TABLE IF EXISTS AllReports";
        String dropQuery5 = "DROP TABLE IF EXISTS ProductMinAmount";
        String dropQuery6 = "DROP TABLE IF EXISTS Items";
        String dropQuery7 = "DROP TABLE IF EXISTS Discounts";
        String dropQuery8 = "DROP TABLE IF EXISTS Products";
        String dropQuery9 = "DROP TABLE IF EXISTS Categories";
        String dropQuery10 = "DROP TABLE IF EXISTS Branches";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(dropQuery1);
            stmt.execute(dropQuery2);
            stmt.execute(dropQuery3);
            stmt.execute(dropQuery4);
            stmt.execute(dropQuery5);
            stmt.execute(dropQuery6);
            stmt.execute(dropQuery7);
            stmt.execute(dropQuery8);
            stmt.execute(dropQuery9);
            stmt.execute(dropQuery10);
            createTablesInventory();
        }
    }
}