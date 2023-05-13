package DataAccessLayer;

import BusinessLayer.Order;
import BusinessLayer.Supplier;
import Utillity.Response;

import java.util.ArrayList;
import java.util.HashMap;

public interface iSupplierOrderDAO {
    HashMap<Integer, Order> getAllOrders();
    Order getOrderByID(int orderID);
    HashMap<Integer, Order> getOrdersFromSupplier(int supplierID);
    HashMap<Integer, Order> getOrdersToBranch(int branchID);
    HashMap<Integer, Order> getAllOrderForToday();
    Response addOrder(Order order);
    Response removeOrder(int orderID);
    Response updateSupplierName(int orderID, String name);

    int getLastOrderID();
    void printOrder(int orderID);
}
