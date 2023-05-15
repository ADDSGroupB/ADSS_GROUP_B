package InterfaceLayer.InventoryInterfaceLayer;

import BusinessLayer.InventoryBusinessLayer.*;
import DataAccessLayer.InventoryDataAccessLayer.*;
import InterfaceLayer.SupplierInterfaceLayer.SupplierCLI;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

public class Cli {
    private MainController mainController;
    private SupplierCLI supplierCLI;
    public Cli() {
        mainController = new MainController();
        supplierCLI = new SupplierCLI();
    }
    public MainController getMainController() {return this.mainController;}
    public void Start() throws SQLException
    {
        Scanner startScanner = new Scanner(System.in);
        int startChoice = 0;
        while (startChoice != 3) {
            System.out.println("Start Menu - Please choose one of the following options : ");
            System.out.println("1. Start with loaded data ");
            System.out.println("2. Start without loaded data ");
            System.out.println("3. Exit ");
            try {startChoice = startScanner.nextInt();}
            catch (Exception e) {
                System.out.println("Please enter an integer between 1-3 ");
                startScanner.nextLine();
                continue;}
            switch (startChoice)
            {
                case 1:{
                    System.out.println("Initializing the information in the system... ");
                    LoadDataInventory(this.getMainController());
                    mainController.getItemsDao().checkExpiredItemsInAllBranches();
                    supplierCLI.loadDataSupplier();
                    MainMenuUI();
                    break;
                }
                case 2:{
                    System.out.println("Initializes the system without information... ");
                    MainMenuUI();
                    break;
                }
                case 3:{System.out.println("Exiting from the system");break;}
                default: {System.out.println("Invalid choice, please try again");break;}
            }
        }
    }
    public void SuppliersUI()throws SQLException
    {

        supplierCLI.start();
    }
    public void MainMenuUI()throws SQLException {

        //TODO : Receiving an order from supplier -- > add here the function
        Scanner mainMenuScanner = new Scanner(System.in);
        int mainMenuChoice = 0;
        while (mainMenuChoice != 3) {
            System.out.println("Main Menu - Please choose one of the following options : ");
            System.out.println("1. Inventory Menu ");
            System.out.println("2. Suppliers Menu ");
            System.out.println("3. Exit system");
            try {mainMenuChoice = mainMenuScanner.nextInt();}
            catch (Exception e) {
                System.out.println("Please enter an integer between 1-3 ");
                mainMenuScanner.nextLine();
                continue;}
            switch (mainMenuChoice)
            {
                case 1:{InventoryUI();break;}
                case 2:{SuppliersUI();break;}
                case 3:{System.out.println("Exiting from the system");break;}
                default:{System.out.println("Invalid choice, please try again");break;}
            }
        }
    }
    public void InventoryUI()throws SQLException {
        Scanner InventoryScanner = new Scanner(System.in);
        int InventoryChoice = 0;
        while (InventoryChoice != 5) {
            System.out.println("Inventory Menu - Please choose one of the following options : ");
            System.out.println("1. Creating a new branch ");
            System.out.println("2. Entering the menu of a specific branch ");
            System.out.println("3. Entering the product menu");
            System.out.println("4. Entering the category menu");
            System.out.println("5. Exit to Main Menu ");
            try {InventoryChoice = InventoryScanner.nextInt();}
            catch (Exception e) {
                System.out.println("Please enter an integer between 1-5 ");
                InventoryScanner.nextLine();
                continue;
            }
            switch (InventoryChoice)
            {
                case 1:{
                    List<Branch> allBranches = mainController.getBranchController().getAllBranchesController();
                    if (allBranches.size() >= 10)
                    {
                        System.out.println("We have reached the limit of branches in the network, you cannot open a new branch.");
                        break;
                    }
                    Scanner newBranchScanner = new Scanner(System.in);
                    System.out.println("What is the name of the branch?");
                    String newBranchName = newBranchScanner.next();
                    Branch newBranch = mainController.getBranchController().createNewBranch(newBranchName);
                    if (newBranch == null)
                    {
                        System.out.println("There is a problem creating a new branch, please try again.");
                        break;
                    }
                    System.out.println("The branch was created successfully ! \n");
                    System.out.println("Below are the details of the newly created branch : \n");
                    System.out.println(newBranch);
                    break;
                }
                case 2:{
                    System.out.println("Now you have to choose the number of the branch you want to work at : ");
                    List<Branch> allBranches =  mainController.getBranchController().getAllBranchesController();
                    if (allBranches.size() == 0) {
                        System.out.println("There are currently no branches in the system, there is an option to create a new branch.");
                        break;
                    }
                    int branchID;
                    while (true) {
                        System.out.println("Which branch would you like to work on (1 - " + allBranches.size() + "):");
                        branchID = HelperFunctions.positiveItegerInsertion();
                        if (branchID < 1 || branchID > allBranches.size()) {
                            System.out.println("Invalid choice, please try again");
                            continue;
                        }
                        break;
                    }
                    Branch chosenBranch = mainController.getBranchController().getBranchID(branchID);
                    BranchUI(chosenBranch);
                    break;
                }
                case 3:{ productUI();break;}
                case 4:{categoryUI();break;}
                case 5:{System.out.println("Exiting to Main menu");break;}
                default:{System.out.println("Invalid choice, please try again");break;}
            }
        }
    }
    public void BranchUI(Branch branch)throws SQLException {
        Scanner branchScanner = new Scanner(System.in);
        int branchChoice = 0;
        while (branchChoice != 11) {
            System.out.println("Branch Menu - Please choose one of the following options : ");
            System.out.println("1. New sale ");
            System.out.println("2. Update damaged item ");
            System.out.println("3. Add new discount for product");
            System.out.println("4. Add new discount for category");
            System.out.println("5. Print all items in store ");
            System.out.println("6. Print all items in storage ");
            System.out.println("7. Print Product sales history ");
            System.out.println("8. Placing an order from supplier "); // what we need to do here
            System.out.println("9. Create Periodic Order "); // ADD ITEMS
            System.out.println("10. Report Manager ");
            System.out.println("11. Exit to Inventory Menu ");
            try {
                branchChoice = branchScanner.nextInt();
            } catch (Exception e) {
                System.out.println("Please enter an integer between 1-11 ");
                branchScanner.nextLine();
                continue;
            }
            switch (branchChoice) {
                case 1: {
                    System.out.println("Add Products to the Sale : ");
                    Scanner productSaleScanner = new Scanner(System.in);

                    List<Item> itemsInSale = new ArrayList<>();
                    int productID = 0;
                    while (productID != -1) {
                        System.out.print("Enter the product ID (or -1 to finish): ");
                        try {
                            productID = productSaleScanner.nextInt();
                        } catch (Exception e) {
                            System.out.println("Please enter an integer");
                            productSaleScanner.nextLine(); // clear input buffer
                            continue;
                        }
                        if (productID == -1) {
                            break;
                        }
                        Product productToSell = mainController.getProductController().getProduct(productID);
                        if (productToSell == null) {
                            System.out.println("Unknown product ID. Please try again");
                            productSaleScanner.nextLine(); // clear input buffer
                            continue;
                        }
                        List<Item> itemInStore = mainController.getItemsDao().getAllStoreItemsByBranchIDAndProductID(branch.getBranchID(),productToSell.getProductID());
                        List<Item> itemInStorage = mainController.getItemsDao().getAllStorageItemsByBranchIDAndProductID(branch.getBranchID(),productToSell.getProductID());
                        if (itemInStore.size() == 0 && itemInStorage.size() == 0)
                        {
                            System.out.println("At the moment we are unable to make a sale due to the lack of all the products in the store. ");
                            break;
                        }
                        Item itemToSale = mainController.getItemsDao().getItemForSale(productID, branch.getBranchID());
                        if (itemToSale == null)
                        {
                            System.out.println("We currently don't have items from product you want. Please try again");
                            productSaleScanner.nextLine(); // clear input buffer
                            continue;
                        }
                        itemToSale = mainController.getItemsDao().updateItemStatus(itemToSale.getItemID(),"Sold");
                        itemsInSale.add(itemToSale);
                    }
                    if (itemsInSale.size() == 0 )
                    {
                        System.out.println("No products were added during the purchase.....");
                        break;
                    }
                    System.out.println("Receipt after purchase :");
                    for (Item itemToCheckPrice : itemsInSale)
                    {
                        itemToCheckPrice = mainController.PriceCalculationAfterDiscount(itemToCheckPrice, branch.getBranchID());
                        System.out.println("Product Name : " +itemToCheckPrice.getProduct().getProductName() + ", Price before Discount : " + itemToCheckPrice.getPriceInBranch() + ", Price after Discount : " + itemToCheckPrice.getPriceAfterDiscount());
                    }
                    mainController.getItemsDao().fromStorageToStore(branch);
                    break;
                }
                case 2: {
                    System.out.println("What is the id of the product you would like to report as defective ? ");
                    int productID = HelperFunctions.positiveItegerInsertion();
                    Product productDef = mainController.getProductController().getProduct(productID);
                    if (productDef != null) {
                        System.out.println("What is the id of the item you would like to report as defective ? ");
                        int itemIDDefective = HelperFunctions.positiveItegerInsertion();
                        Item itemDef = mainController.getItemsDao().getItemByID(itemIDDefective);
                        if (itemDef == null)
                        {
                            System.out.println("There is no item with the ID");
                            break;
                        }
                        if (itemDef.getBranchID() != branch.getBranchID())
                        {
                            System.out.println("The item you specified does not belong to this branch");
                            break;
                        }
                        if (itemDef.getStatusType() == StatusEnum.Damaged)
                        {
                            System.out.println("This item has already been reported as defective");
                            break;
                        }
                        if (itemDef.getStatusType() == StatusEnum.Sold)
                        {
                            System.out.println("This item has already been sold and you cannot report it as defective");
                            break;
                        }
                        if (itemDef.getProduct().getProductID() != productDef.getProductID())
                        {
                            System.out.println("There is a mismatch between the product ID and the item ID");
                            break;
                        }
                        System.out.println("Please specify the defect in the item : ");
                        Scanner discriptionScanner = new Scanner(System.in);
                        String discription = discriptionScanner.nextLine();
                        itemDef = mainController.getItemsDao().updateItemStatus(itemIDDefective,"Damaged");
                        itemDef = mainController.getItemsDao().updateItemDefectiveDescription(itemDef.getItemID(),discription);
                        System.out.println("The items have been successfully update");
                        System.out.println(itemDef + "\n");
                        break;
                    }
                    System.out.println("The ID of the item you specified does not exist in the system");
                    break;
                }
                case 3: { // DISCOUNT PRODUCT
                    System.out.println("What is the id of the product you would like to add to him discount ? ");
                    int productID = HelperFunctions.positiveItegerInsertion();
                    Product productDiscount = mainController.getProductController().getProduct(productID);
                    if (productDiscount == null)
                    {
                        System.out.println("The ID of the product you specified does not exist in the system");
                        break;
                    }
                    boolean isValid = false;
                    LocalDate date1;
                    LocalDate date2;
                    do {
                        System.out.println("Enter the discount start date(YYYY-MM-DD): ");
                        date1 = validDate();
                        System.out.println("Enter the discount end date(YYYY-MM-DD): ");
                        date2 = validDate();
                        if (date1.isAfter(date2))
                        {
                            System.out.print("The start date of the discount must be before the end date of the discount please try again : " + "\n");
                        }
                        else {isValid=true;}
                    } while (!isValid);
                    System.out.println("What percentage of discount is there? ");
                    double percentage = HelperFunctions.positiveDoubleInsertion();
                    Discount newDiscount = mainController.getDiscountsDao().addNewDiscount(branch.getBranchID(), date1,date2,percentage,null,productDiscount);
                    System.out.println("The discount have been successfully added \n");
                    System.out.println("Below are the details of the newly created discount : \n");
                    System.out.println(newDiscount);
                    break;
                }
                case 4: { // DISCOUNT CATEGORY
                    System.out.println("What is the id of the category you would like to add to him discount ? ");
                    int categoryID = HelperFunctions.positiveItegerInsertion();
                    Category categoryDiscount = mainController.getCategoryController().getCategory(categoryID);
                    if (categoryDiscount == null)
                    {
                        System.out.println("The ID of the category you specified does not exist in the system");
                        break;
                    }
                    boolean isValid = false;
                    LocalDate date1;
                    LocalDate date2;
                    do {
                        System.out.println("Enter the discount start date(YYYY-MM-DD): ");
                        date1 = validDate();
                        System.out.println("Enter the discount end date(YYYY-MM-DD): ");
                        date2 = validDate();
                        if (date1.isAfter(date2))
                        {
                            System.out.print("The start date of the discount must be before the end date of the discount please try again : " + "\n");
                        }
                        else {isValid=true;}
                    } while (!isValid);
                    System.out.println("What percentage of discount is there? ");
                    double percentage = HelperFunctions.positiveDoubleInsertion();
                    Discount newDiscount = mainController.getDiscountsDao().addNewDiscount(branch.getBranchID(), date1,date2,percentage,categoryDiscount,null);
                    System.out.println("The discount have been successfully added \n");
                    System.out.println("Below are the details of the newly created discount : \n");
                    System.out.println(newDiscount);
                    break;
                }
                case 5: {
                    List<Item> storeItems = mainController.getItemsDao().getAllStoreItemsByBranchID(branch.getBranchID());
                    if (storeItems.size()==0)
                    {
                        System.out.println("We currently have no items in the store");
                        break;
                    }
                    System.out.println("Branch Name : " +branch.getBranchName() + ", Branch ID : " + branch.getBranchID() + "\n");
                    System.out.println(" **Store Items** \n");
                    for (Item item : storeItems)
                    {
                        System.out.println(item);
                        System.out.println("------------------");
                    }
                    System.out.println("\n");
                    break;
                }
                case 6: {
                    List<Item> storageItems = mainController.getItemsDao().getAllStorageItemsByBranchID(branch.getBranchID());
                    if (storageItems.size()==0)
                    {
                        System.out.println("We currently have no items in the storage");
                        break;
                    }
                    System.out.println("Branch Name : " +branch.getBranchName() + ", Branch ID : " + branch.getBranchID() + "\n");
                    System.out.println(" **Storage Items** \n");
                    for (Item item : storageItems)
                    {
                        System.out.println(item);
                        System.out.println("------------------");
                    }
                    System.out.println("\n");
                    break;
                }
                case 7: {
                    System.out.println("What is the id of the product you would like to get his sold history ? ");
                    int productID = HelperFunctions.positiveItegerInsertion();
                    Product productSoldHistory = mainController.getProductController().getProduct(productID);
                    if (productSoldHistory != null) {
                        List<Item> soldItems = mainController.getItemsDao().getAllSoldItemByBranchID(branch.getBranchID());
                        if (soldItems.size() == 0) {
                            System.out.println("We currently have no items in the sold history of the product");
                            break;
                        }
                        System.out.println(" **Sold Items History for the product : "+ productID + " In the branch : "+ branch.getBranchID() +" **"+ "\n");
                        for (Item item : soldItems) {
                            if (item.getProduct().getProductID() == productID && item.getBranchID() == branch.getBranchID()) {
                                System.out.println(item);
                                System.out.println("------------------");
                            }
                        }
                        System.out.println("\n");
                        break;
                    }
                    System.out.println("The ID of the item you specified does not exist in the system");
                    break;
                }
                case 8: { System.out.println("8. Placing an order from supplier ");
                    // TODO : periodic order // הזמנה תקופתית  --> from here we should create a periodic order ?
                    break;
                }
                case 9: {System.out.println("9. Receiving an order from supplier ");
                    //TODO : Use the function From Storage To Store after we receive the order and insert all the items to the db
                    //TODO : Maybe this function wont be on the inventory menu , maybe this will work like the check expired product that we run in the start of the main menu
                    break;
                }
                case 10: {
                    reportUI(branch);
                    break;
                }
                case 11: {System.out.println("Exiting to Inventory menu");break;}
                default: {System.out.println("Invalid choice, please try again");break;}
            }
        }
    }
    public void productUI() throws SQLException {
        Scanner productScanner = new Scanner(System.in);
        int productChoice = 0;
        while (productChoice != 5) {
            System.out.println("Product Menu - Please choose one of the following options : ");
            System.out.println("1. Add new product ");
            System.out.println("2. Get product categories by ID ");
            System.out.println("3. Print product details by ID ");
            System.out.println("4. Print all products");
            System.out.println("5. Exit to Inventory menu");
            try {productChoice = productScanner.nextInt();}
            catch (Exception e) {
                System.out.println("Please enter an integer between 1-5 ");
                productScanner.nextLine();
                continue;
            }
            switch (productChoice) {
                case 1: {
                    System.out.println("What is the name of the new product ? ");
                    Scanner newProductScanner = new Scanner(System.in);
                    String newProductName = newProductScanner.nextLine();
                    System.out.println("What is the manufacturer's name of the new product? ");
                    String manufacturer = newProductScanner.next();
                    if (mainController.getProductsDao().checkNewName(newProductName,manufacturer)) {
                        System.out.println("What is the weight of the new product? (in gr)");
                        double weight = HelperFunctions.positiveDoubleInsertion();
                        System.out.println("What is the parent category ID of the new product? ");
                        int parentInt = HelperFunctions.positiveItegerInsertion();
                        System.out.println("What is the sub category ID of the new product ? ");
                        int subInt = HelperFunctions.positiveItegerInsertion();
                        System.out.println("What is the subSub category ID of the new product ? ");
                        int subSubInt = HelperFunctions.positiveItegerInsertion();
                        if (!(subSubInt != subInt && subSubInt != parentInt && subInt != parentInt)) {
                            System.out.println("The three categories must be different");
                            break;
                        }
                        Category parent = mainController.getCategoryDao().getCategoryByID(parentInt);
                        Category sub = mainController.getCategoryDao().getCategoryByID(subInt);
                        Category subSub = mainController.getCategoryDao().getCategoryByID(subSubInt);
                        if (parent == null || sub == null || subSub == null) {
                            System.out.println("There is some problem importing the categories");
                            break;
                        }
                        Product product = mainController.getProductController().createProduct(newProductName, weight, manufacturer, parent, sub, subSub);
                        if (product != null) {
                            if (mainController.getProductController().newProductToAllBranches(product)) {
                                System.out.println("The product was created successfully \n");
                                System.out.println("Below are the details of the newly created product : \n");
                                System.out.println(product);
                            }
                        }
                    } else {
                        System.out.println("The product name you provided already exists under the manufacturer you provided in the system");
                    }
                    break;
                }
                case 2: {
                    System.out.println("What is the ID of the product for which you would like to get the categories ?  \n");
                    int productID = HelperFunctions.positiveItegerInsertion();
                    Product product = mainController.getProductController().getProduct(productID);
                    if (product == null) {
                        System.out.println("We do not have a product in the system with the ID number you provided \n");
                        break;
                    }
                    System.out.println("The product with the ID : " + productID + " is under the following categories:" + "\n");
                    Category parent = product.getParentCategory();
                    Category sub = product.getSubCategory();
                    Category subSub = product.getSubSubCategory();
                    System.out.println(parent);
                    System.out.println(sub);
                    System.out.println(subSub);
                    break;
                }
                case 3: {
                    System.out.println("What is the ID of the product for which you would like to get his details ?  \n");
                    int productID = HelperFunctions.positiveItegerInsertion();
                    Product product = mainController.getProductController().getProduct(productID);
                    if (product == null) {
                        System.out.println("We do not have a product in the system with the ID number you provided \n");
                        break;
                    }
                    System.out.println(product);
                    break;
                }
                case 4: {
                    List<Product> products = mainController.getProductController().getAllProducts();
                    if (products == null) {
                        System.out.println("We currently have no products in the system \n");
                        break;
                    }
                    System.out.println("The system includes the following products : \n");
                    for (Product product : products) {
                        System.out.println(product);
                    }
                    break;
                }
                case 5: {System.out.println("Exiting to Inventory menu \n");break;}
                default: {System.out.println("Invalid choice, please try again \n");break;}
            }
        }
    }
    public void categoryUI() throws SQLException
    {
        Scanner categoryScanner = new Scanner(System.in);
        int categoryChoice = 0;
        while (categoryChoice != 4) {
            System.out.println("Category Menu - Please choose one of the following options : ");
            System.out.println("1. Add new category ");
            System.out.println("2. Print category details by ID ");
            System.out.println("3. Print all categories");
            System.out.println("4. Exit to Inventory menu");
            try {categoryChoice = categoryScanner.nextInt();}
            catch (Exception e) {
                System.out.println("Please enter an integer between 1-4 ");
                categoryScanner.nextLine();
                continue;
            }
            switch (categoryChoice)
            {
                case 1:{
                    System.out.println("What is the name of the new category ? ");
                    Scanner newCategoryScanner = new Scanner(System.in);
                    String newCategoryName = newCategoryScanner.nextLine();
                    if (mainController.getCategoryDao().checkNewCategoryName(newCategoryName))
                    {
                        Category category = mainController.getCategoryController().createCategory(newCategoryName);
                        if (category != null)
                        {
                            System.out.println("The category has been successfully added \n");
                            System.out.println("Below are the details of the newly created category : \n");
                            System.out.println(category);
                        }
                    }
                    break;
                }
                case 2:{
                    System.out.println("What is the id of the category you are looking for ? ");
                    int categoryID = HelperFunctions.positiveItegerInsertion();
                    Category category = mainController.getCategoryController().getCategory(categoryID);
                    if (category == null) {
                        System.out.println("We do not have a category in the system with the ID number you provided ");
                        break;
                    }
                    List<Product> productsInCategory = mainController.getCategoryController().getProductInCategory(categoryID);
                    if (productsInCategory != null)
                    {
                        category.setProductsToCategory(productsInCategory);
                    }
                    System.out.println(category);
                    break;
                }
                case 3:{
                    List<Category> categories = mainController.getCategoryController().getAllCategories();
                    if (categories == null) {
                        System.out.println("We currently have no products in the system");
                        break;
                    }
                    System.out.println("The system includes the following categories:");
                    for (Category category : categories) {
                        List<Product> productsInCategory = mainController.getCategoryController().getProductInCategory(category.getCategoryID());
                        if (productsInCategory != null) {category.setProductsToCategory(productsInCategory);}
                        System.out.println(category);
                        System.out.println("----------------------");
                    }
                    break;
                }
                case 4:{  System.out.println("Exiting to Inventory menu");break;}
                default:{ System.out.println("Invalid choice, please try again");break;}
            }
        }
    }
    public void reportUI(Branch branch) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        int choice = 0;
        while (choice != 4) {
            System.out.println("What report would you like to work on?");
            System.out.println("1. Missing Products Report");
            System.out.println("2. Defective Items Report");
            System.out.println("3. Weekly Storage Report");
            System.out.println("4. Exit");
            try {
                choice = scanner.nextInt();
            } catch (Exception e) {
                System.out.println("Please enter an integer");
                scanner.nextLine(); // clear input buffer
                continue;
            }
            switch (choice) {
                case 1:{
                    choice = 0;
                    while (choice != 4) {
                        System.out.println("Missing Products Report Menu:");
                        System.out.println("1. Create a new Missing Products Report");
                        System.out.println("2. Add new product to the current Missing Products Report");
                        System.out.println("3. Print the current Missing Products Report");
                        System.out.println("4. Exit");
                        try {
                            choice = scanner.nextInt();
                        } catch (Exception e) {
                            System.out.println("Please enter an integer");
                            scanner.nextLine(); // clear input buffer
                            continue;
                        }
                        switch (choice) {
                            case 1:
                                System.out.println("Creating new Missing Products Report...");
                                int reportID = mainController.getReportDao().getNewReportID();
                                MissingProductsReport report = mainController.getBranchController().getReportController().createNewMissingReport(reportID, branch.getBranchID());
                                branch.getBranchReportManager().addNewReport(report);
                                mainController.getReportDao().addReport(report);
                                continue;
                            case 2:
                                MissingProductsReport report_curr = branch.getBranchReportManager().getCurrentMissingReport();
                                if (report_curr == null){
                                    System.out.println("Missing Items Report has not been created yet");
                                    break;
                                }
                                System.out.println("Adds new product to the current Missing Products Report...");
                                int productID = 0;
                                while (productID != -1) {
                                    System.out.print("Enter the product ID (or -1 to finish): ");
                                    productID = HelperFunctions.positiveItegerInsertionWithCancel();
                                    if (productID == -1){break;}
                                    Product product = mainController.getProductsDao().getProductByID(productID);
                                    if (product != null)
                                    {
                                        System.out.print("Enter the amount to order : ");
                                        int amount = HelperFunctions.positiveItegerInsertion();
                                        report_curr.addMissingProduct(product, amount);
                                        //mainController.getReportDao().addLineToMissingReport(report_curr.getReportID(), productID, amount);
                                    }
                                    else
                                    {
                                        System.out.println("Unknown product ID. Please try again");
                                    }
                                }
                                branch.getBranchReportManager().setCurrentMissingReport(report_curr);
                                System.out.println("The products have been successfully added to the report");
                                break;
                            case 3:
                                if (branch.getBranchReportManager().getCurrentMissingReport() != null) {
                                    System.out.println(branch.getBranchReportManager().getCurrentMissingReport().toString());
                                } else System.out.println("Missing Products Report has not been created yet");
                                break;
                            case 4:
                                System.out.println("Exiting...");
                                break;
                            default:
                                System.out.println("Invalid choice, please try again");
                                break;
                        }
                    }
                    break;
                }
                case 2:{
                    choice = 0;
                    while (choice != 4) {
                        System.out.println("Defective Items Report Menu:");
                        System.out.println("1. Create a new Defective Items Report");
                        System.out.println("2. Updating the damaged items report");
                        System.out.println("3. Print the current Defective Items Report");
                        System.out.println("4. Exit");
                        try {
                            choice = scanner.nextInt();
                        } catch (Exception e) {
                            System.out.println("Please enter an integer");
                            scanner.nextLine(); // clear input buffer
                            continue;
                        }
                        switch (choice) {
                            case 1:
                                System.out.println("Creating new Defective Items Report...");
                                int reportID = mainController.getReportDao().getNewReportID();
                                DefectiveProductsReport report = mainController.getBranchController().getReportController().createNewDefectiveReport(reportID, branch.getBranchID());
                                branch.getBranchReportManager().addNewReport(report);
                                continue;
                            case 2: {
                                DefectiveProductsReport report_curr = branch.getBranchReportManager().getCurrentDefectiveReport();
                                if (report_curr == null){
                                    System.out.println("Defective Items Report has not been created yet");
                                    break;
                                }
                                System.out.println("Updating the damaged items report...");
                                List<Item> defectiveItems = mainController.getItemsDao().getAllDamagedItemsByBranchID(branch.getBranchID());
                                List<Item> expiredItems = mainController.getItemsDao().getAllExpiredItemsByBranchID(branch.getBranchID());
                                if (defectiveItems.size() == 0 && expiredItems.size() == 0) {
                                    System.out.println("We currently have no damaged or expired items to report...");
                                } else {
                                    for (Item item : defectiveItems){
                                        report_curr.addDefectiveItem(item);
                                    }
                                    for (Item item : expiredItems){
                                        report_curr.addDefectiveItem(item);
                                    }
                                    //reprt_curr = mainController.getBranchController().getReportController().updateProductsInReport(reprt_curr, branch);
                                    System.out.println("The update was successful");
                                }
                                branch.getBranchReportManager().setCurrentDefectiveReport(report_curr);
                                mainController.getReportDao().addReport(report_curr);
                                break;
                            }
                            case 3:
                                if (branch.getBranchReportManager().getCurrentDefectiveReport() != null) {
                                    System.out.println(branch.getBranchReportManager().getCurrentDefectiveReport().toString());
                                } else System.out.println("Defective Items Report has not been created yet");
                                break;
                            case 4:
                                System.out.println("Exiting...");
                                break;
                            default:
                                System.out.println("Invalid choice, please try again");
                                break;
                        }
                    }
                    break;}
                case 3:{
                    choice = 0;
                    while (choice != 4) {
                        System.out.println("Weekly Storage Report Menu:");
                        System.out.println("1. Create a new Weekly Storage Report");
                        System.out.println("2. Add new category to the current Weekly Storage Report");
                        System.out.println("3. Print the current Weekly Storage Report");
                        System.out.println("4. Exit");
                        try {
                            choice = scanner.nextInt();
                        } catch (Exception e) {
                            System.out.println("Please enter an integer");
                            scanner.nextLine(); // clear input buffer
                            continue;
                        }
                        switch (choice) {
                            case 1:
                                System.out.println("Creating new Weekly Storages Report...");
                                int reportID = mainController.getReportDao().getNewReportID();
                                WeeklyStorageReport report = mainController.getBranchController().getReportController().createNewWeeklyReport(reportID, branch.getBranchID());
                                branch.getBranchReportManager().addNewReport(report);
                                continue;
                            case 2:
                                System.out.println("Adds new category to the current Weekly Storages Report...");
                                WeeklyStorageReport curr = branch.getBranchReportManager().getCurrentWeeklyReport();
                                if (curr == null){
                                    System.out.println("Weekly Storages Report has not been created yet");
                                    break;
                                }
                                int categoryID = 0;
                                while (categoryID != -1) {
                                    System.out.print("Enter the category ID (or -1 to finish): ");
                                    try {
                                        categoryID = scanner.nextInt();
                                    } catch (Exception e) {
                                        System.out.println("Please enter an integer");
                                        scanner.nextLine(); // clear input buffer
                                        continue;
                                    }
                                    if (categoryID == -1){break;}
                                    Category category = mainController.getCategoryDao().getCategoryByID(categoryID);
                                    if (category == null) {
                                        System.out.println("Unknown category ID. Please try again");
                                        scanner.nextLine(); // clear input buffer
                                        continue;
                                    }
                                    if (curr.getWeeklyReportMap().containsKey(category)){
                                        System.out.println("The category is already in the report");
                                        scanner.nextLine(); // clear input buffer
                                        continue;
                                    }
                                    List<Product> products = mainController.getProductsDao().getAllProductsInCategory(categoryID);
                                    Map<Product, Integer> productCurrAmount = new HashMap<>();
                                    for (Product product : products){
                                        int productAmount = mainController.getItemsDao().getAllStorageItemsByBranchIDAndProductID(branch.getBranchID(), product.getProductID()).size();
                                        productAmount += mainController.getItemsDao().getAllStoreItemsByBranchIDAndProductID(branch.getBranchID(), product.getProductID()).size();
                                        productCurrAmount.put(product, productAmount);
                                        //mainController.getReportDao().addLineToWeeklyReport(curr.getReportID(), categoryID, product.getProductID(), productAmount);
                                    }
                                    curr.addCategoryToReport(category, productCurrAmount);
                                }
                                branch.getBranchReportManager().setCurrentWeeklyReport(curr);
                                mainController.getReportDao().addReport(curr);
                                System.out.println("Adding categories to the report has been successfully completed");
                                break;
                            case 3:
                                if (branch.getBranchReportManager().getCurrentWeeklyReport() != null) {
                                    System.out.println(branch.getBranchReportManager().getCurrentWeeklyReport().toString(branch));
                                } else System.out.println("Weekly Storage Report has not been created yet");
                                break;
                            case 4:
                                System.out.println("Exiting...");
                                break;
                            default:
                                System.out.println("Invalid choice, please try again");
                                break;
                        }
                    }
                    break;
                }
            }
        }
    }
    public LocalDate validDate() {
        LocalDate date = null;
        Scanner scannerValidDate = new Scanner(System.in);
        String dateString;
        boolean isValid = false;
        do {
            dateString = scannerValidDate.nextLine();
            if (!dateString.matches("\\d{4}-\\d{2}-\\d{2}")) {
                System.out.println("Date is not in the correct format. Please enter a date in the format of YYYY-MM-DD.");
            } else {
                try {
                    date = LocalDate.parse(dateString);
                    isValid = true;
                } catch (DateTimeParseException e) {
                    System.out.println("Invalid date. Please enter a date in the format of YYYY-MM-DD and also MM is a number between 1 and 12 DD is a number between 1 and 30 .");
                }
            }
        } while (!isValid);
        return date;
    }
    public void LoadDataInventory(MainController mainController) throws SQLException
    {
        ProductsDao productsDao = mainController.getProductsDao();
        ItemsDao itemsDao = mainController.getItemsDao();
        BranchesDao branchesDao = mainController.getBranchesDao();
        CategoryDao categoryDao = mainController.getCategoryDao();
        DiscountsDao discountsDao = mainController.getDiscountsDao();
        ProductMinAmountDao productMinAmountDao = mainController.getProductMinAmountDao();
// Data Base From Nothing
//==================================================
//Branches
        Branch b1 = branchesDao.addBranch("SuperLi Beer Sheva");
        Branch b2 = branchesDao.addBranch("SuperLi Tel Aviv");
        Branch b3 = branchesDao.addBranch("SuperLi Jerusalem");
        Branch b4 = branchesDao.addBranch("SuperLi Herzliya");
        Branch b5 = branchesDao.addBranch("SuperLi Eilat");
// Categories
        Category c1 =categoryDao.addCategory("Dairy products");
        Category c2 =categoryDao.addCategory("Milk");
        Category c3 =categoryDao.addCategory("Cottage");
        Category c4 =categoryDao.addCategory("Cream Cheese");
        Category c5 =categoryDao.addCategory("Yellow Cheese");
        Category c6 =categoryDao.addCategory("1% fat");
        Category c7 =categoryDao.addCategory("3% fat");
        Category c8 =categoryDao.addCategory("5% fat");
        Category c9 =categoryDao.addCategory("9% fat");
        Category c10 =categoryDao.addCategory("Vegetables");
        Category c11 =categoryDao.addCategory("Onions");
        Category c12 =categoryDao.addCategory("Potatoes");
        Category c13 =categoryDao.addCategory("Green Onions");
        Category c14 =categoryDao.addCategory("White Onions");
        Category c15 =categoryDao.addCategory("Red Potatoes");
        Category c16 =categoryDao.addCategory("Fruits");
        Category c17 =categoryDao.addCategory("Apples");
        Category c18 =categoryDao.addCategory("Red Apples");
        Category c19 =categoryDao.addCategory("Green Apples");
        Category c20 =categoryDao.addCategory("Citrus Fruits");
        Category c21 =categoryDao.addCategory("Oranges");
        Category c22 =categoryDao.addCategory("Sweet Drinks");
        Category c23 =categoryDao.addCategory("0.5 Liters");
        Category c24 =categoryDao.addCategory("Sodas");
        Category c25 =categoryDao.addCategory("1 Liters");
        Category c26 =categoryDao.addCategory("1.5 Liters");
        Category c27 =categoryDao.addCategory("Soft Drinks");

// Products
        Product p1 = productsDao.addProduct("Milk 3%", "Tara", 500, 1, 2, 7);
        productMinAmountDao.addNewProductToAllBranches(1);
        Product p2 = productsDao.addProduct("Cottage 5%", "Tnova", 250, 1, 3, 8);
        productMinAmountDao.addNewProductToAllBranches(2);
        Product p3 = productsDao.addProduct("White Onion", "VegAndFruits", 20, 10, 11, 14);
        productMinAmountDao.addNewProductToAllBranches(3);
        Product p4 = productsDao.addProduct("Green Onion", "VegAndFruits", 10, 10, 11, 13);
        productMinAmountDao.addNewProductToAllBranches(4);
        Product p5 = productsDao.addProduct("Red Potato", "VegAndFruits", 10, 10, 12, 15);
        productMinAmountDao.addNewProductToAllBranches(5);
        Product p6 = productsDao.addProduct("Red Apple", "VegAndFruits", 10, 16, 17, 18);
        productMinAmountDao.addNewProductToAllBranches(6);
        Product p7 = productsDao.addProduct("Green Apple", "VegAndFruits", 10, 16, 17, 18);
        productMinAmountDao.addNewProductToAllBranches(7);
        Product p8 = productsDao.addProduct("Cottage 9%", "Tara", 250, 1, 3, 9);
        productMinAmountDao.addNewProductToAllBranches(8);
        Product p9 =  productsDao.addProduct("Milk 9%", "Tnova", 500, 1, 2, 9);
        productMinAmountDao.addNewProductToAllBranches(9);
        Product p10 = productsDao.addProduct("Milk 1%", "Tnova", 500, 1, 2, 6);
        productMinAmountDao.addNewProductToAllBranches(10);
        Product p11 = productsDao.addProduct("Milk 5%", "Tnova", 500, 1, 2, 8);
        productMinAmountDao.addNewProductToAllBranches(11);
        Product p12 = productsDao.addProduct("Cottage 3%", "Tara", 250, 1, 3, 7);
        productMinAmountDao.addNewProductToAllBranches(12);
        Product p13 = productsDao.addProduct("Cottage 1%", "Tara", 250, 1, 3, 6);
        productMinAmountDao.addNewProductToAllBranches(13);
        Product p14 = productsDao.addProduct("Cream Cheese 3%", "Tnova", 350, 1, 4, 7);
        productMinAmountDao.addNewProductToAllBranches(14);
        Product p15 = productsDao.addProduct("Cream Cheese 1%", "Tnova", 350, 1, 4, 6);
        productMinAmountDao.addNewProductToAllBranches(15);
        Product p16 = productsDao.addProduct("Cream Cheese 5%", "Tnova", 350, 1, 4, 8);
        productMinAmountDao.addNewProductToAllBranches(16);
        Product p17 = productsDao.addProduct("Milk 3%", "Tnova", 500, 1, 2, 7);
        productMinAmountDao.addNewProductToAllBranches(17);
        Product p18 = productsDao.addProduct("Coca Cola Zero 0.5 Liter", "CocaCola", 500, 22, 24, 23);
        productMinAmountDao.addNewProductToAllBranches(18);
        Product p19 = productsDao.addProduct("Coca Cola Zero 1 Liter", "CocaCola", 1000, 22, 24, 25);
        productMinAmountDao.addNewProductToAllBranches(19);
        Product p20 = productsDao.addProduct("Coca Cola Zero 1.5 Liter", "CocaCola", 1500, 22, 24, 26);
        productMinAmountDao.addNewProductToAllBranches(20);
        Product p21 = productsDao.addProduct("Banana And Strawberry 1 Liter", "Spring", 1000, 22, 27, 25);
        productMinAmountDao.addNewProductToAllBranches(21);
        Product p22 = productsDao.addProduct("Orange juice 1 Liter", "Spring", 1000, 22, 27, 25);
        productMinAmountDao.addNewProductToAllBranches(22);

// Product Min Amount Table
        for (int j=1;j<6;j++)
        {
            for (int i = 1; i < 23; i++)
            {
                productMinAmountDao.UpdateMinAmountToProductInBranch(i,j,30);
            }
        }
//Dates
        LocalDate date1 = LocalDate.of(2023, 5, 26);
        LocalDate date2 = LocalDate.of(2023, 6, 25);
        LocalDate date3 = LocalDate.of(2023, 7, 30);
        LocalDate date4 = LocalDate.of(2023, 8, 12);
        LocalDate date5 = LocalDate.of(2023, 9, 1);
        LocalDate date6 = LocalDate.of(2023, 10, 22);
        LocalDate date7 = LocalDate.of(2023, 11, 17);
        LocalDate date8 = LocalDate.of(2023, 12, 4);
        LocalDate date9 = LocalDate.of(2023, 1, 31);   //"Expired"
        LocalDate date10 = LocalDate.of(2023, 2, 28);  //"Expired"
        LocalDate date11 = LocalDate.of(2023, 3, 15);  //"Expired"
        LocalDate date12 = LocalDate.of(2023, 4, 8);   //"Expired"
        LocalDate date13 = LocalDate.of(2023, 5, 5);   //"Expired"
        LocalDate date14 = LocalDate.of(2023, 6, 19);
        LocalDate date15 = LocalDate.of(2023, 7, 23);
        LocalDate date16 = LocalDate.of(2023, 8, 10);
        LocalDate date17 = LocalDate.of(2023, 9, 2);
        LocalDate date18 = LocalDate.of(2023, 10, 16);
        LocalDate date19 = LocalDate.of(2023, 11, 21);
        LocalDate date20 = LocalDate.of(2023, 5, 13);
//Discounts

// discounts on p1 for all branches
        Discount d1 = discountsDao.addNewDiscount(1,date9, date12, 15, null,p1);
        Discount d2 = discountsDao.addNewDiscount(2,date9, date12, 15, null,p1);
        Discount d3 = discountsDao.addNewDiscount(3,date9, date12, 15, null,p1);
        Discount d4 = discountsDao.addNewDiscount(4,date9, date12, 15, null,p1);
        Discount d5 = discountsDao.addNewDiscount(5,date9, date12, 15, null,p1);
// discounts on p1 for all branches
        Discount d6 = discountsDao.addNewDiscount(1,date10, date3, 15, null,p1);
        Discount d7 = discountsDao.addNewDiscount(2,date10, date3, 15, null,p1);
        Discount d8 = discountsDao.addNewDiscount(3,date10, date3, 15, null,p1);
        Discount d9 = discountsDao.addNewDiscount(4,date10, date3, 15, null,p1);
        Discount d10 = discountsDao.addNewDiscount(5,date10, date3, 15, null,p1);
// discounts on p2 for all branches
        Discount d11 = discountsDao.addNewDiscount(1,date10, date3, 10, null,p2);
        Discount d12 = discountsDao.addNewDiscount(2,date10, date3, 10, null,p2);
        Discount d13 = discountsDao.addNewDiscount(3,date10, date3, 10, null,p2);
        Discount d14 = discountsDao.addNewDiscount(4,date10, date3, 10, null,p2);
        Discount d15 = discountsDao.addNewDiscount(5,date10, date3, 10, null,p2);
// discounts on p3 for all branches
        Discount d16 = discountsDao.addNewDiscount(1,date10, date3, 15, null,p3);
        Discount d17 = discountsDao.addNewDiscount(2,date10, date3, 15, null,p3);
        Discount d18 = discountsDao.addNewDiscount(3,date10, date3, 15, null,p3);
        Discount d19 = discountsDao.addNewDiscount(4,date10, date3, 15, null,p3);
        Discount d20 = discountsDao.addNewDiscount(5,date10, date3, 15, null,p3);
// discounts on p4 for all branches
        Discount d21 = discountsDao.addNewDiscount(1,date14, date4, 20, null,p4);
        Discount d22 = discountsDao.addNewDiscount(2,date14, date4, 20, null,p4);
        Discount d23 = discountsDao.addNewDiscount(3,date14, date4, 20, null,p4);
        Discount d24 = discountsDao.addNewDiscount(4,date14, date4, 20, null,p4);
        Discount d25 = discountsDao.addNewDiscount(5,date14, date4, 20, null,p4);
// discounts on p5 for all branches
        Discount d26 = discountsDao.addNewDiscount(1,date1, date6, 5, null,p5);
        Discount d27 = discountsDao.addNewDiscount(2,date1, date6, 5, null,p5);
        Discount d28 = discountsDao.addNewDiscount(3,date1, date6, 5, null,p5);
        Discount d29 = discountsDao.addNewDiscount(4,date1, date6, 5, null,p5);
        Discount d30 = discountsDao.addNewDiscount(5,date1, date6, 5, null,p5);
// discounts on c1 for all branches
        Discount d31 = discountsDao.addNewDiscount(1,date14, date4, 12, c1,null);
        Discount d32 = discountsDao.addNewDiscount(2,date14, date4, 12, c1,null);
        Discount d33 = discountsDao.addNewDiscount(3,date14, date4, 12, c1,null);
        Discount d34 = discountsDao.addNewDiscount(4,date14, date4, 12, c1,null);
        Discount d35 = discountsDao.addNewDiscount(5,date14, date4, 12, c1,null);
// discounts on c10 for all branches
        Discount d36 = discountsDao.addNewDiscount(1,date14, date19, 12, c10,null);
        Discount d37 = discountsDao.addNewDiscount(2,date14, date19, 12, c10,null);
        Discount d38 = discountsDao.addNewDiscount(3,date14, date19, 12, c10,null);
        Discount d39 = discountsDao.addNewDiscount(4,date14, date19, 12, c10,null);
        Discount d40 = discountsDao.addNewDiscount(5,date14, date19, 12, c10,null);
// discounts on c8 for all branches
        Discount d41 = discountsDao.addNewDiscount(1,date14, date4, 7, c8,null);
        Discount d42 = discountsDao.addNewDiscount(2,date14, date4, 7, c8,null);
        Discount d43 = discountsDao.addNewDiscount(3,date14, date4, 7, c8,null);
        Discount d44 = discountsDao.addNewDiscount(4,date14, date4, 7, c8,null);
        Discount d45 = discountsDao.addNewDiscount(5,date14, date4, 7, c8,null);
// discounts on c21 for all branches
        Discount d46 = discountsDao.addNewDiscount(1,date16, date17, 25, c10,null);
        Discount d47 = discountsDao.addNewDiscount(2,date16, date17, 25, c10,null);
        Discount d48 = discountsDao.addNewDiscount(3,date16, date17, 25, c10,null);
        Discount d49 = discountsDao.addNewDiscount(4,date16, date17, 25, c10,null);
        Discount d50 = discountsDao.addNewDiscount(5,date16, date17, 25, c10,null);

// Items for all Branches
        for (int j=1;j<6;j++)
        {
            for (int i = 1; i < 50; i++)
            {
                Item item1 = itemsDao.addItem(j,date1,date13 , 2, 9 ,1,p1);
                Item item2 = itemsDao.addItem(j,date1,date13 , 4, 12 ,2,p2);
                Item item3 = itemsDao.addItem(j,date4,date13 , 1, 5 ,3,p3);
                Item item4 = itemsDao.addItem(j,date4,date13 , 1, 4 ,4,p4);
                Item item5 = itemsDao.addItem(j,date4,date13 , 0.5, 3 ,5,p5);
                Item item6 = itemsDao.addItem(j,date4,date13 , 1, 3 ,6,p6);
                Item item7 = itemsDao.addItem(j,date4,date13 , 1, 4 ,7,p7);
                Item item8 = itemsDao.addItem(j,date1,date13 , 4, 9 ,3,p8);
                Item item9 = itemsDao.addItem(j,date1,date13 , 3, 10 ,4,p9);
                Item item10 = itemsDao.addItem(j,date1,date13 , 5, 11 ,5,p10);
                Item item11 = itemsDao.addItem(j,date1,date13 , 5, 12 ,6,p11);
                Item item12 = itemsDao.addItem(j,date1,date13 , 5, 14 ,7,p12);
                Item item13 = itemsDao.addItem(j,date1,date13 , 6, 15 ,3,p13);
                Item item14 = itemsDao.addItem(j,date1,date13 , 5, 9 ,4,p14);
                Item item15 = itemsDao.addItem(j,date1,date13 , 4, 12 ,5,p15);
                Item item16 = itemsDao.addItem(j,date1,date13 , 5, 9 ,6,p16);
                Item item17 = itemsDao.addItem(j,date1,date13 , 5, 9 ,7,p17);
                Item item18 = itemsDao.addItem(j,null,date13 , 2, 6 ,3,p18);
                Item item19 = itemsDao.addItem(j,null,date13 , 4, 9 ,4,p19);
                Item item20 = itemsDao.addItem(j,null,date13 , 6, 12 ,5,p20);
                Item item21 = itemsDao.addItem(j,null,date13 , 4,  9,9,p21);
                Item item22 = itemsDao.addItem(j,null,date13 ,4 , 9 ,7,p22);

            }
        }

// add expired Items
        for (int j=1;j<6;j++)
        {
            for (int i = 1; i < 6; i++)
            {
                Item item1 = itemsDao.addItem(j,date20,date13 , 2, 9 ,1,p1);
                Item item2 = itemsDao.addItem(j,date20,date13 , 4, 12 ,2,p2);
            }
        }
    }
}

