package BuisnessLayer.TransportManager;

/**
 * Represents a physical address consisting of a shipping area and an exact address.
 */
public class Address {

    private final String shipping_area;
    private final String exact_address;
    private int address_id;

    /**
     * Constructs an Address object with the specified shipping area and exact address.
     * @param shipping_area a string representing the larger geographical area where the address is located
     * @param exact_address a string representing the specific street address or other detailed location information
     */
    public Address(int address_id, String shipping_area, String exact_address) {
        this.shipping_area = shipping_area;
        this.exact_address = exact_address;
        this.address_id = address_id;
    }

    /**
     * Gets the exact address for this Address object.
     * @return a string representing the specific street address or other detailed location information
     */
    public String getExact_address() {
        return exact_address;
    }

    /**
     * Gets the shipping area for this Address object.
     * @return a string representing the larger geographical area where the address is located
     */
    public String getShipping_area() {
        return shipping_area;
    }

    @Override
    public String toString() {
        return "Address{" +
                "shipping_area='" + shipping_area + '\'' +
                ", exact_address='" + exact_address + '\'' +
                '}';
    }
}