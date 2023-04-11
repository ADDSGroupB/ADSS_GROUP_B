package BusinessLayer;

import java.util.HashMap;
import java.util.NavigableMap;

public class SupplierProduct
{
    private static int id = 0;
    private String name;
    private int supplierId;
    private int catalogID;
    private int productID;
    private double price;
    //private String manufacturer;
    private HashMap<Integer, Double> discountPerAmount; // product amount, discount in percentage
    private int ammount;

    public SupplierProduct(String name, int productID,int catalogID, double price, int amount,  HashMap<Integer, Double> discountPerAmount) {
        this.name = name;
        this.productID = productID;
        this.catalogID = catalogID;
        this.price = price;
        this.ammount = amount;
        //this.manufacturer = manufacturer;
        this.discountPerAmount = discountPerAmount;
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
        return ammount;
    }

    public double getPrice() {
        return price;
    }

    public void setAmount(int amount) {
        this.ammount= amount;
    }
}

