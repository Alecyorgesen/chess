package server;

import chess.ChessBoard;
import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.*;
import model.GameData;
import org.eclipse.jetty.server.Authentication;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.*;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.userCommands.*;

import java.util.*;

import static webSocketMessages.userCommands.UserGameCommand.CommandType.*;

@WebSocket
public class WSServer {
    static AuthDAO authDAO = new AuthSQLDAO();
    static GameDAO gameDAO = new GameSQLDAO();
    static HashSet<Connection> connections = new HashSet<>();
    static Map<Integer, ActiveGame> activeGameMap = new HashMap<>();

    @OnWebSocketMessage
    public void onMessage(Session session, String msg) throws Exception {
        UserGameCommand command = new Gson().fromJson(msg, UserGameCommand.class);
        Connection connection = new Connection(command.getAuthString(), session);
//        Check for connections. If it doesn't exist already, add it.
        boolean shouldAddConnection = true;
        for (Connection connection_ : connections) {
            if (Objects.equals(connection_.getAuthToken(), connection.getAuthToken())) {
                shouldAddConnection = false;
                break;
            }
        }
        if (shouldAddConnection) {
            connections.add(connection);
        }
        var conn = connection.getConnection(command.getAuthString(), session);
        if (conn != null) {
            switch (command.getCommandType()) {
                case JOIN_PLAYER:
                    JoinPlayer joinPlayer = new Gson().fromJson(msg, JoinPlayer.class);
                    join(conn, joinPlayer);
                    break;
//                case JOIN_OBSERVER -> observe(conn, command);
//                case DRAW_BOARD -> drawBoard(conn, command);
//                case MAKE_MOVE -> move(conn, command);
                case LEAVE:
                    Leave leave = new Gson().fromJson(msg, Leave.class);
                    leave(conn, leave);
                    break;
//                case RESIGN -> resign(conn, command);
                default:
                    System.out.println("Something went wrong with message from client");
                    break;
            }
        } else {
            Connection.sendError(session, "unknown user");
        }
    }
    public void join(Connection connection, JoinPlayer joinPlayer) {
        try {
            int gameID = joinPlayer.getGameID();
            ChessGame.TeamColor playerColor = joinPlayer.getPlayerColor();
            boolean shouldAddToMap = true;
            for (Map.Entry<Integer, ActiveGame> entry : activeGameMap.entrySet()) {
                int gameID_ = entry.getKey();
                if (gameID_ == gameID) {
                    shouldAddToMap = false;
                    break;
                }
            }
            ActiveGame activeGame = new ActiveGame(gameID);
            if (shouldAddToMap) {
                activeGameMap.put(gameID, activeGame);
            } else {
                activeGame = activeGameMap.get(gameID);
            }
            String username = authDAO.getAuthUsingAuth(connection.getAuthToken()).username();
            GameData gameData = gameDAO.getGame(gameID);
            if (gameData.whiteUsername().equals(username)) {
                activeGame.setWhitePlayer(connection);
            } else if (gameData.blackUsername().equals(username)) {
                activeGame.setBlackPlayer(connection);
            }
            loadGame(connection, gameID);
        } catch (Exception ex) {
            sendError(connection,ex.getMessage());
        }
    }
    public void loadGame(Connection connection, int gameID) {
        try {
            GameData gameData = gameDAO.getGame(gameID);
            LoadGame loadGame = new LoadGame(gameData);
            String json = new Gson().toJson(loadGame);
            connection.send(json);
        } catch (Exception ex) {
            sendError(connection, "Tried to load game, but exception was thrown: " + ex.getMessage());
        }
    }
    public void leave(Connection connection, Leave leave) {
        try{
            int gameID = leave.getGameID();
            String authToken = leave.getAuthString();
            GameData gameData = gameDAO.getGame(gameID);
            String username = authDAO.getAuthUsingAuth(authToken).username();
            ChessGame.TeamColor playerColor = null;
            if (gameData.whiteUsername().equals(username)) {
                gameData.setWhiteUsername(null);
                playerColor = ChessGame.TeamColor.WHITE;
            } else if (gameData.blackUsername().equals(username)) {
                gameData.setBlackUsername(null);
                playerColor = ChessGame.TeamColor.BLACK;
            }
            gameDAO.updateGame(gameID, gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), gameData.game());

            ActiveGame activeGame = activeGameMap.get(gameID);
            if (playerColor == null) {
                activeGame.notifyAllInGameExceptForConnection(connection, username + " has stopped observing the game.");
                activeGame.removeObserver(connection);
            } else if (playerColor == ChessGame.TeamColor.WHITE) {
                activeGame.notifyAllInGameExceptForConnection(connection, username + " has left the game.");
                activeGame.setWhitePlayer(null);
            } else {
                activeGame.notifyAllInGameExceptForConnection(connection, username + " has left the game.");
                activeGame.setBlackPlayer(null);
            }
        } catch (Exception ex) {
            sendError(connection,"Tried to get game, but failed: " + ex.getMessage());
        }
    }
    public void notifyAll(String message) {
        try {
            Notification notification = new Notification(message);
            String json = new Gson().toJson(notification);
            for (Connection connection : connections) {
                connection.getSession().getRemote().sendString(json);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
    public void sendError(Connection connection, String message) {
        try {
            ErrorMessage errorMessage = new ErrorMessage(message);
            String json = new Gson().toJson(errorMessage);
            connection.getSession().getRemote().sendString(json);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
    public static void notify(Connection connection, String message) {
        try {
            Notification notification = new Notification(message);
            String json = new Gson().toJson(notification);
            connection.getSession().getRemote().sendString(json);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}