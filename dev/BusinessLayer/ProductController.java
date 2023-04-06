package BusinessLayer;

import java.util.HashMap;

public class ProductController {
    private HashMap<Integer, HashMap<Integer, Integer>> productIDAndCatalogID; // <productId: <supplierId: catalogId>>
    public void addProductToSupplier(Supplier supplier, String name, int productID, int quantity, double price, String manufacturer)
    {
        if(!productIDAndCatalogID.containsKey(productID)) productIDAndCatalogID.put(productID, new HashMap<>());
        if (!productIDAndCatalogID.get(productID).containsKey(supplier.getSupplierID()))
        {
            supplier.addProduct(name, productID, quantity, price, manufacturer);
            productIDAndCatalogID.get(productID).put(supplier.getSupplierID(), supplier.getCatalogID(productID));
        }
    }
    public void removeProductFromSupplier(Supplier supplier, int productID)
    {
        if(productIDAndCatalogID.containsKey(productID) && productIDAndCatalogID.get(productID).containsKey(supplier.getSupplierID()))
        {
            supplier.removeProduct(productIDAndCatalogID.get(productID).get(supplier.getSupplierID()));
            productIDAndCatalogID.get(productID).remove(supplier.getSupplierID());
        }
    }

    public HashMap<Integer, Integer> getProductSuppliers(int productID)
    {
        if(productIDAndCatalogID.containsKey(productID)) return productIDAndCatalogID.get(productID);
        return null;
    }

}