package service;

import dataAccess.AuthDAO;
import dataAccess.AuthMemoryDAO;
import error.UnauthorizedException;
import model.AuthData;
import passoffTests.testClasses.TestModels;

import java.util.Map;
import java.util.Set;

public class LogoutService {
    static AuthDAO authDAO = new AuthMemoryDAO();

    public void logout(String authToken) throws UnauthorizedException {
        if (authToken == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        AuthData authData = authDAO.getAuthUsingAuth(authToken);
        if (authData == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        authDAO.deleteAuth(authToken);
    }
}
