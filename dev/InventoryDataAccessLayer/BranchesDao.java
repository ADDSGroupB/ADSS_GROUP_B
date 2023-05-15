package InventoryDataAccessLayer;

import InventoryBusinessLayer.Branch;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
public interface BranchesDao {
    public List<Branch> getAllBranches() throws SQLException;
    public Branch getBranchByID(int branchID) throws SQLException;
    public Branch addBranch(String branchName) throws SQLException;
    public Map<Integer,Branch> getBranchesMapFromDB()throws SQLException;
}
