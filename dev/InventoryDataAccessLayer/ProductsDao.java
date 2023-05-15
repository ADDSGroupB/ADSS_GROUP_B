package InventoryDataAccessLayer;
import InventoryBusinessLayer.Product;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
public interface ProductsDao {
    public List<Product> getAllProducts() throws SQLException;
    public Product getProductByID(int productID) throws SQLException;
    public Product addProduct(String name, String manufacturer, double productWeight, int parentCategory, int subCategory, int subSubCategory) throws SQLException;
    public Map<Integer, Product> getProductsMapFromDB();
    public boolean checkNewName(String newProductName,String manufacturer)throws SQLException;
    public List<Product> getAllProductsInCategory(int categoryID) throws SQLException;
}
