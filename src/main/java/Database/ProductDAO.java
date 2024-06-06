package Database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Product;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ProductDAO {

    private static final ObservableList<Product> products = FXCollections.observableArrayList();

    public static void setProducts() {
        try {
            Statement stmt = SQLite.conn.createStatement();
            String query = "SELECT id, " +
                    "              name, " +
                    "              price, " +
                    "              stock, " +
                    "              min, " +
                    "              max, " +
                    "              date_created" +
                    "       FROM   products;";
            ResultSet result = stmt.executeQuery(query);
            while (result.next()) {
                int id = result.getInt("id");
                String name = result.getString("name");
                double price = result.getDouble("price");
                int stock = result.getInt("stock");
                int min = result.getInt("min");
                int max = result.getInt("max");
                String dateCreated = result.getString("date_created");

                if (products.size() != getProductsSize()) {
                    Product product = new Product(id, name, price, stock, min, max, dateCreated);
                    products.add(product);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static ObservableList<Product> getProducts() { return products; }

    /**
     * Search for products by ID
     * @param productID search parameter
     * @return matching product
     */
    public static Product lookupProduct(int productID) {
        for (Product product : products) {
            if (productID == product.getId()) {
                return product;
            }
        }
        System.out.println("NA");
        return null;
    }

    /**
     * Search for products by name
     * @param productName search parameter
     * @return list of matching products
     */
    public static ObservableList<Product> lookupProduct(String productName) {
        ObservableList<Product> results = FXCollections.observableArrayList();
        for (Product p : products) {
            if (p.getName().toLowerCase().contains(productName.toLowerCase())) {
                results.add(p);
            }
        }
        return results;
    }
    public static int getLatestId() throws SQLException {
        int id;
        Statement statement = SQLite.conn.createStatement();
        String query = "SELECT max(id) FROM products;";
        ResultSet resultSet = statement.executeQuery(query);
        id = resultSet.getInt(1);
        return id + 1;
    }

    public static int getProductsSize() throws SQLException {
        String countQuery = "SELECT COUNT(id) FROM products;";
        Statement stmt = SQLite.conn.createStatement();
        ResultSet resultSet = stmt.executeQuery(countQuery);
        return resultSet.getInt(1);
    }

    /**
     * Insert product into database
     * @param product product to be added
     * @throws SQLException catches SQL errors
     */
    public static void insert(Product product) throws SQLException {
        String insertStmt = "INSERT INTO products (id, name, price, stock, min, max) VALUES (?, ?, ?, ?, ?, ?);";
        PreparedStatement preparedStatement = SQLite.conn.prepareStatement(insertStmt);
        preparedStatement.setInt(1, product.getId());
        preparedStatement.setString(2, product.getName());
        preparedStatement.setDouble(3, product.getPrice());
        preparedStatement.setInt(4, product.getStock());
        preparedStatement.setInt(5, product.getMin());
        preparedStatement.setInt(6, product.getMax());
        preparedStatement.executeUpdate();
    }

    public static void update(Product product) throws SQLException {
        String updateStmt = "UPDATE products SET name = ?, price = ?, stock = ?, min = ?, max = ? WHERE id = ?";
        PreparedStatement preparedStatement = SQLite.conn.prepareStatement(updateStmt);
        preparedStatement.setString(1, product.getName());
        preparedStatement.setDouble(2, product.getPrice());
        preparedStatement.setInt(3, product.getStock());
        preparedStatement.setInt(4, product.getMin());
        preparedStatement.setInt(5, product.getMax());
        preparedStatement.setInt(6, product.getId());
        preparedStatement.executeUpdate();
    }
    public static void delete(int id) throws SQLException {
        String deleteStmt = "DELETE FROM products WHERE id = " + id + ";";
        Statement stmt = SQLite.conn.createStatement();
        stmt.execute(deleteStmt);
    }
}
