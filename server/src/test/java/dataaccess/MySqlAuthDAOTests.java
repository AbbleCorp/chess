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
}
