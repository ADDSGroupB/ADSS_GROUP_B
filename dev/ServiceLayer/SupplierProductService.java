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

    //private String manufacturer;
    private HashMap<Integer, Double> discountPerAmount;

    public SupplierProductService(String name, int productId, int catalogNumber, double price, int amount, HashMap<Integer, Double> discountPerAmount) {
        this.name = name;
        this.productId = productId;
        this.catalogNumber = catalogNumber;
        this.price = price;
        this.amount = amount;
        this.discountPerAmount = discountPerAmount;
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
}
