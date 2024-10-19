package handlers;
import service.DataService;
import spark.Request;
import spark.Response;

public class DataHandler {
    private final DataService dataService;

    public DataHandler(DataService dataService) {
        this.dataService = dataService;
    }

    public String clear(Request req, Response res) {
        return null;
    }

}
