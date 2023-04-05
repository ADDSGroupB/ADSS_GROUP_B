import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Supplier
{
    private static int id = 0;
    private static int cid = 0;
    private int supplierID;
    private String name;
    private Map<String, String> contact;
    private String bankAccount;
    private ArrayList<String> manufacturers;
    private ArrayList<String> domains;
    private boolean isActive;
    private Map<Integer, Integer> productsMap;
    private Map<Integer, ArrayList<SupplierProduct>> products;
    private Map<LocalDate, ArrayList<Order>> orderHistory;
    private Agreement agreement;

    public Supplier(String name, String bankAccount, ArrayList<String> manufacturers, ArrayList<String> domains) {
        this.supplierID = id++;
        this.name = name;
        this.contact = new HashMap<String, String>();
        this.bankAccount = bankAccount;
        this.manufacturers = manufacturers;
        this.domains = domains;
        this.isActive = true;
        this.products = new HashMap<Integer, ArrayList<SupplierProduct>>();;
        this.orderHistory = new HashMap<LocalDate, ArrayList<Order>>();
        this.agreement = null;
    }

    public void addOrder(LocalDate date, Order order)
    {
        if(!orderHistory.containsKey(date))
            orderHistory.put(date, new ArrayList<Order>());
        orderHistory.get(date).add(order);
    }
    public void removeOrder(LocalDate date, int orderID)
    {
        if(orderHistory.containsKey(date))
            for(Order order : orderHistory.get(date))
                if(order.getOrderID() == orderID)
                    orderHistory.get(date).remove(order);
    }
    public ArrayList<Order> getOrdersByDate(LocalDate date)
    {
        if(!orderHistory.containsKey(date)) return null;
        return orderHistory.get(date);
    }

    public Map<LocalDate, ArrayList<Order>> getOrderHistory() {
        return orderHistory;
    }

    public void setAgreement(String paymentType, boolean selfSupply, int daysAmountToSupply, ArrayList<DaysOfWeek> supplyDays) {
        this.agreement = new Agreement(paymentType, selfSupply, daysAmountToSupply, supplyDays);
    }

    public int getSupplierID() {
        return supplierID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
    public Map<Integer, Integer> getProductsMap() {
        return productsMap;
    }
    public int getCatalogID(int productID)
    {
        return productsMap.get(productID);
    }
    public int getProductAmount(int catalogID) {
        return products.get(catalogID).size();
    }

    public ArrayList<SupplierProduct> getProductListByCatalogID(int catalogID)
    {
        return products.get(catalogID);
    }

    public void addProduct(String name, int productID, double price, String manufacturer) {
        if(!productsMap.containsKey(productID))
        {
            productsMap.put(productID, cid++);
            products.put(cid, new ArrayList<SupplierProduct>());
        }
        SupplierProduct product = new SupplierProduct(name, productID, cid, price, manufacturer);
        products.get(productsMap.get(productID)).add(product);
    }
    public boolean removeProduct(int catalogID)
    {
        if(products.containsKey(catalogID) && !products.get(catalogID).isEmpty()) {
            products.get(catalogID).remove(0);
            return true;
        }
        return false;
    }
    public boolean removeProductFromSupplier(int catalogID)
    {
        if(products.containsKey(catalogID)) {
            products.get(catalogID).clear();
            products.remove(catalogID);
            for (int productID : productsMap.keySet()) {
                if (productsMap.get(productID) == catalogID) {
                    productsMap.remove(productID);
                    return true;
                }
            }
        }
        return false;
    }


}
