public class SupplierProduct
{
    private String name;
    private int catalogID;
    private int productID;
    private double price;
    private String manufacturer;

    public SupplierProduct(String name, int productID, int catalogID, double price, String manufacturer)
    {
        this.name = name;
        this.catalogID = catalogID;
        this.productID = productID;
        this.price = price;
        this.manufacturer = manufacturer;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCatalogID() {
        return catalogID;
    }

    public void setCatalogID(int catalogID) {
        this.catalogID = catalogID;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }


}
