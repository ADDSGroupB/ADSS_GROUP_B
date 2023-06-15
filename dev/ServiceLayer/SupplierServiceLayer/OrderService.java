package ServiceLayer.SupplierServiceLayer;

import BusinessLayer.SupplierBusinessLayer.Order;
import BusinessLayer.SupplierBusinessLayer.PeriodicOrder;
import BusinessLayer.SupplierBusinessLayer.SupplierProduct;
import Utillity.Response;

import javax.swing.*;
import java.awt.*;
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
        StringBuilder ordersForToday = new StringBuilder();
        StringBuilder ordersErrors = new StringBuilder();
        for(PeriodicOrder periodicOrder : getAllPeriodicOrderForToday().values())
        {
            Response response = executePeriodicOrder(periodicOrder.getPeriodicOrderID());
            if(response.errorOccurred())
                ordersErrors.append(response.getErrorMessage()).append("\n");
            else
                ordersForToday.append("Periodic Order ID: ").append(periodicOrder.getPeriodicOrderID()).append("\n").append(getOrderByID(response.getSupplierId())).append("\n");
        }

        JTextArea textArea = new JTextArea(10, 30);
        textArea.setText(String.valueOf(ordersForToday));
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(700, 400)); // Set preferred size

        if(!ordersErrors.isEmpty()) JOptionPane.showMessageDialog(null, ordersErrors, "Order Errors", JOptionPane.ERROR_MESSAGE);
        if(!ordersForToday.isEmpty()) JOptionPane.showMessageDialog(null, scrollPane, "Orders For Today", JOptionPane.INFORMATION_MESSAGE);
        else JOptionPane.showMessageDialog(null, "There is no orders for today", "Orders For Today", JOptionPane.INFORMATION_MESSAGE);
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

    public HashMap<Integer, SupplierProduct> getAllSupplierProductsByID(int supplierID) { return orderService.getAllSupplierProductsByID(supplierID); }

    public Integer getLastSupplierID() {return orderService.getLastSupplierID();}
    public Response getSupplierNameById(Integer id){return orderService.getSupplierNameById(id);}
    public HashMap<Integer, PeriodicOrder> getPeriodicOrdersToBranch(int branchID){ return orderService.getPeriodicOrdersToBranch(branchID); }
    public PeriodicOrder getPeriodicOrderByID(int orderID) { return orderService.getPeriodicOrderByID(orderID); }
    public Response updatePeriodicOrder(int orderID, DayOfWeek fixedDay, HashMap<Integer, Integer> productsAndAmount) { return orderService.updatePeriodicOrder(orderID, fixedDay, productsAndAmount); }
    public Response updateOrder(int orderID, HashMap<Integer, Integer> productsAndAmount) { return orderService.updateOrder(orderID, productsAndAmount); }
}
