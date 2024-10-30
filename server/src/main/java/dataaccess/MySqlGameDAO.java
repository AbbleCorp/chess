package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;
import model.GameDataAutoId;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MySqlGameDAO implements GameDAO {


    @Override
    public void clear() {
        String[] clearStatements = {"DELETE FROM game", "ALTER TABLE game AUTO_INCREMENT = 1"};
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : clearStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }}
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int createGame(int gameId, String whiteUsername, String blackUsername, String gameName, ChessGame game) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(
                    "INSERT INTO game (whiteUsername, blackUsername, gameName,game) VALUES(?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, whiteUsername);
                preparedStatement.setString(2, blackUsername);
                preparedStatement.setString(3, gameName);
                var Serializer = new Gson();
                String jsonGame = Serializer.toJson(new GameDataAutoId(whiteUsername,blackUsername,gameName,game));
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public GameData getGame(int gameId) throws DataAccessException {
        return null;
    }

    @Override
    public ArrayList<GameData> listGames() {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT * FROM game", Statement.RETURN_GENERATED_KEYS)) {
                var resultSet = preparedStatement.executeQuery();
                ArrayList<GameData> gameList = new ArrayList<>();
                GameData data = null;
                var Serializer = new Gson();
                while (resultSet.next()) {
                    var gameId = resultSet.getInt("gameId");
                    var whiteUser = resultSet.getString("whiteUsername");
                    var blackUser = resultSet.getString("blackUsername");
                    var gameName = resultSet.getString("gameName");
                    ChessGame game = Serializer.fromJson(resultSet.getString("game"),ChessGame.class);
                    data = new GameData(gameId,whiteUser,blackUser,gameName,game);
                    gameList.add(data);
                }
                return gameList;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateGame(int gameId, GameData game) {

    }
}
