package BusinessLayer.SupplierBusinessLayer;

import DataAccessLayer.SupplierDataAccessLayer.AgreementDAO;
import ServiceLayer.SupplierServiceLayer.ServiceAgreement;
import ServiceLayer.SupplierServiceLayer.ServiceContact;
import Utillity.Response;
import java.time.DayOfWeek;

import java.util.ArrayList;
import java.util.HashMap;

public class FacadeSupplier {
    private final SupplierController supplierController;
    private final ProductController productController;
    private final OrderController orderController;
    private final PeriodicOrderController periodicOrderController;
    private final AgreementDAO agreementDAO;
    public FacadeSupplier(){
        supplierController = new SupplierController();
        productController = new ProductController();
        orderController = new OrderController();
        periodicOrderController = new PeriodicOrderController();
        agreementDAO = new AgreementDAO();
    }

    //    public static void main(String[] args) {
//        FacadeSupplier facadeSupplier = new FacadeSupplier();
//        HashMap<Integer, Order> ordersForToday = facadeSupplier.getNoneCollectedOrdersForToday(1);
//        facadeSupplier.markOrderAsCollected(1);
//        ordersForToday = facadeSupplier.getNoneCollectedOrdersForToday(1);
//        SupplierProductDAO supplierProductDAO = new SupplierProductDAO();
//        HashMap<Integer, SupplierProduct> supplierProducts = supplierProductDAO.getAllSupplierProductsByID(3);
//        ArrayList<SupplierProduct> productsToOrder = new ArrayList<>();
//        for (SupplierProduct supplierProduct : supplierProducts.values())
//        {
//            SupplierProduct product = new SupplierProduct(supplierProduct);
//            product.setAmount(30);
//            productsToOrder.add(product);
//        }
//        facadeSupplier.createPeriodicOrder(3, 1, DayOfWeek.SUNDAY, productsToOrder);
//        facadeSupplier.executePeriodicOrder(1);
//    }
    public Response addSupplier(String name, String address, String bankAccount, ServiceAgreement serviceAgreement,  ArrayList<ServiceContact> contactList) {
        Response res = supplierController.addSupplier(name, address, bankAccount);
        if (!res.errorOccurred()) {
            int supplierId = res.getSupplierId();
            HashMap<Integer, SupplierProduct> supllyingProducts = productController.createSupllyingProducts(serviceAgreement.getSupllyingProducts(), supplierId);
            if(serviceAgreement.getTotalDiscountInPrecentageForOrderAmount()!=null && serviceAgreement.getTotalOrderDiscountPerOrderPrice()!=null){
                Agreement agreement1 = supplierController.createAgreementWithDiscounts(serviceAgreement.getPaymentType(), serviceAgreement.getSelfSupply(), serviceAgreement.getSupplyDays(), supllyingProducts, serviceAgreement.getSupplyMethod(), serviceAgreement.getSupplyTime() ,serviceAgreement.getTotalDiscountInPrecentageForOrderAmount(), serviceAgreement.getTotalOrderDiscountPerOrderPrice());
                supplierController.setAgreement(agreement1, supplierId);
                agreementDAO.addAgreementWithDiscount(supplierId, agreement1);
            }
            else {
                Agreement agreement1 = supplierController.createAgreement(serviceAgreement.getPaymentType(), serviceAgreement.getSelfSupply(), serviceAgreement.getSupplyDays(), supllyingProducts, serviceAgreement.getSupplyMethod(), serviceAgreement.getSupplyTime());
                supplierController.setAgreement(agreement1, supplierId);
                agreementDAO.addAgreement(supplierId, agreement1);
            }
            supplierController.setContacts(contactList, supplierId);
//            supplierDAO.addSupplier(supplierController.getSupllierByID(supplierId));
        }
        return res;
    }


    public Response removeSupplier(int id) {
        Response res1 = supplierController.removeSupplier(id);
        if(!res1.errorOccurred()){
            Response res2 = productController.removeSupplierProducts(id);
            if(res2.getErrorMessage()!= null && res2.getErrorMessage().equals("The user doesn't have any products yet")){
                return new Response("The user with id: " + id + " deleted successfully and he doesn't have any products");
            }
            return res1;
        }
        return res1;
    }

    public Response changeAddress(int id, String address) {
        return supplierController.changeAddress(id, address);
    }

    public Response changeSupplierBankAccount(int id, String bankAccount) {
        return supplierController.changeSupplierBankAccount(id, bankAccount);
    }

    public Response changeSupplierName(int id, String name) {
        return supplierController.changeSupplierName(id, name);
    }

    public Response addContactsTOSupplier(int id, String name, String email, String phone) {
        return supplierController.addContactsTOSupplier(id, name, email, phone);
    }

    public Response removeSupplierContact(int id, String phoneNumber) {
        return supplierController.removeSupplierContact(id, phoneNumber);
    }

    public Response editSupplierContacts(int id, String email, String newEmail, String newphone, String oldPhone) {
        return supplierController.editSupplierContacts(id, email, newEmail, newphone, oldPhone);
    }

    public Response addItemToAgreement(int supplierID, String name, int productId, int catalogNumber, double price, int amount,  HashMap<Integer, Double> discountPerAmount, double weight, String manufacturer, int expirationDays) {
        Response res1 = supplierController.addItemToAgreement(supplierID, name,  productId, catalogNumber, price, amount, discountPerAmount, weight, manufacturer, expirationDays);
        if (!res1.errorOccurred()){//want to add product to product controller
            productController.addProductToSupplier(supplierID, name, productId, catalogNumber, price, discountPerAmount);
        }
        return res1;
    }

    public Response removeItemFromAgreement(int supplierID, int itemIdToDelete) {
        Response res1 = supplierController.removeItemFromAgreement(supplierID, itemIdToDelete);
        if (!res1.errorOccurred()){//want to delte product from product controller
            return productController.removeProductToSupplier(supplierID, itemIdToDelete);
        }
        return res1;
    }

    public Response editPaymentMethodAndDeliveryMethodAndDeliveryDays(int supplierId, boolean selfSupply, String paymentMethod, ArrayList<DayOfWeek> days, String supplyMethod, int supplyTime) {
        return supplierController.editPaymentMethodAndDeliveryMethodAndDeliveryDays(supplierId, selfSupply, paymentMethod, days, supplyMethod, supplyTime);
    }

    public Response editItemCatalodNumber(int supplierId, int productId, int newCatalogNumber) {
        Response res = supplierController.editItemCatalodNumber(supplierId, productId, newCatalogNumber);
        if(!res.errorOccurred()){
            productController.editItemCatalodNumber(supplierId, productId, newCatalogNumber);
        }
        return res;
    }

    public void printSuppliers() {
        supplierController.printSuppliers();
    }

    public Response addDiscounts(int supplierId, int productId, int ammount, double discount) {
        return supplierController.addDiscount(supplierId, productId, ammount, discount);
    }

    public Response removeDiscounts(int supplierId, int productId, int ammount, double discount) {
        return supplierController.removeDiscount(supplierId, productId, ammount, discount);
    }

    public Response createOrderByShortage(int branchId ,HashMap<Integer, Integer> shortage) {
        //Response res = supplierController.findSuppliersForOrder(shortage);
        ArrayList<ArrayList<Supplier>> supplierstoOrder = supplierController.findFastestSuppliers(shortage);
//        if(res.errorOccurred()){
//            return res;
//        }
//        orderController.createOrderByShortage(res);
        return orderController.createOrderByShortage(supplierstoOrder, branchId ,shortage);
        //return new Response();

    }

    public Response createPeriodicOrder(int supplierID, int branchID, DayOfWeek fixedDay, HashMap<Integer, Integer> productsAndAmount)
    {
        return periodicOrderController.createPeriodicOrder(supplierID, branchID, fixedDay, productsAndAmount);
    }

    public void printOrders() {
        orderController.PrintOrders();
    }

    public Response updateProductsInOrder(int orderID, HashMap<Integer, Integer> productsToAdd) { return orderController.updateProductsInOrder(orderID, productsToAdd); }
    public Response removeProductsFromOrder(int orderID, ArrayList<Integer> productsToRemove) { return orderController.removeProductsFromOrder(orderID, productsToRemove); }
    public Response updateProductsInPeriodicOrder(int orderID, HashMap<Integer, Integer> productsToAdd) { return periodicOrderController.updateProductsInPeriodicOrder(orderID, productsToAdd); }
    public Response removeProductsFromPeriodicOrder(int orderID, ArrayList<Integer> productsToRemove) { return periodicOrderController.removeProductsFromPeriodicOrder(orderID, productsToRemove); }
    public Response executePeriodicOrder(int periodicOrderID) {
        return orderController.executePeriodicOrder(periodicOrderID);
    }

    public HashMap<Integer, Order> getNoneCollectedOrdersForToday(int branchID) { return orderController.getNoneCollectedOrdersForToday(branchID); }
    public HashMap<Integer, Order> getOrdersFromSupplier(int supplierID) { return orderController.getOrdersFromSupplier(supplierID); }
    public HashMap<Integer, Order> getOrdersToBranch(int branchID) { return orderController.getOrdersToBranch(branchID); }
    public HashMap<Integer, Order> getAllOrderForToday() { return orderController.getAllOrderForToday(); }
    public Response markOrderAsCollected(int orderID) { return orderController.markOrderAsCollected(orderID); }

    public Order getOrderByID(int orderID) { return orderController.getOrderByID(orderID); }

    public HashMap<Integer, PeriodicOrder> getAllPeriodicOrderForToday() { return periodicOrderController.getAllPeriodicOrderForToday(); }

    public void printOrder(int supplierID) { orderController.printOrder(supplierID); }

    public void printSuppliersGui() {
        supplierController.printSuppliersGui();
    }

    public Response checkPhoneNumber(int supplierID, String phoneNumber) { return supplierController.checkPhoneNumber(supplierID, phoneNumber); }
    public Response checkBankAccount(String bankAccount) { return supplierController.checkBankAccount(bankAccount); }

    public Integer getActiveSupplierById(Integer id) {return supplierController.getActiveSupplierById(id);}

    public Integer getLastSupplierID() {return supplierController.getLastSupplierID();}

    public Response getSupplierNameById(Integer id) {return supplierController.getSupplierNameById(id);}

    public HashMap<Integer, SupplierProduct> getAllSupplierProductsByID(int supplierID) { return periodicOrderController.getAllSupplierProductsByID(supplierID); }


    public HashMap<String, String> getContactsFromSupplier(int supplierID) {
        return supplierController.getContactsFromSupplier(supplierID);
    }

    public void orderViaGui() {orderController.orderViaGui();}

    public HashMap<Integer, PeriodicOrder> getPeriodicOrdersToBranch(int branchID){ return periodicOrderController.getPeriodicOrdersToBranch(branchID); }
    public PeriodicOrder getPeriodicOrderByID(int orderID) { return periodicOrderController.getPeriodicOrderByID(orderID); }

    public Response updatePeriodicOrder(int orderID, DayOfWeek fixedDay, HashMap<Integer, Integer> productsAndAmount) { return periodicOrderController.updatePeriodicOrder(orderID, fixedDay, productsAndAmount); }
}