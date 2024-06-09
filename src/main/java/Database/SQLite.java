package Database;

import java.sql.*;

public class SQLite {

    public static Connection conn;

    public static void connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            String url = "jdbc:sqlite:bicycleshop.db";

            conn = DriverManager.getConnection(url);

            createPartTable();
            createProductTable();
            createUserTable();
            insertAdminUser();

            System.out.println("Connection to database successful.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void close() throws SQLException {
        conn.close();
    }

    private static void createUserTable() throws SQLException {
        Statement statement = conn.createStatement();
        String createPartTable = "CREATE TABLE IF NOT EXISTS users ("
                + " id INTEGER UNIQUE,"
                + " name TEXT UNIQUE,"
                + " password TEXT UNIQUE,"
                + " PRIMARY KEY('id' AUTOINCREMENT)),";

        statement.execute(createPartTable);
    }

    private static void createPartTable() throws SQLException {
        Statement statement = conn.createStatement();
        String createPartTable = "CREATE TABLE IF NOT EXISTS parts ("
                                    + " id INTEGER,"
                                    + " name TEXT,"
                                    + " price REAL,"
                                    + " stock INTEGER,"
                                    + " min INTEGER,"
                                    + " max INTEGER,"
                                    + " machineCode INTEGER,"
                                    + " companyName TEXT,"
                                    + " productID INTEGER DEFAULT -1,"
                                    + " date_created DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,"
                                    + " PRIMARY KEY('id' AUTOINCREMENT));";

        statement.execute(createPartTable);
    }

    private static void createProductTable() throws SQLException {
        Statement statement = conn.createStatement();
        String createPartTable = "CREATE TABLE IF NOT EXISTS products ("
                + " id INTEGER,"
                + " name TEXT,"
                + " price REAL,"
                + " stock INTEGER,"
                + " min INTEGER,"
                + " max INTEGER,"
                + " date_created DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,"
                + " PRIMARY KEY('id' AUTOINCREMENT));";

        statement.execute(createPartTable);
    }

    private static void insertAdminUser() throws SQLException {
        String createAdmin = "INSERT OR REPLACE INTO users (name, password) VALUES (?, ?);";
        PreparedStatement preparedStatement = conn.prepareStatement(createAdmin);
        preparedStatement.setString(1,"admin");
        preparedStatement.setString(2, "admin");
        preparedStatement.executeUpdate();
    }
}
