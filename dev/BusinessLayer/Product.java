package BusinessLayer;

public class Product
{
    private static int lastAssignedId = 1;
    private String productName;
    private final int productID;
    private String manufacturer;
    private double productWeight;
    private Category parentCategory;
    private Category subCategory;
    private Category subSubCategory;

    // Constructor for retrieving a product from a database - without increasing the lastAssignedId .
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
    public Product(int productID, String name, String manufacturer, double productWeight, Category parentCategory, Category subCategory, Category subSubCategory) {
        this.productID = productID;
        this.productName = name;
        this.manufacturer = manufacturer;
        this.productWeight = productWeight;
        this.parentCategory = parentCategory;
        this.subCategory = subCategory;
        this.subSubCategory = subSubCategory;
    }

    public Category getParentCategory() {return parentCategory;}
    public Category getSubCategory() {return subCategory;}
    public Category getSubSubCategory() {return subSubCategory;}
    public double getProductWeight() {return productWeight;}
    public int getProductID() {return productID;}
    public String getManufacturer() {return manufacturer;}
    public String getProductName() {return productName;}
    public void setProductName(String productName) {this.productName = productName;}

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

