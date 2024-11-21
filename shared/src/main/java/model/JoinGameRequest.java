package model;

public class JoinGameRequest {
    String authorization;
    String playerColor;
    Integer gameID;

    public JoinGameRequest(String authorization, String playerColor, Integer gameID) {
        this.authorization=authorization;
        this.playerColor=playerColor;
        this.gameID=gameID;
    }


    public String getAuthorization() {
        return authorization;
    }

    public void setAuthorization(String authorization) {
        this.authorization=authorization;
    }

    public String getPlayerColor() {
        return playerColor;
    }


    public Integer getGameID() {
        return gameID;
    }


}
