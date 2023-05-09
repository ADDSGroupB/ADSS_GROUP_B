package BusinessLayer;

import java.util.Map;
//TODO : In each function in the menu that has input tests to pass to the BusinessLayer.HelperFunctions class

public class CategoryController {
  private MainController mainController;
  public CategoryController(MainController m)
  {
    mainController = m;
  }
  public boolean addNewCategory(String categoryName)
  {
    for (Category category : mainController.getCategoryMap().values()) {
      if (category.getCategoryName().equals(categoryName)) {
        return false;
      }
    }
    Category category1 = new Category(categoryName);
    mainController.getCategoryMap().put(category1.getCategoryID(), category1);
    return true;
  }
  public boolean changeCategoryName(Category category,String categoryNewName) {
    for (Category cat : mainController.getCategoryMap().values()) {
      if (cat.getCategoryName().equals(categoryNewName)) {
        return false;
      }}
    category.setCategoryName(categoryNewName);
    return true;
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
