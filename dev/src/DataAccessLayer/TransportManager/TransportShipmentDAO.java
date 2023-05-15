package DataAccessLayer.TransportManager;
import BuisnessLayer.TransportManager.*;
import DataAccessLayer.Database;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.HashSet;

public class TransportShipmentDAO {
    private Connection conn;
    private HashMap<Integer, TransportShipment> allTransportShipments;

    public TransportShipmentDAO() throws SQLException {
        allTransportShipments = new HashMap<>();
    }

    public HashMap<Integer, TransportShipment> getAllTransportShipments() throws SQLException {
        try {
            conn = Database.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM allTransportShipments");

            while (rs.next()) {

                int transport_id = rs.getInt("transport_shipment_id");

                if (this.allTransportShipments.containsKey(transport_id)) {
                    continue;
                }

                String driver_name = rs.getString("driver_name");
                String date = rs.getString("date");
                String departure_time = rs.getString("departure_time");
                int truck_license_plate_number = rs.getInt("truck_license_plate_number");
                int cargo_weight = rs.getInt("cargo_weight");
                int transport_document_id = rs.getInt("transport_document_id");
                int source_id = rs.getInt("source_id");

                ASiteDAO ASite_DAO = new ASiteDAO();
                Source source = (Source) ASite_DAO.getASiteById(source_id);

                TransportDocumentDAO transportDocumentDAO = new TransportDocumentDAO();
                TransportDocument transport_document = transportDocumentDAO.getTransportDocumentById(transport_document_id);

                HashSet<ASite> destinations_list = new HashSet<ASite>();
                PreparedStatement stmt2 = conn.prepareStatement("SELECT * FROM SitesInShipment WHERE shipment_id = ?");
                stmt2.setInt(1, transport_id);
                ResultSet rs2 = stmt2.executeQuery();

                while (rs2.next()) {

                    int site_id = rs2.getInt("site_id");
                    ASite site = ASite_DAO.getASiteById(site_id);
                    destinations_list.add(site);
                }
                TransportShipment transportShipment = new TransportShipment(driver_name, LocalDate.parse(date),
                        LocalTime.parse(departure_time), truck_license_plate_number, cargo_weight, destinations_list,
                        source, transport_document, transport_id);
                this.allTransportShipments.put(transport_id, transportShipment);
            }

            Database.closeConnection(conn);
            return this.allTransportShipments;
        }
        catch (SQLException e){
            System.out.println("Exception has occurred while getting all transport shipments: " + e.getMessage());
            System.out.println(e.getMessage());
            Database.closeConnection(conn);
        }
        return null;
    }

    public TransportShipment getTransportShipmentById(int id) throws SQLException {


        if (this.allTransportShipments.containsKey(id)) {
            Database.closeConnection(conn);
            return this.allTransportShipments.get(id);
        }

        try {
            conn = Database.connect();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM allTransportShipments WHERE transport_shipment_id = ?");
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int transport_id = rs.getInt("transport_shipment_id");

                String driver_name = rs.getString("driver_name");
                String date = rs.getString("date");
                String departure_time = rs.getString("departure_time");
                int truck_license_plate_number = rs.getInt("truck_license_plate_number");
                int cargo_weight = rs.getInt("cargo_weight");
                int transport_document_id = rs.getInt("transport_document_id");
                int source_id = rs.getInt("source_id");

                ASiteDAO ASite_DAO = new ASiteDAO();
                Source source = (Source) ASite_DAO.getASiteById(source_id);

                TransportDocumentDAO transportDocumentDAO = new TransportDocumentDAO();
                TransportDocument transport_document = transportDocumentDAO.getTransportDocumentById(transport_document_id);

                HashSet<ASite> destinations_list = new HashSet<ASite>();

                PreparedStatement stmt2 = conn.prepareStatement("SELECT * FROM SitesInShipment WHERE shipment_id = ?");
                stmt2.setInt(1, transport_id);
                ResultSet rs2 = stmt2.executeQuery();

                while (rs2.next()) {

                    int site_id = rs2.getInt("site_id");
                    ASite site = ASite_DAO.getASiteById(site_id);
                    destinations_list.add(site);
                }
                TransportShipment transportShipment = new TransportShipment(driver_name, LocalDate.parse(date), LocalTime.parse(departure_time), truck_license_plate_number, cargo_weight, destinations_list, source, transport_document, transport_id);
                this.allTransportShipments.put(transport_id, transportShipment);
                Database.closeConnection(conn);
                return transportShipment;
            } else {
                Database.closeConnection(conn);
                return null;
            }
        }
        catch (SQLException e){
            System.out.println("Exception has occurred while getting transport shipment by id: " + e.getMessage());
            System.out.println(e.getMessage());
            Database.closeConnection(conn);
        }
        return null;
    }

    public void insertTransportShipment (TransportShipment shipment) throws SQLException {

        try {
            conn = Database.connect();
            int transport_shipment_id;

            PreparedStatement stmt = conn.prepareStatement("INSERT INTO allTransportShipments (driver_name, date," +
                    " departure_time, truck_license_plate_number, cargo_weight, source_id, transport_document_id) VALUES (?, ?, ?, ?, ?, ?, ?)",  Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, shipment.getDriver_name());
            stmt.setString(2, shipment.getDate().toString());
            stmt.setString(3, shipment.getDeparture_time().toString());
            stmt.setInt(4, shipment.getTruck_license_plate_number());
            stmt.setDouble(5, shipment.getCargo_weight());
            stmt.setInt(6, shipment.getSource().getID());
            stmt.setInt(7, shipment.getTransport_document().getTransport_document_ID());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    transport_shipment_id = rs.getInt(1);
                    shipment.setTransport_id(transport_shipment_id);

                }
            }

            boolean stop = false;
            /** adding to sitesInShipment all sources by order, starting from source number 2 (because its the first destination) */
            for (TransportOrder order : shipment.getTransport_document().getAll_transport_orders()) {

                if (stop) {
                    PreparedStatement stmt2 = conn.prepareStatement("INSERT INTO SitesInShipment (shipment_id, site_id) VALUES (?, ?)");
                    stmt2.setInt(1, shipment.getTransport_id());
                    stmt2.setInt(2, order.getSource().getID());
                    stmt2.executeUpdate();
                } else stop = true;
            }
            /** adding to sitesInShipment all destinations by order */
            for (TransportOrder order : shipment.getTransport_document().getAll_transport_orders()) {

                PreparedStatement stmt4 = conn.prepareStatement("SELECT * FROM SitesInShipment WHERE shipment_id = ? AND site_id = ?");
                stmt4.setInt(1, shipment.getTransport_id());
                stmt4.setInt(2, order.getDestination().getID());
                ResultSet rs2 = stmt4.executeQuery();

                if (!rs2.next())
                {
                    PreparedStatement stmt3 = conn.prepareStatement("INSERT INTO SitesInShipment (shipment_id, site_id) VALUES (?, ?)");
                    stmt3.setInt(1, shipment.getTransport_id());
                    stmt3.setInt(2, order.getDestination().getID());
                    stmt3.executeUpdate();
                }
            }
            this.allTransportShipments.put(shipment.getTransport_id(), shipment);
            Database.closeConnection(conn);
        }
        catch (SQLException e){
            System.out.println("Exception has occurred while inserting transport shipment: " + e.getMessage());
            Database.closeConnection(conn);
        }
    }

    public void updateTransportShipment(TransportShipment shipment) throws SQLException {

        try {
            conn = Database.connect();

            PreparedStatement stmt = conn.prepareStatement("UPDATE allTransportShipments SET driver_name = ?, date = ?," +
                    " departure_time = ?, truck_license_plate_number = ?, cargo_weight = ?, transport_document_id = ?, source_id = ? WHERE transport_shipment_id = ?");
            stmt.setString(1, shipment.getDriver_name());
            stmt.setString(2, shipment.getDate().toString());
            stmt.setString(3, shipment.getDeparture_time().toString());
            stmt.setInt(4, shipment.getTruck_license_plate_number());
            stmt.setDouble(5, shipment.getCargo_weight());
            stmt.setInt(6, shipment.getTransport_document().getTransport_document_ID());
            stmt.setInt(7, shipment.getSource().getID());
            stmt.setInt(8, shipment.getTransport_id());
            stmt.executeUpdate();

            PreparedStatement stmt4 = conn.prepareStatement("DELETE FROM SitesInShipment WHERE shipment_id = ?");
            stmt4.setInt(1, shipment.getTransport_id());
            stmt4.executeUpdate();

            boolean stop = false;
            /** adding to sitesInShipment all sources by order, starting from source number 2 (because it's the first destination) */
            for (TransportOrder order : shipment.getTransport_document().getAll_transport_orders()) {

                if (stop) {
                    PreparedStatement stmt2 = conn.prepareStatement("INSERT INTO SitesInShipment (shipment_id, site_id) VALUES (?, ?)");
                    stmt2.setInt(1, shipment.getTransport_id());
                    stmt2.setInt(2, order.getSource().getID());
                    stmt2.executeUpdate();
                } else stop = true;
            }
            /** adding to sitesInShipment all destinations by order */
            for (TransportOrder order : shipment.getTransport_document().getAll_transport_orders()) {

                PreparedStatement stmt3 = conn.prepareStatement("INSERT INTO SitesInShipment (shipment_id, site_id) VALUES (?, ?)");
                stmt3.setInt(1, shipment.getTransport_id());
                stmt3.setInt(2, order.getDestination().getID());
                stmt3.executeUpdate();
            }

            this.allTransportShipments.put(shipment.getTransport_id(), shipment);
        }
        catch (SQLException e){
            System.out.println("Exception has occurred while updating transport shipment: " + e.getMessage());
            System.out.println(e.getMessage());
            Database.closeConnection(conn);
        }
        Database.closeConnection(conn);
    }
    public void deleteTransportShipment(int id) throws SQLException {

        try {
            conn = Database.connect();
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM allTransportShipments WHERE transport_shipment_id = ?");
            stmt.setInt(1, id);
            stmt.executeUpdate();

            PreparedStatement stmt2 = conn.prepareStatement("DELETE FROM SitesInShipment WHERE shipment_id = ?");
            stmt2.setInt(1, id);
            stmt2.executeUpdate();

            this.allTransportShipments.remove(id);

        } catch (SQLException e){
            System.out.println("Exception has occurred while deleting transport shipment: " + e.getMessage());
            System.out.println(e.getMessage());
            Database.closeConnection(conn);
        }
        Database.closeConnection(conn);
    }


    public void deleteContent_shipments () throws SQLException{
        Statement stmt = null;
        try {
            conn = Database.connect();
            stmt = conn.createStatement();
            stmt.executeUpdate("DELETE FROM allTransportShipments");

            Statement stmt2 = conn.createStatement();
            String sql = "DELETE FROM SQLITE_SEQUENCE WHERE NAME='allTransportShipments';";
            stmt2.executeUpdate(sql);


            Database.closeConnection(conn);
            allTransportShipments.clear();
        }
        catch (SQLException e){
            System.out.println("Exception has occurred while deleting table allTransportShipments: " + e.getMessage());
            System.out.println(e.getMessage());
            Database.closeConnection(conn);
        }
    }

    public void deleteContent_SitesInShipment () throws SQLException{
        Statement stmt = null;
        try {
            conn = Database.connect();
            stmt = conn.createStatement();
            stmt.executeUpdate("DELETE FROM SitesInShipment");
            Database.closeConnection(conn);
        }
        catch (SQLException e){
            System.out.println("Exception has occurred while deleting table SitesInShipment: " + e.getMessage());
            System.out.println(e.getMessage());
            Database.closeConnection(conn);
        }
    }
}
