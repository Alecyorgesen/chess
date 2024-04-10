package client;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import model.AuthData;
import response.ListGamesResponse;
import webSocketMessages.userCommands.DrawBoard;
import webSocketMessages.userCommands.UserGameCommand;

public class ServerFacade {
    int port;
    public ServerFacade(int port) {
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
    public void redrawChessBoard(WSClient wsClient, AuthData authData, int gameID, ChessGame.TeamColor teamColor) {
        try {
            wsClient.redrawChessBoard(wsClient, authData, gameID, teamColor);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }
    public void leave(WSClient wsClient, AuthData authData, int gameID){
        try {
            wsClient.leave(authData, gameID);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }
    public void makeMove(WSClient wsClient, AuthData authData, int gameID, ChessMove chessMove) {
        try {
            wsClient.makeMove(authData, gameID, chessMove);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }
    public void resign(WSClient wsClient, AuthData authData, int gameID) {
        try {
            wsClient.resign(authData, gameID);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }
    public void joinThroughWebSocket(WSClient wsClient, AuthData authData, int gameID, ChessGame.TeamColor teamColor) {
        try {
            wsClient.joinPlayer(authData, gameID, teamColor);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }
}
