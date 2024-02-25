package dataAccess;

import model.AuthData;
import model.UserData;

import java.util.*;

public class UserMemoryDAO implements UserDAO {
    static List<UserData> data;
    public UserMemoryDAO() {
        data = new ArrayList<>();
    }
    @Override
    public void clear() {
        data.clear();
    }
    @Override
    public void createUser(String username, String password, String email) {
        UserData newUser = new UserData(username, password, email);
        data.add(newUser);
    }
    @Override
    public UserData getUser(String username) {
        for (UserData user : data) {
            if (username.equals(user.username())) {
                return user;
            }
        }
        return null;
    }

}
