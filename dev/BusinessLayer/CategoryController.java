package BusinessLayer;

import DataAccess.CategoryDao;
import DataAccess.CategoryDaoImpl;

import java.sql.SQLException;
import java.util.Map;

public class CategoryController {
  private MainController mainController;
  public CategoryController(MainController m)
  {
    mainController = m;
  }
  public Category createCategory(String categoryName) throws SQLException {
    return mainController.getCategoryDao().addCategory(categoryName);
  }

//  public void changeCategoryName(int categoryID,String categoryNewName) throws SQLException {
//    mainController.getCategoryDao().updateCategoryName(categoryID,categoryNewName);
//  }

//  public void printAllCategories()
//  {
//    System.out.println("------------------------------System Categories-------------------------------------");
//    for (Map.Entry<Integer, Category> entry : mainController.getCategoryMap().entrySet()) {
//      System.out.println("** CategoryID: " + entry.getKey() + "  Category Name : " + entry.getValue().getCategoryName());
//      System.out.println("------------------------------------------------------------------------------");
//    }
//  }
//  public String printCategoryDetailsByID(Category category)
//  {
//    return category.toString();
//
//  }
}
