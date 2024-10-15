package dataaccess;

import model.AuthData;

public class MemoryAuthDAO implements AuthDAO {

    @Override
    public AuthData createAuth() throws DataAccessException {
        //TODO: implement, create token and return
        throw new DataAccessException("not implemented");
    }

    @Override
    public void getAuth(AuthData authToken) throws DataAccessException {
        //TODO: implement, will need to change method type
        throw new DataAccessException("not implemented");
    }

    @Override
    public void deleteAuth(AuthData authToken) throws DataAccessException {
        //TODO: implement, may need to change return type?
        throw new DataAccessException("not implemented");
    }
}
