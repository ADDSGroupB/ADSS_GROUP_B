package BusinessLayer;

import java.util.ArrayList;

public class Category {
    // Static variable to keep track of the last assigned ID
    private static int lastAssignedId = 1;
    private String CategoryName;
    private int CategoryID;
    // product and amount
    private ArrayList<Product> productInCategory;
    public Category(String name)
    {
        this.CategoryID = lastAssignedId;
        this.CategoryName = name;
        this.productInCategory = new ArrayList<>();
        Category.lastAssignedId++;
    }
    public Category(int categoryID,String name)
    {
        this.CategoryID = categoryID;
        this.CategoryName = name;
//        this.productInCategory = new ArrayList<>();

    }

    public int getCategoryID() {
        return CategoryID;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }
    public ArrayList<Product> getProductsInCategory(){return productInCategory;}
    public void addProductToCategory(Product product) {this.productInCategory.add(product);}
    @Override
    public String toString() {
        StringBuilder output = new StringBuilder("** Category ID: " + CategoryID + " ** " + "Category Name: " + CategoryName + " **\n");
        for (Product product : productInCategory) {
            output.append("Product ID: ").append(product.getProductID()).append(", Product name: ").append(product.getProductName()).append("\n");
        }
        return output.toString();
    }
}
