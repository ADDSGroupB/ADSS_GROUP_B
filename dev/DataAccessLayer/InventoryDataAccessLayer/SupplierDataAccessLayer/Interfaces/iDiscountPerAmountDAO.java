package DataAccessLayer.InventoryDataAccessLayer.SupplierDataAccessLayer.Interfaces;

import Utillity.Response;

import java.util.HashMap;

public interface iDiscountPerAmountDAO {
    HashMap<Integer, Double> getProductDiscountByID(int supplierID, int productID);
    Response addDiscount(int supplierID, int productID, int discountPerAmount, double discount);
    Response removeAllDiscount(int supplierID, int productID);
    Response removeDiscount(int supplierID, int productID, int discountPerAmount);

}
