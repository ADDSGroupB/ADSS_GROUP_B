package InterfaceLayer;

import BusinessLayer.*;
import DataAccess.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws SQLException {
//        Cli cli = new Cli();
//        cli.start();

        ProductsDao productsDao = new ProductsDaoImpl();
        CategoryDao categoryDao = new CategoryDaoImpl();
        BranchesDao branchesDao = new BranchesDaoImpl();
        ItemsDao itemsDao = new ItemsDaoImpl();






    }
}

