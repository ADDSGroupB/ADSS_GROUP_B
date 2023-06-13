package BusinessLayer.SupplierBusinessLayer;

import DataAccessLayer.SupplierDataAccessLayer.*;
import ServiceLayer.SupplierServiceLayer.ServiceContact;
import Utillity.Pair;
import Utillity.Response;

import javax.swing.*;
import java.awt.*;
import java.time.DayOfWeek;
import java.util.*;

public class SupplierController {
    //    private HashMap<Integer, Supplier> suppliers; //<supplierId : Supplier>
    private final ContactDAO contactDAO;
    private final DiscountPerAmountDAO discountPerAmountDAO;
    private final SupplierProductDAO supplierProductDAO;
    private final SupplierDAO supplierDAO;
    private final AgreementDAO agreementDAO;
    private final DeliveryDaysDAO deliveryDaysDAO;
    private static int id;


    public SupplierController() {
//        suppliers = new HashMap<>();
        contactDAO = new ContactDAO();
        discountPerAmountDAO = new DiscountPerAmountDAO();
        supplierProductDAO = new SupplierProductDAO();
        supplierDAO = new SupplierDAO();
        agreementDAO = new AgreementDAO();
        deliveryDaysDAO = new DeliveryDaysDAO();
        id = supplierDAO.getLastSupplierID() + 1;
    }

    public Response addSupplier(String name, String address, String bankAccount) {
//        for (Map.Entry<Integer, Supplier> entry : suppliers.entrySet()) {
//            Supplier supplier = entry.getValue();
//            //System.out.println("new bank account is " + bankAccount);
//            //System.out.println("previous supplier bank account is " + supplier.getBankAccount());
//            if (Objects.equals(supplier.getBankAccount(), bankAccount)) {
//                System.out.println("Supplier with the same bank account is already exist in the system");
//                return new Response("cannot add supplier, bankAccount is already exist");
//            }
//        }
        Response response = supplierDAO.searchBankAccount(bankAccount);
        if(response.errorOccurred()) return response;
        if(response.getAnswer())
        {
            System.out.println("Supplier with the same bank account is already exist in the system");
            return new Response("cannot add supplier, bankAccount is already exist");
        }
        Supplier newSupplier = new Supplier(id++, name, address, bankAccount);
        supplierDAO.addSupplier(newSupplier);
//        suppliers.put(newSupplier.getSupplierId(), newSupplier);
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
//        suppliers.get(id).setNewAgreement(a);
        supplierDAO.getSupplierByID(id).setNewAgreement(a);
    }

    public void setContacts(ArrayList<ServiceContact> contactList, int supplierId) {
        ArrayList<Contact> newContacts = new ArrayList<>();
        for (ServiceContact c : contactList) {
            Contact newContact = new Contact(c.getName(), c.getEmail(), c.getPhoneNumber());
            newContacts.add(newContact);
            contactDAO.addContact(supplierId, newContact);
        }
        supplierDAO.getSupplierByID(supplierId).setContacts(newContacts);
    }

    public Response removeSupplier(int id) {
        if (supplierDAO.getSupplierByID(id) == null) {
            return new Response("Supplier can't be deleted because there is no supplier with the given id: " + id);
        }
//        suppliers.remove(id);
        Response res = supplierDAO.removeSupplier(id);
        if(res.errorOccurred()) return res;
        return new Response(id);
    }

    public Response changeAddress(int id, String address) {
        if (supplierDAO.getSupplierByID(id) == null) {
            return new Response("Can't change address because supplier with id " + id + " doesn't exist in the system");
        } else {
            supplierDAO.getSupplierByID(id).setAddress(address);
            Response res = supplierDAO.updateSupplierAddress(id, address);
            if(res.errorOccurred()) return res;
            return new Response(id);
        }

    }

    public Response changeSupplierBankAccount(int id, String bankAccount) {
        if (supplierDAO.getSupplierByID(id) == null) {
            return new Response("Can't change bankAccount because supplier with id " + id + " doesn't exist in the system");
        } else {
            supplierDAO.getSupplierByID(id).setBankAccount(bankAccount);
            Response res = supplierDAO.updateSupplierBankAccount(id, bankAccount);
            if(res.errorOccurred()) return res;
            return new Response(id);
        }
    }

    public Response changeSupplierName(int id, String name) {
        if (supplierDAO.getSupplierByID(id) == null) {
            return new Response("Can't change name because supplier with id " + id + " doesn't exist in the system");
        } else {
            supplierDAO.getSupplierByID(id).setName(name);
            Response res = supplierDAO.updateSupplierName(id, name);
            if(res.errorOccurred()) return res;
            return new Response(id);
        }

    }

    public Response addContactsTOSupplier(int id, String name, String email, String phone) {
        if (supplierDAO.getSupplierByID(id) == null)
            return new Response("Can't add contact because supplier with id " + id + " doesn't exist in the system");
        if(contactDAO.getContactBySupplierID(id, phone) != null)
            return new Response("can not add the new contact beacuse he is already in the supplier's contactsList");
        Contact contact = supplierDAO.getSupplierByID(id).addContact(name, email, phone);
        Response res = contactDAO.addContact(id, contact);
        if(res.errorOccurred()) return res;
        return new Response(id);
    }

    public Response removeSupplierContact(int id, String phoneNumber) {
        if (supplierDAO.getSupplierByID(id) == null)
            return new Response("Can't change name because supplier with id " + id + " doesn't exist in the system");
        if(contactDAO.getContactBySupplierID(id, phoneNumber) == null)
            return new Response("Contact with the phone number: "+ phoneNumber +" does not exist ");
        for (Contact c : supplierDAO.getSupplierByID(id).getContacts()) {
            if (c.getPhoneNumber().equals(phoneNumber)) {
                supplierDAO.getSupplierByID(id).getContacts().remove(c);
                contactDAO.removeContact(id, phoneNumber);
                return new Response(id);
            }
        }
        Response res = contactDAO.removeContact(id, phoneNumber);
        if(res.errorOccurred()) return res;
        return new Response("Contact with the phone number: "+ phoneNumber +" does not exist ");
    }

    public Response editSupplierContacts(int id, String email, String newEmail, String newPhone, String oldPhone) {
        if (supplierDAO.getSupplierByID(id) == null) return new Response("Can't change name because supplier with id " + id + " doesn't exist in the system");
        if(contactDAO.getContactBySupplierID(id, oldPhone) == null) return new Response("Can't edit phoneNumber beacuse contact with the phoneNumber: " + oldPhone +" does not exist ");
        if(newEmail.equals("")) {
            Response res = editSupplierContactPhone(id, oldPhone, newPhone);
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
        for (Contact c : supplierDAO.getSupplierByID(id).getContacts()) {
            if (c.getEmail().equals(email)) {
                c.setEmail(newEmail);
                return new Response(id);
            }
        }
        return new Response("Can't edit email beacuse contact with the email: "+ email +" does not exist ");
    }

    public Response editSupplierContactPhone(int id, String oldPhone, String newPhone){
        for (Contact c : supplierDAO.getSupplierByID(id).getContacts()) {
            if (c.getPhoneNumber().equals(oldPhone)) {
                c.setPhoneNumber(newPhone);
                return new Response(id);
            }
        }
        return new Response("Can't edit phone number beacuse contact with the  phone number: " + oldPhone + " does not exist ");
    }

    public Response addItemToAgreement(int supplierID,String name, int productId, int catalogNumber, double price, int amount,  HashMap<Integer, Double> discountPerAmount, double weight, String manufacturer, int expirationDays) {
        if (supplierDAO.getSupplierByID(supplierID) == null) {
            return new Response("Can't change name because supplier with id " + supplierID + " doesn't exist in the system");
        }
        HashMap<Integer, SupplierProduct> supplierProductsMap= supplierDAO.getSupplierByID(supplierID).getAgreement().getSupllyingProducts();
        for (Map.Entry<Integer, SupplierProduct> entry : supplierProductsMap.entrySet()) {
            SupplierProduct product = entry.getValue();
            if (product.getProductID() == productId)
                return new Response("can not add item with id: " + productId + " beacuse it's already exist in the supplier's products List");
        }
        SupplierProduct supplierProduct = supplierDAO.getSupplierByID(supplierID).addProduct(name, supplierID, productId, catalogNumber, price, amount, discountPerAmount, weight, manufacturer, expirationDays);
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
        if (supplierDAO.getSupplierByID(supplierID) == null) {
            return new Response("Can't change name because supplier with id " + supplierID + " doesn't exist in the system");
        }
        HashMap<Integer, SupplierProduct> supplierProductsMap= supplierDAO.getSupplierByID(supplierID).getAgreement().getSupllyingProducts();
        for (Map.Entry<Integer, SupplierProduct> entry : supplierProductsMap.entrySet()) {
            SupplierProduct product = entry.getValue();
            if (product.getProductID() == itemIdToDelete) {
                supplierDAO.getSupplierByID(supplierID).removeProduct(itemIdToDelete);
                Response response = supplierProductDAO.removeSupplierProduct(supplierID, itemIdToDelete);
                if (response.errorOccurred()) return response;
                return new Response(supplierID);
            }
        }
        return new Response("can not delete item with id: " + itemIdToDelete + " beacuse it is not exist in the supplier's products List");

    }

    public Response editPaymentMethodAndDeliveryMethodAndDeliveryDays(int supplierId, boolean selfSupply, String paymentMethod, ArrayList<DayOfWeek> days, String supplyMethod, int supplyTime) {
        if(supplierDAO.getSupplierByID(supplierId) == null){
            return new Response("Supplier with id: " + supplierId + " is not exist, please try again");
        }
        Supplier supplier = supplierDAO.getSupplierByID(supplierId);
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
        if(supplierDAO.getSupplierByID(supplierId) == null){
            return new Response("Supplier with id: " + supplierId + " is not exist, please try again");
        }
        HashMap<Integer, SupplierProduct> supplierProductsMap= supplierDAO.getSupplierByID(supplierId).getAgreement().getSupllyingProducts();
        for (Map.Entry<Integer, SupplierProduct> entry : supplierProductsMap.entrySet()) {
            SupplierProduct product = entry.getValue();
            if (product.getProductID() == productId) {
                supplierDAO.getSupplierByID(supplierId).setProdactCatalogNumber(productId, newCatalogNumber);
                Response response = supplierProductDAO.updateSupplierProductCatalogNumber(supplierId, productId, newCatalogNumber);
                if (response.errorOccurred()) return response;
                return new Response(supplierId);
            }
        }
        return new Response("can not edit item with id: " + productId + " because it is not exist in the supplier's products List");

    }

    public void printSuppliers()
    {
        if(id <= 1) System.out.println("Please add a supplier before choosing this option");
        else supplierDAO.printAllSuppliers();
    }

    public Response addDiscount(int supplierId, int productId, int ammount, double discount) {
        if(supplierDAO.getSupplierByID(supplierId) == null){
            return new Response("Supplier with id: " + supplierId + " is not exist, please try again");
        }
        HashMap<Integer, SupplierProduct> supplierProductsMap= supplierDAO.getSupplierByID(supplierId).getAgreement().getSupllyingProducts();
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
        if(supplierDAO.getSupplierByID(supplierId) == null){
            return new Response("Supplier with id: " + supplierId + " is not exist, please try again");
        }
        HashMap<Integer, SupplierProduct> supplierProductsMap= supplierDAO.getSupplierByID(supplierId).getAgreement().getSupllyingProducts();
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
        HashMap<Integer, Supplier> copyOfSuppliers = new HashMap<>(supplierDAO.getAllSuppliers());
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
                //                                              ,
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
            }//(supplierProduct)                             1
            supplyListSupplierProduct.add(supplierProducts);
            i++;

        }
        return new Response(suppliersInOrder, supplyListSupplierProduct);
        //END WHILE, ORDER CAN BE DONE
    }

    public Supplier getSupllierByID(int id)
    {
        return supplierDAO.getSupplierByID(id);
    }

    public ArrayList<ArrayList<Supplier>> findFastestSuppliers(HashMap<Integer, Integer> products) {
        HashMap<Integer, Supplier> copyOfSuppliers = new HashMap<>(supplierDAO.getAllSuppliers());
        // ArrayList<Supplier> fastestSuppliers = new ArrayList<>();
        ArrayList<ArrayList<Supplier>> sortedSuppliers = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : products.entrySet()) {
            int productId = entry.getKey();
            int amountLeftToOrder = entry.getValue();
            ArrayList<Supplier> supplierCanSupply = new ArrayList<>();
            for (Map.Entry<Integer, Supplier> e : copyOfSuppliers.entrySet()) {
                Supplier currSup = e.getValue();
                if (currSup.getSupplyingProducts().containsKey(entry.getKey()))
                    supplierCanSupply.add(currSup);
            }
            SuppliersComparator c = new SuppliersComparator();
            supplierCanSupply.sort(c);

            for (int i = 0; i < supplierCanSupply.size() ; i++) {
                Supplier currSup = supplierCanSupply.get(i);
                //               amountLeftToOrder = Math.max(amountLeftToOrder - currSup.getAmountByProduct(productId), 0);
                for (int j = 0; j<i; j++ ){
                    Supplier prevSup = supplierCanSupply.get(j);
                    if (currSup.getSupplierClosestDaysToDelivery() == prevSup.getSupplierClosestDaysToDelivery()) {
                        if (currSup.getAmountByProduct(productId) > prevSup.getAmountByProduct(productId)) {
                            Collections.swap(supplierCanSupply, i, j);
                        }
                        if (currSup.getAmountByProduct(productId) == prevSup.getAmountByProduct(productId)) {
                            int amountCanSupply = Math.min(amountLeftToOrder, currSup.getAmountByProduct(productId));
//                            System.out.println("im hereee1111111eeee");
//                            System.out.println("cur");
//                            System.out.println(currSup.calculatePricePerProduct(productId, amountCanSupply)+"\n");
//                            System.out.println("prev");
//                            System.out.println(prevSup.calculatePricePerProduct(productId, amountCanSupply)+"\n");
                            if (currSup.calculatePricePerProduct(productId, amountCanSupply) < prevSup.calculatePricePerProduct(productId, amountCanSupply)){
//                                System.out.println("im hereeee222222eeeeeeeee");
                                Collections.swap(supplierCanSupply, i, j);
                            }
                            amountLeftToOrder = Math.max(amountLeftToOrder - currSup.getAmountByProduct(productId), 0);
                        }
                    }

                }
            }
            sortedSuppliers.add(supplierCanSupply);

        }
        return sortedSuppliers;
    }


    void printSuppliersGui() {
        JFrame suppliersFrame = new JFrame("Suppliers");
        suppliersFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        suppliersFrame.setSize(400, 300);
        suppliersFrame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JTextArea suppliersTextArea = new JTextArea();
        suppliersTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(suppliersTextArea);

        // Retrieve the list of suppliers from the supplierService
        HashMap<Integer, Supplier> suppliers = supplierDAO.getAllSuppliers();

        // Populate the suppliersTextArea with supplier information
        StringBuilder sb = new StringBuilder();
        for(Map.Entry<Integer, Supplier> supplier : suppliers.entrySet()) {
            sb.append("Supplier ID: ").append(supplier.getValue().getSupplierId()).append("\n");
            sb.append("Name: ").append(supplier.getValue().getName()).append("\n");
            sb.append("Address: ").append(supplier.getValue().getAddress()).append("\n");
            sb.append("Bank Account: ").append(supplier.getValue().getBankAccount()).append("\n");
            sb.append("\n");
        }
        suppliersTextArea.setText(sb.toString());

        panel.add(scrollPane, BorderLayout.CENTER);

        suppliersFrame.getContentPane().add(panel);
        suppliersFrame.setVisible(true);
    }

    public Response checkBankAccount(String bankAccount)
    {
        Response response = supplierDAO.searchBankAccount(bankAccount);
        if(response.errorOccurred()) return response;
        if(response.getAnswer())
            return new Response("cannot add supplier, bankAccount is already exist");
        return new Response(1);
    }

    public Response checkPhoneNumber(int supplierID, String phoneNumber)
    {
        if(contactDAO.getContactBySupplierID(supplierID, phoneNumber) != null) return new Response("This supplier already have contact with the same phone number");
        return new Response(supplierID);
    }

    public Integer getActiveSupplierById(Integer id) {return supplierDAO.getActiveSupplierById(id);}

    public Integer getLastSupplierID() {return supplierDAO.getLastSupplierID();}

    public Response getSupplierNameById(Integer id) {return supplierDAO.getSupplierNameById(id);}
}