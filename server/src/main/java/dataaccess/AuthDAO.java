package dataaccess;

import model.AuthData;


public interface AuthDAO {


    AuthData createAuth(String username);

    boolean getAuth(String authToken);

    String getUsername(String authToken) throws DataAccessException;

    void deleteAuth(String authToken) throws DataAccessException;


}
