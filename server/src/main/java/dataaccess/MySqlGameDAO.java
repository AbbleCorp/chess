package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.sql.SQLException;
import java.util.ArrayList;

public class MySqlGameDAO implements GameDAO {


    @Override
    public void clear() {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("DELETE FROM game")) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createGame(int gameId, String whiteUsername, String blackUsername, String gameName, ChessGame game) throws DataAccessException {

    }

    @Override
    public GameData getGame(int gameId) throws DataAccessException {
        return null;
    }

    @Override
    public ArrayList<GameData> listGames() {
        return null;
    }

    @Override
    public void updateGame(int gameId, GameData game) {

    }
}
