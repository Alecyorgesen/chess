package client;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import model.AuthData;
import model.GameData;
import ui.ChessBoardPrinter;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.*;

import javax.websocket.*;
import java.net.URI;
import java.util.Objects;

public class WSClient extends Endpoint {
    ChessBoardPrinter chessBoardPrinter = new ChessBoardPrinter();
    public Session session;

    public WSClient() throws Exception {
        URI uri = new URI("ws://localhost:8080/connect");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, uri);

        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            public void onMessage(String message) {
//                System.out.println(message + "hey now");
                ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
                LoadGame loadGame = new Gson().fromJson(message, LoadGame.class);
                Notification notification = new Gson().fromJson(message, Notification.class);
                ErrorMessage errorMessage = new Gson().fromJson(message, ErrorMessage.class);
                switch (serverMessage.getServerMessageType()) {
                    case LOAD_GAME:
                        loadGame(loadGame);
                        break;
                    case ERROR:
                        serverError(errorMessage.getErrorMessage());
                        break;
                    case NOTIFICATION:
                        notifyClient(notification.getMessage());
                }
            }
        });
    }

    public void send(String msg) throws Exception {
        this.session.getBasicRemote().sendText(msg);
    }

    public void redrawChessBoard(AuthData authData, int gameID, ChessGame.TeamColor teamColor) throws Exception {
        DrawBoard drawBoard = new DrawBoard(authData.authToken(), gameID);
        String json = new Gson().toJson(drawBoard);
        this.send(json);
    }

    public void leave(AuthData authData, int gameID) throws Exception {
        Leave leave = new Leave(authData.authToken(), gameID);
        String json = new Gson().toJson(leave);
        this.send(json);
    }

    public void makeMove(AuthData authData, int gameID, ChessMove chessMove) throws Exception {
        MakeMove makeMove = new MakeMove(authData.authToken(), gameID, chessMove);
        String json = new Gson().toJson(makeMove);
        this.send(json);
    }

    @OnOpen
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void loadGame(LoadGame loadGame) {
        GameData gameData = loadGame.getGame();
        Client.currentGameData = gameData;
        ChessBoard chessBoard = gameData.game().getBoard();
        String currentUser = Client.getCurrentUser();
        if (Objects.equals(gameData.blackUsername(), currentUser)) {
            chessBoardPrinter.printBoardFromBlackSide(chessBoard);
        } else {
            chessBoardPrinter.printBoardFromWhiteSide(chessBoard);
        }
    }

    public void serverError(String errorMessage) {
        System.out.println("The server sent an error: " + errorMessage);
    }

    public void notifyClient(String message) {
        System.out.println(message);
    }

    public void resign(AuthData authData, int gameID) throws Exception {
        Resign resign = new Resign(authData.authToken(), gameID);
        String json = new Gson().toJson(resign);
        this.send(json);
    }

    public void joinPlayer(AuthData authData, int gameID, ChessGame.TeamColor teamColor) throws Exception {
        JoinPlayer joinPlayer = new JoinPlayer(authData.authToken(), gameID, teamColor);
        String json = new Gson().toJson(joinPlayer);
        this.send(json);
    }
    public void observeGame(AuthData authData, int gameID) throws Exception {
        JoinObserver joinObserver = new JoinObserver(authData.authToken(), gameID);
        String json = new Gson().toJson(joinObserver);
        this.send(json);
    }
}
