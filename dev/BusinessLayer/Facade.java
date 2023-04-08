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

    public void addProductToSupplier(int supplierID, String name, int productID, int quantity, double price, String manufacturer) throws Exception {
        if (supplierController.getSupplier(supplierID) == null) throw new Exception("There is no supplier with this ID.");
        productController.addProductToSupplier(supplierController.getSupplier(supplierID), name, productID, quantity, price, manufacturer);
    }
    public void removeProductFromSupplier(int supplierID, int productID) throws Exception
    {
        if (supplierController.getSupplier(supplierID) == null) throw new Exception("There is no supplier with this ID.");
        productController.removeProductFromSupplier(supplierController.getSupplier(supplierID), productID);
    }
    public void updateProductOnSupplier(int supplierID, String name, int productID, int quantity, double price, String manufacturer) throws Exception
    {
        if (supplierController.getSupplier(supplierID) == null) throw new Exception("There is no supplier with this ID.");
        productController.updateProductOnSupplier(supplierController.getSupplier(supplierID), name, productID, quantity, price, manufacturer);
    }
    public void addQuantityToProductOnSupplier(int supplierID, int productID, int quantity) throws Exception
    {
        if (supplierController.getSupplier(supplierID) == null) throw new Exception("There is no supplier with this ID.");
        productController.addQuantityToProductOnSupplier(supplierController.getSupplier(supplierID), productID, quantity);
    }
    public HashMap<Integer, Integer> getProductSuppliers(int productID) throws Exception {
        return productController.getProductSuppliers(productID);
    }
}
