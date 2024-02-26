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
        return new GameData(currentGameID, null, null, gameName, new ChessGame());
    }

    @Override
    public List<GameData> listGames() {
        return data;
    }
}
