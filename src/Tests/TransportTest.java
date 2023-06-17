package Tests;
import BuisnessLayer.HR.CompanyController;
import BuisnessLayer.HR.Driver;
import BuisnessLayer.HR.JobType;
import BuisnessLayer.TransportManager.*;
import DataAccessLayer.Database;
import DataAccessLayer.HR.ShiftDAO;
import DataAccessLayer.TransportManager.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class TransportTest {

    TransportController transportController;
    CompanyController companyController;

    @BeforeEach
    void setUp() {
        try {
            companyController = new CompanyController();
            transportController = new TransportController();
        }
        catch (SQLException e) {
            System.out.println("Failed in setUp transport controller test: " + e.getMessage());

        }
    }

    @AfterEach
    void tearDown() {
    }

    /**
     * Tests the add_Truck() method of the TransportController class by adding a new CoolerTruck object
     * with the given license plate number, net weight, maximum cargo weight, and truck level, and then
     * verifying that the truck was added to the system correctly by checking its attributes. Finally,
     * the method removes the added truck from the system.
     */
    @Test
    void add_Truck() {
        transportController.add_Truck(1234567, 1500, 1000, 3);
        assertEquals(1234567, transportController.get_truck(1234567).getLicense_plate_number(), "Failed in add truck test");
        assertEquals(1500, transportController.get_truck(1234567).getNet_weight(), "Failed in add truck test");
        assertEquals(1000, transportController.get_truck(1234567).getMax_cargo_weight(), "Failed in add truck test");
        assertEquals(3, transportController.get_truck(1234567).getTruck_level(), "Failed in add truck test");
        transportController.remove_Truck(1234567);
    }

    /**
     * Tests the WeightTruck() method of the TransportController class by creating a new CoolerTruck,
     * TransportOrder, Driver, TransportDocument and TransportShipment objects, inserting the shipment
     * into the database, verifying that the truck can carry the shipment weight, and then deleting the
     * shipment from the database.
     */
    @Test
    void weightTruck() {

        Product pr1 = new Product("20597", "Tomato", "Dry", "Osem",  "Dry", "eat", 500);
        Product pr2 = new Product("19191" , "Tuna", "Dry", "Tara", "Dry", "eat", 750);

        Address add1 = new Address(7, "South", "Mezada 36, Beer Sheva");
        Address add2 = new Address(245, "North", "King Salomon 68, Beer Sheva");

        Destination destination = new Destination(7, "0544692010", "Miri Agmon", "Destination", add1);
        Source source = new Source(245, "0509030798", "Gil Agmon", "Source", add2);

        HashSet<ASite> dest_list = new HashSet<>();
        dest_list.add(destination);

        HashMap<Product, Integer> products = new HashMap<>();
        products.put(pr1, 50);
        products.put(pr2, 100);

        TransportOrder order = new TransportOrder(15, source, destination, products);

        ATruck truck3 = new CoolerTruck(1234567, 2250, 1500);

        ArrayList<JobType> jobs_type = new ArrayList<>();
        Set<Integer> set_branches = new HashSet<Integer>();
        Driver driver = new Driver(3000, 3, "Noam Barak", "208753698",
                "Bank Leumi, 36987", "17-10-2021", 50,
                "Student", jobs_type, "Regular terms Of Employment", "noamb12", set_branches);

        LocalDate date = LocalDate.of(2023, 05, 14);
        LocalTime time = LocalTime.of(8, 0, 0);
        ArrayList<TransportOrder> allOrders = new ArrayList<>();
        allOrders.add(order);

        TransportDocument document = new TransportDocument(date, truck3.getLicense_plate_number(), time, driver, allOrders, "South", "Cooler",456789);
        TransportShipment shipment = new TransportShipment(driver.getEmployeeFullName(), date, time, truck3.getLicense_plate_number(), 1000, dest_list, source, document);

        TransportShipmentDAO transportShipmentDAO = null;
        try {
            transportShipmentDAO = new TransportShipmentDAO();
            transportShipmentDAO.insertTransportShipment(shipment);

        } catch (Exception e) {
            System.out.println("Failed while insert shipment in WeightTruckTest: " + e.getMessage());
        }

        boolean weight_truck = transportController.WeightTruck(shipment, truck3, 3250);
        assertTrue(weight_truck, "Failed in weightTruck Test, got false and not true ");

        try {
            transportShipmentDAO.deleteTransportShipment(shipment.getTransport_id());
        }
        catch (SQLException e){
            System.out.println("Failed while delete shipment in WeightTruckTest: " + e.getMessage());
        }
    }

    /** Test the Finish_Transport_Controller function by creating a TransportShipment and a TransportDocument, inserting them into the database,
     then calling the Finish_Transport_Controller function and verifying that the TransportDocument status has changed to "finished".
     Also tests the insertion and deletion of TransportDocument and TransportShipment into/from the database.
     */
    @Test
    void finish_Transport_Controller() {

        Product pr1 = new Product("20597", "Tomato", "Dry", "Osem",  "Dry", "eat", 500);
        Product pr2 = new Product("19191" , "Tuna", "Dry", "Tara", "Dry", "eat", 750);

        Address add1 = new Address(7, "South", "Mezada 36, Beer Sheva");
        Address add2 = new Address(245, "North", "King Salomon 68, Beer Sheva");

        Destination destination = new Destination(7, "0544692010", "Miri Agmon", "Destination", add1);
        Source source = new Source(245, "0509030798", "Gil Agmon", "Source", add2);

        HashSet<ASite> dest_list = new HashSet<>();
        dest_list.add(destination);

        HashMap<Product, Integer> products = new HashMap<>();
        products.put(pr1, 50);
        products.put(pr2, 100);

        TransportOrder order = new TransportOrder(15, source, destination, products);

        ATruck truck3 = new CoolerTruck(1234567, 2250, 1500);

        ArrayList<JobType> jobs_type = new ArrayList<>();
        Set<Integer> set_branches = new HashSet<Integer>();
        Driver driver = new Driver(3000, 3, "Noam Barak", "208753698",
                "Bank Leumi, 36987", "17-10-2021", 50,
                "Student", jobs_type, "Regular terms Of Employment", "noamb12", set_branches);

        LocalDate date = LocalDate.of(2023, 05, 14);
        LocalTime time = LocalTime.of(8, 0, 0);
        ArrayList<TransportOrder> allOrders = new ArrayList<>();
        allOrders.add(order);

        TransportDocument document = new TransportDocument(date, truck3.getLicense_plate_number(), time, driver, allOrders, "South", "Cooler", 938457);
        TransportShipment shipment = new TransportShipment(driver.getEmployeeFullName(), date, time, truck3.getLicense_plate_number(), 1000, dest_list, source, document);

        TransportShipmentDAO transportShipmentDAO = null;
        TransportDocumentDAO transportDocumentDAO = null;
        try {
            transportDocumentDAO = new TransportDocumentDAO();
            transportDocumentDAO.insertTransportDocument(document);
            transportShipmentDAO = new TransportShipmentDAO();
            transportShipmentDAO.insertTransportShipment(shipment);


        } catch (Exception e) {
            System.out.println("Failed while insert shipment / document / order in FinishTransport Test: " + e.getMessage());
        }

        transportController.Finish_Transport_Controller(shipment);
        assertEquals(document.getDoc_status(), DocumentStatus.finished, "Failed in FinishTransport Test, wanted finished but got " + document.getDoc_status());

        try {
            transportDocumentDAO.deleteTransportDocument(document.getTransport_document_ID());
        }
        catch (SQLException e){
            System.out.println("Failed while delete shipment in WeightTruckTest: " + e.getMessage());
        }
    }

    /** This unit test checks whether the check_if_truck_plate_number_exist method in the
     * TransportController class correctly identifies whether a truck with a given license plate number exists in the system.
     * It adds a new truck with a specific license plate number, calls the method with the same number, and checks that the result is true.
     * Finally, it removes the added truck to ensure that the system state is not affected by the test.*/
    @Test
    void check_if_truck_plate_number_exist() {

        transportController.add_Truck(1758697, 1500, 1000, 3);
        boolean result = transportController.check_if_truck_plate_number_exist(1758697);
        assertTrue(result, "Failed in check_if_truck_plate_number_exist Test, got false and not true ");

        transportController.remove_Truck(1758697);
    }

    /** This test function checks if the get_transport_order_by_id() method of the TransportController class is working correctly.
     It creates a TransportOrder object, adds it to the controller, and then retrieves it using the get_transport_order_by_id() method.
     The test checks if the retrieved order matches the original order.
     Finally, the added order is removed from the database. */
    @Test
    void get_transport_order_by_id() {
        Product pr1 = new Product("18675", "Milk 3%", "Cooler", "Tara", "Dairy", "drink", 750);
        Product pr2 = new Product("95043", "Koteg 5%", "Cooler", "Tnoova", "Dairy", "eat", 1000);

        Address add1 = new Address(7, "South", "Mezada 36, Beer Sheva");
        Address add2 = new Address(657, "North", "King Salomon 68, Beer Sheva");

        Destination destination = new Destination(7, "0544692010", "Miri Agmon", "Destination", add1);
        Source source = new Source(657, "0509030798", "Gil Agmon", "Source", add2);

        HashMap<Product, Integer> products = new HashMap<>();
        products.put(pr1, 50);
        products.put(pr2, 100);

        TransportOrder order = new TransportOrder(10876, source, destination, products);
        try {
            transportController.add_new_transport_order(order);
        }
        catch (Exception e) {
            System.out.println("Failed while adding new transport order in get transport order test: " + e.getMessage());
        }

        assertEquals(order,transportController.get_transport_order_by_id(order.getTransport_order_id()),"Failed in get transport order by id test.");

        try {
            TransportOrderDAO transportOrderDAO = new TransportOrderDAO();
            transportOrderDAO.deleteTransportOrder(order.getTransport_order_id());
        }
        catch (Exception e){
            System.out.println("Failed while creating new TransportOrderDAO in get transport order test: " + e.getMessage());
        }
    }

    /** Tests the check_if_exist_any_transport_order() function in the TransportController class.
     It creates a new transport order and adds it to the system, then calls the function to check if any transport orders exist.
     It expects the result to be true since a transport order was added.
     Finally, it removes the transport order from the system.
     */
    @Test
    void check_if_exist_any_transport_order() {

        Product pr1 = new Product("18675", "Milk 3%", "Cooler", "Tara", "Dairy", "drink", 750);
        Product pr2 = new Product("95043", "Koteg 5%", "Cooler", "Tnoova", "Dairy", "eat", 1000);

        Address add1 = new Address(7, "South", "Mezada 36, Beer Sheva");
        Address add2 = new Address(657, "North", "King Salomon 68, Beer Sheva");

        Destination destination = new Destination(7, "0544692010", "Miri Agmon", "Destination", add1);
        Source source = new Source(657, "0509030798", "Gil Agmon", "Source", add2);

        HashMap<Product, Integer> products = new HashMap<>();
        products.put(pr1, 50);
        products.put(pr2, 100);

        TransportOrder order = new TransportOrder(464599, source, destination, products);
        try {
            transportController.add_new_transport_order(order);
        }
        catch (Exception e) {
            System.out.println("Failed while adding new transport order in get transport order test: " + e.getMessage());
        }

        boolean result = transportController.check_if_exist_unsigned_transport_orders();
        assertTrue(result, "Failed in check_if_exist_any_transport_order Test, got false and not true ");

        try {
            TransportOrderDAO transportOrderDAO = new TransportOrderDAO();
            transportOrderDAO.deleteTransportOrder(order.getTransport_order_id());
        }
        catch (SQLException e){
            System.out.println("Failed while REMOVING new TransportOrderDAO at the end of the check_if_exist_any_transport_order Test: " + e.getMessage());
        }
    }

}