package server;

import dataaccess.*;
import service.*;
import handlers.*;
import spark.*;

import javax.xml.crypto.Data;

public class Server {
    private UserHandler userHandler;
    private GameHandler gameHandler;
    private DataHandler dataHandler;

    public Server() {
        createDatabase();
        constructHandlers();
    }

    private void createDatabase() {
        DatabaseManager databaseManager = new DatabaseManager();
        try {
        databaseManager.configureDatabase(); }
        catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private void constructHandlers() {
        UserDAO userData = new MySqlUserDAO();
        GameDAO gameData = new MySqlGameDAO();
        AuthDAO authData = new MySqlAuthDAO();
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
    }


}
