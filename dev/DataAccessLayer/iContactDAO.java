package DataAccessLayer;

import BusinessLayer.Contact;
import BusinessLayer.Supplier;
import Utillity.Response;

import java.util.ArrayList;

public interface iContactDAO {
    public ArrayList<Contact> getContactsBySupplierID(int id);
    public Contact getContactBySupplierID(int id, String phoneNumber);
    public Response addContact(int id, Contact contact);
    public Response removeContact(int id, String phoneNumber);
    public Response updatePhoneNumber(int id, String oldPhoneNumber,  String newPhoneNumber);
    public Response updateName(int id,  String phoneNumber, String name);
    public Response updateEmail(int id,  String phoneNumber, String email);
}
