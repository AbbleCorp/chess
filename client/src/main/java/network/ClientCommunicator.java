package network;

import java.net.HttpURLConnection;

import com.google.gson.Gson;
import java.io.*;
import java.net.*;

public class ClientCommunicator {
    private final String serverUrl;

    public ClientCommunicator(String url) {
        serverUrl = url;
    }

    public HttpURLConnection createConnection(String type) throws Exception {
        HttpURLConnection http = null;
        switch (type) {
            case "login" -> http = makeRequest("POST", "/session");
            case "register" -> http = makeRequest("POST", "/user");
            case "logout" -> http = makeRequest("DELETE", "/session");
            case "list" -> http = makeRequest("GET", "/game");
            case "create" -> http = makeRequest("POST", "/game");
            case "join" -> http = makeRequest("PUT", "game");
        }
        return http;
    }

    private HttpURLConnection makeRequest(String method, String path) throws Exception {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            return http;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}