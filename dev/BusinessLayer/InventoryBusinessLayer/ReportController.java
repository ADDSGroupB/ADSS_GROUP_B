package BusinessLayer.InventoryBusinessLayer;

import java.time.LocalDate;
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
}
