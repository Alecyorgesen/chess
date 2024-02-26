package dataAccess;

import model.GameData;

import java.util.List;

public interface GameDAO {
    void clear();
    GameData createGame(String gameName);
    List<GameData> listGames();
}
