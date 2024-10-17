package dataaccess;

import model.AuthData;

import java.util.Map;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO {
    Map<String, String> authList;

    @Override
    public AuthData createAuth(String username) throws DataAccessException {
        String token = UUID.randomUUID().toString();
        return new AuthData(username,token);
    }

    @Override
    public boolean getAuth(AuthData authToken) throws DataAccessException {
        return authList.containsKey(authToken.authToken());
    }

    @Override
    public void deleteAuth(AuthData authToken) throws DataAccessException {
        authList.remove(authToken.authToken());
    }


}
