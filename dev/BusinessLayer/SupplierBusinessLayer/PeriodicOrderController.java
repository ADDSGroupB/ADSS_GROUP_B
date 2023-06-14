package BusinessLayer.SupplierBusinessLayer;

import DataAccessLayer.SupplierDataAccessLayer.PeriodicOrderDAO;
import DataAccessLayer.SupplierDataAccessLayer.SupplierProductDAO;
import Utillity.Response;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class PeriodicOrderController {
    private final PeriodicOrderDAO periodicOrderDAO;
    private final SupplierProductDAO supplierProductDAO;
    private static int id;

    public PeriodicOrderController( ) {
        this.periodicOrderDAO = new PeriodicOrderDAO();
        this.supplierProductDAO = new SupplierProductDAO();
        id = periodicOrderDAO.getLastPeriodicOrderID() + 1;
    }

    public HashMap<Integer, SupplierProduct> getAllSupplierProductsByID(int supplierID) {
        return supplierProductDAO.getAllSupplierProductsByID(supplierID);
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

    public Response updateProductsInPeriodicOrder(int orderID, HashMap<Integer, Integer> productsToAdd)
    {
        PeriodicOrder order = periodicOrderDAO.getPeriodicOrderByID(orderID);
        if(order == null)
            return new Response("Order Updating Fails, Reason: Periodic Order ID Is Not Exists");
        for(int productID : productsToAdd.keySet())
            if (supplierProductDAO.getSupplierProduct(order.getSupplierID(), productID) == null)
                return new Response("The supplier with the ID: " + order.getSupplierID() + " not supplying the product with the ID: " + productID);
        TreeSet<SupplierProduct> productsToOrder = new TreeSet<>(Comparator.comparingInt(SupplierProduct::getProductID));
        for (Map.Entry<Integer, Integer> productAndAmount : productsToAdd.entrySet())
        {
            int productID = productAndAmount.getKey();
            int amount = productAndAmount.getValue();
            SupplierProduct productInSupplier = supplierProductDAO.getSupplierProduct(order.getSupplierID(), productID);
            if(productInSupplier == null) continue;
            SupplierProduct product = new SupplierProduct(productInSupplier);
            product.setAmount(amount);
            productsToOrder.add(product);
        }
        productsToOrder.addAll(order.getItemsInOrder());
        ArrayList<SupplierProduct> productsList = new ArrayList<>(productsToOrder);
        PeriodicOrder updatedOrderForSupplier = new PeriodicOrder(order, productsList);
        Response response = periodicOrderDAO.removePeriodicOrder(orderID);
        if(response.errorOccurred()) return response;
        response = periodicOrderDAO.addPeriodicOrder(updatedOrderForSupplier);
        if(response.errorOccurred()) return response;
        return new Response(updatedOrderForSupplier.getPeriodicOrderID());
    }

    public Response removeProductsFromPeriodicOrder(int orderID, ArrayList<Integer> productsToRemove)
    {
        PeriodicOrder order = periodicOrderDAO.getPeriodicOrderByID(orderID);
        if(order == null)
            return new Response("Order Updating Fails, Reason: Periodic Order ID Is Not Exists");
        boolean found = false;
        for(int productID : productsToRemove) {
            for (SupplierProduct supplierProduct : order.getItemsInOrder())
            {
                if (supplierProduct.getProductID() == productID) {
                    found = true;
                    break;
                }
            }
            if(!found) return new Response("In this order there is no such product with the ID: " + productID);
            else found = false;
        }
        ArrayList<SupplierProduct> productsToOrder = new ArrayList<>(order.getItemsInOrder());
        for (int productID : productsToRemove)
            productsToOrder.removeIf(product -> product.getProductID() == productID);
        PeriodicOrder updatedOrderForSupplier = new PeriodicOrder(order, productsToOrder);
        Response response = periodicOrderDAO.removePeriodicOrder(orderID);
        if(response.errorOccurred()) return response;
        response = periodicOrderDAO.addPeriodicOrder(updatedOrderForSupplier);
        if(response.errorOccurred()) return response;
        return new Response(updatedOrderForSupplier.getPeriodicOrderID());
    }

}