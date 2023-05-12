package DataAccess;

import BusinessLayer.Branch;
import BusinessLayer.Product;
import BusinessLayer.Pair;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class ProductMinAmountDaoImpl implements ProductMinAmountDao{
    private Connection connection;
    private ProductsDao productsDao;
    private BranchesDao branchesDao;
    // Map<Integer(BranchID), Map<Integer(ProductID), Pair<Integer(MinAmount), String(OrderStatus)>>> branchProductMinStatusFromDB ;
    private Map<Integer, Map<Integer, Pair<Integer, String>>> branchProductMinStatusFromDB ;
    public ProductMinAmountDaoImpl() throws SQLException
    {
        connection = DBConnector.connect();
        productsDao = new ProductsDaoImpl();
        branchesDao = new BranchesDaoImpl();
        branchProductMinStatusFromDB = new HashMap<>();
    }
    @Override
    public Integer getMinAmountOfProductByBranch(int productID, int branchID) throws SQLException {
        // Test Done
        if (branchProductMinStatusFromDB.containsKey(branchID))
        {
            Map<Integer, Pair<Integer, String>> currProduct = branchProductMinStatusFromDB.get(branchID);
            if (currProduct.containsKey(productID))
            {
                Pair<Integer, String> currAmountStatus = currProduct.get(productID);
                return currAmountStatus.getFirst();
            }
        }
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            statement = connection.prepareStatement("SELECT * FROM ProductMinAmount WHERE BranchID = ? AND ProductID = ?");
            statement.setInt(1,branchID);
            statement.setInt(2,productID);
            rs = statement.executeQuery();
            if (rs.next())
            {
                Pair<Integer, String> currPair = new Pair<>(rs.getInt("MinAmount"), rs.getString("OrderStatus"));
                addToBranchProductMinStatusFromDB(productID,branchID,currPair);
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
        // Test Done
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
                Pair<Integer, String> currPair = new Pair<>(rs.getInt("MinAmount"), rs.getString("OrderStatus"));
                addToBranchProductMinStatusFromDB(currProduct.getProductID(),branchID,currPair);
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
        //Test Done
        if (branchProductMinStatusFromDB.containsKey(branchID))
        {
            Map<Integer, Pair<Integer, String>> currProduct = branchProductMinStatusFromDB.get(branchID);
            if (currProduct.containsKey(productID))
            {
                Pair<Integer, String> currAmountStatus = currProduct.get(productID);
                return currAmountStatus.getSecond();
            }
        }
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            statement = connection.prepareStatement("SELECT * FROM ProductMinAmount WHERE BranchID = ? AND ProductID = ?");
            statement.setInt(1,branchID);
            statement.setInt(2,productID);
            rs = statement.executeQuery();
            if (rs.next())
            {
                Pair<Integer, String> currPair = new Pair<>(rs.getInt("MinAmount"), rs.getString("OrderStatus"));
                addToBranchProductMinStatusFromDB(productID,branchID,currPair);
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
        //Test Done
        PreparedStatement statement = null;
        PreparedStatement statement2 = null;
        ResultSet rs = null;
        try {
            statement = connection.prepareStatement("UPDATE ProductMinAmount SET MinAmount = ? WHERE BranchID = ? AND ProductID = ?");
            statement.setInt(1,newAmount);
            statement.setInt(2,branchID);
            statement.setInt(3,productID);
            statement.executeUpdate();
            if (branchProductMinStatusFromDB.containsKey(branchID))
            {
                Map<Integer, Pair<Integer, String>> currProduct = branchProductMinStatusFromDB.get(branchID);
                if (currProduct.containsKey(productID))
                {
                    Pair<Integer, String> currAmountStatus = currProduct.get(productID);
                    currAmountStatus.setFirst(newAmount);
                    addToBranchProductMinStatusFromDB(productID,branchID,currAmountStatus);
                }
                else
                {
                    statement2 = connection.prepareStatement("SELECT * FROM ProductMinAmount WHERE BranchID = ? AND ProductID = ?");
                    statement2.setInt(1,branchID);
                    statement2.setInt(2,productID);
                    rs = statement2.executeQuery();
                    if (rs.next())
                    {
                        Pair<Integer, String> currPair = new Pair<>(rs.getInt("MinAmount"), rs.getString("OrderStatus"));
                        addToBranchProductMinStatusFromDB(productID,branchID,currPair);
                    }
                }
            }
            else
            {
                statement2 = connection.prepareStatement("SELECT * FROM ProductMinAmount WHERE BranchID = ? AND ProductID = ?");
                statement2.setInt(1,branchID);
                statement2.setInt(2,productID);
                rs = statement2.executeQuery();
                if (rs.next())
                {
                    Pair<Integer, String> currPair = new Pair<>(rs.getInt("MinAmount"), rs.getString("OrderStatus"));
                    addToBranchProductMinStatusFromDB(productID,branchID,currPair);
                }
            }
            return true;
        }
        catch (Exception e) {
            System.out.println("Error while trying to update product amount in branch : " + e.getMessage());
            return false;
        } finally {
            if (statement != null) {statement.close();}
            if (statement2 != null) {statement2.close();}
            if (rs != null) {rs.close();}
        }
    }
    @Override
    public boolean UpdateOrderStatusToProductInBranch(int productID, int branchID, String Status) throws SQLException
    {
        //Test Done
        PreparedStatement statement = null;
        PreparedStatement statement2 = null;
        ResultSet rs = null;
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
            if (branchProductMinStatusFromDB.containsKey(branchID))
            {
                Map<Integer, Pair<Integer, String>> currProduct = branchProductMinStatusFromDB.get(branchID);
                if (currProduct.containsKey(productID))
                {
                    Pair<Integer, String> currAmountStatus = currProduct.get(productID);
                    currAmountStatus.setSecond(Status);
                    addToBranchProductMinStatusFromDB(productID,branchID,currAmountStatus);
                }
                else
                {
                    statement2 = connection.prepareStatement("SELECT * FROM ProductMinAmount WHERE BranchID = ? AND ProductID = ?");
                    statement2.setInt(1,branchID);
                    statement2.setInt(2,productID);
                    rs = statement2.executeQuery();
                    if (rs.next())
                    {
                        Pair<Integer, String> currPair = new Pair<>(rs.getInt("MinAmount"), rs.getString("OrderStatus"));
                        addToBranchProductMinStatusFromDB(productID,branchID,currPair);
                    }
                }
            }
            else
            {
                statement2 = connection.prepareStatement("SELECT * FROM ProductMinAmount WHERE BranchID = ? AND ProductID = ?");
                statement2.setInt(1,branchID);
                statement2.setInt(2,productID);
                rs = statement2.executeQuery();
                if (rs.next())
                {
                    Pair<Integer, String> currPair = new Pair<>(rs.getInt("MinAmount"), rs.getString("OrderStatus"));
                    addToBranchProductMinStatusFromDB(productID,branchID,currPair);
                }
            }
            return true;
        }
        catch (Exception e) {
            System.out.println("Error while trying to update product status in branch : " + e.getMessage());
            return false;
        } finally {
            if (statement != null) {statement.close();}
            if (statement2 != null) {statement2.close();}
            if (rs != null) {rs.close();}
        }
    }

    @Override
    public boolean addNewProductToAllBranches(int productID) throws SQLException
    {
        //Test Done
        PreparedStatement statement = null;
        PreparedStatement statement2 = null;
        ResultSet rs = null;
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
                if (branchProductMinStatusFromDB.containsKey(branch.getBranchID()))
                {
                    Map<Integer, Pair<Integer, String>> currProducts = branchProductMinStatusFromDB.get(branch.getBranchID());
                    if (currProducts.containsKey(productID))
                    {
                        Pair<Integer, String> currAmountStatus =new Pair<>(0,"Not Invited");
                        addToBranchProductMinStatusFromDB(productID,branch.getBranchID(),currAmountStatus);
                    }
                    else
                    {
                        statement2 = connection.prepareStatement("SELECT * FROM ProductMinAmount WHERE BranchID = ? AND ProductID = ?");
                        statement2.setInt(1,branch.getBranchID());
                        statement2.setInt(2,productID);
                        rs = statement2.executeQuery();
                        if (rs.next())
                        {
                            Pair<Integer, String> currPair = new Pair<>(rs.getInt("MinAmount"), rs.getString("OrderStatus"));
                            addToBranchProductMinStatusFromDB(productID,branch.getBranchID(),currPair);
                        }
                    }
                }
                else
                {
                    statement2 = connection.prepareStatement("SELECT * FROM ProductMinAmount WHERE BranchID = ? AND ProductID = ?");
                    statement2.setInt(1,branch.getBranchID());
                    statement2.setInt(2,productID);
                    rs = statement2.executeQuery();
                    if (rs.next())
                    {
                        Pair<Integer, String> currPair = new Pair<>(rs.getInt("MinAmount"), rs.getString("OrderStatus"));
                        addToBranchProductMinStatusFromDB(productID,branch.getBranchID(),currPair);
                    }
                }
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
            if (statement2 != null) {statement2.close();}
            if (rs != null) {rs.close();}
        }
    }
    public boolean updateAllProductsToNewBranch(int branchID)throws SQLException
    {
        //Test Done
        PreparedStatement statement = null;
        List<Product> products = productsDao.getAllProducts();
        try
        {
            if (products == null){throw new SQLException();}
            Map<Integer, Pair<Integer, String>> currProducts = new HashMap<>();
            for (Product product : products)
            {
                statement = connection.prepareStatement("INSERT INTO ProductMinAmount (BranchID,ProductID,MinAmount,OrderStatus) VALUES(?,?,?,?)");
                statement.setInt(1,branchID);
                statement.setInt(2,product.getProductID());
                statement.setInt(3,0);
                statement.setString(4,"Not Invited");
                statement.executeUpdate();
                Pair<Integer, String> currAmountStatus =new Pair<>(0,"Not Invited");
                addToBranchProductMinStatusFromDB(product.getProductID(),branchID,currAmountStatus);
            }
            return true;
        }
        catch (Exception e)
        {
            System.out.println("Error while trying to update all the products to new branch in ProductMinAmount table : " + e.getMessage());
            return false;
        }
        finally {
            if (statement != null) {statement.close();}
        }
    }
    public boolean checkAllBranchesKnowAllProducts()throws SQLException
    {
        // Test Done
        List<Product> products = productsDao.getAllProducts();
        List<Branch> branches = branchesDao.getAllBranches();
        PreparedStatement statement = null;
        PreparedStatement statement2 = null;
        ResultSet rs = null;
        try {
            if (products == null || branches == null){throw new SQLException();}
            Map<Integer, Pair<Integer, String>> currProducts;
            for (Branch branch : branches)
            {
                for (Product product : products)
                {
                    Pair<Integer, String> currAmountStatus;
                    // case branch know product
                    statement = connection.prepareStatement("SELECT * FROM  ProductMinAmount WHERE BranchID = ? AND ProductID = ?");
                    statement.setInt(1,branch.getBranchID());
                    statement.setInt(2,product.getProductID());
                    rs = statement.executeQuery();
                    if (!rs.next()) // case branch dont  know product
                    {
                        statement2 = connection.prepareStatement("INSERT INTO ProductMinAmount (BranchID,ProductID,MinAmount,OrderStatus) VALUES(?,?,?,?)");
                        statement2.setInt(1,branch.getBranchID());
                        statement2.setInt(2,product.getProductID());
                        statement2.setInt(3,0);
                        statement2.setString(4,"Not Invited");
                        statement2.executeUpdate();
                        currAmountStatus =new Pair<>(0,"Not Invited");
                    }
                    else {currAmountStatus =new Pair<>(rs.getInt("MinAmount"),rs.getString("OrderStatus"));}
                    addToBranchProductMinStatusFromDB(product.getProductID(),branch.getBranchID(),currAmountStatus);
                }
            }
                return true;
        }
        catch (Exception e)
        {
            System.out.println("Error while trying to update all the products to all branches in ProductMinAmount table : " + e.getMessage());
            return false;
        }
        finally {
            if (statement != null) {statement.close();}
            if (statement2 != null) {statement2.close();}
            if (rs != null) {rs.close();}
        }
    }
    public Map<Integer, Map<Integer, Pair<Integer, String>>> getBranchProductMinStatusFromDB()throws SQLException
    {
        //Test Done
        return this.branchProductMinStatusFromDB;
    }
    // helper function
    public void addToBranchProductMinStatusFromDB(int productID, int branchID,Pair<Integer, String> currPair) throws SQLException
    {
        //Test Done
        if (!this.branchProductMinStatusFromDB.containsKey(branchID))
        {
            Map<Integer, Pair<Integer, String>> newProduct = new HashMap<>();
            newProduct.put(productID,currPair);
            this.branchProductMinStatusFromDB.put(branchID,newProduct);
        }
        else
        {
            Map<Integer, Pair<Integer, String>> currProducts = this.branchProductMinStatusFromDB.get(branchID);
            if (!currProducts.containsKey(productID))
            {
                currProducts.put(productID,currPair);
                this.branchProductMinStatusFromDB.put(branchID,currProducts);
            }
        }
    }


}
