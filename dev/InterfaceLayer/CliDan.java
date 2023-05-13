package InterfaceLayer;

import BusinessLayer.Category;
import BusinessLayer.HelperFunctions;
import BusinessLayer.MainController;
import BusinessLayer.Product;
import DataAccess.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class CliDan {
    private MainController mainController;

    public CliDan() {
        mainController = new MainController();
    }

    public void productUI() throws SQLException {
        Scanner productScanner = new Scanner(System.in);
        int productChoice = 0;
        while (productChoice != 5) {
            System.out.println("Product Menu - Please choose one of the following options : ");
            System.out.println("1. Add new product ");
            System.out.println("2. Get product categories by ID ");
            System.out.println("3. Print product details by ID ");
            System.out.println("4. Print all products");
            System.out.println("5. Exit to Inventory menu");
            try {
                productChoice = productScanner.nextInt();
            } catch (Exception e) {
                System.out.println("Please enter an integer between 1-5 ");
                productScanner.nextLine();
                continue;
            }
            switch (productChoice) {
                case 1: {
                    System.out.println("What is the name of the new product ? ");
                    Scanner newProductScanner = new Scanner(System.in);
                    String newProductName = newProductScanner.nextLine();
                    System.out.println("What is the manufacturer's name of the new product? ");
                    String manufacturer = newProductScanner.next();
                    if (mainController.getProductsDao().checkNewName(newProductName,manufacturer)) {
                        System.out.println("What is the weight of the new product? (in gr)");
                        double weight = HelperFunctions.positiveDoubleInsertion();
                        System.out.println("What is the parent category ID of the new product? ");
                        int parentInt = HelperFunctions.positiveItegerInsertion();
                        System.out.println("What is the sub category ID of the new product ? ");
                        int subInt = HelperFunctions.positiveItegerInsertion();
                        System.out.println("What is the subSub category ID of the new product ? ");
                        int subSubInt = HelperFunctions.positiveItegerInsertion();
                        if (!(subSubInt != subInt && subSubInt != parentInt && subInt != parentInt)) {
                            System.out.println("The three categories must be different");
                            break;
                        }
                        Category parent = mainController.getCategoryDao().getCategoryByID(parentInt);
                        Category sub = mainController.getCategoryDao().getCategoryByID(subInt);
                        Category subSub = mainController.getCategoryDao().getCategoryByID(subSubInt);
                        if (parent == null || sub == null || subSub == null) {
                            System.out.println("There is some problem importing the categories");
                            break;
                        }
                        Product product = mainController.getProductController().createProduct(newProductName, weight, manufacturer, parent, sub, subSub);
                        if (product != null) {
                            if (mainController.getProductController().newProductToAllBranches(product)) {
                                System.out.println("The product was created successfully ");
                            }
                        }
                    } else {
                        System.out.println("The product name you provided already exists under the manufacturer you provided in the system");
                    }
                    break;
                }
                case 2: {
                    System.out.println("What is the ID of the product for which you would like to get the categories ?  ");
                    int productID = HelperFunctions.positiveItegerInsertion();
                    Product product = mainController.getProductController().getProduct(productID);
                    if (product == null) {
                        System.out.println("We do not have a product in the system with the ID number you provided ");
                        break;
                    }
                    System.out.println("The product with the ID : " + productID + " is under the following categories:" + "\n");
                    Category parent = product.getParentCategory();
                    Category sub = product.getSubCategory();
                    Category subSub = product.getSubSubCategory();
                    System.out.println(parent);
                    System.out.println(sub);
                    System.out.println(subSub);
                    break;
                }
                case 3: {
                    System.out.println("What is the ID of the product for which you would like to get his details ?  ");
                    int productID = HelperFunctions.positiveItegerInsertion();
                    Product product = mainController.getProductController().getProduct(productID);
                    if (product == null) {
                        System.out.println("We do not have a product in the system with the ID number you provided ");
                        break;
                    }
                    System.out.println(product);
                    break;
                }
                case 4: {
                    List<Product> products = mainController.getProductController().getAllProducts();
                    if (products == null) {
                        System.out.println("We currently have no products in the system");
                        break;
                    }
                    System.out.println("The system includes the following products:");
                    for (Product product : products) {
                        System.out.println(product);
                    }
                    break;
                }
                case 5: {
                    System.out.println("Exiting to Inventory menu");
                    break;
                }
                default: {
                    System.out.println("Invalid choice, please try again");
                    break;
                }
            }
        }
    }
    public void categoryUI() throws SQLException
    {
        Scanner categoryScanner = new Scanner(System.in);
        int categoryChoice = 0;
        while (categoryChoice != 4) {
            System.out.println("Category Menu - Please choose one of the following options : ");
            System.out.println("1. Add new category ");
            System.out.println("2. Print category details by ID ");
            System.out.println("3. Print all categories");
            System.out.println("4. Exit to Inventory menu");
            try {
                categoryChoice = categoryScanner.nextInt();
            } catch (Exception e) {
                System.out.println("Please enter an integer between 1-4 ");
                categoryScanner.nextLine();
                continue;
            }
            switch (categoryChoice)
            {
                case 1:{
                    System.out.println("What is the name of the new category ? ");
                    Scanner newCategoryScanner = new Scanner(System.in);
                    String newCategoryName = newCategoryScanner.nextLine();
                    if (mainController.getCategoryDao().checkNewCategoryName(newCategoryName))
                    {
                      Category category = mainController.getCategoryController().createCategory(newCategoryName);
                      if (category != null)
                      {
                          System.out.println("The category has been successfully added");
                      }
                    }
                    break;
                }
                case 2:{
                    System.out.println("What is the id of the category you are looking for ? ");
                    int categoryID = HelperFunctions.positiveItegerInsertion();
                    Category category = mainController.getCategoryDao().getCategoryByID(categoryID);
                    if (category == null) {
                        System.out.println("We do not have a category in the system with the ID number you provided ");
                        break;
                    }
                    List<Product> productsInCategory = mainController.getProductsDao().getAllProductsInCategory(categoryID);
                    if (productsInCategory != null)
                    {
                        category.setProductsToCategory(productsInCategory);
                    }
                    System.out.println(category);
                    break;
                }
                case 3:{
                    List<Category> categories = mainController.getCategoryDao().getAllCategories();
                    if (categories == null) {
                        System.out.println("We currently have no products in the system");
                        break;
                    }
                    System.out.println("The system includes the following categories:");
                    for (Category category : categories) {
                        List<Product> productsInCategory = mainController.getProductsDao().getAllProductsInCategory(category.getCategoryID());
                        if (productsInCategory != null) {category.setProductsToCategory(productsInCategory);}
                        System.out.println(category);
                        System.out.println("----------------------");
                    }
                    break;
                   }
                case 4:{  System.out.println("Exiting to Inventory menu");
                    break;}
                default:{ System.out.println("Invalid choice, please try again");
                    break;}

            }
        }

    }
}
