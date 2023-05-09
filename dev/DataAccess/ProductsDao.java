package DataAccess;

import BusinessLayer.Product;

import java.sql.SQLException;
import java.util.List;

public interface ProductsDao {

    public List<Product> getAllProducts() throws SQLException;
    public Product getProductByID(int productID) throws SQLException;
    public void addProduct(Product product) throws SQLException;
}
