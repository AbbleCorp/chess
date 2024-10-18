package service;

import chess.ChessGame;
import dataaccess.*;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class DataServiceTest {
    private DataService dataServ;
    private AuthDAO authDAO;
    private UserDAO userDAO;
    private GameDAO gameDAO;


    @BeforeEach
    public void setUp() throws DataAccessException {
        authDAO = new MemoryAuthDAO();
        userDAO = new MemoryUserDAO();
        gameDAO = new MemoryGameDAO();
        authDAO.createAuth("user1");
        authDAO.createAuth("user2");
        userDAO.createUser(new UserData("user3", "password3", "email3"));
        userDAO.createUser(new UserData("user4","password4", "email4"));
        gameDAO.createGame(0001, "white1", "black2", "game1", new ChessGame());
        gameDAO.createGame(0002, null,null, "game2", new ChessGame());
        dataServ = new DataService(userDAO,gameDAO,authDAO);
    }

    @Test
    public void testClear() {
        //make sure it actually has stuff in it
        Assertions.assertFalse(authDAO.listAuth().isEmpty());
        Assertions.assertFalse(gameDAO.listGames().isEmpty());
        Assertions.assertFalse(userDAO.listUsers().isEmpty());
        dataServ.clearDatabase();
        //make sure it emptied
        Assertions.assertTrue(authDAO.listAuth().isEmpty());
        Assertions.assertTrue(gameDAO.listGames().isEmpty());
        Assertions.assertTrue(userDAO.listUsers().isEmpty());
    }

}
