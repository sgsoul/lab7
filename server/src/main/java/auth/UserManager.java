package auth;

import common.auth.User;
import common.exceptions.DatabaseException;

import java.util.List;

public interface UserManager {
    void add(User user) throws DatabaseException;

    boolean isValid(User user);

    boolean isPresent(String username);

    List<User> getUsers();
}