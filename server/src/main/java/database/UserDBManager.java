package database;

import auth.UserManager;
import common.auth.User;
import exceptions.DataBaseException;
import log.Log;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * A manager of user database.
 */
public class UserDBManager implements UserManager {

    private final DBManager dbManager;


    public UserDBManager(DBManager dbManager) throws DataBaseException {
        this.dbManager = dbManager;
        create();
    }

    private void create() throws DataBaseException {
        //language=SQL
        String createTableSQL = "CREATE TABLE IF NOT EXISTS USERS" +
                "(login TEXT PRIMARY KEY, " +
                "password TEXT NOT NULL);";

        dbManager.setCommitMode();
        dbManager.setSavepoint();
        try (PreparedStatement statement = dbManager.getPreparedStatement(createTableSQL)) {
            statement.execute();
            dbManager.commit();
        } catch (SQLException e) {
            dbManager.rollback();
            throw new DataBaseException("cannot create user database");
        } finally {
            dbManager.setNormalMode();
        }
    }


    public void add(User user) throws DataBaseException {
        String sql = "INSERT INTO USERS (login, password) VALUES (?, ?)";

        dbManager.setCommitMode();
        dbManager.setSavepoint();
        try (PreparedStatement preparedStatement = dbManager.getPreparedStatement(sql)) {
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.execute();
            dbManager.commit();
        } catch (SQLException e) {
            dbManager.rollback();
            throw new DataBaseException("something went wrong during adding new user");
        } finally {
            dbManager.setNormalMode();
        }
    }


    public boolean isValid(User user) {
        try {
            String password = user.getPassword();
            ResultSet rs = dbManager.getStatement().executeQuery("SELECT * FROM USERS WHERE login = '" + user.getLogin() + "'");
            while (rs.next())
                if (password.equals(rs.getString(2)))
                    return true;
            return false;
        } catch (SQLException e) {
            Log.logger.error("Can't get user from database.");
            return false;
        }
    }

    public boolean isPresent(String username) {
        try {
            ResultSet rs = dbManager.getStatement().executeQuery("SELECT * FROM USERS WHERE login = '" + username + "'");
            return rs.next();
        } catch (SQLException e) {
            Log.logger.error("Can't get user from database.");
            return false;
        }
    }

    public List<User> getUsers() {
        List<User> users = new LinkedList<>();
        try (PreparedStatement statement = dbManager.getPreparedStatement("SELECT * FROM USERS")) {
            ResultSet resultSet = statement.executeQuery();
            try {
                while (resultSet.next()) {
                    User user = new User(resultSet.getString("login"));
                    user.setPassword(resultSet.getString("password"));
                    users.add(user);
                }
            } catch (SQLException ignored) {

            }

        } catch (SQLException | DataBaseException e) {
            if (users.isEmpty()) throw new DataBaseException("no registered users found");
        }
        return users;
    }

}