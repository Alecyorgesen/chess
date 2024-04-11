package server;

import java.util.HashSet;
import java.util.Objects;

public class ActiveGame {
    Connection whitePlayerConnection = null;
    Connection blackPlayerConnection = null;
    HashSet<Connection> observers = new HashSet<>();
    int gameID;
    public ActiveGame(int gameID) {
        this.gameID = gameID;
    }
    public int getGameID() {
        return gameID;
    }
    public Connection getWhitePlayer() {
        return whitePlayerConnection;
    }
    public Connection getBlackPlayer() {
        return blackPlayerConnection;
    }
    public void setWhitePlayer(Connection whitePlayer) {
        this.whitePlayerConnection = whitePlayer;
    }
    public void setBlackPlayer(Connection blackPlayerConnection) {
        this.blackPlayerConnection = blackPlayerConnection;
    }
    public void removeObserver(Connection connection) {
        observers.remove(connection);
    }
    public void notifyAllInGameExceptForConnection(Connection connection, String message) {
        if (getWhitePlayer()!=null) {
            if (!getWhitePlayer().getAuthToken().equals(connection.getAuthToken())) {
                WSServer.notify(connection, message);
            }
        }
        if (getBlackPlayer()!=null) {
            if (!getBlackPlayer().getAuthToken().equals(connection.getAuthToken())) {
                WSServer.notify(connection, message);
            }
        }
        for (Connection connection_ : observers) {
            if (!connection_.getAuthToken().equals(connection.getAuthToken())) {
                WSServer.notify(connection, message);
            }
        }
    }
}