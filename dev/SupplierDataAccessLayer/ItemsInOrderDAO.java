package SupplierDataAccessLayer;

import SupplierBusinessLayer.SupplierProduct;
import SupplierDataAccessLayer.Interfaces.iItemsInOrderDAO;
import Utillity.Response;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class ItemsInOrderDAO implements iItemsInOrderDAO {
    private final Connection connection;
    private final HashMap<Integer, ArrayList<SupplierProduct>> itemsInOrderIM;
    private final SupplierProductDAO supplierProductDAO;

    public ItemsInOrderDAO() {
        connection = Database.connect();
        try {
            Statement statement = connection.createStatement();
            statement.execute("PRAGMA foreign_keys=ON;");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        itemsInOrderIM = new HashMap<>();
        supplierProductDAO = new SupplierProductDAO();
    }

    @Override
    public ArrayList<SupplierProduct> getProductsInOrder(int orderID, int supplierID) {
        if(itemsInOrderIM.containsKey(orderID)) return itemsInOrderIM.get(orderID);
        itemsInOrderIM.put(orderID, new ArrayList<>());
        try (PreparedStatement supplierStatement = connection.prepareStatement("SELECT * FROM itemsInOrder WHERE orderID = ?")) {
            supplierStatement.setInt(1, orderID);
            ResultSet result = supplierStatement.executeQuery();
            while (result.next())
            {
                int productID = result.getInt("productID");
                int amountInOrder = result.getInt("amountInOrder");
                SupplierProduct supplierProduct = new SupplierProduct(supplierProductDAO.getSupplierProduct(supplierID, productID));
                supplierProduct.setAmount(amountInOrder);
                itemsInOrderIM.get(orderID).add(supplierProduct);
            }
            return itemsInOrderIM.get(orderID);
        } catch (SQLException e) { System.out.println(e.getMessage()); }
        return null;
    }

    @Override
    public Response addProductsToOrder(int orderID, ArrayList<SupplierProduct> productsInOrder) {
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO itemsInOrder (orderID, supplierID, productID, amountInOrder) VALUES (?, ?, ?, ?)"))
        {
            for(SupplierProduct supplierProduct : productsInOrder)
            {
                statement.setInt(1, orderID);
                statement.setInt(2, supplierProduct.getSupplierId());
                statement.setInt(3, supplierProduct.getProductID());
                statement.setInt(4, supplierProduct.getAmount());
                statement.executeUpdate();
            }
            itemsInOrderIM.put(orderID, productsInOrder);
            return new Response(orderID);
        } catch (SQLException e) { return new Response(e.getMessage()); }
    }

    @Override
    public Response addProductToOrder(int orderID, SupplierProduct supplierProduct) {
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO itemsInOrder (orderID, supplierID, productID, amountInOrder) VALUES (?, ?, ?, ?)"))
        {
            statement.setInt(1, orderID);
            statement.setInt(2, supplierProduct.getSupplierId());
            statement.setInt(3, supplierProduct.getProductID());
            statement.setInt(4, supplierProduct.getAmount());
            statement.executeUpdate();
            if (!itemsInOrderIM.containsKey(orderID)) itemsInOrderIM.put(orderID, new ArrayList<>());
            itemsInOrderIM.get(orderID).add(supplierProduct);
            return new Response(orderID);
        } catch (SQLException e) { return new Response(e.getMessage()); }
    }

    @Override
    public Response removeProductFromOrder(int orderID, int productID) {
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM itemsInOrder WHERE orderID = ? AND productID = ?"))
        {
            statement.setInt(1, orderID);
            statement.setInt(2, productID);
            statement.executeUpdate();
            itemsInOrderIM.get(orderID).removeIf(supplierProduct -> supplierProduct.getProductID() == productID);
            return new Response(orderID);
        } catch (SQLException e) { return new Response(e.getMessage()); }
    }

    @Override
    public Response updateProductAmountInOrder(int orderID, int productID, int amountInOrder) {
        try (PreparedStatement statement = connection.prepareStatement("UPDATE itemsInOrder SET amountInOrder = ? WHERE orderID = ? AND productID = ?"))
        {
            statement.setInt(1, amountInOrder);
            statement.setInt(2, orderID);
            statement.setInt(3, productID);
            statement.executeUpdate();
            for(SupplierProduct supplierProduct : itemsInOrderIM.get(orderID))
                if(supplierProduct.getProductID() == productID) supplierProduct.setAmount(amountInOrder);
            return new Response(orderID);
        } catch (SQLException e) { return new Response(e.getMessage()); }
    }
}
