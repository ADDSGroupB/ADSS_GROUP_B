package InterfaceLayer.CLI;

import BusinessLayer.InventoryBusinessLayer.*;
import BusinessLayer.SupplierBusinessLayer.Order;
import DataAccessLayer.DBConnector;
import ServiceLayer.SupplierServiceLayer.OrderService;
import Utillity.Response;

import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class StorekeeperCLI {
    private MainController mainController;
    private SupplierManagerCLI supplierManagerCLI;
    private OrderService orderService;
    public StorekeeperCLI() {
        mainController = new MainController();
        supplierManagerCLI = new SupplierManagerCLI();
        orderService = new OrderService();
        startDailyTask();
    }
    public MainController getMainController() {return this.mainController;}



//    public void loadData() throws SQLException {
//        System.out.println("Initializing the information in the system... ");
//        LoadDataInventory(this.getMainController());
//        supplierManagerCLI.loadDataSupplier();
//        mainController.getItemsDao().checkExpiredItemsInAllBranches();
//        List<Branch> allBranches = mainController.getBranchesDao().getAllBranches();
//        if (allBranches.size() > 0)
//        {
//            mainController.getItemsDao().checkAllOrdersForToday(this.orderService,allBranches);
//        }
//        for (Branch branch : allBranches)
//        {
//            mainController.getItemsDao().fromStorageToStore(branch);
//        }
//    }
    public void Start() throws SQLException
    {
        StorekeeperUI();
//        Scanner startScanner = new Scanner(System.in);
//        int startChoice = 0;
//        while (startChoice != 3) {
//            System.out.println("Start Menu - Please choose one of the following options : ");
//            System.out.println("1. Start with loaded data ");
//            System.out.println("2. Start without loaded data ");
//            System.out.println("3. Exit ");
//            try {startChoice = startScanner.nextInt();}
//            catch (Exception e) {
//                System.out.println("Please enter an integer between 1-3 ");
//                startScanner.nextLine();
//                continue;}
//            switch (startChoice)
//            {
//                case 1:{
//                    System.out.println("Initializing the information in the system... ");
//                    LoadDataInventory(this.getMainController());
//                    supplierManagerCLI.loadDataSupplier();
//                    mainController.getItemsDao().checkExpiredItemsInAllBranches();
//                    List<Branch> allBranches = mainController.getBranchesDao().getAllBranches();
//                    if (allBranches.size() > 0)
//                    {
//                        mainController.getItemsDao().checkAllOrdersForToday(this.orderService,allBranches);
//                    }
//                    for (Branch branch : allBranches)
//                    {
//                        mainController.getItemsDao().fromStorageToStore(branch);
//                    }
//                    MainMenuUI();
//                    break;
//                }
//                case 2:{
//                    System.out.println("Initializes the system without information... ");
//                    MainMenuUI();
//                    break;
//                }
//                case 3:{System.out.println("Exiting from the system");break;}
//                default: {System.out.println("Invalid choice, please try again");break;}
//            }
//        }
    }
    public void SuppliersUI()throws SQLException
    {
        supplierManagerCLI.Start();
    }
    public void MainMenuUI()throws SQLException {
        Scanner mainMenuScanner = new Scanner(System.in);
        int mainMenuChoice = 0;
        while (mainMenuChoice != 3) {
            System.out.println("Main Menu - Please choose one of the following options : ");
            System.out.println("1. Storekeeper menu ");
            System.out.println("2. Suppliers Menu ");
            System.out.println("3. Exit system");
            try {mainMenuChoice = mainMenuScanner.nextInt();}
            catch (Exception e) {
                System.out.println("Please enter an integer between 1-3 ");
                mainMenuScanner.nextLine();
                continue;}
            switch (mainMenuChoice)
            {
                case 1:{
                    StorekeeperUI();break;}
                case 2:{SuppliersUI();break;}
                case 3:{System.out.println("Exiting from the system");break;}
                default:{System.out.println("Invalid choice, please try again");break;}
            }
        }
    }
    public void StorekeeperUI()throws SQLException {
        Scanner InventoryScanner = new Scanner(System.in);
        int InventoryChoice = 0;
        while (InventoryChoice != 4) {
            System.out.println("Storekeeper menu - Please choose one of the following options : ");
            System.out.println("1. Entering the menu of a specific branch ");
            System.out.println("2. Entering the product menu");
            System.out.println("3. Entering the category menu");
            System.out.println("4. Exit ");
            try {InventoryChoice = InventoryScanner.nextInt();}
            catch (Exception e) {
                System.out.println("Please enter an integer between 1-4 ");
                InventoryScanner.nextLine();
                continue;
            }
            switch (InventoryChoice)
            {
                case 1:{
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
                case 2:{ productUI();break; }
                case 3:{ categoryUI();break; }
                case 4:{ System.out.println("Exiting from the system"); break; }
                default:{ System.out.println("Invalid choice, please try again"); break; }
            }
        }
    }
    public void BranchUI(Branch branch)throws SQLException {
        Scanner branchScanner = new Scanner(System.in);
        int branchChoice = 0;
        while (branchChoice != 9) {
            System.out.println("Branch Menu - Please choose one of the following options : ");
            System.out.println("1. New sale ");
            System.out.println("2. Update damaged item ");
            System.out.println("3. Print all items in store ");
            System.out.println("4. Print all items in storage ");
            System.out.println("5. Orders"); // what we need to do here
            System.out.println("6. Execute Periodic Orders For Today "); // ADD ITEMS
            System.out.println("7. Execute Shortage Orders For Today "); // ADD ITEMS
            System.out.println("8. Print branch's report history "); // ADD ITEMS
            System.out.println("9. Exit to Storekeeper menu ");
            try {
                branchChoice = branchScanner.nextInt();
            } catch (Exception e) {
                System.out.println("Please enter an integer between 1-9 ");
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
                        mainController.getItemsDao().fromStorageToStore(branch);
                        System.out.println("The items have been successfully update");
                        System.out.println(itemDef + "\n");
                        break;
                    }
                    System.out.println("The ID of the item you specified does not exist in the system");
                    break;
                }
                case 3: {
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
                case 4: {
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
                case 5: { 
                    OrdersUI(branch.getBranchID());
                    break;
                }
                case 6: {
                    if(LocalTime.now().isAfter(LocalTime.of(10, 0))) orderService.run();
                    else System.out.println("Periodic Orders Will Execute Automatically at 10AM");
                    break;
                }
                case 7:
                {
                    if(LocalTime.now().isAfter(LocalTime.of(17, 0))) autoShortage();
                    else System.out.println("Shortage Orders Will Execute Automatically at 8PM");
                    break;
                }
                case 8:
                {
                    System.out.println("Branch Name : " +branch.getBranchName() + ", Branch ID : " + branch.getBranchID() + "\n");
                    System.out.println(" **Orders History** \n");
                    printOrderToBranch(branch.getBranchID());
                    break;
                }
                case 9: {System.out.println("Exiting to Storekeeper menu");break;}
                default: {System.out.println("Invalid choice, please try again");break;}
            }
        }
    }

    private void OrdersUI(int branchID) {
        Scanner startScanner = new Scanner(System.in);
        int startChoice = 0;
        while (startChoice != 3) {
            System.out.println("Orders Menu - Please choose one of the following options : ");
            System.out.println("1. Periodic Order ");
            System.out.println("2. Existing Order ");
            System.out.println("3. Back To Branch Menu");
            try { startChoice = startScanner.nextInt(); }
            catch (Exception e) {
                System.out.println("Please enter an integer between 1-3 ");
                startScanner.nextLine();
                continue; }
            switch (startChoice)
            {
                case 1:{
                    PeriodicOrderUI(branchID);
                    break;
                }
                case 2:{
                    ExistingOrderUI(branchID);
                    break;
                }
                case 3: { System.out.println("Exiting to Storekeeper menu"); break; }
                default: { System.out.println("Invalid choice, please try again"); break; }
            }
        }
    }

    private void ExistingOrderUI(int branchID) {
        Scanner startScanner = new Scanner(System.in);
        int startChoice = 0;
        while (startChoice != 3) {
            System.out.println("Existing Orders Menu - Please choose one of the following options : ");
            System.out.println("1. Add / Update Products On Order ");
            System.out.println("2. Remove Products From Order ");
            System.out.println("3. Back To Orders Menu ");
            try { startChoice = startScanner.nextInt(); }
            catch (Exception e) {
                System.out.println("Please enter an integer between 1-4 ");
                startScanner.nextLine();
                continue; }
            switch (startChoice)
            {
                case 1:{
                    updateProductsInOrder();
                    break;
                }
                case 2:{
                    removeProductsFromOrder();
                    break;
                }
                case 3: { System.out.println("Exiting to Storekeeper menu"); break; }
                default: { System.out.println("Invalid choice, please try again"); break; }
            }
        }
    }

    private void PeriodicOrderUI(int branchID) {
        Scanner startScanner = new Scanner(System.in);
        int startChoice = 0;
        while (startChoice != 4) {
            System.out.println("Periodic Orders Menu - Please choose one of the following options : ");
            System.out.println("1. Create New Periodic Order ");
            System.out.println("2. Add / Update Products On Periodic Order ");
            System.out.println("3. Remove Products From Periodic Order ");
            System.out.println("4. Back To Orders Menu ");
            try { startChoice = startScanner.nextInt(); }
            catch (Exception e) {
                System.out.println("Please enter an integer between 1-4 ");
                startScanner.nextLine();
                continue; }
            switch (startChoice)
            {
                case 1:{
                    createPeriodicOrder(branchID);
                    break;
                }
                case 2:{
                    updateProductsInPeriodicOrder();
                    break;
                }
                case 3: {
                    removeProductsFromPeriodicOrder();
                    break;
                }
                case 4: { System.out.println("Exiting to Storekeeper menu"); break; }
                default: { System.out.println("Invalid choice, please try again"); break; }
            }
        }
    }

    private void createPeriodicOrder(int branchID)
    {
        Scanner reader = new Scanner(System.in);
        System.out.println("Please Enter Supplier ID: ");
        int supplierID = reader.nextInt();
        reader.nextLine();
        System.out.println("Please Choose The Periodic Order Day");
        System.out.println("1. Monday \n2. Tuesday \n3. Wednesday \n4. Thursday \n5. Friday \n6. Saturday \n7. Sunday");
        int day = reader.nextInt();
        reader.nextLine();
        while (day < 1 || day > 7)
        {
            System.out.println("Please enter a valid number : 1 to 7");
            day = reader.nextInt();
            reader.nextLine();
        }
        DayOfWeek fixedDay = DayOfWeek.of(day);
        boolean correct = false;
        HashMap<Integer, Integer> productsAndAmount = new HashMap<>();
        while(!correct) {
            System.out.println("Choose Products And Amounts According To The Format: ProductID:Amount, ProductID:Amount, ...");
            try {
                String[] arr = reader.nextLine().split("\\s*,\\s*");
                for (String s1 : arr) {
                    String[] val = s1.split(":");
                    int productID = Integer.parseInt(val[0]);
                    int amount = Integer.parseInt(val[1]);
                    productsAndAmount.put(productID, amount);
                }
                correct = true;
            } catch (Exception e) {
                System.out.println("Please Enter Only According To The Format!");
                productsAndAmount.clear();
            }
        }
        Response response = orderService.createPeriodicOrder(supplierID, branchID, fixedDay, productsAndAmount);
        if(response.errorOccurred()) System.out.println(response.getErrorMessage());
        else System.out.println("Periodic Order With The ID " + response.getSupplierId() + " Has Successfully Been Created");
    }

    private void updateProductsInOrder()
    {
        Scanner reader = new Scanner(System.in);
        System.out.println("Please Enter Order ID: ");
        int orderID = reader.nextInt();
        reader.nextLine();
        boolean correct = false;
        HashMap<Integer, Integer> productsAndAmount = new HashMap<>();
        while (!correct)
        {
            System.out.println("Choose Products And Amounts According To The Format: ProductID:Amount, ProductID:Amount, ...");
            try {
                String[] arr = reader.nextLine().split("\\s*,\\s*");
                for (String s1 : arr) {
                    String[] val = s1.split(":");
                    int productID = Integer.parseInt(val[0]);
                    int amount = Integer.parseInt(val[1]);
                    productsAndAmount.put(productID, amount);
                }
                correct = true;
            }
            catch (Exception e)
            {
                System.out.println("Please Enter Only According To The Format!");
                productsAndAmount.clear();
            }
        }

        Response response = orderService.updateProductsInOrder(orderID, productsAndAmount);
        if(response.errorOccurred()) System.out.println(response.getErrorMessage());
        else System.out.println("Order With The ID " + response.getSupplierId() + " Has Successfully Been Updated");
    }

    private void removeProductsFromOrder()
    {
        Scanner reader = new Scanner(System.in);
        System.out.println("Please Enter Order ID: ");
        int orderID = reader.nextInt();
        reader.nextLine();
        boolean correct = false;
        ArrayList<Integer> products = new ArrayList<>();
        while (!correct)
        {
            System.out.println("Choose Products To The Format: ProductID_1, ProductID_2, ProductID_3,...");
            try {
                String[] arr = reader.nextLine().split("\\s*,\\s*");
                for (String s1 : arr)
                    products.add(Integer.parseInt(s1));
                correct = true;
            }
            catch (Exception e) { System.out.println("Please Enter Only Product IDs!"); products.clear(); }
        }
        Response response = orderService.removeProductsFromOrder(orderID, products);
        if(response.errorOccurred()) System.out.println(response.getErrorMessage());
        else System.out.println("Order With The ID " + response.getSupplierId() + " Has Successfully Been Updated");
    }

    private void updateProductsInPeriodicOrder()
    {
        Scanner reader = new Scanner(System.in);
        System.out.println("Please Enter Periodic Order ID: ");
        int orderID = reader.nextInt();
        reader.nextLine();
        boolean correct = false;
        HashMap<Integer, Integer> productsAndAmount = new HashMap<>();
        while (!correct)
        {
            System.out.println("Choose Products And Amounts According To The Format: ProductID:Amount");
            try {
                String[] arr = reader.nextLine().split("\\s*,\\s*");
                for (String s1 : arr) {
                    String[] val = s1.split(":");
                    int productID = Integer.parseInt(val[0]);
                    int amount = Integer.parseInt(val[1]);
                    productsAndAmount.put(productID, amount);
                }
                correct = true;
            }
            catch (Exception e)
            {
                System.out.println("Please Enter Only According To The Format!");
                productsAndAmount.clear();
            }
        }

        Response response = orderService.updateProductsInPeriodicOrder(orderID, productsAndAmount);
        if(response.errorOccurred()) System.out.println(response.getErrorMessage());
        else System.out.println("Order With The ID " + response.getSupplierId() + " Has Successfully Been Updated");
    }

    private void removeProductsFromPeriodicOrder()
    {
        Scanner reader = new Scanner(System.in);
        System.out.println("Please Enter Periodic Order ID: ");
        int orderID = reader.nextInt();
        reader.nextLine();
        boolean correct = false;
        ArrayList<Integer> products = new ArrayList<>();
        while (!correct)
        {
            System.out.println("Choose Products To The Format: ProductID_1, ProductID_2, ProductID_3,...");
            try {
                String[] arr = reader.nextLine().split("\\s*,\\s*");
                for (String s1 : arr)
                    products.add(Integer.parseInt(s1));
                correct = true;
            }
            catch (Exception e) { System.out.println("Please Enter Only Product IDs!"); products.clear(); }
        }
        Response response = orderService.removeProductsFromPeriodicOrder(orderID, products);
        if(response.errorOccurred()) System.out.println(response.getErrorMessage());
        else System.out.println("Order With The ID " + response.getSupplierId() + " Has Successfully Been Updated");
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
            System.out.println("5. Exit to Storekeeper menu");
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
                    } else
                    {
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
                case 5: {System.out.println("Exiting to Storekeeper menu \n");break;}
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
            System.out.println("4. Exit to Storekeeper menu");
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
                case 4:{  System.out.println("Exiting to Storekeeper menu");break;}
                default:{ System.out.println("Invalid choice, please try again");break;}
            }
        }
    }
    public void missingReportUI(Branch branch) throws SQLException {
        Scanner missingScanner = new Scanner(System.in);

        int choice = 0;
        while (choice != 3) {
            System.out.println("Missing Products Report Menu:");
            System.out.println("1. Create a new Missing Products Report");
            System.out.println("2. Print the current Missing Products Report");
            System.out.println("3. Exit");
            try {
                choice = missingScanner.nextInt();
            } catch (Exception e) {
                System.out.println("Please enter an integer");
                missingScanner.nextLine(); // clear input buffer
                continue;
            }
            switch (choice)
            {
                case 1 :
                {
                    System.out.println("Creating new Missing Products Report...");
                    int reportID = mainController.getReportDao().getNewReportID();
                    MissingProductsReport report = mainController.getBranchController().getReportController().createNewMissingReport(reportID, branch.getBranchID());
                    int productID = 0;
                    while (productID != -1)
                    {
                        System.out.print("Enter the product ID (or -1 to finish): ");
                        productID = HelperFunctions.positiveItegerInsertionWithCancel();
                        if (productID == -1)
                        {
                            break;
                        }
                        Product product = mainController.getProductsDao().getProductByID(productID);
                        if (product != null)
                        {
                            System.out.print("Enter the amount to order : ");
                            int amount = HelperFunctions.positiveItegerInsertion();
                            report.addMissingProduct(product, amount);
                        }
                        else
                        {
                            System.out.println("Unknown product ID. Please try again");
                        }
                    }
                    System.out.println("The products have been successfully added to the report");
                    branch.getBranchReportManager().addNewReport(report);
                    mainController.getReportDao().addReport(report);
                    break;
                }
                case 2 : {
                    if (branch.getBranchReportManager().getCurrentMissingReport() != null)
                    {
                        System.out.println(branch.getBranchReportManager().getCurrentMissingReport().toString());
                    }
                    else
                    {
                        System.out.println("Missing Products Report has not been created yet");
                    }
                    break;
                }
                case 3:
                {
                    System.out.println("Exiting...");
                    break;
                }
                default:{
                    System.out.println("Invalid choice, please try again");
                    break;
                }
            }
        }
    }
    public void defectiveReportUI(Branch branch) throws SQLException {
        Scanner defectiveScanner = new Scanner(System.in);

        int choice = 0;
        while (choice != 3) {
            System.out.println("Defective Items Report Menu:");
            System.out.println("1. Create a new Defective Items Report");
            System.out.println("2. Print the current Defective Items Report");
            System.out.println("3. Exit");
            try {
                choice = defectiveScanner.nextInt();
            } catch (Exception e) {
                System.out.println("Please enter an integer");
                defectiveScanner.nextLine(); // clear input buffer
                continue;
            }
            switch (choice)
            {
                case 1:{
                    System.out.println("Creating new Defective Items Report...");
                    int reportID = mainController.getReportDao().getNewReportID();
                    DefectiveProductsReport report = mainController.getBranchController().getReportController().createNewDefectiveReport(reportID, branch.getBranchID());
                    List<Item> defectiveItems = mainController.getItemsDao().getAllDamagedItemsByBranchID(branch.getBranchID());
                    List<Item> expiredItems = mainController.getItemsDao().getAllExpiredItemsByBranchID(branch.getBranchID());
                    if (defectiveItems.size() == 0 && expiredItems.size() == 0)
                    {
                        System.out.println("We currently have no damaged or expired items to report...");
                        break;
                    }
                    if (report == null) {
                        System.out.println("Defective Items Report has not been created yet");
                        break;
                    }
                        Map<Integer, DefectiveProductsReport> allDefectiveReports = new HashMap<>();
                        allDefectiveReports = mainController.getReportDao().getAllDefectiveReports();
                        if (allDefectiveReports != null) {
                            List<Item> allItemsInReports = new ArrayList<>();
                            for (DefectiveProductsReport defectiveProductsReport : allDefectiveReports.values()) {
                                if (defectiveProductsReport.getBranchID() == branch.getBranchID()) {
                                    List<Item> currItemsInReport = defectiveProductsReport.getDefectiveOrExpiredProducts(defectiveProductsReport.getReportID());
                                    allItemsInReports.addAll(currItemsInReport);
                                }
                            }
                            for (Item item : defectiveItems)
                            {
                                boolean check = false;
                                if (allItemsInReports.size() > 0) {
                                    for (Item item1 : allItemsInReports)
                                    {
                                        if (item1.getItemID() == item.getItemID())
                                        {
                                            check = true;
                                            break;
                                        }
                                    }
                                    if (!check)
                                    {
                                        report.addDefectiveItem(item);
                                    }
                                }
                                else {report.addDefectiveItem(item);}
                            }
                            for (Item item : expiredItems) {
                                boolean check = false;
                                if (allItemsInReports.size()>0) {
                                    for (Item item1 : allItemsInReports) {
                                        if (item1.getItemID() == item.getItemID()) {
                                            check = true;
                                            break;
                                        }
                                    }
                                    if (!check) {
                                        report.addDefectiveItem(item);
                                    }
                                }
                                else {report.addDefectiveItem(item);}
                            }
                            branch.getBranchReportManager().addNewReport(report);
                            mainController.getReportDao().addReport(report);
                            System.out.println("Adding items to the report has been successfully completed");
                            break;
                        }
                        else
                        {
                            for (Item item : defectiveItems)
                            {
                                report.addDefectiveItem(item);
                            }
                            for (Item item : expiredItems)
                            {
                                report.addDefectiveItem(item);
                            }
                            branch.getBranchReportManager().addNewReport(report);
                            mainController.getReportDao().addReport(report);
                            System.out.println("Adding items to the report has been successfully completed");
                            break;
                        }
                }
                case 2:{
                    if (branch.getBranchReportManager().getCurrentDefectiveReport() != null) {
                        System.out.println(branch.getBranchReportManager().getCurrentDefectiveReport().toString());
                    } else
                        System.out.println("Defective Items Report has not been created yet");
                    break;
                }
                case 3:{System.out.println("Exiting...");
                    break;}
                default:{System.out.println("Invalid choice, please try again");
                    break;}
            }
        }
    }
    public void weeklyReportUI(Branch branch) throws SQLException {
        Scanner weeklyScanner = new Scanner(System.in);
        int choice =0;
        while (choice != 3) {
            System.out.println("Weekly Items Report Menu:");
            System.out.println("1. Create a new Defective Items Report");
            System.out.println("2. Print the current Defective Items Report");
            System.out.println("3. Exit");
            try {
                choice = weeklyScanner.nextInt();
            } catch (Exception e) {
                System.out.println("Please enter an integer");
                weeklyScanner.nextLine(); // clear input buffer
                continue;
            }
            switch (choice)
            {
                case 1:{
                    System.out.println("Creating new Weekly Storages Report...");
                    int reportID = mainController.getReportDao().getNewReportID();
                    WeeklyStorageReport report = mainController.getBranchController().getReportController().createNewWeeklyReport(reportID, branch.getBranchID());
                    int categoryID = 0;
                    while (categoryID != -1) {
                        System.out.print("Enter the category ID (or -1 to finish): ");
                        try {
                            categoryID = weeklyScanner.nextInt();
                        } catch (Exception e) {
                            System.out.println("Please enter an integer");
                            weeklyScanner.nextLine(); // clear input buffer
                            continue;
                        }
                        if (categoryID == -1) {
                            break;
                        }
                        Category category = mainController.getCategoryDao().getCategoryByID(categoryID);
                        if (category == null) {
                            System.out.println("Unknown category ID. Please try again");
                            weeklyScanner.nextLine(); // clear input buffer
                            continue;
                        }
                        if (report.getWeeklyReportMap().containsKey(category)) {
                            System.out.println("The category is already in the report");
                            weeklyScanner.nextLine(); // clear input buffer
                            continue;
                        }
                        List<Product> products = mainController.getProductsDao().getAllProductsInCategory(categoryID);
                        Map<Product, Integer> productCurrAmount = new HashMap<>();
                        for (Product product : products) {
                            int productAmount = mainController.getItemsDao().getAllStorageItemsByBranchIDAndProductID(branch.getBranchID(), product.getProductID()).size();
                            productAmount += mainController.getItemsDao().getAllStoreItemsByBranchIDAndProductID(branch.getBranchID(), product.getProductID()).size();
                            productCurrAmount.put(product, productAmount);
                        }
                        report.addCategoryToReport(category, productCurrAmount);
                    }
                    branch.getBranchReportManager().addNewReport(report);
                    mainController.getReportDao().addReport(report);
                    System.out.println("Adding categories to the report has been successfully completed");
                    break;
                }
                case 2:{
                    if (branch.getBranchReportManager().getCurrentWeeklyReport() != null) {
                        System.out.println(branch.getBranchReportManager().getCurrentWeeklyReport().toString(branch));
                    } else System.out.println("Weekly Storage Report has not been created yet");
                    break;
                }
                case 3:{System.out.println("Exiting...");
                    break;}
                default:{System.out.println("Invalid choice, please try again");
                    break;}
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
            switch (choice)
            {
                case 1:{missingReportUI(branch);
                    break;}
                case 2:{defectiveReportUI(branch);
                    break;}
                case 3:{weeklyReportUI(branch);
                    break;}
                default:{System.out.println("Invalid choice, please try again");
                    break;}
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

    private void startDailyTask()
    {
        Timer timerPeriodicOrder = new Timer();
        // Schedule the task to execute every day at 10:00am
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 10);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        if (calendar.getTimeInMillis() < System.currentTimeMillis())
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        timerPeriodicOrder.scheduleAtFixedRate(orderService, calendar.getTime(), TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS));
        Timer timerForShortageOrder = new Timer();
        TimerTask otherTask = new TimerTask() {
            @Override
            public void run()  {
                autoShortage();
            }
        };
        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(Calendar.HOUR_OF_DAY, 10);
        calendar2.set(Calendar.MINUTE, 0);
        calendar2.set(Calendar.SECOND, 0);
        if (calendar2.getTimeInMillis() < System.currentTimeMillis())
            calendar2.add(Calendar.DAY_OF_MONTH, 1);
        timerForShortageOrder.scheduleAtFixedRate(otherTask, calendar2.getTime(), TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS));
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            timerPeriodicOrder.cancel();
            timerForShortageOrder.cancel();
            DBConnector.disconnect();
        }));
    }
    public void autoShortage()
    {
        try {
            List<Branch> branches = mainController.getBranchesDao().getAllBranches();
            HashMap<Integer, Integer> shortage;
            for(Branch branch: branches) {
                shortage = mainController.getItemsDao().fromStorageToStore(branch);
                Response response = orderService.createOrderByShortage(branch.getBranchID(), shortage);
                if (!response.errorOccurred())
                {
                    for (Integer productID : shortage.keySet())
                    {
                        mainController.getProductMinAmountDao().UpdateOrderStatusToProductInBranch(productID, branch.getBranchID(),"Invited");
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error while run function autoShortage in Cli: " + e.getMessage());
        }
    }
//    public static void LoadDataInventory(MainController mainController) throws SQLException
//    {
//        ProductsDao productsDao = mainController.getProductsDao();
//        ItemsDao itemsDao = mainController.getItemsDao();
//        BranchesDao branchesDao = mainController.getBranchesDao();
//        CategoryDao categoryDao = mainController.getCategoryDao();
//        DiscountsDao discountsDao = mainController.getDiscountsDao();
//        ProductMinAmountDao productMinAmountDao = mainController.getProductMinAmountDao();
//// Data Base From Nothing
////==================================================
////Branches
//        Branch b1 = branchesDao.addBranch("SuperLi Beer Sheva");
//        Branch b2 = branchesDao.addBranch("SuperLi Tel Aviv");
//        Branch b3 = branchesDao.addBranch("SuperLi Jerusalem");
//        Branch b4 = branchesDao.addBranch("SuperLi Herzliya");
//        Branch b5 = branchesDao.addBranch("SuperLi Eilat");
//// Categories
//        Category c1 =categoryDao.addCategory("Dairy products");
//        Category c2 =categoryDao.addCategory("Milk");
//        Category c3 =categoryDao.addCategory("Cottage");
//        Category c4 =categoryDao.addCategory("Cream Cheese");
//        Category c5 =categoryDao.addCategory("Yellow Cheese");
//        Category c6 =categoryDao.addCategory("1% fat");
//        Category c7 =categoryDao.addCategory("3% fat");
//        Category c8 =categoryDao.addCategory("5% fat");
//        Category c9 =categoryDao.addCategory("9% fat");
//        Category c10 =categoryDao.addCategory("Vegetables");
//        Category c11 =categoryDao.addCategory("Onions");
//        Category c12 =categoryDao.addCategory("Potatoes");
//        Category c13 =categoryDao.addCategory("Green Onions");
//        Category c14 =categoryDao.addCategory("White Onions");
//        Category c15 =categoryDao.addCategory("Red Potatoes");
//        Category c16 =categoryDao.addCategory("Fruits");
//        Category c17 =categoryDao.addCategory("Apples");
//        Category c18 =categoryDao.addCategory("Red Apples");
//        Category c19 =categoryDao.addCategory("Green Apples");
//        Category c20 =categoryDao.addCategory("Citrus Fruits");
//        Category c21 =categoryDao.addCategory("Oranges");
//        Category c22 =categoryDao.addCategory("Sweet Drinks");
//        Category c23 =categoryDao.addCategory("0.5 Liters");
//        Category c24 =categoryDao.addCategory("Sodas");
//        Category c25 =categoryDao.addCategory("1 Liters");
//        Category c26 =categoryDao.addCategory("1.5 Liters");
//        Category c27 =categoryDao.addCategory("Soft Drinks");
//
//// Products
//        Product p1 = productsDao.addProduct("Milk 3%", "Tara", 500, 1, 2, 7);
//        productMinAmountDao.addNewProductToAllBranches(1);
//        Product p2 = productsDao.addProduct("Cottage 5%", "Tnova", 250, 1, 3, 8);
//        productMinAmountDao.addNewProductToAllBranches(2);
//        Product p3 = productsDao.addProduct("White Onion", "VegAndFruits", 20, 10, 11, 14);
//        productMinAmountDao.addNewProductToAllBranches(3);
//        Product p4 = productsDao.addProduct("Green Onion", "VegAndFruits", 10, 10, 11, 13);
//        productMinAmountDao.addNewProductToAllBranches(4);
//        Product p5 = productsDao.addProduct("Red Potato", "VegAndFruits", 10, 10, 12, 15);
//        productMinAmountDao.addNewProductToAllBranches(5);
//        Product p6 = productsDao.addProduct("Red Apple", "VegAndFruits", 10, 16, 17, 18);
//        productMinAmountDao.addNewProductToAllBranches(6);
//        Product p7 = productsDao.addProduct("Green Apple", "VegAndFruits", 10, 16, 17, 18);
//        productMinAmountDao.addNewProductToAllBranches(7);
//        Product p8 = productsDao.addProduct("Cottage 9%", "Tara", 250, 1, 3, 9);
//        productMinAmountDao.addNewProductToAllBranches(8);
//        Product p9 =  productsDao.addProduct("Milk 9%", "Tnova", 500, 1, 2, 9);
//        productMinAmountDao.addNewProductToAllBranches(9);
//        Product p10 = productsDao.addProduct("Milk 1%", "Tnova", 500, 1, 2, 6);
//        productMinAmountDao.addNewProductToAllBranches(10);
//        Product p11 = productsDao.addProduct("Milk 5%", "Tnova", 500, 1, 2, 8);
//        productMinAmountDao.addNewProductToAllBranches(11);
//        Product p12 = productsDao.addProduct("Cottage 3%", "Tara", 250, 1, 3, 7);
//        productMinAmountDao.addNewProductToAllBranches(12);
//        Product p13 = productsDao.addProduct("Cottage 1%", "Tara", 250, 1, 3, 6);
//        productMinAmountDao.addNewProductToAllBranches(13);
//        Product p14 = productsDao.addProduct("Cream Cheese 3%", "Tnova", 350, 1, 4, 7);
//        productMinAmountDao.addNewProductToAllBranches(14);
//        Product p15 = productsDao.addProduct("Cream Cheese 1%", "Tnova", 350, 1, 4, 6);
//        productMinAmountDao.addNewProductToAllBranches(15);
//        Product p16 = productsDao.addProduct("Cream Cheese 5%", "Tnova", 350, 1, 4, 8);
//        productMinAmountDao.addNewProductToAllBranches(16);
//        Product p17 = productsDao.addProduct("Milk 3%", "Tnova", 500, 1, 2, 7);
//        productMinAmountDao.addNewProductToAllBranches(17);
//        Product p18 = productsDao.addProduct("Coca Cola Zero 0.5 Liter", "CocaCola", 500, 22, 24, 23);
//        productMinAmountDao.addNewProductToAllBranches(18);
//        Product p19 = productsDao.addProduct("Coca Cola Zero 1 Liter", "CocaCola", 1000, 22, 24, 25);
//        productMinAmountDao.addNewProductToAllBranches(19);
//        Product p20 = productsDao.addProduct("Coca Cola Zero 1.5 Liter", "CocaCola", 1500, 22, 24, 26);
//        productMinAmountDao.addNewProductToAllBranches(20);
//        Product p21 = productsDao.addProduct("Banana And Strawberry 1 Liter", "Spring", 1000, 22, 27, 25);
//        productMinAmountDao.addNewProductToAllBranches(21);
//        Product p22 = productsDao.addProduct("Orange juice 1 Liter", "Spring", 1000, 22, 27, 25);
//        productMinAmountDao.addNewProductToAllBranches(22);
//
//// Product Min Amount Table
//        for (int j=1;j<6;j++)
//        {
//            for (int i = 1; i < 23; i++)
//            {
//                productMinAmountDao.UpdateMinAmountToProductInBranch(i,j,30);
//            }
//        }
////Dates
//        LocalDate date1 = LocalDate.of(2023, 5, 26);
//        LocalDate date2 = LocalDate.of(2023, 6, 25);
//        LocalDate date3 = LocalDate.of(2023, 7, 30);
//        LocalDate date4 = LocalDate.of(2023, 8, 12);
//        LocalDate date5 = LocalDate.of(2023, 9, 1);
//        LocalDate date6 = LocalDate.of(2023, 10, 22);
//        LocalDate date7 = LocalDate.of(2023, 11, 17);
//        LocalDate date8 = LocalDate.of(2023, 12, 4);
//        LocalDate date9 = LocalDate.of(2023, 1, 31);   //"Expired"
//        LocalDate date10 = LocalDate.of(2023, 2, 28);  //"Expired"
//        LocalDate date11 = LocalDate.of(2023, 3, 15);  //"Expired"
//        LocalDate date12 = LocalDate.of(2023, 4, 8);   //"Expired"
//        LocalDate date13 = LocalDate.of(2023, 5, 5);   //"Expired"
//        LocalDate date14 = LocalDate.of(2023, 6, 19);
//        LocalDate date15 = LocalDate.of(2023, 7, 23);
//        LocalDate date16 = LocalDate.of(2023, 8, 10);
//        LocalDate date17 = LocalDate.of(2023, 9, 2);
//        LocalDate date18 = LocalDate.of(2023, 10, 16);
//        LocalDate date19 = LocalDate.of(2023, 11, 21);
//        LocalDate date20 = LocalDate.of(2023, 5, 13);
////Discounts
//
//// discounts on p1 for all branches
//        Discount d1 = discountsDao.addNewDiscount(1,date9, date12, 15, null,p1);
//        Discount d2 = discountsDao.addNewDiscount(2,date9, date12, 15, null,p1);
//        Discount d3 = discountsDao.addNewDiscount(3,date9, date12, 15, null,p1);
//        Discount d4 = discountsDao.addNewDiscount(4,date9, date12, 15, null,p1);
//        Discount d5 = discountsDao.addNewDiscount(5,date9, date12, 15, null,p1);
//// discounts on p1 for all branches
//        Discount d6 = discountsDao.addNewDiscount(1,date10, date3, 15, null,p1);
//        Discount d7 = discountsDao.addNewDiscount(2,date10, date3, 15, null,p1);
//        Discount d8 = discountsDao.addNewDiscount(3,date10, date3, 15, null,p1);
//        Discount d9 = discountsDao.addNewDiscount(4,date10, date3, 15, null,p1);
//        Discount d10 = discountsDao.addNewDiscount(5,date10, date3, 15, null,p1);
//// discounts on p2 for all branches
//        Discount d11 = discountsDao.addNewDiscount(1,date10, date3, 10, null,p2);
//        Discount d12 = discountsDao.addNewDiscount(2,date10, date3, 10, null,p2);
//        Discount d13 = discountsDao.addNewDiscount(3,date10, date3, 10, null,p2);
//        Discount d14 = discountsDao.addNewDiscount(4,date10, date3, 10, null,p2);
//        Discount d15 = discountsDao.addNewDiscount(5,date10, date3, 10, null,p2);
//// discounts on p3 for all branches
//        Discount d16 = discountsDao.addNewDiscount(1,date10, date3, 15, null,p3);
//        Discount d17 = discountsDao.addNewDiscount(2,date10, date3, 15, null,p3);
//        Discount d18 = discountsDao.addNewDiscount(3,date10, date3, 15, null,p3);
//        Discount d19 = discountsDao.addNewDiscount(4,date10, date3, 15, null,p3);
//        Discount d20 = discountsDao.addNewDiscount(5,date10, date3, 15, null,p3);
//// discounts on p4 for all branches
//        Discount d21 = discountsDao.addNewDiscount(1,date14, date4, 20, null,p4);
//        Discount d22 = discountsDao.addNewDiscount(2,date14, date4, 20, null,p4);
//        Discount d23 = discountsDao.addNewDiscount(3,date14, date4, 20, null,p4);
//        Discount d24 = discountsDao.addNewDiscount(4,date14, date4, 20, null,p4);
//        Discount d25 = discountsDao.addNewDiscount(5,date14, date4, 20, null,p4);
//// discounts on p5 for all branches
//        Discount d26 = discountsDao.addNewDiscount(1,date1, date6, 5, null,p5);
//        Discount d27 = discountsDao.addNewDiscount(2,date1, date6, 5, null,p5);
//        Discount d28 = discountsDao.addNewDiscount(3,date1, date6, 5, null,p5);
//        Discount d29 = discountsDao.addNewDiscount(4,date1, date6, 5, null,p5);
//        Discount d30 = discountsDao.addNewDiscount(5,date1, date6, 5, null,p5);
//// discounts on c1 for all branches
//        Discount d31 = discountsDao.addNewDiscount(1,date14, date4, 12, c1,null);
//        Discount d32 = discountsDao.addNewDiscount(2,date14, date4, 12, c1,null);
//        Discount d33 = discountsDao.addNewDiscount(3,date14, date4, 12, c1,null);
//        Discount d34 = discountsDao.addNewDiscount(4,date14, date4, 12, c1,null);
//        Discount d35 = discountsDao.addNewDiscount(5,date14, date4, 12, c1,null);
//// discounts on c10 for all branches
//        Discount d36 = discountsDao.addNewDiscount(1,date14, date19, 12, c10,null);
//        Discount d37 = discountsDao.addNewDiscount(2,date14, date19, 12, c10,null);
//        Discount d38 = discountsDao.addNewDiscount(3,date14, date19, 12, c10,null);
//        Discount d39 = discountsDao.addNewDiscount(4,date14, date19, 12, c10,null);
//        Discount d40 = discountsDao.addNewDiscount(5,date14, date19, 12, c10,null);
//// discounts on c8 for all branches
//        Discount d41 = discountsDao.addNewDiscount(1,date14, date4, 7, c8,null);
//        Discount d42 = discountsDao.addNewDiscount(2,date14, date4, 7, c8,null);
//        Discount d43 = discountsDao.addNewDiscount(3,date14, date4, 7, c8,null);
//        Discount d44 = discountsDao.addNewDiscount(4,date14, date4, 7, c8,null);
//        Discount d45 = discountsDao.addNewDiscount(5,date14, date4, 7, c8,null);
//// discounts on c21 for all branches
//        Discount d46 = discountsDao.addNewDiscount(1,date16, date17, 25, c10,null);
//        Discount d47 = discountsDao.addNewDiscount(2,date16, date17, 25, c10,null);
//        Discount d48 = discountsDao.addNewDiscount(3,date16, date17, 25, c10,null);
//        Discount d49 = discountsDao.addNewDiscount(4,date16, date17, 25, c10,null);
//        Discount d50 = discountsDao.addNewDiscount(5,date16, date17, 25, c10,null);
//
//// Items for all Branches
//        for (int j=1;j<6;j++)
//        {
//            for (int i = 1; i < 50; i++)
//            {
//                Item item1 = itemsDao.addItem(j,date1,date13 , 2, 9 ,1,p1);
//                Item item2 = itemsDao.addItem(j,date1,date13 , 4, 12 ,2,p2);
//                Item item3 = itemsDao.addItem(j,date4,date13 , 1, 5 ,3,p3);
//                Item item4 = itemsDao.addItem(j,date4,date13 , 1, 4 ,4,p4);
//                Item item5 = itemsDao.addItem(j,date4,date13 , 0.5, 3 ,5,p5);
//                Item item6 = itemsDao.addItem(j,date4,date13 , 1, 3 ,6,p6);
//                Item item7 = itemsDao.addItem(j,date4,date13 , 1, 4 ,7,p7);
//                Item item8 = itemsDao.addItem(j,date1,date13 , 4, 9 ,3,p8);
//                Item item9 = itemsDao.addItem(j,date1,date13 , 3, 10 ,4,p9);
//                Item item10 = itemsDao.addItem(j,date1,date13 , 5, 11 ,5,p10);
//                Item item11 = itemsDao.addItem(j,date1,date13 , 5, 12 ,6,p11);
//                Item item12 = itemsDao.addItem(j,date1,date13 , 5, 14 ,7,p12);
//                Item item13 = itemsDao.addItem(j,date1,date13 , 6, 15 ,3,p13);
//                Item item14 = itemsDao.addItem(j,date1,date13 , 5, 9 ,4,p14);
//                Item item15 = itemsDao.addItem(j,date1,date13 , 4, 12 ,5,p15);
//                Item item16 = itemsDao.addItem(j,date1,date13 , 5, 9 ,6,p16);
//                Item item17 = itemsDao.addItem(j,date1,date13 , 5, 9 ,7,p17);
//                Item item18 = itemsDao.addItem(j,null,date13 , 2, 6 ,3,p18);
//                Item item19 = itemsDao.addItem(j,null,date13 , 4, 9 ,4,p19);
//                Item item20 = itemsDao.addItem(j,null,date13 , 6, 12 ,5,p20);
//                Item item21 = itemsDao.addItem(j,null,date13 , 4,  9,9,p21);
//                Item item22 = itemsDao.addItem(j,null,date13 ,4 , 9 ,7,p22);
//
//            }
//        }
//        // add expired Items
//        for (int j=1;j<6;j++)
//        {
//            for (int i = 1; i < 6; i++)
//            {
//                Item item1 = itemsDao.addItem(j,date20,date13 , 2, 9 ,1,p1);
//                Item item2 = itemsDao.addItem(j,date20,date13 , 4, 12 ,2,p2);
//            }
//        }
//    }
    public void printOrderToBranch(int branchID) {
        HashMap<Integer, Order> orders = orderService.getOrdersToBranch(branchID);
        if(orders == null || orders.size() == 0) System.out.println("There is not orders in this branch");
        for(Order order : orders.values())
            System.out.println(order);
    }
}
