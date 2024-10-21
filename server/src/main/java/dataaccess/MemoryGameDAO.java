package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;
import java.util.Collection;
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
    public void createGame(int GameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) throws DataAccessException {
        if (!gameList.containsKey(GameID)) {
            gameList.put(GameID, new GameData(GameID, whiteUsername, blackUsername, gameName, game));
        }
        else throw new DataAccessException("Game already exists");
    }

    @Override
    public GameData getGame(int GameID) throws DataAccessException {
        if (gameList.containsKey(GameID)) {
            return gameList.get(GameID);
        }
        else throw new DataAccessException("Game not found");
    }

    @Override
    public ArrayList<GameData> listGames() {
        return new ArrayList<>(gameList.values());
    }

    @Override
    public void updateGame(int gameID, GameData updatedGame) {
        gameList.put(gameID, updatedGame);
    }
}
