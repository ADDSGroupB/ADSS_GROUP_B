package BusinessLayer;

import java.util.ArrayList;
import java.util.List;

public class Category {
    private String CategoryName;
    private int CategoryID;
    private List<Product> productInCategory;
    public Category(int categoryID,String name)
    {
        this.CategoryID = categoryID;
        this.CategoryName = name;
        this.productInCategory = new ArrayList<>();
    }
    public int getCategoryID() {return CategoryID;}
    public String getCategoryName() {
        return CategoryName;
    }
    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }
    public List<Product> getProductsInCategory(){return productInCategory;}
    public void addProductToCategory(Product product) {this.productInCategory.add(product);}
    public void setProductsToCategory(List<Product> productsToCategory) {this.productInCategory=productsToCategory;}
    @Override
    public String toString() {
        StringBuilder output = new StringBuilder("** Category ID: " + CategoryID + " ** " + "Category Name: " + CategoryName + " **\n");
        if (productInCategory.size() > 0) {
            output.append("The category has the following products: " +"\n");
            for (Product product : productInCategory) {
                output.append("Product ID: ").append(product.getProductID()).append(", Product name: ").append(product.getProductName()).append("\n");
            }
        }
        return output.toString();
    }
}
