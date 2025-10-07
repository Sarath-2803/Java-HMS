package controller;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import service.AmenityService;
import model.Amenity;
import util.Utils;
import dto.ResponseDTO;
import util.ResponseUtils;
import util.HelperUtils;

public class AmenityController {
    private final AmenityService amenityService;

    public AmenityController(AmenityService amenityService) {
        this.amenityService = amenityService;
    }

    // Register routes
    public void registerRoutes(HttpServer server) {
        server.createContext("/amenity", this::handleAmenityPath);
        server.createContext("/amenity/", this::handleAmenityIdPath);
    }

    // Handle /amenity
    public void handleAmenityPath(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        switch (method.toUpperCase()) {
            case "GET":
                displayAllAmenities(exchange);
                break;
            case "POST":
                createAmenity(exchange);
                break;
            case "PUT":
                updateAmenity(exchange);
                break;
        }
    }

    // Handle /amenity/{id}
    public void handleAmenityIdPath(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String[] parts = path.split("/");
        if (parts.length == 3 && method.equalsIgnoreCase("GET")) {
            Long id = Long.parseLong(parts[2]);
            displayAmenityById(exchange, id);
        } else if (parts.length == 3 && method.equalsIgnoreCase("DELETE")) {
            Long id = Long.parseLong(parts[2]);
            deleteAmenityById(exchange, id);
        }
    }

    public void displayAllAmenities(HttpExchange exchange) throws IOException {
        try (Connection conn = Utils.getConnection()) {
            List<Amenity> amenities = amenityService.getAllAmenities(conn);
            System.out.println("Retrieved Amenities: " + amenities);
            ResponseDTO response = new ResponseDTO(200, amenities);
            ResponseUtils.sendResponse(exchange, response);
        } catch (SQLException e) {
            ResponseDTO response = new ResponseDTO(500, e.getMessage());
            ResponseUtils.sendResponse(exchange, response);
        }
    }

    public void displayAmenityById(HttpExchange exchange, Long id) throws IOException {
        try (Connection conn = Utils.getConnection()) {
            Amenity amenity = amenityService.getById(id, conn);
            ResponseDTO response = new ResponseDTO(200, amenity);
            ResponseUtils.sendResponse(exchange, response);
        } catch (SQLException | RuntimeException e) {
            ResponseDTO response = new ResponseDTO(500, e.getMessage());
            ResponseUtils.sendResponse(exchange, response);
        }
    }

    public void createAmenity(HttpExchange exchange) throws IOException {
        InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), "utf-8");
        BufferedReader br = new BufferedReader(isr);
        StringBuilder buf = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            buf.append(line);
        }
        br.close();
        String requestBody = buf.toString();
        System.out.println("Request Body: " + requestBody);

        String typeStr = HelperUtils.extractJsonValue(requestBody, "type");
        if (typeStr == null) {
            ResponseDTO response = new ResponseDTO(400, "Missing type parameter");
            ResponseUtils.sendResponse(exchange, response);
            return;
        }

        int type = Integer.parseInt(typeStr);

        try (Connection conn = Utils.getConnection()) {
            Amenity amenity = amenityService.createAmenity(type, conn);
            ResponseDTO response = new ResponseDTO(201, amenity);
            ResponseUtils.sendResponse(exchange, response);
        } catch (SQLException e) {
            ResponseDTO response = new ResponseDTO(500, e.getMessage());
            ResponseUtils.sendResponse(exchange, response);
        }
    }

    public void updateAmenity(HttpExchange exchange) throws IOException {
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
        String typeStr = HelperUtils.extractJsonValue(requestBody, "type");

        if (idStr == null || typeStr == null) {
            ResponseDTO response = new ResponseDTO(400, "Missing parameters");
            ResponseUtils.sendResponse(exchange, response);
            return;
        }

        Long id = Long.parseLong(idStr);
        int type = Integer.parseInt(typeStr);

        Amenity amenity = new Amenity(type);
        amenity.setId(id);

        try (Connection conn = Utils.getConnection()) {
            Amenity updatedAmenity = amenityService.updateAmenity(amenity, conn);
            ResponseDTO response = new ResponseDTO(200, updatedAmenity);
            ResponseUtils.sendResponse(exchange, response);
        } catch (SQLException | RuntimeException e) {
            ResponseDTO response = new ResponseDTO(500, e.getMessage());
            ResponseUtils.sendResponse(exchange, response);
        }
    }

    public void deleteAmenityById(HttpExchange exchange, Long id) throws IOException {
        try (Connection conn = Utils.getConnection()) {
            amenityService.deleteAmenity(id, conn);
            ResponseDTO response = new ResponseDTO(204, -1);
            ResponseUtils.sendResponse(exchange, response);
        } catch (SQLException | RuntimeException e) {
            ResponseDTO response = new ResponseDTO(500, e.getMessage());
            ResponseUtils.sendResponse(exchange, response);
        }
    }
}