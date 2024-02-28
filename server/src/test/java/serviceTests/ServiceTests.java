package serviceTests;

import chess.ChessGame;
import dataAccess.*;
import error.AlreadyTakenException;
import error.BadRequestException;
import error.UnauthorizedException;
import message.JoinGameRequest;
import message.ListGamesResponse;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import passoffTests.testClasses.TestException;
import service.*;
public class ServiceTests {
    @Test
    @Order(1)
    public void registerTests() throws TestException {
        UserData userData = new UserData("lDrac360l", "notPassword", "someone@gmail.com");
        RegisterService registerService = new RegisterService();
        try {
            AuthData authData = registerService.register(userData);
            Assertions.assertEquals("lDrac360l", authData.username());
        } catch (Exception exception) {
            throw new TestException("It threw it error when it shouldn't have");
        }

        userData = new UserData("Meow",null, "somebody@gmail.com");
        try {
            AuthData authData = registerService.register(userData);
            throw new TestException("Should have thrown an error");
        } catch (Exception exception) {
            Assertions.assertEquals(BadRequestException.class, exception.getClass());
        }
    }
    @Test
    @Order(2)
    public void loginTests() throws TestException {
        RegisterService registerService = new RegisterService();
        LoginService loginService = new LoginService();

        UserData userData = new UserData("lDrac360l", "myPassWord", "aVerySpecificEmail");
        try {
            registerService.register(userData);
        } catch (Exception e) {
            throw new TestException("Shouldn't have thrown an exception");
        }
        try {
            AuthData authData = loginService.login(userData);
            AuthData correctAuthData = new AuthData("randomString", "lDrac360l");
            Assertions.assertEquals(correctAuthData.username(), authData.username());
        } catch (Exception exception) {
            throw new TestException("Should not have thrown the exception: " + exception.getMessage());
        }
        userData = new UserData("Tom","pass","mail");

        try {
            loginService.login(userData);
            throw new TestException("Didn't throw an exception when should have thrown unAuthorized");
        } catch (Exception exception) {
            Assertions.assertEquals(UnauthorizedException.class, exception.getClass());
        }
    }

    @Test
    @Order(3)
    public void logoutTest() throws TestException {
        RegisterService registerService = new RegisterService();
        LoginService loginService = new LoginService();
        LogoutService logoutService = new LogoutService();

        UserData userData = new UserData("lDrac360l", "myPassWord", "aVerySpecificEmail");
        AuthData authData;
        try {
            authData = registerService.register(userData);
        } catch (Exception e) {
            throw new TestException("Shouldn't have thrown an exception");
        }
        try {
            logoutService.logout(authData.authToken());
        } catch (Exception e) {
            throw new TestException("Should not have thrown: " + e.getMessage());
        }

        try {
            loginService.login(userData);
        } catch (Exception e) {
            throw new TestException("Should not have thrown: " + e.getMessage());
        }
        userData = new UserData("lDrac360l", "notThis", "aVerySpecificEmail");
        try {
            loginService.login(userData);
        } catch (Exception e) {
            Assertions.assertEquals(UnauthorizedException.class, e.getClass());
        }
    }
    @Test
    @Order(4)
    public void createGameTests() {
        RegisterService registerService = new RegisterService();
        LoginService loginService = new LoginService();
        CreateGameService createGameService = new CreateGameService();
        GameDAO gameDAO = new GameMemoryDAO();

        UserData userData = new UserData("lDrac360l", "myPassWord", "email");
        AuthData authData;
        try {
            authData = registerService.register(userData);
        } catch (Exception e) {
            throw new TestException("Should not have thrown: " + e.getMessage());
        }
        try {
            int gameID = createGameService.createGame(authData.authToken(),"The Best Game");
            GameData gameData = gameDAO.getGame(gameID);
            Assertions.assertEquals("The Best Game", gameData.gameName());
        } catch (Exception e) {
            throw new TestException("Shouldn't have thrown: " + e.getMessage());
        }
        try {
            int gameID = createGameService.createGame("no sirree","The Best Game");
            throw new TestException("should have thrown an unAuthorizedException");
        } catch (Exception e) {
            Assertions.assertEquals(UnauthorizedException.class, e.getClass());
        }
    }

    @Test
    @Order(5)
    public void joinGameTests() throws TestException {
        RegisterService registerService = new RegisterService();
        CreateGameService createGameService = new CreateGameService();
        JoinGameService joinGameService = new JoinGameService();
        GameDAO gameDAO = new GameMemoryDAO();

        UserData userData = new UserData("lDrac360l", "myPassWord", "email");
        AuthData authData;
        int gameID;
        try {
            authData = registerService.register(userData);
            gameID = createGameService.createGame(authData.authToken(), "Cool Game");
        } catch (Exception e) {
            throw new TestException("Should not have thrown: " + e.getMessage());
        }
        JoinGameRequest colorAndGameID = new JoinGameRequest(ChessGame.TeamColor.WHITE, gameID);
        try {
            joinGameService.joinGame(authData.authToken(), colorAndGameID);
            GameData gameData = gameDAO.getGame(gameID);
            Assertions.assertEquals("lDrac360l", gameData.whiteUsername());
        } catch (Exception e) {
            throw new TestException("Should not have thrown: " + e.getMessage());
        }

        try {
            joinGameService.joinGame(authData.authToken(), colorAndGameID);
            throw new TestException("Should have thrown AlreadyTakenException");
        } catch (Exception e) {
            Assertions.assertEquals(AlreadyTakenException.class, e.getClass());
        }
    }

    @Test
    @Order(6)
    public void listGamesTests() throws TestException {
        RegisterService registerService = new RegisterService();
        CreateGameService createGameService = new CreateGameService();
        JoinGameService joinGameService = new JoinGameService();
        ListGamesService listGamesService = new ListGamesService();
        GameDAO gameDAO = new GameMemoryDAO();

        UserData userData = new UserData("lDrac360l", "myPassWord", "email");
        AuthData authData;
        int gameID;
        JoinGameRequest colorAndGameID;
        try {
            authData = registerService.register(userData);
            gameID = createGameService.createGame(authData.authToken(), "Cool Game");
            colorAndGameID = new JoinGameRequest(ChessGame.TeamColor.BLACK, gameID);
            joinGameService.joinGame(authData.authToken(), colorAndGameID);
        } catch (Exception e) {
            throw new TestException("Should not have thrown: " + e.getMessage());
        }
        ListGamesResponse games;
        try {
            games = listGamesService.listGames(authData.authToken());
        } catch (Exception e) {
            throw new TestException("Should not have thrown: " + e.getMessage());
        }
    }

    @Test
    @Order(7)
    public void clearTests() throws TestException {
        RegisterService registerService = new RegisterService();
        CreateGameService createGameService = new CreateGameService();
        JoinGameService joinGameService = new JoinGameService();
        ClearService clearService = new ClearService();
        GameDAO gameDAO = new GameMemoryDAO();
        UserDAO userDAO = new UserMemoryDAO();
        AuthDAO authDAO = new AuthMemoryDAO();

        UserData userData = new UserData("lDrac360l", "myPassWord", "email");
        AuthData authData;
        int gameID;
        JoinGameRequest colorAndGameID;
        try {
            authData = registerService.register(userData);
            gameID = createGameService.createGame(authData.authToken(), "Cool Game");
            colorAndGameID = new JoinGameRequest(ChessGame.TeamColor.BLACK, gameID);
            joinGameService.joinGame(authData.authToken(), colorAndGameID);
        } catch (Exception e) {
            throw new TestException("Should not have thrown: " + e.getMessage());
        }

        try {
            clearService.clear();
            Assertions.assertNull(gameDAO.getGame(gameID));
            Assertions.assertNull(userDAO.getUser(userData.username()));
            Assertions.assertNull(authDAO.getAuthUsingAuth(authData.authToken()));
        } catch (Exception e) {
            throw new TestException("Should not have thrown: " + e.getMessage());
        }
    }
}