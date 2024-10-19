package handlers;
import service.DataService;

public class DataHandler {
    private final DataService dataService;

    public DataHandler(DataService dataService) {
        this.dataService = dataService;
    }

}
