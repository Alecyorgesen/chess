package client;

import model.AuthData;

public class ServerFacade {
    HTTPRequest httpRequest = new HTTPRequest();
    public AuthData register(String username, String password, String email) {
        try {
            AuthData authData = httpRequest.registerRequest(username,password,email,"http://localhost:8080/user");
            if (authData == null) {
                throw new Exception("Something went wrong.");
            }
            return authData;
        } catch (Exception exception) {
            System.out.println("Got an exception: " + exception.getMessage());
        }
        return null;
    }
    public AuthData login(String username, String password) {
        try {
            AuthData authData = httpRequest.loginRequest(username,password,"http://localhost:8080/session");
            if (authData == null) {
                throw new Exception("Something went wrong.");
            }
            return authData;
        } catch (Exception exception) {
            System.out.println("Got an exception: " + exception.getMessage());
        }
        return null;
    }
    public void logout() {

    }
    public void listGames() {

    }
    public void createGame() {

    }
    public void joinGame() {

    }
    public void clear() {

    }
}
