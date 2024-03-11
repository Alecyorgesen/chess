package dataAccess;

import model.AuthData;

public interface AuthDAO {
    void clear() throws DataAccessException;
    AuthData createAuth(String username) throws DataAccessException;
    void deleteAuth(String authToken) throws DataAccessException;
    AuthData getAuthUsingAuth(String authToken) throws DataAccessException;
}
