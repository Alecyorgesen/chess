package dataAccess;

import model.UserData;

import java.sql.Connection;
import java.sql.SQLException;

public class UserSQLDAO implements UserDAO {

    @Override
    public void clear() throws DataAccessException {
        try (Connection connection = DatabaseManager.getConnection()) {
            String sqlString = "truncate userData";
            var preparedStatement = connection.prepareStatement(sqlString);
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            throw new DataAccessException("Something broke in the database!");
        }
    }
    @Override
    public void createUser(String username, String password, String email) throws DataAccessException {
        try (var connection = DatabaseManager.getConnection()) {
            String sqlString = "insert into userData values (?, ?, ?);";
            var preparedStatement = connection.prepareStatement(sqlString);
            preparedStatement.setString(1,username);
            preparedStatement.setString(2,password);
            preparedStatement.setString(3,email);
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            throw new DataAccessException("Something broke in the database!");
        }
    }
    @Override
    public UserData getUser(String username) throws DataAccessException {
        try (Connection connection = DatabaseManager.getConnection()) {
            String sqlString = "select * from userData where username=?;";
            var preparedStatement = connection.prepareStatement(sqlString);
            preparedStatement.setString(1, username);
            preparedStatement.executeQuery();
            var resultSet = preparedStatement.getResultSet();
            String existingUsername;
            String existingPassword;
            String existingEmail;
            try {
                resultSet.next();
                existingUsername = resultSet.getString("username");
                existingPassword = resultSet.getString("password");
                existingEmail = resultSet.getString("email");
            } catch (Exception ex) {
                return null;
            }
            return new UserData(existingUsername,existingPassword,existingEmail);
        } catch (SQLException exception) {
            throw new DataAccessException("Something broke in the database!");
        }
    }
}
