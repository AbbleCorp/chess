package handlers;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.*;

import service.UserService;
import spark.Request;
import spark.Response;

import java.io.Serializable;

public class UserHandler {
    private final UserService userService;

    public UserHandler(UserService userService) {
        this.userService = userService;
    }

    //handler takes in response/request objects

    public String register(Request req, Response res) throws DataAccessException {
        var Serializer = new Gson();
        RegisterRequest request = Serializer.fromJson(req.body(), RegisterRequest.class);
        AuthData auth = userService.register(request);
        return Serializer.toJson(auth);
        //turn req into record object, try register
        //set status
        //set body, return same info as body(as json string)
        //String authToken
        //String authToken = req.headers("Authorization");
    }

    public String login(Request req, Response res) throws DataAccessException {
        var Serializer = new Gson();
        LoginRequest request = Serializer.fromJson(req.body(), LoginRequest.class);
        AuthData auth = userService.login(request);
        return Serializer.toJson(auth);
    }

    public String logout(Request req, Response res) throws DataAccessException {
        var Serializer = new Gson();
        LogoutRequest request = new LogoutRequest(req.headers("authorization"));
        userService.logout(request);
        return Serializer.toJson(new LogoutResult());
    }
}
