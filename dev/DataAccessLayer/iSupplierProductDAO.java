package DataAccessLayer;

import BusinessLayer.SupplierProduct;
import Utillity.Response;

import java.util.HashMap;

public interface iSupplierProductDAO {

    HashMap<Integer, SupplierProduct> getAllSupplierProductsByID(int supplierID);
    public SupplierProduct getSupplierProduct(int supplierID, int productID);
    public Response addSupplierProduct(int supplierID, SupplierProduct supplierProduct);
    public Response removeSupplierProduct(int supplierID, int productID);
    public Response updateSupplierProductCatalogNumber(int supplierID, int productID, int catalogNumber);

    // TODO: --------------------- Add this updates From CLI ------------------------------------
    public Response updateSupplierProductPrice(int supplierID, int productID, double price);
    public Response updateSupplierProductAmount(int supplierID, int productID, int amount);
    public Response updateSupplierProductWeight(int supplierID, int productID, double weight);
    public Response updateSupplierProductManufacturer(int supplierID, int productID, String manufacturer);
    public Response updateSupplierProductExpirationDays(int supplierID, int productID, int expirationDays);
}
