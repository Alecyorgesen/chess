package service;

import dataAccess.*;
import dataAccess.UserMemoryDAO;
import error.UnauthorizedException;
import model.AuthData;
import model.UserData;

public class LoginService {
    static UserDAO userDAO = new UserMemoryDAO();
    static AuthDAO authDAO = new AuthMemoryDAO();

    public AuthData login(UserData userData) throws UnauthorizedException {
        if (userData.password() == null || userData.username() == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        UserData existingUser = userDAO.getUser(userData.username());
        if (existingUser == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        if (!existingUser.password().equals(userData.password())) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        return authDAO.createAuth(userData.username());
    }
}
