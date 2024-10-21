package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.AuthDAO;
import model.CreateGameRequest;
import model.GameData;
import model.JoinGameRequest;
import model.ListGamesRequest;

import java.util.ArrayList;
import java.util.Objects;


public class GameService {
    private final GameDAO gameData;
    private final AuthDAO authData;
    int gameID = 0;

    public GameService(GameDAO gameData, AuthDAO authData) {
        this.gameData = gameData;
        this.authData = authData;
    }


    public void joinGame(JoinGameRequest request) throws Exception {
        if (request.getAuthorization() == null || request.getPlayerColor() == null || request.getGameID() == null) {
            throw new Exception("Error: bad request"); }
        if (authData.getAuth(request.getAuthorization()) != null) {
            String username = authData.getUsername(request.getAuthorization());
            GameData originalGame = gameData.getGame(gameID);
            if (Objects.equals(request.getPlayerColor(), "WHITE") && originalGame.whiteUsername()==null) {
                gameData.updateGame(gameID, new GameData(gameID, username, originalGame.blackUsername(),
                        originalGame.gameName(), originalGame.game()));
            } else if (Objects.equals(request.getPlayerColor(), "BLACK") && originalGame.blackUsername()==null) {
                gameData.updateGame(gameID, new GameData(gameID, originalGame.whiteUsername(), username,
                        originalGame.gameName(), originalGame.game()));
            }
            else throw new DataAccessException("Error: already taken");
        }
        else throw new DataAccessException("Error: unauthorized");
    }

    public int createGame(CreateGameRequest request) throws Exception {
        if (request.getAuthorization() == null || request.getGameName() == null) throw new Exception("Error: bad request");
        if (authData.getAuth(request.getAuthorization()) != null) {
            int id = gameIDinc();
            gameData.createGame(id, null, null, request.getGameName(), new ChessGame());
            return gameID;
        } else throw new DataAccessException("Error: unauthorized");
    }

    public ArrayList<GameData> listGames(ListGamesRequest req) throws Exception {
        if (req.authorization() == null) throw new Exception("Error: bad request");
        if (authData.getAuth(req.authorization()) != null) {
            return gameData.listGames(); }
        else throw new DataAccessException("Error: unauthorized");
    }

    int gameIDinc() {
        return ++gameID;
    }

}
