package dataaccess;

import model.UserData;

import java.util.HashMap;
import java.util.Map;

public class MemoryUserDAO implements UserDAO {
    Map<String, UserData> userList;

    public MemoryUserDAO() {
        userList = new HashMap<>();
    }

    @Override
    public void clear() {
        userList.clear();
    }

    @Override
    public void createUser(UserData u) {
        userList.put(u.username(), u);
    }


    @Override
    public UserData getUser(String username) {
        if (userList.containsKey(username)) {
            return userList.get(username);
        } else {
            return null;
        }
    }


    @Override
    public Map<String, UserData> listUsers() {
        return userList;
    }


}
