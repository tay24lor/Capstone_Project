package Database;

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
}
