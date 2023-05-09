package BusinessLayer;

import java.util.HashMap;
import java.util.Map;

public class BranchReportManager {
    private MissingProductsReport currentMissingReport;
    private DefectiveProductsReport currentDefectiveReport;
    private WeeklyStorageReport currentWeeklyReport;
    private Map<Integer, MissingProductsReport> missingReportHistory;
    private Map<Integer, DefectiveProductsReport> defectiveReportHistory;
    private Map<Integer, WeeklyStorageReport> weeklyReportHistory;


    public BranchReportManager() {
        this.missingReportHistory = new HashMap<>();
        this.defectiveReportHistory = new HashMap<>();
        this.weeklyReportHistory = new HashMap<>();
    }

    public void addNewReport(Report newReport) {
        switch (newReport.reportKind) {
            case Weekly:
                if (currentWeeklyReport != null)
                    weeklyReportHistory.put(currentWeeklyReport.getReportID(), currentWeeklyReport);
                currentWeeklyReport = (WeeklyStorageReport) newReport;
                break;
            case Defective:
                if (currentDefectiveReport != null)
                    defectiveReportHistory.put(currentDefectiveReport.getReportID(), currentDefectiveReport);
                currentDefectiveReport = (DefectiveProductsReport) newReport;
                break;
            case Missing:
                if (currentMissingReport != null)
                    missingReportHistory.put(currentMissingReport.getReportID(), currentMissingReport);
                currentMissingReport = (MissingProductsReport) newReport;
                break;
        }
    }
    public Map<Integer, MissingProductsReport> getMissingReportHistory() {
        return missingReportHistory;
    }

    public void setCurrentMissingReport(MissingProductsReport currentMissingReport) {
        this.currentMissingReport = currentMissingReport;
    }

    public Map<Integer, DefectiveProductsReport> getDefectiveReportHistory() {
        return defectiveReportHistory;
    }

    public void setCurrentDefectiveReport(DefectiveProductsReport currentDefectiveReport) {
        this.currentDefectiveReport = currentDefectiveReport;
    }

    public Map<Integer, WeeklyStorageReport> getWeeklyReportHistory() {
        return weeklyReportHistory;
    }

    public void setCurrentWeeklyReport(WeeklyStorageReport currentWeeklyReport) {
        this.currentWeeklyReport = currentWeeklyReport;
    }

    public DefectiveProductsReport getCurrentDefectiveReport() {
        return currentDefectiveReport;
    }

    public MissingProductsReport getCurrentMissingReport() {
        return currentMissingReport;
    }

    public WeeklyStorageReport getCurrentWeeklyReport() {
        return currentWeeklyReport;
    }
}