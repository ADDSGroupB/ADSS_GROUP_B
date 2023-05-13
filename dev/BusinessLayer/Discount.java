package BusinessLayer;

import java.time.LocalDate;
import java.util.Date;
public class Discount {
    private int discountID;
    private int branchID;
    private LocalDate startDate;
    private LocalDate endDate;
    private double amount;
    private Integer productID;
    private Integer categoryID;
    public Discount(int discountID,int branchID,LocalDate sDate, LocalDate eDate,double amount,Object ProductOrCategory)
    {
        this.discountID = discountID;
        this.branchID = branchID;
        this.startDate = sDate;
        this.endDate = eDate;
        this.amount = amount;
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
    public int getCategoryID() {return categoryID;}
    public int getProductID() {return productID;}
    @Override
    public String toString() {
        String returnString = "";
        returnString += "Discount ID : " + discountID + " ";
        returnString += "\n" + "Branch ID : " + branchID + " ";
        if (productID != null)
        {returnString += "\n" + "Product ID : " + productID + " ";}
        if (categoryID != null)
        {returnString += "\n" + "Category ID : " + categoryID + " ";}
        returnString += "\n" + "Start Date : " + startDate + " ";
        returnString += "\n" + "End Date : " + endDate + " ";
        returnString += "\n" + "Discount Amount : " + amount + " ";
        return returnString;
    }

}
