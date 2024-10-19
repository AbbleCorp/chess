package handlers;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.*;
import service.GameService;
import spark.Request;
import spark.Response;

import java.util.Collection;

public class GameHandler {
    private final GameService gameService;

    public GameHandler(GameService gameService) {
        this.gameService = gameService;
    }

    public String listGames(Request req, Response res) throws DataAccessException {
        var Serializer = new Gson();
        ListGamesRequest request = Serializer.fromJson(req.headers("authorization"), ListGamesRequest.class);
        Collection<GameData> gamesList = gameService.listGames(request);
        return Serializer.toJson(gamesList);
    }

    public String createGame(Request req, Response res) throws DataAccessException {
        var Serializer = new Gson();
        CreateGameRequest request = new CreateGameRequest(req.headers("authorization"),req.params("gameName"));
        int gameID = gameService.createGame(request);
        return Serializer.toJson(gameID);
    }

    public String joinGame(Request req, Response res) throws DataAccessException {
        var Serializer = new Gson();
        JoinGameRequest request = new JoinGameRequest(req.headers("authorization"),
                req.params("playerColor"),Integer.parseInt(req.params("gameID")));
        gameService.joinGame(request);
        return Serializer.toJson(new JoinGameResult());
    }



}
