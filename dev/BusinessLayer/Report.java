package BusinessLayer;

import java.time.LocalDate;
import java.util.HashMap;

enum ReportKind{Missing, Defective, Weekly}
public abstract class Report
{
    private final int reportID;
    protected ReportKind reportKind;
    private LocalDate reportDate;
    private int branchID;
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
    public void setBranchID(int branchID){this.branchID = branchID;}

}
