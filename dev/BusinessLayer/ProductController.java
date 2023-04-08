package BusinessLayer;

import java.util.HashMap;

public class ProductController {
    private HashMap<Integer, HashMap<Integer, Integer>> productIDAndCatalogID; // <productId: <supplierId: catalogId>>

    public ProductController() {
        this.productIDAndCatalogID = new HashMap<>();
    }

    public void addProductToSupplier(Supplier supplier, String name, int productID, int quantity, double price, String manufacturer) throws Exception {

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
    public void updateProductOnSupplier(Supplier supplier, String name, int productID, int quantity, double price, String manufacturer)
    {
        if(productIDAndCatalogID.containsKey(productID) && productIDAndCatalogID.get(productID).containsKey(supplier.getSupplierID()))
        {
            SupplierProduct product = supplier.getProduct(productID);
            product.setName(name);
            product.setProductID(productID);
            product.setManufacturer(manufacturer);
            product.setPrice(price);
            product.setQuantity(quantity);
        }
    }
    public void addQuantityToProductOnSupplier(Supplier supplier, int productID, int quantity)
    {
        if(productIDAndCatalogID.containsKey(productID) && productIDAndCatalogID.get(productID).containsKey(supplier.getSupplierID()))
            supplier.getProduct(productID).addQuantity(quantity);
    }

    public HashMap<Integer, Integer> getProductSuppliers(int productID) throws Exception {
        if(!productIDAndCatalogID.containsKey(productID)) throw new Exception("There is no product with this ID.");
        return productIDAndCatalogID.get(productID);
    }

}