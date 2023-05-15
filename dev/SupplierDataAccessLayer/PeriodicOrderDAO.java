package SupplierDataAccessLayer;

import SupplierBusinessLayer.PeriodicOrder;
import SupplierDataAccessLayer.Interfaces.iPeriodicOrderDAO;
import Utillity.Response;

import java.sql.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashMap;

public class PeriodicOrderDAO implements iPeriodicOrderDAO {
    private final Connection connection;
    private final HashMap<Integer, PeriodicOrder> periodicOrderIM;
    private final ItemsInPeriodicOrderDAO itemsInPeriodicOrderDAO;

    public PeriodicOrderDAO() {
        connection = Database.connect();
        try {
            Statement statement = connection.createStatement();
            statement.execute("PRAGMA foreign_keys=ON;");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        periodicOrderIM = new HashMap<>();
        itemsInPeriodicOrderDAO = new ItemsInPeriodicOrderDAO();
    }

    @Override
    public HashMap<Integer, PeriodicOrder> getAllPeriodicOrders() {
        try (Statement stmt = connection.createStatement())
        {
            ResultSet result = stmt.executeQuery("SELECT * FROM periodicOrder");
            while (result.next())
            {
                int orderID = result.getInt("periodicOrderID");
                if(periodicOrderIM.containsKey(orderID)) continue;
                int supplierID = result.getInt("supplierID");
                int branchID = result.getInt("branchID");
                DayOfWeek fixedDay = DayOfWeek.valueOf(result.getString("fixedDay"));
                PeriodicOrder order = new PeriodicOrder(orderID, supplierID, branchID, fixedDay, itemsInPeriodicOrderDAO.getProductsInPeriodicOrder(orderID, supplierID));
                periodicOrderIM.put(orderID, order);
            }
            return periodicOrderIM;
        } catch (SQLException e) { System.out.println(e.getMessage()); }
        return null;
    }

    @Override
    public PeriodicOrder getPeriodicOrderByID(int orderID) {
        if(periodicOrderIM.containsKey(orderID)) return periodicOrderIM.get(orderID);
        try (PreparedStatement supplierStatement = connection.prepareStatement("SELECT * FROM periodicOrder WHERE periodicOrderID = ?")) {
            supplierStatement.setInt(1, orderID);
            ResultSet result = supplierStatement.executeQuery();
            if (result.next())
            {
                int supplierID = result.getInt("supplierID");
                int branchID = result.getInt("branchID");
                DayOfWeek fixedDay = DayOfWeek.valueOf(result.getString("fixedDay"));
                PeriodicOrder order = new PeriodicOrder(orderID, supplierID, branchID, fixedDay, itemsInPeriodicOrderDAO.getProductsInPeriodicOrder(orderID, supplierID));
                periodicOrderIM.put(orderID, order);
                return order;
            }
        } catch (SQLException e) { System.out.println(e.getMessage()); }
        return null;
    }

    @Override
    public HashMap<Integer, PeriodicOrder> getPeriodicOrdersFromSupplier(int supplierID) {
        HashMap<Integer, PeriodicOrder> supplierOrders = new HashMap<>();
        for(PeriodicOrder order : periodicOrderIM.values())
            if(order.getSupplierID() == supplierID) supplierOrders.put(order.getPeriodicOrderID(), order);
        try (PreparedStatement supplierStatement = connection.prepareStatement("SELECT * FROM periodicOrder WHERE supplierID = ?")) {
            supplierStatement.setInt(1, supplierID);
            ResultSet result = supplierStatement.executeQuery();
            while (result.next())
            {
                int orderID = result.getInt("periodicOrderID");
                if(supplierOrders.containsKey(orderID)) continue;
                int branchID = result.getInt("branchID");
                DayOfWeek fixedDay = DayOfWeek.valueOf(result.getString("fixedDay"));
                PeriodicOrder order = new PeriodicOrder(orderID, supplierID, branchID, fixedDay, itemsInPeriodicOrderDAO.getProductsInPeriodicOrder(orderID, supplierID));
                periodicOrderIM.put(orderID, order);
                supplierOrders.put(orderID, order);
            }
            return supplierOrders;
        } catch (SQLException e) { System.out.println(e.getMessage()); }
        return null;
    }

    @Override
    public HashMap<Integer, PeriodicOrder> getPeriodicOrdersToBranch(int branchID) {
        HashMap<Integer, PeriodicOrder> branchOrders = new HashMap<>();
        for(PeriodicOrder order : periodicOrderIM.values())
            if(order.getBranchID() == branchID) branchOrders.put(order.getPeriodicOrderID(), order);
        try (PreparedStatement supplierStatement = connection.prepareStatement("SELECT * FROM periodicOrder WHERE branchID = ?")) {
            supplierStatement.setInt(1, branchID);
            ResultSet result = supplierStatement.executeQuery();
            while (result.next())
            {
                int orderID = result.getInt("periodicOrderID");
                if(branchOrders.containsKey(orderID)) continue;
                int supplierID = result.getInt("supplierID");
                DayOfWeek fixedDay = DayOfWeek.valueOf(result.getString("fixedDay"));
                PeriodicOrder order = new PeriodicOrder(orderID, supplierID, branchID, fixedDay, itemsInPeriodicOrderDAO.getProductsInPeriodicOrder(orderID, supplierID));
                periodicOrderIM.put(orderID, order);
                branchOrders.put(orderID, order);
            }
            return branchOrders;
        } catch (SQLException e) { System.out.println(e.getMessage()); }
        return null;
    }

    @Override
    public HashMap<Integer, PeriodicOrder> getAllPeriodicOrderForToday() {
        DayOfWeek todayDate =   LocalDate.now().getDayOfWeek();
        HashMap<Integer, PeriodicOrder> todayOrders = new HashMap<>();
        for(PeriodicOrder order : periodicOrderIM.values())
            if(order.getFixedDay() == todayDate) todayOrders.put(order.getPeriodicOrderID(), order);
        try (PreparedStatement supplierStatement = connection.prepareStatement("SELECT * FROM periodicOrder WHERE fixedDay = ?")) {
            supplierStatement.setString(1, String.valueOf(todayDate));
            ResultSet result = supplierStatement.executeQuery();
            while (result.next())
            {
                int orderID = result.getInt("periodicOrderID");
                if(todayOrders.containsKey(orderID)) continue;
                int supplierID = result.getInt("supplierID");
                int branchID = result.getInt("branchID");
                DayOfWeek fixedDay = DayOfWeek.valueOf(result.getString("fixedDay"));
                PeriodicOrder order = new PeriodicOrder(orderID, supplierID, branchID, fixedDay, itemsInPeriodicOrderDAO.getProductsInPeriodicOrder(orderID, supplierID));
                order.setItemsInOrder(itemsInPeriodicOrderDAO.getProductsInPeriodicOrder(orderID, supplierID));
                periodicOrderIM.put(orderID, order);
                todayOrders.put(orderID, order);
            }
            return todayOrders;
        } catch (SQLException e) { System.out.println(e.getMessage()); }
        return null;
    }

    @Override
    public Response addPeriodicOrder(PeriodicOrder order) {
        try (PreparedStatement contactStatement = connection.prepareStatement("INSERT INTO periodicOrder (periodicOrderID, supplierID, branchID, fixedDay) VALUES (?, ?, ?, ?)"))
        {
            contactStatement.setInt(1, order.getPeriodicOrderID());
            contactStatement.setInt(2, order.getSupplierID());
            contactStatement.setInt(3, order.getBranchID());
            contactStatement.setString(4, order.getFixedDay().toString());
            contactStatement.executeUpdate();
            itemsInPeriodicOrderDAO.addProductsToPeriodicOrder(order.getPeriodicOrderID(), order.getItemsInOrder());
            periodicOrderIM.put(order.getPeriodicOrderID(), order);
        } catch (SQLException e) { return new Response(e.getMessage()); }
        return new Response(order.getPeriodicOrderID());
    }

    @Override
    public Response removePeriodicOrder(int orderID) {
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM periodicOrder WHERE periodicOrderID = ?"))
        {
            statement.setInt(1, orderID);
            statement.executeUpdate();
            periodicOrderIM.remove(orderID);
            return new Response(orderID);
        } catch (SQLException e) { return new Response(e.getMessage()); }
    }

    @Override
    public int getLastPeriodicOrderID() {
        try (Statement statement = connection.createStatement()) {
            String sql = "SELECT MAX(periodicOrderID) AS maxPeriodicOrderID FROM periodicOrder";
            ResultSet rs = statement.executeQuery(sql);
            int maxOrderID = 0;
            if (rs.next()) maxOrderID = rs.getInt("maxPeriodicOrderID");
            rs.close();
            return maxOrderID;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }
}
