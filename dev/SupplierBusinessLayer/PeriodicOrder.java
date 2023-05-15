package SupplierBusinessLayer;

import java.time.DayOfWeek;
import java.util.ArrayList;

public class PeriodicOrder {
    private int periodicOrderID;
    private int supplierID;
    private int branchID;
    private DayOfWeek fixedDay;
    ArrayList<SupplierProduct> itemsInOrder;

    public PeriodicOrder(int periodicOrderID, int supplierID, int branchID, DayOfWeek fixedDay, ArrayList<SupplierProduct> itemsInOrder) {
        this.periodicOrderID = periodicOrderID;
        this.supplierID = supplierID;
        this.branchID = branchID;
        this.fixedDay = fixedDay;
        this.itemsInOrder = itemsInOrder;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("Periodic Order ID: ").append(periodicOrderID).append(", Supplier ID: ").append(supplierID).append(", Branch ID: ").append(branchID).append(", Fixed Day: ").append(fixedDay).append("\n");
        s.append("Products details: \n");
        for (SupplierProduct p : itemsInOrder) {
            int productId = p.getProductID();
            String productName = p.getName();
            int amount = p.getAmount();
            double productPrice = p.getPrice();
            s.append("Product ID: ").append(productId).append(", Product Name: ").append(productName).append(", Amount: ").append(amount).append(", Price: ").append(productPrice).append("\n");
        }
        return s.toString();
    }

    public int getPeriodicOrderID() {
        return periodicOrderID;
    }


    public int getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(int supplierID) {
        this.supplierID = supplierID;
    }

    public int getBranchID() {
        return branchID;
    }

    public void setBranchID(int branchID) {
        this.branchID = branchID;
    }

    public DayOfWeek getFixedDay() {
        return fixedDay;
    }

    public void setFixedDay(DayOfWeek fixedDay) {
        this.fixedDay = fixedDay;
    }

    public ArrayList<SupplierProduct> getItemsInOrder() {
        return itemsInOrder;
    }

    public void setItemsInOrder(ArrayList<SupplierProduct> itemsInOrder) {
        this.itemsInOrder = itemsInOrder;
    }
}
