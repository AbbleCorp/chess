package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.Collection;

public class MemoryGameDAO implements GameDAO {

    @Override
    public void createGame(int GameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) throws DataAccessException {
        //TODO: implement, may need to change method type
    }

    @Override
    public GameData getGame(int GameID) throws DataAccessException {
        //TODO: implement
        throw new DataAccessException("not implemented");
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        //TODO: implement, need to change return type to whatever Collection interface
        throw new DataAccessException("not implemented");
    }

    @Override
    public void updateGame(int GameID) throws DataAccessException {
        //TODO: implement, change return type?
        throw new DataAccessException("not implemented");
    }
}
