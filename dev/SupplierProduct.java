public class SupplierProduct
{
    private static int id = 0;
    private String name;
    private int catalogID;
    private int productID;
    private double price;
    private String manufacturer;
    public SupplierProduct(String name, int productID, double price, String manufacturer)
    {
        this.name = name;
        this.catalogID = id++;
        this.productID = productID;
        this.price = price;
        this.manufacturer = manufacturer;
    }
}
