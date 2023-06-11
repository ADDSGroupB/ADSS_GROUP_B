package InterfaceLayer.CLI;

import BusinessLayer.InventoryBusinessLayer.*;
import BusinessLayer.SupplierBusinessLayer.Order;
import ServiceLayer.SupplierServiceLayer.OrderService;
import ServiceLayer.SupplierServiceLayer.SupplierService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

public class StoreManagerCLI {
    private final Scanner reader;
    private final SupplierService supplierService;
    private final OrderService orderService;
    private MainController mainController;

    public StoreManagerCLI() {
        reader = new Scanner(System.in);
        supplierService = new SupplierService();
        orderService = new OrderService();
        mainController = new MainController();
    }

    public void Start() throws SQLException
    {
        int startChoice = 0;
        while (startChoice != 3) {
            System.out.println("Store Manager Menu - Please choose one of the following options : ");
            System.out.println("1. Branch Menu ");
            System.out.println("2. Supplier Menu ");
            System.out.println("3. Exit ");
            try 
            { 
                startChoice = reader.nextInt();
                reader.nextLine();
            }
            catch (Exception e) 
            {
                System.out.println("Please Enter a valid number (1-3) ");
                reader.nextLine();
                continue;
            }
            switch (startChoice)
            {
                case 1 -> branchMenu();
                case 2 -> supplierMenu();
                default -> System.out.println("Please Enter a valid number (1-3)");
            }
        }
    }

    private void branchMenu() throws SQLException {
        int branchChoice = 0;
        while (branchChoice != 5) {
            System.out.println("Branch Menu - Please choose one of the following options : ");
            System.out.println("1. Creating a new branch");
            System.out.println("2. Entering the menu of a specific branch");
            System.out.println("3. Entering the product menu");
            System.out.println("4. Entering the category menu");
            System.out.println("5. Exit to store manager menu ");
            try {
                branchChoice = reader.nextInt();
                reader.nextLine();
            } catch (Exception e) {
                System.out.println("Please Enter a valid number (1-5) ");
                reader.nextLine();
                continue;
            }
            switch (branchChoice) {
                case 1 -> createNewBranch();
                case 2 -> BranchUI();
                case 3 -> productUI();
                case 4 -> categoryUI();
                case 5 -> { return; }
                default -> System.out.println("Please enter a valid number (1-5)");
            }
        }
    }

    private void createNewBranch() throws SQLException {
        List<Branch> allBranches = mainController.getBranchController().getAllBranchesController();
        if (allBranches.size() >= 10)
        {
            System.out.println("We have reached the limit of branches in the network, you cannot open a new branch.");
            return;
        }
        Scanner newreader = new Scanner(System.in);
        System.out.println("What is the name of the branch?");
        String newBranchName = newreader.next();
        Branch newBranch = mainController.getBranchController().createNewBranch(newBranchName);
        if (newBranch == null)
        {
            System.out.println("There is a problem creating a new branch, please try again.");
            return;
        }
        System.out.println("The branch was created successfully ! \n");
        System.out.println("Below are the details of the newly created branch : \n");
        System.out.println(newBranch);
    }

    public void BranchUI()throws SQLException {
        System.out.println("Now you have to choose the number of the branch you want to work at : ");
        List<Branch> allBranches =  mainController.getBranchController().getAllBranchesController();
        if (allBranches.size() == 0) {
            System.out.println("There are currently no branches in the system, there is an option to create a new branch.");
            return;
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
        Branch branch = mainController.getBranchController().getBranchID(branchID);
        int branchChoice = 0;
        while (branchChoice != 8) {
            System.out.println("Specific Branch Menu - Please choose one of the following options : ");
            System.out.println("1. Add new discount for product");
            System.out.println("2. Add new discount for category");
            System.out.println("3. Print all items in store ");
            System.out.println("4. Print all items in storage ");
            System.out.println("5. Print Product sales history ");
            System.out.println("6. Print branch's order history ");
            System.out.println("7. Report Manager ");
            System.out.println("8. Exit to branch menu ");
            try
            {
                branchChoice = reader.nextInt();
                reader.nextLine();
            }
            catch (Exception e)
            {
                System.out.println("Please enter a valid number (1-8) ");
                reader.nextLine();
                continue;
            }
            switch (branchChoice) {
                case 1 -> addProductDiscount(branch);
                case 2 -> addCategoryDiscount(branch);
                case 3 -> printAllItemsInStore(branch);
                case 4 -> printAllItemsInStorage(branch);
                case 5 -> printProductSalesHistory(branch);
                case 6 -> printBranchOrderHistory(branch);
                case 7 -> reportUI(branch);
                case 8 -> System.out.println("Exiting to store manager menu");
                default -> System.out.println("Please enter a valid number (1-8)");
            }
        }
    }

    private void addProductDiscount(Branch branch) throws SQLException {
        System.out.println("What is the id of the product you would like to add to him discount ? ");
        int productID = HelperFunctions.positiveItegerInsertion();
        Product productDiscount = mainController.getProductController().getProduct(productID);
        if (productDiscount == null)
        {
            System.out.println("The ID of the product you specified does not exist in the system");
            return;
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
    }

    private void addCategoryDiscount(Branch branch) throws SQLException {
        System.out.println("What is the id of the category you would like to add to him discount ? ");
        int categoryID = HelperFunctions.positiveItegerInsertion();
        Category categoryDiscount = mainController.getCategoryController().getCategory(categoryID);
        if (categoryDiscount == null)
        {
            System.out.println("The ID of the category you specified does not exist in the system");
            return;
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
    }

    private void printAllItemsInStore(Branch branch) throws SQLException {
        List<Item> storeItems = mainController.getItemsDao().getAllStoreItemsByBranchID(branch.getBranchID());
        if (storeItems.size()==0)
        {
            System.out.println("We currently have no items in the store");
            return;
        }
        System.out.println("Branch Name : " +branch.getBranchName() + ", Branch ID : " + branch.getBranchID() + "\n");
        System.out.println(" **Store Items** \n");
        for (Item item : storeItems)
        {
            System.out.println(item);
            System.out.println("------------------");
        }
        System.out.println("\n");
    }

    private void printAllItemsInStorage(Branch branch) throws SQLException {
        List<Item> storageItems = mainController.getItemsDao().getAllStorageItemsByBranchID(branch.getBranchID());
        if (storageItems.size()==0)
        {
            System.out.println("We currently have no items in the storage");
            return;
        }
        System.out.println("Branch Name : " +branch.getBranchName() + ", Branch ID : " + branch.getBranchID() + "\n");
        System.out.println(" **Storage Items** \n");
        for (Item item : storageItems)
        {
            System.out.println(item);
            System.out.println("------------------");
        }
        System.out.println("\n");
    }

    private void printProductSalesHistory(Branch branch) throws SQLException {
        System.out.println("What is the id of the product you would like to get his sold history ? ");
        int productID = HelperFunctions.positiveItegerInsertion();
        Product productSoldHistory = mainController.getProductController().getProduct(productID);
        if (productSoldHistory != null) {
            List<Item> soldItems = mainController.getItemsDao().getAllSoldItemByBranchID(branch.getBranchID());
            if (soldItems.size() == 0) {
                System.out.println("We currently have no items in the sold history of the product");
                return;
            }
            System.out.println(" **Sold Items History for the product : "+ productID + " In the branch : "+ branch.getBranchID() +" **"+ "\n");
            for (Item item : soldItems) {
                if (item.getProduct().getProductID() == productID && item.getBranchID() == branch.getBranchID()) {
                    System.out.println(item);
                    System.out.println("------------------");
                }
            }
            System.out.println("\n");
            return;
        }
        System.out.println("The ID of the item you specified does not exist in the system");
    }

    private void printBranchOrderHistory(Branch branch) throws SQLException {
        System.out.println("Branch Name : " +branch.getBranchName() + ", Branch ID : " + branch.getBranchID() + "\n");
        System.out.println(" **Orders History** \n");
        HashMap<Integer, Order> orders = orderService.getOrdersToBranch(branch.getBranchID());
        if(orders == null || orders.size() == 0) System.out.println("There is not orders in this branch");
        for(Order order : orders.values())
            System.out.println(order);
    }

    public void reportUI(Branch branch) throws SQLException {
        int choice = 0;
        while (choice != 4) {
            System.out.println("What report would you like to work on?");
            System.out.println("1. Missing Products Report");
            System.out.println("2. Defective Items Report");
            System.out.println("3. Weekly Storage Report");
            System.out.println("4. Exit");
            try
            {
                choice = reader.nextInt();
                reader.nextLine();
            }
            catch (Exception e)
            {
                System.out.println("Please enter a valid number (1-4) ");
                reader.nextLine();
                continue;
            }
            switch (choice)
            {
                case 1 -> missingReportUI(branch);
                case 2 -> defectiveReportUI(branch);
                case 3 -> weeklyReportUI(branch);
                default -> System.out.println("Please enter a valid number (1-4)");
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
            try
            {
                choice = missingScanner.nextInt();
                missingScanner.nextLine();
            } catch (Exception e) {
                System.out.println("Please enter a valid number (1-3)");
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
                    System.out.println("Please enter a valid number (1-3)");
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
                System.out.println("Please enter a valid number (1-3)");
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
                    Map<Integer, DefectiveProductsReport> allDefectiveReports = mainController.getReportDao().getAllDefectiveReports();
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
                    }
                    branch.getBranchReportManager().addNewReport(report);
                    mainController.getReportDao().addReport(report);
                    System.out.println("Adding items to the report has been successfully completed");
                    break;
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
                default:{System.out.println("Please enter a valid number (1-3)");
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
                System.out.println("Please enter a valid number (1-3)");
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
                default:{System.out.println("Please enter a valid number (1-3)");
                    break;}
            }
        }
    }

    private LocalDate validDate() {
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

    public void productUI() throws SQLException {
        Scanner productScanner = new Scanner(System.in);
        int productChoice = 0;
        while (productChoice != 4) {
            System.out.println("Product Menu - Please choose one of the following options : ");
            System.out.println("1. Get product categories by ID ");
            System.out.println("2. Print product details by ID ");
            System.out.println("3. Print all products");
            System.out.println("4. Exit to Inventory menu");
            try {productChoice = productScanner.nextInt();}
            catch (Exception e) {
                System.out.println("Please enter an integer between 1-4 ");
                productScanner.nextLine();
                continue;
            }
            switch (productChoice) {
                case 1 -> getProductCategoriesByID();
                case 2 -> printProductDetailsByID();
                case 3 -> printAllProducts();
                case 4 -> { return; }
                default -> System.out.println("Please enter an integer between 1-4 ");
            }
        }
    }

    private void getProductCategoriesByID() throws SQLException {
        System.out.println("What is the ID of the product for which you would like to get the categories ?  \n");
        int productID = HelperFunctions.positiveItegerInsertion();
        Product product = mainController.getProductController().getProduct(productID);
        if (product == null) {
            System.out.println("We do not have a product in the system with the ID number you provided \n");
            return;
        }
        System.out.println("The product with the ID : " + productID + " is under the following categories:" + "\n");
        Category parent = product.getParentCategory();
        Category sub = product.getSubCategory();
        Category subSub = product.getSubSubCategory();
        System.out.println(parent);
        System.out.println(sub);
        System.out.println(subSub);
    }

    private void printProductDetailsByID() throws SQLException {
        System.out.println("What is the ID of the product for which you would like to get his details ?  \n");
        int productID = HelperFunctions.positiveItegerInsertion();
        Product product = mainController.getProductController().getProduct(productID);
        if (product == null) {
            System.out.println("We do not have a product in the system with the ID number you provided \n");
            return;
        }
        System.out.println(product);
    }

    private void printAllProducts() throws SQLException {
        List<Product> products = mainController.getProductController().getAllProducts();
        if (products == null) {
            System.out.println("We currently have no products in the system \n");
            return;
        }
        System.out.println("The system includes the following products : \n");
        for (Product product : products) {
            System.out.println(product);
        }
    }
    public void categoryUI() throws SQLException
    {
        Scanner categoryScanner = new Scanner(System.in);
        int categoryChoice = 0;
        while (categoryChoice != 3) {
            System.out.println("Category Menu - Please choose one of the following options : ");
            System.out.println("1. Print category details by ID ");
            System.out.println("2. Print all categories");
            System.out.println("3. Exit to Inventory menu");
            try {categoryChoice = categoryScanner.nextInt();}
            catch (Exception e) {
                System.out.println("Please enter an integer between 1-3 ");
                categoryScanner.nextLine();
                continue;
            }
            switch (categoryChoice)
            {
                case 1 -> printCategoryDetailsByID();
                case 2 -> printAllCategories();
                case 3 -> { return; }
                default -> System.out.println("Please enter an integer between 1-3 ");
            }
        }
    }

    private void printCategoryDetailsByID() throws SQLException {
        System.out.println("What is the id of the category you are looking for ? ");
        int categoryID = HelperFunctions.positiveItegerInsertion();
        Category category = mainController.getCategoryController().getCategory(categoryID);
        if (category == null) {
            System.out.println("We do not have a category in the system with the ID number you provided ");
            return;
        }
        List<Product> productsInCategory = mainController.getCategoryController().getProductInCategory(categoryID);
        if (productsInCategory != null)
        {
            category.setProductsToCategory(productsInCategory);
        }
        System.out.println(category);
    }

    private void printAllCategories() throws SQLException {
        List<Category> categories = mainController.getCategoryController().getAllCategories();
        if (categories == null) {
            System.out.println("We currently have no products in the system");
            return;
        }
        System.out.println("The system includes the following categories:");
        for (Category category : categories) {
            List<Product> productsInCategory = mainController.getCategoryController().getProductInCategory(category.getCategoryID());
            if (productsInCategory != null) {category.setProductsToCategory(productsInCategory);}
            System.out.println(category);
            System.out.println("----------------------");
        }
    }

    private void supplierMenu()
    {
        int branchChoice = 0;
        while (branchChoice != 3) {
            System.out.println("Supplier Menu - Please choose one of the following options : ");
            System.out.println("1. Print suppliers ");
            System.out.println("2. Show supplier order history");
            System.out.println("3. Exit to store manager menu ");
            try {
                branchChoice = reader.nextInt();
                reader.nextLine();
            } catch (Exception e) {
                System.out.println("Please Enter a valid number (1-3) ");
                reader.nextLine();
                continue;
            }
            switch (branchChoice) {
                case 1 -> supplierService.printSuppliers();
                case 2 -> supplierOrderHistory();
                default -> System.out.println("Please enter a valid number (1-3)");
            }
        }
    }
    private void supplierOrderHistory() {
        System.out.println("Please Enter Supplier ID: ");
        int supplierID = reader.nextInt();
        reader.nextLine();
        orderService.printOrder(supplierID);
    }
    
}
