package network;

import model.*;

public class ServerFacade {
    private ClientCommunicator clientComm;

    public ServerFacade() {
        clientComm = new ClientCommunicator("http://localhost:8080");
    }

    public ServerFacade(int port) {
        clientComm = new ClientCommunicator("http://localhost:" + port);
    }


    public LoginResult login(LoginRequest req) throws Exception {
        LoginResult result = clientComm.makeRequestWithBody("POST","/session", req, LoginResult.class);
        return result;
    }

    public LoginResult register(RegisterRequest req) throws Exception {
        LoginResult result = clientComm.makeRequestWithBody("POST","/user", req, LoginResult.class);
        return result;
    }

    public CreateGameResult createGame(CreateGameRequest req) throws Exception {
        try {
            CreateGameResult result = clientComm.makeRequestWithBoth("POST", "/game", req, req.getAuthorization(), CreateGameResult.class);
            return result;
        }
        catch (Exception e) {
            if (e.getMessage().contains("401")) {
                throw new Exception("failure: 401");
            }
        }
        return null;
        }

    public ListGamesResult listGames(ListGamesRequest req) throws Exception {
        try {
            ListGamesResult result = clientComm.makeRequestWithHeader("GET", "/game", req, req.authorization(), ListGamesResult.class);
            return result;
        }
        catch (Exception e) {
            if (e.getMessage().contains("401")) {
                throw new Exception("failure: 401");
            }
        }
        return null;
    }

    public void logout(LogoutRequest req) throws Exception {
        try {
        clientComm.makeRequestWithBoth("DELETE","/session", req, req.authorization(), null); }
        catch (Exception e) {
            throw new Exception("failure: 401");
        }
    }




}
