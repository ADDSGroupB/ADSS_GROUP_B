package DataAccess;

import BusinessLayer.Category;
import BusinessLayer.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductsDaoImpl implements ProductsDao {
    private Connection connection;
    private CategoryDao categoryDao;
    private Map<Integer, Product> productMapFromDB;
    public ProductsDaoImpl() throws SQLException {
        connection = DBConnector.connect();
        categoryDao = new CategoryDaoImpl();
        productMapFromDB = new HashMap<>();
    }
    public Map<Integer, Product> getProductsMapFromDB(){return this.productMapFromDB;}
    @Override
    public List<Product> getAllProducts() throws SQLException {
        List<Product> products = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            statement = connection.prepareStatement("SELECT * FROM Products");
            rs = statement.executeQuery();
            while (rs.next())
            {
                int currProductID = rs.getInt("ProductID");
                if (!productMapFromDB.containsKey(currProductID)) {
                    Category parent = categoryDao.getCategoryByID(rs.getInt("ParentCategory"));
                    Category sub = categoryDao.getCategoryByID(rs.getInt("SubCategory"));
                    Category subSub = categoryDao.getCategoryByID(rs.getInt("SubSubCategory"));
                    Product product = new Product(currProductID, rs.getString("ProductName"), rs.getString("Manufacturer"), rs.getDouble("Weight"), parent, sub, subSub);
                    products.add(product);
                    productMapFromDB.put(product.getProductID(), product);
                }
                else
                {
                    products.add(productMapFromDB.get(currProductID));
                }
            }
            return products;
        }
        catch (Exception e){
            System.out.println("Error while getting all products: " + e.getMessage());
            return null;
        }
        finally {
            if (rs != null){rs.close();}
            if (statement != null){statement.close();}
        }
    }
    @Override
    public Product getProductByID(int productID) throws SQLException {
        if (productMapFromDB.containsKey(productID))
        {
            return productMapFromDB.get(productID);
        }
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        Product product = null;
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM Products WHERE ProductID = ?");
            preparedStatement.setInt(1,productID);
            rs = preparedStatement.executeQuery();
            if (rs.next())
            {
                Category parent = categoryDao.getCategoryByID(rs.getInt("ParentCategory"));
                Category sub = categoryDao.getCategoryByID(rs.getInt("SubCategory"));
                Category subSub = categoryDao.getCategoryByID(rs.getInt("SubSubCategory"));
                product = new Product(rs.getInt("ProductID"),rs.getString("ProductName"),rs.getString("Manufacturer"),rs.getDouble("Weight"), parent,sub,subSub);
                productMapFromDB.put(product.getProductID(),product);
            }
            return product;
        }
        catch (Exception e){
            System.out.println("Error while getting product: " + e.getMessage());
            return null;
        }
        finally {
            if (preparedStatement!=null){preparedStatement.close();}
            if (rs != null) {rs.close();}
        }
    }
    @Override
    public Product addProduct(String name, String manufacturer, double productWeight, int parentCategoryID, int subCategoryID, int subSubCategoryID) throws SQLException
    {
        PreparedStatement preparedStatement =null;
        ResultSet rs =null;
        Product product;
        try {
            preparedStatement = connection.prepareStatement("INSERT INTO Products (ProductName, Manufacturer, Weight, ParentCategory, SubCategory, SubSubCategory) VALUES(?, ?, ?, ?, ?, ?)");
            preparedStatement.setString(1,name);
            preparedStatement.setString(2,manufacturer);
            preparedStatement.setDouble(3,productWeight);
            preparedStatement.setInt(4,parentCategoryID);
            preparedStatement.setInt(5,subCategoryID);
            preparedStatement.setInt(6,subSubCategoryID);
            preparedStatement.executeUpdate();
            Category parent = categoryDao.getCategoryByID(parentCategoryID);
            Category sub =categoryDao.getCategoryByID(subCategoryID);
            Category subSub =categoryDao.getCategoryByID(subSubCategoryID);
            rs = connection.createStatement().executeQuery("SELECT MAX(ProductID) FROM Products");
            int last_ID = rs.getInt(1);
            product = new Product(last_ID,name,manufacturer,productWeight,parent,sub,subSub);
            productMapFromDB.put(product.getProductID(),product);
            return product;
        }
        catch (Exception e){
            System.out.println("Error while trying to add new product: " + e.getMessage());
            return null;
        }
        finally {
            if (preparedStatement!=null){preparedStatement.close();}
            if (rs != null) {rs.close();}
        }
    }
    public boolean checkNewName(String newProductName,String manufacturer)throws SQLException
    {
        PreparedStatement preparedStatement =null;
        ResultSet rs =null;
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM Products WHERE ProductName = ? And Manufacturer = ?");
            preparedStatement.setString(1,newProductName);
            preparedStatement.setString(2,manufacturer);
            rs = preparedStatement.executeQuery();
            return !rs.next();
        }
        catch (Exception e){
            System.out.println("Error while trying to check if the product name exist: " + e.getMessage());
            return false;
        }
        finally {
            if (preparedStatement!=null){preparedStatement.close();}
            if (rs != null) {rs.close();}
        }
    }
    public List<Product> getAllProductsInCategory(int categoryID) throws SQLException
    {
        List<Product> productsInCategory = new ArrayList<>();
        PreparedStatement preparedStatement =null;
        ResultSet rs =null;
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM Products WHERE ParentCategory = ? OR SubCategory = ? OR SubSubCategory =?");
            preparedStatement.setInt(1,categoryID);
            preparedStatement.setInt(2,categoryID);
            preparedStatement.setInt(3,categoryID);
            rs = preparedStatement.executeQuery();
            while (rs.next())
            {
                int productID = rs.getInt(1);
                Product product = getProductByID(productID);
                productsInCategory.add(product);
            }
            return productsInCategory;
        }
        catch (Exception e){
            System.out.println("Error while trying to get all products in category: " + e.getMessage());
            return null;
        }
        finally {
            if (preparedStatement!=null){preparedStatement.close();}
            if (rs != null) {rs.close();}
        }
    }
}
