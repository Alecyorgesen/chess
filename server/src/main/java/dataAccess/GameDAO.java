package dataAccess;

import chess.ChessGame;
import model.GameData;

import java.util.List;

public interface GameDAO {
    void clear();
    GameData createGame(String gameName);
    List<GameData> listGames();

    GameData getGame(int gameID);

    void updateGame(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame gameData);
}
