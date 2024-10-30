package dataaccess;

import model.AuthData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
}
