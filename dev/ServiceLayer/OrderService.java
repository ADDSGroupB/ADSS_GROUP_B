package ServiceLayer;

import BusinessLayer.Order;
import BusinessLayer.PeriodicOrder;
import BusinessLayer.SupplierProduct;
import Utillity.Response;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TimerTask;

public class OrderService  extends TimerTask {
    private iOrderService orderService;

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
    public void createOrderByShortage(int branchId ,HashMap<Integer, Integer> shortage) { orderService.createOrderByShortage(branchId ,shortage); }
}
