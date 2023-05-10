package DataAccessLayer;
import java.sql.*;

public class Database {
    private static final String DB_URL = "jdbc:sqlite:C:\\Users\\guybi\\Desktop\\super- lee\\SPL231-Assignment3-student-template\\ADSS_GROUP_B\\res\\superlee.db";
    private static Connection connection;

    public static Connection connect() throws SQLException
    {
        if(connection == null)
        {
            try { connection = DriverManager.getConnection(DB_URL); }
            catch (SQLException e) { System.out.println(e.getMessage()); }
        }
        //TODO: Add branchID FOREIGN KEY in supplierOrder table and periodicOrder table.
        createTables();
        return connection;
    }
    private static void createTables()
    {
        String sql1 = "CREATE TABLE IF NOT EXISTS supplier (\n"
                + "supplierID INTEGER,\n"
                + "name TEXT NOT NULL,\n"
                + "address TEXT NOT NULL,\n"
                + "bankAccount TEXT NOT NULL UNIQUE,\n"
                + "PRIMARY KEY(supplierID)\n"
                + ");";

        String sql2 = "CREATE TABLE IF NOT EXISTS contact (\n"
                + "supplierID INTEGER,\n"
                + "phoneNumber TEXT,\n"
                + "name TEXT NOT NULL,\n"
                + "email TEXT NOT NULL,\n"
                + "PRIMARY KEY(supplierID,phoneNumber),\n"
                + "FOREIGN KEY(supplierID) REFERENCES supplier(supplierID) ON UPDATE CASCADE ON DELETE CASCADE\n"
                + ");";

        String sql3 = "CREATE TABLE IF NOT EXISTS agreement (\n"
                + "supplierID INTEGER,\n"
                + "deliveryDays TEXT NOT NULL,\n"
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
                + "PRIMARY KEY(supplierID),\n"
                + "FOREIGN KEY(supplierID) REFERENCES supplier(supplierID) ON UPDATE CASCADE ON DELETE CASCADE\n"
                + ");";

        String sql5 = "CREATE TABLE IF NOT EXISTS supplierProduct (\n"
                + "supplierID INTEGER,\n"
                + "productID INTEGER,\n"
                + "catalogNumber INTEGER NOT NULL,\n"
                + "name TEXT NOT NULL,\n"
                + "amount INTEGER NOT NULL,\n"
                + "price DOUBLE NOT NULL,\n"
                + "PRIMARY KEY(supplierID,productID),\n"
                + "FOREIGN KEY(supplierID) REFERENCES supplier(supplierID) ON UPDATE CASCADE ON DELETE CASCADE\n"
                + ");";

        String sql6 = "CREATE TABLE IF NOT EXISTS discountPerAmount (\n"
                + "supplierID INTEGER,\n"
                + "productID INTEGER,\n"
                + "discountPerAmount INTEGER NOT NULL,\n"
                + "discount DOUBLE NOT NULL,\n"
                + "PRIMARY KEY(supplierID,productID),\n"
                + "FOREIGN KEY(supplierID) REFERENCES supplier(supplierID) ON UPDATE CASCADE ON DELETE CASCADE,\n"
                + "FOREIGN KEY(productID) REFERENCES supplierProduct(productID) ON UPDATE CASCADE ON DELETE CASCADE\n"
                + ");";
        String sql7 = "CREATE TABLE IF NOT EXISTS supplierOrder (\n"
                + "supplierID INTEGER,\n"
                + "orderID INTEGER,\n"
                + "branchID INTEGER,\n"
                + "orderDate TEXT NOT NULL,\n"
                + "deliveryDate TEXT NOT NULL,\n"
                + "totalPrice DOUBLE NOT NULL,\n"
                + "PRIMARY KEY(orderID,supplierID),\n"
                + "FOREIGN KEY(supplierID) REFERENCES supplier(supplierID) ON UPDATE CASCADE\n"
                + ");";

        String sql8 = "CREATE TABLE IF NOT EXISTS itemsInOrder (\n"
                + "orderID INTEGER,\n"
                + "productID INTEGER NOT NULL,\n"
                + "amountInOrder INTEGER NOT NULL,\n"
                + "PRIMARY KEY(orderID),\n"
                + "FOREIGN KEY(orderID) REFERENCES supplierOrder(orderID) ON UPDATE CASCADE ON DELETE CASCADE,\n"
                + "FOREIGN KEY(productID) REFERENCES supplierProduct(productID) ON UPDATE CASCADE\n"
                + ");";

        String sql9 = "CREATE TABLE IF NOT EXISTS periodicOrder (\n"
                + "periodicOrderID INTEGER,\n"
                + "supplierID INTEGER,\n"
                + "branchID INTEGER,\n"
                + "fixedDay TEXT NOT NULL,\n"
                + "PRIMARY KEY(periodicOrderID,supplierID),\n"
                + "FOREIGN KEY(supplierID) REFERENCES supplier(supplierID) ON UPDATE CASCADE\n"
                + "-- FOREIGN KEY(branchID) REFERENCES branch(branchID) ON UPDATE CASCADE ON DELETE CASCADE\n"
                + ");";

        String sql10 = "CREATE TABLE IF NOT EXISTS itemsInPeriodicOrder (\n"
                + "periodicOrderID INTEGER,\n"
                + "productID INTEGER NOT NULL,\n"
                + "amountInOrder INTEGER NOT NULL,\n"
                + "PRIMARY KEY(periodicOrderID,productID),\n"
                + "FOREIGN KEY(periodicOrderID) REFERENCES periodicOrder(periodicOrderID) ON UPDATE CASCADE ON DELETE CASCADE,\n"
                + "FOREIGN KEY(productID) REFERENCES supplierProduct(productID) ON UPDATE CASCADE\n"
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

}
