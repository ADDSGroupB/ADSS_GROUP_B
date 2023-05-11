package BusinessLayer;

import Utillity.Pair;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashMap;


public class Agreement {
    private static int id = 0;
    private int agreementID;
    private String paymentType;
    private Pair<Integer,Double> totalDiscountInPrecentageForOrderAmount; //
    private Pair<Double,Double> totalOrderDiscountPerOrderPrice;
    private boolean selfSupply;
    private String supplyMethod; // "FixedDay" or "DaysAmount"
    private int supplyTime;
    private ArrayList<DayOfWeek> supplyDays;

    private HashMap <Integer, SupplierProduct> supllyingProducts; //prodId, supplierProduct
    //private ArrayList<String> productsByCatlogNumber; // products that supplied to the super by the *supplier* ����� ��� �����


    public Agreement(String paymentType, boolean selfSupply, String supplyMethod, int supplyTime, ArrayList<DayOfWeek> supplyDays) {
        this.paymentType = paymentType;
        this.selfSupply = selfSupply;
        this.supplyMethod = supplyMethod;
        this.supplyTime = supplyTime;
        this.supplyDays = supplyDays;
    }
    public Agreement(String paymentType, boolean selfSupply, String supplyMethod, int supplyTime, ArrayList<DayOfWeek> supplyDays,
                     HashMap <Integer, SupplierProduct> supllyingProducts) {
        this.paymentType = paymentType;
        this.selfSupply = selfSupply;
        this.supplyMethod = supplyMethod;
        this.supplyTime = supplyTime;
        this.supplyDays = supplyDays;
        this.supllyingProducts = supllyingProducts;
    }
    public Agreement(String paymentType, boolean selfSupply, String supplyMethod, int supplyTime, ArrayList<DayOfWeek> supplyDays, HashMap <Integer, SupplierProduct> supllyingProducts,
                     Pair<Integer,Double> totalDiscountInPrecentageForOrderAmount, Pair<Double,Double> totalOrderDiscountPerOrderPrice) {
        this.paymentType = paymentType;
        this.selfSupply = selfSupply;
        this.supplyMethod = supplyMethod;
        this.supplyTime = supplyTime;
        this.supplyDays = supplyDays;
        this.supllyingProducts = supllyingProducts;
        this.totalDiscountInPrecentageForOrderAmount = totalDiscountInPrecentageForOrderAmount;
        this.totalOrderDiscountPerOrderPrice = totalOrderDiscountPerOrderPrice;
    }


//    public Agreement(String paymentType, boolean selfSupply, ArrayList<DayOfWeek> supplyDays) {
//        this.agreementID = id++;
//        this.paymentType = paymentType;
//    //    this.discountProducts = new HashMap<Integer, ArrayList<Discount>>();
//    //    this.totalOrderDiscount = new ArrayList<Discount>();
//        this.selfSupply = selfSupply;
//        this.supplyDays = supplyDays;
//    }
//
//    public Agreement(String paymentType, boolean selfSupply, ArrayList<DayOfWeek> supplyDays, HashMap <Integer, SupplierProduct> supllyingProducts) {
//        this.paymentType = paymentType;
//        this.selfSupply = selfSupply;
//        this.supplyDays = supplyDays;
//        this.supllyingProducts = supllyingProducts;
//    }
//
//    public Agreement(String paymentType, boolean selfSupply, ArrayList<DayOfWeek> supplyDays, HashMap <Integer, SupplierProduct> supllyingProducts,
//                     Pair<Integer,Double> totalDiscountInPrecentageForOrderAmount, Pair<Double,Double> totalOrderDiscountPerOrderPrice) {
//        this.paymentType = paymentType;
//        this.selfSupply = selfSupply;
//        this.supplyDays = supplyDays;
//        this.supllyingProducts = supllyingProducts;
//        this.totalDiscountInPrecentageForOrderAmount = totalDiscountInPrecentageForOrderAmount;
//        this.totalOrderDiscountPerOrderPrice = totalOrderDiscountPerOrderPrice;
//    }

    public HashMap <Integer, SupplierProduct> getSupllyingProducts(){
        return supllyingProducts;
    }
    public int getAgreementID() {
        return agreementID;
    }

    public void setSupplyMethod(String supplyMethod) {
        this.supplyMethod = supplyMethod;
    }

    public void setSupplyTime(int supplyTime) {
        this.supplyTime = supplyTime;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public boolean getSelfSupply(){
        return this.selfSupply;
    }

    public void setSelfSupply(boolean selfSupply){
        this.selfSupply = selfSupply;
    }

    public ArrayList<DayOfWeek> getSupplyDays() {
        return supplyDays;
    }

    public void setSupplyDays(ArrayList<DayOfWeek> supplyDays) {
        this.supplyDays = supplyDays;
    }

    public Pair<Double,Double> getTotalOrderDiscountPerOrderPrice(){
        return totalOrderDiscountPerOrderPrice;
    }
    public Pair<Integer,Double> getTotalDiscountInPrecentageForOrder(){
        return totalDiscountInPrecentageForOrderAmount;
    }

    public void setTotalDiscountInPrecentageForOrderAmount(Pair<Integer, Double> totalDiscountInPrecentageForOrderAmount) {
        this.totalDiscountInPrecentageForOrderAmount = totalDiscountInPrecentageForOrderAmount;
    }

    public void setTotalOrderDiscountPerOrderPrice(Pair<Double, Double> totalOrderDiscountPerOrderPrice) {
        this.totalOrderDiscountPerOrderPrice = totalOrderDiscountPerOrderPrice;
    }

    public void setSupllyingProducts(HashMap<Integer, SupplierProduct> supllyingProducts) {
        this.supllyingProducts = supllyingProducts;
    }

    public Pair<Integer, Double> getTotalDiscountInPrecentageForOrderAmount() {
        return totalDiscountInPrecentageForOrderAmount;
    }


    public String getSupplyMethod() {
        return supplyMethod;
    }

    public int getSupplyTime() {
        return supplyTime;
    }
    //    public void addTotalDiscount(String type, int quantity, int value)
//    {
//        if (type.toLowerCase().equals("Percent"))
//            totalOrderDiscount.add(new PercentDiscount(quantity, value));
//        else if (type.toLowerCase().equals("Value"))
//            totalOrderDiscount.add(new ValueDiscount(quantity, value));
//        else
//            System.out.println("Wrong type, please Choose Percent / Value");
//    }
//    public void addProductDiscount(String type, int productID, int quantity, int value)
//    {
////        discountProducts[productID].
//    }
    }
