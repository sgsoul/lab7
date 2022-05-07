package database;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConnectionManager {
    public static final String USERNAME = "postgres";
    public static final String PASSWORD = "qwerty";
    public static final String URL = "jdbc:postgresql://localhost:5432/postgres";

//    private ConnectionManager() {
//    }

    public static Connection open(){
        try {
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
