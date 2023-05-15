package InterfaceLayer;

import BusinessLayer.InventoryBusinessLayer.Branch;
import BusinessLayer.InventoryBusinessLayer.Category;
import DataAccessLayer.DBConnector;
import DataAccessLayer.InventoryDataAccessLayer.*;
import InterfaceLayer.InventoryInterfaceLayer.Cli;

import java.sql.SQLException;
public class Main {
    public static void main(String[] args) throws SQLException {
        Cli cli = new Cli();
        cli.Start();
    }
}

