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
    private ArrayList<Discount> totalOrderDiscount;
    private boolean selfSupply;
    private ArrayList<DaysOfWeek> supplyDays;

    public Agreement(String paymentType, boolean selfSupply, ArrayList<DaysOfWeek> supplyDays) {
        this.agreementID = id++;
        this.paymentType = paymentType;
        this.discountProducts = new HashMap<Integer, ArrayList<Discount>>();
        this.totalOrderDiscount = new ArrayList<Discount>();
        this.selfSupply = selfSupply;
        this.supplyDays = supplyDays;
    }
    public void addTotalDiscount(String type, int quantity, int value)
    {
        if (type.toLowerCase() == "Percent")
            totalOrderDiscount.add(new PercentDiscount(quantity, value));
        else if (type.toLowerCase() == "Value")
            totalOrderDiscount.add(new ValueDiscount(quantity, value));
        else
            System.out.println("Wrong type, please Choose Percent / Value");
    }
    public void addProductDiscount(String type, int productID, int quantity, int value)
    {
//        discountProducts[productID].
    }
}
