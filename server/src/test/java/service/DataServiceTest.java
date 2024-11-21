package service;

import chess.ChessGame;
import dataaccess.*;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class DataServiceTest {
    DatabaseManager db;
    private DataService dataServ;
    private AuthDAO authDAO;
    private UserDAO userDAO;
    private GameDAO gameDAO;


    @BeforeEach
    public void setUp() throws DataAccessException {
        db = new DatabaseManager();
        db.configureDatabase();
        authDAO = new MySqlAuthDAO();
        userDAO = new MySqlUserDAO();
        gameDAO = new MySqlGameDAO();
        authDAO.createAuth("user1");
        authDAO.createAuth("user2");
        userDAO.createUser(new UserData("user3", "password3", "email3"));
        userDAO.createUser(new UserData("user4", "password4", "email4"));
        gameDAO.createGame("white1", "black2", "game1", new ChessGame());
        gameDAO.createGame(null, null, "game2", new ChessGame());
        dataServ = new DataService(userDAO, gameDAO, authDAO);
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
