package DataAccessLayer.InventoryDataAccessLayer;

import BusinessLayer.InventoryBusinessLayer.Branch;
import DataAccessLayer.DBConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BranchesDaoImpl implements BranchesDao{
    private Connection connection;
    private Map<Integer,Branch> branchMapFromDB;
    public BranchesDaoImpl() throws SQLException {
        connection = DBConnector.connect();
        this.branchMapFromDB = new HashMap<>();
    }
    public Map<Integer,Branch> getBranchesMapFromDB()
    {
        return this.branchMapFromDB;
    }
    @Override
    public List<Branch> getAllBranches()throws SQLException {
        List<Branch> branches = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            statement = connection.prepareStatement("SELECT * FROM Branches");
            rs = statement.executeQuery();
            while (rs.next())
            {
                Branch branch = new Branch(rs.getInt("BranchID"),rs.getString("BranchName"));
                branchMapFromDB.put(branch.getBranchID(),branch);
                branches.add(branch);
            }
            return branches;

        }
        catch (Exception e){
            System.out.println("Error while getting all branches: " + e.getMessage());
            return null;
        }
        finally {
            if (rs != null){rs.close();}
            if (statement != null){statement.close();}
        }
    }
    @Override
    public Branch getBranchByID(int branchID) throws SQLException {
        if (branchMapFromDB.containsKey(branchID))
        {
            return branchMapFromDB.get(branchID);
        }
        PreparedStatement statement = null;
        ResultSet rs = null;
        Branch branch = null;
        try {
            statement = connection.prepareStatement("SELECT * FROM Branches WHERE BranchID = ?");
            statement.setInt(1,branchID);
            rs = statement.executeQuery();
            if (rs.next())
            {
                branch = new Branch(rs.getInt("BranchID"),rs.getString("BranchName"));
                branchMapFromDB.put(branchID,branch);
            }
            return branch;
        }
        catch (Exception e){
            System.out.println("Error while getting branch: " + e.getMessage());
            return null;
        }
        finally {
            if (statement!=null){statement.close();
            if (rs != null) {rs.close();}}
        }
    }
    @Override
    public Branch addBranch(String branchName) throws SQLException {
        PreparedStatement statement = null;
        ResultSet rs = null;
        Branch branch ;
        try {
            statement = connection.prepareStatement("INSERT INTO Branches (BranchName) VALUES(?)");
            statement.setString(1,branchName);
            statement.executeUpdate();
            rs = connection.createStatement().executeQuery("SELECT MAX(BranchID) FROM Branches");
            int last_ID = rs.getInt(1);
            branch = new Branch(last_ID,branchName);
            branchMapFromDB.put(branch.getBranchID(),branch);
            return branch;
        }
        catch (Exception e){
            System.out.println("Error while trying to add new branch: " + e.getMessage());
            return null;
        }
        finally {
            if (statement!=null){statement.close();
                if (rs != null) {rs.close();}}
        }
    }
}
