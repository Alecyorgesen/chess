package service;

import dataAccess.*;
import error.AlreadyTakenException;
import error.BadRequestException;
import model.AuthData;
import model.UserData;
import message.ErrorMessage;

import javax.xml.crypto.Data;
import java.util.UUID;

public class RegisterService {
    static UserDAO userDAO = new UserSQLDAO();
    static AuthDAO authDAO = new AuthMemoryDAO();

    public AuthData register(UserData userData) throws AlreadyTakenException, BadRequestException, DataAccessException {
        if (userData.username() == null || userData.email() == null || userData.password() == null) {
            throw new BadRequestException("Error: bad request");
        }
        String existingUserName = getUsername(userData.username());
        if (existingUserName != null) {
            throw new AlreadyTakenException("Error: already taken");
        }
        userDAO.createUser(userData.username(), userData.password(), userData.email());
        authDAO.createAuth(userData.username());

//        return new AuthData(UUID.randomUUID().toString(), userData.username());
        return authDAO.createAuth(userData.username());
    }

    private String getUsername(String username) throws DataAccessException {
        UserData user = userDAO.getUser(username);
        if (user == null) {
            return null;
        }
        return user.username();
    }
}
