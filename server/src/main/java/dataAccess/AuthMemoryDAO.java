package dataAccess;

import model.AuthData;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AuthMemoryDAO implements AuthDAO {
    static List<AuthData> data;
    public AuthMemoryDAO() {
        data = new ArrayList<>();
    }
    public void clear() {
        data.clear();
    }
    @Override
    public AuthData getAuth(String username) {
        for (AuthData authData : data) {
            if (authData.username().equals(username)) {
                return authData;
            }
        }
        return null;
    }
    @Override
    public AuthData createAuth(String username) {
        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(authToken, username);
        data.add(authData);
        return authData;
    }
}
