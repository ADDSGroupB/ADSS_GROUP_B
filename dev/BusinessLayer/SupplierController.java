package BusinessLayer;

import DataAccessLayer.*;
import ServiceLayer.ServiceContact;
import Utillity.Pair;
import Utillity.Response;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SupplierController {
    private HashMap<Integer, Supplier> suppliers; //<supplierId : Supplier>
    private ContactDAO contactDAO;
    private DiscountPerAmountDAO discountPerAmountDAO;
    private SupplierProductDAO supplierProductDAO;
    private SupplierDAO supplierDAO;
    private AgreementDAO agreementDAO;
    private DeliveryDaysDAO deliveryDaysDAO;


    public SupplierController() {
        suppliers = new HashMap<>();
        contactDAO = new ContactDAO();
        discountPerAmountDAO = new DiscountPerAmountDAO();
        supplierProductDAO = new SupplierProductDAO();
        supplierDAO = new SupplierDAO();
        agreementDAO = new AgreementDAO();
        deliveryDaysDAO = new DeliveryDaysDAO();
    }

    public Response addSupplier(String name, String address, String bankAccount) {
        for (Map.Entry<Integer, Supplier> entry : suppliers.entrySet()) {
            Supplier supplier = entry.getValue();
            //System.out.println("new bank account is " + bankAccount);
            //System.out.println("previous supplier bank account is " + supplier.getBankAccount());
            if (Objects.equals(supplier.getBankAccount(), bankAccount)) {
                System.out.println("Supplier with the same bank account is already exist in the system");
                return new Response("cannot add supplier, bankAccount is already exist");
            }
        }
        Supplier newSupplier = new Supplier(name, address, bankAccount);
        suppliers.put(newSupplier.getSupplierId(), newSupplier);
//        Response res = supplierDAO.addSupplier(newSupplier);
//        if(res.errorOccurred()) return res;
        return new Response(newSupplier.getSupplierId());
    }


    public Agreement createAgreement(String paymentType, boolean selfSupply, ArrayList<DayOfWeek> supplyDays, HashMap<Integer, SupplierProduct> SupplyingProducts, String supplyMethod, int supplyTime) {
        Agreement agreement = new Agreement(paymentType, selfSupply, supplyMethod, supplyTime, supplyDays, SupplyingProducts);
        return new Agreement(paymentType, selfSupply, supplyMethod, supplyTime, supplyDays, SupplyingProducts);
    }
    public Agreement createAgreementWithDiscounts(String paymentType, boolean selfSupply, ArrayList<DayOfWeek> supplyDays, HashMap<Integer, SupplierProduct> SupplyingProducts, String supplyMethod, int supplyTime, Pair<Integer,Double> totalDiscountInPrecentageForOrderAmount ,  Pair<Double,Double> totalOrderDiscountPerOrderPrice) {
        return new Agreement(paymentType, selfSupply, supplyMethod, supplyTime, supplyDays, SupplyingProducts, totalDiscountInPrecentageForOrderAmount, totalOrderDiscountPerOrderPrice);
    }

    public void setAgreement(Agreement a, int id) {
        suppliers.get(id).setNewAgreement(a);
    }

    public void setContacts(ArrayList<ServiceContact> contactList, int supplierId) {
        ArrayList newContacts = new ArrayList<>();
        for (ServiceContact c : contactList) {
            Contact newContact = new Contact(c.getName(), c.getEmail(), c.getPhoneNumber());
            newContacts.add(newContact);
        }
        suppliers.get(supplierId).setContacts(newContacts);
    }

    public Response removeSupplier(int id) {
        if (!suppliers.containsKey(id)) {
            return new Response("Supplier can't be deleted because there is no supplier with the given id: " + id);
        }
        suppliers.remove(id);
        Response res = supplierDAO.removeSupplier(id);
        if(res.errorOccurred()) return res;
        return new Response(id);
    }

    public Response changeAddress(int id, String address) {
        if (!suppliers.containsKey(id)) {
            return new Response("Can't change address because supplier with id " + id + " doesn't exist in the system");
        } else {
            suppliers.get(id).setAddress(address);
            Response res = supplierDAO.updateSupplierAddress(id, address);
            if(res.errorOccurred()) return res;
            return new Response(id);
        }

    }

    public Response changeSupplierBankAccount(int id, String bankAccount) {
        if (!suppliers.containsKey(id)) {
            return new Response("Can't change bankAccount because supplier with id " + id + " doesn't exist in the system");
        } else {
            suppliers.get(id).setBankAccount(bankAccount);
            Response res = supplierDAO.updateSupplierBankAccount(id, bankAccount);
            if(res.errorOccurred()) return res;
            return new Response(id);
        }
    }

    public Response changeSupplierName(int id, String name) {
        if (!suppliers.containsKey(id)) {
            return new Response("Can't change name because supplier with id " + id + " doesn't exist in the system");
        } else {
            suppliers.get(id).setName(name);
            Response res = supplierDAO.updateSupplierName(id, name);
            if(res.errorOccurred()) return res;
            return new Response(id);
        }

    }

    public Response addContactsTOSupplier(int id, String name, String email, String phone) {
        if (!suppliers.containsKey(id)) {
            return new Response("Can't change name because supplier with id " + id + " doesn't exist in the system");
        }
        for (Contact c : suppliers.get(id).getContacts()) {
            if (c.getEmail().equals(email))
                return new Response("can not add the new contact beacuse he is already in the supplier's contactsList");
        }
        Contact contact = suppliers.get(id).addContact(name, email, phone);
        Response res = contactDAO.addContact(id, contact);
        if(res.errorOccurred()) return res;
        return new Response(id);
    }

    public Response removeSupplierContact(int id, String email, String phoneNumber) {
        if (!suppliers.containsKey(id)) {
            return new Response("Can't change name because supplier with id " + id + " doesn't exist in the system");
        }
        for (Contact c : suppliers.get(id).getContacts()) {
            if (c.getEmail().equals(email)) {
                suppliers.get(id).getContacts().remove(c);
                return new Response(id);
            }
        }
        Response res = contactDAO.removeContact(id, phoneNumber);
        if(res.errorOccurred()) return res;
        return new Response("Contact with the email: "+ email +" does not exist ");
    }

    public Response editSupplierContacts(int id, String email, String newEmail, String newPhone, String oldPhone) {
        if(newEmail.equals("")) {
            Response res = editSupplierContactPhone(id, email, newPhone);
            if(res.errorOccurred()) return res;
            Response res2 = contactDAO.updatePhoneNumber(id, oldPhone, newPhone);
            if(res2.errorOccurred()) return res2;
            else return res;
        }
        else {
            Response res = editSupplierContactEmail(id, email, newEmail);
            if(res.errorOccurred()) return res;
            Response res2 = contactDAO.updateEmail(id, oldPhone, newEmail);
            if(res2.errorOccurred()) return res2;
            else return res;
        }
    }

    public Response editSupplierContactEmail(int id, String email, String newEmail){
        if (!suppliers.containsKey(id)) {
            return new Response("Can't change name because supplier with id " + id + " doesn't exist in the system");
        }
        for (Contact c : suppliers.get(id).getContacts()) {
            if (c.getEmail().equals(email)) {
                c.setEmail(newEmail);
                return new Response(id);
            }
        }
        return new Response("Can't edit email beacuse contact with the email: "+ email +" does not exist ");

    }

    public Response editSupplierContactPhone(int id, String email, String newPhone){
        if (!suppliers.containsKey(id)) {
            return new Response("Can't change name because supplier with id " + id + " doesn't exist in the system");
        }
        for (Contact c : suppliers.get(id).getContacts()) {
            if (c.getEmail().equals(email)) {
                c.setPhoneNumber(newPhone);
                return new Response(id);
            }
        }
        return new Response("Can't edit phoneNumber beacuse contact with the email: "+ email +" does not exist ");
    }

    public Response addItemToAgreement(int supplierID,String name, int productId, int catalogNumber, double price, int amount,  HashMap<Integer, Double> discountPerAmount, double weight, String manufacturer, int expirationDays) {
        if (!suppliers.containsKey(supplierID)) {
            return new Response("Can't change name because supplier with id " + supplierID + " doesn't exist in the system");
        }
        HashMap<Integer, SupplierProduct> supplierProductsMap= suppliers.get(supplierID).getAgreement().getSupllyingProducts();
        for (Map.Entry<Integer, SupplierProduct> entry : supplierProductsMap.entrySet()) {
            SupplierProduct product = entry.getValue();
            if (product.getProductID() == productId)
                return new Response("can not add item with id: " + productId + " beacuse it's already exist in the supplier's products List");
        }
        SupplierProduct supplierProduct = suppliers.get(supplierID).addProduct(name, supplierID, productId, catalogNumber, price, amount, discountPerAmount, weight, manufacturer, expirationDays);
        Response response = supplierProductDAO.addSupplierProduct(supplierID, supplierProduct);
        if (response.errorOccurred()) return response;
        for(Map.Entry<Integer, Double> discount : discountPerAmount.entrySet())
        {
            response = discountPerAmountDAO.addDiscount(supplierID, productId, discount.getKey(), discount.getValue());
            if (response.errorOccurred()) return response;
        }
        return new Response(supplierID);
    }


    public Response removeItemFromAgreement(int supplierID, int itemIdToDelete) {
        if (!suppliers.containsKey(supplierID)) {
            return new Response("Can't change name because supplier with id " + supplierID + " doesn't exist in the system");
        }
        HashMap<Integer, SupplierProduct> supplierProductsMap= suppliers.get(supplierID).getAgreement().getSupllyingProducts();
        for (Map.Entry<Integer, SupplierProduct> entry : supplierProductsMap.entrySet()) {
            SupplierProduct product = entry.getValue();
            if (product.getProductID() == itemIdToDelete) {
                suppliers.get(supplierID).removeProduct(itemIdToDelete);
                Response response = supplierProductDAO.removeSupplierProduct(supplierID, itemIdToDelete);
                if (response.errorOccurred()) return response;
                return new Response(supplierID);
            }
        }
        return new Response("can not delete item with id: " + itemIdToDelete + " beacuse it is not exist in the supplier's products List");

    }

    public Response editPaymentMethodAndDeliveryMethodAndDeliveryDays(int supplierId, boolean selfSupply, String paymentMethod, ArrayList<DayOfWeek> days, String supplyMethod, int supplyTime) {
        if(!suppliers.containsKey(supplierId)){
            return new Response("Supplier with id: " + supplierId + " is not exist, please try again");
        }
        Supplier supplier = suppliers.get(supplierId);
        supplier.setSelfSupply(selfSupply);
        supplier.SetPaymentType(paymentMethod);
        supplier.setDeliveyDays(days);
        supplier.setSupplyMethod(supplyMethod);
        supplier.setSupplyTime(supplyTime);
        Response response = agreementDAO.updateAgreement(supplierId, paymentMethod, selfSupply, supplyMethod, supplyTime);
        if (response.errorOccurred()) return response;
        if(days != null) response = deliveryDaysDAO.updateDeliveryDays(supplierId, days);
        else response = deliveryDaysDAO.removeDeliveryDays(supplierId);
        if(response.errorOccurred()) return response;
        return new Response(supplierId);
    }

    public Response editItemCatalodNumber(int supplierId, int productId, int newCatalogNumber) {
        if(!suppliers.containsKey(supplierId)){
            return new Response("Supplier with id: " + supplierId + " is not exist, please try again");
        }
        HashMap<Integer, SupplierProduct> supplierProductsMap= suppliers.get(supplierId).getAgreement().getSupllyingProducts();
        for (Map.Entry<Integer, SupplierProduct> entry : supplierProductsMap.entrySet()) {
            SupplierProduct product = entry.getValue();
            if (product.getProductID() == productId) {
                suppliers.get(supplierId).setProdactCatalogNumber(productId, newCatalogNumber);
                Response response = supplierProductDAO.updateSupplierProductCatalogNumber(supplierId, productId, newCatalogNumber);
                if (response.errorOccurred()) return response;
                return new Response(supplierId);
            }
        }
        return new Response("can not edit item with id: " + productId + " because it is not exist in the supplier's products List");

    }

    public void printSuppliers() {
        if(!suppliers.isEmpty()){
            for (Map.Entry<Integer, Supplier> entry : suppliers.entrySet()) {
                Integer supplierId = entry.getKey();
                Supplier supplier = entry.getValue();

                System.out.println("Supplier's name: " + supplier.getName() + ", supplier's id: " + supplierId + ", supplier's products:" );
                printSupplyingProducts(supplier.getSupplyingProducts());
                System.out.println("\n");
                }
            }
        else{
            System.out.println("Please add a supplier before choosing this option");
        }
        }

        public void printSupplyingProducts(Map<Integer, SupplierProduct> sp) {
            for (Map.Entry<Integer, SupplierProduct> entry : sp.entrySet()) {
                Integer productId = entry.getKey();
                SupplierProduct supplierProduct = entry.getValue();
                Integer catalogId = supplierProduct.getCatalogId();
                int amount = supplierProduct.getAmount();
                System.out.println("productId: " + productId + ", catalogId: " + catalogId + ", amount: " + amount);
            }
        }

    public Response addDiscount(int supplierId, int productId, int ammount, double discount) {
        if(!suppliers.containsKey(supplierId)){
            return new Response("Supplier with id: " + supplierId + " is not exist, please try again");
        }
        HashMap<Integer, SupplierProduct> supplierProductsMap= suppliers.get(supplierId).getAgreement().getSupllyingProducts();
        for (Map.Entry<Integer, SupplierProduct> entry : supplierProductsMap.entrySet()) {
            SupplierProduct product = entry.getValue();
            if (product.getProductID() == productId) {
                product.addDiscount(ammount, discount);
                Response res = discountPerAmountDAO.addDiscount(supplierId, productId, ammount, discount);
                if(res.errorOccurred()) return res;
                return new Response(supplierId);
            }
        }
        return new Response("can not add discount to item with id : " + productId + " because item is not exist in the supplier's products List");
    }

    public Response removeDiscount(int supplierId, int productId, int ammount, double discount) {
        if(!suppliers.containsKey(supplierId)){
            return new Response("Supplier with id: " + supplierId + " is not exist, please try again");
        }
        HashMap<Integer, SupplierProduct> supplierProductsMap= suppliers.get(supplierId).getAgreement().getSupllyingProducts();
        for (Map.Entry<Integer, SupplierProduct> entry : supplierProductsMap.entrySet()) {
            SupplierProduct product = entry.getValue();
            if (product.getProductID() == productId) {
                product.removeDiscount(ammount);
                Response res = discountPerAmountDAO.removeDiscount(supplierId, productId, ammount);
                if(res.errorOccurred()) return res;
                return new Response(supplierId);
            }
        }
        return new Response("can not delete discount to item with id : " + productId + " because item is not exist in the supplier's products List");
    }


    public int getTotalAmount (ArrayList<Pair<Integer,Integer>> products) {
        int totalAmount=0;
        for (Pair<Integer,Integer> p:products){
            totalAmount+=p.getSecond();
        }
        return totalAmount;
    }

    public Response findSuppliersForOrder(HashMap<Integer, Integer> products){
        HashMap<Integer, Supplier> copyOfSuppliers = new HashMap<>(suppliers);
        ArrayList<Supplier> suppliersInOrder = new ArrayList<>();
        ArrayList<ArrayList<Pair<Integer, Integer>>> supplyLists = new ArrayList<>();
        while(products.entrySet().size() > 0){
            Supplier currentSupplier = null;
            ArrayList<Pair<Integer, Integer>> supplyList = null;
            int currentMax = 0;
            for (Map.Entry<Integer, Supplier> entry : copyOfSuppliers.entrySet()) {
                Supplier supplier = entry.getValue();
                ArrayList<Pair<Integer, Integer>> canSupply = supplier.getItemsToCreateOrder(products);
                if (currentSupplier == null){
                    currentSupplier = supplier;
                    supplyList = canSupply;
                }
                int amount = getTotalAmount(canSupply);
                if (amount > currentMax) {
                    currentSupplier = supplier;
                    supplyList = canSupply;
                    currentMax = amount;
                }
                //אם הספקים מספקים את אותה הכמות לפי מוצר ספציפי, ניקח את הזול מביניהם
                if (amount == currentMax){
                    double currentBest = currentSupplier.calculatePriceAfterDiscount(supplyList);
                    double maybeNewBest = supplier.calculatePriceAfterDiscount(canSupply);
                    if (currentBest > maybeNewBest){
                        currentSupplier = supplier;
                        supplyList = canSupply;
                        currentMax = amount;
                    }
                }
            }
            if (currentMax == 0 && products.entrySet().size() > 0){
                return new Response("order can't be created"); // can't complete order
            }
            suppliersInOrder.add(currentSupplier);
            copyOfSuppliers.remove(currentSupplier.getSupplierId());
            //
            supplyLists.add(supplyList);
            for (Pair<Integer, Integer> pair : supplyList){
                int id = pair.getFirst();
                int prevAmount = products.get(id);
                //currentSupplier.getProductById(id).setAmount(currentSupplier.getProductById(id).getAmount()-prevAmount);
                products.put(id, prevAmount-pair.getSecond());
                if (products.get(id) == 0){
                    products.remove(id);
                }
            }
        }
        ArrayList<ArrayList<Pair<SupplierProduct,Integer>>> supplyListSupplierProduct=new ArrayList<>();
        int i=0;
        for(Supplier sup: suppliersInOrder){
            ArrayList<Pair<Integer,Integer>> prod = supplyLists.get(i);
            ArrayList<Pair<SupplierProduct,Integer>> supplierProducts = new ArrayList<>();
            for (Pair<Integer,Integer> p: prod){
                int productID= p.getFirst();
                SupplierProduct sp = sup.getProductById(productID);
                sp.setAmount(sp.getAmount() - p.getSecond());
                Pair<SupplierProduct,Integer> supplierProductPair= new Pair<>(sp, p.getSecond());//supplierProduct, amount
                supplierProducts.add(supplierProductPair);
            }//(supplierProduct) הוספנו את כל המוצרים של ספק 1
            supplyListSupplierProduct.add(supplierProducts);
            i++;

        }
        return new Response(suppliersInOrder, supplyListSupplierProduct);
        //END WHILE, ORDER CAN BE DONE
    }

    public Supplier getSupllierByID(int id)
    {
        return suppliers.get(id);
    }



}
