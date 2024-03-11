package service;

import dataAccess.*;
import error.UnauthorizedException;
import message.ListGamesResponse;
import model.AuthData;
import model.GameData;

import java.util.ArrayList;
import java.util.List;

public class ListGamesService {
    public static GameDAO gameDAO = new GameMemoryDAO();
    public static AuthDAO authDAO = new AuthSQLDAO();

    public ListGamesResponse listGames(String authToken) throws UnauthorizedException, DataAccessException {
        if (authToken == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        AuthData authData = authDAO.getAuthUsingAuth(authToken);
        if (authData == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        List<GameData> listOfGames = gameDAO.listGames();
        List<GameData> emptyList = new ArrayList<>();
        ListGamesResponse games = new ListGamesResponse(emptyList);
        for (GameData gameData : listOfGames) {
            games = games.addGameInformation(gameData);
        }
        return games;
    }
}
