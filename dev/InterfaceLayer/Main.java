package InterfaceLayer;

import InterfaceLayer.InventoryInterfaceLayer.Cli;

import java.sql.SQLException;
public class Main {
    public static void main(String[] args) throws SQLException {
        Cli cli = new Cli();
        cli.Start();
    }
}

