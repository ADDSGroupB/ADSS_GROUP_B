// Import necessary classes
import java.util.Date;
import java.util.HashSet;

// Define the TransportShipment class
public class TransportShipment {

    // Define private attributes of the class
    private String driver_name; // name of the driver
    private final Date date; // date of the shipment
    private final int transport_id; // unique ID for the shipment
    private static int static_id = 1; // static variable for ID generation
    private String departure_time; // departure time for the shipment
    private int truck_license_plate_number; // license plate number of the truck
    private int cargo_weight; // weight of the cargo
    private HashSet<ASite> destinations_list; // list of destination sites for the shipment
    private Source source; // source site for the shipment
    private TransportDocument transport_document; // transport document for the shipment

    // Define getter method for the transport document
    public TransportDocument getTransport_document() {
        return transport_document;
    }

    // Define constructor method for the class
    public TransportShipment(String driver_name, Date date, String departure_time, int truck_license_plate_number,
                             int cargo_weight, HashSet<ASite> destinations_list, Source source, TransportDocument transport_document) {
        // Assign values to the attributes of the class
        this.driver_name = driver_name;
        this.date = date;
        this.transport_id = static_id++; // Assign unique ID to the shipment
        this.departure_time = departure_time;
        this.truck_license_plate_number = truck_license_plate_number;
        this.cargo_weight = cargo_weight;
        this.destinations_list = destinations_list;
        this.source = source;
        this.transport_document = transport_document;
    }

    // Define getter method for the source site
    public Source getSource() {
        return source;
    }

    // Define setter method for the cargo weight
    public void setCargo_weight(int cargo_weight) {
        this.cargo_weight = cargo_weight;
    }

    // Define getter method for the cargo weight
    public int getCargo_weight() {
        return cargo_weight;
    }

    // Define toString method to represent the class as a string
    @Override
    public String toString() {
        return "TransportShipment { " +
                transport_document +
                '}';
    }

    // Define getter method for the destinations list
    public HashSet<ASite> getDestinations_list() {
        return destinations_list;
    }

    // Define getter method for the transport ID
    public int getTransport_id() {
        return transport_id;
    }

    // Define getter method for the license plate number of the truck
    public int getTruck_license_plate_number() {
        return truck_license_plate_number;
    }

    // Define setter method for the driver name
    public void setDriver_name(String driver_name) {
        this.driver_name = driver_name;
    }

    // Define setter method for the license plate number of the truck
    public void setTruck_license_plate_number(int truck_license_plate_number) {
        this.truck_license_plate_number = truck_license_plate_number;
    }

    public void ExecuteTransport(){

        for (int i = 1; i < this.transport_document.getAll_transport_orders().size(); i++) {
            this.destinations_list.add(this.transport_document.getAll_transport_orders().get(i).getSource());
        }
        for (int i = 0; i < this.transport_document.getAll_transport_orders().size(); i++) {
            this.destinations_list.add(this.transport_document.getAll_transport_orders().get(i).getDestination());
        }
    }
    public void FinishTransport(ATruck truck){

        truck.setStatus(TruckStatus.Available);
        this.getTransport_document().getDriver().setDriver_status(DriversStatus.Available);
    }

    public void RemoveTransportOrder(TransportOrder removing_transport_order){

        this.transport_document.RemoveTransportOrder(removing_transport_order);
    }

    public void RemoveSupplier(TransportOrder current_transport_order){

        this.RemoveTransportOrder(current_transport_order);

        boolean double_order_supplier = false;
        boolean double_order_branch = false;

        if (!this.transport_document.getAll_transport_orders().isEmpty()) {
            for (TransportOrder checking_for_double_site : this.transport_document.getAll_transport_orders()) {

                if (checking_for_double_site.getSource().getID() == current_transport_order.getSource().getID()) {
                    double_order_supplier = true;
                }

                if (checking_for_double_site.getDestination().getID() == current_transport_order.getDestination().getID()) {
                    double_order_branch = true;
                }
            }
        }

        if (!double_order_supplier) {

            this.destinations_list.remove(current_transport_order.getSource());
        }

        if (!double_order_branch) {

            this.destinations_list.remove(current_transport_order.getDestination());
        }
    }

}
