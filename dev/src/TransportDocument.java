
import java.util.ArrayList;
import java.util.Date;


public class TransportDocument {

    // Properties of the TransportDocument class
    private final int transport_document_ID;
    private static int static_id=1;
    private Date date;
    private int truck_license_plate_number;
    private final String departure_time;
    private Driver driver;
    private ArrayList<TransportOrder> all_transport_orders;
    private final String shipping_area;
    private final String transport_type;

    // Constructor of the TransportDocument class
    public TransportDocument(Date date, int truck_license_plate_number, String departure_time,
                             Driver driver, ArrayList<TransportOrder> all_transport_orders, String shipping_area,
                             String transport_type) {

        // Set the properties of the TransportDocument class using the constructor arguments
        this.transport_document_ID = static_id++;
        this.date = date;
        this.truck_license_plate_number = truck_license_plate_number;
        this.departure_time = departure_time;
        this.driver = driver;
        this.all_transport_orders = all_transport_orders;
        this.shipping_area = shipping_area;
        this.transport_type = transport_type;
    }

    // Getter method for the transport_document_ID property
    public int getTransport_document_ID() {
        return transport_document_ID;
    }

    // Getter method for the static_id property
    public static int getStatic_id() {
        return static_id;
    }

    // Getter method for the date property
    public Date getDate() {
        return date;
    }

    // Getter method for the truck_license_plate_number property
    public int getTruck_license_plate_number() {
        return truck_license_plate_number;
    }

    // Getter method for the departure_time property
    public String getDeparture_time() {
        return departure_time;
    }

    // Getter method for the driver_name property
    public String getDriver_name() {
        return this.driver.getDriver_name();
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
        this.all_transport_orders.add(transportOrder_to_add);
    }

    // toString method for the TransportDocument class
    @Override
    public String toString() {
        return "TransportDocument {" +
                "transport_document_ID = " + transport_document_ID +
                ", date = " + date +
                ", departure_time = '" + departure_time + '\'' +
                ", shipping_area = '" + shipping_area + '\'' +
                ", transport_type = '" + transport_type + '\'' +
                '}';
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
