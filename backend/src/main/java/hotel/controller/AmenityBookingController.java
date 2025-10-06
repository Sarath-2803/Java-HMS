package controller;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import service.AmenityBookingService;
import model.AmenityBooking;
import util.Utils;
import dto.ResponseDTO;
import util.ResponseUtils;
import util.HelperUtils;

public class AmenityBookingController {
    private final AmenityBookingService bookingService;

    public AmenityBookingController(AmenityBookingService bookingService) {
        this.bookingService = bookingService;
    }

    // Register routes
    public void registerRoutes(HttpServer server) {
        server.createContext("/booking", this::handleBookingPath);
        server.createContext("/booking/", this::handleBookingIdPath);
    }

    // Handle /booking
    public void handleBookingPath(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        switch (method.toUpperCase()) {
            case "GET":
                displayAllBookings(exchange);
                break;
            case "POST":
                createBooking(exchange);
                break;
            case "PUT":
                updateBooking(exchange);
                break;
        }
    }

    // Handle /booking/{id}
    public void handleBookingIdPath(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String[] parts = path.split("/");
        if (parts.length == 3 && method.equalsIgnoreCase("GET")) {
            Long id = Long.parseLong(parts[2]);
            displayBookingById(exchange, id);
        } else if (parts.length == 3 && method.equalsIgnoreCase("DELETE")) {
            Long id = Long.parseLong(parts[2]);
            deleteBookingById(exchange, id);
        }
    }

    public void displayAllBookings(HttpExchange exchange) throws IOException {
        try (Connection conn = Utils.getConnection()) {
            List<AmenityBooking> bookings = bookingService.getAllAmenityBookings(conn);
            ResponseDTO response = new ResponseDTO(200, bookings);
            ResponseUtils.sendResponse(exchange, response);
        } catch (SQLException e) {
            ResponseDTO response = new ResponseDTO(500, e.getMessage());
            ResponseUtils.sendResponse(exchange, response);
        }
    }

    public void displayBookingById(HttpExchange exchange, Long id) throws IOException {
        try (Connection conn = Utils.getConnection()) {
            AmenityBooking booking = bookingService.getById(id, conn);
            ResponseDTO response = new ResponseDTO(200, booking);
            ResponseUtils.sendResponse(exchange, response);
        } catch (SQLException | RuntimeException e) {
            ResponseDTO response = new ResponseDTO(500, e.getMessage());
            ResponseUtils.sendResponse(exchange, response);
        }
    }

    public void createBooking(HttpExchange exchange) throws IOException {
        InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), "utf-8");
        BufferedReader br = new BufferedReader(isr);
        StringBuilder buf = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            buf.append(line);
        }
        br.close();
        String requestBody = buf.toString();

        String capacityStr = HelperUtils.extractJsonValue(requestBody, "requiredCapacity");
        String startTimeStr = HelperUtils.extractJsonValue(requestBody, "startTime");
        String endTimeStr = HelperUtils.extractJsonValue(requestBody, "endTime");

        if (capacityStr == null || startTimeStr == null || endTimeStr == null) {
            ResponseDTO response = new ResponseDTO(400, "Missing parameters");
            ResponseUtils.sendResponse(exchange, response);
            return;
        }

        int requiredCapacity = Integer.parseInt(capacityStr);
        Timestamp startTime = Timestamp.valueOf(startTimeStr);
        Timestamp endTime = Timestamp.valueOf(endTimeStr);

        try (Connection conn = Utils.getConnection()) {
            AmenityBooking booking = bookingService.bookAmenity(requiredCapacity, startTime, endTime, conn);
            if (booking != null) {
                ResponseDTO response = new ResponseDTO(201, booking);
                ResponseUtils.sendResponse(exchange, response);
            } else {
                ResponseDTO response = new ResponseDTO(409, "No available amenity for requested time/capacity");
                ResponseUtils.sendResponse(exchange, response);
            }
        } catch (SQLException e) {
            ResponseDTO response = new ResponseDTO(500, e.getMessage());
            ResponseUtils.sendResponse(exchange, response);
        }
    }

    public void updateBooking(HttpExchange exchange) throws IOException {
        InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), "utf-8");
        BufferedReader br = new BufferedReader(isr);
        StringBuilder buf = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            buf.append(line);
        }
        br.close();
        String requestBody = buf.toString();

        String idStr = HelperUtils.extractJsonValue(requestBody, "id");
        String amenityIdStr = HelperUtils.extractJsonValue(requestBody, "amenityId");
        String startTimeStr = HelperUtils.extractJsonValue(requestBody, "startTime");
        String endTimeStr = HelperUtils.extractJsonValue(requestBody, "endTime");

        if (idStr == null || amenityIdStr == null || startTimeStr == null || endTimeStr == null) {
            ResponseDTO response = new ResponseDTO(400, "Missing parameters");
            ResponseUtils.sendResponse(exchange, response);
            return;
        }

        Long id = Long.parseLong(idStr);
        Long amenityId = Long.parseLong(amenityIdStr);
        Timestamp startTime = Timestamp.valueOf(startTimeStr);
        Timestamp endTime = Timestamp.valueOf(endTimeStr);

        AmenityBooking booking = new AmenityBooking();
        booking.setId(id);
        booking.setAmenityId(amenityId);
        booking.setStartTime(startTime);
        booking.setEndTime(endTime);

        try (Connection conn = Utils.getConnection()) {
            AmenityBooking updatedBooking = bookingService.updateAmenityBooking(booking, conn);
            ResponseDTO response = new ResponseDTO(200, updatedBooking);
            ResponseUtils.sendResponse(exchange, response);
        } catch (SQLException | RuntimeException e) {
            ResponseDTO response = new ResponseDTO(500, e.getMessage());
            ResponseUtils.sendResponse(exchange, response);
        }
    }

    public void deleteBookingById(HttpExchange exchange, Long id) throws IOException {
        try (Connection conn = Utils.getConnection()) {
            bookingService.deleteAmenityBooking(id, conn);
            ResponseDTO response = new ResponseDTO(204, -1);
            ResponseUtils.sendResponse(exchange, response);
        } catch (SQLException | RuntimeException e) {
            ResponseDTO response = new ResponseDTO(500, e.getMessage());
            ResponseUtils.sendResponse(exchange, response);
        }
    }
}