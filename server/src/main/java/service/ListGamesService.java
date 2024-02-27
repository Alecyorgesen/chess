package service;

import dataAccess.*;
import error.UnauthorizedException;
import message.Games;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.ArrayList;
import java.util.List;

public class ListGamesService {
    public static final GameDAO gameDAO = new GameMemoryDAO();
    public static final AuthDAO authDAO = new AuthMemoryDAO();

    public Games listGames(String authToken) throws UnauthorizedException {
        if (authToken == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        AuthData authData = authDAO.getAuthUsingAuth(authToken);
        if (authData == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        List<GameData> listOfGames = gameDAO.listGames();
        List<GameData> emptyList = new ArrayList<>();
        Games games = new Games(emptyList);
        for (GameData gameData : listOfGames) {
            games = games.addGameInformation(gameData);
        }
        return games;
    }
}
