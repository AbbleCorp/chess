package dataaccess;

import chess.ChessGame;
import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class MySqlGameDaoTests {
    private GameDAO gameDB;

    @BeforeEach
    void setUp() {
        gameDB = new MySqlGameDAO();
    }

    @Test
    void clear() {
        gameDB.clear();
    }

    @Test
    void createGame() throws DataAccessException {
        int game1 = gameDB.createGame("white1",null,"game1", new ChessGame());
        System.out.print(game1);
        int game2 = gameDB.createGame(null,null,"game2",new ChessGame());
        System.out.print(game2);
    }

    @Test
    void listGames() throws DataAccessException {
        createGame();
        ArrayList<GameData> gameList = gameDB.listGames();
        System.out.print("test");
    }

    @Test
    void updateGame() throws DataAccessException {
        gameDB.updateGame(2,new GameData(2,"newwhite",null,"game2",new ChessGame()));
    }

    @Test
    void getGame() throws DataAccessException {
        GameData data = gameDB.getGame(1);
        System.out.print(data.gameName());
    }
}
