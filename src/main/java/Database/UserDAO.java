package Database;

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
            if (Objects.equals(name, rs.getString("NAME"))) {
                return (Objects.equals(pass, rs.getString("PASSWORD")));
            }
        }
        return false;
    }

    public static void insert(String name, String pass) throws SQLException {
        String query = "INSERT INTO users (NAME, PASSWORD) VALUES (?, ?);";
        PreparedStatement stmt = SQLite.conn.prepareStatement(query);
        stmt.setString(1, name);
        stmt.setString(2, pass);
        stmt.executeUpdate();
    }
}
