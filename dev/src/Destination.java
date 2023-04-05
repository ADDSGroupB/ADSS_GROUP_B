/**
 * A destination site with an address, phone number, contact name, and ID.
 */
public class Destination extends ASite {

    /**
     * Constructs a new Destination object with the specified address, phone number, contact name, and ID.
     * @param address the address of the destination site
     * @param phone_number the phone number of the destination site
     * @param contact_name the name of the contact person for the destination site
     * @param ID the ID of the destination site
     */
    public Destination(Address address, String phone_number, String contact_name, int ID) {
        super(address, phone_number, contact_name, ID);
    }
}
