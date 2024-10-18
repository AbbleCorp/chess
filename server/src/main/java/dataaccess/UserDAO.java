package dataaccess;

import model.UserData;

import java.util.Map;

public interface UserDAO {
    
    void createUser(UserData u);

    UserData getUser(String username) throws DataAccessException;

    void clear();

    Map<String,UserData> listUsers();
}
