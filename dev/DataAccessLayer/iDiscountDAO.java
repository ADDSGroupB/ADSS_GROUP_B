package DataAccessLayer;

import BusinessLayer.Contact;
import Utillity.Pair;
import Utillity.Response;

public interface iDiscountDAO {
    public Pair<Integer, Double> getAmountDiscountByID(int id);
    public Pair<Double, Double> getPriceDiscountByID(int id);
    public Response addDiscount(int supplierID, String type, Pair discount);
    public Response removeDiscount(int id, String type);
    // Maybe will add update
}
