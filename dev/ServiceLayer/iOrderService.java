package ServiceLayer;

import BusinessLayer.Order;
import BusinessLayer.PeriodicOrder;
import BusinessLayer.SupplierProduct;
import Utillity.Response;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashMap;

public interface iOrderService {
    void createOrderByShortage(int branchId , HashMap<Integer, Integer> shortage);
    HashMap<Integer, Order> getOrdersFromSupplier(int supplierID);
    HashMap<Integer, Order> getOrdersToBranch(int branchID);
    HashMap<Integer, Order> getAllOrderForToday();
    HashMap<Integer, Order> getNoneCollectedOrdersForToday(int branchID);
    Response markOrderAsCollected(int orderID);

    Response executePeriodicOrder(int periodicOrderID);

    Response createPeriodicOrder(int supplierID, int branchID, DayOfWeek fixedDay, HashMap<Integer, Integer> productsAndAmount);
    Order getOrderByID(int orderID);

    HashMap<Integer, PeriodicOrder> getAllPeriodicOrderForToday();
}
