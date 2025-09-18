public class MigrationRunner{
	private static final MIG_DIR="src/main/resources/db/migrations";

	public static void main(String args[]){
		try(Connection conn = DB.getConnection()){
			if(conn!=null && !conn.isClosed()){
				System.out.println("DB connected successfully!");
				runMigrations(conn);
			}
			else{
				System.out.println("DB connection closed...");
			}
		}
		catch(SQLException e){
			e.getMessage();
		}
	}

	public static void runMigrations(Connection conn) throws SQLException{
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
