package dataaccess;

import model.AuthData;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MySqlAuthDAO implements AuthDAO {


    @Override
    public void clear() {
        try (var conn=DatabaseManager.getConnection()) {
            try (var preparedStatement=conn.prepareStatement("DELETE FROM auth")) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<String, String> listAuth() {
        try (var conn=DatabaseManager.getConnection()) {
            try (var preparedStatement=conn.prepareStatement("SELECT * FROM auth", Statement.RETURN_GENERATED_KEYS)) {
                var resultSet=preparedStatement.executeQuery();
                Map<String, String> authList=new HashMap<>();
                while (resultSet.next()) {
                    authList.put(resultSet.getString("username"), resultSet.getString("authToken"));
                }
                return authList;
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public AuthData createAuth(String username) {
        try (var conn=DatabaseManager.getConnection()) {
            try (var preparedStatement=conn.prepareStatement(
                    "INSERT INTO auth (username, authToken) VALUES(?, ?)" +
                            "ON DUPLICATE KEY UPDATE authToken=?")) {
                String token=UUID.randomUUID().toString();
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, token);
                preparedStatement.setString(3, token);
                preparedStatement.executeUpdate();
                return new AuthData(username, token);
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getAuth(String authToken) {
        try (var conn=DatabaseManager.getConnection()) {
            try (var preparedStatement=conn.prepareStatement("SELECT authToken FROM auth WHERE authToken=?", Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, authToken);
                var resultSet=preparedStatement.executeQuery();
                String token=null;
                if (resultSet.next()) {
                    token=resultSet.getString("authToken");
                }
                return token;
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getUsername(String authToken) throws DataAccessException {
        try (var conn=DatabaseManager.getConnection()) {
            try (var preparedStatement=conn.prepareStatement("SELECT username FROM auth WHERE authToken=?", Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, authToken);
                var resultSet=preparedStatement.executeQuery();
                String username=null;
                if (resultSet.next()) {
                    username=resultSet.getString("username");
                }
                return username;
            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        try (var conn=DatabaseManager.getConnection()) {
            try (var preparedStatement=conn.prepareStatement("DELETE FROM auth WHERE authToken=?", Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, authToken);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
