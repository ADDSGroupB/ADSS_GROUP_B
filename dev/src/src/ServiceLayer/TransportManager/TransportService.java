package ServiceLayer.TransportManager;


import BuisnessLayer.HR.Driver;
import BuisnessLayer.TransportManager.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;

public class TransportService {

    private TransportController transportController;

    public TransportService() {

        try {
            transportController = new TransportController();
        }
        catch (Exception e) {
            System.out.println("Exception in TransportSerivce constructor: " + e.getMessage());
        }
    }

    public void build () {
        transportController.build();
    }

    public boolean check_if_truck_plate_number_exist(int license_plate_number) {
        return transportController.check_if_truck_plate_number_exist(license_plate_number);
    }

    public boolean switch_truck_status_controller(int license_plate_number) {
        return transportController.switch_truck_status_controller(license_plate_number);
    }

    public void add_Truck(int license_plate_number, int truck_net_weight, int truck_max_cargo_weight, int truck_level_type){
         transportController.add_Truck(license_plate_number,truck_net_weight,truck_max_cargo_weight,truck_level_type);
    }

    public void updating_documents_in_the_system(TransportOrder order, int document_id){
        transportController.updating_documents_in_the_system(order,document_id);
    }

    public void update_transport_order(TransportOrder order){
        transportController.update_transport_order(order);
    }

    public boolean remove_Truck(int license_plate_number_truck_to_remove) {
        return transportController.remove_Truck(license_plate_number_truck_to_remove);
    }

    public HashMap<Integer, TransportShipment> getAll_active_transports (){
        return transportController.getAll_active_transports();
    }

    public ArrayList<TransportDocument> getAll_former_transport_document () {
        return transportController.getAll_former_transport_document();
    }

    public ArrayList<TransportDocument> getAll_waiting_transport_documents() {
        return transportController.getAll_waiting_transport_documents();
    }


    public boolean check_if_existing_any_Site_Controller() {
        return transportController.check_if_existing_any_Site_Controller();
    }

    public ASite find_source_Controller(int ID_to_find) {
        return transportController.find_source_Controller(ID_to_find);
    }

    public boolean WeightTruck(TransportShipment chosen_transport, ATruck current_truck, int current_weight) {
        return transportController.WeightTruck(chosen_transport,current_truck,current_weight);
    }

    public void setWeight (TransportShipment shipment, int weight) {
        transportController.setWeight(shipment,weight);
    }

    public ASite find_destination_Controller(int ID_to_find) {
        return transportController.find_destination_Controller(ID_to_find);
    }

    public Product return_product_if_exist_Controller(String product_code_input){
        return transportController.return_product_if_exist_Controller(product_code_input);
    }

    public ArrayList<ATruck> getAll_trucks_by_type_totalWeight_availability(TransportShipment chosen_shipment_to_manage, int current_weight) {
        return transportController.getAll_trucks_by_type_totalWeight_availability(chosen_shipment_to_manage,current_weight);
    }

    public void creating_new_transport_order_by_products_type_Controller(Source find_source, Destination find_dest, HashMap<Product, Integer> current_order) {
        transportController.creating_new_transport_order_by_products_type_Controller(find_source,find_dest,current_order);
    }

    public ATruck get_truck_of_transport (int license_plate) {
        return transportController.get_truck_of_transport(license_plate);
    }

    public void Finish_Transport_Controller (TransportShipment chosen_transport_shipment) {
        transportController.Finish_Transport_Controller(chosen_transport_shipment);
    }

    public TransportOrder get_transport_order_by_id(int id) {
        return transportController.get_transport_order_by_id(id);
    }

    public boolean check_if_exist_unsigned_transport_orders() {
        return transportController.check_if_exist_unsigned_transport_orders();
    }

    public TransportDocument creating_new_transport_document(LocalDate date, int license_plate_number, LocalTime departureTime, Driver chosen_driver, ArrayList<TransportOrder> all_transport_orders_into_document, String shipping_area, String order_type){
        return transportController.creating_new_transport_document(date,license_plate_number,departureTime,chosen_driver,all_transport_orders_into_document,shipping_area,order_type);
    }

    public ArrayList<TransportDocument> getAll_transport_documents_by_type (String order_type, String order_area) {
        return transportController.getAll_transport_documents_by_type(order_type,order_area);
    }

    public ArrayList<ATruck> getAll_trucks_by_type_and_available(int level_skill, LocalDate date){
        return transportController.getAll_trucks_by_type_and_available(level_skill, date);
    }

    public void HandleWeight_SkipSupplier_Controller (TransportShipment transport_shipment, TransportOrder transport_order)  {
        transportController.HandleWeight_SkipSupplier_Controller(transport_shipment,transport_order);
    }

    public ArrayList<TransportOrder> getAllTransportOrdersOfShipment(TransportShipment transportShipment){
        return transportController.getAllTransportOrdersOfShipment(transportShipment);
    }
    //TODO: FIX THIS?
    /*
    public static String displayTransportsForShift (LocalDate date, LocalTime start_time, LocalTime end_time, int branchID) {
        return transportController.displayTransportsForShift(date,start_time,end_time,branchID);
    }
     */

    public ATruck get_truck (int licence_plate) {
        return transportController.get_truck(licence_plate);
    }

    public void update_truck_status (TruckStatus new_status, ATruck truck_to_update){
        transportController.update_truck_status(new_status,truck_to_update);
    }

    public void update_truck_transports_controller (ATruck truck, LocalDate date){
        transportController.update_truck_transports_controller(truck, date);
    }

    public void update_details_after_change_truck(TransportShipment transportShipment, Driver new_driver_for_transport, ATruck chosen_truck, int current_weight) {
        transportController.update_details_after_change_truck(transportShipment,new_driver_for_transport,chosen_truck,current_weight);
    }

    public void update_product_amount_in_order_controller(TransportOrder order, Product product, int amount) {
        transportController.update_product_amount_in_order_controller(order,product,amount);
    }

    public void add_new_transport_order (TransportOrder new_transport) {
        transportController.add_new_transport_order(new_transport);
    }

    public int get_num_of_waiting_transport_documents(){
        return transportController.get_num_of_waiting_transport_documents();
    }

    public void execute_transport_controller (TransportDocument chosen_document) {
        transportController.execute_transport_controller(chosen_document);
    }

    public boolean check_if_document_id_exist(int document_ID){
        return transportController.check_if_document_id_exist(document_ID);
    }

    public TransportDocument get_transport_document_by_id (int doc_id){
        return transportController.get_transport_document_by_id(doc_id);
    }

    public TransportShipment get_transport_shipment_by_id (int ship_id){
        return transportController.get_transport_shipment_by_id(ship_id);
    }


    /** function that get driver by id */
    public Driver get_driver_in_company_controller (String id) {
        return transportController.get_driver_in_company_controller(id);
    }

    /** function that display all drivers that available in specific date and time */
    public String display_available_drivers_controller (LocalDate date, LocalTime time, double weight, int license) {
        return transportController.display_available_drivers_controller(date,time,weight,license);
    }

    public ArrayList<Driver> get_all_available_drivers_controller (LocalDate date, LocalTime time, double weight, int license) {
        return transportController.get_all_available_drivers_controller(date,time,weight,license);
    }



        /** function that add driver by id date and time to a shift */
    public boolean add_driver_to_shift_controller (String driver_id, LocalDate date, LocalTime time) {
        return transportController.add_driver_to_shift_controller(driver_id,date,time);
    }

    /** function that checking if there is a storekeeper assigned to specific shift according to site id, date and time  */
    public boolean check_if_storeKeeperA_assigned (int site_id, LocalDate date, LocalTime time) {
        return transportController.check_if_storeKeeperA_assigned(site_id,date,time);
    }

    public int get_highest_order_id(){
        return transportController.get_highest_order_id();
    }

    public HashMap<Integer,ATruck> get_all_trucks () {
        return transportController.get_all_trucks();
    }

    public HashMap<Integer,TransportShipment> getAll_transport_shipments () {
        return transportController.getAll_transport_shipments();
    }

    public HashMap<Integer,TransportDocument> getAll_transport_documents () {
        return transportController.getAll_transport_documents();
    }

    public HashMap<Integer,TransportOrder> getAll_transport_orders () {
        return transportController.getAll_transport_orders();
    }

    public Product getProductByCode(String code_of_product) {
        return transportController.getProductByCode(code_of_product);
    }
    public HashMap<Integer,ASite> getAllSources(){
        return transportController.getAll_sources();
    }
    public HashMap<Integer,ASite> getAllDestinations(){
        return transportController.getAll_destinations();
    }
    public HashMap<String,Product> getAllProducts(){
        return transportController.getAllProducts();
    }
    public ArrayList<TransportOrder> getAll_unsigned_transport_orders () { return transportController.getAll_unsigned_transport_orders();}








































    }
