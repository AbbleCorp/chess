package dataaccess;

import model.UserData;

public interface UserDAO {


    void clear();

    void createUser(UserData u);

    void deleteUser(UserData u) throws DataAccessException;

    UserData getUser(String username) throws DataAccessException;

    public boolean isUserFound(String username);

    boolean checkPassword(String username, String password) throws DataAccessException;
}
