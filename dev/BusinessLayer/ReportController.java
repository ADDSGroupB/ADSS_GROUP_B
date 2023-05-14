package BusinessLayer;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

public class ReportController {

    public MissingProductsReport createNewMissingReport(int reportID, int branchID) {
        return new MissingProductsReport(reportID, branchID, LocalDate.now());
    }
    public WeeklyStorageReport createNewWeeklyReport(int reportID, int branchID) {
        return new WeeklyStorageReport(reportID, branchID, LocalDate.now());
    }
    public DefectiveProductsReport createNewDefectiveReport(int reportID, int branchID) {
        return new DefectiveProductsReport(reportID, branchID, LocalDate.now());
    }

//    public DefectiveProductsReport updateProductsInReport(DefectiveProductsReport report, Branch branch) {
//        if (branch.getDemagedItems().size() > 0) {
//            for (Queue<Item> itemQueue : branch.getDemagedItems().values()) {
//                for (Item item : itemQueue) {
//                    report.addDefectiveItem(item, item.getDefectiveDiscription());
//                }
//            }
//            Map<Product, Queue<Item>> newDamagedItems = new HashMap<>();
//            branch.setDemagedItems(newDamagedItems);
//        }
//
//        if (branch.getExpiredItems().size() > 0) {
//            for (Queue<Item> itemQueue : branch.getExpiredItems().values()) {
//                for (Item item : itemQueue) {
//                    report.addDefectiveItem(item, item.getDefectiveDiscription());
//                }
//            }
//            Map<Product, Queue<Item>> newExpiredItems = new HashMap<>();
//            branch.setExpiredItems(newExpiredItems);
//        }
//        return report;
//    }

}
