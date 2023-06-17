package DataAccessLayer.TransportManager;
import BuisnessLayer.TransportManager.Destination;
import BuisnessLayer.TransportManager.Product;
import BuisnessLayer.TransportManager.Source;
import BuisnessLayer.TransportManager.TransportOrder;
import DataAccessLayer.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;


public class TransportOrderDAO {
    private Connection conn;
    private HashMap<Integer, TransportOrder> allTransportOrders;


    public TransportOrderDAO() throws SQLException {
        allTransportOrders = new HashMap<>();
    }

    public HashMap<Integer, TransportOrder> getAllTransportOrders() throws SQLException {

        try {
            conn = Database.connect();

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM allTransportOrders");

            while (rs.next()) {
                int transport_order_ID = rs.getInt("transport_order_id");

                if (this.allTransportOrders.containsKey(transport_order_ID)) {
                    continue;
                }

                int source_id = rs.getInt("source_id");
                int destination_id = rs.getInt("destination_id");
                int assigned_doc_id = rs.getInt("assigned_doc_id");

                ASiteDAO ASite_DAO = new ASiteDAO();
                Source source = (Source) ASite_DAO.getASiteById(source_id);
                Destination destination = (Destination) ASite_DAO.getASiteById(destination_id);
                HashMap<Product, Integer> products_list = new HashMap<Product, Integer>();

                PreparedStatement stmt2 = conn.prepareStatement("SELECT * FROM ProductsInOrder WHERE transport_order_id = ?");
                stmt2.setInt(1, transport_order_ID);
                ResultSet rs2 = stmt2.executeQuery();

                while (rs2.next()) {

                    int amount_of_product = rs2.getInt("amount");
                    String product_code = rs2.getString("product_code");

                    ProductDAO Product_DAO = new ProductDAO();
                    Product product = Product_DAO.getProductByCode(product_code);
                    products_list.put(product, amount_of_product);
                }

                TransportOrder transportOrder = new TransportOrder(source, destination, products_list, transport_order_ID, assigned_doc_id);
                this.allTransportOrders.put(transport_order_ID, transportOrder);
            }
            Database.closeConnection(conn);
            return this.allTransportOrders;

        } catch (Exception e) {
            System.out.println("Exception has occurred while getting all transport orders: " + e.getMessage());
            System.out.println(e.getMessage());
            Database.closeConnection(conn);
        }
        return null;
    }


    public TransportOrder getTransportOrderById(int id) throws SQLException {

        if (this.allTransportOrders.containsKey(id)) {
            return this.allTransportOrders.get(id);
        }

        try {
            conn = Database.connect();

            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM allTransportOrders WHERE transport_order_id = ?");
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {

                int transport_order_ID = rs.getInt("transport_order_id");
                int source_id = rs.getInt("source_id");
                int destination_id = rs.getInt("destination_id");
                int assigned_doc_id = rs.getInt("assigned_doc_id");

                ASiteDAO ASite_DAO = new ASiteDAO();
                Source source = (Source) ASite_DAO.getASiteById(source_id);
                Destination destination = (Destination) ASite_DAO.getASiteById(destination_id);
                HashMap<Product, Integer> products_list = new HashMap<Product, Integer>();

                PreparedStatement stmt2 = conn.prepareStatement("SELECT * FROM ProductsInOrder WHERE transport_order_id = ?");
                stmt2.setInt(1, transport_order_ID);
                ResultSet rs2 = stmt2.executeQuery();

                while (rs2.next()) {

                    int amount_of_product = rs2.getInt("amount");
                    String product_code = rs2.getString("product_code");

                    ProductDAO Product_DAO = new ProductDAO();
                    Product product = Product_DAO.getProductByCode(product_code);
                    products_list.put(product, amount_of_product);
                }

                TransportOrder transportOrder = new TransportOrder(source, destination, products_list, transport_order_ID, assigned_doc_id);
                this.allTransportOrders.put(transport_order_ID, transportOrder);
                Database.closeConnection(conn);
                return transportOrder;
            } else {
                Database.closeConnection(conn);
                return null;
            }
        } catch (Exception e) {
            System.out.println("Exception has occurred while getting transport order by id: " + e.getMessage());
            System.out.println(e.getMessage());
            Database.closeConnection(conn);
        }
        return null;
    }

    public void deleteTransportOrder(int id) throws SQLException {

        try {
            conn = Database.connect();
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM allTransportOrders WHERE transport_order_id = ?");
            stmt.setInt(1, id);
            stmt.executeUpdate();

            PreparedStatement stmt1 = conn.prepareStatement("DELETE FROM ProductsInOrder WHERE transport_order_id = ?");
            stmt1.setInt(1, id);
            stmt1.executeUpdate();
            allTransportOrders.remove(id);
            Database.closeConnection(conn);
        } catch (Exception e) {
            System.out.println("Exception has occurred while deleting a transport order: " + e.getMessage());
            System.out.println(e.getMessage());
            Database.closeConnection(conn);
        }
    }

    public void insertTransportOrder(TransportOrder transportOrder) throws SQLException {

        try {
            //int max_id = getMax_transport_id();
            //transportOrder.setTransport_order_id(max_id + 1);
            conn = Database.connect();
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO allTransportOrders (source_id, destination_id, assigned_doc_id, transport_order_id) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, transportOrder.getSource().getID());
            stmt.setInt(2, transportOrder.getDestination().getID());
            stmt.setInt(3, transportOrder.getAssigned_doc_id());
            stmt.setInt(4, transportOrder.getTransport_order_id());
            stmt.executeUpdate();

//            try (ResultSet rs = stmt.getGeneratedKeys()) {
//                if (rs.next()) {
//                    transportOrder.setTransport_order_id(rs.getInt(1));
//                    Database.closeConnection(conn);
//                    updateTransportOrder(transportOrder);
//                    conn = Database.connect();
//                }
//            }

            PreparedStatement stmt2 = conn.prepareStatement("INSERT INTO ProductsInOrder (transport_order_id, product_code, amount) VALUES (?, ?, ?)");
            for (Product product : transportOrder.getProducts_list().keySet()) {
                stmt2.setInt(1, transportOrder.getTransport_order_id());
                stmt2.setString(2, product.getProduct_code());
                stmt2.setInt(3, transportOrder.getProducts_list().get(product));
            }
            stmt2.executeUpdate();

            this.allTransportOrders.put(transportOrder.getTransport_order_id(), transportOrder);
            Database.closeConnection(conn);
        } catch (Exception e) {
            System.out.println("Exception has occurred while inserting transport order: " + e.getMessage());
            Database.closeConnection(conn);
        }
    }


    public void updateTransportOrder(TransportOrder transportOrder) throws SQLException {

        try {
            conn = Database.connect();

            int id = transportOrder.getTransport_order_id();

            PreparedStatement stmt = conn.prepareStatement("UPDATE allTransportOrders SET source_id = ?, destination_id = ?, assigned_doc_id = ? WHERE transport_order_id = ?");
            stmt.setInt(1, transportOrder.getSource().getID());
            stmt.setInt(2, transportOrder.getDestination().getID());
            stmt.setInt(3, transportOrder.getAssigned_doc_id());
            stmt.setInt(4, transportOrder.getTransport_order_id());

//            if (transportOrder.getAssigned_doc_id() == -1) {
//                stmt.setInt(5, -1);
//            } else {
//                stmt.setInt(5, transportOrder.getTransport_order_id());
//            }
            stmt.executeUpdate();

            PreparedStatement stmt5 = conn.prepareStatement("SELECT * FROM ProductsInOrder WHERE transport_order_id = ?");
            stmt5.setInt(1, id);
            ResultSet rs = stmt5.executeQuery();
            while (rs.next()) {

                String product_code = rs.getString("product_code");
                ProductDAO productDAO = new ProductDAO();
                Product product = productDAO.getProductByCode(product_code);
                int count = 0;
                for (Product product1 : transportOrder.getProducts_list().keySet()) {
                    if (!Objects.equals(product1.getProduct_code(), product_code) && count == transportOrder.getProducts_list().size()) {
                        PreparedStatement stmt3 = conn.prepareStatement("DELETE FROM ProductsInOrder WHERE product_code = ? AND transport_order_id = ? ");
                        stmt3.setString(1, product_code);
                        stmt3.setInt(2, id);
                        stmt3.executeUpdate();
                    }
                    count++;
                }

            }

            PreparedStatement stmt4 = conn.prepareStatement("UPDATE ProductsInOrder SET amount = ? WHERE product_code = ? AND transport_order_id = ?");
            for (Product product : transportOrder.getProducts_list().keySet()) {
                stmt4.setInt(1, transportOrder.getProducts_list().get(product));
                stmt4.setString(2, product.getProduct_code());
                stmt4.setInt(3, id);
            }
            stmt4.executeUpdate();
            this.allTransportOrders.put(transportOrder.getTransport_order_id(), transportOrder);

            Database.closeConnection(conn);

        } catch (Exception e) {
            System.out.println("Exception has occurred while updating transport order: " + e.getMessage());
            System.out.println(e.getMessage());
            Database.closeConnection(conn);
        }
    }


    public void deleteContent_orders() throws SQLException {
        Statement stmt = null;
        try {
            conn = Database.connect();
            stmt = conn.createStatement();
            stmt.executeUpdate("DELETE FROM allTransportOrders");

            Statement stmt2 = conn.createStatement();
            String sql = "DELETE FROM SQLITE_SEQUENCE WHERE NAME='allTransportOrders';";
            stmt2.executeUpdate(sql);


            Database.closeConnection(conn);
            allTransportOrders.clear();
        } catch (SQLException e) {
            System.out.println("Exception has occurred while deleting table allTransportOrders: " + e.getMessage());
            System.out.println(e.getMessage());
            Database.closeConnection(conn);
        }
    }

    public void deleteContent_ProductsInOrders() throws SQLException {
        Statement stmt = null;
        try {
            conn = Database.connect();
            stmt = conn.createStatement();
            stmt.executeUpdate("DELETE FROM ProductsInOrder");
            Database.closeConnection(conn);
        } catch (SQLException e) {
            System.out.println("Exception has occurred while deleting table ProductsInOrder: " + e.getMessage());
            Database.closeConnection(conn);
        }
    }

    public ArrayList<Integer> get_all_transport_orders_id() {
        ArrayList<Integer> all_ids = new ArrayList<>();
        try {
            conn = Database.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT transport_order_id FROM allTransportOrders");

            while (rs.next()) {
                all_ids.add(rs.getInt(1));
            }
        } catch (SQLException e) {
            System.out.println("Exception has occurred while getting transport order IDs: " + e.getMessage());
        }
        Database.closeConnection(conn);
        return all_ids;
    }
}