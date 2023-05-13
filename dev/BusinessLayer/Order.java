package BusinessLayer;

import Utillity.Pair;

import java.util.ArrayList;
import java.time.LocalDate;

public class Order {
    private static int id = 1;

    private String supplierName;
    private String supplierAddress;
    private int supplierId;
    private int orderID;
    private int branchID;
    private boolean collected;
    private LocalDate deliveryDate;
    private String contactPhoneNumber;
    private final LocalDate creationDate;
    private ArrayList<Pair<SupplierProduct, Integer>> productsInOrder;
    private ArrayList<SupplierProduct> itemsInOrder;



    private double totalPriceBeforeDiscount;
    private double totalPriceAfterDiscount;


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

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("OrderId: ").append(orderID).append(", date: ").append(creationDate).append("\n");
        s.append("Supplier's details: \n");
        s.append("Supplier name: ").append(supplierName).append(", supplier address: ").append(supplierAddress).append(", supplierId: ").append(supplierId).append(", supplier's contact phone number: ").append(contactPhoneNumber).append("\n");
        s.append("Products details: \n");
        for (SupplierProduct p : itemsInOrder) {
            int productId = p.getProductID();
            String productName = p.getName();
            int amount = p.getAmount();
            double productPrice = p.getPrice();
            s.append("productId: ").append(productId).append(", product name: ").append(productName).append(", amount: ").append(amount).append(", price: ").append(productPrice).append("\n");
        }
        s.append("Total price before discount: ").append(totalPriceBeforeDiscount).append("\n");
        s.append("Total price after discount: ").append(totalPriceAfterDiscount).append("\n");
        return s.toString();
    }

    public static int getId() {
        return id;
    }

    public static void setId(int id) {
        Order.id = id;
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

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
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



    public ArrayList<Pair<SupplierProduct, Integer>> getProductsInOrder() {
        return productsInOrder;
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
