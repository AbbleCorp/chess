package dataaccess;

import model.AuthData;

import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;

public class MySqlAuthDAO implements AuthDAO {


    @Override
    public void clear() {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("DELETE FROM auth")) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<String, String> listAuth() {
        return Map.of();
    }

    @Override
    public AuthData createAuth(String username) {
        String token = UUID.randomUUID().toString();
        return null;
    }

    @Override
    public String getAuth(String authToken) {
        return "";
    }

    @Override
    public String getUsername(String authToken) throws DataAccessException {
        return "";
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {

    }
}
