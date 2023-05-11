package DataAccessLayer;

import Utillity.Pair;
import Utillity.Response;

import java.util.HashMap;

public interface iDiscountPerAmountDAO {
    public HashMap<Integer, Double> getProductDiscountByID(int supplierID, int productID);
    public Response addDiscount(int supplierID, int productID, int discountPerAmount, double discount);
    public Response removeAllDiscount(int supplierID, int productID);
    public Response removeDiscount(int supplierID, int productID, int discountPerAmount);

}
