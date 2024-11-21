package handlers;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.*;

import service.UserService;
import spark.Request;
import spark.Response;


public class UserHandler {
    private final UserService userService;

    public UserHandler(UserService userService) {
        this.userService = userService;
    }


    public String register(Request req, Response res) {
        var serializer = new Gson();
        RegisterRequest request = serializer.fromJson(req.body(), RegisterRequest.class);
        try {
            AuthData auth = userService.register(request);
            return serializer.toJson(auth);
        } catch (DataAccessException e) {
            res.status(403);
            return serializer.toJson(new ModelErrorMessage(e.getMessage()));
        } catch (Exception e) {
            res.status(400);
            return serializer.toJson(new ModelErrorMessage(e.getMessage()));
        }

    }

    public String login(Request req, Response res) {
        var serializer = new Gson();
        LoginRequest request = serializer.fromJson(req.body(), LoginRequest.class);
        try {
            AuthData auth = userService.login(request);
            return serializer.toJson(new LoginResult(auth.username(), auth.authToken()));
        } catch (DataAccessException e) {
            res.status(401);
            return serializer.toJson(new ModelErrorMessage(e.getMessage()));
        } catch (Exception e) {
            res.status(500);
            return serializer.toJson(new ModelErrorMessage(e.getMessage()));
        }

    }

    public String logout(Request req, Response res) {
        var serializer = new Gson();
        LogoutRequest request = new LogoutRequest(req.headers("authorization"));
        try {
            userService.logout(request);
            return "{}";
        } catch (DataAccessException e) {
            res.status(401);
            return serializer.toJson(new ModelErrorMessage(e.getMessage()));
        } catch (Exception e) {
            res.status(500);
            return serializer.toJson(new ModelErrorMessage(e.getMessage()));
        }
    }
}
