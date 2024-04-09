package webSocketMessages.serverMessages;

import chess.ChessGame;
import model.GameData;

public class LoadGame extends ServerMessage {
    GameData game;
    ChessGame.TeamColor playerColor;
    public LoadGame(ServerMessageType serverMessage, GameData game, ChessGame.TeamColor playerColor) {
        super(serverMessage);
        this.serverMessageType = ServerMessageType.LOAD_GAME;
        this.game = game;
        this.playerColor = playerColor;
    }
    public GameData getGame() {
        return game;
    }

    public ChessGame.TeamColor getPlayerColor() {
        return playerColor;
    }
}
