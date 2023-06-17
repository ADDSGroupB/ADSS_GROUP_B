package DataAccessLayer.TransportManager;
import java.sql.*;
import java.util.HashMap;
import java.util.Objects;

import BuisnessLayer.TransportManager.ASite;
import BuisnessLayer.TransportManager.Address;
import BuisnessLayer.TransportManager.Source;
import BuisnessLayer.TransportManager.Destination;
import DataAccessLayer.Database;

public class ASiteDAO {

    private Connection conn;
    private HashMap<Integer, ASite> allSites;

    public ASiteDAO() throws SQLException {
        allSites = new HashMap<>();
    }

    public HashMap<Integer, ASite> getAllSites() throws SQLException {
        try {
            conn = Database.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM allSites");

            while (rs.next()) {

                int site_id = rs.getInt("site_id");

                if (allSites.containsKey(site_id))
                    continue;

                String site_type = rs.getString("site_type");
                String phone_number = rs.getString("phone_number");
                String contact_name = rs.getString("contact_name");

                String shipping_area = rs.getString("shipping_area");
                String exact_address = rs.getString("exact_address");

                Address address = new Address(site_id, shipping_area, exact_address);

                if (Objects.equals(site_type, "Source")) {
                    Source site = new Source(site_id, phone_number, contact_name, site_type, address);
                    this.allSites.put(site_id, site);
                } else {
                    Destination site = new Destination(site_id, phone_number, contact_name, site_type, address);
                    this.allSites.put(site_id, site);
                }

            }
            Database.closeConnection(conn);
            return this.allSites;
        }
        catch (SQLException e){
            System.out.println("Exception has occurred while getting all sites: " + e.getMessage());
            System.out.println(e.getMessage());
            Database.closeConnection(conn);
        }
        return null;
    }


    public ASite getASiteById(int id) throws SQLException {

        if (allSites.containsKey(id)) {
            return allSites.get(id);
        }

        try {
            conn = Database.connect();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM allSites WHERE site_id = ?");
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int site_id = rs.getInt("site_id");

                String site_type = rs.getString("site_type");
                String phone_number = rs.getString("phone_number");
                String contact_name = rs.getString("contact_name");
                String shipping_area = rs.getString("shipping_area");
                String exact_address = rs.getString("exact_address");

                Address address = new Address(site_id, shipping_area, exact_address);
                if (Objects.equals(site_type, "Source")) {
                    Source site = new Source(site_id, phone_number, contact_name, site_type, address);
                    this.allSites.put(site_id, site);
                    Database.closeConnection(conn);
                    return site;
                } else {
                    Destination site = new Destination(site_id, phone_number, contact_name, site_type, address);
                    this.allSites.put(site_id, site);
                    Database.closeConnection(conn);
                    return site;
                }
            } else {
                Database.closeConnection(conn);
                return null;
            }
        }
        catch (SQLException e){
            System.out.println("Exception has occurred while getting a site by ID: " + e.getMessage());
            System.out.println(e.getMessage());
            Database.closeConnection(conn);
        }

        return null;

    }

    public void insertASite(ASite site) throws SQLException {

        try {
            conn = Database.connect();
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO allSites (site_id, site_type, phone_number, contact_name, shipping_area, exact_address) VALUES (?,?, ?, ?, ?, ?)");
            stmt.setInt(1, site.getID());
            stmt.setString(2, site.getSite_type());
            stmt.setString(3, site.getPhone_number());
            stmt.setString(4, site.getContact_name());
            stmt.setString(5, site.getAddress().getShipping_area());
            stmt.setString(6, site.getAddress().getExact_address());
            stmt.executeUpdate();
            allSites.put(site.getID(), site);
            Database.closeConnection(conn);
        }
        catch (SQLException e){
            System.out.println("Exception has occurred while inserting a site: " + e.getMessage());
            System.out.println(e.getMessage());
            Database.closeConnection(conn);
        }

    }

    public void deleteASite(int id) throws SQLException {
        try {
            conn = Database.connect();
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM allSites WHERE site_id = ?");
            stmt.setInt(1,id);
            stmt.executeUpdate();
            allSites.remove(id);
            Database.closeConnection(conn);
        }
        catch (SQLException e){
            System.out.println("Exception has occurred while deleting a site: " + e.getMessage());
            System.out.println(e.getMessage());
            Database.closeConnection(conn);
        }
    }

    public void updateASite (ASite site) throws SQLException {
        try {
            conn = Database.connect();
            PreparedStatement stmt = conn.prepareStatement("UPDATE allSites SET site_type = ?, phone_number = ?, contact_name = ?, shipping_area = ?, exact_address = ? WHERE site_id = ?");
            stmt.setString(1, site.getSite_type());
            stmt.setString(2, site.getPhone_number());
            stmt.setString(3, site.getContact_name());
            stmt.setString(4, site.getAddress().getShipping_area());
            stmt.setString(5, site.getAddress().getExact_address());
            stmt.setInt(6, site.getID());
            stmt.executeUpdate();
            allSites.put(site.getID(), site);
            Database.closeConnection(conn);
        }
        catch (SQLException e){
            System.out.println("Exception has occurred while updating a site: " + e.getMessage());
            System.out.println(e.getMessage());
            Database.closeConnection(conn);
        }

    }

    public void deleteContent_sites () throws SQLException{
        Statement stmt = null;
        try {
            conn = Database.connect();
            stmt = conn.createStatement();
            stmt.executeUpdate("DELETE FROM allSites");
            Database.closeConnection(conn);
            allSites.clear();
        }
        catch (SQLException e){
            System.out.println("Exception has occurred while deleting table allSites: " + e.getMessage());
            System.out.println(e.getMessage());
            Database.closeConnection(conn);
        }
    }

    public void deleteContent_Addresses () throws SQLException{
        Statement stmt = null;
        try {
            conn = Database.connect();
            stmt = conn.createStatement();
            stmt.executeUpdate("DELETE FROM allAddresses");
            Database.closeConnection(conn);
        }
        catch (SQLException e){
            System.out.println("Exception has occurred while deleting table allAddresses: " + e.getMessage());
            System.out.println(e.getMessage());
            Database.closeConnection(conn);
        }
    }




}
