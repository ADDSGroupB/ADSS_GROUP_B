package Utillity;

import BusinessLayer.SupplierBusinessLayer.Supplier;
import BusinessLayer.SupplierBusinessLayer.SupplierProduct;

import java.util.ArrayList;

public class Response {

    private String errorMessage;
    private int supplierId;
    private ArrayList<Supplier> suppliersInOrder;
    private ArrayList<ArrayList<Pair<SupplierProduct,Integer>>> supplyLists;
    private boolean answer;
    private String stringValue;

    public Response(ArrayList<Supplier> suppliersInOrder, ArrayList<ArrayList<Pair<SupplierProduct,Integer>>> supplyLists){
        this.suppliersInOrder = suppliersInOrder;
        this.supplyLists = supplyLists;
    }
    public Response(boolean answer) { this.answer = answer; }

    public Response(int supplierId){
        this.supplierId = supplierId;
    }
    public Response(){}
    public Response(boolean answer, String name){this.answer = answer; this.stringValue = name;}

    public Response(String errorMessage){
        this.errorMessage = errorMessage;
    }
    public boolean errorOccurred(){
        return errorMessage != null;
    }

    public ArrayList<Supplier> getSuppliersInOrder(){return this.suppliersInOrder;}
    public ArrayList<ArrayList<Pair<SupplierProduct,Integer>>> getSupplyLists(){return this.supplyLists;}
    public String getErrorMessage() {
        return errorMessage;
    }
    public int getSupplierId(){
        return supplierId;
    }
    //
    public boolean getAnswer() { return answer; }
    public String getStringValue(){return stringValue;}
}