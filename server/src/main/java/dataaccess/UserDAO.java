package dataaccess;

import model.UserData;

public interface UserDAO {


    void clear() throws DataAccessException;

    void createUser(UserData u) throws DataAccessException;

    void deleteUser(UserData u) throws DataAccessException;

    UserData getUser() throws DataAccessException;
}
