package DataAccessLayer;

import BusinessLayer.Pair;
import BusinessLayer.Product;
import java.sql.SQLException;
import java.util.Map;
public interface ProductMinAmountDao {
    public Integer getMinAmountOfProductByBranch(int productID,int branchID) throws SQLException;
    public Map<Product,Integer> getMinOfAllProductsByBranchID(int branchID)throws SQLException;
    public String getOrderStatusByProductInBranch(int productID,int branchID)throws SQLException;
    public boolean UpdateMinAmountToProductInBranch(int productID,int branchID,int newAmount)throws SQLException;
    public boolean UpdateOrderStatusToProductInBranch(int productID,int branchID,String Status)throws SQLException;
    public boolean addNewProductToAllBranches(int productID)throws SQLException;
    public boolean updateAllProductsToNewBranch(int branchID)throws SQLException;
    public boolean checkAllBranchesKnowAllProducts()throws SQLException;
    public Map<Integer, Map<Integer, Pair<Integer, String>>> getBranchProductMinStatusFromDB()throws SQLException;
    public void addToBranchProductMinStatusFromDB(int productID, int branchID,Pair<Integer, String> currPair) throws SQLException;
}
