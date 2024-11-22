package network;

import model.*;
import ui.ServerMessageObserver;
import websocket.commands.*;

import javax.websocket.DeploymentException;
import java.io.IOException;
import java.net.URISyntaxException;

public class ServerFacade {
    private HttpCommunicator clientComm;
    private WebSocketCommunicator webSocketComm;

    public ServerFacade(ServerMessageObserver SMO) throws ResponseException, DeploymentException, URISyntaxException, IOException {
        clientComm = new HttpCommunicator("http://localhost:8080");
        webSocketComm = new WebSocketCommunicator(SMO);
    }

    public ServerFacade(int port) {
        clientComm = new HttpCommunicator("http://localhost:" + port);
    }


    public LoginResult login(LoginRequest req) throws Exception {
        LoginResult result = clientComm.makeRequestWithBody("POST", "/session", req, LoginResult.class);
        return result;
    }

    public LoginResult register(RegisterRequest req) throws Exception {
        LoginResult result = clientComm.makeRequestWithBody("POST", "/user", req, LoginResult.class);
        return result;
    }

    public CreateGameResult createGame(CreateGameRequest req) throws Exception {
        try {
            CreateGameResult result = clientComm.makeRequestWithBoth("POST", "/game", req, req.getAuthorization(), CreateGameResult.class);
            return result;
        } catch (Exception e) {
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
        } catch (Exception e) {
            if (e.getMessage().contains("401")) {
                throw new Exception("failure: 401");
            }
        }
        return null;
    }

    public void logout(LogoutRequest req) throws Exception {
        try {
            clientComm.makeRequestWithBoth("DELETE", "/session", req, req.authorization(), null);
        } catch (Exception e) {
            throw new Exception("failure: 401");
        }
    }

    public void joinGame(JoinGameRequest req, ConnectCommand.JoinType joinType) throws Exception {
        try {
            clientComm.makeRequestWithBoth("PUT", "/game", req, req.getAuthorization(), JoinGameResult.class);
        } catch (Exception e) {
            if (e.getMessage().contains("400")) {
                throw new Exception("failure: 400");
            } else if (e.getMessage().contains("401")) {
                throw new Exception("failure: 401");
            } else if (e.getMessage().contains("403")) {
                throw new Exception("failure: 403");
            }
        }
        try {
            webSocketComm.sendMessage(new ConnectCommand(req.getAuthorization(), req.getGameID(), joinType));
        }
        catch (Exception e){
            throw new Exception("failure: caught at joinGame websocket");
        }
    }

    public void leave(String authToken, int gameId) throws Exception {
        webSocketComm.sendMessage(new LeaveCommand(authToken,gameId));
    }

    public void resign(String authToken, int gameId) throws Exception {
        webSocketComm.sendMessage(new ResignCommand(authToken,gameId));
    }

}
