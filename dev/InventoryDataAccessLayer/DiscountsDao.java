package InventoryDataAccessLayer;

import InventoryBusinessLayer.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface DiscountsDao {
    public Discount addNewDiscount(int branchID,LocalDate sDate, LocalDate eDate, double amount, Category categoryDiscount,Product productDiscount) throws SQLException;
    public Discount getDiscountByID(int discountID)throws SQLException;
    public List<Discount> getAllDiscount()throws SQLException;
    public List<Discount> getAllDiscountByBranchID(int branchID)throws SQLException;
    public Discount getLastDiscountOfProductInBranch(int productID,int branchID)throws SQLException;
    public Discount getLastDiscountOfCategoryInBranch(int categoryID,int branchID)throws SQLException;
    public boolean checkValidDiscount(int discountID);
    public Map<Integer, Discount> getDiscountsMapFromDB();





}
