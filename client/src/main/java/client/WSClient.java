package client;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import ui.ChessBoardPrinter;
import webSocketMessages.serverMessages.Error;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.DrawBoard;
import webSocketMessages.userCommands.Leave;
import webSocketMessages.userCommands.MakeMove;
import webSocketMessages.userCommands.UserGameCommand;

import javax.websocket.*;
import java.net.URI;
import java.util.Scanner;

public class WSClient extends Endpoint {

//    public static void main(String[] args) throws Exception {
//        var ws = new WSClient();
//        Scanner scanner = new Scanner(System.in);
//
//        System.out.println("Enter a message you want to echo");
//        while (true) {
//            ws.send(scanner.nextLine());
//        }
//    }
    ChessBoardPrinter chessBoardPrinter = new ChessBoardPrinter();
    public Session session;

    public WSClient() throws Exception {
        URI uri = new URI("ws://localhost:8080/connect");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, uri);

        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            public void onMessage(String message) {
                System.out.println(message);
            }
        });
    }
    @OnMessage
    public void onMessage(String message) {
        ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
        switch (serverMessage.getServerMessageType()) {
            case LOAD_GAME:
                LoadGame loadGame = (LoadGame) serverMessage;
                this.loadGame(loadGame.getGame(), loadGame.getPlayerColor());
                break;
            case ERROR:
                Error error = (Error) serverMessage;
                serverError(error.getErrorMessage());
                break;
            case NOTIFICATION:
                Notification notification = (Notification) serverMessage;
                notifyClient(notification.getMessage());
        }
    }
    public void send(String msg) throws Exception {
        this.session.getBasicRemote().sendText(msg);
    }
    public void redrawChessBoard(WSClient wsClient, AuthData authData, int gameID, ChessGame.TeamColor teamColor) throws Exception {
        DrawBoard drawBoard = new DrawBoard(authData.authToken(), gameID);
        String json = new Gson().toJson(drawBoard);
        wsClient.send(json);
    }
    public void leave(WSClient wsClient, AuthData authData, int gameID) throws Exception {
        Leave leave = new Leave(authData.authToken(), gameID);
        String json = new Gson().toJson(leave);
        wsClient.send(json);
    }
    public void makeMove(WSClient wsClient, AuthData authData, int gameID, ChessMove chessMove) throws Exception {
        MakeMove makeMove = new MakeMove(authData.authToken(),gameID,chessMove);
        String json = new Gson().toJson(makeMove);
        wsClient.send(json);
    }
    @OnOpen
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void loadGame(GameData gameData, ChessGame.TeamColor playerColor) {
        if (playerColor == ChessGame.TeamColor.WHITE) {
            chessBoardPrinter.printBoardFromWhiteSide(gameData.game().getBoard());
        } else {
            chessBoardPrinter.printBoardFromBlackSide(gameData.game().getBoard());
        }
    }
    public void serverError(String errorMessage) {
        System.out.println("The server sent an error: " + errorMessage);
    }
    public void notifyClient(String message) {
        System.out.println(message);
    }
}
