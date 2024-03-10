package service;

import dataAccess.*;
import error.BadRequestException;
import error.UnauthorizedException;
import model.*;

public class CreateGameService {
    public static AuthDAO authDAO = new AuthMemoryDAO();
    public static GameDAO gameDAO = new GameMemoryDAO();
    public int createGame(String authToken, String gameName) throws BadRequestException, UnauthorizedException, DataAccessException {
        if (gameName == null || authToken == null) {
            throw new BadRequestException("Error: bad request");
        }
        AuthData authData = authDAO.getAuthUsingAuth(authToken);
        if (authData == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        GameData gameData = gameDAO.createGame(gameName);
        return gameData.gameID();
    }
}
