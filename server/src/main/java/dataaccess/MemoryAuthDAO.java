package dataaccess;

import model.AuthData;

import java.util.Map;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO {
    Map<String, String> authList; //auth is key, username is value

    @Override
    public AuthData createAuth(String username) {
        String token = UUID.randomUUID().toString();
        authList.put(token,username);
        return new AuthData(token, username);
    }

    @Override
    public boolean getAuth(String authToken) {
        return authList.containsKey(authToken);
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        if (authList.containsKey(authToken)) {
            authList.remove(authToken); }
        else throw new DataAccessException("Not authorized");
    }

    @Override
    public String getUsername(String authToken) throws DataAccessException {
        if (authList.containsKey(authToken)) {
            return authList.get(authToken);
        }
        else throw new DataAccessException("Not authorized");
    }

}
