package ServiceLayer.SupplierServiceLayer;

import BusinessLayer.SupplierBusinessLayer.Order;
import BusinessLayer.SupplierBusinessLayer.PeriodicOrder;
import BusinessLayer.SupplierBusinessLayer.SupplierProduct;
import Utillity.Response;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashMap;

public interface iOrderService {
    Response createOrderByShortage(int branchId , HashMap<Integer, Integer> shortage);
    HashMap<Integer, Order> getOrdersFromSupplier(int supplierID);
    HashMap<Integer, Order> getOrdersToBranch(int branchID);
    HashMap<Integer, Order> getAllOrderForToday();
    HashMap<Integer, Order> getNoneCollectedOrdersForToday(int branchID);
    Response markOrderAsCollected(int orderID);

    Response executePeriodicOrder(int periodicOrderID);

    Response createPeriodicOrder(int supplierID, int branchID, DayOfWeek fixedDay, HashMap<Integer, Integer> productsAndAmount);
    Order getOrderByID(int orderID);

    HashMap<Integer, PeriodicOrder> getAllPeriodicOrderForToday();

    Response updateProductsInOrder(int orderID, HashMap<Integer, Integer> productsToAdd);

    Response removeProductsFromOrder(int orderID, ArrayList<Integer> productsToRemove);

    Response updateProductsInPeriodicOrder(int orderID, HashMap<Integer, Integer> productsToAdd);

    Response removeProductsFromPeriodicOrder(int orderID, ArrayList<Integer> productsToRemove);

    void printOrder(int supplierID);

    Integer getLastSupplierID();

    Response getSupplierNameById(Integer id);

    HashMap<Integer, SupplierProduct> getAllSupplierProductsByID(int supplierID);

    HashMap<Integer, PeriodicOrder> getPeriodicOrdersToBranch(int branchID);

    PeriodicOrder getPeriodicOrderByID(int orderID);

    Response updatePeriodicOrder(int orderID, DayOfWeek fixedDay, HashMap<Integer, Integer> productsAndAmount);

    Response updateOrder(int orderID, HashMap<Integer, Integer> productsAndAmount);
}
