package DataAccessLayer;

import BusinessLayer.SupplierProduct;
import Utillity.Response;

import java.util.HashMap;

public interface iSupplierProductDAO {

    HashMap<Integer, SupplierProduct> getAllSupplierProductsByID(int supplierID);
    SupplierProduct getSupplierProduct(int supplierID, int productID);
    Response addSupplierProduct(int supplierID, SupplierProduct supplierProduct);
    Response removeSupplierProduct(int supplierID, int productID);
    Response updateSupplierProductCatalogNumber(int supplierID, int productID, int catalogNumber);
    void printProductsBySupplierID(int supplierID);

    // TODO: --------------------- Add this updates From CLI ------------------------------------
    Response updateSupplierProductPrice(int supplierID, int productID, double price);
    Response updateSupplierProductAmount(int supplierID, int productID, int amount);
    Response updateSupplierProductWeight(int supplierID, int productID, double weight);
    Response updateSupplierProductManufacturer(int supplierID, int productID, String manufacturer);
    Response updateSupplierProductExpirationDays(int supplierID, int productID, int expirationDays);
}
