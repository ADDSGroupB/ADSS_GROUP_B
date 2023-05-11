package BusinessLayer;

import DataAccessLayer.SupplierDAO;
import ServiceLayer.ServiceAgreement;
import ServiceLayer.ServiceContact;
import Utillity.Response;
import java.time.DayOfWeek;

import java.util.ArrayList;
import java.util.HashMap;

public class FacadeSupplier {
    private SupplierController supplierController;
    private ProductController productController;
    private OrderController orderController;
    private SupplierDAO supplierDAO;
    public FacadeSupplier(){
        supplierController = new SupplierController();
        productController = new ProductController();
        orderController = new OrderController();
        supplierDAO = new SupplierDAO();
    }
    public Response addSupplier(String name, String address, String bankAccount, ServiceAgreement serviceAgreement,  ArrayList<ServiceContact> contactList) {
        Response res = supplierController.addSupplier(name, address, bankAccount);
        if (!res.errorOccurred()) {
            int supplierId = res.getSupplierId();
            HashMap<Integer, SupplierProduct> supllyingProducts = productController.createSupllyingProducts(serviceAgreement.getSupllyingProducts(), supplierId);
            if(serviceAgreement.getTotalDiscountInPrecentageForOrderAmount()!=null && serviceAgreement.getTotalOrderDiscountPerOrderPrice()!=null){
                Agreement agreement1 = supplierController.createAgreementWithDiscounts(serviceAgreement.getPaymentType(), serviceAgreement.getSelfSupply(), serviceAgreement.getSupplyDays(), supllyingProducts, serviceAgreement.getSupplyMethod(), serviceAgreement.getSupplyTime() ,serviceAgreement.getTotalDiscountInPrecentageForOrderAmount(), serviceAgreement.getTotalOrderDiscountPerOrderPrice());
                supplierController.setAgreement(agreement1, supplierId);
            }
            else {
                Agreement agreement1 = supplierController.createAgreement(serviceAgreement.getPaymentType(), serviceAgreement.getSelfSupply(), serviceAgreement.getSupplyDays(), supllyingProducts, serviceAgreement.getSupplyMethod(), serviceAgreement.getSupplyTime());
                supplierController.setAgreement(agreement1, supplierId);
            }
            supplierController.setContacts(contactList, supplierId);
            supplierDAO.addSupplier(supplierController.getSupllierByID(supplierId));
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

    public Response removeSupplierContact(int id, String email, String phoneNumber) {
        return supplierController.removeSupplierContact(id, email, phoneNumber);
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

    public Response createAnOrder(HashMap<Integer, Integer> shortage) {
        Response res = supplierController.findSuppliersForOrder(shortage);
        if(res.errorOccurred()){
            return res;
        }
        orderController.createOrder(res);
        return new Response();

    }

    public void printOrders() {
        orderController.PrintOrders();
    }


}


