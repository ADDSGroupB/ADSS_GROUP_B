package DataAccessLayer.InventoryDataAccessLayer.SupplierDataAccessLayer;

import BusinessLayer.InventoryBusinessLayer.SupplierBusinessLayer.Contact;
import DataAccessLayer.InventoryDataAccessLayer.SupplierDataAccessLayer.Interfaces.iContactDAO;
import Utillity.Pair;
import Utillity.Response;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class ContactDAO implements iContactDAO {
    private final Connection connection;
    private final HashMap<Pair<Integer, String>, Contact> contactIM;

    public ContactDAO()
    {
        connection = Database.connect();
        try {
            Statement statement = connection.createStatement();
            statement.execute("PRAGMA foreign_keys=ON;");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        contactIM = new HashMap<>();
//        contactIM.put(new Pair<>(1, "052-3801919"), new Contact("test", "test@test.com", "052-3801919"));
//        getContactBySupplierID(1, "052-3801919");
    }

//    public static void main(String[] args) {
//
//        ContactDAO contactDAO = new ContactDAO();
//        contactDAO.getContactBySupplierID(1, "052-3801919");
//    }
    @Override
    public ArrayList<Contact> getContactsBySupplierID(int id) {
        try (PreparedStatement contactStatement = connection.prepareStatement("SELECT * FROM contact WHERE supplierID = ?")) {
            contactStatement.setInt(1, id);
            ResultSet discountResult = contactStatement.executeQuery();
            ArrayList<Contact> contacts = new ArrayList<>();
            while (discountResult.next())
            {
                String name = discountResult.getString("name");
                String email = discountResult.getString("email");
                String phoneNumber = discountResult.getString("phoneNumber");
                Pair<Integer, String> pair = new Pair<>(id, phoneNumber);
                if(contactIM.containsKey(pair)) contacts.add(contactIM.get(pair));
                else
                {
                    Contact contact = new Contact(name, email, phoneNumber);
                    contactIM.put(pair, contact);
                    contacts.add(contact);
                }
            }
            return contacts;
        } catch (SQLException e) { System.out.println(e.getMessage()); }
        return null;
    }

    @Override
    public Contact getContactBySupplierID(int id, String phoneNumber) {
        Pair<Integer, String> pair = new Pair<>(id, phoneNumber);
        if(contactIM.containsKey(pair)) return contactIM.get(pair);
        try (PreparedStatement contactStatement = connection.prepareStatement("SELECT * FROM contact WHERE supplierID = ? AND phoneNumber = ?")) {
            contactStatement.setInt(1, id);
            contactStatement.setString(2, phoneNumber);
            ResultSet discountResult = contactStatement.executeQuery();
            if (discountResult.next())
            {
                String name = discountResult.getString("name");
                String email = discountResult.getString("email");
                Contact contact = new Contact(name, email, phoneNumber);
                contactIM.put(pair, contact);
                return contact;
            }
        } catch (SQLException e) { System.out.println(e.getMessage()); }
        return null;
    }

    @Override
    public Response addContact(int id, Contact contact) {
        try (PreparedStatement contactStatement = connection.prepareStatement("INSERT INTO contact (supplierID, phoneNumber, name, email) VALUES (?, ?, ?, ?)"))
        {
            contactStatement.setInt(1, id);
            contactStatement.setString(2, contact.getPhoneNumber());
            contactStatement.setString(3, contact.getName());
            contactStatement.setString(4, contact.getEmail());
            contactStatement.executeUpdate();
            Pair<Integer, String> pair = new Pair<>(id, contact.getPhoneNumber());
            contactIM.put(pair, contact);
        } catch (SQLException e) { return new Response(e.getMessage()); }
        return new Response(id);
    }

    @Override
    public Response removeContact(int id, String phoneNumber) {
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM contact WHERE supplierID = ? AND phoneNumber = ?"))
        {
            statement.setInt(1, id);
            statement.setString(2, phoneNumber);
            statement.executeUpdate();
            contactIM.remove(new Pair<>(id, phoneNumber));
            return new Response(id);
        } catch (SQLException e) { return new Response(e.getMessage()); }
    }

    @Override
    public Response updatePhoneNumber(int id, String oldPhoneNumber,  String newPhoneNumber) {
        try (PreparedStatement statement = connection.prepareStatement("UPDATE contact SET phoneNumber = ? WHERE supplierID = ? AND phoneNumber = ?"))
        {
            statement.setString(1, newPhoneNumber);
            statement.setInt(2, id);
            statement.setString(3, oldPhoneNumber);
            statement.executeUpdate();
            Pair<Integer, String> oldPair = new Pair<>(id, oldPhoneNumber);
            if (contactIM.containsKey(oldPair)){
                Contact contact = contactIM.get(oldPair);
                contact.setPhoneNumber(newPhoneNumber);
                contactIM.put(new Pair<>(id, newPhoneNumber), contact);
                contactIM.remove(oldPair);
            }
            return new Response(id);
        } catch (SQLException e) { return new Response(e.getMessage()); }
    }

    @Override
    public Response updateName(int id,  String phoneNumber, String name) {
        try (PreparedStatement statement = connection.prepareStatement("UPDATE contact SET name = ? WHERE supplierID = ? AND phoneNumber = ?"))
        {
            statement.setString(1, name);
            statement.setInt(2, id);
            statement.setString(3, phoneNumber);
            statement.executeUpdate();
            Pair<Integer, String> oldPair = new Pair<>(id, phoneNumber);
            if (contactIM.containsKey(oldPair)) contactIM.get(oldPair).setName(name);
            return new Response(id);
        } catch (SQLException e) { return new Response(e.getMessage()); }
    }

    @Override
    public Response updateEmail(int id,  String phoneNumber, String email) {
        try (PreparedStatement statement = connection.prepareStatement("UPDATE contact SET email = ? WHERE supplierID = ? AND phoneNumber = ?"))
        {
            statement.setString(1, email);
            statement.setInt(2, id);
            statement.setString(3, phoneNumber);
            statement.executeUpdate();
            Pair<Integer, String> oldPair = new Pair<>(id, phoneNumber);
            if (contactIM.containsKey(oldPair)) contactIM.get(oldPair).setEmail(email);
            return new Response(id);
        } catch (SQLException e) { return new Response(e.getMessage()); }
    }
}
