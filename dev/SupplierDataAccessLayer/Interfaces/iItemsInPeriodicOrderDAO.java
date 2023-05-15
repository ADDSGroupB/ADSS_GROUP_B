package SupplierDataAccessLayer.Interfaces;

import SupplierBusinessLayer.SupplierProduct;
import Utillity.Response;

import java.util.ArrayList;

public interface iItemsInPeriodicOrderDAO {
    ArrayList<SupplierProduct> getProductsInPeriodicOrder(int orderID, int supplierID);
    Response addProductsToPeriodicOrder(int orderID, ArrayList<SupplierProduct> productsInOrder);
    Response addProductToPeriodicOrder(int orderID, SupplierProduct product);
    Response removeProductFromPeriodicOrder(int orderID, int productID);
    Response updateProductAmountInPeriodicOrder(int orderID, int productID, int amountInOrder);
}
