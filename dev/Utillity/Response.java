package Utillity;

import BusinessLayer.Supplier;
import BusinessLayer.SupplierProduct;

import java.util.ArrayList;

public class Response {

    private String errorMessage;
    private int supplierId;
    private ArrayList<Supplier> suppliersInOrder;
    private ArrayList<ArrayList<Pair<SupplierProduct,Integer>>> supplyLists;

    public Response(ArrayList<Supplier> suppliersInOrder, ArrayList<ArrayList<Pair<SupplierProduct,Integer>>> supplyLists){
        this.suppliersInOrder = suppliersInOrder;
        this.supplyLists = supplyLists;
    }

    public Response(int supplierId){
        this.supplierId = supplierId;
    }
    public Response(){};

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
}