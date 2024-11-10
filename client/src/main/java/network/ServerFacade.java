package network;

import model.*;

public class ServerFacade {


    public LoginResult login(LoginRequest req) {
        //TODO: implement
        return new LoginResult("user","authToken");
    }

    public LoginResult register(RegisterRequest req) {
        //TODO: implement
        return null;
    }

    public CreateGameResult createGame(CreateGameRequest req) {
        //TODO: implement
        return null;
    }



}
