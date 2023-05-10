package DataAccessLayer;

import BusinessLayer.Agreement;
import BusinessLayer.Contact;
import BusinessLayer.Supplier;
import BusinessLayer.SupplierProduct;
import Utillity.Pair;
import Utillity.Response;

import java.sql.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SupplierDAO implements iSupplierDAO {
    private Connection connection;
    private HashMap<Integer, Supplier> suppliersIM;
    public SupplierDAO() {
        connection = Database.connect();
//        Database.createTables();
        suppliersIM = new HashMap<>();
    }
    ///========================================Test===========================================///
    public static void main(String[] args) {

        SupplierDAO supplierDAO = new SupplierDAO();
//        Database.testDAO();
//        Supplier supplier1 = supplierDAO.getSupplierByID(1);
        supplierDAO.getAllSuppliers();
        supplierDAO.getAllSuppliers();
        supplierDAO.printSuppliers();
    }
    public void printSuppliers() {
        if(!suppliersIM.isEmpty()){
            for (Map.Entry<Integer, Supplier> entry : suppliersIM.entrySet()) {
                Integer supplierId = entry.getKey();
                Supplier supplier = entry.getValue();

                System.out.println("Supplier's name: " + supplier.getName() + ", supplier's id: " + supplierId + ", supplier's products:" );
                printSupplyingProducts(supplier.getSupplyingProducts());
                System.out.println("\n");
            }
        }
        else{
            System.out.println("Please add a supplier before choosing this option");
        }
    }

    public void printSupplyingProducts(Map<Integer, SupplierProduct> sp) {
        for (Map.Entry<Integer, SupplierProduct> entry : sp.entrySet()) {
            Integer productId = entry.getKey();
            SupplierProduct supplierProduct = entry.getValue();
            Integer catalogId = supplierProduct.getCatalogId();
            int amount = supplierProduct.getAmount();
            System.out.println("productId: " + productId + ", catalogId: " + catalogId + ", amount: " + amount);
        }
    }
    ///=======================================================================================///
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
                ArrayList<Contact> contacts = getContacts(id);
                String bankAccount = supplierResult.getString("bankAccount");
                Agreement agreement = getAgreementByID(id);
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
                ArrayList<Contact> contacts = getContacts(id);
                String bankAccount = supplierResult.getString("bankAccount");
                Agreement agreement = getAgreementByID(id);
                Supplier supplier = new Supplier(id, name, address, contacts, bankAccount, agreement);
                suppliersIM.put(id, supplier);
                return supplier;
            }
        } catch (SQLException e) { System.out.println(e.getMessage()); }
        return null;
    }

    public ArrayList<Contact> getContacts(int id){
        try (PreparedStatement contactStatement = connection.prepareStatement("SELECT * FROM contact WHERE supplierID = ?")) {
            contactStatement.setInt(1, id);
            ResultSet discountResult = contactStatement.executeQuery();
            ArrayList<Contact> contacts = new ArrayList<>();
            while (discountResult.next())
            {
                String name = discountResult.getString("name");
                String email = discountResult.getString("email");
                String phoneNumber = discountResult.getString("phoneNumber");
                contacts.add(new Contact(name, email, phoneNumber));
            }
            return contacts;
        } catch (SQLException e) { System.out.println(e.getMessage()); }
        return null;
    }
    public Agreement getAgreementByID(int id){
        try (PreparedStatement agreementStatement = connection.prepareStatement("SELECT * FROM agreement WHERE supplierID = ?")) {
            agreementStatement.setInt(1, id);
            ResultSet agreementResult = agreementStatement.executeQuery();
            if (agreementResult.next())
            {
                String paymentType = agreementResult.getString("paymentType");
                boolean selfSupply = agreementResult.getBoolean("selfSupply");
                String supplyMethod = agreementResult.getString("supplyMethod");
                int supplyTime = agreementResult.getInt("supplyTime");
                ArrayList<DayOfWeek> supplyDays = getDaysFromString(agreementResult.getString("deliveryDays"));
                Agreement agreement = new Agreement(paymentType, selfSupply, supplyMethod, supplyTime, supplyDays);
                agreement.setTotalDiscountInPrecentageForOrderAmount(getAmountDiscountByID(id));
                agreement.setTotalOrderDiscountPerOrderPrice(getPriceDiscountByID(id));
                agreement.setSupllyingProducts(getSupplierProductsByID(id));
                return agreement;
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static ArrayList<DayOfWeek> getDaysFromString(String days)
    {
        ArrayList<DayOfWeek> daysOfWeek= new ArrayList<>();
        String[] daysArray = days.split(", ");
        for (String day : daysArray) {
            if (day != "None") return null;
            daysOfWeek.add(DayOfWeek.valueOf(day.toUpperCase()));
        }
        return daysOfWeek;
    }

    private static String getStringFromDays(ArrayList<DayOfWeek> daysOfWeek) {
        if (daysOfWeek == null || daysOfWeek.isEmpty()) {
            return "None";
        }
        StringBuilder sb = new StringBuilder();
        for (DayOfWeek day : daysOfWeek) {
            sb.append(day.toString()).append(", ");
        }
        return sb.substring(0, sb.length() - 2);
    }

    public Pair<Integer, Double> getAmountDiscountByID(int id) {
        try (PreparedStatement discountStatement = connection.prepareStatement("SELECT * FROM discount WHERE supplierID = ?")) {
            discountStatement.setInt(1, id);
            ResultSet discountResult = discountStatement.executeQuery();
            while (discountResult.next())
            {
                if(Objects.equals(discountResult.getString("type"), "Amount"))
                {
                    int amount = (int)discountResult.getDouble("amount");
                    double discount = discountResult.getDouble("discount");
                    return new Pair<>(amount, discount);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public Pair<Double, Double> getPriceDiscountByID(int id) {
        try (PreparedStatement discountStatement = connection.prepareStatement("SELECT * FROM discount WHERE supplierID = ?")) {
            discountStatement.setInt(1, id);
            ResultSet discountResult = discountStatement.executeQuery();
            while (discountResult.next())
            {
                if(Objects.equals(discountResult.getString("type"), "Price")) // There is "Amount" or "Price"
                {
                    double amount = discountResult.getDouble("amount");
                    double discount = discountResult.getDouble("discount");
                    return new Pair<>(amount, discount);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    HashMap<Integer, SupplierProduct> getSupplierProductsByID(int supplierId)
    {
        try (PreparedStatement sProductStatement = connection.prepareStatement("SELECT * FROM supplierProduct WHERE supplierID = ?")) {
            sProductStatement.setInt(1, supplierId);
            ResultSet supplierResult = sProductStatement.executeQuery();
            HashMap<Integer, SupplierProduct> SupplierProducts = new HashMap<>();
            while (supplierResult.next())
            {
                String name = supplierResult.getString("name");
                int catalogID = supplierResult.getInt("catalogNumber");
                int productID = supplierResult.getInt("productID");
                double price = supplierResult.getDouble("price");
                String manufacturer = supplierResult.getString("manufacturer");
                int expirationDays = supplierResult.getInt("expirationDays");
                Double weight = supplierResult.getDouble("weight");
                HashMap<Integer, Double> discountPerAmount = getProductDiscountByID(supplierId, productID);
                int amount = supplierResult.getInt("amount");
                SupplierProducts.put(productID, new SupplierProduct(name, supplierId, catalogID, productID, price, amount, discountPerAmount, manufacturer, expirationDays, weight));
            }
            return SupplierProducts;
        } catch (SQLException e) { System.out.println(e.getMessage()); }
        return null;
    }

    public static LocalDate stringToLocalDate(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate date = LocalDate.parse(dateString, formatter);
        return date;
    }

    public HashMap<Integer, Double> getProductDiscountByID(int supplierID, int productID) {
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
            return discountPerAmount;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
            Response response = addContacts(supplier.getSupplierId(), supplier.getContacts());
            if(response.errorOccurred()) return response;
            response = addAgreement(supplier.getSupplierId(), supplier.getAgreement());
            if(response.errorOccurred()) return response;
            Pair<Integer, Double> discount = supplier.getTotalDiscountInPrecentageForOrder();
            if(supplier.getTotalDiscountInPrecentageForOrder() != null) response = addDiscount(supplier.getSupplierId(), "Amount", discount);
            if(response.errorOccurred()) return response;
            Pair<Double, Double> discount2 = supplier.getTotalOrderDiscountPerOrderPrice();
            if(supplier.getTotalDiscountInPrecentageForOrder() != null) response = addDiscount(supplier.getSupplierId(), "Price", discount2);
            if(response.errorOccurred()) return response;
            response = addSupplierProducts(supplier.getSupplierId(), supplier.getSupplyingProducts());
            if(response.errorOccurred()) return response;
            response = addDiscountOnProducts(supplier.getSupplierId(), supplier.getSupplyingProducts());
            if(response.errorOccurred()) return response;
            return new Response(supplier.getSupplierId());
        } catch (SQLException e) {
            return new Response(e.getMessage());
        }
    }

    public Response addContacts(int supplierID, ArrayList<Contact> contacts)
    {
        try (PreparedStatement contactStatement = connection.prepareStatement("INSERT INTO contact (supplierID, phoneNumber, name, email) VALUES (?, ?, ?, ?)"))
        {
            for (Contact contact : contacts)
            {
                contactStatement.setInt(1, supplierID);
                contactStatement.setString(2, contact.getPhoneNumber());
                contactStatement.setString(3, contact.getName());
                contactStatement.setString(4, contact.getEmail());
                contactStatement.executeUpdate();
            }
        } catch (SQLException e) { return new Response(e.getMessage()); }
        return new Response(supplierID);
    }
    public Response addAgreement(int supplierID, Agreement agreement)
    {
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO agreement (supplierID, paymentType, deliveryDays, selfSupply, supplyMethod, supplyTime) VALUES (?, ?, ?, ?, ?, ?)"))
        {
            statement.setInt(1, supplierID);
            statement.setString(2, agreement.getPaymentType());
            statement.setString(3, getStringFromDays(agreement.getSupplyDays()));
            statement.setBoolean(4, agreement.getSelfSupply());
            statement.setString(5, agreement.getSupplyMethod());
            statement.setInt(6, agreement.getSupplyTime());
            statement.executeUpdate();
            return new Response(supplierID);
        } catch (SQLException e) { return new Response(e.getMessage()); }
    }
    public Response addDiscount(int supplierID, String type, Pair discount)
    {
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO discount (supplierID, type, amount, discount) VALUES (?, ?, ?, ?)"))
        {
            statement.setInt(1, supplierID);
            statement.setString(2, type);
            if(discount.getFirst() instanceof Integer) statement.setDouble(3, ((Integer) discount.getFirst()).doubleValue());
            else statement.setDouble(3, (Double) discount.getFirst());
            statement.setDouble(4, (Double) discount.getSecond());
            statement.executeUpdate();
            return new Response(supplierID);
        } catch (SQLException e) { return new Response(e.getMessage()); }
    }

    public Response addSupplierProducts(int supplierID, Map <Integer, SupplierProduct> supllyingProducts)
    {
//        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO supplierProduct (supplierID, productID, catalogNumber, name, price, amount) VALUES (?, ?, ?, ?, ?, ?)"))
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO supplierProduct (supplierID, productID, catalogNumber, name, price, amount, weight, manufacturer, expirationDays) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"))
        {
            for(SupplierProduct supplierProduct : supllyingProducts.values())
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
            }
            return new Response(supplierID);
        } catch (SQLException e) { return new Response(e.getMessage()); }
    }
    public Response addDiscountOnProducts(int supplierID, Map <Integer, SupplierProduct> supllyingProducts)
    {
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO discountPerAmount (supplierID, productID, discountPerAmount, discount) VALUES (?, ?, ?, ?)"))
        {
            for(SupplierProduct supplierProduct : supllyingProducts.values())
            {
                for(Map.Entry<Integer, Double> discount : supplierProduct.getDiscountPerAmount().entrySet()) {
                    statement.setInt(1, supplierID);
                    statement.setInt(2, supplierProduct.getProductID());
                    statement.setInt(3, discount.getKey());
                    statement.setDouble(4, discount.getValue());
                    statement.executeUpdate();
                }
            }
            return new Response(supplierID);
        } catch (SQLException e) { return new Response(e.getMessage()); }
    }


    @Override
    public Response removeSupplier(int id) {
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM supplier WHERE supplierID = ?"))
        {
            statement.setInt(1, id);
            statement.executeUpdate();
//            System.out.println("Rows effected:" + statement.executeUpdate());
//            DatabaseMetaData metaData = connection.getMetaData();
//            ResultSet rs = metaData.getExportedKeys(null, null, "supplier");
//
//            while (rs.next()) {
//                String pkTable = rs.getString("PKTABLE_NAME");
//                String pkColumn = rs.getString("PKCOLUMN_NAME");
//                String fkTable = rs.getString("FKTABLE_NAME");
//                String fkColumn = rs.getString("FKCOLUMN_NAME");
//                System.out.println("Foreign key from " + fkTable + "." + fkColumn + " to " + pkTable + "." + pkColumn);
//            }
//            rs.close();
            return new Response(id);
        } catch (SQLException e) { return new Response(e.getMessage()); }
    }

    @Override
    public Response updateSupplierName(int id, String name) {
        return null;
    }

    @Override
    public Response updateSupplierAddress(int id, String address) {
        return null;
    }

    @Override
    public Response updateSupplierBankAccount(int id, String bankAccount) {
        return null;
    }
}
