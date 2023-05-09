package InterfaceLayer;

import BusinessLayer.Product;
import DataAccess.ProductsDao;
import DataAccess.ProductsDaoImpl;

import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws SQLException {
        ProductsDao productsDao = new ProductsDaoImpl();
        Product product = productsDao.getProductByID(1);
        System.out.println(product.toString());

//        Cli cli = new Cli();
//        cli.start();


    }
}

