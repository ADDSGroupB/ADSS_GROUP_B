package BusinessLayer;

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

    public SupplierController() {
        suppliers = new HashMap<>();
    }

    public Response addSupplier(String name, String address, String bankAccount) {
        for (Map.Entry<Integer, Supplier> entry : suppliers.entrySet()) {
            Supplier supplier = entry.getValue();
            //System.out.println("new bank account is " + bankAccount);
            //System.out.println("previous supplier bank account is " + supplier.getBankAccount());
            if (Objects.equals(supplier.getBankAccount(), bankAccount)) {
                System.out.println("equals bankAccounts");
                return new Response("cannot add supplier, bankAccount is already exist");
            }
        }
        Supplier newSupplier = new Supplier(name, address, bankAccount);
        suppliers.put(newSupplier.getSupplierId(), newSupplier);
        return new Response(newSupplier.getSupplierId());
    }


    public Agreement createAgreement(String paymentType, boolean selfSupply, ArrayList<DayOfWeek> supplyDays, HashMap<Integer, SupplierProduct> SupplyingProducts) {
        return new Agreement(paymentType, selfSupply, supplyDays, SupplyingProducts);
    }
    public Agreement createAgreementWithDiscounts(String paymentType, boolean selfSupply, ArrayList<DayOfWeek> supplyDays, HashMap<Integer, SupplierProduct> SupplyingProducts, Pair<Integer,Double> totalDiscountInPrecentageForOrderAmount ,  Pair<Double,Double> totalOrderDiscountPerOrderPrice) {
        return new Agreement(paymentType, selfSupply, supplyDays, SupplyingProducts, totalDiscountInPrecentageForOrderAmount, totalOrderDiscountPerOrderPrice);
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
        return new Response(id);
    }

    public Response changeAddress(int id, String address) {
        if (!suppliers.containsKey(id)) {
            return new Response("Can't change address because supplier with id " + id + " doesn't exist in the system");
        } else {
            suppliers.get(id).setAddress(address);
            return new Response(id);
        }

    }

    public Response changeSupplierBankAccount(int id, String bankAccount) {
        if (!suppliers.containsKey(id)) {
            return new Response("Can't change bankAccount because supplier with id " + id + " doesn't exist in the system");
        } else {
            suppliers.get(id).setBankAccount(bankAccount);
            return new Response(id);
        }
    }

    public Response changeSupplierName(int id, String name) {
        if (!suppliers.containsKey(id)) {
            return new Response("Can't change name because supplier with id " + id + " doesn't exist in the system");
        } else {
            suppliers.get(id).setName(name);
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
        suppliers.get(id).addContact(name, email, phone);
        return new Response(id);
    }

    public Response removeSupplierContact(int id, String email) {
        if (!suppliers.containsKey(id)) {
            return new Response("Can't change name because supplier with id " + id + " doesn't exist in the system");
        }
        for (Contact c : suppliers.get(id).getContacts()) {
            if (c.getEmail().equals(email)) {
                suppliers.get(id).getContacts().remove(c);
                return new Response(id);
            }
        }
        return new Response("Contact with the email: "+ email +" does not exist ");
    }

    public Response editSupplierContacts(int id, String email, String newEmail, String newPhone) {
        if(newEmail.equals(""))
            return editSupplierContactPhone(id, email, newPhone);
        else
            return editSupplierContactEmail(id, email, newEmail);
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

    public Response addItemToAgreement(int supplierID,String name, int productId, int catalogNumber, double price, int amount,  HashMap<Integer, Double> discountPerAmount) {
        if (!suppliers.containsKey(supplierID)) {
            return new Response("Can't change name because supplier with id " + supplierID + " doesn't exist in the system");
        }
        HashMap<Integer, SupplierProduct> supplierProductsMap= suppliers.get(supplierID).getAgreement().getSupllyingProducts();
        for (Map.Entry<Integer, SupplierProduct> entry : supplierProductsMap.entrySet()) {
            SupplierProduct product = entry.getValue();
            if (product.getProductID() == productId)
                return new Response("can not add item with id: " + productId + " beacuse it's already exist in the supplier's products List");
        }
        suppliers.get(supplierID).addProduct(name, productId, catalogNumber, price, amount, discountPerAmount);
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
                return new Response(supplierID);
            }
        }
        return new Response("can not delete item with id: " + itemIdToDelete + " beacuse it is not exist in the supplier's products List");

    }

    public Response editPaymentMethodAndDeliveryMethodAndDeliveryDays(int supplierId, boolean selfSupply, String paymentMethod, ArrayList<DayOfWeek> days) {
        if(!suppliers.containsKey(supplierId)){
            return new Response("Supplier with id: " + supplierId + " is not exist, please try again");
        }
        Supplier supplier = suppliers.get(supplierId);
        supplier.setSelfSupply(selfSupply);
        supplier.SetPaymentType(paymentMethod);
        supplier.setDeliveyDays(days);
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
        ArrayList<Supplier> suppliersInOrder = new ArrayList<>();
        ArrayList<ArrayList<Pair<Integer, Integer>>> supplyLists = new ArrayList<>();
        while(products.entrySet().size() > 0){
            Supplier currentSupplier = null;
            ArrayList<Pair<Integer, Integer>> supplyList = null;
            int currentMax = 0;
            for (Map.Entry<Integer, Supplier> entry : suppliers.entrySet()) {
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
            //
            supplyLists.add(supplyList);
            for (Pair<Integer, Integer> pair : supplyList){
                int id = pair.getFirst();
                int prevAmount = products.get(id);
                currentSupplier.getProductById(id).setAmount(currentSupplier.getProductById(id).getAmount()-prevAmount);
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
                Pair<SupplierProduct,Integer> supplierProductPair= new Pair<>(sp, p.getSecond());//supplierProduct, amount
                supplierProducts.add(supplierProductPair);
            }//(supplierProduct) הוספנו את כל המוצרים של ספק 1
            supplyListSupplierProduct.add(supplierProducts);
            i++;

        }
        return new Response(suppliersInOrder, supplyListSupplierProduct);
        //END WHILE, ORDER CAN BE DONE
    }



}
