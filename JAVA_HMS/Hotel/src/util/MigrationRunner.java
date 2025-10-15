package util;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class MigrationRunner{
	//private static final String MIG_DIR="C:\\Users\\LENOVO\\OneDrive\\Desktop\\JAVA_HMS_Final\\Java-HMS\\JAVA_HMS\\Hotel\\resources\\db\\migrations";
	private static final String MIG_DIR="/home/varghesejohn/johan/CET/oop_proj/JAVA_HMS/Hotel/resources/db/migrations";
	public static void main(String args[]){
		try(Connection conn = Utils.getConnection()){
			if(conn!=null && !conn.isClosed()){
				System.out.println("DB connected successfully!");
				runMigrations(conn);
			}
			else{
				System.out.println("DB connection closed...");
			}
		}
		catch(SQLException e){
			System.out.println(e.getMessage());
			throw new RuntimeException(e);
		}
		catch(Exception e){
			throw new RuntimeException(e);
		}
	}

	public static void runMigrations(Connection conn) throws Exception{
		List<Path> sqlFiles = Files.list(Paths.get(MIG_DIR))
			.filter(f -> f.toString().endsWith(".sql"))
			.sorted().toList();

        	for (Path path : sqlFiles) {
            		System.out.println("Running migration: " + path.getFileName());
            		String sql = Files.readString(path);
            		try (Statement stmt = conn.createStatement()) {
                		stmt.execute(sql);
            		}
        	}
        	System.out.println("All migrations executed successfully!");
	}
}
