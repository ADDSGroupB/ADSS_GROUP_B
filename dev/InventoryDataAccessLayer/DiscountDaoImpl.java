package InventoryDataAccessLayer;

import InventoryBusinessLayer.Category;
import InventoryBusinessLayer.Discount;
import InventoryBusinessLayer.Product;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class DiscountDaoImpl implements DiscountsDao {
    private Connection connection;
    private Map<Integer, Discount> discountsMapFromDB;
    private ProductsDao productsDao;
    private CategoryDao categoryDao;
    public DiscountDaoImpl() throws SQLException
    {
        connection = DBConnector.connect();
        discountsMapFromDB = new HashMap<>();
        productsDao = new ProductsDaoImpl();
        categoryDao = new CategoryDaoImpl();
    }
    @Override
    public Discount addNewDiscount(int branchID,LocalDate sDate, LocalDate eDate, double amount, Category categoryDiscount,Product productDiscount) throws SQLException {
        PreparedStatement statementCategory = null;
        ResultSet rsCategory = null;
        PreparedStatement statementProduct = null;
        ResultSet rsProduct = null;
        Discount discount;
        try {
            if (categoryDiscount != null && productDiscount == null)
            {
                statementCategory = connection.prepareStatement("INSERT INTO Discounts (BranchID,CategoryID, StartDate, EndDate, Amount) VALUES(?,?,?,?,?)");
                statementCategory.setInt(1,branchID);
                statementCategory.setInt(2,categoryDiscount.getCategoryID());
                statementCategory.setString(3,sDate.toString());
                statementCategory.setString(4,eDate.toString());
                statementCategory.setDouble(5,amount);
                statementCategory.executeUpdate();
                rsCategory = connection.createStatement().executeQuery("SELECT MAX(DiscountID) FROM Discounts");
                int last_ID = rsCategory.getInt(1);
                discount = new Discount(last_ID,branchID,sDate,eDate,amount,categoryDiscount);
                discountsMapFromDB.put(last_ID,discount);
                return discount;
            }
            else if (productDiscount != null && categoryDiscount == null)
            {
                statementProduct = connection.prepareStatement("INSERT INTO Discounts (BranchID,ProductID, StartDate, EndDate, Amount) VALUES(?,?,?,?,?)");
                statementProduct.setInt(1,branchID);
                statementProduct.setInt(2,productDiscount.getProductID());
                statementProduct.setString(3,sDate.toString());
                statementProduct.setString(4,eDate.toString());
                statementProduct.setDouble(5,amount);
                statementProduct.executeUpdate();
                rsProduct = connection.createStatement().executeQuery("SELECT MAX(DiscountID) FROM Discounts");
                int last_ID = rsProduct.getInt(1);
                discount = new Discount(last_ID,branchID,sDate,eDate,amount,productDiscount);
                discountsMapFromDB.put(last_ID,discount);
                return discount;
            }
            else {throw new SQLException();}
        }
        catch (Exception e) {
            System.out.println("Error while trying to add new discount : " + e.getMessage());
            return null;
        } finally {
            if (statementCategory != null) {statementCategory.close();}
            if (rsCategory != null) {rsCategory.close();}
            if (statementProduct != null) {statementProduct.close();}
            if (rsProduct != null) {rsProduct.close();}
        }
    }
    public Discount getDiscountByID(int discountID)throws SQLException
    {
        if (discountsMapFromDB.containsKey(discountID)){return discountsMapFromDB.get(discountID);}
        PreparedStatement statement = null;
        ResultSet rs = null;
        Discount discount ;
        try {
            statement = connection.prepareStatement("SELECT * FROM Discounts WHERE DiscountID = ?");
            statement.setInt(1, discountID);
            rs = statement.executeQuery();
            if (rs.next())
            {
                int branchID = rs.getInt("BranchID");
                int productID = rs.getInt("ProductID");
                int categoryID = rs.getInt("CategoryID");
                LocalDate startDate  =LocalDate.parse(rs.getString("StartDate"));
                LocalDate endDate  =LocalDate.parse(rs.getString("EndDate"));
                double amount  = rs.getDouble("Amount");
                Product product = productsDao.getProductByID(productID);
                Category category = categoryDao.getCategoryByID(categoryID);
                if (category == null && product !=null)
                {
                    discount = new Discount(discountID,branchID,startDate,endDate,amount,product);
                    discountsMapFromDB.put(discountID,discount);
                    return discount;
                }
                if (product == null && category != null)
                {
                    discount = new Discount(discountID,branchID,startDate,endDate,amount,category);
                    discountsMapFromDB.put(discountID,discount);
                    return discount;
                }
            }
            return null;
        }
        catch (Exception e) {
            System.out.println("Error while getting discount: " + e.getMessage());
            return null;
        } finally {
            if (rs != null) {rs.close();}
            if (statement != null) {statement.close();}
        }
    }
    @Override
    public List<Discount> getAllDiscount() throws SQLException
    {
        List<Discount> discounts = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            statement = connection.prepareStatement("SELECT * FROM Discounts");
            rs = statement.executeQuery();
            while (rs.next())
            {
                Discount discount;
                int currDiscountID = rs.getInt("DiscountID");
                if (!discountsMapFromDB.containsKey(currDiscountID))
                {
                    discount = getDiscountByID(currDiscountID);
                    discounts.add(discount);
                    discountsMapFromDB.put(currDiscountID,discount);
                }
                else
                {
                    discounts.add(discountsMapFromDB.get(currDiscountID));
                }
            }
            return discounts;
        }
        catch (Exception e) {
            System.out.println("Error while getting all discounts: " + e.getMessage());
            return null;
        } finally
        {
            if (rs != null) {rs.close();}
            if (statement != null) {statement.close();}
        }
    }
    @Override
    public List<Discount> getAllDiscountByBranchID(int branchID) throws SQLException {
            List<Discount> discounts = new ArrayList<>();
            PreparedStatement statement = null;
            ResultSet rs = null;
            try {
                statement = connection.prepareStatement("SELECT * FROM Discounts WHERE BranchID=?");
                statement.setInt(1,branchID);
                rs = statement.executeQuery();
                while (rs.next())
                {
                    Discount discount;
                    int currDiscountID = rs.getInt("DiscountID");
                    if (!discountsMapFromDB.containsKey(currDiscountID))
                    {
                        discount = getDiscountByID(currDiscountID);
                        discounts.add(discount);
                        discountsMapFromDB.put(currDiscountID,discount);
                    }
                    else
                    {
                        discounts.add(discountsMapFromDB.get(currDiscountID));
                    }
                }
                return discounts;
            }
            catch (Exception e) {
                System.out.println("Error while getting all discounts by branch ID: " + e.getMessage());
                return null;
            } finally
            {
                if (rs != null) {rs.close();}
                if (statement != null) {statement.close();}
            }
    }
    @Override
    public Discount getLastDiscountOfProductInBranch(int productID,int branchID) throws SQLException {
        PreparedStatement statement = null;
        ResultSet rs = null;
        Discount discount = null;
        try {
            statement = connection.prepareStatement(
                    "SELECT * FROM Discounts WHERE ProductID = ? AND BranchID = ? " +
                            "AND DiscountID = (SELECT MAX(DiscountID) FROM Discounts WHERE ProductID = ? AND BranchID = ?)"
            );
            statement.setInt(1, productID);
            statement.setInt(2, branchID);
            statement.setInt(3, productID);
            statement.setInt(4, branchID);
            rs = statement.executeQuery();
            if (rs.next()) {
                int discountID = rs.getInt("DiscountID");
                discount = getDiscountByID(discountID);
                discountsMapFromDB.put(discountID,discount);
            }
        } catch (Exception e) {
            System.out.println("Error while getting last discount of product in branch: " + e.getMessage());
        } finally {
            if (rs != null) {rs.close();}
            if (statement != null) {statement.close();}
        }
        return discount;
    }

    @Override
    public Discount getLastDiscountOfCategoryInBranch(int categoryID,int branchID) throws SQLException {
        PreparedStatement statement = null;
        ResultSet rs = null;
        Discount discount = null;
        try {
            statement = connection.prepareStatement(
                    "SELECT * FROM Discounts WHERE CategoryID = ? AND BranchID = ? " +
                            "AND DiscountID = (SELECT MAX(DiscountID) FROM Discounts WHERE CategoryID = ? AND BranchID = ?)"
            );
            statement.setInt(1, categoryID);
            statement.setInt(2, branchID);
            statement.setInt(3, categoryID);
            statement.setInt(4, branchID);
            rs = statement.executeQuery();
            if (rs.next()) {
                int discountID = rs.getInt("DiscountID");
                discount = getDiscountByID(discountID);
                discountsMapFromDB.put(discountID,discount);
            }
        } catch (Exception e) {
            System.out.println("Error while getting last discount of category in branch: " + e.getMessage());
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (statement != null) {
                statement.close();
            }
        }
        return discount;
    }

    @Override
    public boolean checkValidDiscount(int discountID){
        if (discountsMapFromDB.containsKey(discountID))
        {
            Discount discount = discountsMapFromDB.get(discountID);
            LocalDate startDate = discount.getStartDate();
            LocalDate endDate = discount.getEndDate();
            return startDate.isBefore(LocalDate.now()) && endDate.isAfter(LocalDate.now());
        }
        try {
                Discount discount = getDiscountByID(discountID);
                return discount.getStartDate().isBefore(LocalDate.now()) && discount.getEndDate().isAfter(LocalDate.now());
        }
        catch (Exception e) {
            System.out.println("Error while trying to check if discount is valid : " + e.getMessage());
            return false;
        }
    }
    @Override
    public Map<Integer, Discount> getDiscountsMapFromDB() {
        {return discountsMapFromDB;}
    }
}
