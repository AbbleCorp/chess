package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;

public class DataService {
    UserDAO userData;
    GameDAO gameData;
    AuthDAO authData;

    public DataService(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO) {
        this.userData = userDAO;
        this.gameData = gameDAO;
        this.authData = authDAO;
    }

    public void clearDatabase() {
        userData.clear();
        gameData.clear();
        authData.clear();
    }

}
