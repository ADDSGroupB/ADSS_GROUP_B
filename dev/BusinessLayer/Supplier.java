package BusinessLayer;


import Utillity.Pair;

//import java.awt.geom.Area;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Supplier {
    private static int id = 1;
    private int supplierId;
    private String name;
    private String address;
    //private HashMap<String, Contact> contacts; //<key: firstName lastName, value: contact: Contact>
    private ArrayList<Contact> contacts;
    private String bankAccount;
    private ArrayList<String> manufacturers; //יצרנים
    private ArrayList<String> domains; //תחומים
    private boolean isActive;
    //private Map<Integer, Integer> productsAmount; // < catalogId: int, quantity: int >
    private Agreement agreement;

    public Supplier(int supplierId, String name, String address, ArrayList<Contact> contacts, String bankAccount, Agreement agreement) {
        this.supplierId = supplierId;
        this.name = name;
        this.address = address;
        this.contacts = contacts;
        this.bankAccount = bankAccount;
        this.agreement = agreement;
    }

    public Supplier(String name, String bankAccount, String address, ArrayList<String> manufacturers, ArrayList<String> domains, Agreement agreement, HashMap<Integer, Integer> productsAmount) {
        this.supplierId = id++;
        this.name = name;
        this.contacts = new ArrayList<>();
        this.bankAccount = bankAccount;
        this.address = address;
        this.manufacturers = manufacturers;
        this.domains = domains;
        this.isActive = true;
        //this.productsAmount  = productsAmount;
        this.agreement = agreement;
    }

    public Supplier(String name, String bankAccount, String address, Agreement agreement) {
        this.supplierId = id++;
        this.name = name;
        this.bankAccount = bankAccount;
        this.address = address;
        this.isActive = true;
        this.agreement = agreement;
    }

    public Supplier(String name, String address, String bankAccount) {
        this.supplierId = id++;
        this.name = name;
        this.address = address;
        this.bankAccount = bankAccount;
        this.isActive = true;
        this.agreement = null;
        contacts = new ArrayList<>();
    }

    //   Get / Set   //
    public int getSupplierId() {
        return supplierId;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public void setName(String newName) {
        name = newName;
    }

    public void setContacts(ArrayList<Contact> contactsList) {
        this.contacts = contactsList;
    }


//    public HashMap<String, String> getContact(){
//        return contacts;
//    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String newBankAccount) {
        bankAccount = newBankAccount;
    }

    public ArrayList<String> getManufacturers() {
        return manufacturers;
    }

    public void setNewManufacturers(ArrayList<String> manufacturers) {
        this.manufacturers = manufacturers;
    }

    public void addNewManufactures(ArrayList<String> manufacturers) {
        for (String manufacturer : manufacturers) {
            if (!this.manufacturers.contains(manufacturer)) {
                this.manufacturers.add(manufacturer);
            }
        }
    }

    public ArrayList<String> getDomains() {
        return domains;
    }

    public void setNewDomains(ArrayList<String> domain) {
        this.domains = domain;
    }

    public void addNewDomains(ArrayList<String> domains) {
        for (String dom : domains) {
            if (!this.domains.contains(dom)) {
                this.domains.add(dom);
            }
        }
    }

    public void changeIsActiveStatment() {
        if (isActive) {
            isActive = false;
        } else {
            isActive = true;
        }
    }

    public Agreement getAgreement() {
        return agreement;
    }

//    public void setAgreement(String paymentType, boolean selfSupply, ArrayList<DaysOfWeek> supplyDays) {
//        this.agreement = new Agreement(paymentType, selfSupply, supplyDays);
//    }

    public void setNewAgreement(Agreement agreement) {
        this.agreement = agreement;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void addContact(String name, String email, String phone) {
        contacts.add(new Contact(name, email, address));

    }

    public ArrayList<Contact> getContacts() {
        return contacts;
    }

    public void addProduct(String name, int productId, int catalogNumber, double price, int amount, HashMap<Integer, Double> discountPerAmount) {
        agreement.getSupllyingProducts().put(productId, new SupplierProduct(name, productId, catalogNumber, price, amount, discountPerAmount));
    }

    public void removeProduct(int productId) {
        agreement.getSupllyingProducts().remove(productId);
    }

    public void setSelfSupply(boolean selfSupply) {
        this.agreement.setSelfSupply(selfSupply);
    }

    public void SetPaymentType(String paymentType) {
        this.agreement.setPaymentType(paymentType);
    }

    public void setDeliveyDays(ArrayList<DayOfWeek> days) {
        agreement.setSupplyDays(days);
    }

    public void setProdactCatalogNumber(int productId, int newCatalogNumber) {
        agreement.getSupllyingProducts().get(productId).setCatalogID(newCatalogNumber);
    }

    public Map<Integer, SupplierProduct> getSupplyingProducts() {
        return agreement.getSupllyingProducts();
    }


    // return Array of products the specific supplier can supply:  get a list of products
    // getItemsToCreateOrder: <productId, amount>
/*    public ArrayList<Pair<Integer, Integer>> getItemsToCreateOrder(ArrayList<Pair<Integer, Integer>> productsToOrder){
        ArrayList<Pair<Integer, Integer>> itemsToCreateOrder = new ArrayList<>();
        Map<Integer, SupplierProduct> supplyingProducts = getSupplyingProducts();

        for(Pair<Integer, Integer> pair : productsToOrder){
            if(supplyingProducts.containsKey(pair.getFirst())){
                int amount = supplyingProducts.get(pair.getFirst()).getAmount();
                if(amount - pair.getSecond() >= 0){
                    itemsToCreateOrder.add(new Pair<>(pair.getFirst(), pair.getSecond()));
                }
                else {
                    itemsToCreateOrder.add(new Pair<>(pair.getFirst(), amount));
                }
            }
        }
        return itemsToCreateOrder;
        //<כמות ,מוצרים> ספק מקבל רשימת מוצרים ומחזיר רשימת מוצרים שהוא יכול לספק
    }*/


    public ArrayList<Pair<Integer, Integer>> getItemsToCreateOrder(HashMap<Integer, Integer> productsToOrder) {
        ArrayList<Pair<Integer, Integer>> itemsToCreateOrder = new ArrayList<>();
        Map<Integer, SupplierProduct> supplyingProducts = getSupplyingProducts();

        for (Map.Entry<Integer, Integer> entry : productsToOrder.entrySet()) {

            if (supplyingProducts.containsKey(entry.getKey())) {
                int amount = supplyingProducts.get(entry.getKey()).getAmount();
                if (amount - entry.getValue() >= 0) {
                    itemsToCreateOrder.add(new Pair<>(entry.getKey(), entry.getValue()));
                } else {
                    itemsToCreateOrder.add(new Pair<>(entry.getKey(), amount));
                }
            }
        }
        return itemsToCreateOrder;
        //<כמות ,מוצרים> ספק מקבל רשימת מוצרים ומחזיר רשימת מוצרים שהוא יכול לספק
    }


    public double calculatePriceAfterDiscount (ArrayList<Pair<Integer,Integer>> products) {
        double totalPriceForAllOrder = 0.0;
        for (Pair<Integer, Integer> pair : products) {
            double totalPrice=0.0;
            int productId = pair.getFirst();
            int amountForDiscount = pair.getSecond();
            boolean canUseAdiscount = false;
            SupplierProduct product = agreement.getSupllyingProducts().get(productId);
            double productPrice = product.getPrice();
            HashMap<Integer, Double> productDiscounts = product.getDiscountPerAmount();
            for (Map.Entry<Integer, Double> entry : productDiscounts.entrySet()) {
                if (entry.getKey() <= pair.getSecond()) {
                    canUseAdiscount = true;
                    amountForDiscount = entry.getKey();
                } else {
                    break;
                }
            }
            if (canUseAdiscount) {
                double priceAfterDiscount = ((100 - productDiscounts.get(amountForDiscount)) * amountForDiscount * productPrice) / 100;
                totalPrice = (pair.getSecond() - amountForDiscount) * productPrice + priceAfterDiscount;
            } else {
                totalPrice = (pair.getSecond()) * productPrice;
            }
            totalPriceForAllOrder+=totalPrice;
        }
        return totalPriceForAllOrder;
    }

    public SupplierProduct getProductById (int productId){
        return getSupplyingProducts().get(productId);
}


    public double calculatePriceBeforeDiscount (ArrayList<Pair<Integer,Integer>> products) {
        double totalPriceForAllOrder = 0.0;
        for (Pair<Integer, Integer> pair : products) {
            int productId = pair.getFirst();
            int amountToOrder = pair.getSecond();
            SupplierProduct product = agreement.getSupllyingProducts().get(productId);
            double productPrice = product.getPrice();
            totalPriceForAllOrder+=amountToOrder*productPrice;
        }
        return totalPriceForAllOrder;
    }
    public Pair<Double,Double> getTotalOrderDiscountPerOrderPrice(){
        return agreement.getTotalOrderDiscountPerOrderPrice();
    }
    public Pair<Integer,Double> getTotalDiscountInPrecentageForOrder(){
        return agreement.getTotalDiscountInPrecentageForOrder();
    }

    public int getTotalAmount (ArrayList<Pair<Integer,Integer>> products) {
        int totalAmount=0;
        for (Pair<Integer,Integer> p:products){
            totalAmount+=p.getSecond();
        }
        return totalAmount;
    }

//    public HashMap<Integer, Integer> getsupplierProducts(){
//        return supplierProducts;
//    }

    //public addProductToSupplierProduct(SupplierProduct supplierProduct){
      //  if(!this.supplierProducts.containsKey())
    //}

//    public void addTotalDiscount(String type, int quantity, int value)
//    {
//        this.agreement.addTotalDiscount(type, quantity, value);
//    }


}
