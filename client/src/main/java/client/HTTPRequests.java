package client;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
public class HTTPRequests {
    public void registerRequest(String username, String password, String email, String urlString) {
        try {
            HttpURLConnection connection = getHttpURLConnection(username, password, email, urlString);

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // Get HTTP response headers, if necessary
                // Map<String, List<String>> headers = connection.getHeaderFields();

                // OR

                //connection.getHeaderField("Content-Length");

                InputStream responseBody = connection.getInputStream();
                // Read response body from InputStream ...
            } else {
                // SERVER RETURNED AN HTTP ERROR

                InputStream responseBody = connection.getErrorStream();
                // Read and process error response body from InputStream ...
            }
        } catch (Exception exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }

    private static HttpURLConnection getHttpURLConnection(String username, String password, String email, String urlString) throws IOException {
        URL url = new URL(urlString);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setReadTimeout(5000);
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        // Set HTTP request headers, if necessary
//        connection.addRequestProperty("Accept", "text/html");

        connection.connect();

        try (OutputStream requestBody = connection.getOutputStream()) {
            // Write request body to OutputStream ...
            requestBody.write(("{ \"username\":\""+ username +"\", \"password\":\""+ password +"\", \"email\":\""+ email +"\" }").getBytes());

        }
        return connection;
    }
}
