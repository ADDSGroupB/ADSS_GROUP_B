public abstract class Discount {
    private static int id = 0;
    protected int discountID;
    protected int quantity;
    public Discount(int quantity)
    {
        this.discountID = id++;
        this.quantity = quantity;
    }
    public abstract double calcDiscount(double price);
    public abstract void setDiscountValue(double value);
    public abstract double getDiscountValue();

    public int getDiscountID() {
        return discountID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
