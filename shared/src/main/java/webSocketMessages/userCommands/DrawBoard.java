package webSocketMessages.userCommands;

import chess.ChessGame;

public class DrawBoard extends UserGameCommand {
    int gameID;
    public DrawBoard(String authToken, int gameID) {
        super(authToken);
        this.commandType = CommandType.DRAW_BOARD;
        this.gameID = gameID;
    }
}
