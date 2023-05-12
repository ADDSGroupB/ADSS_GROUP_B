package InterfaceLayer;

import DataAccessLayer.Database;

import java.sql.SQLException;

public class Main
{
    public static void main(String[] args) {
        Cli cli = new Cli();
        cli.start();
    }
}
