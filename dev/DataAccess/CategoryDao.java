package DataAccess;

import BusinessLayer.Category;

import java.sql.SQLException;
import java.util.List;

public interface CategoryDao {

    public List<Category> getAllCategories() throws SQLException;
    public Category getCategoryByID(int categoryID) throws SQLException;
    public Category addCategory(String categoryName) throws SQLException;
}
