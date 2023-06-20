package DataAccessLayer;
import java.sql.*;

public class Database {
    private static final String DB_URL = "jdbc:sqlite:identifier.sqlite";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }
    public static void closeConnection(Connection connection) {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
