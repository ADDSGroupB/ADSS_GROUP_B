package DataAccess;

import BusinessLayer.Category;
import BusinessLayer.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductsDaoImpl implements ProductsDao {
    private Connection connection;
    private CategoryDao categoryDao;

    public ProductsDaoImpl() throws SQLException {
        connection = DBConnector.connect();
        categoryDao = new CategoryDaoImpl();

    }
    @Override
    public List<Product> getAllProducts() throws SQLException {
        List<Product> products = new ArrayList<>();
        Statement stmt =connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM Products");
        while (rs.next())
        {
            Statement stmt2 =connection.createStatement();
            ResultSet rs2 = stmt2.executeQuery("SELECT * FROM Products WHERE ParentCategory = ?");
            int parentID = rs.getInt("ParentCategory");
            Category parent = categoryDao.getCategoryByID(parentID);
            ResultSet rs3 = stmt2.executeQuery("SELECT * FROM Products WHERE SubCategory = ?");
            int subID = rs.getInt("SubCategory");
            Category sub = categoryDao.getCategoryByID(subID);
            ResultSet rs4 = stmt2.executeQuery("SELECT * FROM Products WHERE SubSubCategory = ?");
            int subSubID = rs.getInt("SubSubCategory");
            Category subSub = categoryDao.getCategoryByID(subSubID);
            Product product = new Product(rs.getInt("ProductID"),rs.getString("ProductName"),rs.getString("Manufacturer"),rs.getDouble("Weight"), parent,sub,subSub);
            products.add(product);
        }
        return products;
    }

    @Override
    public Product getProductByID(int productID) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Products WHERE ProductID = ?");
        preparedStatement.setInt(1,productID);
        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next())
        {
            Statement stmt2 =connection.createStatement();
            ResultSet rs2 = stmt2.executeQuery("SELECT * FROM Products WHERE ParentCategory = ?");
            int parentID = rs.getInt("ParentCategory");
            Category parent = categoryDao.getCategoryByID(parentID);
            ResultSet rs3 = stmt2.executeQuery("SELECT * FROM Products WHERE SubCategory = ?");
            int subID = rs.getInt("SubCategory");
            Category sub = categoryDao.getCategoryByID(subID);
            ResultSet rs4 = stmt2.executeQuery("SELECT * FROM Products WHERE SubSubCategory = ?");
            int subSubID = rs.getInt("SubSubCategory");
            Category subSub = categoryDao.getCategoryByID(subSubID);
            Product product = new Product(rs.getInt("ProductID"),rs.getString("ProductName"),rs.getString("Manufacturer"),rs.getDouble("Weight"), parent,sub,subSub);
            return product;
        }
        else {return null;}
    }
    // TODO : CHANGE -->   public void addProduct(Product product) throws SQLException
    @Override
    public void addProduct(Product product) throws SQLException
    {
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Products (ProductID, ProductName, Manufacturer, Weight, ParentCategory, SubCategory, SubSubCategory) VALUES(?, ?, ?, ?, ?, ?, ?)");
        preparedStatement.setString(2, product.getProductName());
        preparedStatement.setString(3, product.getManufacturer());
        preparedStatement.setDouble(4,product.getProductWeight());
        preparedStatement.setInt(5,product.getParentCategory().getCategoryID());
        preparedStatement.setInt(6,product.getSubCategory().getCategoryID());
        preparedStatement.setInt(7,product.getSubSubCategory().getCategoryID());
        preparedStatement.executeUpdate();
    }
}
