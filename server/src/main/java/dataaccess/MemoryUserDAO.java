package dataaccess;

import model.UserData;

import java.util.Map;

public class MemoryUserDAO implements UserDAO {
    Map<String,UserData> userList;

    @Override
    public void clear() {
        userList.clear();
    }

    @Override
    public void createUser(UserData u) {
        userList.put(u.username(), u);
    }

    @Override
    public void deleteUser(UserData u) throws DataAccessException {
        if (!userList.containsValue(u)) {
        userList.remove(u.username()); }
        else throw new DataAccessException("User not found");
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        if (userList.containsKey(username)) {
            return userList.get(username); }
        else throw new DataAccessException("User not found");
    }

    public boolean isUserFound(String username) {
        return userList.containsKey(username);
    }


    public boolean checkPassword(String username, String password) throws DataAccessException {
        if (userList.containsKey(username)) {
            return userList.get(username).password().equals(password);
        }
        else throw new DataAccessException("User not found");
    }

}
