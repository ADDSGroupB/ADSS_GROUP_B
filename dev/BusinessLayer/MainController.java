package BusinessLayer;

import DataAccess.*;

import java.sql.SQLException;
import java.util.*;

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

    public MainController() {
        this.branchController = new BranchController();
        this.categoryController = new CategoryController(this);
        this.productController = new ProductController(this);
        try {
            this.branchesDao = new BranchesDaoImpl();
            this.categoryDao = new CategoryDaoImpl();
            this.itemsDao = new ItemsDaoImpl();
            this.productMinAmountDao = new ProductMinAmountDaoImpl();
            this.productsDao = new ProductsDaoImpl();
            this.reportDao = new ReportDaoImpl();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public BranchesDao getBranchesDao() {return branchesDao;}
    public CategoryDao getCategoryDao() {return categoryDao;}
    public ItemsDao getItemsDao() {return itemsDao;}
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

}














