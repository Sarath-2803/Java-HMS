import util.Utils;
import java.sql.Connection;
import frames.login;

public class Main {
    public static void main(String[] args) {
        try(Connection conn = Utils.getConnection()) {
            // Run migrations
            System.out.println("Running migrations...");
            util.MigrationRunner.runMigrations(conn);
            System.out.println("Migrations done!");

        } catch (Exception e) {
            e.printStackTrace();
        }
        login.main(new String[]{});
    }
}

