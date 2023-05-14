package InterfaceLayer;

import BusinessLayer.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class CliItamar {
    private MainController mainController;

    public CliItamar() {
        mainController = new MainController();
    }
    public void reportUI(Branch branch) throws SQLException {
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
                                int reportID = mainController.getReportDao().getNewReportID();
                                MissingProductsReport report = mainController.getBranchController().getReportController().createNewMissingReport(reportID, branch.getBranchID());
                                branch.getBranchReportManager().addNewReport(report);
                                continue;
                            case 2:
                                MissingProductsReport report_curr = branch.getBranchReportManager().getCurrentMissingReport();
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
                                        mainController.getReportDao().addLineToMissingReport(report_curr.getReportID(), productID, amount);
                                    }
                                    else
                                    {
                                        System.out.println("Unknown product ID. Please try again");
                                    }
                                }
                                branch.getBranchReportManager().setCurrentMissingReport(report_curr);
                                mainController.getReportDao().addReport(report_curr);
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
                                int reportID = mainController.getReportDao().getNewReportID();
                                WeeklyStorageReport report = mainController.getBranchController().getReportController().createNewWeeklyReport(reportID, branch.getBranchID());
                                branch.getBranchReportManager().addNewReport(report);
                                continue;
                            case 2:
                                System.out.println("Adds new category to the current Weekly Storages Report...");
                                WeeklyStorageReport curr = branch.getBranchReportManager().getCurrentWeeklyReport();
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
                                        mainController.getReportDao().addLineToWeeklyReport(curr.getReportID(), categoryID, product.getProductID(), productAmount);
                                    }
                                    curr.addCategoryToReport(category, productCurrAmount);
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
}
