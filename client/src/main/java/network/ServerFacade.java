package network;

import model.*;

public class ServerFacade {
    private ClientCommunicator clientComm;

    public ServerFacade() {
        clientComm = new ClientCommunicator("http://localhost:8080");
    }


    public LoginResult login(LoginRequest req) throws Exception {
        LoginResult result = clientComm.makeRequestWithBody("POST","/session", req, LoginResult.class);
        return result;
    }

    public LoginResult register(RegisterRequest req) throws Exception {
        LoginResult result = clientComm.makeRequestWithBody("POST","/user", req, LoginResult.class);
        return result;
    }

    public CreateGameResult createGame(CreateGameRequest req) {
        try {
            CreateGameResult result = clientComm.makeRequestWithBoth("POST", "/game", req, req.getAuthorization(), CreateGameResult.class);
            return result;
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
        }

    public ListGamesResult listGames(ListGamesRequest req) {
        try {
            ListGamesResult result = clientComm.makeRequestWithHeader("GET", "/game", req, req.authorization(), ListGamesResult.class);
            return result;
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public void logout(LogoutRequest req) {
        try {
        clientComm.makeRequestWithBoth("DELETE","/session", req, req.authorization(), null); }
        catch (Exception e) {
            System.out.print(e.getMessage());
        }
    }




}
