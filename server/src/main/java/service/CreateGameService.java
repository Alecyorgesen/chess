package service;

import dataAccess.AuthDAO;
import dataAccess.AuthMemoryDAO;
import dataAccess.GameDAO;
import dataAccess.GameMemoryDAO;
import error.BadRequestException;
import error.UnauthorizedException;
import model.*;

public class CreateGameService {
    public static final AuthDAO authDAO = new AuthMemoryDAO();
    public static final GameDAO gameDAO = new GameMemoryDAO();
    public int createGame(String authToken, String gameName) throws BadRequestException, UnauthorizedException {
        if (gameName == null || authToken == null) {
            throw new BadRequestException("Error: bad request");
        }
        AuthData authData = authDAO.getAuthUsingAuth(authToken);
        if (authData == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        GameData gameData = gameDAO.createGame(gameName);
        return gameData.gameId();
    }
}
