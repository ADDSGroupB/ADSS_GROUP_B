package BusinessLayer;

import java.util.ArrayList;
import java.time.LocalDate;

public class Order
{
    private static int id = 0;
    private final int orderID;
    private int supplierID;
    private final LocalDate orderDate;
    private ArrayList<Integer> products;
    private double price;

    public Order(int supplierID, ArrayList<Integer> products, double price)
    {
        this.orderID = id++;
        this.supplierID = supplierID;
        this.orderDate = LocalDate.now();
        this.products = products;
        this.price = price;
    }

    public int getOrderID() {
        return orderID;
    }

    public int getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(int supplierID) {
        this.supplierID = supplierID;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

}
