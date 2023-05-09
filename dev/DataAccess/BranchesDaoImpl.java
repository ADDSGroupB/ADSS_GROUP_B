package DataAccess;

import BusinessLayer.Branch;
import BusinessLayer.Category;
import BusinessLayer.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BranchesDaoImpl implements BranchesDao{
    private Connection conn;
    @Override
    public List<Branch> getAllBranches()throws SQLException {
        List<Branch> branches = new ArrayList<>();
        Statement stmt =conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM Branches");
        while (rs.next())
        {
            Branch branch = new Branch(rs.getInt("BranchID"),rs.getString("BranchName"));
            branches.add(branch);
        }
        return branches;
    }

    @Override
    public Branch getBranchByID(int branchID) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM Branches WHERE BranchID = ?");
        preparedStatement.setInt(1,branchID);
        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next())
        {
            Branch branch = new Branch(rs.getInt("BranchID"),rs.getString("BranchName"));
            return branch;
        }
        else {return null;}
    }

    @Override
    public void addBranch(Branch branch) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO Branches (BranchID,BranchName) VALUES(?, ?)");
        preparedStatement.setInt(1,branch.getBranchID());
        preparedStatement.setString(2,branch.getBranchName());
        preparedStatement.executeUpdate();

    }
}
