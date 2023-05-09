package DataAccess;

import BusinessLayer.Category;
import BusinessLayer.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDaoImpl implements CategoryDao{
    private Connection conn;
    @Override
    public List<Category> getAllCategories() throws SQLException {
        //TODO : IMPLEMENT THIS METHOD
        List<Category> categories = new ArrayList<>();
        Statement stmt =conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM Categories");
        while (rs.next())
        {
        }
        return null;
    }

    @Override
    public Category getCategoryByID(int categoryID) throws SQLException {

        PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM Categories WHERE CategoryID = ?");
        preparedStatement.setInt(1,categoryID);
        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next())
        {
            Category category = new Category(rs.getInt("CategoryID"),rs.getString("CategoryName"));
            return category;
        }
        else {return null;}
    }

    @Override
    public void addCategory(Category category) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO Categories (CategoryID,CategoryName) VALUES(?, ?)");
        preparedStatement.setInt(1,category.getCategoryID());
        preparedStatement.setString(2,category.getCategoryName());
        preparedStatement.executeUpdate();
    }
}
