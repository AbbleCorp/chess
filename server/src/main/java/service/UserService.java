package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.*;


public class UserService {
    private final UserDAO users;
    private final AuthDAO auths;

    public UserService(UserDAO userdata, AuthDAO authData) {
        this.users = userdata;
        this.auths = authData;
    }



    public AuthData register(RegisterRequest req) throws Exception {
        if (req.username() == null || req.password() == null) {
            throw new Exception("Error: bad request");
        }
        if (users.getUser(req.username()) == null) {
            users.createUser(new UserData(req.username(),req.password(),req.email()));
            return auths.createAuth(req.username());
        } else throw new DataAccessException("Error: already taken");
    }

    public AuthData login(LoginRequest req) throws DataAccessException {
        if (users.getUser(req.username()) != null) {
            if (users.getUser(req.username()).password().equals(req.password())) {
                return auths.createAuth(req.username());
            }
            else throw new DataAccessException("Error: unauthorized");
        }
        else throw new DataAccessException("Error: User not found");
    }

    public void logout(LogoutRequest req) throws Exception {
        if (req.authorization() == null) throw new Exception("Error: bad request");
        if (auths.getAuth(req.authorization()) != null) {
            auths.deleteAuth(req.authorization());
        } else throw new DataAccessException("Error: unauthorized");
    }

}
