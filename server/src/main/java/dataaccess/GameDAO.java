package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.Collection;

public interface GameDAO {


    void clear() throws DataAccessException;

    void createGame(int GameID, String whiteUsername, String blackUsername, String gameName, ChessGame game)
            throws DataAccessException;

    GameData getGame(int GameID) throws DataAccessException;

    Collection<GameData> listGames();

    void updateGame(int GameID, GameData game);
}
