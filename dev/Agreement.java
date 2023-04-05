import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Agreement
{
    private static int id = 0;
    private int agreementID;
    private String paymentType;
    private Map<Integer, ArrayList<Discount>> discountProducts;
    private Map<Integer, Discount> totalOrderDiscount;
    private boolean selfSupply;
    private int daysAmountToSupply;
    private ArrayList<DaysOfWeek> supplyDays;

    public Agreement(String paymentType, boolean selfSupply, int daysAmountToSupply, ArrayList<DaysOfWeek> supplyDays) {
        this.agreementID = id++;
        this.paymentType = paymentType;
        this.discountProducts = new HashMap<Integer, ArrayList<Discount>>();
        this.totalOrderDiscount = new HashMap<Integer, Discount>();
        this.selfSupply = selfSupply;
        this.daysAmountToSupply = daysAmountToSupply;
        this.supplyDays = supplyDays;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public int getDaysAmountToSupply() {
        return daysAmountToSupply;
    }

    public void setDaysAmountToSupply(int daysAmountToSupply) {
        this.daysAmountToSupply = daysAmountToSupply;
    }

    public boolean isSelfSupply() {
        return selfSupply;
    }

    public void setSelfSupply(boolean selfSupply) {
        this.selfSupply = selfSupply;
    }

    public Map<Integer, ArrayList<Discount>> getDiscountProducts() {
        return discountProducts;
    }

    public Map<Integer, Discount> getTotalOrderDiscount() {
        return totalOrderDiscount;
    }

    public void printDiscountProduct(int catalogID) {
        System.out.println("Product number " + catalogID + "got the following discounts:");
        for(Discount discount : this.discountProducts.get(catalogID))
            System.out.println(discount);
    }

    public void printTotalOrderDiscount() {
        System.out.println("The total order discounts are:");
        for(Discount discount : this.totalOrderDiscount.values())
            System.out.println(discount);
    }

    public String getSupplyDays() {
        String days = "The supply days are: ";
        for(DaysOfWeek day : this.supplyDays)
            days += day + ", ";
        return days.substring(0, days.length()-3);
    }

    public void addSupplyDay(DaysOfWeek day) {
        if(!this.supplyDays.contains(day))
            this.supplyDays.add(day);
    }

    public int getAgreementID() {
        return agreementID;
    }

    public void removeSupplyDay(DaysOfWeek day) {
        if(this.supplyDays.contains(day))
            this.supplyDays.remove(day);
    }

    public boolean addTotalDiscount(String type, int quantity, int value)
    {
        if (type.toLowerCase() != "Percent" && type.toLowerCase() != "Value")
        {
            System.out.println("Wrong type, please Choose Percent / Value");
            return false;
        }
        Discount discount;
        if (type.toLowerCase() == "Percent")
        {
            discount = new PercentDiscount(quantity, value);
            totalOrderDiscount.put(discount.getDiscountID(), discount);
        }
        else if (type.toLowerCase() == "Value")
        {
            discount = new ValueDiscount(quantity, value);
            totalOrderDiscount.put(discount.getDiscountID(), discount);
        }
        return true;
    }
    public boolean removeTotalDiscount(int discountID)
    {
        if(!this.totalOrderDiscount.containsKey(discountID)) {
            System.out.println("There is not total order discount with the id " + discountID);
            return false;
        }
        else {
            this.totalOrderDiscount.remove(discountID);
            return true;
        }
    }
    public boolean removeTotalDiscount(String type, int quantity, double value)
    {
        for(Discount discount : this.totalOrderDiscount.values()) {
            if (((type == "Percent" && discount instanceof PercentDiscount) ||
                    (type == "Value" && discount instanceof ValueDiscount)) &&
                    discount.getQuantity() == quantity && discount.getDiscountValue() == value) {
                this.totalOrderDiscount.remove(discount.getDiscountID());
                return true;
            }
        }
        System.out.println("There is not " + type + " total order discount with the quantity "
                + quantity + " and the value " + value);
        return false;
    }
    public boolean updateTotalDiscount(int discountID, String type, int newQuantity, int newValue)
    {

        if(((type == "Percent" && this.totalOrderDiscount.get(discountID) instanceof ValueDiscount ) ||
                (type == "Value" && this.totalOrderDiscount.get(discountID) instanceof PercentDiscount))) {
            System.out.println("There is not total order discount with the id " + discountID + " and the type " + type);
            return false;
        }
        this.totalOrderDiscount.get(discountID).setQuantity(newQuantity);
        this.totalOrderDiscount.get(discountID).setDiscountValue(newValue);
        return true;
    }
    public boolean addProductDiscount(String type, int catalogID, int quantity, int value)
    {
        if (type.toLowerCase() != "Percent" && type.toLowerCase() != "Value")
        {
            System.out.println("Wrong type, please Choose Percent / Value");
            return false;
        }
        if(!discountProducts.containsKey(catalogID))
            discountProducts.put(catalogID, new ArrayList<Discount>());
        if (type.toLowerCase() == "Percent")
            discountProducts.get(catalogID).add(new PercentDiscount(quantity, value));
        else if (type.toLowerCase() == "Value")
            discountProducts.get(catalogID).add(new ValueDiscount(quantity, value));
        return true;
    }
    public boolean removeProductDiscount(int catalogID, int discountID)
    {
        if(!this.discountProducts.containsKey(catalogID)) {
            System.out.println("There is not product with the catalog id " + catalogID);
            return false;
        }
        for(Discount discount : this.discountProducts.get(catalogID))
        {
            if(discount.getDiscountID() == discountID)
            {
                this.discountProducts.get(catalogID).remove(discount);
                return true;
            }
        }
        System.out.println("There is not product discount for product with the catalog id "
                + catalogID + " and discount id " + discountID);
        return false;
    }
    public boolean removeProductDiscount(int catalogID, String type, int quantity, int value)
    {
        if(!this.discountProducts.containsKey(catalogID)) {
            System.out.println("There is not product with the catalog id " + catalogID);
            return false;
        }
        for(Discount discount : this.discountProducts.get(catalogID))
        {
            if(((type == "Percent" && discount instanceof PercentDiscount) ||
                    (type == "Value" && discount instanceof ValueDiscount)) &&
                    discount.getQuantity() == quantity && discount.getDiscountValue() == value)
            {
                this.discountProducts.get(catalogID).remove(discount);
                return true;
            }
        }
        System.out.println("There is not " + type + " product discount for product with the catalog id "
                + catalogID + " with the quantity "
                + quantity + " and the value " + value);
        return false;
    }
    public boolean updateProductDiscount(int catalogID, int discountID, int newQuantity, int newValue)
    {
        if(!this.discountProducts.containsKey(catalogID)) {
            System.out.println("There is not product with the catalog id " + catalogID);
            return false;
        }
        for(Discount discount : this.discountProducts.get(catalogID))
        {
            if(discount.getDiscountID() == discountID)
            {
                discount.setDiscountValue(newValue);
                discount.setQuantity(newQuantity);
                return true;
            }
        }
        System.out.println("There is not product discount for product with the catalog id "
                + catalogID + " and discount id " + discountID);
        return false;
    }
}
