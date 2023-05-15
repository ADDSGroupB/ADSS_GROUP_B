package DataAccessLayer.SupplierDataAccessLayer;

import DataAccessLayer.SupplierDataAccessLayer.Interfaces.iDeliveryDaysDAO;
import Utillity.Response;

import java.sql.*;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashMap;

public class DeliveryDaysDAO implements iDeliveryDaysDAO {
    private Connection connection;
    private HashMap<Integer, ArrayList<DayOfWeek>> deliveryDaysIM;

    public DeliveryDaysDAO() {
        connection = Database.connect();
        try {
            Statement statement = connection.createStatement();
            statement.execute("PRAGMA foreign_keys=ON;");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        deliveryDaysIM = new HashMap<>();
    }

    @Override
    public ArrayList<DayOfWeek> getDeliveryDays(int id) {
        if(deliveryDaysIM.containsKey(id)) return deliveryDaysIM.get(id);
        try (PreparedStatement agreementStatement = connection.prepareStatement("SELECT * FROM deliveryDays WHERE supplierID = ?")) {
            agreementStatement.setInt(1, id);
            ResultSet agreementResult = agreementStatement.executeQuery();
            ArrayList<DayOfWeek> days = new ArrayList<>();
            while (agreementResult.next())
            {
                DayOfWeek day = getDayFromString(agreementResult.getString("day"));
                days.add(day);
            }
            if(days.size() == 0) return null;
            deliveryDaysIM.put(id, days);
            return days;
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public Response addDeliveryDays(int supplierID, ArrayList<DayOfWeek> days) {
        if(days == null) return null;
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO deliveryDays (supplierID, day) VALUES (?, ?)"))
        {
            for(DayOfWeek day : days)
            {
                statement.setInt(1, supplierID);
                statement.setString(2, getStringFromDay(day));
                statement.executeUpdate();
            }
            deliveryDaysIM.put(supplierID, days);
            return new Response(supplierID);
        } catch (SQLException e) { return new Response(e.getMessage()); }
    }

    @Override
    public Response removeDeliveryDays(int supplierID) {
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM deliveryDays WHERE supplierID = ?"))
        {
            statement.setInt(1, supplierID);
            statement.executeUpdate();
            deliveryDaysIM.remove(supplierID);
            return new Response(supplierID);
        } catch (SQLException e) { return new Response(e.getMessage()); }
    }

    @Override
    public Response updateDeliveryDays(int supplierID, ArrayList<DayOfWeek> days)
    {
//        ArrayList<DayOfWeek> result = new ArrayList<>(days);
//        ArrayList<DayOfWeek> currDeliveryDays = getDeliveryDays(supplierID);
//        result.removeAll(currDeliveryDays);
        Response response = removeDeliveryDays(supplierID);
        if(response.errorOccurred()) return response;
        return addDeliveryDays(supplierID, days);
    }

    private static DayOfWeek getDayFromString(String day)
    {
        if (day.equalsIgnoreCase("None")) return null;
        return DayOfWeek.valueOf(day.toUpperCase());
    }

    private static String getStringFromDay(DayOfWeek day)
    {
        if (day == null) return "None";
        else return day.name();
    }
}
