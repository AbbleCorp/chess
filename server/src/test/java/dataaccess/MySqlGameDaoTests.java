package dataaccess;

import chess.ChessGame;
import dataaccess.*;
import model.GameData;
import model.GameDataAutoId;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
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
        int game1 = gameDB.createGame(0,"white1",null,"game1", new ChessGame());
        System.out.print(game1);
        int game2 = gameDB.createGame(1,null,null,"game2",new ChessGame());
        System.out.print(game2);
    }

    @Test
    void listGames() throws DataAccessException {
        createGame();
        ArrayList<GameData> gameList = gameDB.listGames();
        System.out.print("test");
    }
}
