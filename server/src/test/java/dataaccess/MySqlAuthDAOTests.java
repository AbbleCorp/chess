package dataaccess;

import model.AuthData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Map;

public class MySqlAuthDAOTests {
    DatabaseManager db;
    private AuthDAO authDB;

    @BeforeEach
    void setUp() throws DataAccessException {
        db = new DatabaseManager();
        db.configureDatabase();
        authDB = new MySqlAuthDAO();
        authDB.clear();

    }

    @Test
    void testClear() {
        authDB.createAuth("user1");
        authDB.createAuth("user2");
        authDB.clear();
        Map<String,String> authList = authDB.listAuth();
        Assertions.assertTrue(authList.isEmpty());
    }

    //positive
    @Test
    void testCreateAuth() {
        AuthData data = authDB.createAuth("user1");
        Map<String,String> authList = authDB.listAuth();
        Assertions.assertTrue(authList.containsKey(data.username())&&authList.containsValue(data.authToken()));
    }

    //negative
    @Test
    void testCreateNullUsername() {
        Exception e = Assertions.assertThrows(RuntimeException.class, () -> authDB.createAuth(null));
        Assertions.assertEquals(
                "java.sql.SQLIntegrityConstraintViolationException: Column 'username' cannot be null",
                e.getMessage());
    }


    //positive
    @Test
    void testGetAuth() {
        AuthData data = authDB.createAuth("user1");
        String token = authDB.getAuth(data.authToken());
        Assertions.assertEquals(data.authToken(),token);
    }

    //negative
    @Test
    void testGetAuthInvalidToken() {
            String token = authDB.getAuth("invalid");
            Assertions.assertNull(token);
    }


    //positive
    @Test
    void testGetUsername() throws DataAccessException {
        AuthData data = authDB.createAuth("user1");
        String user = authDB.getUsername(data.authToken());
        Assertions.assertEquals("user1",user);
    }

    //negative
    @Test
    void testGetUsernameInvalidUsername() throws DataAccessException {
        authDB.createAuth("user1");
        String user = authDB.getAuth("invalidUsername");
        Assertions.assertNull(user);
    }


    //positive
    @Test
    void testListAuth() {
        authDB.createAuth("user1");
        authDB.createAuth("user2");
        authDB.createAuth("user3");
        Map<String,String> authList = authDB.listAuth();
        Assertions.assertEquals(3,authList.size());
    }

    //negative
    @Test
    void testListAuthTableDropped() {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("DROP TABLE auth")) {
                preparedStatement.executeUpdate();
                Exception e = Assertions.assertThrows(RuntimeException.class, () -> {
                    Map<String,String> authList = authDB.listAuth();
                });
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);} catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }


    //positive
    @Test
    void testDeleteAuth() throws DataAccessException {
        authDB.createAuth("user1");
        AuthData data = authDB.createAuth("user2");
        authDB.createAuth("user3");
        Map<String,String> authList = authDB.listAuth();
        Assertions.assertEquals(3,authList.size());
        authDB.deleteAuth(authList.get("user2"));
        authList = authDB.listAuth();
        Assertions.assertEquals(2,authList.size());
        Assertions.assertFalse(authList.containsValue(data.authToken()));
    }


    //negative
    @Test
    void testDeleteAuthTableDropped() {
        AuthData data = authDB.createAuth("user2");
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("DROP TABLE auth")) {
                preparedStatement.executeUpdate();
                Exception e = Assertions.assertThrows(DataAccessException.class, () ->
                        authDB.deleteAuth(data.authToken()));
                }
        } catch (SQLException e) {
            throw new RuntimeException(e);} catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
