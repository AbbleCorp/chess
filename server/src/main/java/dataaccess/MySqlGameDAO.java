package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class MySqlGameDAO implements GameDAO {


    @Override
    public void clear() {
        String[] clearStatements = {"DELETE FROM game","ALTER TABLE game AUTO_INCREMENT = 1"};
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : clearStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }}
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int createGame(String whiteUsername, String blackUsername, String gameName, ChessGame game) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(
                    "INSERT INTO game (whiteUsername, blackUsername, gameName,game) VALUES(?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, whiteUsername);
                preparedStatement.setString(2, blackUsername);
                preparedStatement.setString(3, gameName);
                var Serializer = new Gson();
                String jsonGame = Serializer.toJson(game);
                preparedStatement.setString(4,jsonGame);
                int id = 0;
                if (preparedStatement.executeUpdate() == 1) {
                    try(ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                        generatedKeys.next();
                        id = generatedKeys.getInt(1);
                    }
                }
                return id;
            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public GameData getGame(int gameId) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT * FROM game WHERE gameID=?", Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setInt(1, gameId);
                var resultSet = preparedStatement.executeQuery();
                GameData data = null;
                if (resultSet.next()) {
                    data = getGameData(resultSet);
                }
                return data;
            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    private GameData getGameData(ResultSet resultSet) throws SQLException {
        var Serializer = new Gson();
        var id = resultSet.getInt("gameId");
        var whiteUser = resultSet.getString("whiteUsername");
        var blackUser = resultSet.getString("blackUsername");
        var gameName = resultSet.getString("gameName");
        ChessGame game = Serializer.fromJson(resultSet.getString("game"),ChessGame.class);
        return new GameData(id,whiteUser,blackUser,gameName,game);
    }

    @Override
    public ArrayList<GameData> listGames() {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT * FROM game", Statement.RETURN_GENERATED_KEYS)) {
                var resultSet = preparedStatement.executeQuery();
                ArrayList<GameData> gameList = new ArrayList<>();
                GameData data;
                while (resultSet.next()) {
                    data = getGameData(resultSet);
                    gameList.add(data);
                }
                return gameList;
            }
    } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateGame(int gameId, GameData game) {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(
                    "UPDATE game SET whiteUsername=?, blackUsername=?, game=? WHERE gameId=?")) {
                preparedStatement.setString(1,game.whiteUsername());
                preparedStatement.setString(2,game.blackUsername());
                var Serializer = new Gson();
                String jsonGame = Serializer.toJson(game.game());
                preparedStatement.setString(3,jsonGame);
                preparedStatement.setInt(4,gameId);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
