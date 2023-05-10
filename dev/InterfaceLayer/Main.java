package InterfaceLayer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main
{
    public static void main(String args[]) throws SQLException {
//        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306", "root", "363636");

        Cli cli = new Cli();
        cli.start();
    }
}
