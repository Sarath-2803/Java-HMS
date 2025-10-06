package util;

import com.sun.net.httpserver.HttpExchange;
import java.io.OutputStream;
import java.io.IOException;
import java.util.List;
import dto.ResponseDTO;
import model.User;
import model.Amenity;
import model.AmenityBooking;
import model.Payment;

public class ResponseUtils {

    public static void sendResponse(HttpExchange exchange, ResponseDTO response) throws IOException {
	System.out.println("Response : " + response);
        String jsonResponse = convertToJson(response);

	int statusCode = response.getStatus();

        // If status = 204 No Content → no body allowed
        if (statusCode == 204) {
            exchange.sendResponseHeaders(204, -1); // no body
            exchange.close();
            return;
        }

        // Set response headers
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        byte[] bytes = jsonResponse.getBytes("UTF-8");
        exchange.sendResponseHeaders(response.getStatus(), bytes.length);

        // Write response
        OutputStream os = exchange.getResponseBody();
        os.write(bytes);
        os.close();
    }

    public static String convertToJson(Object obj) {
        if (obj == null) {
            return "null";
        }

        // Handle primitive types
        if (obj instanceof String) {
            return "\"" + obj + "\"";
        }
        if (obj instanceof Number || obj instanceof Boolean) {
            return obj.toString();
        }

        // Handle List
        if (obj instanceof List) {
            List<?> list = (List<?>) obj;
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            for (int i = 0; i < list.size(); i++) {
                sb.append(convertToJson(list.get(i)));
                if (i < list.size() - 1) sb.append(",");
            }
            sb.append("]");
            return sb.toString();
        }

        // Handle User
        if (obj instanceof User) {
            User u = (User) obj;
            return "{"
                    + "\"id\":" + u.getId() + ","
                    + "\"username\":\"" + u.getUsername() + "\","
                    + "\"email\":\"" + u.getEmail() + "\","
                    + "\"phone\":\"" + u.getPhone() + "\","
                    + "\"loyalty\":" + u.getLoyalty()
                    + "}";
        }

	//Handle Payment
	if (obj instanceof Payment){
		Payment p = (Payment) obj;
		return "{"
			+ "\"id\":" + p.getId() + ","
			+ "\"userId\":" + p.getUserId() + ","
			+ "\"bookingId\":" + p.getBookingId() + ","
			+ "\"amount\":" + p.getAmount() + ","
			+ "\"status\":" + p.getStatus()
			+ "}";
	}

        // Handle ResponseDTO
        if (obj instanceof ResponseDTO) {
            ResponseDTO r = (ResponseDTO) obj;
            return "{"
                    + "\"status\":" + r.getStatus() + ","
                    + "\"body\":" + convertToJson(r.getBody())
                    + "}";
        }
        if(obj instanceof Amenity){
            Amenity a = (Amenity) obj;
            return "{"
                    + "\"id\":" + a.getId() + ","
                    + "\"capacity\":" + a.getCapacity()
                    + "}";
        }
        if(obj instanceof AmenityBooking){
            AmenityBooking ab = (AmenityBooking) obj;
            return "{"
                    + "\"id\":" + ab.getId() + ","
                    + "\"amenityId\":" + ab.getAmenityId() + ","
                    + "\"startTime\":\"" + ab.getStartTime() + "\","
                    + "\"endTime\":\"" + ab.getEndTime() + "\","
                    + "}";
        }

        // Fallback for unknown objects
        return "\"" + obj.toString() + "\"";
    }
}

