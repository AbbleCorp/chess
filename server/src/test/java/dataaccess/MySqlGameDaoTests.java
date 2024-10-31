package dataaccess;

import chess.ChessGame;
import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class MySqlGameDaoTests {
    DatabaseManager DB;
    private GameDAO gameDB;

    @BeforeEach
    void setUp() throws DataAccessException {
        DB = new DatabaseManager();
        DB.configureDatabase();
        gameDB = new MySqlGameDAO();
        gameDB.clear();
    }

    @Test
    void testClear() throws DataAccessException {
        gameDB.createGame("white","black","game1",new ChessGame());
        gameDB.createGame(null,null,"game2",new ChessGame());
        gameDB.clear();
        ArrayList<GameData> gameList = gameDB.listGames();
        Assertions.assertTrue(gameList.isEmpty());
    }

    //positive
    @Test
    void createGame() throws DataAccessException {
        gameDB.createGame("white1",null,"game1", new ChessGame());
        gameDB.createGame(null,null,"game2",new ChessGame());
        ArrayList<GameData> gameList = gameDB.listGames();
        Assertions.assertEquals(2,gameList.size());
        Assertions.assertEquals("game1",gameList.get(0).gameName());
    }


    //negative
    @Test
    void createGameNullGameName() throws DataAccessException {
        Exception e = Assertions.assertThrows(DataAccessException.class, () -> {
            gameDB.createGame(null,null,null,new ChessGame());
        });
        Assertions.assertEquals("Column 'gameName' cannot be null",e.getMessage());
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
