package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;

public interface GameDAO {


    void clear();

    void createGame(int GameId, String whiteUsername, String blackUsername, String gameName, ChessGame game)
            throws DataAccessException;

    GameData getGame(int GameId) throws DataAccessException;

    ArrayList<GameData> listGames();

    void updateGame(int GameId, GameData game);
}
