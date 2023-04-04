public class PercentDiscount extends Discount
{
    private double discountPercent;
    public PercentDiscount(int quantity, int discountValue)
    {
        super(quantity);
        this.discountPercent = discountValue;
    }
    public double getDiscount(double price)
    {
        return price - (discountPercent / 100 * price);
    }
}
