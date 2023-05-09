package DataAccess;

import BusinessLayer.Category;
import BusinessLayer.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductsDaoImpl implements ProductsDao {
    private Connection conn;

    public ProductsDaoImpl() throws SQLException {conn = DBConnector.connect();}
    @Override
    public List<Product> getAllProducts() throws SQLException {
        List<Product> products = new ArrayList<>();
        Statement stmt =conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM Products");
        while (rs.next())
        {
            Product product = new Product(rs.getInt("ProductID"),rs.getString("ProductName"),rs.getString("Manufacturer"),rs.getDouble("Weight"), rs.getInt("ParentCategory"), rs.getInt("SubCategory"),rs.getInt("SubSubCategory"));
            products.add(product);
        }
        return products;
    }

    @Override
    public Product getProductByID(int productID) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM Products WHERE ProductID = ?");
        preparedStatement.setInt(1,productID);
        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next())
        {
            Product product = new Product(rs.getInt("ProductID"),rs.getString("ProductName"),rs.getString("Manufacturer"),rs.getDouble("Weight"), rs.getInt("ParentCategory"), rs.getInt("SubCategory"),rs.getInt("SubSubCategory"));
            return product;
        }
        else {return null;}
    }

    @Override
    public void addProduct(Product product) throws SQLException
    {
        PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO Products (ProductID, ProductName, Manufacturer, Weight, ParentCategory, SubCategory, SubSubCategory) VALUES(?, ?, ?, ?, ?, ?, ?)");
        preparedStatement.setString(2, product.getProductName());
        preparedStatement.setString(3, product.getManufacturer());
        preparedStatement.setDouble(4,product.getProductWeight());
        preparedStatement.setInt(5,product.getParentCategory().getCategoryID());
        preparedStatement.setInt(6,product.getSubCategory().getCategoryID());
        preparedStatement.setInt(7,product.getSubSubCategory().getCategoryID());
        preparedStatement.executeUpdate();
    }
}
