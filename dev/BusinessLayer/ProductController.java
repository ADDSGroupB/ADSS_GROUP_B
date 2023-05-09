package BusinessLayer;

import java.util.Map;

public class ProductController {
    private MainController mainController;
    public ProductController(MainController m)
    {
        mainController = m;
    }

    public boolean findProductInMap(MainController mainController, int ID)
    {
        return (mainController.getProductMap().containsKey(ID));
    }
    public boolean checkProductName(String productName) {
        for (Product product : mainController.getProductMap().values()) {
            if (product.getProductName().equalsIgnoreCase(productName)) {
                return false;
            }
        }
        return true;
    }
    public Product createProduct(String name, double weightInt ,String manufacturer,Category parent,Category sub,Category subSub)
    {
        return new Product(name,manufacturer,weightInt,parent,sub,subSub);
    }
    public void changeProductName(Product product, String productNewName)
    {
            product.setProductName(productNewName);
    }
    public String getProductCategoriesByID(Product product)
    {
        Category parent = product.getParentCategory();
        Category sub = product.getSubCategory();
        Category subSub = product.getSubSubCategory();
        return ("The categories of the product with the ID :" +product.getProductID()+ " are: " + "parent category : " + parent.getCategoryName()+ "sub category : " + sub.getCategoryName()+ "subSub category : " + subSub.getCategoryName());
    }
    public String getProductWeightByID(Product product)
    {
        return("The weight of the product with the id : " +product.getProductID()+ " is : " + product.getProductWeight() + " gr");
    }
    public String printProductDetailsByID(Product product)
    {
        return product.toString();
    }
    public void printAllProducts()
    {

        System.out.println("------------------------------System Products-------------------------------------");
        for (Map.Entry<Integer, Product> entry : mainController.getProductMap().entrySet()) {
            System.out.println("** ProductID: " + entry.getKey()+ " Product Name: "+entry.getValue().getProductName());
            System.out.println("   Parent Category: " + entry.getValue().getParentCategory().getCategoryName()+ " Sub Category: "+entry.getValue().getSubCategory().getCategoryName() +" SubSub Category: "+entry.getValue().getSubSubCategory().getCategoryName() );
            System.out.println("------------------------------------------------------------------------------");

        }
    }
}
