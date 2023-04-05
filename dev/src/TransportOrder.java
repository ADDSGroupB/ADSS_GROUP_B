/**

 Represents a transport order that includes the source and destination address, as well as a list of products and their quantities.
 Each transport order is identified by a unique transport_order_ID.
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TransportOrder {

    private final Source source; // the source address of the transport order
    private final Destination destination; // the destination address of the transport order
    private HashMap<Product, Integer> products_list; // the list of products and their quantities in the transport order

    private final int transport_order_ID; // the unique ID of the transport order

    private static int static_id = 1; // the static ID used to generate unique transport order IDs

    /**
     * Constructs a new transport order with the given source, destination, and products list.
     * Assigns a unique transport_order_ID to the transport order.
     *
     * @param source        The source address of the transport order.
     * @param destination   The destination address of the transport order.
     * @param products_list The list of products and their quantities in the transport order.
     */
    public TransportOrder(Source source, Destination destination, HashMap<Product, Integer> products_list) {
        this.transport_order_ID = static_id++; // assigns a unique transport order ID
        this.source = source;
        this.destination = destination;
        this.products_list = products_list;
    }

    /**
     * Returns the unique transport order ID of the transport order.
     *
     * @return The unique transport order ID of the transport order.
     */
    public int getTransport_order_ID() {
        return transport_order_ID;
    }

    /**
     * Returns the source address of the transport order.
     *
     * @return The source address of the transport order.
     */
    public Source getSource() {
        return source;
    }

    /**
     * Returns the destination address of the transport order.
     *
     * @return The destination address of the transport order.
     */
    public Destination getDestination() {
        return destination;
    }

    /**
     * Returns the list of products and their quantities in the transport order.
     *
     * @return The list of products and their quantities in the transport order.
     */
    public HashMap<Product, Integer> getItems_list() {
        return products_list;
    }

    /**
     * Prints the details of the transport order, including its ID, source and destination addresses, and list of products and their quantities.
     *
     * @param order The transport order to be printed.
     */
    public void print_transport_order(TransportOrder order) {

        System.out.print("Transport order ID: " + order.getTransport_order_ID());
        System.out.print(", Source address: " + order.getSource().getAddress().getExact_address());
        System.out.print(", Destination address: " + order.getDestination().getAddress().getExact_address());
        System.out.println(", Shipping area: " + order.getDestination().getAddress().getShipping_area());

        for (Map.Entry<Product, Integer> entry : products_list.entrySet())
            System.out.println(entry.getKey() + ": " + entry.getValue());

    }

    /**
     * Returns a string representation of the transport order, including its list of products and their quantities.
     *
     * @return A string representation of the transport order, including its list of products and their quantities.
     */
    @Override
    public String toString() {
        return "TransportOrder {" +
                "products_list = " + products_list +
                ", transport_order_ID = " + transport_order_ID +
                '}';
    }

    public static HashMap<Product, Integer> get_cooler_products(HashMap<Product, Integer> given_products_list) {

        HashMap<Product, Integer> rtn_hash_map = new HashMap<>();
        for (Map.Entry<Product, Integer> set : given_products_list.entrySet()) {
            if (Objects.equals(set.getKey().getType(), "Cooler"))
                rtn_hash_map.put(set.getKey(), set.getValue());
        }

        return rtn_hash_map;
    }

    public static HashMap<Product, Integer> get_freezer_products(HashMap<Product, Integer> given_products_list) {

        HashMap<Product, Integer> rtn_hash_map = new HashMap<>();
        for (Map.Entry<Product, Integer> set : given_products_list.entrySet()) {
            if (Objects.equals(set.getKey().getType(), "Freezer"))
                rtn_hash_map.put(set.getKey(), set.getValue());
        }

        return rtn_hash_map;
    }

    public static Product get_product_according_to_code(ArrayList<Product> all_products, String code) {
        for (Product all_product : all_products) {
            if (Objects.equals(all_product.getProduct_code(), code))
                return all_product;
        }

        return null;
    }

    public Product FindProduct(Product find_product, String code_item_to_remove){

        for (Product product_to_remove : this.products_list.keySet()) {

            if (Objects.equals(product_to_remove.getProduct_code(), code_item_to_remove)) {

                find_product = product_to_remove;
            }
        }
        return find_product;
    }

    public void Update_amount_of_product(Product update_product, int new_amount){

        this.products_list.put(update_product, new_amount);

    }

    public void RemoveProduct_FromOrder(Product product_to_remove){

        this.products_list.remove(product_to_remove);


    }
}
