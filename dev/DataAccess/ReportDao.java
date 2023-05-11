package DataAccess;

import BusinessLayer.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface ReportDao {
    public Map<Integer, MissingProductsReport> getAllMissingReports() throws SQLException;
    public Map<Integer, DefectiveProductsReport> getAllDefectiveReports() throws SQLException;
    public Map<Integer, WeeklyStorageReport> getAllWeeklyReports() throws SQLException;
    public Report getReportByID(int reportID) throws SQLException;
    public void addReport(Report report) throws SQLException;
}
