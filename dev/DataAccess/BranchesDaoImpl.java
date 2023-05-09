package DataAccess;

import BusinessLayer.Branch;
import BusinessLayer.Category;
import BusinessLayer.Product;

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
    public Map<Integer,Branch> getBranchMapFromDB()
    {
        return this.branchMapFromDB;
    }
    @Override
    public List<Branch> getAllBranches()throws SQLException {
        List<Branch> branches = new ArrayList<>();
        Statement stmt =connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM Branches");
        while (rs.next())
        {
            Branch branch = new Branch(rs.getInt("BranchID"),rs.getString("BranchName"));
            branchMapFromDB.put(branch.getBranchID(),branch);
            branches.add(branch);
        }
        return branches;
    }

    @Override
    public Branch getBranchByID(int branchID) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Branches WHERE BranchID = ?");
        preparedStatement.setInt(1,branchID);
        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next())
        {
            Branch branch = new Branch(rs.getInt("BranchID"),rs.getString("BranchName"));
            branchMapFromDB.put(branchID,branch);
            return branch;
        }
        else {return null;}
    }

    @Override
    public Branch addBranch(String branchName) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Branches (BranchName) VALUES(?)");
        preparedStatement.setString(1,branchName);
        preparedStatement.executeUpdate();
        ResultSet rs = connection.createStatement().executeQuery("SELECT MAX(BranchID) FROM Branches");
        int last_ID = rs.getInt(1);
        return new Branch(last_ID,branchName);
    }

}
