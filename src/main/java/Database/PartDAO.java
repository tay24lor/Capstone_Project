package Database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.InHousePart;
import model.OutSourcedPart;
import model.Part;
import model.Product;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PartDAO {
    private static final ObservableList<Part> parts = FXCollections.observableArrayList();

    public static void setParts() {
        parts.clear();
        try {
            Statement stmt = SQLite.conn.createStatement();
            String query = "SELECT id, " +
                    "              name, " +
                    "              price, " +
                    "              stock, " +
                    "              min, " +
                    "              max, " +
                    "              machineCode, " +
                    "              companyName, " +
                    "              productID," +
                    "              date_created" +
                    "       FROM   parts;";
            ResultSet result = stmt.executeQuery(query);
            while (result.next()) {
                int id = result.getInt("id");
                String name = result.getString("name");
                double price = result.getDouble("price");
                int stock = result.getInt("stock");
                int min = result.getInt("min");
                int max = result.getInt("max");
                int machCode = result.getInt("machineCode");
                String companyName = result.getString("companyName");
                int productID = result.getInt("productID");
                String dateCreated = result.getString("date_created");

                if (!companyName.isEmpty()) {
                    OutSourcedPart osPart = new OutSourcedPart(id, name, price, stock, min, max, productID);
                    osPart.setCompanyName(companyName);
                    osPart.setDate(dateCreated);
                    parts.add(osPart);

                } else if (machCode > 0) {
                    InHousePart ihPart = new InHousePart(id, name, price, stock, min, max, productID);
                    ihPart.setMachineCode(machCode);
                    ihPart.setDate(dateCreated);
                    parts.add(ihPart);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static ObservableList<Part> returnAllParts() {
        return parts;
    }


    public static ObservableList<Part> getParts() {
        ObservableList<Part> freeParts = FXCollections.observableArrayList();

        for (Part part : parts) {
            if (part.getProdID() == -1) {
                freeParts.add(part);
            }
        }
        return freeParts;
    }

    public static ObservableList<Part> getAsscParts(Product product) {
        ObservableList<Part> asscParts = FXCollections.observableArrayList();

        for (Part part : parts) {
            if (part.getProdID() == product.getId()) {
                asscParts.add(part);
            }
        }
        return asscParts;
    }

    public static ObservableList<Part> getInHouseParts() {
        ObservableList<Part> inHouseParts = FXCollections.observableArrayList();

        try {
            Statement stmt = SQLite.conn.createStatement();
            String query = "SELECT id, " +
                    "              name, " +
                    "              price, " +
                    "              stock, " +
                    "              min, " +
                    "              max, " +
                    "              machineCode, " +
                    "              companyName, " +
                    "              productID," +
                    "              date_created" +
                    "       FROM   parts" +
                    "       WHERE machineCode > 0;";
            ResultSet result = stmt.executeQuery(query);
            while (result.next()) {
                int id = result.getInt("id");
                String name = result.getString("name");
                double price = result.getDouble("price");
                int stock = result.getInt("stock");
                int min = result.getInt("min");
                int max = result.getInt("max");
                int machCode = result.getInt("machineCode");
                int productID = result.getInt("productID");
                String dateCreated = result.getString("date_created");


                InHousePart ihPart = new InHousePart(id, name, price, stock, min, max, productID);
                ihPart.setMachineCode(machCode);
                ihPart.setDate(dateCreated);
                inHouseParts.add(ihPart);

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return inHouseParts;
    }
    public static ObservableList<Part> getOutSourcedParts() {
        ObservableList<Part> outSourcedParts = FXCollections.observableArrayList();

        try {
            Statement stmt = SQLite.conn.createStatement();
            String query = "SELECT id, " +
                    "              name, " +
                    "              price, " +
                    "              stock, " +
                    "              min, " +
                    "              max, " +
                    "              machineCode, " +
                    "              companyName, " +
                    "              productID," +
                    "              date_created" +
                    "       FROM   parts" +
                    "       WHERE machineCode < 1;";
            ResultSet result = stmt.executeQuery(query);
            while (result.next()) {
                int id = result.getInt("id");
                String name = result.getString("name");
                double price = result.getDouble("price");
                int stock = result.getInt("stock");
                int min = result.getInt("min");
                int max = result.getInt("max");
                String compName = result.getString("companyName");
                int productID = result.getInt("productID");
                String dateCreated = result.getString("date_created");


                OutSourcedPart osPart = new OutSourcedPart(id, name, price, stock, min, max, productID);
                osPart.setCompanyName(compName);
                osPart.setDate(dateCreated);
                outSourcedParts.add(osPart);

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return outSourcedParts;
    }

    public static Part lookupPart(int partID) {
        for (Part part : parts) {
            if (partID == part.getId()) {
                return part;
            }
        }
        System.out.println("NA");
        return null;
    }

    public static ObservableList<Part> lookupPart(String partName) {
        ObservableList<Part> results = FXCollections.observableArrayList();
        for (Part p : parts) {
            if (p.getName().toLowerCase().contains(partName.toLowerCase())) {
                results.add(p);
            }
        }
        return results;
    }

    public static int getLatestId() throws SQLException {
        int id;
        Statement statement = SQLite.conn.createStatement();
        String query = "SELECT max(id) FROM parts;";
        ResultSet resultSet = statement.executeQuery(query);
        id = resultSet.getInt(1);
        return id + 1;
    }

    public static void insert(Part part, int machCode, String compName) throws SQLException {
        String insertStmt = "INSERT INTO parts (id, name, price, stock, min, max, machineCode, companyName, productID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = SQLite.conn.prepareStatement(insertStmt);
        preparedStatement.setInt(1, part.getId());
        preparedStatement.setString(2, part.getName());
        preparedStatement.setDouble(3, part.getPrice());
        preparedStatement.setInt(4, part.getStock());
        preparedStatement.setInt(5, part.getMin());
        preparedStatement.setInt(6, part.getMax());
        preparedStatement.setInt(7, machCode);
        preparedStatement.setString(8, compName);
        preparedStatement.setInt(9, part.getProdID());
        preparedStatement.executeUpdate();
    }

    public static void update(Part part, int machCode, String compName, int prodID) throws SQLException {
        String updateStmt = "UPDATE parts SET id = ?, name = ?, price = ?, stock = ?, min = ?, max = ?, machineCode = ?, companyName = ?, productID = ? WHERE id = ?";
        PreparedStatement preparedStatement = SQLite.conn.prepareStatement(updateStmt);
        preparedStatement.setInt(1, part.getId());
        preparedStatement.setString(2, part.getName());
        preparedStatement.setDouble(3, part.getPrice());
        preparedStatement.setInt(4, part.getStock());
        preparedStatement.setInt(5, part.getMin());
        preparedStatement.setInt(6, part.getMax());
        preparedStatement.setInt(7, machCode);
        preparedStatement.setString(8, compName);
        preparedStatement.setInt(9, prodID);
        preparedStatement.setInt(10, part.getId());
        preparedStatement.executeUpdate();
    }

    public static void updateProdId(Part part, int prodID) throws SQLException {
        part.setProdID(prodID);
        String updateStmt = "UPDATE parts SET productID = ? WHERE id = ?";
        PreparedStatement preparedStatement = SQLite.conn.prepareStatement(updateStmt);
        preparedStatement.setInt(1, prodID);
        preparedStatement.setInt(2, part.getId());
        preparedStatement.executeUpdate();
    }
    public static void deletePart(int id) throws SQLException {
        String deleteStmt = "DELETE FROM parts WHERE id = " + id + ";";
        Statement stmt = SQLite.conn.createStatement();
        stmt.execute(deleteStmt);
    }
}
