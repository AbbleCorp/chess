package service;

import dataaccess.*;
import model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class GameServiceTest {
    DatabaseManager db;
    private GameService gameService;
    private GameDAO gameDAO;
    private AuthData auth1;
    private AuthData auth2;

    @BeforeEach
    void setUp() throws DataAccessException {
        db = new DatabaseManager();
        db.configureDatabase();
        gameDAO = new MySqlGameDAO();
        AuthDAO authDAO = new MySqlAuthDAO();
        gameDAO.clear();
        authDAO.clear();
        auth1 = authDAO.createAuth("user1");
        auth2 = authDAO.createAuth("user2");
        gameService = new GameService(gameDAO, authDAO);
    }


    //positive list games
    @Test
    void testListGames() throws Exception {
        gameService.createGame(new CreateGameRequest(auth1.authToken(), "game1"));
        gameService.createGame(new CreateGameRequest(auth1.authToken(), "game2"));
        Assertions.assertFalse(gameService.listGames(new ListGamesRequest(auth1.authToken())).isEmpty());
        Assertions.assertEquals(2, gameService.listGames(new ListGamesRequest(auth1.authToken())).size());
    }

    //negative listGames test
    @Test
    void testListGamesNeg() {
        Exception e = Assertions.assertThrows(DataAccessException.class, () ->
                gameService.listGames(new ListGamesRequest("incorrect")));
        Assertions.assertEquals("Error: unauthorized", e.getMessage());
    }


    //create Game test
    @Test
    void testCreateGame() throws Exception {
        int gameID = gameService.createGame(new CreateGameRequest(auth1.authToken(), "game1"));
        Assertions.assertEquals(1, gameID);
        Assertions.assertFalse(gameService.listGames(new ListGamesRequest(auth1.authToken())).isEmpty());
    }


    //negativeCreateGame
    @Test
    void testCreateGameNegative() {
        Exception e = Assertions.assertThrows(DataAccessException.class, () ->
                gameService.createGame(new CreateGameRequest("incorrect", "game1")));
        Assertions.assertEquals("Error: unauthorized", e.getMessage());
    }


    //joinGame white positive
    @Test
    void testJoinGameWhitePos() throws Exception {
        gameService.createGame(new CreateGameRequest(auth1.authToken(), "game1"));
        gameService.joinGame(new JoinGameRequest(auth1.authToken(), "WHITE", 1));
        GameData game = gameDAO.getGame(1);
        Assertions.assertEquals("user1", game.whiteUsername());
        Assertions.assertNull(game.blackUsername());
    }


    //test joinGame black positive
    @Test
    void testJoinGameBlackPos() throws Exception {
        gameService.createGame(new CreateGameRequest(auth1.authToken(), "game1"));
        JoinGameRequest request = new JoinGameRequest(auth1.authToken(), "BLACK", 1);
        gameService.joinGame(request);
        GameData game = gameDAO.getGame(1);
        Assertions.assertEquals("user1", game.blackUsername());
        Assertions.assertNull(game.whiteUsername());
    }

    //test joinGame negative, already taken
    @Test
    void testJoinGameSpotTaken() throws Exception {
        gameService.createGame(new CreateGameRequest(auth1.authToken(), "game1"));
        gameService.joinGame(new JoinGameRequest(auth1.authToken(), "WHITE", 1));
        Exception e = Assertions.assertThrows(DataAccessException.class, () ->
                gameService.joinGame(new JoinGameRequest(auth2.authToken(), "WHITE", 1)));
        Assertions.assertEquals("Error: already taken", e.getMessage());
    }

    //test joinGame, invalid auth
    @Test
    void testJoinGameInvalidAuth() throws Exception {
        gameService.createGame(new CreateGameRequest(auth1.authToken(), "game1"));
        Exception e = Assertions.assertThrows(Exception.class, () ->
                gameService.joinGame(new JoinGameRequest("incorrect", "WHITE", 1)));
        Assertions.assertEquals("Error: unauthorized", e.getMessage());
    }


}
