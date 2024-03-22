package client;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.AuthData;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
public class HTTPRequest {
    public AuthData registerRequest(String username, String password, String email, String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setReadTimeout(5000);
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            // Set HTTP request headers, if necessary
//        connection.addRequestProperty("Accept", "text/html");
            connection.connect();

            try (OutputStream requestBody = connection.getOutputStream()) {
                requestBody.write(("{ \"username\":\""+ username +"\", \"password\":\""+ password +"\", \"email\":\""+ email +"\" }").getBytes());
            }

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // Get HTTP response headers, if necessary
                // Map<String, List<String>> headers = connection.getHeaderFields();
                // OR
                //connection.getHeaderField("Content-Length");
                InputStream responseBody = connection.getInputStream();
                // Read response body from InputStream ...
                Gson gson = new GsonBuilder().create();
                return gson.fromJson(new InputStreamReader(responseBody), AuthData.class);
            } else {
                // SERVER RETURNED AN HTTP ERROR
                InputStream responseBody = connection.getErrorStream();
                // Read and process error response body from InputStream ...
                System.out.println(responseBody.toString());
                return null;
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }
    public AuthData loginRequest(String username, String password, String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(5000);
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.connect();

            try (OutputStream requestBody = connection.getOutputStream()) {
                requestBody.write(("{ \"username\":\""+ username +"\", \"password\":\""+ password +"\" }").getBytes());
            }
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream responseBody = connection.getInputStream();
                Gson gson = new GsonBuilder().create();
                return gson.fromJson(new InputStreamReader(responseBody), AuthData.class);
            } else {
                InputStream responseBody = connection.getErrorStream();
                System.out.println(responseBody.toString());
                return null;
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }
    public void logoutRequest(AuthData authData, String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(5000);
            connection.setRequestMethod("DELETE");
            connection.setDoOutput(true);

            connection.addRequestProperty("Authorization", authData.authToken());

            connection.connect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {

            } else {
                InputStream responseBody = connection.getErrorStream();
                System.out.println(responseBody.toString());
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }
}
