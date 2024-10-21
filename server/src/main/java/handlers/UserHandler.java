package handlers;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.*;

import service.UserService;
import spark.Request;
import spark.Response;

import javax.xml.crypto.Data;
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
        try {
        AuthData auth = userService.register(request);
        //res.body(Serializer.toJson(auth));
        return Serializer.toJson(auth); }
        catch (DataAccessException e) {
            res.status(403);
            return Serializer.toJson(new ErrorMessage(e.getMessage())); }
        catch (Exception e) {
            res.status(400);
            return Serializer.toJson(new ErrorMessage(e.getMessage()));
        }

    }

    public String login(Request req, Response res) throws DataAccessException {
        var Serializer = new Gson();
        LoginRequest request = Serializer.fromJson(req.body(), LoginRequest.class);
        try {
        AuthData auth = userService.login(request);
        //res.body(auth.authorization());
        return Serializer.toJson(new LoginResult(auth.username(), auth.authToken()));
            }
            catch (DataAccessException e) {
            //if (e.getMessage().equals("Error: unauthorized")) {
                res.status(401);
                return Serializer.toJson(new ErrorMessage(e.getMessage())); }
            catch (Exception e ) {
                res.status(500);
                return Serializer.toJson(new ErrorMessage(e.getMessage()));
            }
            //else {
//                res.status(500);
//                return Serializer.toJson(new ErrorMessage(e.getMessage()));


    }

    public String logout(Request req, Response res) throws DataAccessException {
        var Serializer = new Gson();
        LogoutRequest request = new LogoutRequest(req.headers("authorization"));
        try {
        userService.logout(request);
        //res.body("{}");
        return "{}"; }
        catch (DataAccessException e) {
            res.status(401);
            return Serializer.toJson(new ErrorMessage(e.getMessage()));
        }
        catch (Exception e) {
            res.status(500);
            return Serializer.toJson(new ErrorMessage(e.getMessage()));
        }
    }
}
