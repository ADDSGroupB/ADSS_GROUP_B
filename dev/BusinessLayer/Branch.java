package BusinessLayer;

import java.time.LocalDate;
import java.util.*;

public class Branch {
    // Static variable to keep track of the last assigned ID
    private final int BranchID;
    private String BranchName;
    private static int MinItemsInShelf = 4;
    private static int MaxItemsInShelf = 10;
    private Map<Product, ArrayList<Discount>> ProductDiscount;
    private Map<Category, ArrayList<Discount>> CategoryDiscount;
    private Map<Product, Queue<Item>> ItemsInStore;
    private Map<Product, Queue<Item>> ItemsInStorage;
    private Map<Product, Queue<Item>> SoldItems;
    private Map<Product, Queue<Item>> ExpiredItems;
    private Map<Product, Queue<Item>> DemagedItems;
    private Map<Product, Integer> MinProductAmount;
    private BranchReportManager branchReportManager;

    public Branch(int branchID,String name) {
        this.BranchID = branchID;
        this.BranchName = name;// get
        this.ProductDiscount = new HashMap<>();// get
        this.CategoryDiscount = new HashMap<>();// get
        this.ItemsInStore = new HashMap<>();// get
        this.ItemsInStorage = new HashMap<>();// get
        this.SoldItems = new HashMap<>();// get
        this.ExpiredItems = new HashMap<>();// get + set
        this.DemagedItems = new HashMap<>();// get + set
        this.MinProductAmount = new HashMap<>();// get
        this.branchReportManager = new BranchReportManager();// get
    }
    ///////////////// getters ////////////////////////
    public int getBranchID() {
        return BranchID;
    }
    public String getBranchName() {
        return BranchName;
    }
    public Map<Product, ArrayList<Discount>> getProductDiscount() {return ProductDiscount;}
    public Map<Category, ArrayList<Discount>> getCategoryDiscount() {return CategoryDiscount;}
    public Map<Product, Queue<Item>> getItemsInStore() {return ItemsInStore;}
    public Map<Product, Queue<Item>> getItemsInStorage() {return ItemsInStorage;}
    public Map<Product, Queue<Item>> getSoldItems() {return SoldItems;}
    public Map<Product, Queue<Item>> getExpiredItems() {return ExpiredItems;}
    public Map<Product, Queue<Item>> getDemagedItems() {return DemagedItems;}
    public Map<Product, Integer> getMinProductAmount() {return MinProductAmount;}
    public BranchReportManager getBranchReportManager() {
        return branchReportManager;
    }

    /////////////////////setters/////////////////////////////////////////////
    public void setDemagedItems(Map<Product, Queue<Item>> demagedItems) {DemagedItems = demagedItems;}
    public void setExpiredItems(Map<Product, Queue<Item>> expiredItems) {ExpiredItems = expiredItems;}

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void addItemToMapOfItems(Map<Product, Queue<Item>> map, Product product, Item item) {// function that add item to the maps :ItemsInStore,ItemsInStorage,SoldItems,ExpiredItems ,DemagedItems
        if (map == null || product == null || item == null) {
            return;
        }
        if (map.containsKey(product)) {
            Queue<Item> itemQueue = map.get(product);
            itemQueue.add(item);
        } else {
            Queue<Item> queue = new LinkedList<>();
            queue.add(item);
            map.put(product, queue);
        }
//        UpdateProductTotalAmount();
    }
    public void addProductDiscountMap(Product product, Discount discount) { // function that add discount to the map ProductDiscount
        if (this.ProductDiscount.containsKey(product)) {
            this.ProductDiscount.get(product).add(discount);
        } else {
            ArrayList<Discount> discountList = new ArrayList<>();
            discountList.add(discount);
            this.ProductDiscount.put(product, discountList);
        }
    }
    public void addCategoryDiscountMap(Category category, Discount discount) { // function that add discount to the map ProductDiscount
        if (this.CategoryDiscount.containsKey(category)) {
            this.CategoryDiscount.get(category).add(discount);
        } else {
            ArrayList<Discount> discountList = new ArrayList<>();
            discountList.add(discount);
            this.CategoryDiscount.put(category, discountList);
        }
    }
    /////////////////////////// Prints ///////////////////////////////////////////////////////
    // function that print the maps :ItemsInStore,ItemsInStorage,SoldItems,ExpiredItems ,DemagedItems, DemagedItems
    public void printMapWithItems(Map<Product, Queue<Item>> map) {
        for (Map.Entry<Product, Queue<Item>> entry : map.entrySet()) {
            System.out.println("Product Name : " + entry.getKey().getProductName() + " ProductID: " + entry.getKey().getProductID());
            System.out.println("Items: " + "\n" + entry.getValue());
        }
    }
    public void printMapProductDiscount() {// function that print the map: ProductDiscount
        for (Map.Entry<Product, ArrayList<Discount>> entry : this.ProductDiscount.entrySet()) {
            System.out.println("Product Name : " + entry.getKey().getProductName() + " ProductID: " + entry.getKey().getProductID());
            System.out.println("Discounts: " + "\n" + entry.getValue());
        }
    }
    public void printMapCategoryDiscount() { // function that print the map: CategoryDiscount
        for (Map.Entry<Category, ArrayList<Discount>> entry : this.CategoryDiscount.entrySet()) {
            System.out.println("Category Name : " + entry.getKey().getCategoryName() + " CategoryID: " + entry.getKey().getCategoryName());
            System.out.println("Discounts: " + "\n" + entry.getValue());
        }
    }
    // function that print MinProductAmountMap
    public void printMinProductAmountMap() {
        for (Map.Entry<Product, Integer> entry : this.MinProductAmount.entrySet()) {
            System.out.println("Product Name : " + entry.getKey().getProductName() + " ProductID: " + entry.getKey().getProductID() + ", MinAmount: " + entry.getValue());
        }
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void updateMinProductAmount(Product product, int amount) {// function that add or update min amount for product
        if (this.MinProductAmount.containsKey(product)) { // check if id exists in map
            this.MinProductAmount.put(product, amount); // update value for existing id
        } else {
            this.MinProductAmount.put(product, amount); // add new key-value pair to map
        }
    }
    public boolean checkProductAmountForAlert(Product product) {
        int numItemsInStore = 0;
        int numItemsInStorage = 0;
        int minAmount = 0;
        if (this.getItemsInStore().containsKey(product)) {
            numItemsInStore = this.getItemsInStore().get(product).size(); // sum the items of product in store
        }
        if (this.getItemsInStorage().containsKey(product)) {
            numItemsInStorage = this.getItemsInStorage().get(product).size();// sum the items of product in storage
        }
        if (this.getMinProductAmount().containsKey(product)) {
            minAmount = this.getMinProductAmount().get(product); // sum total items in branch
        }
        // check if we need to pop up alert
        return (numItemsInStorage + numItemsInStore) <= minAmount;
    }
    public void afterCheckAlertCall(ArrayList<Product> productArray) {//function that gest productID and check if we need to make alert to order from supplier
        ArrayList<Product> currArray = new ArrayList<>();
        for (Product product : productArray) {
            if (checkProductAmountForAlert(product)) {
                currArray.add(product);
            }
        }
        if (currArray.size() > 0) {
            PopUpAlert alert = new PopUpAlert();
            alert.showPopupWindow(currArray); // pop up alert
        }
    }
    public void checkProductAmountInShelfToMoveItems(Product product) {  //function that gest productID and check if we need to move items from storage to store
        if (this.getItemsInStore().containsKey(product)) { // check if id exists in map
            Queue<Item> itemQueue = this.getItemsInStore().get(product); // get the corresponding queue
            if (itemQueue.size() < MinItemsInShelf) // check if we need to move items
            {
                fromStorageToStore(product);
            }
        }
    }
    public void sellItem(Product product) {// function that get item that sold , remove the item from ItemsInStore add the item to SoldItems
        ArrayList<Product> productArray = new ArrayList<>();
        if (this.getItemsInStore().get(product).size() + this.getItemsInStorage().get(product).size() == 0) {
            return;
        }
        if (!this.getSoldItems().containsKey(product)) {
            Queue<Item> itemQueue = new LinkedList<>();
            this.getSoldItems().put(product, itemQueue);
        }
        this.getSoldItems().get(product).add(this.getItemsInStore().get(product).poll());
        checkProductAmountInShelfToMoveItems(product);// check if we need to move items from storage to store
        if (checkProductAmountForAlert(product))// check if we need to Pop up alert for the product
        {
            productArray.add(product);
            afterCheckAlertCall(productArray);
        }
    }
    public void fromStorageToStore(Product product) {  // function that get product id and move items of the product from storage to store
        Queue<Item> inStore = ItemsInStore.get(product);
        Queue<Item> inStorage = ItemsInStorage.get(product);
        while (!inStorage.isEmpty()) {
            Item item = inStorage.poll();
            inStore.add(item);
            if (inStore.size() == MaxItemsInShelf) {
                break;
            }
        }
        ItemsInStore.put(product, inStore);
        ItemsInStorage.put(product, inStorage);
    }
    // function that runn all over the items in store and in storage and checks if they have expired
    public ArrayList<Product> checkExpiredItems() {
        ArrayList<Product> productIDArray = new ArrayList<>();
        // Iterate over the entries in ItemsInStorage Map
        for (Map.Entry<Product, Queue<Item>> entry : ItemsInStorage.entrySet()) {
            int counter = 0;
            // Iterate over the items in the Queue
            Iterator<Item> iter = entry.getValue().iterator();
            while (iter.hasNext()) {
                Item item = iter.next();
                if (item.getExpiredDate() != null && item.getExpiredDate().isBefore(LocalDate.now())) {
                    iter.remove();
                    addItemToMapOfItems(ExpiredItems, item.getProduct(), item);
                    item.setStatusType(StatusEnum.Expired);
                    item.setDefectiveDiscription("Expired");

                    counter++;
                }
            }
            if (counter != 0) {
                productIDArray.add(entry.getKey());
            }
        }
        // Iterate over the entries in ItemsInStore Map
        for (Map.Entry<Product, Queue<Item>> entry : ItemsInStore.entrySet()) {
            int counter = 0;
            // Iterate over the items in the Queue
            Iterator<Item> iter = entry.getValue().iterator();
            while (iter.hasNext()) {
                Item item = iter.next();
                if (item.getExpiredDate() != null && item.getExpiredDate().isBefore(LocalDate.now())) {
                    iter.remove();
                    addItemToMapOfItems(ExpiredItems, item.getProduct(), item);
                    item.setStatusType(StatusEnum.Expired);
                    item.setDefectiveDiscription("Expired");
                    counter++;
                }
            }
            if (counter != 0 && !(productIDArray.contains((entry.getKey())))) {
                productIDArray.add(entry.getKey());
            }
        }
        // Iterate over the entries in ItemsInStorage Map and check if we need to move items from storage to store
        for (Product product : ItemsInStore.keySet()) {
            checkProductAmountInShelfToMoveItems(product);
        }
        return productIDArray;
    }
    public void updateDamagedItem(Product product, int itemInput, String damagedDiscription) {
        for (Item item : ItemsInStore.get(product)) {
            if (item.getItemID() == itemInput) {
                item.setStatusType(StatusEnum.Damaged);
                item.setDefectiveDiscription(damagedDiscription);
                break;
            }
        }
        for (Item item : ItemsInStorage.get(product)) {
            if (item.getItemID() == itemInput) {
                item.setStatusType(StatusEnum.Damaged);
                item.setDefectiveDiscription(damagedDiscription);
                break;
            }
        }
    }
    // function that runn all over the items in store and in storage and checks if they have damaged
    public ArrayList<Product> CheckDamagedItems() {
        ArrayList<Product> productIDArray = new ArrayList<>();
        int counter = 0;
        for (Map.Entry<Product, Queue<Item>> entry : ItemsInStorage.entrySet()) {
            // Iterate over the items in the Queue
            Iterator<Item> iter = entry.getValue().iterator();
            while (iter.hasNext()) {
                Item item = iter.next();
                if (item.getStatusType() == StatusEnum.Damaged) {
                    iter.remove();
                    addItemToMapOfItems(DemagedItems, item.getProduct(), item);
                    counter++;
                }
            }
            if (counter != 0) {
                productIDArray.add(entry.getKey());
            }
        }
        for (Map.Entry<Product, Queue<Item>> entry : ItemsInStore.entrySet()) {
            // Iterate over the items in the Queue
            Iterator<Item> iter = entry.getValue().iterator();
            while (iter.hasNext()) {
                Item item = iter.next();
                if (item.getStatusType() == StatusEnum.Damaged) {
                    iter.remove();
                    addItemToMapOfItems(DemagedItems, item.getProduct(), item);
                    counter++;
                }
            }
            if (counter != 0 && !(productIDArray.contains((entry.getKey())))) {
                productIDArray.add(entry.getKey());
            }
        }
        // Iterate over the entries in ItemsInStorage Map and check if we need to move items from storage to store
        for (Product product : ItemsInStore.keySet()) {
            checkProductAmountInShelfToMoveItems(product);
        }
        return productIDArray;
    }

//    public void fromSupplierToStorage(Product product, int quantity, int branchID, LocalDate arrivalDate, LocalDate Expireddate, double pricefromsupplier, double priceinbranch, int supplierID) {
//        for (int i = 0; i < quantity; i++) {
//            Item item;
//            if (Expireddate == null) {
//                item = new Item(branchID, pricefromsupplier, arrivalDate, priceinbranch, supplierID, product);
//            } else {
//                item = new Item(branchID, Expireddate, arrivalDate, pricefromsupplier, priceinbranch, supplierID, product);
//            }
//            addItemToMapOfItems(ItemsInStorage, product, item);
//        }
//        checkProductAmountInShelfToMoveItems(product);
//    }
}
