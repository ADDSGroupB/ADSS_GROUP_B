package BusinessLayer;

import java.time.LocalDate;
import java.util.HashMap;

enum ReportKind{Missing, Defective, Weekly}
public abstract class Report
{
    // Static variable to keep track of the last assigned ID
    private static int lastAssignedId = 1;
    private final int reportID;
    protected ReportKind reportKind;
    private LocalDate reportDate;
    private int branchID;

    public Report(LocalDate reportDate)
    {
        this.reportID = lastAssignedId;
        this.reportDate = reportDate;
        Report.lastAssignedId++;
    }

    public Report(int reportID, int branchID, LocalDate reportDate)
    {
        this.reportID = reportID;
        this.reportDate = reportDate;
        this.branchID = branchID;
    }
    public int getReportID(){
        return reportID;
    }

    public LocalDate getReportDate() {return reportDate;}
    public int getBranchID() {return branchID;}
    public String getReportType(){return reportKind.toString();}
}
