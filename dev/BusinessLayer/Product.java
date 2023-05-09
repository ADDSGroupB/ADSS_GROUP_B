package BusinessLayer;

public class Product
{
    private static int lastAssignedId = 1;
    private String productName;
    private int productID;
    private String manufacturer;
    private double productWeight;

    private int parentCategoryS;
    private int subCategoryS;
    private int subSubCategoryS;

    private Category parentCategory;
    private Category subCategory;
    private Category subSubCategory;


    public Product(String name, String manufacturer, double productWeight, Category parentCategory, Category subCategory, Category subSubCategory) {
        this.productID = lastAssignedId; // increment static productID for each new product
        this.productName = name;
        this.manufacturer = manufacturer;
        this.productWeight = productWeight;
        this.parentCategory = parentCategory;
        this.subCategory = subCategory;
        this.subSubCategory = subSubCategory;
        Product.lastAssignedId++;
    }
    // Constructor for retrieving a product from a database - without increasing the lastAssignedId .
    // Should be in place -----Category parentCategory, Category subCategory, Category subSubCategory -- > int parentCategory, int subCategory, int subSubCategory
    //TODO: check how can i get for the constructor the categories and not their ids
    public Product(int productID, String name, String manufacturer, double productWeight, int parentCategory, int subCategory, int subSubCategory) {
        this.productID = productID;
        this.productName = name;
        this.manufacturer = manufacturer;
        this.productWeight = productWeight;
        this.parentCategoryS = parentCategory;
        this.subCategoryS = subCategory;
        this.subSubCategoryS = subSubCategory;
    }

    public Category getParentCategory() {return parentCategory;}
    public Category getSubCategory() {return subCategory;}
    public Category getSubSubCategory() {return subSubCategory;}
    public double getProductWeight() {return productWeight;}
    public int getProductID() {return productID;}
    public String getManufacturer() {return manufacturer;}
    public String getProductName() {return productName;}
    public void setProductName(String productName) {this.productName = productName;}
    public void setProductID(int id) {this.productID = id;}

    @Override
    public String toString() {
        return "Product ID: " + productID + "\n" +
                "Product Name: " + productName + "\n" +
                "Manufacturer: " + manufacturer + "\n" +
                "Product Weight: " + productWeight + "\n" +
                "Product Parent Category: " + parentCategory.getCategoryName() + "\n" +
                "Product Sub Category: " + subCategory.getCategoryName() + "\n" +
                "Product SubSub Category: " + subSubCategory.getCategoryName() + "\n";
    }
}

