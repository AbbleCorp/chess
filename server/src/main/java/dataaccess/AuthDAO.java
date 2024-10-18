package dataaccess;

import model.AuthData;

import java.util.Map;


public interface AuthDAO {

    void clear();

    Map<String,String> listAuth();

    AuthData createAuth(String username);

    String getAuth(String authToken);

    String getUsername(String authToken) throws DataAccessException;

    void deleteAuth(String authToken) throws DataAccessException;


}
