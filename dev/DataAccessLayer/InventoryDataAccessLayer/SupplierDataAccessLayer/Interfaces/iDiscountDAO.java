package DataAccessLayer.InventoryDataAccessLayer.SupplierDataAccessLayer.Interfaces;

import Utillity.Pair;
import Utillity.Response;

public interface iDiscountDAO {
    Pair<Integer, Double> getAmountDiscountByID(int id);
    Pair<Double, Double> getPriceDiscountByID(int id);
    Response addDiscount(int supplierID, String type, Pair<? extends Number, Double> discount);
    Response removeDiscount(int id, String type);
    // Maybe will add update
}
