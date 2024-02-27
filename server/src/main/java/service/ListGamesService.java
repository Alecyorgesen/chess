package service;

import dataAccess.*;
import error.UnauthorizedException;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.List;

public class ListGamesService {
    public static final GameDAO gameDAO = new GameMemoryDAO();
    public static final AuthDAO authDAO = new AuthMemoryDAO();

    public List<GameData> listGames(String authToken) throws UnauthorizedException {
        if (authToken == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        AuthData authData = authDAO.getAuth(authToken);
        if (authData == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        return gameDAO.listGames();
    }
}
