package SupplierDataAccessLayer.Interfaces;

import SupplierBusinessLayer.PeriodicOrder;
import Utillity.Response;

import java.util.HashMap;

public interface iPeriodicOrderDAO {
    HashMap<Integer, PeriodicOrder> getAllPeriodicOrders();
    PeriodicOrder getPeriodicOrderByID(int orderID);
    HashMap<Integer, PeriodicOrder> getPeriodicOrdersFromSupplier(int supplierID);
    HashMap<Integer, PeriodicOrder> getPeriodicOrdersToBranch(int branchID);
    HashMap<Integer, PeriodicOrder> getAllPeriodicOrderForToday();
    Response addPeriodicOrder(PeriodicOrder order);
    Response removePeriodicOrder(int orderID);
    int getLastPeriodicOrderID();
}
