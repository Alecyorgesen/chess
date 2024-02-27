package service;

import chess.ChessGame;
import dataAccess.AuthDAO;
import dataAccess.AuthMemoryDAO;
import dataAccess.GameDAO;
import dataAccess.GameMemoryDAO;
import error.AlreadyTakenException;
import error.BadRequestException;
import error.UnauthorizedException;
import message.JoinGameRequest;
import model.AuthData;
import model.GameData;

public class JoinGameService {
    public static final AuthDAO authDAO = new AuthMemoryDAO();
    public static final GameDAO gameDAO = new GameMemoryDAO();
    public void joinGame(String authToken, JoinGameRequest colorAndGameID) throws BadRequestException, UnauthorizedException, AlreadyTakenException {
        if (authToken == null || colorAndGameID == null) {
            throw new BadRequestException("Error: bad request");
        }
        ChessGame.TeamColor playerColor = colorAndGameID.playerColor();
        int gameID = colorAndGameID.gameID();
        GameData gameData = gameDAO.getGame(gameID);

        if (gameData == null) {
            throw new BadRequestException("Error: bad request");
        }
        AuthData authData = authDAO.getAuthUsingAuth(authToken);
        if (authData == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        if (!authToken.equals(authData.authToken())) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        if (playerColor == ChessGame.TeamColor.WHITE) {
            if (gameData.whiteUsername() == null) {
                gameData = gameData.setWhiteUsername(authData.username());
            } else {
                throw new AlreadyTakenException("Error: already taken");
            }
        } else if (playerColor == ChessGame.TeamColor.BLACK){
            if (gameData.blackUsername() == null) {
                gameData = gameData.setBlackUsername(authData.username());

            } else {
                throw new AlreadyTakenException("Error: already taken");
            }
        }
        gameDAO.updateGame(gameData.gameId(), gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), gameData.game());
    }
}
