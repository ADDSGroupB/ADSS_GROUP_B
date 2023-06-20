package DataAccessLayer.TransportManager;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

import BuisnessLayer.TransportManager.DocumentStatus;
import BuisnessLayer.TransportManager.TransportDocument;
import BuisnessLayer.TransportManager.TransportOrder;
import DataAccessLayer.Database;
import DataAccessLayer.HR.DriverDAO;

public class TransportDocumentDAO {
    private Connection conn;
    private HashMap<Integer, TransportDocument> allTransportDocuments;


    public TransportDocumentDAO() throws SQLException {
        this.allTransportDocuments = new HashMap<>();

    }

    public HashMap<Integer, TransportDocument> getAllTransportDocuments() throws SQLException {

        try {
            conn = Database.connect();

            Statement stmt = conn.createStatement();
            ResultSet rs1 = stmt.executeQuery("SELECT * FROM allTransportDocuments");

            while (rs1.next()) {

                int transport_document_ID = rs1.getInt("transport_document_id");

                if (this.allTransportDocuments.containsKey(transport_document_ID))
                    continue;

                String date = rs1.getString("date");
                String shipping_area = rs1.getString("shipping_area");
                String transport_type = rs1.getString("transport_type");
                String driver_id = rs1.getString("driver_id");
                String departure_time = rs1.getString("departure_time");
                int truck_license_plate_number = rs1.getInt("truck_license_plate_number");
                String doc_status = rs1.getString("doc_status");

                ArrayList<TransportOrder> all_transport_orders = new ArrayList<TransportOrder>();

                PreparedStatement stmt2 = conn.prepareStatement("SELECT * FROM allTransportOrders WHERE assigned_doc_id = ?");
                stmt2.setInt(1, transport_document_ID);
                ResultSet rs2 = stmt2.executeQuery();

                TransportOrderDAO orderDAO = new TransportOrderDAO();
                DriverDAO driverDAO = new DriverDAO();
                while (rs2.next()) {

                    all_transport_orders.add(orderDAO.getTransportOrderById(rs2.getInt("transport_order_id")));
                }

                TransportDocument transportDocument = new TransportDocument(LocalDate.parse(date), truck_license_plate_number,
                        LocalTime.parse(departure_time), driverDAO.getDriverByID(driver_id),
                        all_transport_orders, shipping_area, transport_type, transport_document_ID);

                if (Objects.equals(doc_status, "inProgress")) {
                    transportDocument.setDoc_status(DocumentStatus.inProgress);
                }
                if (Objects.equals(doc_status, "finished")) {
                    transportDocument.setDoc_status(DocumentStatus.finished);
                }
                this.allTransportDocuments.put(transport_document_ID, transportDocument);

            }
            Database.closeConnection(conn);
            return this.allTransportDocuments;
        }
        catch (SQLException e) {
            System.out.println("Exception has occurred while getting alL transport shipments: " + e.getMessage());
            System.out.println(e.getMessage());
            Database.closeConnection(conn);
        }
        return null;
    }

    public TransportDocument getTransportDocumentById(int id) throws SQLException {


        if (this.allTransportDocuments.containsKey(id)) {
            return allTransportDocuments.get(id);
        }

        try {
            conn = Database.connect();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM allTransportDocuments WHERE transport_document_id = ?");
            stmt.setInt(1, id);
            ResultSet rs1 = stmt.executeQuery();
            if (rs1.next()) {
                int transport_document_ID = rs1.getInt("transport_document_ID");


                String date = rs1.getString("date");
                String shipping_area = rs1.getString("shipping_area");
                String transport_type = rs1.getString("transport_type");
                String driver_id = rs1.getString("driver_id");
                String departure_time = rs1.getString("departure_time");
                int truck_license_plate_number = rs1.getInt("truck_license_plate_number");
                String doc_status = rs1.getString("doc_status");

                ArrayList<TransportOrder> all_transport_orders = new ArrayList<TransportOrder>();

                PreparedStatement stmt2 = conn.prepareStatement("SELECT * FROM allTransportOrders WHERE assigned_doc_id = ?");
                stmt2.setInt(1, transport_document_ID);
                ResultSet rs2 = stmt2.executeQuery();

                TransportOrderDAO orderDAO = new TransportOrderDAO();
                DriverDAO driverDAO = new DriverDAO();
                while (rs2.next()) {

                    all_transport_orders.add(orderDAO.getTransportOrderById(rs2.getInt("transport_order_id")));
                }

                TransportDocument transportDocument = new TransportDocument(LocalDate.parse(date), truck_license_plate_number,
                        LocalTime.parse(departure_time), driverDAO.getDriverByID(driver_id),
                        all_transport_orders, shipping_area, transport_type, transport_document_ID);

                if (Objects.equals(doc_status, "inProgress")) {
                    transportDocument.setDoc_status(DocumentStatus.inProgress);
                }
                if (Objects.equals(doc_status, "finished")) {
                    transportDocument.setDoc_status(DocumentStatus.finished);
                }
                this.allTransportDocuments.put(transport_document_ID, transportDocument);
                Database.closeConnection(conn);
                return transportDocument;

            } else {
                Database.closeConnection(conn);
                return null;
            }
        }
        catch (SQLException e) {
            System.out.println("Exception has occurred while getting transport document by id: " + e.getMessage());
            System.out.println(e.getMessage());
            Database.closeConnection(conn);
        }
        return null;
    }

    public void insertTransportDocument(TransportDocument document) throws SQLException {

        try {
            conn = Database.connect();
            int transport_document_id;

            PreparedStatement stmt = conn.prepareStatement("INSERT INTO allTransportDocuments (date," +
                    " truck_license_plate_number, departure_time, driver_id, shipping_area, transport_type, doc_status) VALUES (?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, document.getDate().toString());
            stmt.setInt(2, document.getTruck_license_plate_number());
            stmt.setString(3, document.getDeparture_time().toString());
            stmt.setString(4, document.getDriver().employeeID);
            stmt.setString(5, document.getShipping_area());
            stmt.setString(6, document.getTransport_type());
            stmt.setString(7, document.getDoc_status().toString());

            TransportOrderDAO transportOrderDAO = new TransportOrderDAO();
            for (TransportOrder order : document.getAll_transport_orders()) {

                order.setAssigned_doc_id(document.getTransport_document_ID());
                transportOrderDAO.updateTransportOrder(order);
            }

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    transport_document_id = rs.getInt(1);
                    document.setTransport_document_id(transport_document_id);

                }
            }

            allTransportDocuments.put(document.getTransport_document_ID(), document);
            Database.closeConnection(conn);
        }
        catch (SQLException e) {
            System.out.println("Exception has occurred while inserting transport document: " + e.getMessage());
            System.out.println(e.getMessage());
            Database.closeConnection(conn);
        }
    }

    public void updateTransportDocument(TransportDocument transportDocument) throws SQLException {

        try {
            conn = Database.connect();


            PreparedStatement stmt = conn.prepareStatement("UPDATE allTransportDocuments SET date = ?, truck_license_plate_number = ?, departure_time = ?, driver_id = ?, shipping_area = ?, transport_type = ?, doc_status = ? WHERE transport_document_id = ?");
            stmt.setString(1, transportDocument.getDate().toString());
            stmt.setInt(2, transportDocument.getTruck_license_plate_number());
            stmt.setString(3, transportDocument.getDeparture_time().toString());
            stmt.setString(4, transportDocument.getDriver().employeeID);
            stmt.setString(5, transportDocument.getShipping_area());
            stmt.setString(6, transportDocument.getTransport_type());
            stmt.setString(7, transportDocument.getDoc_status().toString());
            stmt.setInt(8, transportDocument.getTransport_document_ID());

//
//            if(transportDocument.getDoc_status() != DocumentStatus.finished) {
//                TransportOrderDAO transportOrderDAO = new TransportOrderDAO();
//                HashMap<Integer, TransportOrder> allTransportOrdersInTheSystem = transportOrderDAO.getAllTransportOrders();
//                for (TransportOrder transportOrder : allTransportOrdersInTheSystem.values()) {
//                    //if he is in the tables assigned to this transport document
//                    if (transportOrder.getAssigned_doc_id() == transportDocument.getTransport_document_ID()) {
//
//                        transportDocument.getAll_transport_orders().forEach((key) -> {
//                            //but not really in the transport document anymore
//                            if (transportOrder.getTransport_order_ID() != key.getTransport_order_ID()) {
//                                transportOrder.setAssigned_doc_id(-1);
//                                try {
//                                    transportOrderDAO.updateTransportOrder(transportOrder);
//                                } catch (SQLException e) {
//                                    System.out.println("Failed while update transport document in TD-DAO: " + e.getMessage());
//                                }
//                            }
//                        });
//                    }
//                }
//            }
            stmt.executeUpdate();
            allTransportDocuments.put(transportDocument.getTransport_document_ID(), transportDocument);
            Database.closeConnection(conn);
        }
        catch (SQLException e) {
            System.out.println("Exception has occurred while updating transport document: " + e.getMessage());
            System.out.println(e.getMessage());
            Database.closeConnection(conn);
        }
    }

    public void deleteTransportDocument (int id) throws SQLException {
        try {
            conn = Database.connect();
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM allTransportDocuments WHERE transport_document_id = ?");
            stmt.setInt(1,id);
            stmt.executeUpdate();
            TransportDocument transportDocument = this.allTransportDocuments.get(id);
            this.allTransportDocuments.remove(id);

            TransportOrderDAO transportOrderDAO = new TransportOrderDAO();
            HashMap<Integer, TransportOrder> allTransportOrders = transportOrderDAO.getAllTransportOrders();

            for (TransportOrder transportOrder : allTransportOrders.values()) {
                if (transportOrder.getAssigned_doc_id() == transportDocument.getTransport_document_ID()) {
                    transportOrder.setAssigned_doc_id(-1);
                    transportOrderDAO.updateTransportOrder(transportOrder);
                }
            }
            Database.closeConnection(conn);
        } catch (SQLException e) {
            System.out.println("Exception has occurred while deleting a transport document: " + e.getMessage());
            System.out.println(e.getMessage());
            Database.closeConnection(conn);
        }

    }

    public void deleteContent_documents () throws SQLException{
        Statement stmt = null;
        try {
            conn = Database.connect();
            stmt = conn.createStatement();
            stmt.executeUpdate("DELETE FROM allTransportDocuments");

            Statement stmt2 = conn.createStatement();
            String sql = "DELETE FROM SQLITE_SEQUENCE WHERE NAME='allTransportDocuments';";
            stmt2.executeUpdate(sql);

            Database.closeConnection(conn);
            allTransportDocuments.clear();
        }
        catch (SQLException e){
            System.out.println("Exception has occurred while deleting table allTransportDocuments: " + e.getMessage());
            System.out.println(e.getMessage());
            Database.closeConnection(conn);
        }
    }


}
