package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;

public interface GameDAO {


    void clear();

    void createGame(int GameID, String whiteUsername, String blackUsername, String gameName, ChessGame game)
            throws DataAccessException;

    GameData getGame(int GameID) throws DataAccessException;

    ArrayList<GameData> listGames();

    void updateGame(int GameID, GameData game);
}
