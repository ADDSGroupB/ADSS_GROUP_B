/**
 * A destination site with an address, phone number, contact name, and ID.
 */
public class Source extends ASite {

    /**
     * Initializes a destination site object with the given information.
     *
     * @param address (Address): the physical address of the source site
     * @param phone_number (str): the phone number of the contact person at the source site
     * @param contact_name (str): the name of the contact person at the source site
     * @param ID (int): the unique identifier of the source site
     */
    public Source(Address address, String phone_number, String contact_name, int ID) {
        super(address, phone_number, contact_name, ID);
    }

}
