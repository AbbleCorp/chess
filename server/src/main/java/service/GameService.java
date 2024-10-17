package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.AuthDAO;
import model.GameData;

import java.util.Collection;
import java.util.Objects;


public class GameService {
    private final GameDAO gameData;
    private final AuthDAO authData;
    int gameID = 0000;

    GameService(GameDAO gameData, AuthDAO authData) {
        this.gameData = gameData;
        this.authData = authData;
    }

    public void clearGameData() throws DataAccessException {
        gameData.clear();
    }

    public void joinGame(String authToken, String playerColor, int gameID) throws DataAccessException {
        if (authData.getAuth(authToken)) {
            String username = authData.getUsername(authToken);
            GameData originalGame = gameData.getGame(gameID);
            if (Objects.equals(playerColor, "WHITE") && originalGame.whiteUsername() != null) {
                gameData.updateGame(gameID, new GameData(gameID, username, originalGame.blackUsername(), originalGame.gameName(), originalGame.game()));
            } else if (Objects.equals(playerColor, "BLACK") && originalGame.blackUsername() != null) {
                gameData.updateGame(gameID, new GameData(gameID, originalGame.whiteUsername(), username, originalGame.gameName(), originalGame.game()));
            }
            else throw new DataAccessException("Already taken");
        }
        else throw new DataAccessException("Not authorized");
    }

    public int createGame(String authToken, String gameName) throws DataAccessException {
        if (authData.getAuth(authToken)) {
            int ID = gameIDinc();
            gameData.createGame(ID, null, null, gameName, new ChessGame());
            return gameID;
        } else throw new DataAccessException("Not authorized");
    }

    public Collection<GameData> listGames(String authToken) throws DataAccessException {
        if (authData.getAuth(authToken)) {
            return gameData.listGames(); }
        else throw new DataAccessException("Not authorized");
    }

    int gameIDinc() {
        return gameID++;
    }

}
