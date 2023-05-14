package DataAccessLayer;

import BusinessLayer.Category;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface CategoryDao {

    public List<Category> getAllCategories() throws SQLException;
    public Category getCategoryByID(int categoryID) throws SQLException;
    public Category addCategory(String categoryName) throws SQLException;
    public Category updateCategoryName(int categoryID,String categoryNewName) throws SQLException;
    public Map<Integer, Category> getCategoryMapFromDB();
    public boolean checkNewCategoryName(String newCategoryName)throws SQLException;


}
