package service;

import dataAccess.*;
import model.AuthData;
import model.UserData;
import response.Response;

import java.util.UUID;

public class RegisterService {
    static UserDAO userDAO;
    static AuthDAO authDAO;

    public RegisterService() {
        userDAO = new UserMemoryDAO();
        authDAO = new AuthMemoryDAO();
    }

    public AuthData register(UserData userData) {
        String existingUserName = getUsername(userData.username());
        if (existingUserName != null) {
            return null;
        }
        userDAO.createUser(userData.username(), userData.password(), userData.password());
        authDAO.createAuth(userData.username());
        AuthData authData = new AuthData(UUID.randomUUID().toString(), userData.username());

        return authData;
    }

    private String getUsername(String username) {
        UserData user = userDAO.getUser(username);
        return user.username();
    }
}
