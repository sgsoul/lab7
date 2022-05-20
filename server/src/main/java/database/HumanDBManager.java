package database;

import auth.UserManager;
import collection.HumanCollectionManager;
import common.auth.User;
import common.data.*;
import exceptions.DataBaseException;
import common.exceptions.InvalidDataException;
import common.exceptions.InvalidEnumException;
import common.utils.DateConverter;
import log.Log;

import java.sql.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


public class HumanDBManager extends HumanCollectionManager {
    //language=SQL
    private final static String INSERT_HUMANS_QUERY = "INSERT INTO HUMANS (name, coordinates_x, coordinates_y, creation_date, real_hero, has_toothpick, impact_speed, soundtrack_name, minutes_of_waiting, weapon_type, car_name, car_coolcheck, user_login,id)" +
            "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,DEFAULT) RETURNING id; ";
    private final DBManager dbManager;
    private final UserManager userManager;
    private Connection connection;
    private static final String url = "jdbc:postgresql://localhost:5432/postgres";
    private static final String user = "postgres";
    private static final String password = "qwerty";



    public HumanDBManager(DBManager c, UserManager userManager) throws DataBaseException {
        super();
        dbManager = c;

        this.userManager = userManager;
        create();
    }

    private void create() throws DataBaseException {
        //language=SQL
        String create =
                "CREATE TABLE IF NOT EXISTS HUMANS (" +
                        "id SERIAL PRIMARY KEY CHECK ( id > 0 )," +
                        "name TEXT NOT NULL CHECK (name <> '')," +
                        "coordinates_x FLOAT NOT NULL ," +
                        "coordinates_y FLOAT NOT NULL ," +
                        "creation_date TEXT NOT NULL," +
                        "real_hero BOOLEAN," +
                        "has_toothpick BOOLEAN," +
                        "impact_speed INTEGER ," +
                        "soundtrack_name TEXT NOT NULL CHECK (soundtrack_name <> '')," +
                        "minutes_of_waiting FLOAT NOT NULL," +
                        "weapon_type TEXT NOT NULL," +
                        "car_name TEXT NOT NULL," +
                        "car_coolcheck BOOLEAN NOT NULL," +
                        "user_login TEXT NOT NULL REFERENCES USERS(login)" +
                        ");";

        try (PreparedStatement createStatement = dbManager.getPreparedStatement(create)) {
            createStatement.execute();
        } catch (SQLException e) {
            throw new DataBaseException("cannot create worker database");
        }
    }

    @Override
    public int generateNextId() {
        try (PreparedStatement statement = dbManager.getPreparedStatement("SELECT nextval('id')")) {
            ResultSet r = statement.executeQuery();
            r.next();
            return r.getInt(1);
        } catch (SQLException e) {
            return 1;
        }
    }

    private void setHuman(PreparedStatement statement, HumanBeing human) throws SQLException {
        statement.setString(1, human.getName());
        statement.setDouble(2, human.getCoordinates().getX());
        statement.setDouble(3, human.getCoordinates().getY());
        statement.setString(4, DateConverter.dateToString(human.getCreationDate()));
        statement.setBoolean(5, human.checkRealHero());
        statement.setBoolean(6, human.checkHasToothpick());
        statement.setLong(7, human.getImpactSpeed());
        statement.setString(8, human.getSoundtrackName());
        statement.setFloat(9, human.getMinutesOfWaiting());
        statement.setString(10, human.getWeaponType().toString());
        statement.setString(11, human.getCar().getName());
        statement.setBoolean(12, human.getCar().checkCool());
        statement.setString(13, human.getUserLogin());

    }

    private HumanBeing getHuman(ResultSet resultSet) throws SQLException, InvalidDataException {
        Coordinates coordinates = new Coordinates(resultSet.getFloat("coordinates_x"), resultSet.getLong("coordinates_y"));
        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");

        Date creationDate = DateConverter.parseDate(resultSet.getString("creation_date"));
        boolean realHero = resultSet.getBoolean("real_hero");

        boolean hasToothpick = resultSet.getBoolean("has_toothpick");

        Integer impactSpeed = resultSet.getInt("impact_speed");
        String soundtrackName = resultSet.getString("soundtrack_name");
        float minutesOfWaiting = resultSet.getFloat("minutes_of_waiting");
        WeaponType weaponType;
        try {
            weaponType = WeaponType.valueOf(resultSet.getString("weapon_type"));
        } catch (IllegalArgumentException e) {
            throw new InvalidEnumException();
        }
        String carName = resultSet.getString("car_name");
        boolean carCool = resultSet.getBoolean("car_coolcheck");
        Car car = new Car(carName, carCool);
        HumanBeing human = new HumanBeing(name, coordinates, realHero, hasToothpick, impactSpeed, soundtrackName, minutesOfWaiting, weaponType, car);
        human.setCreationDate(creationDate);
        human.setId(id);
        human.setUserLogin(resultSet.getString("user_login"));
        if (!userManager.isPresent(human.getUserLogin())) throw new DataBaseException("no user found");
        return human;
    }

    @Override
    public void add(HumanBeing humanBeing) {

        dbManager.setCommitMode();
        dbManager.setSavepoint();
        try (PreparedStatement statement = dbManager.getPreparedStatement(INSERT_HUMANS_QUERY, true)) {
            setHuman(statement, humanBeing);
            if (statement.executeUpdate() == 0)
                throw new DataBaseException();
            ResultSet resultSet = statement.getGeneratedKeys();

            if (!resultSet.next()) throw new DataBaseException();
            humanBeing.setId(resultSet.getInt("id"));

            dbManager.commit();
        } catch (SQLException | DataBaseException e) {
            e.printStackTrace();
            dbManager.rollback();
            throw new DataBaseException("cannot add to database");
        } finally {
            dbManager.setNormalMode();
        }
        super.addWithoutIdGeneration(humanBeing);
    }


    @Override
    public void removeByID(Integer id) {
        //dbManager.setCommitMode();
        //dbManager.setSavepoint();
        //language=SQL
        Connection connection = dbManager.connectToDataBase();
        String query = "DELETE FROM humans WHERE id = ?";
        /*try (PreparedStatement statement = dbManager.getPreparedStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataBaseException("ошибка при удалении из датабазы... ну и кринж");
        }*/
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,id);
            preparedStatement.executeUpdate();
            //return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //return false;
        super.removeByID(id);
        //dbManager.commit();
        //dbManager.setNormalMode();
    }

    @Override
    public void removeFirst() {
        removeByID(getCollection().get(0).getId());
    }

    @Override
    public void updateByID(Integer id, HumanBeing human) {
        dbManager.setCommitMode();
        dbManager.setSavepoint();
        //language = SQL;
        String sql = "UPDATE HUMANS SET " +
                "name=?," +
                "coordinates_x=?," +
                "coordinates_y=?," +
                "creation_date=?," +
                "real_hero=?," +
                "has_toothpick=?," +
                "impact_speed=?," +
                "minutes_of_waiting=?," +
                "weapon_type=?," +
                "car_name=?," +
                "car_coolcheck=?," +
                "user_login=?" +
                "WHERE id=?";
        try (PreparedStatement statement = dbManager.getPreparedStatement(sql)) {
            setHuman(statement, human);
            statement.setInt(13, id);
            statement.execute();
            dbManager.commit();
        } catch (SQLException e) {
            dbManager.rollback();
            throw new DataBaseException("cannot update worker #" + human.getId() + " in database");
        } finally {
            dbManager.setNormalMode();
        }
        super.updateByID(id, human);
    }

    @Override
    public void addIfMax(HumanBeing human) {
        //language=SQL
        String getMaxQuery = "SELECT MAX(impact_speed) FROM HUMANS";

        if (getCollection().isEmpty()) {
            add(human);
            return;
        }
        dbManager.setCommitMode();
        dbManager.setSavepoint();
        try (Statement getStatement = dbManager.getStatement();
             PreparedStatement insertStatement = dbManager.getPreparedStatement(INSERT_HUMANS_QUERY)) {

            ResultSet resultSet = getStatement.executeQuery(getMaxQuery);
            if (!resultSet.next()) throw new DataBaseException("unable to add");

            long impactspeed = resultSet.getLong(1);
            if (human.getImpactSpeed() < impactspeed)
                throw new DataBaseException("unable to add, max impact speed is " + impactspeed + " current impact speed is " + human.getImpactSpeed());

            setHuman(insertStatement, human);

            human.setId(resultSet.getInt("id"));
            dbManager.commit();
        } catch (SQLException e) {
            dbManager.rollback();
            throw new DataBaseException("cannot add due to internal error");
        } finally {
            dbManager.setNormalMode();
        }
        super.addWithoutIdGeneration(human);
    }

    @Override
    public void addIfMin(HumanBeing human) {
        //language=SQL
        String getMinQuery = "SELECT MIN(impact_speed) FROM HUMANS";

        if (getCollection().isEmpty()) {
            add(human);
            return;
        }
        dbManager.setCommitMode();
        dbManager.setSavepoint();
        try (Statement getStatement = dbManager.getStatement();
             PreparedStatement insertStatement = dbManager.getPreparedStatement(INSERT_HUMANS_QUERY)) {

            ResultSet resultSet = getStatement.executeQuery(getMinQuery);
            if (!resultSet.next()) throw new DataBaseException("unable to add");

            long impactSpeed = resultSet.getLong(1);
            if (human.getImpactSpeed() > impactSpeed)
                throw new DataBaseException("unable to add, min impact speed is " + impactSpeed + " current impact speed is " + human.getImpactSpeed());

            setHuman(insertStatement, human);

            human.setId(resultSet.getInt("id"));
            dbManager.commit();
        } catch (SQLException e) {
            dbManager.rollback();
            throw new DataBaseException("cannot add due to internal error");
        } finally {
            dbManager.setNormalMode();
        }
        super.addWithoutIdGeneration(human);
    }

    public void clear(User user) {
        dbManager.setCommitMode();
        dbManager.setSavepoint();
        Set<Integer> ids = new HashSet<>();
        try (PreparedStatement statement = dbManager.getPreparedStatement("DELETE FROM HUMANS WHERE user_login=? RETURNING id")) {
            statement.setString(1, user.getLogin());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Integer id = resultSet.getInt(1);
                ids.add(id);
            }
        } catch (SQLException | RuntimeException e) {
            dbManager.rollback();
            deserializeCollection("");
            throw new DataBaseException("cannot clear database");
        } finally {
            dbManager.setNormalMode();
        }
        removeAll(ids);
    }

    @Override
    public void deserializeCollection(String ignored) {
        if (!getCollection().isEmpty()) super.clear();
        //language=SQL
        String query = "SELECT * FROM HUMANS";
        try (PreparedStatement selectAllStatement = dbManager.getPreparedStatement(query)) {
            ResultSet resultSet = selectAllStatement.executeQuery();
            int damagedElements = 0;
            while (resultSet.next()) {
                try {
                    HumanBeing human = getHuman(resultSet);
                    if (!human.validate()) throw new InvalidDataException("element is damaged");
                    super.addWithoutIdGeneration(human);
                } catch (InvalidDataException | SQLException e) {
                    damagedElements += 1;
                }
            }
            if (super.getCollection().isEmpty()) throw new DataBaseException("nothing to load");
            if (damagedElements == 0) Log.logger.info("collection successfully loaded");
            else Log.logger.warn(damagedElements + " elements are damaged");
        } catch (SQLException e) {
            throw new DataBaseException("cannot load");
        }
    }
}