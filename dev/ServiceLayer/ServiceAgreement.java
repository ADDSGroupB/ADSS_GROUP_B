package ServiceLayer;

import Utillity.Pair;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashMap;

public class ServiceAgreement {
    private String paymentType;

    private boolean selfSupply;
    private ArrayList<DayOfWeek> supplyDays;

    private HashMap <Integer, SupplierProductService> supllyingProducts;

    private Pair<Integer,Double> totalDiscountInPrecentageForOrderAmount; //
    private Pair<Double,Double> totalOrderDiscountPerOrderPrice;

    public ServiceAgreement(String paymentType, boolean selfSupply, ArrayList<DayOfWeek> supplyDays, HashMap <Integer, SupplierProductService> supllyingProducts
    ){
        this.paymentType=paymentType;
        this.selfSupply=selfSupply;
        this.supplyDays=supplyDays;
        this.supllyingProducts=supllyingProducts;
        //System.out.println("created a new service agreement, paymentType="+paymentType+" supplDays= "+supplyDays+" supllingProducts"+supllyingProducts.get(123));
    }
    public ServiceAgreement(String paymentType, boolean selfSupply, ArrayList<DayOfWeek> supplyDays, HashMap <Integer, SupplierProductService> supllyingProducts
            ,Pair<Integer,Double> totalDiscountInPrecentageForOrderAmount ,  Pair<Double,Double> totalOrderDiscountPerOrderPrice){
        this.paymentType = paymentType;
        this.selfSupply = selfSupply;
        this.supplyDays = supplyDays;
        this.supllyingProducts = supllyingProducts;
        this.totalDiscountInPrecentageForOrderAmount = totalDiscountInPrecentageForOrderAmount;
        this.totalOrderDiscountPerOrderPrice = totalOrderDiscountPerOrderPrice;
        //System.out.println("created a new service agreement, paymentType="+paymentType+" supplDays= "+supplyDays+" supllingProducts"+supllyingProducts.get(123));
    }

    public String getPaymentType(){
        return paymentType;
    }
    public boolean getSelfSupply(){
        return selfSupply;
    }
    public ArrayList<DayOfWeek> getSupplyDays(){
        return supplyDays;
    }
    public  HashMap <Integer, SupplierProductService> getSupllyingProducts(){
        return supllyingProducts;
    }

    public Pair<Double, Double> getTotalOrderDiscountPerOrderPrice() {
        return totalOrderDiscountPerOrderPrice;
    }

    public Pair<Integer, Double> getTotalDiscountInPrecentageForOrderAmount() {
        return totalDiscountInPrecentageForOrderAmount;
    }
}
