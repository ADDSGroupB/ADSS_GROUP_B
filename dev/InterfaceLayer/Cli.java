package InterfaceLayer;

import ServiceLayer.ServiceAgreement;
import ServiceLayer.SupplierProductService;
import ServiceLayer.SupplierService;
import Utillity.Response;

import java.time.DayOfWeek;
import java.util.*;
//import ServiceLayer.MainService;
//import BusinessLayer.SuppliersBusiness.Supplier;



public class Cli {
    private Scanner reader;
    private SupplierService supplierService;


    public Cli() {
        reader = new Scanner(System.in);
        supplierService = new SupplierService();
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
        int keepAdding = 1;
        while (keepAdding==1) {
            print("Please enter the supplier's name");
            String name = reader.nextLine();
            print("Please enter the supplier's address");
            String address = reader.nextLine();
            print("Please enter the supplier's bank account number");
            String bankAccount = reader.nextLine();
            ServiceAgreement serviceAgreement = createAgreement();
            Response res = supplierService.addSupplier(name, address, bankAccount, serviceAgreement);
            if (!res.errorOccurred()) {
                print("Supplier added successfully, supplier's id is: " + res.getSupplierId());
            }
            else{
                print(res.getErrorMessage());
            }
            print("Would you like to add another supplier?  \n1. Yes\n2. No");
            keepAdding = reader.nextInt();
            reader.nextLine();
        }
    }

    private ServiceAgreement createAgreement() {
        String paymentMethod = choosePaymentMethod();
        boolean selfSupply;
        ArrayList<DayOfWeek> days = new ArrayList<>();
        System.out.println("Choose one of the following Supply methods according to the supplier's agreement \n1. By Days \n2. By Order \n3. By Super-Lee");
        int userInput = reader.nextInt();
        reader.nextLine();
        while (userInput < 1 || userInput > 3) {
            System.out.println("Please Enter a valid number (1,2,3)");
            userInput = reader.nextInt();
            reader.nextLine();
        }
        //supplying in specific days
        if (userInput == 1) {
            selfSupply = true;
            int day = 0;
            System.out.println("please choose suppling days");
            System.out.println("\n1. Sunday \n2. Monday \n3. Tuesday \n4. Wednesday \n5. Thursday \n6. Friday \n7. Saturday \n8.That will be all...");
            while (day != 8) {
                day = reader.nextInt();
                reader.nextLine();
                if (day >= 1 && day <= 7)
                    days.add(DayOfWeek.of(day));
                else if (day != 8)
                    System.out.println("Please enter a valid number : 1 to 7, or 8 if you done adding days");
            }
        }
        //supplying by order
        else if (userInput == 2){
            selfSupply = true;
        }
        ////userinput=3 , superLee supply
        else
            selfSupply = false;

        System.out.println("Would you like to add Items Specifications to the agreement? \n1. Yes\n2. No");
        int keepAdding = reader.nextInt();

        reader.nextLine();
        HashMap<Integer, SupplierProductService> items = new HashMap<>();
//        if (keepAdding == 1)
//            printAllProducts();
        while (keepAdding == 1) {
            System.out.println("Please enter product's id");
            int productId = reader.nextInt();
            System.out.println("Please enter product's price");
            int price = reader.nextInt();
            System.out.println("Please enter product's catalog number");
            int catalogNumber = reader.nextInt();
            reader.nextLine();
            System.out.println("Ok, now please add Discounts according to the format:amount:Discount in percentages,amount:Discount in percentages,..");
            String[] arr = reader.nextLine().split("\\s*,\\s*");
            HashMap<Integer, Double> discounts = new HashMap<>();
            for(String s1 : arr) {
                String[] val = s1.split(":");
                int key = Integer.parseInt(val[0]);
                double value = Double.parseDouble(val[1]);
                discounts.put(key, value);}
            SupplierProductService newProduct = new SupplierProductService(productId, catalogNumber, price, discounts);
            items.put(catalogNumber, newProduct);
            System.out.println("Would you like to add another Item? \n1. Yes\n2. No");
            keepAdding = reader.nextInt();
            reader.nextLine();

        }
        return new ServiceAgreement(paymentMethod, selfSupply, days, items);
    }



    //            System.out.println("Please enter the item in the following format:");
//            System.out.println("product ID, price, catalog number, amount:Discount percentages,amount:Discount percentages,...,amount:Discount percentages");
//           // String[] arr = reader.nextLine().split(",");
//            List<String> discounts = new ArrayList<>();
//            if (arr.length < 3) {
//                System.out.println("Please enter the item according to the format mentioned above!");
//            } else {
//                if (arr.length > 3) {
//                    discounts = Arrays.stream(Arrays.copyOfRange(arr, 3, arr.length)).collect(Collectors.toList());
//                }
//                ServiceProductS itemS = new ServiceProductS(Integer.parseInt(arr[0].trim()), arr[1].trim(), arr[2].trim(), discounts);
//                itemSpecMap.put(arr[2], itemS);
//                System.out.println("Would you like to add another Item? \n1. Yes\n2. No");
//                keepAdding = reader.nextInt();
//                reader.nextLine();
//            }
//        }
//    }//end create agreement
    private String choosePaymentMethod() {
        System.out.println("please choose  Payment method according to the supplier's agreement \n1. Cash \n2. TransitToAccount \n3. Credit\n4. net 30 EOM \n5. net 60 EOM");
        int paymentMethod = reader.nextInt();
        reader.nextLine();
        while (paymentMethod < 1 || paymentMethod > 5) {
            System.out.println("Please Enter a valid number (1 to 5)");
            paymentMethod = reader.nextInt();
            reader.nextLine();
        }
        switch (paymentMethod) {
            case 1:
                return "Cash";
            case 2:
                return "TransitToAccount";
            case 3:
                return "Credit";
            case 4:
                return "net 30 EOM";
            case 5:
                return "net 60 EOM";
            default:
                choosePaymentMethod();
        }
        return "";
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
            Response res = supplierService.getSupplier(supplierID);
        }


    }
