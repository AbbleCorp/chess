package dataaccess;

import model.AuthData;

public interface AuthDAO {


    AuthData createAuth(String username) throws DataAccessException;

    boolean getAuth(AuthData authToken) throws DataAccessException;

    void deleteAuth(AuthData authToken) throws DataAccessException;


}
