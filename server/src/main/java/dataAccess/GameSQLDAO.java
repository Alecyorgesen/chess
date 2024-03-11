package dataAccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.AuthData;
import model.GameData;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class GameSQLDAO implements GameDAO {
    @Override
    public void clear() throws DataAccessException {
        try (var connection = DatabaseManager.getConnection()) {
            String sqlString = "truncate gameData";
            var preparedStatement = connection.prepareStatement(sqlString);
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            throw new DataAccessException(exception.getMessage());
        }
    }
    @Override
    public GameData createGame(String gameName) throws DataAccessException {
        try (var connection = DatabaseManager.getConnection()) {
            String sqlString = "insert into gameData (whiteUsername, blackUsername, gameName, game) values (null, null, ?, ?);";
            var preparedStatement = connection.prepareStatement(sqlString, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1,gameName);
            ChessGame chessGame = new ChessGame();
            String chessGameJson = new Gson().toJson(chessGame);
            preparedStatement.setString(2,chessGameJson);
            preparedStatement.executeUpdate();
            var generatedKeys = preparedStatement.getGeneratedKeys();
            generatedKeys.next();
            int gameID = generatedKeys.getInt(1);
            return new GameData(gameID,null,null,gameName,chessGame);
        } catch (SQLException exception) {
            throw new DataAccessException(exception.getMessage());
        }
    }
    @Override
    public List<GameData> listGames() throws DataAccessException {
        try (Connection connection = DatabaseManager.getConnection()) {
            String sqlString = "select * from gameData";
            var preparedStatement = connection.prepareStatement(sqlString);
            preparedStatement.executeQuery();
            var resultSet = preparedStatement.getResultSet();
            int gameID;
            String whiteUsername;
            String blackUsername;
            String gameName;
            String game;
            List<GameData> listOfGames = new ArrayList<>();
            try {
                while (resultSet.next()) {
                    gameID = resultSet.getInt("gameID");
                    whiteUsername = resultSet.getString("whiteUsername");
                    blackUsername = resultSet.getString("blackUsername");
                    gameName = resultSet.getString("gameName");
                    game = resultSet.getString("game");
                    ChessGame chessGame = new Gson().fromJson(game,ChessGame.class);
                    listOfGames.add(new GameData(gameID,whiteUsername,blackUsername,gameName,chessGame));
                }
            } catch (Exception ex) {
                return null;
            }
            return listOfGames;
        } catch (SQLException exception) {
            throw new DataAccessException(exception.getMessage());
        }
    }
    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        try (Connection connection = DatabaseManager.getConnection()) {
            String sqlString = "select * from gameData where gameID=?;";
            var preparedStatement = connection.prepareStatement(sqlString);
            preparedStatement.setInt(1, gameID);
            preparedStatement.executeQuery();
            var resultSet = preparedStatement.getResultSet();
            String whiteUsername;
            String blackUsername;
            String gameName;
            String game;
            try {
                resultSet.next();
                whiteUsername = resultSet.getString("whiteUsername");
                blackUsername = resultSet.getString("blackUsername");
                gameName = resultSet.getString("gameName");
                game = resultSet.getString("game");
            } catch (Exception ex) {
                return null;
            }
            ChessGame chessGame = new Gson().fromJson(game,ChessGame.class);
            return new GameData(gameID,whiteUsername,blackUsername,gameName,chessGame);
        } catch (SQLException exception) {
            throw new DataAccessException(exception.getMessage());
        }
    }
    @Override
    public void updateGame(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame gameData)  throws DataAccessException {
        try (var connection = DatabaseManager.getConnection()) {
            String sqlString = "replace into gameData (gameID, whiteUsername, blackUsername, gameName, game) values (?, ?, ?, ?, ?);";
            var preparedStatement = connection.prepareStatement(sqlString);
            preparedStatement.setInt(1,gameID);
            preparedStatement.setString(2,whiteUsername);
            preparedStatement.setString(3,blackUsername);
            preparedStatement.setString(4,gameName);
            String chessGameJson = new Gson().toJson(gameData);
            preparedStatement.setString(5,chessGameJson);
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            throw new DataAccessException(exception.getMessage());
        }
    }
}
