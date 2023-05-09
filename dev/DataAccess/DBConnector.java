package DataAccess;
import java.sql.*;
public class DBConnector {
    public static final String DB_URL = "jdbc:sqlite:Inventory.db";
    public static Connection connect() throws  SQLException
    {
        return DriverManager.getConnection(DB_URL);
    }

}
