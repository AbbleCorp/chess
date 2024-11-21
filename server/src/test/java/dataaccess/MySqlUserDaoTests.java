package dataaccess;

import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;
import java.util.Map;

public class MySqlUserDaoTests {
    private UserDAO userDB;
    DatabaseManager db;

    @BeforeEach
    void setUp() throws DataAccessException {
        db=new DatabaseManager();
        db.configureDatabase();
        userDB=new MySqlUserDAO();
        userDB.clear();
    }


    @Test
    void testClear() {
        userDB.createUser(new UserData("user1", "pw1", "email1"));
        userDB.createUser(new UserData("user2", "pw2", "email2"));
        Map<String, UserData> userList=userDB.listUsers();
        Assertions.assertEquals(2, userList.size());
        userDB.clear();
        userList=userDB.listUsers();
        Assertions.assertTrue(userList.isEmpty());
    }


    //positive
    @Test
    void testCreateUser() throws DataAccessException {
        userDB.createUser(new UserData("user1", "password1", "email1"));
        UserData data=userDB.getUser("user1");
        Assertions.assertEquals("user1", data.username());
        Assertions.assertTrue(BCrypt.checkpw("password1", data.password()));
        Assertions.assertEquals("email1", data.email());
    }

    //negative
    @Test
    void testCreateUserExistingUsername() {
        Exception e=Assertions.assertThrows(RuntimeException.class, () -> {
            userDB.createUser(new UserData("user1", "password1", "email1"));
            userDB.createUser(new UserData("user1", "password2", "email2"));
        });
        String s="java.sql.SQLIntegrityConstraintViolationException: Duplicate entry 'user1' for key 'user.PRIMARY'";
        Assertions.assertEquals(s, e.getMessage());
    }

    //positive
    @Test
    void testGetUser() throws DataAccessException {
        userDB.createUser(new UserData("user1", "password1", "email1"));
        userDB.createUser(new UserData("user2", "password2", "email2"));
        UserData result=userDB.getUser("user1");
        Assertions.assertEquals("user1", result.username());
        Assertions.assertTrue(BCrypt.checkpw("password1", result.password()));
        Assertions.assertEquals("email1", result.email());
    }

    //negative
    @Test
    void testGetUserNonExistentUser() throws DataAccessException {
        userDB.createUser(new UserData("user1", "password1", "email1"));
        UserData result=userDB.getUser("user2");
        Assertions.assertNull(result);
    }


    //positive
    @Test
    void testListUsers() {
        userDB.createUser(new UserData("user1", "password1", "email1"));
        userDB.createUser(new UserData("user2", "password2", "email2"));
        userDB.createUser(new UserData("user3", "password3", "email3"));
        Map<String, UserData> userList=userDB.listUsers();
        Assertions.assertEquals(3, userList.size());
        Assertions.assertTrue(userList.containsKey("user1"));
        Assertions.assertTrue(userList.containsKey("user2"));
        Assertions.assertTrue(userList.containsKey("user3"));
    }

    //negative
    @Test
    void testListUsersTableDropped() {
        try (var conn=DatabaseManager.getConnection()) {
            try (var preparedStatement=conn.prepareStatement("DROP TABLE user")) {
                preparedStatement.executeUpdate();
                Exception e=Assertions.assertThrows(RuntimeException.class, () -> {
                    Map<String, UserData> userList=userDB.listUsers();
                });
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
