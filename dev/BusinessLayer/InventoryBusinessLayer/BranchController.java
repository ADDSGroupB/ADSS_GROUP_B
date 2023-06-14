package BusinessLayer.InventoryBusinessLayer;

import java.sql.SQLException;
import java.util.List;

public class BranchController {
    private ReportController reportController;
    private MainController mainController;
    public BranchController(MainController m) {
        this.reportController = new ReportController();
        mainController =m ;
    }
    public ReportController getReportController() {
        return reportController;
    }
    public Branch createNewBranch(String newBranchName) throws SQLException {
        return mainController.getBranchesDao().addBranch(newBranchName);
    }
    public List<Branch> getAllBranchesController()
    {
        try {
            return mainController.getBranchesDao().getAllBranches();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public Branch getBranchID(int branchID) {
        try {
            return mainController.getBranchesDao().getBranchByID(branchID);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}


