package dataaccess;

import model.AuthData;

public interface AuthDAO {


    AuthData createAuth() throws DataAccessException;

    void getAuth(AuthData authToken) throws DataAccessException;

    void deleteAuth(AuthData authToken) throws DataAccessException;
}
