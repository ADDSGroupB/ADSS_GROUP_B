package ServiceLayer;

import BusinessLayer.FacadeSupplier;
import Utillity.Response;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashMap;


public class SupplierService {
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

    public Response editItemCatalodNumber(int supplierId, int productId, int newCatalogNumber) {
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

    public void createOrderByShortage(int branchId ,HashMap<Integer, Integer> shortage) {
        facadeSupplier.createOrderByShortage(branchId ,shortage);
    }

    public void printOrders() {
        facadeSupplier.printOrders();
    }
}