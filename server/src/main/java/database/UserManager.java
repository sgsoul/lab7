package database;

import common.utils.User;
import exceptions.DataBaseException;

import java.util.List;

public interface UserManager{
    public void add(User user) throws DataBaseException;

    public boolean isValid(User user);

    public boolean isPresent(String username);

    public List<User> getUsers();
}