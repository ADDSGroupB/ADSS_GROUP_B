package BusinessLayer;

import java.util.HashMap;

public class SupplierProduct
{
    private static int id = 0;
    private String name;
    private int supplierId;
    private int catalogID;
    private int productID;
    private double price;
    private String manufacturer;
    private int expirationDays;
    private Double weight;
    private HashMap<Integer, Double> discountPerAmount; // product amount, discount in percentage
    private int amount;

    public SupplierProduct(String name, int supplierId, int productID, int catalogID, double price, int amount, HashMap<Integer, Double> discountPerAmount, String manufacturer, int expirationDays, Double weight) {
        this.name = name;
        this.supplierId = supplierId;
        this.catalogID = catalogID;
        this.productID = productID;
        this.price = price;
        this.manufacturer = manufacturer;
        this.expirationDays = expirationDays;
        this.weight = weight;
        this.discountPerAmount = discountPerAmount;
        this.amount = amount;
    }

    public SupplierProduct(String name, int productID, int catalogID, double price, int amount, HashMap<Integer, Double> discountPerAmount) {
        this.name = name;
        this.productID = productID;
        this.catalogID = catalogID;
        this.price = price;
        this.amount = amount;
        //this.manufacturer = manufacturer;
        this.discountPerAmount = discountPerAmount;
    }

    public SupplierProduct(SupplierProduct supplierProduct)
    {
        this.name = supplierProduct.name;
        this.supplierId = supplierProduct.supplierId;
        this.catalogID = supplierProduct.catalogID;
        this.productID = supplierProduct.productID;
        this.price = supplierProduct.price;
        this.manufacturer = supplierProduct.manufacturer;
        this.expirationDays = supplierProduct.expirationDays;
        this.weight = supplierProduct.weight;
        this.discountPerAmount = supplierProduct.discountPerAmount;
        this.amount = supplierProduct.amount;
    }

    public SupplierProduct(){
    }

    public int getProductID() {
        return productID;
    }
    public void setCatalogID(int newCatalogNumber){
        this.catalogID=newCatalogNumber;
    }
    public HashMap<Integer, Double> getDiscountPerAmount(){
        return discountPerAmount;
    }

    public void addDiscount(int ammount, double discount){
        discountPerAmount.put(ammount, discount);
        System.out.println(discountPerAmount.get(ammount));
    }

    public void removeDiscount(int ammount){
        discountPerAmount.remove(ammount);

    }

    public String getName() {
        return name;
    }

    public int getCatalogId() {
        return catalogID;
    }

    public int getAmount() {
        return amount;
    }

    public double getPrice() {
        return price;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public int getExpirationDays() {
        return expirationDays;
    }

    public Double getWeight() {
        return weight;
    }

    public static void setId(int id) {
        SupplierProduct.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public void setExpirationDays(int expirationDays) {
        this.expirationDays = expirationDays;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public void setDiscountPerAmount(HashMap<Integer, Double> discountPerAmount) {
        this.discountPerAmount = discountPerAmount;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public int getCatalogID() {
        return catalogID;
    }
}

