package DataAccessLayer;

import BusinessLayer.Order;
import BusinessLayer.Supplier;
import Utillity.Response;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

public class SupplierOrderDAO implements iSupplierOrderDAO{
    private final Connection connection;
    private final HashMap<Integer, Order> supplierOrderIM;
    private final ItemsInOrderDAO itemsInOrderDAO;

    public SupplierOrderDAO() {
        connection = Database.connect();
        try {
            Statement statement = connection.createStatement();
            statement.execute("PRAGMA foreign_keys=ON;");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        supplierOrderIM = new HashMap<>();
        itemsInOrderDAO = new ItemsInOrderDAO();
    }

    public static void main(String[] args) {
        SupplierOrderDAO supplierOrderDAO = new SupplierOrderDAO();
        HashMap<Integer, Order> allOrders = supplierOrderDAO.getAllOrders();
        Order order = supplierOrderDAO.getOrderByID(1);
        HashMap<Integer, Order> supplierOrders = supplierOrderDAO.getOrdersFromSupplier(1);
        HashMap<Integer, Order> branchOrders = supplierOrderDAO.getOrdersToBranch(1);
        HashMap<Integer, Order> todayOrders = supplierOrderDAO.getAllOrderForToday();
    }

    @Override
    public HashMap<Integer, Order> getAllOrders() {
        try (Statement stmt = connection.createStatement())
        {
            ResultSet result = stmt.executeQuery("SELECT * FROM supplierOrder");
            while (result.next())
            {
                int orderID = result.getInt("orderID");
                if(supplierOrderIM.containsKey(orderID)) continue;
                int supplierID = result.getInt("supplierID");
                int branchID = result.getInt("branchID");
                LocalDate orderDate = stringToLocalDate(result.getString("orderDate"));
                LocalDate deliveryDate = stringToLocalDate(result.getString("deliveryDate"));
                boolean collected = result.getBoolean("collected");
                double totalPriceBeforeDiscount = result.getDouble("totalPriceBeforeDiscount");
                double totalPriceAfterDiscount = result.getDouble("totalPriceAfterDiscount");
                Order order = new Order(orderID, supplierID, branchID, orderDate, deliveryDate, collected, totalPriceBeforeDiscount, totalPriceAfterDiscount);
                order.setItemsInOrder(itemsInOrderDAO.getProductsInOrder(orderID, supplierID));
                supplierOrderIM.put(orderID, order);
            }
            return supplierOrderIM;
        } catch (SQLException e) { System.out.println(e.getMessage()); }
        return null;
    }

    @Override
    public Order getOrderByID(int orderID) {
        if(supplierOrderIM.containsKey(orderID)) return supplierOrderIM.get(orderID);
        try (PreparedStatement supplierStatement = connection.prepareStatement("SELECT * FROM supplierOrder WHERE orderID = ?")) {
            supplierStatement.setInt(1, orderID);
            ResultSet result = supplierStatement.executeQuery();
            if (result.next())
            {
                int supplierID = result.getInt("supplierID");
                int branchID = result.getInt("branchID");
                LocalDate orderDate = stringToLocalDate(result.getString("orderDate"));
                LocalDate deliveryDate = stringToLocalDate(result.getString("deliveryDate"));
                boolean collected = result.getBoolean("collected");
                double totalPriceBeforeDiscount = result.getDouble("totalPriceBeforeDiscount");
                double totalPriceAfterDiscount = result.getDouble("totalPriceAfterDiscount");
                Order order = new Order(orderID, supplierID, branchID, orderDate, deliveryDate, collected, totalPriceBeforeDiscount, totalPriceAfterDiscount);
                order.setItemsInOrder(itemsInOrderDAO.getProductsInOrder(orderID, supplierID));
                supplierOrderIM.put(orderID, order);
                return order;
            }
        } catch (SQLException e) { System.out.println(e.getMessage()); }
        return null;
    }

    @Override
    public HashMap<Integer, Order> getOrdersFromSupplier(int supplierID) {
        HashMap<Integer, Order> supplierOrders = new HashMap<>();
        for(Order order : supplierOrderIM.values())
            if(order.getSupplierId() == supplierID) supplierOrders.put(order.getOrderID(), order);
        try (PreparedStatement supplierStatement = connection.prepareStatement("SELECT * FROM supplierOrder WHERE supplierID = ?")) {
            supplierStatement.setInt(1, supplierID);
            ResultSet result = supplierStatement.executeQuery();
            while (result.next())
            {
                int orderID = result.getInt("orderID");
                if(supplierOrders.containsKey(orderID)) continue;
                int branchID = result.getInt("branchID");
                LocalDate orderDate = stringToLocalDate(result.getString("orderDate"));
                LocalDate deliveryDate = stringToLocalDate(result.getString("deliveryDate"));
                boolean collected = result.getBoolean("collected");
                double totalPriceBeforeDiscount = result.getDouble("totalPriceBeforeDiscount");
                double totalPriceAfterDiscount = result.getDouble("totalPriceAfterDiscount");
                Order order = new Order(orderID, supplierID, branchID, orderDate, deliveryDate, collected, totalPriceBeforeDiscount, totalPriceAfterDiscount);
                order.setItemsInOrder(itemsInOrderDAO.getProductsInOrder(orderID, supplierID));
                supplierOrderIM.put(orderID, order);
                supplierOrders.put(orderID, order);
            }
            return supplierOrders;
        } catch (SQLException e) { System.out.println(e.getMessage()); }
        return null;
    }

    @Override
    public HashMap<Integer, Order> getOrdersToBranch(int branchID) {
        HashMap<Integer, Order> branchOrders = new HashMap<>();
        for(Order order : supplierOrderIM.values())
            if(order.getBranchID() == branchID) branchOrders.put(order.getOrderID(), order);
        try (PreparedStatement supplierStatement = connection.prepareStatement("SELECT * FROM supplierOrder WHERE branchID = ?")) {
            supplierStatement.setInt(1, branchID);
            ResultSet result = supplierStatement.executeQuery();
            while (result.next())
            {
                int orderID = result.getInt("orderID");
                if(branchOrders.containsKey(orderID)) continue;
                int supplierID = result.getInt("supplierID");
                LocalDate orderDate = stringToLocalDate(result.getString("orderDate"));
                LocalDate deliveryDate = stringToLocalDate(result.getString("deliveryDate"));
                boolean collected = result.getBoolean("collected");
                double totalPriceBeforeDiscount = result.getDouble("totalPriceBeforeDiscount");
                double totalPriceAfterDiscount = result.getDouble("totalPriceAfterDiscount");
                Order order = new Order(orderID, supplierID, branchID, orderDate, deliveryDate, collected, totalPriceBeforeDiscount, totalPriceAfterDiscount);
                order.setItemsInOrder(itemsInOrderDAO.getProductsInOrder(orderID, supplierID));
                supplierOrderIM.put(orderID, order);
                branchOrders.put(orderID, order);
            }
            return branchOrders;
        } catch (SQLException e) { System.out.println(e.getMessage()); }
        return null;
    }

    @Override
    public HashMap<Integer, Order> getAllOrderForToday() {
        LocalDate todayDate = LocalDate.now();
        HashMap<Integer, Order> todayOrders = new HashMap<>();
        for(Order order : supplierOrderIM.values())
            if(order.getDeliveryDate() == todayDate) todayOrders.put(order.getOrderID(), order);
        try (PreparedStatement supplierStatement = connection.prepareStatement("SELECT * FROM supplierOrder WHERE deliveryDate = ?")) {
            supplierStatement.setString(1, localDateToString(todayDate));
            ResultSet result = supplierStatement.executeQuery();
            while (result.next())
            {
                int orderID = result.getInt("orderID");
                if(todayOrders.containsKey(orderID)) continue;
                int supplierID = result.getInt("supplierID");
                int branchID = result.getInt("branchID");
                LocalDate orderDate = stringToLocalDate(result.getString("orderDate"));
                LocalDate deliveryDate = stringToLocalDate(result.getString("deliveryDate"));
                boolean collected = result.getBoolean("collected");
                double totalPriceBeforeDiscount = result.getDouble("totalPriceBeforeDiscount");
                double totalPriceAfterDiscount = result.getDouble("totalPriceAfterDiscount");
                Order order = new Order(orderID, supplierID, branchID, orderDate, deliveryDate, collected, totalPriceBeforeDiscount, totalPriceAfterDiscount);
                order.setItemsInOrder(itemsInOrderDAO.getProductsInOrder(orderID, supplierID));
                supplierOrderIM.put(orderID, order);
                todayOrders.put(orderID, order);
            }
            return todayOrders;
        } catch (SQLException e) { System.out.println(e.getMessage()); }
        return null;
    }

    @Override
    public Response addOrder(Order order) {
        return null;
    }

    @Override
    public Response removeOrder(int orderID) {
        return null;
    }

    @Override
    public Response updateSupplierName(int orderID, String name) {
        return null;
    }

    @Override
    public void printOrder(int orderID) {

    }

    @Override
    public int getLastOrderID()
    {
        try (Statement statement = connection.createStatement()) {
            String sql = "SELECT MAX(orderID) AS maxOrderID FROM supplierOrder";
            ResultSet rs = statement.executeQuery(sql);
            int maxOrderID = 0;
            if (rs.next()) maxOrderID = rs.getInt("maxOrderID");
            rs.close();
            return maxOrderID;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }

    public LocalDate stringToLocalDate(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return LocalDate.parse(dateString, formatter);
    }

    public String localDateToString(LocalDate localDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return localDate.format(formatter);
    }
}
