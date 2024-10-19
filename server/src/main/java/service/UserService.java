package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.MemoryUserDAO;
import dataaccess.UserDAO;
import model.AuthData;
import model.RegisterRequest;
import model.UserData;


public class UserService {
    private final UserDAO users;
    private final AuthDAO auths;

    public UserService(UserDAO userdata, AuthDAO authData) {
        this.users = userdata;
        this.auths = authData;
    }



    public AuthData register(RegisterRequest req) throws DataAccessException {
        if (users.getUser(req.username()) == null) {
            users.createUser(new UserData(req.username(),req.password(),req.email()));
            AuthData authToken = auths.createAuth(req.username());
            return authToken;
        } else throw new DataAccessException("User already exists");
    }

    public AuthData login(String username, String password) throws DataAccessException {
        if (users.getUser(username) != null) {
            //do password check here
            if (users.getUser(username).password().equals(password)) {
                return auths.createAuth(username);
            }
            else throw new DataAccessException("Not authorized");
        }
        else throw new DataAccessException("User not found");
    }

    public void logout(String auth) throws DataAccessException {
        if (auths.getAuth(auth) != null) {
            auths.deleteAuth(auth);
        } else throw new DataAccessException("Not authorized");
    }

}
