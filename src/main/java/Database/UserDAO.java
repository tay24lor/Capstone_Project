package Database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

public class UserDAO {

    public static boolean check(String name, String pass) throws SQLException {
        Statement stmt = SQLite.conn.createStatement();
        String query = "SELECT * FROM users;";
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            if (Objects.equals(name, rs.getString("name"))) {
                return (Objects.equals(pass, rs.getString("password")));
            }
        }
        return false;
    }

    public static void insert(String name, String pass) throws SQLException {
        String query = "INSERT INTO users (name, password) VALUES (?, ?);";
        PreparedStatement stmt = SQLite.conn.prepareStatement(query);
        stmt.setString(1, name);
        stmt.setString(2, pass);
        stmt.executeUpdate();
    }
    public static ObservableList<User> getUsers() throws SQLException {
        ObservableList<User> users = FXCollections.observableArrayList();
        Statement statement = SQLite.conn.createStatement();
        String query = "SELECT name, password FROM users";

        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
            User user = new User(resultSet.getString(1), resultSet.getString(2));
            users.add(user);
        }
        return users;
    }
    public static void delete(User user) throws SQLException {
        String deleteStmt = "DELETE FROM users WHERE name = " + "'" + user.getName() + "'";
        Statement stmt = SQLite.conn.createStatement();
        stmt.execute(deleteStmt);
    }
}
