package client;

import model.AuthData;
import response.ListGamesResponse;

public class ServerFacade {
    int port;
    ServerFacade(int port) {
        this.port = port;
    }
    HTTPRequest httpRequest = new HTTPRequest();
    public AuthData register(String username, String password, String email) {
        try {
            AuthData authData = httpRequest.registerRequest(username,password,email,"http://localhost:8080/user");
            if (authData == null) {
                throw new Exception("Something went wrong.");
            }
            return authData;
        } catch (Exception ex) {
            System.out.println("Got an exception: " + ex.getMessage());
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
        } catch (Exception ex) {
            System.out.println("Got an exception: " + ex.getMessage());
        }
        return null;
    }
    public void logout(AuthData authData) {
        try {
            httpRequest.logoutRequest(authData,"http://localhost:8080/session");
        } catch (Exception ex) {
            System.out.println("Got an exception: " + ex.getMessage());
        }
    }
    public ListGamesResponse listGames(AuthData authData) {
        try {
            ListGamesResponse listGamesResponse = httpRequest.listGamesRequest(authData, "http://localhost:8080/game");
            if (listGamesResponse == null) {
                System.out.println("Something went wrong.");
            }
            if (authData == null) {
                System.out.println("Not authorized.");
            }
            return listGamesResponse;
        } catch (Exception ex) {
            System.out.println("Got an exception: " + ex.getMessage());
            return null;
        }
    }
    public void createGame(AuthData authData, String gameName) {
        try {
            httpRequest.createGameRequest(authData, gameName, "http://localhost:8080/game");
            if (authData == null) {
                System.out.println("Something went wrong, game not created.");
            }
        } catch (Exception ex) {
            System.out.println("Got an exception: " + ex.getMessage());
        }
    }
    public void joinGame(AuthData authData, int gameID, String teamColor) {
        try {
            httpRequest.joinGameRequest(authData, gameID, teamColor, "http://localhost:8080/game");
            if (authData == null) {
                System.out.println("Unauthorized.");
            }
        } catch (Exception ex) {
            System.out.println("Got an exception: " + ex.getMessage());
        }
    }
    public void clear() {
        try {
            httpRequest.clearGameRequest("http://localhost:8080/db");
        } catch (Exception ex) {
            System.out.println("Got an exception: " + ex.getMessage());
        }
    }
}
