package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;
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
    void testRegisterPos() throws DataAccessException {
        userService.register(new UserData("user3", "password3", "email3"));
        Assertions.assertNotNull(userDAO.getUser("user3"));
    }

    //register negative test
    @Test
    void testRegisterNeg() throws DataAccessException {
        boolean exceptionThrown = false;
        try {
        userService.register(new UserData("user1", "password1", "email1")); }
        catch (DataAccessException e) {
            exceptionThrown = true;
        }
            Assertions.assertTrue(exceptionThrown);
    }

    //login positive test
    @Test
    void testLoginPos() throws DataAccessException {
        AuthData auth = userService.login("user1","password1");
        Assertions.assertTrue(auth instanceof AuthData);
    }

    //login negative test, not auth
    @Test
    void testLoginNotAuth() throws DataAccessException {
        boolean exceptionThrown = false;
        try {
            userService.login("user1", "incorrect");
        } catch (DataAccessException e) {
            exceptionThrown = true;
            Assertions.assertEquals("Not authorized", e.getMessage());
        }
        Assertions.assertTrue(exceptionThrown);
    }


    //login negative test, user not found
    @Test
    void testLoginUserNotFound() {
        boolean exceptionThrown = false;
        try {
            userService.login("user3", "password3");
        } catch (DataAccessException e) {
            exceptionThrown = true;
            Assertions.assertEquals("User not found", e.getMessage());
        }
        Assertions.assertTrue(exceptionThrown);

    }

    //logout positive
    @Test
    void testLogoutPosition() throws DataAccessException {
        Set<String> authTokens = authDAO.listAuth().keySet();
        Iterator<String> it = authTokens.iterator();
        String auth = it.next();
        try {
        userService.logout(auth); }
        catch (DataAccessException e) {
        }
        Assertions.assertFalse(authDAO.listAuth().containsKey(auth));
    }

    //
    @Test
    void testLogoutNeg() {
        boolean exceptionThrown = false;
        try {
            userService.logout("incorrect"); }
        catch (DataAccessException e) {
            exceptionThrown = true;
        }
        Assertions.assertTrue(exceptionThrown);
    }

}
