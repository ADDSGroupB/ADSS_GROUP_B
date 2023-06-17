package BuisnessLayer.TransportManager; /**

 Represents a transport order that includes the source and destination address, as well as a list of products and their quantities.
 Each transport order is identified by a unique transport_order_ID.
 */
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TransportOrder {

    private final Source source; // the source address of the transport order
    private final Destination destination; // the destination address of the transport order
    private HashMap<Product, Integer> products_list; // the list of products and their quantities in the transport order

    private int transport_order_id; // the unique ID of the transport order

    private int assigned_doc_id;

    public TransportOrder(Source source, Destination destination, HashMap<Product, Integer> products_list, int transport_order_ID, int assigned_doc_id) {
        this.transport_order_id = transport_order_ID; // assigns transport order ID
        this.source = source;
        this.destination = destination;
        this.products_list = products_list;
        this.assigned_doc_id = assigned_doc_id;
    }
    /**
     * Constructs a new transport order with the given source, destination, and products list.
     * Assigns a unique transport_order_ID to the transport order.
     *
     * @param source        The source address of the transport order.
     * @param destination   The destination address of the transport order.
     * @param products_list The list of products and their quantities in the transport order.
     */
    public TransportOrder(int transport_order_id, Source source, Destination destination, HashMap<Product, Integer> products_list) {
        this.transport_order_id = transport_order_id; // assigns a unique transport order ID
        this.source = source;
        this.destination = destination;
        this.products_list = products_list;
        this.assigned_doc_id = - 1;
    }

    public void setTransport_order_id(int transport_order_id) {
        this.transport_order_id = transport_order_id;
    }

    public void setAssigned_doc_id(int assigned_doc_id) {
        this.assigned_doc_id = assigned_doc_id;
    }

    public int getAssigned_doc_id() {
        return this.assigned_doc_id;
    }


    /**
     * Returns the unique transport order ID of the transport order.
     *
     * @return The unique transport order ID of the transport order.
     */
    public int getTransport_order_id() {
        return transport_order_id;
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
    public HashMap<Product, Integer> getProducts_list() {
        return products_list;
    }


    public void print_items_list () {
        for (Map.Entry<Product,Integer> entry : products_list.entrySet())
            System.out.println("Product: " + entry.getKey() + " with amount: " + entry.getValue());
    }

    /**
     * Returns a string representation of the transport order, including its list of products and their quantities.
     *
     * @return A string representation of the transport order, including its list of products and their quantities.
     */
    @Override
    public String toString() {
        return "Transport Order ID: " + transport_order_id +
                ", Assigned to Transport Document ID " + assigned_doc_id +
                ", Leaving Source " + source.getID() +
                ", To Destination " + destination.getID() +
                ", Transporting the following cargo: " + productListToString();
    }

    public String productListToString() {

        String ans="";
        for (Map.Entry<Product,Integer> entry : products_list.entrySet()){
            ans += "Product: " + entry.getKey().getName() + " with amount: " + entry.getValue() + " ";
        }

        return ans;
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

    public Product FindProduct(String code_item_to_remove){

        for (Product product_to_remove : this.products_list.keySet()) {

            if (Objects.equals(product_to_remove.getProduct_code(), code_item_to_remove)) {

                return product_to_remove;
            }
        }
        return null;
    }

    public int get_amount_of_specific_product(Product product) {

        return products_list.get(product);
    }

    public void update_amount_of_product_in_order(Product product, int new_amount){

        if (this.products_list.size() != 0 ) {
            for (Product curr_product : products_list.keySet()) {

                if (Objects.equals(curr_product.getProduct_code(), product.getProduct_code())) {
                    if (new_amount == 0)
                        products_list.remove(product);
                    else
                        products_list.remove(product);
                    products_list.put(product, new_amount);
                }
            }
        }
        else {
            products_list.put(product, new_amount);
        }

    }

    public void add_items_and_amount_to_transport_order(Product new_product, int amount) {
        products_list.put(new_product,amount);
    }






}
