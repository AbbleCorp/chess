package network;

import com.google.gson.Gson;
import model.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public class ServerFacade {
    private ClientCommunicator clientComm;

    public ServerFacade() {
        clientComm = new ClientCommunicator("http://localhost:8080");
    }


    public LoginResult login(LoginRequest req) throws Exception {
        HttpURLConnection http = clientComm.createConnection("login");
        writeBody(req, http);
        http.connect();
        LoginResult result = readBody(http, LoginResult.class);
        return result;
    }

    public LoginResult register(RegisterRequest req) throws Exception {
        HttpURLConnection http = clientComm.createConnection("register");
        writeBody(req, http);
        http.connect();
        LoginResult result = readBody(http, LoginResult.class);
        return result;
    }

    public CreateGameResult createGame(CreateGameRequest req) {
        //TODO: implement
        return null;
    }

    public ListGamesResult listGames(ListGamesRequest req) {
        //TODO: implement
        return null;
    }

    public void logout(LogoutRequest req) {
        //TODO: implement
    }


    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }


//    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException {
//        var status = http.getResponseCode();
//        if (!isSuccessful(status)) {
//            throw new ResponseException(status, "failure: " + status);
//        }
//    }


    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}
