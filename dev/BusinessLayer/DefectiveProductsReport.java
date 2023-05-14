package BusinessLayer;

import java.time.LocalDate;
import java.util.*;

public class DefectiveProductsReport extends Report {
    private ArrayList<Item> defectiveOrExpiredProducts;
    public DefectiveProductsReport(int reportID, int branchID, LocalDate date, ArrayList<Item> defectiveItemsMap) {
        super(reportID, branchID, date);
        this.reportKind = ReportKind.Defective;
        this.defectiveOrExpiredProducts = defectiveItemsMap;
    }
    public DefectiveProductsReport(int reportID, int branchID, LocalDate date) {
        this(reportID, branchID, date, new ArrayList<>());
    }
    public void addDefectiveItem(Item item){
        defectiveOrExpiredProducts.add(item);
    }
    public String toString() {
        StringBuilder output = new StringBuilder("** Defective Products Report **\n");
        output.append("-------------------------------\n");
        output.append("Report ID: ").append(this.getReportID()).append("\n");
        output.append("Production date: ").append(this.getReportDate()).append("\n");
        output.append("---------------------------").append("\n");
        for (Item item : defectiveOrExpiredProducts){
            if (item.getStatusType() == StatusEnum.Damaged) {
                output.append("Item ID: ").append(item.getItemID()).append(", Product ID: ").append(item.getProductID()).append(", Product name: ").append(item.getProduct().getProductName()).append("\n").append("DefectiveType: ")
                        .append(item.getStatusType()).append("\n").append("Defective Discription: ").append(item.getDefectiveDiscription()).append("\n");
            }
            else
            {
                output.append("Item ID: ").append(item.getItemID()).append(", Product ID: ").append(item.getProductID()).append(", Product name: ").append(item.getProduct().getProductName()).append("\n").append("DefectiveType: ")
                        .append(item.getStatusType()).append("\n").append("Defective Discription: ").append(item.getDefectiveDiscription()).append("ExpireDate: ").append(item.getExpiredDate()).append("\n");
            }
        }
        return output.toString();
    }
    public ArrayList<Item> getDefectiveItemsList(){
        return defectiveOrExpiredProducts;
    }
}
