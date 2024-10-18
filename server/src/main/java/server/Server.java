package server;

import dataaccess.*;
import spark.*;

public class Server {
    private final UserDAO userData = new MemoryUserDAO();
    private final GameDAO gameData = new MemoryGameDAO();
    private final AuthDAO authData = new MemoryAuthDAO();

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.



        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
