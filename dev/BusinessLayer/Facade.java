package BusinessLayer;

import java.util.HashMap;

public class Facade
{
    private ProductController productController;
    private SupplierController supplierController;

    public Facade() {
        this.productController = new ProductController();
        this.supplierController = new SupplierController();
    }

    public void addProductToSupplier(int supplierID, String name, int productID, int quantity, double price, String manufacturer)
    {
        productController.addProductToSupplier(supplierController.getSupplier(supplierID), name, productID, quantity, price, manufacturer);
    }
    public void removeProductFromSupplier(int supplierID, int productID)
    {
        productController.removeProductFromSupplier(supplierController.getSupplier(supplierID), productID);
    }
    public void removeProduct(int productID)
    {
        HashMap<Integer, Integer> supplierIDAndCatalogID = productController.getProductSuppliers(productID);
        if(supplierIDAndCatalogID != null)
            for(int supplierID : supplierIDAndCatalogID.keySet())
                removeProductFromSupplier(supplierID, productID);
    }
}
