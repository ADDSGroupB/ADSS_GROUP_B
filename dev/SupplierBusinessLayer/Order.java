package SupplierBusinessLayer;


import java.util.ArrayList;
import java.time.LocalDate;

public class Order {

    private final String supplierName;
    private final String supplierAddress;
    private final int supplierId;
    private final int orderID;
    private final int branchID;
    private boolean collected;
    private final LocalDate deliveryDate;
    private final String contactPhoneNumber;
    private final LocalDate creationDate;
    private ArrayList<SupplierProduct> itemsInOrder;



    private final double totalPriceBeforeDiscount;
    private final double totalPriceAfterDiscount;


    public Order(int orderID, String supplierName, String supplierAddress, int supplierId, String contactPhoneNumber, ArrayList<SupplierProduct> productsInOrder, double totalPriceBeforeDiscount, double totalPriceAfterDiscount, LocalDate deliveryDate, int branchID) {
        this.orderID = orderID;
        this.supplierName = supplierName;
        this.supplierAddress = supplierAddress;
        this.supplierId = supplierId;
        this.contactPhoneNumber = contactPhoneNumber;
        this.creationDate = LocalDate.now();
        this.itemsInOrder = productsInOrder;
        this.totalPriceBeforeDiscount = totalPriceBeforeDiscount;
        this.totalPriceAfterDiscount = totalPriceAfterDiscount;
        this.deliveryDate = deliveryDate;
        this.collected = false;
        this.branchID = branchID;
    }

    public Order(int orderID, int supplierID, String supplierName, String supplierAddress, String contactPhoneNumber, int branchID, LocalDate creationDate, LocalDate deliveryDate, boolean collected, double totalPriceBeforeDiscount, double totalPriceAfterDiscount) {
        this.orderID = orderID;
        this.supplierId = supplierID;
        this.supplierName = supplierName;
        this.supplierAddress = supplierAddress;
        this.contactPhoneNumber = contactPhoneNumber;
        this.branchID = branchID;
        this.creationDate = creationDate;
        this.deliveryDate = deliveryDate;
        this.collected = collected;
        this.totalPriceBeforeDiscount = totalPriceBeforeDiscount;
        this.totalPriceAfterDiscount = totalPriceAfterDiscount;
    }

    public Order(Order other, ArrayList<SupplierProduct> productsInOrder, double priceBeforeDiscount, double priceAfterDiscount) {
        this.supplierName = other.supplierName;
        this.supplierAddress = other.supplierAddress;
        this.supplierId = other.supplierId; // getSupplierID
        this.orderID = other.orderID;
        this.branchID = other.branchID; // getBranchID
        this.collected = other.collected;
        this.deliveryDate = other.deliveryDate; // getDeliveryDate()
        this.contactPhoneNumber = other.contactPhoneNumber;
        this.creationDate = other.creationDate;
        this.itemsInOrder = productsInOrder; // ArrayList<SupplierProduct> - product.getPrice() , getAmount() , getWeight(), getExpirationDays()
        this.totalPriceBeforeDiscount = priceBeforeDiscount;
        this.totalPriceAfterDiscount = priceAfterDiscount;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("OrderID: ").append(orderID).append(", branchID: ").append(branchID).append(", creation date: ").append(creationDate).append(", delivery date: ").append(deliveryDate).append("\n");
        s.append("Supplier's details: \n");
        s.append("Supplier name: ").append(supplierName).append(", supplier address: ").append(supplierAddress).append(", supplierID: ").append(supplierId).append(", supplier's contact phone number: ").append(contactPhoneNumber).append("\n");
        s.append("Products details: \n");
        for (SupplierProduct p : itemsInOrder) {
            int productId = p.getProductID();
            String productName = p.getName();
            int amount = p.getAmount();
            double productPrice = p.getPrice();
            s.append("productID: ").append(productId).append(", product name: ").append(productName).append(", amount: ").append(amount).append(", price: ").append(productPrice).append("\n");
        }
        s.append("Total price before discount: ").append(totalPriceBeforeDiscount).append("\n");
        s.append("Total price after discount: ").append(totalPriceAfterDiscount).append("\n");
        return s.toString();

    }

    public String getSupplierName() {
        return supplierName;
    }

    public String getSupplierAddress() {
        return supplierAddress;
    }

    public int getSupplierId() {
        return supplierId;
    }


    public int getOrderID() {
        return orderID;
    }


    public int getBranchID() {
        return branchID;
    }


    public boolean isCollected() {
        return collected;
    }

    public void setCollected(boolean collected) {
        this.collected = collected;
    }

    public LocalDate getDeliveryDate() {
        return deliveryDate;
    }


    public String getContactPhoneNumber() {
        return contactPhoneNumber;
    }



    public ArrayList<SupplierProduct> getItemsInOrder() {
        return itemsInOrder;
    }

    public void setItemsInOrder(ArrayList<SupplierProduct> itemsInOrder) {
        this.itemsInOrder = itemsInOrder;
    }

    public double getTotalPriceBeforeDiscount() {
        return totalPriceBeforeDiscount;
    }



    public double getTotalPriceAfterDiscount() {
        return totalPriceAfterDiscount;
    }


    public LocalDate getCreationDate() {
        return creationDate;
    }
}
