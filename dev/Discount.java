public abstract class Discount {
    private static int id = 0;
    private int discountID;
    private int quantity;
    public Discount(int quantity)
    {
        this.discountID = id++;
        this.quantity = quantity;
    }
    public abstract double getDiscount(double price);
}
