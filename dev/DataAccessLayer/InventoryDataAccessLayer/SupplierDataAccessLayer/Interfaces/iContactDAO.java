package DataAccessLayer.InventoryDataAccessLayer.SupplierDataAccessLayer.Interfaces;

import BusinessLayer.InventoryBusinessLayer.SupplierBusinessLayer.Contact;
import Utillity.Response;

import java.util.ArrayList;

public interface iContactDAO {
    ArrayList<Contact> getContactsBySupplierID(int id);
    Contact getContactBySupplierID(int id, String phoneNumber);
    Response addContact(int id, Contact contact);
    Response removeContact(int id, String phoneNumber);
    Response updatePhoneNumber(int id, String oldPhoneNumber,  String newPhoneNumber);
    Response updateName(int id,  String phoneNumber, String name);
    Response updateEmail(int id,  String phoneNumber, String email);
}
