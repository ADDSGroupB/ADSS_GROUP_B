package BuisnessLayer.TransportManager;

import BuisnessLayer.HR.CompanyController;
import BuisnessLayer.HR.Driver;
import DataAccessLayer.TransportManager.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class TransportController {
    HelperFunctions helperFunctions;
    CompanyController companyController;
    ASiteDAO aSiteDAO;
    TransportShipmentDAO transportShipmentDAO;
    static TransportDocumentDAO transportDocumentDAO;

    static {
        try {
            transportDocumentDAO = new TransportDocumentDAO();
        } catch (SQLException e) {
            System.out.println("SQL excpetion in TransportController - creating TransportDocumentDAO");
            System.out.println(e.getMessage());
        }
    }
    ProductDAO productDAO;
    TransportOrderDAO transportOrderDAO;
    ATruckDAO aTruckDAO;

    public TransportController() throws SQLException {
        this.helperFunctions = new HelperFunctions();
        this.companyController = new CompanyController();
        this.productDAO = new ProductDAO();
        this.aTruckDAO = new ATruckDAO();
        this.aSiteDAO = new ASiteDAO();
        this.transportShipmentDAO = new TransportShipmentDAO();
        this.transportOrderDAO = new TransportOrderDAO();
    }

    public void build () {

        try {
            aTruckDAO.deleteContent_trucks();
            aSiteDAO.deleteContent_sites();
            aSiteDAO.deleteContent_Addresses();
            productDAO.deleteContent_products();
            transportOrderDAO.deleteContent_orders();
            transportOrderDAO.deleteContent_ProductsInOrders();
            transportShipmentDAO.deleteContent_shipments();
            transportShipmentDAO.deleteContent_SitesInShipment();
            transportDocumentDAO.deleteContent_documents();
        }
        catch (SQLException e){
            System.out.println("Failed while building the system: " + e.getMessage());
        }

        Product pr1 = new Product("18675", "Milk 3%", "Cooler", "Tara",  "Dairy", "drink", 750);
        Product pr2 = new Product("95043", "Koteg 5%", "Cooler", "Tnoova",  "Dairy", "eat", 1000);
        Product pr3 = new Product("39586", "Chicken", "Freezer", "Osem",  "Freezer", "eat", 500);
        Product pr4 = new Product("10596", "Shnitzel", "Freezer", "Tara",  "Freezer", "eat", 750);
        Product pr5 = new Product("76767", "Corn", "Dry", "Tnoova",  "Dry", "eat", 1000);
        Product pr6 = new Product("20597", "Tomato", "Dry", "Osem",  "Dry", "eat", 500);
        Product pr7 = new Product("19191" , "Tuna", "Dry", "Tara", "Dry", "eat", 750);
        Product pr8 = new Product("95847", "Cheese", "Cooler", "Tnoova",  "Dairy", "eat", 500);
        Product pr9 = new Product("85964", "Beans", "Freezer", "OSem",  "Freezer", "eat", 750);
        try {
            productDAO.insertProduct(pr1);
            productDAO.insertProduct(pr2);
            productDAO.insertProduct(pr3);
            productDAO.insertProduct(pr4);
            productDAO.insertProduct(pr5);
            productDAO.insertProduct(pr6);
            productDAO.insertProduct(pr7);
            productDAO.insertProduct(pr8);
            productDAO.insertProduct(pr9);
        }
        catch (Exception e) {
            System.out.println("Failed to insert products to system: " + e.getMessage());
        }

        Address address1 = new Address(657, "South", "Grand Kanion, Beer Sheva");
        Address address2 = new Address(978, "West", "Azrieli mall, Beer Sheva");
        Address address3 = new Address(245, "North", "Big, Beer Sheva");
        Address address4 = new Address(1, "West", "Negev Center, Beer Sheva");
        Address address5 = new Address(2, "North", "Ramot, Beer Sheva");
        Address address6 = new Address(3, "West", "Ringelblom, Beer Sheva");

        ASite src1 = new Source(657,  "0593849506", "Tamar", "Source", address1);
        ASite src2 = new Source(978,  "0593849506", "Yuri", "Source", address2);
        ASite src3 = new Source(245,  "0596849384", "Arnon", "Source", address3);
        ASite dest1 = new Destination( 1, "0549586079", "Ehud", "Destination", address4);
        ASite dest2 = new Destination( 2, "0495960495", "Gil", "Destination",address5);
        ASite dest3 = new Destination( 3, "0779685968", "Shir", "Destination",address6);

        try {
            aSiteDAO.insertASite(src1);
            aSiteDAO.insertASite(src2);
            aSiteDAO.insertASite(src3);
            aSiteDAO.insertASite(dest1);
            aSiteDAO.insertASite(dest2);
            aSiteDAO.insertASite(dest3);
        }
        catch (Exception e) {
            System.out.println("Failed to insert sites to system: " + e.getMessage());
        }

        ATruck truck1 = new DryTruck(3614131, 1500, 1000);
        ATruck truck2 = new DryTruck(4758691, 3000, 2000);
        ATruck truck3 = new CoolerTruck(1849506, 2250, 1500);
        ATruck truck4 = new CoolerTruck(4475869, 1500, 1000);
        ATruck truck5 = new FreezerTruck(1958694, 3000, 2000);
        ATruck truck6 = new FreezerTruck(9684585, 2250, 1500);

        try {
            aTruckDAO.insertATruck(truck1);
            aTruckDAO.insertATruck(truck2);
            aTruckDAO.insertATruck(truck3);
            aTruckDAO.insertATruck(truck4);
            aTruckDAO.insertATruck(truck5);
            aTruckDAO.insertATruck(truck6);
        }
        catch (Exception e) {
            System.out.println("Failed to insert trucks to system: " + e.getMessage());
        }

    }

    public void deleteAllTrucks () {
        try {
            aTruckDAO.deleteContent_trucks();
        }
        catch (Exception e) {
            System.out.println("Failed to delete content of allTrucks table");
        }
    }

    public void deleteAllSites () {
        try {
            aSiteDAO.deleteContent_sites();
        }
        catch (Exception e) {
            System.out.println("Failed to delete content of allSites table");
        }
    }

    public void deleteAllProducts () {
        try {
            productDAO.deleteContent_products();
        }
        catch (Exception e) {
            System.out.println("Failed to delete content of allProducts table");
        }
    }


    public void printAllProducts () {
        try {
            for (Product product : productDAO.getAllProducts().values()) {
                System.out.println(product);
            }
        }
        catch (Exception e) {
            System.out.println("Failed to print all products: " + e.getMessage());
        }
    }

    public void printAllSites () {
        try {
            for (ASite site : aSiteDAO.getAllSites().values()) {
                System.out.println(site);
            }
        }
        catch (Exception e) {
            System.out.println("Failed to print all sites: " + e.getMessage());
        }
    }

    public void printAllTrucks () {
        try {
            for (ATruck truck : aTruckDAO.getAllTrucks().values()) {
                System.out.println(truck);
            }
        }
        catch (Exception e) {
            System.out.println("Failed to print all trucks: " + e.getMessage());
        }
    }


    /** function that getting license plate number and checking if its exist in the system  */
    public boolean check_if_truck_plate_number_exist(int license_plate_number) {

        try {
            ATruck truck = aTruckDAO.getATruckById(license_plate_number);
            if (truck != null){
                return true;
            }
        }
        catch (SQLException e){
            System.out.println("Exception while trying get all the trucks in the system");
        }
        return false;
    }

    /** function that switch the status of the truck by getting license plate number */
    public boolean switch_truck_status_controller(int license_plate_number) {
        try {
            ATruck truck = aTruckDAO.getATruckById(license_plate_number);

            if (!truck.getAll_transports().isEmpty()){
                return false;
            }

            if (truck.getStatus().equals(TruckStatus.Available))
                truck.setStatus(TruckStatus.NotAvailable);

            else
                truck.setStatus(TruckStatus.Available);

            aTruckDAO.updateATruck(truck);
        }
        catch (Exception e) {
            System.out.println("Failed to get truck in switch truck status controller");
        }
        return true;
    }

    /** function that add a new truck to the system by the type of the truck */
    public void add_Truck(int license_plate_number, int truck_net_weight, int truck_max_cargo_weight, int truck_level_type){

        try {
            switch (truck_level_type) {

                case 1:
                    aTruckDAO.insertATruck(new DryTruck(license_plate_number, truck_net_weight, truck_max_cargo_weight));
                    break;

                case 2:
                    aTruckDAO.insertATruck(new CoolerTruck(license_plate_number, truck_net_weight, truck_max_cargo_weight));
                    break;

                case 3:
                    aTruckDAO.insertATruck(new FreezerTruck(license_plate_number, truck_net_weight, truck_max_cargo_weight));
                    break;
            }
        }
        catch (SQLException e){
            System.out.println("Exception from trying insert new truck to the system");
        }
    }


    public void updating_documents_in_the_system(TransportOrder order, int document_id){

        try {
            TransportDocument document = transportDocumentDAO.getTransportDocumentById(document_id);
            document.add_transport_order(order);
            transportDocumentDAO.updateTransportDocument(document);
        }
        catch (SQLException e){
            System.out.println("Exception from trying to get document / order by id / update the transport document in the system");
        }
    }

    /** updating transport orders in the system */
    public void update_transport_order(TransportOrder order){
        try {
            transportOrderDAO.updateTransportOrder(order);
        }
        catch (SQLException e){
            System.out.println("Exception from trying get / update transport order");
        }
    }

    /** function adding transport order to exist transport document */
    /*
    public void add_order_to_doc_by_id(TransportOrder order, int doc_id) {
            try {
                TransportDocument transportDocument = transportDocumentDAO.getTransportDocumentById(doc_id);
                transportDocument.add_transport_order(order);
                transportDocumentDAO.updateTransportDocument(transportDocument);
            }
            catch (SQLException e){
                System.out.println("Exception from trying get document by id / update document");
            }
    }
    */


    /** function that removing truck from the system if the truck is not shipping right now */
    public boolean remove_Truck(int license_plate_number_truck_to_remove) {

        try {
            ATruck truck_to_remove = aTruckDAO.getATruckById(license_plate_number_truck_to_remove);
            if (truck_to_remove.getAll_transports().size() > 0)
                return false;
            else {
                aTruckDAO.deleteATruck(license_plate_number_truck_to_remove);
            }
        }
        catch (SQLException e){
            System.out.println("Exception from removing truck " + e.getMessage());
        }
        return true;
    }


    /** function that print trucks finding by id */
    /*
    public void print_trucks_by_id (ArrayList<Integer> trucks) {
        try {
            for (int curr_truck_id : trucks) {
                System.out.println(aTruckDAO.getATruckById(curr_truck_id));
            }
        } catch (SQLException e) {
            System.out.println("Exception from trying get truck by id");
        }
    }
    */

    //TODO: change this? or leave it and in the GUI call the get function a print each truck
    /** function that print all the trucks in the system*/
    /*
    public void print_Trucks() {
        try {
            HashMap<Integer, ATruck> all_trucks = aTruckDAO.getAllTrucks();
            if (all_trucks == null || all_trucks.size() == 0) {
                System.out.println("There aren't any truck at the system at this moment.");
                System.out.println("");
                return;
            }
            for (ATruck print_truck : all_trucks.values()) {
                System.out.println(print_truck);
            }
            System.out.println("");
            System.out.println("");
        } catch (SQLException e) {
            System.out.println("Exception from trying get all trucks");
        }
    }
    */


    //TODO: change this? or leave it and in the GUI call the get function a print each transport
    /** printing the active transports if exist */
    /*
    public void print_active_transports_Controller() {
        try {
            int count = 1;
            HashMap<Integer, TransportShipment> all_transport_shipments = transportShipmentDAO.getAllTransportShipments();
            for (TransportShipment print_active_transports : all_transport_shipments.values()) {
                System.out.println(count + ". " + print_active_transports);
                count++;
            }
            if (all_transport_shipments.size() == 0) {
                System.out.println("There aren't any active transports");
            }
            System.out.println("");
        } catch (SQLException e) {
            System.out.println("Exception from trying get all transport shipments in the system");
        }
    }
    */


    public HashMap<Integer, TransportShipment> getAll_active_transports (){
        try {
            return transportShipmentDAO.getAllTransportShipments();
        }
        catch (Exception e){
            System.out.println("Exception from getting all active transports");
        }
        return null;
    }



    public ArrayList<TransportDocument> getAll_former_transport_document () {
        ArrayList<TransportDocument> all_former_transport_document = new ArrayList<>();

        try {
            for (TransportDocument curr_document : transportDocumentDAO.getAllTransportDocuments().values())
            {
                if (curr_document.getDoc_status() == DocumentStatus.finished)
                    all_former_transport_document.add(curr_document);
            }

        }
        catch (Exception e) {
            System.out.println("Exception in getAll_former_transport_document: " + e.getMessage());
        }

        return all_former_transport_document;
    }

    public ArrayList<TransportDocument> getAll_waiting_transport_documents() {
        ArrayList<TransportDocument> all_waiting_transport_document = new ArrayList<>();

        try {
            for (TransportDocument curr_document : transportDocumentDAO.getAllTransportDocuments().values())
            {
                if (curr_document.getDoc_status() == DocumentStatus.waiting)
                    all_waiting_transport_document.add(curr_document);
            }

        }
        catch (Exception e) {
            System.out.println("Exception in getAll_waiting_transport_documents: " + e.getMessage());
        }

        return all_waiting_transport_document;

    }


    //TODO: change this? or leave it and in the GUI call the get function a print each former document
    /** printing the transport's documents former if exist */
    /*
    public void print_former_transport_document_Controller() {
        try {
            int count = 1;
            // checking for the documents in progress and filtering
            HashMap<Integer, TransportDocument> all_transport_documents = transportDocumentDAO.getAllTransportDocuments();
            for(TransportDocument check_transport_document : all_transport_documents.values()){
                if(check_transport_document.getDoc_status() != DocumentStatus.finished){
                    all_transport_documents.remove(check_transport_document.getTransport_document_ID());
                }
            }
            // printing the documents in progress *
            for (TransportDocument print_former_transport_document : all_transport_documents.values()) {
                System.out.println(count + ". " + print_former_transport_document);
                count++;
            }
            if (all_transport_documents.size() == 0) {
                System.out.println("There aren't any former transports");
            }
        }
        catch (SQLException e){
            System.out.println("Exception from trying get all transport documents in the system ");
        }
    }
    */


    //TODO: change this? or leave it and in the GUI call the get function a print each document
    /** printing the transport's documents if exist */
    /*
    public void print_transport_document_Controller() {
        try {
            HashMap<Integer, TransportDocument> all_transport_documents = transportDocumentDAO.getAllTransportDocuments();
            int count = 1;
            // checking for the documents in progress and filtering
            for(TransportDocument check_transport_document : all_transport_documents.values()){
                if(check_transport_document.getDoc_status() == DocumentStatus.finished || check_transport_document.getDoc_status() == DocumentStatus.inProgress){
                    all_transport_documents.remove(check_transport_document.getTransport_document_ID());
                }
            }
            for (TransportDocument print_transport_document : all_transport_documents.values()) {
                System.out.println(count + ". " + print_transport_document);
                count++;
            }
            if (all_transport_documents.size() == 0) {
                System.out.println("There aren't any document's transports in the system");
            }
            System.out.println("");
        } catch (SQLException e) {
            System.out.println("Exception from trying getting all transport documents");
        }
    }
    */


    //TODO: change this? or leave it and in the GUI call the get function a print each order
    /** printing the transport's orders if exist */
    /*
    public void print_transport_order_Controller() {
        try {
            HashMap<Integer, TransportOrder> all_transport_orders = transportOrderDAO.getAllTransportOrders();
            int count = 1;
            boolean check = false;
            for (TransportOrder current_order : all_transport_orders.values()) {

                if (current_order.getAssigned_doc_id() == -1) {
                    System.out.println(count + ". " + current_order);
                    count++;
                    check = true;
                }

            }
            if (!check) {
                System.out.println("There aren't any order's transports in the system");
            }
            System.out.println("");
        } catch (SQLException e) {
            System.out.println("Exception from trying getting all transport orders");
        }
    }
    */


    /** checking if there are any sites in the system */
    public boolean check_if_existing_any_Site_Controller() {
        try {
            if (aSiteDAO.getAllSites().size() == 0) {
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Exception from trying to get all sites in the system");
        }
        return true;
    }

    /** function that find specific source by id */
    public ASite find_source_Controller(int ID_to_find) {
        try {
            ASite site = aSiteDAO.getASiteById(ID_to_find);
            Source temp_source;
                if (site.getID() == ID_to_find && site instanceof Source) {
                    temp_source = (Source) site;
                    return temp_source;
                }
        } catch (SQLException e) {
            System.out.println("Exception from trying to get all sites in the system");
        }
        return null;
    }

    /** function that weighting the truck and return if exceeded */
    public boolean WeightTruck(TransportShipment chosen_transport, ATruck current_truck, int current_weight) {

        try {
            double max_cargo_weight = current_truck.getMax_cargo_weight();
            double net_weight = current_truck.getNet_weight();

            if (current_weight <= max_cargo_weight + net_weight) {
                chosen_transport.setCargo_weight(current_weight);
                transportShipmentDAO.updateTransportShipment(chosen_transport);
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Exception from trying to update transport shipment");
        }
        return false;
    }

    /** function that set the weight of the shipment while shipment in progress */
    public void setWeight (TransportShipment shipment, int weight) {
        shipment.setCargo_weight(weight);
        try {
            transportShipmentDAO.updateTransportShipment(shipment);
        }
        catch (Exception e) {
            System.out.println("Failed to set weight to truck: " + e.getMessage());
        }
    }

    /** function that find destination by id*/
    public ASite find_destination_Controller(int ID_to_find) {
        try {
            ASite site = aSiteDAO.getASiteById(ID_to_find);
            Destination temp_destination;
            if (site.getID() == ID_to_find && site instanceof Destination) {
                temp_destination = (Destination) site;
                return temp_destination;
            }
        } catch (SQLException e) {
            System.out.println("Exception from trying to get all sites in the system");
        }
        return null;
    }


    /** function that return product if exist finding by product code */
    public Product return_product_if_exist_Controller(String product_code_input){

        try {
            return productDAO.getProductByCode(product_code_input);

        } catch (SQLException e) {
            System.out.println("Exception from trying get product by code");
        }
        return null;
    }

    public  HashMap<Integer, ASite> getAll_sources(){
        try {
            HashMap<Integer, ASite> all_sites = aSiteDAO.getAllSites();
            HashMap<Integer, ASite> all_sources = new HashMap<>();
            for (ASite check_site : all_sites.values()){
                if (check_site instanceof Source){
                    all_sources.put(check_site.getID(), check_site);
                }
            }
            return all_sources;
        }
        catch (SQLException e) {
            System.out.println("Exception from trying get product by code");
        }
        return null;
    }

    public  HashMap<Integer, ASite> getAll_destinations(){
        try {
            HashMap<Integer, ASite> all_sites = aSiteDAO.getAllSites();
            HashMap<Integer, ASite> all_destinations = new HashMap<>();
            for (ASite check_site : all_sites.values()){
                if (check_site instanceof Destination){
                    all_destinations.put(check_site.getID(), check_site);
                }
            }
            return all_destinations;
        }
        catch (SQLException e) {
            System.out.println("Exception from trying get product by code");
        }
        return null;
    }
    public HashMap<String, Product> getAllProducts(){
        try {
            return productDAO.getAllProducts();
        }
            catch (SQLException e) {
                System.out.println("Exception from trying get product by code");
            }
        return null;
    }
    public ArrayList<ATruck> getAll_trucks_by_type_totalWeight_availability(TransportShipment chosen_shipment_to_manage, int current_weight) {

        ArrayList<ATruck> allTrucks_by_type_totalWeight_availability = new ArrayList<>();

        String transport_type = chosen_shipment_to_manage.getTransport_document().getTransport_type();

        int level_skill_type = 1;

        if (Objects.equals(transport_type, "Dry"))
            level_skill_type = 1;
        else if (Objects.equals(transport_type, "Cooler"))
            level_skill_type = 2;
        else if (Objects.equals(transport_type, "Freezer"))
            level_skill_type = 3;

        boolean checker;
        for (ATruck truck : get_all_trucks().values()) {
            checker=true;
            if (truck.getTruck_level() == level_skill_type &&
                    Objects.equals(truck.getStatus(),TruckStatus.Available) &&
                    truck.getStatus().ordinal() == 0 &&
                    (truck.getNet_weight() + truck.getMax_cargo_weight() >= current_weight)) {

                for (LocalDate curr_date : truck.getAll_transports()) {
                    if (curr_date.equals(chosen_shipment_to_manage.getDate())) {
                        checker = false;
                        break;
                    }
                }
                if (checker) {
                    allTrucks_by_type_totalWeight_availability.add(truck);
                }
            }
        }
        return allTrucks_by_type_totalWeight_availability;

    }


    //TODO: change this? or leave it and in the GUI call the get function a print each truck
    /** function that printing all the truck their type, total weight and availability */
    /*
    public Set<Integer> printing_truck_by_type_totalWeight_availability(TransportShipment chosen_shipment_to_manage, int current_weight) {

        String transport_type = chosen_shipment_to_manage.getTransport_document().getTransport_type();

        int level_skill_type = 1;

        if (Objects.equals(transport_type, "Dry"))
            level_skill_type = 1;
        else if (Objects.equals(transport_type, "Cooler"))
            level_skill_type = 2;
        else if (Objects.equals(transport_type, "Freezer"))
            level_skill_type = 3;

        boolean checker20 = false;
        Set<Integer> truck_choices = new HashSet<Integer>();

        int count = 1;

        boolean checker;
        for (ATruck truck : get_all_trucks().values()) {
            checker=true;
            if (truck.getTruck_level() == level_skill_type &&
                    Objects.equals(truck.getStatus(),TruckStatus.Available) &&
                    truck.getStatus().ordinal() == 0 &&
                    (truck.getNet_weight() + truck.getMax_cargo_weight() >= current_weight)) {

                for (LocalDate curr_date : truck.getAll_transports()) {
                    if (curr_date.equals(chosen_shipment_to_manage.getDate())) {
                        checker = false;
                        break;
                    }
                }
                if (checker) {
                    checker20 = true;
                    System.out.println(count + ". ");
                    truck_choices.add(truck.getLicense_plate_number());
                    System.out.println(truck);
                }
            }
        }

        //truck.check_if_truck_transporting
        if (!checker20) {
            return null;
        }
        return truck_choices;
    }

     */

    /** function that creat new transport orders by the types of the products that have been chosen */
    public void creating_new_transport_order_by_products_type_Controller(Source find_source, Destination find_dest, HashMap<Product, Integer> current_order) {

        try {

            int max = get_highest_order_id() + 1;
            HashMap<Product, Integer> all_dry_products = TransportOrder.get_dry_products(current_order);
            HashMap<Product, Integer> all_cooler_products = TransportOrder.get_cooler_products(current_order);
            HashMap<Product, Integer> all_freezer_products = TransportOrder.get_freezer_products(current_order);

            if (!all_dry_products.isEmpty()) {
                TransportOrder only_dry_order = new TransportOrder(max, find_source, find_dest, all_dry_products);
                transportOrderDAO.insertTransportOrder(only_dry_order);
                max++;
            }
            if (!all_cooler_products.isEmpty()) {
                TransportOrder only_cooler_order = new TransportOrder(max, find_source, find_dest, all_cooler_products);
                transportOrderDAO.insertTransportOrder(only_cooler_order);
                max++;

            }
            if (!all_freezer_products.isEmpty()) {
                TransportOrder only_freezer_order = new TransportOrder(max, find_source, find_dest, all_freezer_products);
                transportOrderDAO.insertTransportOrder(only_freezer_order);
                max++;
            }
        } catch (SQLException e) {
            System.out.println("Exception from trying insert new transport order to the system");
        }
    }

    /** saving the truck of the chosen shipment */
    public ATruck get_truck_of_transport (int license_plate) {

        try {
            ATruck current_truck = null;
            for (ATruck allTruck : aTruckDAO.getAllTrucks().values()) {
                if (allTruck.getLicense_plate_number() == license_plate)
                    current_truck = allTruck;
            }
            return current_truck;
        } catch (SQLException e) {
            System.out.println("Exception from trying to get all trucks in the system");
        }
        return null;
    }

    /** function that update all needed while finishing transport shipment */
    public void Finish_Transport_Controller (TransportShipment chosen_transport_shipment) {
        try {
            chosen_transport_shipment.FinishTransport();
            transportShipmentDAO.deleteTransportShipment(chosen_transport_shipment.getTransport_id());
            transportDocumentDAO.updateTransportDocument(chosen_transport_shipment.getTransport_document());
        } catch (SQLException e) {
            System.out.println("Exception from trying delete transport shipment or update transport document");
        }
    }

    /** function that return transport order by id*/
    public TransportOrder get_transport_order_by_id(int id) {
        try {
            return transportOrderDAO.getTransportOrderById(id);
        } catch (SQLException e) {
            System.out.println("Exception from trying get transport order by id");
        }
        return null;
    }

    /** checking if there are any orders in the system */
    public boolean check_if_exist_unsigned_transport_orders() {
        try {
            HashMap<Integer, TransportOrder> transportOrders = transportOrderDAO.getAllTransportOrders();

                for (TransportOrder order : transportOrders.values()){
                    if (order.getAssigned_doc_id() == -1){
                        return true;
                    }
                }

        } catch (SQLException e) {
            System.out.println("Exception from trying get all transport orders in the system");
        }
        return false;
    }

    /** function that creating a new transport document and adding it to the system and update the assigned of the transport order belong to it in the data base */
    public TransportDocument creating_new_transport_document(LocalDate date, int license_plate_number, LocalTime departureTime, Driver chosen_driver,ArrayList<TransportOrder> all_transport_orders_into_document, String shipping_area, String order_type){

        TransportDocument new_transport_document = new TransportDocument(date, license_plate_number, departureTime, chosen_driver, all_transport_orders_into_document, shipping_area, order_type);
        try {
            transportDocumentDAO.insertTransportDocument(new_transport_document);
            new_transport_document.update_all_orders_doc_id();
            for (TransportOrder order : new_transport_document.getAll_transport_orders())
            {
                transportOrderDAO.updateTransportOrder(order);
            }
            transportDocumentDAO.updateTransportDocument(new_transport_document);
            return new_transport_document;
        }
        catch (Exception e) {
            System.out.println("Failed to create new transport document: " + e.getMessage());
        }
        return null;
    }

    public ArrayList<TransportDocument> getAll_transport_documents_by_type (String order_type, String order_area) {
        ArrayList<TransportDocument> all_transport_documents_by_type = new ArrayList<>();

        try {
            for (TransportDocument current_doc : transportDocumentDAO.getAllTransportDocuments().values()) {

                if (Objects.equals(current_doc.getTransport_type(), order_type) && Objects.equals(current_doc.getShipping_area(), order_area) && current_doc.getDoc_status() == DocumentStatus.waiting) {
                    all_transport_documents_by_type.add(current_doc);
                }
            }
        }
        catch (Exception e) {
            System.out.println("Exception in getAll_transport_documents_by_type " + e.getMessage());
        }

        return all_transport_documents_by_type;
    }


    //TODO: change this? or leave it and in the GUI call the get function a print each document by type
    /** function that represent all transport documents by type and by area */
    /*
    public ArrayList<Integer> represent_all_transport_documents_by_type(String order_type, String order_area){

        try {
            ArrayList<Integer> index_list = new ArrayList<Integer>();
            boolean exist_document = false;

            // looking for all the transports documents if exist in the system and show him only the relevant documents by the same type
            int count = 1;
            for (TransportDocument current_doc : transportDocumentDAO.getAllTransportDocuments().values()) {

                if (Objects.equals(current_doc.getTransport_type(), order_type) && Objects.equals(current_doc.getShipping_area(), order_area) && current_doc.getDoc_status() == DocumentStatus.waiting) {
                    exist_document = true;
                    System.out.println(count + ". " + current_doc);
                    count++;
                    index_list.add(current_doc.getTransport_document_ID());
                }
            }
            if (!exist_document) {
                System.out.println("There aren't matches for you.");
            }
            return index_list;
        }
        catch (Exception e) {
            System.out.println("Failed to represent all transport documents by type: " + e.getMessage());
        }
        return null;
    }
    */


    public ArrayList<ATruck> getAll_trucks_by_type_and_available(int level_skill, LocalDate date){
        ArrayList<ATruck> all_trucks_by_type_and_available = new ArrayList<>();

        try {


            for (ATruck truck : aTruckDAO.getAllTrucks().values()) {
                if (truck.getTruck_level() == level_skill &&
                        truck.getStatus() == TruckStatus.Available &&
                        !truck.check_if_truck_transporting(date)) {
                    all_trucks_by_type_and_available.add(truck);
                }
            }
        }
        catch (Exception e) {
            System.out.println("Failed to represent all trucks exist by type and availability: " + e.getMessage());
        }

        return all_trucks_by_type_and_available;
    }



    //TODO: change this? or leave it and in the GUI call the get function a print each truck by type and availability
    /** function that represent all trucks exist by type and availability */
    /*
    public ArrayList<Integer> represent_all_trucks_exist_by_type_and_available(int level_skill, LocalDate date){

        try {
            ArrayList<Integer> index_list = new ArrayList<Integer>();
            boolean checker4 = false;

            int count = 1;
            for (ATruck truck : aTruckDAO.getAllTrucks().values()) {
                if (truck.getTruck_level() == level_skill &&
                        truck.getStatus() == TruckStatus.Available &&
                        !truck.check_if_truck_transporting(date)) {
                    checker4 = true;
                    System.out.println(count + ". ");
                    count++;
                    System.out.println(truck);
                    index_list.add(truck.getLicense_plate_number());
                }
            }
            if (!checker4) {
                return index_list;
            }
            return index_list;
        }
        catch (Exception e) {
            System.out.println("Failed to represent all trucks exist by type and availability: " + e.getMessage());
        }
        return null;
    }

     */

    /** function that handle with exceeded weight of the shipment by skip the specific supplier */
    public void HandleWeight_SkipSupplier_Controller (TransportShipment transport_shipment, TransportOrder transport_order)  {
        /**  removing the supplier and creating a new transport order with the same items  */

        transport_shipment.RemoveSupplier(transport_order);
        try {
            transportDocumentDAO.updateTransportDocument(transport_shipment.getTransport_document());
            transportShipmentDAO.updateTransportShipment(transport_shipment);
            transport_order.setAssigned_doc_id(-1);
            transportOrderDAO.updateTransportOrder(transport_order);
        }
        catch (Exception e)
        {
            System.out.println("Failed to Handle Weight- Skip Supplier exception: " + e.getMessage());
        }
    }

    public ArrayList<TransportOrder> getAllTransportOrdersOfShipment(TransportShipment transportShipment){
        return transportShipment.getTransport_document().getAll_transport_orders();
    }
    /** function that featuring all the transport documents that are relevant for a specific date, time and branch */
    public static String displayTransportsForShift (LocalDate date, LocalTime start_time, LocalTime end_time, int branchID) {

        String output = "";

        try {
            for (TransportDocument curr_doc : transportDocumentDAO.getAllTransportDocuments().values()) {

                if (curr_doc.getDate().equals(date) && (curr_doc.getDeparture_time().isBefore(end_time) || curr_doc.getDeparture_time().equals(end_time)) && (curr_doc.getDeparture_time().isAfter(start_time)) || curr_doc.getDeparture_time().equals(start_time)) {

                    for (TransportOrder curr_order : curr_doc.getAll_transport_orders()) {
                        if (curr_order.getDestination().getID() == branchID)
                            output += curr_doc.toString() + "\n";
                        //  output += "\n";
                    }
                }
            }
            return output;
        }
        catch (Exception e) {
            System.out.println("Failed to display transports for shift: " + e.getMessage());
            return null;
        }
    }


    /** function that return truck by the license plate number */
    public ATruck get_truck (int licence_plate) {
        try {
            return aTruckDAO.getATruckById(licence_plate);
        }
        catch (Exception e) {
            System.out.println("Failed to get a truck by ID: " + e.getMessage());
        }
        return null;
    }

    /** function that updates a truck status */
    public void update_truck_status (TruckStatus new_status, ATruck truck_to_update){
        try {
            truck_to_update.setStatus(new_status);
            aTruckDAO.updateATruck(truck_to_update);
        }
        catch (Exception e) {
            System.out.println("Failed to update truck status: " + e.getMessage());
        }
    }

    /** function that adds to a truck a new date for it to carry a transport */
    public void update_truck_transports_controller (ATruck truck, LocalDate date){
            truck.addTransportDate(date);

    }

    /** function that update the neccecery details in the system, after changing a truck during transport. */
    public void update_details_after_change_truck(TransportShipment transportShipment, Driver new_driver_for_transport, ATruck chosen_truck, int current_weight) {
        /**  finding the old truck and update it to available  */

        try {
            int old_truck_license_plate = transportShipment.getTruck_license_plate_number();
            update_truck_status(TruckStatus.Available, aTruckDAO.getATruckById(old_truck_license_plate));
            update_truck_status(TruckStatus.NotAvailable, chosen_truck);
            aTruckDAO.updateATruck(aTruckDAO.getATruckById(old_truck_license_plate));
            aTruckDAO.updateATruck(aTruckDAO.getATruckById(chosen_truck.getLicense_plate_number()));
        }
        catch (Exception e) {
            System.out.println("Failed to update details after change truck: " + e.getMessage());
        }

        /**  updating all necessary details after the change of the truck  */
        transportShipment.setDriver_name(new_driver_for_transport.getEmployeeFullName());
        transportShipment.getTransport_document().setDriver(new_driver_for_transport);
        transportShipment.setTruck_license_plate_number(chosen_truck.getLicense_plate_number());
        transportShipment.getTransport_document().setTruck_license_plate_number(chosen_truck.getLicense_plate_number());
        transportShipment.setCargo_weight(current_weight);
        try {
            transportDocumentDAO.updateTransportDocument(transportShipment.getTransport_document());
            transportShipmentDAO.updateTransportShipment(transportShipment);
        }
        catch (Exception e) {
            System.out.println("Failed to update transport shipment in the system: " + e.getMessage());
        }
    }

    public Product getProductByCode(String code_of_product){
        try{
             return productDAO.getProductByCode(code_of_product);
        }
        catch (Exception e){
            System.out.println("Failed to get product by code: " + e.getMessage());
        }
        return null;
    }
    /** function that update in a transport order in the system the new amount of a specific product */
    public void update_product_amount_in_order_controller(TransportOrder order, Product product, int amount) {

        order.update_amount_of_product_in_order(product,amount);
        try {
            transportOrderDAO.updateTransportOrder(order);
        }
        catch (Exception e) {
            System.out.println("Failed to update product amount in order: " + e.getMessage());
        }
    }

    /** function that adding a new transport order to the system */
    public void add_new_transport_order (TransportOrder new_transport) {
        try {
            transportOrderDAO.insertTransportOrder(new_transport);
        }
        catch (Exception e) {
            System.out.println("Failed to add a new transport order to the system: " + e.getMessage());
        }
    }

    /** function that check returns the number of transport documents in the system */
    public int get_num_of_waiting_transport_documents(){
        try {
            HashMap<Integer, TransportDocument> all_transport_documents = transportDocumentDAO.getAllTransportDocuments();
            int count = 0;
            for (TransportDocument transportDocument : all_transport_documents.values()){
                if (transportDocument.getDoc_status().equals(DocumentStatus.waiting)){
                    count++;
                }
            }
            return count;
        }
        catch (Exception e) {
            System.out.println("Failed to return the number of transport documents in the system: " + e.getMessage());
            return -1;
        }
    }

    /** function that execute the transport that the user chose  */
    public void execute_transport_controller (TransportDocument chosen_document) {

        chosen_document.setDoc_status(DocumentStatus.inProgress);

        try {
            transportDocumentDAO.updateTransportDocument(chosen_document);
        }
        catch (Exception e) {
            System.out.println("Failed to update document new status to inProgress: " + e.getMessage());
            return;
        }
        /** creating the transport shipment with all the details the system got from the manager */
        Source source_of_transport = chosen_document.getAll_transport_orders().get(0).getSource();

        TransportShipment new_transport_shipment = new TransportShipment(chosen_document.getDriver().getEmployeeFullName(),
                chosen_document.getDate(), chosen_document.getDeparture_time(),
                chosen_document.getTruck_license_plate_number(), 0, new HashSet<ASite>(), source_of_transport, chosen_document);

        /** activate the execute transport function after the system create the transport with all the details*/
        new_transport_shipment.ExecuteTransport();

        /** adding the transport to the system */
        try {
            transportShipmentDAO.insertTransportShipment(new_transport_shipment);
        }
        catch (Exception e) {
            System.out.println("Failed to insert new transport shipment to the system: " + e.getMessage());
            return;
        }

    }


    /** function that check if transport document exist by id */
    public boolean check_if_document_id_exist(int document_ID){

        try {
            TransportDocument transportDocument = transportDocumentDAO.getTransportDocumentById(document_ID);
            if (transportDocument == null) {
                return false;
            }
        }
        catch (SQLException e){
            System.out.println("Failed to check if a transport document exist by id: " + e.getMessage());
        }
        return true;
    }

    /** function that get transport document exist by id */
    public TransportDocument get_transport_document_by_id (int doc_id){

        try{
            return transportDocumentDAO.getTransportDocumentById(doc_id);
        }
        catch (SQLException e){
            System.out.println("Failed to return a specific transport document by id: " + e.getMessage());
        }
        return null;
    }

    /** function that check if transport shipment exist by id */
    public TransportShipment get_transport_shipment_by_id (int ship_id){

        try{
            return transportShipmentDAO.getTransportShipmentById(ship_id);
        }
        catch (SQLException e){
            System.out.println("Failed to return a specific transport shipment by id: " + e.getMessage());
        }
        return null;
    }

    /** function that get driver by id */
    public Driver get_driver_in_company_controller (String id) {
        return companyController.driverController.getDriverInCompany(id);
    }

    /** function that display all drivers that available in specific date and time */
    public String display_available_drivers_controller (LocalDate date, LocalTime time, double weight, int license) {
        return companyController.displayAvailableDrivers(date, time, weight, license);
    }
    public ArrayList<Driver> get_all_available_drivers_controller (LocalDate date, LocalTime time, double weight, int license) {
        return companyController.getAvailableDrivers(date,time,weight,license);
    }

    public String get_available_drivers_controller (LocalDate date, LocalTime time, double weight, int license) {
        return companyController.displayAvailableDrivers(date, time, weight, license);
    }

    /** function that add driver by id date and time to a shift */
    public boolean add_driver_to_shift_controller (String driver_id, LocalDate date, LocalTime time) {
        return companyController.addDriverToShift(driver_id, date, time);
    }

    /** function that checking if there is a storekeeper assigned to specific shift according to site id, date and time  */
    public boolean check_if_storeKeeperA_assigned (int site_id, LocalDate date, LocalTime time) {
        return companyController.checkIfStoreKeeperAssigned(site_id, date, time);
    }

    public int get_highest_order_id(){
        try {
            ArrayList<Integer> all_orders_id = transportOrderDAO.get_all_transport_orders_id();
            int max = 0;
            for (int num : all_orders_id){
                if (num > max){
                    max = num;
                }
            }
            return max;

        } catch (Exception e) {
            System.out.println("Exception while trying get all transport orders id :" + e.getMessage());
        }
        return 1;
    }


    /** function that return all the truck in the system */
    public HashMap<Integer,ATruck> get_all_trucks () {
        try {
            return aTruckDAO.getAllTrucks();
        }
        catch (Exception e) {
            System.out.println("Failed to get all trucks in the system: " + e.getMessage());
        }
        return null;
    }

    /** function that returns all transport shipments in the system */
    public HashMap<Integer,TransportShipment> getAll_transport_shipments () {
        try {
            return transportShipmentDAO.getAllTransportShipments();
        }
        catch (Exception e){
            System.out.println("Failed to return all transport shipments: " + e.getMessage());
            return null;
        }
    }

    /** function that returns all transport documents in the system */
    public HashMap<Integer,TransportDocument> getAll_transport_documents () {
        try {
            return transportDocumentDAO.getAllTransportDocuments();
        }
        catch (Exception e){
            System.out.println("Failed to return all transport documents: " + e.getMessage());
            return null;
        }
    }

    /** function that returns all transport orders in the system */
    public HashMap<Integer,TransportOrder> getAll_transport_orders () {
        try {
            return transportOrderDAO.getAllTransportOrders();
        }
        catch (Exception e){
            System.out.println("Failed to return all transport orders: " + e.getMessage());
            return null;
        }
    }

    public ArrayList<TransportOrder> getAll_unsigned_transport_orders () {
        ArrayList<TransportOrder> all_unsigned_orders = new ArrayList<>();

        try {
            for (TransportOrder curr_order : transportOrderDAO.getAllTransportOrders().values()) {
                if (curr_order.getAssigned_doc_id() == -1)
                    all_unsigned_orders.add(curr_order);
            }
        }
        catch (Exception e) {
            System.out.println("Failed to return all unsigned transport orders: " + e.getMessage());
            return null;
        }
        return all_unsigned_orders;
    }
}

