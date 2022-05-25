package commands;


import auth.UserManager;
import common.auth.User;
import common.commands.CommandImpl;
import common.commands.CommandType;
import common.connection.AnswerMsg;
import common.connection.Response;
import common.exceptions.*;
import exceptions.DataBaseException;

public class RegisterCommand extends CommandImpl {
        private final UserManager userManager;

    public RegisterCommand(UserManager manager) {
        super("register", CommandType.AUTH);
        userManager = manager;
    }

    @Override
    public Response run() throws DataBaseException {
        User user = getArgument().getUser();
        if (user != null && user.getLogin() != null && user.getPassword() != null) {
            if (userManager.isPresent(user.getLogin())) {
                throw new DatabaseException("Пользователь " + user.getLogin() + " уже существует.");
            }
            userManager.add(user);
            return new AnswerMsg().info("Пользователь " + user.getLogin() + " успешно зарегистрирован.").setStatus(Response.Status.AUTH_SUCCESS);
        }
        throw new DataBaseException("Пользователь заболел...?");

    }
}

