package DataAccessLayer.TransportManager;
import BuisnessLayer.TransportManager.Product;
import DataAccessLayer.Database;

import java.sql.*;
import java.util.HashMap;


public class ProductDAO {
    private Connection conn;
    private HashMap<String, Product> allProducts;


    public ProductDAO() throws SQLException {
        allProducts = new HashMap<>();
    }

    public HashMap<String, Product> getAllProducts() throws SQLException {

        try {
            conn = Database.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM allProducts");

            while (rs.next()) {
                String product_code = rs.getString("product_code");

                if (this.allProducts.containsKey(product_code))
                    continue;

                String name = rs.getString("product_name");
                String type = rs.getString("type");
                String manufacturer = rs.getString("manufacturer");
                String category = rs.getString("category");
                String sub_category = rs.getString("sub_category");
                double size = rs.getDouble("size");

                Product product = new Product(product_code, name, type, manufacturer, category, sub_category, size);

                this.allProducts.put(product_code, product);

            }
            Database.closeConnection(conn);
            return this.allProducts;
        }
        catch (SQLException e){
            System.out.println("Exception has occurred while getting all products: " + e.getMessage());
            System.out.println(e.getMessage());
            Database.closeConnection(conn);
        }
        return null;
    }

    public Product getProductByCode(String code) throws SQLException {

        if (this.allProducts.containsKey(code)) {
            return allProducts.get(code);
        }

        try {
            conn = Database.connect();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM allProducts WHERE product_code = ?");
            stmt.setString(1, code);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {

                String product_code = rs.getString("product_code");


                String name = rs.getString("product_name");
                String type = rs.getString("type");
                String manufacturer = rs.getString("manufacturer");
                String category = rs.getString("category");
                String sub_category = rs.getString("sub_category");
                double size = rs.getDouble("size");

                Product product = new Product(product_code, name, type, manufacturer, category, sub_category, size);

                this.allProducts.put(product_code, product);
                Database.closeConnection(conn);
                return product;
            } else {
                Database.closeConnection(conn);
                return null;
            }
        }
        catch (SQLException e){
            System.out.println("Exception has occurred while getting a product by code: " + e.getMessage());
            System.out.println(e.getMessage());
            Database.closeConnection(conn);
        }
        return null;
    }

    public void deleteProduct (String code) throws SQLException {

        try {
            conn = Database.connect();
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM allProducts WHERE product_code = code");
            stmt.executeUpdate();
            Database.closeConnection(conn);
            allProducts.remove(code);
        } catch (SQLException e) {
            System.out.println("Exception has occurred while deleting a product: " + e.getMessage());
            System.out.println(e.getMessage());
            Database.closeConnection(conn);
        }
    }

    public void updateProduct (Product product) throws SQLException {

        try{
            conn = Database.connect();

            PreparedStatement stmt = conn.prepareStatement("UPDATE allProducts SET product_name = ?, type = ?, manufacturer = ?, category = ?, sub_category = ?, size = ? WHERE product_code = ?");
            stmt.setString(1, product.getName());
            stmt.setString(2, product.getType());
            stmt.setString(3, product.getManufacturer());
            stmt.setString(4, product.getCategory());
            stmt.setDouble(5, product.getSize());
            stmt.setString(6, product.getProduct_code());
            stmt.executeUpdate();
            allProducts.put(product.getProduct_code(), product);
            Database.closeConnection(conn);
        }
        catch (SQLException e){
            System.out.println("Exception has occurred while updating a product: " + e.getMessage());
            System.out.println(e.getMessage());
            Database.closeConnection(conn);
        }
    }

    public void insertProduct(Product product) throws SQLException {

        try {
            conn = Database.connect();
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO allProducts (product_code, product_name, type, manufacturer, category, sub_category, size) VALUES (?, ?, ?, ?, ?, ?, ?)");

            stmt.setString(1, product.getProduct_code());
            stmt.setString(2, product.getName());
            stmt.setString(3, product.getType());
            stmt.setString(4, product.getManufacturer());
            stmt.setString(5, product.getCategory());
            stmt.setString(6, product.getSub_category());
            stmt.setDouble(7, product.getSize());
            stmt.executeUpdate();
            allProducts.put(product.getProduct_code(), product);
            Database.closeConnection(conn);
        }
        catch (SQLException e){
            System.out.println("Exception has occurred while inserting a product: " + e.getMessage());
            System.out.println(e.getMessage());
            Database.closeConnection(conn);
        }
    }
    public void deleteContent_products () throws SQLException{
        Statement stmt = null;
        try {
            conn = Database.connect();
            stmt = conn.createStatement();
            stmt.executeUpdate("DELETE FROM allProducts");
            Database.closeConnection(conn);
            allProducts.clear();
        }
        catch (SQLException e){
            System.out.println("Exception has occurred while deleting table allProducts: " + e.getMessage());
            System.out.println(e.getMessage());
            Database.closeConnection(conn);
        }
    }
}
