package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;

public interface GameDAO {


    void clear();

    int createGame(String whiteUsername, String blackUsername, String gameName, ChessGame game)
            throws DataAccessException;

    GameData getGame(int gameId) throws DataAccessException;

    ArrayList<GameData> listGames();

    void updateGame(int gameId, GameData game);
}
