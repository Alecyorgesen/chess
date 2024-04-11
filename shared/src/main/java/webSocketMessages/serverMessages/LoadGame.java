package webSocketMessages.serverMessages;

import chess.ChessBoard;
import chess.ChessGame;
import model.GameData;

public class LoadGame extends ServerMessage {
    GameData game;

    //    ChessGame.TeamColor playerColor;
    public LoadGame(GameData game) {
        super(ServerMessageType.LOAD_GAME);
        this.game = game;
//        this.playerColor = playerColor;
    }

    public GameData getGame() {
        return game;
    }
}

