package http;
import java.io.*;
import java.net.*;
import org.json.*;

public class HttpClient {

    private static final String BASE_URL = "http://localhost:8080";

    public static JSONObject sendGet(String endpoint) {
        try {
            URL url = new URL(BASE_URL + endpoint);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            // Reading response
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();
            JSONObject jsonResponse = new JSONObject(response.toString());
            int status = jsonResponse.getInt("status");
            if (status == 200) {
                return jsonResponse;
            } else {
                System.out.println("Error: " + status);
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

