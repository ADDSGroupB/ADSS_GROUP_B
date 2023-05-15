package DataAccessLayer.SupplierDataAccessLayer;

import DataAccessLayer.SupplierDataAccessLayer.Interfaces.iDiscountDAO;
import Utillity.Pair;
import Utillity.Response;

import java.sql.*;
import java.util.HashMap;
import java.util.Objects;

public class DiscountDAO implements iDiscountDAO {
    private Connection connection;
    private HashMap<Pair<Integer, String>, Pair<Double, Double>> discountIM;

    public DiscountDAO() {
        connection = Database.connect();
        try {
            Statement statement = connection.createStatement();
            statement.execute("PRAGMA foreign_keys=ON;");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        discountIM = new HashMap<>();
    }

    @Override
    public Pair<Integer, Double> getAmountDiscountByID(int id) {
        Pair<Integer, String> pair = new Pair<>(id, "Amount");
        Pair<Double, Double> oldPair;
        if(discountIM.containsKey(pair))
        {
            oldPair = discountIM.get(pair);
            return new Pair<>(oldPair.getFirst().intValue(), oldPair.getSecond());
        }
        try (PreparedStatement discountStatement = connection.prepareStatement("SELECT * FROM discount WHERE supplierID = ?")) {
            discountStatement.setInt(1, id);
            ResultSet discountResult = discountStatement.executeQuery();
            while (discountResult.next())
            {
                if(Objects.equals(discountResult.getString("type"), "Amount"))
                {
                    int amount = (int)discountResult.getDouble("amount");
                    double discount = discountResult.getDouble("discount");
                    Pair<Integer, Double> pairAmountAndDiscount = new Pair<>(amount, discount);
                    discountIM.put(pair, new Pair<>(discountResult.getDouble("amount"), discount));
                    return pairAmountAndDiscount;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Pair<Double, Double> getPriceDiscountByID(int id) {
        Pair<Integer, String> pair = new Pair<>(id, "Price");
        if(discountIM.containsKey(pair)) return discountIM.get(pair);
        try (PreparedStatement discountStatement = connection.prepareStatement("SELECT * FROM discount WHERE supplierID = ?")) {
            discountStatement.setInt(1, id);
            ResultSet discountResult = discountStatement.executeQuery();
            while (discountResult.next())
            {
                if(Objects.equals(discountResult.getString("type"), "Price")) // There is "Amount" or "Price"
                {
                    double amount = discountResult.getDouble("amount");
                    double discount = discountResult.getDouble("discount");
                    Pair<Double, Double> pairAmountAndDiscount = new Pair<>(amount, discount);
                    discountIM.put(pair, pairAmountAndDiscount);
                    return pairAmountAndDiscount;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Response addDiscount(int id, String type, Pair discount) {
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO discount (supplierID, type, amount, discount) VALUES (?, ?, ?, ?)"))
        {
            statement.setInt(1, id);
            statement.setString(2, type);
            if(discount.getFirst() instanceof Integer) statement.setDouble(3, ((Integer) discount.getFirst()).doubleValue());
            else statement.setDouble(3, (Double) discount.getFirst());
            statement.setDouble(4, (Double) discount.getSecond());
            statement.executeUpdate();
            Pair<Integer, String> pair = new Pair<>(id, type);
            Pair<Double, Double> pairAmountAndDiscount;
            if(discount.getFirst() instanceof Integer) pairAmountAndDiscount = new Pair<>(((Integer) discount.getFirst()).doubleValue(), (Double)discount.getSecond());
            else pairAmountAndDiscount = new Pair<>((Double) discount.getFirst(), (Double)discount.getSecond());
            discountIM.put(pair, pairAmountAndDiscount);
            return new Response(id);
        } catch (SQLException e) { return new Response(e.getMessage()); }
    }

    @Override
    public Response removeDiscount(int id, String type) {
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM discount WHERE supplierID = ? AND type = ?"))
        {
            statement.setInt(1, id);
            statement.setString(2, type);
            statement.executeUpdate();
            discountIM.remove(new Pair<>(id, type));
            return new Response(id);
        } catch (SQLException e) { return new Response(e.getMessage()); }
    }
}
