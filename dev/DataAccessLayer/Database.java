package DataAccessLayer;
import java.sql.*;

public class Database {
    private static final String DB_URL = "jdbc:sqlite:res/superlee.db";
    private static Connection connection;

    private Database() {
    }

    public static Connection connect()
    {
        if(connection == null)
        {
            try { connection = DriverManager.getConnection(DB_URL); }
            catch (SQLException e) { System.out.println(e.getMessage()); }
        }
        //TODO: Add branchID FOREIGN KEY in supplierOrder table and periodicOrder table.
        return connection;
    }

    public static void testDAO()
    {
        String sql = "INSERT INTO supplier (supplierID, name, address, bankAccount) " +
                "VALUES (?, ?, ?, ?)";
        int supplierID = 2; // replace with the desired supplier ID
        String name = "Guy Biton"; // replace with the desired supplier name
        String address = "123 Main St, Anytown, USA"; // replace with the desired supplier address
        String bankAccount = "987654"; // replace with the desired supplier bank account
        try(PreparedStatement pstmt = connection.prepareStatement(sql))
        {
            pstmt.setInt(1, supplierID);
            pstmt.setString(2, name);
            pstmt.setString(3, address);
            pstmt.setString(4, bankAccount);
            pstmt.executeUpdate();
            sql = "INSERT INTO contact (supplierID, phoneNumber, name, email) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt2 = connection.prepareStatement(sql);
            pstmt2.setInt(1, 1);
            pstmt2.setString(2, "052-5993123");
            pstmt2.setString(3, "John Doe");
            pstmt2.setString(4, "johndoe@example.com");
            pstmt2.executeUpdate();
            sql = "INSERT INTO agreement (supplierID, paymentType, deliveryDays, selfSupply, supplyMethod, supplyTime) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt3 = connection.prepareStatement(sql);
            pstmt3.setInt(1, 1);
            pstmt3.setString(2, "Credit Card");
            pstmt3.setString(3, "None");
            pstmt3.setBoolean(4, true);
            pstmt3.setString(5, "DaysAmount");
            pstmt3.setInt(6, 3);
            pstmt3.executeUpdate();
            sql = "INSERT INTO discount (supplierID, type, amount, discount) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt4 = connection.prepareStatement(sql);
            pstmt4.setInt(1, 1);
            pstmt4.setString(2, "Amount");
            pstmt4.setDouble(3, 100);
            pstmt4.setDouble(4, 10);
            pstmt4.executeUpdate();
            sql = "INSERT INTO discount (supplierID, type, amount, discount) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt5 = connection.prepareStatement(sql);
            pstmt5.setInt(1, 1);
            pstmt5.setString(2, "Price");
            pstmt5.setDouble(3, 255);
            pstmt5.setDouble(4, 25);
            pstmt5.executeUpdate();
            sql = "INSERT INTO supplierProduct (supplierID, productID, catalogNumber, name, price, amount, weight, manufacturer, expirationDate) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt6 = connection.prepareStatement(sql);
            pstmt6.setInt(1, 1);
            pstmt6.setInt(2, 1);
            pstmt6.setInt(3, 12345);
            pstmt6.setString(4, "Product 1");
            pstmt6.setDouble(5, 10.5);
            pstmt6.setInt(6, 100);
            pstmt6.setDouble(7, 0.5);
            pstmt6.setString(8, "Manufacturer 1");
            pstmt6.setString(9, "01/04/2024");
            pstmt6.executeUpdate();
            sql = "INSERT INTO discountPerAmount (supplierID, productID, discountPerAmount, discount) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt7 = connection.prepareStatement(sql);
            pstmt7.setInt(1, 1);
            pstmt7.setInt(2, 1);
            pstmt7.setInt(3, 10);
            pstmt7.setDouble(4, 5);
            pstmt7.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error inserting row: " + e.getMessage());
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
                + "phoneNumber TEXT,\n"
                + "supplierID INTEGER NOT NULL,\n"
                + "name TEXT NOT NULL,\n"
                + "email TEXT NOT NULL,\n"
                + "PRIMARY KEY(phoneNumber),\n"
                + "FOREIGN KEY(supplierID) REFERENCES supplier(supplierID) ON UPDATE CASCADE ON DELETE CASCADE\n"
                + ");";

        String sql3 = "CREATE TABLE IF NOT EXISTS agreement (\n"
                + "supplierID INTEGER,\n"
                + "paymentType TEXT NOT NULL,\n"
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
                + "expirationDate TEXT NOT NULL,\n"
                + "PRIMARY KEY(supplierID,productID),\n"
                + "FOREIGN KEY(supplierID) REFERENCES supplier(supplierID) ON UPDATE CASCADE ON DELETE CASCADE\n"
                + ");";

        String sql6 = "CREATE TABLE IF NOT EXISTS discountPerAmount (\n"
                + "supplierID INTEGER,\n"
                + "productID INTEGER,\n"
                + "discountPerAmount INTEGER NOT NULL,\n"
                + "discount DOUBLE NOT NULL,\n"
                + "PRIMARY KEY(supplierID,productID, discountPerAmount),\n"
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
                + "PRIMARY KEY(orderID, productID),\n"
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
