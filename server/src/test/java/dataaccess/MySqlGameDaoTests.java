package dataaccess;

import chess.ChessGame;
import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
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
    void testCreateGame() throws DataAccessException {
        gameDB.createGame("white1",null,"game1", new ChessGame());
        gameDB.createGame(null,null,"game2",new ChessGame());
        ArrayList<GameData> gameList = gameDB.listGames();
        Assertions.assertEquals(2,gameList.size());
        Assertions.assertEquals("game1",gameList.getFirst().gameName());
    }


    //negative
    @Test
    void testCreateGameNullGameName() {
        Exception e = Assertions.assertThrows(DataAccessException.class, () -> gameDB.createGame(null,null,null,new ChessGame()));
        Assertions.assertEquals("Column 'gameName' cannot be null",e.getMessage());
    }


    //positive
    @Test
    void testListGames() throws DataAccessException {
        gameDB.createGame("white","black","game1",new ChessGame());
        gameDB.createGame(null,null,"game1",new ChessGame());
        ArrayList<GameData> gameList = gameDB.listGames();
        Assertions.assertEquals(2,gameList.size());
        Assertions.assertEquals("white",gameList.getFirst().whiteUsername());
    }

    //negative
    @Test
    void testListGamesTableDropped() {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("DROP TABLE game")) {
                preparedStatement.executeUpdate();
                Exception e = Assertions.assertThrows(RuntimeException.class, () -> {
                    ArrayList<GameData> gameList = gameDB.listGames();
                });
                Assertions.assertEquals("java.sql.SQLSyntaxErrorException: Table 'chess.game' doesn't exist",
                        e.getMessage());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);} catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }


    //positive
    @Test
    void testUpdateGame() throws DataAccessException {
        gameDB.clear();
        gameDB.createGame(null,null,"game1",new ChessGame());
        gameDB.updateGame(1,new GameData(1,"newWhite","newBlack","game2",new ChessGame()));
        GameData updatedGame = gameDB.getGame(1);
        Assertions.assertEquals("newWhite",updatedGame.whiteUsername());
        Assertions.assertEquals("newBlack",updatedGame.blackUsername());
    }

    //negative
    @Test
    void testUpdateGameDroppedTable() throws DataAccessException {
        gameDB.createGame(null,null,"game1",new ChessGame());
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("DROP TABLE game")) {
                preparedStatement.executeUpdate();
                Exception e = Assertions.assertThrows(RuntimeException.class, () -> gameDB.updateGame(1,new GameData(1,"newWhite","newBlack","game2",new ChessGame())));
                Assertions.assertEquals("java.sql.SQLSyntaxErrorException: Table 'chess.game' doesn't exist",
                        e.getMessage());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);} catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }


    //positive
    @Test
    void testGetGame() throws DataAccessException {
        gameDB.createGame("white","black","game1",new ChessGame());
        GameData data = gameDB.getGame(1);
        Assertions.assertEquals("white",data.whiteUsername());
        Assertions.assertEquals("black",data.blackUsername());
        Assertions.assertEquals("game1",data.gameName());
    }

    //negative
    @Test
    void testGetGameInvalidId() throws DataAccessException {
        gameDB.createGame(null,null,"game1",new ChessGame());
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("DROP TABLE game")) {
                preparedStatement.executeUpdate();
                Exception e = Assertions.assertThrows(DataAccessException.class, () -> gameDB.getGame(1));
                Assertions.assertEquals("Table 'chess.game' doesn't exist",
                        e.getMessage());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);} catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
