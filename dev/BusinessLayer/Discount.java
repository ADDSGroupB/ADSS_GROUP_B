package BusinessLayer;

import java.time.LocalDate;
import java.util.Date;
public class Discount {
    // Static variable to keep track of the last assigned ID
    private static int lastAssignedId = 1;
    private int discountID;
    private LocalDate startDate;
    private LocalDate endDate;
    private double amount;
    // Maybe product id and category id will be in a list ?
    private int productID;
    private int categoryID;

    public Discount(LocalDate sDate, LocalDate eDate,double amount,Object ProductOrCategory)
    {
        this.discountID = lastAssignedId;
        this.startDate = sDate;
        this.endDate = eDate;
        this.amount = amount;
        Discount.lastAssignedId++;
        if (ProductOrCategory instanceof Product) {
            this.productID = ((Product) ProductOrCategory).getProductID();
        } else if (ProductOrCategory instanceof Category) {
            this.categoryID = ((Category) ProductOrCategory).getCategoryID();
        } else {
            throw new IllegalArgumentException("Invalid object type");
        }
    }

    public int getDiscountID() {
        return discountID;
    }
    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public double getAmount() {
        return amount;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public int getProductID() {
        return productID;
    }
    public void setStartDate(Date startDate) {
    }
    public void setEndDate(Date endDate) {
    }
    public void setAmount(double amount) {
    }

}
