package client;

import chess.ChessGame;
import dataaccess.*;
import model.*;
import network.ServerFacade;
import org.junit.jupiter.api.*;
import server.Server;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;
    private static DatabaseManager db;
    private static UserDAO userDb;
    private static GameDAO gameDb;
    private static AuthDAO authDb;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port);
        userDb = new MySqlUserDAO();
        gameDb = new MySqlGameDAO();
        authDb = new MySqlAuthDAO();
    }

    @BeforeEach
    void setUp() throws DataAccessException {
        db = new DatabaseManager();
        db.configureDatabase();
        userDb.clear();
        gameDb.clear();
        authDb.clear();
    }

    @AfterAll
    static void stopServer() {
        server.stop();

    }


    @Test
    void testPosRegister() throws Exception {
        var authData = facade.register(new RegisterRequest("user1", "password", "email"));
        Assertions.assertTrue(authData.authToken().length() > 10);
    }

    @Test
    void testNegRegister() throws Exception {
        facade.register(new RegisterRequest("user1", "password", "email"));
        Exception e = Assertions.assertThrows(Exception.class, () ->
                facade.register(new RegisterRequest("user1", "password", "email")));
        Assertions.assertEquals("failure: 403", e.getMessage());
    }

    @Test
    void testPosLogout() throws Exception {
        var auth = facade.register(new RegisterRequest("user1", "password", "email"));
        facade.logout(new LogoutRequest(auth.authToken()));
        Assertions.assertNull(authDb.getAuth(auth.authToken()));
    }


    @Test
    void testNegLogout() throws Exception {
        facade.register(new RegisterRequest("user1", "password", "email"));
        Exception e = Assertions.assertThrows(Exception.class, () ->
                facade.logout(new LogoutRequest("invalid")));
        Assertions.assertEquals("failure: 401", e.getMessage());
    }

    @Test
    void testPosLogin() throws Exception {
        facade.register(new RegisterRequest("user1", "password", "email"));
        var auth = facade.login(new LoginRequest("user1", "password"));
        Assertions.assertTrue(auth.authToken().length() > 10);
        Assertions.assertNotNull(authDb.getAuth(auth.authToken()));
    }

    @Test
    void testNegLogin() throws Exception {
        facade.register(new RegisterRequest("user1", "password", "email"));
        Exception e = Assertions.assertThrows(Exception.class, () ->
                facade.login(new LoginRequest("user1", "invalid")));
        Assertions.assertEquals("failure: 401", e.getMessage());
    }

    @Test
    void testPosCreateGame() throws Exception {
        var auth = facade.register(new RegisterRequest("user1", "password", "email"));
        facade.createGame(new CreateGameRequest(auth.authToken(), "game1"));
        facade.createGame(new CreateGameRequest(auth.authToken(), "game2"));
        facade.createGame(new CreateGameRequest(auth.authToken(), "game3"));
        Assertions.assertEquals(3, gameDb.listGames().size());
    }

    @Test
    void testNegCreateGame() throws Exception {
        Exception e = Assertions.assertThrows(Exception.class, () ->
                facade.createGame(new CreateGameRequest("invalid", "game1")));
        Assertions.assertEquals("failure: 401", e.getMessage());
    }


    @Test
    void testPosListGames() throws Exception {
        var auth = facade.register(new RegisterRequest("user1", "password", "email"));
        facade.createGame(new CreateGameRequest(auth.authToken(), "game1"));
        facade.createGame(new CreateGameRequest(auth.authToken(), "game2"));
        facade.createGame(new CreateGameRequest(auth.authToken(), "game3"));
        var gameList = facade.listGames(new ListGamesRequest(auth.authToken()));
        Assertions.assertEquals(gameList.games().size(), gameDb.listGames().size());
    }

    @Test
    void testNegListGames() throws Exception {
        var auth = facade.register(new RegisterRequest("user1", "password", "email"));
        facade.createGame(new CreateGameRequest(auth.authToken(), "game1"));
        facade.createGame(new CreateGameRequest(auth.authToken(), "game2"));
        facade.createGame(new CreateGameRequest(auth.authToken(), "game3"));
        Exception e = Assertions.assertThrows(Exception.class, () ->
                facade.listGames(new ListGamesRequest("invalid")));
        Assertions.assertEquals("failure: 401", e.getMessage());
    }

    @Test
    void testPosJoinGame() throws Exception {
        var auth = facade.register(new RegisterRequest("user1", "password", "email"));
        var game = facade.createGame(new CreateGameRequest(auth.authToken(), "game1"));
        facade.joinGame(new JoinGameRequest(auth.authToken(), "WHITE", game.gameID()));
        Assertions.assertEquals(gameDb.getGame(game.gameID()).whiteUsername(), "user1");
    }

    @Test
    void testNegJoinGame() throws Exception {
        var auth = facade.register(new RegisterRequest("user1", "password", "email"));
        var game = facade.createGame(new CreateGameRequest(auth.authToken(), "game1"));
        Exception e = Assertions.assertThrows(Exception.class, () ->
                facade.joinGame(new JoinGameRequest("invalid", "WHITE", game.gameID())));
        Assertions.assertEquals("failure: 401", e.getMessage());
    }

    @Test
    void testNegJoinTakenGame() throws Exception {
        var auth = facade.register(new RegisterRequest("user1", "password", "email"));
        var auth1 = facade.register(new RegisterRequest("user2", "password2", "email2"));
        var game = facade.createGame(new CreateGameRequest(auth.authToken(), "game1"));
        facade.joinGame(new JoinGameRequest(auth.authToken(), "WHITE", game.gameID()));
        Exception e = Assertions.assertThrows(Exception.class, () ->
                facade.joinGame(new JoinGameRequest(auth1.authToken(), "WHITE", game.gameID())));
        Assertions.assertEquals("failure: 403", e.getMessage());
    }
}
