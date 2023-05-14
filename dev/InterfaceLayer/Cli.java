package InterfaceLayer;

import BusinessLayer.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

public class Cli {
    private MainController mainController;
    public Cli() {
        mainController = new MainController();
    }
    public MainController getMainController() {return this.mainController;}
    public void SuppliersUI()throws SQLException { }
    public void MainMenuUI()throws SQLException {
        mainController.getItemsDao().checkExpiredItemsInAllBranches();
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
            System.out.println("9. Receiving an order from supplier "); // ADD ITEMS
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
}

