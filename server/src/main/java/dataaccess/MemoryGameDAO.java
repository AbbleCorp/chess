package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class MemoryGameDAO implements GameDAO {
    Map<Integer, GameData> gameList;

    @Override
    public void clear() {
        gameList.clear();
    }

    @Override
    public void createGame(int GameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) throws DataAccessException {
        gameList.put(GameID, new GameData(GameID,whiteUsername,blackUsername,gameName,game));
    }

    @Override
    public GameData getGame(int GameID) throws DataAccessException {
        return gameList.get(GameID);
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        Collection<GameData> gameDataList = new ArrayList<>(gameList.values());
        return gameDataList;
    }

    @Override
    public void updateGame(int GameID) throws DataAccessException {
        //TODO: implement, change return type?
        throw new DataAccessException("not implemented");
    }
}
