package service;

import chess.ChessGame;
import dataaccess.*;
import model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;


public class GameServiceTest {
    private GameService gameService;
    private GameDAO gameDAO;
    private AuthDAO authDAO;
    private AuthData auth1;
    private AuthData auth2;

    @BeforeEach
    void setUp() {
        gameDAO = new MemoryGameDAO();
        authDAO = new MemoryAuthDAO();
        auth1 = authDAO.createAuth("user1");
        auth2 = authDAO.createAuth("user2");
        gameService = new GameService(gameDAO,authDAO);
    }


    @Test
    void testGameID() {
        Assertions.assertEquals(0001,gameService.gameIDinc());
        Assertions.assertEquals(0002,gameService.gameIDinc());
        Assertions.assertEquals(0003,gameService.gameIDinc());
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
        Exception e = Assertions.assertThrows(DataAccessException.class, () -> {
            gameService.listGames(new ListGamesRequest("incorrect"));
        });
        Assertions.assertEquals("Not authorized", e.getMessage());
    }


    //create Game test
    @Test
    void testCreateGame() throws Exception {
        int gameID = gameService.createGame(new CreateGameRequest(auth1.authToken(), "game1"));
        Assertions.assertEquals(0001, gameID);
        Assertions.assertFalse(gameService.listGames(new ListGamesRequest(auth1.authToken())).isEmpty());
    }


    //negativeCreateGame
    @Test
    void testCreateGameNegative() {
        Exception e = Assertions.assertThrows(DataAccessException.class, () -> {
            gameService.createGame(new CreateGameRequest("incorrect", "game1"));
        });
        Assertions.assertEquals("Not authorized", e.getMessage());
    }


    //joinGame white positive
    @Test
    void testJoinGameWhitePos() throws Exception {
        gameService.createGame(new CreateGameRequest(auth1.authToken(),"game1"));
        gameService.joinGame(new JoinGameRequest(auth1.authToken(),"WHITE", 0001));
        GameData game = gameDAO.getGame(0001);
        Assertions.assertEquals("user1",game.whiteUsername());
        Assertions.assertNull(game.blackUsername());
    }


    //test joinGame black positive
    @Test
    void testJoinGameBlackPos() throws Exception {
        gameService.createGame(new CreateGameRequest(auth1.authToken(),"game1"));
        gameService.joinGame(new JoinGameRequest(auth1.authToken(),"BLACK", 0001));
        GameData game = gameDAO.getGame(0001);
        Assertions.assertEquals("user1",game.blackUsername());
        Assertions.assertNull(game.whiteUsername());
    }

    //test joinGame negative, already taken
    @Test
    void testJoinGameSpotTaken() throws Exception {
        gameService.createGame(new CreateGameRequest(auth1.authToken(),"game1"));
        gameService.joinGame(new JoinGameRequest(auth1.authToken(),"WHITE", 0001));
        Exception e = Assertions.assertThrows(DataAccessException.class, () -> {
            gameService.joinGame(new JoinGameRequest(auth2.authToken(),"WHITE", 0001));
        });
        Assertions.assertEquals("Already taken",e.getMessage());
    }

    //test joinGame, invalid auth
    @Test
    void testJoinGameInvalidAuth() throws Exception {
        gameService.createGame(new CreateGameRequest(auth1.authToken(),"game1"));
        Exception e = Assertions.assertThrows(DataAccessException.class, () -> {
            gameService.joinGame(new JoinGameRequest("incorrect","WHITE", 0001));
        });
        Assertions.assertEquals("Not authorized",e.getMessage());
    }


}
