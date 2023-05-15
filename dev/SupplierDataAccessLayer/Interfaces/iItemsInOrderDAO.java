package SupplierDataAccessLayer.Interfaces;

import SupplierBusinessLayer.SupplierProduct;
import Utillity.Response;

import java.util.ArrayList;

public interface iItemsInOrderDAO {
    ArrayList<SupplierProduct> getProductsInOrder(int orderID, int supplierID);
    Response addProductsToOrder(int orderID, ArrayList<SupplierProduct> productsInOrder);
    Response addProductToOrder(int orderID, SupplierProduct product);
    Response removeProductFromOrder(int orderID, int productID);
    Response updateProductAmountInOrder(int orderID, int productID, int amountInOrder);
}
