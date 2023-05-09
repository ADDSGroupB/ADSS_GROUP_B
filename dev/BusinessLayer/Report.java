package BusinessLayer;

import java.time.LocalDate;
enum ReportKind{Missing, Defective, Weekly}
public abstract class Report
{
    // Static variable to keep track of the last assigned ID
    private static int lastAssignedId = 1;
    private final int reportID;
    protected ReportKind reportKind;
    private LocalDate reportDate;

    public Report(LocalDate reportDate)
    {
        this.reportID = lastAssignedId;
        this.reportDate = reportDate;
        Report.lastAssignedId++;
    }
    public int getReportID(){
        return reportID;
    }

    public LocalDate getReportDate() {return reportDate;}
}
