package dataAccess;

import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;
import java.util.List;

public class GameMemoryDAO implements GameDAO {
    static List<GameData> data;
    static int currentGameID = 0;
    public GameMemoryDAO() {
        data = new ArrayList<>();
    }
    @Override
    public void clear() {
        data.clear();
    }

    @Override
    public GameData createGame(String gameName) {
        GameData newGame = new GameData(currentGameID, null, null, gameName, new ChessGame());
        data.add(newGame);
        currentGameID += 1;
        return newGame;
    }

    @Override
    public List<GameData> listGames() {
        return data;
    }

    @Override
    public GameData getGame(int gameID) {
        for (GameData gameData : data) {
            if (gameData.gameID() == gameID) {
                return gameData;
            }
        }
        return null;
    }

    @Override
    public void updateGame(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame updatedGame) {
        GameData oldGame = null;
        for (GameData gameData : data) {
            if (gameData.gameID() == gameID) {
                oldGame = gameData;
            }
        }
        int index = data.indexOf(oldGame);
        data.set(index, new GameData(gameID, whiteUsername, blackUsername, gameName, updatedGame));
    }
}
