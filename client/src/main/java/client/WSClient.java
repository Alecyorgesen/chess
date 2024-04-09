package client;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import model.AuthData;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.DrawBoard;
import webSocketMessages.userCommands.Leave;
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
        ServerMessage serverMessage = new Gson().fromJson(message, serverMessage.class);
        switch (serverMessage.getServerMessageType()) {
            case ServerMessage.ServerMessageType.LOAD_GAME -> loadGame();
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
    public void makeMove(WSClient wsClient, int gameID, ChessMove chessMove) {

    }
    @OnOpen
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void loadGame() {

    }
}
