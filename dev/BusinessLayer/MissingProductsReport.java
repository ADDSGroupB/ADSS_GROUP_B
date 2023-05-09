package BusinessLayer;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class MissingProductsReport extends Report {
    // Integer-Left  --> productID
    //Integer-Right --> Amount to order for each product
    private Map<Product,Integer> missingProducts;
    public MissingProductsReport(LocalDate date)
    {
        super(date);
        this.reportKind = ReportKind.Missing;
        this.missingProducts = new HashMap<>();
    }
    public void addMissingProduct(Product product, int amountForOrder) {
        if (!missingProducts.containsKey(product)) {
            missingProducts.put(product, amountForOrder);
        }
        else {System.out.println("This product has already been added to the Missing Products report");}
    }
    public String toString() {
        StringBuilder output = new StringBuilder("** Missing Products Report **\n");
        output.append("---------------------------").append("\n");
        output.append("Production date: ").append(this.getReportDate()).append("\n");
        output.append("---------------------------").append("\n");
        for (Product product : missingProducts.keySet()){
            output.append("Product ID: ").append(product.getProductID()).append(", Product Name: ").append(product.getProductName()).append(", Amount to order: ").append(missingProducts.get(product)).append("\n");}
        return output.toString();
    }
}
