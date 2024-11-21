package handlers;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.*;
import service.GameService;
import spark.Request;
import spark.Response;

import java.util.ArrayList;
import java.util.Objects;

public class GameHandler {
    private final GameService gameService;

    public GameHandler(GameService gameService) {
        this.gameService=gameService;
    }

    public String listGames(Request req, Response res) {
        var serializer=new Gson();
        ListGamesRequest request=new ListGamesRequest(req.headers("authorization"));
        try {
            ArrayList<GameData> gamesList=gameService.listGames(request);
            return serializer.toJson(new ListGamesResult(gamesList));
        } catch (DataAccessException e) {
            res.status(401);
            return serializer.toJson(new ErrorMessage(e.getMessage()));
        } catch (Exception e) {
            res.status(500);
            return serializer.toJson(new ErrorMessage(e.getMessage()));
        }
    }

    public String createGame(Request req, Response res) throws Exception {
        var serializer=new Gson();
        String auth=req.headers("authorization");
        CreateGameRequest request=serializer.fromJson(req.body(), CreateGameRequest.class);
        request.setAuthorization(auth);
        try {
            CreateGameResult gameID=new CreateGameResult(gameService.createGame(request));
            return serializer.toJson(gameID);
        } catch (DataAccessException e) {
            res.status(401);
            return serializer.toJson(new ErrorMessage(e.getMessage()));
        } catch (Exception e) {
            return catchBadRequest(e, res);
        }
    }

    public String joinGame(Request req, Response res) {
        var serializer=new Gson();
        String auth=req.headers("authorization");
        JoinGameRequest request=serializer.fromJson(req.body(), JoinGameRequest.class);
        request.setAuthorization(auth);
        try {
            gameService.joinGame(request);
            return serializer.toJson(new JoinGameResult());
        } catch (DataAccessException e) {
            if (Objects.equals(e.getMessage(), "Error: unauthorized")) {
                res.status(401);
            } else {
                res.status(403);
            }
            return serializer.toJson(new ErrorMessage(e.getMessage()));
        } catch (Exception e) {
            return catchBadRequest(e, res);
        }
    }

    public String catchBadRequest(Exception e, Response res) {
        var serializer=new Gson();
        if (Objects.equals(e.getMessage(), "Error: bad request")) {
            res.status(400);
        } else {
            res.status(500);
        }
        return serializer.toJson(new ErrorMessage(e.getMessage()));
    }

}
