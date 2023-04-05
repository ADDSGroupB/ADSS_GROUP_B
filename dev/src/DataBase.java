import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DataBase {

    private ArrayList<TransportOrder> all_transport_orders_in_the_system;
    private ArrayList<TransportDocument> all_transport_documents_in_the_system;
    private ArrayList<TransportDocument> all_former_transport_documents;
    private ArrayList<TransportShipment> active_transports_shipments;
    private ArrayList<Driver> allDrivers;
    private ArrayList<ASite> allSites;
    private ArrayList<ATruck> allTrucks;
    private ArrayList<Product> allProducts;

    /**
     * Initializes a new instance of the {@link DataBase} class.
     * Initializes all the fields with empty {@link ArrayList}s.
     */
    public DataBase (){
        this.all_transport_orders_in_the_system = new ArrayList<TransportOrder>();
        this.all_transport_documents_in_the_system = new ArrayList<TransportDocument>();
        this.all_former_transport_documents = new ArrayList<TransportDocument>();
        this.active_transports_shipments = new ArrayList<TransportShipment>();
        this.allDrivers = new ArrayList<Driver>();
        this.allSites = new ArrayList<ASite>();
        this.allTrucks = new ArrayList<ATruck>();
        this.allProducts = new ArrayList<Product>();
    }

    /**
     * Builds the database by initializing the drivers, addresses and sites, trucks, and products.
     */
    public void buildDataBase () {
        DataBase data_base = new DataBase();
        buildDrivers();
        buildAddressesAndSites();
        buildTrucks();
        buildProducts();

    }

    /**
     * Returns all transport orders in the system.
     * @return An {@link ArrayList} of {@link TransportOrder} objects.
     */
    public ArrayList<TransportOrder> getAll_transport_orders_in_the_system() {
        return all_transport_orders_in_the_system;
    }

    /**
     * Returns all transport documents in the system.
     * @return An {@link ArrayList} of {@link TransportDocument} objects.
     */
    public ArrayList<TransportDocument> getAll_transport_documents_in_the_system() {
        return all_transport_documents_in_the_system;
    }

    /**
     * Returns all former transport documents.
     * @return An ArrayList of TransportDocument objects.
     */
    public ArrayList<TransportDocument> getAll_former_transport_documents() {
        return all_former_transport_documents;
    }

    /**
     * Returns all active transport shipments.
     * @return An ArrayList of TransportShipment objects.
     */
    public ArrayList<TransportShipment> getActive_transports_shipments() {
        return active_transports_shipments;
    }

    /**
     * Returns all drivers.
     * @return An ArrayList of Driver objects.
     */
    public ArrayList<Driver> getAllDrivers() {
        return allDrivers;
    }

    /**
     * Returns all sites.
     * @return An ArrayList of ASite objects.
     */
    public ArrayList<ASite> getAllSites() {
        return allSites;
    }

    /**
     * Returns all trucks.
     * @return An ArrayList of ATruck objects.
     */
    public ArrayList<ATruck> getAllTrucks() {
        return allTrucks;
    }

    /**
     * Returns all products.
     * @return An ArrayList of Product objects.
     */
    public ArrayList<Product> getAllProducts() {
        return allProducts;
    }


        /* create an array list of drivers */
    private void buildDrivers() {
        Driver d1 = new Driver("Shimi", "1541568", 1500, 1);
        Driver d2 = new Driver("Roni", "9485781", 2250, 2);
        Driver d3 = new Driver("Avi", "1563157", 3000, 3);
        Driver d4 = new Driver("Alon", "4391489", 1500, 3);
        Driver d5 = new Driver("Yael", "6953158", 2250, 2);
        Driver d6 = new Driver("Shlomit", "5671548", 3000, 1);
        allDrivers.add(d1);
        allDrivers.add(d2);
        allDrivers.add(d3);
        allDrivers.add(d4);
        allDrivers.add(d5);
        allDrivers.add(d6);
    }

    /* create an array list of sites */
    private void buildAddressesAndSites() {

        Address address1 = new Address("South", "Grand Kanion, Beer Sheva");
        Address address2 = new Address("West", "Azrieli mall, Beer Sheva");
        Address address3 = new Address("North", "Big, Beer Sheva");
        Address address4 = new Address("West", "Negev Center, Beer Sheva");
        Address address5 = new Address("North", "Ramot, Beer Sheva");
        Address address6 = new Address("West", "Ringelblom, Beer Sheva");

        ASite src1 = new Source(address1, "0593849506", "Tamar", 145);
        ASite src2 = new Source(address2, "0593849506", "Yuri", 876);
        ASite src3 = new Source(address3, "0596849384", "Arnon", 986);
        ASite dest1 = new Destination(address4, "0549586079", "Ehud", 935);
        ASite dest2 = new Destination(address5, "0495960495", "Gil", 157);
        ASite dest3 = new Destination(address6, "0779685968", "Shir", 756);
        allSites.add(src1);
        allSites.add(src2);
        allSites.add(src3);
        allSites.add(dest1);
        allSites.add(dest2);
        allSites.add(dest3);
    }

    /* create an array list of trucks */
    private void buildTrucks() {
        ATruck truck1 = new DryTruck(3614131, 1500, 1000);
        ATruck truck2 = new DryTruck(4758691, 3000, 2000);
        ATruck truck3 = new CoolerTruck(1849506, 2250, 1500);
        ATruck truck4 = new CoolerTruck(4475869, 1500, 1000);
        ATruck truck5 = new FreezerTruck(1958694, 3000, 2000);
        ATruck truck6 = new FreezerTruck(9684585, 2250, 1500);
        allTrucks.add(truck1);
        allTrucks.add(truck2);
        allTrucks.add(truck3);
        allTrucks.add(truck4);
        allTrucks.add(truck5);
        allTrucks.add(truck6);
    }

    /* create an array list of products */
    private void buildProducts() {
        Product pr1 = new Product("Milk 3%", "Cooler", "Tara", "18675", "Dairy", "drink", 750);
        Product pr2 = new Product("Koteg 5%", "Cooler", "Tnoova", "95043", "Dairy", "eat", 1000);
        Product pr3 = new Product("Chicken", "Freezer", "Osem", "39586", "Freezer", "eat", 500);
        Product pr4 = new Product("Shnitzel", "Freezer", "Tara", "10596", "Freezer", "eat", 750);
        Product pr5 = new Product("Corn", "Dry", "Tnoova", "76767", "Dry", "eat", 1000);
        Product pr6 = new Product("Tomato", "Dry", "Osem", "20597", "Dry", "eat", 500);
        Product pr7 = new Product("Tuna", "Dry", "Tara", "19191", "Dry", "eat", 750);
        Product pr8 = new Product("Cheese", "Cooler", "Tnoova", "95847", "Dairy", "eat", 500);
        Product pr9 = new Product("Beans", "Freezer", "OSem", "85964", "Freezer", "eat", 750);
        allProducts.add(pr1);
        allProducts.add(pr2);
        allProducts.add(pr3);
        allProducts.add(pr4);
        allProducts.add(pr5);
        allProducts.add(pr6);
        allProducts.add(pr7);
        allProducts.add(pr8);
        allProducts.add(pr9);
    }

}
