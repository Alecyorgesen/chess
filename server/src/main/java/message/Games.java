package message;

import model.GameData;

import java.util.List;

public record Games(List<GameData> listOfGames) {

    public Games addGameInformation(GameData gameData) {
        listOfGames.add(new GameData(gameData.gameId(),gameData.whiteUsername(),gameData.blackUsername(),gameData.gameName(),null));
        return new Games(listOfGames);
    }
}
