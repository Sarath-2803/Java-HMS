import com.sun.net.httpserver.HttpServer;
import controller.UserController;
import controller.PaymentController;
import service.UserService;
import service.PaymentService;
import dao.UserDAO;
import dao.PaymentDAO;
import service.AmenityService;
import dao.AmenityDAO;
import controller.AmenityController;
import model.Amenity;
import service.AmenityBookingService;
import dao.AmenityBookingDAO;
import controller.AmenityBookingController;
import model.AmenityBooking;
import util.Utils;

import java.net.InetSocketAddress;
import java.sql.Connection;
import frames.login;

public class Main {
    public static void main(String[] args) {
        try(Connection conn = Utils.getConnection()) {
            // Run migrations
            System.out.println("Running migrations...");
            util.MigrationRunner.runMigrations(conn);
            System.out.println("Migrations done!");

            // Create services & controllers
            UserService userService = new UserService(new UserDAO());
	        PaymentService paymentService = new PaymentService(new PaymentDAO());
            AmenityService amenityService = new AmenityService(new AmenityDAO());
            AmenityBookingService amenityBookingService = new AmenityBookingService(new AmenityBookingDAO());

            UserController userController = new UserController(userService);
	        PaymentController paymentController = new PaymentController(paymentService);
            AmenityController amenityController = new AmenityController(amenityService);
            AmenityBookingController amenityBookingController = new AmenityBookingController(amenityBookingService);

            // Start HTTP server
            HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
            userController.registerRoutes(server);
	        paymentController.registerRoutes(server);
            amenityController.registerRoutes(server);
            amenityBookingController.registerRoutes(server);
            server.setExecutor(null);
            server.start();
            System.out.println("Server started on port 8080");

        } catch (Exception e) {
            e.printStackTrace();
        }
        login.main(new String[]{});
    }
}

