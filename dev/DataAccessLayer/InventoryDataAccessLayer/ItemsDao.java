package DataAccessLayer.InventoryDataAccessLayer;

import BusinessLayer.InventoryBusinessLayer.Branch;
import BusinessLayer.InventoryBusinessLayer.Item;
import BusinessLayer.InventoryBusinessLayer.Product;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ItemsDao {
    public List<Item> getAllItems() throws SQLException;
    public Item getItemByID(int itemID) throws SQLException;
    public Item addItem(int branchID, LocalDate expiredDate, LocalDate arrivalDate, double priceFromSupplier , double priceInBranch , int supplierID, Product product) throws SQLException;
    public Item updateItemStatus(int itemID,String status)throws SQLException;
    public Item updateItemPriceAfterDiscount(int itemID,double priceAfterDiscount)throws SQLException;
    public Item updateItemDefectiveDescription(int itemID,String description)throws SQLException;
    public List<Item> getAllExpiredItemsByBranchID(int branchID) throws SQLException;
    public List<Item> getAllDamagedItemsByBranchID(int branchID) throws SQLException;
    public List<Item> getAllSoldItemByBranchID(int branchID) throws SQLException;
    public List<Item> getAllStoreItemsByBranchID(int branchID) throws SQLException;
    public List<Item> getAllStorageItemsByBranchID(int branchID) throws SQLException;
    public Item getItemForSale(int productID,int branchID)throws SQLException;
    public Map<Integer, Item> getItemsMapFromDB();
    public void fromStorageToStore(Branch branch)throws SQLException;
    public List<Item> getAllStoreItemsByBranchIDAndProductID(int branchID,int productID) throws SQLException;
    public List<Item> getAllStorageItemsByBranchIDAndProductID(int branchID,int productID) throws SQLException;
    public Item getMinIDItemInStorage(int productID,int branchID)throws SQLException;
    public void checkExpiredItemsInAllBranches()throws SQLException;


}
