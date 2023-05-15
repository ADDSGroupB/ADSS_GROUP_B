package ServiceLayer.SupplierServiceLayer;

import BusinessLayer.SupplierBusinessLayer.Order;
import BusinessLayer.SupplierBusinessLayer.PeriodicOrder;
import Utillity.Response;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TimerTask;

public class OrderService extends TimerTask {
    private final iOrderService orderService;

    public OrderService() {
        orderService = new SupplierService();
    }
    @Override
    public void run()
    {
        System.out.println("Periodic Orders That Executed Today: ");
        for(PeriodicOrder periodicOrder : getAllPeriodicOrderForToday().values())
        {
            Response response = executePeriodicOrder(periodicOrder.getPeriodicOrderID());
            if(response.errorOccurred()) System.out.println(response.getErrorMessage());
            else
            {
                System.out.println("Periodic Order ID: " + periodicOrder.getPeriodicOrderID());
                System.out.println(getOrderByID(response.getSupplierId()));
            }
        }
    }

    public Order getOrderByID(int orderID) { return orderService.getOrderByID(orderID); }
    public HashMap<Integer, PeriodicOrder> getAllPeriodicOrderForToday() { return orderService.getAllPeriodicOrderForToday(); }
    public HashMap<Integer, Order> getNoneCollectedOrdersForToday(int branchID) { return orderService.getNoneCollectedOrdersForToday(branchID); }
    public HashMap<Integer, Order> getOrdersFromSupplier(int supplierID) { return orderService.getOrdersFromSupplier(supplierID); }
    public HashMap<Integer, Order> getOrdersToBranch(int branchID) { return orderService.getOrdersToBranch(branchID); }
    public HashMap<Integer, Order> getAllOrderForToday() { return orderService.getAllOrderForToday(); }
    public Response markOrderAsCollected(int orderID) { return orderService.markOrderAsCollected(orderID); }
    public Response executePeriodicOrder(int periodicOrderID) { return orderService.executePeriodicOrder(periodicOrderID); }
    public Response createPeriodicOrder(int supplierID, int branchID, DayOfWeek fixedDay, HashMap<Integer, Integer> productsAndAmount) { return orderService.createPeriodicOrder(supplierID, branchID, fixedDay, productsAndAmount); }
    public Response createOrderByShortage(int branchId ,HashMap<Integer, Integer> shortage) { return orderService.createOrderByShortage(branchId ,shortage); }
    public Response updateProductsInOrder(int orderID, HashMap<Integer, Integer> productsToAdd) { return orderService.updateProductsInOrder(orderID, productsToAdd); }
    public Response removeProductsFromOrder(int orderID, ArrayList<Integer> productsToRemove) { return orderService.removeProductsFromOrder(orderID, productsToRemove); }
    public Response updateProductsInPeriodicOrder(int orderID, HashMap<Integer, Integer> productsToAdd) { return orderService.updateProductsInPeriodicOrder(orderID, productsToAdd); }
    public Response removeProductsFromPeriodicOrder(int orderID, ArrayList<Integer> productsToRemove) { return orderService.removeProductsFromPeriodicOrder(orderID, productsToRemove); }
    public void printOrder(int supplierID) { orderService.printOrder(supplierID); }
}
