package dataAccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.AuthData;
import model.GameData;

import java.sql.Connection;
import java.sql.SQLException;
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
            var preparedStatement = connection.prepareStatement(sqlString);
            preparedStatement.setString(1,gameName);
            ChessGame chessGame = new ChessGame();
            String chessGameJson = new Gson().toJson(chessGame);
            preparedStatement.setString(2,chessGameJson);
            preparedStatement.executeUpdate();
            // RETURN HERE AFTER SETTING UP GETGAME
            return new GameData(,,,,);
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

    }
    @Override
    public void updateGame(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame gameData)  throws DataAccessException {

    }
}
