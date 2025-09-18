import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB{
    private static final String url = "jdbc:postgresql://localhost:5432/test";
    private static final String user = "postgres";
    private static final String password = "postgres";

    public static Connection getConnection() throws SQLException{
        return DriverManager.getConnection(url,user,password);
    }

    public static void connectionHealth(){
        try(Connection conn = getConnection()){  //try with resource
            if(conn!=null && !conn.isClosed()){
                System.out.println("DB connection establised successfully!");
            }
            else{
                System.out.println("DB connection failed....");
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
            DB.connectionHealth();
        }

} 
