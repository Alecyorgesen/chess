package dataAccess;

import model.AuthData;
import model.UserData;

public interface UserDAO {
    void clear();
    void createUser(String username, String password, String email);
    UserData getUser(String username);
}
