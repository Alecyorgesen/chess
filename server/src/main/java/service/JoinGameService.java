package service;

import chess.ChessGame;
import dataAccess.*;
import error.AlreadyTakenException;
import error.BadRequestException;
import error.UnauthorizedException;
import message.JoinGameRequest;
import model.AuthData;
import model.GameData;

public class JoinGameService {
    public static AuthDAO authDAO = new AuthSQLDAO();
    public static GameDAO gameDAO = new GameSQLDAO();
    public void joinGame(String authToken, JoinGameRequest colorAndGameID) throws BadRequestException, UnauthorizedException, AlreadyTakenException, DataAccessException {
        if (authToken == null || colorAndGameID == null) {
            throw new BadRequestException("Error: bad request");
        }
        ChessGame.TeamColor playerColor = null;
        if (colorAndGameID.playerColor() != null) {
            if (colorAndGameID.playerColor().equals("WHITE")) {
                playerColor = ChessGame.TeamColor.WHITE;
            } else if (colorAndGameID.playerColor().equals("BLACK")) {
                playerColor = ChessGame.TeamColor.BLACK;
            } else {
                throw new BadRequestException("Bad request it would seem");
            }
        }

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
        gameDAO.updateGame(gameData.gameID(), gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), gameData.game());
    }
}
