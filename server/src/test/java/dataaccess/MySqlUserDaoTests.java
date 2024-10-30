package dataaccess;

import chess.ChessGame;
import dataaccess.*;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

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


    @Test
    void listUsers() {
        clear();
        createUser();
        userDB.createUser(new UserData("user2", "password2", "email2"));
        userDB.createUser(new UserData("user3", "password3", "email3"));
        Map<String, UserData> userList = userDB.listUsers();
    }
}
