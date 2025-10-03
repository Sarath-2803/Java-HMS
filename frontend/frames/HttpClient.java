import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

//@Sarath, read throught this and comment if this is the expected implementation
public class HttpClient {
    public static String sendGET(String urlStr) throws Exception {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            int status = conn.getResponseCode();
            if (status == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();
                //JSONObject jsonResponse = new JSONObject(response.toString());
                //
                //int statusCode = jsonResponse.getInt("status");
                //Object body = jsonResponse.get("body");
                return content.toString();
            }
            else if (status == 404) {
                throw new RuntimeException("Resource not found");
            } 
            else if (status == 500) {
                throw new RuntimeException("Server error occurred");
            } 
            else {
                throw new RuntimeException("Unexpected status: " + status);
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String sendPOST(String urlStr, String jsonInput) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; utf-8");
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInput.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) {
            response.append(line.trim());
        }
        in.close();
        return response.toString();
    }
}
