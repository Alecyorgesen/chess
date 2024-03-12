package dataAccess;

import model.AuthData;
import model.UserData;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;

public class AuthSQLDAO implements AuthDAO {
    static {
        try (Connection connection = DatabaseManager.getConnection()) {
            String sqlString = "create table authData(auth varchar(64), username varchar(128));";
            var preparedStatement = connection.prepareStatement(sqlString);
            preparedStatement.executeUpdate();
        } catch (Exception exception) {
            System.out.println("Could not create authData table");
        }
    }
    @Override
    public void clear() throws DataAccessException {
        try (var connection = DatabaseManager.getConnection()) {
            String sqlString = "truncate authData";
            var preparedStatement = connection.prepareStatement(sqlString);
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            throw new DataAccessException(exception.getMessage());
        }
    }
    @Override
    public AuthData createAuth(String username) throws DataAccessException {
        try (var connection = DatabaseManager.getConnection()) {
            String sqlString = "insert into authData values (?, ?);";
            var preparedStatement = connection.prepareStatement(sqlString);
            String authToken = UUID.randomUUID().toString();
            preparedStatement.setString(1,authToken);
            preparedStatement.setString(2,username);
            preparedStatement.executeUpdate();
            return new AuthData(authToken,username);
        } catch (SQLException exception) {
            throw new DataAccessException(exception.getMessage());
        }
    }
    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        try (var connection = DatabaseManager.getConnection()) {
            String sqlString = "delete from authData where auth=?";
            var preparedStatement = connection.prepareStatement(sqlString);
            preparedStatement.setString(1,authToken);
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            throw new DataAccessException(exception.getMessage());
        }
    }
    @Override
    public AuthData getAuthUsingAuth(String authToken) throws DataAccessException {
        try (Connection connection = DatabaseManager.getConnection()) {
            String sqlString = "select * from authData where auth=?;";
            var preparedStatement = connection.prepareStatement(sqlString);
            preparedStatement.setString(1, authToken);
            preparedStatement.executeQuery();
            var resultSet = preparedStatement.getResultSet();
            String existingAuth;
            String existingUsername;
            try {
                resultSet.next();
                existingAuth = resultSet.getString("auth");
                existingUsername = resultSet.getString("username");
            } catch (Exception ex) {
                return null;
            }
            return new AuthData(existingAuth,existingUsername);
        } catch (SQLException exception) {
            throw new DataAccessException(exception.getMessage());
        }
    }
}
