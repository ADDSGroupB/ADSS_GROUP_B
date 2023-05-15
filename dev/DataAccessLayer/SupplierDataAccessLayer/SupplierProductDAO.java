package DataAccessLayer.SupplierDataAccessLayer;

import BusinessLayer.SupplierBusinessLayer.SupplierProduct;
import DataAccessLayer.DBConnector;
import DataAccessLayer.SupplierDataAccessLayer.Interfaces.iSupplierProductDAO;
import Utillity.Pair;
import Utillity.Response;

import java.sql.*;
import java.util.HashMap;

public class SupplierProductDAO implements iSupplierProductDAO {
    private final Connection connection;
    private final HashMap<Pair<Integer, Integer>, SupplierProduct> supplierProductIM;
    private final DiscountPerAmountDAO discountPerAmountDAO;

    public SupplierProductDAO() {
        connection = DBConnector.connect();
        try {
            Statement statement = connection.createStatement();
            statement.execute("PRAGMA foreign_keys=ON;");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        supplierProductIM = new HashMap<>();
        discountPerAmountDAO = new DiscountPerAmountDAO();
    }

    @Override
    public HashMap<Integer, SupplierProduct> getAllSupplierProductsByID(int supplierId) {
        try (PreparedStatement sProductStatement = connection.prepareStatement("SELECT * FROM supplierProduct WHERE supplierID = ?")) {
            sProductStatement.setInt(1, supplierId);
            ResultSet supplierResult = sProductStatement.executeQuery();
            HashMap<Integer, SupplierProduct> supplierProducts = new HashMap<>();
            while (supplierResult.next())
            {
                String name = supplierResult.getString("name");
                int catalogID = supplierResult.getInt("catalogNumber");
                int productID = supplierResult.getInt("productID");
                double price = supplierResult.getDouble("price");
                String manufacturer = supplierResult.getString("manufacturer");
                int expirationDays = supplierResult.getInt("expirationDays");
                Double weight = supplierResult.getDouble("weight");
                HashMap<Integer, Double> discountPerAmount = discountPerAmountDAO.getProductDiscountByID(supplierId, productID);
                int amount = supplierResult.getInt("amount");
                Pair<Integer, Integer> pair = new Pair<>(supplierId, productID);
                if(!supplierProductIM.containsKey(pair))
                {
                    SupplierProduct supplierProduct = new SupplierProduct(name, supplierId, productID, catalogID, price, amount, discountPerAmount, manufacturer, expirationDays, weight);
                    supplierProductIM.put(pair, supplierProduct);
                    supplierProducts.put(productID, supplierProduct);
                }
                else
                    supplierProducts.put(productID, supplierProductIM.get(pair));
            }
            return supplierProducts;
        } catch (SQLException e) { System.out.println(e.getMessage()); }
        return null;
    }

    @Override
    public SupplierProduct getSupplierProduct(int supplierID, int productID) {
        Pair<Integer, Integer> pair = new Pair<>(supplierID, productID);
        if(supplierProductIM.containsKey(pair)) return supplierProductIM.get(pair);
        try (PreparedStatement sProductStatement = connection.prepareStatement("SELECT * FROM supplierProduct WHERE supplierID = ? AND productID = ?")) {
            sProductStatement.setInt(1, supplierID);
            sProductStatement.setInt(2, productID);
            ResultSet supplierResult = sProductStatement.executeQuery();
            if (supplierResult.next())
            {
                String name = supplierResult.getString("name");
                int catalogID = supplierResult.getInt("catalogNumber");
                double price = supplierResult.getDouble("price");
                String manufacturer = supplierResult.getString("manufacturer");
                int expirationDays = supplierResult.getInt("expirationDays");
                Double weight = supplierResult.getDouble("weight");
                HashMap<Integer, Double> discountPerAmount = discountPerAmountDAO.getProductDiscountByID(supplierID, productID);
                int amount = supplierResult.getInt("amount");
                SupplierProduct supplierProduct = new SupplierProduct(name, supplierID, productID, catalogID, price, amount, discountPerAmount, manufacturer, expirationDays, weight);
                supplierProductIM.put(pair, supplierProduct);
                return supplierProduct;
            }
        } catch (SQLException e) { System.out.println(e.getMessage()); }
        return null;
    }

    @Override
    public Response addSupplierProduct(int supplierID, SupplierProduct supplierProduct) {
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO supplierProduct (supplierID, productID, catalogNumber, name, price, amount, weight, manufacturer, expirationDays) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"))
        {
            statement.setInt(1, supplierID);
            statement.setInt(2, supplierProduct.getProductID());
            statement.setInt(3, supplierProduct.getCatalogId());
            statement.setString(4, supplierProduct.getName());
            statement.setDouble(5, supplierProduct.getPrice());
            statement.setInt(6, supplierProduct.getAmount());
            statement.setDouble(7, supplierProduct.getWeight());
            statement.setString(8, supplierProduct.getManufacturer());
            statement.setInt(9, supplierProduct.getExpirationDays());
            statement.executeUpdate();
            supplierProductIM.put(new Pair<>(supplierID, supplierProduct.getProductID()), supplierProduct);
            return new Response(supplierID);
        } catch (SQLException e) { return new Response(e.getMessage()); }
    }

    @Override
    public Response removeSupplierProduct(int supplierID, int productID) {
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM supplierProduct WHERE supplierID = ? AND productID = ?"))
        {
            statement.setInt(1, supplierID);
            statement.setInt(2, productID);
            statement.executeUpdate();
            Pair<Integer, Integer> pair = new Pair<>(supplierID, productID);
            supplierProductIM.remove(pair);
            return new Response(supplierID);
        } catch (SQLException e) { return new Response(e.getMessage()); }
    }

    @Override
    public Response updateSupplierProductCatalogNumber(int supplierID, int productID, int catalogNumber) {
        try (PreparedStatement statement = connection.prepareStatement("UPDATE supplierProduct SET catalogNumber = ? WHERE supplierID = ? AND productID = ?"))
        {
            statement.setInt(1, catalogNumber);
            statement.setInt(2, supplierID);
            statement.setInt(3, productID);
            statement.executeUpdate();
            Pair<Integer, Integer> pair = new Pair<>(supplierID, productID);
            if (supplierProductIM.containsKey(pair)) supplierProductIM.get(pair).setCatalogID(catalogNumber);
            return new Response(supplierID);
        } catch (SQLException e) { return new Response(e.getMessage()); }
    }

    @Override
    public Response updateSupplierProductPrice(int supplierID, int productID, double price) {
        try (PreparedStatement statement = connection.prepareStatement("UPDATE supplierProduct SET price = ? WHERE supplierID = ? AND productID = ?"))
        {
            statement.setDouble(1, price);
            statement.setInt(2, supplierID);
            statement.setInt(3, productID);
            statement.executeUpdate();
            Pair<Integer, Integer> pair = new Pair<>(supplierID, productID);
            if (supplierProductIM.containsKey(pair)) supplierProductIM.get(pair).setPrice(price);
            return new Response(supplierID);
        } catch (SQLException e) { return new Response(e.getMessage()); }
    }

    @Override
    public Response updateSupplierProductAmount(int supplierID, int productID, int amount) {
        try (PreparedStatement statement = connection.prepareStatement("UPDATE supplierProduct SET amount = ? WHERE supplierID = ? AND productID = ?"))
        {
            statement.setInt(1, amount);
            statement.setInt(2, supplierID);
            statement.setInt(3, productID);
            statement.executeUpdate();
            Pair<Integer, Integer> pair = new Pair<>(supplierID, productID);
            if (supplierProductIM.containsKey(pair)) supplierProductIM.get(pair).setAmount(amount);
            return new Response(supplierID);
        } catch (SQLException e) { return new Response(e.getMessage()); }
    }

    @Override
    public Response updateSupplierProductWeight(int supplierID, int productID, double weight) {
        try (PreparedStatement statement = connection.prepareStatement("UPDATE supplierProduct SET weight = ? WHERE supplierID = ? AND productID = ?"))
        {
            statement.setDouble(1, weight);
            statement.setInt(2, supplierID);
            statement.setInt(3, productID);
            statement.executeUpdate();
            Pair<Integer, Integer> pair = new Pair<>(supplierID, productID);
            if (supplierProductIM.containsKey(pair)) supplierProductIM.get(pair).setWeight(weight);
            return new Response(supplierID);
        } catch (SQLException e) { return new Response(e.getMessage()); }
    }

    @Override
    public Response updateSupplierProductManufacturer(int supplierID, int productID, String manufacturer) {
        try (PreparedStatement statement = connection.prepareStatement("UPDATE supplierProduct SET manufacturer = ? WHERE supplierID = ? AND productID = ?"))
        {
            statement.setString(1, manufacturer);
            statement.setInt(2, supplierID);
            statement.setInt(3, productID);
            statement.executeUpdate();
            Pair<Integer, Integer> pair = new Pair<>(supplierID, productID);
            if (supplierProductIM.containsKey(pair)) supplierProductIM.get(pair).setManufacturer(manufacturer);
            return new Response(supplierID);
        } catch (SQLException e) { return new Response(e.getMessage()); }
    }

    @Override
    public Response updateSupplierProductExpirationDays(int supplierID, int productID, int expirationDays) {
        try (PreparedStatement statement = connection.prepareStatement("UPDATE supplierProduct SET expirationDays = ? WHERE supplierID = ? AND productID = ?"))
        {
            statement.setInt(1, expirationDays);
            statement.setInt(2, supplierID);
            statement.setInt(3, productID);
            statement.executeUpdate();
            Pair<Integer, Integer> pair = new Pair<>(supplierID, productID);
            if (supplierProductIM.containsKey(pair)) supplierProductIM.get(pair).setExpirationDays(expirationDays);
            return new Response(supplierID);
        } catch (SQLException e) { return new Response(e.getMessage()); }
    }

    @Override
    public void printProductsBySupplierID(int supplierID)
    {
        try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM supplierProduct WHERE supplierID = ?"))
        {
            stmt.setInt(1, supplierID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next())
            {
                int productID = rs.getInt("productID");
                int catalogNumber = rs.getInt("catalogNumber");
                String name = rs.getString("name");
                int amount = rs.getInt("amount");
                double price = rs.getDouble("price");
                double weight = rs.getDouble("weight");
                String manufacturer = rs.getString("manufacturer");
                String expirationDays = rs.getString("expirationDays");
                System.out.println("Supplier ID: " + supplierID + ", Product ID: " + productID + ", Catalog Number: " + catalogNumber + ", Name: " + name + ", Amount: " + amount + ", Price: " + price + ", Weight: " + weight + ", Manufacturer: " + manufacturer + ", Expiration Days: " + expirationDays);
            }
            rs.close();
        } catch (SQLException e) { System.out.println(e.getMessage());}
    }
}
