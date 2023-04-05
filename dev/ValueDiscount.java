public class ValueDiscount extends Discount{
    private double discountValue;
    public ValueDiscount(int quantity, int discountValue)
    {
        super(quantity);
        this.discountValue = discountValue;
    }
    @Override
    public double calcDiscount(double price)
    {
        return price - discountValue;
    }

    @Override
    public double getDiscountValue() {
        return discountValue;
    }
    @Override
    public void setDiscountValue(double value) {
        this.discountValue = value;
    }
    @Override
    public String toString() {
        return "Value Discount:\nDiscount ID: " + this.discountID +
                "Quantity: " + this.quantity + "\nDiscount Value: "
                + this.discountValue + "₪";
    }
}
