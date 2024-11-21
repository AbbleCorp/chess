package model;

public class CreateGameRequest {
    String authorization;
    String gameName;

    public String getAuthorization() {
        return authorization;
    }

    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }

    public String getGameName() {
        return gameName;
    }

    public CreateGameRequest(String authorization, String gameName) {
        this.authorization = authorization;
        this.gameName = gameName;

    }

}
