package dataaccess;

import model.UserData;

import java.util.Map;

public class MySqlGameDAO implements UserDAO {


    @Override
    public void createUser(UserData u) {

    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        return null;
    }

    @Override
    public void clear() {

    }

    @Override
    public Map<String, UserData> listUsers() {
        return Map.of();
    }
}
