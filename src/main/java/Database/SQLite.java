package Database;

import java.sql.*;

public class SQLite {

    public static Connection conn;

    public static void connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            String url = "jdbc:sqlite:bicycleshop.db";

            conn = DriverManager.getConnection(url);

            System.out.println("Connection to database successful.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void close() throws SQLException {
        conn.close();
    }

}
