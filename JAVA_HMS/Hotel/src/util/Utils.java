package util;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.HashMap;

public class Utils{
    private static final String url = "jdbc:postgresql://localhost:5433/test";
    private static final String user = "postgres";
    private static final String password = "postgres";

    public static Connection getConnection() throws SQLException{
        return DriverManager.getConnection(url,user,password);
    }

/*    public static void connectionHealth(){
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
*/

/*    public static Map<String, String> queryToMap(String query) {
	Map<String, String> result = new HashMap<>();
	if(query == null || query.isEmpty()) return result;
	for (String param : query.split("&")) {
		String[] entry = param.split("=");
        	if(entry.length > 1) {
            		result.put(entry[0], entry[1]);
        	} else {
           	 result.put(entry[0], "");
        	}
    	}
    	return result;
	}
*/


/*    public static void main(String[] args) {
            Utils.connectionHealth();
        }
*/
} 
