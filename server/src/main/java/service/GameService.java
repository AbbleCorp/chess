package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.AuthDAO;
import model.CreateGameRequest;
import model.GameData;
import model.JoinGameRequest;
import model.ListGamesRequest;

import java.util.Collection;
import java.util.Objects;


public class GameService {
    private final GameDAO gameData;
    private final AuthDAO authData;
    int gameID = 0000;

    public GameService(GameDAO gameData, AuthDAO authData) {
        this.gameData = gameData;
        this.authData = authData;
    }

    public void clearGameData() throws DataAccessException {
        gameData.clear();
    }

    public void joinGame(JoinGameRequest request) throws DataAccessException {
        if (authData.getAuth(request.getAuthorization()) != null) {
            String username = authData.getUsername(request.getAuthorization());
            GameData originalGame = gameData.getGame(gameID);
            if (Objects.equals(request.getPlayerColor(), "WHITE") && Objects.equals(originalGame.whiteUsername(), "")) {
                gameData.updateGame(gameID, new GameData(gameID, username, originalGame.blackUsername(), originalGame.gameName(), originalGame.game()));
            } else if (Objects.equals(request.getPlayerColor(), "BLACK") && Objects.equals(originalGame.blackUsername(), "")) {
                gameData.updateGame(gameID, new GameData(gameID, originalGame.whiteUsername(), username, originalGame.gameName(), originalGame.game()));
            }
            else throw new DataAccessException("Already taken");
        }
        else throw new DataAccessException("Not authorized");
    }

    public int createGame(CreateGameRequest request) throws DataAccessException {
        if (authData.getAuth(request.getAuthorization()) != null) {
            int ID = gameIDinc();
            gameData.createGame(ID, null, null, request.getGameName(), new ChessGame());
            return gameID;
        } else throw new DataAccessException("Not authorized");
    }

    public Collection<GameData> listGames(ListGamesRequest req) throws DataAccessException {
        if (authData.getAuth(req.authorization()) != null) {
            return gameData.listGames(); }
        else throw new DataAccessException("Not authorized");
    }

    int gameIDinc() {
        return ++gameID;
    }

}
