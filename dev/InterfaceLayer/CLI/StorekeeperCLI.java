package InterfaceLayer.CLI;

import BusinessLayer.InventoryBusinessLayer.*;
import BusinessLayer.SupplierBusinessLayer.Order;
import DataAccessLayer.DBConnector;
import InterfaceLayer.Main;
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
        while (branchChoice != 6) {
            System.out.println("Branch Menu - Please choose one of the following options : ");
            System.out.println("1. New sale ");
            System.out.println("2. Update damaged item ");
            System.out.println("3. Print all items in store ");
            System.out.println("4. Print all items in storage ");
            System.out.println("5. Orders Menu"); // what we need to do here
            System.out.println("6. Exit to Storekeeper menu ");
            try {
                branchChoice = branchScanner.nextInt();
            } catch (Exception e) {
                System.out.println("Please enter an integer between 1-6 ");
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
                    OrdersUI(branch);
                    break;
                }
                case 6: {System.out.println("Exiting to Storekeeper menu");break;}
                default: {System.out.println("Invalid choice, please try again");break;}
            }
        }
    }

    private void OrdersUI(Branch branch) {
        Scanner startScanner = new Scanner(System.in);
        int startChoice = 0;
        while (startChoice != 3) {
            System.out.println("Orders Menu - Please choose one of the following options : ");
            System.out.println("1. Periodic Order ");
            System.out.println("2. Existing Order ");
            System.out.println("3. Execute Periodic Orders For Today "); // ADD ITEMS
            System.out.println("4. Execute Shortage Orders For Today "); // ADD ITEMS
            System.out.println("5. Print branch's orders history "); // ADD ITEMS
            System.out.println("6. Back To Branch Menu");
            try { startChoice = startScanner.nextInt(); }
            catch (Exception e) {
                System.out.println("Please enter an integer between 1-6 ");
                startScanner.nextLine();
                continue; }
            switch (startChoice)
            {
                case 1:{
                    PeriodicOrderUI(branch.getBranchID());
                    break;
                }
                case 2:{
                    ExistingOrderUI(branch.getBranchID());
                    break;
                }
                case 3: {
                    if(LocalTime.now().isAfter(LocalTime.of(10, 0))) orderService.run();
                    else System.out.println("Periodic Orders Will Execute Automatically at 10AM");
                    break;
                }
                case 4:
                {
                    if(LocalTime.now().isAfter(LocalTime.of(17, 0))) Main.autoShortage();
                    else System.out.println("Shortage Orders Will Execute Automatically at 8PM");
                    break;
                }
                case 5:
                {
                    System.out.println("Branch Name : " +branch.getBranchName() + ", Branch ID : " + branch.getBranchID() + "\n");
                    System.out.println(" **Orders History** \n");
                    printOrderToBranch(branch.getBranchID());
                    break;
                }
                case 6: { System.out.println("Exiting to Storekeeper menu"); break; }
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
    public void printOrderToBranch(int branchID) {
        HashMap<Integer, Order> orders = orderService.getOrdersToBranch(branchID);
        if(orders == null || orders.size() == 0) System.out.println("There is not orders in this branch");
        for(Order order : orders.values())
            System.out.println(order);
    }
}

