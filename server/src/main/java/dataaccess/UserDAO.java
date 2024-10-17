package dataaccess;

import model.UserData;

public interface UserDAO {


    void clear() throws DataAccessException;

    void createUser(UserData u) throws DataAccessException;

    void deleteUser(UserData u) throws DataAccessException;

    UserData getUser(String username) throws DataAccessException;

    boolean isUserFound(UserData u) throws DataAccessException;

    boolean checkPassword(UserData user, String password) throws DataAccessException;
}
