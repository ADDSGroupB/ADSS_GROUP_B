package InterfaceLayer;

import BusinessLayer.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
public class Cli {
    private MainController mainController;

    public Cli() {
        mainController = new MainController();
    }
    public MainController getMainController(){return this.mainController;}

    public void start() throws SQLException {
        {
//            LoadData(mainController);
            Scanner scanner = new Scanner(System.in);
            int choice = 0;
            while (choice != 5) {
                System.out.println("What would you like to work on?");
                System.out.println("1. Create new branch");
                System.out.println("2. Work on specific branch");
                System.out.println("3. Products");
                System.out.println("4. Categories");
                System.out.println("5. Exit");
                choice = HelperFunctions.positiveItegerInsertion();
                switch (choice)
                {
                    case 1:
                            createBrunch();
                        break;
                    case 2: {
                        Branch chosenBranch = chooseBranch();
                        Scanner scanner2 = new Scanner(System.in);
                        int choice2 = 0;
                        while (choice2 != 10) {
                            System.out.println("What would you like to work on?");
                            System.out.println("1. Sell Item ");
                            System.out.println("2. Go through and search for damaged or expired products  ");
                            System.out.println("3. Add items ");
                            System.out.println("4. Update danaged item");
                            System.out.println("5. Add new discount for product");
                            System.out.println("6. Add new discount for category");
                            System.out.println("7. Print all items in store");
                            System.out.println("8. Print all items in storage");
                            System.out.println("9. Reports manager");
                            System.out.println("10. Exit to main menu");
                            try {
                                choice2 = scanner.nextInt();
                            } catch (Exception e) {
                                System.out.println("Please enter an integer");
                                scanner.nextLine();
                                continue;
                            }
                            switch (choice2) {
                                case 1: {
                                    System.out.println("What is the id of the product you would like to sell ? ");
                                    Product product = validProduct();
                                    if (mainController.getBranchController().sellItemCase(chosenBranch, product)) {
                                        System.out.println("The item has been successfully sold ");
                                    } else {
                                        System.out.println("We currently have no items of the product you would like to purchase");
                                    }
                                    break;}
                                case 2: {mainController.getBranchController().searchDamagedOrExpired(chosenBranch);
                                    System.out.println("The inspection of damaged and expired products has been successfully completed ");
                                    break;}
                                case 3: {
                                    System.out.println("What is the ID of the product you would like to insert items into ? ");
                                    Product product = validProduct();
                                    checkAddItems(chosenBranch,product);
                                    System.out.println("The items have been successfully added");
                                break;}
                                case 4: {
                                    System.out.println("What is the id of the product you would like to report as defective ? ");
                                    Product product = validProduct();
                                    System.out.println("What is the id of the item you would like to report as defective ? ");
                                    int itemIDdefective = HelperFunctions.positiveItegerInsertion();
                                    if (mainController.getBranchController().checkDamagedItem(chosenBranch,product,itemIDdefective))
                                    {
                                        System.out.println("Please specify the defect in the item : ");
                                        Scanner scannerCheck = new Scanner(System.in);
                                        String discription = scannerCheck.nextLine();
                                        chosenBranch.updateDamagedItem(product, itemIDdefective, discription);
                                        System.out.println("The items have been successfully update");
                                    }
                                    else {
                                        System.out.println("The ID of the item you specified does not exist in the system");
                                    }
                                break;}
                                case 5: {
                                    System.out.println("What is the id of the product you would like to add to him discount ? ");
                                    Product product = validProduct();
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
                                            System.out.print("The start date of the discount must be before the end date of the discount" + "\n");
                                        }
                                        else {isValid=true;}
                                    } while (!isValid);
                                    System.out.println("What percentage of discount is there? ");
                                    double percentage = HelperFunctions.positiveDoubleInsertion();
                                    Discount discount = new Discount(date1,date2,percentage,product);
                                    mainController.getBranchController().addNewDiscountForProduct(chosenBranch,product,discount);
                                    System.out.println("The discount have been successfully added");
                                    break;}
                                case 6: {
                                    System.out.println("What is the id of the category you would like to add to him discount ? ");
                                    Category category = validCategory();
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
                                            System.out.println("The start date of the discount must be before the end date of the discount" + "\n");
                                        }
                                        else {isValid=true;}
                                    } while (!isValid);
                                    System.out.println("What percentage of discount is there? ");
                                    double percentage = HelperFunctions.positiveDoubleInsertion();
                                    Discount discount = new Discount(date1,date2,percentage,category);
                                    mainController.getBranchController().addNewDiscountForCategory(chosenBranch,category,discount);
                                    System.out.println("The discount have been successfully added");
                                    break;}
                                case 7: {
                                    System.out.println(" **Store Items** \n");
                                    chosenBranch.printMapWithItems(chosenBranch.getItemsInStore());
                                break;}
                                case 8:{
                                    System.out.println(" **Storage Items** \n");
                                    chosenBranch.printMapWithItems(chosenBranch.getItemsInStorage());
                                break;}
                                case 9:{reportUI(chosenBranch);
                                    break;}
                                case 10: {
                                    System.out.println("Exiting to main menu");
                                    break;
                                }
                                default:
                                    System.out.println("Invalid choice, please try again");
                                    break;

                            }
                        }
                    }
                    break;
                    case 3:
                    {
                        Scanner scanner3 = new Scanner(System.in);
                        int choice3 = 0;
                        while (choice3 != 7) {
                            System.out.println("What would you like to work on? ");
                            System.out.println("1. Add new product ");
                            System.out.println("2. Change product name by ID ");
                            System.out.println("3. Get product categories by ID ");
                            System.out.println("4. Get product weight by ID ");
                            System.out.println("5. Print product details by ID ");
                            System.out.println("6. Print all products");
                            System.out.println("7. Exit to main menu");
                            try {
                                choice3 = scanner.nextInt();
                            } catch (Exception e) {
                                System.out.println("Please enter an integer");
                                scanner.nextLine();
                                continue;
                            }
                            switch (choice3) {
                                case 1:{
                                    System.out.println("What is the name of the new product ? ");
                                    Scanner scannerCheckPname = new Scanner(System.in);
                                    String newProductName = scannerCheckPname.nextLine();
                                    if (mainController.getProductController().checkProductName(newProductName))
                                    {
                                        System.out.println("What is the manufacturer's name of the new product? ");

                                        String manufacturer = scanner.next();
                                        System.out.println("What is the weight of the new product? (in gr)");
                                        double weightInt = HelperFunctions.positiveDoubleInsertion();
                                        System.out.println("What is the parent category ID of the new product?  ");
                                        Category parent = validCategory();
//                                        int parentInt = HelperFunctions.positiveItegerInsertion();
                                        System.out.println("What is the sub category ID of the new product ? ");
                                        Category sub = validCategory();
//                                        int subInt = HelperFunctions.positiveItegerInsertion();
                                        System.out.println("What is the subSub category ID of the new product ? ");
                                        Category subSub = validCategory();
//                                        int subSubInt = HelperFunctions.positiveItegerInsertion();
                                        Product product = mainController.getProductController().createProduct(newProductName,weightInt,manufacturer,parent,sub,subSub);
                                        mainController.getProductMap().put(product.getProductID(),product);
                                        parent.addProductToCategory(product);
                                        sub.addProductToCategory(product);
                                        subSub.addProductToCategory(product);
                                        System.out.println("The product was created successfully ");
                                    }
                                    else {
                                        System.out.println("The category you want to create is already in the system");
                                    }
                                break;}
                                case 2:{
                                    System.out.println("What is the id of the product you would like to rename ? ");
                                    Product product =validProduct();
                                    System.out.println("What is the new name you would like to give to the product ? ");
                                    Scanner scannerCheckP = new Scanner(System.in);
                                    String productNewName = scannerCheckP.nextLine();
                                    if (mainController.getProductController().checkProductName(productNewName))
                                    {
                                        mainController.getProductController().changeProductName(product,productNewName);
                                        System.out.println("Rename to product completed successfully !");
                                    }
                                    else
                                    {
                                        System.out.println("We already have this category , please try again !");
                                    }
                                break;}
                                case 3:{
                                    System.out.println("What is the id of the product for which you would like to get the categories ? ");
                                    Product product = validProduct();
                                    System.out.println(mainController.getProductController().getProductCategoriesByID(product));
                                break;}
                                case 4:{
                                    System.out.println("What is the id of the product for which you would like to get his weight ? ");
                                    Product product = validProduct();
                                    System.out.println(mainController.getProductController().getProductWeightByID(product));
                                break;}
                                case 5:{
                                    System.out.println("What is the id of the product for which you would like to get his details ? ");
                                    Product product=validProduct();
                                    System.out.println( mainController.getProductController().printProductDetailsByID(product));
                                break;}
                                case 6: {
                                    if (mainController.getProductMap().size() == 0) {
                                        System.out.println("We currently have no products at all");
                                    } else {
                                        mainController.getProductController().printAllProducts();
                                    }
                                }
                                break;
                                case 7:{System.out.println("Exiting to main menu");
                                    break;}
                                default:
                                {System.out.println("Invalid choice, please try again");
                                    break;}
                            }
                            }
                    }
                        mainController.updateCategory();
                        mainController.updateProductInMapsInBranch();
                        break;
                    case 4: {
                        Scanner scanner4 = new Scanner(System.in);
                        int choice4 = 0;
                        while (choice4 != 6) {
                            System.out.println("What would you like to work on?");
                            System.out.println("1. Add new Category");
                            System.out.println("2. Change category name");
                            System.out.println("3. Get category name by id");
                            System.out.println("4. Print all categories");
                            System.out.println("5. Print category details by ID");
                            System.out.println("6. Exit to main menu");
                            try {
                                choice4 = scanner.nextInt();
                            } catch (Exception e) {
                                System.out.println("Please enter an integer");
                                scanner.nextLine(); // clear input buffer
                                continue;
                            }
                            switch (choice4) {
                                case 1: {
                                    System.out.println("What is the name of the new category ? ");
                                    Scanner scannerCheckname = new Scanner(System.in);
                                    String newCategoryName = scannerCheckname.nextLine();
                                    if (mainController.getCategoryController().addNewCategory(newCategoryName))
                                    {
                                        System.out.println("The category has been successfully added");
                                    }
                                    else {
                                        System.out.println("We already have this category , please try again");
                                    }
                                    break;
                                }
                                case 2: {
                                    System.out.println("What is the id of the category you would like to rename ? ");
                                    Category category = validCategory();
                                    System.out.println("What is the new name you would like to give to the category ? ");
                                    Scanner scannerCheck = new Scanner(System.in);
                                    String categoryNewName = scannerCheck.nextLine();
                                    if(mainController.getCategoryController().changeCategoryName(category,categoryNewName))
                                    {
                                        System.out.println("Rename to category completed successfully !");
                                    }
                                    else
                                    {
                                        System.out.println("We already have this category name , please try again !");
                                    }
                                    break;
                                }
                                case 3: {
                                    System.out.println("What is the id of the category you are looking for ? ");
                                    Category category = validCategory();
                                    String categoryName = mainController.getCategoryController().getCategoryNameByID(category);
                                    System.out.println("The name of the category with ID : " + category.getCategoryID() + " is : " + category.getCategoryName());
                                    break;
                                }
                                case 4: {
                                    if (mainController.getCategoryMap().size() ==0)
                                    {
                                        System.out.println("We currently have no categories at all");
                                    }
                                    else {
                                        mainController.getCategoryController().printAllCategories();
                                    }
                                    break;
                                }
                                case 5: {
                                    System.out.println("What is the id of the category you are looking for ? ");
                                    Category category = validCategory();
                                    System.out.println(mainController.getCategoryController().printCategoryDetailsByID(category));
                                    break;
                                }
                                case 6: {
                                    System.out.println("Exiting to main menu");
                                    break;
                                }
                                default: {
                                    System.out.println("Invalid choice, please try again");
                                    break;
                                }
                            }
                        }
                        mainController.updateCategory();
                        mainController.updateProductInMapsInBranch();
                        break;
                    }
                    case 5:
                        break;
                    default:
                        System.out.println("Invalid choice, please try again");
                        break;
                }
            }
        }
    }
    public void createBrunch() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Creating new branch...");
        if (mainController.getBranchMap().size() < 10) {
            System.out.println("What is the name of the branch?");
            String branchName = scanner.next();
            Branch branch = mainController.getBranchesDao().addBranch(branchName);
            if (branch != null){
                mainController.getBranchMap().put(branch.getBranchID(), branch);
                mainController.updateCategory();
                mainController.updateProductInMapsInBranch();
            }
        } else
        {
            System.out.println("We already have ten branches, we cannot open another branch.");
        }
    }
    public Branch chooseBranch() {
        System.out.println("Working on specific branch...");
        int numberOfBranches = mainController.getBranchMap().size();
        if (numberOfBranches == 0) {
            System.out.println("No branches in the system yet");
            return null;
        }
        int branchID;
        while (true) {
            System.out.println("Which branch would you like to work on (1 - " + numberOfBranches + "):");
            branchID = HelperFunctions.positiveItegerInsertion();
            if (branchID < 1 || branchID > numberOfBranches) {
                System.out.println("Invalid choice, please try again");
                continue;
            }
            break;
        }
        return mainController.getBranchMap().get(branchID);
    }
    public Product validProduct()
    {
        Scanner scannerBranch = new Scanner(System.in);
        int productID;
        boolean isValidID = false;
        do {
            productID = HelperFunctions.positiveItegerInsertion();
            if (!mainController.getProductMap().containsKey(productID)) {
                System.out.println("There is no product with the given id. Please insert a new ID :");
            } else {
                isValidID = true;
            }
        }
        while (!isValidID);
        return mainController.getProductMap().get(productID);
    }
    public Category validCategory()
    {
        Scanner scannerBranch = new Scanner(System.in);
        int categoryID;
        boolean isValidID = false;
        do {
            categoryID = HelperFunctions.positiveItegerInsertion();
            if (!mainController.getCategoryMap().containsKey(categoryID)) {
                System.out.println("There is no category with the given id. Please insert a new ID :");
            } else {
                isValidID = true;
            }
        }
        while (!isValidID);
        return mainController.getCategoryMap().get(categoryID);
    }

    public void checkAddItems(Branch branch,Product product) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("How many items do you want to add ? ");
        int quantity = HelperFunctions.positiveItegerInsertion();
        System.out.println("What is the price of the items purchased from the supplier ? ");
        double priceFromSupplier = HelperFunctions.positiveDoubleInsertion();
        System.out.println("What is the price at which the items will sell in the branch ? ");
        double priceInBranch = HelperFunctions.positiveDoubleInsertion();
        System.out.println("What is the id of the supplier from whom the items was purchased ? ");
        int supplierID = HelperFunctions.positiveItegerInsertion();
        String yOrN;
        boolean isValidYoR = false;
        do {
            System.out.print("This item has an expiration date ? (y/n)  ");
            yOrN = scanner.next();
            if (!yOrN.equalsIgnoreCase("n") && !yOrN.equalsIgnoreCase("y")) {
                System.out.println("Invalid choice, please try again");
            } else {
                isValidYoR = true;
            }
        }
        while (!isValidYoR);
        if (yOrN.equalsIgnoreCase("y")) {
            System.out.print("Enter the expiration date(YYYY-MM-DD): ");
            LocalDate date = validDate();
            
            mainController.getBranchController().addItems(branch,product,quantity, branch.getBranchID(), date, priceFromSupplier, priceInBranch, supplierID);
        } else {
            mainController.getBranchController().addItems(branch,product, quantity, branch.getBranchID(), null, priceFromSupplier, priceInBranch, supplierID);
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
    public void reportUI(Branch branch)
    {
        /** Report user interface menu.
         The function prompts the user with a menu of options to choose from */
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
                                LocalDate date = LocalDate.now();
                                MissingProductsReport report = mainController.getBranchController().getReportController().createNewMissingReport(date);
                                branch.getBranchReportManager().addNewReport(report);
                                continue;
                            case 2:
                                MissingProductsReport report_curr = branch.getBranchReportManager().getCurrentMissingReport();
                                System.out.println("Adds new product to the current Missing Products Report...");
                                int productID = 0;
                                while (productID != -1) {
                                    System.out.print("Enter the product ID (or -1 to finish): ");
                                    String productIDstr = scanner.next();
                                    productID = Integer.parseInt(productIDstr);
                                    if (mainController.getProductMap().containsKey(productID))
                                    {
                                        Product product = mainController.getProductMap().get(productID);
                                        System.out.print("Enter the amount to order : ");
                                        String amountStr = scanner.next();
                                        int amount = Integer.parseInt(amountStr);
                                        if (amount > 0) {
                                            report_curr.addMissingProduct(product, amount);
                                        }
                                        else {System.out.println("Quantity not rules. The quantity must be greater than 0");}
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
                                LocalDate date = LocalDate.now();
                                DefectiveProductsReport report = mainController.getBranchController().getReportController().createNewDefectiveReport(date);
                                branch.getBranchReportManager().addNewReport(report);
                                continue;
                            case 2: {
                                DefectiveProductsReport reprt_curr = branch.getBranchReportManager().getCurrentDefectiveReport();
                                System.out.println("Updating the damaged items report...");
                                if (branch.getExpiredItems().size() == 0 && branch.getDemagedItems().size() == 0) {
                                    System.out.println("We currently have no damaged items to report...");
                                } else {
                                    reprt_curr = mainController.getBranchController().getReportController().updateProductsInReport(reprt_curr, branch);
                                    System.out.println("The update was successful");
                                }
                                branch.getBranchReportManager().setCurrentDefectiveReport(reprt_curr);
                                break;
                            }
                            case 3:
                                if (branch.getBranchReportManager().getCurrentDefectiveReport() != null) {
                                    System.out.println(branch.getBranchReportManager().getCurrentDefectiveReport().toString());
                                } else System.out.println("Missing Items Report has not been created yet");
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
                                LocalDate date = LocalDate.now();
                                WeeklyStorageReport report = mainController.getBranchController().getReportController().createNewWeeklyReport(date);
                                branch.getBranchReportManager().addNewReport(report);
                                continue;
                            case 2:
                                System.out.println("Adds new category to the current Weekly Storages Report...");
                                WeeklyStorageReport curr= branch.getBranchReportManager().getCurrentWeeklyReport();
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
                                    if (!mainController.getCategoryMap().containsKey(categoryID)) {
                                        System.out.println("Unknown category ID. Please try again");
                                        scanner.nextLine(); // clear input buffer
                                        continue;
                                    }
                                    branch.getBranchReportManager().getCurrentWeeklyReport().addCategoryToReport(mainController.getCategoryMap().get(categoryID));
                                }
                                branch.getBranchReportManager().setCurrentWeeklyReport(curr);
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




//    public void LoadData(MainController m)
//    {
//        if (m== null) return;
//        // Branches - open 5 branches - but we can hold 10 branches
//        Branch branch1 = new Branch("Super1");
//        Branch branch2 = new Branch("Super2");
//        Branch branch3 = new Branch("Super3");
//        Branch branch4 = new Branch("Super4");
//        Branch branch5 = new Branch("Super5");
//
//        m.getBranchMap().put(branch1.getBranchID(),branch1);
//        m.getBranchMap().put(branch2.getBranchID(),branch2);
//        m.getBranchMap().put(branch3.getBranchID(),branch3);
//        m.getBranchMap().put(branch4.getBranchID(),branch4);
//        m.getBranchMap().put(branch5.getBranchID(),branch5);
//
//        // Categories
//
//        Category MotzariHalav = new Category("MotzariHalav");
//        Category MilkCat = new Category("Milk Category");
//        Category fat3 = new Category("3% fat");
//        Category CookingUtensils = new Category("CookingUtensils");
//        Category Pans = new Category("Pans");
//        Category Twentycm = new Category("20cm");
//        Category vegetables = new Category("vegetables");
//        Category groundVegetables = new Category("groundVegetables");
//        Category Onions = new Category("Onions");
//        Category fruits = new Category("fruits");
//        Category RedFruits = new Category("RedFruits");
//        Category strawberries = new Category("strawberries");
//
//        m.getCategoryMap().put(MotzariHalav.getCategoryID(), MotzariHalav);
//        m.getCategoryMap().put(MilkCat.getCategoryID(), MilkCat);
//        m.getCategoryMap().put(fat3.getCategoryID(), fat3);
//        m.getCategoryMap().put(CookingUtensils.getCategoryID(), CookingUtensils);
//        m.getCategoryMap().put(Pans.getCategoryID(), Pans);
//        m.getCategoryMap().put(Twentycm.getCategoryID(), Twentycm);
//        m.getCategoryMap().put(vegetables.getCategoryID(), vegetables);
//        m.getCategoryMap().put(groundVegetables.getCategoryID(), groundVegetables);
//        m.getCategoryMap().put(Onions.getCategoryID(), Onions);
//        m.getCategoryMap().put(fruits.getCategoryID(), fruits);
//        m.getCategoryMap().put(RedFruits.getCategoryID(), RedFruits);
//        m.getCategoryMap().put(strawberries.getCategoryID(), strawberries);
//
//        // Products
//        Product p1 = new Product("Milk 3%","Tara" , 500,MotzariHalav,MilkCat,fat3);
//        MotzariHalav.addProductToCategory(p1);
//        MilkCat.addProductToCategory(p1);
//        fat3.addProductToCategory(p1);
//        Product p2 = new Product("Black Pan","Arcos" , 1000,CookingUtensils,Pans,Twentycm);
//        CookingUtensils.addProductToCategory(p2);
//        Pans.addProductToCategory(p2);
//        Twentycm.addProductToCategory(p2);
//        Product p3 = new Product("White Onion","vegandfruit" , 20,vegetables,groundVegetables,Onions);
//        vegetables.addProductToCategory(p3);
//        groundVegetables.addProductToCategory(p3);
//        Onions.addProductToCategory(p3);
//        Product p4 = new Product("Purple Onion","vegandfruit" , 20,vegetables,groundVegetables,Onions);
//        vegetables.addProductToCategory(p4);
//        groundVegetables.addProductToCategory(p4);
//        Onions.addProductToCategory(p4);
//        Product p5 = new Product("Green Onion","vegandfruit" , 10,vegetables,groundVegetables,Onions);
//        vegetables.addProductToCategory(p5);
//        groundVegetables.addProductToCategory(p5);
//        Onions.addProductToCategory(p5);
//        Product p6 = new Product("Camino Real","vegandfruit" , 150,fruits,RedFruits,strawberries);
//        fruits.addProductToCategory(p6);
//        RedFruits.addProductToCategory(p6);
//        strawberries.addProductToCategory(p6);
//        Product p7 = new Product("Earliglow","vegandfruit" , 200,fruits,RedFruits,strawberries);
//        fruits.addProductToCategory(p7);
//        RedFruits.addProductToCategory(p7);
//        strawberries.addProductToCategory(p7);
//
//        // set min amount for each product
//        m.getProductMap().put(p1.getProductID(), p1);
//        m.getProductMap().put(p2.getProductID(), p2);
//        m.getProductMap().put(p3.getProductID(), p3);
//        m.getProductMap().put(p4.getProductID(), p4);
//        m.getProductMap().put(p5.getProductID(), p5);
//        m.getProductMap().put(p6.getProductID(), p6);
//        m.getProductMap().put(p7.getProductID(), p7);
//
//        branch1.updateMinProductAmount(p1,10);
//        branch1.updateMinProductAmount(p2,10);
//        branch1.updateMinProductAmount(p3,10);
//        branch1.updateMinProductAmount(p4,10);
//        branch1.updateMinProductAmount(p5,10);
//        branch1.updateMinProductAmount(p6,10);
//        branch1.updateMinProductAmount(p7,10);
//
//        branch2.updateMinProductAmount(p1,10);
//        branch2.updateMinProductAmount(p2,10);
//        branch2.updateMinProductAmount(p3,10);
//        branch2.updateMinProductAmount(p4,10);
//        branch2.updateMinProductAmount(p5,10);
//        branch2.updateMinProductAmount(p6,10);
//        branch2.updateMinProductAmount(p7,10);
//
//        branch3.updateMinProductAmount(p1,10);
//        branch3.updateMinProductAmount(p2,10);
//        branch3.updateMinProductAmount(p3,10);
//        branch3.updateMinProductAmount(p4,10);
//        branch3.updateMinProductAmount(p5,10);
//        branch3.updateMinProductAmount(p6,10);
//        branch3.updateMinProductAmount(p7,10);
//
//        branch4.updateMinProductAmount(p1,10);
//        branch4.updateMinProductAmount(p2,10);
//        branch4.updateMinProductAmount(p3,10);
//        branch4.updateMinProductAmount(p4,10);
//        branch4.updateMinProductAmount(p5,10);
//        branch4.updateMinProductAmount(p6,10);
//        branch4.updateMinProductAmount(p7,10);
//
//        branch5.updateMinProductAmount(p1,10);
//        branch5.updateMinProductAmount(p2,10);
//        branch5.updateMinProductAmount(p3,10);
//        branch5.updateMinProductAmount(p4,10);
//        branch5.updateMinProductAmount(p5,10);
//        branch5.updateMinProductAmount(p6,10);
//        branch5.updateMinProductAmount(p7,10);
//
//
//        m.updateCategory();
//        m.updateProductInMapsInBranch();
//
//        // add items of product for each branch
//        LocalDate p1Date = LocalDate.of(2023,10,28);
//        branch1.fromSupplierToStorage(p1, 15, branch1.getBranchID(),LocalDate.now() ,p1Date, 5, 10, 123);
//        branch2.fromSupplierToStorage(p1, 15, branch2.getBranchID(),LocalDate.now() , p1Date, 5, 10, 123);
//        branch3.fromSupplierToStorage(p1, 15, branch3.getBranchID(),LocalDate.now() , p1Date, 5, 10, 123);
//        branch4.fromSupplierToStorage(p1, 15, branch4.getBranchID(),LocalDate.now() , p1Date, 5, 10, 123);
//        branch5.fromSupplierToStorage(p1, 15, branch5.getBranchID(),LocalDate.now() , p1Date, 5, 10, 123);
//
//        LocalDate p2Date = LocalDate.of(2023,9,28);
//        branch1.fromSupplierToStorage(p2, 15, branch1.getBranchID(),LocalDate.now() , p2Date, 2, 6, 611);
//        branch2.fromSupplierToStorage(p2, 15, branch2.getBranchID(),LocalDate.now() , p2Date, 2, 6, 611);
//        branch3.fromSupplierToStorage(p2, 15, branch3.getBranchID(),LocalDate.now() , p2Date, 2, 6, 611);
//        branch4.fromSupplierToStorage(p2, 15, branch4.getBranchID(), LocalDate.now() ,p2Date, 2, 6, 611);
//        branch5.fromSupplierToStorage(p2, 15, branch5.getBranchID(),LocalDate.now() , p2Date, 2, 6, 611);
//
//        LocalDate p3Date = LocalDate.of(2023,11,11);
//        branch1.fromSupplierToStorage(p3, 15, branch1.getBranchID(),LocalDate.now() , p3Date, 4, 9, 11);
//        branch2.fromSupplierToStorage(p3, 15, branch2.getBranchID(),LocalDate.now() , p3Date, 4, 9, 11);
//        branch3.fromSupplierToStorage(p3, 15, branch3.getBranchID(), LocalDate.now() ,p3Date, 4, 9, 11);
//        branch4.fromSupplierToStorage(p3, 15, branch4.getBranchID(),LocalDate.now() , p3Date, 4, 9, 11);
//        branch5.fromSupplierToStorage(p3, 15, branch5.getBranchID(),LocalDate.now() , p3Date, 4, 9, 11);
//
//        LocalDate p4Date = LocalDate.of(2023,12,23);
//        branch1.fromSupplierToStorage(p4, 15, branch1.getBranchID(),LocalDate.now() , p4Date, 15, 25, 211);
//        branch2.fromSupplierToStorage(p4, 15, branch2.getBranchID(),LocalDate.now() , p4Date, 15, 25, 211);
//        branch3.fromSupplierToStorage(p4, 15, branch3.getBranchID(),LocalDate.now() , p4Date, 15, 25, 211);
//        branch4.fromSupplierToStorage(p4, 15, branch4.getBranchID(),LocalDate.now() , p4Date, 15, 25, 211);
//        branch5.fromSupplierToStorage(p4, 15, branch5.getBranchID(),LocalDate.now() , p4Date, 15, 25, 211);
//
//        LocalDate p5Date = LocalDate.of(2023,2,18);
//        branch1.fromSupplierToStorage(p5, 15, branch1.getBranchID(),LocalDate.now() , p5Date, 15, 25, 211);
//        branch2.fromSupplierToStorage(p5, 15, branch2.getBranchID(),LocalDate.now() , p5Date, 15, 25, 211);
//        branch3.fromSupplierToStorage(p5, 15, branch3.getBranchID(),LocalDate.now() , p5Date, 15, 25, 211);
//        branch4.fromSupplierToStorage(p5, 15, branch4.getBranchID(),LocalDate.now() , p5Date, 15, 25, 211);
//        branch5.fromSupplierToStorage(p5, 15, branch5.getBranchID(),LocalDate.now() , p5Date, 15, 25, 211);
//
//        LocalDate p6Date = LocalDate.of(2023,5,18);
//        branch1.fromSupplierToStorage(p6, 15, branch1.getBranchID(),LocalDate.now() , p6Date, 15, 25, 211);
//        branch2.fromSupplierToStorage(p6, 15, branch2.getBranchID(),LocalDate.now() , p6Date, 15, 25, 211);
//        branch3.fromSupplierToStorage(p6, 15, branch3.getBranchID(),LocalDate.now() , p6Date, 15, 25, 211);
//        branch4.fromSupplierToStorage(p6, 15, branch4.getBranchID(),LocalDate.now() , p6Date, 15, 25, 211);
//        branch5.fromSupplierToStorage(p6, 15, branch5.getBranchID(),LocalDate.now() , p6Date, 15, 25, 211);
//
//        LocalDate p7Date = LocalDate.of(2023,6,18);
//        branch1.fromSupplierToStorage(p7, 15, branch1.getBranchID(),LocalDate.now() , p7Date, 12, 33, 21);
//        branch2.fromSupplierToStorage(p7, 15, branch2.getBranchID(),LocalDate.now() , p7Date, 12, 33, 21);
//        branch3.fromSupplierToStorage(p7, 15, branch3.getBranchID(),LocalDate.now() , p7Date, 12, 33, 21);
//        branch4.fromSupplierToStorage(p7, 15, branch4.getBranchID(),LocalDate.now() , p7Date, 12, 33, 21);
//        branch5.fromSupplierToStorage(p7, 15, branch5.getBranchID(),LocalDate.now() , p7Date, 12, 33, 21);
//
//    }



}
