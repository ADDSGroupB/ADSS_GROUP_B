import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.lang.Character;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class UserInterface {

    public UserInterface() {
    }

    public static boolean check_int_in_range(int val, int start, int end){
        /**
       Check if an integer is within a given range.

       Args:
           val (int): The value to check.
           start (int): The start of the range.
           end (int): The end of the range.

       Returns:
           bool: True if the value is within the range, False otherwise.
       */
                return val >= start && val <= end;
            }

            public static boolean check_int_in_length(int real_length, int wanted_length) {
                /**
                Check if the length of an integer is the same as a given length.

                Args:
                    real_length (int): The length of the integer to check.
                    wanted_length (int): The desired length.

                Returns:
                    bool: True if the length is equal to the desired length, False otherwise.
                 */
                return real_length==wanted_length;
            }

            public static boolean check_string_contains_only_letters (String str) {
                /**
                Check if a string contains only letters.

                Args:
                    str (str): The string to check.

                Returns:
                    bool: True if the string contains only letters, False otherwise.
                */
                return str.matches("[a-zA-Z]+");
            }

            public static boolean check_string_contains_only_numbers (String input) {
                /**
                Check if a string contains only numbers.

                Args:
                    input (str): The string to check.

                Returns:
                    bool: True if the string contains only numbers, False otherwise.
                 */
                if (input == null || input.isEmpty()) {
                    return false;
                }
                for (char c : input.toCharArray()) {
                    if (!Character.isDigit(c)) {
                        return false;
                    }
                }
                return true;
            }

            public static String capitalize_first_letter(String str) {
                /**
                Capitalize the first letter of each word in a string.

                Args:
                    str (str): The string to capitalize.

                Returns:
                    str: The capitalized string.
                 */
                StringBuilder result = new StringBuilder();
                String[] words = str.split(" ");
                for (String word : words) {
                    if (!word.isEmpty()) {
                        result.append(Character.toUpperCase(word.charAt(0)));
                        if (word.length() > 1) {
                            result.append(word.substring(1));
                        }
                        result.append(" ");
                    }
                }
                return result.toString().trim();
            }

            public static boolean check_if_driver_id_exist(ArrayList<Driver> all_drivers, String id) {
                /**
                Check if a driver with a given ID exists in a list of drivers.

                Args:
                    all_drivers (list): A list of drivers.
                    id (str): The ID of the driver to check.

                Returns:
                    bool: True if a driver with the given ID exists, False otherwise.
                 */
                for (Driver current_driver : all_drivers) {
                    if (Objects.equals(current_driver.getDriver_ID(), id))
                        return true;
                }
                return false;
            }

            public static boolean check_if_product_code_exist_in_the_system(ArrayList<Product> all_products, String code) {
                /**
                Check if a product with a given code exists in a list of products.

                Args:
                    all_products (list): A list of products.
                    code (str): The code of the product to check.

                Returns:
                    bool: True if a product with the given code exists, False otherwise.
                 */
                for (Product current_product : all_products) {
                    if (Objects.equals(current_product.getProduct_code(), code))
                        return true;
                }
                return false;
            }

            public static boolean check_if_product_code_exist_in_order(TransportOrder order, String code) {
                /**
                Checks if a product with the given product code exists in the items list of the given transport order.

                Parameters:
                order (TransportOrder): The transport order to check.
                code (str): The product code to search for.

                Returns:
                bool: True if a product with the given code exists in the items list of the given transport order, False otherwise.
                 */
                for (Product current_product : order.getItems_list().keySet()){
                    if (Objects.equals(current_product.getProduct_code(), code))
                        return true;
                }
                return false;
            }


            public static HashMap<Product, Integer> get_dry_products(HashMap<Product, Integer> given_products_list) {
                /**
                Returns a new HashMap that contains only the dry products from the given HashMap.

                Parameters:
                given_products_list (HashMap): The HashMap to search for dry products.

                Returns:
                HashMap: A new HashMap that contains only the dry products from the given HashMap.
                 */
                HashMap<Product, Integer> rtn_hash_map = new HashMap<>();
                for (Map.Entry<Product, Integer> set : given_products_list.entrySet()) {
                    if (Objects.equals(set.getKey().getType(), "Dry"))
                        rtn_hash_map.put(set.getKey(), set.getValue());
                }

                return rtn_hash_map;
            }

            public static boolean check_if_order_id_exist(ArrayList<TransportOrder> all_orders, int id) {
                /**
                Checks if a transport order with the given ID exists in the given ArrayList.

                Parameters:
                all_orders (ArrayList): The ArrayList of transport orders to search.
                id (int): The ID of the transport order to search for.

                Returns:
                bool: True if a transport order with the given ID exists in the given ArrayList, False otherwise.

                 */
                for (TransportOrder current_order : all_orders) {
                    if (current_order.getTransport_order_ID() == id)
                        return true;
                }
                return false;
            }


    public void Start_System(){

                boolean continue_main_menu = true;
                boolean visit_option_7 = false;
                DataBase data_base = new DataBase();

                /** Starts the main system interface for a transportation company.

                 The function prompts the user with a menu of options to choose from */

                while (continue_main_menu) {
                    System.out.println("Hello Manager, What would you like to do?");
                    System.out.println("1. Add, Delete or Update a driver, or print all drivers");
                    System.out.println("2. Print details: all active transports / all former transports / all transports documents / all transports orders ");
                    System.out.println("3. Enter a new order from supplier");
                    System.out.println("4. Create a new Transport Document");
                    System.out.println("5. Execute Transport");
                    System.out.println("6. Manage active transport shipments");
                    System.out.println("7. Set up the system with existing data");
                    System.out.println("8. Get out of the system");
                    System.out.println("");

                    int choice_main_menu;
                    Scanner console1 = new Scanner(System.in);
                    try {
                        choice_main_menu = console1.nextInt();
                    } catch (Exception e) {
                        System.out.println("Please enter an integer");
                        continue;
                    }


                    switch (choice_main_menu) {

                        /** first case belong to the drivers */
                        case 1:
                            ;

                            boolean continue_menu_1 = true;

                            /** options for the manager with the drivers */
                            while (continue_menu_1) {

                                System.out.println("a. Add a driver ");
                                System.out.println("b. Delete a driver ");
                                System.out.println("c. Update a driver ");
                                System.out.println("d. print all drivers ");
                                System.out.println("");


                                Scanner console24 = new Scanner(System.in);
                                String choice_menu_1 = console24.next();

                                if (Objects.equals(choice_menu_1, "a")) {

                                    System.out.println("Please enter the details: ");

                                    /** variables that help us to manage the drivers - consider the names of the variables */
                                    String name_insert_for_new_driver;
                                    String ID_insert_for_new_driver;
                                    int max_weight_allowed_insert_for_new_driver;
                                    int driver_license_insert_for_new_driver;

                                    while (true) {

                                        System.out.println("Please enter Name: ");
                                        Scanner console25 = new Scanner(System.in);
                                        name_insert_for_new_driver = console25.next();

                                        if (!check_string_contains_only_letters(name_insert_for_new_driver)) {
                                            System.out.println("Name can contain only letters");
                                        } else {
                                            name_insert_for_new_driver = capitalize_first_letter(name_insert_for_new_driver);
                                            break;
                                        }
                                    }

                                    /** asking for all the details for the driver */
                                    while (true) {
                                        System.out.println("Please enter driver ID [7 characters]: ");
                                        Scanner console26 = new Scanner(System.in);
                                        ID_insert_for_new_driver = console26.next();

                                        if (!check_string_contains_only_numbers(ID_insert_for_new_driver)) {
                                            System.out.println("ID must contain only numbers.");
                                        } else if (ID_insert_for_new_driver.length() != 7) {
                                            System.out.println("ID must be 7 characters");
                                        } else if (check_if_driver_id_exist(data_base.getAllDrivers(), ID_insert_for_new_driver)) {
                                            System.out.println("This driver ID is already in the system.");
                                        } else break;
                                    }

                                    /** asking for all the details for the driver */
                                    while (true) {
                                        System.out.println("Please enter driver max weight allowed: [1000,1500,2000] ");
                                        Scanner console27 = new Scanner(System.in);

                                        try {
                                            max_weight_allowed_insert_for_new_driver = console27.nextInt();
                                        } catch (Exception e) {
                                            System.out.println("Please enter a valid integer");
                                            continue;
                                        }

                                        if (max_weight_allowed_insert_for_new_driver != 1000 && max_weight_allowed_insert_for_new_driver != 1500 && max_weight_allowed_insert_for_new_driver != 2000) {
                                            System.out.println("Driver's max weight allowed can only be 1000 / 1500 / 2000");

                                        } else break;
                                    }

                                    /** asking for all the details for the driver */
                                    while (true) {
                                        System.out.println("Please enter driver license [1/2/3]: ");
                                        Scanner console28 = new Scanner(System.in);
                                        try {
                                            driver_license_insert_for_new_driver = console28.nextInt();
                                        } catch (Exception e) {
                                            System.out.println("Please enter a valid integer");
                                            continue;
                                        }

                                        if (driver_license_insert_for_new_driver != 1 && driver_license_insert_for_new_driver != 2 && driver_license_insert_for_new_driver != 3) {
                                            System.out.println("Driver license can only be 1 / 2 / 3.");
                                        } else break;
                                    }

                                    /** adding the new driver to the data base */
                                    data_base.getAllDrivers().add(new Driver(name_insert_for_new_driver, ID_insert_for_new_driver,
                                            max_weight_allowed_insert_for_new_driver, driver_license_insert_for_new_driver));


                                    continue_menu_1 = false;
                                    break;

                                /** the next option of removing driver */
                                } else if (Objects.equals(choice_menu_1, "b")) {

                                    String ID_driver_to_remove;

                                    /** the manage should choose the driver he wants to remove */
                                    while (true) {
                                        System.out.println("Please enter driver ID you want to remove: ");
                                        Scanner console29 = new Scanner(System.in);
                                        ID_driver_to_remove = console29.next();

                                        if (!check_string_contains_only_numbers(ID_driver_to_remove)) {
                                            System.out.println("ID must contain only numbers.");
                                        } else if (ID_driver_to_remove.length() != 7) {
                                            System.out.println("ID must be 7 characters");
                                        } else if (!check_if_driver_id_exist(data_base.getAllDrivers(), ID_driver_to_remove)) {
                                            System.out.println("This driver ID is not in the system.");
                                        } else break;
                                    }


                                    /** looking for the right driver */
                                    for (Driver driver_to_remove : data_base.getAllDrivers()) {

                                        if (Objects.equals(driver_to_remove.getDriver_ID(), ID_driver_to_remove)) {
                                            if (driver_to_remove.getDriver_status() == DriversStatus.NotAvailable) {
                                                System.out.println(driver_to_remove.getDriver_name() +
                                                        " is currently on a drive and cannot be remove at this moment");
                                                break;
                                            } else {
                                                //  allDrivers.remove(driver_to_remove);
                                                data_base.getAllDrivers().remove(driver_to_remove);
                                                continue_menu_1 = false;
                                                break;
                                            }
                                        }

                                    }
                                    /** the next option of updating driver */
                                } else if (Objects.equals(choice_menu_1, "c")) {

                                    /** the manage should choose the driver he wants to update */
                                    String ID_driver_update;
                                    while (true) {
                                        System.out.println("Please enter driver ID you want to update: ");
                                        Scanner console30 = new Scanner(System.in);
                                        ID_driver_update = console30.next();

                                        if (!check_string_contains_only_numbers(ID_driver_update)) {
                                            System.out.println("ID must contain only numbers.");
                                        } else if (ID_driver_update.length() != 7) {
                                            System.out.println("ID must be 7 characters");
                                        } else if (!check_if_driver_id_exist(data_base.getAllDrivers(), ID_driver_update)) {
                                            System.out.println("This driver ID is not in the system.");
                                        } else break;
                                    }

                                    /** looking for the right driver */
                                    boolean is_working = false;
                                    for (Driver driver_to_update : data_base.getAllDrivers()) {

                                        if (Objects.equals(driver_to_update.getDriver_ID(), ID_driver_update)) {


                                            if (driver_to_update.getDriver_status() == DriversStatus.NotAvailable) {
                                                System.out.println(driver_to_update.getDriver_name() +
                                                        " is currently at a transform, so his details cannot be updated. Please try again latr");
                                                is_working = true;
                                                break;
                                            }
                                            boolean continue_menu_2c = true;

                                            /** asking the manager what he wants to update */
                                            while (continue_menu_2c) {
                                                System.out.println("Please enter what do you want to update: ");
                                                System.out.println("1. Name ");
                                                System.out.println("2. driver status ");
                                                System.out.println("3. driver max weight allowed ");
                                                System.out.println("4. driver license ");
                                                System.out.println("");


                                                Scanner console31 = new Scanner(System.in);
                                                int update_choice;

                                                try {
                                                    update_choice = console31.nextInt();
                                                } catch (Exception e) {
                                                    System.out.println("Please enter a valid integer");
                                                    continue;
                                                }

                                                switch (update_choice) {

                                                    /** updating the name of the driver */
                                                    case 1:
                                                        ;

                                                        String update_name;
                                                        while (true) {
                                                            System.out.println("Please enter the new name: ");
                                                            Scanner console32 = new Scanner(System.in);
                                                            update_name = console32.next();

                                                            if (!check_string_contains_only_letters(update_name)) {
                                                                System.out.println("Name must contain only letters.");
                                                            } else break;
                                                        }
                                                        update_name = capitalize_first_letter(update_name);
                                                        driver_to_update.setDriver_name(update_name);
                                                        System.out.println("The status has been changed successfully ");
                                                        System.out.println("");
                                                        continue_menu_2c = false;
                                                        continue_menu_1 = false;
                                                        break;

                                                    /** updating the status of the driver */
                                                    case 2:
                                                        ;

                                                        System.out.println("The status of the driver is: " + driver_to_update.getDriver_status());

                                                        if (driver_to_update.getDriver_status() == DriversStatus.Available)
                                                            driver_to_update.setDriver_status(DriversStatus.OffShift);
                                                        else
                                                            driver_to_update.setDriver_status(DriversStatus.Available);


                                                        System.out.println("The status has been changed successfully to " + driver_to_update.getDriver_status());
                                                        System.out.println("");
                                                        continue_menu_2c = false;
                                                        continue_menu_1 = false;
                                                        break;

                                                    /** updating the driver max weight allowed */
                                                    case 3:
                                                        ;

                                                        System.out.println("The driver max weight allowed is: " + driver_to_update.getDriver_max_weight_allowed());

                                                        int update_max_weight_allowed;

                                                        while (true) {
                                                            System.out.println("Please enter a new max weight allowed: [1000, 1500, 2000]");
                                                            Scanner console33 = new Scanner(System.in);

                                                            try {
                                                                update_max_weight_allowed = console33.nextInt();
                                                            } catch (Exception e) {
                                                                System.out.println("Please enter a valid integer");
                                                                continue;
                                                            }

                                                            if (update_max_weight_allowed != 1000 && update_max_weight_allowed != 1500 && update_max_weight_allowed != 2000) {
                                                                System.out.println("Max weight allowed must be 1000 / 1500 / 2000");
                                                            } else break;
                                                        }

                                                        /** updating the driver max weight allowed */
                                                        driver_to_update.setDriver_max_weight_allowed(update_max_weight_allowed);

                                                        System.out.println("The driver max weight allowed has been changed successfully ");
                                                        System.out.println("");
                                                        continue_menu_2c = false;
                                                        continue_menu_1 = false;
                                                        break;


                                                    /** updating the driver license */
                                                    case 4:
                                                        ;

                                                        System.out.println("The driver license is: " + driver_to_update.getDriver_license());

                                                        int update_driver_license;
                                                        while (true) {
                                                            System.out.println("Please enter driver license [1/2/3] :");
                                                            Scanner console34 = new Scanner(System.in);

                                                            try {
                                                                update_driver_license = console34.nextInt();
                                                            } catch (Exception e) {
                                                                System.out.println("Please enter a valid integer");
                                                                continue;
                                                            }

                                                            if (update_driver_license != 1 && update_driver_license != 2 && update_driver_license != 3) {
                                                                System.out.println("Driver license must be 1 / 2 / 3");
                                                            } else break;
                                                        }

                                                        /** updating the driver license */
                                                        driver_to_update.setDriver_license(update_driver_license);

                                                        System.out.println("The driver license has been changed successfully ");
                                                        System.out.println("");
                                                        continue_menu_2c = false;
                                                        continue_menu_1 = false;
                                                        break;


                                                    default:
                                                        System.out.println("Wrong option. Please try again");
                                                        break;
                                                }
                                            }

                                        }
                                    }

                                    if (is_working)
                                        break;

                                    /** printing all the drivers in the system */
                                } else if (Objects.equals(choice_menu_1, "d")) {

                                    for (Driver print_driver : data_base.getAllDrivers()) {
                                        System.out.println(print_driver);
                                    }
                                    System.out.println("");
                                    System.out.println("");
                                    continue_menu_1 = false;
                                }
                                /** wrong choice */
                                else
                                {
                                    System.out.println("Your choice is wrong, please try again");
                                }
                            }

                            break;

                        /** option to print details about the transports or orders or documents */
                        case 2:
                            ;

                            /** which details the manager want to print */
                            boolean keep_menu = true;
                            while (keep_menu) {
                                System.out.println("What do you want to see ?");
                                System.out.println("a. Active transports ");
                                System.out.println("b. Former transport's documents");
                                System.out.println("c. Transport Documents ");
                                System.out.println("d. Transport orders ");
                                System.out.println("");


                                Scanner console35 = new Scanner(System.in);
                                String printing_choice = console35.next();

                                /** printing the active transports if exist */
                                if (Objects.equals(printing_choice, "a")) {

                                    for (TransportShipment print_transport : data_base.getActive_transports_shipments()) {

                                        System.out.println(print_transport);
                                    }
                                    keep_menu = false;

                                    if (data_base.getActive_transports_shipments().size() == 0){

                                        System.out.println("There are'nt any active transports");
                                    }
                                    System.out.println("");

                                    /** printing the Former transports if exist */
                                } else if (Objects.equals(printing_choice, "b")) {

                                    for (TransportDocument print_former_transport_document : data_base.getAll_former_transport_documents()) {

                                        System.out.println(print_former_transport_document);
                                    }
                                    keep_menu = false;

                                    if (data_base.getAll_former_transport_documents().size() == 0){

                                        System.out.println("There are'nt any former transports");
                                    }
                                    System.out.println("");

                                    /** printing the transport's documents if exist */
                                } else if (Objects.equals(printing_choice, "c")) {

                                    for (TransportDocument print_transport_document : data_base.getAll_transport_documents_in_the_system()) {

                                        System.out.println(print_transport_document);
                                    }
                                    keep_menu = false;

                                    if (data_base.getAll_transport_documents_in_the_system().size() == 0){

                                        System.out.println("There are'nt any document's transports in the system");
                                    }
                                    System.out.println("");

                                    /** printing the transport's orders if exist */
                                } else if (Objects.equals(printing_choice, "d")) {

                                    for (TransportOrder print_transport_order : data_base.getAll_transport_orders_in_the_system()) {

                                        System.out.println(print_transport_order);
                                    }
                                    keep_menu = false;

                                    if (data_base.getAll_transport_orders_in_the_system().size() == 0){

                                        System.out.println("There are'nt any order's transports in the system");
                                    }
                                    System.out.println("");

                                    /** wrong choice */
                                } else
                                {
                                    System.out.println("Your choice is wrong, please try again");
                                }
                            }
                            break;

                            /** enter a new order from supplier to the system */
                        case 3:
                            ;

                            /** checking if there are any sites in the data base */
                            if (data_base.getAllSites().size() == 0){
                                System.out.println("There are'nt any sites in the system, cannot create a new transport order");
                                System.out.println(" ");
                                break;
                            }
                            Source temp_source = null;
                            Destination temp_destination = null;

                            boolean checker2 = false;
                            boolean checking_legal = true;

                            /** asking for details to fill the order */
                            while (checking_legal) {

                                System.out.println("Please enter source id: ");

                                Scanner console2 = new Scanner(System.in);
                                int input2;
                                try {
                                    input2 = console2.nextInt();
                                } catch (Exception e) {
                                    System.out.println("Please enter a valid integer");
                                    continue;
                                }

                                /** looking for the site the has been chosen by the manager */
                                for (ASite site : data_base.getAllSites()) {
                                    if (site.getID() == input2 && site instanceof Source) {
                                        checker2 = true;
                                        temp_source = (Source) site;
                                    }

                                }

                                if (!checker2) {
                                    System.out.println("Source ID cannot be found in the system");
                                    continue;
                                }
                                checking_legal = false;
                            }

                            checking_legal = true;
                            while (checking_legal) {
                                System.out.println("Please enter destination id: ");
                                checker2 = false;
                                int destination_code_input;
                                Scanner console_1 = new Scanner(System.in);
                                destination_code_input = console_1.nextInt();

                                /** looking for the destination the has been chosen by the manager */
                                for (ASite destination : data_base.getAllSites()) {
                                    if (destination.getID() == destination_code_input && destination instanceof Destination) {
                                        checker2 = true;
                                        temp_destination = (Destination) destination;

                                    }
                                }

                                if (!checker2) {
                                    System.out.println("Destination ID cannot be found in the system");
                                    continue;
                                }
                                checking_legal = false;
                            }

                            /** some variables that help us to build the order - consider the names of the varibles */
                            String input5 = null;
                            String product_code_input, amount_of_product_to_add_to_order;
                            HashMap<Product, Integer> current_order = new HashMap<>();
                            Product new_product = null;
                            checking_legal = true;

                            while (true) {

                                /** adding products to the order */
                                while (checking_legal) {
                                    System.out.println("Please enter product code: ");
                                    Scanner console3 = new Scanner(System.in);
                                    product_code_input = console3.next();

                                    if (!check_string_contains_only_numbers(product_code_input)) {

                                        System.out.println("Your code should be only numbers ");
                                        System.out.println("");
                                        continue;
                                    }

                                    /** the product the manager want to add to the order from the supplier */
                                    new_product = TransportOrder.get_product_according_to_code(data_base.getAllProducts(), product_code_input);

                                    if (new_product == null) {

                                        System.out.println("Your code is not exist, please try again");
                                        System.out.println("");
                                        continue;

                                    }
                                    checking_legal = false;
                                }

                                checking_legal = true;

                                /** asking for the amount of the product the manager just entered in the order */
                                while (checking_legal) {

                                    System.out.println("Please enter amount: ");
                                    Scanner console4 = new Scanner(System.in);
                                    amount_of_product_to_add_to_order = console4.next();

                                    if (!check_string_contains_only_numbers(amount_of_product_to_add_to_order)) {

                                        System.out.println("Your amount should be only numbers ");
                                        continue;
                                    }

                                    int int_amount_of_product_to_add_to_order = Integer.parseInt(amount_of_product_to_add_to_order);

                                    /** adding the product and the amount to the order */
                                    current_order.put(new_product, int_amount_of_product_to_add_to_order);

                                    checking_legal = false;
                                }

                                checking_legal = true;

                                /** checking with the manager if he wants to add more products to the order */
                                while (checking_legal) {
                                    System.out.println("Do you wish to add more products? [Y/N]");
                                    Scanner console5 = new Scanner(System.in);
                                    input5 = console5.next();

                                    if (!Objects.equals(input5, "Y") && !Objects.equals(input5, "N")) {

                                        System.out.println("Your choice is not legal, please try again ");
                                        continue;
                                    }
                                    checking_legal = false;
                                }

                                if (Objects.equals(input5, "Y")) {
                                    checking_legal = true;
                                    continue;
                                } else {
                                    break;
                                }
                            }

                            /** after finish adding all the details and products to the order, making the order from the supplier by the right type - dry/cooler/freezer
                             * checking each product in the order and making orders by their types and keep them in the system */

                            TransportOrder new_transport_order = new TransportOrder(temp_source, temp_destination, current_order);

                            HashMap<Product, Integer> all_dry_products = get_dry_products(new_transport_order.getItems_list());
                            TransportOrder only_dry_order = new TransportOrder(new_transport_order.getSource(), new_transport_order.getDestination(), all_dry_products);

                            HashMap<Product, Integer> all_cooler_products = TransportOrder.get_cooler_products(new_transport_order.getItems_list());
                            TransportOrder only_cooler_order = new TransportOrder(new_transport_order.getSource(), new_transport_order.getDestination(), all_cooler_products);

                            HashMap<Product, Integer> all_freezer_products = TransportOrder.get_freezer_products(new_transport_order.getItems_list());
                            TransportOrder only_freezer_order = new TransportOrder(new_transport_order.getSource(), new_transport_order.getDestination(), all_freezer_products);

                            if (!all_dry_products.isEmpty())
                                data_base.getAll_transport_orders_in_the_system().add(only_dry_order);
                            if (!all_cooler_products.isEmpty())
                                data_base.getAll_transport_orders_in_the_system().add(only_cooler_order);
                            if (!all_freezer_products.isEmpty())
                                data_base.getAll_transport_orders_in_the_system().add(only_freezer_order);

                            break;

                            /** creating a new transport document from the transport order in the system or adding to a exist one */
                        case 4:
                            ;

                            /** cheking if there are any orders in the system */
                            if (data_base.getAll_transport_orders_in_the_system().size() == 0) {
                                System.out.println("There is no transport orders awaiting, so you cant create a new transport document");
                                System.out.println("");
                                break;
                            }
                            int transport_order_choice = 0;
                            ArrayList<Integer> index_list = new ArrayList<Integer>();

                            boolean legal_checking = true;

                            /** asking for order id that the manager want to make a transport document from */
                            while (legal_checking) {
                                System.out.println("Please choose an order to transport:");

                                /** looking for the right transport order */
                                for (int i = 0; i < data_base.getAll_transport_orders_in_the_system().size(); i++) {
                                    System.out.println(i + 1 + ". ");
                                    data_base.getAll_transport_orders_in_the_system().get(i).print_transport_order(data_base.getAll_transport_orders_in_the_system().get(i));
                                }

                                System.out.println("Your choice: ");

                                while (true) {
                                    Scanner console6 = new Scanner(System.in);
                                    try {
                                        transport_order_choice = console6.nextInt();

                                        try {
                                            data_base.getAll_transport_orders_in_the_system().get(transport_order_choice);
                                        }
                                        catch (Exception e) {
                                            System.out.println("Please enter a number from the given list.");
                                        }

                                        break;
                                    }
                                    catch (Exception e) {
                                        System.out.println("Please enter a valid integer");
                                    }

                                }

                                /** showing the order that the manager chose */
                                System.out.println("You order's choice is: ");
                                System.out.println(data_base.getAll_transport_orders_in_the_system().get(transport_order_choice-1));

                                if (transport_order_choice < 1 || transport_order_choice > data_base.getAll_transport_orders_in_the_system().size()) {

                                    System.out.println("Wrong choice, please try again ");
                                    continue;
                                }
                                legal_checking = false;
                            }

                            /** keeping the type of the products in the order that the manager chose */
                            String order_type = data_base.getAll_transport_orders_in_the_system().get(transport_order_choice - 1).getItems_list().entrySet().iterator().next().getKey().getType();

                            String present_choice = null;
                            legal_checking = true;

                            /** asking the manager if he wants to check if he can add the order to an exist transport document in the system */
                            while (legal_checking) {

                                System.out.println("Do you want to check if there any other transport document to add it to? [Y/N]");
                                Scanner console10 = new Scanner(System.in);
                                present_choice = console10.next();

                                if (!Objects.equals(present_choice, "Y") && !Objects.equals(present_choice, "N")) {

                                    System.out.println("Your choice is not legal, please try again ");
                                    continue;
                                }
                                legal_checking = false;
                            }

                            /** if he wants to check if he can add the order to an exist transport document in the system */
                            if (Objects.equals(present_choice, "Y")) {

                                boolean exist_document = false;

                                /** looking for all the transports documents if exist in the system and show him only the relevant documents by the same type */
                                for (int i = 0; i < data_base.getAll_transport_documents_in_the_system().size(); i++) {

                                    if (data_base.getAll_transport_documents_in_the_system().get(i).getTransport_type() == order_type
                                            && data_base.getAll_transport_documents_in_the_system().get(i).getShipping_area() ==
                                            data_base.getAll_transport_orders_in_the_system().get(transport_order_choice - 1).getDestination().getAddress().getShipping_area()) {

                                        exist_document = true;
                                        System.out.println(i + 1 + ". ");
                                        System.out.println(data_base.getAll_transport_documents_in_the_system().get(i));
                                        index_list.add(i);
                                    }
                                }

                                if (!exist_document) {

                                    System.out.println("There aren't matches for you.");

                                } else {

                                    int transport_document_choice_to_add_order = 0;
                                    legal_checking = true;

                                    /** asking the manager to choose the transport document the he wants to add to his order */
                                    while (legal_checking) {
                                        System.out.println("Please choose transport document you want to add to: ");
                                        Scanner console11 = new Scanner(System.in);
                                        transport_document_choice_to_add_order = console11.nextInt();

                                        if (!index_list.contains(transport_document_choice_to_add_order - 1)) {
                                            System.out.println("Wrong choice, please try again ");
                                            continue;
                                        }

                                        legal_checking = false;
                                    }

                                    index_list.clear();
                                    /** update all the system with the orders or documents that has been change - existing change */
                                    data_base.getAll_transport_documents_in_the_system().get(transport_document_choice_to_add_order - 1).add_transport_order(data_base.getAll_transport_orders_in_the_system().get(transport_order_choice - 1));
                                    data_base.getAll_transport_orders_in_the_system().remove(data_base.getAll_transport_orders_in_the_system().get(transport_order_choice - 1));
                                    break;
                                }

                            }

                            System.out.println("Your transport's type is: " + order_type);
                            System.out.println("Please choose a truck: ");
                            boolean checker5 = false;
                            int level_skill = 0;

                            if (Objects.equals(order_type, "Dry"))
                                level_skill = 1;
                            else if (Objects.equals(order_type, "Cooler"))
                                level_skill = 2;
                            else if (Objects.equals(order_type, "Freezer"))
                                level_skill = 3;


                            /** let the manager choose a truck from all the trucks exist */

                            boolean checker4 = false;
                            for (int i = 0; i < data_base.getAllTrucks().size(); i++) {
                                if (data_base.getAllTrucks().get(i).getTruck_level() == level_skill &&
                                        data_base.getAllTrucks().get(i).getStatus() == TruckStatus.Available) {
                                    checker4 = true;
                                    System.out.println(i + 1 + ". ");
                                    System.out.println(data_base.getAllTrucks().get(i));
                                    index_list.add(i);
                                }

                            }

                            if (!checker4) {
                                System.out.println("No available trucks at this moment. Please try again later");
                                break;
                            }

                            int truck_choice = 0;
                            legal_checking = true;
                            while (legal_checking) {
                                System.out.println("Your choice: ");
                                Scanner console7 = new Scanner(System.in);
                                truck_choice = console7.nextInt();

                                if (!index_list.contains(truck_choice - 1)) {
                                    System.out.println("Wrong choice, please try again ");
                                    continue;
                                }
                                legal_checking = false;
                            }

                            index_list.clear();
                            System.out.println("Please choose a driver: ");
                            Driver tempDriver = null;

                            /** let the manager choose a driver from all the drivers exist */

                            for (int i = 0; i < data_base.getAllDrivers().size(); i++) {
                                tempDriver = data_base.getAllDrivers().get(i);

                                if (tempDriver.getDriver_status() == DriversStatus.Available &&
                                        tempDriver.getDriver_license() >= level_skill) {
                                    checker5 = true;
                                    System.out.println(i + 1 + ". ");
                                    System.out.println(data_base.getAllDrivers().get(i));
                                    index_list.add(i);
                                }
                            }

                            if (!checker5) {
                                System.out.println("No available drivers at this moment. Please try again later");
                                break;
                            }

                            int driver_choice = 0;
                            legal_checking = true;
                            while (legal_checking) {

                                while (true) {
                                    Scanner console8 = new Scanner(System.in);
                                    try {
                                        driver_choice = console8.nextInt();
                                        break;
                                    }
                                    catch (Exception e) {
                                        System.out.println("Please enter a valid integer");
                                    }
                                }

                                if (!index_list.contains(driver_choice - 1)) {
                                    System.out.println("Wrong choice, please try again: ");
                                    continue;
                                }
                                legal_checking = false;
                            }

                            /** checking if the driver can drive the specific truck that he just chose */
                            if (data_base.getAllTrucks().get(truck_choice - 1).getNet_weight() > data_base.getAllDrivers().get(driver_choice - 1).getDriver_max_weight_allowed()) {
                                System.out.println("Your driver is not allowed to drive at this truck because of the net weight of the truck");
                                break;
                            }

                            Scanner date_from_user = new Scanner(System.in);

                            /** asking for the date of the transport */
                            System.out.println("Enter the Date of the transport: [dd-MM-yyyy] ");

                            String date = date_from_user.next();

                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                            Date date2 = null;
                            try {
                                date2 = dateFormat.parse(date);

                            } catch (ParseException e) {

                                e.printStackTrace();
                            }


                            legal_checking = true;
                            String departure_time_input = null;
                            LocalTime departureTime = null;

                            /** asking for the exactly time of the transport */

                            while (legal_checking) {
                                System.out.println("Please enter departure time [HH:mm]: ");
                                Scanner console9 = new Scanner(System.in);
                                departure_time_input = console9.next();

                                try {
                                    departureTime = LocalTime.parse(departure_time_input);
                                    System.out.println("Valid departure time: " + departureTime);
                                    System.out.println("");
                                    legal_checking = false;
                                } catch (DateTimeParseException e) {
                                    System.out.println("Invalid input format: " + departure_time_input);
                                    System.out.println("Please try again");
                                }
                            }

                            /** getting the right transport order from the system */
                            TransportOrder transport_order_to_add = data_base.getAll_transport_orders_in_the_system().get(transport_order_choice - 1);

                            /** adding the new transport document to the system */
                            ArrayList<TransportOrder> all_transport_orders_into_document = new ArrayList<TransportOrder>();
                            all_transport_orders_into_document.add(transport_order_to_add);

                            /** looking for the right driver that the manager chose */
                            tempDriver = data_base.getAllDrivers().get(driver_choice - 1);

                            /** making the new transport document and adding it into the system
                             * and removing the old transport order from the system */
                            TransportDocument new_transport_document = new TransportDocument(date2, data_base.getAllTrucks().get(truck_choice - 1).getLicense_plate_number(), departure_time_input, tempDriver, all_transport_orders_into_document, transport_order_to_add.getDestination().getAddress().getShipping_area(), order_type);
                            data_base.getAll_transport_documents_in_the_system().add(new_transport_document);
                            data_base.getAll_transport_orders_in_the_system().remove(transport_order_to_add);

                            /** updating the availability of the driver and the truck */

                            tempDriver.setDriver_status(DriversStatus.NotAvailable);
                            data_base.getAllTrucks().get(truck_choice - 1).setStatus(TruckStatus.NotAvailable);


                            break;

                            /** Executing a transport */
                        case 5:

                            /** checking if there are any transport documents in the system */
                            if (data_base.getAll_transport_documents_in_the_system().size() == 0) {
                                System.out.println("There isn't any transport documents waiting to be execute");
                                System.out.println("");
                                break;
                            }

                            /** asking for a transport after showing the manager all the transport documents int the system */
                            System.out.println("Please choose a transport to execute: ");
                            for (int i = 0; i < data_base.getAll_transport_documents_in_the_system().size(); i++) {
                                System.out.println(i + 1 + ". ");
                                System.out.println(data_base.getAll_transport_documents_in_the_system().get(i));
                            }

                            int document_number;
                            System.out.println("Your choice: ");
                            while (true) {

                                Scanner console11 = new Scanner(System.in);

                                try {
                                    document_number = console11.nextInt();
                                }

                                catch (Exception e) {
                                    System.out.println("Please enter valid integer");
                                    continue;
                                }

                                if (check_int_in_range(document_number, 1, data_base.getAll_transport_documents_in_the_system().size()))
                                    break;
                                else {
                                    System.out.println("Please enter a number in range: ");
                                }
                            }

                            /** looking for the transport document that the manager chose */
                            TransportDocument chosen_document = data_base.getAll_transport_documents_in_the_system().get(document_number - 1);

                            /** removing the transport document that the manager chose from the system and adding it to the formers transport documents in the system */
                            data_base.getAll_transport_documents_in_the_system().remove(document_number - 1);
                            data_base.getAll_former_transport_documents().add(chosen_document);

                            /** creating the transport shipment with all the details the system got from the manager */
                            Source source_of_transport = chosen_document.getAll_transport_orders().get(0).getSource();
                            TransportShipment new_transport_shipment = new TransportShipment(chosen_document.getDriver_name(),
                                    chosen_document.getDate(), chosen_document.getDeparture_time(),
                                    chosen_document.getTruck_license_plate_number(), 0, new HashSet<ASite>(), source_of_transport, chosen_document);

                            /** activate the execute transport function after the system create the transport with all the details*/
                            new_transport_shipment.ExecuteTransport();

                            /** adding the transport to the system */
                            data_base.getActive_transports_shipments().add(new_transport_shipment);

                            System.out.println("Transport executed successfully ");
                            System.out.println("");

                            break;

                        /** Managing an active transport */
                        case 6:
                            ;

                            /** checking size is positive */
                            if (data_base.getActive_transports_shipments().size() == 0) {
                                System.out.println("There is not transport shipments to manage");
                                System.out.println("");
                                break;
                            }

                            /** user choose one shipment from all the active transports at the moment. */
                            System.out.println("Please choose a transport shipment to manage: ");
                            for (int i = 0; i < data_base.getActive_transports_shipments().size(); i++) {
                                System.out.println(i + 1 + ". ");
                                System.out.println(data_base.getActive_transports_shipments().get(i));
                            }


                            System.out.println("Your choice: ");
                            int shipment_number;

                            while (true) {
                                Scanner console12 = new Scanner(System.in);

                                try {
                                    shipment_number = console12.nextInt();

                                    if (!check_int_in_range(shipment_number, 1, data_base.getActive_transports_shipments().size())) {
                                        System.out.println("Please enter number in range");

                                    } else break;

                                } catch (Exception e) {
                                    System.out.println("Please enter a valid integer");

                                }

                            }
                            /** Saving to "chosen_shipment" the active transport the use wants to manage */
                            TransportShipment chosen_shipment_to_manage = data_base.getActive_transports_shipments().get(shipment_number - 1);
                            System.out.println("Details about the shipment you chose: ");
                            System.out.println(chosen_shipment_to_manage.getTransport_document());
                            System.out.println("");



                            System.out.println("Where is the truck? Press B for Branch or S for Supplier");

                            String current_location;
                            while (true) {

                                Scanner console13 = new Scanner(System.in);
                                current_location = console13.next();

                                if (!Objects.equals(current_location, "S") && !Objects.equals(current_location, "B")) {
                                    System.out.println("Please enter only B for branch,or S for Supplier");
                                } else break;
                            }

                            /** saving the truck of the chosen shipment */
                            ATruck current_truck = null;
                            for (ATruck allTruck : data_base.getAllTrucks()) {
                                if (allTruck.getLicense_plate_number() == chosen_shipment_to_manage.getTransport_document().getTruck_license_plate_number())
                                    current_truck = allTruck;
                            }


                            if (current_location.equals("B")) {
                                System.out.println("Did the truck finished it shipment? [Y/N]");
                                String is_shipment_finished;

                                while (true) {
                                    Scanner console14 = new Scanner(System.in);
                                    is_shipment_finished = console14.next();

                                    if (!Objects.equals(is_shipment_finished, "Y") && !Objects.equals(is_shipment_finished, "N")) {
                                        System.out.println("Please enter only Y for yes or N for no");
                                    }
                                    else {
                                        break;
                                    }

                                }

                                /** if the truck is at a branch and hasn't finished the transport, nothing to update  */
                                if (is_shipment_finished.equals("N")) {
                                    break;
                                }

                                else {
                                    /** if the truck is at a branch and finished the transport: */
                                    /** update the array lists as needed */
                                    chosen_shipment_to_manage.FinishTransport(current_truck);
                                    data_base.getActive_transports_shipments().remove(chosen_shipment_to_manage);
                                }

                            } else if (current_location.equals("S")) {
                                System.out.println("Please enter the current weight of the truck");

                                int current_weight;

                                while (true) {
                                    Scanner console15 = new Scanner(System.in);
                                    try {
                                        current_weight = console15.nextInt();
                                        break;
                                    } catch (Exception e) {
                                        System.out.println("Please enter a valid integer");
                                    }
                                }
                                /** if the truck is at a supplier, and after weight, the weight of the truck is legal */
                                if (current_weight <= current_truck.getMax_cargo_weight() + current_truck.getNet_weight()) {
                                    chosen_shipment_to_manage.setCargo_weight(current_weight);
                                    System.out.println("Cargo weight updated.");
                                }
                                else {

                                    System.out.println("Cargo weight is overweight, you need to handle it ");
                                    System.out.println("Please enter order ID: ");

                                    int current_transport_order_id;

                                    while (true) {
                                        Scanner console16 = new Scanner(System.in);
                                        try {
                                            current_transport_order_id = console16.nextInt();

                                            if (!check_if_order_id_exist(chosen_shipment_to_manage.getTransport_document().getAll_transport_orders(), current_transport_order_id)) {
                                                System.out.println("This order is not in that transport document.");
                                                System.out.println("These are the orders that belong to this transport: ");
                                                System.out.println("Please try again: ");
                                                System.out.println(chosen_shipment_to_manage.getTransport_document().getAll_transport_orders());
                                                System.out.println(" ");
                                            }
                                            else
                                                break;

                                        }
                                        catch (Exception e) {
                                            System.out.println("Please enter a valid integer");
                                        }
                                    }
                                    /** showing the manager his 3 options when truck is overweight. */

                                    System.out.println("Please choose one option: ");
                                    System.out.println("1. Remove the supplier from the shipment ");
                                    System.out.println("2. Change truck ");
                                    System.out.println("3. Remove Items ");
                                    System.out.println("");


                                    int solution_choice;

                                    while (true) {
                                        Scanner console17 = new Scanner(System.in);
                                        try {
                                            solution_choice = console17.nextInt();

                                            if (solution_choice != 1 && solution_choice != 2 && solution_choice != 3) {
                                                System.out.println("Invalid option. Please enter 1 / 2 / 3 only");
                                            } else break;
                                        } catch (Exception e) {
                                            System.out.println("Please enter a valid integer");
                                        }
                                    }

                                    TransportOrder current_transport_order = null;

                                    /** saving the current transport order from the chosen shipment that is relavent to this supplier */
                                    for (TransportOrder current_supplier : chosen_shipment_to_manage.getTransport_document().getAll_transport_orders()) {

                                        if (current_supplier.getTransport_order_ID() == current_transport_order_id) {
                                            current_transport_order = current_supplier;
                                            break;
                                        }
                                    }

                                    switch (solution_choice) {

                                        /**  Remove the supplier from the shipment  */
                                        case 1:
                                            ;
                                            /**  removing the supplier and creating a new tranpsort order with the same items  */
                                            chosen_shipment_to_manage.RemoveSupplier(current_transport_order);
                                            data_base.getAll_transport_orders_in_the_system().add(current_transport_order);

                                            break;

                                        /**  change truck  */
                                        case 2:
                                            ;

                                            String transport_type = chosen_shipment_to_manage.getTransport_document().getTransport_type();

                                            int truck_cargo_weight = chosen_shipment_to_manage.getCargo_weight();

                                            int level_skill_type = 1;

                                            if (Objects.equals(transport_type, "Dry"))
                                                level_skill_type = 1;
                                            else if (Objects.equals(transport_type, "Cooler"))
                                                level_skill_type = 2;
                                            else if (Objects.equals(transport_type, "Freezer"))
                                                level_skill_type = 3;


                                            boolean checker20 = false;
                                            Set<Integer> truck_choices = new HashSet<Integer>();

                                            /**  searching for a truck who is available, and has the proper cargo constraint, and is bigger from the current truck  */
                                            for (int i = 0; i < data_base.getAllTrucks().size(); i++) {
                                                if (data_base.getAllTrucks().get(i).getTruck_level() == level_skill_type &&
                                                        Objects.equals(data_base.getAllTrucks().get(i).getStatus(),TruckStatus.Available) &&
                                                        data_base.getAllTrucks().get(i).getStatus().ordinal() == 0 &&
                                                        data_base.getAllTrucks().get(i).getMax_cargo_weight() > truck_cargo_weight) {

                                                    checker20 = true;
                                                    System.out.println(i + 1 + ". ");
                                                    truck_choices.add(i+1);
                                                    System.out.println(data_base.getAllTrucks().get(i));
                                                }
                                            }

                                            if (!checker20) {
                                                System.out.println("Sorry, there aren't available trucks");
                                                break;
                                            }

                                            System.out.println("Your choice: ");
                                            int new_truck_choice;

                                            while (true) {
                                                Scanner console18 = new Scanner(System.in);
                                                try {
                                                    new_truck_choice = console18.nextInt();

                                                    if (!truck_choices.contains(new_truck_choice)) {
                                                        System.out.println("Please choose a number from the list above. ");
                                                    }
                                                    else break;
                                                }
                                                catch (Exception e) {
                                                    System.out.println("Please enter a valid integer");
                                                }
                                            }

                                            System.out.println("The truck you chose: ");
                                            System.out.println(data_base.getAllTrucks().get(new_truck_choice-1));
                                            System.out.println(" ");

                                            /**  searching for a drivers who is available, has a proper license and a proper training for that transport.   */
                                            boolean checker30 = false;
                                            Set<Integer> driver_choices = new HashSet<Integer>();
                                            for (int i = 0; i < data_base.getAllDrivers().size(); i++) {
                                                if (data_base.getAllDrivers().get(i).getDriver_license() >= level_skill_type &&
                                                        Objects.equals(data_base.getAllDrivers().get(i).getDriver_status(), DriversStatus.Available) &&
                                                        data_base.getAllDrivers().get(i).getDriver_status().ordinal() == 0 &&
                                                        data_base.getAllDrivers().get(i).getDriver_max_weight_allowed() >= data_base.getAllTrucks().get(new_truck_choice).getNet_weight()) {

                                                    checker30 = true;
                                                    System.out.println(i + 1 + ". ");
                                                    driver_choices.add(i+1);
                                                    System.out.println(data_base.getAllDrivers().get(i));

                                                }

                                            }

                                            if (!checker30) {
                                                System.out.println("Sorry, there aren't match driver for you");
                                                System.out.println("");
                                                break;
                                            }


                                            System.out.println("Your choice: ");
                                            int new_driver_choice;

                                            while (true) {

                                                Scanner console19 = new Scanner(System.in);
                                                try {
                                                    new_driver_choice = console19.nextInt();
                                                    if (!driver_choices.contains(new_driver_choice)){
                                                        System.out.println("Please choose a number from the list above. ");
                                                    }
                                                    else break;
                                                }
                                                catch(Exception ee){
                                                    System.out.println("Please enter a valid integer");
                                                }
                                            }

                                            System.out.println("Your driver details: ");
                                            System.out.println(data_base.getAllDrivers().get(new_driver_choice - 1));
                                            System.out.println(" ");

                                            /**  the new driver the manager chose  */
                                            Driver new_driver_for_transport = data_base.getAllDrivers().get(new_driver_choice - 1);

                                            /**  updating the old driver is available again  */
                                            chosen_shipment_to_manage.getTransport_document().getDriver().setDriver_status(DriversStatus.Available);

                                            int license_plate_number_old_truck = data_base.getAllTrucks().get(new_truck_choice - 1).getLicense_plate_number();

                                            /**  finding the old truck and update it to available  */
                                            for (ATruck change_truck_status : data_base.getAllTrucks()) {

                                                if (change_truck_status.getLicense_plate_number() == license_plate_number_old_truck) {

                                                    change_truck_status.setStatus(TruckStatus.Available);
                                                }
                                            }

                                            /**  updating all necessary details after the change of the truck  */
                                            chosen_shipment_to_manage.setDriver_name(new_driver_for_transport.getDriver_name());
                                            chosen_shipment_to_manage.getTransport_document().setDriver(new_driver_for_transport);
                                            chosen_shipment_to_manage.setTruck_license_plate_number(data_base.getAllTrucks().get(new_truck_choice).getLicense_plate_number());
                                            chosen_shipment_to_manage.getTransport_document().setTruck_license_plate_number(data_base.getAllTrucks().get(new_truck_choice).getLicense_plate_number());
                                            new_driver_for_transport.setDriver_status(DriversStatus.NotAvailable);
                                            data_base.getAllTrucks().get(new_truck_choice).setStatus(TruckStatus.NotAvailable);
                                            chosen_shipment_to_manage.setCargo_weight(current_weight);

                                            System.out.println("Congratulation the switch completed successfully !");
                                            break;


                                        /**  Remove items  */
                                        case 3:
                                            ;

                                            /**  creating a new transport order object
                                             * all removed items will be in this object
                                             * at the end the order will be added to the system as a new tranpsort order  */
                                            TransportOrder new_transport_order_after_remove_items = new TransportOrder(current_transport_order.getSource(), current_transport_order.getDestination(), new HashMap<Product, Integer>());
                                            boolean keep_remove = true;

                                            while (keep_remove) {

                                                System.out.println("Those are all the items of the current supplier: ");

                                                System.out.println(current_transport_order);

                                                System.out.println("Please enter product's code to remove: ");

                                                String item_code_to_remove;

                                                while (true) {
                                                    Scanner console20 = new Scanner(System.in);
                                                    item_code_to_remove = console20.next();

                                                    if (!check_if_product_code_exist_in_the_system(data_base.getAllProducts(), item_code_to_remove)) {
                                                        System.out.println("This product is not in the system. Please enter another product code");
                                                    } else if (!check_if_product_code_exist_in_order(current_transport_order, item_code_to_remove)) {
                                                        System.out.println("This product is not in this order. Please enter another product code");
                                                    } else break;

                                                }

                                                Product removing_product = null;

                                                /**  saving the product object that the manager wants to remove  */
                                                removing_product = current_transport_order.FindProduct(removing_product, item_code_to_remove);

                                                int current_amount = current_transport_order.getItems_list().get(removing_product);
                                                System.out.println("You have " + current_amount + " " + removing_product.getName());

                                                System.out.println("Please enter amount to remove from this product: ");

                                                int amount_of_item_to_remove;

                                                while (true) {
                                                    Scanner console21 = new Scanner(System.in);
                                                    try {
                                                        amount_of_item_to_remove = console21.nextInt();
                                                        if (amount_of_item_to_remove <= 0 || amount_of_item_to_remove > current_amount) {
                                                            System.out.println("Please enter a legal amount to remove");
                                                        } else break;
                                                    } catch (Exception e) {
                                                        System.out.println("Please enter a valid integer");
                                                    }
                                                }


                                                /** updating the transport order  */
                                                new_transport_order_after_remove_items.Update_amount_of_product(removing_product, amount_of_item_to_remove);

                                                int current_amount_of_item = current_transport_order.getItems_list().get(removing_product);

                                                /**  updating new amount  */
                                                if (current_amount_of_item == amount_of_item_to_remove) {

                                                    current_transport_order.RemoveProduct_FromOrder(removing_product);

                                                } else if (current_amount_of_item > amount_of_item_to_remove) {

                                                    current_transport_order.Update_amount_of_product(removing_product, current_amount_of_item - amount_of_item_to_remove);
                                                }

                                                System.out.println("Do you want to remove more items ? [Y/N]");
                                                Scanner console22 = new Scanner(System.in);
                                                String keep_removing_choice = console22.next();

                                                while (true) {
                                                    if (!Objects.equals(keep_removing_choice, "N") && !Objects.equals(keep_removing_choice, "Y")) {
                                                        System.out.println("Please enter only Y for Yes or N for NO");
                                                    } else break;
                                                }

                                                if (Objects.equals(keep_removing_choice, "N")) {

                                                    System.out.println("Please insert the new cargo weight now: ");

                                                    int new_cargo_weight_of_transport;
                                                    while (true) {
                                                        Scanner console23 = new Scanner(System.in);
                                                        try {
                                                            new_cargo_weight_of_transport = console23.nextInt();
                                                            break;
                                                        } catch (Exception e) {
                                                            System.out.println("Please enter an integer");
                                                        }
                                                    }

                                                    if (new_cargo_weight_of_transport > current_weight) {

                                                        keep_removing_choice = "Y";
                                                    }

                                                    if (keep_removing_choice.equals("N")) {

                                                        /**  adding the new transport order to the system  */
                                                        data_base.getAll_transport_orders_in_the_system().add(new_transport_order_after_remove_items);
                                                        keep_remove = false;
                                                        break;

                                                    }
                                                }
                                            }
                                    }

                                    break;

                                }
                            }

                            break;

                        /** add data to the system */
                        case 7:;
                            if (!visit_option_7) {
                                data_base.buildDataBase();

                                visit_option_7 = true;
                            }
                            else
                                System.out.println("You have already uploaded the system.");
                            break;

                            /** leave the system */
                        case 8:;

                            continue_main_menu = false;
                            break;

                        default:
                            System.out.println("Wrong option. Please try again");
                    }

                }

            }

        }



