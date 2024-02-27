package message;

import chess.ChessGame;

public record ColorAndGameID(ChessGame.TeamColor playerColor, int gameID) {
}
