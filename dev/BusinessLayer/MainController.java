package BusinessLayer;

import java.util.*;

public class MainController {
    private Map<Integer, Branch> branchMap;
    private Map<Integer, Category> categoryMap;
    private Map<Integer, Product> productMap;
    private CategoryController categoryController;
    private ProductController productController;
    private BranchController branchController;

    public MainController() {
        this.categoryMap = new HashMap<>();
        this.productMap = new HashMap<>();
        this.branchMap = new HashMap<>();
        this.branchController = new BranchController();
        this.categoryController = new CategoryController(this);
        this.productController = new ProductController(this);

    }
    public BranchController getBranchController() {
        return branchController;
    }

    public Map<Integer, Branch> getBranchMap() {
        return branchMap;
    }

    public ProductController getProductController() {
        return productController;
    }

    public Map<Integer, Product> getProductMap() {
        return productMap;
    }

    public CategoryController getCategoryController() {
        return categoryController;
    }

    public Map<Integer, Category> getCategoryMap() {
        return categoryMap;
    }

    public void updateCategory() {
        if (branchMap.size() == 0) {
            return;
        }
        for (Branch branch : branchMap.values()) {
            for (Category category : this.getCategoryMap().values())
            {
                if (!branch.getCategoryDiscount().containsKey(category)) {
                    ArrayList<Discount> categoryDiscount = new ArrayList<>();
                    branch.getCategoryDiscount().put(category, categoryDiscount);
                }
            }
        }
    }
    public void updateProductInMapsInBranch() {
        if (this.getBranchMap().size() == 0) {
            return;
        }
        for (Branch branch : this.getBranchMap().values()) {
            for (Product p : this.getProductMap().values()) {
                if (!branch.getItemsInStore().containsKey(p)) {
                    Queue<Item> itemQueueStore = new LinkedList<>();
                    branch.getItemsInStore().put(p, itemQueueStore);
                }
                if (!branch.getItemsInStorage().containsKey(p)) {
                    Queue<Item> itemQueueStorage = new LinkedList<>();
                    branch.getItemsInStorage().put(p, itemQueueStorage);
                }
                if (!branch.getSoldItems().containsKey(p)) {
                    Queue<Item> itemQueueSold = new LinkedList<>();
                    branch.getSoldItems().put(p, itemQueueSold);
                }
                if (!branch.getDemagedItems().containsKey(p)) {
                    Queue<Item> itemQueueDamaged = new LinkedList<>();
                    branch.getDemagedItems().put(p, itemQueueDamaged);
                }
                if (!branch.getExpiredItems().containsKey(p)) {
                    Queue<Item> itemQueueExpired = new LinkedList<>();
                    branch.getExpiredItems().put(p, itemQueueExpired);
                }
                if (!branch.getProductDiscount().containsKey(p)) {
                    ArrayList<Discount> productDiscount = new ArrayList<>();
                    branch.getProductDiscount().put(p, productDiscount);
                }

            }
        }
    }
}














