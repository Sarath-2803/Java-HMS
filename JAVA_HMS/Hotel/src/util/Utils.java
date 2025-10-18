package util;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.HashMap;

public class Utils{
    private static final String url = System.getenv().get("DB_URL");
    private static final String user = System.getenv().get("DB_USER");
    private static final String password = System.getenv().get("DB_PASSWORD");

    public static Connection getConnection() throws SQLException{
        return DriverManager.getConnection(url,user,password);
    }
} 
