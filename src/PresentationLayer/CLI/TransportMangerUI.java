package PresentationLayer.CLI;

import BuisnessLayer.TransportManager.HelperFunctions;
import BuisnessLayer.TransportManager.*;
import BuisnessLayer.HR.Driver;
import ServiceLayer.TransportManager.TransportService;
import javafx.util.Pair;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.time.DayOfWeek;
import java.time.temporal.TemporalAdjusters;

public class TransportMangerUI {

    HelperFunctions helperFunctions;
    TransportService transportService;

    public TransportMangerUI() throws SQLException {

        helperFunctions = new HelperFunctions();
        transportService = new TransportService();
    }

    public void Start_System() {


        boolean continue_main_menu = true;
        boolean visit_option_7 = false;
        HelperFunctions helperFunctions = new HelperFunctions();


        /** Starts the main system interface for a transportation company.

         The function prompts the user with a menu of options to choose from */

        while (continue_main_menu) {
            System.out.println("Hello Manager, What would you like to do?");
            System.out.println("1. Add or Remove a truck or Print all trucks or change truck status");
            System.out.println("2. Print details: all active transports / all former transports / all transports documents / all transports orders ");
            System.out.println("3. Enter a new order from supplier");
            System.out.println("4. Create a new Transport Document");
            System.out.println("5. Execute Transport");
            System.out.println("6. Manage active transport shipments");
            System.out.println("7. Reset system data to a starting position");
            System.out.println("8. Return to login menu");
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
                    Manage_Trucks();
                    break;

                /** option to print details about the transports or orders or documents */
                case 2:
                    ;
                    System_Details();
                    break;

                /** enter a new order from supplier to the system */
                case 3:
                    ;
                    enter_new_TransportOrder();
                    break;

                /** creating a new transport document from the transport order in the system or adding to a exist one */
                case 4:
                    ;
                    creating_new_transport_document();
                    break;

                /** Executing a transport */
                case 5:
                    ;
                    execute_transport();
                    break;

                /** Managing an active transport */
                case 6:
                    ;
                    Manage_Active_Transports();
                    break;

                /** add data to the system */
                case 7:
                    ;
                    transportService.build();
                    break;

                /** leave the system */
                case 8:
                    ;
                    continue_main_menu = false;
                    break;

                default:
                    System.out.println("Wrong option. Please try again");
            }

        }

    }

    //case 1
    public void Manage_Trucks() {

        boolean continue_menu_1 = true;

        /** options for the manager with the drivers */
        while (continue_menu_1) {

            System.out.println("1. Add a truck ");
            System.out.println("2. Remove a truck ");
            System.out.println("3. print all trucks ");
            System.out.println("4. change truck status ");
            System.out.println("");

            int choice_main_menu_1;
            Scanner console24 = new Scanner(System.in);

            try {
                choice_main_menu_1 = console24.nextInt();
                switch (choice_main_menu_1) {

                    case 1:
                        ;
                        /** the option od adding a new truck to the system */
                        addTruck();
                        continue_menu_1 = false;
                        break;

                    case 2:
                        ;
                        /** the next option of removing truck */
                        removeTruck();
                        continue_menu_1 = false;
                        break;

                    case 3:
                        ;
                        /** printing all the trucks in the system */
                        print_all_trucks();
                        continue_menu_1 = false;
                        break;

                    case 4:
                        /** changing a truck current status*/
                        switch_truck_status();
                        continue_menu_1 = false;
                        break;
                    default:
                        System.out.println("Wrong option. Please try again");
                }
            } catch (Exception e) {
                System.out.println("Please enter an integer");
            }
        }
    }

    //case 2
    public void System_Details() {

        /** which details the manager want to print */
        boolean keep_menu = true;
        while (keep_menu) {
            System.out.println("What do you want to see ?");
            System.out.println("1. Active transports ");
            System.out.println("2. Former transport's documents");
            System.out.println("3. Transport documents ");
            System.out.println("4. Transport orders ");
            System.out.println("");

            int printing_choice;
            Scanner console35 = new Scanner(System.in);
            try {
                printing_choice = console35.nextInt();

                switch (printing_choice) {

                    case 1:
                        ;
                        /** printing the active transports if exist */
                        print_all_active_transports();
                        keep_menu = false;
                        break;

                    case 2:
                        ;
                        /** printing the transport's documents former if exist */
                       print_all_former_transport_documents();
                        keep_menu = false;
                        break;

                    case 3:
                        ;
                        /** printing the transport's documents if exist */
                        print_all_transport_documents();
                        keep_menu = false;
                        break;

                    case 4:
                        ;
                        /** printing the transport's documents if exist */
                        print_all_transport_orders();
                        keep_menu = false;
                        break;

                    default:
                        System.out.println("Wrong option. Please try again");
                }
            } catch (Exception e) {
                System.out.println("Please enter an integer");
            }
        }
    }

    //case 3
    public void enter_new_TransportOrder() {

        /** checking if there are any sites in the database */
        if (!transportService.check_if_existing_any_Site_Controller()) {
            System.out.println("There are currently no sites in the system, so there is no option for you to create a new transport order.");
            return;
        }
        boolean checking_legal = true;
        /** asking for details to fill the order */
        Source find_source = null;
        Destination find_dest = null;
        while (checking_legal) {

            try {
                System.out.println("Please enter source id: ");
                Scanner console2 = new Scanner(System.in);
                int source_code_input = console2.nextInt();

                System.out.println("Please enter destination id: ");
                Scanner console_1 = new Scanner(System.in);
                int destination_code_input = console_1.nextInt();

                find_source = (Source) transportService.find_source_Controller(source_code_input);
                if (find_source == null) {
                    System.out.println("Source ID cannot be found in the system, please try again");
                    continue;
                }
                find_dest = (Destination) transportService.find_destination_Controller(destination_code_input);
                if (find_dest == null) {
                    System.out.println("Destination ID cannot be found in the system, please try again");
                    continue;
                }
                checking_legal = false;

            } catch (Exception e) {
                System.out.println("One of the details is invalid. Please try again.");
            }
        }

        HashMap<Product, Integer> current_order = product_for_order();
        /** after finish adding all the details and products to the order, making the order from the supplier by the right type - dry/cooler/freezer
         * checking each product in the order and making orders by their types and keep them in the system */
        transportService.creating_new_transport_order_by_products_type_Controller(find_source, find_dest, current_order);
    }

    //case 6
    public void Manage_Active_Transports() {

        /** checking size is positive */
        if (transportService.getAll_transport_shipments().size() == 0) {
            System.out.println("There is no transport shipments to manage");
            System.out.println("");
            return;
        }
        TransportShipment chosen_shipment_to_manage = chosen_transport_shipment();

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
        ATruck current_truck = transportService.get_truck_of_transport(chosen_shipment_to_manage.getTransport_document().getTruck_license_plate_number());

        if (current_location.equals("B")) {
            System.out.println("Did the truck finished it shipment? [Y/N]");
            String is_shipment_finished;

            while (true) {
                Scanner console14 = new Scanner(System.in);
                is_shipment_finished = console14.next();

                if (!Objects.equals(is_shipment_finished, "Y") && !Objects.equals(is_shipment_finished, "N")) {
                    System.out.println("Please enter only Y for yes or N for no");
                } else {
                    break;
                }
            }
            /** if the truck is at a branch and hasn't finished the transport, nothing to update  */
            /** if the truck is at a branch and finished the transport, update. */
            if (is_shipment_finished.equals("Y")) {
                transportService.Finish_Transport_Controller(chosen_shipment_to_manage);
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
            if (transportService.WeightTruck(chosen_shipment_to_manage, current_truck, current_weight)) {
                System.out.println("Cargo weight updated.");
            } else {
                Handle_OverWeight(chosen_shipment_to_manage, current_weight);
            }
        }
    }

    public void Handle_OverWeight(TransportShipment chosen_shipment_to_manage, int current_weight) {

        int current_transport_order_id = get_specific_transport_order_id(chosen_shipment_to_manage);

        /** showing the manager his 3 options when truck is overweight. */
        System.out.println("Please choose one option: ");
        System.out.println("1. Remove the supplier from the shipment ");
        System.out.println("2. Change truck ");
        System.out.println("3. Remove Items ");
        System.out.println("");

        int solution_choice;
        boolean keep_menu = true;
        while (keep_menu) {
            Scanner console17 = new Scanner(System.in);
            try {
                solution_choice = console17.nextInt();

                /** saving the current transport order from the chosen shipment that is relevant to this supplier */
                TransportOrder current_transport_order = transportService.get_transport_order_by_id(current_transport_order_id);
                double max_weight_allowed = transportService.get_truck(chosen_shipment_to_manage.getTruck_license_plate_number()).getMax_cargo_weight() +
                        transportService.get_truck(chosen_shipment_to_manage.getTruck_license_plate_number()).getNet_weight();
                switch (solution_choice) {

                    /**  Remove the supplier from the shipment  */
                    case 1:
                        ;
                        HandleWeight_SkipSupplier(chosen_shipment_to_manage, current_transport_order);
                        keep_menu = false;
                        break;

                    /**  change truck  */
                    case 2:
                        ;
                        HandleWeight_ChangeTruck(chosen_shipment_to_manage, current_weight);
                        keep_menu = false;
                        break;

                    /**  Remove items  */
                    case 3:
                        ;
                        HandleWeight_RemoveItems(chosen_shipment_to_manage, current_transport_order, max_weight_allowed);
                        keep_menu = false;
                        break;

                    default:
                        System.out.println("Invalid option. Please enter 1 / 2 / 3 only");
                }
            } catch (Exception e) {
                System.out.println("Please enter a valid integer !!!!!!!!!");
            }
        }
    }

    public int get_specific_transport_order_id(TransportShipment chosen_transport_shipment) {
        System.out.println("Cargo weight is overweight, you need to handle it ");
        System.out.println("Please enter transport order ID: ");

        int current_transport_order_id;

        while (true) {
            Scanner console16 = new Scanner(System.in);
            try {
                current_transport_order_id = console16.nextInt();

                if (transportService.get_transport_order_by_id(current_transport_order_id) == null) {
                    System.out.println("This order is not in that transport document.");
                    System.out.println("These are the orders that belong to this transport: ");
                    System.out.println(chosen_transport_shipment.getTransport_document().getAll_transport_orders());
                    System.out.println("Please try again: ");
                    System.out.println(" ");
                } else
                    break;
            } catch (Exception e) {
                System.out.println("Please enter a valid integer");
            }
        }
        return current_transport_order_id;
    }

    public void addTruck() {

        int truck_license_plate_insert;
        int truck_net_weight_insert;
        int max_cargo_weight_insert;
        int truck_level_type_insert;

        System.out.println("Please enter the details: ");
        /** variables that help us to manage the trucks - consider the names of the variables */

        while (true) {

            System.out.println("Please enter license plate number [7 numbers]: ");
            Scanner console25 = new Scanner(System.in);
            try {
                truck_license_plate_insert = console25.nextInt();
                if (transportService.check_if_truck_plate_number_exist(truck_license_plate_insert)) {
                    System.out.println("License plate is already exist");
                } else if (!helperFunctions.check_int_in_length(String.valueOf(truck_license_plate_insert).length(), 7)) {
                    System.out.println("License plate number need to be 7 numbers");
                } else break;

            } catch (Exception e) {
                System.out.println("Please enter a valid integer");
            }
        }

        while (true) {
            System.out.println("Please enter truck's net weight [1500/2250/3000]: ");
            Scanner console26 = new Scanner(System.in);
            try {
                truck_net_weight_insert = console26.nextInt();
                if (truck_net_weight_insert != 1500 && truck_net_weight_insert != 2250 && truck_net_weight_insert != 3000) {
                    System.out.println("Truck's net weight must be 1500 / 2250 / 3000, please try again ");
                } else break;

            } catch (Exception e) {
                System.out.println("Please enter a valid integer");
            }
        }

        while (true) {

            System.out.println("Please enter max cargo weight: [1000,1500,2000] ");
            Scanner console27 = new Scanner(System.in);
            try {
                max_cargo_weight_insert = console27.nextInt();
                if (max_cargo_weight_insert != 1000 && max_cargo_weight_insert != 1500 && max_cargo_weight_insert != 2000) {
                    System.out.println("Truck's max cargo weight can only be 1000 / 1500 / 2000, please try again ");
                } else break;
            } catch (Exception e) {
                System.out.println("Please enter a valid integer");
            }
        }

        while (true) {
            System.out.println("Please enter the truck level by type [dry = 1 / cooler = 2 / freezer = 3]: ");
            Scanner console28 = new Scanner(System.in);
            try {
                truck_level_type_insert = console28.nextInt();
                if (truck_level_type_insert != 1 && truck_level_type_insert != 2 && truck_level_type_insert != 3) {
                    System.out.println("Truck's level can only be 1 / 2 / 3.");
                } else break;
            } catch (Exception e) {
                System.out.println("Please enter a valid integer");
            }
        }

        transportService.add_Truck(truck_license_plate_insert, truck_net_weight_insert, max_cargo_weight_insert, truck_level_type_insert);
    }


    public void removeTruck() {
        int license_plate_number_truck_to_remove;

        /** the manage should choose the driver he wants to remove */
        while (true) {
            System.out.println("Please enter truck's license plate number you want to remove: ");
            Scanner console29 = new Scanner(System.in);

            try {
                license_plate_number_truck_to_remove = console29.nextInt();
            } catch (Exception e) {
                System.out.println("Please enter an integer");
                continue;
            }
            if (!transportService.check_if_truck_plate_number_exist(license_plate_number_truck_to_remove)) {
                System.out.println("This license plate number is not in the system.");
            } else break;
        }

        boolean removeTruckAns = transportService.remove_Truck(license_plate_number_truck_to_remove);

        if (!removeTruckAns)
            System.out.println("Truck with license plate " + license_plate_number_truck_to_remove + " has future transports. System can only remove trucks that haven't assigned to any transport.");
    }




    public Pair<Product,Integer> removingProducts(TransportOrder order) {


        Scanner console30 = new Scanner(System.in);
        String productCode;
        Product product_to_remove;
        while (true) {

            System.out.println("Please choose a product code to remove");
            productCode = console30.next();
            product_to_remove = transportService.return_product_if_exist_Controller(productCode);
            Product prod = order.FindProduct(productCode);
            if (product_to_remove == null || prod == null) {
                System.out.println("Did not find this product in the order. Please try again.");
                System.out.println("");
            }else break;
        }
        int amount;
        Scanner console31 = new Scanner(System.in);
        final boolean[] checker = {true};
        final int[] former_value = {0};
        while (true) {
            System.out.println("Please enter the amount you wish to remove");
            try {
                amount = console31.nextInt();
                String finalProductCode = productCode;
                int finalAmount = amount;
                order.getProducts_list().forEach((key, value) -> {
                    if (key.getProduct_code().equals(finalProductCode)) {
                        former_value[0] = value;
                        if (value < finalAmount || finalAmount < 0) {
                            System.out.println("cannot enter amount that is bigger then the current amount, or enter a negative amount. Please try again.");
                            checker[0] = false;
                        }
                    }
                });
                if (checker[0])
                    break;
                else checker[0] = true;

            }
            catch (Exception e) {

                System.out.println("Please enter a valid integer");
            }
        }

        transportService.update_product_amount_in_order_controller(order,product_to_remove,former_value[0] - amount);

        return new Pair<Product,Integer> (product_to_remove,amount);
    }

    /** function that removing items or amount of items that the user choosing to remove */
    public void HandleWeight_RemoveItems (TransportShipment shipment, TransportOrder current_transport_order, double max_weight_allowed) {

            /**  creating a new transport order object
             * all removed items will be in this object
             * at the end the order will be added to the system as a new transport order  */
            int max_order_id = transportService.get_highest_order_id();
            TransportOrder new_transport_order_after_remove_items = new TransportOrder(max_order_id + 1, current_transport_order.getSource(), current_transport_order.getDestination(), new HashMap<Product, Integer>());
            boolean keep_remove = true;

            while (keep_remove) {

                System.out.println("Those are all the items of the current supplier: ");
                System.out.println(current_transport_order.getProducts_list());

                Pair<Product,Integer> product_and_amount_to_remove = removingProducts(current_transport_order);

                transportService.update_product_amount_in_order_controller(new_transport_order_after_remove_items,
                        product_and_amount_to_remove.getKey(), product_and_amount_to_remove.getValue());

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
                        }
                        catch (Exception e)
                        {
                            System.out.println("Please enter an integer");
                        }
                    }

                    if (new_cargo_weight_of_transport > max_weight_allowed) {
                        System.out.println("There is still overweight. please remove more items. ");
                        keep_removing_choice = "Y";
                    }

                    if (keep_removing_choice.equals("N")) {
                        /**  adding the new transport order to the system  */
                        transportService.add_new_transport_order(new_transport_order_after_remove_items);
                        transportService.setWeight(shipment, new_cargo_weight_of_transport);
                        keep_remove = false;
                        break;
                    }
                }
            }
    }

    public ATruck get_a_suitable_truck(TransportShipment chosen_shipment_to_manage, int current_weight) {

        ArrayList<ATruck> allSuitables_trucks = transportService.getAll_trucks_by_type_totalWeight_availability(chosen_shipment_to_manage, current_weight);
        if (allSuitables_trucks.size() == 0){
            System.out.println("Sorry, there aren't available trucks");
            return null;
        }

        System.out.println("Please choose the truck you want from the following options: ");
        System.out.println("");

        int count=1;
        for (ATruck curr_truck : allSuitables_trucks) {
            System.out.println(count + ". " + curr_truck);
            count++;
        }

        System.out.println("");

        int truck_index_in_list;

        while (true) {
            Scanner console18 = new Scanner(System.in);
            try {
                truck_index_in_list = console18.nextInt();

                if (truck_index_in_list < 1 || truck_index_in_list > allSuitables_trucks.size()){
                    System.out.println("Wrong option. Please choose a number from the above list. ");
                }
                else break;
            }
            catch (Exception e) {
                System.out.println("Please enter a valid integer");
            }
        }
        return allSuitables_trucks.get(truck_index_in_list-1);
    }

    public void HandleWeight_ChangeTruck (TransportShipment transportShipment, int current_weight) {

        /** the user choosing a new truck from the available trucks */
        ATruck chosen_truck = get_a_suitable_truck(transportShipment, current_weight);

        if (chosen_truck == null)
            return;

        System.out.println("");
        System.out.println("The truck you chose: ");
        System.out.println(chosen_truck);
        System.out.println(" ");

        /**  searching for a driver who is available, has a proper license and a proper training for that transport */
        String ID_chosen = get_a_suitable_driver_ID(transportShipment, chosen_truck);

        System.out.println("Your driver details: ");
        Driver new_driver_for_transport = transportService.get_driver_in_company_controller(ID_chosen);
        System.out.println(new_driver_for_transport);
        System.out.println(" ");

        /**  update the new driver the manager chose  */
        transportService.update_details_after_change_truck(transportShipment, new_driver_for_transport, chosen_truck, current_weight);
        System.out.println("Congratulation the switch completed successfully !");
    }

    public String get_a_suitable_driver_ID (TransportShipment transportShipment, ATruck chosen_truck) {

        boolean checking = true;
        String ID_chosen = null;

        while (checking) {
            System.out.println(transportService.display_available_drivers_controller(transportShipment.getTransport_document().getDate(), transportShipment.getTransport_document().getDeparture_time(),
                   chosen_truck.getNet_weight(), chosen_truck.getTruck_level()));

            System.out.println("Please enter the ID of the driver you want to choose: ");
            Scanner ID_input = new Scanner(System.in);
            ID_chosen = ID_input.next();

            boolean add_driver_succeed = transportService.add_driver_to_shift_controller(ID_chosen,
                    transportShipment.getTransport_document().getDate(), transportShipment.getTransport_document().getDeparture_time());

            if (!add_driver_succeed) {
                System.out.println("The ID you entered is incorrect, please try again ");
            } else {
                checking = false;
            }
        }
        return ID_chosen;
    }
    public void HandleWeight_SkipSupplier (TransportShipment transport_shipment, TransportOrder transport_order) {
        transportService.HandleWeight_SkipSupplier_Controller(transport_shipment, transport_order);
    }

    public HashMap<Product, Integer> product_for_order(){

        String input5 = null;
        String product_code_input;
        int amount_of_product_to_add_to_order;
        HashMap<Product, Integer> current_order = new HashMap<>();
        Product new_product = null;
        boolean checking_legal = true;

        while (true) {

            /** adding products to the order */
            while (checking_legal) {
                System.out.println("Please enter product code: ");
                Scanner console3 = new Scanner(System.in);
                try {
                     product_code_input = console3.next();
                     if (!helperFunctions.check_string_contains_only_numbers(product_code_input)){
                         System.out.println("Your code should be only numbers ");
                         continue;
                     }
                }
                catch (Exception e){
                    System.out.println("Please enter valid input ");
                    System.out.println("");
                    continue;
                }
                /** the product the manager want to add to the order from the supplier */
                new_product = transportService.return_product_if_exist_Controller(product_code_input);
                if (new_product == null) {
                    System.out.println("Product code is not exist, please try again");
                    System.out.println("");
                    continue;
                }
                checking_legal = false;
            }

            checking_legal = true;
            /** asking for the amount of the product the manager just entered the order */
            while (checking_legal) {
                System.out.println("Please enter amount: ");
                Scanner console4 = new Scanner(System.in);
                try {
                    amount_of_product_to_add_to_order = console4.nextInt();
                }
                catch (Exception e){
                    System.out.println("Your amount should be only numbers, please try again ");
                    System.out.println("");
                    continue;
                }
                /** adding the product and the amount to the order */
                current_order.put(new_product, amount_of_product_to_add_to_order);
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
            } else {
                break;
            }
        }
        return current_order;
    }

    public void execute_transport () {

        /** checking if there are any transport documents in the system */
        int sum_of_transport_controller = transportService.get_num_of_waiting_transport_documents();
        if (sum_of_transport_controller == 0 ) {
            System.out.println("There aren't any transport documents waiting to be execute");
            System.out.println("");
            return;
        }
        else if(sum_of_transport_controller == -1){
            return;
        }

        /** looking for the transport document that the manager chose */
        TransportDocument chosen_document = get_specific_transport_document();
        transportService.execute_transport_controller(chosen_document);
        System.out.println("Transport executed successfully ");
        System.out.println("");

    }

    public void creating_new_transport_document(){
        /** checking if there are any orders in the system */
        if (!transportService.check_if_exist_unsigned_transport_orders()) {
            System.out.println("There is no transport orders awaiting, so you cant create a new transport document");
            System.out.println("");
            return;
        }
        int transport_order_choice = 0;
        ArrayList<Integer> index_list = new ArrayList<Integer>();
        boolean legal_checking = true;

        /** asking for order id that the manager want to make a transport document from */
        TransportOrder transportOrder_to_ship = null;

        System.out.println("Those are all the transport orders in the system: ");

        if (!print_all_transport_orders())
            return;

        System.out.println("Please enter the ID of the transport order you want to choose: ");
        System.out.println("Your choice: ");

        while (true) {
            Scanner console6 = new Scanner(System.in);
            try {
                transport_order_choice = console6.nextInt();
                transportOrder_to_ship = transportService.get_transport_order_by_id(transport_order_choice);
                if (transportOrder_to_ship == null) {
                    System.out.println("There is not any transport order with this ID, please try again: ");
                    System.out.println("");
                    continue;
                }
                break;
            } catch (Exception e) {
                System.out.println("Please enter a valid integer ");
            }
        }
        /** showing the order that the manager chose */
        System.out.println("You order's choice is: ");
        System.out.println(transportOrder_to_ship);

        /** keeping the type of the products in the order that the manager chose */
        String order_type = transportOrder_to_ship.getProducts_list().entrySet().iterator().next().getKey().getType();
        String present_choice = null;
        int transport_document_choice_to_add_order = 0;
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

            /** looking for all the transports documents if exist in the system and show him only the relevant documents by the same type and area */
            ArrayList<TransportDocument> allTransport_documents_by_type = transportService.getAll_transport_documents_by_type(order_type,
                    transportOrder_to_ship.getDestination().getAddress().getShipping_area());

            if (allTransport_documents_by_type.size() != 0) {

                System.out.println("There are all of the relevant transport documents: ");
                int count = 1;
                for (TransportDocument curr_doc : allTransport_documents_by_type) {
                    System.out.println(count + ". " + curr_doc);
                    count++;
                }

                legal_checking = true;
                /** asking the manager to choose the transport document that he wants to add to his order */
                while (legal_checking) {
                    System.out.println("Please enter the number of the transport document you want to add to: ");
                    Scanner console11 = new Scanner(System.in);
                    try {
                        transport_document_choice_to_add_order = console11.nextInt();

                        if (transport_document_choice_to_add_order < 1 || transport_document_choice_to_add_order > allTransport_documents_by_type.size()) {
                            System.out.println("Wrong choice, please try again ");
                            continue;
                        }
                        legal_checking = false;

                        /** update all the system with the orders or documents that has been change - existing change */
                        int chosenDocID = allTransport_documents_by_type.get(transport_document_choice_to_add_order+1).getTransport_document_ID();
                        transportOrder_to_ship.setAssigned_doc_id(chosenDocID);
                        transportService.update_transport_order(transportOrder_to_ship);
                        transportService.updating_documents_in_the_system(transportOrder_to_ship, chosenDocID);
                        System.out.println("Finished adding your transport order to an existing transport document");
                        System.out.println("");
                        return;
                    } catch (Exception e) {
                        System.out.println("Please enter valid integer ");
                    }
                }
            }
        }
        System.out.println("Your transport's type is: " + order_type);

        boolean no_available_storekeeper_try_again = true;
        while (no_available_storekeeper_try_again) {
            /** asking for the date of the transport */
            boolean checker = false;
            Scanner date_input_scanner;
            String date_input_string;
            String decide_if_try_another_date = "";
            LocalDate date = null;

            while (true) {

                if (checker)
                    break;
                System.out.print("Enter the date of the transport (yyyy-mm-dd): ");
                try {
                    date_input_scanner = new Scanner(System.in);
                    date_input_string = date_input_scanner.nextLine();
                    date = LocalDate.parse(date_input_string);

                    //check if the date of the shift is only in the next week sunday - saturday
                    LocalDate nowDate = LocalDate.now();
                    LocalDate startOfWeek = nowDate.with(TemporalAdjusters.next(DayOfWeek.SUNDAY));
                    LocalDate endOfWeek = startOfWeek.plusDays(6);
                    //print massage if not a valid date
                    if (!(!date.isAfter(endOfWeek) && !date.isBefore(startOfWeek))) {
                        System.out.println("You can only order new transportation up to next week");
                        System.out.println("");
                        System.out.println("Do you want to try another date ? [Y / N]");

                        boolean try_again = true;
                        while (try_again) {
                            Scanner decision = new Scanner(System.in);
                            decide_if_try_another_date = decision.nextLine();

                            if (decide_if_try_another_date.equals("N")) {
                                checker = true;
                                try_again = false;
                            } else if (!decide_if_try_another_date.equals("Y")) {
                                System.out.println("Please enter a valid input - [Y / N] ");
                            } else {
                                try_again = false;
                            }
                        }
                    } else checker = true;
                } catch (DateTimeParseException e) {
                    System.out.println("The date is in invalid format. Please try again ");
                }
            }
            if (decide_if_try_another_date.equals("N")) {
                break;
            }

            /** asking for the exact time of the transport */
            LocalTime departureTime = null;
            System.out.print("Start time of the shift in the format hh:mm:ss :");
            Scanner time_scanner = new Scanner(System.in);
            String time = time_scanner.nextLine();

            while (departureTime == null) {
                try {
                    departureTime = LocalTime.parse(time);
                } catch (DateTimeParseException e) {
                    System.out.println("The time is Invalid format. Please enter time in the format hh:mm:ss : ");
                    time = time_scanner.nextLine();
                }
            }
            boolean check_if_exist_storekeeper = transportService.check_if_storeKeeperA_assigned(transportOrder_to_ship.getDestination().getID(), date, departureTime);

            if (check_if_exist_storekeeper) {


                boolean checker5 = false;
                int level_skill = 0;

                if (Objects.equals(order_type, "Dry"))
                    level_skill = 1;
                else if (Objects.equals(order_type, "Cooler"))
                    level_skill = 2;
                else if (Objects.equals(order_type, "Freezer"))
                    level_skill = 3;

                /** let the manager choose a truck from all the trucks exist */
                ArrayList<ATruck> all_trucks_exist_by_type_and_available = transportService.getAll_trucks_by_type_and_available(level_skill, date);
                if (all_trucks_exist_by_type_and_available.size() == 0){
                    System.out.println("No available trucks at this moment. Please try again later");
                    break;
                }

                System.out.println("Please choose a truck from the following options:  ");
                System.out.println("");
                int count = 1;
                for (ATruck curr_truck : all_trucks_exist_by_type_and_available){
                    System.out.println(count + ". " + curr_truck);
                    count++;
                }

                int truck_choice = 0;
                legal_checking = true;
                while (legal_checking) {

                    Scanner console7 = new Scanner(System.in);
                    try {
                        truck_choice = console7.nextInt();

                        if (truck_choice < 1 || truck_choice > all_trucks_exist_by_type_and_available.size()) {
                            System.out.println("Wrong choice, please try again ");
                            continue;
                        }
                        legal_checking = false;
                    }
                    catch (Exception e){
                        System.out.println("Please enter valid integer ");
                    }
                }

                System.out.println("Here are all the available drivers for you to choose:");
                ATruck chosen_truck = all_trucks_exist_by_type_and_available.get(truck_choice-1);

                String ID_chosen = null;
                boolean checker_available_drivers = true;
                while (!checker5) {
                    String result_available_drivers = transportService.display_available_drivers_controller(date, departureTime, chosen_truck.getNet_weight(), chosen_truck.getTruck_level());
                    System.out.println(result_available_drivers);
                    if (Objects.equals(result_available_drivers, "There are no available employees for this shift.")){
                        System.out.println("");
                        checker_available_drivers = false;
                        break;
                    }
                    System.out.println("Please enter the ID of the driver you want to choose: ");
                    Scanner ID_input = new Scanner(System.in);
                    ID_chosen = ID_input.next();

                    boolean add_driver_succeed = transportService.add_driver_to_shift_controller(ID_chosen, date, departureTime);

                    if (!add_driver_succeed) {
                        System.out.println("The ID you entered is incorrect, please try again ");
                    } else {
                        checker5 = true;
                    }
                }

                if (!checker_available_drivers){
                    no_available_storekeeper_try_again = false;
                    break;
                }
                /** getting the right transport order from the system */
                TransportOrder transport_order_to_add = transportService.get_transport_order_by_id(transport_order_choice);
                /** adding the new transport document to the system */
                ArrayList<TransportOrder> all_transport_orders_into_document = new ArrayList<TransportOrder>();
                all_transport_orders_into_document.add(transport_order_to_add);
                /** looking for the right driver that the manager chose */
                Driver chosen_driver = transportService.get_driver_in_company_controller(ID_chosen);
                /** making the new transport document and adding it into the system
                 * and removing the old transport order from the system */
                transportService.creating_new_transport_document(date, chosen_truck.getLicense_plate_number(), departureTime, chosen_driver, all_transport_orders_into_document, transport_order_to_add.getDestination().getAddress().getShipping_area(), order_type);
                transportService.update_truck_transports_controller(chosen_truck, date);
                no_available_storekeeper_try_again = false;
                System.out.println("Congratulation ! the creating of the transport document worked successfully !");
            }
            else {
                System.out.println("There aren't any storekeeper at this date, do you want to try another date? [Y / N]");
                while (true) {
                    Scanner choose_to_try_again = new Scanner(System.in);
                    String choose_to_try_another_date = choose_to_try_again.next();

                    if (choose_to_try_another_date.equals("Y")) {
                        break;
                    } else if (choose_to_try_another_date.equals("N")) {
                        no_available_storekeeper_try_again = false;
                        break;
                    }
                    System.out.println("Your chose is invalid, please try again - [Y / N] ");
                }
            }
            if (!no_available_storekeeper_try_again){
                break;
            }
        }
    }

    public TransportDocument get_specific_transport_document () {

        /** asking for transport after showing the manager all the transport documents in the system */
        System.out.println("Please choose a transport ID to execute: ");

        try {
            if (!print_all_transport_documents())
                return null;

            int document_ID;
            System.out.println("Your choice: ");
            while (true) {

                Scanner console11 = new Scanner(System.in);
                try {
                    document_ID = console11.nextInt();
                    if (transportService.check_if_document_id_exist(document_ID)){
                        break;
                    }
                    else {
                        System.out.println("The id you choose is not exist, please try again: ");
                    }
                } catch (Exception e) {
                    System.out.println("Please enter valid integer");
                }
            }
            /** looking for the transport document that the manager chose */
            return transportService.get_transport_document_by_id(document_ID);
        }
        catch (Exception e) {
            System.out.println("Failed to get a specific transport document: " + e.getMessage());
            return null;
        }
    }

    /** user choose one shipment from all the active transports at the moment */
    public TransportShipment chosen_transport_shipment () {

        System.out.println("Please choose the id of the transport shipment you want to manage: ");
        System.out.println(" ");
        if (!print_all_active_transports())
            return null;

        int shipment_number;
        TransportShipment transportShipment_chosen = null;
        while (true) {
            Scanner console12 = new Scanner(System.in);
            try {
                shipment_number = console12.nextInt();

                transportShipment_chosen = transportService.get_transport_shipment_by_id(shipment_number);
                if (transportShipment_chosen == null) {
                    System.out.println("Your choice is not exist, please try again: ");
                } else break;
            } catch (Exception e) {
                System.out.println("Please enter a valid integer");
            }
        }
        /** Saving the "chosen_shipment" the user wants to manage */
        return transportShipment_chosen;
    }

    public void switch_truck_status () {
        int license_plate_number;

        while (true) {
            System.out.println("Please enter truck's license plate number you want to change status: ");
            Scanner console29 = new Scanner(System.in);

            try {
                license_plate_number = console29.nextInt();
            } catch (Exception e) {
                System.out.println("Please enter an integer");
                continue;
            }
            if (!transportService.check_if_truck_plate_number_exist(license_plate_number)) {
                System.out.println("This license plate number is not in the system.");
            } else break;
        }

        boolean switchStatus = transportService.switch_truck_status_controller(license_plate_number);

        if (!switchStatus){
            System.out.println("Cannot change the status of a truck with future transports schedule.");
        }
        else {
            if (transportService.get_truck(license_plate_number).getStatus().equals(TruckStatus.Available))
                System.out.println("Truck old status was Not Available, and changed to Available" );
            else System.out.println("Truck old status was Available, and changed to Not Available" );

        }

    }

    public void print_all_trucks() {
        HashMap<Integer,ATruck> allTrucks = transportService.get_all_trucks();

        if (allTrucks.size() == 0)
        {
            System.out.println("There aren't any truck at the system at this moment.");
            System.out.println("");
        }
        else {
            for (ATruck print_truck : allTrucks.values()) {
                System.out.println(print_truck);
            }
        }

    }

    public boolean print_all_active_transports() {

        HashMap<Integer, TransportShipment> allActiveTransports = transportService.getAll_active_transports();

        if (allActiveTransports.size() == 0)
        {
            System.out.println("There aren't any truck at the system at this moment.");
            System.out.println("");
            return false;
        }
        else {
            for (TransportShipment current_shipment : allActiveTransports.values()) {
                System.out.println(current_shipment);
            }
            return true;
        }
    }

    public boolean print_all_former_transport_documents() {
        ArrayList<TransportDocument> allFormer_transport_document = transportService.getAll_former_transport_document();

        if (allFormer_transport_document.size() == 0){
            System.out.println("There arent any former transport documents in the system.");
            return false;
        }

        int count =1;
        for (TransportDocument curr_doc : allFormer_transport_document){
            System.out.println(count + ". " + curr_doc);
            count++;
        }
        return true;
    }

    public boolean print_all_transport_documents() {
        HashMap<Integer,TransportDocument> all_transport_document = transportService.getAll_transport_documents();

        if (all_transport_document.size() == 0){
            System.out.println("There arent any former transport documents in the system.");
            return false;
        }

        int count =1;
        for (TransportDocument curr_doc : all_transport_document.values()){
            System.out.println(count + ". " + curr_doc);
            count++;
        }
        return true;
    }

    public boolean print_all_transport_orders() {
        HashMap<Integer,TransportOrder> all_transport_orders = transportService.getAll_transport_orders();

        if (all_transport_orders.size() == 0){
            System.out.println("There arent any transport orders in the system.");
            return false;
        }

        int count =1;
        for (TransportOrder curr_order : all_transport_orders.values()){
            System.out.println(count + ". " + curr_order);
            count++;
        }
        return true;
    }



}



