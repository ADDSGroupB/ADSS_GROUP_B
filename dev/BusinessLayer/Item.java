package BusinessLayer;

import java.time.LocalDate;
public class Item {

    // Static variable to keep track of the last assigned ID
    private static int lastAssignedId = 1;
    private final int ItemID;//
    private int SupplierID;//
    private int BranchID;//
    private int ProductID;//
    private LocalDate ExpiredDate;//
    private LocalDate ArrivalDate;//
    private String DefectiveDiscription; // Maybe call it Comment
    private double PriceFromSupplier;//
    private double PriceInBranch;//
    private DanmagedOrExpiredEnum DamagedOrExpiredType;
    private double priceAfterDiscount;//
    private Product product;
    // constructor for items with expired date
    public Item(int branchID, LocalDate expiredDate, LocalDate arrivalDate, double priceFromSupplier , double priceInBranch , int supplierID, Product product)
    {
        this.ItemID = lastAssignedId;
        this.BranchID = branchID;
        this.ProductID = product.getProductID();
        this.PriceFromSupplier=priceFromSupplier;
        this.PriceInBranch = priceInBranch;
        this.SupplierID = supplierID;
        this.ExpiredDate = expiredDate;
        this.ArrivalDate = arrivalDate;
        this.priceAfterDiscount = priceInBranch;
        this.product = product;
        Item.lastAssignedId++;
    }
    // constructor for items without expired date
    public Item(int branchID,double priceFromSupplier , LocalDate arrivalDate,double priceInBranch ,int supplierID,Product product)
    {
        this.ItemID = lastAssignedId;
        this.BranchID = branchID;
        this.ProductID = product.getProductID();
        this.PriceFromSupplier=priceFromSupplier;
        this.PriceInBranch = priceInBranch;
        this.SupplierID = supplierID;
        this.priceAfterDiscount = priceInBranch;
        this.ArrivalDate = arrivalDate;
        this.product = product;
        Item.lastAssignedId++;
    }

    public Product getProduct() {return product;}

    public int getBranchID() {
        return BranchID;
    }
    public int getProductID() {
        return ProductID;
    }
    public LocalDate getExpiredDate() {
        return ExpiredDate;
    }
    public double getPriceFromSupplier() {
        return PriceFromSupplier;
    }
    public double getPriceInBranch() {
        return PriceInBranch;
    }
    public int getItemID() {
        return ItemID;
    }
    public int getSupplierID() {
        return SupplierID;
    }
    public String getDefectiveDiscription() {
        return DefectiveDiscription;
    }
    public void setDefectiveDiscription(String defectiveDiscription) {
        DefectiveDiscription = defectiveDiscription;
    }
    public void setPriceInBranch(double priceInBranch) {PriceInBranch = priceInBranch;}
    public DanmagedOrExpiredEnum getDamagedOrExpiredType() {
        return DamagedOrExpiredType;
    }
    public void setDamagedOrExpiredType(DanmagedOrExpiredEnum damagedOrExpiredType) {DamagedOrExpiredType = damagedOrExpiredType;}
    public double getPriceAfterDiscount() {return priceAfterDiscount;}
    public void setPriceAfterDiscount(double priceAfterDiscount) {this.priceAfterDiscount = priceAfterDiscount;}

    @Override
    public String toString() {
        if (DamagedOrExpiredType == null) {
            if (ExpiredDate != null) {
                return "\n" + "Item ID: " + ItemID + " " +
                        "Branch ID: " + BranchID + " " +
                        "Product ID: " + ProductID + " " +
                        "Expired Date: " + ExpiredDate + " " +
                        "Price from Supplier: " + PriceFromSupplier + " " +
                        "Price in Branch: " + PriceInBranch + " " +
                        "Supplier ID: " + SupplierID+ " " +
                        "ArrivalDate: " + ArrivalDate;}
            else {
                return "\n" + "Item ID: " + ItemID + " " +
                        "Branch ID: " + BranchID + " " +
                        "Product ID: " + ProductID + " " +
                        "Price from Supplier: " + PriceFromSupplier + " " +
                        "Price in Branch: " + PriceInBranch + " " +
                        "Supplier ID: " + SupplierID+ " " +
                        "ArrivalDate: " + ArrivalDate;}}
        else {
            if (DamagedOrExpiredType == DanmagedOrExpiredEnum.Damaged) {
                if (ExpiredDate != null) {
                    return "\n" + "Item ID: " + ItemID + " " +
                            "Branch ID: " + BranchID + " " +
                            "Product ID: " + ProductID + " " +
                            "Expired Date: " + ExpiredDate + " " +
                            "Price from Supplier: " + PriceFromSupplier + " " +
                            "Price in Branch: " + PriceInBranch + " " +
                            "Supplier ID: " + SupplierID + " " +
                            "DamagedOrExpiredType: " + DamagedOrExpiredType + " " +
                            "DefectiveDiscription: " + DefectiveDiscription + " " +
                            "ArrivalDate: " + ArrivalDate;}
                else {
                    return "\n" + "Item ID: " + ItemID + " " +
                            "Branch ID: " + BranchID + " " +
                            "Product ID: " + ProductID + " " +
                            "Price from Supplier: " + PriceFromSupplier + " " +
                            "Price in Branch: " + PriceInBranch + " " +
                            "Supplier ID: " + SupplierID + " " +
                            "DamagedOrExpiredType: " + DamagedOrExpiredType + " " +
                            "DefectiveDiscription: " + DefectiveDiscription+ " "
                            +"ArrivalDate: " + ArrivalDate;}}
            else {
                return "\n" + "Item ID: " + ItemID + " " +
                        "Branch ID: " + BranchID + " " +
                        "Product ID: " + ProductID + " " +
                        "Price from Supplier: " + PriceFromSupplier + " " +
                        "Price in Branch: " + PriceInBranch + " " +
                        "Supplier ID: " + SupplierID + " " +
                        "DamagedOrExpiredType: " + DamagedOrExpiredType + " " +
                        "Expired Date: " + ExpiredDate+ " " +
                        "ArrivalDate: " + ArrivalDate;}}}
}
