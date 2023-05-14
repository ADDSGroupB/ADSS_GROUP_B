package BusinessLayer;

import java.sql.SQLException;
import java.util.List;

public class CategoryController {
  private MainController mainController;
  public CategoryController(MainController m)
  {
    mainController = m;
  }
  public Category createCategory(String categoryName) throws SQLException {
    return mainController.getCategoryDao().addCategory(categoryName);
  }
  public Category getCategory(int categoryID) throws  SQLException
  {
    return mainController.getCategoryDao().getCategoryByID(categoryID);
  }
  public List<Product> getProductInCategory(int categoryID)throws  SQLException
  {
    return mainController.getProductsDao().getAllProductsInCategory(categoryID);
  }
  public List<Category> getAllCategories()throws  SQLException
  {
    return mainController.getCategoryDao().getAllCategories();
  }
}
