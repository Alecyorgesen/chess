package service;

import dataAccess.*;
import model.AuthData;

public class ClearService {
    static AuthDAO authDAO = new AuthSQLDAO();
    static UserDAO userDAO = new UserSQLDAO();
    static GameDAO gameDAO = new GameSQLDAO();
    public ClearService() {

    }
    public void clear() throws DataAccessException {
        authDAO.clear();
        userDAO.clear();
        gameDAO.clear();
    }
}
