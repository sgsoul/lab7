package auth;

import common.auth.User;
import exceptions.DataBaseException;

import java.util.List;

public interface UserManager {
    void add(User user) throws DataBaseException;

    boolean isValid(User user);

    boolean isPresent(String username);

    List<User> getUsers();
}