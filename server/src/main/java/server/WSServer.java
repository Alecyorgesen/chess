package server;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import dataAccess.*;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.*;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.userCommands.*;

import java.util.*;


@WebSocket
public class WSServer {
    public static final Object syncronizedObject = new Object();
    static AuthDAO authDAO = new AuthSQLDAO();
    static GameDAO gameDAO = new GameSQLDAO();
    static HashSet<Connection> connections = new HashSet<>();
    static Map<Integer, ActiveGame> activeGameMap = new HashMap<>();

    @OnWebSocketMessage
    public void onMessage(Session session, String msg) {
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
                case JOIN_OBSERVER:
                    JoinObserver joinObserver = new Gson().fromJson(msg, JoinObserver.class);
                    observe(conn, joinObserver);
                    break;
                case DRAW_BOARD:
                    DrawBoard drawBoard = new Gson().fromJson(msg, DrawBoard.class);
                    drawBoard(conn, drawBoard);
                    break;
                case MAKE_MOVE:
                    MakeMove makeMove = new Gson().fromJson(msg, MakeMove.class);
                    makeMove(conn, makeMove);
                    break;
                case LEAVE:
                    Leave leave = new Gson().fromJson(msg, Leave.class);
                    leave(conn, leave);
                    break;
                case RESIGN:
                    Resign resign = new Gson().fromJson(msg, Resign.class);
                    resign(conn, resign);
                    break;
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
            AuthData authData = authDAO.getAuthUsingAuth(connection.getAuthToken());
            if (authData == null) {
                sendError(connection, "Not Authorized.");
                return;
            }
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
            if (gameData.whiteUsername() != null && gameData.whiteUsername().equals(username)) {
                activeGame.setWhitePlayer(connection);
            } else if (gameData.blackUsername().equals(username)) {
                activeGame.setBlackPlayer(connection);
            }
            loadGame(connection, gameID);
            String playerColorAsString;
            if (playerColor.equals(ChessGame.TeamColor.WHITE)) {
                playerColorAsString = "white";
            } else {
                playerColorAsString = "black";
            }
            activeGame.notifyAllInGameExceptForConnection(connection, username + " joined the game as " + playerColorAsString);
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
            if (gameData.whiteUsername() != null){
                if (gameData.whiteUsername().equals(username)) {
                    gameData = gameData.setWhiteUsername(null); //You must assign it like this because gameData is a record.
                    playerColor = ChessGame.TeamColor.WHITE;
                }
            } else if (gameData.blackUsername() != null) {
                if (gameData.blackUsername().equals(username)) {
                    gameData = gameData.setBlackUsername(null);
                    playerColor = ChessGame.TeamColor.BLACK;
                }
            }
            gameDAO.updateGame(gameID, gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), gameData.game());

            ActiveGame activeGame = activeGameMap.get(gameID);
            if (playerColor == null) {
                if (activeGame != null) {
                    activeGame.notifyAllInGameExceptForConnection(connection, username + " has stopped observing the game.");
                    activeGame.removeObserver(connection);
                }
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
    public void makeMove(Connection connection, MakeMove makeMove) {
        try {
            int gameID = makeMove.getGameID();
            ChessMove chessMove = makeMove.getChessMove();
            ActiveGame activeGame = activeGameMap.get(gameID);
            GameData gameData = gameDAO.getGame(gameID);
            ChessGame chessGame = gameData.game();
            if (chessGame.playerResigned) {
                sendError(connection, "A Player has resigned. The game is over.");
                return;
            }
            ChessGame.TeamColor teamColor = null;
            if (activeGame.getWhitePlayer() != null && activeGame.getWhitePlayer().getAuthToken().equals(connection.getAuthToken())) {
                if (chessGame.getTeamTurn() == ChessGame.TeamColor.WHITE) {
                    chessGame.makeMove(chessMove);
                    teamColor = ChessGame.TeamColor.WHITE;
                } else {
                    sendError(connection,"It's not your turn!");
                    return;
                }
            } else if (activeGame.getBlackPlayer() != null && activeGame.getBlackPlayer().getAuthToken().equals((connection.getAuthToken()))) {
                if (chessGame.getTeamTurn() == ChessGame.TeamColor.BLACK) {
                    chessGame.makeMove(chessMove);
                    teamColor = ChessGame.TeamColor.BLACK;
                } else {
                    sendError(connection,"It's not your turn!");
                    return;
                }
            } else {
                sendError(connection, "You can't make a move if you are observing.");
                return;
            }
            synchronized (syncronizedObject) {
                gameDAO.updateGame(gameID, gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), chessGame);
            }
            loadGame(activeGame.getWhitePlayer(), gameID);
            loadGame(activeGame.getBlackPlayer(), gameID);
            for (Connection connection_ : activeGame.getObservers()) {
                loadGame(connection_, gameID);
            }
            int row1 = chessMove.getStartPosition().getRow();
            int col1 = chessMove.getStartPosition().getColumn();
            int row2 = chessMove.getEndPosition().getRow();
            int col2 = chessMove.getEndPosition().getColumn();
            String col1AsString = switch (col1) {
                case 1 -> "a";
                case 2 -> "b";
                case 3 -> "c";
                case 4 -> "d";
                case 5 -> "e";
                case 6 -> "f";
                case 7 -> "g";
                case 8 -> "h";
                default -> "";
            };
            String col2AsString = switch (col2) {
                case 1 -> "a";
                case 2 -> "b";
                case 3 -> "c";
                case 4 -> "d";
                case 5 -> "e";
                case 6 -> "f";
                case 7 -> "g";
                case 8 -> "h";
                default -> "";
            };
            String coordinate1 = row1 + col1AsString;
            String coordinate2 = row2 + col2AsString;
            String playerColor;
            if (teamColor == ChessGame.TeamColor.WHITE) {
                playerColor = "White";
            } else {
                playerColor = "Black";
            }
            activeGame.notifyAllInGameExceptForConnection(connection, playerColor + " moved " + coordinate2 + " to " + coordinate1 + ".");
            if (chessGame.isInCheckmate(ChessGame.TeamColor.WHITE)) {
                notifyAll("White won the game!");
            } else if (chessGame.isInCheckmate(ChessGame.TeamColor.BLACK)) {
                notifyAll("Black won the game!");
            } else if (chessGame.isInStalemate(ChessGame.TeamColor.WHITE)) {
                notifyAll("The game finished in a stalemate.");
            } else if (chessGame.isInStalemate(ChessGame.TeamColor.BLACK)) {
                notifyAll("The game finished in a stalemate.");
            }
        } catch (Exception ex) {
            sendError(connection, ex.getMessage());
        }
    }
    public void observe(Connection connection, JoinObserver joinObserver) {
        try {
            AuthData authData = authDAO.getAuthUsingAuth(joinObserver.getAuthString());
            if (authData == null) {
                sendError(connection, "Not Authorized.");
                return;
            }
            int gameID = joinObserver.getGameID();
            GameData gamedata = gameDAO.getGame(gameID);
            if (gamedata == null) {
                sendError(connection, "Game not found");
                return;
            }
            ActiveGame activeGame = activeGameMap.get(gameID);
            if (activeGame == null) {
                activeGame = new ActiveGame(gameID);
            }
            String username = authData.username();
            activeGame.addObserver(connection);
            loadGame(connection, gameID);
            activeGame.notifyAllInGameExceptForConnection(connection, username + " is now observing");
        } catch (Exception ex) {
            sendError(connection, ex.getMessage());
        }
    }
    public void drawBoard(Connection connection, DrawBoard drawBoard) {
        try {
            loadGame(connection, drawBoard.getGameID());
        } catch (Exception ex) {
            sendError(connection, ex.getMessage());
        }
    }
    public void resign(Connection connection, Resign resign) {
        try {
            int gameID = resign.getGameID();
            GameData gameData = gameDAO.getGame(gameID);
            ActiveGame activeGame = activeGameMap.get(gameID);
            gameData.game().playerResigned = true;
            AuthData authData = authDAO.getAuthUsingAuth(resign.getAuthString());
            String username = authData.username();
            activeGame.notifyAllInGame(username + " resigned. The game is over.");
        } catch (Exception ex) {
            sendError(connection, ex.getMessage());
        }
    }
}