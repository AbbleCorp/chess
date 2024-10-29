package dataaccess;

import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLDataException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

public class MySqlUserDAO implements UserDAO {



    @Override
    public void createUser(UserData u) {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("INSERT INTO user (username, password, email) VALUES(?, ?, ?)")) {
                preparedStatement.setString(1, u.username());
                //hash password
                String hashedPassword = BCrypt.hashpw(u.password(), BCrypt.gensalt());
                preparedStatement.setString(2, hashedPassword);
                preparedStatement.setString(3, u.email());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public UserData getUser(String username) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT * FROM user WHERE username=?", Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, username);
                var resultSet = preparedStatement.executeQuery();
                UserData data = null;
                if (resultSet.next()) {
                    data = new UserData(resultSet.getString("username"), resultSet.getString("password"), resultSet.getString("email"));
                }
                return data;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        // SELECT * FROM user WHERE username='user1';
    }

    @Override
    public void clear() {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("DELETE FROM user")) {
                preparedStatement.executeUpdate();
            }
    } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }}

        @Override
    public Map<String, UserData> listUsers() {
        return Map.of();
    }
}
