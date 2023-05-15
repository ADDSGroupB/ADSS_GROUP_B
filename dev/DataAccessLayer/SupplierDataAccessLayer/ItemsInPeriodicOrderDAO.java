package DataAccessLayer.SupplierDataAccessLayer;

import BusinessLayer.SupplierBusinessLayer.SupplierProduct;
import DataAccessLayer.DBConnector;
import DataAccessLayer.SupplierDataAccessLayer.Interfaces.iItemsInPeriodicOrderDAO;
import Utillity.Response;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class ItemsInPeriodicOrderDAO implements iItemsInPeriodicOrderDAO {
    private final Connection connection;
    private HashMap<Integer, ArrayList<SupplierProduct>> itemsInPeriodicOrderIM;
    private final SupplierProductDAO supplierProductDAO;

    public ItemsInPeriodicOrderDAO() {
        connection = DBConnector.connect();
        try {
            Statement statement = connection.createStatement();
            statement.execute("PRAGMA foreign_keys=ON;");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        itemsInPeriodicOrderIM = new HashMap<>();
        supplierProductDAO = new SupplierProductDAO();
    }

    @Override
    public ArrayList<SupplierProduct> getProductsInPeriodicOrder(int orderID, int supplierID) {
        if(itemsInPeriodicOrderIM.containsKey(orderID)) return itemsInPeriodicOrderIM.get(orderID);
        itemsInPeriodicOrderIM.put(orderID, new ArrayList<>());
        try (PreparedStatement supplierStatement = connection.prepareStatement("SELECT * FROM itemsInPeriodicOrder WHERE periodicOrderID = ?")) {
            supplierStatement.setInt(1, orderID);
            ResultSet result = supplierStatement.executeQuery();
            while (result.next())
            {
                int productID = result.getInt("productID");
                int amountInOrder = result.getInt("amountInOrder");
                SupplierProduct supplierProduct = new SupplierProduct(supplierProductDAO.getSupplierProduct(supplierID, productID));
                supplierProduct.setAmount(amountInOrder);
                itemsInPeriodicOrderIM.get(orderID).add(supplierProduct);
            }
            return itemsInPeriodicOrderIM.get(orderID);
        } catch (SQLException e) { System.out.println(e.getMessage()); }
        return null;
    }

    @Override
    public Response addProductsToPeriodicOrder(int orderID, ArrayList<SupplierProduct> productsInOrder) {
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO itemsInPeriodicOrder (periodicOrderID, supplierID, productID, amountInOrder) VALUES (?, ?, ?, ?)"))
        {
            for(SupplierProduct supplierProduct : productsInOrder)
            {
                statement.setInt(1, orderID);
                statement.setInt(2, supplierProduct.getSupplierId());
                statement.setInt(3, supplierProduct.getProductID());
                statement.setInt(4, supplierProduct.getAmount());
                statement.executeUpdate();
            }
            itemsInPeriodicOrderIM.put(orderID, productsInOrder);
            return new Response(orderID);
        } catch (SQLException e) { return new Response(e.getMessage()); }
    }

    @Override
    public Response addProductToPeriodicOrder(int orderID, SupplierProduct supplierProduct) {
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO itemsInPeriodicOrder (periodicOrderID, supplierID, productID, amountInOrder) VALUES (?, ?, ?, ?)"))
        {
            statement.setInt(1, orderID);
            statement.setInt(2, supplierProduct.getSupplierId());
            statement.setInt(3, supplierProduct.getProductID());
            statement.setInt(4, supplierProduct.getAmount());
            statement.executeUpdate();
            if (!itemsInPeriodicOrderIM.containsKey(orderID)) itemsInPeriodicOrderIM.put(orderID, new ArrayList<>());
            itemsInPeriodicOrderIM.get(orderID).add(supplierProduct);
            return new Response(orderID);
        } catch (SQLException e) { return new Response(e.getMessage()); }
    }

    @Override
    public Response removeProductFromPeriodicOrder(int orderID, int productID) {
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM itemsInPeriodicOrder WHERE periodicOrderID = ? AND productID = ?"))
        {
            statement.setInt(1, orderID);
            statement.setInt(2, productID);
            statement.executeUpdate();
            itemsInPeriodicOrderIM.get(orderID).removeIf(supplierProduct -> supplierProduct.getProductID() == productID);
            return new Response(orderID);
        } catch (SQLException e) { return new Response(e.getMessage()); }
    }

    @Override
    public Response updateProductAmountInPeriodicOrder(int orderID, int productID, int amountInOrder) {
        try (PreparedStatement statement = connection.prepareStatement("UPDATE itemsInPeriodicOrder SET amountInOrder = ? WHERE periodicOrderID = ? AND productID = ?"))
        {
            statement.setInt(1, amountInOrder);
            statement.setInt(2, orderID);
            statement.setInt(3, productID);
            statement.executeUpdate();
            for(SupplierProduct supplierProduct : itemsInPeriodicOrderIM.get(orderID))
                if(supplierProduct.getProductID() == productID) supplierProduct.setAmount(amountInOrder);
            return new Response(orderID);
        } catch (SQLException e) { return new Response(e.getMessage()); }
    }
}
