package handlers;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.*;
import service.GameService;
import spark.Request;
import spark.Response;

import java.util.ArrayList;
import java.util.Collection;

public class GameHandler {
    private final GameService gameService;

    public GameHandler(GameService gameService) {
        this.gameService = gameService;
    }

    public String listGames(Request req, Response res) throws DataAccessException {
        var Serializer = new Gson();
        ListGamesRequest request = new ListGamesRequest(req.headers("authorization"));
        Collection<GameData> gamesList = gameService.listGames(request);
        ArrayList<GameInfo> games = new ArrayList<>();
//        for (GameData game : gamesList) {
//            games.add(new GameInfo(game.GameID(),game.whiteUsername(),game.blackUsername(),game.gameName()));
//        }
        return Serializer.toJson(new ListGamesResult(gamesList));
    }

    public String createGame(Request req, Response res) throws DataAccessException {
        var Serializer = new Gson();
        String auth = req.headers("authorization");
        CreateGameRequest request = Serializer.fromJson(req.body(), CreateGameRequest.class);
        request.setAuthorization(auth);
        CreateGameResult gameID = new CreateGameResult(gameService.createGame(request));
        return Serializer.toJson(gameID);
    }

    public String joinGame(Request req, Response res) throws DataAccessException {
        var Serializer = new Gson();
        String auth = req.headers("authorization");
        JoinGameRequest request = Serializer.fromJson(req.body(),JoinGameRequest.class);
        request.setAuthorization(auth);
        gameService.joinGame(request);
        return Serializer.toJson(new JoinGameResult());
    }



}
