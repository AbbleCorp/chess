package service;

import dataaccess.*;
import model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.Set;

//one positive negative for each service method
//assert throws

public class UserServiceTest {
    private UserService userService;
    private UserDAO userDAO;
    private AuthDAO authDAO;

    @BeforeEach
    void setUp() {
        userDAO = new MemoryUserDAO();
        authDAO = new MemoryAuthDAO();
        authDAO.createAuth("user1");
        authDAO.createAuth("user2");
        userDAO.createUser(new UserData("user1", "password1", "email1"));
        userDAO.createUser(new UserData("user2","password2", "email2"));
        userService = new UserService(userDAO,authDAO);
    }

    //register positive test
    @Test
    void testRegisterPos() throws Exception {
        userService.register(new RegisterRequest("user3", "password3", "email3"));
        Assertions.assertNotNull(userDAO.getUser("user3"));
    }

    //register negative test
    @Test
    void testRegisterNeg() throws DataAccessException {
        Exception e = Assertions.assertThrows(DataAccessException.class, () -> {
            userService.register(new RegisterRequest("user1", "password1", "email1"));
        });
        Assertions.assertEquals("Error: already taken", e.getMessage());
    }

    //login positive test
    @Test
    void testLoginPos() throws DataAccessException {
        AuthData auth = userService.login(new LoginRequest("user1","password1"));
        Assertions.assertTrue(auth instanceof AuthData);
    }

    //login negative test, not auth
    @Test
    void testLoginNotAuth() throws DataAccessException {
        Exception e = Assertions.assertThrows(DataAccessException.class, () -> {
            userService.login(new LoginRequest("user1", "incorrect"));
        });
        Assertions.assertEquals("Error: unauthorized", e.getMessage());
    }


    //login negative test, user not found
    @Test
    void testLoginUserNotFound() {
        Exception e = Assertions.assertThrows(DataAccessException.class, () -> {
            userService.login(new LoginRequest("user3", "password3")); });
        Assertions.assertEquals("Error: User not found", e.getMessage());
    }

    //logout positive
    @Test
    void testLogoutPosition() throws Exception {
        Set<String> authTokens = authDAO.listAuth().keySet();
        Iterator<String> it = authTokens.iterator();
        String auth = it.next();
        userService.logout(new LogoutRequest(auth));
        Assertions.assertFalse(authDAO.listAuth().containsKey(auth));
    }

    //
    @Test
    void testLogoutNeg() {
        Exception e = Assertions.assertThrows(DataAccessException.class, () -> {
            userService.logout(new LogoutRequest("incorrect")); });
        Assertions.assertEquals("Error: unauthorized", e.getMessage());
    }

}
