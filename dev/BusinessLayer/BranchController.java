package BusinessLayer;

import DataAccess.BranchesDao;
import DataAccess.BranchesDaoImpl;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Queue;

public class BranchController {
    private ReportController reportController;


    public BranchController() {

        this.reportController = new ReportController();
    }

    public ReportController getReportController() {
        return reportController;
    }

    public boolean sellItemCase(Branch branch, Product product) {
        if (branch.getItemsInStore().get(product).size() == 0 && branch.getItemsInStorage().get(product).size() == 0) {
            return false;
        }
        double currPriceProduct = 0;
        double currPriceCategoryParent = 0;
        double currPriceCategorySub = 0;
        double currPriceCategorySubSub = 0;

        // Product discount
        if (branch.getProductDiscount().containsKey(product) && branch.getProductDiscount().get(product).size() > 0) {
            ArrayList<Discount> cuurDisProduct = branch.getProductDiscount().get(product);
            Discount lastInProduct = cuurDisProduct.get(cuurDisProduct.size() - 1);
            if (lastInProduct.getStartDate().isBefore(LocalDate.now()) && lastInProduct.getEndDate().isAfter(LocalDate.now())) {
                currPriceProduct = (((branch.getItemsInStore()).get(product).peek()).getPriceInBranch()) * ((100 - lastInProduct.getAmount()) / 100);
            }
        }
        // category parent discount

        int parentID = product.getParentCategory().getCategoryID();
        if (branch.getCategoryDiscount().containsKey(product.getParentCategory()) && branch.getCategoryDiscount().get(product.getParentCategory()).size() > 0) {
            ArrayList<Discount> cuurDisCategory = branch.getCategoryDiscount().get(product.getParentCategory());
            Discount lastInCategoryParent = cuurDisCategory.get(cuurDisCategory.size() - 1);
            if (lastInCategoryParent.getStartDate().isBefore(LocalDate.now()) && lastInCategoryParent.getEndDate().isAfter(LocalDate.now())) {
                currPriceCategoryParent = branch.getItemsInStore().get(product).peek().getPriceInBranch() * ((100 - lastInCategoryParent.getAmount()) / 100);
            }
        }
        // category sub discount
        int subID = product.getSubCategory().getCategoryID();
        if (branch.getCategoryDiscount().containsKey(product.getSubCategory()) && branch.getCategoryDiscount().get(product.getSubCategory()).size() > 0) {
            ArrayList<Discount> cuurDisCategory = branch.getCategoryDiscount().get(product.getSubCategory());
            Discount lastInCategorySub = cuurDisCategory.get(cuurDisCategory.size() - 1);
            if (lastInCategorySub.getStartDate().isBefore(LocalDate.now()) && lastInCategorySub.getEndDate().isAfter(LocalDate.now())) {
                currPriceCategorySub = branch.getItemsInStore().get(product).peek().getPriceInBranch() * ((100 - lastInCategorySub.getAmount()) / 100);
            }
        }
        // category subSub discount
        int subSubID = product.getSubSubCategory().getCategoryID();
        if (branch.getCategoryDiscount().containsKey(product.getSubSubCategory()) && branch.getCategoryDiscount().get(product.getSubSubCategory()).size() > 0) {
            ArrayList<Discount> cuurDisCategory = branch.getCategoryDiscount().get(product.getSubSubCategory());
            Discount lastInCategorySubSub = cuurDisCategory.get(cuurDisCategory.size() - 1);
            if (lastInCategorySubSub.getStartDate().isBefore(LocalDate.now()) && lastInCategorySubSub.getEndDate().isAfter(LocalDate.now())) {
                currPriceCategorySubSub = branch.getItemsInStore().get(product).peek().getPriceInBranch() * ((100 - lastInCategorySubSub.getAmount()) / 100);
            }
        }
        double minPrice = Integer.MAX_VALUE;
        if (currPriceProduct != 0 && currPriceProduct < minPrice) {
            minPrice = currPriceProduct;
        }
        if (currPriceCategoryParent != 0 && currPriceCategoryParent < minPrice) {
            minPrice = currPriceCategoryParent;
        }
        if (currPriceCategorySub != 0 && currPriceCategorySub < minPrice) {
            minPrice = currPriceCategorySub;
        }
        if (currPriceCategorySubSub != 0 && currPriceCategorySubSub < minPrice) {
            minPrice = currPriceCategorySubSub;
        }

        if (currPriceProduct == 0 && currPriceCategoryParent == 0 && currPriceCategorySub == 0 && currPriceCategorySubSub == 0) {
            branch.sellItem(product);
            return true;
        }
        branch.getItemsInStore().get(product).peek().setPriceAfterDiscount(minPrice);
        branch.sellItem(product);
        return true;
    }

    public void searchDamagedOrExpired(Branch branch) {
        ArrayList<Product> damagedItems = branch.CheckDamagedItems();
        ArrayList<Product> expiredItems = branch.checkExpiredItems();
        ArrayList<Product> finalList = new ArrayList<>();
        HashSet<Product> products = new HashSet<>();
        // Add all items from damagedItems to finalList, checking for duplicates
        for (Product p : damagedItems) {
            if (!products.contains(p)) {
                finalList.add(p);
                products.add(p);
            }
        }
        for (Product p : expiredItems) {
            if (!products.contains(p)) {
                finalList.add(p);
                products.add(p);
            }
        }
        if (finalList.size() > 0) {
            branch.afterCheckAlertCall(finalList);
        }
    }

    public void addItems(Branch branch,Product product,int quantity,int BranchID, LocalDate date, double priceFromSupplier,double priceInBranch,int supplierID) {
        branch.fromSupplierToStorage(product, quantity, branch.getBranchID(),LocalDate.now() ,date, priceFromSupplier, priceInBranch, supplierID);
    }

    public boolean checkDamagedItem(Branch branch,Product product,int itemIDdefective) {
        boolean checkInStore = false;
        Queue<Item> itemQueueInStore = branch.getItemsInStore().get(product);
            for (Item item : itemQueueInStore) {
                if (item.getItemID() == itemIDdefective) {
                    checkInStore = true;
                    break;
                }
            }
        boolean checkInStorage = false;
            Queue<Item> itemQueueInStorage = branch.getItemsInStorage().get(product);
            for (Item item : itemQueueInStorage) {
                if (item.getItemID() == itemIDdefective) {
                    checkInStorage = true;
                    break;
                }
            }
        return checkInStore || checkInStorage;
    }

    public void addNewDiscountForProduct(Branch branch ,Product product ,Discount discount) {
            branch.addProductDiscountMap(product, discount);
        }
    public void addNewDiscountForCategory(Branch branch,Category category,Discount discount) {
            branch.addCategoryDiscountMap(category, discount);
        }
    public LocalDate validDate(String dateString) {
        LocalDate date;

        if (!dateString.matches("\\d{4}-\\d{2}-\\d{2}")) {
            System.out.println("Date is not in the correct format.");
            return null;
        }
        try {
            date = LocalDate.parse(dateString);
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Please enter a date in the format of YYYY-MM-DD.");
            return null;
        }
        return date;
    }
}


