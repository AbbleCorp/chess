package model;

public class GameInfo {
    int gameID;
    String whiteUsername = "";
    String blackUsername = "";
    String gameName;

    public GameInfo(int gameID, String whiteUsername, String blackUsername, String gameName) {
        this.gameID = gameID;
        this.gameName = gameName;
    }
}
