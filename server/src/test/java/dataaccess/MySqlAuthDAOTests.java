package dataaccess;

import model.AuthData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class MySqlAuthDAOTests {
    private AuthDAO authDB;

    @BeforeEach
    void setUp() {
        authDB = new MySqlAuthDAO();
    }

    @Test
    void testClear() {
        authDB.clear();
    }

    @Test
    void testCreateAuth() {
        AuthData data = authDB.createAuth("user1");
        System.out.println(data.username());
        System.out.println(data.authToken());
    }

    @Test
    void testGetAuth() {
        AuthData data = authDB.createAuth("user1");
        String token = authDB.getAuth(data.authToken());
        System.out.print(token);
    }

    @Test
    void testGetUsername() throws DataAccessException {
        AuthData data = authDB.createAuth("user1");
        String user = authDB.getUsername(data.authToken());
        System.out.print(user);
    }

    @Test
    void testListAuth() {
        authDB.createAuth("user1");
        authDB.createAuth("user2");
        authDB.createAuth("user3");
        Map<String,String> authList = authDB.listAuth();
        System.out.print("test");
    }

    @Test
    void testDeleteAuth() throws DataAccessException {
        authDB.createAuth("user1");
        authDB.createAuth("user2");
        authDB.createAuth("user3");
        Map<String,String> authList = authDB.listAuth();
        System.out.println(authList.size());
        authDB.deleteAuth(authList.get("user2"));
        authList = authDB.listAuth();
        System.out.println(authList.size());
    }
}
