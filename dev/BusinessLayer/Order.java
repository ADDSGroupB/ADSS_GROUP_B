package BusinessLayer;

import Utillity.Pair;

import java.util.ArrayList;
import java.time.LocalDate;

public class Order
{
    private static int id = 1;

    private String supplierName;
    private String supplierAddress;
    private int supplierId;
    private int orderID;
    private int branchID;
    private boolean collected;
    private LocalDate deliveryDate;
    private String contactPhoneNumber;
    private LocalDate orderDate;
    private ArrayList<Pair<SupplierProduct,Integer>> productsInOrder;
    private ArrayList<SupplierProduct> ItemsInOrder;

    private double totalPriceBeforeDiscount;
    private double totalPriceAfterDiscount;



    public Order(String supplierName, String supplierAddress, int supplierId ,String contactPhoneNumber, ArrayList<Pair<SupplierProduct,Integer>> productsInOrder, double totalPriceBeforeDiscount, double totalPriceAfterDiscount)
    {
        this.orderID = id++;
        this.supplierName = supplierName;
        this.supplierAddress = supplierAddress;
        this.supplierId = supplierId;
        this.contactPhoneNumber = contactPhoneNumber;
        this.orderDate = LocalDate.now();
        this.productsInOrder = productsInOrder;
        this.totalPriceBeforeDiscount = totalPriceBeforeDiscount;
        this.totalPriceAfterDiscount = totalPriceAfterDiscount;
    }

    public Order(int orderID, int supplierID, int branchID, LocalDate orderDate, LocalDate deliveryDate, boolean collected, double totalPriceBeforeDiscount, double totalPriceAfterDiscount) {
        this.orderID = orderID;
        this.supplierId = supplierID;
        this.branchID = branchID;
        this.orderDate = orderDate;
        this.deliveryDate = deliveryDate;
        this.collected = collected;
        this.totalPriceBeforeDiscount = totalPriceBeforeDiscount;
        this.totalPriceAfterDiscount = totalPriceAfterDiscount;
    }

    @Override
    public String toString(){
        String s ="";
        s+="OrderId: " + orderID + ", date: " + orderDate +  "\n";
        s+="Supplier's details: \n";
        s+="Supplier name: " + supplierName +", supplier address: " + supplierAddress + ", supplierId: " + supplierId + ", supplier's contact phone number: " + contactPhoneNumber + "\n";
        s+="Products details: \n";
        for(Pair<SupplierProduct, Integer> p : productsInOrder){
            int productId = p.getFirst().getProductID();
            String productName = p.getFirst().getName();
            int amount = p.getSecond();
            double productPrice = p.getFirst().getPrice();
            s = s + "productId: " + productId +", product name: " + productName +", amount: " +amount +", price: " + productPrice +"\n";
        }
        s+="Total price before discount: " + totalPriceBeforeDiscount +"\n";
        s+="Total price after discount: " + totalPriceAfterDiscount +"\n";
        return s;
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

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getSupplierAddress() {
        return supplierAddress;
    }

    public void setSupplierAddress(String supplierAddress) {
        this.supplierAddress = supplierAddress;
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

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getBranchID() {
        return branchID;
    }

    public void setBranchID(int branchID) {
        this.branchID = branchID;
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

    public void setDeliveryDate(LocalDate deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getContactPhoneNumber() {
        return contactPhoneNumber;
    }

    public void setContactPhoneNumber(String contactPhoneNumber) {
        this.contactPhoneNumber = contactPhoneNumber;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public ArrayList<Pair<SupplierProduct, Integer>> getProductsInOrder() {
        return productsInOrder;
    }

    public void setProductsInOrder(ArrayList<Pair<SupplierProduct, Integer>> productsInOrder) {
        this.productsInOrder = productsInOrder;
    }

    public ArrayList<SupplierProduct> getItemsInOrder() {
        return ItemsInOrder;
    }

    public void setItemsInOrder(ArrayList<SupplierProduct> itemsInOrder) {
        ItemsInOrder = itemsInOrder;
    }

    public double getTotalPriceBeforeDiscount() {
        return totalPriceBeforeDiscount;
    }

    public void setTotalPriceBeforeDiscount(double totalPriceBeforeDiscount) {
        this.totalPriceBeforeDiscount = totalPriceBeforeDiscount;
    }

    public double getTotalPriceAfterDiscount() {
        return totalPriceAfterDiscount;
    }

    public void setTotalPriceAfterDiscount(double totalPriceAfterDiscount) {
        this.totalPriceAfterDiscount = totalPriceAfterDiscount;
    }
}
