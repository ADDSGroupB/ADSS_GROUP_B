package DataAccessLayer.SupplierDataAccessLayer;

import DataAccessLayer.DBConnector;
import DataAccessLayer.SupplierDataAccessLayer.Interfaces.iDiscountPerAmountDAO;
import Utillity.Pair;
import Utillity.Response;

import java.sql.*;
import java.util.HashMap;

public class DiscountPerAmountDAO implements iDiscountPerAmountDAO {
    private Connection connection;
    private HashMap<Pair<Integer, Integer>, HashMap<Integer, Double>> discountIM;

    public DiscountPerAmountDAO() {
        connection = DBConnector.connect();
        try {
            Statement statement = connection.createStatement();
            statement.execute("PRAGMA foreign_keys=ON;");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        discountIM = new HashMap<>();
    }

    @Override
    public HashMap<Integer, Double> getProductDiscountByID(int supplierID, int productID) {
        Pair<Integer, Integer> pair = new Pair<>(supplierID, productID);
        if(discountIM.containsKey(pair)) return discountIM.get(pair);
        try (PreparedStatement discountStatement = connection.prepareStatement("SELECT * FROM discountPerAmount WHERE supplierID = ? AND productID = ?")) {
            discountStatement.setInt(1, supplierID);
            discountStatement.setInt(2, productID);
            ResultSet discountResult = discountStatement.executeQuery();
            HashMap<Integer, Double> discountPerAmount = new HashMap<>();
            while (discountResult.next())
            {
                int amount = discountResult.getInt("discountPerAmount");
                double discount = discountResult.getDouble("discount");
                discountPerAmount.put(amount, discount);
            }
            discountIM.put(pair, discountPerAmount);
            return discountPerAmount;
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public Response addDiscount(int supplierID, int productID, int discountPerAmount, double discount) {
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO discountPerAmount (supplierID, productID, discountPerAmount, discount) VALUES (?, ?, ?, ?)"))
        {
            statement.setInt(1, supplierID);
            statement.setInt(2, productID);
            statement.setInt(3, discountPerAmount);
            statement.setDouble(4, discount);
            statement.executeUpdate();
            Pair<Integer, Integer> pair = new Pair<>(supplierID, productID);
            if(discountIM.containsKey(pair)) discountIM.get(pair).put(discountPerAmount, discount);
            else
            {
                HashMap<Integer, Double> discountHash = new HashMap<>();
                discountHash.put(discountPerAmount, discount);
                discountIM.put(pair, discountHash);
            }
            return new Response(supplierID);
        } catch (SQLException e) { return new Response(e.getMessage()); }
    }

    @Override
    public Response removeAllDiscount(int supplierID, int productID) {
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM discountPerAmount WHERE supplierID = ? AND productID = ?"))
        {
            statement.setInt(1, supplierID);
            statement.setInt(2, productID);
            statement.executeUpdate();
            Pair<Integer, Integer> pair = new Pair<>(supplierID, productID);
            discountIM.remove(pair);
            return new Response(supplierID);
        } catch (SQLException e) { return new Response(e.getMessage()); }
    }

    @Override
    public Response removeDiscount(int supplierID, int productID, int discountPerAmount) {
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM discountPerAmount WHERE supplierID = ? AND productID = ? AND discountPerAmount = ?"))
        {
            statement.setInt(1, supplierID);
            statement.setInt(2, productID);
            statement.setInt(3, discountPerAmount);
            statement.executeUpdate();
            Pair<Integer, Integer> pair = new Pair<>(supplierID, productID);
            if(discountIM.containsKey(pair)) discountIM.get(pair).remove(discountPerAmount);
            return new Response(supplierID);
        } catch (SQLException e) { return new Response(e.getMessage()); }
    }
}
