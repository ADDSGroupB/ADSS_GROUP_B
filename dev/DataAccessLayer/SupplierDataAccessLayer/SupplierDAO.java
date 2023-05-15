package DataAccessLayer.SupplierDataAccessLayer;

import BusinessLayer.SupplierBusinessLayer.Agreement;
import BusinessLayer.SupplierBusinessLayer.Contact;
import BusinessLayer.SupplierBusinessLayer.Supplier;
import BusinessLayer.SupplierBusinessLayer.SupplierProduct;
import DataAccessLayer.SupplierDataAccessLayer.Interfaces.*;
import DataAccessLayer.SupplierDataAccessLayer.Interfaces.*;
import Utillity.Pair;
import Utillity.Response;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SupplierDAO implements iSupplierDAO {
    private final Connection connection;
    private final HashMap<Integer, Supplier> suppliersIM;
    private final iContactDAO contactDAO;
    private final iDiscountDAO discountDAO;
    private final iDiscountPerAmountDAO discountPerAmountDAO;
    private final iSupplierProductDAO supplierProductDAO;
    private final iAgreementDAO agreementDAO;
//    private int lastSupplierID;
    public SupplierDAO() {
        connection = Database.connect();
        try {
            Statement statement = connection.createStatement();
            statement.execute("PRAGMA foreign_keys=ON;");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        suppliersIM = new HashMap<>();
        contactDAO = new ContactDAO();
        discountDAO = new DiscountDAO();
        discountPerAmountDAO = new DiscountPerAmountDAO();
        supplierProductDAO = new SupplierProductDAO();
        agreementDAO = new AgreementDAO();
//        lastSupplierID = getLastSupplierID();
    }
    @Override
    public HashMap<Integer, Supplier> getAllSuppliers() {
//        HashMap<Integer, Supplier> suppliers = new HashMap<>();

        try (Statement stmt = connection.createStatement())
        {
            ResultSet supplierResult = stmt.executeQuery("SELECT * FROM supplier");
            while (supplierResult.next())
            {
                int id = supplierResult.getInt("supplierID");
                if(suppliersIM.containsKey(id)) continue;
                String name = supplierResult.getString("name");
                String address = supplierResult.getString("address");
//                ArrayList<Contact> contacts = getContacts(id);
                ArrayList<Contact> contacts = contactDAO.getContactsBySupplierID(id);
                String bankAccount = supplierResult.getString("bankAccount");
                Agreement agreement = agreementDAO.getAgreementByID(id);
                Supplier supplier = new Supplier(id, name, address, contacts, bankAccount, agreement);
                suppliersIM.put(id, supplier);
            }
            return suppliersIM;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public Supplier getSupplierByID(int id) {
        if(suppliersIM.containsKey(id)) return suppliersIM.get(id);
        try (PreparedStatement supplierStatement = connection.prepareStatement("SELECT * FROM supplier WHERE supplierID = ?")) {
            supplierStatement.setInt(1, id);
            ResultSet supplierResult = supplierStatement.executeQuery();
            if (supplierResult.next())
            {
                String name = supplierResult.getString("name");
                String address = supplierResult.getString("address");
                ArrayList<Contact> contacts = contactDAO.getContactsBySupplierID(id);
                String bankAccount = supplierResult.getString("bankAccount");
                Agreement agreement = agreementDAO.getAgreementByID(id);
                Supplier supplier = new Supplier(id, name, address, contacts, bankAccount, agreement);
                suppliersIM.put(id, supplier);
                return supplier;
            }
        } catch (SQLException e) { System.out.println(e.getMessage()); }
        return null;
    }
    @Override
    public Response addSupplier(Supplier supplier) {
        try (PreparedStatement supplierStatement = connection.prepareStatement("INSERT INTO supplier (supplierID, name, address, bankAccount) VALUES (?, ?, ?, ?)"))
        {
            supplierStatement.setInt(1, supplier.getSupplierId());
            supplierStatement.setString(2, supplier.getName());
            supplierStatement.setString(3, supplier.getAddress());
            supplierStatement.setString(4, supplier.getBankAccount());
            supplierStatement.executeUpdate();
            Response response;
            for(Contact contact : supplier.getContacts())
            {
                response = contactDAO.addContact(supplier.getSupplierId(), contact);
                if(response.errorOccurred()) return response;
            }
            if(supplier.getAgreement() != null)
            {
                response = agreementDAO.addAgreement(supplier.getSupplierId(), supplier.getAgreement());
                if (response.errorOccurred()) return response;
                Pair<Integer, Double> discount = supplier.getTotalDiscountInPrecentageForOrder();
                if (supplier.getTotalDiscountInPrecentageForOrder() != null)
                    response = discountDAO.addDiscount(supplier.getSupplierId(), "Amount", discount);
                if (response.errorOccurred()) return response;
                Pair<Double, Double> discount2 = supplier.getTotalOrderDiscountPerOrderPrice();
                if (supplier.getTotalDiscountInPrecentageForOrder() != null)
                    response = discountDAO.addDiscount(supplier.getSupplierId(), "Price", discount2);
                if (response.errorOccurred()) return response;
                response = addSupplierProducts(supplier.getSupplierId(), supplier.getSupplyingProducts());
                if (response.errorOccurred()) return response;
                response = addDiscountOnProducts(supplier.getSupplierId(), supplier.getSupplyingProducts());
                if (response.errorOccurred()) return response;
                suppliersIM.put(supplier.getSupplierId(), supplier);
            }
            return new Response(supplier.getSupplierId());
        } catch (SQLException e) {
            return new Response(e.getMessage());
        }
    }

    public Response addSupplierProducts(int supplierID, Map <Integer, SupplierProduct> supllyingProducts)
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


    @Override
    public Response removeSupplier(int id) {
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM supplier WHERE supplierID = ?"))
        {
            statement.setInt(1, id);
            statement.executeUpdate();
            suppliersIM.remove(id);
            return new Response(id);
        } catch (SQLException e) { return new Response(e.getMessage()); }
    }

    @Override
    public Response updateSupplierName(int id, String name) {
        try (PreparedStatement statement = connection.prepareStatement("UPDATE supplier SET name = ? WHERE supplierID = ?"))
        {
            statement.setString(1, name);
            statement.setInt(2, id);
            statement.executeUpdate();
            if(suppliersIM.containsKey(id)) suppliersIM.get(id).setName(name);
            return new Response(id);
        } catch (SQLException e) { return new Response(e.getMessage()); }
    }

    @Override
    public Response updateSupplierAddress(int id, String address) {
        try (PreparedStatement statement = connection.prepareStatement("UPDATE supplier SET address = ? WHERE supplierID = ?"))
        {
            statement.setString(1, address);
            statement.setInt(2, id);
            statement.executeUpdate();
            if(suppliersIM.containsKey(id)) suppliersIM.get(id).setAddress(address);
            return new Response(id);
        } catch (SQLException e) { return new Response(e.getMessage()); }
    }

    @Override
    public Response updateSupplierBankAccount(int id, String bankAccount) {
        try (PreparedStatement statement = connection.prepareStatement("UPDATE supplier SET bankAccount = ? WHERE supplierID = ?"))
        {
            statement.setString(1, bankAccount);
            statement.setInt(2, id);
            statement.executeUpdate();
            statement.close();
            if(suppliersIM.containsKey(id)) suppliersIM.get(id).setBankAccount(bankAccount);
            return new Response(id);
        } catch (SQLException e) { return new Response(e.getMessage()); }
    }

    public int getLastSupplierID()
    {
//        if(lastSupplierID != 0) return lastSupplierID;
        try (Statement statement = connection.createStatement()) {
            String sql = "SELECT MAX(supplierID) AS maxSupplierID FROM supplier";
            ResultSet rs = statement.executeQuery(sql);
            int maxSupplierID = 0;
            if (rs.next()) maxSupplierID = rs.getInt("maxSupplierID");
            rs.close();
            return maxSupplierID;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }

    public Response searchBankAccount(String bankAccount)
    {
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM supplier WHERE bankAccount = ?"))
        {
            statement.setString(1, bankAccount);
            ResultSet supplierResult = statement.executeQuery();
            return new Response(supplierResult.next());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return new Response("SQL ERROR");
    }

    @Override
    public void printAllSuppliers()
    {
        try (Statement stmt = connection.createStatement())
        {
            ResultSet resultSet = stmt.executeQuery("SELECT name, supplierID FROM supplier");
            while (resultSet.next())
            {
                String name = resultSet.getString("name");
                int supplierID = resultSet.getInt("supplierID");
                System.out.println("Supplier's name: " + name + ", supplier's id: " + supplierID + ", supplier's products:" );
                supplierProductDAO.printProductsBySupplierID(supplierID);
            }
            resultSet.close();
            } catch (SQLException e) { System.out.println(e.getMessage());}
    }

    public Response updateSupplierProductAmount(int supplierID, int productID, int amount)
    {
        if(suppliersIM.containsKey(supplierID))
            suppliersIM.get(supplierID).getSupplyingProducts().get(productID).setAmount(amount);
        return supplierProductDAO.updateSupplierProductAmount(supplierID, productID, amount);
    }
}
