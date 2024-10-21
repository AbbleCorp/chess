package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MemoryGameDAO implements GameDAO {
    Map<Integer, GameData> gameList;

    public MemoryGameDAO() {
        gameList = new HashMap<>();
    }

    @Override
    public void clear() {
        gameList.clear();
    }

    @Override
    public void createGame(int gameId, String whiteUsername, String blackUsername, String gameName, ChessGame game) throws DataAccessException {
        if (!gameList.containsKey(gameId)) {
            gameList.put(gameId, new GameData(gameId, whiteUsername, blackUsername, gameName, game));
        }
        else {throw new DataAccessException("Game already exists");}
    }

    @Override
    public GameData getGame(int gameId) throws DataAccessException {
        if (gameList.containsKey(gameId)) {
            return gameList.get(gameId);
        }
        else {throw new DataAccessException("Game not found");}
    }

    @Override
    public ArrayList<GameData> listGames() {
        return new ArrayList<>(gameList.values());
    }

    @Override
    public void updateGame(int gameId, GameData updatedGame) {
        gameList.put(gameId, updatedGame);
    }
}
