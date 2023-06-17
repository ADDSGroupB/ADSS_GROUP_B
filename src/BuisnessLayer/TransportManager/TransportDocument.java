package BuisnessLayer.TransportManager;

import BuisnessLayer.HR.Driver;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;


public class TransportDocument {

    // Properties of the TransportDocument class
    private int transport_document_id;
    private LocalDate date;
    private int truck_license_plate_number;
    private LocalTime departure_time;
    private Driver driver;
    private ArrayList<TransportOrder> all_transport_orders;
    private final String shipping_area;
    private final String transport_type;
    private DocumentStatus doc_status;

    // Constructor of the TransportDocument class
    public TransportDocument(LocalDate date, int truck_license_plate_number, LocalTime departure_time,
                             Driver driver, ArrayList<TransportOrder> all_transport_orders, String shipping_area,
                             String transport_type) {

        // Set the properties of the TransportDocument class using the constructor arguments
        this.transport_document_id = -1;
        this.date = date;
        this.truck_license_plate_number = truck_license_plate_number;
        this.departure_time = departure_time;
        this.driver = driver;
        this.all_transport_orders = all_transport_orders;
        this.shipping_area = shipping_area;
        this.transport_type = transport_type;
        this.doc_status = DocumentStatus.waiting;
    }

    // Constructor of the TransportDocument class
    public TransportDocument(LocalDate date, int truck_license_plate_number, LocalTime departure_time,
                             Driver driver, ArrayList<TransportOrder> all_transport_orders, String shipping_area,
                             String transport_type, int id) {

        // Set the properties of the TransportDocument class using the constructor arguments
        this.date = date;
        this.truck_license_plate_number = truck_license_plate_number;
        this.departure_time = departure_time;
        this.driver = driver;
        this.all_transport_orders = all_transport_orders;
        this.shipping_area = shipping_area;
        this.transport_type = transport_type;
        this.transport_document_id = id;
        this.doc_status = DocumentStatus.waiting;
    }

    public void update_all_orders_doc_id () {
        for (TransportOrder order : all_transport_orders) {
            order.setAssigned_doc_id(transport_document_id);
        }
    }
    public void setTransport_document_id(int transport_document_id) {
        this.transport_document_id = transport_document_id;
    }

    public void setDoc_status (DocumentStatus status) {
        this.doc_status = status;
    }

    public DocumentStatus getDoc_status () {
        return this.doc_status;
    }

    // Getter method for the transport_document_ID property
    public int getTransport_document_ID() {
        return transport_document_id;
    }

    // Getter method for the date property
    public LocalDate getDate() {
        return date;
    }

    // Getter method for the truck_license_plate_number property
    public int getTruck_license_plate_number() {
        return truck_license_plate_number;
    }

    // Getter method for the departure_time property
    public LocalTime getDeparture_time() {
        return departure_time;
    }

    // Getter method for the driver property
    public Driver getDriver() {
        return driver;
    }

    // Getter method for the all_transport_orders property
    public ArrayList<TransportOrder> getAll_transport_orders() {
        return all_transport_orders;
    }

    // Getter method for the shipping_area property
    public String getShipping_area() {
        return shipping_area;
    }

    // Getter method for the transport_type property
    public String getTransport_type() {
        return transport_type;
    }

    // Method to add a transport order to the all_transport_orders property
    public void add_transport_order (TransportOrder transportOrder_to_add){
        transportOrder_to_add.setAssigned_doc_id(transport_document_id);
        this.all_transport_orders.add(transportOrder_to_add);
    }

    // toString method for the TransportDocument class
    @Override
    public String toString() {
        return "Transport Document ID: " + transport_document_id +
                ", Occurring on: " + date +
                ", at: " + departure_time +
                ", Shipping Area: " + shipping_area +
                ", Transport Type: " + transport_type +
                ", Current Status is: " + doc_status +
                ", Holding the following Transport Orders ID: " + get_all_transport_orders_id_in_string();
    }

    public String get_all_transport_orders_id_in_string() {
        String ans="";

        for (TransportOrder curr_order : all_transport_orders)
            ans += curr_order.getTransport_order_id() + ", ";

        ans = ans.substring(0,ans.length()-2);
        return ans;
    }

    // Setter method for the truck_license_plate_number property
    public void setTruck_license_plate_number(int truck_license_plate_number) {
        this.truck_license_plate_number = truck_license_plate_number;
    }

    // Setter method for the driver property
    public void setDriver(Driver driver) {
        this.driver = driver;
    }


    public void RemoveTransportOrder(TransportOrder transport_order_to_remove){

        this.all_transport_orders.remove(transport_order_to_remove);

    }
}
