package dataAccess;

import model.AuthData;

public interface AuthDAO {
    void clear();
    AuthData createAuth(String username);
    void deleteAuth(String authToken);
    AuthData getAuthUsingAuth(String authToken);
}
