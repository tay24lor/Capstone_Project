package Database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.InHousePart;
import model.OutSourcedPart;
import model.Part;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PartDAO {
    private static final ObservableList<Part> parts = FXCollections.observableArrayList();


    public static void setParts() {
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
                    "              productID" +
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

                if (!companyName.isEmpty()) {
                    OutSourcedPart osPart = new OutSourcedPart(id, name, price, stock, min, max);
                    osPart.setCompanyName(companyName);
                    parts.add(osPart);

                } else if (machCode > 0) {
                    InHousePart ihPart = new InHousePart(id, name, price, stock, min, max);
                    ihPart.setMachineCode(machCode);
                    parts.add(ihPart);

                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static ObservableList<Part> getParts() { return parts; }

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

    public static void insert(Part part, int machCode, String compName, int prodID) throws SQLException {
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
        preparedStatement.setInt(9, prodID);
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

    public static void updateStock(int id, int newAmount) throws SQLException {
        String updateStmt = "UPDATE parts SET stock = ? WHERE id = ?";
        PreparedStatement preparedStatement = SQLite.conn.prepareStatement(updateStmt);
        preparedStatement.setInt(1, newAmount);
        preparedStatement.setInt(2, id);
        preparedStatement.executeUpdate();
    }
    public static void deletePart(int id) throws SQLException {
        String deleteStmt = "DELETE FROM parts WHERE id = " + id + ";";
        Statement stmt = SQLite.conn.createStatement();
        stmt.execute(deleteStmt);
    }


}
