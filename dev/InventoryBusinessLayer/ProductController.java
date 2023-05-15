package InventoryBusinessLayer;

import java.sql.SQLException;
import java.util.List;

public class ProductController {
    private MainController mainController;
    public ProductController(MainController m)
    {
        mainController = m;
    }
    public Product createProduct(String name, double weight ,String manufacturer,Category parent,Category sub,Category subSub) throws SQLException {
        return mainController.getProductsDao().addProduct(name,manufacturer,weight, parent.getCategoryID(),sub.getCategoryID(),subSub.getCategoryID() );
    }
    public Product getProduct(int productID) throws SQLException {
        return mainController.getProductsDao().getProductByID(productID);
    }
    public List<Product> getAllProducts()throws SQLException {
        return mainController.getProductsDao().getAllProducts();
    }
    public boolean newProductToAllBranches(Product product) throws SQLException {
        return mainController.getProductMinAmountDao().addNewProductToAllBranches(product.getProductID());
    }


}
