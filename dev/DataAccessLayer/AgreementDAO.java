package DataAccessLayer;

import BusinessLayer.Agreement;
import BusinessLayer.SupplierProduct;
import DataAccessLayer.Interfaces.*;
import Utillity.Pair;
import Utillity.Response;

import java.sql.*;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AgreementDAO implements iAgreementDAO {
    private final Connection connection;
    private final HashMap<Integer, Agreement> agreementIM;
    private final iDiscountDAO discountDAO;
    private final iDiscountPerAmountDAO discountPerAmountDAO;
    private final iSupplierProductDAO supplierProductDAO;
    private final iDeliveryDaysDAO deliveryDaysDAO;

    public AgreementDAO() {
        connection = Database.connect();
        try {
            Statement statement = connection.createStatement();
            statement.execute("PRAGMA foreign_keys=ON;");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        agreementIM = new HashMap<>();
        discountDAO = new DiscountDAO();
        discountPerAmountDAO = new DiscountPerAmountDAO();
        supplierProductDAO = new SupplierProductDAO();
        deliveryDaysDAO = new DeliveryDaysDAO();
    }

    @Override
    public Agreement getAgreementByID(int id) {
        if(agreementIM.containsKey(id)) return agreementIM.get(id);
        try (PreparedStatement agreementStatement = connection.prepareStatement("SELECT * FROM agreement WHERE supplierID = ?")) {
            agreementStatement.setInt(1, id);
            ResultSet agreementResult = agreementStatement.executeQuery();
            if (agreementResult.next())
            {
                String paymentType = agreementResult.getString("paymentType");
                boolean selfSupply = agreementResult.getBoolean("selfSupply");
                String supplyMethod = agreementResult.getString("supplyMethod");
                int supplyTime = agreementResult.getInt("supplyTime");
                ArrayList<DayOfWeek> supplyDays = deliveryDaysDAO.getDeliveryDays(id);
                Agreement agreement = new Agreement(paymentType, selfSupply, supplyMethod, supplyTime, supplyDays);
                agreement.setTotalDiscountInPrecentageForOrderAmount(discountDAO.getAmountDiscountByID(id));
                agreement.setTotalOrderDiscountPerOrderPrice(discountDAO.getPriceDiscountByID(id));
                agreement.setSupllyingProducts(supplierProductDAO.getAllSupplierProductsByID(id));
                agreementIM.put(id, agreement);
                return agreement;
            }
            return null;
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public Response addAgreement(int supplierID, Agreement agreement) {
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO agreement (supplierID, paymentType, selfSupply, supplyMethod, supplyTime) VALUES (?, ?, ?, ?, ?)"))
        {
            statement.setInt(1, supplierID);
            statement.setString(2, agreement.getPaymentType());
            statement.setBoolean(3, agreement.getSelfSupply());
            statement.setString(4, agreement.getSupplyMethod());
            statement.setInt(5, agreement.getSupplyTime());
            statement.executeUpdate();
            Response response = deliveryDaysDAO.addDeliveryDays(supplierID, agreement.getSupplyDays());
            if(response.errorOccurred()) return response;
            response = addSupplierProducts(supplierID, agreement.getSupllyingProducts());
            if (response.errorOccurred()) return response;
            response = addDiscountOnProducts(supplierID, agreement.getSupllyingProducts());
            if (response.errorOccurred()) return response;
            agreementIM.put(supplierID, agreement);
            return new Response(supplierID);
        } catch (SQLException e) { return new Response(e.getMessage()); }
    }

    @Override
    public Response addAgreementWithDiscount(int supplierID, Agreement agreement) {
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO agreement (supplierID, paymentType, selfSupply, supplyMethod, supplyTime) VALUES (?, ?, ?, ?, ?)"))
        {
            statement.setInt(1, supplierID);
            statement.setString(2, agreement.getPaymentType());
            statement.setBoolean(3, agreement.getSelfSupply());
            statement.setString(4, agreement.getSupplyMethod());
            statement.setInt(5, agreement.getSupplyTime());
            statement.executeUpdate();
            Response response = deliveryDaysDAO.addDeliveryDays(supplierID, agreement.getSupplyDays());
            if(response.errorOccurred()) return response;
            Pair<Integer, Double> discount = agreement.getTotalDiscountInPrecentageForOrder();
            if (discount != null)
                response = discountDAO.addDiscount(supplierID, "Amount", discount);
            if (response.errorOccurred()) return response;
            Pair<Double, Double> discount2 = agreement.getTotalOrderDiscountPerOrderPrice();
            if (discount2 != null)
                response = discountDAO.addDiscount(supplierID, "Price", discount2);
            if (response.errorOccurred()) return response;
            response = addSupplierProducts(supplierID, agreement.getSupllyingProducts());
            if (response.errorOccurred()) return response;
            response = addDiscountOnProducts(supplierID, agreement.getSupllyingProducts());
            if (response.errorOccurred()) return response;

//            response = discountDAO.addDiscount(supplierID, "Amount", agreement.getTotalDiscountInPrecentageForOrderAmount());
//            if(response.errorOccurred()) return response;
//            response = discountDAO.addDiscount(supplierID, "Price", agreement.getTotalDiscountInPrecentageForOrder());
//            if(response.errorOccurred()) return response;
//            for(SupplierProduct supplierProduct : agreement.getSupllyingProducts().values())
//            {
//                response = supplierProductDAO.addSupplierProduct(supplierID, supplierProduct);
//                if(response.errorOccurred()) return response;
//            }

            agreementIM.put(supplierID, agreement);
            return new Response(supplierID);
        } catch (SQLException e) { return new Response(e.getMessage()); }
    }

    @Override
    public Response removeAgreement(int supplierID) {
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM agreement WHERE supplierID = ?"))
        {
            statement.setInt(1, supplierID);
            statement.executeUpdate();
            agreementIM.remove(supplierID);
            return new Response(supplierID);
        } catch (SQLException e) { return new Response(e.getMessage()); }
    }

    @Override
    public Response updateAgreement(int supplierID, String paymentType, boolean selfSupply, String supplyMethod, int supplyTime) {
        try (PreparedStatement statement = connection.prepareStatement("UPDATE agreement SET paymentType = ?, selfSupply = ?, supplyMethod = ?, supplyTime = ? WHERE supplierID = ?")) {
            statement.setString(1, paymentType);
            statement.setBoolean(2, selfSupply);
            statement.setString(3, supplyMethod);
            statement.setInt(4, supplyTime);
            statement.setInt(5, supplierID);
            statement.executeUpdate();
            if (agreementIM.containsKey(supplierID))
            {
                Agreement agreement = agreementIM.get(supplierID);
                agreement.setPaymentType(paymentType);
                agreement.setSelfSupply(selfSupply);
                agreement.setSupplyMethod(supplyMethod);
                agreement.setSupplyTime(supplyTime);
            }
            return new Response(supplierID);
        } catch (SQLException e) { return new Response(e.getMessage()); }
    }

    @Override
    public Response updatePaymentType(int supplierID, String paymentType) {
        try (PreparedStatement statement = connection.prepareStatement("UPDATE agreement SET paymentType = ? WHERE supplierID = ?"))
        {
            statement.setString(1, paymentType);
            statement.setInt(2, supplierID);
            statement.executeUpdate();
            if (agreementIM.containsKey(supplierID)) agreementIM.get(supplierID).setPaymentType(paymentType);
            return new Response(supplierID);
        } catch (SQLException e) { return new Response(e.getMessage()); }
    }

    @Override
    public Response updateSelfSupply(int supplierID, boolean selfSupply) {
        try (PreparedStatement statement = connection.prepareStatement("UPDATE agreement SET selfSupply = ? WHERE supplierID = ?"))
        {
            statement.setBoolean(1, selfSupply);
            statement.setInt(2, supplierID);
            statement.executeUpdate();
            if (agreementIM.containsKey(supplierID)) agreementIM.get(supplierID).setSelfSupply(selfSupply);
            return new Response(supplierID);
        } catch (SQLException e) { return new Response(e.getMessage()); }
    }

    @Override
    public Response updateSupplyMethod(int supplierID, String supplyMethod) {
        try (PreparedStatement statement = connection.prepareStatement("UPDATE agreement SET supplyMethod = ? WHERE supplierID = ?"))
        {
            statement.setString(1, supplyMethod);
            statement.setInt(2, supplierID);
            statement.executeUpdate();
            if (agreementIM.containsKey(supplierID)) agreementIM.get(supplierID).setSupplyMethod(supplyMethod);
            return new Response(supplierID);
        } catch (SQLException e) { return new Response(e.getMessage()); }
    }

    @Override
    public Response updateSupplyTime(int supplierID, int supplyTime) {
        try (PreparedStatement statement = connection.prepareStatement("UPDATE agreement SET supplyTime = ? WHERE supplierID = ?"))
        {
            statement.setInt(1, supplyTime);
            statement.setInt(2, supplierID);
            statement.executeUpdate();
            if (agreementIM.containsKey(supplierID)) agreementIM.get(supplierID).setSupplyTime(supplyTime);
            return new Response(supplierID);
        } catch (SQLException e) { return new Response(e.getMessage()); }
    }



    public Response addSupplierProducts(int supplierID, Map<Integer, SupplierProduct> supllyingProducts)
    {
        for(SupplierProduct supplierProduct : supllyingProducts.values())
        {
            Response res = supplierProductDAO.addSupplierProduct(supplierID, supplierProduct);
            if(res.errorOccurred()) return res;
        }
        return new Response(supplierID);
    }
    public Response addDiscountOnProducts(int supplierID, Map <Integer, SupplierProduct> supllyingProducts)
    {
        for(SupplierProduct supplierProduct : supllyingProducts.values())
        {
            for(Map.Entry<Integer, Double> discount : supplierProduct.getDiscountPerAmount().entrySet()) {
                discountPerAmountDAO.addDiscount(supplierID, supplierProduct.getProductID(), discount.getKey(), discount.getValue());
            }
        }
        return new Response(supplierID);
    }
}
