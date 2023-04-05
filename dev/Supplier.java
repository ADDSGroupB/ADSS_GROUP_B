import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Supplier
{
    private static int id = 0;
    private static int cid = 0;
    private int supplierID;
    private String firstName;
    private String lastName;
    private Map<String, String> contact;
    private String bankAccount;
    private ArrayList<String> manufacturers;
    private ArrayList<String> domains;
    private boolean isActive;
    private Map<Integer, Integer> productsMap;
    private Map<Integer, ArrayList<SupplierProduct>> products;
    private Map<Integer, Integer> productsAmount;
    private Agreement agreement;

    public Supplier(String firstName, String lastName, String bankAccount, ArrayList<String> manufacturers, ArrayList<String> domains) {
        this.supplierID = id++;
        this.firstName = firstName;
        this.lastName = lastName;
        this.contact = new HashMap<String, String>();
        this.bankAccount = bankAccount;
        this.manufacturers = manufacturers;
        this.domains = domains;
        this.isActive = true;
        this.productsMap = new HashMap<Integer, Integer>();;
        this.productsAmount  = new HashMap<Integer, Integer>();;
        this.agreement = null;
    }

    public void setAgreement(String paymentType, boolean selfSupply, ArrayList<DaysOfWeek> supplyDays) {
        this.agreement = new Agreement(paymentType, selfSupply, supplyDays);
    }

    public int getSupplierID() {
        return supplierID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getContact(String name) {
        return contact.get(name);
    }

    public void addContact(String name, String email) {
        this.contact.put(name, email);
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public ArrayList<String> getManufacturers() {
        return manufacturers;
    }

    public void addManufacturers(String name) {
        this.manufacturers.add(name);
    }

    public ArrayList<String> getDomains() {
        return domains;
    }

    public void addDomains(String domain) {
        this.domains.add(domain);
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Map<Integer, ArrayList<SupplierProduct>> getProducts() {
        return products;
    }

    public Agreement getAgreement() {
        return agreement;
    }
    //  אולי לעשות על בודד
    public Map<Integer, Integer> getProductsMap() {
        return productsMap;
    }
    //  אולי לעשות על בודד
    public Map<Integer, Integer> getProductsAmount() {
        return productsAmount;
    }

    public void addProduct(String name, int productID, double price, String manufacturer) {
        if(!productsMap.containsKey(productID))
        {
            productsMap.put(productID, cid++);
            products.put(cid, new ArrayList<SupplierProduct>());
            productsAmount.put(cid, 0);
        }
        SupplierProduct product = new SupplierProduct(name, productID, cid, price, manufacturer);
        products.get(productsMap.get(productID)).add(product);
        productsAmount.put(productsMap.get(productID), productsAmount.get(productsMap.get(productID)) + 1);
    }

}
