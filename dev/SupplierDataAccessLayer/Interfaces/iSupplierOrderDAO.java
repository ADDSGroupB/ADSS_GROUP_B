package SupplierDataAccessLayer.Interfaces;

import SupplierBusinessLayer.Order;
import Utillity.Response;

import java.util.HashMap;

public interface iSupplierOrderDAO {
    HashMap<Integer, Order> getAllOrders();
    Order getOrderByID(int orderID);
    HashMap<Integer, Order> getOrdersFromSupplier(int supplierID);
    HashMap<Integer, Order> getOrdersToBranch(int branchID);
    HashMap<Integer, Order> getAllOrderForToday();
    HashMap<Integer, Order> getNoneCollectedOrdersForToday(int branchID);
    Response addOrder(Order order);

    Response removeOrder(int orderID);
    Response markOrderAsCollected(int orderID);

    int getLastOrderID();
//    void printOrder(int orderID); // TODO: Maybe
}
