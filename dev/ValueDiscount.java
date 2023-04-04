public class ValueDiscount extends Discount{
    private double discountValue;
    public ValueDiscount(int quantity, int discountValue)
    {
        super(quantity);
        this.discountValue = discountValue;
    }
    public double getDiscount(double price)
    {
        return price - discountValue;
    }
}
