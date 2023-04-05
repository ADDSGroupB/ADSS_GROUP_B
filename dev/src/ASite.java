/**
 * An abstract class representing a site with an address, phone number, contact name, and ID.
 * This class serves as a base class for specific types of sites, which should extend this class and implement
 * their own behavior.
 */
public abstract class ASite {

    private Address address;
    private String phone_number;
    private String contact_name;
    private final int site_id;

    /**
     * Constructs a new ASite object with the specified address, phone number, contact name, and ID.
     * @param address the address of the site
     * @param phone_number the phone number of the site
     * @param contact_name the name of the site's primary contact
     * @param ID the unique ID of the site
     */
    public ASite(Address address, String phone_number, String contact_name, int ID) {
        this.address = address;
        this.phone_number = phone_number;
        this.contact_name = contact_name;
        this.site_id = ID;
    }

    /**
     * Returns the address of this ASite object.
     * @return the address of the site
     */
    public Address getAddress() {
        return address;
    }

    /**
     * Returns the ID of this ASite object.
     * @return the ID of the site
     */
    public int getID() {
        return site_id;
    }

    /**
     * Returns the phone number of this ASite object.
     * @return the phone number of the site
     */
    public String getPhone_number() {
        return phone_number;
    }

    /**
     * Returns the name of the primary contact for this ASite object.
     * @return the name of the site's primary contact
     */
    public String getContact_name() {
        return contact_name;
    }

}
