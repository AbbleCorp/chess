package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.MemoryUserDAO;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;


public class UserService {
    private final UserDAO users;
    private final AuthDAO auths;

    UserService(UserDAO userdata, AuthDAO authData) {
        this.users = userdata;
        this.auths = authData;
    }


    public void clearUserData() throws DataAccessException {
        users.clear();
    }

    public AuthData register(UserData user) throws DataAccessException {
        if (!users.isUserFound(user.username())) {
            users.createUser(user);
            AuthData authToken = auths.createAuth(user.username());
            return authToken;
        } else throw new DataAccessException("User already exists");
    }

    public AuthData login(String username, String password) throws DataAccessException {
        if (users.isUserFound(username)) {
            users.checkPassword(username,password);
            return auths.createAuth(username);
        }
        else throw new DataAccessException("User not found");
    }

    public void logout(String auth) throws DataAccessException {
        if (auths.getAuth(auth)) {
            auths.deleteAuth(auth);
        } else throw new DataAccessException("Not authorized");
    }

}
