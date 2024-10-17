package dataaccess;

import model.UserData;

import java.util.Map;

public class MemoryUserDAO implements UserDAO {
    Map<String,UserData> userList;

    @Override
    public void clear() throws DataAccessException {
        userList.clear();
    }

    @Override
    public void createUser(UserData u) throws DataAccessException {
        userList.put(u.username(), u);
    }

    @Override
    public void deleteUser(UserData u) throws DataAccessException {
        userList.remove(u.username());
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        return userList.get(username);
    }

    public boolean isUserFound(UserData u) throws DataAccessException{
        return userList.containsValue(u);
    }

    public boolean checkPassword(UserData user, String password) throws DataAccessException {
        return user.username().equals(password);
    }

}
