package BusinessLayer;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class WeeklyStorageReport extends Report {
    private Map<Category, Map<Product, Integer>> reportCategories;
    private Map<Product, Integer> productAmount;
    public WeeklyStorageReport(LocalDate date, Map<Product, Integer> _productAmount)
    {
        super(date);
        this.reportKind = ReportKind.Weekly;
        this.reportCategories = new HashMap<>();
        this.productAmount = _productAmount;
    }
    public void addCategoryToReport(Category category){
        HashMap<Product, Integer> productsAndAmonut = new HashMap<Product, Integer>();
        for (Product product : category.getProductsInCategory()){
            productsAndAmonut.put(product, productAmount.get(product.getProductID()));
        }
        reportCategories.put(category, productsAndAmonut);
    }
    public String toString(Branch branch) {

        StringBuilder output = new StringBuilder("** Weekly Storage Report **\n");
        output.append("---------------------------").append("\n");
        output.append("Production date: ").append(this.getReportDate()).append("\n");
        output.append("---------------------------").append("\n");

        for (Category category : reportCategories.keySet()){
            output.append("** Category ID: ").append(category.getCategoryID()).append(" ** ").append("Category Name: ").append(category.getCategoryName()).append(" **\n");
            for (Product product : reportCategories.get(category).keySet()) {
                int amount = branch.getItemsInStore().get(product).size() + branch.getItemsInStorage().get(product).size();
                output.append("   Product ID: ").append(product.getProductID()).append(", Product name: ").append(product.getProductName()).append(", Amount: ").append(amount).append("\n");
                output.append("-------------------------------------------------------------").append("\n");
            }
        }
        return output.toString();
    }
}
