package database;

import common.exceptions.DatabaseException;
import exceptions.DataBaseException;
import log.Log;

import java.sql.*;

public class DBManager {
    private static final String JDBC_DRIVER = "org.postgresql.Driver";
    //ConnectionManager connectionManager;
    Connection connection;

    private final String user;
    private final String password;
    private final String url;
    //private Connection connection;

/*    public DBManager(ConnectionManager connection) throws DataBaseException {
        this.connectionManager = connectionManager;
        connectToDataBase();
    }*/

    public DBManager(String url, String u, String p) throws DataBaseException {
        user = u;
        password = p;
        this.url = url;
        connectToDataBase();
    }

    private void connectToDataBase() throws DataBaseException {
        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException exception) {
            throw new DatabaseException("error during connection to database");
        } catch (ClassNotFoundException exception) {
            throw new DatabaseException("data driver not found");
        }
    }

    /**
     * @param sqlStatement SQL statement to be prepared.
     * @param generateKeys Is keys needed to be generated.
     * @return Prepared statement.
     * @throws DataBaseException When there's exception inside.
     */

    public PreparedStatement getPreparedStatement(String sqlStatement, boolean generateKeys) throws DataBaseException {
        PreparedStatement preparedStatement;
        try {
            if (connection == null) throw new SQLException();
            int autoGeneratedKeys = generateKeys ? Statement.RETURN_GENERATED_KEYS : Statement.NO_GENERATED_KEYS;
            preparedStatement = connection.prepareStatement(sqlStatement, autoGeneratedKeys);
            return preparedStatement;
        } catch (SQLException exception) {
            throw new DataBaseException("error during preparation sql statement");
        }
    }

    public Statement getStatement() throws DataBaseException {
        try {
            return connection.createStatement();
        } catch (SQLException e) {
            throw new DataBaseException("cannot get SQL statement");
        }
    }


    public PreparedStatement getPreparedStatement(String sql) throws DataBaseException {
        return getPreparedStatement(sql, false);
    }

    public void closePreparedStatement(PreparedStatement sqlStatement) {
        if (sqlStatement == null) return;
        try {
            sqlStatement.close();
            //App.logger.info("Закрыт SQL запрос '" + sqlStatement + "'.");
        } catch (SQLException exception) {
            //App.logger.error("Произошла ошибка при закрытии SQL запроса '" + sqlStatement + "'.");
        }
    }

    /**
     * Close connection to database.
     */
    public void closeConnection() {
        if (connection == null) return;
        try {
            connection.close();
            Log.logger.info("connection to database is interrupted");
        } catch (SQLException exception) {
            Log.logger.error("error during interruption connection to database");
        }
    }

    /**
     * Set commit mode of database.
     */
    public void setCommitMode() {
        try {
            if (connection == null) throw new SQLException();
            connection.setAutoCommit(false);
        } catch (SQLException exception) {
            Log.logger.error("Произошла ошибка при установлении режима транзакции базы данных!");
        }
    }

    /**
     * Set normal mode of database.
     */
    public void setNormalMode() {
        try {
            if (connection == null) throw new SQLException();
            connection.setAutoCommit(true);
        } catch (SQLException exception) {
            Log.logger.error("Произошла ошибка при установлении нормального режима базы данных!");
        }
    }

    /**
     * Commit database status.
     */
    public void commit() {
        try {
            if (connection == null) throw new SQLException();
            connection.commit();
        } catch (SQLException exception) {
            Log.logger.error("Произошла ошибка при подтверждении нового состояния базы данных!");
        }
    }

    /**
     * Roll back database status.
     */
    public void rollback() {
        try {
            if (connection == null) throw new SQLException();
            connection.rollback();
        } catch (SQLException exception) {
            Log.logger.error("Произошла ошибка при возврате исходного состояния базы данных!");
        }
    }

    /**
     * Set save point of database.
     */
    public void setSavepoint() {
        try {
            if (connection == null) throw new SQLException();
            connection.setSavepoint();
        } catch (SQLException exception) {
            Log.logger.error("Произошла ошибка при сохранении состояния базы данных!");
        }
    }


}