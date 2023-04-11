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
    private String contactPhoneNumber;
    private LocalDate orderDate;
    private ArrayList<Pair<SupplierProduct,Integer>> productsInOrder;

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
}
