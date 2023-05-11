package BusinessLayer;

import DataAccess.CategoryDao;
import DataAccess.CategoryDaoImpl;

import java.sql.SQLException;
import java.util.Map;
//TODO : In each function in the menu that has input tests to pass to the BusinessLayer.HelperFunctions class

public class CategoryController {
  private MainController mainController;
  public CategoryController(MainController m)
  {
    mainController = m;
  }
  public boolean addNewCategory(String categoryName) throws SQLException {
    Category category = mainController.getCategoryDao().addCategory(categoryName);
    if (category == null){return false;}
    mainController.getCategoryMap().put(category.getCategoryID(), category);
    return true;
  }
  public void changeCategoryName(int categoryID,String categoryNewName) throws SQLException {
    mainController.getCategoryDao().updateCategoryName(categoryID,categoryNewName);
  }
  public String getCategoryNameByID(Category category)
  {
    return category.getCategoryName();
  }
  public void printAllCategories()
  {
    System.out.println("------------------------------System Categories-------------------------------------");
    for (Map.Entry<Integer, Category> entry : mainController.getCategoryMap().entrySet()) {
      System.out.println("** CategoryID: " + entry.getKey() + "  Category Name : " + entry.getValue().getCategoryName());
      System.out.println("------------------------------------------------------------------------------");
    }
  }
  public String printCategoryDetailsByID(Category category)
  {
    return category.toString();

  }
}
