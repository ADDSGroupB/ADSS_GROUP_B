import java.util.ArrayList;
import java.time.LocalDate;

public class Order
{
    private static int id = 0;
    private int orderID;
    private LocalDate orderDate;
    private ArrayList<SupplierProduct> products;
    private double price;

    public Order(ArrayList<SupplierProduct> products, double price)
    {
        this.orderID = id++;
        this.orderDate = LocalDate.now();
        this.products = products;
        this.price = price;
    }
}
