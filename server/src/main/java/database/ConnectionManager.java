//package database;
//
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//
//public final class ConnectionManager {
//    public static final String user = "s336815";
//    public static final String password = "qwerty";
//    public static final String url = "jdbc:postgresql://localhost:5432/postgres";
//
//
//    public static Connection open(){
//        try {
//            return DriverManager.getConnection(url, user, password);
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }
//}
