package BusinessLayer.InventoryBusinessLayer;

import DataAccessLayer.InventoryDataAccessLayer.*;
import DataAccessLayer.InventoryDataAccessLayer.*;

import java.sql.SQLException;

public class MainController {
    private CategoryController categoryController;
    private ProductController productController;
    private BranchController branchController;
    private BranchesDao branchesDao;
    private CategoryDao categoryDao;
    private ItemsDao itemsDao;
    private ProductMinAmountDao productMinAmountDao;
    private ProductsDao productsDao;
    private ReportDao reportDao;
    private DiscountsDao discountsDao;

    public MainController() {
        this.branchController = new BranchController(this);
        this.categoryController = new CategoryController(this);
        this.productController = new ProductController(this);
        try {
            this.branchesDao = new BranchesDaoImpl();
            this.categoryDao = new CategoryDaoImpl();
            this.itemsDao = new ItemsDaoImpl();
            this.productMinAmountDao = new ProductMinAmountDaoImpl();
            this.productsDao = new ProductsDaoImpl();
            this.reportDao = new ReportDaoImpl(this);
            this.discountsDao = new DiscountDaoImpl();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public BranchesDao getBranchesDao() {return branchesDao;}
    public CategoryDao getCategoryDao() {return categoryDao;}
    public ItemsDao getItemsDao() {return itemsDao;}
    public DiscountsDao getDiscountsDao() {return discountsDao;}
    public ProductMinAmountDao getProductMinAmountDao() {return productMinAmountDao;}
    public ProductsDao getProductsDao() {return productsDao;}
    public ReportDao getReportDao() {return reportDao;}
    public BranchController getBranchController() {
        return branchController;
    }
    public ProductController getProductController() {
        return productController;
    }
    public CategoryController getCategoryController() {
        return categoryController;
    }
    public Item PriceCalculationAfterDiscount(Item item,int branchID) throws SQLException
    {
        if (item == null) {return null;}
        Product productItem = item.getProduct();
        if (productItem == null) {return null;}
        Category parentCategory = productItem.getParentCategory();
        Category subCategory = productItem.getSubCategory();
        Category subSubCategory = productItem.getSubSubCategory();

        Discount productDiscount = discountsDao.getLastDiscountOfProductInBranch(productItem.getProductID(),branchID);
        Discount parentDiscount  = discountsDao.getLastDiscountOfCategoryInBranch(parentCategory.getCategoryID(),branchID);
        Discount subDiscount  = discountsDao.getLastDiscountOfCategoryInBranch(subCategory.getCategoryID(),branchID);
        Discount subSubDiscount  = discountsDao.getLastDiscountOfCategoryInBranch(subSubCategory.getCategoryID(),branchID);
        double priceBeforeDiscounts = item.getPriceInBranch();
        //Prices After the discount !!
        double currPriceProduct = 0;
        double currPriceCategoryParent = 0;
        double currPriceCategorySub = 0;
        double currPriceCategorySubSub = 0;
        double minPrice = Integer.MAX_VALUE;
        // calc price after each discount :
        if (productDiscount != null && discountsDao.checkValidDiscount(productDiscount.getDiscountID()))
        {
            double amount = productDiscount.getAmount();
            currPriceProduct = priceBeforeDiscounts -priceBeforeDiscounts*(amount/100);
            if (currPriceProduct != 0 && currPriceProduct < minPrice) {
                minPrice = currPriceProduct;
            }
        }
        if (parentDiscount != null && discountsDao.checkValidDiscount(parentDiscount.getDiscountID()))
        {
            double amount = parentDiscount.getAmount();
            currPriceCategoryParent = priceBeforeDiscounts -priceBeforeDiscounts*(amount/100);
            if (currPriceCategoryParent != 0 && currPriceCategoryParent < minPrice) {
                minPrice = currPriceCategoryParent;
            }
        }
        if (subDiscount != null && discountsDao.checkValidDiscount(subDiscount.getDiscountID()))
        {
            double amount = subDiscount.getAmount();
            currPriceCategorySub = priceBeforeDiscounts -priceBeforeDiscounts*(amount/100);
            if (currPriceCategorySub != 0 && currPriceCategorySub < minPrice) {
                minPrice = currPriceCategorySub;
            }
        }
        if (subSubDiscount != null && discountsDao.checkValidDiscount(subSubDiscount.getDiscountID()))
        {
            double amount = subSubDiscount.getAmount();
            currPriceCategorySubSub = priceBeforeDiscounts -priceBeforeDiscounts*(amount/100);
            if (currPriceCategorySubSub != 0 && currPriceCategorySubSub < minPrice) {
                minPrice = currPriceCategorySubSub;
            }
        }
        item = itemsDao.updateItemPriceAfterDiscount(item.getItemID(),minPrice);
        item.setPriceAfterDiscount(minPrice);
        return item;
    }

}














