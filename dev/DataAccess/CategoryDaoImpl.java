package DataAccess;
import BusinessLayer.Branch;
import BusinessLayer.Category;
import BusinessLayer.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class CategoryDaoImpl implements CategoryDao{
    private Connection connection;
    public CategoryDaoImpl() throws SQLException {
        connection = DBConnector.connect();}
    @Override
    public List<Category> getAllCategories() throws SQLException {
        //TODO : IMPLEMENT THIS METHOD
        List<Category> categories = new ArrayList<>();
        Statement stmt =connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM Categories");
        while (rs.next())
        {
            Category category = new Category(rs.getInt("CategoryID"),rs.getString("CategoryName"));
            PreparedStatement preparedStatement2 = connection.prepareStatement("SELECT * FROM Products WHERE ParentCategory = ? OR SubCategory = ? OR SubSubCategory = ? ");
            preparedStatement2.setInt(1,category.getCategoryID());
            ResultSet rs2 = preparedStatement2.executeQuery();
            while (rs2.next())
            {
                Statement stmt3 =connection.createStatement();
                ResultSet rs4 = stmt3.executeQuery("SELECT * FROM Products WHERE ParentCategory = ?");
                int parentID = rs4.getInt("ParentCategory");
                Category parent = this.getCategoryByID(parentID);
                ResultSet rs3 = stmt3.executeQuery("SELECT * FROM Products WHERE SubCategory = ?");
                int subID = rs3.getInt("SubCategory");
                Category sub = this.getCategoryByID(subID);
                ResultSet rs5 = stmt3.executeQuery("SELECT * FROM Products WHERE SubSubCategory = ?");
                int subSubID = rs5.getInt("SubSubCategory");
                Category subSub = this.getCategoryByID(subSubID);
                Product product = new Product(rs2.getInt("ProductID"),rs2.getString("ProductName"),rs2.getString("Manufacturer"),rs2.getDouble("Weight"), parent,sub,subSub);
                category.addProductToCategory(product);
            }
            categories.add(category);
        }
        return categories;
    }

    @Override
    public Category getCategoryByID(int categoryID) throws SQLException {

        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Categories WHERE CategoryID = ?");
        preparedStatement.setInt(1,categoryID);
        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next())
        {
            Category category = new Category(rs.getInt("CategoryID"),rs.getString("CategoryName"));
            PreparedStatement preparedStatement2 = connection.prepareStatement("SELECT * FROM Products WHERE ParentCategory = ? OR SubCategory = ? OR SubSubCategory = ? ");
            preparedStatement2.setInt(1,categoryID);
            ResultSet rs2 = preparedStatement2.executeQuery();
            while (rs2.next())
            {
                Statement stmt3 =connection.createStatement();
                ResultSet rs4 = stmt3.executeQuery("SELECT * FROM Products WHERE ParentCategory = ?");
                int parentID = rs4.getInt("ParentCategory");
                Category parent = this.getCategoryByID(parentID);
                ResultSet rs3 = stmt3.executeQuery("SELECT * FROM Products WHERE SubCategory = ?");
                int subID = rs3.getInt("SubCategory");
                Category sub = this.getCategoryByID(subID);
                ResultSet rs5 = stmt3.executeQuery("SELECT * FROM Products WHERE SubSubCategory = ?");
                int subSubID = rs5.getInt("SubSubCategory");
                Category subSub = this.getCategoryByID(subSubID);
                Product product = new Product(rs2.getInt("ProductID"),rs2.getString("ProductName"),rs2.getString("Manufacturer"),rs2.getDouble("Weight"), parent,sub,subSub);
                category.addProductToCategory(product);
            }
            return category;
        }
        else {return null;}
    }
    @Override
    public Category addCategory(String categoryName) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Categories (CategoryName) VALUES(?)");
        preparedStatement.setString(1,categoryName);
        preparedStatement.executeUpdate();
        ResultSet rs = connection.createStatement().executeQuery("SELECT MAX(CategoryID) FROM Categories");
        int last_ID = rs.getInt(1);
        return new Category(last_ID,categoryName);
    }
}
