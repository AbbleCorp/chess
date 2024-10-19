package handlers;

import com.google.gson.Gson;
import model.ErrorMessage;
import spark.Response;
import spark.Request;

public class ExHandler {

    public static String catchException(Exception e, Request req, Response res) {
        res.status(500);
        var Serializer = new Gson();
        return Serializer.toJson(new ErrorMessage(e.getMessage()));
    }
}
