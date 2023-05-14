package BusinessLayer;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
public class WeeklyStorageReport extends Report {
    private Map<Category, Map<Product, Integer>> reportCategories;
    public WeeklyStorageReport(int reportID, int branchID, LocalDate date,  Map<Category, Map<Product, Integer>> reportCategories)
    {
        super(reportID, branchID, date);
        this.reportKind = ReportKind.Weekly;
        this.reportCategories = new HashMap<>();
    }
    public WeeklyStorageReport(int reportID, int branchID, LocalDate date)
    {
        this(reportID, branchID, date, new HashMap<>());
    }
    public void addCategoryToReport(Category category, Map<Product, Integer> productCurrAmount){
        reportCategories.put(category, productCurrAmount);
    }
    public String toString(Branch branch) {

        StringBuilder output = new StringBuilder("** Weekly Storage Report **\n");
        output.append("---------------------------").append("\n");
        output.append("Report ID: ").append(this.getReportID()).append("\n");
        output.append("Production date: ").append(this.getReportDate()).append("\n");
        output.append("---------------------------").append("\n");

        for (Category category : reportCategories.keySet()){
            output.append("** Category ID: ").append(category.getCategoryID()).append(" ** ").append("Category Name: ").append(category.getCategoryName()).append(" **\n");
            for (Product product : reportCategories.get(category).keySet()) {
                //int amount = branch.getItemsInStore().get(product).size() + branch.getItemsInStorage().get(product).size();
                output.append("   Product ID: ").append(product.getProductID()).append(", Product name: ").append(product.getProductName()).append(", Amount: ").append(reportCategories.get(category).get(product)).append("\n");
            }
            output.append("-------------------------------------------------------------").append("\n");
        }
        return output.toString();
    }
    public Map<Category, Map<Product, Integer>> getWeeklyReportMap(){return reportCategories;}

}
