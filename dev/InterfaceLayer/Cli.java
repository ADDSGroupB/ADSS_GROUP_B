package InterfaceLayer;


import java.time.DayOfWeek;
import java.util.*;
//import ServiceLayer.MainService;
//import BusinessLayer.SuppliersBusiness.Supplier;



public class Cli {
    private Scanner reader;
//    private SupplierService supplierService;


    public Cli() {
        reader = new Scanner(System.in);
//        supplierService = new SupplierService();
    }

//    private void loadData() {     // this function will load the data
//        service.loadData();
//    }

    public void print(String message){
        System.out.println(message);
    }


    public void start() {
        int userInput = 0;

        print("Hello! and Welcome to Super Lee- supplier module, I am available to assist you with any requests you may have\nDo you want to upload existing data or start over?  \n1. Load Data\n2. New Data \n3. EXIT\n");
        int n = reader.nextInt();
        //reader.nextLine();
        if (n == 1) {
            // loadData();
        }
        if (n == 3) {
            print("Hope you enjoyed, see you next time :)");
            return;
        }
        //if the user pressed 2 we play the next section
        if (n == 2) {
            while (true) {
                print("Which module are you interested in?  \n1. Supplier\n2. Product\n3. EXIT");
                userInput = reader.nextInt();
                reader.nextLine();
                if (userInput < 1 || userInput > 3) {
                    print("Please choose one of the following options:\n1. Supplier\n2. EXIT");
                    userInput = reader.nextInt();
                    reader.nextLine();
                }
                if (userInput == 1) {
                    supplierManagerCLI();
                }
                if (userInput == 2) {
                    productManagerCLI();
                }
                if (userInput == 3) {
                    print("Hope you enjoyed, see you next time :)");
                    return;
                }
            }
        }
        else{
            print("Input out of range, please choose again");
            start();
        }
    }

    private void supplierManagerCLI() {
        print("Please choose one of the following option:\n1. Add new Supplier\n2. Delete Supplier \n3. Edit Supplier's information \n4. Back");
        int action =0;
        try {
            action = Integer.parseInt(reader.nextLine());
        }
        catch (Exception e){
            print("Illegal Input");
            supplierManagerCLI();
        }
        switch (action) {
            case 1:
                addNewSupplier();
                break;
            case 2:
                deleteSupplier();
                break;
            case 3:
                editSupplier();
                break;
            case 4:
                return;
            default:
                supplierManagerCLI();
        }
    }

    private void editSupplier() {
    }

    private void deleteSupplier() {

    }

    private void addNewSupplier() {
    }



    private void productManagerCLI() {
        print("Please choose one of the following option: \n1. Add new product to supplier \n" +
                "2. Delete product from supplier  \n3. Edit product's information on specific supplier \n" +
                "4. Add quantity to product on supplier \n5. Watch Product Suppliers ID's And CatalogID's \n6. Back");
        int action =0;
        try {
            action = Integer.parseInt(reader.nextLine());
        }
        catch (Exception e){
            print("Illegal Input");
            productManagerCLI();
        }
        switch (action) {
            case 1:
                addNewProductToSupplier();
                break;
            case 2:
//                deleteProductFromSupplier();
                break;
            case 3:
//                updateProductOnSupplier();
                break;
            case 4:
//                addQuantityToProductOnSupplier();
                break;
            case 5:
//                watchProductSuppliersIDsAndCatalogIDs();
                break;
            case 6:
                return;
            default:
                productManagerCLI();
        }
    }

    private void addNewProductToSupplier() {
        int keepAdding = 1;
        while (keepAdding==1) {
            print("Please enter the supplier's id");
            int supplierID = reader.nextInt();
            System.out.println("Please enter product's id");
            int productId = reader.nextInt();
            System.out.println("Please enter product's price");
            int price = reader.nextInt();
            System.out.println("Please enter product's catalog number");
            int catalogNumber = reader.nextInt();
            reader.nextLine();
//            Response res = supplierService.getSupplier(supplierID);
        }

    }
}
