package DataAccess;

import BusinessLayer.Branch;
import BusinessLayer.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ProductMinAmountDaoImpl implements ProductMinAmountDao{
    private Connection connection;
    private ProductsDao productsDao;
    private BranchesDao branchesDao;
    public ProductMinAmountDaoImpl() throws SQLException
    {
        connection = DBConnector.connect();
        productsDao = new ProductsDaoImpl();
        branchesDao = new BranchesDaoImpl();
    }
    @Override
    public Integer getMinAmountOfProductByBranch(int productID, int branchID) throws SQLException {
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            statement = connection.prepareStatement("SELECT * FROM ProductMinAmount WHERE BranchID = ? AND ProductID = ?");
            statement.setInt(1,branchID);
            statement.setInt(2,productID);
            rs = statement.executeQuery();
            if (rs.next())
            {
               return rs.getInt("MinAmount");
            }
            return null;
        }
        catch (Exception e){
            System.out.println("Error while getting min amount of product in branch : " + e.getMessage());
            return null;
        }
        finally {
            if (statement!=null){statement.close();
                if (rs != null) {rs.close();}}
        }
    }

    @Override
    public Map<Product, Integer> getMinOfAllProductsByBranchID(int branchID) throws SQLException {
        PreparedStatement statement = null;
        ResultSet rs = null;
        Map<Product, Integer> productMinAmountMap = new HashMap<>();
        try {
            statement = connection.prepareStatement("SELECT * FROM ProductMinAmount WHERE BranchID = ?");
            statement.setInt(1,branchID);
            rs = statement.executeQuery();
            while (rs.next())
            {
                Product currProduct = productsDao.getProductByID(rs.getInt("ProductID"));
                productMinAmountMap.put(currProduct,rs.getInt("MinAmount"));
            }
            return productMinAmountMap;
        }
        catch (Exception e){
            System.out.println("Error while getting min amount of all products in branch : " + e.getMessage());
            return null;
        }
        finally {
            if (statement!=null){statement.close();
                if (rs != null) {rs.close();}}
        }
    }
    @Override
    public String getOrderStatusByProductInBranch(int productID, int branchID) throws SQLException {
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            statement = connection.prepareStatement("SELECT * FROM ProductMinAmount WHERE BranchID = ? AND ProductID = ?");
            statement.setInt(1,branchID);
            statement.setInt(2,productID);
            rs = statement.executeQuery();
            if (rs.next())
            {
                return rs.getString("OrderStatus");
            }
            return null;
        }
        catch (Exception e){
            System.out.println("Error while getting order status of product in branch : " + e.getMessage());
            return null;
        }
        finally {
            if (statement!=null){statement.close();
                if (rs != null) {rs.close();}}
        }
    }

    @Override
    public boolean UpdateMinAmountToProductInBranch(int productID, int branchID,int newAmount) throws SQLException {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement("UPDATE ProductMinAmount SET MinAmount = ? WHERE BranchID = ? AND ProductID = ?");
            statement.setInt(1,newAmount);
            statement.setInt(2,branchID);
            statement.setInt(3,productID);
            statement.executeUpdate();
            return true;
        }
        catch (Exception e) {
            System.out.println("Error while trying to update product amount in branch : " + e.getMessage());
            return false;
        } finally {
            if (statement != null) {statement.close();}
        }
    }

    @Override
    public boolean UpdateOrderStatusToProductInBranch(int productID, int branchID, String Status) throws SQLException
    {
        PreparedStatement statement = null;
        try {
            if (!Objects.equals(Status, "Invited") && !Objects.equals(Status, "Not Invited"))
            {
                throw new SQLException();
            }
            statement = connection.prepareStatement("UPDATE ProductMinAmount SET OrderStatus = ? WHERE BranchID = ? AND ProductID = ?");
            statement.setString(1,Status);
            statement.setInt(2,branchID);
            statement.setInt(3,productID);
            statement.executeUpdate();
            return true;
        }
        catch (Exception e) {
            System.out.println("Error while trying to update product status in branch : " + e.getMessage());
            return false;
        } finally {
            if (statement != null) {statement.close();}
        }
    }

    @Override
    public boolean addNewProductToAllBranches(int productID) throws SQLException
    {
        PreparedStatement statement = null;
        List<Branch> branches = branchesDao.getAllBranches();
        try
        {
            if (branches == null){throw new SQLException();}
            for (Branch branch : branches)
            {
                statement = connection.prepareStatement("INSERT INTO ProductMinAmount (BranchID,ProductID,MinAmount,OrderStatus) VALUES(?,?,?,?)");
                statement.setInt(1,branch.getBranchID());
                statement.setInt(2,productID);
                statement.setInt(3,0);
                statement.setString(4,"Not Invited");
                statement.executeUpdate();
            }
            return true;
        }
        catch (Exception e)
        {
            System.out.println("Error while trying to add new product to ProductMinAmount table : " + e.getMessage());
            return false;
        }
        finally {
            if (statement != null) {statement.close();}
        }
    }
}
