package ServiceLayer.SupplierServiceLayer;

import BusinessLayer.SupplierBusinessLayer.FacadeSupplier;
import BusinessLayer.SupplierBusinessLayer.Order;
import BusinessLayer.SupplierBusinessLayer.PeriodicOrder;
import BusinessLayer.SupplierBusinessLayer.SupplierProduct;
import Utillity.Response;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashMap;


public class SupplierService implements iOrderService{
    private final FacadeSupplier facadeSupplier;

    public SupplierService() {
        facadeSupplier = new FacadeSupplier();
    }

    public Response addSupplier(String name, String address, String bankAccount, ServiceAgreement serviceAgreement, ArrayList<ServiceContact> contactList) {
        return facadeSupplier.addSupplier(name, address, bankAccount, serviceAgreement, contactList);
    }

    public Response removeSupplier(int id) {
        return facadeSupplier.removeSupplier( id);
    }

    public Response changeAddress(int id, String address) {
        return facadeSupplier.changeAddress(id, address);
    }

    public Response changeSupplierBankAccount(int id, String bankAccount) {
        return facadeSupplier.changeSupplierBankAccount(id, bankAccount);
    }

    public Response changeSupplierName(int id, String name) {
        return facadeSupplier.changeSupplierName(id, name);
    }

    public Response addContactsTOSupplier(int id, String name, String email, String phone){
        return facadeSupplier.addContactsTOSupplier(id, name, email, phone);
    }

    public Response removeSupplierContact(int id, String phoneNumber) {
        return facadeSupplier.removeSupplierContact(id, phoneNumber);
    }

    public Response editSupplierContacts(int id, String email, String newEmail, String newphone, String oldPhone) {
        return facadeSupplier.editSupplierContacts(id, email, newEmail, newphone, oldPhone);
    }

    public Response addItemToAgreement(int supplierID, String name, int productId, int catalogNumber, double price, int amount, HashMap<Integer, Double> discountPerAmount, double weight, String manufacturer, int expirationDays) {
        return facadeSupplier.addItemToAgreement(supplierID, name, productId, catalogNumber, price, amount, discountPerAmount, weight, manufacturer, expirationDays);
    }

    public Response removeItemFromAgreement(int supplierID, int itemIdToDelete) {
        return facadeSupplier.removeItemFromAgreement(supplierID, itemIdToDelete);
    }

    public Response editPaymentMethodAndDeliveryMethodAndDeliveryDays(int supplierId, boolean selfSupply, String paymentMethod, ArrayList<DayOfWeek> days, String supplyMethod, int supplyTime) {
        return facadeSupplier.editPaymentMethodAndDeliveryMethodAndDeliveryDays(supplierId, selfSupply, paymentMethod, days, supplyMethod, supplyTime);
    }

    public Response editItemCatalogdNumber(int supplierId, int productId, int newCatalogNumber) {
        return facadeSupplier.editItemCatalodNumber(supplierId, productId, newCatalogNumber);
    }

    public void printSuppliers() {
        facadeSupplier.printSuppliers();
    }

    public Response addDiscounts(int supplierId, int productId, int ammount, double discount) {
        return facadeSupplier.addDiscounts(supplierId, productId, ammount, discount);
    }

    public Response removeDiscounts(int supplierId, int productId, int ammount, double discount) {
        return facadeSupplier.removeDiscounts(supplierId, productId, ammount, discount);
    }
    @Override
    public Response createOrderByShortage(int branchId ,HashMap<Integer, Integer> shortage) { return facadeSupplier.createOrderByShortage(branchId ,shortage); }
    @Override
    public HashMap<Integer, Order> getNoneCollectedOrdersForToday(int branchID) { return facadeSupplier.getNoneCollectedOrdersForToday(branchID); }
    @Override
    public HashMap<Integer, Order> getOrdersFromSupplier(int supplierID) { return facadeSupplier.getOrdersFromSupplier(supplierID); }
    @Override
    public HashMap<Integer, Order> getOrdersToBranch(int branchID) { return facadeSupplier.getOrdersToBranch(branchID); }
    @Override
    public HashMap<Integer, Order> getAllOrderForToday() { return facadeSupplier.getAllOrderForToday(); }
    @Override
    public Response markOrderAsCollected(int orderID) { return facadeSupplier.markOrderAsCollected(orderID); }
    @Override
    public Response executePeriodicOrder(int periodicOrderID) { return facadeSupplier.executePeriodicOrder(periodicOrderID); }
    @Override
    public Response createPeriodicOrder(int supplierID, int branchID, DayOfWeek fixedDay, HashMap<Integer, Integer> productsAndAmount) { return facadeSupplier.createPeriodicOrder(supplierID, branchID, fixedDay, productsAndAmount); }
    @Override
    public Order getOrderByID(int orderID) { return facadeSupplier.getOrderByID(orderID); }
    @Override
    public HashMap<Integer, PeriodicOrder> getAllPeriodicOrderForToday() { return facadeSupplier.getAllPeriodicOrderForToday(); }

    @Override
    public Response updateProductsInOrder(int orderID, HashMap<Integer, Integer> productsToAdd) { return facadeSupplier.updateProductsInOrder(orderID, productsToAdd); }

    @Override
    public Response removeProductsFromOrder(int orderID, ArrayList<Integer> productsToRemove) { return facadeSupplier.removeProductsFromOrder(orderID, productsToRemove); }
    @Override
    public Response updateProductsInPeriodicOrder(int orderID, HashMap<Integer, Integer> productsToAdd) { return facadeSupplier.updateProductsInPeriodicOrder(orderID, productsToAdd); }
    @Override
    public Response removeProductsFromPeriodicOrder(int orderID, ArrayList<Integer> productsToRemove) { return facadeSupplier.removeProductsFromPeriodicOrder(orderID, productsToRemove); }
    public void printOrder(int supplierID) { facadeSupplier.printOrder(supplierID); }
    public void printOrders() {
        facadeSupplier.printOrders();
    }
    public void printSuppliersGui() {
        facadeSupplier.printSuppliersGui();
    }
    public Response checkPhoneNumber(int supplierID, String phoneNumber) { return facadeSupplier.checkPhoneNumber(supplierID, phoneNumber); }

    public Response checkBankAccount(String bankAccount) { return facadeSupplier.checkBankAccount(bankAccount); }

    public Integer getActiveSupplierById(Integer id){return facadeSupplier.getActiveSupplierById(id);}

    @Override
    public Integer getLastSupplierID() {return facadeSupplier.getLastSupplierID();}
    @Override
    public Response getSupplierNameById(Integer id){return facadeSupplier.getSupplierNameById(id);}
    @Override
    public HashMap<Integer, SupplierProduct> getAllSupplierProductsByID(int supplierID) { return facadeSupplier.getAllSupplierProductsByID(supplierID); }




    public HashMap<String, String> getContactsFromSupplier(int supplierID) {
        return facadeSupplier.getContactsFromSupplier(supplierID);
    }
    public void orderViaGui(){facadeSupplier.orderViaGui();}
    @Override
    public HashMap<Integer, PeriodicOrder> getPeriodicOrdersToBranch(int branchID){ return facadeSupplier.getPeriodicOrdersToBranch(branchID); }
    @Override
    public PeriodicOrder getPeriodicOrderByID(int orderID) { return facadeSupplier.getPeriodicOrderByID(orderID); }
    @Override
    public Response updatePeriodicOrder(int orderID, DayOfWeek fixedDay, HashMap<Integer, Integer> productsAndAmount) { return facadeSupplier.updatePeriodicOrder(orderID, fixedDay, productsAndAmount); }
}