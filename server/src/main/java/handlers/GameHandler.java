package handlers;

import model.ListGamesResult;
import service.GameService;
import spark.Request;
import spark.Response;

public class GameHandler {
    private final GameService gameService;

    public GameHandler(GameService gameService) {
        this.gameService = gameService;
    }

    public ListGamesResult listGames(Request req, Response res) {
        return null;
    }



}
