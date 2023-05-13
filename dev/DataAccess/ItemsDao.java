package DataAccess;

import BusinessLayer.Item;
import BusinessLayer.Product;

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
    public List<Item> getAllExpiredItems() throws SQLException;
    public List<Item> getAllDamagedItems() throws SQLException;
    public List<Item> getAllSoldItems() throws SQLException;
    public List<Item> getAllStoreItems() throws SQLException;
    public List<Item> getAllStorageItems() throws SQLException;
    public Map<Integer, Item> getItemsMapFromDB();

}
