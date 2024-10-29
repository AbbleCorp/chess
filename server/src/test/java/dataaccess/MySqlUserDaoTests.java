package dataaccess;

import chess.ChessGame;
import dataaccess.*;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MySqlUserDaoTests {
    private UserDAO userDB;

    @BeforeEach
    void setUp() {
        userDB = new MySqlUserDAO();
    }

    @Test
    void createUser() {
        userDB.createUser(new UserData("user1", "password1","email1"));
    }

    @Test
    void clear() {
        userDB.clear();
    }

    @Test
    void getUser() throws DataAccessException {
        clear();
        createUser();
        UserData result = userDB.getUser("user1");
        System.out.print(result.username());
        System.out.print(result.password());
        System.out.print(result.email());
    }

}
