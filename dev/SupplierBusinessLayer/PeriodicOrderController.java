package SupplierBusinessLayer;

import SupplierDataAccessLayer.PeriodicOrderDAO;
import SupplierDataAccessLayer.SupplierProductDAO;
import Utillity.Response;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashMap;

public class PeriodicOrderController {
    private final PeriodicOrderDAO periodicOrderDAO;
    private final SupplierProductDAO supplierProductDAO;
    private static int id;

    public PeriodicOrderController( ) {
        this.periodicOrderDAO = new PeriodicOrderDAO();
        this.supplierProductDAO = new SupplierProductDAO();
        id = periodicOrderDAO.getLastPeriodicOrderID() + 1;
    }

    public Response createPeriodicOrder(int supplierID, int branchID, DayOfWeek fixedDay, HashMap<Integer, Integer> productsAndAmount)
    {
        HashMap<Integer, SupplierProduct> supplierProducts = supplierProductDAO.getAllSupplierProductsByID(supplierID);
        ArrayList<SupplierProduct> itemsInOrder = new ArrayList<>();
        // Check if the supplier supply all the products in the list, if one of the isn't supplied by him send informing response
        for(int productID : productsAndAmount.keySet()) {
            SupplierProduct productInSupplier = supplierProducts.get(productID);
            if(productInSupplier == null)
                return new Response("The supplier with the ID: " + supplierID + " not supplying the product with the ID: " + productID);
            SupplierProduct productInOrder = new SupplierProduct(productInSupplier);
            productInOrder.setAmount(productsAndAmount.get(productID));
            itemsInOrder.add(productInOrder);
        }
        PeriodicOrder periodicOrder = new PeriodicOrder(id++, supplierID, branchID, fixedDay, itemsInOrder);
        return periodicOrderDAO.addPeriodicOrder(periodicOrder);
    }

    public Response removePeriodicOrder(int orderID) { return periodicOrderDAO.removePeriodicOrder(orderID); }

    public HashMap<Integer, PeriodicOrder> getAllPeriodicOrders() { return periodicOrderDAO.getAllPeriodicOrders(); }

    public PeriodicOrder getPeriodicOrderByID(int orderID) { return periodicOrderDAO.getPeriodicOrderByID(orderID); }

    public HashMap<Integer, PeriodicOrder> getPeriodicOrdersFromSupplier(int supplierID) { return periodicOrderDAO.getPeriodicOrdersFromSupplier(supplierID); }

    public HashMap<Integer, PeriodicOrder> getPeriodicOrdersToBranch(int branchID){ return periodicOrderDAO.getPeriodicOrdersToBranch(branchID); }

    public HashMap<Integer, PeriodicOrder> getAllPeriodicOrderForToday() { return periodicOrderDAO.getAllPeriodicOrderForToday(); }

}