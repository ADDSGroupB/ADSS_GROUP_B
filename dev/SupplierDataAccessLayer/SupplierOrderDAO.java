package SupplierDataAccessLayer;

import SupplierBusinessLayer.Order;
import SupplierDataAccessLayer.Interfaces.iSupplierOrderDAO;
import Utillity.Response;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class SupplierOrderDAO implements iSupplierOrderDAO {
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

//    public static void main(String[] args) {
//        SupplierOrderDAO supplierOrderDAO = new SupplierOrderDAO();
//        HashMap<Integer, Order> allOrders = supplierOrderDAO.getAllOrders();
//        Order order = supplierOrderDAO.getOrderByID(1);
//        HashMap<Integer, Order> supplierOrders = supplierOrderDAO.getOrdersFromSupplier(1);
//        HashMap<Integer, Order> branchOrders = supplierOrderDAO.getOrdersToBranch(1);
//        HashMap<Integer, Order> todayOrders = supplierOrderDAO.getAllOrderForToday();
//    }

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
                String supplierName = result.getString("supplierName");
                String supplierAddress = result.getString("supplierAddress");
                String contactPhoneNumber = result.getString("contactPhoneNumber");
                int branchID = result.getInt("branchID");
                LocalDate creationDate = stringToLocalDate(result.getString("creationDate"));
                LocalDate deliveryDate = stringToLocalDate(result.getString("deliveryDate"));
                boolean collected = result.getBoolean("collected");
                double totalPriceBeforeDiscount = result.getDouble("totalPriceBeforeDiscount");
                double totalPriceAfterDiscount = result.getDouble("totalPriceAfterDiscount");
                Order order = new Order(orderID, supplierID, supplierName, supplierAddress, contactPhoneNumber, branchID, creationDate, deliveryDate, collected, totalPriceBeforeDiscount, totalPriceAfterDiscount);
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
                String supplierName = result.getString("supplierName");
                String supplierAddress = result.getString("supplierAddress");
                String contactPhoneNumber = result.getString("contactPhoneNumber");
                int branchID = result.getInt("branchID");
                LocalDate creationDate = stringToLocalDate(result.getString("creationDate"));
                LocalDate deliveryDate = stringToLocalDate(result.getString("deliveryDate"));
                boolean collected = result.getBoolean("collected");
                double totalPriceBeforeDiscount = result.getDouble("totalPriceBeforeDiscount");
                double totalPriceAfterDiscount = result.getDouble("totalPriceAfterDiscount");
                Order order = new Order(orderID, supplierID, supplierName, supplierAddress, contactPhoneNumber, branchID, creationDate, deliveryDate, collected, totalPriceBeforeDiscount, totalPriceAfterDiscount);
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
                String supplierName = result.getString("supplierName");
                String supplierAddress = result.getString("supplierAddress");
                String contactPhoneNumber = result.getString("contactPhoneNumber");
                int branchID = result.getInt("branchID");
                LocalDate creationDate = stringToLocalDate(result.getString("creationDate"));
                LocalDate deliveryDate = stringToLocalDate(result.getString("deliveryDate"));
                boolean collected = result.getBoolean("collected");
                double totalPriceBeforeDiscount = result.getDouble("totalPriceBeforeDiscount");
                double totalPriceAfterDiscount = result.getDouble("totalPriceAfterDiscount");
                Order order = new Order(orderID, supplierID, supplierName, supplierAddress, contactPhoneNumber, branchID, creationDate, deliveryDate, collected, totalPriceBeforeDiscount, totalPriceAfterDiscount);
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
                String supplierName = result.getString("supplierName");
                String supplierAddress = result.getString("supplierAddress");
                String contactPhoneNumber = result.getString("contactPhoneNumber");
                LocalDate creationDate = stringToLocalDate(result.getString("creationDate"));
                LocalDate deliveryDate = stringToLocalDate(result.getString("deliveryDate"));
                boolean collected = result.getBoolean("collected");
                double totalPriceBeforeDiscount = result.getDouble("totalPriceBeforeDiscount");
                double totalPriceAfterDiscount = result.getDouble("totalPriceAfterDiscount");
                Order order = new Order(orderID, supplierID, supplierName, supplierAddress, contactPhoneNumber, branchID, creationDate, deliveryDate, collected, totalPriceBeforeDiscount, totalPriceAfterDiscount);
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
                String supplierName = result.getString("supplierName");
                String supplierAddress = result.getString("supplierAddress");
                String contactPhoneNumber = result.getString("contactPhoneNumber");
                int branchID = result.getInt("branchID");
                LocalDate creationDate = stringToLocalDate(result.getString("creationDate"));
                LocalDate deliveryDate = stringToLocalDate(result.getString("deliveryDate"));
                boolean collected = result.getBoolean("collected");
                double totalPriceBeforeDiscount = result.getDouble("totalPriceBeforeDiscount");
                double totalPriceAfterDiscount = result.getDouble("totalPriceAfterDiscount");
                Order order = new Order(orderID, supplierID, supplierName, supplierAddress, contactPhoneNumber, branchID, creationDate, deliveryDate, collected, totalPriceBeforeDiscount, totalPriceAfterDiscount);
                order.setItemsInOrder(itemsInOrderDAO.getProductsInOrder(orderID, supplierID));
                supplierOrderIM.put(orderID, order);
                todayOrders.put(orderID, order);
            }
            return todayOrders;
        } catch (SQLException e) { System.out.println(e.getMessage()); }
        return null;
    }

    @Override
    public HashMap<Integer, Order> getNoneCollectedOrdersForToday(int branchID) {
        LocalDate todayDate = LocalDate.now();
        HashMap<Integer, Order> todayOrders = new HashMap<>();
        for(Order order : supplierOrderIM.values())
            if(order.getDeliveryDate() == todayDate) todayOrders.put(order.getOrderID(), order);
        try (PreparedStatement supplierStatement = connection.prepareStatement("SELECT * FROM supplierOrder WHERE branchID = ? AND deliveryDate = ? AND collected = 0")) {
            supplierStatement.setInt(1, branchID);
            supplierStatement.setString(2, localDateToString(todayDate));
            ResultSet result = supplierStatement.executeQuery();
            while (result.next())
            {
                int orderID = result.getInt("orderID");
                if(todayOrders.containsKey(orderID)) continue;
                int supplierID = result.getInt("supplierID");
                String supplierName = result.getString("supplierName");
                String supplierAddress = result.getString("supplierAddress");
                String contactPhoneNumber = result.getString("contactPhoneNumber");
                LocalDate creationDate = stringToLocalDate(result.getString("creationDate"));
                LocalDate deliveryDate = stringToLocalDate(result.getString("deliveryDate"));
                boolean collected = result.getBoolean("collected");
                double totalPriceBeforeDiscount = result.getDouble("totalPriceBeforeDiscount");
                double totalPriceAfterDiscount = result.getDouble("totalPriceAfterDiscount");
                Order order = new Order(orderID, supplierID, supplierName, supplierAddress, contactPhoneNumber, branchID, creationDate, deliveryDate, collected, totalPriceBeforeDiscount, totalPriceAfterDiscount);
                order.setItemsInOrder(itemsInOrderDAO.getProductsInOrder(orderID, supplierID));
                supplierOrderIM.put(orderID, order);
                todayOrders.put(orderID, order);
            }
            return todayOrders;
        } catch (SQLException e) { System.out.println(e.getMessage()); }
        return null;
    }

    @Override
    public Response markOrderAsCollected(int orderID) {
        try (PreparedStatement statement = connection.prepareStatement("UPDATE supplierOrder SET collected = 1 WHERE orderID = ?"))
        {
            statement.setInt(1, orderID);
            statement.executeUpdate();
            if (supplierOrderIM.containsKey(orderID)) supplierOrderIM.get(orderID).setCollected(true);
            return new Response(orderID);
        } catch (SQLException e) { return new Response(e.getMessage()); }
    }

    @Override
    public Response addOrder(Order order) {
        try (PreparedStatement contactStatement = connection.prepareStatement("INSERT INTO supplierOrder (orderID, supplierID, supplierName, supplierAddress, contactPhoneNumber, branchID, creationDate, deliveryDate, collected, totalPriceBeforeDiscount, totalPriceAfterDiscount) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"))
        {
            contactStatement.setInt(1, order.getOrderID());
            contactStatement.setInt(2, order.getSupplierId());
            contactStatement.setString(3, order.getSupplierName());
            contactStatement.setString(4, order.getSupplierAddress());
            contactStatement.setString(5, order.getContactPhoneNumber());
            contactStatement.setInt(6, order.getBranchID());
            contactStatement.setString(7, localDateToString(order.getCreationDate()));
            contactStatement.setString(8, localDateToString(order.getDeliveryDate()));
            contactStatement.setBoolean(9, order.isCollected());
            contactStatement.setDouble(10, order.getTotalPriceBeforeDiscount());
            contactStatement.setDouble(11, order.getTotalPriceAfterDiscount());
            contactStatement.executeUpdate();
            itemsInOrderDAO.addProductsToOrder(order.getOrderID(), order.getItemsInOrder());
            supplierOrderIM.put(order.getOrderID(), order);
        } catch (SQLException e) { return new Response(e.getMessage()); }
        return new Response(order.getOrderID());
    }

    @Override
    public Response removeOrder(int orderID) {
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM supplierOrder WHERE orderID = ?"))
        {
            statement.setInt(1, orderID);
            statement.executeUpdate();
            supplierOrderIM.remove(orderID);
            return new Response(orderID);
        } catch (SQLException e) { return new Response(e.getMessage()); }
    }

//    @Override
//    public void printOrder(int orderID) {
//
//    }

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
