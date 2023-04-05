public class PercentDiscount extends Discount
{
    private double discountPercent;
    public PercentDiscount(int quantity, int discountValue)
    {
        super(quantity);
        this.discountPercent = discountValue;
    }
    @Override
    public double calcDiscount(double price)
    {
        return price - (discountPercent / 100 * price);
    }

    @Override
    public double getDiscountValue() {
        return discountPercent;
    }

    @Override
    public void setDiscountValue(double value) {
        this.discountPercent = value;
    }

    @Override
    public String toString() {
        return "Percent Discount:\nDiscount ID: " + this.discountID +
                "Quantity: " + this.quantity + "\nDiscount Percent: "
                + this.discountPercent + "%";
    }
}
