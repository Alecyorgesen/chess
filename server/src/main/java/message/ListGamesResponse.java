package message;

import model.GameData;

import java.util.List;

public record ListGamesResponse(List<GameData> games) {

    public ListGamesResponse addGameInformation(GameData gameData) {
        games.add(new GameData(gameData.gameID(),gameData.whiteUsername(),gameData.blackUsername(),gameData.gameName(),null));
        return new ListGamesResponse(games);
    }
}
