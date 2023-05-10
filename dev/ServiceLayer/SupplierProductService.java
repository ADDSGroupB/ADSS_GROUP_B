package ServiceLayer;

import java.util.HashMap;
import java.util.List;
import java.util.NavigableMap;

public class SupplierProductService {
    private String name;
    private int productId;
    private int catalogNumber;
    private double price;

    private int amount;
    private String manufacturer;
    private int expirationDays;
    private Double weight;

    //private String manufacturer;
    private HashMap<Integer, Double> discountPerAmount;

    public SupplierProductService(String name, int productId, int catalogNumber, double price, int amount, HashMap<Integer, Double> discountPerAmount, String manufacturer, int expirationDays, Double weight) {
        this.name = name;
        this.productId = productId;
        this.catalogNumber = catalogNumber;
        this.price = price;
        this.amount = amount;
        this.discountPerAmount = discountPerAmount;
        this.manufacturer = manufacturer;
        this.expirationDays = expirationDays;
        this.weight = weight;
    }

    public double getPrice() {
        return price;
    }

    public int getProductId() {
        return productId;
    }

    public int getCatalogNumber() {
        return catalogNumber;
    }

    public HashMap<Integer, Double> getDiscountPerAmount() {
        return discountPerAmount;
    }

    public int getAmount() {
        return amount;
    }

    public String getName() {
        return name;
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
}
