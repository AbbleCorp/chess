package dataaccess;

import model.AuthData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;
import java.sql.Statement;
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
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("INSERT INTO auth (username, authToken) VALUES(?, ?)")) {
                String token = UUID.randomUUID().toString();
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, token);
                preparedStatement.executeUpdate();
                return new AuthData(username, token);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getAuth(String authToken) {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT authToken FROM auth WHERE authToken=?", Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, authToken);
                var resultSet = preparedStatement.executeQuery();
                String token = null;
                if (resultSet.next()) {
                    token = resultSet.getString("authToken");
                }
                return token;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getUsername(String authToken) throws DataAccessException {
        return "";
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {

    }
}
