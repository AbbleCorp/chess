package dataaccess;

import model.AuthData;

import java.util.Map;

public class MySqlAuthDAO implements AuthDAO {


    @Override
    public void clear() {

    }

    @Override
    public Map<String, String> listAuth() {
        return Map.of();
    }

    @Override
    public AuthData createAuth(String username) {
        return null;
    }

    @Override
    public String getAuth(String authToken) {
        return "";
    }

    @Override
    public String getUsername(String authToken) throws DataAccessException {
        return "";
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {

    }
}
