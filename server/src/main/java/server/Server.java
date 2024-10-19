package server;

import dataaccess.*;
import handlers.DataHandler;
import handlers.GameHandler;
import handlers.UserHandler;
import service.DataService;
import service.GameService;
import service.UserService;
import spark.*;

public class Server {
    private UserHandler userHandler;
    private GameHandler gameHandler;
    private DataHandler dataHandler;

    public Server() {
        constructHandlers();
    }

    private void constructHandlers() {
        UserDAO userData = new MemoryUserDAO();
        GameDAO gameData = new MemoryGameDAO();
        AuthDAO authData = new MemoryAuthDAO();
        UserService userService = new UserService(userData, authData);
        GameService gameService = new GameService(gameData, authData);
        DataService dataService = new DataService(userData, gameData, authData);
        userHandler = new UserHandler(userService);
        gameHandler = new GameHandler(gameService);
        dataHandler = new DataHandler(dataService);
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.


        setRoutes();


        //exception handler or try catch

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    public void setRoutes() {
        Spark.delete("/db", dataHandler::clear); //clear database
        Spark.post("/user", userHandler::register); //register
        Spark.post("/session",userHandler::login); //login
        Spark.delete("/session",userHandler::logout); //logout
        Spark.get("/game",gameHandler::listGames); //listGames
        Spark.post("/game", gameHandler::createGame); //createGame
        Spark.put("/game",gameHandler::joinGame); //joinGame
        Spark.exception(DataAccessException.class, this::exceptionHandler);
    }
}
