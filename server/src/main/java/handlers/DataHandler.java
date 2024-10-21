package handlers;
import com.google.gson.Gson;
import model.ClearResult;
import service.DataService;
import spark.Request;
import spark.Response;

public class DataHandler {
    private final DataService dataService;

    public DataHandler(DataService dataService) {
        this.dataService = dataService;
    }

    public String clear(Request req, Response res) {
        var Serializer = new Gson();
        dataService.clearDatabase();
        //res.body("{}");
        return "{}";
    }

}
