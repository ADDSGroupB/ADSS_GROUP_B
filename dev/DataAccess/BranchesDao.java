package DataAccess;

import BusinessLayer.Branch;
import BusinessLayer.Product;

import java.sql.SQLException;
import java.util.List;

public interface BranchesDao {
    public List<Branch> getAllBranches() throws SQLException;
    public Branch getBranchByID(int branchID) throws SQLException;
    public void addBranch(Branch branch) throws SQLException;
}
