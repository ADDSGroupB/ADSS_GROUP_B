import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Supplier
{
    private static int id = 0;
    private int supplierID;
    private String firstName;
    private String lastName;
    private Map<String, String> contact;
    private String bankAccount;
    private ArrayList<String> manufacturers;
    private ArrayList<String> domains;
    private boolean isActive;
    private Map<Integer, Integer> products;
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
        this.products = new HashMap<Integer, Integer>();;
        this.productsAmount  = new HashMap<Integer, Integer>();;
        this.agreement = null;
    }

    public void setAgreement(String paymentType, boolean selfSupply, ArrayList<DaysOfWeek> supplyDays) {
        this.agreement = new Agreement(paymentType, selfSupply, supplyDays);
    }
    public void addTotalDiscount(String type, int quantity, int value)
    {
        this.agreement.addTotalDiscount(type, quantity, value);
    }

}
