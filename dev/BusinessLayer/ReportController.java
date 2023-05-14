package BusinessLayer;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

public class ReportController {

    public MissingProductsReport createNewMissingReport(LocalDate date) {
        MissingProductsReport report = new MissingProductsReport(date);
        return report;
    }

    public DefectiveProductsReport createNewDefectiveReport(LocalDate date) {
        Map<Product, Queue<Item>> newDamagedItems = new HashMap<>();
        Map<Product, Queue<Item>> newExpiredItems = new HashMap<>();
        return new DefectiveProductsReport(date, newExpiredItems, newDamagedItems);
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
    public WeeklyStorageReport createNewWeeklyReport(LocalDate date) {
        Map<Product, Integer> productAmount = new HashMap<>();
        return new WeeklyStorageReport(date, productAmount);
    }

}
