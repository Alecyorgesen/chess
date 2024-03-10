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
            String sqlString = "select username from userData where username=?;";
            var preparedStatement = connection.prepareStatement(sqlString);
            preparedStatement.setString(1, username);
            preparedStatement.executeUpdate();
            var resultSet = preparedStatement.getResultSet();
            return resultSet.getString("username"); //fix this thing
        } catch (SQLException exception) {
            throw new DataAccessException("Something broke in the database!");
        }
    }
}
