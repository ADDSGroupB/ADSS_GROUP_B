package DataAccessLayer.InventoryDataAccessLayer;
import java.sql.*;
public class DBConnector {
    private static final String DB_URL = "jdbc:sqlite:/Users/Dan/Desktop/Inventory.db";
    private static Connection connection;
    private DBConnector() {}
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
}
