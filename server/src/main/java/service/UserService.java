package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.MemoryUserDAO;
import dataaccess.UserDAO;
import model.*;


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

    public AuthData login(LoginRequest req) throws DataAccessException {
        if (users.getUser(req.username()) != null) {
            //do password check here
            if (users.getUser(req.username()).password().equals(req.password())) {
                return auths.createAuth(req.username());
            }
            else throw new DataAccessException("Not authorized");
        }
        else throw new DataAccessException("User not found");
    }

    public void logout(LogoutRequest req) throws DataAccessException {
        if (auths.getAuth(req.authorization()) != null) {
            auths.deleteAuth(req.authorization());
        } else throw new DataAccessException("Not authorized");
    }

}
