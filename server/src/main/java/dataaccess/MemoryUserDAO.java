package dataaccess;

import model.UserData;

public class MemoryUserDAO implements UserDAO {

    @Override
    public void clear() throws DataAccessException {
        //TODO: implement, may need to change type
    }

    @Override
    public void createUser(UserData u) throws DataAccessException {
        //TODO: implement, may need to change type
    }

    @Override
    public void deleteUser(UserData u) throws DataAccessException {
        //TODO: implement, may need to change type
    }

    @Override
    public UserData getUser() throws DataAccessException {
        //TODO: implement, may need to change type, figure out parameters
        throw new DataAccessException("not implemented");
    }

}
