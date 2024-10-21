package handlers;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.*;
import service.GameService;
import spark.Request;
import spark.Response;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.Collection;

public class GameHandler {
    private final GameService gameService;

    public GameHandler(GameService gameService) {
        this.gameService = gameService;
    }

    public String listGames(Request req, Response res) throws Exception {
        var Serializer = new Gson();
        ListGamesRequest request = new ListGamesRequest(req.headers("authorization"));
        try {
        Collection<GameData> gamesList = gameService.listGames(request);
        return Serializer.toJson(new ListGamesResult(gamesList)); }
        catch (DataAccessException e) {
            res.status(401);
            return Serializer.toJson(new ErrorMessage(e.getMessage()));
        }
        catch (Exception e) {
            res.status(500);
            return Serializer.toJson(new ErrorMessage(e.getMessage()));
        }
    }

    public String createGame(Request req, Response res) throws Exception {
        var Serializer = new Gson();
        String auth = req.headers("authorization");
        CreateGameRequest request = Serializer.fromJson(req.body(), CreateGameRequest.class);
        request.setAuthorization(auth);
        try {
            CreateGameResult gameID = new CreateGameResult(gameService.createGame(request));
            return Serializer.toJson(gameID);
        } catch (DataAccessException e) {
            res.status(401);
            return Serializer.toJson(new ErrorMessage(e.getMessage()));
        }
        catch (Exception e) {
            if (e.getMessage() == "Error: bad request") res.status(400);
            else res.status(500);
            return Serializer.toJson(new ErrorMessage(e.getMessage()));
        }
    }

    public String joinGame(Request req, Response res) throws DataAccessException {
        var Serializer = new Gson();
        String auth = req.headers("authorization");
        JoinGameRequest request = Serializer.fromJson(req.body(),JoinGameRequest.class);
        request.setAuthorization(auth);
        try {
        gameService.joinGame(request);
        return Serializer.toJson(new JoinGameResult());
        } catch (DataAccessException e) {
            if (e.getMessage() == "Error: unauthorized") res.status(401);
            else res.status(403);
            return Serializer.toJson(new ErrorMessage(e.getMessage()));
        }
        catch (Exception e) {
            if (e.getMessage() == "Error: bad request") res.status(400);
            else res.status(500);
            return Serializer.toJson(new ErrorMessage(e.getMessage()));
        }
    }



}
